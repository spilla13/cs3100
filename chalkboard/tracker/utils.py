from tokenapi.decorators import token_required
from tokenapi.http import JsonResponse, JsonError
from tracker.models import Course, Homework, Category, Grade
from django.forms.models import model_to_dict
import django
import json

# check our input
def check(request):
    if request.method != 'POST':
        return JsonError("Must use POST with json.")
    return None


@token_required
def get(request, model):
    response = [ ]

    res = check(request)
    if res is not None:
        return res

    data = json.loads(request.body.decode('utf-8'))

    response = getObjects(data, model)
    if isError(response):
        return response

    return JsonResponse({"data": list(response.values())})

@token_required
def rm(request, model):
    # Force post
    res = check(request)
    if res is not None:
        return res

    data = json.loads(request.body.decode('utf-8'))

    # Get an object using our constraints specified (gets the object from the dict)
    response = getObjects(data, model)
    if isError(response):
        return response

    # Can only delete one thing at a time
    res = singularOp(response, model)
    if res is not None:
        return res

    # We return this so they know what their query matched
    data = list(response.values())

    # We're good, delete
    response.delete()
    return JsonResponse({"data": data})

@token_required
def edit(request, model):
    # force post
    res = check(request)
    if res is not None:
        return res

    # Decode our data from JSON
    data = json.loads(request.body.decode('utf-8'))

    if not 'new' in data:
        return JsonError("'new' must be included in the JSON of an edit.")

    # Remove the proposed changes
    edit = data['new']
    del data['new']

    # Find an object using our constraints specified (gets the object) 
    response = getObjects(data, model)
    if isError(response):
        return response

    # Can only edit once thing at a time
    res = singularOp(response, model, "Edit")
    if res is not None:
        return res

    # We just want the first, since there's only one
    response = response[0]

    # We're good, loop through their proposed changes and check them

    # Do we even have these keys
    res = modelHasKeys(edit, model)
    if res is not None:
        return res
    
    # Convert any change references to actual objects
    dictToObjects(edit)
    if isError(edit):
        return edit 

    # Do the editing
    for key in edit.keys():
        if type(edit[key]) is str:
            if len(edit[key]) <= 4:
                # in general I've enforced that all string fields be 4 characters long.
                return JsonError(''.join([ key, " must be at least 4 characters long." ]))
        if key == 'id':
            return JsonError("Cannot change id field of objects.")
        response.__dict__[key] = edit[key]

    # If we're this far, we're done
    response.save()

    # This is wrong if we allow batch edits
    return JsonResponse({"data": list(model.objects.filter(id=response.id).values())})

def singularOp(response, model, op="Delete"): 
    """
    Checks that an operation is only being performed on a single object.
    Gives a pretty error response if that's not the case.
    """ 
    if len(response) > 1:
        return JsonError(''.join([op, " query matched more than one ", model.__name__, " (matches ", 
                                 str(len(response)), ")."]))
    elif len(response) <= 0:
        return JsonError(''.join([op, " query did not match any ", model.__name__, "s."]))
    return None

def getObjects(data, model):
    """
    Inbetween common function for getting, deleting, editing, etc.
    From our data, it finds an object matching its constaints and returns it.
    Sometimes returns a HttpResponse on an error.
    """
    res = dictToObjects(data)
    if res is not None:
        return res

    res = modelHasKeys(data, model)
    if res is not None:
        return res 

    response = model.objects.filter(**data)

    return response

def dictToObjects(data):
    """
    Modifies data to have its hash entries be actual objects. Returns None if successful, or
    JsonError if not.
    """
    # A map for input fields and their appropriate model... don't eval crap.
    modelmap = { "category": Category, "homework": Homework, "course": Course }

    for key in data.keys():
        if key in modelmap.keys():
            res = modelHasKeys(data[key], modelmap[key]) 
            if res is not None:
                return res

            obs = modelmap[key].objects.filter(**data[key])
            if not obs.exists():
                return JsonError(''.join([modelmap[key].__name__, " does not exist."]))
            elif len(obs) > 1:
                return JsonError(''.join(["Filter for criteria ", modelmap[key].__name__, " matches more than one element."]))
            data[key] = obs.first( )

    return None

def modelHasKeys(data, model):
    for key in data.keys():
        if not key in model._meta.get_all_field_names():
            return JsonError(''.join(["No field for ", model.__name__, ": ", key]))
    return None

def isError(response):
    """
    Checks a response from a check function (heh). If it's a HTTPResponse and not something else,
    it's an error. This returns true then.
    """
    return type(response) is django.http.response.HttpResponse

from tokenapi.decorators import token_required
from tokenapi.http import JsonResponse, JsonError
from tracker.models import Course, Homework, Category, Grade
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
    response = [ ]

    res = check(request)
    if res is not None:
        return res

    data = json.loads(request.body.decode('utf-8'))

    response = getObjects(data, model)
    if isError(response):
        return response

    if len(response) > 1:
        return JsonError(''.join(["Delete query matched more than one ", model.__name__, " (matches ", 
                                 str(len(response)), ")."]))
    elif len(response) <= 0:
        return JsonError(''.join(["Delete query did not match any ", model.__name__, "s."]))

    # We return this so they know what their query matched
    data = list(response.values())

    # We're good, delete
    response.delete()
    return JsonResponse({"data": data})

def getObjects(data, model):
    res = dictToObjects(data)
    if res is not None:
        print(type(res))
        return res

    res = modelHasKeys(model, data)
    if res is not None:
        return res 

    response = model.objects.filter(**data)

    return response

def  dictToObjects(data):
    """
    Modifies data to have its hash entries be actual objects. Returns None if successful, or
    JsonError if not.
    """
    # A map for input fields and their appropriate model... don't eval crap.
    modelmap = { "category": Category, "homework": Homework, "course": Course }

    for key in data.keys():
        if key in modelmap.keys():
            res = modelHasKeys(modelmap[key], data[key]) 
            if res is not None:
                return res

            obs = modelmap[key].objects.filter(**data[key])
            if not obs.exists():
                return JsonError(''.join([modelmap[key].__name__, " does not exist."]))
            elif len(obs) > 1:
                return JsonError(''.join(["Filter for criteria ", modelmap[key].__name__, " matches more than one element."]))
            data[key] = obs.first( )

    return None

def modelHasKeys(model, data):
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

from tokenapi.decorators import token_required
from tokenapi.http import JsonResponse, JsonError
from tracker.models import Course, Homework, Category, Grade
import json

# check our input
def check(request):
    if request.method != 'POST':
        return JsonError("Must use POST with json.")
    return None


@token_required
def getWrapper(request, model, keep=[]):
    response = [ ]

    res = check(request)
    if res is not None:
        return res

    data = json.loads(request.body.decode('utf-8'))
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
                return JsonError(''.join(["Filter for ", modelmap[key].__name__, " matches more than one element."]))
            data[key] = obs.first


    res = modelHasKeys(model, data)
    if res is not None:
        return res 

    response = list(model.objects.filter(**data).values())

    return JsonResponse({"data": response})

def modelHasKeys(model, data):
    for key in data.keys():
        if not key in model._meta.get_all_field_names():
            return JsonError(''.join(["No field for ", model.__name__, ": ", key]))
    return None

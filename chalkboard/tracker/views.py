from django.shortcuts import render
from django.http import HttpResponse
from tokenapi.http import JsonResponse, JsonError
import json
from tokenapi.decorators import token_required

# check our input
def check(request):
    if request.method != 'POST' or not ('data' in request.data):
        return JsonError("Must use POST json.")
    return None

def get(request):
    return json.load(requests.data['data'])

def index(request):
    return HttpResponse("This is the API for chalkboard.")

def authenticate(request):
    return HttpResponse("This is the API for chalkboard.")

@token_required
def addCourse(request):
    if check(request) is not None:
        return HttpResponse(check(request))
    data = get(request)
    
    return JsonError("This is the API for chalkboard.")

@token_required
def addHomework(request):
    return HttpResponse("This is the API for chalkboard.")

@token_required
def addGrade(request):
    return HttpResponse("This is the API for chalkboard.")


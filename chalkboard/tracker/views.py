from django.shortcuts import render
from django.http import HttpResponse
from tokenapi.http import JsonResponse, JsonError
import json
from tokenapi.decorators import token_required
from tracker.models import Course, Homework

# check our input
def check(request):
    if request.method != 'POST':
        return JsonError("Must use POST with json.")
    return None

def index(request):
    return HttpResponse("This is the API for chalkboard.")

def authenticate(request):
    return HttpResponse("This is the API for chalkboard.")

@token_required
def addCourse(request):
    """
    name: coursename
    school: school name
    """
    if check(request) is not None:
        return HttpResponse(check(request))
    data = request.POST

    if not 'name' in data:
        return HttpResponse(JsonError("A name field is required to create a course."))
    if not 'school' in data:
        return HttpResponse(JsonError("A school field is required to create a course."))
    if len(data['name']) < 4:
        return HttpResponse(JsonError("The name provided for the course is too short."))
    if len(data['school']) < 4:
        return HttpResponse(JsonError("The name provided for the school is too short."))

    course = Course(name=data['name'], school=data['school'])
    course.save()
    
    return HttpResponse(JsonResponse({"id": course.id, "success": True}))

@token_required
def getCourses(request):
    if check(request) is not None:
        return check(request)
    data = request.POST
    response = [ ]

    for course in Course.objects.all():
        response.append( [course.id, course.name, course.school ])

    return HttpResponse(JsonResponse({"data": response, "success": True}))


@token_required
def addHomework(request):
    if check(request) is not None:
        return check(request)
    return HttpResponse("This is the API for chalkboard.")

@token_required
def addGrade(request):
    if check(request) is not None:
        return check(request)
    return HttpResponse("This is the API for chalkboard.")


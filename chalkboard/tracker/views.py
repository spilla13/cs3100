from django.shortcuts import render
from django.http import HttpResponse
from tokenapi.http import JsonResponse, JsonError
import json
from tokenapi.decorators import token_required
from tracker.models import Course, Homework, Category, Grade
from django.contrib.auth.models import User

# check our input
def check(request):
    if request.method != 'POST':
        return JsonError("Must use POST with json.")
    return None

def index(request):
    return HttpResponse("This is the API for chalkboard.")

@token_required
def addCategory(request):
    """
    name: category name
    """
    if check(request) is not None:
        return check(request)
    data = request.POST

    if not 'name' in data:
        return JsonError("A name field is required to create a category.")
    if len(data['name']) < 4:
        return JsonError("The name provided for the category is too short.")

    cat = Category(name=data['name'])
    cat.save()
    
    return JsonResponse({"data": {"id": cat.id}, "success": True})

@token_required
def addCourse(request):
    """
    name: coursename
    school: school name
    """
    if check(request) is not None:
        return check(request)
    data = request.POST

    if not 'name' in data:
        return JsonError("A name field is required to create a course.")
    if not 'school' in data:
        return JsonError("A school field is required to create a course.")
    if len(data['name']) < 4:
        return JsonError("The name provided for the course is too short.")
    if len(data['school']) < 4:
        return JsonError("The name provided for the school is too short.")

    course = Course(name=data['name'], school=data['school'])
    course.save()
    
    return JsonResponse({"data": {"id": course.id}, "success": True})

@token_required
def getCourses(request):
    if check(request) is not None:
        return check(request)
    data = request.POST
    response = [ ]

    for course in Course.objects.all():
        response.append( [course.id, course.name, course.school ])

    return JsonResponse({"data": response, "success": True})


@token_required
def addHomework(request):
    """
       categoryid: <categoryid>
       name:      <name>

       
       optional:
         weight:    <weight>
         pointspossible: <# points possible>
    """
    if check(request) is not None:
        return check(request)
    data = request.POST
    
    if not 'name' in data:
        return JsonError("A name field is required to create a homework.")
    if not 'categoryid' in data:
        return JsonError("A categoryid field is required to create a homework.")
    if len(data['name']) < 4:
        return JsonError("The name provided for the homework is too short.")
    try:
        Category.objects.get(id=data['categoryid'])
    except:
        return JsonError("The categoryid provided for the homework isn't valid.")

    homework = Homework(name=data['name'], category=data['categoryid']) 

    if 'weight' in data:
        homework.weight = data['weight']
    if 'pointspossible' in data:
        homework.pointspossible = data['pointspossible']

    homework.save()

    return JsonResponse({"data": { "id": homework.id }, "success": True})

@token_required
def addGrade(request):
    """
        courseid: <courseid>
        homeworkid: <homeworkid>
        
        optional:
         pointsreceived: <# points>

    """

    if check(request) is not None:
        return check(request)
    data = request.POST

    if not 'courseid' in data:
        return JsonError("A courseid field is required to create a grade.")
    if not 'homeworkid' in data:
        return JsonError("A homeworkid field is required to create a grade.")

    try:
        User.objects.get(id=data['user'])
    except:
        return JsonError("The userid provided for the grade isn't valid.")

    try:
        Course.objects.get(id=data['courseid'])
    except:
        return JsonError("The courseid provided for the grade isn't valid.")

    try:
        Homework.objects.get(id=data['homeworkid'])
    except:
        return JsonError("The homeworkid provided for the grade isn't valid.")

    grade = Grade(course=data['courseid'], homework=data['homeworkid'], user=data['userid'])

    if 'pointsreceived' in data:
        grade.points_received = data['pointsreceived']

    grade.save()

    return JsonResponse({"data": { "id": grade.id }, "success": True})


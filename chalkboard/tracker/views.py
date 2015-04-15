from django.shortcuts import render
from django.http import HttpResponse
import json


def index(request):
    return HttpResponse("This is the API for chalkboard.")

def authenticate(request):
    return HttpResponse("This is the API for chalkboard.")

def addCourse(request):
    return HttpResponse("This is the API for chalkboard.")

def addHomework(request):
    return HttpResponse("This is the API for chalkboard.")

def addGrade(request):
    return HttpResponse("This is the API for chalkboard.")


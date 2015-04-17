from django.db import models
from django.contrib.auth.models import User

# check our input
def check(request):
    if request.method != 'POST':
        return JsonError("Must use POST with json.")
    return None

class Category( models.Model ):
    name = models.CharField( max_length=100 )

class Course( models.Model ):
    school = models.CharField( max_length=255 )
    name = models.CharField( max_length=100 )

class Homework( models.Model ):
    category = models.ForeignKey( Category )
    weight = models.FloatField( default=1 )
    name = models.CharField( max_length=100 )

    points_possible = models.FloatField( default=0 )

class Grade( models.Model ):
    course = models.ForeignKey( Course )
    user = models.ForeignKey( User )
    homework = models.ForeignKey( Homework )
    
    points_received = models.FloatField( default=0 )


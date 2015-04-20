from django.db import models
from django.contrib.auth.models import User

class Category( models.Model ):
    name = models.CharField( max_length=100 )
    weight = models.FloatField( default=1 )

class Course( models.Model ):
    school = models.CharField( max_length=255 )
    name = models.CharField( max_length=100 )

class Homework( models.Model ):
    category = models.ForeignKey( Category )
    name = models.CharField( max_length=100 )

    points_possible = models.FloatField( default=0 )

class Grade( models.Model ):
    course = models.ForeignKey( Course )
    user = models.ForeignKey( User )
    homework = models.ForeignKey( Homework )
    
    points_received = models.FloatField( default=0 )


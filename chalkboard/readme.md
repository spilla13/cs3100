#Summary

All API calls should be made over HTTP (HTTPS is currently not supported, but if we want to go that route we can) with POST. 
All data should be encoded in JSON. Responses will be encoded in JSON as well. A 403 response will be returned if a user isn't authenticated, with no data.

Note that all urls must end in a /.

#Administration

You can administrate the server by using the django-provided admin interface at /admin/. For this project, it's:

http://cs3100.brod.es:3100/admin/

The username and password must be that of a superuser.

#API
##Results
###Successful Calls

Successful calls to the API will return JSON-encoded date in the format below:
  
```json
  {
    "success": 1,
    "data": {
      <data>
    }
  }
```

###Failed Calls

Failed calls to the API will return interesting (read: varied) JSON-encoded data in the format below:

```json
  {
    "success": 0,
    "error": "messagehere"
  }  
```

Unauthenticated calls are an exception, since instead of returning HTTP 200 (successful) they will return HTTP 403 (Forbidden).

##Permissions

###Registering

Currently there is no API for registering. However, POST data can be sent to /register/ which matches the id fields of the forms at that location. 
It requires that you confirm your e-mail address afterwards, however this currently has no effect on the API (this can be fixed if desired).

http://cs3100.brod.es:3100/register/

An API will be implemented for this ASAP.


###Authentication

To authenticate, send via POST JSON to:

http://cs3100.brod.es:3100/token/new.json

For example:

```json
  {
    "username": "utest",
    "password": "ptest"
  }
```

This will authenticate user utest with password ptest. For now, send these in plaintext. The server will respond one of two ways:

```json
  {
    "success": false,
    "errors": "Unable to log you in, please try again."             <--- note the s, this isn't my API... I'll fix this if I have time.
  }
```

Or, if successful:

```json
  {
    "success": true,
    "user": 3,
    "token": "40y-47bf2a0b3acc6953475d"
  }
```

This token and user must be sent in all privileged API calls (such as the ones listed below). I will probably inconsistently
show them where need be. 

Note that token may become invalid after one of the below happen:
  * Source code of the server is modified
  * Server is restarted
  * Token is > 7 days old

In this case, all API calls will return HTTP 403 (forbidden) and you must reauthenticate and use the new token. User will not change.

## Access Calls

All access calls can be found under `/get/` which separates them from those calls which add or remove items.

### Access Courses

http://cs3100.brod.es:3100/get/courses/

This call allows you to get a list of all courses. It currently does not allow filtering of courses, and will 
just return all of them listed. Here is an example call:

```json
  {
    "user": 3,
    "token": "40y-47bf2a0b3acc6953475d"
  }
```

So it takes no arguments, just authentication for who you are. This fetches, then, a list of all classes
like so:

```json
  {
    "data": [
              [ courseid, "coursename", "courseschoolname" ],
              [ "<more>" ],
              "<more>",
            ],
    "success": 1
  }
```
## Add Calls

All add calls can be found under `/add/` which seperates them from those calls which get or remove items.

### Add Course

http://cs3100.brod.es:3100/add/course/

Adding a course takes a name and school and only filters to make sure that they are at least 4 characters long.

```json
  {
    "user": 1,
    "token": "40y-47bf2a0b3acc6953475d",
    "name": "name of the course, 4-100 chars",
    "school": "name of the school, 4-100 chars"
  }
``` 

This will return an id in a data array. This is the unique id of the new course in the database.

```json
  {
    "success": 1,
    "data": {
              "id": 4
            }
  }
```

Errors are returned following the general format at the top. This includes fields missing or names being too short.

### Add Category

http://cs3100.brod.es:3100/add/category/

Takes a name in for a category, checks its size, and creates a new one.

```json
  {
    "user": 1,
    "token": "40y-47bf2a0b3acc6953475d",
    "name": "name of the new category, 4-100 chars",
  }
``` 

This will return an id in a data array. This is the unique id of the new category.

```json
  {
    "success": 1,
    "data": {
              "id": 1
            }
  }
```

Errors can be returned for the lack of names in JSON or ids being too short.

### Add Homework

http://cs3100.brod.es:3100/add/homework/

Adds an assignment to the tracker. Anyone can use this assignment.

weight and pointspossible are optional fields. They, respectively, default to
1 and 0.

```json
  {
    "user": 1,
    "token": "40y-47bf2a0b3acc6953475d",
    "name": "name of the assignment, 4-100 chars",
    "categoryid": 1,
    

    "weight": 0.13,
    "pointspossible": 100
  }
``` 

This will return an id in a data array. This is the unique id of the new assignment.

```json
  {
    "success": 1,
    "data": {
              "id": 1
            }
  }
```

Errors can be returned for the lack of fields in the JSON-encoded data, ids not existing, or fields being too short.

### Add Grade

http://cs3100.brod.es:3100/add/grade/

Adds a grade to the tracker. Only the owner can see their grades (right..?).

pointsreceived is an optional field, it defaults to 0.

```json
  {
    "user": 1,
    "token": "40y-47bf2a0b3acc6953475d",
    "courseid": 3,
    "homewordid": 1,

    "pointsreceived": 100
  }
``` 

This will return an id in a data array. This is the unique id of the new grade.

```json
  {
    "success": 1,
    "data": {
              "id": 1
            }
  }
```

Errors are either that the ids were missing from the input data or are not valid ids.

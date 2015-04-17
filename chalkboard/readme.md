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
    "errors": "messagehere"
  }  
```

Unauthenticated calls are an exception, since instead of returning HTTP 200 (successful) they will return HTTP 403 (Forbidden).

##Permissions

###Registering

Registration can be done graphically:

http://cs3100.brod.es:3100/register/

An API also exists, which requires username, password, and e-mail to be passed like so:

```json
  {
    "username": "utest",
    "password": "ptest",
    "email": "ptest@aol.com"
  }
```

This can be done at `/add/user/`, for the production site:

http://cs3100.brod.es:3100/add/user/

A standard response will then be returned if successful, with data containing the new user id.

Errors will be returned if the username is too short, too long, the password is too short, the email is too short,
or the username already exists. Status will be false for these.

###Authentication

To authenticate, send via GET (in the url) user and token to:

http://cs3100.brod.es:3100/token/new.json

For example:

http://cs3100.brod.es:3100/token/new.json?username=utest&password=ptest

This will authenticate user utest with password ptest. For now, send these in plaintext. The server will respond one of two ways:

```json
  {
    "success": false,
    "errors": "Unable to log you in, please try again."
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

This token and user must be sent in the url of all privileged API calls (such as the ones listed below). 
I will probably inconsistently show them where need be. 

Note that token may become invalid after one of the below happen:
  * Source code of the server is modified
  * Server is restarted
  * Token is > 7 days old

In this case, all API calls will return HTTP 403 (forbidden) and you must reauthenticate and use the new token. User will not change.

## Access Calls

All access calls can be found under `/get/` which separates them from those calls which add or remove items. Access
calls can be made with filtering arguments, or without them. These arguments share the name of the model rows / elements. 

For a simple example, filtering on name is as simple as passing `"name": "homework name"`. 

Where the query was sent to:
http://cs3100.brod.es:3100/get/homework/?user=1&token=40y-abeg1907d63f8512c

```json
  {
    "name": "homework name"
  }
```

For a more complex example, grade has homework which is a foreign key. So if we filter by 
homework we have to include a nested hash like so:

```json
  {
    "homework": {
                  "name": "hw2"
                }
  }
```

This returns all grades with the name hw2.

To take this further, grade has another foreign key, course. You can also filter on course which will
return ALL matches.

```json
  {
    "homework": {
                  "name": "hw2"
                },
    "course": {
                "name": "3100",
                "school": "Missouri S&T"
              }
  }
```

This returns all categories with name catname and id 2.

For a final, complex, example you can filter homework results on category. Since category is a table,
you can also filter category results within homework. For example:

```json
  {
    "homework": {
                  "name": "hw2",
                  "category": {
                                "name": "Computer Science" 
                              }
                },
    "course": {
                "name": "3100",
                "school": "Missouri S&T"
              }
  }
```

This will return all grades for homework with a name of hw2, in a category with the name of computer science,
for a course with the name of 3100 at a school Missouri S&T. 

This API will allow you to filter on any table recursively, as shown above. However, the not top level responses 
MUST return only one result. 


Returns will follow this format:

```json
  {
    "data": [
              { "course_id": 2, "homework_id": 1, "user_id": 3, "id": 1 },
              { "^^^" },
              "..."
            ],
    "success": 1
  }
```

Where data is an array of matches. This array can be zero length (no matches but successful query), have one element,
or have 500 elements.

### Access Courses

http://cs3100.brod.es:3100/get/course/

This call allows you to get a list courses which match your query. Here is a query which gets all courses:

```json
  {

  }
```

An example query with maximum filtering:

```json
  {
    "school": "blue eye",
    "name": "CS3100",
    "id": 1
  }
```

Format returned matches: 

```json
  {
    "success": true,
    "data": [
              { "school": "schoolname",
                "name": "coursename",
                "id": 2
              },
              { "...": "..." },
              "..."
            ]
  
  }
```

### Access Categories

http://cs3100.brod.es:3100/get/category/

This call lets you list categories which match your query. The standard blank query will return all categories.

Here is a query with maximum filtering:

```json
  {
    "name": "category name",
    "id": 2
  }
```

Which will return a list of matches, following the format below:

```json
  {
    "success": true,
    "data": [
              { "name": "catname",
                "id": 2
              },
              { "...": "..." },
              "..."
            ]
  
  }
```

Except since id was specified, there will be only one match in the array.

### Access Homework

http://cs3100.brod.es:3100/get/homework/

This call allows you to get a list of homework which match your query. The standard blank query returns
all homework.

An example query with maximum filtering:

```json
  {
    "name": "homework name",
    "points_possible": 100.4,
    "weight": 0.2,
    "category": {
                  "id": 1,
                  "name": "computer science"
                },
    "id": 1
  }
```

Which will return a list of matches, following the format below:

```json
  {
    "success": true,
    "data": [
              { "name": "homework1",
                "id": 1,
                "points_possible": 500.1,
                "weight": 30,
                "category_id": 2
              },
              { "...": "..." },
              "..."
            ]
  
  }
```

### Access Grades

http://cs3100.brod.es:3100/get/grades/

This call allows you to get a list of grades which match your query. The standard blank query returns
all grades.

Grades are always filtered for the user id and token provided. You cannot receive another user's grades.

An example query with maximum filtering:

    course = models.ForeignKey( Course )
    user = models.ForeignKey( User )
    homework = models.ForeignKey( Homework )
    
    points_received = models.FloatField( default=0 )

```json
  {
    course: {
                "id": "name",
                "name": "course name",
                "school": "school name"
            },
    homework: {
                "name": "homework name",
                "id": 1,
                "points_possible": 100.2,
                "weight": 1.2,
                "category": {
                              "name": category name",
                              "id": 2
                            },
    "points_received": 100.4,
    "id": 1
  }
```

So, all nested queries (as stated at the beginning of this parent section) must return one result.
That being said, you can omit as much of this data as you want, you may just get several results depending on
what you omit.

Queries return a list of matches, following the format below:

```json
  {
    "success": true,
    "data": [
              { "course_id": 2,
                "id": 1,
                "points_received": 52.9,
                "homework_id": 2
              },
              { "...": "..." },
              "..."
            ]
  }
```

As always, every hash in data will have the same number of values if there are any results.

## Add Calls

All add calls can be found under `/add/` which seperates them from those calls which get or remove items.

### Add Course

http://cs3100.brod.es:3100/add/course/

Adding a course takes a name and school and only filters to make sure that they are at least 4 characters long.

```json
  {
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

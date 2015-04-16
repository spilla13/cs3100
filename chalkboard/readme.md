soon™

#Summary

All API calls should be made over HTTP (HTTPS is currently not supported, but if we want to go that route we can) with POST. 
All data should be encoded in JSON. Responses will be encoded in JSON as well. A 403 response will be returned if a user isn't authenticated, with no data.

Note that all urls must end in a /.

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
It requires that you 

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

http://cs3100.brod.es/get/courses/

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
              [ ... ],
              ...
            ]
  }
```

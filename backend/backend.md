# Database

- users(<u>username</u>, email, password)
  
      UNIQUE (email)

- charities(<u>orgId</u>, timestamp)

- pausedCharities(<u>orgId</u>, timestamp, admin)
  
    orgId -> charities.orgId

    admin -> administrators.user

- likes(<u>user</u>, <u>charity</u>, timestamp)
  
    user -> users.username

    charity -> charities.orgId

- comments(<u>commentId</u>, <u>charity</u>, comment, date, user)
  
    user -> users.username

    charity -> charities.orgId

- commentScores(<u>comment</u>, <u>charity</u>, <u>user</u>, upDown)
  
    comment, charity -> comments.commentId, comments.charity
    
    user -> users.username
    
    upDown - bool    

- administrators(<u>user</u>, level)
  
    user -> users.username
    
    level IN (1, 2, 3)

- searchedCharities(<u>username</u>, <u>charity</u>, timestamp)
  
   username -> users.username
   
   charity -> charities.orgId

- commentBlame(<u>comment</u>, <u>charity</u>, <u>reporter</u>, reason)
  
    comment, charity -> comments.commentId, comments.charity
   
    reporter -> users.username

- charityBlame(<u>charity</u>, <u>reporter</u>, reason)
  
   charity -> charities.orgId
   
   reporter -> users.username

# Endpoints

## Endpoint description syntax

The following is a set of endpoints that the backend uses to communicate with front-end instances.

Each section will contain a header that contains the endpoint function, followed by the HTTP method of
the endpoint.

### Arguments syntax

Following the header, there will be code block containing a representation of the json values accepted
by the endpoint. Assume that the accepted properties on the json objects are the exact properties on the
representations.

If a comment following a peoperty ends with "(optional)" it signifies that the property can be left out
without causing an error. Assume all other properties are required.

Comments that follow json elements and end with "// , ..." signifies that there may occur more occurences
of similar elements folowing the commented element.

Values of properties are not to be taken litteraly, they are representations of the expected format of the
property. This is true as long as the string does not contain the '|' character, in that case, the expected
format is one, and only one, of the exact strings delimitered by the '|' character.

### Return syntax

Any comment that starts with "TODO: " indicates that the following part of the comment is going to be added
to the representation once the proper functionality is implemented. Specifying a field that is marked with
the "TODO: " comment will _never_ be an error, but it may have no effect on the results, as it is still a
work in progress feature.

Following the representation, there will be one or more code blocks that specify the values that may be returned
by the endpoint. This will occur after a line containing the string "Return:" followed by a code block that
will either contain a json object that represents the returnable value.

Following the "Return:" label, there may also optionally be one or more "or:" labels followed by code blocks.
These specify other options for what may be returned by the endpoint.

Assume that the representation of returned objects follows the same syntax as for arguments, unless something
else is specified.

### Filtering

Any endpoint that has the return format of two properties: one named "value" and one named "message" where "value"
is a json array can be filtered on.

> For an exact definition of filters and advanced queries, please refer to the [documentation on filtering](./docs/filtering.md).

The exact fields that can be filtered on are the exact fields of the objects contained in the returned "value" array.
For an exact schema on these returned objects, refer to the `backend/docs/schemas` files.

## User endpoints

### Login (GET)

> **_NOTE_**: Marked "GET" because logging in is _"getting"_ login credentials (JWT)

`/api/users/login`
```json
{
    "username" : "NameOfUser",
    "email" : "user@email.site",
    "password" : "password123"
}
```
Return:
```jsonc
{
    "token": "jwt"
}
```
or:
```jsonc
"Error message"
```

### Change password (PUT)

`/api/users/change_password`

```json
{
    "username" : "NameOfUser",
    "old" : "The old password...",
    "new" : "Password to set..."
}
```

Return:

```json
"Status message"
```

### Reset password (PUT)

`/api/users/reset_password`

```jsonc
{
    "email" : "user@email.site",
    "new_password" : "Password to set...",
    "verification_code" : 1234 // Code the user read from an email they recieved.
}
```

Return:

```json
"Status message"
```

### Change email (PUT)

`/api/users/change_email`

```json
{
    "username" : "NameOfUser",
    "email" : "user.new@email.site",
    "password" : "Password of user..."

}
```

Return:

```json
"Status message"
```

### Get activity (GET)

`/api/users/get_activity`

```jsonc
{
    "username" : "NameOfUser",
    "type" : "comments|likes",
    "query": {
        "type": "comments|comment_blame",
        "first": 0, // index of first result (optional)
        "max_count": 99, // Max number of results (optional)
        "filters": [ // Filter of results (optional)
            {
                "field": "somefield",
                "value": 42,
                "filter": "less|greater|equals|like"
            },
            {
                "filter": "or|and|not",
                "arguments": [ /* sub-filter(s) */ ]
            }
        ],
        "sorting": { // How results are sorted (optional)
            "field": "field-to-sort-by", // Sort by selected data field
            "ordering": "ascending|descending" // Sort in ascending or descending order
        }
    }
}
```

Return:

```jsonc
{
    "value": [
        {
            "charity": "r424ttt", // Charity that was commented on
            "commentId": 1234, // Id of comment
            "comment": {
                "message": "Comment message"
                // , ...
            },
            "user": "CommentingUser" // User that commented
            // TODO: "time": 34554566 // Timestamp of comment
        } // , ...
    ],
    "message": "Status message"
}
```
or:
```jsonc
{
    "value": [
        {
            "commentId": 1234, // Id of comment that was blamed.
            "charity": "345rt4", // Charity that was commented on.
            "reporter": "ReportingUser", // User that blamed the comment.
            "reason": "none" // Reasoning for blaming the comment.
        } // , ...
    ],
    "message": "Status message"
}
```
or:
```jsonc
{
    "message": "Error message"
}
```

### Create user (POST)

`/api/users/create`

```jsonc 
{
    "username" : "NameOfUser",
    "password" : "password123",
    "email" : "user@email.site",
    "signed" : false // Not used (optional)
}
```
Return:
```json
"Status message"
```

### Delete user (DELETE)

`/api/users/remove`

```json
{
    "username" : "UserToRemove"
}
```
Return:
```json
"Status message"
```

## Charities

### Get charities (GET)

`/api/charities/list`

```jsonc
{
    "type": "comments|comment_blame",
    "first": 0, // index of first result (optional)
    "max_count": 99, // Max number of results (optional)
    "filters": [ // Filter of results (optional)
        {
            "field": "somefield",
            "value": 42,
            "filter": "less|greater|equals|like"
        },
        {
            "filter": "or|and|not",
            "arguments": [ /* sub-filter(s) */ ]
        }
    ],
    "sorting": { // How results are sorted (optional)
        "field": "field-to-sort-by", // Sort by selected data field
        "ordering": "ascending|descending" // Sort in ascending or descending order
    }
}
```
Return:
```jsonc
{
    "value": [
        {
            "identity": "345g35t5" // Charity organization id
            // TODO: , ...
        }
    ],
    "message": "Status message"
}
```
or:
```json
{
    "message": "Error message"
}
```
### Get charity (GET)

`/api/charities/get`

```jsonc
{
    "orgId": "12d33r3f44t" // Organization id.
}
```
Return:
```jsonc
{
    "value": {
        "orgId": "345g35t5" // Charity organization id
        // TODO: , ...
    }
    ,
    "message": "Status message"
}
```
or:
```json
{
    "message": "Error message"
}
```

### Charity vote (POST)

`/api/charities/vote`

```jsonc
{
    "charity" : "12f4344tt", // Organization id
    "up" : true // true - vote for, false - vote against 
}
```
Return:
```json
"Status message"
```

### Change vote (PUT)

`/api/charities/edit_vote`

```jsonc
{
    "charity": "23535g25442", // Organization id
    "up": false // New value of "up", true - vote for, false - vote against
}
```

Return:
```json
"Status message"
```

### Delete vote (DELETE)

`/api/charities/remove_vote`

```jsonc
{
    "charity": "343rg4g44" // Charity the current user voted on
}
```

Return:
```json
"Status message"
```

### Pause charity (POST)

> **_OBS_**: Not implemented

`/api/charities/pause`

```json
{
    "charity_id" : "TODO"
}
```

### Resume charity (DELETE)

> **_OBS_**: Not implemented 

`/api/charities/resume`

```json
{
    "charity_id" : "TODO"
}
```

### Get paused charities (GET)

> **_OBS_**: Not implemented.

`/api/charities/get_paused`

```json
{
}
```

## Comments

### Add comment (POST)

`/api/comments/add`

```jsonc
{
    "comment" : { "message": "Comment message..." },
    "charity" : "aeer323r4" // Charity to comment on
}
```
Return:
```json
"Status message"
```

### Remove comment (DELETE)

`/api/comments/remove`

```jsonc
{
    "comment_id" : 99, // Identifier of the comment
    "charity" : "32542h5454" // Organization id of charity to remove a comment from
}
```
Return:
```json
"Status message"
```

### Blame comment (POST)

`/api/comments/blame`

```jsonc
{
    "comment_id" : 99, // Comment id of comment to blame
    "charity": "sdrt4t4t5", // Organization of the charity that was commented on. 
    "reason" : "none" // Reasoning for blaming the charity
}
```
Return:
```json
"Status message"
```

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
    "identity": "12d33r3f44t" // Organization id.
}
```
Return:
```jsonc
{
    "value": {
        "identity": "345g35t5" // Charity organization id
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

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

`/api/users/login`

### Change password (PUT)

`/api/users/change_password`

```json
{
    "username" : string,
    "old" : string,
    "new" : string
}
```

### Reset password (PUT)

`/api/users/reset_password`

```json
{
    "email" : string,
    "new_password" : string,
    "verification_code" : int
}
```

### Change email (PUT)

`/api/users/change_email`

```json
{
    "username" : string,
    "email" : string,
    "password" : string

}
```

### Get activity (GET)

`/api/users/get_activity`

```json
{
    "username" : string,
    "type" : ["comments" AND/OR "likes"],
    "filters" : string (TODO)
}
```

### Create user (POST)

`/api/users/create`

```json
{
    "username" : string,
    "password" : string,
    "email" : string,
    "signed" : boolean
}
```

### Delete user (DELETE)

`/api/users/remove`

```json
{
    "username" : string
}
```



## Charities

### Get charities (GET)

`/api/charities/list`

```json
{
    "filters" : [],
    "order_by" : string
}
```

### Get charity (GET)

`/api/charities/get`

```json
{
    "identity" : string (orgId)
}

```

### Charity vote (POST)

`/api/charities/vote`

```json
{
    "charity" : string,
    "up" : boolean,
}
```

### Change vote (PUT)

`/api/charities/edit_vote`

```json
{
    "charity": string,
    "up": boolean
}
```

### Delete vote (DELETE)

`/api/charities/delete_vote`

```json
{
    "user": string,
    "charity": string
}
```

### Pause charity (POST)

`/api/charities/pause`

```json
{
    "charity_id" : string
}
```

### Resume charity (DELETE)

`/api/charities/resume`

```json
{
    "charity_id" : string
}
```

### Get paused charities (GET)

`/api/charities/get_paused`

```json
{
    "filters" : [],
    "order_by" : string
}
```



## Comments

### Add comment (POST)

`/api/comments/add`

```json
{
    "comment" : string,
    "charity" : string
}
```

### Remove comment (DELETE)

`/api/comments/remove`

```json
{
    "comment_id" : string,
    "charity" : string
}
```

### Blame comment

`/api/comments/blame`

```json
{
    "comment_id" : string,
    "reason" : string,
    "comment": string OR null,
    "blame_victim" : string
}
```

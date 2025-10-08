# Updated API

The problem with the old API was that many of the endpoints on the back-end would accept GET
mappings and simultaneously required parameters through JSON objects.

The problem with this is that GET mappings cannot include a body. The first idea for a solution
to this problem was to encode the JSON objects inside the query parameters for the endpoint.

RestAPI (and most HTTP engines) however, set a fixed limit on the length of a URL, meaning that
the size of the JSON objects we can encode in is limited. Upon further testing, it turned out
that the requests would fail for only a few properties per JSON object.

## The hack solution

There isn't a real reason why endpoints _have_ to be GET mappings (even if they are used to
retrieve data). According to the internet standard, _GET_ should be used, but methods like
_POST_ still technically _allow_ you to return data.

For this reason, you could in practice always return objects using POST the same way you would
with GET. Is this best practice? - Hell no, this is against the standards of how websites are
supposed to behave. Will it be quicker to implement? - Yes. We can modify just the mapping type
of each endpoint and call it a day, no need to change any parameter passing method.

## The best practice solution

The long term solution is probably still to move over to GET mappings and assign advanded queries
as resources that can be retrieved. The "best practice" pattern to use would then be for the client
to first POST their query.

POST returns an identifier for the query results, and the user would then use that identifier to
get then results of the query:

```
    Query = {
        "first": 3,
        "max_count": 5,
        "query": {
            "sorting": {
                "field": "insertTime",
                "ordering": "ascending"
            },
            "filters": [
                {
                    "field": "insertTime",
                    "filter": "less",
                    "value": "2025-09-10 20:00:00.0000"
                }
            ]
        }
    }

    client -> host:
        POST {
            url: "http://host/api/.../endpoint/query/begin",
            method: POST,
            body: {Query}
        }

    host -> client:
        Status: 200
        Body: {
            "message": "ok",
            "resource": 1000
        }

    client -> host:
        GET http://host/api/.../endpoint/query/result?qid=1000

    host -> client:
        Status: 200
        Body: {
            "message": "ok",
            "value": [
                { ... },
                { ... },
                ...
            ]
        }
```

## The current api

This is the current api spec. All json objects accepted by the endpoints stay the same, but the mappings have changed,
and some endpoints will now be GET with a single, short query parameter.

|  Endpoint                                |  HTTP method                    |  Accepts JSON body |  Query params  |
|------------------------------------------|---------------------------------|--------------------|----------------|
|/api/users/login                          |                            POST |                yes |           none |
|/api/users/create                         |                            POST |                yes |           none |
|/api/users/change_email                   |                             PUT |                yes |           none |
|/api/users/change_password                |                             PUT |                yes |           none |
|/api/users/reset_password                 |                             PUT |                yes |           none |
|/api/users/change_email                   |                             PUT |                yes |           none |
|/api/users/remove                         |                          DELETE |                yes |           none |
|/api/users/get_activity/comments          |                            POST |                yes |           none |
|/api/users/get_activity/comment_blame     |                            POST |                yes |           none |
|/api/users/get_activity/searched_charities|                            POST |                yes |           none |
|/api/charities/list                       |                            POST |                yes |           none |
|/api/charities/get                        |                             GET |                 no | charity:string |
|/api/charities/vote                       |                            POST |                yes |           none |
|/api/charities/edit_vote                  |                             PUT |                yes |           none |
|/api/charities/remove_vote                |                          DELETE |                yes |           none |
|/api/comments/add                         |                            POST |                yes |           none |
|/api/comments/list                        |                            POST |                yes |           none |
|/api/comments/remove                      |                          DELETE |                yes |           none |
|/api/comments/blame                       |                            POST |                yes |           none |
|/api/email/confirm/{email}/{confirmCode}  |                            POST |                 no |           none |
|/api/debug/mock/login                     |                             GET |                 no |           none |

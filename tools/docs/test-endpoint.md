# test-endpoint

`test-endpoint` is a tool for verifying endpoint communication with the benesphere server. The tool utilizes `curl` to
send requests via http to the various endpoints in the back-end server. `test-endpoint` will also automatically keep
track of JWTs after each login, meaning you do not need to specify it manually.

There are two modes of operation, you can either use the tool to launch benesphere with a specific configuration, or
you may use the tool for sending requests to the launched instance.

## Usage

```
    test-endpoint
        [--port|-p <port>]
        [--host|-h <host>]
        --launch
    
    test-endpoint
        [--port|-p <port>]
        [--host|-h <host>]
```

## Command-line options

- `-p|--port <port-number>` port to communicate on (default: 8080)
- `-h|--host <host>` host of the benesphere server (default: 127.0.0.1)
- `-e|--launch` launches benesphere instead of running the tester.
    The launched instance will work on the provided port and host.

## Launch mode

When launching the tool will first build the application using `mvn` and it will then launch the application and provide
the specified port in `--server.port` and host in `--server.host` on the program command line.

After launching, the tool is effectively replaced with the server instance, so further tests need to be made from a separate
shell.

Example of a launch mode run:
```bash
    ./tools/test-endpoint --port 1234 --launch
```
The snippet above launches benesphere and sets it to communicate on the 1234 port.

## Test mode

The test mode is the standard mode of operation for the tool. This mode is an abstraction layer on top of `curl` that automatically
configures the HTTP headers and perform som translations on the absolute URL.

This mode is an interactive process where you will enter the requests you wish to send to the server. A request command starts with
the HTTP method (GET, POST, PUT, or DELETE), followed by the endpoint (/api is prepended to the endpoint if it was not provided) and
then followed by a set of JSON fields to set in the request body.

Example:

```
    ./tools/test-endpoint --port 1234
    --(api)--> GET users/login username="myuser" password="password123"
    Request: GET http://127.0.0.1:1234/api/users/login
    Request body: {
        "username": "myuser",
        "password": "password123"
    }
    Status: 200
    Contents: {
        "token": "<jwt here...>"
    }
    Setting JWT to <jwt here...>
```

To set a JSON field, you need to specify a field name, followed by the '=' character, followed by the JSON value to set it to. Do also
note that the above command will _automatically_ detect whenever a `login` request succeeds and returns a JWT and then also apply that
JWT for all subsequent commands.

When in the test mode, you can always exit by entering `exit`, `quit` or `q` into the prompt, or by sending an interrupt (`Ctrl+C`).

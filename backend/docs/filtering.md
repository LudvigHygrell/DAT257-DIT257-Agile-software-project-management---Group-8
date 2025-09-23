# API for applying filters to queries

A filter is represented as a JSON array of filter objects. These objects have the following schema:

```json
{
    "type": "object",
    "properties": {
        "filter": { "type": "string" },
        "field": { "type": "string" },
        "arguments": { "type": "array" },
        "value": {}
    },
    "required": ["filter"]
}
```

The `filter` property determines what type of filter is applied. `field` is the field that the filter
is applied on and `arguments` is an optional array of arguments that may be passed to the filter. If
the filter is a comparison operator, then the `value` field determines the right hand operand.

> Exact schema can be found in [filtering.schema.json](./filtering.schema.json)

## Filter methods

Available methods of filtering are:

- `"less"` search for entries with `field` less than a specified value.
- `"greater"` search for entries with `field` greater than a specified value.
- `"equal"` search for entries with `field` equal to a specified value.
- `"like"` search for entries with `field` that match a given string pattern.
- `"not"` invert the result of query specified in `arguments`
- `"or"` accept any result of query specified in `arguments`
- `"and"` accept only if all filters in `arguments` pass. 

## Fields

The fields available for any filtering operation will depend on the type of object being queried. The
following is a list of properties and their corresponding object: ***TODO***

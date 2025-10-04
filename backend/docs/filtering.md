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
object type is determined by the endpoint and the arguments passed to the corresponding _GET_.

The exact fields that can be filtered on can be found by looking at one of the schemas under
`schemas/api-return-values` and by looking at the properties on the objects in the returned arrays for
any end point with the properties "value" and "message" (where "value" is an array).

The exact fields that can be filtered on are the names of the properties listed in these objects.

# Advanced queries

Beyond applying a filter, there are other operations you may want to apply to the generated results. These
are applied through advanced query objects, the schema of which is specified in the [query.schema.json](./schemas/query.schema.json) file.

In any endpoint, the query will be specified as an argument, either as the root object, or through the
"query" property.

What queries add is an option to minimize the number of returned results, by specifying a value for the
"max_count" field. To get successive results, you would also specify the "first" property to be the
index of the first element you want to get (you would probably do this by successively adding the value
of "max_count" for every new page to load to "value").

You can also sort the results by specifying the "sorting" field and setting it's value to an object with
a property "field", which specifies what field to sort by. This is followed by a field "ordering" that has
either the value "ascending" for ascending ordering, or "descending" for descending ordering.

Filters are supplied through the "filters" property, which accepts an array, where every entry in the array
is a [filter](./schemas/filtering.schema.json) to accumulatively apply to the search result.

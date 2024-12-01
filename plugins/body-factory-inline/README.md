# body-factory-inline
Body factory that loads the body from the configuration field.

## Configuration

```yaml
request:
  body:
    type: inline
    # This template as a rest of an application uses StringTemplate4 engine:
    # https://github.com/antlr/stringtemplate4
    #
    # Using the YAML multiline string feature is recommended to make the configuration more readable.
    template: >
      {
        "name": "<name>",
        "number": "<number>",
        "state": "<state>"
      }
```
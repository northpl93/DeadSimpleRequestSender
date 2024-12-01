# data-source-random
Datasource that generates random data with a given pattern.
Useful for benchmarking the DSRS.

## Configuration

```yaml
data:
  type: random
  # How many random rows will be generated
  limit: 1000
  # List of columns to generate.
  # Currently supported types:
  # * STRING
  # * INTEGER
  columns:
    - name: name
      type: STRING
    - name: number
      type: STRING
    - name: state
      type: STRING
```
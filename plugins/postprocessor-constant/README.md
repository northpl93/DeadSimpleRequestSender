# postprocessor-constant
Post-processor that inserts constant values into each data row.

## Configuration

```yaml
postProcessors:
  - type: constant
    # Map with values to be added into the data row. The key is a column name.
    values: 
      language: "en-US"
```
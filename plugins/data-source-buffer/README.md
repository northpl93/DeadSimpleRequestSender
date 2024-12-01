# data-source-buffer
Data source that buffers data from another data source.
Useful for example when the original data source has a time limit to fetch the data.

## Configuration

```yaml
data:
  type: buffer
  # Configuration of the another data source
  source: 
    type: "..."
```
# data-source-bigquery
Datasource that reads data from the BigQuery table.

## Configuration

```yaml
data:
  type: bigquery-table
  # The project that will be billed for opening the BigQuery Read Session
  billingProject: "deadsimplerequestsender-test"
  # The table that will be loaded
  table:
    project: "bigquery-public-data"
    dataset: "usa_names"
    name: "usa_1910_current"
  # Columns that will be loaded from BigQuery
  fields:
    - name
    - number
    - state
```

## Getting started with the data source
1. Make sure you have installed [gcloud CLI](https://cloud.google.com/sdk/docs/install).
2. Login to your Google account using `gcloud`
   ```
   gcloud auth application-default login
   ```
3. Run the DSRS with additional startup flags
   ```
   java --add-opens=java.base/java.nio=ALL-UNNAMED -jar application-all.jar your-config.yaml
   ```
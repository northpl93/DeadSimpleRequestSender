# DeadSimple*RequestSender
_*It was simple till the second commit._

DeadSimpleRequestSender is a Java application that sends HTTP requests based on data loaded from sources such as CSV file or a BigQuery table.
The project focuses on the ability to handle a big volume of data, high throughput and extensibility.

## What's inside

### Core application
* Plugins architecture
* HTTP request sending

### Data sources
Data sources are the most important plugins in the DSRS's landscape.
They're responsible for providing the data we're going to send.
* [data-source-bigquery](plugins/data-source-bigquery/README.md)
* [data-source-csv](plugins/data-source-csv/README.md)
* [data-source-buffer](plugins/data-source-buffer/README.md)
* [data-source-random](plugins/data-source-random/README.md)

### Postprocessors
Postprocessors are plugins that transform data from data sources before passing it to body factories.
* [postprocessor-constant](plugins/postprocessor-constant/README.md)

### Body factories
Body factory takes the final data row and transforms it into a request's body.
* [body-factory-inline](plugins/body-factory-inline/README.md)

## Getting started
This application requires **Java 17**.

Download the latest application & plugins from the GitHub Releases or compile it yourself.
The directory structure should look like this:
```
|-- application-all.jar
|-- your-config.yaml
|-- plugins
  |-- data-source-csv-all.jar
  |-- body-factory-inline-all.jar
  |-- (other needed plugins)
```

Prepare the job configuration file:
```yaml
data:
  # ID of the data source
  type: csv
  # The rest of the fields depend on the selected data source
  path: "P:\\Java\\DeadSimpleRequestSender\\test\\example.csv"

# It's an array, so you can have many post-processors.
# If you don't want any then provide an empty array: []
postProcessors:
    # ID of the post-processor
  - type: constant
    # The rest of the fields depend on the selected post-processor
    values: 
      language: "en-US"

executor:
  # How many threads will send requests concurrently
  threads: 8

request:
  # A URL to send requests. You can use templating here.
  url: "http://localhost:8000/<productId>"
  # An HTTP verb
  verb: POST
  # Map of headers, you can use templating here.
  headers:
    Content-Type: application/json
    Content-Language: "<language>"
  body:
    # ID of the body factory
    type: inline
    # The rest of the fields depend on the selected body factory
    template: >
      {
        "content": "<content>"
      }
```

Provide the configuration as an argument for the application:
```
java -jar application-all.jar your-config.yaml
```
When sending requests completes successfully then application exits immediately.

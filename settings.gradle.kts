rootProject.name = "DeadSimpleRequestSender"

include("api", "application")
include("plugins:data-source-csv")
include("plugins:data-source-bigquery")
include("plugins:body-factory-inline")
include("plugins:postprocessor-constant")
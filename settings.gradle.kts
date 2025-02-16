rootProject.name = "DeadSimpleRequestSender"

include("api", "application", "integration")
include("plugins:rest-api")
include("plugins:data-source-csv")
include("plugins:data-source-bigquery")
include("plugins:data-source-buffer")
include("plugins:data-source-random")
include("plugins:body-factory-inline")
include("plugins:postprocessor-constant")
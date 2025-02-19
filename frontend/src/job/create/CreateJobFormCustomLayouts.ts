import type { JsonSchema } from "@jsonforms/core";

export const customLayouts = [
  {
    tester: (jsonSchema: JsonSchema, schemaPath: string) => {
      return schemaPath === '#/properties/body' &&
        jsonSchema.properties &&
        jsonSchema.properties.type.const === 'inline'
        ? 1 : -1;
    },
    uischema: {
      type: "VerticalLayout",
      elements: [
        {
          type: "Control",
          scope: "#/properties/template",
          options: {
            multi: true
          }
        }
      ]
    }
  }
]

package pl.north93.deadsimplerequestsender.rest.schema;

import java.lang.annotation.Annotation;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.fasterxml.classmate.AnnotationInclusion;
import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.victools.jsonschema.generator.CustomDefinition;
import com.github.victools.jsonschema.generator.CustomDefinitionProviderV2;
import com.github.victools.jsonschema.generator.CustomPropertyDefinitionProvider;
import com.github.victools.jsonschema.generator.FieldScope;
import com.github.victools.jsonschema.generator.InstanceAttributeOverrideV2;
import com.github.victools.jsonschema.generator.MemberScope;
import com.github.victools.jsonschema.generator.MethodScope;
import com.github.victools.jsonschema.generator.SchemaGenerationContext;
import com.github.victools.jsonschema.generator.SchemaGeneratorConfig;
import com.github.victools.jsonschema.generator.SchemaKeyword;
import com.github.victools.jsonschema.generator.SchemaVersion;
import com.github.victools.jsonschema.generator.TypeAttributeOverrideV2;
import com.github.victools.jsonschema.generator.TypeScope;
import com.github.victools.jsonschema.generator.naming.SchemaDefinitionNamingStrategy;

final class DelegatingSchemaGeneratorConfig implements SchemaGeneratorConfig
{
    private final SchemaGeneratorConfig schemaGeneratorConfig;

    public DelegatingSchemaGeneratorConfig(final SchemaGeneratorConfig schemaGeneratorConfig)
    {
        this.schemaGeneratorConfig = schemaGeneratorConfig;
    }

    @Override
    public SchemaVersion getSchemaVersion()
    {
        return this.schemaGeneratorConfig.getSchemaVersion();
    }

    @Override
    public String getKeyword(final SchemaKeyword keyword)
    {
        if (keyword == SchemaKeyword.TAG_ANYOF)
        {
            return this.schemaGeneratorConfig.getKeyword(SchemaKeyword.TAG_ONEOF);
        }

        return this.schemaGeneratorConfig.getKeyword(keyword);
    }

    @Override
    public boolean shouldCreateDefinitionsForAllObjects()
    {
        return this.schemaGeneratorConfig.shouldCreateDefinitionsForAllObjects();
    }

    @Override
    public boolean shouldCreateDefinitionForMainSchema()
    {
        return this.schemaGeneratorConfig.shouldCreateDefinitionForMainSchema();
    }

    @Override
    public boolean shouldTransparentlyResolveSubtypesOfMembers()
    {
        return this.schemaGeneratorConfig.shouldTransparentlyResolveSubtypesOfMembers();
    }

    @Override
    public boolean shouldInlineAllSchemas()
    {
        return this.schemaGeneratorConfig.shouldInlineAllSchemas();
    }

    @Override
    public boolean shouldInlineNullableSchemas()
    {
        return this.schemaGeneratorConfig.shouldInlineNullableSchemas();
    }

    @Override
    public boolean shouldAlwaysWrapNullSchemaInAnyOf()
    {
        return this.schemaGeneratorConfig.shouldAlwaysWrapNullSchemaInAnyOf();
    }

    @Override
    public boolean shouldIncludeSchemaVersionIndicator()
    {
        return this.schemaGeneratorConfig.shouldIncludeSchemaVersionIndicator();
    }

    @Override
    public boolean shouldUsePlainDefinitionKeys()
    {
        return this.schemaGeneratorConfig.shouldUsePlainDefinitionKeys();
    }

    @Override
    public boolean shouldIncludeStandardFormatValues()
    {
        return this.schemaGeneratorConfig.shouldIncludeStandardFormatValues();
    }

    @Override
    public boolean shouldIncludeExtraOpenApiFormatValues()
    {
        return this.schemaGeneratorConfig.shouldIncludeExtraOpenApiFormatValues();
    }

    @Override
    public boolean shouldCleanupUnnecessaryAllOfElements()
    {
        return this.schemaGeneratorConfig.shouldCleanupUnnecessaryAllOfElements();
    }

    @Override
    public boolean shouldDiscardDuplicateMemberAttributes()
    {
        return this.schemaGeneratorConfig.shouldDiscardDuplicateMemberAttributes();
    }

    @Override
    public boolean shouldIncludeStrictTypeInfo()
    {
        return this.schemaGeneratorConfig.shouldIncludeStrictTypeInfo();
    }

    @Override
    public boolean shouldIncludeStaticFields()
    {
        return this.schemaGeneratorConfig.shouldIncludeStaticFields();
    }

    @Override
    public boolean shouldIncludeStaticMethods()
    {
        return this.schemaGeneratorConfig.shouldIncludeStaticMethods();
    }

    @Override
    public boolean shouldDeriveFieldsFromArgumentFreeMethods()
    {
        return this.schemaGeneratorConfig.shouldDeriveFieldsFromArgumentFreeMethods();
    }

    @Override
    public boolean shouldRepresentSingleAllowedValueAsConst()
    {
        return this.schemaGeneratorConfig.shouldRepresentSingleAllowedValueAsConst();
    }

    @Override
    public boolean shouldAllowNullableArrayItems()
    {
        return this.schemaGeneratorConfig.shouldAllowNullableArrayItems();
    }

    @Override
    public ObjectMapper getObjectMapper()
    {
        return this.schemaGeneratorConfig.getObjectMapper();
    }

    @Override
    public ObjectNode createObjectNode()
    {
        return this.schemaGeneratorConfig.createObjectNode();
    }

    @Override
    public ArrayNode createArrayNode()
    {
        return this.schemaGeneratorConfig.createArrayNode();
    }

    @Override
    public Map<Class<? extends Annotation>, AnnotationInclusion> getAnnotationInclusionOverrides()
    {
        return this.schemaGeneratorConfig.getAnnotationInclusionOverrides();
    }

    @Override
    public int sortProperties(final MemberScope<?, ?> first, final MemberScope<?, ?> second)
    {
        return this.schemaGeneratorConfig.sortProperties(first, second);
    }

    @Override
    public SchemaDefinitionNamingStrategy getDefinitionNamingStrategy()
    {
        return this.schemaGeneratorConfig.getDefinitionNamingStrategy();
    }

    @Override
    public <M extends MemberScope<?, ?>> CustomDefinition getCustomDefinition(final M scope, final SchemaGenerationContext context, final CustomPropertyDefinitionProvider<M> ignoredDefinitionProvider)
    {
        return this.schemaGeneratorConfig.getCustomDefinition(scope, context, ignoredDefinitionProvider);
    }

    @Override
    public CustomDefinition getCustomDefinition(final ResolvedType javaType, final SchemaGenerationContext context, final CustomDefinitionProviderV2 ignoredDefinitionProvider)
    {
        return this.schemaGeneratorConfig.getCustomDefinition(javaType, context, ignoredDefinitionProvider);
    }

    @Override
    public List<ResolvedType> resolveSubtypes(final ResolvedType javaType, final SchemaGenerationContext context)
    {
        return this.schemaGeneratorConfig.resolveSubtypes(javaType, context);
    }

    @Override
    public List<TypeAttributeOverrideV2> getTypeAttributeOverrides()
    {
        return this.schemaGeneratorConfig.getTypeAttributeOverrides();
    }

    @Override
    public List<InstanceAttributeOverrideV2<FieldScope>> getFieldAttributeOverrides()
    {
        return this.schemaGeneratorConfig.getFieldAttributeOverrides();
    }

    @Override
    public List<InstanceAttributeOverrideV2<MethodScope>> getMethodAttributeOverrides()
    {
        return this.schemaGeneratorConfig.getMethodAttributeOverrides();
    }

    @Override
    public boolean isNullable(final FieldScope field)
    {
        return this.schemaGeneratorConfig.isNullable(field);
    }

    @Override
    public boolean isNullable(final MethodScope method)
    {
        return this.schemaGeneratorConfig.isNullable(method);
    }

    @Override
    public boolean isRequired(final FieldScope field)
    {
        return this.schemaGeneratorConfig.isRequired(field);
    }

    @Override
    public boolean isRequired(final MethodScope method)
    {
        return this.schemaGeneratorConfig.isRequired(method);
    }

    @Override
    public boolean isReadOnly(final FieldScope field)
    {
        return this.schemaGeneratorConfig.isReadOnly(field);
    }

    @Override
    public boolean isReadOnly(final MethodScope method)
    {
        return this.schemaGeneratorConfig.isReadOnly(method);
    }

    @Override
    public boolean isWriteOnly(final FieldScope field)
    {
        return this.schemaGeneratorConfig.isWriteOnly(field);
    }

    @Override
    public boolean isWriteOnly(final MethodScope method)
    {
        return this.schemaGeneratorConfig.isWriteOnly(method);
    }

    @Override
    public boolean shouldIgnore(final FieldScope field)
    {
        return this.schemaGeneratorConfig.shouldIgnore(field);
    }

    @Override
    public boolean shouldIgnore(final MethodScope method)
    {
        return this.schemaGeneratorConfig.shouldIgnore(method);
    }

    @Deprecated
    @Override
    public ResolvedType resolveTargetTypeOverride(final FieldScope field)
    {
        return this.schemaGeneratorConfig.resolveTargetTypeOverride(field);
    }

    @Deprecated
    @Override
    public ResolvedType resolveTargetTypeOverride(final MethodScope method)
    {
        return this.schemaGeneratorConfig.resolveTargetTypeOverride(method);
    }

    @Override
    public List<ResolvedType> resolveTargetTypeOverrides(final FieldScope field)
    {
        return this.schemaGeneratorConfig.resolveTargetTypeOverrides(field);
    }

    @Override
    public List<ResolvedType> resolveTargetTypeOverrides(final MethodScope method)
    {
        return this.schemaGeneratorConfig.resolveTargetTypeOverrides(method);
    }

    @Override
    public String resolvePropertyNameOverride(final FieldScope field)
    {
        return this.schemaGeneratorConfig.resolvePropertyNameOverride(field);
    }

    @Override
    public String resolvePropertyNameOverride(final MethodScope method)
    {
        return this.schemaGeneratorConfig.resolvePropertyNameOverride(method);
    }

    @Override
    public String resolveIdForType(final TypeScope scope)
    {
        return this.schemaGeneratorConfig.resolveIdForType(scope);
    }

    @Override
    public String resolveAnchorForType(final TypeScope scope)
    {
        return this.schemaGeneratorConfig.resolveAnchorForType(scope);
    }

    @Override
    public String resolveTitle(final FieldScope field)
    {
        return this.schemaGeneratorConfig.resolveTitle(field);
    }

    @Override
    public String resolveTitle(final MethodScope method)
    {
        return this.schemaGeneratorConfig.resolveTitle(method);
    }

    @Override
    public String resolveTitleForType(final TypeScope scope)
    {
        return this.schemaGeneratorConfig.resolveTitleForType(scope);
    }

    @Override
    public String resolveDescription(final FieldScope field)
    {
        return this.schemaGeneratorConfig.resolveDescription(field);
    }

    @Override
    public String resolveDescription(final MethodScope method)
    {
        return this.schemaGeneratorConfig.resolveDescription(method);
    }

    @Override
    public String resolveDescriptionForType(final TypeScope scope)
    {
        return this.schemaGeneratorConfig.resolveDescriptionForType(scope);
    }

    @Override
    public Object resolveDefault(final FieldScope field)
    {
        return this.schemaGeneratorConfig.resolveDefault(field);
    }

    @Override
    public Object resolveDefault(final MethodScope method)
    {
        return this.schemaGeneratorConfig.resolveDefault(method);
    }

    @Override
    public Object resolveDefaultForType(final TypeScope scope)
    {
        return this.schemaGeneratorConfig.resolveDefaultForType(scope);
    }

    @Override
    public List<String> resolveDependentRequires(final FieldScope field)
    {
        return this.schemaGeneratorConfig.resolveDependentRequires(field);
    }

    @Override
    public List<String> resolveDependentRequires(final MethodScope method)
    {
        return this.schemaGeneratorConfig.resolveDependentRequires(method);
    }

    @Override
    public Collection<?> resolveEnum(final FieldScope field)
    {
        return this.schemaGeneratorConfig.resolveEnum(field);
    }

    @Override
    public Collection<?> resolveEnum(final MethodScope method)
    {
        return this.schemaGeneratorConfig.resolveEnum(method);
    }

    @Override
    public Collection<?> resolveEnumForType(final TypeScope scope)
    {
        return this.schemaGeneratorConfig.resolveEnumForType(scope);
    }

    @Override
    public JsonNode resolveAdditionalProperties(final FieldScope field, final SchemaGenerationContext context)
    {
        return this.schemaGeneratorConfig.resolveAdditionalProperties(field, context);
    }

    @Override
    public JsonNode resolveAdditionalProperties(final MethodScope method, final SchemaGenerationContext context)
    {
        return this.schemaGeneratorConfig.resolveAdditionalProperties(method, context);
    }

    @Override
    public JsonNode resolveAdditionalPropertiesForType(final TypeScope scope, final SchemaGenerationContext context)
    {
        return this.schemaGeneratorConfig.resolveAdditionalPropertiesForType(scope, context);
    }

    @Override
    public Map<String, JsonNode> resolvePatternProperties(final FieldScope field, final SchemaGenerationContext context)
    {
        return this.schemaGeneratorConfig.resolvePatternProperties(field, context);
    }

    @Override
    public Map<String, JsonNode> resolvePatternProperties(final MethodScope method, final SchemaGenerationContext context)
    {
        return this.schemaGeneratorConfig.resolvePatternProperties(method, context);
    }

    @Override
    public Map<String, JsonNode> resolvePatternPropertiesForType(final TypeScope scope, final SchemaGenerationContext context)
    {
        return this.schemaGeneratorConfig.resolvePatternPropertiesForType(scope, context);
    }

    @Override
    public Integer resolveStringMinLength(final FieldScope field)
    {
        return this.schemaGeneratorConfig.resolveStringMinLength(field);
    }

    @Override
    public Integer resolveStringMinLength(final MethodScope method)
    {
        return this.schemaGeneratorConfig.resolveStringMinLength(method);
    }

    @Override
    public Integer resolveStringMinLengthForType(final TypeScope scope)
    {
        return this.schemaGeneratorConfig.resolveStringMinLengthForType(scope);
    }

    @Override
    public Integer resolveStringMaxLength(final FieldScope field)
    {
        return this.schemaGeneratorConfig.resolveStringMaxLength(field);
    }

    @Override
    public Integer resolveStringMaxLength(final MethodScope method)
    {
        return this.schemaGeneratorConfig.resolveStringMaxLength(method);
    }

    @Override
    public Integer resolveStringMaxLengthForType(final TypeScope scope)
    {
        return this.schemaGeneratorConfig.resolveStringMaxLengthForType(scope);
    }

    @Override
    public String resolveStringFormat(final FieldScope field)
    {
        return this.schemaGeneratorConfig.resolveStringFormat(field);
    }

    @Override
    public String resolveStringFormat(final MethodScope method)
    {
        return this.schemaGeneratorConfig.resolveStringFormat(method);
    }

    @Override
    public String resolveStringFormatForType(final TypeScope scope)
    {
        return this.schemaGeneratorConfig.resolveStringFormatForType(scope);
    }

    @Override
    public String resolveStringPattern(final FieldScope field)
    {
        return this.schemaGeneratorConfig.resolveStringPattern(field);
    }

    @Override
    public String resolveStringPattern(final MethodScope method)
    {
        return this.schemaGeneratorConfig.resolveStringPattern(method);
    }

    @Override
    public String resolveStringPatternForType(final TypeScope scope)
    {
        return this.schemaGeneratorConfig.resolveStringPatternForType(scope);
    }

    @Override
    public BigDecimal resolveNumberInclusiveMinimum(final FieldScope field)
    {
        return this.schemaGeneratorConfig.resolveNumberInclusiveMinimum(field);
    }

    @Override
    public BigDecimal resolveNumberInclusiveMinimum(final MethodScope method)
    {
        return this.schemaGeneratorConfig.resolveNumberInclusiveMinimum(method);
    }

    @Override
    public BigDecimal resolveNumberInclusiveMinimumForType(final TypeScope scope)
    {
        return this.schemaGeneratorConfig.resolveNumberInclusiveMinimumForType(scope);
    }

    @Override
    public BigDecimal resolveNumberExclusiveMinimum(final FieldScope field)
    {
        return this.schemaGeneratorConfig.resolveNumberExclusiveMinimum(field);
    }

    @Override
    public BigDecimal resolveNumberExclusiveMinimum(final MethodScope method)
    {
        return this.schemaGeneratorConfig.resolveNumberExclusiveMinimum(method);
    }

    @Override
    public BigDecimal resolveNumberExclusiveMinimumForType(final TypeScope scope)
    {
        return this.schemaGeneratorConfig.resolveNumberExclusiveMinimumForType(scope);
    }

    @Override
    public BigDecimal resolveNumberInclusiveMaximum(final FieldScope field)
    {
        return this.schemaGeneratorConfig.resolveNumberInclusiveMaximum(field);
    }

    @Override
    public BigDecimal resolveNumberInclusiveMaximum(final MethodScope method)
    {
        return this.schemaGeneratorConfig.resolveNumberInclusiveMaximum(method);
    }

    @Override
    public BigDecimal resolveNumberInclusiveMaximumForType(final TypeScope scope)
    {
        return this.schemaGeneratorConfig.resolveNumberInclusiveMaximumForType(scope);
    }

    @Override
    public BigDecimal resolveNumberExclusiveMaximum(final FieldScope field)
    {
        return this.schemaGeneratorConfig.resolveNumberExclusiveMaximum(field);
    }

    @Override
    public BigDecimal resolveNumberExclusiveMaximum(final MethodScope method)
    {
        return this.schemaGeneratorConfig.resolveNumberExclusiveMaximum(method);
    }

    @Override
    public BigDecimal resolveNumberExclusiveMaximumForType(final TypeScope scope)
    {
        return this.schemaGeneratorConfig.resolveNumberExclusiveMaximumForType(scope);
    }

    @Override
    public BigDecimal resolveNumberMultipleOf(final FieldScope field)
    {
        return this.schemaGeneratorConfig.resolveNumberMultipleOf(field);
    }

    @Override
    public BigDecimal resolveNumberMultipleOf(final MethodScope method)
    {
        return this.schemaGeneratorConfig.resolveNumberMultipleOf(method);
    }

    @Override
    public BigDecimal resolveNumberMultipleOfForType(final TypeScope scope)
    {
        return this.schemaGeneratorConfig.resolveNumberMultipleOfForType(scope);
    }

    @Override
    public Integer resolveArrayMinItems(final FieldScope field)
    {
        return this.schemaGeneratorConfig.resolveArrayMinItems(field);
    }

    @Override
    public Integer resolveArrayMinItems(final MethodScope method)
    {
        return this.schemaGeneratorConfig.resolveArrayMinItems(method);
    }

    @Override
    public Integer resolveArrayMinItemsForType(final TypeScope scope)
    {
        return this.schemaGeneratorConfig.resolveArrayMinItemsForType(scope);
    }

    @Override
    public Integer resolveArrayMaxItems(final FieldScope field)
    {
        return this.schemaGeneratorConfig.resolveArrayMaxItems(field);
    }

    @Override
    public Integer resolveArrayMaxItems(final MethodScope method)
    {
        return this.schemaGeneratorConfig.resolveArrayMaxItems(method);
    }

    @Override
    public Integer resolveArrayMaxItemsForType(final TypeScope scope)
    {
        return this.schemaGeneratorConfig.resolveArrayMaxItemsForType(scope);
    }

    @Override
    public Boolean resolveArrayUniqueItems(final FieldScope field)
    {
        return this.schemaGeneratorConfig.resolveArrayUniqueItems(field);
    }

    @Override
    public Boolean resolveArrayUniqueItems(final MethodScope method)
    {
        return this.schemaGeneratorConfig.resolveArrayUniqueItems(method);
    }

    @Override
    public Boolean resolveArrayUniqueItemsForType(final TypeScope scope)
    {
        return this.schemaGeneratorConfig.resolveArrayUniqueItemsForType(scope);
    }

    @Override
    public void resetAfterSchemaGenerationFinished()
    {
        this.schemaGeneratorConfig.resetAfterSchemaGenerationFinished();
    }
}

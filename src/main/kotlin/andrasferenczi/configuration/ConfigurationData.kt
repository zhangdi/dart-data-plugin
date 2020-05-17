package andrasferenczi.configuration

import andrasferenczi.templater.TemplateConstants

// Input
// Default values
data class ConfigurationData(
    val copyWithMethodName: String,
    val toMapMethodName: String,
    val fromMapMethodName: String,
    val useRequiredAnnotation: Boolean,
    val useNewKeyword: Boolean,
    val useConstForConstructor: Boolean,
    val optimizeConstCopy: Boolean,
    val addKeyMapperForMap: Boolean,
    val noImplicitCasts: Boolean
) {
    companion object {
        val DEFAULT_DATA = ConfigurationData(
            copyWithMethodName = TemplateConstants.COPYWITH_DEFAULT_METHOD_NAME,
            toMapMethodName = TemplateConstants.TO_MAP_METHOD_NAME,
            fromMapMethodName = TemplateConstants.FROM_MAP_METHOD_NAME,
            useRequiredAnnotation = true,
            useNewKeyword = true,
            useConstForConstructor = true,
            optimizeConstCopy = false,
            addKeyMapperForMap = false,
            noImplicitCasts = true
        )

        val TEST_DATA = ConfigurationData(
            copyWithMethodName = "testData",
            toMapMethodName = TemplateConstants.TO_MAP_METHOD_NAME,
            fromMapMethodName = TemplateConstants.FROM_MAP_METHOD_NAME,
            useRequiredAnnotation = true,
            useNewKeyword = false,
            useConstForConstructor = false,
            optimizeConstCopy = false,
            addKeyMapperForMap = false,
            noImplicitCasts = true
        )

    }
}
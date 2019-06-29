package andrasferenczi.intention.utils

import org.dartlang.analysis.server.protocol.IncludedSuggestionRelevanceTag
import org.dartlang.analysis.server.protocol.IncludedSuggestionSet

data class VariableHint(
    val name: String,
    val returnType: String,
    val suggestionElementKind: DartServerCompletionUtils.SuggestionElementKind
)

data class FieldHint(
    val name: String,
    val returnType: String
)

data class NamedArgumentHint(
    val name: String,
    val returnType: String
)

/**
 * All values are calculated in offsets
 */
data class FunctionPlacementData(
    val functionNameStart: Int,
    val openBracketStart: Int,
    val closingBracketStart: Int,
    // For being able to restore the content if deleted
    // String subsequence uses different indexing from the offset
    val functionParametersContent: String
) {
    val closingBracketEnd = closingBracketStart + 1

    val parametersTextRange = openBracketStart + 1..closingBracketStart
}

data class CompletionLibraryRefData(
    val includedSet: IncludedSuggestionSet,
    val includedKinds: Set<String>,
    val includedRelevanceTags: Map<String, IncludedSuggestionRelevanceTag>
)

package andrasferenczi.intention.utils

import org.dartlang.analysis.server.protocol.CompletionSuggestion

data class CompletionSuggestionData(
    val replacementOffset: Int,
    val replacementLength: Int,
    val suggestion: CompletionSuggestion
)

fun List<CompletionSuggestion>.extractVariableHints(): List<VariableHint> {
    val variableKinds = setOf(
        DartServerCompletionUtils.SuggestionElementKind.Parameter,
        DartServerCompletionUtils.SuggestionElementKind.LocalVariable
    )

    return filterSuggestionsForValues(variableKinds) {
        VariableHint(
            it.element.name,
            it.element.returnType,
            DartServerCompletionUtils.SuggestionElementKind.textToKind[it.element.kind]
                ?: throw RuntimeException("Parameter kind is not found - some earlier check is wrong.")
        )

    }
}

fun List<CompletionSuggestion>.extractFieldHints(): List<FieldHint> {
    val fieldKinds = setOf(DartServerCompletionUtils.SuggestionElementKind.Field)

    return filterSuggestionsForValues(fieldKinds) {
        FieldHint(
            it.element.name,
            it.element.returnType
        )
    }
}

fun List<CompletionSuggestion>.extractNamedArguments(): List<NamedArgumentHint> {
    return this.asSequence()
        .filter { it.kind == DartServerCompletionUtils.SuggestionKinds.NamedArgument.kind }
        .map {
            NamedArgumentHint(
                it.parameterName,
                it.parameterType
            )
        }
        .toList()
}

private fun <T> List<CompletionSuggestion>.filterSuggestionsForValues(
    suggestionElementKinds: Set<DartServerCompletionUtils.SuggestionElementKind>,
    mapper: (completionSuggestion: CompletionSuggestion) -> T
): List<T> {
    val kinds = suggestionElementKinds.map { it.kind }

    return this.asSequence()
        // Some getter
        .filter { it.kind == DartServerCompletionUtils.SuggestionKinds.Invocation.kind }
        // Has an element referenced - needed for return value
        .filter { it.element != null }
        // Parameter type, that will be also mapped
        .filter { it.element.kind in kinds }
        // Getter needs to be called
        .filter { it.parameterTypes == null }
        .map(mapper)
        .toList()
}

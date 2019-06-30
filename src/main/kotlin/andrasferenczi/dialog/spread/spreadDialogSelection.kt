package andrasferenczi.dialog.spread

data class Selection(
    val functionParameterName: String,
    val assignedText: String
)

data class SelectionResult(
    val selections: List<Selection>
) : List<Selection> by selections

fun SpreadDialogUIData.toSelectionResult(): SelectionResult {
    return SelectionResult(
        selections = this.variables.map {
            Selection(
                it.functionNamedArgumentName,
                it.assignedValue
            )
        }
    )
}

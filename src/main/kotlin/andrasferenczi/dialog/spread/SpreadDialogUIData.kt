package andrasferenczi.dialog.spread

import andrasferenczi.intention.utils.FieldHint
import andrasferenczi.intention.utils.HierarchicalVariableHint
import andrasferenczi.intention.utils.VariableHint
import andrasferenczi.intention.utils.VariableKind

data class SpreadDialogButtonData(
    val variable: HierarchicalVariableHint
)

data class SpreadDialogVariableData(
    // If the value of the field will be added to the final code generation
    val isGenerated: Boolean,
    val functionNamedArgumentName: String,
    val variableType: String,
    val assignedValue: String,
    // If the button can set this value
    val isSettable: Boolean
)

data class SpreadDialogUIData(
    val buttons: List<SpreadDialogButtonData>,
    val variables: List<SpreadDialogVariableData>
) {

    companion object {

        val TEST_DATA = SpreadDialogUIData(
            listOf(
                SpreadDialogButtonData(
                    HierarchicalVariableHint(
                        VariableHint(
                            "x",
                            0,
                            "String",
                            VariableKind.Parameter
                        ),
                        emptyList()
                    )
                ),
                SpreadDialogButtonData(
                    HierarchicalVariableHint(
                        VariableHint(
                            "value",
                            0,
                            "SomeObject",
                            VariableKind.Parameter
                        ),
                        listOf(
                            FieldHint(
                                "x",
                                "String"
                            ),
                            FieldHint(
                                "y",
                                "int"
                            )
                        )
                    )
                ),
                SpreadDialogButtonData(
                    HierarchicalVariableHint(
                        VariableHint(
                            "what",
                            0,
                            "SomeObject",
                            VariableKind.Parameter
                        ),
                        listOf(
                            FieldHint(
                                "x",
                                "String"
                            ),
                            FieldHint(
                                "z",
                                "String"
                            )
                        )
                    )
                )
            ),
            listOf(
                SpreadDialogVariableData(
                    isGenerated = true,
                    isSettable = true,
                    assignedValue = "",
                    variableType = "int",
                    functionNamedArgumentName = "x"
                ),
                SpreadDialogVariableData(
                    isGenerated = true,
                    isSettable = true,
                    assignedValue = "",
                    variableType = "int",
                    functionNamedArgumentName = "y"
                ),
                SpreadDialogVariableData(
                    isGenerated = true,
                    isSettable = true,
                    assignedValue = "",
                    variableType = "int",
                    functionNamedArgumentName = "z"
                )
            )
        ).applyAllFieldsOrdered()

    }

}

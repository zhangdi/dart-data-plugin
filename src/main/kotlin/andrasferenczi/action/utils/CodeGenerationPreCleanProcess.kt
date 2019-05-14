package andrasferenczi.action.utils

/**
 * Existing methods might have to be deleted.
 *
 * The final caret position is determined in this process.
 */
class CodeGenerationPreCleanProcess(
    val deleteCall: (() -> Unit)?,
    val caretOffsetAfterDelete: Int?
)

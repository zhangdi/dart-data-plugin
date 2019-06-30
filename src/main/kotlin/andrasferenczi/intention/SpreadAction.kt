package andrasferenczi.intention

import andrasferenczi.action.init.ActionData
import andrasferenczi.action.init.extractCurrentElement
import andrasferenczi.action.init.tryCreateActionData
import andrasferenczi.constants.Constants
import andrasferenczi.dialog.spread.SelectionResult
import andrasferenczi.dialog.spread.SpreadDialog
import andrasferenczi.intention.utils.*
import andrasferenczi.intention.utils.DartServerCompletionUtils.getCompletions
import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.editor.Caret
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.VirtualFile
import com.jetbrains.lang.dart.analyzer.DartAnalysisServerService
import com.jetbrains.lang.dart.psi.DartFile
import org.dartlang.analysis.server.protocol.SearchResult

class SpreadAction : AnAction() {

    fun execute(project: Project, caret: Caret, file: DartFile) {
        val selectedElement = extractCurrentElement(file, caret)

        val document = FileDocumentManager.getInstance().getDocument(file.virtualFile)
            ?: throw RuntimeException("Could not get the current document")
        val originalCode = document.text

    }

    fun findElementReferences(
        project: Project,
        file: VirtualFile,
        offset: Int,
        includePotential: Boolean
    ): List<SearchResult> {
        val das = DartAnalysisServerService.getInstance(project)
        das.updateFilesContent()

        val result: MutableList<SearchResult> = ArrayList()

        das.search_findElementReferences(file, offset) { searchResult ->
            result += searchResult
        }

        return result
    }

    private fun performActionPrivate(event: AnActionEvent, actionData: ActionData) {
        val functionPlacement =
            SpreadActionFunctionUtils.extractFunctionPlacementData(actionData.dartFile, actionData.caret) ?: return

        val project = actionData.project
        val virtualFile = actionData.dartFile.virtualFile
        val document = FileDocumentManager.getInstance().getDocument(virtualFile)
            ?: throw RuntimeException("Document could not be extracted.")

        val getCompletionsForOffset =
            { offset: Int -> getCompletions(project, virtualFile, offset) }

        // Place the caret at the beginning and ask for suggestions in regards to the parameters
        val functionBeginCompletions = getCompletionsForOffset(functionPlacement.functionNameStart)
            ?: throw RuntimeException("No completions found for function")

        val variableHints = functionBeginCompletions.first.map { it.suggestion }.extractVariableHints()

        /** For all variable hints write *name*. to and ask for the server analyzer to collect all values needed **/
        // All variables in 1 depth
        val variables = variableHints.map {
            HierarchicalVariableHint(
                it,
                DocumentCompletionUtils.extractVariableInfos(
                    variableName = it,
                    document = document,
                    project = project,
                    functionPlacement = functionPlacement,
                    getCompletionsForOffset = getCompletionsForOffset
                ) ?: throw RuntimeException("No completion could be extract for variable $it")
            )
        }

        /** Clear the function params and ask for help there to list all expected arguments **/

        val callParams = DocumentCompletionUtils.extractAllFunctionCallParams(
            file = virtualFile,
            document = document,
            project = project,
            functionPlacement = functionPlacement,
            getCompletionsForOffset = getCompletionsForOffset
        ) ?: throw RuntimeException("No call parameters could be extracted.")

        /** Match the params with each other, setting up a priority **/
        val callCompletionData = CallCompletionData(
            variables = variables,
            arguments = callParams
        )

        val selection = selectFieldsWithDialog(project, callCompletionData)

        println(selection)
    }

    private fun selectFieldsWithDialog(
        project: Project,
        callCompletionData: CallCompletionData
    ): SelectionResult? {
        val dialog = SpreadDialog(project, callCompletionData)
        dialog.show()

        return if (!dialog.isOK)
            null
        else
            dialog.getDialogResult()
    }

    override fun actionPerformed(event: AnActionEvent) {
        val actionData = tryCreateActionData(event) ?: return

        try {
            performActionPrivate(event, actionData)
        } catch (e: Exception) {
            e.printStackTrace()

            Notifications.Bus.notify(
                Notification(
                    Constants.NOTIFICATION_ID,
                    "Internal error",
                    "Error occurred: ${e.message}",
                    NotificationType.ERROR
                )
            )

        }

//        // Offset it -1, to get the element on the left side of the care
//        val selectedElement = extractCurrentElement(actionData, offsetRelativeToCaret = -1)
//        val selectedReference = extractCurrentReference(actionData)
//
//        val project = actionData.project
//
//        val das = DartAnalysisServerService.getInstance(project)
//        das.updateFilesContent()
//
//
//        val file = actionData.dartFile.virtualFile
//
//        val offset = actionData.caret.offset
//        val completions = getCompletions(project, file, offset)
//
////        val references = findElementReferences(project, file, offset)
//
//        val dummyDocument = FileDocumentManager.getInstance().getDocument(file)
//        val code = dummyDocument?.text ?: return
//
//        val addedText = "value."
//        val newCode = code.substring(0 until offset) + addedText + code.substring(offset)
//        val newOffset = offset + addedText.length

//        val singleChangeId = "sdad"
//
//        project.runWriteAction(groupId = singleChangeId) {
//            dummyDocument.insertString(offset, addedText)
//        }
//
//        val newCompletions = getCompletions(project, file, offset + addedText.length)
//
//        project.runWriteAction(groupId = singleChangeId) {
//            dummyDocument.insertString(offset, "- test 2 -")
//        }
//
//        project.runWriteAction(groupId = singleChangeId) {
//            dummyDocument.insertString(offset, "- test 3 -")
//        }

        println("Performing action")
    }

    companion object {

        /**
         * To collect all temporary changes into the same group for a single undo
         */
        val SPREAD_ACTION_GROUP_ID = "andrasferenczi.spread_action"

    }
}

//fun executeCompletionContributor(
//    actionData: ActionData,
//    completionType: CompletionType,
//    invocationCount: Int
//) {
//    val contributors = CompletionContributor.forLanguage(DartLanguage.INSTANCE)
//
//    val initContext = CompletionInitializationContext(
//        actionData.editor, actionData.caret, actionData.dartFile, completionType, invocationCount
//    )
//
//    contributors[0].fillCompletionVariants()
//
//    contributors.forEach { it.beforeCompletion(initContext) }
//    contributors.forEach { it.duringCompletion(initContext) }
//    contributors.forEach { it.(initContext) }
//
//}


//        val copiedFile = LightVirtualFile(file, code, LocalTimeCounter.currentTime());
//
//        FileDocumentManagerImpl.registerDocument(dummyDocument, copiedFile)
//
//        val dummyCompletions = getCompletions(actionData.project, copiedFile, offset)

// Navigate to function and list all named argument parameters
// das.analysis_getNavigation(file, actionData.caret.offset - 1, 0)

// service.edit_getAssists(actionData.dartFile.virtualFile, actionData.caret.offset, 1)

//        val contextFile = actionData.dartFile.originalFile.virtualFile
//
//        val contextOffset = selectedElement?.textOffset ?: return
//
//        val dummyDocument = FileDocumentManager.getInstance().getDocument(contextFile) ?: return
//        val code = dummyDocument.text
//        val offset = actionData.caret.offset
//
//        val suggestions =
//            service.execution_getSuggestions(code, offset, contextFile, contextOffset, emptyList(), emptyList())

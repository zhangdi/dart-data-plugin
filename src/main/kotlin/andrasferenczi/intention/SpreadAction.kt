package andrasferenczi.intention

import andrasferenczi.action.init.extractCurrentElement
import andrasferenczi.action.init.tryCreateActionData
import andrasferenczi.intention.utils.DartServerCompletionUtils.getCompletions
import andrasferenczi.intention.utils.DocumentCompletionUtils
import andrasferenczi.intention.utils.SpreadActionFunctionUtils
import andrasferenczi.intention.utils.extractVariableHints
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.editor.Caret
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.jetbrains.lang.dart.analyzer.DartAnalysisServerService
import com.jetbrains.lang.dart.psi.DartFile
import org.dartlang.analysis.server.protocol.SearchResult

class SpreadAction : AnAction() {

//    fun getSuggestionsByCode(
//        project: Project,
//        code: String,
//        codeOffset: Int,
//        contextFile: VirtualFile,
//        contextOffset: Int
//    ): RuntimeCompletionResult? {
//        val das = DartAnalysisServerService.getInstance(project)
//        das.updateFilesContent()
//
//        return das.execution_getSuggestions(code, codeOffset, contextFile, contextOffset, emptyList(), emptyList())
//    }
//
//    fun findElementReferences(
//        project: Project,
//        file: VirtualFile,
//        offset: Int
//    ): SearchResult? {
//        val das = DartAnalysisServerService.getInstance(project)
//        das.updateFilesContent()
//
//        var result: SearchResult? = null
//
//        das.search_findElementReferences(file, offset) { searchResult ->
//            result = searchResult
//        }
//
//        return result
//    }

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

    override fun actionPerformed(event: AnActionEvent) {
        val actionData = tryCreateActionData(event) ?: return

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

        val variableHints = functionBeginCompletions?.first?.map { it.suggestion }?.extractVariableHints()

        val originalCode = document.text

//        DartAnalysisServerService.getInstance(project).also { it.updateFilesContent() }.run {
//            this.edit_getAssists(virtualFile, deleteRange.first, 0)
//        }

        /** For all variable hints write *name*. to and ask for the server analyzer to collect all values needed **/
        // All variables in 1 depth
        val variables = variableHints?.map {
            it to DocumentCompletionUtils.extractVariableInfos(
                variableName = it,
                document = document,
                project = project,
                functionPlacement = functionPlacement,
                getCompletionsForOffset = getCompletionsForOffset
            )
        }

        /** Clear the function params and ask for help there to list all expected arguments **/

        val callParams = DocumentCompletionUtils.extractAllFunctionCallParams(
            file = virtualFile,
            document = document,
            project = project,
            functionPlacement = functionPlacement,
            getCompletionsForOffset = getCompletionsForOffset
        )

        println()
        println()
        println()

        /** Match the params with each other, setting up a priority **/

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

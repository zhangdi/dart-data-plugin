package andrasferenczi.action

import andrasferenczi.action.init.ActionData
import andrasferenczi.action.utils.*
import andrasferenczi.configuration.ConfigurationDataManager
import andrasferenczi.declaration.DeclarationExtractor
import andrasferenczi.declaration.canBeAssignedFromConstructor
import andrasferenczi.declaration.variableName
import andrasferenczi.ext.evalAnchorInClass
import andrasferenczi.ext.psi.extractClassName
import andrasferenczi.ext.runWriteAction
import andrasferenczi.ext.setCaretSafe
import andrasferenczi.templater.ConstructorTemplateParams
import andrasferenczi.templater.createConstructorTemplate
import com.intellij.codeInsight.template.TemplateManager
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.psi.PsiDocumentManager
import com.jetbrains.lang.dart.psi.DartClassDefinition

class NamedArgumentConstructorAction : BaseAnAction() {

    override fun performAction(
        event: AnActionEvent,
        actionData: ActionData,
        dartClass: DartClassDefinition
    ) {

        val (project, editor, _, _) = actionData

        val dartClassName = dartClass.extractClassName()
        val declarations = DeclarationExtractor
            .extractDeclarationsFromClass(dartClass)

        val variableNames = declarations
            .filter { it.canBeAssignedFromConstructor }
            .map { it.variableName }

        val templateManager = TemplateManager.getInstance(project)

        val configuration = ConfigurationDataManager.retrieveData(project)

        val template = createConstructorTemplate(
            templateManager,
            ConstructorTemplateParams(
                className = dartClassName,
                publicVariableNames = variableNames,
                addRequiredAnnotation = configuration.useRequiredAnnotation
            )
        )

        val cleanProcess = createConstructorPreCleanProcess(project, dartClass)

        project.runWriteAction {
            cleanProcess.deleteCall?.let {
                it.invoke()

                PsiDocumentManager.getInstance(project)
                    .doPostponedOperationsAndUnblockDocument(editor.document)
            }

            val caretPosition = cleanProcess.caretOffsetAfterDelete
                ?: editor.evalAnchorInClass(dartClass).textRange.endOffset
            editor.setCaretSafe(dartClass, caretPosition)
            templateManager.startTemplate(editor, template)
        }
    }
}
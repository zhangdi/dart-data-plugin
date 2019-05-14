package andrasferenczi.action.utils

import andrasferenczi.utils.mergeCalls
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.jetbrains.lang.dart.psi.DartClass

// Try to put the constructor after the last
fun tryGetFirstNamedConstructorStart(
    dartClass: DartClass
): Int? {
    return dartClass.extractMethodConstructorInfos()
        .firstOrNull { it.constructorType == DartConstructorType.NamedParameterOnly }
        ?.psi
        ?.textRange
        ?.startOffset
}

fun tryGetLastConstructorEnd(
    dartClass: DartClass
): Int? {
    return dartClass.extractMethodConstructorInfos()
        .lastOrNull()
        ?.psi
        ?.textRange
        ?.endOffset
}

fun createConstructorPreCleanProcess(
    project: Project,
    dartClass: DartClass
): CodeGenerationPreCleanProcess {
    val additionalCalls: MutableList<() -> Unit> = ArrayList()

    val existingInfos = dartClass.extractMethodConstructorInfos()
    var caretOffsetAfterDelete: Int? = null
    if (existingInfos.isNotEmpty()) {
        val (namedConstructorInfos, otherConstructorInfos) = existingInfos.partition { it.constructorType == DartConstructorType.NamedParameterOnly }

        if (namedConstructorInfos.isNotEmpty()) {
            // Can only put it to it's start safely
            caretOffsetAfterDelete = namedConstructorInfos
                .first()
                .psi
                .textRange
                .startOffset

            additionalCalls += { namedConstructorInfos.deleteAllPsiElements() }
        }


        if (otherConstructorInfos.isNotEmpty()) {
            val shouldDeleteInt = Messages.showOkCancelDialog(
                project,
                "Constructors already exist. Do you want to the delete the existing constructors to prevent conflicts?",
                "Conflicting Constructors",
                "Delete",
                "Keep",
                null
            )

            val wantsToDelete = shouldDeleteInt == Messages.OK

            if (wantsToDelete) {
                // Would have to calculate the spacing and the formatting and lengths of the removed constructor
                // Let's stay safe
                caretOffsetAfterDelete = null

                additionalCalls += { existingInfos.deleteAllPsiElements() }
            }
        }
    }

    return CodeGenerationPreCleanProcess(
        deleteCall = additionalCalls.mergeCalls(),
        caretOffsetAfterDelete = caretOffsetAfterDelete
    )
}
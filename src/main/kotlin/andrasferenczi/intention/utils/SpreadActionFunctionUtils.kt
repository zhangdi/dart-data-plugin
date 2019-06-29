package andrasferenczi.intention.utils

import andrasferenczi.action.init.extractCurrentElement
import andrasferenczi.ext.psi.allChildren
import andrasferenczi.ext.psi.findFirstParentOfType
import com.intellij.openapi.editor.Caret
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.impl.source.tree.LeafPsiElement
import com.jetbrains.lang.dart.psi.DartArguments
import com.jetbrains.lang.dart.psi.DartCallExpression
import kotlin.test.assertEquals


object SpreadActionFunctionUtils {

    // Fast, even for toggling the intention
    fun extractFunctionPlacementData(
        file: PsiFile,
        caret: Caret
    ): FunctionPlacementData? {
        val psiElement = extractCurrentElement(file, caret, feedbackOnError = false)

        return extractFunctionPlacementData(psiElement)
    }

    fun extractFunctionPlacementData(
        psiElement: PsiElement?
    ): FunctionPlacementData? {
        if (psiElement == null) {
            return null
        }

        val argumentElem = psiElement.findFirstParentOfType<DartArguments>() ?: return null
        val callElem = argumentElem.findFirstParentOfType<DartCallExpression>() ?: return null
        val argumentChildren =
            argumentElem.allChildren()
                .filter { it.text == "(" || it.text == (")") }
                .toList()

        val openBrackets = argumentChildren.firstOrNull() ?: return null
        val closingBrackets = argumentChildren.lastOrNull() ?: return null

        if (openBrackets === closingBrackets) {
            if (openBrackets.text != "(" || closingBrackets.text != ")") {
                return null
            }
        }

        // For smart cast for the startOffset
        if (openBrackets !is LeafPsiElement || closingBrackets !is LeafPsiElement) {
            return null
        }

        // Assertions
        val argumentsStartOffset = argumentElem.textOffset
        val argumentText = argumentElem.text

        val openBracketStartOffset = openBrackets.startOffset

        val closingBracketStartOffset = closingBrackets.startOffset
        val closingBracketEndOffset = closingBracketStartOffset + 1

        assertEquals(argumentsStartOffset, openBracketStartOffset)
        assertEquals(argumentsStartOffset + argumentText.length, closingBracketEndOffset)

        val argumentInnerText = argumentText
            .substringAfter("(")
            .substringBeforeLast(")")

        assertEquals(argumentText.length - 2, argumentInnerText.length)

        return FunctionPlacementData(
            functionNameStart = callElem.textOffset,
            // Because the open bracket seems to have it's offset in the inner side
            openBracketStart = openBracketStartOffset,
            closingBracketStart = closingBracketStartOffset,
            functionParametersContent = argumentInnerText
        )
    }
}

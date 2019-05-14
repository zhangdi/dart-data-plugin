package andrasferenczi._test

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.psi.PsiManager
import java.io.File

/**
 * Manual automated test
 */
class TesterAction : AnAction() {

    fun setup(currentPath: String) {


    }

    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project ?: throw RuntimeException("No Project")
        val editor = event.getData(PlatformDataKeys.EDITOR) ?: throw RuntimeException("No editor")
        val psiFile = event.getData(CommonDataKeys.PSI_FILE) ?: throw RuntimeException("No psiFile")
        val virtualFile =
            event.getData(CommonDataKeys.VIRTUAL_FILE) ?: throw java.lang.RuntimeException("No virtual file")

        val psiManager = PsiManager.getInstance(project)

        val path = virtualFile.path
        val file = File(path)

        val src = TestFileUtils.findDartProjectSrcDirectoryFromChild(file)
            ?: throw RuntimeException("No src file found")
        val testCases = TestFileUtils.extractTestCasesFromSource(file)
        TestFileUtils.clearTestBenchAndCopyStartFilesToTestBench(src, testCases)

        // LocalFileSystem.getInstance().find


//        psiManager.findFile()
//
//        val file =

//        ClassFileViewProvider(psiManager,
//            "C:\\Users\\Meee3\\IdeaProjects\\dart-data-plugin\\test-dart-project\\lib\\src\\test_1\\expected.dart")


    }
//
//    override fun performAction(
//        event: AnActionEvent,
//        actionData: ActionData,
//        dartClass: DartClassDefinition
//    ) {
//
//        val (project, editor, dartFile, caret) = actionData
//
//        PsiManager.getInstance()
//
//        ClassFileViewProvider(PsiManager.getInstance())
//
//        FileDocumentManager.getInstance().getDocument()?.FileVi
//
//    }
}
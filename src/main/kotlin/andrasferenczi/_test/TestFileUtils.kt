package andrasferenczi._test

import org.apache.commons.io.FileUtils
import java.io.File
import java.lang.RuntimeException

const val DART_FILE_TYPE = "dart"
const val DART_FILE_EXTENSION = ".$DART_FILE_TYPE"

const val EXPECTED_TESTFILE_NAME_PREFIX = "expected_"

const val START_TESTFILE_NAME = "start.dart"

const val TESTFOLDER_PREFIX = "test_"

const val TESTBENCH_FOLDER_NAME = "testbench"

const val TESTBENCH_START_FILE_PREFIX = "start"

private fun File.extractTestActionTypeString(): String? {
    val name = name
    if (!name.startsWith(EXPECTED_TESTFILE_NAME_PREFIX) || !name.endsWith(DART_FILE_EXTENSION)) {
        return null
    }

    return name
        .substringBeforeLast(DART_FILE_EXTENSION)
        .substringAfter(EXPECTED_TESTFILE_NAME_PREFIX)
}

enum class TestAction(val fileNamePostFix: String) {
    NamedArgumentConstructor("constructor"),
    CopyFunction("copy");

    val dartFileName: String =
        EXPECTED_TESTFILE_NAME_PREFIX + this.fileNamePostFix + DART_FILE_EXTENSION

    companion object {
        val map: Map<String, TestAction> = TestAction.values().associateBy { it.fileNamePostFix }
    }
}

class TestCase(
    val index: Int,
    val folder: File,
    val actions: List<TestAction>
) {

    fun getExpectedFile(testAction: TestAction): File? {
        return folder.listFiles()?.firstOrNull { it.name == testAction.dartFileName }
    }

    fun getStartFile(): File? {
        return folder.listFiles()?.firstOrNull { it.name == START_TESTFILE_NAME }
    }

}

fun listFilesNullFail(): Nothing = throw RuntimeException("List files was null")

object TestFileUtils {

    fun findDartProjectSrcDirectoryFromChild(projectFile: File): File? {
        return projectFile.findFirstParentFile {
            it.isDirectory && it.name == "src" && it.parentFile.name == "lib"
        }
    }

    fun extractTestBenchDirectory(src: File): File {
        val testBenchFile = File(src, TESTBENCH_FOLDER_NAME)

        if (!testBenchFile.exists()) {
            if (!testBenchFile.mkdir()) {
                throw RuntimeException("Could not create test bench folder " + testBenchFile.absolutePath)
            }
        }

        return testBenchFile
    }

    fun extractTestCasesFromSource(src: File): List<TestCase> {
        return src
            .listFiles()
            ?.filter { it.name.startsWith(TESTFOLDER_PREFIX) }
            ?.map { folder ->
                TestCase(
                    folder = folder,
                    index = folder.name.substringAfter(TESTFOLDER_PREFIX).toIntOrNull()
                        ?: throw RuntimeException("File name ${folder.name} does not match format ${TESTFOLDER_PREFIX}_X"),
                    actions = folder.listFiles()
                        ?.mapNotNull { it.extractTestActionTypeString() }
                        ?.map {
                            TestAction.map[it]
                                ?: throw RuntimeException("The test action of the expected filename $it could not be extracted.")
                        }
                        ?: listFilesNullFail()
                )
            }
            ?: listFilesNullFail()
    }

    fun clearTestBenchAndCopyStartFilesToTestBench(src: File, testCases: List<TestCase>) {
        val testBenchDirectory = extractTestBenchDirectory(src)

        val deleteAll = testBenchDirectory
            .listFiles()
            ?.all { it.delete() }
            ?: listFilesNullFail()

        if (!deleteAll) {
            throw RuntimeException("Could not delete all files in test bench")
        }

        testCases
            .map { it.getStartFile() ?: throw RuntimeException("No start file found in $it") }
            .forEach { FileUtils.copyFileToDirectory(it, testBenchDirectory) }
    }

}
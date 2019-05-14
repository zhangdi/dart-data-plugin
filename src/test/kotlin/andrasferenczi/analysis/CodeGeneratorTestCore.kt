//package andrasferenczi.analysis
//
//import com.intellij.codeInsight.CodeInsightTestCase
//import com.intellij.openapi.projectRoots.Sdk
//import com.intellij.testFramework.LightProjectDescriptor
//import com.intellij.testFramework.fixtures.*
//import com.jetbrains.lang.dart.sdk.DartSdk
//
//class CodeGeneratorTestCore: LightPlatformCodeInsightFixtureTestCase {
//
//    lateinit var fixture: CodeInsightTestFixture
//
//    fun doSetUp() {
//        this.setUp()
//
//        val fixtureFactory = IdeaTestFixtureFactory.getFixtureFactory()
//        val builder = fixtureFactory.createFixtureBuilder("test")
//
//        fixture = fixtureFactory.createCodeInsightFixture(builder.fixture)
//    }
//
//    fun doTearDown() {
//        this.tearDown()
//    }
//
//
//
//    fun doRunTest() {
//        println("Run test")
//    }
//
//}
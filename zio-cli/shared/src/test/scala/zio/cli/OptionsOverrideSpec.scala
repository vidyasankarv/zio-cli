package zio.cli

import zio.test.Assertion.equalTo
import zio.test.{Spec, TestEnvironment, ZIOSpecDefault, assertZIO}
import zio.{Scope, ZIO}

import java.nio.file.StandardOpenOption

object OptionsOverrideSpec extends ZIOSpecDefault {


  override def spec: Spec[TestEnvironment with Scope, Any] = suite("OptionsOverride Spec")(

    test("Should validate successfully") {
      val tempDirPath = java.nio.file.Files.createTempDirectory("my-test")
      val fileOverRideConfig = "-A 2" + System.lineSeparator() + "-B=3"
      for {
        tempDir <- ZIO.attempt(tempDirPath)
        tempFile <- ZIO.attempt(java.nio.file.Files.createTempFile(tempDir, ".", Ag.CommandName))
        _ <- ZIO.attempt(java.nio.file.Files.write(tempFile, fileOverRideConfig.getBytes(), StandardOpenOption.WRITE))
      } yield()
       assertZIO(ZIO.attempt(OptionsOverride.checkForDotFiles(Ag.CommandName)))(
        equalTo(List((tempDirPath, 0), (tempDirPath.getParent, 1)))
      )
    }
  )


  object Ag {
    val CommandName = "grep"
    val afterFlag: Options[BigInt] = Options.integer("after").alias("A")
    val beforeFlag: Options[BigInt] = Options.integer("before").alias("B")

    val options = afterFlag ++ beforeFlag

    val args = Args.text

    val command = Command(CommandName, options, args)
  }
}

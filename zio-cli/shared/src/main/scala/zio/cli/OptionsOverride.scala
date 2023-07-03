package zio.cli

import scala.annotation.tailrec
import scala.jdk.CollectionConverters._
object OptionsOverride extends App{

        import java.nio.file.{FileSystems, Files, Path}

        def checkForDotFiles(name: String)= {
                val cwd = FileSystems.getDefault.getPath(System.getProperty("user.dir"))
                val home = FileSystems.getDefault.getPath(System.getProperty("user.home"))
                val workingDirTree = getCurrentWorkingDirTree (cwd)
                println(name)
                val configDirsWithPriority = (home :: workingDirTree).reverse.zipWithIndex
                configDirsWithPriority.map( dir => (dir._1.resolve(s".$name"), dir._2))
                  .filter( dir => Files.exists({dir._1}))
                  .map( dir => (FileOverrideConfig(Files.readAllLines(dir._1).asScala.toList, dir._2)))
        }

        case class FileOverrideConfig(config: List[String], priority: Int)
        private def getCurrentWorkingDirTree(path: Path): List[Path] = {
                @tailrec
                def loop(path: Path, pathsToSearch: List[Path]): List[Path] = {
                        if(Files.isDirectory(path) ) {
                        val parent = path.getParent
                         if(parent == null) pathsToSearch
                         else loop(parent, parent :: pathsToSearch)
                        } else {
                         pathsToSearch
                        }
                }
                loop(path, List())
        }

  private val configDirs = checkForDotFiles("wc")
  configDirs.foreach(println)
}
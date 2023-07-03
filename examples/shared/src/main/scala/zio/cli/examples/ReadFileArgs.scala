package zio.cli.examples

import scala.annotation.tailrec

object ReadFileArgs extends App{

        import java.nio.file.{FileSystems, Files, Path}

        private def checkForDotFiles(name: String): Unit = {
                val cwd = FileSystems.getDefault.getPath(System.getProperty("user.dir"))
                val home = FileSystems.getDefault.getPath(System.getProperty("user.home"))
                println(name)
                val workingDirTree = getCurrentWorkingDirTree (cwd)
                val pathsToCheck = (home :: workingDirTree).zipWithIndex
                pathsToCheck.foreach(println)
        }

/*        private def checkDirectoryForDotFiles(directoryWithPriority: (Path,Int), name : String): Option[(Path, Int)] = {
                val dotFile = directoryWithPriority._1.resolve(s".$name")
                if (Files.exists(dotFile)) {
                        Some((dotFile, directoryWithPriority._2))
                }else None
        }*/

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
        // Usage
        checkForDotFiles("wc")
}
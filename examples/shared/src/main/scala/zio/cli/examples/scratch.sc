import java.nio.file.FileSystems

val cwd = FileSystems.getDefault.getPath(System.getProperty("user.dir"))
val userHome = FileSystems.getDefault.getPath(System.getProperty("user.home"))
println(cwd)
println(userHome)

java.nio.file.FileSystems.getDefault.getRootDirectories
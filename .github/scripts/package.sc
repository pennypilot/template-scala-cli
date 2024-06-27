//> using scala 3.3
//> using dep com.lihaoyi::os-lib:0.10.1
import scala.util.Properties

val platformSuffix: String =
  if Properties.isWin then "pc-win32"
  else if Properties.isLinux then "pc-linux"
  else if Properties.isMac then
    sys.props("os.arch") match
      case "aarch64" => "apple-darwin-arm64"
      case "x86_64"  => "apple-darwin-x86_64"
      case other     => sys.error(s"Unrecognized Mac architecture: $other")
  else sys.error(s"Unrecognized OS: ${sys.props("os.name")}")

val artifactsPath = os.Path("artifacts", os.pwd)
val destPath =
  if Properties.isWin then artifactsPath / s"ls-$platformSuffix.exe"
  else artifactsPath / s"ls-$platformSuffix"
val scalaCLILauncher =
  if Properties.isWin then "scala-cli.bat" else "scala-cli"

os.makeDir(artifactsPath)
os.proc(scalaCLILauncher,"--power",  "package", ".", "-o", destPath, "--native-image")
  .call(cwd = os.pwd)
  .out
  .text()
  .trim
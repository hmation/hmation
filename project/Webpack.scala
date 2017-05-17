import java.net.InetSocketAddress

import play.sbt.PlayRunHook
import sbt._

import scala.reflect.io.Path.jfile2path

object Webpack {
  def apply(base: File): PlayRunHook = {
    object WebpackHook extends PlayRunHook {
      var process: Option[Process] = None

      val frontendAppSources = base / "src" / "main" / "frontend"

      override def beforeStarted() = {
        if (!(frontendAppSources / "node_modules").exists() || (frontendAppSources / "node_modules").isEmpty)
          if(System.getProperty("os.name").toUpperCase().indexOf("WIN") >= 0) {
            Process("cmd /c npm install", frontendAppSources).run
          } else {
            Process("npm install", frontendAppSources).run
          }
      }

      override def afterStarted(addr: InetSocketAddress) = {
        process = Some(
          if(System.getProperty("os.name").toUpperCase().indexOf("WIN") >= 0) {
            Process("cmd /c node node_modules/webpack/bin/webpack.js --watch", frontendAppSources).run
          } else {
            Process("node node_modules/webpack/bin/webpack.js --watch", frontendAppSources).run
          }
        )

        /*process = Option(
          Process("webpack" + sys.props.get("os.name").filter(_.toLowerCase.contains("windows")).map(_ => ".cmd").getOrElse("") + " --watch", base).run()
//          Process("webpack-dev-server" + sys.props.get("os.name").filter(_.toLowerCase.contains("windows")).map(_ => ".cmd").getOrElse("") + " --inline --hot", base).run()
        )*/
      }

      override def afterStopped() = {
        process.foreach { proc =>
          println("STOPPING node process?")
          proc.destroy()
        }
        process = None
      }
    }

    WebpackHook
  }
}
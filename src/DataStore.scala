import java.io.FileOutputStream
import java.net.URL
import java.nio.charset.Charset

import org.apache.commons.io.IOUtils

/**
 * TODO: Describe type
 */
class DataStore {
  val root = "C:\\cygwin\\home\\ozan\\scalper-data\\html"

  def writeHtml(url: String, id: String): Unit = {
    IOUtils
      .write(IOUtils.toByteArray(new URL(url).openStream()),
        new FileOutputStream(localHtmlPath(id)))
  }

  def localHtmlPath(id: String): String = {
    root + "\\" + id + ".html"
  }
}

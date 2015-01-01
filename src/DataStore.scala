import java.io.{FileInputStream, FileOutputStream}
import java.net.URL
import java.nio.charset.Charset
import java.nio.file.{Paths, Files}
import org.apache.commons.io.IOUtils
import java.io.StringReader
import java.net.URL
import java.nio.charset.Charset

import nu.validator.htmlparser.common.XmlViolationPolicy
import nu.validator.htmlparser.sax.HtmlParser
import org.apache.commons.io.IOUtils
import org.xml.sax.InputSource

import scala.xml.Node
import scala.xml.parsing.NoBindingFactoryAdapter
/**
 * TODO: Describe type
 */
class Concert(val title: String, val date: String, val time: String, val
program: String) {
  override def toString: String = {
    "%s\n%s, %s\n%s".format(title, date, time, program)
  }
}

class DataStore {
  val root = "C:\\cygwin\\home\\ozan\\scalper-data\\html"
  val baseUrl = "http://www.ongakunotomo.co.jp/concert/detail.php?id="

  def writeHtml(url: String, id: String): Unit = {
    println("Fetching url " + url)
    IOUtils.write(IOUtils.toByteArray(new URL(url).openStream()), new
        FileOutputStream(localHtmlPath(id)))
  }

  def localHtmlPath(id: String): String = {
    root + "\\" + id + ".html"
  }

  def toNode(str: String): Node = {
    val parser = new HtmlParser
    parser.setNamePolicy(XmlViolationPolicy.ALLOW)

    val saxer = new NoBindingFactoryAdapter
    parser.setContentHandler(saxer)
    parser.parse(new InputSource(new StringReader(str)))

    saxer.rootElem
  }

  def lookup(id: String): Concert = {
    if (!Files.exists(Paths.get(localHtmlPath(id)))) {
      writeHtml(baseUrl + id, id)
    }
    val bodyString = IOUtils.toString(new FileInputStream(localHtmlPath(id)),
      Charset.forName("EUC-JP"))
    val node = toNode(bodyString)
    val title = ((node \\ "table" \ "tbody" \ "tr")(1) \\ "h1").text
    val date = (node \\ "table" \ "tbody" \\ "p" \ "span").text
    val time = (node \\ "table" \ "tbody" \\ "p").text
    val program = (((node \\ "table" \ "tbody" \\ "tr")(3) \ "td")(3) \ "h2").text
    new Concert(title, date, time, program)
  }
}

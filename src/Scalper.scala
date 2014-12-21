import java.io.StringReader
import java.net.URL
import java.nio.charset.Charset

import nu.validator.htmlparser.common.XmlViolationPolicy
import nu.validator.htmlparser.sax.HtmlParser
import org.apache.commons.io.IOUtils
import org.xml.sax.InputSource

import scala.xml.Node
import scala.xml.parsing.NoBindingFactoryAdapter

object Scalper {
  def main(args: Array[String]) {
    val baseUrl = "http://www.ongakunotomo.co.jp/concert/detail.php?id="
    val dataStore = new DataStore
    (80100 until 80200).map((x) => {
      dataStore.writeHtml(baseUrl + x.toString,
        x.toString)
    })

    /*
    val bodyString = IOUtils
      .toString(new URL(testUrl).openStream(), Charset.forName("EUC-JP"))

    val node = toNode(bodyString)
    println("Blah")
    val mojibake = (node \\ "table" \ "tbody" \\ "p" \ "span").text
    println(mojibake)
    */
  }

  def toNode(str: String): Node = {
    val parser = new HtmlParser
    parser.setNamePolicy(XmlViolationPolicy.ALLOW)

    val saxer = new NoBindingFactoryAdapter
    parser.setContentHandler(saxer)
    parser.parse(new InputSource(new StringReader(str)))

    saxer.rootElem
  }
}


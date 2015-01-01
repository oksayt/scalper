import java.io.StringReader

import nu.validator.htmlparser.common.XmlViolationPolicy
import nu.validator.htmlparser.sax.HtmlParser
import org.xml.sax.InputSource

import scala.xml.Node
import scala.xml.parsing.NoBindingFactoryAdapter

object Scalper {
  def main(args: Array[String]) {
    val dataStore = new DataStore

    sealed trait Command {def name: String}
    case object LOOKUP extends Command {val name = "lookup"}

    val commands = Map() ++ (Vector(LOOKUP) map (t => t.name -> t))

    type OptionMap = Map[Symbol, Any]
    def parseOptions(list: List[String], parsed: Map[Symbol, Any]):
    Map[Symbol, Any] = {
      list match {
      case Nil => parsed
      case "--command" :: value :: tail =>
        parseOptions(tail, parsed ++ Map('command -> commands.get(value)))
      case "--id" :: value :: tail =>
        parseOptions(tail, parsed ++ Map('id -> value))
      case "--delay_ms" :: value :: tail =>
        parseOptions(tail, parsed ++ Map('delay_ms -> value.toInt))
      case option :: tail => throw new IllegalArgumentException("Unknown " +
        "option: " + option)
      }
    }
    val options = parseOptions(args.toList, Map('delay_ms -> 500))
    println(options)
    if (!(options contains 'command)) {
      throw new IllegalArgumentException("Missing or unknown --command, known" +
        " commands are: " + (commands.keySet mkString ", "))
    }

    options.get('command).get match {
    case Some(LOOKUP) => println(dataStore.lookup(options.get('id).get
      .asInstanceOf[String]))
    }

  }

  /*
  val baseUrl = "http://www.ongakunotomo.co.jp/concert/detail.php?id="
  val dataStore = new DataStore
  (80100 until 80200).map((x) => {
    dataStore.writeHtml(baseUrl + x.toString,
      x.toString)
  })
  */
  /*
  val bodyString = IOUtils
    .toString(new URL(testUrl).openStream(), Charset.forName("EUC-JP"))

  val node = toNode(bodyString)
  println("Blah")
  val mojibake = (node \\ "table" \ "tbody" \\ "p" \ "span").text
  println(mojibake)
  */
  def toNode(str: String): Node = {
    val parser = new HtmlParser
    parser.setNamePolicy(XmlViolationPolicy.ALLOW)

    val saxer = new NoBindingFactoryAdapter
    parser.setContentHandler(saxer)
    parser.parse(new InputSource(new StringReader(str)))

    saxer.rootElem
  }
}


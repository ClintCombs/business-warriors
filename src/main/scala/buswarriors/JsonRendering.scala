
package buswarriors

import org.json4s._
import org.json4s.JsonDSL._
import org.json4s.jackson.JsonMethods._
import spray.httpx.Json4sJacksonSupport
import org.json4s.jackson.Serialization.{read, write}

trait JsonRendering extends Json4sJacksonSupport {

  import FieldSerializer._

  val productInventorySerializer =
    FieldSerializer[ProductInventory](renameTo("quantity", "qty"))
  val transactionSerializer =
    FieldSerializer[Transaction](renameTo("quantity", "qty"))

  implicit override def json4sJacksonFormats =
    DefaultFormats + productInventorySerializer + transactionSerializer

  def renderRoot: String = {
    pretty(("system" -> "business-warriors"))
  }

  def renderProduct(p: Product): String = {
    write(p)
  }

  def renderProducts(products: Product *): String = {
    write(products)
  }

  def renderInventory(pi: ProductInventory): String = {
    write(pi)
  }

  def renderInventoryList(pi: ProductInventory *): String = {
    write(pi)
  }
}

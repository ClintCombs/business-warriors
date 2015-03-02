
package buswarriors

import org.json4s._
import org.json4s.JsonDSL._
import org.json4s.jackson.JsonMethods._
import spray.httpx.Json4sJacksonSupport
import org.json4s.jackson.Serialization.{read, write}

trait JsonRendering extends Json4sJacksonSupport {

  import FieldSerializer._

  val productInventorySerializer =
    FieldSerializer[ProductInventory](renameTo("quantity", "qty"), renameFrom("qty", "quantity"))
  val transactionSerializer =
    FieldSerializer[Transaction](renameTo("quantity", "qty"), renameFrom("qty", "quantity"))
  class PriceSerializer extends CustomSerializer[Price](format => (
      { case JDecimal(d) => new Price(d) }, { case price: Price => JDecimal(price.p) }
    ))
  class QuantitySerializer extends CustomSerializer[Quantity](format => (
      { case JInt(i) => new Quantity(i.toInt) }, { case quantity: Quantity => JInt(quantity.q) }
    ))
  class ProductInventorySerializer extends CustomSerializer[ProductInventory](format => (
      {
        case JObject(
            JField("sku", JString(s)) :: JField("name", JString(n)) ::
              JField("price", JDecimal(p)) :: JField("qty", JInt(q)) :: Nil) =>
          ProductInventory(Product(s, n, p), q)
      },
      {
        case pi: ProductInventory =>
          JObject(
            JField("sku", JString(pi.product.sku)) :: JField("name", JString(pi.product.name)) ::
              JField("price", JDecimal(pi.product.price.p)) :: JField("qty", JInt(pi.quantity.q)) :: Nil)
      }
    ))

  implicit override def json4sJacksonFormats: Formats =
    DefaultFormats +
      productInventorySerializer + transactionSerializer +
      new PriceSerializer() + new QuantitySerializer() + new ProductInventorySerializer()

  def renderRoot: String = {
    pretty("system" -> "business-warriors")
  }

  def renderProduct(p: Product): String = {
    write(p)
  }

  def renderProducts(products: Seq[Product]): String = {
    write(products)
  }

  def renderInventory(pi: ProductInventory): String = {
    write(pi)
  }

  def renderInventoryList(pi: ProductInventory *): String = {
    write(pi)
  }
}

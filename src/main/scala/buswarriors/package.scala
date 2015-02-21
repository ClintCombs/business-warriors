
package object buswarriors {
  case class Product(sku: String, name: String, price: BigDecimal)
  case class ProductInventory(product: Product, quantity: Int)
  case class Transaction(product: Product, quantity: Int, total: BigDecimal)
}

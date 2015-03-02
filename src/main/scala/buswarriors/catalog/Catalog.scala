
package buswarriors.catalog

import java.io.{BufferedInputStream, InputStreamReader}

import buswarriors.{Product => BWProduct}
import com.github.marklister.collections.io.CsvParser

trait Catalog {
  def getProduct(sku: String): Option[BWProduct]
  def allProducts: Seq[BWProduct]
}

trait InMemoryCatalog extends Catalog {
  def catalogMap: Map[String, BWProduct]
  def getProduct(sku: String): Option[BWProduct] = catalogMap.get(sku)
  def allProducts: Seq[BWProduct] = catalogMap.values.toList
}

object CSVCatalogLoader {
  def load(path: String): Catalog = {
    val bis = new BufferedInputStream(CSVCatalogLoader.getClass().getClassLoader().getResourceAsStream(path))
    val i = CsvParser[String, String, Double, Int].parse(new InputStreamReader(bis), hasHeader = true).iterator
    val products: Seq[BWProduct] =
      (i.toList).filter { row => row._1.trim != "sku" } map { row => BWProduct(sku = row._1, name = row._2, price = row._3) }
    bis.close()

    val productMap: Map[String, BWProduct] =
      (products.groupBy { p => p.sku }).map { p => (p._1, p._2(0)) }

    new InMemoryCatalog {
      override val catalogMap: Map[String, BWProduct] = productMap
    }
  }
}

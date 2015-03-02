
package buswarriors.catalog.specs

import buswarriors._
import buswarriors.catalog.CSVCatalogLoader
import buswarriors.specs.BusinessWarriorsSpec
import org.scalatest.prop.TableDrivenPropertyChecks._

class CSVCatalogLoaderSpec extends BusinessWarriorsSpec {
  "CSVCatalogLoader" should {
    "load the catalog" in {
      CSVCatalogLoader.load("products.csv") must not be null
    }

    "load the correct number of products in the catalog" in {
      val catalog = CSVCatalogLoader.load("products.csv")
      catalog.allProducts.size mustBe 6
    }

    "load the products and index them by the proper sku" in {
      val catalog = CSVCatalogLoader.load("products.csv")
      val t = Table(
        ("sku", "name", "price"),
        ("1234", "Sound powered phone pattery", 11.99),
        ("6061184", "LEGO Technic 42030 Remote Controlled VOLVO L350F Wheel Load", 274.95),
        ("6025223", "LEGO Technic 42009 Mobile Crane MK II", 219.99),
        ("324843-1040", "Bose� A20 Aviation Headset", 1095.00),
        ("CAT-10020", "California Air Tools CAT-10020 Ultra Quiet and Oil-Free 2.0 HP 10.0-Gallon Steel Tank Air Compressor", 399.00),
        ("FX-1953", "Flux Capacitor", 234718.22)
      )

      forAll(t) { d: (String, String, Double) =>
        val pOpt = catalog.getProduct(d._1)
        pOpt must not be None
        val p = pOpt.get
        p.sku mustBe d._1
        p.name mustBe d._2
        p.price mustBe Price(d._3)
      }
    }
  }
}

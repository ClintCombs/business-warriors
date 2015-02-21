
package buswarriors

import com.codahale.metrics.{JmxReporter, MetricRegistry, Gauge}

package object metrics {

  def functionGauge[T](f: => () => T): Gauge[T] = new Gauge[T] {()
    def getValue(): T = f()
  }

  class Jmx(registry: MetricRegistry) {
    private val reporter = JmxReporter.forRegistry(registry).build()

    def start(): Unit = reporter.start()
    def stop(): Unit = reporter.stop()
  }

  trait Metrics {
    def registry: MetricRegistry
    def metricName(klass: Class[_], names: String*) = MetricRegistry.name(klass, names: _*)
  }
}

package testcase.kafka

import java.util.{Properties, UUID}
import java.util.concurrent.Executors

import com.typesafe.scalalogging.LazyLogging
import org.apache.kafka.clients.consumer.{ConsumerConfig, KafkaConsumer}
import testcase.exchange.Bid

import scala.collection.JavaConverters._

object BidConsumer extends LazyLogging {
  private val consumer = new KafkaConsumer[String, String](consumerConfig())

  def main(args: Array[String]): Unit = {
    run()
    logger.info("Listener started")
  }

  def consumerConfig(): Properties = {
    val properties = new Properties()
    properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
    properties.put(ConsumerConfig.GROUP_ID_CONFIG, "1")
    properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true")
    properties.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000")
    properties.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000")
    properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer")
    properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer")
    properties
  }

  def run(): Unit = {
    consumer.subscribe(Seq("bids").asJavaCollection)

    Executors.newSingleThreadExecutor.execute(() => {
      while (true) {
        val records = consumer.poll(1000)

        records.forEach { record =>
          logger.info("Received {}:{}", record.key(), record.value())
        }
      }
    })
  }
}

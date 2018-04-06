package testcase.exchange

import com.typesafe.scalalogging.LazyLogging
import testcase.exchange.BidTypes._

object Main extends LazyLogging {
  def main(args: Array[String]): Unit = {
    val exchange = new Exchange
    exchange.add(Bid(Buy, 100, 10))
    exchange.add(Bid(Buy, 100, 30))
    exchange.add(Bid(Buy, 100, 20))
    exchange.add(Bid(Buy, 100, 40))
    exchange.add(Bid(Sell, 100, 30))
    exchange.add(Bid(Sell, 100, 20))
    exchange.add(Bid(Sell, 100, 40))
    exchange.add(Bid(Sell, 100, 10))

    logger.info("Best price and amount {}", exchange.calculate())
  }
}

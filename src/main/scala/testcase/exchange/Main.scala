package testcase.exchange

import testcase.exchange.BidTypes._

object Main {
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
    exchange.calculate()
  }
}

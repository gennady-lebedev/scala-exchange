package testcase.exchange

import testcase.exchange.BidTypes._

object Main {
  def main(args: Array[String]): Unit = {
    val exchange = new Exchange
    exchange.add(Bid(Buy, 10, 100))
    exchange.add(Bid(Buy, 30, 100))
    exchange.add(Bid(Buy, 20, 100))
    exchange.add(Bid(Buy, 40, 100))
    exchange.add(Bid(Sell, 30, 100))
    exchange.add(Bid(Sell, 20, 100))
    exchange.add(Bid(Sell, 40, 100))
    exchange.add(Bid(Sell, 10, 100))
    exchange.calculate()
  }
}

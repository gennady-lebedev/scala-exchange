package testcase.exchange

import org.scalatest.{Matchers, WordSpec}
import testcase.exchange.BidTypes._

class ExchangeTest extends WordSpec with Matchers {
  "exchange should match pair of bids with same price and amount" in {
    val exchange = new Exchange
    exchange.add(Bid(Buy, 100, 100))
    exchange.add(Bid(Sell, 100, 100))
    exchange.calculate() should be (100, 100)
  }

  "exchange should match couple of bids with same prices and amounts" in {
    val exchange = new Exchange
    exchange.add(Bid(Buy, 100, 100))
    exchange.add(Bid(Buy, 100, 100))
    exchange.add(Bid(Buy, 100, 100))
    exchange.add(Bid(Sell, 100, 100))
    exchange.add(Bid(Sell, 100, 100))
    exchange.add(Bid(Sell, 100, 100))
    exchange.calculate() should be (100, 300)
  }

  "exchange should ignore not optimal prices" in {
    val exchange = new Exchange
    exchange.add(Bid(Buy, 100, 50))
    exchange.add(Bid(Buy, 100, 10))
    exchange.add(Bid(Sell, 100, 90))
    exchange.add(Bid(Sell, 100, 50))
    exchange.calculate() should be (50, 100)
  }
}

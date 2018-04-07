package testcase.exchange

import java.util.concurrent.ThreadLocalRandom

import org.scalatest.{Matchers, WordSpec}
import testcase.exchange.BidTypes._

class ExchangeTest extends WordSpec with Matchers {
  "match pair of bids with same price and amount" in {
    val exchange = new Exchange
    exchange.add(Bid(Buy, 100, 100))
    exchange.add(Bid(Sell, 100, 100))
    exchange.calculate() should be ("100 1.0")
  }

  "fail if bids doesn't match by price" in {
    val exchange = new Exchange
    exchange.add(Bid(Buy, 100, 10.00))
    exchange.add(Bid(Sell, 100, 100.00))
    exchange.calculate() should be ("0 n/a")
  }

  "match couple of bids with same prices and amounts" in {
    val exchange = new Exchange
    exchange.add(Bid(Buy, 100, 100))
    exchange.add(Bid(Buy, 100, 100))
    exchange.add(Bid(Buy, 100, 100))
    exchange.add(Bid(Sell, 100, 100))
    exchange.add(Bid(Sell, 100, 100))
    exchange.add(Bid(Sell, 100, 100))
    exchange.calculate() should be ("300 1.0")
  }

  "ignore not optimal prices" in {
    val exchange = new Exchange
    exchange.add(Bid(Buy, 100, 50))
    exchange.add(Bid(Buy, 100, 10))
    exchange.add(Bid(Sell, 100, 90))
    exchange.add(Bid(Sell, 100, 50))
    exchange.calculate() should be ("100 0.5")
  }

  "reject 1.000.001's bid" in {
    val exchange = new Exchange
    for(_ <- 1 to Exchange.limit) exchange.add(Bid(Buy, 100, 100))
    an[RuntimeException] should be thrownBy exchange.add(Bid(Sell, 100, 100))
  }

  "endure a maximum size of random bids" in {
    def randomAmount: Int = ThreadLocalRandom.current().nextInt(Bid.minAmount, Bid.maxAmount + 1)
    def randomPrice: Int = ThreadLocalRandom.current().nextInt(Bid.minPrice, Bid.maxPrice + 1)
    def randomDirection: BidKind = if(ThreadLocalRandom.current().nextBoolean()) Buy else Sell

    val exchange = new Exchange
    for(_ <- 1 to Exchange.limit) exchange.add(Bid(randomDirection, randomAmount, randomPrice))
    exchange.calculate()
  }

  "endure a maximum size of random bids 100 times" in {
    def randomAmount: Int = ThreadLocalRandom.current().nextInt(Bid.minAmount, Bid.maxAmount + 1)
    def randomPrice: Int = ThreadLocalRandom.current().nextInt(Bid.minPrice, Bid.maxPrice + 1)
    def randomDirection: BidKind = if(ThreadLocalRandom.current().nextBoolean()) Buy else Sell

    for(_ <- 1 to 100) {
      val exchange = new Exchange
      for(_ <- 1 to Exchange.limit) exchange.add(Bid(randomDirection, randomAmount, randomPrice))
      exchange.calculate()
    }
  }
}

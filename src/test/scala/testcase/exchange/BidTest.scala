package testcase.exchange

import org.scalatest.{Matchers, WordSpec}
import testcase.exchange.BidTypes._

class BidTest extends WordSpec with Matchers {
  "bid should be parsed from string" in {
    Bid("B 100 100") should be (Bid(Buy, 100, 10000))
    Bid("S 42 24") should be (Bid(Sell, 42, 2400))
  }

  "bid price could be decimal" in {
    Bid("B 100 12.00") should be (Bid(Buy, 100, 1200))
    Bid("B 100 12.0")  should be (Bid(Buy, 100, 1200))
    Bid("S 100 12.34") should be (Bid(Sell, 100, 1234))
    Bid("S 100 12.3") should be (Bid(Sell, 100, 1230))
  }

  "bid price should fail if more then 2 digits after dot" in {
    an[RuntimeException] should be thrownBy Bid("B 100 12.345")
    an[RuntimeException] should be thrownBy Bid("S 100 12.345678901234567")
  }

  "bid should fail if price more than 100" in {
    an[RuntimeException] should be thrownBy Bid("B 100 123.45")
  }

  "bid should fail if price less than 1" in {
    an[RuntimeException] should be thrownBy Bid("B 100 0.98")
  }

  "bid should fail if amount less than 1" in {
    an[RuntimeException] should be thrownBy Bid("B 0 12")
  }

  "bid should fail if amount more than 1000" in {
    an[RuntimeException] should be thrownBy Bid("B 1001 12")
  }

  "BidType should be parsed from string" in {
    BidTypes("S") should be (Sell)
    BidTypes("Sell") should be (Sell)
    BidTypes("B") should be (Buy)
    BidTypes("Buy") should be (Buy)
  }
}

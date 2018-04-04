package testcase.exchange

import org.scalatest.{Matchers, WordSpec}
import testcase.exchange.BidTypes._

class BidTest extends WordSpec with Matchers {
  "bid should be parsed from string" in {
    Bid("B 100 100") should be (Bid(Buy, 10000, 100))
    Bid("S 42 24") should be (Bid(Sell, 4200, 24))
  }

  "bid price could be decimal" in {
    Bid("B 12.00 100") should be (Bid(Buy, 1200, 100))
    Bid("B 12.0 100")  should be (Bid(Buy, 1200, 100))
    Bid("S 12.34 100") should be (Bid(Sell, 1234, 100))
    Bid("S 123.4 100") should be (Bid(Sell, 12340, 100))
  }

  "bid price should fail if more then 2 digits after dot" in {
    an[RuntimeException] should be thrownBy Bid("B 12.345 100")
    an[RuntimeException] should be thrownBy Bid("S 12.345678901234567 100")
  }

  "BidType should be parsed from string" in {
    BidTypes("S") should be (Sell)
    BidTypes("Sell") should be (Sell)
    BidTypes("B") should be (Buy)
    BidTypes("Buy") should be (Buy)
  }
}

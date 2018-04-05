package testcase.exchange

import testcase.exchange.BidTypes.{Buy, Sell}

case class Bid(direction: BidType, amount: Int, price: Int)

object Bid {
  def apply(s: String): Bid = {
    val r = raw"([BS]) (\d+) (\d+.?\d{1,2}?)".r
    s match {
      case r(t, a, p) =>
        val d = p.toDouble * 100
        if(d.toInt == d)
          Bid(BidTypes(t), a.toInt, d.toInt)
        else
          throw new RuntimeException(s"Price could be with 2 digits after point, '$p' doesn't fit")
      case other => throw new RuntimeException(s"Can't parse '$other', expected Bid like 'B 100 42'")
    }
  }
}

sealed trait BidType {
  override def toString: String = this match {
    case Buy => "Buy"
    case Sell => "Sell"
  }
}

object BidTypes {
  object Buy extends BidType
  object Sell extends BidType

  def apply(s: String): BidType = s match {
    case "B" | "Buy" => Buy
    case "S" | "Sell" => Sell
    case other => throw new RuntimeException(s"Can't parse '$other', expected values: B, Buy, S, Sell")
  }
}
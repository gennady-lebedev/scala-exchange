package testcase.exchange

import testcase.exchange.BidTypes.{Buy, Sell}

case class Bid(direction: BidType, amount: Int, price: Int)

object Bid {
  val minPrice = 100
  val maxPrice = 10000
  val minAmount = 1
  val maxAmount = 1000

  def apply(s: String): Bid = {
    val r = raw"([BS]) (\d+) (\d+.?\d{1,2}?)".r
    s match {
      case r(t, a, p) => Bid(BidTypes(t), a.toInt, p.toDouble)
      case other => throw new RuntimeException(s"Can't parse '$other', expected Bid like 'B 100 42'")
    }
  }

  def apply(direction: BidType, amount: Int, price: Double): Bid = {
    val d = price * 100
    val p = d.toInt
    if(p == d)
      if(p > maxPrice || p < minPrice)
        throw new RuntimeException(s"Price should be in range from 1 to 100, actual $p")
      else if(amount > maxAmount || amount < minAmount)
        throw new RuntimeException(s"Amount should be in range from 1 to 1000, actual $amount")
      else
        new Bid(direction, amount, p)
    else
      throw new RuntimeException(s"Price could be with 2 digits after point, '$price' doesn't fit")
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
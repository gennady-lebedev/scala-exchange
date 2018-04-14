package testcase.exchange

import testcase.exchange.BidTypes.{Buy, Sell}

/**
  * Representation of Bid
  * @param kind Buy or Sell
  * @param amount size of bid, between 1 and 1000
  * @param price internal representation, cast decimal with precision 2 to Int, between 100 and 10000 (1.00 to 100.00)
  */
case class Bid(kind: BidKind, amount: Int, price: Int) {
  override def toString: String = kind match {
    case Buy => s"B $price $amount"
    case Sell => s"S $price $amount"
  }
}

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

  def apply(direction: BidKind, amount: Int, price: Double): Bid = {
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

sealed trait BidKind {
  override def toString: String = this match {
    case Buy => "Buy"
    case Sell => "Sell"
  }
}

object BidTypes {
  object Buy extends BidKind
  object Sell extends BidKind

  def apply(s: String): BidKind = s match {
    case "B" | "Buy" => Buy
    case "S" | "Sell" => Sell
    case other => throw new RuntimeException(s"Can't parse '$other', expected values: B, Buy, S, Sell")
  }
}
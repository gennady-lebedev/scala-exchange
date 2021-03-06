package testcase.exchange

import com.typesafe.scalalogging.LazyLogging
import testcase.exchange.BidTypes._

import scala.collection.mutable
import scala.collection.immutable.TreeMap

class Exchange extends LazyLogging {
  private val bids = mutable.MutableList[Bid]()

  def add(bid: Bid): Unit = {
    if(bids.size >= Exchange.limit) throw new RuntimeException("Orders limit exceeded")

    bids += bid
  }

  def calculate(): String = {
    val sell = TreeMap(bids
      .filter(_.kind == Sell)
      .groupBy(_.price)
      .mapValues(_.map(_.amount).sum).toSeq:_*)
    logger.debug("Sell map: {}", sell)

    val sellScanned = sell.scanLeft(0 -> 0)((a, b) => b._1 -> (a._2 + b._2))
    logger.debug("Sell scanned: {}", sellScanned)

    val buy = TreeMap(bids
      .filter(_.kind == Buy)
      .groupBy(_.price)
      .mapValues(_.map(_.amount).sum).toSeq:_*)
    logger.debug("Buy map: {}", buy)

    val buyScanned = buy.scanRight(0 -> 0)((a, b) => a._1 -> (a._2 + b._2))
    logger.debug("Buy scanned: {}", buyScanned)

    val deals = sellScanned.map { case(p, s) =>
      logger.trace("Sell more than {}: {}", p, buyScanned.from(p))
      (p, Math.min(s, buyScanned.from(p).headOption.map(_._2).getOrElse(0)))
    }
    logger.debug("Deals: {}", deals)

    deals.reduce((a, b) =>
      if(a._2 > b._2) a
      else if(a._2 < b._2) b
      else/*a._2 == b._2*/(a._1 + b._1)/2 -> a._2
    ) match {
      case (_, 0) => "0 n/a"
      case (p, a) => s"$a ${p.toDouble / 100}"
      case other => throw new RuntimeException(s"Unexpected result $other")
    }
  }
}

object Exchange {
  val limit: Int = 1000000
}

package testcase.exchange

import com.typesafe.scalalogging.LazyLogging
import testcase.exchange.BidTypes._

import scala.collection.mutable
import scala.collection.immutable.TreeMap

class Exchange extends LazyLogging {
  private val limit: Int = 1000000
  private val bids = mutable.MutableList[Bid]()

  def add(bid: Bid): Unit = {
    if(bids.size >= limit) throw new RuntimeException("Orders limit exceeded")

    bids += bid
  }

  def calculate(): (Int, Int) = {
    val sell = TreeMap(bids
      .filter(_.direction == Sell)
      .groupBy(_.price)
      .mapValues(_.map(_.amount).sum).toSeq:_*)
    logger.trace("Sell map: {}", sell)

    val sellScanned = sell.scanLeft(0 -> 0)((a, b) => b._1 -> (a._2 + b._2))
    logger.trace("Sell scanned: {}", sellScanned)

    val buy = TreeMap(bids
      .filter(_.direction == Buy)
      .groupBy(_.price)
      .mapValues(_.map(_.amount).sum).toSeq:_*)
    logger.trace("Buy map: {}", buy)

    val buyScanned = buy.scanRight(0 -> 0)((a, b) => a._1 -> (a._2 + b._2))
    logger.trace("Buy scanned: {}", buyScanned)

    val deals = sellScanned.map { case(p, s) =>
      logger.trace("Sell more than {}: {}", p, buyScanned.from(p))
      (p, Math.min(s, buyScanned.from(p).headOption.map(_._2).getOrElse(0)))
    }
    logger.info("Deals: {}", deals)

    deals.reduce((a, b) =>
      if(a._2 > b._2) a
      else if(a._2 < b._2) b
      else/*a._2 == b._2*/(a._1 + b._1)/2 -> a._2
    )
  }
}

object Exchange {

}

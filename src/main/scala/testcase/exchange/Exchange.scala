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

  def calculate(): Unit = {
    val sell = TreeMap(bids
      .filter(_.direction == Sell)
      .groupBy(_.price)
      .mapValues(_.map(_.amount).sum).toSeq:_*)

    logger.info("Sell map: {}", sell)

    val sellScanned = sell.scanLeft(0 -> 0)((a, b) => b._1 -> (a._2 + b._2))
    logger.info("Sell scanned: {}", sellScanned)

    val buy = TreeMap(bids
      .filter(_.direction == Buy)
      .groupBy(_.price)
      .mapValues(_.map(_.amount).sum).toSeq:_*)

    logger.info("Buy map: {}", buy)

    val buyScanned = buy.scanRight(0 -> 0)((a, b) => a._1 -> (a._2 + b._2))
    logger.info("Buy scanned: {}", buyScanned)
  }
}

object Exchange {

}

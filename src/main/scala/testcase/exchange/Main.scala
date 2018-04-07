package testcase.exchange

import com.typesafe.scalalogging.LazyLogging

/**
  * Print bids in format '<kind> <amount> <price>', like 'B 50 10.12' or 'S 20 99.99'
  * 'exchange' to find optimal amount and price
  * 'quit' or 'exit' to exit
  */
object Main extends LazyLogging {
  def main(args: Array[String]): Unit = {
    var exchange = new Exchange

    for (ln <- io.Source.stdin.getLines) ln match {
      case "quit" | "exit" =>
        println("Exiting...")
        sys.exit(0)
      case "exchange" =>
        exchange.calculate() match {
          case (_, 0) =>
            println("0 n/a")
            exchange = new Exchange
          case (p, a) =>
            println(s"$a ${p.toDouble / 100}")
            exchange = new Exchange
          case other =>
            throw new RuntimeException(s"Unexpected result $other")
        }
      case _ =>
        try {
          exchange.add(Bid(ln))
        } catch {
          case e: Exception => println("not a Bid, try again")
        }
    }
  }
}

package testcase.exchange

import com.typesafe.scalalogging.LazyLogging

import scala.io.Source

object Main {
  def main(args: Array[String]): Unit = {
    if(args.length == 1)
      withFile(args(0))
    else
      cliLoop()
  }

  /**
    * Looking for a file in a working directory to parse bids, then to calculate optimal price
    * @param f filename
    */
  def withFile(f: String): Unit = {
    val exchange = new Exchange
    for (ln <- Source.fromFile(f, "UTF-8").getLines)
      exchange.add(Bid(ln))
    println(exchange.calculate())
    sys.exit(0)
  }

  /**
    * Print bids in format '<kind> <amount> <price>', like 'B 50 10.12' or 'S 20 99.99'
    * 'exchange' to find optimal amount and price
    * 'quit' or 'exit' to exit
    */
  def cliLoop(): Unit = {
    var exchange = new Exchange
    for (ln <- Source.stdin.getLines) ln match {
      case "quit" | "exit" =>
        println("Exiting...")
        sys.exit(0)
      case "exchange" =>
        println(exchange.calculate())
        exchange = new Exchange
      case _ =>
        try {
          exchange.add(Bid(ln))
        } catch {
          case e: Exception => println("not a Bid, try again")
        }
    }
  }

}

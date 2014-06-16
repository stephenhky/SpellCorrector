package home.kwyho.spellcheck.test

import home.kwyho.spellcheck.SpellCorrector
import java.io.File

/**
 * Created by hok1 on 6/16/14.
 */
object SpellCorrectionTester {
  def main(args : Array[String]) = {
    println("Initializing spell-check engine...")
    val spellCorrector = new SpellCorrector()
    println("Loading dictionary...")
    spellCorrector.train(new File("big.txt"))
    var spelling : String = readLine("spelling> ")
    while (spelling != "") {
      try {
        println(spellCorrector.correct(spelling))
      } catch {
        case uoe : UnsupportedOperationException => println("no spell correction available!!")
        case e : Exception => println("Unknown exception!!")
      }
      spelling = readLine("spelling> ")
    }
    println("Testing quit.")
  }
}

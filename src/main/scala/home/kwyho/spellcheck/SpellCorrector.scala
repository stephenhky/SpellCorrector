/**
 * Created by hok1 on 6/16/14.
 */

package home.kwyho.spellcheck

import java.io.File
import scala.io.Source
import scala.collection.mutable.Map

class SpellCorrector {
  var wordCounts : Map[String, Int] = Map()

  def train(trainFile : File) = {
    val lines = Source.fromFile(trainFile) mkString
    val wordREPattern = "[A-Za-z]+"
    for (txtWord<-wordREPattern.r.findAllIn(lines)) {
      val word = txtWord toLowerCase
      if (wordCounts.keySet contains(word)) {
        wordCounts(word) = wordCounts(word)+1
      } else {
        wordCounts += (word -> 1)
      }
    }
  }


}

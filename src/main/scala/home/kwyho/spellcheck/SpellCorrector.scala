/**
 * Created by hok1 on 6/16/14.
 */

package home.kwyho.spellcheck

import java.io.File
import scala.io.Source
import scala.collection.mutable.Map

class SpellCorrector {
  var wordCounts : Map[String, Int] = Map()
  val alphabets = ('a' to 'z').toSet

  def train(trainFile : File) = {
    val lines = Source.fromFile(trainFile) mkString
    val wordREPattern = "[A-Za-z]+"
    wordREPattern.r.findAllIn(lines).foreach( txtWord => {
      val word = txtWord.toLowerCase
      if (wordCounts.keySet contains(word)) {
        wordCounts(word) = wordCounts(word)+1
      } else {
        wordCounts += (word -> 1)
      }
    })
  }

  def getSplittedCombinations(word : String) : Set[(String, String)] =
    (0 to word.length).map( idx => (word.substring(0, idx), word.substring(idx, word.length))).toSet

  def getEditOneSpellings(word: String) : Set[String] = {
    val splits = getSplittedCombinations(word)
    val deletes = splits.map( s => if (s._2.length>1) {s._1+s._2.substring(1)} else {s._1})
    val transposes = splits.map( s => if (s._2.length>1) {
      s._1+s._2.charAt(1)+s._2.charAt(0)+s._2.substring(2)
    } else {s._1})
    val replaces = splits.map( s => alphabets.map(c => if (s._2.length>1) {
      s._1+c+s._2.substring(1)
    } else {s._1})).reduceRight( (set1, set2) => set1 | set2)
    val inserts = splits.map( s => alphabets.map(c => s._1+c+s._2)).reduceRight( (set1, set2) => set1 | set2)
    (deletes | transposes | replaces | inserts).intersect(wordCounts.keySet)
  }

  def getEditTwoSpellings(word: String) : Set[String] =
    getEditOneSpellings(word).map(getEditOneSpellings).filter( set => (set.size>0)).reduceRight( (set1, set2) => set1 | set2)

  def correct(wrongSpelling: String) : String = getEditTwoSpellings(wrongSpelling).maxBy( s => wordCounts(s))

}

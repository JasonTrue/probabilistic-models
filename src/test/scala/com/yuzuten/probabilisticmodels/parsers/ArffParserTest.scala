package com.yuzuten.probabilisticmodels.parsers

import org.scalatest.FunSuite
import java.io.StringReader

/**
 * ArffParserTest: Basic tests for the [ArffParser].
 *
 * Currently leaves out important tests for lots of things that are still only halfheartedly implemented, namely, quoted
 * strings, relational content, and so on.
 *
 * ArffParserTest
 * Author: jason
 */
class ArffParserTest extends FunSuite {
  test("empty document parses with no meaningful content, should not be considered successful") {
    val reader = new StringReader("")
    val parser = new ArffParser
    val result = parser.parseAll(parser.arffDocument, reader)
    println(result)

    assert(result.isEmpty)
    assert(!result.successful)
  }

  test("Comments parse out with an incomplete document") {
    val reader = new StringReader("% some comment\n")
    val parser = new ArffParser
    val result = parser.parseAll(parser.arffDocument, reader)
    println(result)
    assert(result.isEmpty)
    assert(!result.successful)
  }

  test("A minimal document returns an arffDocument") {
    val minimalistDoc =
      "@relation silly\n" +
      "@attribute wealth numeric\n" +
      "@attribute income numeric\n" +
      "@data\n" +
      "0,5000\n" //+

    val reader = new StringReader(minimalistDoc)
    val parser = new ArffParser
    val result = parser.parseAll(parser.arffDocument, reader)
    println(result)
    assert(result.successful, "Failed to parse")
  }

  test("Arff with nominal attribute returns an arffDocument") {
    val minimalistDoc =
      "@relation silly\n" +
      "@attribute age {child, teen, young-adult, adult, middle-aged, elderly}\n" +
      "@attribute wealth numeric\n" +
      "@attribute income numeric\n" +
      "@data\n" +
      "child,0,5000\n" +
      "adult,50000,50000\n" +
      "elderly,500000,20000\n"


    val reader = new StringReader(minimalistDoc)
    val parser = new ArffParser
    val result = parser.parseAll(parser.arffDocument, reader)
    println(result)
    assert(result.successful, "Failed to parse")
  }

  test("Arff with start of line comments") {
    val minimalistDoc =
      "% some story\n" +
      "@relation silly\n" +
      "@attribute wealth numeric\n" +
      "@attribute age {child, teen, young-adult, adult, middle-aged, elderly}\n" +
      "@attribute income numeric\n" +
      "@data\n" +
      "child,0,5000,0\n" +
      "adult,50000,50000\n" +
      "elderly,500000,20000\n"


    val reader = new StringReader(minimalistDoc)
    val parser = new ArffParser
    val result = parser.parseAll(parser.arffDocument, reader)
    println(result)
    assert(result.successful, "Failed to parse")
  }

//  test("Arff with optional spaces between commas") {
//    val minimalistDoc =
//      "% some story\n" +
//      "@relation silly\n" +
//      "@attribute wealth numeric\n" +
//      "@attribute income numeric\n" +
//      "@attribute age {child, teen, young-adult, adult, middle-aged, elderly}\n" +
//      "@data\n" +
//      "child, 0, 5000, 0\n" +
//      "adult,50000,50000\n"
//      "elderly, 500000, 20000\n"
//
//
//    val reader = new StringReader(minimalistDoc)
//    val parser = new ArffParser
//    val result = parser.parseAll(parser.arffDocument, reader)
//    println(result)
//    assert(result.successful, "Failed to parse")
//  }
}

package com.yuzuten.probabilisticmodels.parsers

import util.parsing.combinator.RegexParsers
import util.matching.Regex

/**
 * ArffParser supports parsing
 * Attribute-Relation File Format files, commonly used to exchange
 * Classifier datasets. Used to supports Arff parsing, except that it assumes UTF-8 encoding
 * rather than just ASCII.
 *
 * The Weka project already includes a parser for Arff format files. Justification for
 * this version is to get a basic understanding of Scala parser combinators.
 *
 * The format is described on WEKA's wikispace
 * http://weka.wikispaces.com/ARFF+%28developer+version%29#Examples-The @relation Declaration
 *
 * There was an early attempt at formalization of the Arff grammer at
 * https://list.scms.waikato.ac.nz/pipermail/wekalist/2008-January/038545.html
 * However, there are some inconsistencies with the WEKA specification that are either errors
 * or the result of an evolution of the grammar, so I'm relying primarily on the WEKA
 * documentation. Also I've taken a distinctly different approach to handling newlines and
 * whitespace so there's substantial fallout from that.
 *
 * The initial goal of this parser is to transform ARFF data into something more suitable for
 * pattern matching. Expect nasty bugs on some of the more complex bits until I actually
 * start working with more sophisticated ARFF files.
 *
 * User: jason
 * Date: 1/3/13 2:40 PM
 */

class ArffParser extends RegexParsers {
  // Line endings are syntactically meaningful breaks in expressions in Arff, much like CSV, so we consider only a
  // subset of the categories of whitespace to be ignorable.
  // Also, we want to ignore comments so we're taking a hacky approach of treating them as whitespace
  override val whiteSpace = """[ \t]+""".r
  //I arbitrarily consume the newline, which means that end of line comments are not supported yet
  val comment: Regex = """%[^\n]*\n+""".r

  def arffDocument: Parser[Any] = header ~ data
  def data : Parser[Any] = "@data" ~> newline ~> repsep(record, newline) <~ opt(newline)

  def record : Parser[Any] = opt(newline) ~> commaSeparatedValues
  def header: Parser[Any] = opt(newline) ~> relation <~ newline ~ attributeDeclarations
  def relation: Parser[Any] = "@relation" ~> stringExpr ^^ { r => Relation(stringExpr) }
  // There should be at least one attribute, which the ~rep1 combinator expects
  def attributeDeclarations: Parser[Any] = attributeDeclaration+
  def attributeDeclaration: Parser[Any] = opt(newline) ~> "@attribute" ~> attributeName ~ dataType <~ newline ^^ {
      case attributeName ~ dataType => AttributeDeclaration(attributeName, dataType)
  }
  def attributeName: Parser[Any] = stringExpr

  //It's not 100% clear if instanceWeights are integers-only or numeric so I'm assuming anything numeric is possible
  def instanceWeight: Parser[Any] = "{" ~> numeric <~ "}"

  def dataType: Parser[Any] =  {
    /* It looks like there's a chance relational isn't quite right, but it looks pretty close.
         Relational data is still somewhat experimental in Weka, I believe.
    */
    "relational" ~  attributeDeclarations ~ "@end" ~ (stringExpr) |
    nominalAttribute |
    //We could do some parsing of the date format here, but I'll punt for now
    "date" ~ opt(stringExpr) |
    /* Numeric, integer, real are synonyms, so clients should expect integers may not be just integers. */
    "numeric" |
    "integer" |
    "real" |
    "string"
  }

  def stringExpr: Parser[Any] = stringLiteral | quotedString
  def nominalAttribute: Parser[Any] = "{" ~> rep1sep("""[\w\-]+""".r, ",") <~ "}" ^^ { a=> NominalAttribute(a) }

  def digits: Parser[Any] = """\d+""".r
  def numeric: Parser[Any] =  int | float
  def int: Parser[Any] = opt("+" |  "-") ~ digits

  //Might be good to stick in exponential notation again
  def float: Parser[Any] = {
    opt("+" | "-") ~ opt(digits) ~ "." ^^ (_.toString().toFloat)
  }

  //TODO: This might not be the best matcher but should come pretty close.
  def stringLiteral = """[\S^"^'^,]+""".r  ^^ { l => l.toString}
  /* TODO: Potentially a bit nasty with escaped quotes. The CSV convention isn't really a standard,
     so implementations, and usage habits, vary. I'm trying to be somewhat generous in what can be accepted
     but some work will probably still be needed here.

     Later I should work on implementing as much as practical from
     http://tools.ietf.org/html/rfc4180
  */
  def quotedString = ("\"" ~ (rep("\\\"" | not ("\"" | "\"\""))) ~ "\"" | "\'" ~ (rep("\\\'" | not ("\'" | "\'\'")))) ^^
    { quotedString => quotedString.toString()}

  //needs to handle integers and floats
  def value: Parser[Any] =  unknownValue | numeric |stringExpr //TODO: handle quoted relational values like "42,...,30"
  def unknownValue: Parser[Any] = "?"

  def commaSeparatedValues: Parser[Any] = rep1sep(stringExpr, ",") ~ opt("," ~> instanceWeight) ^^ {
    case values ~ instanceWeight =>  CommaSeparatedValues(values, instanceWeight)
  }

  // SparseValues look like: {position value, position value, ...}
  def sparseValues: Parser[Any] = "{" ~> rep1sep(sparseValue, ",") <~ "}" ~ opt("," ~> instanceWeight)
//
  /* sparseValue items should generally be >= 1, so I'm not allowing negative or positive signs because that
     seems silly  */
  def sparseValue: Parser[Any] = digits ~ value ^^ {
    case ordinal ~ value => SparseValue(ordinal, value)
  }

  //We can collapse newline into a single one because we only care that that at least one newline was present.
  def newline: Parser[Any] = """[\r\n]+""".r

  // We override handleWhiteSpace to deal with comments. Comments start with % and continue to end of line, so
  // we just look for that pattern and add it to the text offset to make it disappear from the input stream.
  override def handleWhiteSpace(source: java.lang.CharSequence, offset: Int): Int =
    (comment findPrefixMatchOf (source.subSequence(offset, source.length))) match {
      case Some(matched) => super.handleWhiteSpace(source, offset + matched.end)
      case None => super.handleWhiteSpace(source, offset)
    }
}

case class Relation(name: Any)
case class NominalAttribute(names: List[String])

case class AttributeDeclaration(name: Any, dataType: Any)

case class Value(content : Any)

case class Data(content: List[Record])

abstract trait Record
  case class CommaSeparatedValues(values : List[Any], instanceWeight: Any) extends Record
  case class SparseValues(pairs: List[SparseValue], instanceWeight: Any) extends Record

case class SparseValue(ordinal : Any, value : Any)

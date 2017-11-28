package util

import org.apache.jena.graph.Triple
import org.apache.jena.query.ResultSet
import org.apache.jena.query.ResultSetFormatter

fun ResultSet.toText(): String = ResultSetFormatter.asText(this)
fun Triple.toText() = "${subject.localName} ${predicate.localName} ${`object`.localName}"
fun Iterator<Triple>.toText() = asSequence().joinToString("\n") { it.toText() }

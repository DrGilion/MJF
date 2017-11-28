package util

import org.apache.jena.query.ResultSet
import org.apache.jena.query.ResultSetFormatter

fun ResultSet.toText(): String = ResultSetFormatter.asText(this)
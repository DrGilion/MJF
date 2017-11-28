package controller

import org.apache.jena.query.QueryExecutionFactory
import org.apache.jena.query.ResultSetFormatter
import org.apache.jena.query.Syntax
import tornadofx.*

class QueryController : Controller() {
	private val modelController: ModelController by inject()

	fun executeQuery(query: String) : String {
		val result = QueryExecutionFactory.create(query, Syntax.syntaxARQ, modelController.model)

		return when {
			query.contains("select", ignoreCase = true) -> {
				val results = result.execSelect()
				ResultSetFormatter.asText(results)
			}

			query.contains("ask", ignoreCase = true) -> {
				val boolVal = result.execAsk()
				boolVal.toString()
			}

			query.contains("construct", ignoreCase = true) -> {
				val newGraph = result.execConstructTriples()
				newGraph.asSequence()
						.map { "" + it.subject.localName + " " + it.predicate.localName + " " + it.`object`.localName + "\n" }
						.joinToString()
						.replace(", ", "")
			}
			else -> "error!"
		}
	}
}
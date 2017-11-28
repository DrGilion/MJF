package controller

import org.apache.jena.query.QueryExecutionFactory
import org.apache.jena.query.Syntax
import tornadofx.*
import util.toText

class QueryController : Controller() {
	private val modelController: ModelController by inject()

	fun executeQuery(query: String): String {
		val result = QueryExecutionFactory.create(query, Syntax.syntaxARQ, modelController.model)

		return when {
			query.contains("select", ignoreCase = true) -> result.execSelect().toText()
			query.contains("ask", ignoreCase = true) -> result.execAsk().toString()
			query.contains("construct", ignoreCase = true) -> result.execConstructTriples().toText()
			else -> "error!"
		}
	}
}
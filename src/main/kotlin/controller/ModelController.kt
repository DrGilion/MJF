package controller

import org.apache.jena.query.QueryExecutionFactory
import org.apache.jena.query.Syntax
import org.apache.jena.rdf.model.InfModel
import org.apache.jena.rdf.model.Model
import org.apache.jena.rdf.model.ModelFactory
import org.apache.jena.reasoner.ReasonerRegistry
import org.apache.jena.reasoner.rulesys.GenericRuleReasoner
import org.apache.jena.reasoner.rulesys.Rule
import tornadofx.*
import util.toText
import java.io.File
import java.io.FileOutputStream

fun main(args: Array<String>) {
	val modelController = find<ModelController>()
	val result = QueryExecutionFactory.create("SELECT ?x ?y ?z WHERE { ?x ?y ?z }", Syntax.syntaxARQ, modelController.owlModel.deductionsModel)
	println(result.execSelect().toText())
}

class ModelController : Controller() {
	val aBox: Model
	val tBox: Model
	val rdfsModel: Model
	val owlModel: InfModel
	val jenaModel: InfModel

	init {
		tBox = loadModelFromFile("TBox.ttl")
		aBox = loadModelFromFile("ABox.ttl")
		rdfsModel = ModelFactory.createRDFSModel(tBox, aBox)
		val reasoner = ReasonerRegistry.getOWLReasoner().bindSchema(rdfsModel)
		owlModel = ModelFactory.createInfModel(reasoner, rdfsModel)

		println(owlModel.validate().reports.forEach { println(it.description) })

		val jenaReasoner = GenericRuleReasoner(Rule.rulesFromURL("JenaRules.jena"))
		jenaReasoner.setDerivationLogging(true)
		jenaModel = ModelFactory.createInfModel(jenaReasoner, owlModel)
	}

	private fun loadModelFromFile(fileName: String): Model {
		val fileModel = ModelFactory.createDefaultModel()
		fileModel.read(fileName, "TURTLE")
		return fileModel
	}

	fun exportWholeModel(file: File) {
		try {
			val fos = FileOutputStream(file)

			if (!file.exists()) file.createNewFile()


			jenaModel.write(fos, "TURTLE")

			fos.flush()
			fos.close()
		} catch (e: Exception) {
			e.printStackTrace()
		}
	}
}
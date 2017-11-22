package controller

import org.apache.jena.rdf.model.Model
import org.apache.jena.rdf.model.ModelFactory
import org.apache.jena.vocabulary.OWL
import org.apache.jena.vocabulary.RDF
import org.apache.jena.vocabulary.RDFS
import org.apache.jena.vocabulary.XSD
import tornadofx.*
import java.io.File
import java.io.FileOutputStream

class ModelController : Controller() {
	val model: Model

	init {
		val tboxModel = createTBox()
		val aboxModel = loadABoxFromFile()

		val tempModel = tboxModel.add(aboxModel)

		model = ModelFactory.createRDFSModel(tempModel)
	}

	private fun createTBox(): Model {
		val tboxModel = ModelFactory.createDefaultModel()

		val mjfns = "http://michajonasfood.com/"
		val dbons = "http://dbpedia.org/ontology/"

		tboxModel.add(tboxModel.createResource(mjfns), RDF.type, OWL.Ontology)

		val food = tboxModel.createResource(mjfns + "food")
		tboxModel.add(food, RDF.type, RDFS.Class)
		tboxModel.add(food, OWL.sameAs, dbons + "Food")

		val fruit = tboxModel.createResource(mjfns + "fruit")
		tboxModel.add(fruit, RDFS.subClassOf, food)

		val vegetable = tboxModel.createResource(mjfns + "vegetable")
		tboxModel.add(vegetable, RDFS.subClassOf, food)

		val animalProduct = tboxModel.createResource(mjfns + "animalProduct")
		tboxModel.add(animalProduct, RDFS.subClassOf, food)

		val spice = tboxModel.createResource(mjfns + "spice")
		tboxModel.add(spice, RDFS.subClassOf, food)

		val meat = tboxModel.createResource(mjfns + "meat")
		tboxModel.add(meat, RDFS.subClassOf, food)

		val fish = tboxModel.createResource(mjfns + "fish")
		tboxModel.add(fish, RDFS.subClassOf, food)

		val cereal = tboxModel.createResource(mjfns + "cereal")
		tboxModel.add(cereal, RDFS.subClassOf, food)

		val nut = tboxModel.createResource(mjfns + "nut")
		tboxModel.add(nut, RDFS.subClassOf, food)

		val liquid = tboxModel.createResource(mjfns + "liquid")
		tboxModel.add(liquid, RDFS.subClassOf, food)

		val dish = tboxModel.createResource(mjfns + "dish")
		tboxModel.add(dish, RDFS.subClassOf, food)

		val contains = tboxModel.createProperty(mjfns, "contains")
		tboxModel.add(contains.asResource(), RDFS.domain, dish)
		tboxModel.add(contains.asResource(), RDFS.range, food)

		val taste = tboxModel.createResource(mjfns + "taste")
		tboxModel.add(taste, RDF.type, RDFS.Class)

		val smell = tboxModel.createResource(mjfns + "smell")
		tboxModel.add(smell, RDF.type, RDFS.Class)

		val characteristic = tboxModel.createProperty(mjfns, "Characteristic")

		val tastes = tboxModel.createProperty(mjfns, "tastes")
		tboxModel.add(tastes.asResource(), RDFS.subPropertyOf, characteristic)
		tboxModel.add(tastes.asResource(), RDFS.domain, food)
		tboxModel.add(tastes.asResource(), RDFS.range, taste)

		val smells = tboxModel.createProperty(mjfns, "smells")
		tboxModel.add(smells.asResource(), RDFS.subPropertyOf, characteristic)
		tboxModel.add(smells.asResource(), RDFS.domain, food)
		tboxModel.add(smells.asResource(), RDFS.range, smell)

		val sugarAmountPer100g = tboxModel.createProperty(mjfns, "sugarAmountPer100g")
		tboxModel.add(sugarAmountPer100g.asResource(), RDFS.subPropertyOf, characteristic)
		tboxModel.add(sugarAmountPer100g.asResource(), RDFS.domain, food)
		tboxModel.add(sugarAmountPer100g.asResource(), RDFS.range, XSD.xdouble)

		val caloriesPer100g = tboxModel.createProperty(mjfns, "caloriesPer100g")
		tboxModel.add(caloriesPer100g.asResource(), RDFS.subPropertyOf, characteristic)
		tboxModel.add(caloriesPer100g.asResource(), RDFS.domain, food)
		tboxModel.add(caloriesPer100g.asResource(), RDFS.range, XSD.integer)

		return tboxModel
	}

	private fun loadABoxFromFile(): Model {
		val fileModel = ModelFactory.createDefaultModel()
		fileModel.read("mjfOntology.ttl", "TURTLE")
		return fileModel
	}

	fun exportWholeModel(file: File) {
		try {
			val fos = FileOutputStream(file)

			if (!file.exists()) file.createNewFile()

			model.write(fos, "RDF/XML")

			fos.flush()
			fos.close()
		} catch (e : Exception) {
			e.printStackTrace()
		}
	}
}
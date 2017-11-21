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
	val model: Model = ModelFactory.createDefaultModel()

	init {
		createTBox()
		loadFromFile()
		exportWholeModel()
	}

	private fun createTBox() {
		val mjfns = "http://michajonasfood.com/"
		val dbons = "http://dbpedia.org/ontology/"

		model.add(model.createResource(mjfns), RDF.type, OWL.Ontology)

		val food = model.createResource(mjfns + "food")
		model.add(food, RDF.type, RDFS.Class)
		model.add(food, OWL.sameAs, dbons + "Food")

		val fruit = model.createResource(mjfns + "fruit")
		model.add(fruit, RDFS.subClassOf, food)

		val vegetable = model.createResource(mjfns + "vegetable")
		model.add(vegetable, RDFS.subClassOf, food)

		val animalProduct = model.createResource(mjfns + "animalProduct")
		model.add(animalProduct, RDFS.subClassOf, food)

		val spice = model.createResource(mjfns + "spice")
		model.add(spice, RDFS.subClassOf, food)

		val meat = model.createResource(mjfns + "meat")
		model.add(meat, RDFS.subClassOf, food)

		val fish = model.createResource(mjfns + "fish")
		model.add(fish, RDFS.subClassOf, food)

		val cereal = model.createResource(mjfns + "cereal")
		model.add(cereal, RDFS.subClassOf, food)

		val nut = model.createResource(mjfns + "nut")
		model.add(nut, RDFS.subClassOf, food)

		val liquid = model.createResource(mjfns + "liquid")
		model.add(liquid, RDFS.subClassOf, food)

		val dish = model.createResource(mjfns + "dish")
		model.add(dish, RDFS.subClassOf, food)

		val contains = model.createProperty(mjfns, "contains")
		model.add(contains.asResource(), RDFS.domain, dish)
		model.add(contains.asResource(), RDFS.range, food)

		val taste = model.createResource(mjfns + "taste")
		model.add(taste, RDF.type, RDFS.Class)

		val smell = model.createResource(mjfns + "smell")
		model.add(smell, RDF.type, RDFS.Class)

		val characteristic = model.createProperty(mjfns, "Characteristic")

		val tastes = model.createProperty(mjfns, "tastes")
		model.add(tastes.asResource(), RDFS.subPropertyOf, characteristic)
		model.add(tastes.asResource(), RDFS.domain, food)
		model.add(tastes.asResource(), RDFS.range, taste)

		val smells = model.createProperty(mjfns, "smells")
		model.add(smells.asResource(), RDFS.subPropertyOf, characteristic)
		model.add(smells.asResource(), RDFS.domain, food)
		model.add(smells.asResource(), RDFS.range, smell)

		val sugarAmountPer100g = model.createProperty(mjfns, "sugarAmountPer100g")
		model.add(sugarAmountPer100g.asResource(), RDFS.subPropertyOf, characteristic)
		model.add(sugarAmountPer100g.asResource(), RDFS.domain, food)
		model.add(sugarAmountPer100g.asResource(), RDFS.range, XSD.xdouble)

		val caloriesPer100g = model.createProperty(mjfns, "caloriesPer100g")
		model.add(caloriesPer100g.asResource(), RDFS.subPropertyOf, characteristic)
		model.add(caloriesPer100g.asResource(), RDFS.domain, food)
		model.add(caloriesPer100g.asResource(), RDFS.range, XSD.integer)
	}

	private fun loadFromFile() {
		val fileModel = ModelFactory.createDefaultModel()
		fileModel.read("mjfOntology.ttl", "TURTLE")
		model.add(fileModel)
	}

	private fun exportWholeModel() {
		try {
			val file = File("mjf.rdf")
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
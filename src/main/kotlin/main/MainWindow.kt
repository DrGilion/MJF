package main

import javafx.beans.property.SimpleStringProperty
import javafx.scene.layout.Priority
import javafx.scene.text.Font
import org.apache.jena.query.QueryExecutionFactory
import org.apache.jena.query.ResultSetFormatter
import org.apache.jena.query.Syntax
import org.apache.jena.rdf.model.ModelFactory
import tornadofx.*

class MainWindow : View("Micha-Jonas-Food Project") {
	private val queryText = SimpleStringProperty()
	private val outputText = SimpleStringProperty()

	//val currentSelected = SimpleObjectProperty<Map.Entry<String,String>>()

	override val root = borderpane {
		setPrefSize(1280.0, 768.0)

		paddingAll = 5
		top = gridpane {
			paddingBottom = 5
			row {
				val selectBox = combobox(values = Queries.selectQueries.entries.toList()) {
					useMaxWidth = true
					gridpaneConstraints { margin = tornadofx.insets(5) }
					cellFormat { text = it.key }

					selectionModel.selectedItemProperty().onChange {
						queryText.value = it?.value
					}
					selectionModel.selectFirst()
				}

				button("Execute Select Query") {
					useMaxWidth = true
					gridpaneConstraints { margin = tornadofx.insets(5) }

					action {
						val model = ModelFactory.createDefaultModel()
						model.read("mjfOntology.ttl", "TURTLE")

						val query = selectBox.selectionModel.selectedItem.value

						val result = QueryExecutionFactory.create(query, Syntax.syntaxARQ, model)
						val results = result.execSelect()

						outputText.value = ResultSetFormatter.asText(results)
					}
				}
			}

			row {
				val askBox = combobox(values = Queries.askQueries.entries.toList()) {
					useMaxWidth = true
					gridpaneConstraints { margin = tornadofx.insets(5) }
					cellFormat { text = it.key }

					selectionModel.selectedItemProperty().onChange {
						queryText.value = it?.value
					}
					selectionModel.selectFirst()
				}

				button("Execute Ask Query") {
					useMaxWidth = true
					gridpaneConstraints { margin = tornadofx.insets(5) }

					action {
						val model = ModelFactory.createDefaultModel()
						model.read("mjfOntology.ttl", "TURTLE")

						val query = askBox.selectionModel.selectedItem.value

						val result = QueryExecutionFactory.create(query, Syntax.syntaxARQ, model)
						val boolVal = result.execAsk()

						outputText.value = boolVal.toString()
					}
				}
			}

			center = hbox(5) {
				vbox {
					label("Query")
					textarea(queryText) {
						hgrow = Priority.ALWAYS
						vgrow = Priority.ALWAYS

						isEditable = false
						font = Font.font("Monospaced")
					}

					hgrow = Priority.ALWAYS
					vgrow = Priority.ALWAYS
				}

				vbox {
					label("Result")
					textarea(outputText) {
						hgrow = Priority.ALWAYS
						vgrow = Priority.ALWAYS

						isEditable = false
						font = Font.font("Monospaced")
					}

					hgrow = Priority.ALWAYS
					vgrow = Priority.ALWAYS
				}
			}
		}
	}
}

package main

import controller.ModelController
import javafx.beans.property.SimpleStringProperty
import javafx.scene.layout.Priority
import javafx.scene.text.Font
import javafx.stage.FileChooser
import org.apache.jena.query.QueryExecutionFactory
import org.apache.jena.query.ResultSetFormatter
import org.apache.jena.query.Syntax
import tornadofx.*

class MainWindow : View("Micha-Jonas-Food Project") {
	private val queryText = SimpleStringProperty()
	private val outputText = SimpleStringProperty()

	private val modelController: ModelController by inject()

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
						val query = selectBox.selectionModel.selectedItem.value

						val result = QueryExecutionFactory.create(query, Syntax.syntaxARQ, modelController.model)
						val results = result.execSelect()

						queryText.value = query
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
						val query = askBox.selectionModel.selectedItem.value

						val result = QueryExecutionFactory.create(query, Syntax.syntaxARQ, modelController.model)
						val boolVal = result.execAsk()

						queryText.value = query
						outputText.value = boolVal.toString()
					}
				}
			}

			row {
				val constructBox = combobox(values = Queries.constructQueries.entries.toList()) {
					useMaxWidth = true
					gridpaneConstraints { margin = tornadofx.insets(5) }
					cellFormat { text = it.key }

					selectionModel.selectedItemProperty().onChange {
						queryText.value = it?.value
					}
					selectionModel.selectFirst()
				}

				button("Execute Construct Query") {
					useMaxWidth = true
					gridpaneConstraints { margin = tornadofx.insets(5) }

					action {
						val query = constructBox.selectionModel.selectedItem.value

						val result = QueryExecutionFactory.create(query, Syntax.syntaxARQ, modelController.model)
						val newGraph = result.execConstructTriples()

						queryText.value = query
						outputText.value = newGraph.asSequence()
								.map { "" + it.subject.localName + " " + it.predicate.localName + " " + it.`object`.localName + "\n" }
								.joinToString()
								.replace(", ", "")
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

			bottom {
				buttonbar {
					paddingTop = 5
					button("Export Model").action {
						val fileChoose = FileChooser()
						fileChoose.initialFileName = "mjf.rdf"
						fileChoose.extensionFilters.add(FileChooser.ExtensionFilter("RDF", "*.rdf"))
						val file = fileChoose.showSaveDialog(this@MainWindow.currentWindow)

						if(file != null)
							modelController.exportWholeModel(file)
					}
				}
			}
		}
	}
}

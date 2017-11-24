package main

import controller.ModelController
import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Pos
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
				label("Templates: ")
				combobox(values = Queries.queries.entries.toList()) {
					useMaxWidth = true
					gridpaneConstraints { margin = tornadofx.insets(5) }
					cellFormat { text = it.key }

					selectionModel.selectedItemProperty().onChange {
						queryText.value = it?.value
					}
					selectionModel.selectFirst()
				}
			}

			center = hbox(5) {
				vbox {
					label("Query")
					textarea(queryText) {
						hgrow = Priority.ALWAYS
						vgrow = Priority.ALWAYS

						isEditable = true
						font = Font.font("Monospaced")
					}

					hgrow = Priority.ALWAYS
					vgrow = Priority.ALWAYS
				}

				vbox(5) {
					alignment = Pos.CENTER
					button("go!"){
						useMaxWidth = true
						gridpaneConstraints { margin = tornadofx.insets(5) }

						action {
							val result = QueryExecutionFactory.create(queryText.value, Syntax.syntaxARQ, modelController.model)
							when{
								queryText.value.contains("select",ignoreCase = true) -> {
									val results = result.execSelect()
									outputText.value = ResultSetFormatter.asText(results)
								}
								queryText.value.contains("ask",ignoreCase = true) -> {
									val boolVal = result.execAsk()
									outputText.value = boolVal.toString()
								}
								queryText.value.contains("construct",ignoreCase = true) -> {
									val newGraph = result.execConstructTriples()
									outputText.value = newGraph.asSequence()
											.map { "" + it.subject.localName + " " + it.predicate.localName + " " + it.`object`.localName + "\n" }
											.joinToString()
											.replace(", ", "")
								}
								else -> {
									outputText.value = "error!"
								}
							}
						}
					}
					label("--->")
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

package main

import controller.ModelController
import controller.QueryController
import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Pos
import javafx.scene.layout.Priority
import javafx.stage.FileChooser
import tornadofx.*

class MainWindow : View("Micha-Jonas-Food Project") {
	private val queryText = SimpleStringProperty()
	private val outputText = SimpleStringProperty()

	private val modelController: ModelController by inject()
	private val queryController: QueryController by inject()

	override val root =
			borderpane {
				top = form {
					fieldset {
						field("Templates: ") {
							combobox(values = Queries.queries.entries.toList()) {
								cellFormat { text = it.key }

								selectionModel.selectedItemProperty().onChange {
									queryText.value = it?.value
								}

								selectionModel.selectFirst()
							}
						}
					}
				}

				center = hbox(5) {
					vbox {
						label("Query")
						textarea(queryText) {
							hgrow = Priority.ALWAYS
							vgrow = Priority.ALWAYS

							isEditable = true
						}

						hgrow = Priority.ALWAYS
						vgrow = Priority.ALWAYS
					}

					vbox(5) {
						alignment = Pos.CENTER

						button("\u279C") {
							addClass(MainWindowStyle.executeButton)
							action {
								outputText.value = queryController.executeQuery(queryText.value)
							}
						}
					}

					vbox {
						label("Result")
						textarea(outputText) {
							hgrow = Priority.ALWAYS
							vgrow = Priority.ALWAYS

							isEditable = false
						}

						hgrow = Priority.ALWAYS
						vgrow = Priority.ALWAYS
					}
				}

				bottom = buttonbar {
					button("Export Model").action {
						val fileChoose = FileChooser()
						fileChoose.initialFileName = "mjf.rdf"
						fileChoose.extensionFilters.add(FileChooser.ExtensionFilter("RDF", "*.rdf"))
						val file = fileChoose.showSaveDialog(this@MainWindow.currentWindow)

						if (file != null)
							modelController.exportWholeModel(file)
					}
				}
			}
}


package main

import javafx.beans.property.SimpleObjectProperty
import javafx.scene.control.TextArea
import org.apache.jena.query.QueryExecutionFactory
import org.apache.jena.query.ResultSetFormatter
import org.apache.jena.query.Syntax
import org.apache.jena.rdf.model.ModelFactory
import tornadofx.*

class MainWindow : View("Micha-Jonas-Food Project") {
    val queryPane = TextArea()
    val outputPane = TextArea()
    val currentSelected = SimpleObjectProperty<Map.Entry<String,String>>()
    override val root = borderpane()

    init {
        with(root){
            top = hbox {
                val box = combobox(currentSelected, Queries.selectQueries.entries.toList()) {
                    cellFormat {
                        text = it.key
                    }

                    selectionModel.selectedItemProperty().onChange {
                        queryPane.text = it?.value
                    }
                    selectionModel.selectFirst()
                }
                button("Execute Query").action {
                    val model = ModelFactory.createDefaultModel()
                    model.read("mjfOntology.ttl", "TURTLE")

                    val query = currentSelected.value.value

                    val result = QueryExecutionFactory.create(query, Syntax.syntaxARQ, model)
                    val results = result.execSelect()

                    outputPane.text = ResultSetFormatter.asText(results)
                }
            }
            left = queryPane
            center = label(" -----> ")
            right = outputPane
        }
    }
}

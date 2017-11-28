package main

import javafx.scene.text.Font
import tornadofx.*

class MainWindowStyle : Stylesheet() {
	companion object {
		val executeButton by cssclass()
	}

	init {
		executeButton {
			fontSize = 20.pt
			minWidth = 65.px
			minHeight = 40.px
		}

		root {
			padding = box(5.0.px)

			prefWidth = 1280.px
			prefHeight = 768.px

			minWidth = 800.px
			minHeight = 600.px
		}

		buttonBar {
			padding = box(5.px, 0.px, 0.px, 0.px)
		}

		textArea {
			font = Font.font("Monospaced")
		}
	}
}
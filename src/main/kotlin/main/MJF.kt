package main

import tornadofx.*

class MJF : App(MainWindow::class, MainWindowStyle::class) {
	init {
		reloadStylesheetsOnFocus()
	}
}
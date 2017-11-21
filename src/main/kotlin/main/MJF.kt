package main

import javafx.application.Application
import tornadofx.*

class MJF : App(MainWindow::class)

fun main(args: Array<String>) = Application.launch(MJF::class.java, *args)
package dk.fitfit.solitaire

import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.layout.StackPane
import javafx.stage.Stage

class FXApplication {
    fun start(stage: Stage) {
        val javaVersion = System.getProperty("java.version")
        val javafxVersion = System.getProperty("javafx.version")
        val l = Label("Hello, JavaFX $javafxVersion, running on Java $javaVersion.")
        val scene = Scene(StackPane(l), 640.toDouble(), 480.toDouble())
        stage.scene = scene
        stage.show()

        val engine = SolitaireEngine(7)
    }
}

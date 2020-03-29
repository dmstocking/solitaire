package dk.fitfit.solitaire

import javafx.application.Application
import javafx.stage.Stage

class LaunchFX : Application() {

    override fun start(stage: Stage) {
        val scene = DaggerMainComponent.builder()
            .build()
            .solitaireSceneFactory()
            .create()
        stage.scene = scene
        stage.show()
    }
}

package dk.fitfit.solitaire

import io.reactivex.rxjavafx.schedulers.JavaFxScheduler
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import javafx.scene.layout.Pane
import javafx.scene.paint.Paint
import javafx.scene.shape.Rectangle
import java.lang.Integer.max
import javax.inject.Inject
import javax.inject.Singleton

data class SolitaireViewModel(
    val tiles: List<List<Color>>,
    val undoEnabled: Boolean,
    val redoEnabled: Boolean,
    val showWon: Boolean,
    val showLost: Boolean
)

private const val tileSize = 80.0

@Singleton
class SolitaireSceneFactory @Inject constructor(
    private val solitaireStateMapper: SolitaireStateMapper,
    private val store: StateStore
) {

    fun create(): Scene {
        val tiles = List(WIDTH) { i ->
            List(HEIGHT) { j ->
                Rectangle(tileSize - 2, tileSize - 2).also { rect ->
                    rect.onMouseClicked = EventHandler { store.dispatch(Click(Position(i, j))) }
                    rect.translateX = i * tileSize + 1
                    rect.translateY = j * tileSize + 1
                }
            }
        }

        tiles[0][0].also { rect ->
            rect.onMouseClicked = EventHandler { store.dispatch(Undo) }
        }

        val pane = Pane().also { p ->
            p.setPrefSize(WIDTH * tileSize, HEIGHT * tileSize)
            p.children.addAll(tiles.flatten())
        }

        val wonPopup = Alert(Alert.AlertType.NONE, "Won", ButtonType.OK)

        val lostPopup = Alert(Alert.AlertType.NONE, "Lost", ButtonType.OK)

        // TODO would need to figure out how to unsubscribe. Can't seem to find a javaFx lifecycle for views
        store.state()
            .map(solitaireStateMapper::map)
            .observeOn(JavaFxScheduler.platform())
            .doOnNext { model ->
                for (i in (0 until max(model.tiles.size, tiles.size))) {
                    for (j in (0 until max(model.tiles[i].size, tiles[i].size))) {
                        val newFill = model.tiles[i][j].toPaint()
                        if (tiles[i][j] != newFill) {
                            tiles[i][j].fill = newFill
                        }
                    }
                }

                if (model.showWon) {
                    wonPopup.show()
                } else if (wonPopup.isShowing) {
                    wonPopup.hide()
                }

                if (model.showLost) {
                    lostPopup.show()
                } else if (lostPopup.isShowing) {
                    lostPopup.hide()
                }
            }
            .doOnError { println(it.message) }
            .subscribe()

        return Scene(pane)
    }
}

private fun Color.toPaint(): Paint? {
    return when (this) {
        Color.BLACK -> javafx.scene.paint.Color.BLACK
        Color.BLUE -> javafx.scene.paint.Color.BLUE
        Color.BROWN -> javafx.scene.paint.Color.BROWN
        Color.YELLOW -> javafx.scene.paint.Color.YELLOW
    }
}

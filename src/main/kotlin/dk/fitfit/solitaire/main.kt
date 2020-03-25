package dk.fitfit.solitaire

import com.andreapivetta.kolor.Color
import com.andreapivetta.kolor.Color.*
import com.andreapivetta.kolor.Kolor
import dk.fitfit.solitaire.SolitaireEngine.DIRECTION.DOWN
import dk.fitfit.solitaire.SolitaireEngine.TILE.*
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.layout.StackPane
import javafx.stage.Stage

class SolitaireEngine(size: Int) {
    enum class TILE(val symbol: String) {
        ILLEGAL("I"), EMPTY("E"), FULL("F")
    }

    enum class DIRECTION(val coordinates: Pair<Int, Int>) {
        LEFT(Pair(-1, 0)), UP(Pair(0, -1)), RIGHT(Pair(1, 0)), DOWN(Pair(0, 1))
    }

    val board: Array<Array<TILE>> = generateBoard(size)

    private fun generateBoard(size: Int): Array<Array<TILE>> {
        if (size < 7 && size % 2 != 0) {
            throw IllegalArgumentException("Size must be bigger than or equal to 7 and odd")
        }

        val board: Array<Array<TILE>> = Array(size) { Array(size) { FULL } }

        board[0][0] = ILLEGAL
        board[1][0] = ILLEGAL
        board[0][1] = ILLEGAL
        board[1][1] = ILLEGAL

        board[size - 1][0] = ILLEGAL
        board[size - 2][0] = ILLEGAL
        board[size - 1][1] = ILLEGAL
        board[size - 2][1] = ILLEGAL

        board[0][size - 1] = ILLEGAL
        board[0][size - 2] = ILLEGAL
        board[1][size - 1] = ILLEGAL
        board[1][size - 2] = ILLEGAL

        board[size - 1][size - 1] = ILLEGAL
        board[size - 2][size - 1] = ILLEGAL
        board[size - 2][size - 2] = ILLEGAL
        board[size - 1][size - 2] = ILLEGAL

        val center = size / 2
        board[center][center] = EMPTY

        return board
    }

    fun move(row: Int, col: Int, trow: Int, tcol: Int) {
        val from = board[row][col]
        val to = board[trow][tcol]

        if (from != FULL) {
            throw IllegalAccessException("From not full")
        }
        if (to != EMPTY) {
            throw IllegalAccessException("To not empty")
        }

        val moveToDirection = mapOf(
            Pair(-2, 0) to DIRECTION.LEFT,
            Pair(0, -2) to DIRECTION.UP,
            Pair(2, 0) to DIRECTION.RIGHT,
            Pair(0, 2) to DOWN
        )
        val move = Pair(tcol - col, trow - row)
        val direction = moveToDirection[move] ?: throw IllegalAccessException("Illegal move")
        val rowRemove = trow - direction.coordinates.second
        val colRemove = tcol - direction.coordinates.first

        if (board[rowRemove][colRemove] != FULL) {
            throw IllegalAccessException("Illegal move, empty... Remove piece")
        } else {
            board[row][col] = EMPTY
            board[rowRemove][colRemove] = EMPTY
            board[trow][tcol] = FULL
        }

        // Detect win
        if (board.flatten().count { it == FULL } == 1) {
            throw Exception("Done! You won!")
        }

        // Detect stall
        // For each F
        // For each l, u, r, d
        // Bail on first valid move?
    }
}

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

object CommandLineApplication {
    @JvmStatic
    fun main(args: Array<String>) {
        val engine = SolitaireEngine(7)
        engine.printBoard()

        engine.move(1, 3, 3, 3)
        engine.printBoard()

        engine.move(2, 1, 2, 3)
        engine.printBoard()

        engine.move(0, 2, 2, 2)
        engine.printBoard()

        engine.move(0, 4, 0, 2)
        engine.printBoard()

        engine.move(3, 2, 1, 2)
        engine.printBoard()

        engine.move(0, 2, 2, 2)
        engine.printBoard()

        engine.move(2, 3, 2, 1)
        engine.printBoard()

        engine.move(2, 0, 2, 2)
        engine.printBoard()

        engine.move(3, 0, 3, 2)
        engine.printBoard()

//        engine.move(3, 3, 2, 1)
        engine.printBoard()
    }

    private fun SolitaireEngine.printBoard() {
        for (row in board) {
            for (col in row) {
                val color = when (col.symbol) {
                    FULL.symbol -> GREEN
                    ILLEGAL.symbol -> BLACK
                    EMPTY.symbol -> BLUE
                    else -> BLACK
                }
                print("${col.symbol} ".color(color))
            }
            println()
        }
        println("=============")
    }

    private fun String.color(color: Color) = Kolor.foreground(this, color)
}

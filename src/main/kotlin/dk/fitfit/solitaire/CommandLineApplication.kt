package dk.fitfit.solitaire

import com.andreapivetta.kolor.Color
import com.andreapivetta.kolor.Kolor

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
                    SolitaireEngine.TILE.FULL.symbol -> Color.GREEN
                    SolitaireEngine.TILE.ILLEGAL.symbol -> Color.BLACK
                    SolitaireEngine.TILE.EMPTY.symbol -> Color.BLUE
                    else -> Color.BLACK
                }
                print("${col.symbol} ".color(color))
            }
            println()
        }
        println("=============")
    }

    private fun String.color(color: Color) = Kolor.foreground(this, color)
}

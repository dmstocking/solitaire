package dk.fitfit.solitaire

import dk.fitfit.solitaire.SolitaireEngine.DIRECTION.DOWN
import dk.fitfit.solitaire.SolitaireEngine.TILE.*
import java.lang.Exception

class SolitaireEngine(size: Int) {
    enum class TILE(val symbol: String) {
        ILLEGAL("I"), EMPTY("E"), FULL("F")
    }

    enum class DIRECTION(val coordinates: Pair<Int, Int>) {
        LEFT(Pair(-1, 0)), UP(Pair(0, -1)), RIGHT(Pair(1, 0)), DOWN(Pair(0, 1))
    }

    private val board: Array<Array<TILE>> = generateBoard(size)

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

    fun move(x: Int, y: Int, tx: Int, ty: Int) {
        val from = board[y][x]
        val to = board[ty][tx]

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
        val move = Pair(tx - x, ty - y)
        val direction = moveToDirection[move] ?: throw IllegalAccessException("Illegal move")

        val xRemove = tx - direction.coordinates.first
        val yRemove = ty - direction.coordinates.second

        if (board[yRemove][xRemove] != FULL) {
            throw IllegalAccessException("Illegal move, empty... Remove piece")
        } else {
            board[y][x] = EMPTY
            board[yRemove][xRemove] = EMPTY
            board[ty][tx] = FULL
        }

        // Detect win
        if (board.flatten().count {it == FULL} == 1) {
            throw Exception("Done! You ")
        }

        // Detect stall

    }

    fun printBoard() {
        for (row in board) {
            for (col in row) {
                print("${col.symbol} ")
            }
            println()
        }
        println("=============")
    }
}

fun main() {
    val engine = SolitaireEngine(7)
    engine.printBoard()

    engine.move(3, 1, 3, 3)
    engine.printBoard()

    engine.move(1, 2, 3, 2)
    engine.printBoard()

    engine.move(2, 0, 2, 2)
    engine.printBoard()

    engine.move(4, 0, 2, 0)
    engine.printBoard()

    engine.move(2, 3, 2, 1)
    engine.printBoard()

    engine.move(2, 0, 2, 2)
    engine.printBoard()

    engine.move(3, 2, 1, 2)
    engine.printBoard()

    engine.move(0, 2, 2, 2)
    engine.printBoard()

    engine.move(0, 3, 2, 3)
    engine.printBoard()
}

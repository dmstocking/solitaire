package dk.fitfit.solitaire

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StateReducer @Inject constructor() {

    fun reduce(prev: State, intent: SolitaireAction): State {
        val board = prev.board
        val pegs = prev.pegs
        return when (intent) {
            is Click -> {
                val to = intent.position
                val from = prev.selectedPosition
                if (from != null) {
                    val validMove = isValidMove(from, to, board, pegs)
                    if (validMove) {
                        val deltaX = to.x - from.x
                        val deltaY = to.y - from.y
                        val newPegs = List(pegs.size) { i ->
                            List(pegs[i].size) { j ->
                                when (i to j) {
                                    to.x to to.y -> true
                                    from.x + deltaX / 2 to from.y + deltaY / 2 -> false
                                    from.x to from.y -> false
                                    else -> pegs[i][j]
                                }
                            }
                        }
                        prev.copy(
                            state = calculateProgress(board, newPegs),
                            selectedPosition = null,
                            pegs = newPegs,
                            undoStack = prev.undoStack.plus(UndoEntry(prev.pegs)).takeLast(10),
                            redoStack = emptyList()
                        )
                    } else {
                        prev.copy(selectedPosition = null)
                    }
                } else {
                    if (pegs[to.x][to.y]) {
                        prev.copy(selectedPosition = to)
                    } else {
                        prev
                    }
                }
            }
            is Undo -> {
                val last = prev.undoStack.lastOrNull()
                if (last != null) {
                    prev.copy(
                        state = calculateProgress(board, last.pegs),
                        selectedPosition = null,
                        pegs = last.pegs,
                        undoStack = prev.undoStack.dropLast(1),
                        redoStack = prev.redoStack.plus(last).take(10)
                    )
                } else {
                    prev
                }
            }
            is Redo -> {
                val first = prev.undoStack.firstOrNull()
                if (first != null) {
                    prev.copy(
                        state = calculateProgress(board, first.pegs),
                        selectedPosition = null,
                        pegs = first.pegs,
                        undoStack = prev.undoStack.plus(first),
                        redoStack = prev.redoStack.drop(1)
                    )
                } else {
                    prev
                }
            }
            is Restart -> initialState()
        }
    }

    private fun isValidMove(from: Position, to: Position, board: List<List<Boolean>>, pegs: List<List<Boolean>>): Boolean {
        val deltaX = to.x - from.x
        val deltaY = to.y - from.y

        val isJump = listOf(
             0 to  2,
             0 to -2,
             2 to  0,
            -2 to  0
        ).any { (x, y) -> deltaX == x && deltaY == y }

        return if (isJump) {
            val hasPeg = pegs[from.x][from.y]
            val open = !pegs[to.x][to.y] && board[to.x][to.y]
            val jumpingPeg = pegs[from.x + deltaX/2][from.y + deltaY/2]
            hasPeg && open && jumpingPeg
        } else {
            false
        }
    }

    private fun calculateProgress(board: List<List<Boolean>>, pegs: List<List<Boolean>>): GameState {
        val canMove = (0 until 9).any { i ->
            (0 until 9).any { j ->
                listOf(
                     0 to  2,
                     2 to  0,
                     0 to -2,
                    -2 to  0
                )
                    .map { (offsetX, offsetY) ->
                        Position(offsetX  + i, offsetY + j) to Position(offsetX/2  + i, offsetY/2 + j)
                    }
                    .filter { (to, _) -> to.x in (0 until WIDTH) && to.y in (0 until HEIGHT) }
                    .any { (to, jumped) ->
                        board[to.x][to.y] &&
                                pegs[i][j] &&
                                pegs[jumped.x][jumped.y] &&
                                !pegs[to.x][to.y]
                    }
            }
        }
        val hasPeg = pegs.flatten().count { it == true }
        return if (hasPeg == 1) {
            GameState.WON
        } else if (!canMove) {
            GameState.LOST
        } else {
            GameState.PLAYING
        }
    }
}

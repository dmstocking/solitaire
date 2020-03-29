package dk.fitfit.solitaire

data class State(
    val state: GameState,
    val selectedPosition: Position?,
    val board: List<List<Boolean>>,
    val pegs: List<List<Boolean>>,
    val undoStack: List<UndoEntry>,
    val redoStack: List<UndoEntry>
)

enum class GameState {
    PLAYING, LOST, WON
}

data class UndoEntry(
    val pegs: List<List<Boolean>>
)

data class Position(val x: Int, val y: Int)

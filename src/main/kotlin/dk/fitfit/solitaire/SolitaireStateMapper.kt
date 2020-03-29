package dk.fitfit.solitaire

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SolitaireStateMapper @Inject constructor() {

    fun map(state: State): SolitaireViewModel {
        return SolitaireViewModel(
            showWon = state.state == GameState.WON,
            showLost = state.state == GameState.LOST,
            tiles = List(WIDTH) { i ->
                List(HEIGHT) { j ->
                    if (state.selectedPosition != null && state.selectedPosition == Position(i, j)) {
                        Color.BLUE
                    } else if (state.pegs[i][j]) {
                        Color.YELLOW
                    } else if (state.board[i][j]) {
                        Color.BROWN
                    } else {
                        Color.BLACK
                    }
                }
            },
            undoEnabled = false,
            redoEnabled = false
        )
    }
}
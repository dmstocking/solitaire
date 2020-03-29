package dk.fitfit.solitaire

sealed class SolitaireAction
data class Click(val position: Position) : SolitaireAction()
object Undo : SolitaireAction()
object Redo : SolitaireAction()
object Restart : SolitaireAction()
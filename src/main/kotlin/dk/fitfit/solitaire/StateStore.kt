package dk.fitfit.solitaire

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StateStore @Inject constructor(private val reducer: StateReducer) {

    private val actionSubject = PublishSubject.create<SolitaireAction>()

    private val publisher = actionSubject
        .scan(initialState()) { p, a -> reducer.reduce(p, a) }
        .replay(1)
        .also { it.connect() }

    fun state(): Observable<State> {
        return publisher
    }

    fun dispatch(action: SolitaireAction) {
        actionSubject.onNext(action)
    }

}

fun initialState(): State {
    val board = List(WIDTH) { i -> List(HEIGHT) { j -> i/3 == 1 || j/3 == 1 } }
    val pegs = List(WIDTH) { i ->
        List(HEIGHT) { j ->
            when (i to j) {
                4 to 4 -> false
                else -> i/3 == 1 || j/3 == 1
            }
        }
    }
    return State(GameState.PLAYING, null, board, pegs, emptyList(), emptyList())
}

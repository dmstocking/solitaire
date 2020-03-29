package dk.fitfit.solitaire

import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [])
interface MainComponent {
    fun solitaireSceneFactory(): SolitaireSceneFactory
}
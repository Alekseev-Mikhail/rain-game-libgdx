package io.github

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration

fun main() {
    val config = Lwjgl3ApplicationConfiguration()
    config.useVsync(true)
    config.setForegroundFPS(60)
    config.setWindowedMode(800, 480)
    config.setResizable(false)
    config.setTitle("Dima Lox")
    Lwjgl3Application(RainGame(), config)
}

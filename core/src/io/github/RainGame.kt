package io.github

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.ScreenUtils
import com.badlogic.gdx.utils.TimeUtils


class RainGame : ApplicationAdapter() {
    private lateinit var camera: OrthographicCamera
    private lateinit var batch: SpriteBatch
    private lateinit var bucketImage: Texture
    private lateinit var dropletImage: Texture
    private lateinit var rainMusic: Music
    private lateinit var dropSound: Sound
    private lateinit var bucket: Rectangle

    private val raindrops = mutableListOf<Rectangle>()
    private val lastDropTime: Long = 0

    override fun create() {
        camera = OrthographicCamera()
        batch = SpriteBatch()
        bucketImage = Texture("sprite/bucket.png")
        dropletImage = Texture("sprite/droplet.png")
        rainMusic = Gdx.audio.newMusic(Gdx.files.internal("sound/rain.mp3"))
        dropSound = Gdx.audio.newSound(Gdx.files.internal("sound/waterdrop.wav"))
        bucket = Rectangle()

        camera.setToOrtho(false, 800f, 480f)
        bucket.width = 64f
        bucket.height = 64f
        bucket.x = camera.viewportWidth / 2 - bucket.width / 2
        bucket.y = 20f
        rainMusic.isLooping = true
        rainMusic.volume = 0.5f
        rainMusic.play()
    }

    override fun render() {
        ScreenUtils.clear(0f, 0f, 0.2f, 1f)
        camera.update()
        batch.begin()

        val speed = 600
        if (Gdx.input.isTouched) {
            val touchPos = Vector3()
            touchPos[Gdx.input.x.toFloat(), Gdx.input.y.toFloat()] = 0f
            camera.unproject(touchPos)
            val newPosition = touchPos.x - bucket.width / 2
            if (newPosition + bucket.width < camera.viewportWidth && newPosition > 0) bucket.x = newPosition
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            if (bucket.x > 0) {
                bucket.x -= speed * Gdx.graphics.deltaTime
            } else {
                bucket.x = 0f
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            if (bucket.x + bucket.width < camera.viewportWidth) {
                bucket.x += speed * Gdx.graphics.deltaTime
            } else {
                bucket.x = camera.viewportWidth - bucket.width
            }
        }

        batch.draw(bucketImage, bucket.x, bucket.y)
        batch.end()
    }

    override fun dispose() {
        batch.dispose()
        bucketImage.dispose()
    }

    private fun spawnRaindrop() {
        val raindrop = Rectangle()
        raindrop.x = MathUtils.random(0, 800 - 64).toFloat()
        raindrop.y = 480f
        raindrop.width = 64f
        raindrop.height = 64f
        raindrops.add(raindrop)
        lastDropTime = TimeUtils.nanoTime()
    }
}

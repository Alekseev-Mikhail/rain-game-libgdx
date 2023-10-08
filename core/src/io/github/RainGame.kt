package io.github

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.ScreenUtils
import com.badlogic.gdx.utils.TimeUtils
import java.util.Timer
import kotlin.concurrent.schedule
import kotlin.random.Random
import kotlin.time.Duration.Companion.seconds

class RainGame : ApplicationAdapter() {
    private lateinit var camera: OrthographicCamera
    private lateinit var batch: SpriteBatch
    private lateinit var bucketImage: Texture
    private lateinit var dropletImage: Texture
    private lateinit var rainMusic: Music
    private lateinit var dropSound: Sound
    private lateinit var bucket: Rectangle

    private val raindrops = mutableListOf<Rectangle>()
    private var lastDropTime: Long = 0
    private val timer = Timer()

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

        if (Gdx.input.isTouched) {
            val touchPos = Vector3()
            touchPos[Gdx.input.x.toFloat(), Gdx.input.y.toFloat()] = 0f
            camera.unproject(touchPos)
            val newPosition = touchPos.x - bucket.width / 2
            if (newPosition + bucket.width < camera.viewportWidth && newPosition > 0) bucket.x = newPosition
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            if (bucket.x > 0) {
                bucket.x -= 600 * Gdx.graphics.deltaTime
            } else {
                bucket.x = 0f
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            if (bucket.x + bucket.width < camera.viewportWidth) {
                bucket.x += 600 * Gdx.graphics.deltaTime
            } else {
                bucket.x = camera.viewportWidth - bucket.width
            }
        }

        if (TimeUtils.nanoTime() - lastDropTime > 1.seconds.inWholeNanoseconds) spawnRaindrop()

        raindrops.forEach { rec ->
            rec.y -= 5
            if (rec.overlaps(bucket)) {
                dropSound.play()
                raindrops.remove(rec)
            }
        }

        batch.begin()
        batch.draw(bucketImage, bucket.x, bucket.y)
        raindrops.forEach { rec ->
            batch.draw(dropletImage, rec.x, rec.y)
        }
        batch.end()
    }

    override fun dispose() {
        batch.dispose()
        bucketImage.dispose()
        dropletImage.dispose()
        dropSound.dispose()
        rainMusic.dispose()
    }

    private fun spawnRaindrop() {
        val raindrop = Rectangle()
        raindrop.width = 64f
        raindrop.height = 64f
        raindrop.x = Random.nextInt(0, (camera.viewportWidth - raindrop.width).toInt()).toFloat()
        raindrop.y = 480f
        raindrops.add(raindrop)
        lastDropTime = TimeUtils.nanoTime()
    }
}

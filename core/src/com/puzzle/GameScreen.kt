package com.puzzle

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.puzzle.entity.EntityFactory
import com.puzzle.entity.Mappers
import com.puzzle.entity.components.HealthComponent
import com.puzzle.entity.systems.*

class GameScreen : ScreenAdapter() {
    internal val gameCamera: OrthographicCamera
    internal val engine = Main.engine
    internal val machineHealth: HealthComponent
    internal val breakSound: Sound = Main.assets.get(AssetPaths.BREAK_SOUND)
    internal var breakSoundPlayed = false

    init {
        val camWidth = Gdx.graphics.width.toFloat()
        val camHeight = Gdx.graphics.height.toFloat()
        gameCamera = OrthographicCamera(camWidth, camHeight)
        gameCamera.position.set(camWidth / 2, camHeight / 2, 0f)
        gameCamera.update()

        engine.addSystem(InputSystem(10))
        engine.addSystem(TetherSystem(20))
        engine.addSystem(SlotSystem(40))
        engine.addSystem(RenderSystem(50, gameCamera))
        engine.addSystem(ArrowSystem())
        engine.addSystem(MatchSystem())
        engine.addSystem(BufferSystem())
        engine.addSystem(FadeSystem())
        engine.addSystem(RemovalSystem())
        engine.addSystem(FallingSystem())
        engine.addSystem(PressAnimationSystem())
        engine.addSystem(HealthSystem())
        engine.addSystem(CrackingSystem())

        Gdx.input.inputProcessor = Main.engine.getSystem(InputSystem::class.java).inputProcessor

        // Fill board with squares
        for (i in 0..Constants.BOARD_SIZE - 1) {
            for (j in 0..Constants.BOARD_SIZE - 1) {
                EntityFactory.square((Math.random() * 5).toInt(), i, j)
            }
        }
        // Arrows
        for (i in 0..Constants.BOARD_SIZE - 1) {
            EntityFactory.arrow(-1, i)
            EntityFactory.arrow(i, -1)
        }

        // Backgrounds
        EntityFactory.sprite(Constants.BOARD_ORIGIN.x + 75f, Constants.BOARD_ORIGIN.y + 496f, AssetPaths.TOP_BACKGROUND)
        EntityFactory.sprite(Constants.BOARD_ORIGIN.x + 75f, Constants.BOARD_ORIGIN.y + 175f, AssetPaths.BOTTOM_BACKGROUND)

        val machine = EntityFactory.machine()
        machineHealth = Mappers.healthComponent.get(machine)
    }

    override fun render(deltaTime: Float) {
        Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT)

        engine.update(deltaTime)

        if (machineHealth.health == 0f) {
            machineHealth.health = -1f

            if (!overflow)
                broken = true

            if (!breakSoundPlayed) {
                breakSoundPlayed = true
                breakSound.play()
            }
        }
    }
}

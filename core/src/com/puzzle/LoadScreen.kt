package com.puzzle

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont

class LoadScreen : ScreenAdapter() {
    internal var minimumShowTime = 0f

    init {
        AssetPaths.textures.forEach { Main.assets.load(it, Texture::class.java) }
        AssetPaths.sounds.forEach { Main.assets.load(it, Sound::class.java) }
        Main.assets.finishLoading()

        Main.gameScreen = GameScreen()
    }

    override fun render(deltaTime: Float) {
        Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 0f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT)

        minimumShowTime -= deltaTime
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER))
            minimumShowTime = 0f
        if (minimumShowTime <= 0 && Main.assets.update())
            Main.game.screen = Main.gameScreen
    }
}
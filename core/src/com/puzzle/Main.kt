package com.puzzle

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Game
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch

class Main : ApplicationAdapter() {

    override fun create() {
        batch = SpriteBatch()
        Texture.setAssetManager(assets)
        game.create()
    }

    override fun render() {
        game.render()
    }

    override fun dispose() {
        assets.dispose()
        batch.dispose()
    }

    companion object {
        val game: Game = object : Game() {
            override fun create() {
                this.setScreen(LoadScreen())
            }
        }
        val assets = AssetManager()
        val engine = Engine()
        lateinit var batch: SpriteBatch
        lateinit var gameScreen: GameScreen
    }
}
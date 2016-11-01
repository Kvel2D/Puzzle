package com.puzzle.entity

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.badlogic.gdx.physics.box2d.PolygonShape
import com.puzzle.AssetPaths
import com.puzzle.Constants
import com.puzzle.Main
import com.puzzle.entity.components.*

object EntityFactory {
    internal var engine = Main.engine

    fun square(number: Int, x: Int, y: Int): Entity {
        val e = Entity()

        val texture: Texture = when (number) {
            0 -> Main.assets.get(AssetPaths.ONE)
            1 -> Main.assets.get(AssetPaths.TWO)
            2 -> Main.assets.get(AssetPaths.THREE)
            3 -> Main.assets.get(AssetPaths.FOUR)
            4 -> Main.assets.get(AssetPaths.FIVE)
            else -> Main.assets.get(AssetPaths.ONE)
        }
        val region = TextureRegion(texture)

        val health = 13f
        val regeneration = 0f

        val textureC = TextureComponent(region)
        val transformC = TransformComponent()
        val slotC = SlotComponent(x, y)
        val touchC = TouchComponent(x, y, Constants.SLOT_SIZE, Constants.SLOT_SIZE)
        val healthC = HealthComponent(health, regeneration)

        e.add(textureC)
                .add(transformC)
                .add(slotC)
                .add(touchC)
                .add(healthC)

        if (y < Constants.BOARD_SIZE) {
            val squareC = SquareComponent(number)
            e.add(squareC)
        } else {
            val bufferC = BufferComponent(number)
            e.add(bufferC)
        }

        crackAnimation(x, y, health, regeneration, transformC.z + 1, e)

        engine.addEntity(e)

        return e
    }

    fun arrow(x: Int, y: Int): Entity {
        val e = Entity()

        val textureDown: Texture =
                if (x == -1) Main.assets.get(AssetPaths.LEFT_DOWN)
                else if (y == -1) Main.assets.get(AssetPaths.DOWN_DOWN)
                else Main.assets.get(AssetPaths.LEFT_DOWN)
        val regionDown = TextureRegion(textureDown)
        val textureUp: Texture =
                if (x == -1) Main.assets.get(AssetPaths.LEFT)
                else if (y == -1) Main.assets.get(AssetPaths.DOWN)
                else Main.assets.get(AssetPaths.LEFT)
        val regionUp = TextureRegion(textureUp)

        val health = 12f
        val regeneration = 0.01f

        val direction =
                if (x == -1) "left"
                else "down"

        val textureC = TextureComponent(regionUp)
        val pressAnimationC = PressAnimationComponent(regionDown, regionUp)
        val transformC = TransformComponent()
        transformC.z = 100 // arrows are drawn on top of squares
        val arrowC = ArrowComponent(direction)
        val slotC = SlotComponent(x, y)
        val touchC = TouchComponent(x, y, Constants.SLOT_SIZE, Constants.SLOT_SIZE)
        val healthC = HealthComponent(health, regeneration)

        e.add(textureC)
                .add(transformC)
                .add(arrowC)
                .add(slotC)
                .add(pressAnimationC)
                .add(touchC)
                .add(healthC)
        engine.addEntity(e)

        crackAnimation(x, y, health, regeneration, transformC.z + 1, e)

        return e
    }

    fun crackAnimation(x: Int, y: Int, health: Float, regeneration: Float, z: Int, parent: Entity) {
        val cracks = Entity()

        val frames = arrayOf(
                TextureRegion(Main.assets.get(AssetPaths.CRACK0, Texture::class.java)),
                TextureRegion(Main.assets.get(AssetPaths.CRACK1, Texture::class.java)),
                TextureRegion(Main.assets.get(AssetPaths.CRACK2, Texture::class.java)),
                TextureRegion(Main.assets.get(AssetPaths.CRACK3, Texture::class.java)),
                TextureRegion(Main.assets.get(AssetPaths.CRACK4, Texture::class.java)),
                TextureRegion(Main.assets.get(AssetPaths.CRACK4, Texture::class.java)),
                TextureRegion(Main.assets.get(AssetPaths.CRACK5, Texture::class.java)),
                TextureRegion(Main.assets.get(AssetPaths.CRACK6, Texture::class.java)),
                TextureRegion(Main.assets.get(AssetPaths.CRACK7, Texture::class.java)),
                TextureRegion(Main.assets.get(AssetPaths.CRACK8, Texture::class.java)),
                TextureRegion(Main.assets.get(AssetPaths.CRACK9, Texture::class.java)),
                TextureRegion(Main.assets.get(AssetPaths.CRACK10, Texture::class.java)))

        val textureC = TextureComponent(frames[0])
        val transformC = TransformComponent()
        transformC.z = z
        val touchC = TouchComponent(x, y, Constants.SLOT_SIZE, Constants.SLOT_SIZE)
        val healthC = HealthComponent(health, regeneration)
        val colorC = ColorComponent(Color(1f, 1f, 1f, 0.5f))
        val crackC = CrackComponent(frames)
        val tetherC = TetherComponent(parent, 0f, 0f, 0f, false)

        cracks.add(textureC)
                .add(transformC)
                .add(touchC)
                .add(healthC)
                .add(colorC)
                .add(crackC)
                .add(tetherC)
        engine.addEntity(cracks)
    }

    fun sprite(x: Float, y: Float, textureFile: String): Entity {
        val e = Entity()

        val texture: Texture = Main.assets.get(textureFile)
        val region = TextureRegion(texture)

        val textureC = TextureComponent(region)
        val transformC = TransformComponent()
        transformC.x = x
        transformC.y = y
        transformC.z = 10 // background is between arrows and squares

        e.add(textureC)
                .add(transformC)
        engine.addEntity(e)

        return e
    }

    fun machine(): Entity {
        val e = Entity()
        val health = 12f
        val regeneration = 0f
        val texture: Texture = Main.assets.get(AssetPaths.TOP)
        val region = TextureRegion(texture)

        run {
            val textureC = TextureComponent(region)
            val transformC = TransformComponent()
            transformC.x = Constants.BOARD_ORIGIN.x + 75f
            transformC.y = Constants.BOARD_ORIGIN.y + 545f
            transformC.z = 10 // background is between arrows and squares
            val touchC = TouchComponent(0, 0, region.regionWidth.toFloat(), region.regionHeight.toFloat())
            val healthC = HealthComponent(health, regeneration)

            e.add(textureC)
                    .add(transformC)
                    .add(touchC)
                    .add(healthC)
            engine.addEntity(e)
        }

        run {
            val cracks = Entity()

            val frames = arrayOf(
                    TextureRegion(Main.assets.get(AssetPaths.CRACK_TOP0, Texture::class.java)),
                    TextureRegion(Main.assets.get(AssetPaths.CRACK_TOP1, Texture::class.java)),
                    TextureRegion(Main.assets.get(AssetPaths.CRACK_TOP2, Texture::class.java)),
                    TextureRegion(Main.assets.get(AssetPaths.CRACK_TOP3, Texture::class.java)),
                    TextureRegion(Main.assets.get(AssetPaths.CRACK_TOP4, Texture::class.java)),
                    TextureRegion(Main.assets.get(AssetPaths.CRACK_TOP5, Texture::class.java)),
                    TextureRegion(Main.assets.get(AssetPaths.CRACK_TOP6, Texture::class.java)),
                    TextureRegion(Main.assets.get(AssetPaths.CRACK_TOP7, Texture::class.java)),
                    TextureRegion(Main.assets.get(AssetPaths.CRACK_TOP8, Texture::class.java)),
                    TextureRegion(Main.assets.get(AssetPaths.CRACK_TOP9, Texture::class.java)),
                    TextureRegion(Main.assets.get(AssetPaths.CRACK_TOP10, Texture::class.java)))

            val textureC = TextureComponent(frames[0])
            val transformC = TransformComponent()
            transformC.z = 200 // cracks are drawn on top of arrows
            val touchC = TouchComponent(0, 0, region.regionWidth.toFloat(), region.regionHeight.toFloat())
            val healthC = HealthComponent(health, regeneration)
            val crackC = CrackComponent(frames)
            val tetherC = TetherComponent(e, 0f, 0f, 0f, false)

            cracks.add(textureC)
                    .add(transformC)
                    .add(touchC)
                    .add(healthC)
                    .add(crackC)
                    .add(tetherC)
            engine.addEntity(cracks)
        }

        return e
    }
}

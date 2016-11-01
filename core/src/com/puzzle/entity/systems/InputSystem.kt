package com.puzzle.entity.systems

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.utils.ImmutableArray
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.math.Vector2
import com.puzzle.Constants
import com.puzzle.Main
import com.puzzle.entity.Mappers
import com.puzzle.entity.components.*

class InputSystem : EntitySystem {
    private lateinit var entities: ImmutableArray<Entity>
    private lateinit var pressAnimations: ImmutableArray<Entity>
    private lateinit var arrows: ImmutableArray<Entity>
    private lateinit var healths: ImmutableArray<Entity>
    private var blockedInputTimer = 0f
    val inputProcessor = MyInputProcessor()

    constructor()

    constructor(priority: Int) :
    super(priority)

    override fun addedToEngine(engine: Engine) {
        entities = engine.getEntitiesFor(Family.all(TouchComponent::class.java).get())
        pressAnimations = engine.getEntitiesFor(Family.all(PressAnimationComponent::class.java).get())
        arrows = engine.getEntitiesFor(Family.all(ArrowComponent::class.java).get())
        healths = engine.getEntitiesFor(Family.all(HealthComponent::class.java).get())
    }

    override fun update(deltaTime: Float) {
        if (blockedInputTimer > 0f)
            blockedInputTimer -= Constants.TIME_STEP
        if (blockedInputTimer <= 0f) {
            Gdx.input.inputProcessor = inputProcessor
            Main.engine.getSystem(MatchSystem::class.java).setProcessing(true)
        }

        entities.forEach {
            updateBounds(it)
        }
    }

    private fun updateBounds(entity: Entity) {
        val transformC = Mappers.transformComponent.get(entity)
        val touchC = Mappers.touchComponent.get(entity)

        touchC.bounds.x = transformC.x - touchC.bounds.width / 2
        touchC.bounds.y = transformC.y - touchC.bounds.height / 2

        // Move hitboxes of slots moving behind arrows, so that clicking arrows doesn't click on them
        if (Mappers.slotComponent.has(entity)) {
            val slotC = Mappers.slotComponent.get(entity)

            if ( (slotC.x == 0 && slotC.xOffset < 0)
                || (slotC.y == 0 && slotC.yOffset < 0))
                touchC.bounds.x = -1000f
        }
    }

    fun blockInput() {
        Gdx.input.inputProcessor = null
        blockedInputTimer = Constants.SQUARE_DISAPPEAR_TIME + 0.1f
    }

    inner class MyInputProcessor() : InputAdapter() {

        override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
            val touch = Vector2(screenX.toFloat(), Constants.VIEWPORT_HEIGHT.toFloat() - screenY)

            entities.forEach {
                val touchC = Mappers.touchComponent.get(it)
                if (touchC.bounds.contains(touch)) {
                    touchC.touched = true
                }
            }

            pressAnimations.forEach {
                Mappers.pressAnimationComponent.get(it).updated = false
            }

            return false
        }

        override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
            entities.forEach {
                val touchC = Mappers.touchComponent.get(it)
                touchC.touched = false
            }

            pressAnimations.forEach {
                Mappers.pressAnimationComponent.get(it).updated = false
            }
            arrows.forEach {
                Mappers.arrowComponent.get(it).updated = false
            }
            healths.forEach {
                Mappers.healthComponent.get(it).updated = false
            }

            return false
        }
    }
}



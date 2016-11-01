package com.puzzle.entity.systems

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.utils.ImmutableArray
import com.puzzle.Constants
import com.puzzle.entity.Mappers
import com.puzzle.entity.components.ArrowComponent
import com.puzzle.entity.components.SlotComponent
import com.puzzle.entity.components.SquareComponent

/*
 * Move a line of squares when arrow entity is touched
 */
class ArrowSystem : EntitySystem {
    lateinit var arrows: ImmutableArray<Entity>
    lateinit var squares: ImmutableArray<Entity>

    constructor()

    constructor(priority: Int) :
    super(priority)

    override fun addedToEngine(engine: Engine) {
        arrows = engine.getEntitiesFor(Family.all(ArrowComponent::class.java, SlotComponent::class.java).get())
        squares = engine.getEntitiesFor(Family.all(SquareComponent::class.java, SlotComponent::class.java).get())
    }

    override fun update(deltaTime: Float) {
        arrows.forEach {
            processEntity(it)
        }
    }

    private fun processEntity(entity: Entity) {
        val arrowC = Mappers.arrowComponent.get(entity)
        val slotC = Mappers.slotComponent.get(entity)
        val touchC = Mappers.touchComponent.get(entity)

        if (touchC != null) { // arrows can have no touchC if they were "broken"(touchC is removed in HealthS)

            if (!arrowC.updated && touchC.touched && arrowC.movementTimer <= 0f) {
                arrowC.movementTimer = arrowC.movementDuration
                arrowC.updated = true
            }
        }

        if (arrowC.movementTimer > 0f) { // moving in progress
            arrowC.movementTimer -= Constants.TIME_STEP

            val movementProgress = 1f - arrowC.movementTimer / arrowC.movementDuration

            if (arrowC.left) {
                squares.forEach {
                    val squareSC = Mappers.slotComponent.get(it)
                    if (squareSC.y == slotC.y) {
                        squareSC.xOffset = Math.max(-Constants.SLOT_SIZE * movementProgress, -Constants.SLOT_SIZE)
                    }
                }
            } else if (arrowC.down) {
                squares.forEach {
                    val squareSC = Mappers.slotComponent.get(it)
                    if (squareSC.x == slotC.x) {
                        squareSC.yOffset = Math.max(-Constants.SLOT_SIZE * movementProgress, -Constants.SLOT_SIZE)
                    }
                }
            }
        }
        else if (arrowC.movementTimer != -1f) { // end movement and turn off timer(set to -1)
            arrowC.movementTimer = -1f

            if (arrowC.left) {
                squares.forEach {
                    val squareSC = Mappers.slotComponent.get(it)
                    if (squareSC.y == slotC.y) {
                        squareSC.xOffset = 0f
                        squareSC.x--
                        if (squareSC.x == -1) {
                            squareSC.x = Constants.BOARD_SIZE - 1
                        }
                    }
                }
            } else if (arrowC.down) {
                squares.forEach {
                    val squareSC = Mappers.slotComponent.get(it)
                    if (squareSC.x == slotC.x) {
                        squareSC.yOffset = 0f
                        squareSC.y--
                        if (squareSC.y == -1) {
                            squareSC.y = Constants.BOARD_SIZE - 1
                        }
                    }
                }
            }
        }
    }
}
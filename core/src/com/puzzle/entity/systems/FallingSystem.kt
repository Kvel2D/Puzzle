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

class FallingSystem : EntitySystem {
    private lateinit var slots: ImmutableArray<Entity>
    private var board = Array(Constants.BOARD_SIZE, { i ->
        Array(20, { j -> 0 }) // 0 - empty space, 1 - occupied by a slot
    })

    constructor()

    constructor(priority: Int) :
    super(priority)

    override fun addedToEngine(engine: Engine) {
        slots = engine.getEntitiesFor(Family.all(SlotComponent::class.java).exclude(ArrowComponent::class.java).get())
    }

    override fun update(deltaTime: Float) {
        board.forEach {
            it.fill(0)
        }

        // find spaces
        slots.forEach {
            val slotC = Mappers.slotComponent.get(it)
            board[slotC.x][slotC.y] = 1
        }

        // fall slots(squares and buffer)
        slots.forEach {
            fallSlot(it)
        }
    }

    private fun fallSlot(entity: Entity) {
        val slotC = Mappers.slotComponent.get(entity)

        // bottom squares don't fall
        if (slotC.y == 0)
            return

        // fall square while there's empty space below
        // falling speed is the same as the speed of arrows
        for (i in 0..board[slotC.x].size - 1) {
            if (board[slotC.x][i] == 0 && i < slotC.y) {
                slotC.yOffset -= Constants.SLOT_SIZE * Constants.TIME_STEP / Constants.MOVEMENT_TIME

                if (slotC.yOffset <= -Constants.SLOT_SIZE) {
                    slotC.yOffset = 0f
                    slotC.y--
                }

                return
            }
        }
    }
}
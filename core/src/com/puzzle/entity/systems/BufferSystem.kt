package com.puzzle.entity.systems

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.ashley.utils.ImmutableArray
import com.puzzle.Constants
import com.puzzle.broken
import com.puzzle.entity.EntityFactory
import com.puzzle.entity.Mappers
import com.puzzle.entity.components.BufferComponent
import com.puzzle.entity.components.SlotComponent
import com.puzzle.entity.components.SquareComponent
import com.puzzle.overflow

/*
 * Update buffer entities to squares if they reach board and drop new buffer
 */
class BufferSystem : EntitySystem {
    lateinit var entities: ImmutableArray<Entity>
    private var dropTimer = Constants.BUFFER_DROP_FREQUENCY

    constructor()

    constructor(priority: Int) :
    super(priority)

    override fun addedToEngine(engine: Engine) {
        entities = engine.getEntitiesFor(Family.all(BufferComponent::class.java, SlotComponent::class.java).get())
    }

    override fun update(deltaTime: Float) {
        // Convert buffer entities to square entities, if they reached the board
        entities.forEach {
            val slotC = Mappers.slotComponent.get(it)

            if (slotC.y < Constants.BOARD_SIZE) {
                val bc = Mappers.bufferComponent.get(it)
                val squareC = SquareComponent(bc.number)

                it.remove(BufferComponent::class.java)
                it.add(squareC)
            }
        }

        // Don't drop more buffer if any ending reached
        if (overflow || broken)
            return

        // Drop buffer
        dropTimer -= Constants.TIME_STEP
        if (dropTimer <= 0f) {
            dropTimer = Constants.BUFFER_DROP_FREQUENCY

            for (i in 0..Constants.BOARD_SIZE - 1) {
                EntityFactory.square((Math.random() * 5).toInt(), i, 11)
            }
        }

        // Check for overflow
        entities.forEach {
            val slotC = Mappers.slotComponent.get(it)
            if (slotC.yOffset == 0f && slotC.y == 11)
                overflow = true
        }
    }
}


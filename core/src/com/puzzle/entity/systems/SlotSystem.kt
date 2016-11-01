package com.puzzle.entity.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.puzzle.Constants
import com.puzzle.entity.Mappers
import com.puzzle.entity.components.SlotComponent
import com.puzzle.entity.components.TransformComponent

/*
 * Slot entities' transform components into a position on the board
 */
class SlotSystem : IteratingSystem {

    constructor() :
    super(Family.all(SlotComponent::class.java, TransformComponent::class.java).get())

    constructor(priority: Int) :
    super(Family.all(SlotComponent::class.java, TransformComponent::class.java).get(), priority)

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val tc = Mappers.transformComponent.get(entity)
        val sc = Mappers.slotComponent.get(entity)

        tc.x = Constants.BOARD_ORIGIN.x + sc.x * Constants.SLOT_SIZE + sc.xOffset
        tc.y = Constants.BOARD_ORIGIN.y + sc.y * Constants.SLOT_SIZE + sc.yOffset
    }
}

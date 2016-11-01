package com.puzzle.entity.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.Vector2
import com.puzzle.Constants
import com.puzzle.entity.Mappers
import com.puzzle.entity.components.TetherComponent
import com.puzzle.entity.components.TransformComponent

class TetherSystem : IteratingSystem {

    constructor() :
    super(Family.all(TetherComponent::class.java, TransformComponent::class.java).get())

    constructor(priority: Int) :
    super(Family.all(TetherComponent::class.java, TransformComponent::class.java).get(), priority)

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val tether = Mappers.tetherComponent.get(entity)

        val tc = Mappers.transformComponent.get(entity)
        val tetherTransform = Mappers.transformComponent.get(tether.entity)

        if (tether.rotated) {
            val position = Vector2(tetherTransform.x + tether.xOffset,
                    tetherTransform.y + tether.yOffset)
            position.rotateAround(tetherTransform.x, tetherTransform.y, tetherTransform.angle)
            tc.x = position.x
            tc.y = position.y
            tc.angle = tetherTransform.angle + tether.angleOffset
        } else {
            tc.x = tetherTransform.x + tether.xOffset
            tc.y = tetherTransform.y + tether.yOffset
            tc.angle = tetherTransform.angle + tether.angleOffset
        }
    }

    fun Vector2.rotateAround(xOrigin: Float, yOrigin: Float, angle: Float) {
        val angleConverted = angle * Constants.DEGTORAD

        val cos = Math.cos(angleConverted.toDouble()).toFloat()
        val sin = Math.sin(angleConverted.toDouble()).toFloat()

        this.add(-xOrigin, -yOrigin)
        val temp = this.cpy()
        this.x = temp.x * cos - temp.y * sin
        this.y = temp.x * sin + temp.y * cos
        this.add(xOrigin, yOrigin)
    }
}
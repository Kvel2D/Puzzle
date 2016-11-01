package com.puzzle.entity

import com.badlogic.ashley.core.ComponentMapper
import com.puzzle.entity.components.*

object Mappers {
    val arrowComponent = ComponentMapper.getFor(ArrowComponent::class.java)
    val bufferComponent = ComponentMapper.getFor(BufferComponent::class.java)
    val colorComponent = ComponentMapper.getFor(ColorComponent::class.java)
    val crackComponent = ComponentMapper.getFor(CrackComponent::class.java)
    val fadeComponent = ComponentMapper.getFor(FadeComponent::class.java)
    val healthComponent = ComponentMapper.getFor(HealthComponent::class.java)
    val pressAnimationComponent = ComponentMapper.getFor(PressAnimationComponent::class.java)
    val removeComponent = ComponentMapper.getFor(RemoveComponent::class.java)
    val slotComponent = ComponentMapper.getFor(SlotComponent::class.java)
    val squareComponent = ComponentMapper.getFor(SquareComponent::class.java)
    val tetherComponent = ComponentMapper.getFor(TetherComponent::class.java)
    val textureComponent = ComponentMapper.getFor(TextureComponent::class.java)
    val touchComponent = ComponentMapper.getFor(TouchComponent::class.java)
    val transformComponent = ComponentMapper.getFor(TransformComponent::class.java)
}
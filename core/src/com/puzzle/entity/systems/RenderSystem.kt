package com.puzzle.entity.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.SortedIteratingSystem
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.puzzle.Main
import com.puzzle.entity.Mappers
import com.puzzle.entity.components.TextureComponent
import com.puzzle.entity.components.TransformComponent
import java.util.*

class RenderSystem : SortedIteratingSystem {
    private val batch = Main.batch
    private val camera: OrthographicCamera
    private var viewportWidth = 0f
    private var viewportHeight = 0f

    constructor(camera: OrthographicCamera) :
    super(Family.all(TextureComponent::class.java, TransformComponent::class.java).get(), ZComparator()) {
        this.camera = camera
    }

    constructor(priority: Int, camera: OrthographicCamera) :
    super(Family.all(TextureComponent::class.java, TransformComponent::class.java).get(), ZComparator(), priority) {
        this.camera = camera
    }

    override fun update(deltaTime: Float) {
        viewportWidth = camera.viewportWidth * camera.zoom
        viewportHeight = camera.viewportHeight * camera.zoom

        batch.projectionMatrix = camera.combined
        batch.begin()
        super.update(deltaTime)
        batch.end()
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val transformC = Mappers.transformComponent.get(entity)
        val textureC = Mappers.textureComponent.get(entity)

        val scale = transformC.scale
        val width = textureC.region.regionWidth.toFloat()
        val height = textureC.region.regionHeight.toFloat()
        val originX = 0.5f * width
        val originY = 0.5f * height

        if (!isInFrustum(transformC, width, height, originX, originY, scale))
            return

        val colorComponent = Mappers.colorComponent.get(entity)
        batch.color =
                if (colorComponent != null)
                    colorComponent.color
                else Color.WHITE

        batch.draw(textureC.region,
                transformC.x - originX,
                transformC.y - originY,
                originX,
                originY,
                width,
                height,
                scale,
                scale,
                transformC.angle)
    }

    private fun isInFrustum(transform: TransformComponent, width: Float, height: Float, originX: Float, originY: Float, scale: Float): Boolean {
        val leftX = transform.x + width * scale - originX
        val rightX = transform.x - originX
        val lowY = transform.y + height * scale - originY
        val highY = transform.y - originY

        return leftX > camera.position.x - viewportWidth / 2
                && rightX < camera.position.x + viewportWidth / 2
                && lowY > camera.position.y - viewportHeight / 2
                && highY < camera.position.y + viewportHeight / 2
    }

    private class ZComparator : Comparator<Entity> {
        override fun compare(e1: Entity, e2: Entity): Int {
            return Integer.signum(Mappers.transformComponent.get(e1).z - Mappers.transformComponent.get(e2).z)
        }
    }
}
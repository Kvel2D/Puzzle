package com.puzzle.entity.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.Color
import com.puzzle.AssetPaths
import com.puzzle.Constants
import com.puzzle.Main
import com.puzzle.entity.Mappers
import com.puzzle.entity.components.*
import com.puzzle.score

/*
 * Perform "matches", initiate removal of matched squares, play matching sound
 * Shuts down input for a period of time after a match
 */
class MatchSystem : IteratingSystem {
    internal var rows = Array(Constants.BOARD_SIZE, { i -> intArrayOf(-1, 0) })
    internal var columns = Array(Constants.BOARD_SIZE, { i -> intArrayOf(-1, 0) })
    internal var removedSquares = arrayListOf<Entity>()
    internal val matchSound: Sound = Main.assets.get(AssetPaths.MATCH_SOUND)

    constructor() :
    super(Family.all(SquareComponent::class.java, SlotComponent::class.java).get()) {
    }

    constructor(priority: Int) :
    super(Family.all(SquareComponent::class.java, SlotComponent::class.java).get(), priority) {
    }

    override fun update(deltaTime: Float) {
        super.update(deltaTime)

        rows.forEach {
            if (it[1] == Constants.BOARD_SIZE)
                matchRow(rows.indexOf(it))
            it[0] = -1
            it[1] = 0
        }

        columns.forEach {
            if (it[1] == Constants.BOARD_SIZE)
                matchColumn(columns.indexOf(it))
            it[0] = -1
            it[1] = 0
        }

        removedSquares.forEach {
            removeSquare(it)
            score += 250
        }
        // if there were matches, shut down the system until matches are cleared
        if (removedSquares.size != 0) {
            Main.engine.getSystem(InputSystem::class.java).blockInput()
            this.setProcessing(false)
            matchSound.play()
        }
        removedSquares.clear()
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val slotC = Mappers.slotComponent.get(entity)
        val squareC = Mappers.squareComponent.get(entity)

        // non-empty cases
        if (rows[slotC.y][0] == squareC.number)
            rows[slotC.y][1]++
        if (columns[slotC.x][0] == squareC.number)
            columns[slotC.x][1]++

        // empty cases
        if (rows[slotC.y][0] == -1) {
            rows[slotC.y][0] = squareC.number
            rows[slotC.y][1] = 1
        }
        if (columns[slotC.x][0] == -1) {
            columns[slotC.x][0] = squareC.number
            columns[slotC.x][1] = 1
        }
    }

    private fun matchRow(y: Int) {
        entities.forEach {
            val slotC = Mappers.slotComponent.get(it)
            if (slotC.y == y) {
                if (!removedSquares.contains(it))
                    removedSquares.add(it)
            }
        }
    }

    private fun matchColumn(x: Int) {
        entities.forEach {
            val slotC = Mappers.slotComponent.get(it)
            if (slotC.x == x) {
                if (!removedSquares.contains(it))
                    removedSquares.add(it)
            }
        }
    }

    private fun removeSquare(entity: Entity) {
        val fadeC = FadeComponent(Constants.SQUARE_DISAPPEAR_TIME)
        val removalC = RemoveComponent(Constants.SQUARE_DISAPPEAR_TIME)
        val colorC = ColorComponent(Color.WHITE) // add color, so that fade has an alpha value to change

        entity.add(fadeC)
                .add(colorC)
                .add(removalC)
    }
}

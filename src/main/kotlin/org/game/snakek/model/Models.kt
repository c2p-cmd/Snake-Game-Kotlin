package org.game.snakek.model

import org.game.snakek.gui.HEIGHT
import org.game.snakek.gui.WIDTH

class Holes(
    var x: Int, var y: Int
) {
    override fun toString(): String =
        "$x, $y"

    fun isEqualTo(other: Holes): Boolean =
        (x == other.x) && (y == other.y)
}

enum class Direction {
    LEFT, RIGHT, UP, DOWN
}

fun createSnake(): ArrayList<Holes> = arrayListOf(
    // snakes default parts
    Holes(WIDTH / 2, HEIGHT / 2),
    Holes(WIDTH / 2, HEIGHT / 2),
    Holes(WIDTH / 2, HEIGHT / 2)
)
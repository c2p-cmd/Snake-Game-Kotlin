package org.game.snakek.model

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color

import org.game.snakek.gui.*

val foodColors = listOf(Color.PURPLE, Color.LIGHTBLUE, Color.YELLOW, Color.PINK, Color.ORANGE, Color.WHITE)

fun GameController.tick(graphicsContext: GraphicsContext) {
    if (isGameOver) {
        showTextOnCanvas("game over!".uppercase(), Color.RED)
        return
    }

    for (i in snake.size - 1 downTo 1) {
        snake[i].x = snake[i-1].x
        snake[i].y = snake[i-1].y
    }

    when (direction) {
        Direction.UP -> {
            snake[0].y--
            if (snake[0].y < 0) {
                isGameOver = true
            }
        }
        Direction.DOWN -> {
            snake[0].y++
            if (snake[0].y > HEIGHT) {
                isGameOver = true
            }
        }
        Direction.LEFT -> {
            snake[0].x--
            if (snake[0].x < 0){
                isGameOver = true
            }
        }
        Direction.RIGHT -> {
            snake[0].x++
            if (snake[0].x > WIDTH){
                isGameOver = true
            }
        }
    }

    // eating food
    if (food.isEqualTo(snake[0])) {
        snake.add(Holes(-1, -1))
        generateFood()
    }

    // self destroy
    for (i in 1 until snake.size) {
        if (snake[0].isEqualTo(snake[i])) {
            isGameOver = true
            break
        }
    }

    // Re Painting background
    graphicsContext.fill = Color.DIMGRAY
    graphicsContext.fillRect(0.0, 0.0, (WIDTH * HOLE_SIZE).toDouble(), (HEIGHT * HOLE_SIZE).toDouble())

    // score
    scoreField.text = "Score: ${SNAKE_SPEED -6}"

    // changing food color
    graphicsContext.fill = when(foodColorKey) {
        0 -> foodColors[0]
        1 -> foodColors[1]
        2 -> foodColors[2]
        3 -> foodColors[3]
        4 -> foodColors[4]
        else -> foodColors[5]
    }
    // food
    graphicsContext.fillOval(
        (food.x * HOLE_SIZE).toDouble(), food.y * HOLE_SIZE.toDouble(),
        HOLE_SIZE.toDouble(), HOLE_SIZE.toDouble()
    )

    for (hole in snake) {
        graphicsContext.fill = Color.LIGHTCYAN
        graphicsContext.fillRect(
            hole.x * HOLE_SIZE.toDouble(),
            hole.y * HOLE_SIZE.toDouble(),
            HOLE_SIZE-1.0, HOLE_SIZE-1.0
        )

        graphicsContext.fill = Color.DARKSLATEBLUE
        graphicsContext.fillRect(
            hole.x * HOLE_SIZE.toDouble(),
            hole.y * HOLE_SIZE.toDouble(),
            HOLE_SIZE-2.0, HOLE_SIZE-2.0
        )
    }
}

// generate food
fun GameController.generateFood() {
    start@ while (true) {
        food.x = (0 until WIDTH).random()
        food.y = (0 until HEIGHT).random()

        for (hole in snake) {
            if (hole.x == food.x && hole.y == food.y) {
                continue@start
            }
        }

        foodColorKey = (0 until 5).random()
        SNAKE_SPEED++
        break
    }
}
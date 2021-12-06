package org.game.snakek.gui

import javafx.animation.Animation
import javafx.animation.AnimationTimer
import javafx.animation.KeyFrame
import javafx.animation.Timeline
import javafx.application.Platform

import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.fxml.Initializable
import javafx.scene.canvas.Canvas
import javafx.scene.control.Alert

import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.text.Font
import javafx.scene.text.Text
import javafx.util.Duration

import org.game.snakek.model.*
import java.net.URL

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import java.util.*
import kotlin.concurrent.thread
import kotlin.system.exitProcess


var SNAKE_SPEED: Long = 5L
val INTERVAL: Long = 1000000000 / SNAKE_SPEED

const val WIDTH = 20
const val HEIGHT = 20
const val HOLE_SIZE = 25

var startFlag = true

class GameController : Initializable {
    @FXML
    lateinit var rootVBox: VBox

    @FXML
    lateinit var canvas: Canvas

    @FXML
    lateinit var scoreField: Text

    @FXML
    lateinit var timeField: Text

    val snake = createSnake()

    var isGameOver = false

    var direction: Direction = Direction.LEFT

    val food = Holes(0, 0)

    var foodColorKey = 0

    override fun initialize(url: URL?, resourceBundle: ResourceBundle?) {
        generateFood()

        canvas.height = HEIGHT * HOLE_SIZE.toDouble()
        canvas.width = WIDTH * HOLE_SIZE.toDouble()

        canvas.isFocusTraversable = true
        canvas.addEventFilter(KeyEvent.KEY_PRESSED) { key ->
            if (key.code == KeyCode.UP && direction != Direction.DOWN) {
                direction = Direction.UP
            }
            if (key.code == KeyCode.DOWN && direction != Direction.UP) {
                direction = Direction.DOWN
            }
            if (key.code == KeyCode.LEFT && direction != Direction.RIGHT) {
                direction = Direction.LEFT
            }
            if (key.code == KeyCode.RIGHT && direction != Direction.LEFT) {
                direction = Direction.RIGHT
            }
            if (key.code == KeyCode.Q) {
                Platform.exit()
                exitProcess(0)
            }
            if (key.code == KeyCode.R) {
                restartGame()
            }
            if (key.code == KeyCode.P) {
                pauseGame(true)
            }
        }

        if (startFlag) {
            showInstructions()
            startFlag = false
        }

        pauseGame(false)
        thread {
            currentTime.start()
        }
    }

    fun showTextOnCanvas(
        msg: String, color: Color
    ): Unit = canvas.graphicsContext2D.let { graphicsContext ->
            graphicsContext.fill = color
            graphicsContext.font = Font("", 42.0)
            graphicsContext.fillText(
                msg,
                100.0, 250.0
            )
        }

    private fun pauseGame(isPause: Boolean) {
        if (isGameOver) {
            restartGame()
        }
        if (isPause) {
            showTextOnCanvas(
                msg = "paused...".uppercase(),
                color = Color.LIGHTCYAN
            )
            ticker.stop()
        }
        if (!isPause) ticker.start()
    }

    private val currentTime = object : AnimationTimer() {
        override fun handle(now: Long) {
            initClock()
        }
    }

    val ticker = object : AnimationTimer() {
        var lastTick: Long = 0

        override fun handle(now: Long) {
            if (lastTick == 0L) {
                lastTick = now
                tick(canvas.graphicsContext2D)
                return
            }

            if (now - lastTick > INTERVAL) {
                lastTick = now
                tick(canvas.graphicsContext2D)
            }
        }
    }
}

private fun GameController.restartGame() {
    SNAKE_SPEED = 5L
    ticker.stop()
    scoreField.text = "Score: 0"

    val scene = rootVBox.scene
    scene.root = FXMLLoader.load(
        Main::class.java.getResource("game.fxml")
    )
}

private fun showInstructions() {
    val instructionsAlert = Alert(Alert.AlertType.INFORMATION)

    // setting fields
    instructionsAlert.title = "Instructions"
    instructionsAlert.headerText = "How To Play Snake?"
    instructionsAlert.contentText = """
    Snake is a simple game:
        ->To move the snake use the arrows keys ⬆,➡,⬇,⬅
        ->To Pause the game press 'P'
        ->To Restart the game press 'R'
        ->To Quit the game press 'Q'
    Hope you like it!
    """.trimIndent()

    // 20% increase in width and height
    instructionsAlert.height = 275.0
    instructionsAlert.width = 395.0

    // show instructions alert
    instructionsAlert.showAndWait()
}

fun GameController.initClock(): Unit =
    Timeline(KeyFrame(Duration.ZERO, {
        // getting current time
        timeField.text = LocalDateTime.now()
            .format(
                DateTimeFormatter.ofPattern("HH:mm:ss")
            )
    }), KeyFrame(Duration.seconds(1.0))).let { clock ->
        clock.cycleCount = Animation.INDEFINITE
        clock.play()
    }
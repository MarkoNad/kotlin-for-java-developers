package games.gameOfFifteen

import board.Direction
import board.createGameBoard
import games.game.Game

/*
 * Implement the Game of Fifteen (https://en.wikipedia.org/wiki/15_puzzle).
 * When you finish, you can play the game by executing 'PlayGameOfFifteen'.
 */
fun newGameOfFifteen(initializer: GameOfFifteenInitializer = RandomGameInitializer()): Game =
        GameOfFifteen(initializer)

class GameOfFifteen(private val initializer: GameOfFifteenInitializer) : Game {
    private val board = createGameBoard<Int?>(4)

    override fun initialize() {
        val initialPermutation = initializer.initialPermutation

        board.getAllCells()
                .zip(initialPermutation)
                .forEach { (cell, value) -> board[cell] = value }
    }

    override fun canMove() = !hasWon()

    override fun hasWon(): Boolean {
        val endGameList: MutableList<Int?> = (1..15).toMutableList()
        endGameList.add(null)
        return board.getAllCells().map { cell -> board[cell] } == endGameList
    }

    override fun processMove(direction: Direction) {
        with(board) {
            val emptyCell = board.find { it == null } ?: throw IllegalStateException("Empty cell not found.")
            val neighbour = emptyCell.getNeighbour(direction.reversed()) ?: throw IllegalArgumentException("Illegal move.")
            set(emptyCell, get(neighbour))
            set(neighbour, null)
        }
    }

    override fun get(i: Int, j: Int): Int? = board.run { get(getCell(i, j)) }
}

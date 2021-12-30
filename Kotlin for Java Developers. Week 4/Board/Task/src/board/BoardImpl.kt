package board

open class SquareBoardImpl(final override val width: Int) : SquareBoard {

    protected val cells: List<Cell>

    init {
        cells = ArrayList()
        for (i in 1..width) {
            for (j in 1..width) {
                cells.add(Cell(i, j))
            }
        }
    }

    override fun getCellOrNull(i: Int, j: Int): Cell? {
        if (isOutsideRange(i, j)) {
            return null
        }
        return cells[(i - 1) * width + j - 1]
    }

    override fun getCell(i: Int, j: Int): Cell {
        if(isOutsideRange(i, j)) {
            throw IllegalArgumentException("Illegal combination (i, j) = ($i, $j) for width $width.")
        }
        return cells[(i - 1) * width + j - 1]
    }

    private fun isOutsideRange(i: Int, j: Int) = i < 1 || j < 1 || i > width || j > width

    override fun getAllCells(): Collection<Cell> = cells

    override fun getRow(i: Int, jRange: IntProgression): List<Cell> {
        return jRange.mapNotNull { j -> getCellOrNull(i, j) }
    }

    override fun getColumn(iRange: IntProgression, j: Int): List<Cell> {
        return iRange.mapNotNull { i -> getCellOrNull(i, j) }
    }

    override fun Cell.getNeighbour(direction: Direction): Cell? {
        return when(direction) {
            Direction.UP -> getCellOrNull(i - 1, j)
            Direction.DOWN -> getCellOrNull(i + 1, j)
            Direction.RIGHT -> getCellOrNull(i, j + 1)
            Direction.LEFT -> getCellOrNull(i, j - 1)
        }
    }
}

class GameBoardImpl<T>(width: Int) : SquareBoardImpl(width), GameBoard<T> {

    private val cellToData: MutableMap<Cell, T?>

    init {
        cellToData = HashMap()
    }

    override operator fun get(cell: Cell): T? {
        return cellToData[cell]
    }

    override operator fun set(cell: Cell, value: T?) {
        cellToData[cell] = value
    }

    override fun filter(predicate: (T?) -> Boolean): Collection<Cell> = cellToData.filter { entry -> predicate(entry.value) }.keys.toList()

    override fun find(predicate: (T?) -> Boolean): Cell? = cellToData.filter { entry -> predicate(entry.value) }.keys.first()

    override fun any(predicate: (T?) -> Boolean): Boolean = cells.any {cell -> predicate(cellToData[cell])}

    override fun all(predicate: (T?) -> Boolean): Boolean = cells.all {cell -> predicate(cellToData[cell])}
}

fun createSquareBoard(width: Int): SquareBoard = SquareBoardImpl(width)
fun <T> createGameBoard(width: Int): GameBoard<T> = GameBoardImpl(width)


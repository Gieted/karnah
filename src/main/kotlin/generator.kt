import kotlin.math.pow

private fun Int.isEven() = this % 2 == 0

private fun Int.isOdd() = !isEven()

fun String.binToDec() =
    reversed().foldIndexed(0) { index, acc, c -> acc + c.toString().toInt() * 2.0.pow(index).toInt() }

private fun grayCoded(length: Int): List<String> = if (length == 1) listOf("0", "1") else {
    val previous = grayCoded(length - 1)
    (previous + previous.reversed()).mapIndexed { index, s -> if (index < previous.size) "0$s" else "1$s" }
}

fun generateKarnah(variablesCount: Int, function: (x: List<Boolean>) -> Boolean) =
    generateKarnah(variablesCount, false, function)

fun generateKarnah(variablesCount: Int, horizontal: Boolean, function: (x: List<Boolean>) -> Boolean) {
    data class Grid(val rowKeys: List<String>, val columnKeys: List<String>) {
        private val rows: MutableMap<String, MutableMap<String, Boolean?>> =
            rowKeys.map { columnKey ->
                columnKey to columnKeys.map { rowKey -> rowKey to null }.toMap<String, Boolean?>().toMutableMap()
            }.toMap().toMutableMap()

        fun set(rowKey: String, columnKey: String, value: Boolean?) {
            rows[rowKey]!![columnKey] = value
        }

        fun get(rowKey: String, columnKey: String) = rows[rowKey]!![columnKey]

        private val columnHeaderLength = columnKeys.first().length
        private val rowHeaderLength = rowKeys.first().length

        override fun toString(): String =
            " ".repeat(rowHeaderLength + 2) + columnKeys.reduce { acc, s -> "$acc $s" } + "\n" + rows.entries.fold("") { rowAcc, row ->
                rowAcc + "\n" + row.key + " " + row.value.values.fold("") { dataAcc, data ->
                    dataAcc + " ".repeat(columnHeaderLength) + if (data!!) "1" else "0"
                }
            }
    }

    fun Char.toBool() = this == '1'

    val rowVariablesCount =
        if (variablesCount.isOdd() && horizontal) variablesCount / 2 + 1 else variablesCount / 2

    val columnVariablesCount = variablesCount - rowVariablesCount

    val grid = Grid(grayCoded(rowVariablesCount), grayCoded(columnVariablesCount))

    val dec = mutableSetOf<Int>()

    for (rowKey in grid.rowKeys) {
        for (columnKey in grid.columnKeys) {
            val args = listOf(false) + (rowKey + columnKey).map { it.toBool() }
            val result = function(args)
            grid.set(rowKey, columnKey, result)

            if (result) {
                dec.add((rowKey + columnKey).binToDec())
            }
        }
    }

    println(grid.toString())
    println("")
    println("E = {${dec.sorted().fold("") { acc, i -> "$acc $i" }.drop(1)}}")
}

fun grayCode(length: Int) = grayCoded(length).forEach { println(it) }

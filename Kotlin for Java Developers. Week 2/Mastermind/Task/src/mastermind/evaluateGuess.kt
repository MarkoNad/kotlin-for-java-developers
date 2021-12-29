package mastermind

data class Evaluation(val rightPosition: Int, val wrongPosition: Int)

fun evaluateGuess(secret: String, guess: String): Evaluation {
    var rightPosition = 0
    var wrongPosition = 0

    val secretNotRight = ArrayList<Char>()
    val guessNotRight = ArrayList<Char>()

    for (i in secret.indices) {
        val c1 = secret[i]
        val c2 = guess[i]

        if (c1 == c2) {
            rightPosition++
        } else {
            secretNotRight.add(c1)
            guessNotRight.add(c2)
        }
    }

    for (c in guessNotRight) {
        if (c in secretNotRight) {
            wrongPosition++
            secretNotRight.remove(c)
        }
    }

    return Evaluation(rightPosition, wrongPosition)
}

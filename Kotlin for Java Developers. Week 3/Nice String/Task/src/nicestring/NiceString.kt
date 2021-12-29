package nicestring

import kotlin.streams.asStream

fun String.isNice(): Boolean {
    val containsBadSubstrings = containsBadSubstrings()
    val containsDoubleLetter = containsDoubleLetter()
    val containsAtLeast3Vowels = containsAtLeast3Vowels()

    var conditionsMet = 0

    if (!containsBadSubstrings) {
        println("No bad substrings.")
        conditionsMet++
    }

    if (containsDoubleLetter) {
        println("Contains double letter.")
        conditionsMet++
    }

    if (containsAtLeast3Vowels) {
        println("Contains at least 3 vowels.")
        conditionsMet++
    }

    return conditionsMet >= 2
}

fun String.containsBadSubstrings(): Boolean {
    return length > 1 &&
            this.zip(this.subSequence(1 until length))
                    .any {
                        "${it.first}${it.second}" in setOf("bu", "ba", "be")
                    }
}

fun String.containsAtLeast3Vowels(): Boolean {
    return this.asSequence()
            .asStream()
            .filter { it in setOf('a', 'e', 'i', 'o', 'u') }
            .count() >= 3
}

fun String.containsDoubleLetter(): Boolean {
    return length > 1 &&
            this.zip(this.subSequence(1 until length))
                    .any { it.first == it.second }
}
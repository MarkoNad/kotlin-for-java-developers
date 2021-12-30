package rationals

import java.math.BigInteger

class Rational(unnormalizedNumerator: BigInteger, unnormalizedDenominator: BigInteger) : Comparable<Rational> {

    private val numerator: BigInteger
    private val denominator: BigInteger

    init {
        val greatestCommonDivisor = unnormalizedNumerator.gcd(unnormalizedDenominator)

        val sign = unnormalizedDenominator.signum().toBigInteger()

        this.numerator = unnormalizedNumerator / greatestCommonDivisor * sign
        this.denominator = unnormalizedDenominator / greatestCommonDivisor * sign
    }

    infix operator fun plus(other: Rational) : Rational {
        val commonDenominator = getCommonDenominator(other)
        val x = commonDenominator / denominator * numerator
        val y = commonDenominator / other.denominator * other.numerator
        return Rational(x + y, commonDenominator)
    }

    infix operator fun minus(other: Rational) : Rational {
        val commonDenominator = getCommonDenominator(other)
        val x = commonDenominator / denominator * numerator
        val y = commonDenominator / other.denominator * other.numerator
        return Rational(x - y, commonDenominator)
    }

    private fun getCommonDenominator(other: Rational): BigInteger {
        return denominator * other.denominator
    }

    infix operator fun times(other: Rational) : Rational {
        return Rational(
                numerator * other.numerator,
                denominator * other.denominator
        )
    }

    infix operator fun div(other: Rational) : Rational {
        return Rational(
                numerator * other.denominator,
                denominator * other.numerator
        )
    }

    operator fun unaryMinus() : Rational {
        return Rational(-numerator, denominator)
    }

    override fun compareTo(other: Rational): Int {
        val lhs = this.numerator * other.denominator
        val rhs = this.denominator * other.numerator
        return lhs.compareTo(rhs)
    }

    override fun toString(): String {
        return if (denominator == BigInteger.ONE) "$numerator" else "$numerator/$denominator"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other !is Rational) {
            return false
        }
        return numerator == other.numerator && denominator == other.denominator
    }

    override fun hashCode(): Int {
        var result = numerator.hashCode()
        result = 31 * result + denominator.hashCode()
        return result
    }
}

infix fun BigInteger.divBy(denominator: BigInteger) : Rational {
    return Rational(this, denominator)
}

infix fun Int.divBy(denominator: Int) : Rational {
    return Rational(this.toBigInteger(), denominator.toBigInteger())
}

infix fun Long.divBy(denominator: Long) : Rational {
    return Rational(this.toBigInteger(), denominator.toBigInteger())
}

fun String.toRational(): Rational {
    val parts = this.split("/")
    return if (parts.size == 1)
        Rational(BigInteger(this), BigInteger.ONE) else
        Rational(BigInteger(parts[0]), BigInteger(parts[1]))
}

fun main() {
    val half = 1 divBy 2
    val third = 1 divBy 3

    val sum: Rational = half + third
    println(5 divBy 6 == sum)

    val difference: Rational = half - third
    println(1 divBy 6 == difference)

    val product: Rational = half * third
    println(1 divBy 6 == product)

    val quotient: Rational = half / third
    println(3 divBy 2 == quotient)

    val negation: Rational = -half
    println(-1 divBy 2 == negation)

    println((2 divBy 1).toString() == "2")
    println((-2 divBy 4).toString() == "-1/2")
    println("117/1098".toRational().toString() == "13/122")

    val twoThirds = 2 divBy 3
    println(half < twoThirds)

    println(half in third..twoThirds)

    println("912016490186296920119201192141970416029".toBigInteger() divBy
            "1824032980372593840238402384283940832058".toBigInteger() == 1 divBy 2)
}
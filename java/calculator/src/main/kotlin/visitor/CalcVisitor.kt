package visitor

import exceptions.InvalidExpressionException
import exceptions.InvalidOperandException
import exceptions.InvalidOperandsNumberException
import token.Brace
import token.NumberToken
import token.Operation
import token.Token

class CalcVisitor : TokenVisitor {

    private val tokens = ArrayDeque<Token>()

    val result: Long
        get() = tokens.let {
            require(tokens.size == 1) {
                throw InvalidExpressionException(tokens.size)
            }

            (tokens.removeFirst() as NumberToken).value
        }

    override fun visit(token: NumberToken) {
        tokens.addFirst(token)
    }

    override fun visit(token: Brace) {
        // do nothing
    }

    override fun visit(token: Operation) {
        require(tokens.size >= 2) {
            throw InvalidOperandsNumberException(tokens.size)
        }

        val left = tokens.removeFirst()
        val right = tokens.removeFirst()

        if (left !is NumberToken) {
            throw InvalidOperandException(left)
        }

        if (right !is NumberToken) {
            throw InvalidOperandException(right)
        }

        val leftValue = left.value
        val rightValue = right.value
        when (token.value) {
            '+' -> {
                tokens.addFirst(NumberToken(leftValue + rightValue))
            }
            '-' -> {
                tokens.addFirst(NumberToken(leftValue - rightValue))
            }
            '*' -> {
                tokens.addFirst(NumberToken(leftValue * rightValue))
            }
            '/' -> {
                tokens.addFirst(NumberToken(leftValue / rightValue))
            }
        }
    }
}
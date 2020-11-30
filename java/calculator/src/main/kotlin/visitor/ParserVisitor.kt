package visitor

import exceptions.InvalidPairingBrackets
import token.Brace
import token.NumberToken
import token.Operation
import token.Token

class ParserVisitor : TokenVisitor {

    private val tokens = mutableListOf<Token>()

    private val operations = ArrayDeque<Token>()

    val result: List<Token>
        get() = tokens.let {
            while (operations.isNotEmpty()) {
                it += operations.removeFirst()
            }
            it
        }

    override fun visit(token: NumberToken) {
        tokens += token
    }

    override fun visit(token: Brace) {
        if (token.value == '(') {
            operations.addFirst(token)
            return
        }

        while (true) {
            require(operations.isNotEmpty()) {
                throw InvalidPairingBrackets()
            }

            val currentOperation = operations.removeFirst()

            if (currentOperation is Brace && currentOperation.value == '(') {
                break
            }

            tokens += currentOperation
        }
    }

    override fun visit(token: Operation) {
        while (true) {
            val currentOperation = operations.firstOrNull()
            if (currentOperation is Operation) {
                if (priorities.getValue(currentOperation.value) >= priorities.getValue(token.value)) {
                    tokens += operations.removeFirst()
                } else {
                    break
                }
            } else {
                break
            }
        }
        operations.addFirst(token)
    }

    companion object {
        val priorities = hashMapOf(
            '+' to 3,
            '-' to 3,
            '*' to 5,
            '/' to 5
        )
    }
}
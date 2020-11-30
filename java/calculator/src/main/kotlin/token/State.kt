package token

import exceptions.UnsupportedTokenException

sealed class State {
    abstract fun handle(symbol: Char, tokenizer: Tokenizer)

    fun nextState(symbol: Char): State = when {
        symbol.isWhitespace() -> {
            EmptyState
        }
        symbol in Tokenizer.brackets -> {
            BraceState(symbol)
        }
        symbol in Tokenizer.operations -> {
            OperationState(symbol)
        }
        symbol.isDigit() -> {
            NumberState(symbol)
        }
        else -> {
            throw UnsupportedTokenException(symbol)
        }
    }
}

object EmptyState : State() {
    override fun handle(symbol: Char, tokenizer: Tokenizer) {
        tokenizer.curState = nextState(symbol)
    }
}

class OperationState(private val operation: Char) : State() {
    override fun handle(symbol: Char, tokenizer: Tokenizer) {
        tokenizer.tokens += Operation(operation)
        tokenizer.curState = nextState(symbol)
    }
}

class BraceState(private val brace: Char) : State() {
    override fun handle(symbol: Char, tokenizer: Tokenizer) {
        tokenizer.tokens += Brace(brace)
        tokenizer.curState = nextState(symbol)
    }
}

class NumberState(firstDigit: Char) : State() {
    private val number = StringBuilder(firstDigit.toString())

    override fun handle(symbol: Char, tokenizer: Tokenizer) {
        if (symbol.isDigit()) {
            number.append(symbol)
        } else {
            tokenizer.tokens += NumberToken(number.toString().toLong())
            tokenizer.curState = nextState(symbol)
        }
    }
}
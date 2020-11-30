package token

class Tokenizer {

    companion object {
        val brackets = hashSetOf('(', ')')
        val operations = hashSetOf('+', '-', '*', '/')
    }

    val tokens = mutableListOf<Token>()

    var curState: State = EmptyState

    val result: List<Token>
        get() = tokens.let {
            if (curState !is EmptyState) {
                curState.handle(' ', this)
            }
            it
        }

    fun handle(symbol: Char) {
        curState.handle(symbol, this)
    }
}


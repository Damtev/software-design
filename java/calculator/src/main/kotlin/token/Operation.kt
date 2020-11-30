package token

import visitor.TokenVisitor

data class Operation(val value: Char) : Token {
    override fun accept(visitor: TokenVisitor) {
        visitor.visit(this)
    }
}
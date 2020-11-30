package token

import visitor.TokenVisitor

data class NumberToken(val value: Long) : Token {
    override fun accept(visitor: TokenVisitor) {
        visitor.visit(this)
    }
}
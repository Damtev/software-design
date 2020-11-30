package token

import visitor.TokenVisitor

data class Brace(val value: Char) : Token {
    override fun accept(visitor: TokenVisitor) {
        visitor.visit(this)
    }
}
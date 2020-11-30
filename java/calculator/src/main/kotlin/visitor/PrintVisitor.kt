package visitor

import token.Brace
import token.NumberToken
import token.Operation
import java.lang.StringBuilder

class PrintVisitor : TokenVisitor {

    private val curResult = StringBuilder()

    val result: String
        get() = curResult.toString()

    override fun visit(token: NumberToken) {
        curResult.append("${token.value} ")
    }

    override fun visit(token: Brace) {
        curResult.append("${token.value} ")
    }

    override fun visit(token: Operation) {
        curResult.append("${token.value} ")
    }
}
import exceptions.CalculatorException
import token.Tokenizer
import visitor.CalcVisitor
import visitor.ParserVisitor
import visitor.PrintVisitor

fun main(args: Array<String>) {
    if (args.size != 1) {
        print("Expected one argument - expression to calculate")
        return
    }

    try {
        val tokenizer = Tokenizer()
        args[0].forEach {
            tokenizer.handle(it)
        }

        val parser = ParserVisitor()
        tokenizer.result.forEach {
            it.accept(parser)
        }

        val printer = PrintVisitor()

        val reversePolishNotationTokens = parser.result
        reversePolishNotationTokens.forEach {
            it.accept(printer)
        }

        println("Reverse polish notation: ${printer.result}")

        val calculator = CalcVisitor()
        reversePolishNotationTokens.forEach {
            it.accept(calculator)
        }

        println("Calculation result: ${calculator.result}")
    } catch (exception: CalculatorException) {
        print("Error occurred: $exception")
    }
}
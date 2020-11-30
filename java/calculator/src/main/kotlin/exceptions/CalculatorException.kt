package exceptions

import token.Token

sealed class CalculatorException(message: String) : Exception(message)

class UnsupportedTokenException(symbol: Char) : CalculatorException("Token $symbol is not supported")

class InvalidPairingBrackets : CalculatorException("Found close bracket without paired open")

class InvalidOperandsNumberException(size: Int) : CalculatorException("Need 2 or more operands for binary operation but have $size")

class InvalidOperandException(token: Token) : CalculatorException("Operands of binary operations must be numbers but cur is $token")

class InvalidExpressionException(size: Int) : CalculatorException("Final deque must contians only one token but its size is $size")
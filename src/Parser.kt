class Parser(private val tokens: List<Token>) {
	private var current = 0

	fun parse(): Expr? {
		return try {
			expression()
		} catch (error: ParseError) {
			null
		}
	}

	private fun expression(): Expr {
		return addition()
	}

	private fun addition(): Expr {
		var expr = multiplication()

		while (match(TokenType.MINUS, TokenType.PLUS)) {
			val operator = previous()
			val right = multiplication()
			expr = Expr.Binary(expr, operator, right)
		}

		return expr
	}

	private fun multiplication(): Expr {
		var expr = exponentiation()

		while (match(TokenType.SLASH, TokenType.STAR)) {
			val operator = previous()
			val right = exponentiation()
			expr = Expr.Binary(expr, operator, right)
		}

		return expr
	}

	private fun exponentiation(): Expr {
		var expr = unary()

		while (match(TokenType.CARET)) {
			val operator = previous()
			val right = unary()
			expr = Expr.Binary(expr, operator, right)
		}

		return expr
	}

	private fun unary(): Expr {
		if (match(TokenType.MINUS)) {
			val operator = previous()
			val right = unary()
			return Expr.Unary(operator, right)
		}

		return function()
	}

	private fun function(): Expr {
		if ( match(
				TokenType.SIN, TokenType.COS, TokenType.TG, TokenType.CTG,
				TokenType.ASIN, TokenType.ACOS, TokenType.ATG, TokenType.ACTG,
				TokenType.SINH, TokenType.COSH, TokenType.TGH, TokenType.CTGH,
				TokenType.LN, TokenType.LOG10, TokenType.LOG2
			)
		) {
			val function = previous()
			if (match(TokenType.LEFT_PAREN)) {
				val expr = expression()
				consume(TokenType.RIGHT_PAREN, "Expect ')' after expression.")
				return Expr.Function(function, expr)
			}
			throw error(peek(), "Expect '(' after function.")
		}

		return primary()
	}

	private fun primary(): Expr {
		if (match(TokenType.NUMBER)) {
			return Expr.Number(previous().literal)
		}
		if (match(TokenType.X)) {
			return Expr.X()
		}

		if (match(TokenType.LEFT_PAREN)) {
			val expr = expression()
			consume(TokenType.RIGHT_PAREN, "Expect ')' after expression.")
			return Expr.Grouping(expr)
		}

		throw error(peek(), "Expect expression.")
	}


	private fun consume(type: TokenType, message: String): Token {
		if (check(type)) return advance()

		throw error(peek(), message)
	}

	private fun error(token: Token, message: String): ParseError {
		println("Unexpected ${token.lexeme}: $message")
		return ParseError()
	}

	class ParseError : RuntimeException()

	private fun match(vararg types: TokenType): Boolean {
		for (type in types) {
			if (check(type)) {
				advance()
				return true
			}
		}

		return false
	}

	private fun check(tokenType: TokenType): Boolean {
		return if (isAtEnd()) false else peek().type === tokenType
	}

	private fun advance(): Token {
		if (!isAtEnd()) current++
		return previous()
	}

	private fun isAtEnd() = peek().type === TokenType.EOF

	private fun peek() = tokens[current]

	private fun previous() = tokens[current - 1]
}
class Derivate {
	fun derivate(expr: Expr): Expr {
		return when(expr) {
			is Expr.Binary -> when (expr.operator.type) {
				TokenType.MINUS -> {
					Expr.Binary(
						derivate(expr.left),
						Token(TokenType.MINUS, "-", null),
						derivate(expr.right)
					)
				}
				TokenType.PLUS -> {
					Expr.Binary(
						derivate(expr.left),
						Token(TokenType.PLUS, "+", null),
						derivate(expr.right)
					)
				}
				TokenType.STAR -> {
					Expr.Binary(
						Expr.Binary(
							derivate(expr.left),
							Token(TokenType.STAR, "*", null),
							expr.right
						),
						Token(TokenType.MINUS, "+", null),
						Expr.Binary(
							expr.left,
							Token(TokenType.STAR, "*", null),
							derivate(expr.right)
						)
					)
					// calc(expr.left) * calc(expr.right)
				}
				TokenType.SLASH -> {
					Expr.Binary(
						Expr.Binary(
							Expr.Binary(
								derivate(expr.left),
								Token(TokenType.STAR, "*", null),
								expr.right
							),
							Token(TokenType.MINUS, "-", null),
							Expr.Binary(
								expr.left,
								Token(TokenType.STAR, "*", null),
								derivate(expr.right)
							)
						),
						Token(TokenType.SLASH, "/", null),
						Expr.Binary(
							expr.right,
							Token(TokenType.STAR, "*", null),
							expr.right
						)
					)
				}
				TokenType.CARET -> {
					Expr.Binary(
						Expr.Binary(
							Expr.Binary(
								expr.right,
								Token(TokenType.STAR, "*", null),
								derivate(expr.left)
							),
							Token(TokenType.STAR, "*", null),
							Expr.Binary(
								expr.left,
								Token(TokenType.CARET, "^", null),
								Expr.Binary(
									expr.right,
									Token(TokenType.MINUS, "-", null),
									Expr.Number(1)
								)
							)
						),
						Token(TokenType.PLUS, "+", null),
						Expr.Binary(
							Expr.Binary(
								expr.left,
								Token(TokenType.CARET, "^", null),
								expr.right
							),
							Token(TokenType.STAR, "*", null),
							Expr.Binary(
								Expr.Function(Token(TokenType.LN, "ln", null), expr.left),
								Token(TokenType.STAR, "*", null),
								derivate(expr.right)
							)
						)
					)
				}

				else -> throw error(expr.operator, "WTF is this binary operator")
			}
			is Expr.Grouping -> derivate(expr.expression)
			is Expr.Number -> Expr.Number(0)
			is Expr.Function -> when (expr.function.type) {
				TokenType.SIN -> {
					Expr.Binary(
						Expr.Function(
							Token(TokenType.COS, "cos", null),
							expr.expression
						),
						Token(TokenType.STAR, "*", null),
						derivate(expr.expression)
					)
				}
				TokenType.COS -> {
					Expr.Binary(
						Expr.Unary(
							Token(TokenType.MINUS, "-", null),
							Expr.Function(
								Token(TokenType.SIN, "sin", null),
								expr.expression
							)
						),
						Token(TokenType.STAR, "*", null),
						derivate(expr.expression)
					)
				}
//				TokenType.TG ->
//				TokenType.CTG ->
//				TokenType.ASIN ->
//				TokenType.ACOS ->
//				TokenType.ATG ->
//				TokenType.ACTG ->
				TokenType.SINH ->
					Expr.Binary(
						Expr.Function(
							Token(TokenType.COSH, "cosh", null),
							expr.expression
						),
						Token(TokenType.STAR, "*", null),
						derivate(expr.expression)
					)
				TokenType.COSH ->
					Expr.Binary(
						Expr.Function(
							Token(TokenType.SINH, "sinh", null),
							expr.expression
						),
						Token(TokenType.STAR, "*", null),
						derivate(expr.expression)
					)
//				TokenType.TGH ->
//				TokenType.CTGH ->
				TokenType.LN ->
					Expr.Binary(
						derivate(expr.expression),
						Token(TokenType.SLASH, "/", null),
						expr.expression
					)
//				TokenType.LOG2 ->
//				TokenType.LOG10 ->

				else -> throw error(expr.function, "WTF is this function")
			}
			is Expr.Unary -> when(expr.operator.type) {
				TokenType.MINUS -> Expr.Unary(Token(TokenType.MINUS, "-", null), derivate(expr.right))
				else -> throw error("WTF is this unary operator")
			}
			is Expr.X -> Expr.Number(1)
			else -> throw RuntimeException("WTF is this")
		}
	}
	private fun error(token: Token, message: String): TokenError {
		println("Unexpected $token: $message")
		return TokenError()
	}
	private fun error(message: String): TokenError {
		println("Unexpected: $message")
		return TokenError()
	}

	class TokenError : RuntimeException()
}
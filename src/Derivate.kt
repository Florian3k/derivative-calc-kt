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
					(ExprMaker()
						+ derivate(expr.left)
						+ "*"
						+ expr.right
						+ "+"
						+ expr.left
						+ "*"
						+ derivate(expr.right)
					).toExpr()
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
				TokenType.TG -> {
					Expr.Binary(
						Expr.Binary(
							Expr.Number(1),
							Token(TokenType.PLUS, "+", null),
							Expr.Binary(
								Expr.Function(
									Token(TokenType.TG, "tg", null),
									expr.expression
								),
								Token(TokenType.CARET, "^", null),
								Expr.Number(2)
							)
						),
						Token(TokenType.STAR, "*", null),
						derivate(expr.expression)
					)
				}
				TokenType.CTG -> {
					Expr.Binary(
						Expr.Binary(
							Expr.Number(-1),
							Token(TokenType.MINUS, "-", null),
							Expr.Binary(
								Expr.Function(
									Token(TokenType.CTG, "ctg", null),
									expr.expression
								),
								Token(TokenType.CARET, "^", null),
								Expr.Number(2)
							)
						),
						Token(TokenType.STAR, "*", null),
						derivate(expr.expression)
					)
				}
				TokenType.ASIN -> {
					Expr.Binary(
						Expr.Binary(
							Expr.Binary(
								Expr.Number(1),
								Token(TokenType.MINUS, "-", null),
								Expr.Binary(
									expr.expression,
									Token(TokenType.CARET, "^", null),
									Expr.Number(2)
								)
							),
							Token(TokenType.CARET, "^", null),
							Expr.Number(-0.5)
						),
						Token(TokenType.STAR, "*", null),
						derivate(expr.expression)
					)
				}
				TokenType.ACOS -> {
					Expr.Unary(
						Token(TokenType.MINUS, "-", null),
						Expr.Binary(
							Expr.Binary(
								Expr.Binary(
									Expr.Number(1),
									Token(TokenType.MINUS, "-", null),
									Expr.Binary(
										expr.expression,
										Token(TokenType.CARET, "^", null),
										Expr.Number(2)
									)
								),
								Token(TokenType.CARET, "^", null),
								Expr.Number(-0.5)
							),
							Token(TokenType.STAR, "*", null),
							derivate(expr.expression)
						)
					)
				}
				TokenType.ATG -> {
					Expr.Binary(
						Expr.Binary(
							Expr.Number(1),
							Token(TokenType.SLASH, "/", null),
							Expr.Binary(
								Expr.Number(1),
								Token(TokenType.PLUS, "+", null),
								Expr.Binary(
									expr.expression,
									Token(TokenType.CARET, "^", null),
									Expr.Number(2)
								)
							)
						),
						Token(TokenType.STAR, "*", null),
						derivate(expr.expression)
					)
				}
				TokenType.ACTG -> {
					Expr.Binary(
						Expr.Binary(
							Expr.Number(-1),
							Token(TokenType.SLASH, "/", null),
							Expr.Binary(
								Expr.Number(1),
								Token(TokenType.PLUS, "+", null),
								Expr.Binary(
									expr.expression,
									Token(TokenType.CARET, "^", null),
									Expr.Number(2)
								)
							)
						),
						Token(TokenType.STAR, "*", null),
						derivate(expr.expression)
					)
				}
				TokenType.SINH -> {
					Expr.Binary(
						Expr.Function(
							Token(TokenType.COSH, "cosh", null),
							expr.expression
						),
						Token(TokenType.STAR, "*", null),
						derivate(expr.expression)
					)
				}
				TokenType.COSH -> {
					Expr.Binary(
						Expr.Function(
							Token(TokenType.SINH, "sinh", null),
							expr.expression
						),
						Token(TokenType.STAR, "*", null),
						derivate(expr.expression)
					)
				}
				TokenType.TGH -> {
					Expr.Binary(
						Expr.Binary(
							Expr.Number(1),
							Token(TokenType.MINUS, "-", null),
							Expr.Binary(
								Expr.Function(
									Token(TokenType.TGH, "tgh", null),
									expr.expression
								),
								Token(TokenType.CARET, "^", null),
								Expr.Number(2)
							)
						),
						Token(TokenType.STAR, "*", null),
						derivate(expr.expression)
					)
				}
				TokenType.CTGH -> {
					Expr.Binary(
						Expr.Binary(
							Expr.Number(1),
							Token(TokenType.MINUS, "-", null),
							Expr.Binary(
								Expr.Function(
									Token(TokenType.CTGH, "ctgh", null),
									expr.expression
								),
								Token(TokenType.CARET, "^", null),
								Expr.Number(2)
							)
						),
						Token(TokenType.STAR, "*", null),
						derivate(expr.expression)
					)
				}
				TokenType.LN -> {
					Expr.Binary(
						derivate(expr.expression),
						Token(TokenType.SLASH, "/", null),
						expr.expression
					)
				}
				TokenType.LOG2 -> {
					Expr.Binary(
						derivate(expr.expression),
						Token(TokenType.SLASH, "/", null),
						Expr.Binary(
							expr.expression,
							Token(TokenType.STAR, "*", null),
							Expr.Function(
								Token(TokenType.LN, "ln", null),
								Expr.Number(2)
							)
						)
					)
				}
				TokenType.LOG10 -> {
					Expr.Binary(
						derivate(expr.expression),
						Token(TokenType.SLASH, "/", null),
						Expr.Binary(
							expr.expression,
							Token(TokenType.STAR, "*", null),
							Expr.Function(
								Token(TokenType.LN, "ln", null),
								Expr.Number(10)
							)
						)
					)
				}

				else -> throw error(expr.function, "WTF is this function")
			}
			is Expr.Unary -> when(expr.operator.type) {
				TokenType.MINUS -> Expr.Unary(Token(TokenType.MINUS, "-", null), derivate(expr.right))
				else -> throw error("WTF is this unary operator")
			}
			is Expr.X -> Expr.Number(1)
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

	private class ExprMaker {
		val tokens = mutableListOf<Token>()
		operator fun plus(string: String): ExprMaker {
			tokens.addAll(Lexer(string).scanTokens().filter { it.type != TokenType.EOF })
			return this
		}
		operator fun plus(expr: Expr): ExprMaker {
			tokens.addAll(Tokenizer().tokenize(expr))
			return this
		}
		fun toExpr(): Expr {
			tokens.add(Token(TokenType.EOF,"",null))
			return Parser(tokens).parse()!!
		}
	}
}

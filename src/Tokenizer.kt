class Tokenizer {
	fun tokenize(expr: Expr): List<Token> = when (expr) {
		is Expr.Binary -> listOf(
			*tokenize(expr.left).toTypedArray(),
			expr.operator,
			*tokenize(expr.right).toTypedArray()
		)
		is Expr.Grouping -> listOf(
			lex1token("("),
			*tokenize(expr.expression).toTypedArray(),
			lex1token("(")
		)
		is Expr.Number -> listOf(lex1token(expr.value.toString()))
		is Expr.Unary -> listOf(
			expr.operator,
			*tokenize(expr.right).toTypedArray()
		)
		is Expr.Function -> listOf(
			expr.function,
			lex1token("("),
			*tokenize(expr.expression).toTypedArray(),
			lex1token("(")
		)
		is Expr.X -> listOf(lex1token("x"))
	}
	private fun lex1token(str: String): Token = Lexer(str).scanTokens()[0]
}

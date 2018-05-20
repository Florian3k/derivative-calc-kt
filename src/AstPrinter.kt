class AstPrinter {
	fun print(expr: Expr): String {
		return parenthesize("", expr)
	}

	private fun parenthesize(name: String, vararg exprs: Expr): String {
		val builder = StringBuilder()

		builder.append("($name ")

		exprs.joinToString(" ") { expr ->
			when(expr) {
				is Expr.Binary -> parenthesize(expr.operator.lexeme, expr.left, expr.right)
				is Expr.Grouping -> parenthesize("group", expr.expression)
				is Expr.Number -> expr.value?.toString() ?: "null"
				is Expr.Function -> parenthesize(expr.function.lexeme, expr.expression)
				is Expr.Unary -> parenthesize(expr.operator.lexeme, expr.right)
				is Expr.X -> "x"
				else -> throw RuntimeException("WTF is this")
			}
		}.let {
			builder.append(it)
		}

		builder.append(")")

		return builder.toString()
	}
}

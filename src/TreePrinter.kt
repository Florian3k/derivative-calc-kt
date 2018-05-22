class TreePrinter {
	fun print(expr: Expr): String = when(expr) {
		is Expr.Binary -> {
			"${expr.operator.lexeme}\n├ ${print(expr.left).indent("| ")}\n└ ${print(expr.right).indent("  ")}"
		}
		is Expr.Grouping -> print(expr.expression)
		is Expr.Number -> expr.value?.toString() ?: "null"
		is Expr.Function -> {
			"${expr.function.lexeme}\n└ ${print(expr.expression).indent("  ")}"
		}
		is Expr.Unary -> {
			"${expr.operator.lexeme}\n└ ${print(expr.right).indent("  ")}"
		}
		is Expr.X -> "x"
		else -> throw RuntimeException("WTF is this")
	}
	private fun String.indent(joiner: String): String {
		return this.split("\n").joinToString("\n$joiner")
	}
}
class InfixPrinter  {
	fun print(expr: Expr): String = when(expr) {
		is Expr.Binary -> "(${print(expr.left)} ${expr.operator.lexeme} ${print(expr.right)})"
		is Expr.Grouping -> "(${print(expr.expression)})"
		is Expr.Number -> expr.value?.toString() ?: "null"
		is Expr.Function -> "${expr.function.lexeme}(${print(expr.expression)})"
		is Expr.Unary -> expr.operator.lexeme + print(expr.right)
		is Expr.X -> "x"
		else -> throw RuntimeException("WTF is this")
	}
}

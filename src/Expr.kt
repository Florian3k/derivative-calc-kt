sealed class Expr {

	class Binary(val left: Expr, val operator: Token, val right: Expr) : Expr()

	class Grouping(val expression: Expr) : Expr()

	class Number(val value: Any?) : Expr()

	class Unary(val operator: Token, val right: Expr) : Expr()

	class Function(val function: Token, val expression: Expr) : Expr()

	class X : Expr()
}
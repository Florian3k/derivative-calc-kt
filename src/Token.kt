class Token(
	val type: TokenType,
	val lexeme: String,
	val literal: Any?
) {
	override fun toString(): String = "$type $lexeme $literal"
}
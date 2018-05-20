class Lexer(private val source: String) {
	private var tokens = mutableListOf<Token>()
	private var start = 0
	private var current = 0
	private val keywords = mapOf(
		"sin" to TokenType.SIN,
		"cos" to TokenType.COS,
		"tg" to TokenType.TG,
		"ctg" to TokenType.CTG,
		"ln" to TokenType.LN,
		"log2" to TokenType.LOG2,
		"log10" to TokenType.LOG10
	)

	private fun addToken(type: TokenType, literal: Any? = null) {
		val text = source.substring(start, current)
		tokens.add( Token(type, text, literal) )
	}

	private fun isAtEnd() = current >= source.length

	private fun advance() = source[current++]

	private fun peek(): Char = if (isAtEnd()) 0.toChar() else source[current]

	private fun peekNext(): Char =
		if (current + 1 >= source.length)
			0.toChar()
		else source[current + 1]

	private fun isDigit(c: Char) = c in '0'..'9'

	private fun isAlpha(c: Char) = c in 'a'..'z' || c in 'A'..'Z' || c == '_'

	private fun isAlphaNumeric(c: Char) = isAlpha(c) || isDigit(c)

	private fun number() {
		while ( isDigit(peek()) ) {
			advance()
		}

		// Look for a fractional part
		if ( peek() == '.' && isDigit(peekNext()) ) {
			advance() // Consume the "."

			while (isDigit(peek())) {
				advance()
			}
		}

		addToken(
			TokenType.NUMBER,
			source.substring(start, current).toDouble()
		)
	}

	private fun identifier() {
		while ( isAlphaNumeric(peek()) ) {
			advance()
		}

		val text = source.substring(start, current)
		val type = keywords[text]

		type?.let {
			addToken(it)
		} ?: println("Unexpected identifier at $current: $text")
	}

	private fun scanToken() {
		val c = advance()
		when (c) {
			'(' -> addToken(TokenType.LEFT_PAREN)
			')' -> addToken(TokenType.RIGHT_PAREN)
			'.' -> addToken(TokenType.DOT)
			'-' -> addToken(TokenType.MINUS)
			'+' -> addToken(TokenType.PLUS)
			'*' -> addToken(TokenType.STAR)
			'/' -> addToken(TokenType.SLASH)
			'^' -> addToken(TokenType.CARET)
			'x' -> addToken(TokenType.X)
			' ', '\r', '\t', '\n' -> return
			else -> when {
				isDigit(c) -> number()
				isAlpha(c) -> identifier()
				else -> println("Unexpected character at $current: $c")
			}
		}
	}

	fun scanTokens(): MutableList<Token> {
		while ( !isAtEnd() ) {
			start = current
			scanToken()
		}

		tokens.add(Token(TokenType.EOF, "EOF", null) )
		return tokens
	}
}
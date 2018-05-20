import java.io.InputStreamReader
import java.nio.file.Files
import java.nio.file.Paths

fun main(args: Array<String>) {
	when (args.size) {
		0 -> runPrompt()
		1 -> runFile(args[0])
		else -> println("Usage: pochodna [file]")
	}
}

private fun runPrompt() {
	val reader = InputStreamReader(System.`in`).buffered()

	while (true) {
		print("> ")
		run(reader.readLine())
	}
}

private fun runFile(path: String) {
	run( Files.readAllBytes( Paths.get(path) ).toString() )
}

private fun run(source: String) {


	try {
		val tokens = Lexer(source).scanTokens()
		val expression = Parser(tokens).parse()!!

		println(
			InfixPrinter().print(
				Derivate().derivate(expression)
			)
		)
	} catch (e: Throwable) {
		println("Sumthing weurd huppend")
		println(e)
	}
}
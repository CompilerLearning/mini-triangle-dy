package com.improve777.scanner

import java.util.LinkedList

class Scanner(source: String) {
    private val source = LinkedList<Char>()

    private var currentChar: Char
    private var currentKind: Byte = -1
    private var currentSpelling = StringBuffer()
    private var isScanDone = false

    init {
        require(source.isNotEmpty()) { "Source program must not be empty." }

        this.source.addAll(source.toList())
        currentChar = this.source.pop()
    }

    fun scan(): Token? {
        if(isScanDone || (currentChar != EOT && source.isEmpty())) return null

        while (currentChar == '!' || currentChar == ' ' || currentChar == '\n') {
            scanSeparator()
        }
        currentSpelling = StringBuffer()
        currentKind = scanToken()
        return Token(currentKind, currentSpelling.toString())
    }

    private fun scanToken(): Byte {  // Token ::=
        when (currentChar) {
            in 'a'..'z', 'I', 'S' -> {
                takeIt()  // Letter
                while (isLetter(currentChar) || isDigit(currentChar))
                    takeIt()
                return Token.IDENTIFIER
            }
            in '0'..'9' -> {  // |
                takeIt()  // Digit
                while (isDigit(currentChar)) takeIt()  // Digit*
                return Token.INT_LITERAL
            }
            '+', '-', '*', '/', '<', '>', '=', '\\' -> {  // |
                takeIt()  // + | - | * | / | < | > | = | \
                return Token.OPERATOR
            }
            ';' -> {
                takeIt()
                return Token.SEMICOLON
            }
            ':' -> {
                takeIt()
                return if (currentChar == '=') {
                    takeIt()
                    Token.BECOMES
                } else {
                    Token.COLON
                }
            }
            '~' -> {
                takeIt()
                return Token.IS
            }
            '(' -> {
                takeIt()
                return Token.LPAREN
            }
            ')' -> {
                takeIt()
                return Token.RPAREN
            }
            EOT -> {
                isScanDone = true
                return Token.EOT
            }
            else ->
                throw IllegalStateException("report a lexical error: currentChar = $currentChar")
        }
    }

    private fun scanSeparator() {
        when (currentChar) {
            '!' -> {
                currentSpelling = StringBuffer()
                takeIt()
                while (isGraphic(currentChar)) takeIt()  // Graphic*
                take('\n')  // eol
            }
            ' ', '\n' -> takeIt()  // space | eol
        }
    }

    private fun take(expectedChar: Char) {
        if (currentChar == expectedChar) {
            currentSpelling.append(currentChar)
            currentChar = source.pop()
        } else {
            throw IllegalStateException(
                "Report a lexical error: currentChar = $currentChar, expectedChar = $expectedChar"
            )
        }
    }

    private fun takeIt() {
        currentSpelling.append(currentChar)
        currentChar = source.pop()
    }

    private fun isDigit(c: Char) = c.isDigit()

    private fun isLetter(c: Char) = c.isLetter()

    private fun isGraphic(c: Char) = c == ' ' || (c in Char(0x21)..Char(0x7E))
}
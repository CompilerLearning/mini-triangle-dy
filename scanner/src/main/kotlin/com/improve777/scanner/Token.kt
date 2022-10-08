package com.improve777.scanner

val EOT = Char(0x04)

class Token(kind: Byte, val spelling: String) {
    val kind: Byte

    init {
        var newKind: Byte? = null
        if (kind == IDENTIFIER) {
            for (k in BEGIN..WHILE) {
                if (spelling == spellings[k]) {
                    newKind = k.toByte()
                    break
                }
            }
        }
        this.kind = newKind ?: kind
    }

    companion object {
        private val spellings = arrayOf(
            "<identifier>", "<integer-literal>", "<char-literal>", "<operator>",
            "begin", "const", "do", "else", "end", "if", "in", "let", "then", "var", "while",
            ";", ":", ":=", "~", "(", ")", "<eot>"
        )

        const val IDENTIFIER: Byte = 0
        const val INT_LITERAL: Byte = 1
        const val CHAR_LITERAL: Byte = 2
        const val OPERATOR: Byte = 3
        const val BEGIN: Byte = 4
        const val CONST: Byte = 5
        const val DO: Byte = 6
        const val ELSE: Byte = 7
        const val END: Byte = 8
        const val IF: Byte = 9
        const val IN: Byte = 10
        const val LET: Byte = 11
        const val THEN: Byte = 12
        const val VAR: Byte = 13
        const val WHILE: Byte = 14
        /**
         * ;
         */
        const val SEMICOLON: Byte = 15
        /**
         * :
         */
        const val COLON: Byte = 16
        /**
         * :=
         */
        const val BECOMES: Byte = 17
        /**
         * ~
         */
        const val IS: Byte = 18
        /**
         * (
         */
        const val LPAREN: Byte = 19
        /**
         * )
         */
        const val RPAREN: Byte = 20
        /**
         * end of text
         */
        const val EOT: Byte = 21
    }
}
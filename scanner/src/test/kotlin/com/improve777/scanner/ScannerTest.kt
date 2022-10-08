package com.improve777.scanner

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class ScannerTest {
    @Test
    fun `소스 프로그램이 비어 있으면 에러를 반환한다`() {
        val source = ""

        assertThrows<IllegalArgumentException>("Source program must not be empty.") {
            val sut = Scanner(source)
        }
    }

    @Test
    fun `스캔을 모두 마치면 토큰이 12개이고 심볼이 일치한다`() {
        val source = """
            let var y: Integer
            in !new year
              y := y + 1$EOT
        """.trimIndent()

        val sut = Scanner(source)
        val results = mutableListOf<Token>()

        for (i in 0..11) {
            sut.scan()?.let {
                results.add(it)
            }
        }

        Assertions.assertEquals(12, results.size)

        val symbols = listOf(
            Token.LET, Token.VAR, Token.IDENTIFIER, Token.COLON, Token.IDENTIFIER,
            Token.IN,
            Token.IDENTIFIER, Token.BECOMES, Token.IDENTIFIER, Token.OPERATOR, Token.INT_LITERAL, Token.EOT,
        )

        Assertions.assertEquals(symbols, results.map { it.kind })
    }

    @Test
    fun `스캔할 문자가 없을 때는 null 을 반환한다`() {
        val source = """
            let var y: Integer
            in !new year
              y := y + 1$EOT
        """.trimIndent()

        val sut = Scanner(source)
        for (i in 0..11) {
            sut.scan()
        }

        val token = sut.scan()
        Assertions.assertNull(token)
    }

    @Test
    fun `Char 리터럴을 스캔하면 CHAR_LITERAL 토큰을 반환한다`() {
        val source = """
            'a'$EOT
        """.trimIndent()

        val sut = Scanner(source)
        val token = sut.scan()
        Assertions.assertEquals("\'a\'", token!!.spelling)
        Assertions.assertEquals(Token.CHAR_LITERAL, token.kind)
    }
}
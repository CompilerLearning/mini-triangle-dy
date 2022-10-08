package com.improve777.scanner

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

internal class ScannerTest {
    @Test
    fun `소스 프로그램이 비어 있으면 에러를 반환한다`() {
        val source = ""

        val result = runCatching {
            val sut = Scanner(source)
        }.onFailure {
            assertThat(it).isInstanceOf(IllegalArgumentException::class.java)
            assertThat(it.message).isEqualTo("Source program must not be empty.")
        }
        assertThat(result.isFailure).isTrue()
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

        assertThat(results.size).isEqualTo(12)

        val expected = listOf(
            Token.LET, Token.VAR, Token.IDENTIFIER, Token.COLON, Token.IDENTIFIER,
            Token.IN,
            Token.IDENTIFIER, Token.BECOMES, Token.IDENTIFIER, Token.OPERATOR, Token.INT_LITERAL, Token.EOT,
        )

        assertThat(results.map { it.kind }).isEqualTo(expected)
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
        assertThat(token).isNull()
    }

    @Test
    fun `Char 리터럴을 스캔하면 CHAR_LITERAL 토큰을 반환한다`() {
        val source = """
            'a'$EOT
        """.trimIndent()

        val sut = Scanner(source)
        val token = sut.scan()

        assertThat(token!!.spelling).isEqualTo("\'a\'")
        assertThat(token.kind).isEqualTo(Token.CHAR_LITERAL)
    }
}
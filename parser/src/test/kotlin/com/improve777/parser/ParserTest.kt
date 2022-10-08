package com.improve777.parser

import com.google.common.truth.Truth.assertThat
import com.improve777.ast.AssignCommand
import com.improve777.ast.LetCommand
import com.improve777.scanner.EOT
import com.improve777.scanner.Scanner
import org.junit.jupiter.api.Test

internal class ParserTest {
    @Test
    fun `프로그램을 구문분석하면 LetCommand AST 를 반환한다`() {
        val source = """
            let
                var n: Integer;
                var c: Char
            in
                begin
                c := '&';
                n := n+1
                end$EOT
        """.trimIndent()

        val scanner = Scanner(source)
        val parser = Parser(scanner)
        val program = parser.parse()
        assertThat(program.C).isInstanceOf(LetCommand::class.java)
    }

    @Test
    fun `프로그램을 구문분석하면 AssignCommand AST 를 반환한다`() {
        val source = """
            c := '&'$EOT
        """.trimIndent()

        val scanner = Scanner(source)
        val parser = Parser(scanner)
        val program = parser.parse()
        assertThat(program.C).isInstanceOf(AssignCommand::class.java)
    }

    @Test
    fun `프로그램을 EOT 로 끝나지 않으면 에러를 반환한다`() {
        val source = """
            c := '&'
        """.trimIndent()

        val scanner = Scanner(source)
        val parser = Parser(scanner)

        val result = runCatching {
            val program = parser.parse()
        }.onFailure {
            assertThat(it).isInstanceOf(IllegalArgumentException::class.java)
        }
        assertThat(result.isFailure).isTrue()
    }
}
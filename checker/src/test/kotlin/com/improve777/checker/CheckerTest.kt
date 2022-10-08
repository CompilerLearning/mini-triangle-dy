package com.improve777.checker

import com.google.common.truth.Truth.assertThat
import com.improve777.parser.Parser
import com.improve777.scanner.EOT
import com.improve777.scanner.Scanner
import org.junit.jupiter.api.Test

internal class CheckerTest {

    @Test
    fun `선언과 동일한 타입으로 값을 할당하면 프로그램이 정상적으로 종료된다`() {
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
        val checker = Checker()

        checker.check(program)
    }

    @Test
    fun `선언과 동일하지 않은 타입으로 값을 할당하면 예외를 반환한다`() {
        val source = """
            let
                var n: Char;
                var c: Integer
            in
                begin
                c := '&';
                n := n+1
                end$EOT
        """.trimIndent()

        val scanner = Scanner(source)
        val parser = Parser(scanner)
        val program = parser.parse()
        val checker = Checker()

        val result = runCatching {
            checker.check(program)
        }.onFailure {
            assertThat(it).isInstanceOf(IllegalStateException::class.java)
        }
        assertThat(result.isFailure).isTrue()
    }

    @Test
    fun `while 커맨드의 표현식의 결과가 bool 타입이 아니면 예외를 반환한다`() {
        val source = """
            let
                var n: Integer
            in
                while n / 2 do
                m := n > 1$EOT
        """.trimIndent()

        val scanner = Scanner(source)
        val parser = Parser(scanner)
        val program = parser.parse()
        val checker = Checker()

        val result = runCatching {
            checker.check(program)
        }.onFailure {
            assertThat(it).isInstanceOf(IllegalStateException::class.java)
        }
        assertThat(result.isFailure).isTrue()
    }

    @Test
    fun `Integer 타입의 변수에 maxint 를 할당하면 프로그램이 정상적으로 종료된다`() {
        val source = """
            let
                var n: Integer
            in
                n := maxint$EOT
        """.trimIndent()

        val scanner = Scanner(source)
        val parser = Parser(scanner)
        val program = parser.parse()
        val checker = Checker()

        checker.check(program)
    }

    @Test
    fun `Boolean 타입의 변수에 false, 단항 연산자 + Boolean 값을 할당하면 프로그램이 정상적으로 종료된다`() {
        val source = """
            let
                const t ~ true;
                var b: Boolean
            in
                begin
                b := false;
                b := \t
                end$EOT
        """.trimIndent()

        val scanner = Scanner(source)
        val parser = Parser(scanner)
        val program = parser.parse()
        val checker = Checker()

        checker.check(program)
    }
}
package com.improve777.parser

import com.improve777.ast.AssignCommand
import com.improve777.ast.BinaryExpression
import com.improve777.ast.CallCommand
import com.improve777.ast.CharLiteral
import com.improve777.ast.CharLiteralExpression
import com.improve777.ast.Command
import com.improve777.ast.ConstDeclaration
import com.improve777.ast.Declaration
import com.improve777.ast.Expression
import com.improve777.ast.Identifier
import com.improve777.ast.IfCommand
import com.improve777.ast.IntegerLiteral
import com.improve777.ast.IntegerLiteralExpression
import com.improve777.ast.LetCommand
import com.improve777.ast.Operator
import com.improve777.ast.Program
import com.improve777.ast.SequentialCommand
import com.improve777.ast.SequentialDeclaration
import com.improve777.ast.SimpleTypeDenoter
import com.improve777.ast.SimpleVname
import com.improve777.ast.TypeDenoter
import com.improve777.ast.UnaryExpression
import com.improve777.ast.VarDeclaration
import com.improve777.ast.VnameExpression
import com.improve777.ast.WhileCommand
import com.improve777.scanner.Scanner
import com.improve777.scanner.Token

class Parser(
    private val scanner: Scanner,
) {
    private lateinit var currentToken: Token

    fun parse(): Program {
        currentToken = scanner.scan() ?: throw IllegalArgumentException()
        val program = parseProgram()
        if (currentToken.kind != Token.EOT) {
            throw IllegalArgumentException()
        }
        return program
    }

    private fun acceptIt() {
        currentToken = scanner.scan() ?: throw IllegalArgumentException()
    }

    private fun accept(expectedKind: Byte) {
        if (currentToken.kind == expectedKind) {
            currentToken = scanner.scan() ?: throw IllegalArgumentException()
        } else {
            throw IllegalArgumentException()
        }
    }

    private fun parseProgram(): Program {
        return Program(parseSingleCommand())
    }

    private fun parseCommand(): Command {
        var command = parseSingleCommand()
        while (currentToken.kind == Token.SEMICOLON) {
            acceptIt()
            val command2 = parseSingleCommand()
            command = SequentialCommand(command, command2)
        }
        return command
    }

    private fun parseSingleCommand(): Command {
        val command: Command

        when (currentToken.kind) {
            Token.IDENTIFIER -> {
                val identifier = parseIdentifier()
                when (currentToken.kind) {
                    Token.BECOMES -> {
                        acceptIt()
                        val expression = parseExpression()
                        command = AssignCommand(identifier, expression)
                    }
                    Token.LPAREN -> {
                        acceptIt()
                        val expression = parseExpression()
                        accept(Token.RPAREN)
                        command = CallCommand(identifier, expression)
                    }
                    else -> throw IllegalArgumentException()
                }
            }
            Token.IF -> {
                acceptIt()
                val expression = parseExpression()
                accept(Token.THEN)
                val command1 = parseSingleCommand()
                accept(Token.ELSE)
                val command2 = parseSingleCommand()
                command = IfCommand(expression, command1, command2)
            }
            Token.WHILE -> {
                acceptIt()
                val expression = parseExpression()
                accept(Token.DO)
                val innerCommand = parseSingleCommand()
                command = WhileCommand(expression, innerCommand)
            }
            Token.LET -> {
                acceptIt()
                val declaration = parseDeclaration()
                accept(Token.IN)
                val com = parseSingleCommand()
                command = LetCommand(declaration, com)
            }
            Token.BEGIN -> {
                acceptIt()
                command = parseCommand()
                accept(Token.END)
            }
            else -> throw IllegalArgumentException()
        }
        return command
    }

    private fun parseExpression(): Expression {
        var expression = parsePrimaryExpression()
        while (currentToken.kind == Token.OPERATOR) {
            val operator = parseOperator()
            val expression2 = parsePrimaryExpression()
            expression = BinaryExpression(expression, operator, expression2)
        }
        return expression
    }

    private fun parsePrimaryExpression(): Expression {
        val expression: Expression

        when (currentToken.kind) {
            Token.INT_LITERAL -> {
                expression = IntegerLiteralExpression(parseIntegerLiteral())
            }
            Token.CHAR_LITERAL -> {
                expression = CharLiteralExpression(parseCharLiteral())
            }
            Token.IDENTIFIER -> {
                expression = VnameExpression(SimpleVname(parseIdentifier()))
            }
            Token.OPERATOR -> {
                val operator = parseOperator()
                val innerExpression = parsePrimaryExpression()
                expression = UnaryExpression(operator, innerExpression)
            }
            Token.LPAREN -> {
                acceptIt()
                expression = parseExpression()
                accept(Token.RPAREN)
            }
            else -> throw IllegalArgumentException()
        }
        return expression
    }

    private fun parseDeclaration(): Declaration {
        var declaration = parseSingleDeclaration()
        while (currentToken.kind == Token.SEMICOLON) {
            acceptIt()
            val declaration2 = parseSingleDeclaration()
            declaration = SequentialDeclaration(declaration, declaration2)
        }
        return declaration
    }

    private fun parseSingleDeclaration(): Declaration {
        val declaration: Declaration

        when (currentToken.kind) {
            Token.CONST -> {
                acceptIt()
                val identifier = parseIdentifier()
                accept(Token.IS)
                val expression = parseExpression()
                declaration = ConstDeclaration(identifier, expression)
            }
            Token.VAR -> {
                acceptIt()
                val identifier = parseIdentifier()
                accept(Token.COLON)
                val typeDenoter = parseTypeDenoter()
                declaration = VarDeclaration(identifier, typeDenoter)
            }
            else -> throw IllegalArgumentException()
        }
        return declaration
    }

    private fun parseTypeDenoter(): TypeDenoter {
        return SimpleTypeDenoter(parseIdentifier())
    }

    private fun parseIdentifier(): Identifier {
        val identifier: Identifier
        if (currentToken.kind == Token.IDENTIFIER) {
            identifier = Identifier(currentToken.spelling)
            currentToken = scanner.scan() ?: throw IllegalArgumentException()
        } else {
            throw IllegalArgumentException()
        }
        return identifier
    }

    private fun parseIntegerLiteral(): IntegerLiteral {
        val integerLiteral: IntegerLiteral
        if (currentToken.kind == Token.INT_LITERAL) {
            integerLiteral = IntegerLiteral(currentToken.spelling)
            currentToken = scanner.scan() ?: throw IllegalArgumentException()
        } else {
            throw IllegalArgumentException()
        }
        return integerLiteral
    }

    private fun parseCharLiteral(): CharLiteral {
        val charLiteral: CharLiteral
        if (currentToken.kind == Token.CHAR_LITERAL) {
            charLiteral = CharLiteral(currentToken.spelling)
            currentToken = scanner.scan() ?: throw IllegalArgumentException()
        } else {
            throw IllegalArgumentException()
        }
        return charLiteral
    }

    private fun parseOperator(): Operator {
        val operator: Operator
        if (currentToken.kind == Token.OPERATOR) {
            operator = Operator(currentToken.spelling)
            currentToken = scanner.scan() ?: throw IllegalArgumentException()
        } else {
            throw IllegalArgumentException()
        }
        return operator
    }
}
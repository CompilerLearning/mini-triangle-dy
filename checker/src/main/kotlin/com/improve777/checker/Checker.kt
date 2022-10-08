package com.improve777.checker

import com.improve777.ast.AssignCommand
import com.improve777.ast.BinaryExpression
import com.improve777.ast.BinaryOperatorDeclaration
import com.improve777.ast.CallCommand
import com.improve777.ast.CharLiteral
import com.improve777.ast.CharLiteralExpression
import com.improve777.ast.ConstDeclaration
import com.improve777.ast.Declaration
import com.improve777.ast.EqualOperatorDeclaration
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
import com.improve777.ast.Type
import com.improve777.ast.TypeDeclaration
import com.improve777.ast.UnaryExpression
import com.improve777.ast.UnaryOperatorDeclaration
import com.improve777.ast.VarDeclaration
import com.improve777.ast.Visitor
import com.improve777.ast.VnameExpression
import com.improve777.ast.WhileCommand

class Checker : Visitor {

    private lateinit var idTable: IdentificationTable

    fun check(program: Program) {
        idTable = IdentificationTable()

        idTable.enter(
            "false",
            ConstDeclaration(
                Identifier("false"),
                CharLiteralExpression(CharLiteral("false")).apply {
                    type = Type.bool
                }
            )
        )
        idTable.enter(
            "true",
            ConstDeclaration(
                Identifier("true"),
                CharLiteralExpression(CharLiteral("true")).apply {
                    type = Type.bool
                }
            )
        )
        idTable.enter(
            "maxint",
            ConstDeclaration(
                Identifier("maxint"),
                IntegerLiteralExpression(IntegerLiteral("2147483647")).apply {
                    type = Type.int
                }
            )
        )

        idTable.enter("Char", TypeDeclaration(Type.char))
        idTable.enter("Integer", TypeDeclaration(Type.int))
        idTable.enter("Boolean", TypeDeclaration(Type.bool))

        // operator
        idTable.enter("+", BinaryOperatorDeclaration(Type.int, Type.int, Type.int))
        idTable.enter("-", BinaryOperatorDeclaration(Type.int, Type.int, Type.int))
        idTable.enter("*", BinaryOperatorDeclaration(Type.int, Type.int, Type.int))
        idTable.enter("/", BinaryOperatorDeclaration(Type.int, Type.int, Type.int))
        idTable.enter(">", BinaryOperatorDeclaration(Type.int, Type.int, Type.bool))
        idTable.enter("<", BinaryOperatorDeclaration(Type.int, Type.int, Type.bool))
        idTable.enter("=", EqualOperatorDeclaration(Type.bool))
        idTable.enter("\\", UnaryOperatorDeclaration(Type.bool, Type.bool))

        program.visit(this, null)
    }

    override fun visitProgram(program: Program, arg: Any?): Any? {
        program.C.visit(this, null)
        return null
    }

    override fun visitAssignCommand(command: AssignCommand, arg: Any?): Any? {
        val vType = command.V.visit(this, null)
        val eType = command.E.visit(this, null)
        if (!command.V.variable) {
            throw IllegalStateException("the left side is not a variable.")
        }
        if (eType != vType) {
            throw IllegalStateException("the left and right sides are not of equivalent type.")
        }
        return null
    }

    override fun visitCallCommand(command: CallCommand, arg: Any?): Any? {
        val decl = command.I.visit(this, null)
        val eType = command.E.visit(this, null)
        if (decl == null) {
            throw IllegalStateException("the identifier is not declared.")
        }
        return null
    }

    override fun visitSequentialCommand(command: SequentialCommand, arg: Any?): Any? {
        command.C1.visit(this, null)
        command.C2.visit(this, null)
        return null
    }

    override fun visitIfCommand(command: IfCommand, arg: Any?): Any? {
        val eType = command.E.visit(this, null)
        if (eType != Type.bool) {
            throw IllegalStateException("the expression is not boolean.")
        }
        command.C1.visit(this, null)
        command.C2.visit(this, null)
        return null
    }

    override fun visitWhileCommand(command: WhileCommand, arg: Any?): Any? {
        val eType = command.E.visit(this, null) as? Type
        if (eType != Type.bool) {
            throw IllegalStateException("the expression is not boolean.")
        }
        command.C.visit(this, null)
        return null
    }

    override fun visitLetCommand(command: LetCommand, arg: Any?): Any? {
        idTable.openScope()
        command.D.visit(this, null)
        command.C.visit(this, null)
        idTable.closeScope()
        return null
    }

    override fun visitUnaryExpression(expression: UnaryExpression, arg: Any?): Any? {
        val decl = expression.O.visit(this, null)
        val eType = expression.E.visit(this, null) as? Type
        when (decl) {
            is UnaryOperatorDeclaration -> {
                if (eType != decl.operandType) {
                    throw IllegalStateException("the subexpression has the wrong type.")
                }
                expression.type = decl.resultType
            }
            else -> {
                expression.type = Type.error
                throw IllegalStateException("the operator is not declared.")
            }
        }
        return expression.type
    }

    override fun visitBinaryExpression(expression: BinaryExpression, arg: Any?): Any? {
        val e1Type = expression.E1.visit(this, null)
        val e2Type = expression.E2.visit(this, null)

        val opDecl = expression.O.visit(this, null)
        when (opDecl) {
            is BinaryOperatorDeclaration -> {
                if (e1Type != opDecl.operand1Type) {
                    throw IllegalStateException("the left subexpression has the wrong type.")
                }
                if (e2Type != opDecl.operand2Type) {
                    throw IllegalStateException("the right subexpression has the wrong type.")
                }
                expression.type = opDecl.resultType
            }
            is EqualOperatorDeclaration -> {
                if (e1Type != e2Type) {
                    throw IllegalStateException("the each subexpression has the different type.")
                }
                expression.type = opDecl.resultType
            }
            else -> {
                expression.type = Type.error
                throw IllegalStateException("the operator is not declared.")
            }
        }
        return expression.type
    }

    override fun visitVnameExpression(expression: VnameExpression, arg: Any?): Any? {
        val vType = expression.V.visit(this, null) as? Type
        expression.type = vType
        return expression.type
    }

    override fun visitIntegerLiteralExpression(expression: IntegerLiteralExpression, arg: Any?): Any? {
        expression.type = Type.int
        return expression.type
    }

    override fun visitCharLiteralExpression(expression: CharLiteralExpression, arg: Any?): Any? {
        expression.type = Type.char
        return expression.type
    }

    override fun visitSimpleVname(vname: SimpleVname, arg: Any?): Any? {
        val decl = vname.I.visit(this, null) as? Declaration
        when (decl) {
            null -> {
                vname.type = Type.error
                vname.variable = true
            }
            is ConstDeclaration -> {
                vname.type = decl.E.type
                vname.variable = false
            }
            is VarDeclaration -> {
                vname.type = decl.T.type
                vname.variable = true
            }
            is TypeDeclaration -> {
                vname.type = decl.type
                vname.variable = false
            }
        }
        return vname.type
    }

    override fun visitConstDeclaration(declaration: ConstDeclaration, arg: Any?): Any? {
        declaration.E.visit(this, null)
        idTable.enter(declaration.I.spelling, declaration)
        return null
    }

    override fun visitVarDeclaration(declaration: VarDeclaration, arg: Any?): Any? {
        declaration.T.visit(this, null)
        idTable.enter(declaration.I.spelling, declaration)
        return null
    }

    override fun visitSequentialDeclaration(declaration: SequentialDeclaration, arg: Any?): Any? {
        declaration.D1.visit(this, null)
        declaration.D2.visit(this, null)
        return null
    }

    override fun visitSimpleTypeDenoter(typeDenoter: SimpleTypeDenoter, arg: Any?): Any? {
        val decl = typeDenoter.I.visit(this, null) as? Declaration
        when (decl) {
            null -> {
                typeDenoter.type = Type.error
            }
            is ConstDeclaration -> {
                typeDenoter.type = decl.E.type
            }
            is VarDeclaration -> {
                typeDenoter.type = decl.T.type
            }
            is TypeDeclaration -> {
                typeDenoter.type = decl.type
            }
        }
        return typeDenoter.type
    }

    override fun visitIdentifier(identifier: Identifier, arg: Any?): Any? {
        identifier.decl = idTable.retrieve(identifier.spelling)
        return identifier.decl
    }

    override fun visitOperator(operator: Operator, arg: Any?): Any? {
        operator.decl = idTable.retrieve(operator.spelling)
        return operator.decl
    }
}
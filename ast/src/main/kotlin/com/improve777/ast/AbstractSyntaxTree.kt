package com.improve777.ast

abstract class AST {
    abstract fun visit(v: Visitor, arg: Any?): Any?
}

class Program(val C: Command) : AST() {
    override fun visit(v: Visitor, arg: Any?): Any? {
        return v.visitProgram(this, arg)
    }
}

abstract class Command : AST()

class AssignCommand(
    val V: Vname,
    val E: Expression,
) : Command() {
    override fun visit(v: Visitor, arg: Any?): Any? {
        return v.visitAssignCommand(this, arg)
    }
}

class CallCommand(
    val I: Identifier,
    val E: Expression,
) : Command() {
    override fun visit(v: Visitor, arg: Any?): Any? {
        return v.visitCallCommand(this, arg)
    }
}

class SequentialCommand(
    val C1: Command,
    val C2: Command,
) : Command() {
    override fun visit(v: Visitor, arg: Any?): Any? {
        return v.visitSequentialCommand(this, arg)
    }
}

class IfCommand(
    val E: Expression,
    val C1: Command,
    val C2: Command,
) : Command() {
    override fun visit(v: Visitor, arg: Any?): Any? {
        return v.visitIfCommand(this, arg)
    }
}

class WhileCommand(
    val E: Expression,
    val C: Command,
) : Command() {
    override fun visit(v: Visitor, arg: Any?): Any? {
        return v.visitWhileCommand(this, arg)
    }
}

class LetCommand(
    val D: Declaration,
    val C: Command,
) : Command() {
    override fun visit(v: Visitor, arg: Any?): Any? {
        return v.visitLetCommand(this, arg)
    }
}

abstract class Expression : AST() {
    var type: Type? = null
}

class UnaryExpression(
    val O: Operator,
    val E: Expression,
) : Expression() {
    override fun visit(v: Visitor, arg: Any?): Any? {
        return v.visitUnaryExpression(this, arg)
    }
}

class BinaryExpression(
    val E1: Expression,
    val O: Operator,
    val E2: Expression,
) : Expression() {
    override fun visit(v: Visitor, arg: Any?): Any? {
        return v.visitBinaryExpression(this, arg)
    }

}

class VnameExpression(val V: Vname) : Expression() {
    override fun visit(v: Visitor, arg: Any?): Any? {
        return v.visitVnameExpression(this, arg)
    }
}

class IntegerLiteralExpression(val IL: IntegerLiteral) : Expression() {
    override fun visit(v: Visitor, arg: Any?): Any? {
        return v.visitIntegerLiteralExpression(this, arg)
    }
}

class CharLiteralExpression(val CL: CharLiteral) : Expression() {
    override fun visit(v: Visitor, arg: Any?): Any? {
        return v.visitCharLiteralExpression(this, arg)
    }
}

abstract class Vname : AST() {
    var type: Type? = null
    var variable: Boolean = false
}

class SimpleVname(val I: Identifier) : Vname() {
    override fun visit(v: Visitor, arg: Any?): Any? {
        return v.visitSimpleVname(this, arg)
    }
}

abstract class Declaration : AST()

class ConstDeclaration(
    val I: Identifier,
    val E: Expression,
) : Declaration() {
    override fun visit(v: Visitor, arg: Any?): Any? {
        return v.visitConstDeclaration(this, arg)
    }
}

class VarDeclaration(
    val I: Identifier,
    val T: TypeDenoter,
) : Declaration() {
    override fun visit(v: Visitor, arg: Any?): Any? {
        return v.visitVarDeclaration(this, arg)
    }
}

class SequentialDeclaration(
    val D1: Declaration,
    val D2: Declaration,
) : Declaration() {
    override fun visit(v: Visitor, arg: Any?): Any? {
        return v.visitSequentialDeclaration(this, arg)
    }
}

class UnaryOperatorDeclaration(
    val operandType: Type,
    val resultType: Type,
) : Declaration() {
    override fun visit(v: Visitor, arg: Any?): Any? = null
}

class BinaryOperatorDeclaration(
    val operand1Type: Type,
    val operand2Type: Type,
    val resultType: Type,
) : Declaration() {
    override fun visit(v: Visitor, arg: Any?): Any? = null
}

class EqualOperatorDeclaration(val resultType: Type) : Declaration() {
    override fun visit(v: Visitor, arg: Any?): Any? = null
}

class TypeDeclaration(val type: Type) : Declaration() {
    override fun visit(v: Visitor, arg: Any?): Any? = null
}

abstract class TypeDenoter : AST() {
    var type: Type? = null
}

class SimpleTypeDenoter(val I: Identifier) : TypeDenoter() {
    override fun visit(v: Visitor, arg: Any?): Any? {
        return v.visitSimpleTypeDenoter(this, arg)
    }
}

abstract class Terminal(val spelling: String) : AST()

class Identifier(spelling: String) : Terminal(spelling) {
    var decl: Declaration? = null

    override fun visit(v: Visitor, arg: Any?): Any? {
        return v.visitIdentifier(this, arg)
    }
}

class IntegerLiteral(spelling: String) : Terminal(spelling) {
    override fun visit(v: Visitor, arg: Any?): Any? = null
}

class CharLiteral(spelling: String) : Terminal(spelling) {
    override fun visit(v: Visitor, arg: Any?): Any? = null
}

class Operator(spelling: String) : Terminal(spelling) {
    var decl: Declaration? = null

    override fun visit(v: Visitor, arg: Any?): Any? {
        return v.visitOperator(this, arg)
    }
}
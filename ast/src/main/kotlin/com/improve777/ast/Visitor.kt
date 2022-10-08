package com.improve777.ast

interface Visitor {
    fun visitProgram(program: Program, arg: Any?): Any?

    fun visitAssignCommand(command: AssignCommand, arg: Any?): Any?
    fun visitCallCommand(command: CallCommand, arg: Any?): Any?
    fun visitSequentialCommand(command: SequentialCommand, arg: Any?): Any?
    fun visitIfCommand(command: IfCommand, arg: Any?): Any?
    fun visitWhileCommand(command: WhileCommand, arg: Any?): Any?
    fun visitLetCommand(command: LetCommand, arg: Any?): Any?

    fun visitUnaryExpression(expression: UnaryExpression, arg: Any?): Any?
    fun visitBinaryExpression(expression: BinaryExpression, arg: Any?): Any?
    fun visitVnameExpression(expression: VnameExpression, arg: Any?): Any?
    fun visitIntegerLiteralExpression(expression: IntegerLiteralExpression, arg: Any?): Any?
    fun visitCharLiteralExpression(expression: CharLiteralExpression, arg: Any?): Any?

    fun visitSimpleVname(vname: SimpleVname, arg: Any?): Any?

    fun visitConstDeclaration(declaration: ConstDeclaration, arg: Any?): Any?
    fun visitVarDeclaration(declaration: VarDeclaration, arg: Any?): Any?
    fun visitSequentialDeclaration(declaration: SequentialDeclaration, arg: Any?): Any?

    fun visitSimpleTypeDenoter(typeDenoter: SimpleTypeDenoter, arg: Any?): Any?

    fun visitIdentifier(identifier: Identifier, arg: Any?): Any?
    fun visitOperator(operator: Operator, arg: Any?): Any?
}
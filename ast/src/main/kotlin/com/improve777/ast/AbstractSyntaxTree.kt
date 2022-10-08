package com.improve777.ast

abstract class AST

class Program(val C: Command) : AST()

abstract class Command : AST()

class AssignCommand(
    val I: Identifier,
    val E: Expression,
) : Command()

class CallCommand(
    val I: Identifier,
    val E: Expression,
) : Command()

class SequentialCommand(
    val C1: Command,
    val C2: Command,
) : Command()

class IfCommand(
    val E: Expression,
    val C1: Command,
    val C2: Command,
) : Command()

class WhileCommand(
    val E: Expression,
    val C: Command,
) : Command()

class LetCommand(
    val D: Declaration,
    val C: Command,
) : Command()

abstract class Expression : AST()

class UnaryExpression(
    val O: Operator,
    val E: Expression,
) : Expression()

class BinaryExpression(
    val E1: Expression,
    val O: Operator,
    val E2: Expression,
) : Expression()

class VnameExpression(val V: Vname) : Expression()
class IntegerLiteralExpression(val IL: IntegerLiteral) : Expression()
class CharLiteralExpression(val CL: CharLiteral) : Expression()

abstract class Vname : AST()

class SimpleVname(val I: Identifier) : Vname()

abstract class Declaration : AST()

class ConstDeclaration(
    val I: Identifier,
    val E: Expression,
) : Declaration()

class VarDeclaration(
    val I: Identifier,
    val T: TypeDenoter,
) : Declaration()

class SequentialDeclaration(
    val D1: Declaration,
    val D2: Declaration,
) : Declaration()

abstract class TypeDenoter : AST()

class SimpleTypeDenoter(val I: Identifier) : TypeDenoter()

abstract class Terminal(val spelling: String) : AST()

class Identifier(spelling: String) : Terminal(spelling)
class IntegerLiteral(spelling: String) : Terminal(spelling)
class CharLiteral(spelling: String) : Terminal(spelling)
class Operator(spelling: String) : Terminal(spelling)
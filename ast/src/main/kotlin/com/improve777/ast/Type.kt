package com.improve777.ast

data class Type(val kind: Byte) {
    override fun equals(other: Any?): Boolean {
        if (other !is Type) return false
        return kind == other.kind || kind == ERROR || other.kind == ERROR
    }

    override fun hashCode(): Int {
        return kind.hashCode()
    }

    companion object {
        const val BOOL: Byte = 0
        const val INT: Byte = 1
        const val CHAR: Byte = 2
        const val ERROR: Byte = 3

        val bool = Type(BOOL)
        val int = Type(INT)
        val char = Type(CHAR)
        val error = Type(ERROR)
    }
}

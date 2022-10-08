package com.improve777.checker

import com.improve777.ast.Declaration

class IdentificationTable {

    private val ids = HashMap<String, Pair<Int, Declaration>>()
    var currentLevel = 0

    fun enter(id: String, attr: Declaration) {
        ids[id] = currentLevel to attr
    }

    fun retrieve(id: String): Declaration? {
        return ids[id]?.second
    }

    fun openScope() {
        currentLevel++
    }

    fun closeScope() {
        currentLevel--
    }
}
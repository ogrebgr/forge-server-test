package com.bolyartech.forge.test.data

import com.bolyartech.forge.server.db.setValue
import java.sql.Connection
import java.sql.SQLException
import java.sql.Statement
import javax.inject.Inject


data class TestTable(
    val id: Int,
    val fieldString: String,
    val fieldInt: Int
)

interface TestTableDbh {
    @Throws(SQLException::class)
    fun createNew(dbc: Connection, fieldString: String, fieldInt: Int): TestTable

    @Throws(SQLException::class)
    fun loadById(dbc: Connection, id: Int): TestTable?

    @Throws(SQLException::class)
    fun loadAll(dbc: Connection): List<TestTable>

    @Throws(SQLException::class)
    fun update(dbc: Connection, fieldString: String, fieldInt: Int, id: Int): TestTable?

    @Throws(SQLException::class)
    fun loadPageIdGreater(dbc: Connection, idGreater: Int, pageSize: Int): List<TestTable>

    @Throws(SQLException::class)
    fun loadPageIdLower(dbc: Connection, idLower: Int, pageSize: Int): List<TestTable>

    @Throws(SQLException::class)
    fun loadLastPage(dbc: Connection, pageSize: Int): List<TestTable>

    @Throws(SQLException::class)
    fun count(dbc: Connection): Int

    @Throws(SQLException::class)
    fun delete(dbc: Connection, id: Int): Int

    @Throws(SQLException::class)
    fun deleteAll(dbc: Connection): Int
}

class TestTableDbhImpl @Inject constructor() : TestTableDbh {
    companion object {
        private const val SQL_INSERT = """INSERT INTO "test_table" ("field_string", "field_int") VALUES (?, ?)"""
        private const val SQL_SELECT_BY_ID = """SELECT "field_string", "field_int" FROM "test_table" WHERE id = ?"""
        private const val SQL_SELECT_ALL = """SELECT "id", "field_string", "field_int" FROM "test_table""""
        private const val SQL_UPDATE = """UPDATE "test_table" SET "field_string" = ?, "field_int" = ? WHERE id = ?"""
        private const val SQL_SELECT_ID_GREATER = """SELECT "id", "field_string", "field_int" FROM "test_table" WHERE id > ?"""
        private const val SQL_SELECT_ID_LOWER = """SELECT "id", "field_string", "field_int" FROM "test_table" WHERE id < ?"""
        private const val SQL_SELECT_LAST = """SELECT "id", "field_string", "field_int" FROM "test_table" ORDER BY id DESC"""
        private const val SQL_COUNT = """SELECT COUNT(id) FROM "test_table""""
        private const val SQL_DELETE = """DELETE FROM "test_table" WHERE id = ?"""
        private const val SQL_DELETE_ALL = """DELETE FROM "test_table""""
    }

    @Throws(SQLException::class)
    override fun createNew(dbc: Connection, fieldString: String, fieldInt: Int): TestTable {
        dbc.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS).use {
            it.setValue(1, fieldString)
            it.setValue(2, fieldInt)
            it.executeUpdate()
            it.generatedKeys.use {
                it.next()
                return TestTable(
                    it.getInt(1),
                    fieldString,
                    fieldInt
                )
            }
        }
    }

    @Throws(SQLException::class)
    override fun loadById(dbc: Connection, id: Int): TestTable? {
        dbc.prepareStatement(SQL_SELECT_BY_ID).use {
            it.setValue(1, id)

            it.executeQuery().use {
                return if (it.next()) {
                    TestTable(
                        id,
                        it.getString(1),
                        it.getInt(2)
                    )
                } else {
                    null
                }
            }
        }
    }

    @Throws(SQLException::class)
    override fun loadAll(dbc: Connection): List<TestTable> {
        val ret = mutableListOf<TestTable>()
        dbc.prepareStatement(SQL_SELECT_ALL).use {
            it.executeQuery().use {
                while (it.next()) {
                    ret.add(
                        TestTable(
                            it.getInt(1),
                            it.getString(2),
                            it.getInt(3)
                        )
                    )
                }
            }
        }

        return ret
    }


    @Throws(SQLException::class)
    override fun update(dbc: Connection, fieldString: String, fieldInt: Int, id: Int): TestTable? {
        dbc.prepareStatement(SQL_UPDATE).use {
            it.setValue(1, fieldString)
            it.setValue(2, fieldInt)
            it.setValue(3, id)

            return if (it.executeUpdate() > 0) {
                return TestTable(
                    id,
                    fieldString,
                    fieldInt
                )
            } else {
                null
            }
        }
    }

    @Throws(SQLException::class)
    override fun loadPageIdGreater(dbc: Connection, idGreater: Int, pageSize: Int): List<TestTable> {
        if (pageSize <= 0) {
            throw IllegalArgumentException("pageSize <= 0")
        }

        dbc.prepareStatement(SQL_SELECT_ID_GREATER).use {
            it.setValue(1, idGreater)

            it.executeQuery().use {
                var count = 0
                val ret = mutableListOf<TestTable>()

                while (it.next()) {
                    ret.add(
                        TestTable(
                            it.getInt(1),
                            it.getString(2),
                            it.getInt(3)
                        )
                    )
                    count++
                    if (count == pageSize) {
                        return ret
                    }
                }

                return ret
            }
        }
    }


    @Throws(SQLException::class)
    override fun loadPageIdLower(dbc: Connection, idLower: Int, pageSize: Int): List<TestTable> {
        if (pageSize <= 0) {
            throw IllegalArgumentException("pageSize <= 0")
        }

        dbc.prepareStatement(SQL_SELECT_ID_LOWER).use {
            it.setValue(1, idLower)

            it.executeQuery().use {
                var count = 0
                val ret = mutableListOf<TestTable>()

                while (it.next()) {
                    ret.add(
                        TestTable(
                            it.getInt(1),
                            it.getString(2),
                            it.getInt(3)
                        )
                    )
                    count++
                    if (count == pageSize) {
                        return ret
                    }
                }

                return ret
            }
        }
    }

    @Throws(SQLException::class)
    override fun loadLastPage(dbc: Connection, pageSize: Int): List<TestTable> {
        val ret = mutableListOf<TestTable>()
        dbc.prepareStatement(SQL_SELECT_LAST).use {
            it.executeQuery().use {
                var count = 0
                while (it.next()) {
                    ret.add(
                        TestTable(
                            it.getInt(1),
                            it.getString(2),
                            it.getInt(3)
                        )
                    )
                    count++
                    if (count == pageSize) {
                        return ret
                    }
                }
            }
        }

        return ret
    }

    @Throws(SQLException::class)
    override fun count(dbc: Connection): Int {
        dbc.prepareStatement(SQL_COUNT).use {
            it.executeQuery().use {
                it.next()
                return it.getInt(1)
            }
        }
    }

    @Throws(SQLException::class)
    override fun delete(dbc: Connection, id: Int): Int {
        dbc.prepareStatement(SQL_DELETE).use {
            it.setValue(1, id)
            return it.executeUpdate()
        }
    }


    @Throws(SQLException::class)
    override fun deleteAll(dbc: Connection): Int {
        dbc.prepareStatement(SQL_DELETE_ALL).use {
            return it.executeUpdate()
        }
    }
}
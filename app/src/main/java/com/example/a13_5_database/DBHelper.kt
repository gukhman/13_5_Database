package com.example.a13_5_database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "PERSON_DATABASE"
        private const val DATABASE_VERSION = 1
        const val TABLE_NAME = "person_table"
        const val KEY_ID = "id"
        const val KEY_NAME = "name"
        const val KEY_PHONE = "phone"
        const val KEY_ROLE = "role"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val query = ("CREATE TABLE " + TABLE_NAME + " (" +
                KEY_ID + " INTEGER PRIMARY KEY, " +
                KEY_NAME + " TEXT, " +
                KEY_PHONE + " TEXT, " +
                KEY_ROLE + " TEXT" + ")")
        db.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db) // Пересоздание таблицы после удаления
    }

    fun addName(name: String, phone: String, role: Roles): Boolean {
        if (name.length > 1 && phone.isNotEmpty()) {
            val values = ContentValues().apply {
                put(KEY_NAME, name)
                put(KEY_PHONE, phone)
                put(KEY_ROLE, role.rus)
            }

            val db = this.writableDatabase
            val result = db.insert(TABLE_NAME, null, values)
            db.close()

            return result != -1L // Возвращаем true, если вставка успешна
        }
        return false
    }

    fun getInfo(): Cursor? {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $TABLE_NAME", null)
    }

    fun removeAll() {
        val db = this.writableDatabase
        db.delete(TABLE_NAME, null, null)
        db.close() // Закрываем базу после удаления
    }
}
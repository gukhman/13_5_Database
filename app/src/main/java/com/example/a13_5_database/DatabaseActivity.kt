package com.example.a13_5_database

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar

class DatabaseActivity : BaseActivity() {

    private val db = DBHelper(this, null)

    private lateinit var selectedRole: Roles

    private lateinit var nameET: EditText
    private lateinit var phoneET: EditText
    private lateinit var roleSpinner: Spinner
    private lateinit var addBTN: Button
    private lateinit var readBTN: Button
    private lateinit var delBTN: Button
    private lateinit var outputTV: TextView

    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_database)
        setupWindowInsets(R.id.main)
        setupToolbar(R.id.toolbar, true)

        init()

        // Должности
        val roles: MutableList<String> = mutableListOf()
        roles.addAll(Roles.entries.map { it.rus })

        // Создание адаптера
        val rolesAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, roles)
        rolesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Установка адаптера
        roleSpinner.adapter = rolesAdapter

        addBTN.setOnClickListener {
            outputTV.text = ""

            val name = nameET.text.toString()
            val phone = phoneET.text.toString()

            if (name.length > 1 && phone.isNotEmpty()) {
                val isAdd = db.addName(name, phone, selectedRole)
                if (isAdd) {
                    nameET.text.clear()
                    phoneET.text.clear()
                    roleSpinner.setSelection(0)

                    Snackbar.make(it, "Сотрудник $name добавлен", Snackbar.LENGTH_LONG).show()
                } else Snackbar.make(it, "Ошибка добавления в БД", Snackbar.LENGTH_LONG).show()
            } else {
                Snackbar.make(it, "Введите все данные", Snackbar.LENGTH_LONG).show()
            }
        }

        readBTN.setOnClickListener {
            outputTV.text = ""

            val cursor = db.getInfo()
            var rowText = ""
            if (cursor != null && cursor.moveToFirst()) {
                cursor.moveToFirst()

                rowText = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_NAME)) + " | " +
                        cursor.getString(cursor.getColumnIndex(DBHelper.KEY_PHONE)) + "\n" +
                        cursor.getString(cursor.getColumnIndex(DBHelper.KEY_ROLE))

                outputTV.append("$rowText\n-------------------\n")

                Snackbar.make(it, "Данные прочитаны", Snackbar.LENGTH_LONG).show()
            } else
                Snackbar.make(
                    it,
                    "База данных отсутствует, давайте создадим ее",
                    Snackbar.LENGTH_LONG
                ).show()

            while (cursor!!.moveToNext()) {
                rowText = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_NAME)) + " | " +
                        cursor.getString(cursor.getColumnIndex(DBHelper.KEY_PHONE)) + "\n" +
                        cursor.getString(cursor.getColumnIndex(DBHelper.KEY_ROLE))

                outputTV.append("$rowText\n-------------------\n")
            }
            cursor.close()
        }

        delBTN.setOnClickListener {
            db.removeAll()
            nameET.text.clear()
            phoneET.text.clear()
            roleSpinner.setSelection(0)
            outputTV.text = ""

            Snackbar.make(it, "База данных удалена", Snackbar.LENGTH_LONG).show()
        }

        roleSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View?, position: Int, id: Long
            ) {
                selectedRole = Roles.entries[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Действия при отсутствии выбранного элемента
            }
        }
    }

    private fun init() {
        nameET = findViewById(R.id.nameET)
        phoneET = findViewById(R.id.phoneET)
        roleSpinner = findViewById(R.id.roleSpinner)
        addBTN = findViewById(R.id.addBTN)
        readBTN = findViewById(R.id.readBTN)
        delBTN = findViewById(R.id.delBTN)
        outputTV = findViewById(R.id.outputTV)
    }
}
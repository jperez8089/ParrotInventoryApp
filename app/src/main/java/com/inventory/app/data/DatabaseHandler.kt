package com.inventory.app.data

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHandler private constructor(context: Context) : SQLiteOpenHelper(
    context,
    DATABASE_NAME,
    null,
    DATABASE_VERSION
) {

    companion object {

        //defining all of the constants that we are using for the file
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "inventory.db"
        private const val TABLE_NAME = "Items"
        private const val KEY_ID = "id"
        private const val KEY_NAME = "name"
        private const val KEY_QUANTITY = "quantity"
        private const val KEY_DESCRIPTION = "description"

        private var instance: DatabaseHandler? = null


        //these are the static methods

        fun init(context: Context) {//to initialize the database
            if (instance == null) {
                instance = DatabaseHandler(context)
            }
        }

        fun getInstance(): DatabaseHandler {//gets instance for database handler
            return instance ?: throw RuntimeException("Please initialize DatabaseHandler first.")
        }
    }


            // since we are extending from the helper, these are our override methods
    // the oncreate is executed everytime everytime the app goes- executes after the first install.

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = ("CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_NAME + " TEXT,"
                + KEY_QUANTITY + " REAL,"
                + KEY_DESCRIPTION + " TEXT)")
        db?.execSQL(createTableQuery)
    }
//this one is called when there is a change to the structure
    // handles new db changes
    //here is an old source i found but the theory of it is explaiend well

    //https://stackoverflow.com/questions/15667965/why-we-need-to-onupgrade-method-in-sqliteopenhelper-class
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    @SuppressLint("Recycle")
    fun getAllItems(): List<RecordModel> { //returns items present inside the db
        val list = ArrayList<RecordModel>()
        try {
            val selectQuery = "SELECT  * FROM $TABLE_NAME"
            val db = this.readableDatabase
            val cursor = db.rawQuery(selectQuery, null)
            if (cursor.moveToFirst()) { //model to add to the list
                do {
                    val model = RecordModel(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("name")),
                        cursor.getDouble(cursor.getColumnIndexOrThrow("quantity")),
                        cursor.getString(cursor.getColumnIndexOrThrow("description"))
                    )
                    list.add(model)
                } while (cursor.moveToNext())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return list//note to self for future, return list mistake messed you up prior! CHECK THIS
    }
        //this method allows us to delete
    fun deleteItem(id: Int) {
        val db = this.writableDatabase
        val where = "id=?"
        val whereArgs = arrayOf(id.toString())
        db.delete(TABLE_NAME, where, whereArgs)
        db.close()
    }
       //takes the info that is entered and needed to save
    fun saveItem(name: String, quantity: Double, description: String) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_NAME, name)
        contentValues.put(KEY_QUANTITY, quantity)
        contentValues.put(KEY_DESCRIPTION, description) //here is where
        db.insert(TABLE_NAME, null, contentValues)//we use the method
        db.close()
    }

    fun updateItem(name: String, quantity: Double, description: String, id: Int) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_NAME, name)
        contentValues.put(KEY_QUANTITY, quantity)
        contentValues.put(KEY_DESCRIPTION, description)
        db.update(TABLE_NAME, contentValues, "$KEY_ID = ?", arrayOf(id.toString()))
        db.close()
    }
}
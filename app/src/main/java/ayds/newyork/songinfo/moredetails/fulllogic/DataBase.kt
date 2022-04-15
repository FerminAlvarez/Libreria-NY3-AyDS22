package ayds.newyork.songinfo.moredetails.fulllogic

import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteDatabase
import ayds.newyork.songinfo.moredetails.fulllogic.DataBase
import android.content.ContentValues
import android.content.Context
import android.util.Log
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.util.ArrayList

private const val DATABASE_URL = "jdbc:sqlite:./dictionary.db"
private const val ARTISTS_TABLE = "artists"
private const val ID = "id"
private const val ARTIST = "artist"
private const val INFO = "info"
private const val SOURCE = "source"

class DataBase(context: Context?) : SQLiteOpenHelper(context, "dictionary.db", null, 1) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "create table " + ARTISTS_TABLE + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + ARTIST + " string, " + INFO + " string, " + SOURCE + " integer)"
        )
        Log.i("DB", "DB created")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

    companion object {

        fun testDB() {
            var connection: Connection? = null
            try {
                connection = DriverManager.getConnection(DATABASE_URL)
                val statement = connection.createStatement()
                statement.queryTimeout = 30
                val rs = statement.executeQuery("select * from " + ARTISTS_TABLE)
                while (rs.next()) {
                    println("id = " + rs.getInt(ID))
                    println("artist = " + rs.getString(ARTIST))
                    println("info = " + rs.getString(INFO))
                    println("source = " + rs.getString(SOURCE))
                }
            } catch (e: SQLException) {
                System.err.println(e.message)
            } finally {
                try {
                    connection?.close()
                } catch (e: SQLException) {
                    System.err.println(e)
                }
            }
        }

        @JvmStatic
        fun saveArtist(dbHelper: DataBase, artist: String?, info: String?) {
            val db = dbHelper.writableDatabase
            val values = ContentValues()
            values.put(ARTIST, artist)
            values.put(INFO, info)
            values.put(SOURCE, 1)

            // Insert the new row, returning the primary key value of the new row
            val newRowId = db.insert(ARTISTS_TABLE, null, values)
        }

        @JvmStatic
        fun getInfo(dbHelper: DataBase, artist: String): String? {
            val db = dbHelper.readableDatabase
            val projection = arrayOf(
                ID,
                ARTIST,
                INFO
            )
            val selection = "artist  = ?"
            val selectionArgs = arrayOf(artist)
            val sortOrder = "artist DESC"
            val cursor = db.query(
                ARTISTS_TABLE,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
            )
            val items: MutableList<String> = ArrayList()
            while (cursor.moveToNext()) {
                val info = cursor.getString(
                    cursor.getColumnIndexOrThrow(INFO)
                )
                items.add(info)
            }
            cursor.close()
            return if (items.isEmpty()) null else items[0]
        }
    }
}
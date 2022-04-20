package ayds.newyork.songinfo.moredetails.fulllogic

import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteDatabase
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.util.Log
import java.sql.*
import java.util.ArrayList

private const val ARTISTS_TABLE = "artists"
private const val ID = "id"
private const val ARTIST = "artist"
private const val INFO = "info"
private const val SOURCE = "source"

class DataBase(context: Context?) : SQLiteOpenHelper(context, "dictionary.db", null, 1) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "create table $ARTISTS_TABLE ($ID INTEGER PRIMARY KEY AUTOINCREMENT, $ARTIST string, $INFO string, $SOURCE integer)"
        )
        Log.i("DB", "DB created")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

    companion object {

        @JvmStatic
        fun saveArtist(dbHelper: DataBase, artist: String?, info: String?) {
            val db = dbHelper.writableDatabase
            val values = createValues(artist, info, "1")

            val newRowId = insertValues(db, ARTISTS_TABLE, values)
        }

        private fun createValues(artist: String?, info: String?, source: String?): ContentValues {
            val values = ContentValues()
            values.put(ARTIST, artist)
            values.put(INFO, info)
            values.put(SOURCE, source)
            return values
        }

        private fun insertValues(db: SQLiteDatabase, table: String, values: ContentValues): Long {
            return db.insert(table, null, values)
        }

        @JvmStatic
        fun getInfo(dbHelper: DataBase, artist: String): String? {
            val db = dbHelper.readableDatabase
            val projection = createArtistProjection()
            val selection = "$ARTIST  = ?"
            val selectionArgs = arrayOf(artist)
            val sortOrder = "$ARTIST DESC"
            val cursor = executeQuery(
                db, ARTISTS_TABLE, projection, selection, selectionArgs,
                null, null, sortOrder
            )
            val items = getInfo(cursor)
            cursor.close()
            return if (items.isEmpty()) null else items[0]
        }

        private fun createArtistProjection() = arrayOf(ID, ARTIST, INFO)

        private fun executeQuery(
            db: SQLiteDatabase, table: String, projection: Array<String>, selection: String,
            selectionArgs: Array<String>, groupBy: String?, having: String?, sortOrder: String
        ): Cursor {
            return db.query(
                table,
                projection,
                selection,
                selectionArgs,
                groupBy,
                having,
                sortOrder
            )
        }

        private fun getInfo(cursor: Cursor): MutableList<String> {
            val items: MutableList<String> = ArrayList()
            while (cursor.moveToNext()) {
                val info = cursor.getString(
                    cursor.getColumnIndexOrThrow(INFO)
                )
                items.add(info)
            }
            return items
        }
    }
}
package ayds.newyork.songinfo.moredetails.fulllogic

import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteDatabase
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import java.util.ArrayList

private const val DATABASE_NAME = "dictionary.db"
private const val DATABASE_VERSION = 1
private const val ARTISTS_TABLE = "artists"
private const val ID_COLUMN = "id"
private const val ARTIST_COLUMN = "artist"
private const val INFO_COLUMN = "info"
private const val CREATE_TABLE_QUERY = "create table $ARTISTS_TABLE ($ID_COLUMN INTEGER PRIMARY KEY AUTOINCREMENT, $ARTIST_COLUMN string, $INFO_COLUMN string)"
private const val QUERY_SELECTION = "$ARTIST_COLUMN  = ?"
private const val QUERY_SORT_ORDER = "$ARTIST_COLUMN DESC"

class DataBase(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME , null, DATABASE_VERSION) {
    override fun onCreate(database: SQLiteDatabase) {
        database.execSQL(CREATE_TABLE_QUERY)
    }

    override fun onUpgrade(database: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

    companion object {

        @JvmStatic
        fun saveArtist(dbHelper: DataBase, artist: String, info: String) {
            val database = dbHelper.writableDatabase
            val newArtist = createArtist(artist, info)
            insertNewArtist(database, newArtist)
        }

        private fun createArtist(artist: String, info: String): ContentValues {
            val values = ContentValues()
            values.put(ARTIST_COLUMN, artist)
            values.put(INFO_COLUMN, info)
            return values
        }

        private fun insertNewArtist(database: SQLiteDatabase, values: ContentValues): Long =
            database.insert(ARTISTS_TABLE, null, values)

        @JvmStatic
        fun getInfo(dbHelper: DataBase, artist: String): String? {
            val database = dbHelper.readableDatabase
            val projection = createArtistProjection()
            val selectionArgs = arrayOf(artist)
            val cursor = executeQuery(
                database, projection, selectionArgs)
            val items = getInfo(cursor)
            closeCursor(cursor)
            return if (items.isEmpty()) null else items[0]
        }

        private fun closeCursor(cursor: Cursor) =
            cursor.close()

        private fun createArtistProjection() = arrayOf(ID_COLUMN, ARTIST_COLUMN, INFO_COLUMN)

        private fun executeQuery(
            database: SQLiteDatabase,
            projection: Array<String>,
            selectionArgs: Array<String>,
            groupBy: String? = null,
            having: String? = null
        ): Cursor {
            return database.query(
                ARTISTS_TABLE,
                projection,
                QUERY_SELECTION,
                selectionArgs,
                groupBy,
                having,
                QUERY_SORT_ORDER
            )
        }

        private fun getInfo(cursor: Cursor): MutableList<String> {
            val items: MutableList<String> = ArrayList()
            while (cursor.moveToNext()) {
                val info = cursor.getString(
                    cursor.getColumnIndexOrThrow(INFO_COLUMN)
                )
                items.add(info)
            }
            return items
        }
    }
}
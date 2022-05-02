package ayds.newyork.songinfo.moredetails

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
private const val CREATE_TABLE_QUERY =
    "create table $ARTISTS_TABLE ($ID_COLUMN INTEGER PRIMARY KEY AUTOINCREMENT, $ARTIST_COLUMN string, $INFO_COLUMN string)"
private const val QUERY_SELECTION = "$ARTIST_COLUMN  = ?"
private const val QUERY_SORT_ORDER = "$ARTIST_COLUMN DESC"

class DataBase(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(database: SQLiteDatabase) {
        createArtistsTable(database)
    }

    private fun createArtistsTable(database: SQLiteDatabase) = database.execSQL(CREATE_TABLE_QUERY)

    override fun onUpgrade(database: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

    fun saveArtist(artist: String, info: String) {
        val newArtist = setArtistValues(artist, info)
        insertNewArtist(newArtist)
    }

    private fun setArtistValues(artist: String, info: String): ContentValues {
        val values = ContentValues()
        values.put(ARTIST_COLUMN, artist)
        values.put(INFO_COLUMN, info)
        return values
    }

    private fun insertNewArtist(artist: ContentValues) {
        val database = this.writableDatabase
        database.insert(ARTISTS_TABLE, null, artist)
    }

    fun getInfo(artist: String): String? {
        val cursor = getCursor(artist)
        val artistInfoItems = getArtistInfo(cursor)
        cursor.close()
        return artistInfoItems.firstOrNull()
    }

    private fun getCursor(artist: String): Cursor {
        val database = this.readableDatabase
        val projection = arrayOf(ID_COLUMN, ARTIST_COLUMN, INFO_COLUMN)
        val selectionArgs = arrayOf(artist)
        return database.query(
            ARTISTS_TABLE,
            projection,
            QUERY_SELECTION,
            selectionArgs,
            null,
            null,
            QUERY_SORT_ORDER
        )
    }

    private fun getArtistInfo(cursor: Cursor): MutableList<String> {
        val artistInfoItems: MutableList<String> = ArrayList()
        while (cursor.moveToNext()) {
            val info = cursor.getString(
                cursor.getColumnIndexOrThrow(INFO_COLUMN)
            )
            artistInfoItems.add(info)
        }
        return artistInfoItems
    }
}
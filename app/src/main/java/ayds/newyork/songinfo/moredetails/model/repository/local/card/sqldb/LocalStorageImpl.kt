package ayds.newyork.songinfo.moredetails.model.repository.local.card.sqldb

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import ayds.newyork.songinfo.moredetails.model.entities.CardImpl
import ayds.newyork.songinfo.moredetails.model.repository.local.card.LocalStorage

const val DATABASE_NAME = "dictionary.db"
private const val DATABASE_VERSION = 1
private const val QUERY_SELECTION = "$ARTIST_COLUMN  = ?"
private const val QUERY_SORT_ORDER = "$ARTIST_COLUMN DESC"

class LocalStorageImpl(
    context: Context,
    private val cursorToArtistArticleMapper: CursorToArtistArticleMapper
) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION),
    LocalStorage {

    private val projection = arrayOf(
        ID_COLUMN,
        ARTIST_COLUMN,
        INFO_COLUMN,
        SOURCE_COLUMN,
        SOURCE_LOGO_URL_COLUMN,
        URL_COLUMN
    )

    override fun onCreate(database: SQLiteDatabase) {
        createArtistsTable(database)
    }

    private fun createArtistsTable(database: SQLiteDatabase) =
        database.execSQL(createArtistArticleTableQuery)

    override fun onUpgrade(database: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

    override fun saveArtist(artist: CardImpl, artistName: String) {
        val newArtist = setArtistValues(artist, artistName)
        insertNewArtist(newArtist)
    }

    private fun setArtistValues(artist: CardImpl, artistName: String): ContentValues {
        val values = ContentValues()
        values.put(ARTIST_COLUMN, artistName)
        values.put(INFO_COLUMN, artist.description)
        values.put(URL_COLUMN, artist.infoURL)
        values.put(SOURCE_COLUMN, artist.source.ordinal)
        values.put(SOURCE_LOGO_URL_COLUMN, artist.sourceLogoUrl)
        return values
    }

    private fun insertNewArtist(artist: ContentValues) {
        val database = this.writableDatabase
        database.insert(ARTISTS_TABLE, null, artist)
    }

    override fun getInfoByArtistName(artist: String): CardImpl? {
        val cursor = getCursor(artist)
        return cursorToArtistArticleMapper.map(cursor)
    }

    private fun getCursor(artist: String): Cursor {
        val database = this.readableDatabase
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
}
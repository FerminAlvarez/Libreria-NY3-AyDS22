package ayds.newyork.songinfo.moredetails.model.repository.local.nyt.sqldb

import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteDatabase
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import ayds.newyork.songinfo.moredetails.model.entities.NytArtistInfo
import ayds.newyork.songinfo.moredetails.model.repository.local.nyt.NytLocalStorage

const val DATABASE_NAME = "dictionary.db"
private const val DATABASE_VERSION = 1
private const val QUERY_SELECTION = "$ARTIST_COLUMN  = ?"
private const val QUERY_SORT_ORDER = "$ARTIST_COLUMN DESC"

class NytLocalStorageImpl(
    context: Context,
    private val cursorToNytArtistArticleMapper: CursorToNytArtistArticleMapper
) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION),
    NytLocalStorage {

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
        database.execSQL(createNytArtistArticleTableQuery)

    override fun onUpgrade(database: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

    override fun saveArtist(artist: NytArtistInfo) {
        val newArtist = setArtistValues(artist)
        insertNewArtist(newArtist)
    }

    private fun setArtistValues(artist: NytArtistInfo): ContentValues {
        val values = ContentValues()
        values.put(ARTIST_COLUMN, artist.artistName)
        values.put(INFO_COLUMN, artist.artistInfo)
        values.put(URL_COLUMN, artist.artistURL)
        values.put(SOURCE_COLUMN, artist.source)
        values.put(SOURCE_LOGO_URL_COLUMN, artist.sourceLogoUrl)
        return values
    }

    private fun insertNewArtist(artist: ContentValues) {
        val database = this.writableDatabase
        database.insert(ARTISTS_TABLE, null, artist)
    }

    override fun getInfoByArtistName(artist: String): NytArtistInfo? {
        val cursor = getCursor(artist)
        return cursorToNytArtistArticleMapper.map(cursor)
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
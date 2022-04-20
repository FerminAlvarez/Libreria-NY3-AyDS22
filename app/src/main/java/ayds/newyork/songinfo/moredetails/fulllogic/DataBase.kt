package ayds.newyork.songinfo.moredetails.fulllogic

import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteDatabase
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.util.Log
import java.sql.*
import java.util.ArrayList

private const val DATABASE_URL = "jdbc:sqlite:./dictionary.db"
private const val QUERY_TIMEOUT = 30
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

        fun testDB() {
            var connection: Connection? = null
            try {
                connection = getConnection()
                val statement = prepareStatement(connection)
                val rs = getAllArtists(statement)
                while (rs.next()) {
                    println("id = " + rs.getInt(ID))
                    println("artist = " + rs.getString(ARTIST))
                    println("info = " + rs.getString(INFO))
                    println("source = " + rs.getString(SOURCE))
                }
            } catch (e: SQLException) {
                System.err.println(e.message)
            } finally {
                closeConnection(connection)
            }
        }

        private fun getConnection(): Connection {
            return DriverManager.getConnection(DATABASE_URL)
        }

        private fun getAllArtists(statement: Statement): ResultSet {
            return statement.executeQuery("select * from $ARTISTS_TABLE")
        }

        private fun prepareStatement(connection: Connection): Statement {
            val statement = connection.createStatement()
            statement.queryTimeout = QUERY_TIMEOUT
            return statement
        }

        private fun closeConnection(connection: Connection?) {
            try {
                connection?.close()
            } catch (e: SQLException) {
                System.err.println(e)
            }
        }

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
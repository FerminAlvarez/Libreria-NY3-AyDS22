package ayds.newyork.songinfo.moredetails.model.repository.local.nyt.sqldb

import android.database.Cursor
import ayds.newyork.songinfo.moredetails.model.entities.NytArtistInfo
import java.sql.SQLException

interface CursorToNytArtistArticleMapper {
    fun map(cursor: Cursor): NytArtistInfo?
}

internal class CursorToNytArtistArticleMapperImpl : CursorToNytArtistArticleMapper {

    override fun map(cursor: Cursor): NytArtistInfo? =
        try {
            with(cursor) {
                if (moveToNext()) {
                    NytArtistInfo(
                        artistName = getString(getColumnIndexOrThrow(ARTIST_COLUMN)),
                        artistInfo = getString(getColumnIndexOrThrow(INFO_COLUMN)),
                        artistURL = getString(getColumnIndexOrThrow(URL_COLUMN))
                    )
                } else {
                    null
                }
            }
        } catch (e: SQLException) {
            e.printStackTrace()
            null
        }
}
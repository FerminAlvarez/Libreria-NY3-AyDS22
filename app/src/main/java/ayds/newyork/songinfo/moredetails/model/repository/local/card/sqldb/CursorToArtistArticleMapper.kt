package ayds.newyork.songinfo.moredetails.model.repository.local.card.sqldb

import android.database.Cursor
import ayds.newyork.songinfo.moredetails.model.entities.CardImpl
import ayds.newyork.songinfo.moredetails.model.repository.broker.InfoSource
import java.sql.SQLException

interface CursorToArtistArticleMapper {
    fun map(cursor: Cursor): CardImpl?
}

internal class CursorToArtistArticleMapperImpl : CursorToArtistArticleMapper {

    override fun map(cursor: Cursor): CardImpl? =
        try {
            with(cursor) {
                if (moveToNext()) {
                    val storedSourceOrdinal = getInt(getColumnIndexOrThrow(SOURCE_COLUMN))
                    CardImpl(
                        description = getString(getColumnIndexOrThrow(INFO_COLUMN)),
                        infoURL = getString(getColumnIndexOrThrow(URL_COLUMN)),
                        source = InfoSource.values()[storedSourceOrdinal],
                        sourceLogoUrl = getString(getColumnIndexOrThrow(SOURCE_LOGO_URL_COLUMN)),
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
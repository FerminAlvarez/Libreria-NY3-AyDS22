package ayds.newyork.songinfo.moredetails.model.repository.local.card.sqldb

import android.database.Cursor
import ayds.newyork.songinfo.moredetails.model.entities.CardImpl
import ayds.newyork.songinfo.moredetails.model.repository.broker.InfoSource
import java.sql.SQLException
import java.util.*

interface CursorToArtistArticleMapper {
    fun map(cursor: Cursor): List<CardImpl>
}

internal class CursorToArtistArticleMapperImpl : CursorToArtistArticleMapper {

    override fun map(cursor: Cursor): List<CardImpl> {
        val cards = LinkedList<CardImpl>()
        try {
            with(cursor) {
                while (moveToNext()) {
                    val storedSourceOrdinal = getInt(getColumnIndexOrThrow(SOURCE_COLUMN))
                    var card = CardImpl(
                        description = getString(getColumnIndexOrThrow(INFO_COLUMN)),
                        infoURL = getString(getColumnIndexOrThrow(URL_COLUMN)),
                        source = InfoSource.values()[storedSourceOrdinal],
                        sourceLogoUrl = getString(getColumnIndexOrThrow(SOURCE_LOGO_URL_COLUMN)),
                    )
                    cards.add(card)
                }
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        return cards
    }
}
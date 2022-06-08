package ayds.newyork.songinfo.moredetails.model.repository.local.card.sqldb

import android.database.Cursor
import ayds.newyork.songinfo.moredetails.model.entities.Card
import ayds.newyork.songinfo.moredetails.model.repository.broker.InfoSource
import java.sql.SQLException
import java.util.*

interface CursorToArtistArticleMapper {
    fun map(cursor: Cursor): List<Card>
}

internal class CursorToArtistArticleMapperImpl : CursorToArtistArticleMapper {

    override fun map(cursor: Cursor): List<Card> {
        val cards = LinkedList<Card>()
        try {
            with(cursor) {
                while (moveToNext()) {
                    val storedSourceOrdinal = getInt(getColumnIndexOrThrow(SOURCE_COLUMN))
                    val card = Card(
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
package ayds.newyork.songinfo.moredetails.model.repository

import ayds.newyork.songinfo.moredetails.model.entities.Card
import ayds.newyork.songinfo.moredetails.model.entities.CardImpl
import ayds.newyork.songinfo.moredetails.model.repository.broker.InfoBroker
import ayds.newyork.songinfo.moredetails.model.repository.local.card.LocalStorage
import java.util.*

interface ArtistInfoRepository {
    fun getInfoByArtistName(artist: String): List<Card>
}

internal class ArtistInfoRepositoryImpl(
    private val localStorage: LocalStorage,
    private val infoBroker: InfoBroker
) : ArtistInfoRepository {


    override fun getInfoByArtistName(artist: String): LinkedList<Card> {
        var artistCard = localStorage.getInfoByArtistName(artist)
        val artistCardResult = LinkedList<Card>()

        when {
            artistCard != null -> markArticleAsLocal(artistCard)
            else -> {
                val infoList = infoBroker.getInfoByArtistName(artist)

                if (infoList.isEmpty())
                    artistCard = null
                else
                    artistCard = infoList.first()

                artistCard?.let {
                    localStorage.saveArtist(it, artist)
                }
            }
        }
        artistCard?.let {
            artistCardResult.add(artistCard)
        }
        return artistCardResult
    }

    private fun markArticleAsLocal(article: CardImpl) {
        article.isLocallyStored = true
    }
}
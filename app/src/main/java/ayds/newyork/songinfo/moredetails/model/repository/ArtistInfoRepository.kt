package ayds.newyork.songinfo.moredetails.model.repository

import ayds.newyork.songinfo.moredetails.model.entities.Card
import ayds.newyork.songinfo.moredetails.model.entities.CardImpl
import ayds.newyork.songinfo.moredetails.model.entities.EmptyCard
import ayds.newyork.songinfo.moredetails.model.repository.broker.InfoBroker
import ayds.newyork.songinfo.moredetails.model.repository.local.card.LocalStorage

interface ArtistInfoRepository {
    fun getInfoByArtistName(artist: String): Card
}

internal class ArtistInfoRepositoryImpl(
    private val localStorage: LocalStorage,
    private val infoBroker: InfoBroker
) : ArtistInfoRepository {


    override fun getInfoByArtistName(artist: String): Card {
        var artistCard = localStorage.getInfoByArtistName(artist)

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
        return artistCard ?: EmptyCard
    }

    private fun markArticleAsLocal(article: CardImpl) {
        article.isLocallyStored = true
    }
}
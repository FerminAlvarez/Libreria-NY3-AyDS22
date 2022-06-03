package ayds.newyork.songinfo.moredetails.model.repository

import ayds.newyork.songinfo.moredetails.model.entities.Card
import ayds.newyork.songinfo.moredetails.model.entities.CardImpl
import ayds.newyork.songinfo.moredetails.model.repository.broker.InfoBroker
import ayds.newyork.songinfo.moredetails.model.repository.local.card.LocalStorage

interface ArtistInfoRepository {
    fun getInfoByArtistName(artist: String): List<Card>
}

internal class ArtistInfoRepositoryImpl(
    private val localStorage: LocalStorage,
    private val infoBroker: InfoBroker
) : ArtistInfoRepository {


    override fun getInfoByArtistName(artist: String): List<Card> {
        var infoList = localStorage.getInfoByArtistName(artist)

        when {
            infoList.isNotEmpty() -> markArticleAsLocal(infoList)
            else -> {
                infoList = infoBroker.getInfoByArtistName(artist)

                if (infoList.isNotEmpty()) {
                    saveArtists(infoList, artist)
                }

            }
        }
        return infoList
    }

    private fun markArticleAsLocal(articles: List<CardImpl>) {
        articles.map { it.isLocallyStored = true }
    }

    private fun saveArtists(articles: List<CardImpl>, artist: String) {
        articles.map { localStorage.saveArtist(it, artist) }
    }
}
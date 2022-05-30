package ayds.newyork.songinfo.moredetails.model.repository

import ayds.newyork.songinfo.moredetails.model.entities.Card
import ayds.newyork.songinfo.moredetails.model.entities.CardImpl
import ayds.newyork.songinfo.moredetails.model.entities.EmptyCard
import ayds.newyork.songinfo.moredetails.model.repository.local.card.LocalStorage
import ayds.ny3.newyorktimes.NytArticleService
import ayds.ny3.newyorktimes.NytCard

interface ArtistInfoRepository {
    fun getInfoByArtistName(artist: String): Card
}

internal class ArtistInfoRepositoryImpl(
    private val localStorage: LocalStorage,
    private val nytArticleService: NytArticleService
) : ArtistInfoRepository {


    override fun getInfoByArtistName(artist: String): Card {
        var artistCard = localStorage.getInfoByArtistName(artist)

        when {
            artistCard != null -> markArticleAsLocal(artistCard)
            else -> {
                try {
                    val serviceNytArtistInfo = nytArticleService.getArtistInfo(artist)

                    serviceNytArtistInfo?.let {
                        artistCard = createArtistInfo(it)
                    }

                    artistCard?.let {
                        localStorage.saveArtist(it, artist)
                    }
                } catch (e: Exception) {
                    artistCard = null
                }
            }
        }
        return artistCard ?: EmptyCard
    }

    private fun markArticleAsLocal(article: CardImpl) {
        article.isLocallyStored = true
    }

    private fun createArtistInfo(card: NytCard) =
        CardImpl(
            card.description,
            card.infoURL,
            card.source,
            card.sourceLogoUrl,
            false
        )
}
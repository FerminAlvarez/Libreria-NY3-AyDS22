package ayds.newyork.songinfo.moredetails.model.repository.broker.proxy.newyorktimes

import ayds.newyork.songinfo.moredetails.model.entities.Card
import ayds.newyork.songinfo.moredetails.model.entities.CardImpl
import ayds.newyork.songinfo.moredetails.model.entities.EmptyCard
import ayds.newyork.songinfo.moredetails.model.repository.broker.InfoSource
import ayds.ny3.newyorktimes.NytArticleService
import ayds.ny3.newyorktimes.NytArtistInfo


interface NytProxy {
    fun getInfoByArtistNameNyt(artist: String): Card
}

internal class NytProxyImpl(
    private val nytArticleService: NytArticleService
) : NytProxy {

    override fun getInfoByArtistNameNyt(artist: String): Card {
        var artistCard: CardImpl? = null

        try {
            val serviceNytArtistInfo = nytArticleService.getArtistInfo(artist)
            serviceNytArtistInfo?.let {
                artistCard = createArtistInfo(it)
            }
        } catch (e: Exception) {
            artistCard = null
        }
        return artistCard ?: EmptyCard
    }

    private fun createArtistInfo(nytArtistInfo: NytArtistInfo) =
        CardImpl(
            nytArtistInfo.artistInfo,
            nytArtistInfo.artistURL,
            InfoSource.NEW_YORK_TIMES,
            nytArtistInfo.nytLogo,
            false
        )
}
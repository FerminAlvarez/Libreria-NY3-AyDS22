package ayds.newyork.songinfo.moredetails.model.repository.broker.proxy.newyorktimes

import ayds.newyork.songinfo.moredetails.model.entities.Card
import ayds.newyork.songinfo.moredetails.model.entities.CardImpl
import ayds.newyork.songinfo.moredetails.model.entities.EmptyCard
import ayds.ny3.newyorktimes.NytArticleService
import ayds.ny3.newyorktimes.NytArtistInfo

private const val SOURCE = "The New York Times"
private const val NYT_LOGO = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRVioI832nuYIXqzySD8cOXRZEcdlAj3KfxA62UEC4FhrHVe0f7oZXp3_mSFG7nIcUKhg&usqp=CAU"

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
            SOURCE,
            NYT_LOGO,
            false
        )
}
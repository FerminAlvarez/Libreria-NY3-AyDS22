package ayds.newyork.songinfo.moredetails.model.repository.broker.proxy.newyorktimes

import ayds.newyork.songinfo.moredetails.model.entities.Card
import ayds.newyork.songinfo.moredetails.model.repository.broker.InfoSource
import ayds.newyork.songinfo.moredetails.model.repository.broker.proxy.ServiceProxy
import ayds.ny3.newyorktimes.NytArticleService
import ayds.ny3.newyorktimes.NytArtistInfo

internal class NytProxy(
    private val nytArticleService: NytArticleService
) : ServiceProxy {

    override fun getInfo(artist: String): Card? {
        return try {
            nytArticleService.getArtistInfo(artist)?.let {
                createArtistInfo(it)
            }
        } catch (e: Exception) {
            null
        }
    }

    private fun createArtistInfo(nytArtistInfo: NytArtistInfo) =
        Card(
            nytArtistInfo.artistInfo,
            nytArtistInfo.artistURL,
            InfoSource.NEW_YORK_TIMES,
            nytArtistInfo.nytLogo,
            false
        )
}
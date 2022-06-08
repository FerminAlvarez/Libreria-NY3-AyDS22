package ayds.newyork.songinfo.moredetails.model.repository.broker.proxy.wikipedia

import ayds.newyork.songinfo.moredetails.model.entities.Card
import ayds.newyork.songinfo.moredetails.model.entities.CardImpl
import ayds.newyork.songinfo.moredetails.model.repository.broker.InfoSource
import ayds.newyork.songinfo.moredetails.model.repository.broker.proxy.ServiceProxy
import ayds.winchester1.wikipedia.WikipediaArtistInfo
import ayds.winchester1.wikipedia.WikipediaService

internal class WikipediaProxy(
    private val wikipediaService: WikipediaService
) : ServiceProxy {

    override fun getInfo(artist: String): Card? {
        return try {
            wikipediaService.getArtistInfo(artist)?.let {
                createArtistInfo(it)
            }
        } catch (e: Exception) {
            null
        }
    }

    private fun createArtistInfo(wikipediaArtistInfo: WikipediaArtistInfo) =
        CardImpl(
            wikipediaArtistInfo.description,
            wikipediaArtistInfo.infoURL,
            InfoSource.WIKIPEDIA,
            wikipediaArtistInfo.sourceLogoURL,
            false
        )
}
package ayds.newyork.songinfo.moredetails.model.repository.broker.proxy.wikipedia

import ayds.newyork.songinfo.moredetails.model.entities.Card
import ayds.newyork.songinfo.moredetails.model.entities.CardImpl
import ayds.newyork.songinfo.moredetails.model.entities.EmptyCard
import ayds.newyork.songinfo.moredetails.model.repository.broker.InfoSource
import ayds.newyork.songinfo.moredetails.model.repository.broker.proxy.ServiceProxy
import ayds.winchester1.wikipedia.WikipediaArtistInfo
import ayds.winchester1.wikipedia.WikipediaCardService


internal class WikipediaProxyImpl(
    private val wikipediaService: WikipediaCardService
) : ServiceProxy {

    override fun getInfo(artist: String): Card {
        var artistCard: CardImpl? = null

        try {
            val serviceArtistInfo = wikipediaService.getCard(artist)
            serviceArtistInfo?.let {
                artistCard = createArtistInfo(it)
            }
        } catch (e: Exception) {
            artistCard = null
        }
        return artistCard ?: EmptyCard
    }

    private fun createArtistInfo(wikipediaArtistInfo: WikipediaArtistInfo) =
        CardImpl(
            wikipediaArtistInfo.description,
            wikipediaArtistInfo.infoURL,
            InfoSource.LAST_FM,
            "",
            false
        )
}
package ayds.newyork.songinfo.moredetails.model.repository.broker.proxy.lastfm

import ayds.lisboa.lastfmdata.lastfm.LastFMService
import ayds.lisboa.lastfmdata.lastfm.entities.LastFMArtist
import ayds.newyork.songinfo.moredetails.model.entities.Card
import ayds.newyork.songinfo.moredetails.model.entities.CardImpl
import ayds.newyork.songinfo.moredetails.model.entities.EmptyCard
import ayds.newyork.songinfo.moredetails.model.repository.broker.InfoSource
import ayds.newyork.songinfo.moredetails.model.repository.broker.proxy.ServiceProxy


internal class LastFMProxyImpl(
    private val lastFMService: LastFMService
) : ServiceProxy {

    override fun getInfo(artist: String): Card {
        var artistCard: CardImpl? = null

        try {
            val serviceArtistInfo = lastFMService.getArtist(artist)
            serviceArtistInfo?.let {
                artistCard = createArtistInfo(it)
            }
        } catch (e: Exception) {
            artistCard = null
        }
        return artistCard ?: EmptyCard
    }

    private fun createArtistInfo(lastFMArtistInfo: LastFMArtist) =
        CardImpl(
            lastFMArtistInfo.description,
            lastFMArtistInfo.infoUrl,
            InfoSource.LAST_FM,
            lastFMArtistInfo.sourceLogoUrl,
            false
        )
}
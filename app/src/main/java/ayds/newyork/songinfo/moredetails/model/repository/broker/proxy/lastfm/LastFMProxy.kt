package ayds.newyork.songinfo.moredetails.model.repository.broker.proxy.lastfm

import ayds.lisboa.lastfmdata.lastfm.LastFMService
import ayds.lisboa.lastfmdata.lastfm.entities.LastFMArtist
import ayds.newyork.songinfo.moredetails.model.entities.Card
import ayds.newyork.songinfo.moredetails.model.entities.CardImpl
import ayds.newyork.songinfo.moredetails.model.repository.broker.InfoSource
import ayds.newyork.songinfo.moredetails.model.repository.broker.proxy.ServiceProxy

internal class LastFMProxy(
    private val lastFMService: LastFMService
) : ServiceProxy {

    override fun getInfo(artist: String): Card? {
        return try {
            lastFMService.getArtist(artist)?.let {
                createArtistInfo(it)
            }
        } catch (e: Exception) {
            null
        }
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
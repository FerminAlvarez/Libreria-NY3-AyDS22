package ayds.newyork.songinfo.moredetails.view

import ayds.newyork.songinfo.moredetails.model.entities.ArtistInfo
import ayds.newyork.songinfo.moredetails.model.entities.EmptyArtistInfo
import ayds.newyork.songinfo.moredetails.model.entities.NytArtistInfo

private const val INFO_IN_DATABASE_SYMBOL = "[*] \n"
private const val ARTIST_INFO_NOT_FOUND = "Artist Info not found"

interface ArtistInfoHelper {
    fun getArtistInfoText(artistInfo: ArtistInfo = EmptyArtistInfo): String
}

internal class ArtistInfoHelperImpl : ArtistInfoHelper {

    override fun getArtistInfoText(artistInfo: ArtistInfo) = when (artistInfo) {
        is NytArtistInfo -> formatArtisInfo(artistInfo)
        else -> ARTIST_INFO_NOT_FOUND
    }

    private fun formatArtisInfo(artistInfo: ArtistInfo): String {
        val symbol = if (artistInfo.isLocallyStored) INFO_IN_DATABASE_SYMBOL else ""
        return symbol + artistInfo.artistInfo
    }

}
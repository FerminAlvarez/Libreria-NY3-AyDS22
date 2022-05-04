package ayds.newyork.songinfo.moredetails.view

import ayds.newyork.songinfo.moredetails.model.entities.ArtistInfo
import ayds.newyork.songinfo.moredetails.model.entities.EmptyArtistInfo

interface ArtistInfoHelper {
    fun getArtistInfoText(artistInfo: ArtistInfo = EmptyArtistInfo): String
}

internal class ArtistInfoHelperImpl : ArtistInfoHelper {
    override fun getArtistInfoText(artistInfo: ArtistInfo): String {
        return when (artistInfo) {
            is ArtistInfo ->
                "${
                    if (artistInfo.isLocallyStored) "[*]" else ""
                }\n" +
                        { artistInfo.artistInfo }
            else -> "Artist Info not found"
        }
    }

}
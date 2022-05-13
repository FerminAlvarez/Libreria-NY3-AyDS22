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
        is NytArtistInfo -> formatArtistInfo(artistInfo)
        else -> ARTIST_INFO_NOT_FOUND
    }

    private fun formatArtistInfo(artistInfo: ArtistInfo): String {
        return (if (artistInfo.isLocallyStored) INFO_IN_DATABASE_SYMBOL else "") +
                articleToHTML(artistInfo)
    }

    private fun articleToHTML(artistInfo: ArtistInfo): String {
        val builder = StringBuilder()
        builder.append("<html><div width=400>")
        builder.append("<font face=\"arial\">")
        val textWithBold = artistInfo.artistInfo
            .replace("'", " ")
            .replace("\n", "<br>")
            .replace("(?i)" + artistInfo.artistName.toRegex(), "<b>" + artistInfo.artistName.uppercase() + "</b>")
        builder.append(textWithBold)
        builder.append("</font></div></html>")
        return builder.toString()
    }
}
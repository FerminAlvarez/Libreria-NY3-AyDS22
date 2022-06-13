package ayds.newyork.songinfo.moredetails.view

import ayds.newyork.songinfo.moredetails.model.entities.*

private const val INFO_IN_DATABASE_SYMBOL = "[*]\n"
private const val ARTIST_INFO_NOT_FOUND = "Artist Info not found"
private const val HEADER = "<html>"
private const val WIDTH = "<div width=400>"
private const val FONT = "<font face=\"arial\">"
private const val FOOTER = "</font></div></html>"

interface ArtistInfoHelper {
    fun getArtistInfoText(artistInfo: Card, artistName: String): String

    fun getEmptyInfoText(): String
}

internal class ArtistInfoHelperImpl : ArtistInfoHelper {

    override fun getArtistInfoText(artistInfo: Card, artistName: String) =
        formatArtistInfo(artistInfo, artistName)

    private fun formatArtistInfo(artistInfo: Card, artistName: String): String {
        return ("${if (artistInfo.isLocallyStored) INFO_IN_DATABASE_SYMBOL else ""}${
            articleToHTML(
                artistInfo,
                artistName
            )
        }")
    }

    private fun articleToHTML(artistInfo: Card, artistName: String): String {
        val builder = StringBuilder()
        builder.append(HEADER)
        builder.append(WIDTH)
        builder.append(FONT)
        val textWithBold = artistInfo.description
            .replace("'", " ")
            .replace("\n", "<br>")
            .replace("(?i)" + artistName.toRegex(), "<b>" + artistName.uppercase() + "</b>")
        builder.append(textWithBold)
        builder.append(FOOTER)
        return builder.toString()
    }

    override fun getEmptyInfoText() = ARTIST_INFO_NOT_FOUND
}
package ayds.newyork.songinfo.moredetails.model.repository.external.nyt.article

import ayds.newyork.songinfo.moredetails.EMPTY_ABSTRACT
import ayds.newyork.songinfo.moredetails.model.entities.ArtistInfo
import ayds.newyork.songinfo.moredetails.model.entities.NytArtistInfo
import ayds.newyork.songinfo.moredetails.model.repository.local.nyt.sqldb.ARTIST_COLUMN
import ayds.newyork.songinfo.moredetails.model.repository.local.nyt.sqldb.ID_COLUMN
import ayds.newyork.songinfo.moredetails.model.repository.local.nyt.sqldb.INFO_COLUMN
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import java.lang.StringBuilder

interface NytToArtistInfoResolver {
    fun getArtistInfoFromExternalData(serviceData: String?): ArtistInfo?
}

private const val ID = "id"
private const val URL = "url"

private const val EMPTY_ABSTRACT = "No Results"
private const val SECTION_DOCS = "docs"
private const val SECTION_ABSTRACT = "abstract"
private const val SECTION_WEB_URL = "web_url"
private const val SECTION_RESPONSE = "response"

internal class JsonToArtistInfoResolver() : NytToArtistInfoResolver {

    override fun getArtistInfoFromExternalData(serviceData: String?): ArtistInfo? =
        try {
            serviceData?.getFirstItem()?.let { item ->
                NytArtistInfo(
                    item.getId(),
                    item.getArtistName(),
                    item.getArtistInfo(),
                )
            }
        } catch (e: Exception) {
            null
        }


    private fun String?.getFirstItem(): JsonObject {
        val jobj = Gson().fromJson(this, JsonObject::class.java)
        val docs = jobj[SECTION_DOCS].asJsonObject
        return docs
    }

    private fun getArtistInfoFromAbstract(abstractNYT: JsonElement?) =
        abstractNYT?.let { abstractToString(abstractNYT) } ?: EMPTY_ABSTRACT

    private fun abstractToString(abstractNYT: JsonElement?): String {
        var artistInfoFromService = ""
        abstractNYT?.let { artistInfoFromService = it.asString.replace("\\n", "\n") }

        return artistNameToHtml(artistInfoFromService)
    }

    private fun artistNameToHtml(nytInfo: String): String {
        val builder = StringBuilder()
        builder.append("<html><div width=400>")
        builder.append("<font face=\"arial\">")
        val textWithBold = nytInfo
            .replace("'", " ")
            .replace("\n", "<br>")
            .replace("(?i)" + artistName.toRegex(), "<b>" + artistName.uppercase() + "</b>")
        builder.append(textWithBold)
        builder.append("</font></div></html>")
        return builder.toString()
    }

    private fun JsonObject.getId() = this[ID].asString

    private fun JsonObject.getSongName() = this[NAME].asString

    private fun JsonObject.getArtistName(): String {
        val artist = this[ARTISTS].asJsonArray[0].asJsonObject
        return artist[NAME].asString
    }

    private fun JsonObject.getSpotifyUrl(): String {
        val externalUrl = this[EXTERNAL_URL].asJsonObject
        return externalUrl[SPOTIFY].asString
    }


    private fun getArtistInfoFromAbstract(abstractNYT: JsonElement?) =
        abstractNYT?.let { abstractToString(abstractNYT) } ?: EMPTY_ABSTRACT
}
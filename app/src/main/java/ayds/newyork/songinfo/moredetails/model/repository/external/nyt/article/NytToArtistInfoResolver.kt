package ayds.newyork.songinfo.moredetails.model.repository.external.nyt.article

import ayds.newyork.songinfo.moredetails.model.entities.NytArtistInfo
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import java.lang.StringBuilder

interface NytToArtistInfoResolver {
    fun getArtistInfoFromExternalData(serviceData: String?, artistName:String): NytArtistInfo?
}

private const val SECTION_DOCS = "docs"
private const val SECTION_ABSTRACT = "abstract"
private const val SECTION_WEB_URL = "web_url"
private const val RESPONSE = "response"

internal class JsonToArtistInfoResolver : NytToArtistInfoResolver {

    private lateinit var artistName : String

    override fun getArtistInfoFromExternalData(serviceData: String?, artistName:String): NytArtistInfo? {
        this.artistName = artistName
        val result =
        try {
            serviceData?.getFirstItem()?.let { item ->
                NytArtistInfo(
                    artistName,
                    item.getArtistInfo(),
                    item.getArtistUrl()
                )
            }
        } catch (e: Exception) {
            null
        }
        return result
    }



    private fun String?.getFirstItem(): JsonElement {
        val jobj = Gson().fromJson(this, JsonObject::class.java)
        val response = jobj[RESPONSE].asJsonObject
        return response[SECTION_DOCS].asJsonArray[0]
    }

    private fun JsonElement.getArtistInfo() = abstractToString(this.asJsonObject[SECTION_ABSTRACT])

    private fun JsonElement.getArtistUrl() = this.asJsonObject[SECTION_WEB_URL].asString

    private fun abstractToString(abstractNYT: JsonElement?): String {
        var artistInfoFromService = ""
        abstractNYT?.let { artistInfoFromService = it.asString.replace("\\n", "\n") }

        return articleToHTML(artistInfoFromService)
    }

    private fun articleToHTML(nytInfo: String): String {
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
}
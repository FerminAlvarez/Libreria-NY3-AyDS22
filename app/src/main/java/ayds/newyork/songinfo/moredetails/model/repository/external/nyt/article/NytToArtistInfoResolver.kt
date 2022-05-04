package ayds.newyork.songinfo.moredetails.model.repository.external.nyt.article

import ayds.newyork.songinfo.moredetails.model.entities.NytArtistInfo
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import java.lang.StringBuilder

interface NytToArtistInfoResolver {
    fun getArtistInfoFromExternalData(serviceData: String?): NytArtistInfo?
}

private const val ID = "id"
private const val URL = "url"

private const val EMPTY_ABSTRACT = "No Results"
private const val SECTION_DOCS = "docs"
private const val SECTION_ABSTRACT = "abstract"
private const val SECTION_WEB_URL = "web_url"
private const val SECTION_RESPONSE = "response"

internal class JsonToArtistInfoResolver() : NytToArtistInfoResolver {
//TODO: si no entendi mal, el json nos devuelve en Abstract la Artist.info, el name lo tendriamos q pasar por parametro
    override fun getArtistInfoFromExternalData(serviceData: String?): NytArtistInfo? =
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


    private fun String?.getFirstItem(): JsonElement {
        val jobj = Gson().fromJson(this, JsonObject::class.java)
        return jobj[SECTION_DOCS].asJsonArray[0]
    }

    private fun JsonElement.getArtistInfo() = abstractToString(this.asJsonObject[SECTION_ABSTRACT])

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
            //.replace("(?i)" + artistName.toRegex(), "<b>" + artistName.uppercase() + "</b>")
        builder.append(textWithBold)
        builder.append("</font></div></html>")
        return builder.toString()
    }

    //falta integrar esta funcion
    private fun getArtistInfoFromAbstract(abstractNYT: JsonElement?) =
        abstractNYT?.let { abstractToString(abstractNYT) } ?: EMPTY_ABSTRACT
}
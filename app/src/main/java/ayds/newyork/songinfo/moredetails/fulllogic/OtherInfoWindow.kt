package ayds.newyork.songinfo.moredetails.fulllogic

import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import android.os.Bundle
import ayds.newyork.songinfo.R
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import com.google.gson.Gson
import com.google.gson.JsonObject
import android.content.Intent
import android.net.Uri
import com.squareup.picasso.Picasso
import android.view.View
import android.widget.ImageView
import androidx.core.text.HtmlCompat
import com.google.gson.JsonElement
import java.lang.StringBuilder

class OtherInfoWindow : AppCompatActivity() {
    private val logoNYT =
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRVioI832nuYIXqzySD8cOXRZEcdlAj3KfxA62UEC4FhrHVe0f7oZXp3_mSFG7nIcUKhg&usqp=CAU"
    private val EMPTY_ABSTRACT = "No Results"
    private val NYT_API_URL = "https://api.nytimes.com/svc/search/v2/"
    private val SECTION_DOCS = "docs"
    private val SECTION_ABSTRACT = "abstract"
    private val SECTION_WEB_URL = "web_url"
    private val SECTION_RESPONSE = "response"
    private lateinit var ARTIST_NAME : String
    private lateinit var dataBase: DataBase
    private lateinit var apiResponse: JsonObject
    private var artistInfoWasInDataBase: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ARTIST_NAME = intent.getStringExtra(ARTIST_NAME_EXTRA)!!
        dataBase = openDataBase()
        prepareOtherInfoView()
    }

    private fun prepareOtherInfoView() {
        setContentView(R.layout.activity_other_info)
        Thread{
            val artistInfo = getArtistInfo()
            saveInDataBase(artistInfo)
            updateArtistInfoView(artistInfo)
        }.start()
    }

    private fun saveInDataBase(artistInfo: String) {
        if (!artistInfoWasInDataBase)
            dataBase.saveArtist(ARTIST_NAME, artistInfo)
    }

    private fun getArtistInfo(): String {
        val infoDataBase = dataBase.getInfo(ARTIST_NAME)
        infoDataBase?.let { artistInfoWasInDataBase = true }
        return addAlreadyInDataBaseSymbol(infoDataBase) ?: getArtistInfoFromService()
    }

    private fun openDataBase() = DataBase(this)

    private fun updateArtistInfoView(artistInfo: String) {
        updateURLButton()
        runOnUiThread {
            showLogo()
            showArtistInfo(artistInfo)
        }
    }

    private fun updateURLButton() {
        if(!artistInfoWasInDataBase) {
            val nytURL = getURLFromService(apiResponse)
            nytURL.let { createURLButtonListener(nytURL) }
        }
    }

    private fun addAlreadyInDataBaseSymbol(infoDataBase: String?): String? =
        if (artistInfoWasInDataBase) "[*]$infoDataBase" else null

    private fun getArtistInfoFromService(): String {
        apiResponse = createArtistInfoJsonObject()
        val abstractNYT = apiResponse[SECTION_DOCS].asJsonArray[0].asJsonObject[SECTION_ABSTRACT]
        return getArtistInfoFromAbstract(abstractNYT)
    }

    private fun getURLFromService(response: JsonObject) = response[SECTION_DOCS].asJsonArray[0].asJsonObject[SECTION_WEB_URL]

    private fun createRetrofit(): NYTimesAPI {
        val retrofit = Retrofit.Builder()
            .baseUrl(NYT_API_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
        return retrofit.create(NYTimesAPI::class.java)
    }

    private fun createArtistInfoJsonObject(): JsonObject {
        val nytimesAPI = createRetrofit()
        val callResponse = nytimesAPI.getArtistInfo(ARTIST_NAME).execute()
        val gson = Gson()
        val jobj = gson.fromJson(callResponse.body(), JsonObject::class.java)
        return jobj[SECTION_RESPONSE].asJsonObject
    }

    private fun getArtistInfoFromAbstract(abstractNYT: JsonElement?) =
        checkEmptyAbstract(abstractNYT) ?: abstractToString(abstractNYT)

    private fun checkEmptyAbstract(abstractNYT: JsonElement?) = if (abstractNYT == null) EMPTY_ABSTRACT else null

    private fun abstractToString(abstractNYT: JsonElement?): String {
        val artistInfoFromService = abstractNYT!!.asString.replace("\\n", "\n")
        val artistInfoResult = artistNameToHtml(artistInfoFromService)
        return artistInfoResult
    }

    private fun artistNameToHtml(NYTinfo: String): String {
        val builder = StringBuilder()
        builder.append("<html><div width=400>")
        builder.append("<font face=\"arial\">")
        val textWithBold = NYTinfo
            .replace("'", " ")
            .replace("\n", "<br>")
            .replace("(?i)" + ARTIST_NAME.toRegex(), "<b>" + ARTIST_NAME.uppercase() + "</b>")
        builder.append(textWithBold)
        builder.append("</font></div></html>")
        return builder.toString()
    }

    private fun createURLButtonListener(urlNYT: JsonElement) {
        val urlString = urlNYT.asString
        findViewById<View>(R.id.openUrlButton).setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(urlString)
            this.startActivity(intent)
        }
    }

    private fun showArtistInfo(artistInfo: String) {
        val nytInfoPane : TextView = findViewById(R.id.NYTInfoPane)
        nytInfoPane.text =
            HtmlCompat.fromHtml(artistInfo, HtmlCompat.FROM_HTML_MODE_LEGACY)
    }

    private fun showLogo(){
        Picasso.get().load(logoNYT).into(findViewById<View>(R.id.imageView) as ImageView)
    }

    companion object {
        const val ARTIST_NAME_EXTRA = "artistName"
    }
}
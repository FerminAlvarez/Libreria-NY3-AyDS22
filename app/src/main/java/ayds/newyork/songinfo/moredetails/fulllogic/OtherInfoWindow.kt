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
    private lateinit var nytInfoPane: TextView
    private lateinit var dataBase: DataBase
    private lateinit var nytimesAPI: NYTimesAPI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_info)

        nytInfoPane = findViewById(R.id.NYTInfoPane)

        openDataBase()
        //TODO checkear null
        prepareArtistInfoView(intent.getStringExtra("artistName")!!)
    }

    private fun openDataBase() {
        dataBase = DataBase(this)
    }

    private fun prepareArtistInfoView(artistName: String) {
        Thread {
            val infoDB = DataBase.getInfo(dataBase, artistName)

            val artistInfo : String = recoverArtistInfoFromDB(infoDB) ?: recoverArtistInfoFromService(artistName)

            runOnUiThread {
                Picasso.get().load(logoNYT).into(findViewById<View>(R.id.imageView) as ImageView)
                nytInfoPane.text =
                    HtmlCompat.fromHtml(artistInfo, HtmlCompat.FROM_HTML_MODE_LEGACY)
            }
        }.start()
    }

    private fun recoverArtistInfoFromService(
        artistName: String
    ): String {
        val artistInfoResult : String
        nytimesAPI = createRetrofit()
        val response = createArtistInfoJsonObject(artistName)
        val abstractNYT = response["docs"].asJsonArray[0].asJsonObject["abstract"]
        artistInfoResult = getArtistInfo(abstractNYT, artistName)
        abstractNYT.let { DataBase.saveArtist(dataBase, artistName, artistInfoResult) }
        val urlNYT = response["docs"].asJsonArray[0].asJsonObject["web_url"]
        createURLButtonListener(urlNYT)
        return artistInfoResult
    }

    private fun recoverArtistInfoFromDB(infoDB: String?): String? {
        return if (infoDB != null) "[*]$infoDB" else null
    }

    private fun getArtistInfo(
        abstractNYT: JsonElement?,
        artistName: String
    ): String {
        var artistInfoResult : String
        if (abstractNYT == null) {
            artistInfoResult = "No Results"
        } else {
            artistInfoResult = abstractNYT.asString.replace("\\n", "\n")
            artistInfoResult = artistNameToHtml(artistInfoResult, artistName)
        }
        return artistInfoResult
    }

    private fun createRetrofit(): NYTimesAPI {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.nytimes.com/svc/search/v2/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
        return retrofit.create(nytimesAPI::class.java)
    }

    private fun createURLButtonListener(urlNYT: JsonElement) {
        val urlString = urlNYT.asString
        findViewById<View>(R.id.openUrlButton).setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(urlString)
            this.startActivity(intent)
        }
    }

    private fun createArtistInfoJsonObject(
        artistName: String
    ): JsonObject {
        val callResponse = nytimesAPI.getArtistInfo(artistName).execute()
        val gson = Gson()
        val jobj = gson.fromJson(callResponse.body(), JsonObject::class.java)
        return jobj["response"].asJsonObject
    }

    private fun artistNameToHtml(NYTinfo: String, artistName: String): String {
        val builder = StringBuilder()
        builder.append("<html><div width=400>")
        builder.append("<font face=\"arial\">")
        val textWithBold = NYTinfo
            .replace("'", " ")
            .replace("\n", "<br>")
            .replace("(?i)" + artistName.toRegex(), "<b>" + artistName.uppercase() + "</b>")
        builder.append(textWithBold)
        builder.append("</font></div></html>")
        return builder.toString()
    }

    companion object {
        const val ARTIST_NAME_EXTRA = "artistName"
    }
}
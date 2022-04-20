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
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.core.text.HtmlCompat
import retrofit2.Response
import java.io.IOException
import java.lang.StringBuilder

class OtherInfoWindow : AppCompatActivity() {
    private val logoNYT =
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRVioI832nuYIXqzySD8cOXRZEcdlAj3KfxA62UEC4FhrHVe0f7oZXp3_mSFG7nIcUKhg&usqp=CAU"
    private var NYTInfoPane: TextView? = null
    private var dataBase: DataBase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_info)

        NYTInfoPane = findViewById(R.id.NYTInfoPane)
        open(intent.getStringExtra("artistName"))
    }

    private fun open(artist: String?) {
        dataBase = DataBase(this)
        DataBase.saveArtist(dataBase!!, "test", "sarasa")
        Log.e("TAG", "" + DataBase.getInfo(dataBase!!, "test"))
        Log.e("TAG", "" + DataBase.getInfo(dataBase!!, "nada"))
        getArtistInfo(artist!!)
    }

    fun getArtistInfo(artistName: String) {

        // create
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.nytimes.com/svc/search/v2/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
        val NYTimesAPI = retrofit.create(NYTimesAPI::class.java)

        //TODO ¿Otro log?
        Log.e("TAG", "artistName $artistName")

        //TODO colocar un nombre representativo, sacarle los argumentos posibles
        CreateThreadCAMBIARNOMBRE(artistName, NYTimesAPI)
    }

    private fun CreateThreadCAMBIARNOMBRE(artistName: String, NYTimesAPI: NYTimesAPI) {
        Thread {
            val infoDB = DataBase.getInfo(dataBase!!, artistName)
            var NYTinfo = ""
            //TODO sacar comentarios, cambiar el != null por algo de null operators
            if (infoDB != null) { // exists in db
                NYTinfo = "[*]$infoDB"
            } else { // get from service
                val callResponse: Response<String>
                try {
                    callResponse = NYTimesAPI.getArtistInfo(artistName).execute()
                    Log.e("TAG", "JSON " + callResponse.body())
                    val gson = Gson()
                    //TODO cambiar jobj por algo representativo
                    val jobj = gson.fromJson(callResponse.body(), JsonObject::class.java)
                    val response = jobj["response"].asJsonObject
                    //TODO cambiar _abstract porque incluso el nombre es una palabra reservada
                    val abstractNYT = response["docs"].asJsonArray[0].asJsonObject["abstract"]
                    val urlNYT = response["docs"].asJsonArray[0].asJsonObject["web_url"]

                    //TODO Este text tendría que ser otra variable, no tiene que ver con lo que hace la anterior
                    if (abstractNYT == null) {
                        NYTinfo = "No Results"
                    } else {
                        NYTinfo = abstractNYT.asString.replace("\\n", "\n")
                        NYTinfo = textToHtml(NYTinfo, artistName)
                        // save to DB  <o/
                        DataBase.saveArtist(dataBase!!, artistName, NYTinfo)
                    }
                    val urlString = urlNYT.asString
                    findViewById<View>(R.id.openUrlButton).setOnClickListener {
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.data = Uri.parse(urlString)
                        this.startActivity(intent)
                    }
                } catch (e1: IOException) {
                    //TODO más logs
                    Log.e("TAG", "Error $e1")
                    e1.printStackTrace()
                }
            }

            //TODO Hay un log
            Log.e("TAG", "Get Image from $logoNYT")
            val finalText = NYTinfo
            runOnUiThread {
                Picasso.get().load(logoNYT).into(findViewById<View>(R.id.imageView) as ImageView)
                NYTInfoPane!!.text = HtmlCompat.fromHtml(finalText, HtmlCompat.FROM_HTML_MODE_LEGACY)
            }
        }.start()
    }

    private fun textToHtml(NYTinfo: String, artistName: String?): String {
        val builder = StringBuilder()
        builder.append("<html><div width=400>")
        builder.append("<font face=\"arial\">")
        val textWithBold = NYTinfo
            .replace("'", " ")
            .replace("\n", "<br>")
            .replace("(?i)" + artistName!!.toRegex(), "<b>" + artistName.uppercase() + "</b>")
        builder.append(textWithBold)
        builder.append("</font></div></html>")
        return builder.toString()
    }

    companion object {
        //TODO ("Esta constante la usa la clase HomeView de forma estatica")
        const val ARTIST_NAME_EXTRA = "artistName"
    }
}
package ayds.newyork.songinfo.moredetails.fulllogic

import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import ayds.newyork.songinfo.moredetails.fulllogic.DataBase
import android.os.Bundle
import ayds.newyork.songinfo.R
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import ayds.newyork.songinfo.moredetails.fulllogic.NYTimesAPI
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonElement
import ayds.newyork.songinfo.moredetails.fulllogic.OtherInfoWindow
import android.content.Intent
import android.net.Uri
import com.squareup.picasso.Picasso
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.ImageView
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

        //TODO'ed alto nombre textPane2 -> NYTInfoPane, tambien lo cambiamos en la activity.otherinfo.xml
        NYTInfoPane = findViewById(R.id.NYTInfoPane)
        open(intent.getStringExtra("artistName"))
    }

    //TODO el método hace muchas cosas -> hay que dividirlo
    fun getArtistInfo(artistName: String?) {

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

    private fun CreateThreadCAMBIARNOMBRE(artistName: String?, NYTimesAPI: NYTimesAPI) {
        Thread { //TODO text -> el nombre del artista que encuentra en la base de datos
            var infoDB = DataBase.getInfo(dataBase, artistName)
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
                    //_abstract es la información de la página, la que va debajo de la foto
                    val _abstract = response["docs"].asJsonArray[0].asJsonObject["abstract"]
                    val url = response["docs"].asJsonArray[0].asJsonObject["web_url"]

                    //TODO Este text tendría que ser otra variable, no tiene que ver con lo que hace la anterior
                    //Este text es la información que devuelve NYT (debajo de la imagen)
                    if (_abstract == null) {
                        NYTinfo = "No Results"
                    } else {
                        NYTinfo = _abstract.asString.replace("\\n", "\n")
                        NYTinfo = textToHtml(NYTinfo, artistName)


                        // save to DB  <o/
                        DataBase.saveArtist(dataBase, artistName, NYTinfo)
                    }
                    val urlString = url.asString
                    findViewById<View>(R.id.openUrlButton).setOnClickListener {
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.data = Uri.parse(urlString)
                        startActivity(intent)
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
                NYTInfoPane!!.text = Html.fromHtml(finalText)
            }
        }.start()
    }

    private fun open(artist: String?) {
        dataBase = DataBase(this)
        DataBase.saveArtist(dataBase, "test", "sarasa")
        Log.e("TAG", "" + DataBase.getInfo(dataBase, "test"))
        Log.e("TAG", "" + DataBase.getInfo(dataBase, "nada"))
        getArtistInfo(artist)
    }

    companion object {
        const val ARTIST_NAME_EXTRA = "artistName"
        fun textToHtml(text: String, term: String?): String {
            val builder = StringBuilder()
            builder.append("<html><div width=400>")
            builder.append("<font face=\"arial\">")
            val textWithBold = text
                .replace("'", " ")
                .replace("\n", "<br>")
                .replace("(?i)" + term!!.toRegex(), "<b>" + term.toUpperCase() + "</b>")
            builder.append(textWithBold)
            builder.append("</font></div></html>")
            return builder.toString()
        }
    }
}
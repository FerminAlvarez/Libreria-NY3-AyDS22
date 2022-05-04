package ayds.newyork.songinfo.moredetails.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import ayds.newyork.songinfo.R
import ayds.newyork.songinfo.home.model.HomeModel
import ayds.newyork.songinfo.home.model.HomeModelInjector
import ayds.newyork.songinfo.home.view.HomeViewInjector
import ayds.newyork.songinfo.moredetails.*
import ayds.newyork.songinfo.moredetails.model.MoreDetailsModel
import ayds.newyork.songinfo.moredetails.model.entities.ArtistInfo
import ayds.newyork.songinfo.moredetails.model.entities.EmptyArtistInfo
import ayds.newyork.songinfo.moredetails.model.entities.NytArtistInfo
import ayds.newyork.songinfo.moredetails.model.repository.external.nyt.article.NYTimesAPI
import ayds.newyork.songinfo.utils.UtilsInjector
import ayds.newyork.songinfo.utils.navigation.NavigationUtils
import ayds.observer.Observable
import ayds.observer.Subject
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.squareup.picasso.Picasso
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.lang.StringBuilder

interface MoreDetailsView {

    val uiEventObservable: Observable<MoreDetailsUiEvent>
    val uiState: MoreDetailsUiState

    fun openExternalLink(url: String)
}

private const val logoNYT =
    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRVioI832nuYIXqzySD8cOXRZEcdlAj3KfxA62UEC4FhrHVe0f7oZXp3_mSFG7nIcUKhg&usqp=CAU"
private const val INFO_IN_DATABASE_SYMBOL = "[*]"
private const val SECTION_RESPONSE = "response"
private const val SECTION_DOCS = "docs"
private const val EMPTY_ABSTRACT = "No Results"
private const val SECTION_ABSTRACT = "abstract"
private const val SECTION_WEB_URL = "web_url"
private const val NYT_API_URL = "https://api.nytimes.com/svc/search/v2/"

class MoreDetailsViewActivity : AppCompatActivity(), MoreDetailsView {

    private val onActionSubject = Subject<MoreDetailsUiEvent>()
    private lateinit var moreDetailsModel: MoreDetailsModel
    private lateinit var logoImageView: ImageView
    private lateinit var nytInfoPane: TextView
    private lateinit var openArticleButton: Button
    private lateinit var homeModel: HomeModel
    private lateinit var artistName: String
    private lateinit var dataBase: DataBase
    private lateinit var urlButton: Button
    private lateinit var apiResponse: JsonObject
    private lateinit var nytimesAPI: NYTimesAPI
    private val navigationUtils: NavigationUtils = UtilsInjector.navigationUtils

    override val uiEventObservable: Observable<MoreDetailsUiEvent> = onActionSubject
    override var uiState: MoreDetailsUiState = MoreDetailsUiState()


    override fun openExternalLink(url: String) {
        navigationUtils.openExternalUrl(this, url)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_info)

        initModule()
        initProperties()
        prepareOtherInfoView()
        initListeners()
        initObservers()
    }

    private fun initModule() {
        HomeViewInjector.init(this)
        homeModel = HomeModelInjector.getHomeModel()
    }

    private fun initListeners() {
        val urlString = urlNYT.asString
        openArticleButton.setOnClickListener {
            notifyOpenArticleUrlAction()
        }
    }

    private fun notifyOpenArticleUrlAction() {
        onActionSubject.notify(MoreDetailsUiEvent.OpenArticleUrl)
    }


    private fun initObservers() {
        moreDetailsModel.songObservable
            .subscribe { value -> this.updateArtistInfo(value) }
    }

    private fun updateArtistInfo(artistInfo: ArtistInfo) {
        updateUiState(artistInfo)
        updateArtistInfo()
    }

    private fun updateUiState(artistInfo: ArtistInfo) {
        when (artistInfo) {
            is NytArtistInfo -> updateArtistInfoUiState(artistInfo)
            EmptyArtistInfo -> updateNoResultsUiState()
        }
    }

    private fun updateArtistInfoUiState(artistInfo: ArtistInfo) {
        uiState = uiState.copy(
            articleUrl = artistInfo.artistName,
            artistInfo = artistInfo.artistInfo
        )
    }

    private fun updateNoResultsUiState() {
        uiState = uiState.copy(
            articleUrl = "",
            artistInfo = ""
        )
    }

    private fun updateArtistInfo() {
        runOnUiThread {
            nytInfoPane.text = uiState.artistInfo
        }
    }

    private fun initProperties() {
        initStateProperty()
        initDependencyProperties()
        initViewsProperties()
    }

    private fun initStateProperty() {
        artistName = intent.getStringExtra(OtherInfoWindow.ARTIST_NAME_EXTRA) ?: ""
    }

    private fun initDependencyProperties() {
        dataBase = openDataBase()
        nytimesAPI = createRetrofit()
    }

    private fun openDataBase() = DataBase(this)

    private fun createRetrofit(): NYTimesAPI {
        val retrofit = Retrofit.Builder()
            .baseUrl(NYT_API_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
        return retrofit.create(NYTimesAPI::class.java)
    }

    private fun initViewsProperties() {
        urlButton = findViewById<(R.id.openUrlButton)
        logoImageView = findViewById(R.id.imageView)
        nytInfoPane = findViewById(R.id.nytInfoPane)
    }

    private fun prepareOtherInfoView() {
        Thread {
            runPrepareOtherInfoView()
        }.start()
    }

    private fun runPrepareOtherInfoView() {
        val artistInfo = getArtistInfo()
        updateArtistInfoView(artistInfo)
    }

    private fun getArtistInfo(): String {
        var artistInfo = dataBase.getInfo(artistName)

        when {
            artistInfo != null -> artistInfo = addAlreadyInDataBaseSymbol(artistInfo)
            else -> {
                artistInfo = getArtistInfoFromService()
                saveInDataBase(artistInfo)
            }
        }
        return artistInfo
    }

    private fun addAlreadyInDataBaseSymbol(infoDataBase: String): String =
        "$INFO_IN_DATABASE_SYMBOL$infoDataBase"

    private fun getArtistInfoFromService(): String {
        apiResponse = createArtistInfoJsonObject()
        val abstractNYT = apiResponse[SECTION_DOCS].asJsonArray[0].asJsonObject[SECTION_ABSTRACT]
        return getArtistInfoFromAbstract(abstractNYT)
    }

    private fun createArtistInfoJsonObject(): JsonObject {
        val callResponse = nytimesAPI.getArtistInfo(artistName)!!.execute()
        val gson = Gson()
        val jobj = gson.fromJson(callResponse.body(), JsonObject::class.java)
        return jobj[SECTION_RESPONSE].asJsonObject
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

    private fun saveInDataBase(artistInfo: String) {
        dataBase.saveArtist(artistName, artistInfo)
    }

    private fun updateArtistInfoView(artistInfo: String) {
        updateURLButton()
        updateLogo()
        updateArtistInfo(artistInfo)
    }

    private fun updateURLButton() {
        if (this::apiResponse.isInitialized) {
            val nytURL = getURLFromService(apiResponse)
            createURLButtonListener(nytURL)
        }
    }

    private fun getURLFromService(response: JsonObject) =
        response[SECTION_DOCS].asJsonArray[0].asJsonObject[SECTION_WEB_URL]


    private fun navigateToUrl(urlString: String?) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(urlString)
        this.startActivity(intent)
    }

    private fun updateLogo() {
        runOnUiThread {
            Picasso.get().load(logoNYT).into(logoImageView)
        }
    }

    private fun updateArtistInfo(artistInfo: String) {
        runOnUiThread {
            nytInfoPane.text =
                HtmlCompat.fromHtml(artistInfo, HtmlCompat.FROM_HTML_MODE_LEGACY)
        }
    }

}
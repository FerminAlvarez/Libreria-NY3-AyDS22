package ayds.newyork.songinfo.moredetails.view

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import ayds.newyork.songinfo.R
import ayds.newyork.songinfo.moredetails.model.MoreDetailsModel
import ayds.newyork.songinfo.moredetails.model.MoreDetailsModelInjector
import ayds.newyork.songinfo.moredetails.model.entities.ArtistInfo
import ayds.newyork.songinfo.moredetails.model.entities.EmptyArtistInfo
import ayds.newyork.songinfo.moredetails.model.entities.NytArtistInfo
import ayds.newyork.songinfo.utils.UtilsInjector
import ayds.newyork.songinfo.utils.navigation.NavigationUtils
import ayds.observer.Observable
import ayds.observer.Subject
import com.squareup.picasso.Picasso

interface MoreDetailsView {
    val uiEventObservable: Observable<MoreDetailsUiEvent>
    val uiState: MoreDetailsUiState

    fun openExternalLink(url: String)
}

class MoreDetailsViewActivity : AppCompatActivity(), MoreDetailsView {

    companion object {
        const val ARTIST_NAME_EXTRA = "artistName"
    }

    private val onActionSubject = Subject<MoreDetailsUiEvent>()
    private val artistInfoHelper: ArtistInfoHelper = MoreDetailsViewInjector.artistInfoHelper
    private lateinit var moreDetailsModel: MoreDetailsModel
    private lateinit var artistName: String
    private lateinit var logoImageView: ImageView
    private lateinit var infoPane: TextView
    private lateinit var sourcePane: TextView
    private lateinit var openArticleButton: Button
    private val navigationUtils: NavigationUtils = UtilsInjector.navigationUtils

    override val uiEventObservable: Observable<MoreDetailsUiEvent> = onActionSubject
    override var uiState: MoreDetailsUiState = MoreDetailsUiState()

    override fun openExternalLink(url: String) {
        if (url.isNotEmpty())
            navigationUtils.openExternalUrl(this, url)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_info)

        initModule()
        initStateProperty()
        initProperties()
        initListeners()
        initObservers()
        notifySearchNytInfoAction()
    }

    private fun initModule() {
        MoreDetailsViewInjector.init(this)
        moreDetailsModel = MoreDetailsModelInjector.getMoreDetailsModel()
    }

    private fun initStateProperty() {
        artistName = intent.getStringExtra(ARTIST_NAME_EXTRA) ?: ""
        uiState = uiState.copy(artistName = artistName)
    }

    private fun initProperties() {
        openArticleButton = findViewById(R.id.openUrlButton)
        logoImageView = findViewById(R.id.imageView)
        infoPane = findViewById(R.id.infoPane)
        sourcePane = findViewById(R.id.infoSourcePane)
    }

    private fun initListeners() {
        openArticleButton.setOnClickListener {
            notifyOpenArticleUrlAction()
        }
    }

    private fun notifySearchNytInfoAction() {
        onActionSubject.notify(MoreDetailsUiEvent.SearchNytInfo)
    }

    private fun notifyOpenArticleUrlAction() {
        onActionSubject.notify(MoreDetailsUiEvent.OpenArticleUrl)
    }

    private fun initObservers() {
        moreDetailsModel.artistObservable
            .subscribe { value -> this.updateArtistInfo(value) }
    }

    private fun updateArtistInfo(artistInfo: ArtistInfo) {
        updateUiState(artistInfo)
        updateSourceLogo()
        updateSourceName()
        updateArtistInfoPanel()
    }

    private fun updateUiState(artistInfo: ArtistInfo) {
        when (artistInfo) {
            is NytArtistInfo -> updateArtistInfoUiState(artistInfo)
            EmptyArtistInfo -> updateNoResultsUiState()
        }
    }

    private fun updateArtistInfoUiState(artistInfo: ArtistInfo) {
        uiState = uiState.copy(
            articleUrl = artistInfo.artistURL,
            artistInfo = artistInfoHelper.getArtistInfoText(artistInfo),
            source = artistInfo.source,
            sourceLogoUrl = artistInfo.sourceLogoUrl
        )
    }

    private fun updateNoResultsUiState() {
        uiState = uiState.copy(
            articleUrl = "",
            artistInfo = artistInfoHelper.getArtistInfoText(),
            source = "Sourceless?",
            sourceLogoUrl = MoreDetailsUiState.DEFAULT_LOGO
        )
    }

    private fun updateArtistInfoPanel() {
        runOnUiThread {
            infoPane.text =
                HtmlCompat.fromHtml(uiState.artistInfo, HtmlCompat.FROM_HTML_MODE_LEGACY)
        }
    }

    private fun updateSourceLogo() {
        runOnUiThread {
            Picasso.get().load(uiState.sourceLogoUrl).into(logoImageView)
        }
    }

    private fun updateSourceName() {
        runOnUiThread {
            val source = uiState.source
            this.sourcePane.text = "Source: $source"
        }
    }
}

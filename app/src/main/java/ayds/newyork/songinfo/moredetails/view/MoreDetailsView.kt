package ayds.newyork.songinfo.moredetails.view

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import ayds.newyork.songinfo.R
import ayds.newyork.songinfo.moredetails.model.MoreDetailsModel
import ayds.newyork.songinfo.moredetails.model.MoreDetailsModelInjector
import ayds.newyork.songinfo.moredetails.model.entities.Card
import ayds.newyork.songinfo.moredetails.model.entities.CardImpl
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
    private lateinit var logoImageView: ArrayList<ImageView>
    private lateinit var infoPane: ArrayList<TextView>
    private lateinit var sourcePane: ArrayList<TextView>
    private lateinit var openArticleButton: ArrayList<Button>
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
        notifySearchInfoAction()
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
        openArticleButton = arrayListOf(
            findViewById(R.id.openUrlButton),
            findViewById(R.id.openUrlButton2),
            findViewById(R.id.openUrlButton3),
        )
        logoImageView = arrayListOf(
            findViewById(R.id.imageView1),
            findViewById(R.id.imageView2),
            findViewById(R.id.imageView3)
        )
        infoPane = arrayListOf(
            findViewById(R.id.infoPane1),
            findViewById(R.id.infoPane2),
            findViewById(R.id.infoPane3)
        )
        sourcePane = arrayListOf(
            findViewById(R.id.infoSourcePane1),
            findViewById(R.id.infoSourcePane2),
            findViewById(R.id.infoSourcePane3)
        )
    }

    private fun initListeners() {
        for ((buttonIndex, button) in openArticleButton.withIndex()) {
            button.setOnClickListener {
                notifyOpenArticleUrlAction(buttonIndex)
            }
        }
    }

    private fun notifyOpenArticleUrlAction(buttonIndex: Int) {
        MoreDetailsUiEvent.OpenArticleUrl.uiCardIndex = buttonIndex
        onActionSubject.notify(MoreDetailsUiEvent.OpenArticleUrl)
    }

    private fun initObservers() {
        moreDetailsModel.artistObservable
            .subscribe { value -> this.updateArtistInfo(value) }
    }

    private fun updateArtistInfo(artistInfoResult: List<Card>) {
        for ((cardNumber, artistInfo) in artistInfoResult.withIndex()) {
            updateArtistInfoUiState(artistInfo)
            updateSourceLogo(cardNumber)
            updateSourceName(cardNumber)
            updateArtistInfoPanel(cardNumber)
            updateArticleButton(cardNumber)
        }
    }

    private fun updateArtistInfoUiState(artistInfo: Card) {
        uiState = uiState.copy(
            cards = uiState.cards.plus(CardImpl(
                infoURL = artistInfo.infoURL,
                description = artistInfoHelper.getArtistInfoText(
                    artistInfo,
                    artistName
                ),
                source = artistInfo.source,
                sourceLogoUrl = artistInfo.sourceLogoUrl
            ))
        )
    }

    private fun updateSourceLogo(selectedCard: Int) {
        runOnUiThread {
            Picasso.get().load(uiState.cards[selectedCard].sourceLogoUrl)
                .into(logoImageView[selectedCard])
            logoImageView[selectedCard].visibility = View.VISIBLE
        }
    }

    private fun updateSourceName(selectedCard: Int) {
        runOnUiThread {
            val source = uiState.cards[selectedCard].source.value
            val sourceText = "Source: $source"
            this.sourcePane[selectedCard].text = sourceText
            this.sourcePane[selectedCard].visibility = View.VISIBLE
        }
    }

    private fun updateArtistInfoPanel(selectedCard: Int) {
        runOnUiThread {
            infoPane[selectedCard].text =
                HtmlCompat.fromHtml(
                    uiState.cards[selectedCard].description,
                    HtmlCompat.FROM_HTML_MODE_LEGACY
                )
            infoPane[selectedCard].visibility = View.VISIBLE
        }
    }

    private fun updateArticleButton(selectedCard: Int) {
        runOnUiThread {
            this.openArticleButton[selectedCard].visibility = View.VISIBLE
        }
    }

    private fun notifySearchInfoAction() {
        onActionSubject.notify(MoreDetailsUiEvent.SearchInfo)
    }
}

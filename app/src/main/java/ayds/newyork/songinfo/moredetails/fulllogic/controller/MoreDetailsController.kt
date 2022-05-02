package ayds.newyork.songinfo.moredetails.fulllogic.controller

import ayds.newyork.songinfo.moredetails.fulllogic.view.MoreDetailsUiEvent
import ayds.newyork.songinfo.moredetails.fulllogic.view.MoreDetailsView
import ayds.observer.Observer

interface MoreDetailsController {
    fun setMoreDetailsView(moreDetailsView: MoreDetailsView)
}

internal class MoreDetailsControllerImpl(
) : MoreDetailsController {

    private lateinit var moreDetailsView: MoreDetailsView

    override fun setMoreDetailsView(moreDetailsView: MoreDetailsView) {
        this.moreDetailsView = moreDetailsView
    }

    private val observer: Observer<MoreDetailsUiEvent> =
        Observer { value ->
            if (value == MoreDetailsUiEvent.OpenArticleUrl) openArticle()
        }

    private fun openArticle() {
        moreDetailsView.openExternalLink(moreDetailsView.uiState.articleUrl)
    }
}
package ayds.newyork.songinfo.moredetails.controller

import ayds.newyork.songinfo.moredetails.model.MoreDetailsModel
import ayds.newyork.songinfo.moredetails.view.MoreDetailsUiEvent
import ayds.newyork.songinfo.moredetails.view.MoreDetailsView
import ayds.observer.Observer

interface MoreDetailsController {
    fun setMoreDetailsView(moreDetailsView: MoreDetailsView)
}

internal class MoreDetailsControllerImpl(
    private val moreDetailsModel: MoreDetailsModel
) : MoreDetailsController {

    private lateinit var moreDetailsView: MoreDetailsView

    override fun setMoreDetailsView(moreDetailsView: MoreDetailsView) {
        this.moreDetailsView = moreDetailsView
        moreDetailsView.uiEventObservable.subscribe(observer)
    }

    private val observer: Observer<MoreDetailsUiEvent> =
        Observer { value ->
            when (value) {
                is MoreDetailsUiEvent.SearchNytInfo -> searchNytInfo()
                is MoreDetailsUiEvent.OpenArticleUrl -> openArticle()
            }
        }

    private fun searchNytInfo() {
        Thread {
            moreDetailsModel.getInfoByArtistName(moreDetailsView.uiState.artistName)
        }.start()
    }

    private fun openArticle() {
        Thread {
            moreDetailsView.openExternalLink(moreDetailsView.uiState.articleUrl)
        }.start()
    }
}
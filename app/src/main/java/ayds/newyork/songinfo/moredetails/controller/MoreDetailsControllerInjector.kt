package ayds.newyork.songinfo.moredetails.controller

import ayds.newyork.songinfo.moredetails.view.MoreDetailsView

object MoreDetailsControllerInjector {

    fun onViewStarted(moreDetailsView: MoreDetailsView) {
        MoreDetailsControllerImpl(MoreDetailsModelInjector.getMoreDetailsModel()).apply {
            setMoreDetailsView(moreDetailsView)
        }
    }

}
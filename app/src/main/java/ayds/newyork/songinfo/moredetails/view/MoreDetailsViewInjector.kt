package ayds.newyork.songinfo.moredetails.view

import ayds.newyork.songinfo.moredetails.controller.MoreDetailsControllerInjector

object MoreDetailsViewInjector {

    val artistInfoHelper: ArtistInfoHelper = ArtistInfoHelperImpl()

    fun init(moreDetailsView: MoreDetailsView) {
        MoreDetailsModelInjector.initMoreDetailsModel(moreDetailsView)
        MoreDetailsControllerInjector.onViewStarted(moreDetailsView)
    }

}
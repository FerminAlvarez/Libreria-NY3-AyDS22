package ayds.newyork.songinfo.home.controller

import ayds.newyork.songinfo.home.model.HomeModelInjector
import ayds.newyork.songinfo.home.view.HomeView

object HomeControllerInjector {

    fun onViewStarted(homeView: HomeView) {
        HomeControllerImpl(HomeModelInjector.getHomeModel()).apply {
            setHomeView(homeView)
        }
    }
}
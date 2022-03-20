package ayds.newyork.songinfo.home.model

import android.content.Context
import ayds.newyork.songinfo.home.model.repository.SongRepository
import ayds.newyork.songinfo.home.model.repository.SongRepositoryImpl
import ayds.newyork.songinfo.home.model.repository.external.spotify.SpotifyInjector
import ayds.newyork.songinfo.home.model.repository.external.spotify.SpotifyTrackService
import ayds.newyork.songinfo.home.model.repository.local.spotify.SpotifyLocalStorage
import ayds.newyork.songinfo.home.model.repository.local.spotify.sqldb.CursorToSpotifySongMapperImpl
import ayds.newyork.songinfo.home.model.repository.local.spotify.sqldb.SpotifyLocalStorageImpl
import ayds.newyork.songinfo.home.view.HomeView

object HomeModelInjector {

    private lateinit var homeModel: HomeModel

    fun getHomeModel(): HomeModel = homeModel

    fun initHomeModel(homeView: HomeView) {
        val spotifyLocalStorage: SpotifyLocalStorage = SpotifyLocalStorageImpl(
          homeView as Context, CursorToSpotifySongMapperImpl()
        )
        val spotifyTrackService: SpotifyTrackService = SpotifyInjector.spotifyTrackService

        val repository: SongRepository =
            SongRepositoryImpl(spotifyLocalStorage, spotifyTrackService)

        homeModel = HomeModelImpl(repository)
    }
}
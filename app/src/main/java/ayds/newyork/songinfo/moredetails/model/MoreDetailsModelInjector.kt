package ayds.newyork.songinfo.moredetails.model

import android.content.Context
import ayds.newyork.songinfo.moredetails.model.repository.ArtistInfoRepository
import ayds.newyork.songinfo.moredetails.model.repository.ArtistInfoRepositoryImpl
import ayds.ny3.newyorktimes.NytArticleService
import ayds.newyork.songinfo.moredetails.model.repository.local.nyt.NytLocalStorage
import ayds.newyork.songinfo.moredetails.model.repository.local.nyt.sqldb.CursorToNytArtistArticleMapperImpl
import ayds.newyork.songinfo.moredetails.model.repository.local.nyt.sqldb.NytLocalStorageImpl
import ayds.newyork.songinfo.moredetails.view.MoreDetailsView

object MoreDetailsModelInjector {

    private lateinit var moreDetailsModel: MoreDetailsModel

    fun getMoreDetailsModel(): MoreDetailsModel = moreDetailsModel

    fun initMoreDetailsModel(moreDetailsView: MoreDetailsView) {
        val nytLocalStorage: NytLocalStorage = NytLocalStorageImpl(
            moreDetailsView as Context, CursorToNytArtistArticleMapperImpl()
        )
        val nytArticleService: NytArticleService = ayds.ny3.newyorktimes.NytInjector.nytArticleService

        val repository: ArtistInfoRepository =
            ArtistInfoRepositoryImpl(nytLocalStorage, nytArticleService)

        moreDetailsModel = MoreDetailsModelImpl(repository)
    }
}
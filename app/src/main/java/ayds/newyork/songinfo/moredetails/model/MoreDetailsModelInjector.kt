package ayds.newyork.songinfo.moredetails.model

import android.content.Context
import ayds.newyork.songinfo.moredetails.model.repository.ArtistInfoRepository
import ayds.newyork.songinfo.moredetails.model.repository.ArtistInfoRepositoryImpl
import ayds.newyork.songinfo.moredetails.model.repository.broker.InfoBroker
import ayds.newyork.songinfo.moredetails.model.repository.broker.InfoBrokerImpl
import ayds.newyork.songinfo.moredetails.model.repository.broker.proxy.newyorktimes.NytProxy
import ayds.newyork.songinfo.moredetails.model.repository.broker.proxy.newyorktimes.NytProxyImpl
import ayds.newyork.songinfo.moredetails.model.repository.local.card.LocalStorage
import ayds.newyork.songinfo.moredetails.model.repository.local.card.sqldb.CursorToArtistArticleMapperImpl
import ayds.newyork.songinfo.moredetails.model.repository.local.card.sqldb.LocalStorageImpl
import ayds.newyork.songinfo.moredetails.view.MoreDetailsView
import ayds.ny3.newyorktimes.NytArticleService

object MoreDetailsModelInjector {

    private lateinit var moreDetailsModel: MoreDetailsModel

    fun getMoreDetailsModel(): MoreDetailsModel = moreDetailsModel

    fun initMoreDetailsModel(moreDetailsView: MoreDetailsView) {
        val localStorage: LocalStorage = LocalStorageImpl(
            moreDetailsView as Context, CursorToArtistArticleMapperImpl()
        )
        val nytArticleService: NytArticleService =
            ayds.ny3.newyorktimes.NytInjector.nytArticleService

        val nytProxy: NytProxy = NytProxyImpl(nytArticleService)

        val infoBroker: InfoBroker = InfoBrokerImpl(nytProxy)

        val repository: ArtistInfoRepository =
            ArtistInfoRepositoryImpl(localStorage, infoBroker)

        moreDetailsModel = MoreDetailsModelImpl(repository)
    }
}
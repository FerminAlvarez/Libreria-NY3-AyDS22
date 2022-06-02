package ayds.newyork.songinfo.moredetails.model

import ayds.newyork.songinfo.moredetails.model.repository.broker.proxy.lastfm.LastFMProxyImpl
import ayds.newyork.songinfo.moredetails.model.repository.broker.proxy.wikipedia.WikipediaProxyImpl
import android.content.Context
import ayds.lisboa.lastfmdata.lastfm.LastFMInjector
import ayds.lisboa.lastfmdata.lastfm.LastFMService
import ayds.newyork.songinfo.moredetails.model.repository.ArtistInfoRepository
import ayds.newyork.songinfo.moredetails.model.repository.ArtistInfoRepositoryImpl
import ayds.newyork.songinfo.moredetails.model.repository.broker.InfoBroker
import ayds.newyork.songinfo.moredetails.model.repository.broker.InfoBrokerImpl
import ayds.newyork.songinfo.moredetails.model.repository.broker.proxy.ServiceProxy
import ayds.newyork.songinfo.moredetails.model.repository.broker.proxy.newyorktimes.NytProxyImpl
import ayds.newyork.songinfo.moredetails.model.repository.local.card.LocalStorage
import ayds.newyork.songinfo.moredetails.model.repository.local.card.sqldb.CursorToArtistArticleMapperImpl
import ayds.newyork.songinfo.moredetails.model.repository.local.card.sqldb.LocalStorageImpl
import ayds.newyork.songinfo.moredetails.view.MoreDetailsView
import ayds.ny3.newyorktimes.NytArticleService
import ayds.ny3.newyorktimes.NytInjector
import ayds.winchester1.wikipedia.WikipediaCardService
import ayds.winchester1.wikipedia.WikipediaInjector
import java.util.*

object MoreDetailsModelInjector {

    private lateinit var moreDetailsModel: MoreDetailsModel

    fun getMoreDetailsModel(): MoreDetailsModel = moreDetailsModel

    fun initMoreDetailsModel(moreDetailsView: MoreDetailsView) {
        val localStorage: LocalStorage = LocalStorageImpl(
            moreDetailsView as Context, CursorToArtistArticleMapperImpl()
        )
        val proxyList = LinkedList<ServiceProxy>()

        val nytArticleService: NytArticleService = NytInjector.nytArticleService
        val nytProxy: ServiceProxy = NytProxyImpl(nytArticleService)

        val lastFMArticleService: LastFMService = LastFMInjector.lastFMService
        val lastFMProxy: ServiceProxy = LastFMProxyImpl(lastFMArticleService)

        val wikipediaArticleService: WikipediaCardService = WikipediaInjector.wikipediaCardService
        val wikipediaProxy: ServiceProxy = WikipediaProxyImpl(wikipediaArticleService)

        proxyList.add(nytProxy)
        proxyList.add(lastFMProxy)
        proxyList.add(wikipediaProxy)

        val infoBroker: InfoBroker = InfoBrokerImpl(proxyList)

        val repository: ArtistInfoRepository =
            ArtistInfoRepositoryImpl(localStorage, infoBroker)

        moreDetailsModel = MoreDetailsModelImpl(repository)
    }
}
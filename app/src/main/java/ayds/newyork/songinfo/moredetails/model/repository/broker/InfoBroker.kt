package ayds.newyork.songinfo.moredetails.model.repository.broker

import ayds.newyork.songinfo.moredetails.model.entities.Card
import ayds.newyork.songinfo.moredetails.model.repository.broker.proxy.ServiceProxy

interface InfoBroker {
    fun getInfoByArtistName(artist: String): List<Card>
}

internal class InfoBrokerImpl(
    private val proxyList: List<ServiceProxy>
) : InfoBroker {

    override fun getInfoByArtistName(artist: String): List<Card> = getCardsFromProxies(artist)

    private fun getCardsFromProxies(artist: String): List<Card> =
        proxyList.map {
            getCardFromProxy(artist, it)
        }.filterIsInstance<Card>()

    private fun getCardFromProxy(artist: String, serviceProxy: ServiceProxy): Card? =
        serviceProxy.getInfo(artist)

}
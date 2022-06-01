package ayds.newyork.songinfo.moredetails.model.repository.broker

import ayds.newyork.songinfo.moredetails.model.entities.Card
import ayds.newyork.songinfo.moredetails.model.entities.CardImpl
import ayds.newyork.songinfo.moredetails.model.repository.broker.proxy.ServiceProxy
import java.util.*

interface InfoBroker {
    fun getInfoByArtistName(artist: String): List<CardImpl>
}

internal class InfoBrokerImpl(
    private val proxyList: List<ServiceProxy>
) : InfoBroker {
    private lateinit var artist: String
    private var info = LinkedList<CardImpl>()

    override fun getInfoByArtistName(artist: String): List<CardImpl> {
        this.artist = artist
        callProxies()
        return info
    }

    private fun callProxies() {
        proxyList.forEach {
            addCardToList(getCardFromProxy(it))
        }
    }

    private fun getCardFromProxy(serviceProxy: ServiceProxy): Card = serviceProxy.getInfo(artist)

    private fun addCardToList(card: Card) {
        when (card) {
            is CardImpl -> this.info.add(card)
        }
    }
}
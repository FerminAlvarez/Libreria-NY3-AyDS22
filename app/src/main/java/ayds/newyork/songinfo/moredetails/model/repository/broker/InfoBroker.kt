package ayds.newyork.songinfo.moredetails.model.repository.broker

import ayds.newyork.songinfo.moredetails.model.entities.Card
import ayds.newyork.songinfo.moredetails.model.entities.CardImpl
import ayds.newyork.songinfo.moredetails.model.repository.broker.proxy.newyorktimes.NytProxy
import java.util.*

interface InfoBroker {
    fun getInfoByArtistName(artist: String): List<CardImpl>
}

internal class InfoBrokerImpl(
    private val nytProxy: NytProxy
) : InfoBroker {
    private lateinit var artist: String
    private var info = LinkedList<CardImpl>()

    override fun getInfoByArtistName(artist: String): List<CardImpl> {
        this.artist = artist
        callProxys()
        return info
    }

    private fun callProxys() {
        addResultToList(callNytProxy())
        //todo( resto de los proxys)
    }

    private fun callNytProxy(): Card = nytProxy.getInfoByArtistNameNyt(artist)

    private fun addResultToList(card: Card) {
        when (card) {
            is CardImpl -> this.info.add(card)
        }
    }
}
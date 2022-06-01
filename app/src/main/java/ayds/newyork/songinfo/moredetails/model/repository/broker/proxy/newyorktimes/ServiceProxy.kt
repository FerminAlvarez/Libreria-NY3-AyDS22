package ayds.newyork.songinfo.moredetails.model.repository.broker.proxy.newyorktimes

import ayds.newyork.songinfo.moredetails.model.entities.Card

interface ServiceProxy {
    fun getInfo(artist: String): Card
}
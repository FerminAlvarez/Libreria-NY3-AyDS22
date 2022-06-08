package ayds.newyork.songinfo.moredetails.model.repository.local.card

import ayds.newyork.songinfo.moredetails.model.entities.Card

interface LocalStorage {
    fun saveArtist(artist: Card, artistName: String)

    fun getInfoByArtistName(artist: String): List<Card>
}
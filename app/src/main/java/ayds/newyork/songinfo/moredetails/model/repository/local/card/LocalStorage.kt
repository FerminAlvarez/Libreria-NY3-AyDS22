package ayds.newyork.songinfo.moredetails.model.repository.local.card

import ayds.newyork.songinfo.moredetails.model.entities.CardImpl

interface LocalStorage {
    fun saveArtist(artist: CardImpl, artistName: String)

    fun getInfoByArtistName(artist: String): CardImpl?
}
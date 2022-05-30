package ayds.newyork.songinfo.moredetails.model.repository.local.nyt

import ayds.newyork.songinfo.moredetails.model.entities.CardImpl

interface NytLocalStorage {
    fun saveArtist(artist: CardImpl, artistName: String)

    fun getInfoByArtistName(artist: String): CardImpl?
}
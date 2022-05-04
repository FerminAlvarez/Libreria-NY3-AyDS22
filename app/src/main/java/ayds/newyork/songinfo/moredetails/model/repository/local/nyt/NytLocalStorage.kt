package ayds.newyork.songinfo.moredetails.model.repository.local.nyt

import ayds.newyork.songinfo.moredetails.model.entities.NytArtistInfo

interface NytLocalStorage {
    fun saveArtist(artist: NytArtistInfo)

    fun getInfoByArtistName(artist: String): NytArtistInfo?
}
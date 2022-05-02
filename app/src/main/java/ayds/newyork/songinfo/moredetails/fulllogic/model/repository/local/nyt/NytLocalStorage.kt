package ayds.newyork.songinfo.moredetails.fulllogic.model.repository.local.nyt

import ayds.newyork.songinfo.moredetails.fulllogic.model.entities.ArtistInfo

interface NytLocalStorage {
    fun saveArtist(artist: String, info: String)

    fun getInfoByArtistName(artist: String): ArtistInfo
}
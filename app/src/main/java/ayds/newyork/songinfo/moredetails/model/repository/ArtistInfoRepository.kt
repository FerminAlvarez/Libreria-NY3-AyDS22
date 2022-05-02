package ayds.newyork.songinfo.moredetails.model.repository

import ayds.newyork.songinfo.moredetails.model.entities.ArtistInfo
import ayds.newyork.songinfo.moredetails.model.entities.EmptyArtistInfo
import ayds.newyork.songinfo.moredetails.model.repository.local.nyt.NytLocalStorage

interface ArtistInfoRepository {
    fun getInfoByArtistName(artist: String): ArtistInfo
}

internal class ArtistInfoRepositoryImpl(
    private val nytLocalStorage: NytLocalStorage
) : ArtistInfoRepository {


    override fun getInfoByArtistName(artist: String): ArtistInfo {
        var artistInfo = nytLocalStorage.getInfoByArtistName(artist)
        nytLocalStorage.saveArtist(artistInfo.artistName, artistInfo.artistInfo)
        //TODO agregar el asterisco, ver estructura de SpotifyLocalStorageImpl
        return artistInfo ?: EmptyArtistInfo
    }

}
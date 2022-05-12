package ayds.newyork.songinfo.moredetails.model.repository

import ayds.newyork.songinfo.moredetails.model.entities.ArtistInfo
import ayds.newyork.songinfo.moredetails.model.entities.EmptyArtistInfo
import ayds.newyork.songinfo.moredetails.model.entities.NytArtistInfo
import ayds.newyork.songinfo.moredetails.model.repository.external.nyt.NytArticleService
import ayds.newyork.songinfo.moredetails.model.repository.local.nyt.NytLocalStorage
import java.lang.Exception

interface ArtistInfoRepository {
    fun getInfoByArtistName(artist: String): ArtistInfo
}

internal class ArtistInfoRepositoryImpl(
    private val nytLocalStorage: NytLocalStorage,
    private val nytArticleService: NytArticleService
) : ArtistInfoRepository {


    override fun getInfoByArtistName(artist: String): ArtistInfo {
        var artistInfo = nytLocalStorage.getInfoByArtistName(artist)

        when {
            artistInfo != null -> markArticleAsLocal(artistInfo)
            else -> {
                try {
                    artistInfo = nytArticleService.getArtistInfo(artist)

                    artistInfo?.let {
                        nytLocalStorage.saveArtist(it)
                    }
                } catch (e: Exception) {
                    artistInfo = null
                }
            }
        }
        return artistInfo ?: EmptyArtistInfo
    }

    private fun NytArtistInfo.isSavedArticle() =
        nytLocalStorage.getInfoByArtistName(artistName) != null

    private fun markArticleAsLocal(article: NytArtistInfo) {
        article.isLocallyStored = true
    }


}
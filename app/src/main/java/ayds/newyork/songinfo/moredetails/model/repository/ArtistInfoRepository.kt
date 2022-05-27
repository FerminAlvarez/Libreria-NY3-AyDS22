package ayds.newyork.songinfo.moredetails.model.repository

import ayds.newyork.songinfo.moredetails.model.entities.ArtistInfo
import ayds.newyork.songinfo.moredetails.model.entities.EmptyArtistInfo
import ayds.newyork.songinfo.moredetails.model.entities.NytArtistInfo
import ayds.newyork.songinfo.moredetails.model.repository.local.nyt.NytLocalStorage
import ayds.ny3.newyorktimes.NytArticleService
import ayds.ny3.newyorktimes.Card as ServiceNytArtistInfo
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
                    val serviceNytArtistInfo = nytArticleService.getArtistInfo(artist)

                    serviceNytArtistInfo?.let {
                        artistInfo = NytArtistInfo(
                            artist,
                            it.description,
                            it.infoURL,
                            it.source,
                            it.sourceLogoUrl
                        )
                    }

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

    private fun markArticleAsLocal(article: NytArtistInfo) {
        article.isLocallyStored = true
    }
}
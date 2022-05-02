package ayds.newyork.songinfo.moredetails.model.repository.external.nyt

import ayds.newyork.songinfo.moredetails.model.entities.NytArtistInfo

interface NytArticleService {
    fun getArtistInfo(name: String): NytArtistInfo?
}
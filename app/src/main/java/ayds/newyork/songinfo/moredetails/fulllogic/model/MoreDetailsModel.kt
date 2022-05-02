package ayds.newyork.songinfo.moredetails.fulllogic.model

import ayds.newyork.songinfo.home.model.repository.SongRepository
import ayds.newyork.songinfo.moredetails.fulllogic.model.entities.ArtistInfo
import ayds.newyork.songinfo.moredetails.fulllogic.model.repository.ArtistInfoRepository
import ayds.observer.Observable
import ayds.observer.Subject

interface MoreDetailsModel {

        val artistObservable: Observable<ArtistInfo>

        fun getInfoByArtistName(artist: String): ArtistInfo
}

internal class MoreDetailsModelImpl(private val repository: ArtistInfoRepository) : MoreDetailsModel {

        override val artistObservable = Subject<ArtistInfo>()

        override fun getInfoByArtistName(artistName: String): ArtistInfo = repository.getInfoByArtistName(artistName)
}
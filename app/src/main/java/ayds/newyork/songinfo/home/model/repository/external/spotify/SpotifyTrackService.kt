package ayds.newyork.songinfo.home.model.repository.external.spotify

import ayds.newyork.songinfo.home.model.entities.SpotifySong

interface SpotifyTrackService {

    fun getSong(title: String): SpotifySong?
}
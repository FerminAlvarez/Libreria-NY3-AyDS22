package ayds.newyork.songinfo.home.model.repository.external.spotify

import ayds.newyork.songinfo.home.model.repository.external.spotify.tracks.*

object SpotifyInjector {

    val spotifyTrackService: SpotifyTrackService = SpotifyTrackInjector.spotifyTrackService
}
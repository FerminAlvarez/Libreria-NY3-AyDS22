package ayds.newyork.songinfo.home.model.repository.local.spotify

import ayds.newyork.songinfo.home.model.entities.SpotifySong

interface SpotifyLocalStorage {

    fun updateSongTerm(query: String, songId: String)

    fun insertSong(query: String, song: SpotifySong)

    fun getSongByTerm(term: String): SpotifySong?

    fun getSongById(id: String): SpotifySong?
}
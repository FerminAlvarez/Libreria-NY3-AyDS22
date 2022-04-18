package ayds.newyork.songinfo.home.model.repository.external.spotify.tracks

import ayds.newyork.songinfo.home.model.repository.local.spotify.DatePrecision

private const val SPOTIFY_DAY = "day"
private const val SPOTIFY_MONTH = "month"
private const val SPOTIFY_YEAR = "year"

interface SpotifyToDatePrecisionMapper{
    fun getDatePrecision(spotifyDatePrecision: String) : DatePrecision
}

internal class SpotifyToDatePrecisionMapperImpl : SpotifyToDatePrecisionMapper {
    override fun getDatePrecision(spotifyDatePrecision: String): DatePrecision =
        when(spotifyDatePrecision){
            SPOTIFY_DAY -> DatePrecision.DAY
            SPOTIFY_MONTH -> DatePrecision.MONTH
            SPOTIFY_YEAR -> DatePrecision.YEAR
            else -> DatePrecision.EMPTY
        }
}

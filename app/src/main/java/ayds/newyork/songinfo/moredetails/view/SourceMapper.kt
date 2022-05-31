package ayds.newyork.songinfo.moredetails.view

import ayds.newyork.songinfo.moredetails.model.repository.broker.InfoSource

private const val NYT_SOURCE = "The New York Times"
private const val LASTFM_SOURCE = "LastFM"
private const val WIKIPEDIA_SOURCE = "Wikipedia"

class SourceMapper {
    fun getSource(source: InfoSource) =
        when (source) {
            InfoSource.NEW_YORK_TIMES -> NYT_SOURCE
            InfoSource.LAST_FM -> LASTFM_SOURCE
            InfoSource.WIKIPEDIA -> WIKIPEDIA_SOURCE
            else -> ""
        }
}
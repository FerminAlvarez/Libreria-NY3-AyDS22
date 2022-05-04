package ayds.newyork.songinfo.moredetails.view

data class MoreDetailsUiState (
    val articleUrl: String = "",
    val artistInfo: String = "",
) {
    companion object {
        const val ARTIST_NAME_EXTRA = "artistName"
    }
}
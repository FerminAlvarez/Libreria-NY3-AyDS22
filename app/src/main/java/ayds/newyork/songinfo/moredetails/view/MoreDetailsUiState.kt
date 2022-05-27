package ayds.newyork.songinfo.moredetails.view

data class MoreDetailsUiState(
    val artistName: String = "",
    val articleUrl: String = "",
    val artistInfo: String = "",
    val source: String = "",
    val sourceLogoUrl: String = DEFAULT_LOGO,
) {
    companion object {
        const val DEFAULT_LOGO =
            "https://sourceless.io/wp-content/uploads/2021/05/sourceless-blockchain-logo-long-version.png"
    }
}
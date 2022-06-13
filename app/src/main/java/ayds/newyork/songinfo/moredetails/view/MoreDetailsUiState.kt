package ayds.newyork.songinfo.moredetails.view

import ayds.newyork.songinfo.moredetails.model.entities.Card

data class MoreDetailsUiState(
    val artistName: String = "",
    val cards: List<Card> = emptyList(),
    val sourceLogoUrl: String = DEFAULT_LOGO,
) {
    companion object {
        const val DEFAULT_LOGO =
            "https://sourceless.io/wp-content/uploads/2021/05/sourceless-blockchain-logo-long-version.png"
    }
}
package ayds.newyork.songinfo.moredetails.view

data class MoreDetailsUiState(
    val artistName: String = "",
    val articleUrl: String = "",
    val artistInfo: String = "",
    val logoUrl: String = LOGO_NYT,
) {
    companion object {
        private const val LOGO_NYT =
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRVioI832nuYIXqzySD8cOXRZEcdlAj3KfxA62UEC4FhrHVe0f7oZXp3_mSFG7nIcUKhg&usqp=CAU"
        const val EMPTY_INFO = "No Results"
        const val EMPTY_URL = ""
    }
}
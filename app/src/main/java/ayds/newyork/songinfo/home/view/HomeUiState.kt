package ayds.newyork.songinfo.home.view

data class HomeUiState(
    val songId: String = "",
    val searchTerm: String = "",
    val songDescription: String = "",
    val songImageUrl: String = DEFAULT_IMAGE,
    val songUrl: String = "",
    val actionsEnabled: Boolean = false,
) {

    companion object {
        const val DEFAULT_IMAGE =
            "https://upload.wikimedia.org/wikipedia/commons/thumb/9/9d/NYC_Montage_2014_4_-_Jleon.jpg/456px-NYC_Montage_2014_4_-_Jleon.jpg"
    }
}
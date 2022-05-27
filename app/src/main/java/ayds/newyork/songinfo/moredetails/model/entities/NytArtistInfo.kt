package ayds.newyork.songinfo.moredetails.model.entities


interface ArtistInfo {
    val artistName: String
    val artistInfo: String
    val artistURL: String
    val source: String
    val sourceLogoUrl: String
    var isLocallyStored: Boolean
}

data class NytArtistInfo(
    override val artistName: String,
    override val artistInfo: String,
    override val artistURL: String,
    override val source: String,
    override val sourceLogoUrl: String,
    override var isLocallyStored: Boolean = false
) : ArtistInfo

object EmptyArtistInfo : ArtistInfo {
    override val artistName: String = ""
    override val artistInfo: String = ""
    override val artistURL: String = ""
    override val source: String = ""
    override val sourceLogoUrl: String = ""
    override var isLocallyStored: Boolean = false
}
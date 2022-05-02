package ayds.newyork.songinfo.moredetails.model.entities

interface ArtistInfo {
    val id: String
    val artistName: String
    val artistInfo: String
    var isLocallyStored: Boolean
}

data class NytArtistInfo (
    override val id: String,
    override val artistName: String,
    override val artistInfo: String,
    override var isLocallyStored: Boolean = false
) : ArtistInfo

object EmptyArtistInfo : ArtistInfo {
    override val id: String = ""
    override val artistName: String = ""
    override val artistInfo: String = ""
    override var isLocallyStored: Boolean = false
}
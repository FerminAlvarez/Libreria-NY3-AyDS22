package ayds.newyork.songinfo.moredetails.model.entities

interface Card {
    val description: String
    val infoURL: String
    val source: String
    var sourceLogoUrl: String
    var isLocallyStored: Boolean
}

data class CardImpl(
    override val description: String,
    override val infoURL: String,
    override val source: String,
    override var sourceLogoUrl: String,
    override var isLocallyStored: Boolean = false,
) : Card

object EmptyCard : Card {
    override val description: String = ""
    override val infoURL: String = ""
    override val source: String = ""
    override var sourceLogoUrl: String = ""
    override var isLocallyStored: Boolean = false
}
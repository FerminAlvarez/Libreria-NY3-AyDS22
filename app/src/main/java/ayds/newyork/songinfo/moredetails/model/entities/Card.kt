package ayds.newyork.songinfo.moredetails.model.entities

import ayds.newyork.songinfo.moredetails.model.repository.broker.InfoSource

interface Card {
    val description: String
    val infoURL: String
    val source: InfoSource
    var sourceLogoUrl: String
    var isLocallyStored: Boolean
}

data class CardImpl(
    override val description: String,
    override val infoURL: String,
    override val source: InfoSource,
    override var sourceLogoUrl: String,
    override var isLocallyStored: Boolean = false,
) : Card

object EmptyCard : Card {
    override val description: String = ""
    override val infoURL: String = ""
    override val source: InfoSource = InfoSource.EMPTY
    override var sourceLogoUrl: String = ""
    override var isLocallyStored: Boolean = false
}
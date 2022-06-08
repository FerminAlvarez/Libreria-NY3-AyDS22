package ayds.newyork.songinfo.moredetails.model.entities

import ayds.newyork.songinfo.moredetails.model.repository.broker.InfoSource

data class Card(
    val description: String,
    val infoURL: String,
    val source: InfoSource,
    var sourceLogoUrl: String,
    var isLocallyStored: Boolean = false,
)
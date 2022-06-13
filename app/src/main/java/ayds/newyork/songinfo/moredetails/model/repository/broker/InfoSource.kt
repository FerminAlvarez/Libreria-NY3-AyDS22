package ayds.newyork.songinfo.moredetails.model.repository.broker

enum class InfoSource(val value: String) {
    NEW_YORK_TIMES("The New York Times"),
    LAST_FM("Last.FM"),
    WIKIPEDIA("Wikipedia"),
    EMPTY("")
}
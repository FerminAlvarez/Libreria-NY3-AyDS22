package ayds.newyork.songinfo.moredetails.model.repository.local.card.sqldb

const val ARTISTS_TABLE = "artists"
const val ID_COLUMN = "id"
const val ARTIST_COLUMN = "artist"
const val INFO_COLUMN = "info"
const val SOURCE_COLUMN = "source"
const val SOURCE_LOGO_URL_COLUMN = "sourceLogoUrl"
const val URL_COLUMN = "url"

const val createArtistArticleTableQuery =
    "create table $ARTISTS_TABLE (" +
            "$ID_COLUMN INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "$ARTIST_COLUMN string, " +
            "$INFO_COLUMN string," +
            "$SOURCE_COLUMN integer," +
            "$SOURCE_LOGO_URL_COLUMN string," +
            "$URL_COLUMN string)"
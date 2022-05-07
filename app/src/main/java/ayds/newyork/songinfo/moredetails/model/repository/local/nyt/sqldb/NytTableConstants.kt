package ayds.newyork.songinfo.moredetails.model.repository.local.nyt.sqldb

const val ARTISTS_TABLE = "artists"
const val ID_COLUMN = "id"
const val ARTIST_COLUMN = "artist"
const val INFO_COLUMN = "info"
const val URL_COLUMN = "url"

const val createNytArtistArticleTableQuery =
    "create table $ARTISTS_TABLE (" +
            "$ID_COLUMN INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "$ARTIST_COLUMN string, " +
            "$INFO_COLUMN string)" +
            "$URL_COLUMN string)"
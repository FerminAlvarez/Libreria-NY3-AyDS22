package ayds.newyork.songinfo.moredetails.view


import ayds.newyork.songinfo.moredetails.model.entities.Card
import ayds.newyork.songinfo.moredetails.model.repository.broker.InfoSource
import org.junit.Assert
import org.junit.Test

class ArtistInfoHelperImplTest {
    private val artistInfoHelperImpl = ArtistInfoHelperImpl()
    private var cardInfo = Card(
        "One of the most lasting and influential artistic movements of the 20th century " +
                "was created with and for Black artists." +
                "Why has their contribution been so overlooked?",
        "",
        InfoSource.EMPTY,
        ""
    )

    @Test
    fun `given a EmptyArtistInfo it should return artist info not found`() {
        val result = artistInfoHelperImpl.getEmptyInfoText()

        val expected = "Artist Info not found"

        Assert.assertEquals(expected, result)
    }

    @Test
    fun `given a ArtistInfo not locally stored it should return the info`() {
        cardInfo.isLocallyStored = false

        val result = artistInfoHelperImpl.getArtistInfoText(cardInfo,"artist")

        val expected = "<html><div width=400><font face=\"arial\">" +
                "One of the most lasting and influential artistic movements of the 20th century was " +
                "created with and for Black artists." +
                "Why has their contribution been so overlooked?" +
                "</font></div></html>"

        Assert.assertEquals(expected, result)
    }

    @Test
    fun `given a ArtistInfo locally stored it should return the info`() {
        cardInfo.isLocallyStored = true

        val result = artistInfoHelperImpl.getArtistInfoText(cardInfo, "artist")

        val expected = "[*]\n" +
                "<html><div width=400><font face=\"arial\">" +
                "One of the most lasting and influential artistic movements of the 20th century was " +
                "created with and for Black artists." +
                "Why has their contribution been so overlooked?" +
                "</font></div></html>"

        Assert.assertEquals(expected, result)
    }

}
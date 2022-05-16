package ayds.newyork.moredetails.view

import ayds.newyork.songinfo.moredetails.model.entities.EmptyArtistInfo
import ayds.newyork.songinfo.moredetails.model.entities.NytArtistInfo
import ayds.newyork.songinfo.moredetails.view.ArtistInfoHelperImpl
import org.junit.Assert
import org.junit.Test

class ArtistInfoHelperImplTest {
    private val artistInfoHelperImpl = ArtistInfoHelperImpl()

    @Test
    fun `given a EmptyArtistInfo it should return artist info not found`() {
        val artistInfo = EmptyArtistInfo

        val result = artistInfoHelperImpl.getArtistInfoText(artistInfo)

        val expected = "Artist Info not found"

        Assert.assertEquals(expected, result)
    }

    @Test
    fun `given a ArtistInfo not locally stored it should return the info`() {
        val artistInfo = NytArtistInfo(
            "Jimi Hendrix",
            "One of the most lasting and influential artistic movements of the 20th century " +
                    "was created with and for Black artists." +
                    "Why has their contribution been so overlooked?",
            "https://www.nytimes.com/2022/02/10/t-magazine/black-psychedelia.html",
            false
        )

        val result = artistInfoHelperImpl.getArtistInfoText(artistInfo)

        val expected = "<html><div width=400><font face=\"arial\">" +
                "One of the most lasting and influential artistic movements of the 20th century was " +
                "created with and for Black artists." +
                "Why has their contribution been so overlooked?" +
                "</font></div></html>"

        Assert.assertEquals(expected, result)
    }

    @Test
    fun `given a ArtistInfo locally stored it should return the info`() {
        val artistInfo = NytArtistInfo(
            "Jimi Hendrix",
            "One of the most lasting and influential artistic movements of the 20th century " +
                    "was created with and for Black artists." +
                    "Why has their contribution been so overlooked?",
            "https://www.nytimes.com/2022/02/10/t-magazine/black-psychedelia.html",
            true
        )

        val result = artistInfoHelperImpl.getArtistInfoText(artistInfo)

        val expected = "[*]\n" +
                "<html><div width=400><font face=\"arial\">" +
                "One of the most lasting and influential artistic movements of the 20th century was " +
                "created with and for Black artists." +
                "Why has their contribution been so overlooked?" +
                "</font></div></html>"

        Assert.assertEquals(expected, result)
    }

}
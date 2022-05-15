package ayds.newyork.moredetails.view

import ayds.newyork.songinfo.moredetails.model.entities.EmptyArtistInfo
import ayds.newyork.songinfo.moredetails.view.ArtistInfoHelperImpl
import io.mockk.mockk
import org.junit.Assert
import org.junit.Test

class ArtistInfoHelperImplTest {
    private val artistInfoHelperImpl = ArtistInfoHelperImpl()

    @Test
    fun `given a EmptyArtistInfo it should return artist info not found`(){
        val artistInfo = EmptyArtistInfo

        val result = artistInfoHelperImpl.getArtistInfoText(artistInfo)

        val expected = "Artist Info not found"

        Assert.assertEquals(expected,result)
    }

}
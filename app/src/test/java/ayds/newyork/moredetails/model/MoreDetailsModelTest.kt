package ayds.newyork.moredetails.model

import ayds.newyork.songinfo.moredetails.model.MoreDetailsModel
import ayds.newyork.songinfo.moredetails.model.MoreDetailsModelImpl
import ayds.newyork.songinfo.moredetails.model.entities.ArtistInfo
import ayds.newyork.songinfo.moredetails.model.repository.ArtistInfoRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert
import org.junit.Test

class MoreDetailsModelTest {

    private val repository: ArtistInfoRepository = mockk()

    private val moreDetailsModel = MoreDetailsModelImpl(repository)

    @Test
    fun `given an artistName it should notify the result`() {
        val artistInfo: ArtistInfo = mockk()
        every { repository.getInfoByArtistName("test") } returns artistInfo
        val artistTester: (ArtistInfo) -> Unit = mockk(relaxed = true)
        moreDetailsModel.artistObservable.subscribe{
            artistTester(it)
        }

        moreDetailsModel.getInfoByArtistName("test")

        verify { artistTester(artistInfo) }
    }
}
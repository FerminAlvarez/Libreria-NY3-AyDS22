package ayds.newyork.songinfo.moredetails.model

import ayds.newyork.songinfo.moredetails.model.entities.Card
import ayds.newyork.songinfo.moredetails.model.repository.ArtistInfoRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class MoreDetailsModelTest {

    private val repository: ArtistInfoRepository = mockk()

    private val moreDetailsModel = MoreDetailsModelImpl(repository)

    @Test
    fun `given an artistName it should notify the result`() {
        val artistInfo: List<Card> = mockk()
        every { repository.getInfoByArtistName("test") } returns artistInfo
        val artistTester: (List<Card>) -> Unit = mockk(relaxed = true)
        moreDetailsModel.artistObservable.subscribe {
            artistTester(it)
        }

        moreDetailsModel.getInfoByArtistName("test")

        verify { artistTester(artistInfo) }
    }
}
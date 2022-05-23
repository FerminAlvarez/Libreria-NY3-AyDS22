package ayds.newyork.songinfo.moredetails.model.repository

import ayds.newyork.songinfo.moredetails.model.entities.EmptyArtistInfo
import ayds.newyork.songinfo.moredetails.model.entities.NytArtistInfo
import ayds.ny3.newyorktimes.NytArticleService
import ayds.newyork.songinfo.moredetails.model.repository.local.nyt.NytLocalStorage
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.*
import org.junit.Test

class ArtistInfoRepositoryTest {
    private val nytLocalStorage: NytLocalStorage = mockk(relaxUnitFun = true)
    private val nytArticleService: ayds.ny3.newyorktimes.NytArticleService = mockk(relaxUnitFun = true)

    private val artistInfoRepository: ArtistInfoRepository by lazy {
        ArtistInfoRepositoryImpl(nytLocalStorage, nytArticleService)
    }

    @Test
    fun `given non existing artist info by name should return empty artist info`() {
        every { nytLocalStorage.getInfoByArtistName("name") } returns null
        every { nytArticleService.getArtistInfo("name") } returns null

        val result = artistInfoRepository.getInfoByArtistName("name")

        assertEquals(EmptyArtistInfo, result)
    }

    @Test
    fun `given existing artist info by name should return artist info and mark it as local`() {
        val artistInfo = NytArtistInfo("name", "artist info", "artist URL")
        every { nytLocalStorage.getInfoByArtistName("name") } returns artistInfo

        val result = artistInfoRepository.getInfoByArtistName("name")

        assertEquals(artistInfo, result)
        assertTrue(artistInfo.isLocallyStored)
    }

    @Test
    fun `given non existing artist info by name should get the info and store it`() {
        val artistInfo = NytArtistInfo("name", "artist info", "artist URL")
        every { nytLocalStorage.getInfoByArtistName("name") } returns null
        every { nytArticleService.getArtistInfo("name") } returns artistInfo

        val result = artistInfoRepository.getInfoByArtistName("name")

        assertEquals(artistInfo, result)
        assertFalse(artistInfo.isLocallyStored)
        verify { nytLocalStorage.saveArtist(artistInfo) }
    }

    @Test
    fun `given service exception should return empty artist info`() {
        every { nytLocalStorage.getInfoByArtistName("name") } returns null
        every { nytArticleService.getArtistInfo("name") } throws mockk<Exception>()

        val result = artistInfoRepository.getInfoByArtistName("name")

        assertEquals(EmptyArtistInfo, result)
    }
}
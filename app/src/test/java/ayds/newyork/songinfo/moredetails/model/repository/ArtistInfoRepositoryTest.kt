package ayds.newyork.songinfo.moredetails.model.repository

import ayds.newyork.songinfo.moredetails.model.entities.Card
import ayds.newyork.songinfo.moredetails.model.repository.broker.InfoBroker
import ayds.newyork.songinfo.moredetails.model.repository.broker.InfoSource
import ayds.newyork.songinfo.moredetails.model.repository.local.card.LocalStorage
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.*
import org.junit.Test
import java.util.*

class ArtistInfoRepositoryTest {
    private val localStorage: LocalStorage = mockk(relaxUnitFun = true)
    private val infoBroker: InfoBroker = mockk(relaxUnitFun = true)

    private val artistInfoRepository: ArtistInfoRepository by lazy {
        ArtistInfoRepositoryImpl(localStorage, infoBroker)
    }

    @Test
    fun `given non existing artist info by name should return empty artist info`() {
        every { localStorage.getInfoByArtistName("name") } returns emptyList()
        every { infoBroker.getInfoByArtistName("name") } returns emptyList()

        val result = artistInfoRepository.getInfoByArtistName("name")

        assertTrue(result.isEmpty())
    }

    @Test
    fun `given existing artist info by name should return artist info and mark it as local`() {
        val artistsList = LinkedList<Card>()
        val artistInfo = Card("artist info", "", InfoSource.EMPTY, "")
        artistsList.push(artistInfo)

        every { localStorage.getInfoByArtistName("name") } returns artistsList

        val result = artistInfoRepository.getInfoByArtistName("name")

        assertEquals(artistsList, result)
        assertTrue(artistInfo.isLocallyStored)
    }

    @Test
    fun `given non existing artist info by name should get the info and store it`() {
        val artistsList = LinkedList<Card>()
        val artistInfo = Card("name", "artist info URL", InfoSource.EMPTY, "")
        artistsList.push(artistInfo)

        every { localStorage.getInfoByArtistName("name") } returns emptyList()
        every { infoBroker.getInfoByArtistName("name") } returns artistsList

        val result = artistInfoRepository.getInfoByArtistName("name")

        assertEquals(artistsList, result)
        assertFalse(artistInfo.isLocallyStored)
        verify { localStorage.saveArtist(artistInfo, "name") }
    }

}
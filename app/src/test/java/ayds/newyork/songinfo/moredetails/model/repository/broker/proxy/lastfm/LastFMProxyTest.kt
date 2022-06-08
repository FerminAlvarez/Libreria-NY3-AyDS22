package ayds.newyork.songinfo.moredetails.model.repository.broker.proxy.lastfm

import ayds.lisboa.lastfmdata.lastfm.LastFMService
import ayds.lisboa.lastfmdata.lastfm.entities.LastFMArtist
import ayds.newyork.songinfo.moredetails.model.entities.CardImpl
import ayds.newyork.songinfo.moredetails.model.repository.broker.InfoSource
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Test

class LastFMProxyTest {
    private val lastFMService: LastFMService = mockk()

    private val lastFMProxy = LastFMProxyImpl(lastFMService)

    @Test
    fun `given an artistName it should call the service`() {
        val lastFMArtist = LastFMArtist(
            name = "name",
            description = "info",
            infoUrl = "article",
            sourceLogoUrl = "logo",
        )

        val expected = CardImpl(
            description = "info",
            infoURL = "article",
            source = InfoSource.LAST_FM,
            sourceLogoUrl = "logo",
        )

        every { lastFMService.getArtist("name") } returns lastFMArtist

        val result = lastFMProxy.getInfo("name")

        assertEquals(expected, result)
        verify { lastFMService.getArtist("name") }
    }
}
package ayds.newyork.songinfo.moredetails.model.repository.broker.proxy.wikipedia

import ayds.newyork.songinfo.moredetails.model.entities.CardImpl
import ayds.newyork.songinfo.moredetails.model.repository.broker.InfoSource
import ayds.winchester1.wikipedia.WikipediaArtistInfo
import ayds.winchester1.wikipedia.WikipediaService
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test

class WikipediaProxyTest {
    private val wikipediaService: WikipediaService = mockk()

    private val wikipediaProxy = WikipediaProxy(wikipediaService)

    @Test
    fun `given an artistName it should call the service`() {
        val wikipediaArtistInfo = WikipediaArtistInfo(
            description = "info",
            infoURL = "article",
            sourceLogoURL = "logo",
        )

        val expected = CardImpl(
            description = "info",
            infoURL = "article",
            source = InfoSource.WIKIPEDIA,
            sourceLogoUrl = "logo",
        )

        every { wikipediaService.getArtistInfo("name") } returns wikipediaArtistInfo

        val result = wikipediaProxy.getInfo("name")

        assertEquals(expected, result)
    }
}
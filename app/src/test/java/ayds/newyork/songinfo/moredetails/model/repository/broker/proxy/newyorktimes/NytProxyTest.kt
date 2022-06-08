package ayds.newyork.songinfo.moredetails.model.repository.broker.proxy.newyorktimes

import ayds.newyork.songinfo.moredetails.model.entities.Card
import ayds.newyork.songinfo.moredetails.model.repository.broker.InfoSource
import ayds.ny3.newyorktimes.NytArticleService
import ayds.ny3.newyorktimes.NytArtistInfo
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Test

class NytProxyTest {
    private val nytArticleService: NytArticleService = mockk()

    private val nytProxy = NytProxy(nytArticleService)

    @Test
    fun `given an artistName it should call the service`() {
        val nytArtistInfo = NytArtistInfo(
            artistName = "name",
            artistInfo = "info",
            artistURL = "article",
            nytLogo = "logo",
        )

        val expected = Card(
            description = "info",
            infoURL = "article",
            source = InfoSource.NEW_YORK_TIMES,
            sourceLogoUrl = "logo",
        )

        every { nytArticleService.getArtistInfo("name") } returns nytArtistInfo

        val result = nytProxy.getInfo("name")

        assertEquals(expected, result)
        verify { nytArticleService.getArtistInfo("name") }
    }
}
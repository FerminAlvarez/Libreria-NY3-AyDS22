package ayds.newyork.songinfo.moredetails.model.repository.broker

import ayds.newyork.songinfo.moredetails.model.entities.Card
import ayds.newyork.songinfo.moredetails.model.repository.broker.proxy.ServiceProxy
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test

class InfoBrokerTest {
    private val proxy: ServiceProxy = mockk()
    private val proxyList: List<ServiceProxy> = arrayListOf(
        proxy,
        proxy,
        proxy
    )

    private val infoBroker = InfoBrokerImpl(proxyList)

    @Test
    fun `given an artistName it should call all proxies`() {
        val card: Card = mockk()
        val infoList = arrayListOf<Card>(
            card,
            card,
            card
        )

        every { proxy.getInfo("name") } returns card

        val result = infoBroker.getInfoByArtistName("name")

        assertEquals(infoList, result)
    }
}
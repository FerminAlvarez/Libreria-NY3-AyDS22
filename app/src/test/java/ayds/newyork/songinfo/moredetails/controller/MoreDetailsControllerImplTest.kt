package ayds.newyork.songinfo.moredetails.controller

import ayds.newyork.songinfo.moredetails.model.MoreDetailsModel
import ayds.newyork.songinfo.moredetails.model.entities.Card
import ayds.newyork.songinfo.moredetails.model.entities.CardImpl
import ayds.newyork.songinfo.moredetails.model.entities.EmptyCard.infoURL
import ayds.newyork.songinfo.moredetails.model.repository.broker.InfoSource
import ayds.newyork.songinfo.moredetails.model.repository.broker.proxy.ServiceProxy
import ayds.newyork.songinfo.moredetails.view.MoreDetailsUiEvent
import ayds.newyork.songinfo.moredetails.view.MoreDetailsUiState
import ayds.newyork.songinfo.moredetails.view.MoreDetailsView
import ayds.observer.Subject
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class MoreDetailsControllerImplTest {

    private val moreDetailsModel: MoreDetailsModel = mockk(relaxUnitFun = true)

    private val onActionSubject = Subject<MoreDetailsUiEvent>()
    private val moreDetailsView: MoreDetailsView = mockk(relaxUnitFun = true) {
        every { uiEventObservable } returns onActionSubject
    }

    private val moreDetailsController by lazy {
        MoreDetailsControllerImpl(moreDetailsModel)
    }

    @Before
    fun setup() {
        moreDetailsController.setMoreDetailsView(moreDetailsView)
    }

    @Test
    fun `on search event should search artist info`() {
        every { moreDetailsView.uiState } returns MoreDetailsUiState(artistName = "name")

        onActionSubject.notify(MoreDetailsUiEvent.SearchInfo)

        verify { moreDetailsModel.getInfoByArtistName("name") }
    }

    @Test
    fun `on open article event should open indicated external link`() {
        val source: InfoSource = mockk()
        val cards: List<Card> = arrayListOf(
            CardImpl(
                description= "description",
                infoURL="url0",
                source = source,
                sourceLogoUrl = "sourcerul0"
            ),
            CardImpl(
                description= "description",
                infoURL="url1",
                source = source,
                sourceLogoUrl = "sourcerul1"
            ),
            CardImpl(
                description= "description",
                infoURL="url2",
                source = source,
                sourceLogoUrl = "sourcerul2"
            ),
        )

        every { moreDetailsView.uiState.cards } returns cards
        MoreDetailsUiEvent.OpenArticleUrl.uiCardIndex = 1

        onActionSubject.notify(MoreDetailsUiEvent.OpenArticleUrl)

        verify { moreDetailsView.openExternalLink("url1") }
    }
}
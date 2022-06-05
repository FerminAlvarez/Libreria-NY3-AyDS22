package ayds.newyork.songinfo.moredetails.controller

import ayds.newyork.songinfo.moredetails.model.MoreDetailsModel
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
        val uiState: ArrayList<MoreDetailsUiState> = arrayListOf(
            MoreDetailsUiState(artistName = "name"),
        )

        every { moreDetailsView.uiState } returns uiState

        onActionSubject.notify(MoreDetailsUiEvent.SearchInfo)

        verify { moreDetailsModel.getInfoByArtistName("name") }
    }

    @Test
    fun `on open article event should open indicated external link`() {
        val uiState: ArrayList<MoreDetailsUiState> = arrayListOf(
            MoreDetailsUiState(articleUrl = "url0"),
            MoreDetailsUiState(articleUrl = "url1"),
            MoreDetailsUiState(articleUrl = "url2"),
        )

        every { moreDetailsView.uiState } returns uiState
        MoreDetailsUiEvent.OpenArticleUrl.uiStateIndex = 1

        onActionSubject.notify(MoreDetailsUiEvent.OpenArticleUrl)

        verify { moreDetailsView.openExternalLink("url1") }
    }
}
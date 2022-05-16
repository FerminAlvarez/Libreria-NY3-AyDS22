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
        every { moreDetailsView.uiState } returns MoreDetailsUiState(artistName = "name")

        onActionSubject.notify(MoreDetailsUiEvent.SearchNytInfo)

        verify { moreDetailsModel.getInfoByArtistName("name") }
    }

    @Test
    fun `on open article event should open external link`() {
        every { moreDetailsView.uiState } returns MoreDetailsUiState(articleUrl = "url")

        onActionSubject.notify(MoreDetailsUiEvent.OpenArticleUrl)

        verify { moreDetailsView.openExternalLink("url") }
    }
}
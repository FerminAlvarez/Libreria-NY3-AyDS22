package ayds.newyork.songinfo.moredetails.fulllogic.view

import androidx.appcompat.app.AppCompatActivity
import ayds.newyork.songinfo.utils.UtilsInjector.navigationUtils
import ayds.observer.Observable
import ayds.observer.Subject

interface MoreDetailsView {

    val uiEventObservable: Observable<MoreDetailsUiEvent>
    val uiState: MoreDetailsUiState

    fun openExternalLink(url: String)
}

class MoreDetailsViewActivity : AppCompatActivity(), MoreDetailsView{

    private val onActionSubject = Subject<MoreDetailsUiEvent>()

    override val uiEventObservable: Observable<MoreDetailsUiEvent> = onActionSubject
    override var uiState: MoreDetailsUiState = MoreDetailsUiState()


    override fun openExternalLink(url: String) {
        navigationUtils.openExternalUrl(this, url)
    }
}
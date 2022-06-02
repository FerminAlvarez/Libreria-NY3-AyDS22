package ayds.newyork.songinfo.moredetails.view

sealed class MoreDetailsUiEvent {
    object SearchInfo : MoreDetailsUiEvent() {
        var uiStateIndex: Int = 0
    }
    object OpenArticleUrl : MoreDetailsUiEvent() {
        var uiStateIndex: Int = 0
    }
}
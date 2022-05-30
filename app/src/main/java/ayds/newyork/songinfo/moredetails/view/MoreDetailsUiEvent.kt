package ayds.newyork.songinfo.moredetails.view

sealed class MoreDetailsUiEvent {
    object SearchInfo : MoreDetailsUiEvent()
    object OpenArticleUrl : MoreDetailsUiEvent()
}
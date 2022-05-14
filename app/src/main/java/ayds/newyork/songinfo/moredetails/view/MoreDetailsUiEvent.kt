package ayds.newyork.songinfo.moredetails.view

sealed class MoreDetailsUiEvent {
    object SearchNytInfo : MoreDetailsUiEvent()
    object OpenArticleUrl : MoreDetailsUiEvent()
}
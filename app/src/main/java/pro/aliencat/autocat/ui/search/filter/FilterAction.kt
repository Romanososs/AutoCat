package pro.aliencat.autocat.ui.search.filter

import pro.aliencat.autocat.ui.search.model.AddedBy
import pro.aliencat.autocat.ui.search.model.StringOption

sealed class FilterAction {
    data object OnBrandClick : FilterAction()
    data object OnModelClick : FilterAction()
    data object OnColorClick : FilterAction()
    data object OnYearClick : FilterAction()
    data class OnAddedByClick(val newValue: AddedBy) : FilterAction()

    data object OnDoneClick : FilterAction()
    data object OnBackClick : FilterAction()
    //TODO
}

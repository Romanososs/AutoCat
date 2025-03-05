package pro.aliencat.autocat.ui.search.filter.list

import pro.aliencat.autocat.ui.search.model.StringOption

sealed class FilterListAction{
    data class OnElementClick(val item: StringOption): FilterListAction()
    data object OnBackClick: FilterListAction()
}

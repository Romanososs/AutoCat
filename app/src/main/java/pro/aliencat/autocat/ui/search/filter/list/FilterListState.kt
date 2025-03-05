package pro.aliencat.autocat.ui.search.filter.list

import pro.aliencat.autocat.ui.search.model.StringOption

//data class FilterListState(
//    val activeOption: Int = 0,
//    val list: List<StringOption> = listOf(StringOption.Any)
//)

data class MainFilterInfoState(
    val brands: List<StringOption> = listOf(StringOption.Any),
    val models: List<StringOption> = listOf(StringOption.Any),
    val colors: List<StringOption> = listOf(StringOption.Any),
    val years: List<StringOption> = listOf(StringOption.Any)
)

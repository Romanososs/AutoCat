package pro.aliencat.autocat.paging

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pro.aliencat.autocat.models.common.Result
import pro.aliencat.autocat.models.common.onError
import pro.aliencat.autocat.models.common.onSuccess

abstract class PagingDataSource<I, O> {
    protected val scope = CoroutineScope(Dispatchers.IO)
    protected var searchJob: Job? = null
    protected var lastToken: String? = null

    protected val _pagingState: MutableStateFlow<PagingState<O>> = MutableStateFlow(PagingState())
    val pagingState: StateFlow<PagingState<O>> = _pagingState.asStateFlow()
    //FIXME could add cashing

    protected fun refresh(mapper: (I) -> O, getPage: suspend () -> Result<PagingDto<I>>) {
        searchJob?.cancel()
        searchJob = scope.launch {
            _pagingState.update { it.copy(state = it.state.copy(refresh = LoadState.Loading)) }
            getPage().onSuccess {
                lastToken = it.pageToken
                _pagingState.update { st ->
                    st.copy(
                        totalItems = it.count ?: 0,
                        state = st.state.copy(refresh = LoadState.Idle),
                        pages = listOf(Page(it.items.map(mapper)))
                    )
                }
            }.onError { _, _ ->
                _pagingState.update {
                    it.copy(
                        state = it.state.copy(refresh = LoadState.Error),
                    )
                }
            }
        }
    }

    protected fun reload(mapper: (I) -> O, getPage: suspend () -> Result<PagingDto<I>>) {
        searchJob?.cancel()
        searchJob = scope.launch {
            _pagingState.update { it.copy(state = it.state.copy(preload = LoadState.Loading)) }
            getPage().onSuccess {
                lastToken = it.pageToken
                _pagingState.update { st ->
                    st.copy(
                        totalItems = it.count ?: 0,
                        state = st.state.copy(preload = LoadState.Idle),
                        pages = listOf(Page(it.items.map(mapper)))
                    )
                }
            }.onError { _, _ ->
                _pagingState.update {
                    it.copy(
                        state = it.state.copy(preload = LoadState.Error),
                        pages = emptyList()
                    )
                }
            }
        }
    }

    protected fun append(mapper: (I) -> O, getPage: suspend () -> Result<PagingDto<I>>) {
        if (lastToken == null || _pagingState.value.state.append is LoadState.Loading) return
        searchJob?.cancel()
        searchJob = scope.launch {
            _pagingState.update { it.copy(state = it.state.copy(append = LoadState.Loading)) }
            getPage().onSuccess {
                lastToken = it.pageToken
                _pagingState.update { st ->
                    st.copy(
                        state = st.state.copy(append = LoadState.Idle),
                        pages = st.pages + Page(it.items.map(mapper))
                    )
                }
            }.onError { _, _ ->
                _pagingState.update {
                    it.copy(
                        state = it.state.copy(append = LoadState.Error),
                    )
                }
            }
        }
    }
}
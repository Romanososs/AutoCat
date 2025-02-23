package pro.aliencat.autocat.paging

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pro.aliencat.autocat.models.common.Result

abstract class PagingDataSource<I, O> {
    protected val scope = CoroutineScope(Dispatchers.IO)
    protected var searchJob: Job? = null
    protected var lastToken: String? = null

    protected val _pagingState: MutableStateFlow<PagingData<O>> = MutableStateFlow(PagingData())
    val pagingState: StateFlow<PagingData<O>> = _pagingState

    protected fun refresh(mapper: (I) -> O, getPage: suspend () -> Result<PagingDto<I>>) {
        searchJob?.cancel()
        searchJob = scope.launch {
            _pagingState.update { it.copy(state = it.state.copy(refresh = PageLoadState.Loading)) }
            when (val data = getPage()) {
                is Result.Error -> _pagingState.update {
                    it.copy(
                        state = it.state.copy(refresh = PageLoadState.Error),
                    )
                }

                is Result.Success -> {
                    lastToken = null
                    _pagingState.update {
                        it.copy(
                            totalItems = data.data.count ?: 0,
                            state = it.state.copy(refresh = PageLoadState.Idle),
                            data = data.data.items.map(mapper)
                        )
                    }
                }
            }
        }
    }

    protected fun reload(mapper: (I) -> O, getPage: suspend () -> Result<PagingDto<I>>) {
        searchJob?.cancel()
        searchJob = scope.launch {
            _pagingState.update { it.copy(state = it.state.copy(preload = PageLoadState.Loading)) }
            when (val data = getPage()) {
                is Result.Error -> _pagingState.update {
                    it.copy(
                        state = it.state.copy(preload = PageLoadState.Error),
                        data = emptyList()
                    )
                }

                is Result.Success -> {
                    lastToken = null
                    _pagingState.update {
                        it.copy(
                            totalItems = data.data.count ?: 0,
                            state = it.state.copy(preload = PageLoadState.Idle),
                            data = data.data.items.map(mapper)
                        )
                    }
                }
            }
        }
    }

    protected fun append(mapper: (I) -> O, getPage: suspend () -> Result<PagingDto<I>>) {
        searchJob?.cancel()
        searchJob = scope.launch {
            _pagingState.update { it.copy(state = it.state.copy(append = PageLoadState.Loading)) }
            when (val data = getPage()) {
                is Result.Error -> _pagingState.update {
                    it.copy(
                        state = it.state.copy(append = PageLoadState.Error),
                    )
                }

                is Result.Success -> {
                    lastToken = data.data.pageToken
                    _pagingState.update {
                        it.copy(
                            state = it.state.copy(append = PageLoadState.Idle),
                            data = it.data + data.data.items.map(mapper)
                        )
                    }
                }
            }
        }
    }

}
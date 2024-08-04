package com.bruno13palhano.hqsmarvel.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.cachedIn
import com.bruno13palhano.data.di.ComicsRep
import com.bruno13palhano.data.model.Comic
import com.bruno13palhano.data.repository.comics.ComicsRepository
import com.bruno13palhano.hqsmarvel.ui.common.RemotePresentationState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
    @Inject
    constructor(
        @ComicsRep private val comicsRepository: ComicsRepository
    ) : ViewModel() {
        val comics = comicsRepository.getComics().cachedIn(viewModelScope)

        fun updateFavorite(comic: Comic) {
            viewModelScope.launch {
                comicsRepository.updateComicFavorite(
                    id = comic.id,
                    isFavorite = !comic.isFavorite
                )
            }
        }

        private val _pagingUIState = MutableStateFlow<PagingUIState>(PagingUIState.MainCircularProgress)
        val pagingUIState: StateFlow<PagingUIState> = _pagingUIState

        private val remotePresentationState = MutableStateFlow(RemotePresentationState.INITIAL)

        /**
         * Set the load state value obtained from LazyPagingItems.
         *
         * @param loadState The combined load states from LazyPagingItems.
         */
        fun setLoadState(loadState: CombinedLoadStates) {
            when (remotePresentationState.value) {
                RemotePresentationState.PRESENTED -> {
                    when {
                        loadState.mediator?.refresh is LoadState.Loading -> {
                            setPagingUIState(
                                pagingUIState = PagingUIState.MainCircularProgress,
                                presentationState = RemotePresentationState.REMOTE_LOADING
                            )
                        }

                        loadState.mediator?.append is LoadState.Loading -> {
                            setPagingUIState(
                                pagingUIState = PagingUIState.ItemCircularProgress,
                                presentationState = RemotePresentationState.REMOTE_LOADING
                            )
                        }

                        else -> {
                            _pagingUIState.value = PagingUIState.Success
                        }
                    }
                }

                RemotePresentationState.INITIAL -> {
                    when {
                        loadState.mediator?.refresh is LoadState.Loading -> {
                            setPagingUIState(
                                pagingUIState = PagingUIState.MainCircularProgress,
                                presentationState = RemotePresentationState.REMOTE_LOADING
                            )
                        }

                        loadState.mediator?.append is LoadState.Loading -> {
                            setPagingUIState(
                                pagingUIState = PagingUIState.ItemCircularProgress,
                                presentationState = RemotePresentationState.REMOTE_LOADING
                            )
                        }

                        else -> {
                            _pagingUIState.value = PagingUIState.Success
                        }
                    }
                }

                RemotePresentationState.REMOTE_LOADING -> {
                    when {
                        loadState.source.refresh is LoadState.Loading ||
                            loadState.source.append is LoadState.Loading -> {
                            setPagingUIState(
                                pagingUIState = PagingUIState.Success,
                                presentationState = RemotePresentationState.SOURCE_LOADING
                            )
                        }

                        loadState.mediator?.refresh is LoadState.Error -> {
                            setPagingUIState(
                                pagingUIState = PagingUIState.RefreshError,
                                presentationState = RemotePresentationState.INITIAL
                            )
                        }

                        loadState.mediator?.append is LoadState.Error -> {
                            setPagingUIState(
                                pagingUIState = PagingUIState.AppendError,
                                presentationState = RemotePresentationState.INITIAL
                            )
                        }

                        else -> {}
                    }
                }

                RemotePresentationState.SOURCE_LOADING -> {
                    when {
                        loadState.source.refresh is LoadState.NotLoading -> {
                            setPagingUIState(
                                pagingUIState = PagingUIState.ItemCircularProgress,
                                presentationState = RemotePresentationState.PRESENTED
                            )
                        }

                        loadState.source.append is LoadState.NotLoading -> {
                            setPagingUIState(
                                pagingUIState = PagingUIState.MainCircularProgress,
                                presentationState = RemotePresentationState.PRESENTED
                            )
                        }

                        else -> {}
                    }
                }
            }
        }

        private fun setPagingUIState(
            pagingUIState: PagingUIState,
            presentationState: RemotePresentationState
        ) {
            _pagingUIState.value = pagingUIState
            remotePresentationState.value = presentationState
        }
    }

sealed class PagingUIState {
    data object Success : PagingUIState()

    data object MainCircularProgress : PagingUIState()

    data object ItemCircularProgress : PagingUIState()

    data object RefreshError : PagingUIState()

    data object AppendError : PagingUIState()
}
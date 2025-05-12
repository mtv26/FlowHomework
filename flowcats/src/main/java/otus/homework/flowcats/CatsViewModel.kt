package otus.homework.flowcats

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import otus.homework.flowcats.Result

class CatsViewModel(
    private val catsRepository: CatsRepository
) : ViewModel() {

    private val _cats = MutableStateFlow<Result>(
        Result.Success(
            Fact(
                "",
                false,
                "",
                "",
                "",
                false,
                "",
                "",
                ""
            )
        )
    )
    val cats = _cats.asStateFlow()

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                catsRepository.listenForCatFacts()
                    .catch {
                        _cats.value = Result.Error
                    }
                    .collect {
                        _cats.value = Result.Success(it)
                }
            }
        }
    }
}

class CatsViewModelFactory(private val catsRepository: CatsRepository) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        CatsViewModel(catsRepository) as T
}
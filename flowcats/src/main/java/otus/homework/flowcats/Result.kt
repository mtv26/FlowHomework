package otus.homework.flowcats

sealed class Result {
    data class Success<T>(val res: T): Result()
    data object Error: Result()
}
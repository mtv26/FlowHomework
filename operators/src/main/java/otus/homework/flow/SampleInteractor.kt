package otus.homework.flow

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlin.random.Random
import kotlin.random.nextInt

@ExperimentalCoroutinesApi
class SampleInteractor(
    private val sampleRepository: SampleRepository
) {

    /**
     * Реализуйте функцию task1 которая последовательно:
     * 1) умножает числа на 5
     * 2) убирает чила <= 20
     * 3) убирает четные числа
     * 4) добавляет постфикс "won"
     * 5) берет 3 первых числа
     * 6) возвращает результат
     */
    fun task1(): Flow<String> {
        val values = flowOf(7, 12, 4, 8, 11, 5, 7, 16, 99, 1)
        return values.transform {
            emit(it * 5)
        }.filter {
            it > 20 && it % 2 > 0
        }.transform {
            emit("${it} won")
        }.take(3)
    }

    /**
     * Классическая задача FizzBuzz с небольшим изменением.
     * Если входное число делится на 3 - эмитим само число и после него эмитим строку Fizz
     * Если входное число делится на 5 - эмитим само число и после него эмитим строку Buzz
     * Если входное число делится на 15 - эмитим само число и после него эмитим строку FizzBuzz
     * Если число не делится на 3,5,15 - эмитим само число
     */
    fun task2(): Flow<String> {
        val values = (1..21).asFlow()
        return values.transform<Int, String> {
            when {
                (it % 15) == 0 -> {
                    emit("$it")
                    emit("FizzBuzz")
                }
                (it % 5) == 0 -> {
                    emit("$it")
                    emit("Buzz")
                }
                (it % 3) == 0 -> {
                    emit("$it")
                    emit("Fizz")
                }
                else -> {
                    emit("$it")
                }
            }
        }
    }

    /**
     * Реализуйте функцию task3, которая объединяет эмиты из двух flow и возвращает кортеж Pair<String,String>(f1,f2),
     * где f1 айтем из первого флоу, f2 айтем из второго флоу.
     * Если айтемы в одно из флоу кончились то результирующий флоу также должен закончится
     */
    fun task3(): Flow<Pair<String, String>> {
        val values = flowOf(
            "Red",
            "Green",
            "Blue",
            "Black",
            "White"
        )
        val values1 = flowOf("Circle", "Square", "Triangle")
        return values.zip(values1) { v, v1 ->
            v to v1
        }
    }

    /**
     * Реализайте функцию task4, которая обрабатывает IllegalArgumentException и в качестве фоллбека
     * эмитит число -1.
     * Если тип эксепшена != IllegalArgumentException, пробросьте его дальше
     * При любом исходе, будь то выброс исключения или успешная отработка функции вызовите метод dotsRepository.completed()
     */
    fun task4(): Flow<Int> {
        val values = flow {
            (1..10).forEach {
                emit(it)
            }
        }
        return values.catch {
            if (it is IllegalArgumentException) {
                throw it
            } else {
                emit(-1)
            }
        }.onCompletion {
            sampleRepository.completed()
        }
    }

    fun task4IllegalArgumentException(): Flow<Int> {
        val values = flow {
            (1..10).forEach {
                if (it == 5) {
                    throw IllegalArgumentException("Failed")
                } else {
                    emit(it)
                }
            }
        }
        return values.catch {
            if (it !is IllegalArgumentException) {
                throw it
            } else {
                emit(-1)
            }
        }.onCompletion {
            sampleRepository.completed()
        }
    }

    fun task4NotIllegalArgumentException(): Flow<Int> {
        val values = flow {
            (1..10).forEach {
                if (it == 5) {
                    throw SecurityException("Security breach")
                } else {
                    emit(it)
                }
            }
        }
        return values.catch {
            if (it !is IllegalArgumentException) {
                throw it
            } else {
                emit(-1)
            }
        }.onCompletion {
            sampleRepository.completed()
        }
    }
}
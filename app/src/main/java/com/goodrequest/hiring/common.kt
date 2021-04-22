package com.goodrequest.hiring

import androidx.activity.ComponentActivity
import androidx.lifecycle.*
import com.goodrequest.hiring.cats.CatFact
import com.goodrequest.hiring.cats.CatImage
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.logging.HttpLoggingInterceptor
import ru.gildor.coroutines.okhttp.await
import java.io.Serializable

object CatsApi: Api {
    override val client =
        OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor()
                .apply { level = HttpLoggingInterceptor.Level.BODY })
            .build()
}

interface Api {
    val client: OkHttpClient

    suspend fun getCatImages(page: Int): Result<List<CatImage>> =
        client.httpGet("https://api.thecatapi.com/v1/images/search?limit=10&size=med&page=${page}&order=ASC".toHttpUrl())

    suspend fun getCatFact(): Result<CatFact> =
        client.httpGet("https://cat-fact.herokuapp.com/facts/random?animal_type=cat&amount=1".toHttpUrl())
}

private suspend inline fun <reified T> OkHttpClient.httpGet(url: HttpUrl): Result<T> =
    try {
        val good: Request = Request.Builder().url(url).get().build()
        val result = newCall(good).await()
        val data = Json { ignoreUnknownKeys = true }.decodeFromString<T>(result.body!!.string())
        Result.Success(data)
    } catch (e: Exception) {
        Result.Failure(e)
    }

sealed class Result<out V> {
    data class Success<out A> (val value: A): Result<A>(), Serializable
    data class Failure(val error: Throwable): Result<Nothing>(), Serializable
}

fun <V, V2> Result<V>.flatMap(func: (V) -> Result<V2>): Result<V2> = when(this) {
    is Result.Failure -> this
    is Result.Success -> func(value)
}

fun <V, V2> Result<V>.map(func: (V) ->  V2): Result<V2> = when(this) {
    is Result.Failure -> this
    is Result.Success -> Result.Success(func(value))
}

inline fun <reified VM: ViewModel> ComponentActivity.viewModel(crossinline create: (SavedStateHandle) -> VM) =
    ViewModelLazy(VM::class, { viewModelStore }) {
        object: AbstractSavedStateViewModelFactory(this, null) {
            override fun <T : ViewModel> create(key: String, type: Class<T>, handle: SavedStateHandle): T =
                create(handle) as T
        }
    }
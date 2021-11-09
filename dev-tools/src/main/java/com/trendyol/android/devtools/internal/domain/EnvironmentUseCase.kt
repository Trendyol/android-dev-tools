package com.trendyol.android.devtools.internal.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.trendyol.android.devtools.internal.data.EnvironmentRepository
import com.trendyol.android.devtools.internal.util.SingleLiveEvent
import com.trendyol.android.devtools.model.DefaultEnvironments
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers

internal class EnvironmentUseCase(private val repository: EnvironmentRepository) {

    private val environmentChangedLiveData: MutableLiveData<String> = SingleLiveEvent()

    fun getEnvironmentChangedLiveData(): LiveData<String> = environmentChangedLiveData

    fun getCurrentEnvironment(): String = repository.getCurrentEnvironment() ?: DefaultEnvironments.PRODUCTION.also {
        updateCurrentEnvironment(0)
    }

    fun updateCurrentEnvironment(environmentIndex: Int) {
        val environment = getEnvironments()[environmentIndex]
        if (environment.isEmpty() || environment.isBlank()) {
            throw IllegalArgumentException("Provided environment should not be empty or blank")
        }
        repository.updateCurrentEnvironment(environment)
        environmentChangedLiveData.value = environment
    }

    fun updateEnvironments(environments: List<String>) {
        repository.updateEnvironments(environments)
        updateCurrentEnvironment(0)
    }

    fun getEnvironmentPairs(): Observable<List<Pair<Boolean, String>>> =
        Observable.just(getCurrentEnvironment())
            .subscribeOn(Schedulers.io())
            .map { currentEnvironment ->
                getEnvironments().map { environment -> (environment == currentEnvironment) to environment }
            }

    private fun getEnvironments(): List<String> =
        repository.getEnvironments() ?: DefaultEnvironments.getAll().also { repository.updateEnvironments(it) }
}

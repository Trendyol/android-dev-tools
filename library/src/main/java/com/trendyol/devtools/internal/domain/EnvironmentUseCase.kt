package com.trendyol.devtools.internal.domain

import com.trendyol.devtools.internal.data.EnvironmentRepository
import com.trendyol.devtools.internal.di.ContextContainer
import com.trendyol.devtools.model.DefaultEnvironments

internal class EnvironmentUseCase(
    private val repository: EnvironmentRepository
) {

    fun getCurrentEnvironment(): String = repository.getCurrentEnvironment() ?: DefaultEnvironments.PRODUCTION.also {
        updateCurrentEnvironment(it)
    }

    fun updateCurrentEnvironment(environment: String) {
        if (environment.isEmpty() || environment.isBlank()) {
            throw IllegalArgumentException("Provided environment should not be empty or blank")
        }
        repository.updateCurrentEnvironment(environment)
    }

    companion object {

        fun getInstance(): EnvironmentUseCase =
            ContextContainer.getEnvironmentsContainer().provideGetCurrentEnvironmentUseCase()
    }
}

package com.trendyol.devtools.internal.domain

import com.trendyol.devtools.internal.data.EnvironmentRepository
import com.trendyol.devtools.model.DefaultEnvironments

internal class EnvironmentUseCase(private val repository: EnvironmentRepository) {

    var onEnvironmentChanged: ((String) -> Unit)? = null

    private var environments: List<String> = emptyList()
        get() {
            return if (field.isEmpty()) {
                (repository.getEnvironments() ?: DefaultEnvironments.getAll()).also { environments = it }
            } else {
                field
            }
        }

    fun getCurrentEnvironment(): String = repository.getCurrentEnvironment() ?: DefaultEnvironments.PRODUCTION.also {
        updateCurrentEnvironment(0)
    }

    fun updateCurrentEnvironment(environmentIndex: Int) {
        val environment = environments[environmentIndex]
        if (environment.isEmpty() || environment.isBlank()) {
            throw IllegalArgumentException("Provided environment should not be empty or blank")
        }
        repository.updateCurrentEnvironment(environment)
        onEnvironmentChanged?.invoke(environment)
    }

    fun updateEnvironments(environments: List<String>) {
        if (this.environments == environments) return

        this.environments = environments
        repository.updateEnvironments(environments)
        updateCurrentEnvironment(0)
    }

    fun getEnvironmentPairs(): List<Pair<Boolean, String>> {
        val currentEnvironment = getCurrentEnvironment()
        return environments.map { (it == currentEnvironment) to it }
    }
}

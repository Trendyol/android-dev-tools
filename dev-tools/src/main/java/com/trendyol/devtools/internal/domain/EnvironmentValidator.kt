package com.trendyol.devtools.internal.domain

internal object EnvironmentValidator {

    fun validateEnvironments(environments: List<String>) {
        if (environments.isEmpty()) {
            throw IllegalArgumentException("Given environments should not be empty.")
        } else {
            environments.forEach { environment ->
                if (environment.isEmpty() || environment.isBlank()) {
                    throw IllegalArgumentException("Provided environment should not be empty or blank.")
                }
            }
        }
        if (environments.distinct().size != environments.size) {
            throw java.lang.IllegalArgumentException("Environment name should not be repeated.")
        }
    }
}

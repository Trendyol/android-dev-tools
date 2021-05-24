package com.trendyol.devtools.internal.domain

internal object EnvironmentValidator {

    fun validateEnvironments(environments: List<String>) {
        if (environments.isEmpty()) {
            throw IllegalArgumentException("Given environments should not be empty.")
        }
        if (environments.any { it.isBlank() }) {
            throw IllegalArgumentException("Provided environment should not be empty or blank.")
        }
        if (environments.distinct().size != environments.size) {
            throw IllegalArgumentException("Environment name should not be repeated.")
        }
    }
}

package com.trendyol.android.devtools.plugins.publish

open class PublishConfiguration {
    var artifactId: String = ""
    var description: String = ""
    var url: String = ""
    var license: LicenseConfig? = null
    var developers: List<DeveloperConfig> = emptyList()
    var scm: ScmConfig? = null

    fun license(name: String, url: String) {
        license = LicenseConfig(name, url)
    }

    fun developer(id: String, name: String, email: String) {
        developers = developers + DeveloperConfig(id, name, email)
    }

    fun scm(connection: String, developerConnection: String, url: String) {
        scm = ScmConfig(connection, developerConnection, url)
    }
}

data class DeveloperConfig(
    val id: String,
    val name: String,
    val email: String
)

data class ScmConfig(
    val connection: String,
    val developerConnection: String,
    val url: String
)
data class LicenseConfig(
    val name: String,
    val url: String
)

fun PublishConfiguration.defaultConfiguration(
    artifactId: String,
    description: String,
): PublishConfiguration {
    this.artifactId = artifactId
    this.description = description
    url = "https://github.com/Trendyol/android-dev-tools"

    license("Android DevTools License", "https://github.com/Trendyol/android-dev-tools/blob/master/LICENSE")

    developer("erkutaras", "Erkut Aras", "erkut.aras@trendyol.com")
    developer("muratcanbur", "Murat Can Bur", "muratcan.bur@trendyol.com")
    developer("MertNYuksel", "Mert Nevzat Yüksel", "mert.yuksel@trendyol.com")
    developer("bilgehankalkan", "Bilgehan Kalkan", "bilgehan.kalkan@trendyol.com")
    developer("ibrahimsn98", "İbrahim Süren", "ibrahim.suren@trendyol.com")
    developer("mucahidkambur", "Mücahid Kambur", "mucahid.kambur@trendyol.com")

    scm(
        connection = "scm:git:github.com/Trendyol/android-dev-tools.git",
        developerConnection = "scm:git:ssh://github.com/Trendyol/android-dev-tools.git",
        url = "https://github.com/Trendyol/android-dev-tools/tree/main"
    )
    return this
}

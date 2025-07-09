package com.trendyol.android.devtools.plugins.publish
import com.vanniktech.maven.publish.MavenPublishBaseExtension
import com.vanniktech.maven.publish.MavenPublishPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project

class PublishConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        plugins.apply(MavenPublishPlugin::class.java)

        val publishConfig = extensions.create("publishConfig", PublishConfiguration::class.java)

        extensions.configure<MavenPublishBaseExtension>("mavenPublishing") {
            publishToMavenCentral(automaticRelease = false)
            pom {
                name.set(publishConfig.artifactId.takeIf { it.isNotEmpty() } ?: project.name)
                description.set(publishConfig.description)
                url.set(publishConfig.url)

                publishConfig.license?.let { licenseConfig ->
                    licenses {
                        license {
                            name.set(licenseConfig.name)
                            url.set(licenseConfig.url)
                        }
                    }
                }

                if (publishConfig.developers.isNotEmpty()) {
                    developers {
                        publishConfig.developers.forEach { dev ->
                            developer {
                                id.set(dev.id)
                                name.set(dev.name)
                                email.set(dev.email)
                            }
                        }
                    }
                }

                publishConfig.scm?.let { scmConfig ->
                    scm {
                        connection.set(scmConfig.connection)
                        developerConnection.set(scmConfig.developerConnection)
                        url.set(scmConfig.url)
                    }
                }
            }
            signAllPublications()
        }
    }
}

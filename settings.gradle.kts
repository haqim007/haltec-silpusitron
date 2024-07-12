pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "SILPUSITRON"
include(":app")
include(":app-petugas")
include(":core:data")
include(":core:di")
include(":core:domain")
include(":core:ui")
include(":feature:landingpage")
include(":feature:auth")
include(":feature:dashboard")
include(":feature:submission")
include(":core:common")
include(":feature:home")
include(":feature:user")
include(":shared")
include(":shared:district")
include(":shared:form")
include(":shared:auth")
include(":feature:requirementdocs")

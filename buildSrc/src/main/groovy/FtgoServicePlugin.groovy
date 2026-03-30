import org.gradle.api.Plugin
import org.gradle.api.Project

class FtgoServicePlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {

        project.dependencies {

            implementation "org.springframework.boot:spring-boot-starter-data-redis"
            implementation 'org.redisson:redisson:3.21.0'
            implementation "org.springdoc:springdoc-openapi-starter-webmvc-ui"

        }

    }
}

import org.gradle.api.Plugin
import org.gradle.api.Project

class FtgoServicePlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {

        project.apply(plugin: 'org.springframework.boot')


        project.dependencies {

            implementation "org.springframework.cloud:spring-cloud-starter-sleuth"
            implementation "org.springframework.cloud:spring-cloud-sleuth-zipkin"
            implementation "io.eventuate.tram.springcloudsleuth:eventuate-tram-spring-cloud-sleuth-tram-starter"

            compile "org.springframework.data:spring-data-redis"
            compile 'io.lettuce:lettuce-core:5.1.4.RELEASE'
            implementation 'org.redisson:redisson:3.21.0'
            implementation "org.springdoc:springdoc-openapi-ui"


        }

    }
}

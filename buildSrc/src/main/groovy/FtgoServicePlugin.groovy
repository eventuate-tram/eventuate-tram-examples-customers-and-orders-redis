import org.gradle.api.Plugin
import org.gradle.api.Project

class FtgoServicePlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {

        project.apply(plugin: 'org.springframework.boot')
    	project.apply(plugin: "io.spring.dependency-management")

        project.dependencyManagement {
            imports {
                mavenBom "org.springframework.cloud:spring-cloud-sleuth:${project.ext.springCloudSleuthVersion}"
            }
        }


        project.dependencies {

            compile 'org.springframework.cloud:spring-cloud-starter-sleuth'
            compile 'org.springframework.cloud:spring-cloud-starter-zipkin'
            compile 'io.zipkin.brave:brave-bom:4.17.1'

            compile "io.eventuate.tram.core:eventuate-tram-spring-cloud-sleuth-integration"

            compile "org.springframework.data:spring-data-redis:${project.ext.springBootVersion}"
            compile 'io.lettuce:lettuce-core:6.1.2.RELEASE'
            
        }

    }
}

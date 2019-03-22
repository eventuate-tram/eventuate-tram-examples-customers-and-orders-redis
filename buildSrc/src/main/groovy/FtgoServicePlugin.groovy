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

            compile "io.eventuate.tram.core:eventuate-tram-spring-cloud-sleuth-integration:${project.ext.eventuateTramVersion}"

            compile "org.springframework.data:spring-data-redis:2.2.0.M1"
            compile 'io.lettuce:lettuce-core:5.1.4.RELEASE'
            
        }

    }
}

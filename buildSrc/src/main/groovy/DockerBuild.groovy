import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Exec

class DockerBuild implements Plugin<Project> {

    @Override
    void apply(Project project) {

      project.task('dockerBuild', type:Exec, dependsOn: ":${project.name}:assemble") {
        workingDir project.projectDir
        commandLine 'docker', 'build', '-t', project.name + ":" + project.ext.dockerImageTag, '.'
      }

    }
}

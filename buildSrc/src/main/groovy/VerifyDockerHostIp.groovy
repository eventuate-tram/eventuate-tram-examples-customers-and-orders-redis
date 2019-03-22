import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Exec

class VerifyDockerHostIp implements Plugin<Project> {

    @Override
    void apply(Project project) {
      project.task('verifyDockerHostIp', type:Exec) {
        workingDir project.projectDir
        commandLine 'docker', 'run', '-p', '8889:8888', '-e', 'DOCKER_DIAGNOSTICS_PORT=8889', '-e', "DOCKER_HOST_IP=" + System.getenv("DOCKER_HOST_IP"),
            '--rm', 'eventuateio/eventuateio-docker-networking-diagnostics:0.2.0.RELEASE'
      }

    }
}

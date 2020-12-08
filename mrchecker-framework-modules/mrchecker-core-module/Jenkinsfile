env.module = "${JOB_NAME}".split('/')[1]
env.workspace = "mrchecker-framework-modules"
pipeline{
    agent {docker {image 'mrchecker/mrchecker:v1.1.3'}}
    stages{
        stage('Run integration tests'){
          steps{
            script{
                sh """
                cd ${env.workspace}
                mvn -q clean jacoco:prepare-agent verify -Denv=QA -Dgroups=UnitTest,IntegrationTest --projects ${env.module}
                """
            }
          }
        }
    }
    post{
      always{
        allure includeProperties: false, jdk: '', report: "${env.workspace}/${env.module}/target/allure-report", results: [[path: "${env.workspace}/${env.module}/target/allure-results"]]
        archiveArtifacts artifacts: "${env.workspace}/${env.module}/target/*.jar", fingerprint: true
        jacoco()
        junit testResults:'**/surefire-reports/*.xml', healthScaleFactor: 0.8 //as I set the junit treshold to 80%
      }
    }
}
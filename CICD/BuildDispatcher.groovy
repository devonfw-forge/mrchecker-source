try{
    node('master'){
        stage('See if there is Jenkinsfile'){
            checkout scm
        }
        load 'CICD/Build_Jenkinsfile'
    }
} catch (Exception e){
    stage("Fall back to default"){
        ansiColor{
            echo """
\u001b[37m
\u001b[41;1m
            This is only default script. That means you're branch has no correct            
            jenkins file for this job. If any customization is needed fix this.             
\u001b[0m
            """
        }
    }
} finally {
    configFileProvider([configFile(fileId: 'buildDefault', variable: 'buildDefault')]) {
        load buildDefault
    }
}

//https://github.com/jenkinsci/pipeline-multibranch-defaults-plugin/blob/master/README.md
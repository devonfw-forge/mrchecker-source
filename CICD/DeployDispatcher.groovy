try{
    node('master'){
        stage('See if there is Jenkinsfile'){
            checkout scm
        }
        load 'CICD/Deploy_Jenkinsfile'
    }
} catch (Exception e){
    stage("Fall back to default"){
        ansiColor{
            print (\'\'\'
            This is only default script. That means you're branch has no correct            
            jenkins file for this job. If any customization is needed fix this.             
            \'\'\')
        }
    }
} finally {
    configFileProvider([configFile(fileId: 'deployDefault', variable: 'deployDefault')]) {
        load deployDefault
    }
}
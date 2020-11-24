try{
    node('master'){
        stage('See if there is Jenkinsfile'){
            checkout scm
        }
        def module = "${JOB_NAME}".split('/')[1]
        load "mrchecker-framework-modules/${module}/Jenkinsfile" //what about that it
    }
} catch (java.nio.file.NoSuchFileException e){
    stage("Fall back to default"){
        ansiColor{
            print('Exception ${e}')
            print (\'\'\'
            This is only default script. That means you're branch has no correct            
            jenkins file for this job. If any customization is needed fix this.             
            \'\'\')
             node('master'){
                configFileProvider([configFile(fileId: 'testDefault', variable: 'testDefault')]) {
                load testDefault
                }
            }
        }
    }
} finally {
   
}
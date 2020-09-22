try{
    node('master'){
        stage('See if there is Jenkinsfile'){
            checkout scm
        }
        def module = "${JOB_NAME}".split('/')[1]
        load "mrchecker-framework-modules/${module}/Jenkinsfile" //what about that it
    }
} catch (Exception e){
    stage("Fall back to default"){
        ansiColor{
            print ("""
\u001b[37m
\u001b[41;1m
            This is only default script. That means you're branch has no correct            
            jenkins file for this job. If any customization is needed fix this.             
\u001b[0m
            """)
        }
    }
} finally {
    configFileProvider([configFile(fileId: 'testDefault', variable: 'testDefault')]) {
       load testDefault
    }
}
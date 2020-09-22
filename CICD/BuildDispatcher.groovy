try{
	node('master'){
		stage('See if there is Jenkinsfile'){
			checkout scm
		}
		load 'CICD/Build_Jenkinsfile'
	}
} catch (Exception e){
	throw e
} finally {
	stage("Fall back to default"){
	}
}
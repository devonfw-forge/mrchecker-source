try{
	node('master'){
		stage('See if there is Jenkinsfile'){
			checkout scm
		}
		load 'CICD/Deploy_Jenkinsfile'
	}
} catch (Exception e){
	throw e
} finally {
	stage("Fall back to default"){
	}
}
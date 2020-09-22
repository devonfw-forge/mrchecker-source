try{
	node('master'){
		stage('See if there is Jenkinsfile'){
			checkout scm
		}
		load 'mrchecker-framework-modules/${it}/Jenkinsfile' //what about that it
	}
} catch (Exception e){
	throw e
} finally {
	stage("Fall back to default"){
	}
}
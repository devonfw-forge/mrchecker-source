# Docker Image
This docker image is meant to be a easy way to have unified jenkins worker.

## How to build
`docker build -t mrchecker/mrchecker:v1.1.3`

*Important remarks*

You don't need to push image anywhere if you build it on machine running jenkins.
After setting the version up make sure you upgraded all the Jenkinsfiles in the project to match newest version.

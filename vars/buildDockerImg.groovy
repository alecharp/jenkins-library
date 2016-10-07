#!groovy

def call(def imageName) {
  properties([
    buildDiscarder(logRotator(artifactDaysToKeepStr: '', artifactNumToKeepStr: '', daysToKeepStr: '', numToKeepStr: '10')),
    pipelineTriggers([[$class: 'PeriodicFolderTrigger', interval: '2m']])
  ])

  node {
    stage('Checkout') {
      checkout scm
      sh 'git rev-parse --short HEAD > GIT_COMMIT'
      short_commit = readFile('GIT_COMMIT').trim()
      currentBuild.description = "${short_commit}"
    }

    stage('Build') {
      image = docker.build("${imageName}:${short_commit}")
    }

    stage('Publish') {
      image.push 'latest'
      image.push "${short_commit}"
    }
  }
}

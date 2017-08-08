#!/usr/bin/env groovy

def call(def imageName) {
  properties([
    buildDiscarder(logRotator(numToKeepStr: '10'))
  ])

  node {
    stage('Checkout') {
      checkout scm
      short_commit = getGitCommit()
      currentBuild.description = "${imageName}:${short_commit}"
    }

    stage('Build') {
      image = docker.build("${imageName}:${short_commit}", "--squash .")
    }

    stage('Publish') {
      image.push "${short_commit}"
      milestone label: 'Do not push an old latest image'
      image.push 'latest'
    }
  }
}

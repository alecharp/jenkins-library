#!groovy

def call() {
  sh 'git rev-parse --short HEAD > GIT_COMMIT'
  def shortCommit = readFile('GIT_COMMIT').trim()
  return shortCommit
}

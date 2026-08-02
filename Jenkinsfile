pipeline {
    agent any

    environment {
        IMAGE_NAME = "pkistrying/receiver-server"
    }

    stages {

        stage('Build JAR') {
            steps {
                bat 'mvnw.cmd clean package'
            }
        }

        stage('Archive JAR') {
            steps {
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            }
        }

        stage('Build Docker Image') {
            steps {
                bat 'docker build -t %IMAGE_NAME%:%BUILD_NUMBER% .'
                bat 'docker tag %IMAGE_NAME%:%BUILD_NUMBER% %IMAGE_NAME%:latest'
            }
        }

        stage('Test Credentials') {
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: 'dockerhub-creds',
                        usernameVariable: 'DOCKER_USER',
                        passwordVariable: 'DOCKER_PASS'
                    )
                ]) {
                    powershell '''
                    Write-Host "Username from Jenkins:"
                    Write-Host $env:DOCKER_USER
                    whoami

                    Write-Host "Password length:"
                    Write-Host $env:DOCKER_PASS.Length
                    $env:USERPROFILE

                    docker context ls

                    docker version
                    '''
                }
            }
        }
        
        stage('Push Docker Image') {
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: 'dockerhub-creds',
                        usernameVariable: 'DOCKER_USER',
                        passwordVariable: 'DOCKER_PASS'
                    )
                ]) {
                    powershell '''
                    $pass = $env:DOCKER_PASS
                    $pass | docker login -u $env:DOCKER_USER --password-stdin

                    docker push "${env:IMAGE_NAME}:${env:BUILD_NUMBER}"
                    docker push "${env:IMAGE_NAME}:latest"

                    docker logout
                    '''
                }
            }
        }
    }
}
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
        
        stage('Push Docker Image') {
            environment {
                DOCKER_CONFIG = "${WORKSPACE}\\.docker"
            }
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: 'dockerhub-creds',
                        usernameVariable: 'DOCKER_USER',
                        passwordVariable: 'DOCKER_PASS'
                    )
                ]) {
                    bat '''
                    echo === DOCKER_CONFIG is: %DOCKER_CONFIG% ===
                    if not exist "%DOCKER_CONFIG%" mkdir "%DOCKER_CONFIG%"

                    echo === Attempting login ===
                    echo %DOCKER_PASS%| docker login -u %DOCKER_USER% --password-stdin
                    echo === Login exit code: %ERRORLEVEL% ===

                    echo === Contents of config.json ===
                    type "%DOCKER_CONFIG%\\config.json"

                    echo === Attempting push ===
                    docker push "%IMAGE_NAME%:latest"
                    echo === Push exit code: %ERRORLEVEL% ===
                    '''
                }
            }
        }
    }
}
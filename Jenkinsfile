pipeline {
    agent any
    environment {
        JAVA_HOME = 'C:\\Program Files\\Java\\jdk-18.0.2.1'
        ANDROID_HOME = 'C:\\Users\\henry\\AppData\\Local\\Android\\Sdk'
        PATH = "${env.JAVA_HOME}\\bin;${env.ANDROID_HOME}\\cmdline-tools\\latest\\bin;${env.ANDROID_HOME}\\platform-tools;${env.PATH}"
    }
    stages {
        stage('Checkout') {
            steps {
                git 'https://github.com/guicarneiro11/GoniometroApp.git'
            }
        }
        stage('Build') {
            steps {
                bat './gradlew clean build'
            }
        }
        stage('Test') {
            steps {
                junit '**/build/test-results/test/*.xml'
            }
        }

        stage('Assemble APK') {
            steps {
                bat './gradlew assembleRelease'
            }
        }
    }

    post {
        always {
            junit 'app/build/test-results/testDebugUnitTest/*.xml'
            archiveArtifacts artifacts: 'app/build/outputs/apk/release/*.apk', fingerprint: true
        }
    }
}

pipeline {
    agent any

    environment {
        JAVA_HOME = 'C:\\Program Files\\Java\\jdk-18.0.2.1'
        ANDROID_HOME = "${env.JENKINS_HOME}/tools/android-sdk"
        PATH = "${env.JAVA_HOME}\\bin;${env.ANDROID_HOME}/cmdline-tools/latest/bin;${env.PATH}"
    }

    stages {
        stage('Checkout') {
            steps {
                git credentialsId: '2c2a51ed-cb69-4369-acdf-d362b4bf2829', url: 'https://github.com/guicarneiro11/GoniometroApp.git'
            }
        }

        stage('Build') {
            steps {
                bat './gradlew clean build'
            }
        }

        stage('Test') {
            steps {
                bat './gradlew test'
                bat './gradlew connectedAndroidTest'
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

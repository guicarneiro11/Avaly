pipeline {
    agent any

    environment {
        ANDROID_HOME = "${env.JENKINS_HOME}/tools/android-sdk"
        PATH = "${env.ANDROID_HOME}/cmdline-tools/latest/bin:${env.PATH}"
    }

    stages {
        stage('Checkout') {
            steps {
                git credentialsId: '71f2881e-c261-4194-930e-c214da6bf8c6', url: 'https://github.com/guicarneiro11/GoniometroApp'
            }
        }

        stage('Build') {
            steps {
                sh './gradlew clean build'
            }
        }

        stage('Test') {
            steps {
                sh './gradlew test'
                sh './gradlew connectedAndroidTest'
            }
        }

        stage('Assemble APK') {
            steps {
                sh './gradlew assembleRelease'
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

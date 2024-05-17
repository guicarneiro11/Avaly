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
                git credentialsId: '2c2a51ed-cb69-4369-acdf-d362b4bf2829', url: 'https://github.com/guicarneiro11/GoniometroApp.git'
            }
        }
        stage('Setup Emulator') {
            steps {
                script {
                    sh 'echo no | android create avd --force -n test -t android-30 --abi x86_64'
                    sh 'emulator -avd test -no-skin -no-audio -no-window &'
                    sh 'adb wait-for-device'
                    sh 'adb shell input keyevent 82 &'
                }
            }
        }
        stage('Clean') {
            steps {
                bat './gradlew clean'
            }
        }
        stage('Build') {
            steps {
                bat './gradlew assembleDebug'
            }
        }
        stage('Test') {
            steps {
                bat './gradlew connectedDebugAndroidTest'
            }
            post {
                always {
                    junit '**/build/test-results/testDebugUnitTest/*.xml'
                }
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
            archiveArtifacts artifacts: '**/build/outputs/apk/release/*.apk', allowEmptyArchive: true
        }
    }
}

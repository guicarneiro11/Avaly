pipeline {
    agent any

    environment {
        JAVA_HOME = 'C:\\Program Files\\Java\\jdk-18.0.2.1'
        ANDROID_HOME = 'C:\\Users\\henry\\AppData\\Local\\Android\\Sdk'
        PATH = "${env.JAVA_HOME}\\bin;${env.ANDROID_HOME}\\cmdline-tools\\latest\\bin;${env.ANDROID_HOME}\\platform-tools;${env.PATH}"
    }

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

        stage('Start Emulator') {
            steps {
                bat 'echo no | avdmanager create avd -n test -k "system-images;android-29;google_apis;x86"'
                bat 'emulator -avd test -no-window -no-audio -no-boot-anim &'
                bat 'adb wait-for-device'
                bat 'adb shell input keyevent 82'
            }
        }

        stage('Run Tests') {
            steps {
                bat './gradlew connectedAndroidTest'
            }
        }

    post {
        always {
            junit 'app/build/test-results/testDebugUnitTest/*.xml'
            archiveArtifacts artifacts: 'app/build/outputs/apk/release/*.apk', fingerprint: true
        }
    }
}

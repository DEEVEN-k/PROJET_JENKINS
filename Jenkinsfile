pipeline {
    agent any

    environment {
        JAVA_HOME = tool 'jdk17'
        PATH = "${JAVA_HOME}/bin:${env.PATH}"
    }

    stages {
        stage('📥 Checkout Code') {
            steps {
                git branch: 'main', url: 'https://github.com/DEEVEN-k/PROJET_JENKINS.git'
            }
        }

        stage('🛠️ Compile Sources') {
            steps {
                sh './gradlew clean build'
            }
        }

        stage('📦 Build JAR') {
            steps {
                sh './gradlew jar'
            }
        }

        stage('📦 Build .deb (Linux)') {
            steps {
                sh './scripts/build_deb.sh'
            }
        }

        stage('📦 Build .exe (Windows)') {
            steps {
                sh './scripts/build_exe.sh'
            }
        }

        stage('🚀 Déploiement (CD)') {
            steps {
                echo 'Déploiement en cours...'
            }
        }
    }

    post {
        failure {
            echo '❌ Échec du pipeline.'
        }
        success {
            echo '✅ Pipeline terminé avec succès.'
        }
    }
}

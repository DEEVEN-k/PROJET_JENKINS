pipeline {
    agent any

    environment {
        JAVA_HOME = '/usr/lib/jvm/java-17-openjdk' // adapte si nécessaire
        PATH_TO_FX = '/opt/javafx-sdk-21/lib'
        APP_NAME = 'CalculatriceDEEVEN'
        VERSION = '1.0.0'
    }

    stages {
        stage('🔄 Clean Workspace') {
            steps {
                cleanWs()
            }
        }

        stage('📥 Checkout Code') {
            steps {
                git 'https://github.com/ton-utilisateur/ton-repo.git' // 🔁 adapte ton repo
            }
        }

        stage('🛠️ Compile Sources') {
            steps {
                sh '''
                mkdir -p build/classes
                javac --module-path $PATH_TO_FX --add-modules javafx.controls,javafx.fxml \
                      -d build/classes $(find src -name "*.java")
                '''
            }
        }

        stage('📦 Build JAR') {
            steps {
                sh '''
                mkdir -p build/jar
                jar --create --file=build/jar/${APP_NAME}.jar \
                    --main-class=com.example.calculatrice.CalculatriceApp \
                    -C build/classes .
                '''
            }
        }

        stage('📦 Build .deb (Linux)') {
            when {
                expression { isUnix() }
            }
            steps {
                sh '''
                mkdir -p deb/DEBIAN
                echo "Package: ${APP_NAME}
Version: ${VERSION}
Section: utils
Priority: optional
Architecture: all
Depends: default-jre
Maintainer: DEEVEN
Description: Calculatrice JavaFX avec interface moderne" > deb/DEBIAN/control

                mkdir -p deb/usr/local/${APP_NAME}
                cp build/jar/${APP_NAME}.jar deb/usr/local/${APP_NAME}/

                dpkg-deb --build deb ${APP_NAME}_${VERSION}.deb
                '''
            }
        }

        stage('📦 Build .exe (Windows)') {
            when {
                expression { isUnix() == false }
            }
            steps {
                bat '''
                jpackage ^
                    --name %APP_NAME% ^
                    --input build\\jar ^
                    --main-jar %APP_NAME%.jar ^
                    --main-class com.example.calculatrice.CalculatriceApp ^
                    --type exe ^
                    --icon icon.ico
                '''
            }
        }

        stage('🚀 Déploiement (CD)') {
            steps {
                script {
                    // 🔧 Remplace ce bloc par ton propre système de déploiement
                    // Exemples : scp, ftp, rsync ou copie vers un dossier partagé
                    echo "Déploiement des artefacts..."
                    sh '''
                    mkdir -p deployment_output
                    cp build/jar/*.jar deployment_output/
                    [ -f *.deb ] && cp *.deb deployment_output/ || true
                    '''
                    // Exemple de déploiement : rsync, scp, API REST...
                    // sh "scp *.deb user@serveur:/var/www/deploy/"
                }
            }
        }
    }

    post {
        success {
            echo '✅ Build et déploiement réussis !'
            archiveArtifacts artifacts: '**/*.jar,**/*.deb,**/*.exe', fingerprint: true
        }
        failure {
            echo '❌ Échec du pipeline.'
        }
    }
}

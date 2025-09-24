// Jenkinsfile that leverages Docker Compose for deployment
pipeline {
    agent any

    // Best practice: Define environment variables for the pipeline
    // Jenkins can inject credentials securely into these variables
    environment {
        // Example for accessing Jenkins credentials by ID
        CONFIG_REPO_CREDS = credentials('oyasumi Punpun')
    }

    stages {
        // Stage 1: Build all source code in parallel to create JARs and JS bundles 🏗️
        stage('Build Source Code') {
            parallel {
                stage('Build Frontend (Angular)') {
                    steps {
                        dir('buy-01-frontend') {
                            sh 'echo "Building the Angular frontend..."'
                            sh 'npm install'
                            sh 'npm run build'
                        }
                    }
                }
                // Build all Spring Boot services
                stage('Build Backend Services') {
                    // This builds all backend services in a single parallel step
                    parallel {
                        stage('API Gateway') { steps { dir('api-gateway') { sh 'mvn clean package -DskipTests' } } }
                        stage('Config Service') { steps { dir('config-service') { sh 'mvn clean package -DskipTests' } } }
                        stage('Discovery Service') { steps { dir('discovery-service') { sh 'mvn clean package -DskipTests' } } }
                        stage('Media Service') { steps { dir('media-service') { sh 'mvn clean package -DskipTests' } } }
                        stage('Product Service') { steps { dir('product-service') { sh 'mvn clean package -DskipTests' } } }
                        stage('User Service') { steps { dir('user-service') { sh 'mvn clean package -DskipTests' } } }
                    }
                }
            }
        }

        // Stage 2: Run tests (This is a placeholder for now)
        stage('Test') {
            steps {
                echo 'Running automated tests...'

                dir('api-gateway') { sh 'mvn test' }
                dir('config-service') { sh 'mvn test' }
                dir('discovery-service') { sh 'mvn test' }
                dir('media-service') { sh 'mvn test' }
                dir('product-service') { sh 'mvn test' }
                dir('user-service') { sh 'mvn test' }
            }
        }

        // Stage 3: Build all the Docker images using docker-compose 🐳
        // This command builds the images but does not run them.
        stage('Build Docker Images') {
            steps {
                echo 'Building all Docker images from their Dockerfiles...'
                // Pass the credentials to the docker-compose command
                sh """
                    export CONFIG_REPO_URI=${env.CONFIG_REPO_URI}
                    export CONFIG_REPO_USERNAME=${CONFIG_REPO_CREDS_USR}
                    export CONFIG_REPO_PASSWORD=${CONFIG_REPO_CREDS_PSW}
                    docker-compose build --parallel
                """
            }
        }

        // Stage 4: Deploy the entire application using Docker Compose 🚀
        // This will bring down old containers and start new ones in the correct order.
        stage('Deploy Application') {
            steps {
                echo 'Stopping any old containers and starting new ones...'
                // The 'up -d' command starts the containers in detached mode
                sh """
                    export CONFIG_REPO_URI=${env.CONFIG_REPO_URI}
                    export CONFIG_REPO_USERNAME=${CONFIG_REPO_CREDS_USR}
                    export CONFIG_REPO_PASSWORD=${CONFIG_REPO_CREDS_PSW}
                    docker-compose up -d
                 """
            }
        }
    }

    // The post block runs after all stages are completed
    post {
        always {
            // Clean up Docker images to save space
            echo 'Cleaning up dangling Docker images...'
            sh 'docker image prune -f'
        }
    }
}

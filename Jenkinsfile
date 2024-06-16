pipeline {
    agent any

    environment {
        dockerhub = credentials('dockerhub')
        RELEASE = "1.X.0"
        DOCKER_USER = "beatsbyalif"
        VERSION = "${RELEASE}-${env.BUILD_ID}"
    }

    tools {
        maven "Maven"
    }

    stages {
        stage('Maven Build') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Docker build and push') {
            steps {
                sh 'echo $dockerhub_PSW | docker login -u $dockerhub_USR --password-stdin'
                sh 'docker build -t beatsbyalif/mailer-service:${VERSION} .'
                sh 'docker push beatsbyalif/mailer-service:${VERSION}'
            }
        }

        stage('Clean workspace') {
            steps {
                deleteDir()
            }
        }

        stage('Update Image Tag in GitOps') {
            steps {
                checkout scmGit(branches: [[name: '*/main']], extensions: [], userRemoteConfigs: [[ credentialsId: 'git-ssh', url: 'git@github.com:byalif/alifthenic-k8s.git']])
                script {
                    sh '''
                        sed -i "s/image:.*/image: beatsbyalif\\/mailer-service:${VERSION}/" mail-service.yml
                        '''
                        sh 'git checkout main'
                        sh 'git add .'
                        sh 'git commit -m "Update image tag"'
                        sshagent(['git-ssh'])
                            {
                                sh('git push')
                            }
                }
            }
        }


        
    }
}
pipeline {
    agent any

    tools {
        jdk 'jdk17'
        maven 'maven3'
    }

    options {
        timestamps()
        timeout(time: 20, unit: 'MINUTES')
        disableConcurrentBuilds(abortPrevious: true)
    }

    parameters {
        choice(
                name: 'TEST_ENVIRONMENT',
                choices: ['qa', 'dev'],
                description: 'Target API environment')
        choice(
                name: 'TEST_SUITE',
                choices: ['sequential', 'parallel'],
                description: 'TestNG execution mode')
        choice(
                name: 'TEST_GROUP',
                choices: ['all', 'smoke', 'regression', 'negative', 'e2e'],
                description: 'TestNG group to execute')
        choice(
                name: 'LOG_LEVEL',
                choices: ['info', 'debug', 'warn'],
                description: 'Framework logging level')
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Test') {
            steps {
                script {
                    String suiteFile = params.TEST_SUITE == 'parallel'
                            ? 'src/test/resources/testng-parallel.xml'
                            : 'src/test/resources/testng.xml'
                    String groupOption = params.TEST_GROUP == 'all'
                            ? ''
                            : "-Dgroups=${params.TEST_GROUP}"

                    withCredentials([string(
                            credentialsId: 'gorest-api-token',
                            variable: 'GOREST_API_TOKEN')]) {
                        sh """
                            mvn --batch-mode --no-transfer-progress clean test \\
                                -Dtest.environment=${params.TEST_ENVIRONMENT} \\
                                -Dtest.suite=${suiteFile} \\
                                -Dlog.level=${params.LOG_LEVEL} \\
                                ${groupOption}
                        """
                    }
                }
            }
        }
    }

    post {
        always {
            junit(
                    testResults: 'target/surefire-reports/*.xml',
                    allowEmptyResults: true)

            archiveArtifacts(
                    artifacts: 'target/allure-results/**, target/surefire-reports/**, target/logs/**',
                    allowEmptyArchive: true,
                    fingerprint: true)

            script {
                if (fileExists('target/allure-results')) {
                    allure(
                            includeProperties: false,
                            results: [[path: 'target/allure-results']])
                }
            }
        }
    }
}

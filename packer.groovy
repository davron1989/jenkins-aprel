node {
    stage ('Pulling Repo') {
        git url: 'https://github.com/davron1989/packer.git'
    }
    stage ('Packer Valadate') {
        sh 'packer validate apache.json'
    }
    withCredentials([usernamePassword(credentialsId: 'jenkins-aws-access-key', passwordVariable: 'AWS_SECRET_ACCESS_KEY', usernameVariable: 'AWS_ACCESS_KEY_ID')]) {
        withEnv(["AWS_REGION=${aws_region_var}"]){
            stage ('Packer Build') {
                sh 'packer build apache.json'
            }
        }
    }
}


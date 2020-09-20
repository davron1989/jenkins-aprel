node {
    stage('Init') {
        withCredentials([sshUserPrivateKey(credentialsId: 'jenkins-master-ssh-key', keyFileVariable: 'SSHKEY', passphraseVariable: '', usernameVariable: 'SSHUSER')]) {
            sh 'ssh -o StrictHostKeyChecking=no -i $SSHKEY  root@159.203.106.166 yum install epel-release -y'
        }
    }
}
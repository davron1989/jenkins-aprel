properties([
    parameters([
        string(defaultValue: '', description: 'Please enter VM IP address', name: 'Node-IP', trim: true)
        ])
    ])
node {
    withCredentials([sshUserPrivateKey(credentialsId: 'jenkins-master-ssh-key', keyFileVariable: 'SSHKEY', passphraseVariable: '', usernameVariable: 'SSHUSER')]) {
        stage('Init') {    
            sh 'ssh -o StrictHostKeyChecking=no -i $SSHKEY  SSHUSER@${Node-IP} yum install epel-release -y'
        }
        stage('Instal Git') {
            sh 'ssh -o StrictHostKeyChecking=no -i $SSHKEY  SSHUSER@${Node-IP} yum install git -y'            
        }
        stage('Install Java'){
            sh 'ssh -o StrictHostKeyChecking=no -i $SSHKEY  SSHUSER@${Node-IP} yum install java-1.8.0-openjdk-devel -y'
        }
    }
}
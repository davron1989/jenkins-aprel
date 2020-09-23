properties([
    parameters([
        booleanParam(defaultValue: true, description: 'Do You want to apply?', name: 'terraform_apply '), 
        booleanParam(defaultValue: true, description: 'Do You want to destroy?', name: 'terraform_destroy')
    ])
])

node {
    stage("Pull Repo") {
        git url: 'https://github.com/davron1989/terraform-vpc.git'
    }
    withCredentials([usernamePassword(credentialsId: 'jenkins-aws-access-key', passwordVariable: 'AWS_SECRET_ACCESS_KEY', usernameVariable: 'AWS_ACCESS_KEY_ID')]) {
        stage("Terraform Init") {
            sh '''
                bash setenv.sh dev.tfvars
                export AWS_REGION=us-east-1
                terraform-0.13 init
                terraform-0.13 plan -var-file dev.tfvars
            '''
        }
        if (terraform_apply.toBoolean()){
            stage("Terraform Apply") {
            sh '''
                export AWS_REGION=us-east-1
                terraform-0.13 apply  -var-file dev.tfvars  -auto-approve
            '''
            }

        }

        else if (terraform_destroy.toBoolean()){
            stage("Terraform Destroy") {
            sh '''
                export AWS_REGION=us-east-1
                terraform-0.13 destroy  -var-file dev.tfvars  -auto-approve
            '''
            }

        }
        else (Unknowen){
            sh 'echo "Unknowen"'
        }
        
    }
}
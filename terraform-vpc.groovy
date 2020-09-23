properties([
    parameters([
        booleanParam(defaultValue: true, description: 'Do You want to apply?', name: 'terraform_apply'), 
        booleanParam(defaultValue: true, description: 'Do You want to destroy?', name: 'terraform_destroy'),
        choice(choices: ['dev', 'qa', 'prod'], description: 'In which environment do you want to run this script?', name: 'environment') 
    ])
])

def aws_region_var = ''

if (params.environment){
    aws_region_var == "us-east-1"
}
else if (params.environment){
    aws_region_var == "us-east-2"
}
else if (params.environment){
    aws_region_var == "us-west-2"
}

node {
    stage("Pull Repo") {
        cleanWs()
        git url: 'https://github.com/davron1989/terraform-vpc.git'
    }
    withCredentials([usernamePassword(credentialsId: 'jenkins-aws-access-key', passwordVariable: 'AWS_SECRET_ACCESS_KEY', usernameVariable: 'AWS_ACCESS_KEY_ID')]) {
        withEnv(["AWS_REGION=${aws_region_var}"]){
            
            stage("Terraform Init") {
                sh """
                    bash setenv.sh ${environment}.tfvars
                    terraform-0.13 init
                """
            }
            if (terraform_apply.toBoolean()) {
                stage("Terraform Apply") {
                    sh """
                        terraform-0.13 apply  -var-file ${environment}.tfvars  -auto-approve
                    """
                }
            }

            else if (terraform_destroy.toBoolean()){
                stage("Terraform Destroy") {
                    sh """
                        terraform-0.13 destroy  -var-file ${environment}.tfvars  -auto-approve
                    """
                }
            }
            else {
                stage("Terraform Plan"){
                    sh """
                        export AWS_REGION=${aws_region_var}
                        terraform-0.13 plan -var-file ${environment}.tfvars
                    """
                }
            }
        }
    }
}
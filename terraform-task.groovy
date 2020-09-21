node {
    stage("Pull Repo") {
        git branch: 'solution', url: 'https://github.com/ikambarov/terraform-task.git'
    }

    stage("Init") {
        sh 'echo "Terraform Init"'
    }

    stage("Terraform Apply") {
        sh 'echo "Terraform Apply"'
    }
}
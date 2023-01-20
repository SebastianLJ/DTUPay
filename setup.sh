#!/bin/bash
set -e

printf "\033[94mStep 1/5: Setup Maven\033[0m\n"
## Install maven
sudo apt-get install maven

## install docker
# Set up the repository
printf "\033[94mStep 2/5: Setup Docker (repository)\033[0m\n"
sudo apt-get update
sudo apt-get install ca-certificates curl gnupg lsb-release

sudo mkdir -p /etc/apt/keyrings
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /etc/apt/keyrings/docker.gpg

echo "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null

# Install the docker engine
printf "\033[94mStep 3/5: Setup Docker (Install docker engine/compose)\033[0m\n"
sudo apt-get update
sudo apt-get install docker-ce docker-ce-cli containerd.io docker-compose-plugin
sudo systemctl enable docker.service
sudo systemctl start docker.service

# Set up docker
printf "\033[94mStep 4/5: Setup Docker (Add current user to Docker group)\033[0m\n"
sudo groupadd docker
sudo gpasswd -a "$USER" docker
sudo usermod -aG docker "$USER"

printf "\033[94mStep 5/5: Finished setup\033[0m\n"
echo "Log out / restart to make sure user is in the docker group"
echo "TRY USING 'docker run hello-world'"
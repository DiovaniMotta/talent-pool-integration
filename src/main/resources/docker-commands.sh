#Instalar docker no WSL
sudo apt update
sudo apt install docker.io
sudo usermod -aG docker $USER

# Iniciar o docker instalado via WSL no windows
sudo service docker start

# Adicionar permissão para o usuario do WSL
groups
sudo usermod -aG docker $USER
exec su -l $USER

#Instalar docker-compose
sudo curl -L "https://github.com/docker/compose/releases/download/1.29.2/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose
docker-compose --version

# Atualize os pacotes
sudo apt update
sudo apt install -y ca-certificates curl gnupg lsb-release

# Adicione a chave GPG do Docker
sudo mkdir -p /etc/apt/keyrings
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | \
sudo gpg --dearmor -o /etc/apt/keyrings/docker.gpg

# Adicione o repositório do Docker
echo \
  "deb [arch=$(dpkg --print-architecture) \
  signed-by=/etc/apt/keyrings/docker.gpg] \
  https://download.docker.com/linux/ubuntu \
  $(lsb_release -cs) stable" | \
sudo tee /etc/apt/sources.list.d/docker.list > /dev/null

# Instale o Docker Engine
sudo apt update
sudo apt install -y docker-ce docker-ce-cli containerd.io

# Inicie o serviço do Docker (via script manual)
sudo service docker start

# Adicione seu usuário ao grupo docker para rodar sem sudo:
sudo usermod -aG docker $USER
newgrp docker
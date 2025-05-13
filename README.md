# Talent Pool Integration

Este projeto experimental utiliza **Debezium** e **Kafka Connect** para integrar um banco de dados **PostgreSQL** com o **AWS SQS**, capturando mudanças em tempo real através do mecanismo de **Change Data Capture (CDC)**.


## 🛠️ Requisitos necessários para execução do projeto

### 1. Criação da base de dados

Para executar o experimento, é necessário configurar o banco de dados PostgreSQL com as tabelas e dados iniciais. Siga os passos abaixo:

1. **Instale o PostgreSQL**: Certifique-se de que o PostgreSQL está instalado em seu ambiente. Você pode baixá-lo em [https://www.postgresql.org/download/](https://www.postgresql.org/download/).

2. **Crie um banco de dados**: Após a instalação, crie os dois novos de dados para o projeto. Por exemplo:

```bash
createdb talent_pool_db
createdb talent_assessments_db
```

3. **Execute os scripts SQL:** No diretório resources/scripts do repositório, você encontrará os scripts necessários para criar as tabelas e inserir os dados iniciais. Execute-os na ordem apropriada. Por exemplo:

```bash
psql -d talent_pool_db -f resources/scripts/create-tables-profile-assessments.sql
psql -d talent_assessments_db -f resources/scripts/create-tables-vacancy-management.sql
```
---

## 📌 Sumário

1. [O que é Change Data Capture (CDC)](#1-o-que-é-change-data-capture-cdc)
2. [Entendendo o Write-Ahead Logging (WAL) no PostgreSQL](#2-entendendo-o-write-ahead-logging-wal-no-postgresql)
3. [Localização dos arquivos WAL no PostgreSQL](#3-localização-dos-arquivos-wal-no-postgresql)
4. [O que é Debezium e como ele utiliza o CDC](#4-o-que-é-debezium-e-como-ele-utiliza-o-cdc)
5. [Configurando o PostgreSQL para replicação lógica via CDC](#5-configurando-o-postgresql-para-replicação-lógica-via-cdc)
6. [Parâmetros do `application.properties`](#6-parâmetros-do-applicationproperties)
7. [Explicação do Dockerfile](#7-explicação-do-dockerfile)
8. [Diagrama da Solução](#8-diagrama-da-solução)

---

## 1. O que é Change Data Capture (CDC)

**Change Data Capture (CDC)** é uma técnica que permite identificar e capturar alterações realizadas nos dados de um banco de dados, como inserções, atualizações e exclusões. Essas mudanças são então propagadas para sistemas downstream, possibilitando integrações em tempo real e sincronização de dados entre diferentes sistemas.

---

## 2. Entendendo o Write-Ahead Logging (WAL) no PostgreSQL

O **Write-Ahead Logging (WAL)** é um mecanismo do PostgreSQL que garante a integridade dos dados. Antes que qualquer modificação seja aplicada ao banco de dados, ela é registrada em um log de transações (WAL). Isso permite a recuperação de dados em caso de falhas e é fundamental para a replicação de dados.

---

## 3. Localização dos arquivos WAL no PostgreSQL

Os arquivos WAL são armazenados no diretório `pg_wal` dentro do diretório de dados do PostgreSQL. Por exemplo, se o diretório de dados do PostgreSQL for `/var/lib/postgresql/10/main`, os arquivos WAL estarão em `/var/lib/postgresql/10/main/pg_wal`.

---

## 4. O que é Debezium e como ele utiliza o CDC

**Debezium** é uma plataforma de código aberto para captura de dados em tempo real. Ele monitora os logs de transações dos bancos de dados (como o WAL no PostgreSQL) para detectar alterações e emite eventos correspondentes que podem ser consumidos por outros sistemas.

![Debezium.png](images%2FDebezium.png)

Para mais informações, accesse a documentação oficial do Debezium: [Debezium Documentation](https://debezium.io)

---

## 5. Configurando o PostgreSQL para replicação lógica via CDC

Para habilitar a replicação lógica no PostgreSQL:

1. **Editar o arquivo `postgresql.conf`**:

```ini
   wal_level = logical
   max_replication_slots = 4
   max_wal_senders = 4
   wal_keep_size = 512
```

2. **Permitir conexões de replicação**: No arquivo `pg_hba.conf`, adicione a seguinte linha para permitir conexões do usuário de replicação:

```conf
host    replication     replicator     0.0.0.0/0       md5
```

3. **Criar usuário com permissão de replicação:** Execute os comandos abaixo no PostgreSQL:

```sql
CREATE ROLE replicator WITH LOGIN REPLICATION PASSWORD 'sua_senha';
```

4. **Conceder permissões ao usuário:** O Debezium precisa de acesso de leitura às tabelas monitoradas. Execute:

```sql
GRANT SELECT ON ALL TABLES IN SCHEMA public TO replicator;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT SELECT ON TABLES TO replicator;
```

## 6. Parâmetros do `application.properties`:

O arquivo `application.properties` contém configurações essenciais para o funcionamento da aplicação Spring Boot. Alguns parâmetros comuns incluem:

```properties
# Configurações para acesso a base de dados que contém as informações do candidato
recruitment-process.datasource.host=jdbc:postgresql://localhost:5432/seu_banco
recruitment-process.datasource.username=seu_usuario
recruitment-process.datasource.password=sua_senha
recruitment-process.datasource.database=sua_base_dados
recruitment-process.datasource.port=5432

# Configurações para acesso a base de dados que contém os resultados dos testes
profile-assessments.datasource.host=jdbc:postgresql://localhost:5432/seu_banco
profile-assessments.datasource.username=seu_usuario
profile-assessments.datasource.password=sua_senha
profile-assessments.datasource.database=sua_base_dados
profile-assessments.datasource.port=5432

# Configurações do banco de dados
spring.datasource.url=jdbc:postgresql://localhost:5432/seu_banco
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
spring.datasource.driver-class-name=org.postgresql.Driver

# Configurações do JPA/Hibernate
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# Configurações do AWS SQS
aws.region=us-east-1
aws.sqs.queue-name=nome-da-sua-fila
aws.access-key-id=SEU_ACCESS_KEY
aws.secret-access-key=SEU_SECRET_KEY

# Configurações de logging
logging.level.org.springframework=INFO
logging.level.com.seu.pacote=DEBUG
```

## 7.📦 Arquivo `docker-compose.yaml`

Este arquivo `docker-compose.yaml` define e orquestra os serviços necessários para executar o projeto *Talent Pool Integration* em um ambiente de contêineres Docker. Ele facilita a configuração e execução dos componentes da aplicação de forma integrada e reproduzível.

### 🔧 Serviços Definidos

- **app**: Contêiner principal da aplicação, responsável por executar o serviço de integração de talentos. Ele é construído a partir do `Dockerfile` localizado no diretório raiz do projeto e expõe a porta 8080 para acesso externo.

- **db**: Serviço de banco de dados PostgreSQL utilizado pela aplicação para armazenar dados persistentes. Ele utiliza a imagem oficial do PostgreSQL e define variáveis de ambiente para configuração do banco de dados, como usuário, senha e nome do banco.

### ⚙️ Recursos e Configurações

- **Volumes**: O serviço `db` utiliza um volume nomeado `db-data` para persistência dos dados do banco de dados, garantindo que as informações não sejam perdidas entre reinicializações dos contêineres.

- **Redes**: Ambos os serviços estão conectados à rede padrão criada pelo Docker Compose, permitindo que se comuniquem entre si utilizando os nomes dos serviços como hostnames.

- **Dependências**: O serviço `app` depende do serviço `db`, garantindo que o banco de dados esteja pronto antes de iniciar a aplicação.

### ▶️ Como Utilizar

Para iniciar os serviços definidos no `docker-compose.yaml`, execute o seguinte comando na raiz do projeto:

```bash
docker-compose up
```

## 8. Diagrama da Solução

A arquitetura da solução é composta pelos seguintes componentes:

![Diagrama](https://uploaddeimagens.com.br/images/004/898/246/full/Diagrama_Talent_Pool_Integration.png?1747088368)

### **Descrição do Fluxo:**

1. **PostgreSQL**: As alterações nos dados são registradas nos logs de transação (WAL).

2. **Debezium**: Monitora os logs WAL e captura as alterações em tempo real.

3. **Kafka Connect**: Recebe os eventos do Debezium e os encaminha para a fila SQS.

4. **AWS SQS**: Armazena as mensagens para serem processadas por consumidores downstream.

## 9. Contato

Para qualquer dúvida ou sugestão, por favor entre em contato com [diovanimotta@gmail.com](mailto:diovanimotta@gmail.com).


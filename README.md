# Talent Pool Integration

Este projeto experimental utiliza **Debezium** e **Kafka Connect** para integrar um banco de dados **PostgreSQL** com o **AWS SQS**, capturando mudanças em tempo real através do mecanismo de **Change Data Capture (CDC)**.
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

![Debezium](data:image/png;base64,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)

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


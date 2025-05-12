# Debezium + PostgreSQL - Change Data Capture (CDC)

Este guia descreve os passos necessários para configurar o **Debezium** com um banco **PostgreSQL** para captura de mudanças (CDC).

---

## ✅ 1. Configuração do PostgreSQL

### a. Habilitar replicação lógica

No arquivo `postgresql.conf`, defina os seguintes parâmetros:

```conf
wal_level = logical
max_replication_slots = 4
max_wal_senders = 4
wal_keep_size = 512
```

### b. Permitir conexões de replicação

No arquivo `pg_hba.conf`, adicione a seguinte linha para permitir conexões do usuário de replicação:

```conf
host    replication     replicator     0.0.0.0/0       md5
```

### c. Criar usuário com permissão de replicação

Execute os comandos abaixo no PostgreSQL:

```sql
CREATE ROLE replicator WITH LOGIN REPLICATION PASSWORD 'sua_senha';
```

### d. Conceder permissões ao usuário

O Debezium precisa de acesso de leitura às tabelas monitoradas. Execute:

```sql
GRANT SELECT ON ALL TABLES IN SCHEMA public TO replicator;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT SELECT ON TABLES TO replicator;
```

# Courses API

API REST para gestao de cursos, desenvolvida com Spring Boot e arquitetura orientada a casos de uso (ports/adapters).

## Tecnologias

- Java 17
- Spring Boot 3.4.1
- Spring Web
- Spring Data JPA
- Bean Validation
- PostgreSQL
- OpenAPI/Swagger (springdoc)
- JUnit 5 + Mockito
- JaCoCo (relatorio de cobertura)

## Funcionalidades atuais

- Criar curso
- Listar cursos
- Atualizar curso
- Alternar status ativo/inativo de curso
- Deletar curso

## Estrutura do projeto

O projeto segue uma organizacao por modulo (`course`) e separacao por camadas:

- `controllers`: exposicao HTTP
- `application/ports/in`: contratos de entrada (use cases)
- `useCases`: regras de negocio
- `adapters/out/persistence`: acesso a dados com JPA
- `dto`: objetos de entrada e saida da API
- `exceptions`: tratamento global de excecoes

## Pre-requisitos

- JDK 17+
- Maven 3.9+
- PostgreSQL em execucao

## Configuracao

As configuracoes atuais estao em `src/main/resources/application.properties`:

```properties
spring.application.name=courses-api
spring.datasource.url=jdbc:postgresql://localhost:5436/courses_api
spring.datasource.username=admin
spring.datasource.password=admin
spring.jpa.hibernate.ddl-auto=update
```

Se necessario, ajuste URL, usuario e senha do banco para seu ambiente.

## Como executar

1. Clone o repositorio:

```bash
git clone <url-do-repositorio>
cd courses-api
```

2. Suba/garanta o PostgreSQL com as credenciais configuradas.

3. Execute a aplicacao:

```bash
./mvnw spring-boot:run
```

Ou com Maven instalado globalmente:

```bash
mvn spring-boot:run
```

A API sera iniciada em `http://localhost:8080` (porta padrao do Spring Boot).

## Documentacao da API

Com a aplicacao em execucao:

- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## Endpoints (Courses)

Base path: `/courses`

- `POST /courses/` - cria um curso
- `GET /courses/list` - lista todos os cursos
- `PUT /courses/{id}` - atualiza dados de um curso
- `PATCH /courses/{id}/active` - alterna status ativo/inativo
- `DELETE /courses/{id}` - remove um curso

### Exemplo de payload - Criacao

```json
{
  "name": "Java com Spring Boot",
  "description": "Curso completo de Spring Boot com foco em APIs REST.",
  "category": "Backend",
  "active": true
}
```

### Exemplo de payload - Atualizacao

```json
{
  "name": "Java com Spring Boot - Avancado",
  "description": "Atualizacao do curso com conteudo avancado.",
  "category": "Backend"
}
```

## Testes

Para executar os testes:

```bash
./mvnw test
```

## Cobertura de testes

O projeto usa JaCoCo. Apos rodar os testes, o relatorio pode ser acessado em:

- `target/site/jacoco/index.html`


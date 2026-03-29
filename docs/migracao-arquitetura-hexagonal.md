# Migração para arquitetura hexagonal — courses-api

Este documento descreve o passo a passo para evoluir a aplicação **courses-api** (Spring Boot, JPA, módulo `course` com controllers, use cases e `JpaRepository`) para uma organização em **portas e adaptadores** (arquitetura hexagonal), sem prescrever uma única “verdade absoluta” de nomenclatura — o importante é manter o núcleo independente de frameworks.

---

## 1. Estado atual em relação ao hexagonal

Pontos positivos:

- Regras de fluxo já estão em *use cases* separados do `CourseController`.

Pontos que impedem chamar o núcleo de “hexagonal”:

- Os casos de uso dependem **diretamente** de `CourseRepository` estendendo `JpaRepository` — a persistência Spring Data está acoplada à camada de aplicação.
- `CourseEntity` é **modelo JPA**; o domínio não deveria ser definido só por anotações de persistência.
- DTOs como `CreateCourseDTO` entram nos *use cases*, acoplando a aplicação ao contrato HTTP/Bean Validation do request.

**Objetivo:** domínio + aplicação **sem** depender de Spring Data, JPA, anotações web, etc. Frameworks ficam nos **adaptadores**.

---

## 2. Mapa de conceitos para este projeto

| Conceito hexagonal | Onde colocar (exemplo) |
|--------------------|-------------------------|
| **Domínio** | Entidade `Course` (sem `@Entity`), `CourseStatus`, regras (unicidade de nome, toggle de ativo, etc.). |
| **Porta de saída (driven)** | Interface `CoursePersistencePort` (ou nome equivalente) — **não** estende `JpaRepository`. |
| **Porta de entrada (driving)** | Interfaces dos casos de uso ou fachada de aplicação que o controller invoca. |
| **Adaptador HTTP** | `CourseController`, DTOs de request/response, conversão para comandos/resultados da aplicação. |
| **Adaptador JPA** | `CourseJpaEntity`, `SpringDataCourseRepository`, `CoursePersistenceAdapter` implementando a porta, mappers domínio ↔ JPA. |

O Spring Boot continua como **compositor**: injeta implementações das portas nos serviços de aplicação.

---

## 3. Estrutura: pacotes vs módulos Maven

### Opção A — Pacotes em um único módulo (recomendado para começar)

Exemplo de organização:

- `...course.domain`
- `...course.application.port.in` / `...course.application.port.out`
- `...course.application.usecase` (serviços de aplicação)
- `...course.adapter.in.web` (controllers)
- `...course.adapter.out.persistence` (JPA + adapter)

### Opção B — Multi-módulo Maven

Módulos como `domain`, `application`, `infrastructure`, `api` — fronteiras reforçadas no build, mais trabalho no `pom.xml`.

Sugestão: começar pela **Opção A**; evoluir para **Opção B** ou reforçar com **ArchUnit** (seção 9).

---

## 4. Passo a passo

### Passo 1 — Modelo de domínio puro

1. Criar classe `Course` (ou equivalente) **sem** `jakarta.persistence`.
2. Campos alinhados ao negócio: identificador, nome, descrição, categoria, estado ativo/inativo, datas se forem parte do contrato de negócio.
3. Concentrar comportamento no domínio quando fizer sentido (ex.: `toggleActive()`).
4. Exceções de domínio/aplicação **sem** referência a HTTP ou Spring Web.

### Passo 2 — Porta de saída (persistência)

1. Definir interface na aplicação, por exemplo:
   - `save(Course)`
   - `findById(UUID)`
   - `findByName(String)`
   - `findAll()` (ou consultas mais específicas no futuro)
   - `delete(Course)` ou `deleteById(UUID)`
2. A interface **não** estende `JpaRepository`.
3. Casos de uso dependem **apenas** dessa interface (injeção por construtor é o padrão mais claro).

### Passo 3 — Comandos e resultados da aplicação

1. Criar tipos de entrada/saída da aplicação (records ou classes), por exemplo:
   - `CreateCourseCommand`, `UpdateCourseCommand`
   - `CourseCreatedResult`, itens de listagem, etc.
2. Manter `CreateCourseDTO` / `UpdateCourseDTO` **só** no adaptador web; o controller converte DTO → comando.
3. Validação:
   - **Borda HTTP:** `@Valid` nos DTOs do controller.
   - **Núcleo:** regras de negócio (ex.: nome duplicado) nos *use cases* ou no domínio.

### Passo 4 — Casos de uso como serviços de aplicação

Para cada `*UseCase` atual:

1. Substituir `@Autowired` em campos por **construtor** com dependências (portas).
2. Usar `Course` (domínio), não entidade JPA.
3. Registro no Spring: `@Service` na classe de aplicação **ou** classe `@Configuration` nos adaptadores expondo `@Bean` — escolha uma convenção e mantenha em todo o módulo.

### Passo 5 — Adaptador de persistência (JPA)

1. Entidade JPA dedicada (ex.: `CourseJpaEntity`) com `@Entity`.
2. `SpringDataCourseRepository extends JpaRepository<CourseJpaEntity, UUID>` apenas em `adapter.out.persistence`.
3. `CoursePersistenceAdapter` implementando a porta: usa o repositório Spring Data e mapeia `Course` ↔ `CourseJpaEntity`.
4. Garantir que **nenhum** código em `domain` ou `application` importe `CourseJpaEntity` ou `JpaRepository`.

### Passo 6 — Adaptador de entrada (HTTP)

1. `CourseController` injeta portas de entrada / serviços de aplicação; não conhece JPA.
2. Respostas HTTP montadas a partir de resultados da aplicação (ex.: `CourseIdDTO` como view da API).
3. OpenAPI/Swagger: schemas devem referenciar **DTOs de API**, não a entidade JPA (hoje há uso de `CourseEntity` em anotações — ajustar).

### Passo 7 — Exceções e mapeamento HTTP

1. Núcleo lança exceções semânticas (domínio/aplicação).
2. `GlobalExceptionHandler` (ou equivalente) no adaptador web/infra traduz para status e corpo (`ErrorMessageDTO`).
3. Casos de uso não conhecem códigos HTTP.

### Passo 8 — Testes

1. Testes de caso de uso: mock da **porta de persistência**, preferencialmente **sem** `@SpringBootTest` completo.
2. Adaptador JPA: `@DataJpaTest` + adapter + `JpaRepository`.
3. Controller: `@WebMvcTest` com mock da porta de entrada.
4. Atualizar testes existentes (`CreateCourseUseCaseTest`, `DeleteCourseUseCaseTest`, etc.) para construtores e novas abstrações.

### Passo 9 (opcional) — ArchUnit

Regras exemplares:

- `domain` não importa Spring, `jakarta.persistence`, pacotes `adapter`.
- `application` depende de `domain` (e de tipos próprios).
- `adapter` pode depender de `application` e `domain`.

### Passo 10 — Migração incremental

1. Introduzir domínio + porta + adapter JPA **em paralelo** ao código legado.
2. Migrar **um** fluxo (ex.: criar curso), apontar o controller, rodar testes.
3. Repetir para listar, atualizar, toggle e deletar.
4. Remover entidade/repositório “de negócio” que ainda estiverem acoplados ao JPA fora do adaptador.

---

## 5. Checklist de conclusão

- [ ] Nenhuma classe em `domain` / `application` importa `JpaRepository`, `@Entity` ou DTOs exclusivos de REST.
- [ ] Existe interface de persistência estável usada por todos os casos de uso.
- [ ] Controller só conversa com portas de entrada (ou serviços de aplicação) e DTOs HTTP.
- [ ] Testes de caso de uso rodam sem subir o contexto Spring completo, quando possível.
- [ ] Documentação OpenAPI reflete DTOs de API, não entidade JPA.

---

## 6. Armadilhas específicas deste repositório

- **`CourseMapper`:** hoje liga DTO ↔ `CourseEntity`. No hexagonal, tende a haver **dois** mapeamentos: HTTP ↔ comandos/resultados (web) e domínio ↔ JPA (persistência).
- **`ListCoursesUseCase`:** ordenação e projeção podem retornar modelos de leitura da aplicação; o controller converte para o JSON da API.
- **`CourseStatus` e `toggle()`:** bons candidatos a permanecerem no **domínio**.

---

## 7. Referência rápida dos artefatos atuais (contexto)

- Controller: `modules/course/controllers/CourseController.java`
- Casos de uso: `modules/course/useCases/*.java`
- Persistência direta: `CourseRepository` (`JpaRepository`), `CourseEntity`
- DTOs: `modules/course/dto/*`

Este guia não substitui decisões de time (nomenclatura exata de pacotes, uso ou não de Lombok no domínio, multi-módulo). Ajuste os nomes à convenção que adotar, mantendo a **direção das dependências**: de fora para dentro, nunca o contrário.

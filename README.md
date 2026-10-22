# HabitFlow Backend

API REST do **HabitFlow**, um gerenciador de hábitos com cadastro verificado por e-mail e autenticação JWT. É consumida pelo frontend [`habitFlow-web`](https://github.com/yannickdavila07/habitFlow-web).

![Java](https://img.shields.io/badge/Java-17-3C507D?style=flat-square&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.1-09CBC2?style=flat-square&logo=springboot&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-E0C58F?style=flat-square&logo=postgresql&logoColor=112250)

## Destaques

- **Cadastro com verificação:** o usuário recebe por e-mail um código de 6 dígitos, válido por 15 minutos, e só consegue entrar depois de verificá-lo. Há reenvio de código.
- **JWT com refresh:** token de acesso de 30 minutos e refresh token de 120 minutos, assinados com HMAC256.
- **Isolamento por usuário:** cada usuário enxerga e altera apenas os próprios hábitos.
- **CRUD de hábitos:** frequência diária, semanal, mensal ou anual, meta em minutos ou horas, e ativação/desativação.
- **Filtros:** listagem por frequência e por estado ativo.
- **CORS** liberado para o frontend em `http://localhost:5173`.

## Stack

- Java 17 e Spring Boot 4.1 (Web MVC, Data JPA, Validation, Security, Mail)
- PostgreSQL, com esquema criado automaticamente (`ddl-auto=update`)
- `com.auth0:java-jwt` para os tokens
- Lombok

## Como rodar

Pré-requisitos: JDK 17 e um PostgreSQL com o banco `habitFlow` criado.

O arquivo `src/main/resources/application.properties` **não é versionado** (está no `.gitignore`). Crie-o com este conteúdo:

```properties
spring.application.name=habitFlow

spring.datasource.url=jdbc:postgresql://localhost:5432/habitFlow
spring.datasource.username=postgres
spring.datasource.password=SUA_SENHA
spring.datasource.driver-class-name=org.postgresql.Driver

spring.jpa.hibernate.ddl-auto=update
spring.web.error.include-stacktrace=never

spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=SEU_EMAIL
spring.mail.password=SENHA_DE_APP_DO_GMAIL
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

Depois:

```bash
./mvnw spring-boot:run
```

A API sobe em http://localhost:8080. O teste `HabitFlowApplicationTests` carrega o contexto completo e por isso também exige o banco ativo.

## Endpoints

Rotas públicas:

| Método | Rota | Corpo | Descrição |
|---|---|---|---|
| POST | `/login` | `{login, senha}` | Login; `login` é o e-mail. Retorna `{token, refreshToken}` |
| POST | `/login/refresh` | `{refreshToken}` | Renova os tokens |
| POST | `/login/create` | `{nomeUsuario, nomeCompleto, email, senha}` | Cria a conta e envia o código |
| POST | `/login/verificar?email=` | `{codigoVerificacao}` | Verifica a conta |
| POST | `/login/reenviarcodigo?email=` | | Reenvia o código |

Rotas protegidas (`Authorization: Bearer <token>`):

| Método | Rota | Descrição |
|---|---|---|
| GET | `/habitos?frequency=&active=` | Lista os hábitos, com filtros opcionais |
| GET | `/habitos/{id}` | Detalha um hábito |
| POST | `/habitos` | Cria `{name, description, frequency, target, unit}` |
| PUT | `/habitos` | Atualiza `{id, name, description, frequency, target, unit}` |
| PATCH | `/habitos/{id}/ativar` ou `/desativar` | Altera o estado |
| DELETE | `/habitos/{id}` | Exclui |

Valores aceitos: `frequency` em `DAILY | WEEKLY | MONTHLY | ANNUALLY` e `unit` em `MINUTES | HOURS`.

Erros de negócio retornam `404` com `{"message": "..."}`. Token inválido ou expirado retorna `401`.

## Estrutura

```
src/main/java/br/com/api/habitFlow/
├── controller/   Auth e hábitos
├── service/      Regras de negócio, tokens e e-mail
├── repository/   Spring Data JPA
├── model/        Entidades User e Habito, enums
├── dto/          Records de entrada e saída
└── infra/        Segurança (filtro JWT, CORS) e tratamento de erros
```

## Licença

Sem licença definida até o momento.

# Comment Service - Microservice

Guntz Comments é um microsserviço de criação de comentários construído com Spring Boot. <br>
Esse microsserviço exibe cometários com recurso de páginação, além de pesquisa individual por ID, <br>
Válida e armazena apenas comentários aprovados através de comunicação síncrona com outro microsserviço (Guntz Moderation), <br>
Que por sua vez é responsável por toda inteligência de identificar palavras de baixo calão, ou termos utilizados em discurso de ódio.<br>
Uma vez identificado que se trata de uma palavra proíbida, ele reprova o comentário.

## ✨ Funcionalidades

- 💾 **Armazenamento**: Persiste apenas comentários aprovados
- 🔍 **Consulta**: Busca por ID e listagem com páginação
- ⚡ **Comunicação Resiliente**: Tratamento de erros em diversas camadas com Spring Validation e também com tratamento de exceções, inclusive entre serviços 

## 🛠️ Stack Tecnológica

- **Java 17** - Linguagem de programação
- **Spring Boot 3.x** - Framework principal
- **Spring RestClient** - Comunicação HTTP entre serviços
- **Spring Validation** - Tratamento de erros entre cliente e serviço
- **Spring Doc OpenAPI WebMvc UI** - Documentação da api de forma descomplicada
- **H2 Database** - Banco de dados em memória
- **Lombok** - Facilitador de escrita de código limpo
- **UUID Generator** - Gerenciador de Id com UUID
- **Maven** - Gerenciador de dependências
- **Log Slf4j** - Log com Slf4j 

## 🚀 Início Rápido

### Pré-requisitos

- ☕ JDK 17+
- 🐘 Maven
- 🔧 Git

### Instalação e Execução

1. **Clone o repositório**
   ```bash
   git clone https://github.com/ricardoguntzell/guntz-guntzcomments-comment-service.git comment-service
   ```
2. **Inicie o CommentService**
   ```bash
   cd comment-service
   ./mvnw spring-boot:run
   ```
   > 🌐 Serviço disponível em: http://localhost:8080

### Verificação Rápida

## 📖 Documentação da API
- http://localhost:8080/swagger-ui/index.html

#### Criar Comentário
```http
POST /api/comments
Content-Type: application/json

{
  "text": "Texto do comentário",
  "author": "Nome do autor"
}
```

**Respostas:**
- `201 Created` - Comentário aprovado e criado
- `422 Unprocessable Entity` - Comentário rejeitado pela moderação
- `502 Bad Gateway` - Erro de comunicação com o serviço de moderação
- `504 Gateway Timeout` - Timeout na moderação (>5s)

#### Buscar Comentário
```http
GET /api/comments/{id}
```

**Respostas:**
- `200 OK` - Comentário encontrado
- `404 Not Found` - Comentário não existe

#### Listar Comentários
```http
GET /api/comments?page=0&size=20
```

## ⚙️ Configurações e Regras

### Validações
- **IDs**: Devem ser UUIDs válidos
- **Timeout**: 5 segundos para comunicação entre serviços
- **Palavras Proibidas**: `["ódio", "xingamento"]` (configurável)

### Tratamento de Erros

| Cenário | Código HTTP | Descrição |
|---------|-------------|-----------|
| Comentário rejeitado | `422` | Contém palavras proíbidas |
| Timeout de moderação | `504` | Serviço de moderação não responde |
| Erro de integração | `502` | Falha na comunicação entre serviços |
| Comentário não encontrado | `404` | ID não existe na base |

### Fluxo de Dados

1. **Recepção**: CommentService recebe requisição
2. **Moderação**: Envia para ModerationService via HTTP
3. **Validação**: Verifica palavras proibidas
4. **Decisão**: Aprova ou rejeita baseado na validação
5. **Persistência**: Armazena apenas se aprovado
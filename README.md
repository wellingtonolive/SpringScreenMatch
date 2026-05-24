# 🎬 ScreenMatch API Consumer

Projeto desenvolvido em Java utilizando Spring Boot com foco em consumo de APIs externas, manipulação de JSON, programação funcional com Streams e análise de dados de séries e episódios.

---

# 📌 Objetivo do Projeto

O **ScreenMatch** é uma aplicação console desenvolvida para realizar consultas de séries através da API pública da [OMDb API](https://www.omdbapi.com/?utm_source=chatgpt.com).

A aplicação permite:

* Buscar informações de séries
* Listar temporadas e episódios
* Filtrar episódios
* Exibir estatísticas
* Ordenar avaliações
* Trabalhar com datas
* Utilizar Streams API para processamento funcional

O projeto demonstra conceitos modernos do ecossistema Java e boas práticas de separação de responsabilidades.

---

# 🏗️ Arquitetura Utilizada

O projeto segue uma arquitetura em camadas simplificada, muito comum em aplicações Java modernas.

## Estrutura de Pacotes

```text
br.com.alura.screenmatch
│
├── model
│   ├── DadosSerie
│   ├── DadosTemporada
│   ├── DadosEpisodio
│   └── Episodio
│
├── principal
│   └── Principal
│
├── service
│   ├── ConsumoApi
│   └── ConverteDados
│
└── ScreenmatchApplication
```

---

# 📐 Camadas da Aplicação

## 1. Camada de Inicialização

### `ScreenmatchApplication`

Classe principal responsável por iniciar o contexto do Spring Boot.

Utiliza:

```java
@SpringBootApplication
```

e implementa:

```java
CommandLineRunner
```

Isso permite executar lógica imediatamente após o boot da aplicação.

---

## 2. Camada de Orquestração

### `Principal`

Responsável pelo fluxo principal da aplicação.

Funções:

* Interação com usuário
* Controle do fluxo da aplicação
* Orquestração de chamadas de serviço
* Processamento de listas e filtros
* Exibição de relatórios

Apesar de ser uma aplicação console, a classe já demonstra um conceito semelhante a uma camada Controller.

---

## 3. Camada de Serviço

### `ConsumoApi`

Responsável por:

* Realizar chamadas HTTP
* Consumir endpoints REST
* Retornar JSON bruto

Provavelmente utiliza:

* `HttpClient`
* `HttpURLConnection`
* ou `RestTemplate`

---

### `ConverteDados`

Responsável por:

* Converter JSON em objetos Java
* Desserialização
* Mapeamento de DTOs

Normalmente implementado utilizando:

* Jackson ObjectMapper
* Gson

---

## 4. Camada de Modelo

### DTOs

Objetos utilizados para representar dados vindos da API:

* `DadosSerie`
* `DadosTemporada`
* `DadosEpisodio`

Esses objetos seguem o padrão:

## DTO — Data Transfer Object

Responsáveis apenas pelo transporte de dados.

---

### Entidade de Domínio

### `Episodio`

Representa uma abstração mais rica da aplicação.

Possui:

* regras de negócio
* parsing de dados
* transformação de tipos

---

# ⚙️ Tecnologias Utilizadas

## Linguagem

* Java 17+

---

## Framework

* Spring Boot

Utilizado para:

* bootstrap da aplicação
* gerenciamento do ciclo de vida
* simplificação da configuração

---

## API Externa

* [OMDb API](https://www.omdbapi.com/?utm_source=chatgpt.com)

Base de dados pública de filmes e séries.

---

## Processamento JSON

Provavelmente:

* Jackson
  ou
* Gson

---

## Paradigma Funcional

Uso extensivo de:

* Streams API
* Lambdas
* Optional
* Collectors

Exemplos:

```java
stream()
filter()
map()
collect()
groupingBy()
averagingDouble()
summarizingDouble()
```

---

# 🚀 Funcionalidades

## 🔎 Busca de Séries

Consulta uma série através da API OMDb:

```text
Breaking Bad
Game of Thrones
Dark
```

---

## 📺 Listagem de Temporadas

A aplicação percorre automaticamente todas as temporadas:

```java
for(int i =1; i <=dadosSerie.totalTemporadas(); i++)
```

---

## 🎞️ Listagem de Episódios

Todos os episódios são agrupados em uma lista única utilizando:

```java
flatMap()
```

---

## ⭐ Top 5 Episódios

Ordenação por avaliação:

```java
sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())
```

---

## 🔍 Busca por Título

Permite localizar episódios por trecho do nome.

Exemplo:

```text
Digite o nome do título:
fly
```

---

## 📅 Filtro por Data

Permite listar episódios lançados após determinado ano.

---

## 📊 Estatísticas

A aplicação calcula:

* média das avaliações
* maior nota
* menor nota
* quantidade de episódios

Utilizando:

```java
DoubleSummaryStatistics
```

---

# 🧠 Conceitos Java Demonstrados

## Java Streams API

Uso intensivo de programação funcional.

---

## Optional

Evita NullPointerException:

```java
Optional<Episodio>
```

---

## Records (Possivelmente)

Os DTOs podem ter sido implementados como:

```java
public record DadosSerie(...)
```

---

## LocalDate

Manipulação moderna de datas com Java Time API.

---

## Collectors

Agrupamentos e estatísticas avançadas.

---

# 📡 Fluxo da Aplicação

```text
Usuário
   ↓
Principal
   ↓
ConsumoApi
   ↓
OMDb API
   ↓
JSON
   ↓
ConverteDados
   ↓
DTOs
   ↓
Processamento Streams
   ↓
Relatórios e Estatísticas
```

---

# 📦 Dependências Esperadas

Exemplo de dependências Maven:

```xml
<dependencies>

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter</artifactId>
    </dependency>

    <dependency>
        <groupId>com.fasterxml.jackson.core</groupId>
        <artifactId>jackson-databind</artifactId>
    </dependency>

</dependencies>
```

---

# ▶️ Como Executar

## Pré-requisitos

* Java 17+
* Maven 3.8+

---

## Clone do Projeto

```bash
git clone https://github.com/seu-repo/screenmatch.git
```

---

## Executar

```bash
mvn spring-boot:run
```

ou

```bash
java -jar screenmatch.jar
```

---

# 🔐 Configuração da API KEY

A aplicação utiliza a OMDb API Key:

```java
private final String APIKEY = "&apikey=xxxx";
```

Recomendado mover para:

## `application.properties`

```properties
omdb.apikey=YOUR_KEY
```

e utilizar:

```java
@Value("${omdb.apikey}")
```

---

# 📈 Melhorias Futuras

## Melhorias Arquiteturais

* Implementar injeção de dependência
* Separar camada de Controller
* Criar Services especializados
* Adicionar persistência com JPA
* Implementar cache
* Adicionar logs estruturados

---

## Melhorias Técnicas

* Docker
* Testes unitários
* Mockito
* Spring Data JPA
* PostgreSQL
* Tratamento global de exceções
* Swagger/OpenAPI
* API REST

---

# 🧪 Possíveis Testes

## Unitários

* Conversão JSON
* Parsing de datas
* Filtros Streams

---

## Integração

* Consumo da API OMDb
* Fluxo completo da aplicação

---

# 📚 Conceitos Demonstrados no Projeto

✅ Programação funcional
✅ Streams API
✅ Consumo de APIs REST
✅ Manipulação de JSON
✅ Java Time API
✅ Estatísticas com Collectors
✅ Organização em camadas
✅ DTO Pattern
✅ Clean Code básico
✅ Optional API
✅ Spring Boot Bootstrap

---

# 👨‍💻 Autor

Projeto desenvolvido para fins educacionais com foco em:

* Java moderno
* Spring Boot
* APIs REST
* Streams API
* Arquitetura em camadas

---

# 📄 Licença

Projeto para fins de estudo e aprendizado.

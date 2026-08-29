# 🍃 EcoPath API

![Status](https://img.shields.io/badge/Status-Em_Desenvolvimento-orange)
![License](https://img.shields.io/badge/License-MIT-blue)

> **A Motivação:** Eu sempre gostei muito de pedalar e planejar rotas por ciclovias, mas o clima frequentemente deixava a desejar e acabava me pegando de surpresa no meio do caminho. O EcoPath nasceu exatamente dessa minha dor pessoal. Senti a necessidade de criar uma ferramenta inteligente que não apenas calcule o melhor trajeto, mas que sirva de base para cruzar esses dados de rota com variáveis climáticas, garantindo um pedal muito mais seguro, previsível e agradável.

> 🚧 **Status do Projeto:** Este sistema está em **desenvolvimento ativo**. Estou construindo e aprimorando a arquitetura aos poucos. As próximas etapas incluem a integração com dados climáticos em tempo real e a persistência de histórico de rotas.

Uma API RESTful desenvolvida em Spring Boot para simulação e cálculo de rotas para ciclistas. O sistema utiliza a OpenRouteService API para calcular rotas e obter informações de distância e tempo estimado entre coordenadas geográficas, enquanto a Open-Meteo API fornece dados meteorológicos para permitir, posteriormente, o cruzamento das condições climáticas com as rotas calculadas.

A ideia central do EcoPath é combinar dados de rotas e condições meteorológicas, permitindo construir uma experiência mais segura e previsível para quem utiliza a bicicleta como meio de transporte ou lazer.

---

# 🛠️ Tecnologias Utilizadas

* **Java 21**
* **Spring Boot (Web, Data JPA)**
* **PostgreSQL 16** (Banco de dados relacional)
* **Docker & Docker Compose** (Orquestração de ambiente)
* **OpenRouteService API** (Serviço externo de geolocalização e rotas)
* **Open-Meteo API** (Serviço externo de dados meteorológicos)
* **Maven** (Gerenciamento de dependências)

---

## ⚙️ Como Executar o Projeto

Certifique-se de ter o **Docker** instalado e em execução na sua máquina.

### 1. Clone o repositório

```bash
git clone https://github.com/igorRoberto/eco-path-api.git
```

Entre na pasta do projeto:

```bash
cd eco-path-api
```

### 2. Suba a aplicação com Docker Compose

Execute:

```bash
docker compose up --build
```

O comando irá construir a aplicação e iniciar todos os serviços necessários, incluindo o banco de dados.

Para executar a aplicação em segundo plano:

```bash
docker compose up --build -d
```

### 3. Verifique os containers

Para verificar se os containers estão em execução:

```bash
docker compose ps
```

Para acompanhar os logs da aplicação:

```bash
docker compose logs -f
```

Também é possível visualizar os logs de um serviço específico:

```bash
docker compose logs -f nome-do-servico
```

### 4. **Configuração da Chave de Acesso (Token):**
   O serviço utiliza a API do [OpenRouteService](https://openrouteservice.org/), que exige uma chave de acesso para funcionar. Acesse o site oficial, crie sua conta gratuita para gerar o token e configure-o no arquivo `.env` na raiz do projeto:
   ```env
   ORS_TOKEN=seu_token_aqui
```

### 5. Acesse a API

Após a inicialização, a API estará disponível em:

```text
http://localhost:8080
```

### 6. Encerrar a aplicação

Para parar os containers:

```bash
docker compose down
```

Caso também queira remover os volumes:

```bash
docker compose down -v
```

> ⚠️ O comando `docker compose down -v` remove os volumes persistidos, podendo apagar os dados armazenados no banco de dados.

---

## 🌐 APIs Externas

### 🗺️ OpenRouteService

O **OpenRouteService** é utilizado para realizar o cálculo das rotas da aplicação.

A API recebe as coordenadas de origem e destino e retorna informações utilizadas pelo EcoPath para representar o percurso, como:

- Distância do percurso;
- Tempo estimado de viagem;
- Geometria da rota;
- Coordenadas do trajeto.

Esses dados são utilizados como base para o planejamento das rotas realizadas pelos usuários.

### ⛅ Open-Meteo

A Open-Meteo é utilizada para obter informações meteorológicas e ambientais relacionadas à localização do percurso.

A integração permite consultar informações como:

* Temperatura;
* Precipitação;
* Velocidade do vento;
* Condições meteorológicas;
* **Índice de qualidade do ar (AQI);**
* **Status e recomendações de saúde para o percurso;**
* Previsões para diferentes horários.

A proposta é utilizar essas informações em conjunto com os dados fornecidos pelo OpenRouteService, permitindo analisar as condições climáticas e ambientais ao longo do percurso.

---



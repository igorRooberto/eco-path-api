# 🍃 EcoPath API

![Status](https://img.shields.io/badge/Status-Em_Desenvolvimento-orange)
![License](https://img.shields.io/badge/License-MIT-blue)

> **A Motivação:** Eu sempre gostei muito de pedalar e planejar rotas por ciclovias, mas o clima frequentemente deixava a desejar e acabava me pegando de surpresa no meio do caminho. O EcoPath nasceu exatamente dessa minha dor pessoal. Senti a necessidade de criar uma ferramenta inteligente que não apenas calcule o melhor trajeto, mas que sirva de base para cruzar esses dados de rota com variáveis climáticas, garantindo um pedal muito mais seguro, previsível e agradável.

> **🚧 Status do Projeto:** 
Este sistema está em desenvolvimento ativo. A camada de segurança com Spring Security/JWT e a integração arquitetural com APIs externas (OpenRoute e Open-Meteo) já estão implementadas com modelagem de dados de alta precisão. 
**Foco Atual:** Implementação de uma suíte abrangente de **Testes Unitários** (JUnit 5 e Mockito) para garantir a resiliência das regras de negócio e das conversões de dados, preparando o repositório para a futura integração de pipelines de CI/CD.

Uma API RESTful desenvolvida em Spring Boot para simulação e cálculo de rotas para ciclistas. O sistema utiliza a OpenRouteService API para calcular rotas e obter informações de distância e tempo estimado entre coordenadas geográficas, enquanto a Open-Meteo API fornece dados meteorológicos para permitir o cruzamento das condições climáticas com as rotas calculadas.

---

## 🛠️ Tecnologias Utilizadas

* **Java 21**
* **Spring Boot 4.1.0** (Web, Data JPA, Security)
* **Spring Security & JWT** (Autenticação e Autorização)
* **PostgreSQL 16** (Banco de dados relacional)
* **Docker & Docker Compose** (Containerização)
* **JUnit 5 & Mockito** (Testes Unitários)
* **OpenRouteService API & Open-Meteo API** (Serviços externos)

---

## ⚙️ Execução Rápida

### Pré-requisitos
* Docker e Docker Compose instalados.
* Chave de acesso gratuita da [OpenRouteService API](https://openrouteservice.org/dev/).

### Rodando a Aplicação

1. Clone o repositório:
   ```bash
   git clone [https://github.com/igorRoberto/eco-path-api.git](https://github.com/igorRoberto/eco-path-api.git)
   cd eco-path-api
   ```

2. Crie um arquivo `.env` na raiz do projeto com o seu token da API:
   ```env
   ORS_TOKEN=seu_token_aqui
   ```

3. Suba o ambiente com o Docker Compose:
   ```bash
   docker compose up -d --build
   ```

A API estará disponível em `http://localhost:8080`.

---

## 📌 Endpoints Principais

### 🚴 Simulação de Rota

`POST /route/simulate`

Recebe os pontos de origem, destino e perfil de mobilidade para calcular o trajeto entre as coordenadas.

**Headers:**
- `Content-Type: application/json`
- `Authorization: Bearer <seu_token_jwt>`

**Request Body:**
```json
{
  "originName": "Casa",
  "destinationName": "Trabalho",
  "profile": "CYCLING_REGULAR",
  "originCoordinates": {
    "latitude": -16.3267,
    "longitude": -48.9534
  },
  "destinationCoordinates": {
    "latitude": -16.3300,
    "longitude": -48.9500
  }
}
```

**Exemplo via cURL:**
```bash
curl -X POST http://localhost:8080/api/v1/routes/simulate \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer SEU_TOKEN_JWT" \
  -d '{
    "originName": "Casa",
    "destinationName": "Trabalho",
    "profile": "CYCLING_REGULAR",
    "originCoordinates": {
      "latitude": -16.3267,
      "longitude": -48.9534
    },
    "destinationCoordinates": {
      "latitude": -16.3300,
      "longitude": -48.9500
    }
  }'
```

---

## 🌐 APIs Externas Integradas

* 🗺️ **[OpenRouteService](https://openrouteservice.org/dev/#/api-docs):** Responsável pelos cálculos geográficos, geometria da rota, distância e tempo estimado.
* ⛅ **[Open-Meteo](https://open-meteo.com/en/docs):** Fornece métricas climáticas (temperatura, vento, chuva) e o Índice de Qualidade do Ar (AQI) para recomendações de saúde no percurso.

---



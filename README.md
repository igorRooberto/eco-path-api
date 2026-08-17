# 🍃 EcoPath API

![Status](https://img.shields.io/badge/Status-Em_Desenvolvimento-orange)
![License](https://img.shields.io/badge/License-MIT-blue)

> **A Motivação:** Eu sempre gostei muito de pedalar e planejar rotas por ciclovias, mas o clima frequentemente deixava a desejar e acabava me pegando de surpresa no meio do caminho. O EcoPath nasceu exatamente dessa minha dor pessoal. Senti a necessidade de criar uma ferramenta inteligente que não apenas calcule o melhor trajeto, mas que sirva de base para cruzar esses dados de rota com variáveis climáticas, garantindo um pedal muito mais seguro, previsível e agradável.

> 🚧 **Status do Projeto:** Este sistema está em **desenvolvimento ativo**. Estou construindo e aprimorando a arquitetura aos poucos. As próximas etapas incluem a integração com dados climáticos em tempo real e a persistência de histórico de rotas.

Uma API RESTful desenvolvida em **Spring Boot** para simulação e cálculo de rotas. O sistema integra-se com a API externa do **OpenRouteService** para fornecer métricas precisas de distância e tempo estimado de viagem entre duas coordenadas geográficas.

---

## 🚀 Tecnologias Utilizadas

*   **Java 21**
*   **Spring Boot 4.1.0** (Web, Data JPA)
*   **H2 Database** (Banco de dados em memória para testes rápidos)
*   **OpenRouteService API** (Serviço externo de geolocalização e rotas)
*   **Maven** (Gerenciamento de dependências)

# LuthierFlow: Sistema de Gestão para Ateliês de Luthieria

**LuthierFlow** é uma aplicação web full-stack projetada para ser a solução completa na gestão de um ateliê de luthieria. O sistema visa otimizar e organizar todos os processos do dia a dia de um luthier, desde o cadastro de clientes e seus instrumentos até o controle financeiro e a divulgação de trabalhos finalizados.

## ✨ Funcionalidades Principais

O sistema foi desenhado para cobrir todo o fluxo de trabalho de um luthier, incluindo:

* **Gestão de Clientes:** Cadastro completo de clientes e seus dados de contato.
* **Gestão de Instrumentos:** Associação de múltiplos instrumentos a cada cliente, com detalhes como marca, modelo e número de série.
* **Ordens de Serviço (O.S.):** Controle total do fluxo de trabalho através de um **quadro Kanban** visual e intuitivo, com status como "Orçamento", "Em Andamento", "Finalizado", etc.
* **Controle de Estoque:** Gerenciamento do inventário de peças e componentes, com controle de quantidade e preço de venda.
* **Módulo de Portfólio:** Uma vitrine pública para o luthier exibir seus melhores trabalhos, com galeria de fotos e descrições detalhadas, totalmente integrada às Ordens de Serviço finalizadas.

## 🛠️ Tecnologias Utilizadas

Este projeto foi construído utilizando tecnologias modernas e robustas, tanto no backend quanto no frontend.

<p align="center">
  <img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java"/>
  <img src="https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white" alt="Spring Boot"/>
  <img src="https://img.shields.io/badge/Angular-DD0031?style=for-the-badge&logo=angular&logoColor=white" alt="Angular"/>
  <img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white" alt="MySQL"/>
  <img src="https://img.shields.io/badge/Git-F05032?style=for-the-badge&logo=git&logoColor=white" alt="Git"/>
  <img src="https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=github&logoColor=white" alt="GitHub"/>
  <img src="https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white" alt="Maven"/>
</p>

### Backend:

* **Java 21**
* **Spring Boot 3:** Framework principal para a construção da API REST.
    * **Spring Data JPA:** Para a camada de persistência com o banco de dados.
    * **Spring Web:** Para a criação dos endpoints RESTful.

### Frontend:

* **Angular 17:** Framework para a construção da interface do usuário (Single Page Application).
* **TypeScript:** Linguagem principal do frontend.

### Banco de Dados:

* **MySQL 8:** Sistema de Gerenciamento de Banco de Dados Relacional.

### Controle de Versão:

* **Git** & **GitHub:** Para versionamento do código e colaboração.

### DevOps (Planejado):

* **GitHub Actions:** Para Integração Contínua (CI).
* **AWS:** Para o deploy da aplicação em nuvem.

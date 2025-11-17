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

---

## 🚦 Status do Projeto

**Em Desenvolvimento (Work in Progress)**

O projeto está em desenvolvimento ativo, mas quase finalizado. As funcionalidades principais do backend e frontend estão implementadas, faltando apenas a seção de Portfólio. Não é recomendado para uso em produção.

---

## ▶️ Como Executar (Localmente)

Para executar este projeto em sua máquina local, siga os passos abaixo.

### Pré-requisitos

* [JDK 21](https://www.oracle.com/java/technologies/downloads/) ou superior
* [Maven](https://maven.apache.org/download.cgi)
* [Node.js e npm](https://nodejs.org/en/)
* [Angular CLI](https://angular.io/cli) (instalado globalmente: `npm install -g @angular/cli`)
* [MySQL 8](https://dev.mysql.com/downloads/) (recomenda-se o MySQL Workbench para gerenciamento)

---

### 1. Configuração do Banco de Dados

1.  Abra o **MySQL Workbench** (ou outro cliente de sua preferência).
2.  Crie um novo schema (banco de dados). Você pode chamá-lo de `luthierflow_db`.
    ```sql
    CREATE DATABASE luthierflow_db;
    ```

---

### 2. Backend (Spring Boot)

1.  **Clone o repositório** (se ainda não o fez):
    ```bash
    git clone [https://github.com/seu-usuario/LuthierFlow.git](https://github.com/seu-usuario/LuthierFlow.git)
    cd LuthierFlow
    ```

2.  **Navegue até a pasta do backend** (ex: `cd backend/` ou `cd api/`, ajuste conforme seu projeto).

3.  **Configure o `application.properties`**:
    * Localize o arquivo em `src/main/resources/application.properties`.
    * Adicione as credenciais do seu banco de dados. Ele deve ficar parecido com isto:
    ```properties
    # Configuração do Banco de Dados MySQL
    spring.datasource.url=jdbc:mysql://localhost:3306/luthierflow_db?useTimezone=true&serverTimezone=UTC
    spring.datasource.username=root
    spring.datasource.password=sua-senha-do-mysql

    # Configuração do Hibernate (O Spring Boot fará o resto)
    spring.jpa.hibernate.ddl-auto=update
    spring.jpa.show-sql=true
    ```

4.  **Execute o Backend**:
    * Você pode rodar diretamente pela sua IDE (IntelliJ, Eclipse) ou pelo terminal Maven:
    ```bash
    mvn spring-boot:run
    ```
    * O servidor backend estará disponível em `http://localhost:8080`.

---

### 3. Frontend (Angular)

1.  **Abra um novo terminal**.

2.  **Navegue até a pasta do frontend** (ex: `cd frontend/`, ajuste conforme seu projeto):
    ```bash
    # (A partir da raiz do projeto)
    cd frontend/
    ```

3.  **Instale as dependências** do Node.js:
    ```bash
    npm install
    ```

4.  **Execute o Frontend**:
    ```bash
    ng serve
    ```
    * Acesse a aplicação no seu navegador em `http://localhost:4200`. A aplicação Angular já está configurada para se comunicar com a API em `localhost:8080`.

---

## 👨‍💻 Autoria

Desenvolvido por **Daniel Ferri**.

* [GitHub](https://github.com/danferri)
* [LinkedIn](https://www.linkedin.com/in/daniel-ferri/)

---

## 📜 Licença

Este projeto está sob a licença MIT.

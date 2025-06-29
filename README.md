# 🚀 Upload API - Sistema de Importação e Análise de Transações

Este projeto é uma **API RESTful** desenvolvida em **Java com Spring Boot**, com foco em importação, autenticação e gerenciamento de **transações financeiras**. A aplicação é backend puro, com uso de templates HTML, resolvendo o desafio Back-end #3 da empresa Alura.

---

## 📌 Visão Geral

- 📂 Upload de arquivos de transações  
- 👤 Cadastro e alteração de senhas  
- 📊 Listagem de transações importadas  
- 🧪 Testes automatizados  
- 🛡️ Segurança com Spring Security  

---

## 🧱 Tecnologias

- Java 17+  
- Spring Boot 3.5.0  
- Spring Security   
- Spring Data JPA  
- Maven  
- MySQL

---

## 📁 Estrutura do Projeto
<br>
src/
<br>
└── main/java/dev/Zerphyis/upload/
<br>
├── UploadApplication.java
<br>
├── controller/
<br>
├── domain/
<br>
├── repository/
<br>
├── service/
<br>
└── aplication/records/
<br>

---

## 🧩 Funcionalidades

### 1. 🔐 Autenticação
- `POST /login`: Retorna um token JWT  
- `PUT /esqueci-minha-senha`: Altera a senha do usuário autenticado  

<h3>-Pagina de Login:</h3>

![Image](https://github.com/user-attachments/assets/03770357-b159-4b1e-a699-6bf9004caca0)

<h3>-Pagina de recuperação de conta:</h3>

![Image](https://github.com/user-attachments/assets/dfe4987c-5c29-4328-9434-b1926907f2dd)

### 2. 📂 Importação de Transações
- `POST /transacoes/importar`: Faz upload de dados de transações  
- Aceita arquivos com dados em formato estruturado  
- Realiza persistência no banco  

<h3>-Pagina de Importação de transações</h3>

![Image](https://github.com/user-attachments/assets/a2ce6122-ef7d-43c3-b0ec-b68eba83c055)

### 3. 📊 Listagem de Transações
- `GET /transacoes`: Lista as transações salvas no sistema  

<h3>-Pagina de listagem de transações</h3>

![Image](https://github.com/user-attachments/assets/28eef981-18cd-4aba-b36a-61c5c5cb2278)

---
## ▶️ Como Rodar

### Pré-requisitos
- Java 17  
- Maven 3.8+

### Comandos

```bash
git clone https://github.com/seu-usuario/UploadAPI.git
cd UploadAPI
./mvnw clean install
./mvnw spring-boot:run
API disponível em: http://localhost:8080

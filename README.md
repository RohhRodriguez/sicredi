# sicredi

## Planejamento

- [x] Etapa 1: Configuração básica do projeto
- [x] Etapa 2: Criação das tabelas e relacionamentos
- [x] Etapa 3: Criação dos endpoints e dos services
- [x] Etapa 4: Integração com serviço externo de validação de CPF
- [x] Etapa 5: Configuração e publicação/deploy
- [x] Etapa 6: Configuração de filas externas (rabbitMq, Azure, etc)

## Contatos

- Desenvolvedora
  * Rônica CS Rodrigues - ronica_cristina@hotmail.com
  * https://www.linkedin.com/in/ronicacsrodrigues/

  ### Ferramentas Utilizadas

  As ferramentas (Linguagens/Frameworks/Bancos de Dados) utilizadas para desenvolver o projeto foram:

  * Java 21 : Garantindo performance e novas funcionalidades.
  * Spring Boot 3.5.0 : Framework confiável e escalável, ideal para aplicações modernas.
  * Flyway: migrations
  * Render: publicação e armazenagem

  <p align="right">(<a href="#readme-top">voltar ao início</a>)</p>

  ## Iniciando

  Instruções para instalar o ambiente de desenvolvimento:

  ### Pre Requisitos

  * Java 21 : Certifique-se de que o JDK 21 está instalado e configurado corretamente no seu sistema.
  * Apache Maven : Ferramenta de automação de compilação, usada para gerenciar dependências e construir o projeto. Verifique se o Maven está instalado e adicionado ao seu PATH.
  * Git : Para clonar o repositório do projeto. Certifique-se de que o Git está instalado na sua máquina.
  * IDE (Integrated Development Environment) : Embora não estritamente necessário, recomenda-se uma IDE como IntelliJ IDEA ou Eclipse, que proporciona um ambiente amigável para o desenvolvimento Java.
  * Banco de Dados SQL Server : Um servidor de banco de dados configurado e acessível para o desenvolvimento e teste das funcionalidades da API.

  ### Instalação

  Primeiramente clone o projeto:

  `git clone https://github.com/seu-usuario/nome-do-repositorio.git`

  Crie um arquivo env.properties com base no de exemplo "env.properties.example"

  Navegue até o repositório:

  `cd nome-do-repositorio`

  Construir o Projeto com Maven:

  `mvn clean install`

  Execute o servidor de desenvolvimento:

  `mvn spring-boot:run`


  ## Uso do Sistema

  A documentação Swagger da API ficará disponível no endereço abaixo:

  https://sicredi-ntf8.onrender.com/swagger-ui/index.html

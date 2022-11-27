# mini-autorizador
Desafio VR Desenvolvimento.

## O desafio
A descrição completa do desafio pode ser encontrada no arquivo [DESAFIO.md](DESAFIO.md).

## Desenvolvimento

### DDD
Durante o desenvolvimento do projeto foi utilizado a abordagem DDD(Domain Drive Design) e Injeção de dependencias com objetivo de facilitar a implementação de complexas regras.
https://coodesh.com/blog/candidates/metodologias/tdd-e-seu-significado-por-que-ele-ajuda-a-aumentar-a-sua-produtividade/
### TDD
TDD (_Test-Driven Development_) no qual o código fica mais limpo,e simples para que o teste tenha resultado positivo aumentando não só a correção de BUGS mais apresenta uma maior produtividade para o developer e traz uma cultura no qual o codigo já foi testado e em funcionamento , independentemente do número de Devs trabalhando no mesmo projeto.

### Arquitetura  

O projeto foi desenvolvido utilizando a arquitetura padrão Spring MVC n-tier, esta arquitetura foi escolida por afinidade e
por proporcionar uma redução da complexibilidade do codigo, facilidade de manutenção, separação muito clara entre as 
camadas de visualização e regras de negócios e outras vantagens que esta arquitetura traz. 
<br/><br/>
Sendo assim temos a divisão do projeto nas seguintes camadas:
 * **Controller**: Responsável por lidar com as requisições dos usuarios;
 * **Business**: Business-tier uma das camadas mais importantes para o desenvolvedor, pois trata de toda a lógica da aplicação no qual  se definem todas as regras do negócio, alocação de recursos, validação de dados de segurança;
 * **Service**: Camada onde esta localizada a aplicação das regras de negocios;
 * **Repository**: Isola os objetos ou entidades do domínio do código que acessa o banco de dados.

##  Pré -requisitos

- [ `Java 11+` ](https://www.oracle.com/java/technologies/downloads/#java11)
- [ `Junit 5` ](https://www.oracle.com/java/technologies/downloads/#java11)
- [ `Docker` ](https://junit.org/junit5/docs/current/user-guide/)
- [ `Docker-Compose` ](https://docs.docker.com/compose/install/)

*** Configurar variaveis de ambiente

### Comandos (Linux)

### Comandos (Linux)
Clonar projeto em uma pasta
> git clone https://github.com/saudrei/miniAutorizador.git

Iniciar o banco de dados com docker-compose

> cd docker

> docker compose up

Iniciar o projeto

> cd autorizador

> ./mvnw  spring-boot:run
> 
 ### Testes Integracao

> cd autorizador
> ./mvnw  test -Punit-test

### Testes Unitarios

> ./mvnw  test -Pintegration

### REST
O autorizador utliza uma interface rest para comunicação com os seguintes endpoints

> /cartoes [POST]  --- Criar novo cartão

> /cartoes/6549873025634501 [GET] --- Obter saldo do Cartão

> /transacoes [POST] --- Realizar uma Transação

Documentação da API
> /swagger-ui.html

### Detalhes

#### Criar novo cartão
```
Method: POST
URL: http://localhost:8080/cartoes
Body (json):
{
    "numeroCartao": "6549873025634501",
    "senha": "1234"
}
```
##### Possíveis respostas:
```
Criação com sucesso:
   Status Code: 201
   Body (json):
   {
      "senha": "1234",
      "numeroCartao": "6549873025634501"
   } 
-----------------------------------------
Caso o cartão já exista:
   Status Code: 422
   Body (json):
   {
      "senha": "1234",
      "numeroCartao": "6549873025634501"
   } 
```

#### Obter saldo do Cartão
```
Method: GET
URL: http://localhost:8080/cartoes/{numeroCartao} , onde {numeroCartao} é o número do cartão que se deseja consultar
```

##### Possíveis respostas:
```
Obtenção com sucesso:
   Status Code: 200
   Body: 495.15 
-----------------------------------------
Caso o cartão não exista:
   Status Code: 404 
   Sem Body
```

#### Realizar uma Transação
```
Method: POST
URL: http://localhost:8080/transacoes
Body (json):
{
    "numeroCartao": "6549873025634501",
    "senhaCartao": "1234",
    "valor": 10.00
}
```

##### Possíveis respostas:
```
Transação realizada com sucesso:
   Status Code: 201
   Body: OK 
-----------------------------------------
Caso alguma regra de autorização tenha barrado a mesma:
   Status Code: 422 
   Body: SALDO_INSUFICIENTE|SENHA_INVALIDA|CARTAO_INEXISTENTE (dependendo da regra que impediu a autorização)
```

### Implementação

### Desafios Opcionais
O desafio apresentado contou com 2 desafios opcionais:
<br/><br/>
**Construção da solução sem a utilização dos comandos `if`, `continue` e `break`**:<br/>
O processo de desenvolvimento foi aplicado clean code,ou “código limpo”, é possível programar bons códigos para facilitar a sua leitura e torná-lo claro, simples e escalável, a fim de que alcance os objetivos do desafio,
paralelamente foi ajustados os testes para que contemplasse as regras determinadas pelo desafio, após os requisitos atendidos foi feito uma refatoração para atender os desafios opcionais, para isso foi utilizado os padroes de projeto ([Factory](https://www.gofpatterns.com/creational/patterns/factory-method-pattern.php)
e [Command](https://www.gofpatterns.com/behavioral/patterns/command-pattern.php))criando validadores independentes para cada regra, tabem foi implementado ControllerAdvice (Extends ResponseEntityExceptionHandler) e UnprocessableException e valiraveis staticas
no qual retorna o tipo de exceção, no caso do if foi utilizado operadores ternarios. Foram criadas pull requests (https://doordash.engineering/2022/08/23/6-best-practices-to-manage-pull-request-creation-and-feedback/) para cada momento relevantes de atualização.


```
@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class UnprocessableException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	public static String SALDO_INSUFICIENTE = "SALDO_INSUFICIENTE";
	public static String SENHA_INVALIDA = "SENHA_INVALIDA";
	public static String CARTAO_INEXISTENTE = "CARTAO_INEXISTENTE";

	public UnprocessableException(String message) {
		super(message);
	}
}
```

**Prevenção contra transações concorrentes**:<br/>
Esta prevenção foi feita através do uso das Transações no Spring Framework que possibilita o uso de transação,
garantindo:
* Atomicidade: Todos os comandos presentes podem ser concluídos com sucesso ou sem sucesso. Caso uma das operações do
banco de dados falhe havera um _rollback_ em todas as outras operações ja executadas naquela transação.
* Consistência: Representa a consistência de integridade da base de dados.
* Durabilidade: Uma transação após persistida deve continuar persistida e não pode ser apagada por 
falhas no sistema.
* Isolação: Muitas transação podem ser executadas em simultâneo, a isolação garante que cada transação 
é isolada da outra, prevenindo corrupção de dados.

>TODO

* Fazer um estudo da viabiliade da arquitetura hexagonal com implementaçãlo de ports e adapters.

Implementar as seguintes stacks

- **Sonar** Analise de qualidade e cobertura de testes
- **Elasticsearch** Busca e análise de dados
- **Logstash** Pipeline de dados
- **Kibana** Visualização de dados
- **Filebeat** Log shipper
- **Prometheus** Monitoramento e alertas
- **Grafana** Análise e Monitoramento
- **Alertmanager** Envio de alertas
- **Jaeger** Tracing Distribuído
- **Kafka** Processar os fluxos de transacoes 


 

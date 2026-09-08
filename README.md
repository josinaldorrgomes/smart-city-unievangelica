# Smart City UniEVANGÉLICA — Plataforma Didática de IoT

Projeto acadêmico em **Spring Boot 3 + Thymeleaf + Java 17 + H2** que ilustra,
na prática, os conceitos de **Smart City / IoT urbano**, com dashboards
**animados e em tempo real** (via Server-Sent Events) para 7 módulos de
sensoriamento simulados, autenticação com dois perfis de acesso e cadastro
dinâmico de novos módulos IoT.

## Módulos de Smart City contemplados

1. **Monitoramento da Água** — pH, turbidez, índice de poluição, oxigênio dissolvido
2. **Monitoramento do Ar** — PM2.5, PM10, CO₂, índice de qualidade do ar
3. **Índice de Radiação Ultravioleta (UV)** — índice UV e classificação de risco
4. **Rastreamento via Celular** — velocidade, distância percorrida, dispositivos ativos
5. **Rastreamento por Sensores** — fluxo de veículos/pessoas e detecção de produtos inflamáveis
6. **Monitoramento por Imagens (CFTV)** — pessoas/veículos detectados e status da câmera
7. **Detector de Fumaça e Ruído** — nível de fumaça (ppm), ruído (dB), alerta de incêndio/disparo

Cada módulo cadastrado gera, a cada **4 segundos**, uma leitura aleatória
plausível para o seu tipo. As leituras são persistidas no H2 e transmitidas
em tempo real (SSE) para todos os navegadores conectados, que atualizam os
cards e gráficos sem precisar recarregar a página.

## Como executar

Pré-requisitos: **JDK 17+** e **Maven 3.9+** (ou use o `mvnw` se preferir
gerar o wrapper com `mvn -N wrapper:wrapper`).

```bash
cd smart-city-unievangelica
mvn spring-boot:run
```

Acesse: **http://localhost:8080**

O H2 Console (para inspecionar o banco em memória) fica disponível em
`http://localhost:8080/h2-console` — JDBC URL: `jdbc:h2:mem:smartcity`,
usuário `sa`, senha em branco.

## Credenciais de demonstração

| Perfil        | Usuário    | Senha         | Permissões                                             |
|---------------|-----------|---------------|----------------------------------------------------------|
| Administrador | `admin`    | `admin123`    | Acesso total + cadastro/edição/remoção de módulos IoT     |
| Operador      | `operador` | `operador123` | Acesso aos dashboards de monitoramento em tempo real      |

Os usuários e 7 módulos de demonstração (um de cada tipo) são criados
automaticamente na primeira execução pela classe `DataInitializer`. O banco
H2 é em memória, então tudo é reiniciado a cada `mvn spring-boot:run`.

## Estrutura do projeto

```
src/main/java/br/edu/unievangelica/smartcity/
 ├─ config/        SecurityConfig, DataInitializer
 ├─ model/         IoTModule, SensorReading, AppUser, ModuleType, ReadingStatus
 ├─ repository/    Interfaces Spring Data JPA
 ├─ service/       Simulação (@Scheduled), broadcast SSE, geração de dados, regras de negócio
 └─ controller/    Dashboard, detalhe do módulo, SSE, login, CRUD administrativo

src/main/resources/
 ├─ templates/     Thymeleaf (dashboard, detalhe do módulo, login, admin)
 └─ static/        CSS (identidade visual) e JS (SSE + Chart.js)
```

## Arquitetura do tempo real

- `SensorSimulationService` roda a cada 4s (`@Scheduled(fixedRate = 4000)`),
  gera uma leitura para cada módulo **ativo** via `SensorDataGenerator` e
  publica um evento `reading` através de `SseBroadcastService`.
- O front-end abre uma conexão `EventSource("/api/stream")` e atualiza os
  cards do dashboard (`dashboard.js`) ou o gráfico Chart.js da página de
  detalhe (`module-detail.js`) conforme os eventos chegam — sem polling e
  sem recarregar a página.

## Cadastro de novos módulos IoT

Com o usuário `admin`, acesse **Módulos IoT (Admin)** no menu superior para
listar, cadastrar, editar, ativar/inativar ou remover módulos. Ao cadastrar
um novo módulo, basta escolher um dos 7 tipos — a simulação passa a gerar
dados para ele automaticamente no próximo ciclo (até 4s).

## Identidade visual

A paleta de cores (azul institucional + dourado) foi definida em
`src/main/resources/static/css/style.css` como uma aproximação da
identidade visual da UniEVANGÉLICA (https://www4.unievangelica.edu.br).
Caso vocês tenham acesso ao Manual da Marca oficial da instituição, basta
ajustar as variáveis `--uni-blue-dark`, `--uni-blue` e `--uni-gold` no topo
do arquivo CSS para alinhar exatamente aos tons oficiais (e, se desejado,
trocar o ícone `bi-buildings` da navbar/login pela logo oficial em PNG/SVG).

## Observações didáticas

- Todos os dados exibidos são **simulados** (números aleatórios plausíveis),
  não provenientes de sensores reais — o foco é ilustrar a arquitetura de
  uma plataforma de Smart City (coleta → processamento → tempo real → visualização).
- O projeto usa H2 em memória por simplicidade; para persistência real,
  basta trocar as propriedades `spring.datasource.*` em
  `application.properties` para PostgreSQL/MySQL — o restante do código
  (JPA/Hibernate) não precisa mudar.

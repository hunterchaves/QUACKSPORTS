QUACK SPORTS 🏀

Plataforma inteligente para reserva de quadras esportivas, conectando atletas, arenas e administradores em uma experiência moderna, rápida e intuitiva.

📌 Sobre o Projeto

O QUACK SPORTS é um aplicativo Android desenvolvido para facilitar a busca, visualização e reserva de quadras esportivas em tempo real.

A plataforma conecta atletas a estabelecimentos esportivos através de uma interface moderna, oferecendo gerenciamento completo de reservas, controle administrativo e dashboards específicos para cada perfil de usuário.

👨‍💻 Equipe de Desenvolvimento
Integrante
Camille Santos
Emanuel Chaves
Higor Rosa
Victor Marcom
Carlos Farias
Fernando Pereira
🚀 Principais Funcionalidades
🏟️ Gestão de Quadras
Cadastro de arenas esportivas
Gerenciamento de quadras
Controle de horários disponíveis
Configuração de preços
📅 Sistema de Reservas
Consulta de disponibilidade em tempo real
Agendamento de horários
Simulação de pagamento
Histórico de reservas
📍 Geolocalização
Busca de quadras próximas
Integração com Google Maps
Visualização geográfica das arenas
👤 Controle de Usuários
Cadastro e autenticação
Login com Facebook
Perfis personalizados
📊 Dashboards Inteligentes
Painel para Atletas
Painel para Empresas
Painel Administrativo
🏗️ Arquitetura do Projeto

O projeto foi desenvolvido seguindo os princípios de MVVM (Model-View-ViewModel) e Clean Architecture, garantindo escalabilidade, organização e facilidade de manutenção.

UI (Compose)
    ↓
ViewModel
    ↓
Use Cases
    ↓
Repository
    ↓
Firebase / APIs Externas
📐 MVVM + Clean Architecture
Presentation Layer
Jetpack Compose
Navigation Compose
State Management
Domain Layer
Regras de negócio
Casos de uso
Entidades do sistema
Data Layer
Repositories
Firebase Services
APIs externas
🛠️ Stack Tecnológica
Tecnologia	Utilização
Kotlin 2.2.10	Linguagem principal
Jetpack Compose	Interface moderna
Material Design 3	Design System
Firebase Auth	Autenticação
Cloud Firestore	Banco de dados
Google Maps SDK	Geolocalização
Facebook SDK	Login Social
Coil	Carregamento de imagens
Coroutines	Processamento assíncrono
Flow	Fluxo reativo de dados
🎨 Interface e Experiência do Usuário
Jetpack Compose

Interface totalmente declarativa e moderna.

Material Design 3

Utilização dos componentes mais recentes recomendados pelo Google:

Scaffold
TopAppBar
NavigationBar
Cards
Dialogs
Snackbar
Responsividade

Layouts adaptados para diferentes tamanhos de tela e versões Android.

💾 Persistência de Dados
Cloud Firestore

Estrutura principal do banco de dados:

Users
├── Dados do usuário
├── Perfil
└── Histórico

Arenas
├── Informações da arena
├── Quadras
└── Horários

Reservations
├── Usuário
├── Quadra
├── Horário
└── Status
Firebase Authentication
Login por Email e Senha
Login Social com Facebook
Gerenciamento seguro de sessão
🔌 Integrações Externas
Google Maps SDK
Localização das arenas
Exibição em mapa
Georreferenciamento
Facebook SDK
Autenticação social
Firebase Services
Authentication
Firestore Database
Analytics
Coil
Carregamento eficiente de imagens
Cache otimizado
⚙️ Qualidade de Código
Kotlin Moderno

✔ Coroutines

✔ Flow

✔ StateFlow

✔ Null Safety

✔ Extensions

✔ Data Classes

✔ Sealed Classes

Organização
com.quacksports
│
├── data
├── domain
├── ui
├── navigation
├── viewmodel
├── repository
├── models
└── utils
🛡️ Tratamento de Erros

O aplicativo possui mecanismos para garantir maior estabilidade e experiência do usuário:

Tratamento de erros de rede
Controle de permissões de GPS
Validação de autenticação
Feedback visual para falhas
Tratamento de exceções Firebase
Correção de crashes de UI
📊 Diferenciais do Projeto

✅ Arquitetura escalável

✅ Firebase em tempo real

✅ Login social integrado

✅ Geolocalização com Google Maps

✅ Dashboards por perfil de usuário

✅ Interface moderna com Material Design 3

✅ Código modular e de fácil manutenção

📋 Ficha Técnica
Item	Descrição
Nome	QUACK SPORTS
Versão	1.0
Plataforma	Android
Linguagem	Kotlin
Arquitetura	MVVM + Clean Architecture
Banco de Dados	Cloud Firestore
Autenticação	Firebase Auth + Facebook Login
Target SDK	Android 15 (API 35)
🚀 Build e Publicação
Gerar APK Release
./gradlew assembleRelease
Gerar Android App Bundle (AAB)
./gradlew bundleRelease
🛠️ Instalação do Projeto
1. Clonar o repositório
git clone https://github.com/seu-repositorio/quack-sports.git
2. Configurar Firebase

Adicionar o arquivo:

google-services.json

na pasta:

app/
3. Configurar APIs

Inserir as chaves no arquivo:

res/values/strings.xml
<string name="google_maps_key">SUA_CHAVE</string>
<string name="facebook_app_id">SEU_APP_ID</string>
4. Executar
Sync Gradle
Run App
📈 Próximas Evoluções
Integração com gateway de pagamento real
Notificações Push
Sistema de avaliações
Programa de fidelidade
Chat entre usuários e arenas
Aplicativo iOS
📄 Licença

Projeto desenvolvido para fins acadêmicos e demonstração de conhecimentos em desenvolvimento Android nativo utilizando Kotlin, Jetpack Compose, Firebase e Clean Architecture.

📱 QUACK SPORTS

Reserve. Jogue. Conecte-se. 🏀⚽🎾🏐
Esse formato já fica com aparência de projeto profissional de GitHub e costuma impressionar bastante em apresentações e avaliações acadêmicas.

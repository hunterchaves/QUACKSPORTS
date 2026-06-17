# 🏀 QUACK SPORTS

<div align="center">

### Plataforma Inteligente para Reserva de Quadras Esportivas

**Encontre, reserve e jogue. Simples, rápido e inteligente.**

Conectando atletas, arenas esportivas e administradores em uma única plataforma moderna e eficiente.

---

![Kotlin](https://img.shields.io/badge/Kotlin-2.2.10-purple)
![Android](https://img.shields.io/badge/Android-15-green)
![Firebase](https://img.shields.io/badge/Firebase-Enabled-orange)
![Architecture](https://img.shields.io/badge/Architecture-MVVM%20%2B%20Clean%20Architecture-blue)
![Status](https://img.shields.io/badge/Status-Completed-success)

</div>

---

# 📌 Sobre o Projeto

O **QUACK SPORTS** é um aplicativo Android desenvolvido para facilitar a busca, visualização e reserva de quadras esportivas em tempo real.

A plataforma oferece uma experiência intuitiva para atletas e uma poderosa ferramenta de gestão para empresas e administradores, permitindo o gerenciamento completo de reservas, horários, quadras e usuários.

---

# 🚀 Funcionalidades

## 🏟️ Gestão de Arenas e Quadras

* Cadastro de arenas esportivas
* Gerenciamento de quadras
* Controle de horários disponíveis
* Configuração de preços
* Administração de disponibilidade

## 📅 Sistema de Reservas

* Consulta de horários em tempo real
* Agendamento rápido
* Simulação de pagamento
* Histórico completo de reservas
* Controle de status das reservas

## 📍 Geolocalização

* Busca de quadras próximas
* Integração com Google Maps
* Visualização geográfica das arenas
* Navegação até o local escolhido

## 👤 Gerenciamento de Usuários

* Cadastro e autenticação
* Login com Facebook
* Perfis personalizados
* Controle de permissões

## 📊 Dashboards Inteligentes

### Atleta

* Histórico de reservas
* Controle de agendamentos
* Consulta de quadras favoritas

### Empresa

* Gestão de arenas
* Controle de reservas
* Monitoramento de ocupação

### Administrador

* Gestão global da plataforma
* Controle de usuários
* Administração de conteúdos

---

# 🏗️ Arquitetura

O projeto segue os princípios de **MVVM (Model-View-ViewModel)** e **Clean Architecture**, garantindo:

✅ Escalabilidade

✅ Baixo acoplamento

✅ Facilidade de manutenção

✅ Código reutilizável

✅ Testabilidade

## Fluxo da Aplicação

```text
UI (Jetpack Compose)
        ↓
    ViewModel
        ↓
    Use Cases
        ↓
   Repository
        ↓
 Firebase / APIs
```

## Estrutura das Camadas

### 🎨 Presentation Layer

* Jetpack Compose
* Navigation Compose
* State Management
* ViewModels

### 🧠 Domain Layer

* Regras de negócio
* Casos de uso
* Entidades

### 💾 Data Layer

* Repositories
* Firebase Services
* APIs externas

---

# 🛠️ Tecnologias Utilizadas

| Tecnologia              | Descrição                |
| ----------------------- | ------------------------ |
| Kotlin 2.2.10           | Linguagem principal      |
| Jetpack Compose         | Interface moderna        |
| Material Design 3       | Design System            |
| Firebase Authentication | Autenticação             |
| Cloud Firestore         | Banco de dados           |
| Google Maps SDK         | Geolocalização           |
| Facebook SDK            | Login Social             |
| Coil                    | Carregamento de imagens  |
| Coroutines              | Processamento assíncrono |
| Flow / StateFlow        | Programação reativa      |

---

# 🎨 Interface e Experiência do Usuário

Desenvolvido utilizando **Jetpack Compose** e **Material Design 3**, proporcionando:

* Interface moderna
* Navegação intuitiva
* Alta performance
* Responsividade
* Componentes nativos Android

### Componentes Utilizados

* Scaffold
* TopAppBar
* NavigationBar
* Cards
* Dialogs
* Snackbars

---

# 💾 Banco de Dados

## Cloud Firestore

```text
Users
 ├── Dados do Usuário
 ├── Perfil
 └── Histórico

Arenas
 ├── Informações da Arena
 ├── Quadras
 └── Horários

Reservations
 ├── Usuário
 ├── Quadra
 ├── Horário
 └── Status
```

---

# 🔐 Autenticação

### Firebase Authentication

* Login por E-mail e Senha
* Login Social com Facebook
* Gerenciamento de Sessão
* Segurança integrada Firebase

---

# 🔌 Integrações Externas

## Google Maps SDK

* Localização das arenas
* Exibição em mapa
* Georreferenciamento

## Facebook SDK

* Login social

## Firebase Services

* Authentication
* Cloud Firestore
* Analytics

---

# ⚙️ Qualidade de Código

O projeto segue boas práticas modernas de desenvolvimento Android:

✔ Coroutines

✔ Flow

✔ StateFlow

✔ Null Safety

✔ Extensions

✔ Data Classes

✔ Sealed Classes

✔ Clean Architecture

✔ MVVM

---

# 📂 Organização do Projeto

```text
com.quacksports
│
├── data
├── domain
├── repository
├── ui
├── navigation
├── viewmodel
├── models
└── utils
```

---

# 🛡️ Tratamento de Erros

* Tratamento de falhas de rede
* Controle de permissões GPS
* Validação de autenticação
* Feedback visual ao usuário
* Tratamento de exceções Firebase
* Prevenção de crashes de interface

---

# ⭐ Diferenciais

* Arquitetura escalável
* Firebase em tempo real
* Geolocalização integrada
* Login social
* Dashboards específicos por perfil
* Material Design 3
* Código modular e organizado
* Fácil manutenção e expansão

---

# 📋 Ficha Técnica

| Item           | Descrição                      |
| -------------- | ------------------------------ |
| Nome           | QUACK SPORTS                   |
| Versão         | 1.0                            |
| Plataforma     | Android                        |
| Linguagem      | Kotlin                         |
| Arquitetura    | MVVM + Clean Architecture      |
| Banco de Dados | Cloud Firestore                |
| Autenticação   | Firebase Auth + Facebook Login |
| Target SDK     | Android 15 (API 35)            |

---

# 🚀 Instalação

## Clonar Repositório

```bash
git clone https://github.com/seu-repositorio/quack-sports.git
```

## Configurar Firebase

Adicione o arquivo:

```text
google-services.json
```

na pasta:

```text
app/
```

## Configurar APIs

No arquivo:

```xml
res/values/strings.xml
```

adicione:

```xml
<string name="google_maps_key">SUA_CHAVE</string>
<string name="facebook_app_id">SEU_APP_ID</string>
```

## Executar Projeto

```bash
./gradlew build
```

ou execute diretamente pelo Android Studio.

---

# 📦 Build

### APK Release

```bash
./gradlew assembleRelease
```

### Android App Bundle (AAB)

```bash
./gradlew bundleRelease
```

---

# 🔮 Próximas Evoluções

* Gateway de pagamento real
* Notificações Push
* Sistema de avaliações
* Programa de fidelidade
* Chat entre usuários e arenas
* Aplicativo iOS
* Integração com PIX

---

# 👨‍💻 Equipe de Desenvolvimento

* Camille Santos
* Emanuel Chaves
* Higor Rosa
* Victor Marcom
* Carlos Farias
* Fernando Pereira

---

# 📄 Licença

Projeto desenvolvido para fins acadêmicos e demonstração de conhecimentos em desenvolvimento Android nativo utilizando Kotlin, Jetpack Compose, Firebase e Clean Architecture.

---

<div align="center">

## 📱 QUACK SPORTS

### Reserve. Jogue. Conecte-se.

🏀 ⚽ 🎾 🏐

</div>

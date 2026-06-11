# QUACK SPORTS 🏀

Plataforma moderna para reserva de quadras esportivas, conectando atletas e estabelecimentos de forma ágil e intuitiva.

---
PARTICIPANTES 
Camille Santos
Emanuel Chaves
Higor Rosa
Victor Marcom
Carlos Farias 
Fernando Pereira 


## 🏗️ Desenvolvimento (5.0 pts)

### 📐 Arquitetura (MVVM / Clean Architecture)
- **MVVM:** Separação clara entre UI (Compose), Lógica de Negócio (ViewModels) e Dados (Repositories).
- **Clean Architecture:** Camadas de domínio independentes facilitando a manutenção.
- **State Management:** Uso de `StateFlow` e `State` do Compose para reatividade fluida.

### 💻 Qualidade do Código Kotlin
- **Coroutines & Flow:** Gerenciamento eficiente de chamadas assíncronas ao Firebase.
- **Null Safety:** Aproveitamento total da segurança de tipos do Kotlin para evitar crashes.
- **Modularidade:** Código organizado por responsabilidades (data, model, ui, viewmodel).

### 🎨 Interface UI/UX (Material Design 3)
- **Jetpack Compose:** UI 100% declarativa e moderna.
- **Material 3:** Componentes como `Scaffold`, `Cards` e `NavigationBars` seguindo as últimas diretrizes do Google.
- **Responsividade:** Layouts adaptáveis para diferentes tamanhos de tela.

### 💾 Banco de Dados e Persistência
- **Cloud Firestore:** Persistência NoSQL em tempo real para Arenas, Quadras, Reservas e Usuários.
- **Firebase Auth:** Gerenciamento seguro de sessões e perfis de usuários.

### 🔌 Integração com APIs
- **Google Maps SDK:** Localização geográfica e visualização de quadras próximas.
- **Facebook SDK:** Login social integrado para facilidade de acesso.
- **Firebase Services:** Analytics, Firestore e Auth integrados.
- **Coil:** Carregamento otimizado de imagens de rede.

### 🛡️ Tratamento de Erros
- **Feedback ao Usuário:** Toasts e estados de erro visuais para falhas de rede ou pagamento.
- **Robustez:** Tratamento de permissões de GPS e visibilidade de pacotes (Android 11 a 15).
- **Prevenção:** Correções de formatação de strings e crashes de UI (`NoSuchMethodError` resolvidos).

---

## 📄 Documentação (2.0 pts)

### 📖 README.md Completo
Este arquivo serve como guia completo para avaliação técnica e funcional do projeto.

### 📝 Relatório Técnico
- **Dashboards por Role:** Experiências distintas para Atletas, Donos de Arena (Company) e Administradores.
- **Gestão de Negócio:** Módulo para empresas gerenciarem quadras, preços, horários e visualizarem faturamento.
- **Fluxo de Reserva:** Sistema robusto de verificação de disponibilidade e simulação de pagamento.

---

## 🚀 Publicação (3.0 pts)

### 📦 APK/AAB Assinado
Configurado para geração de build de release otimizada:
```bash
./gradlew assembleRelease
```

### 🖼️ Ícone do Aplicativo
- **Adaptive Icons:** Ícone do app compatível com todas as variações de launchers do Android.

### 🌐 Disponibilidade
- Configuração de `google-services.json` integrada e permissões de manifesto validadas.

---

## 🛠️ Requisitos de Instalação

1. Clonar o repositório.
2. Inserir chaves de API (Maps/Facebook) em `res/values/strings.xml`.
3. Sincronizar Gradle e rodar via Android Studio.

 QUACK SPORTS 🏀
Plataforma moderna para reserva de quadras esportivas, conectando atletas e estabelecimentos de forma ágil e intuitiva.
🏗️ Desenvolvimento (5.0 pts)
📐 Arquitetura (MVVM / Clean Architecture)
•
MVVM: Separação clara entre UI (Compose), Lógica de Negócio (ViewModels) e Dados (Repositories).
•
Clean Architecture: Camadas de domínio independentes facilitando a manutenção.
•
State Management: Uso de StateFlow e State do Compose para reatividade fluida.
💻 Qualidade do Código Kotlin
•
Coroutines & Flow: Gerenciamento eficiente de chamadas assíncronas ao Firebase.
•
Null Safety: Aproveitamento total da segurança de tipos do Kotlin para evitar crashes.
•
Modularidade: Código organizado por responsabilidades (data, model, ui, viewmodel).
🎨 Interface UI/UX (Material Design 3)
•
Jetpack Compose: UI 100% declarativa e moderna.
•
Material 3: Componentes como Scaffold, Cards e NavigationBars seguindo as últimas diretrizes do Google.
•
Responsividade: Layouts adaptáveis para diferentes tamanhos de tela.
💾 Banco de Dados e Persistência
•
Cloud Firestore: Persistência NoSQL em tempo real para Arenas, Quadras, Reservas e Usuários.
•
Firebase Auth: Gerenciamento seguro de sessões e perfis de usuários.  
🔌 Integração com APIs
•
Google Maps SDK: Localização geográfica e visualização de quadras próximas.
•
Facebook SDK: Login social integrado para facilidade de acesso.
•
Firebase Services: Analytics, Firestore e Auth integrados.
•
Coil: Carregamento otimizado de imagens de rede.
🛡️ Tratamento de Erros
•
Feedback ao Usuário: Toasts e estados de erro visuais para falhas de rede ou pagamento.
•
Robustez: Tratamento de permissões de GPS e visibilidade de pacotes (Android 11 a 15).
•
Prevenção: Correções de formatação de strings e crashes de UI (NoSuchMethodError resolvidos).
📄 Documentação (2.0 pts)
📖 README.md Completo
Este arquivo serve como guia completo para avaliação técnica e funcional do projeto.
📝 Relatório Técnico
•
Dashboards por Role: Experiências distintas para Atletas, Donos de Arena (Company) e Administradores.
•
Gestão de Negócio: Módulo para empresas gerenciarem quadras, preços, horários e visualizarem faturamento.
•
Fluxo de Reserva: Sistema robusto de verificação de disponibilidade e simulação de pagamento.
🚀 Publicação (3.0 pts)
📦 APK/AAB Assinado
Configurado para geração de build de release otimizada:
./gradlew assembleRelease
🖼️ Ícone do Aplicativo
•
Adaptive Icons: Ícone do app compatível com todas as variações de launchers do Android.
🌐 Disponibilidade
•
Configuração de google-services.json integrada e permissões de manifesto validadas.
🛠️ Requisitos de Instalação
1.
Clonar o repositório.
2.
Inserir chaves de API (Maps/Facebook) em res/values/strings.xml.
3.
Sincronizar Gradle e rodar via Android Studio.
📋 Ficha Técnica
1. Informações Gerais
•
Nome: QUACK SPORTS
•
Versão: 1.0
•
Plataforma: Android (Nativo)
2. Stack Tecnológica
•
Linguagem: Kotlin 2.2.10
•
UI: Jetpack Compose (Material Design 3)
•
Arquitetura: MVVM + Clean Architecture
•
Banco de Dados: Cloud Firestore
•
Autenticação: Firebase Auth & Facebook Login
•
Target SDK: 35 (Android 15)
3. Funcionalidades
•
Busca e filtragem de quadras por esporte.
•
Localização via Google Maps.
•
Sistema de reservas com horários em tempo real.
•
Dashboards específicos para Usuários, Empresas e Admin.

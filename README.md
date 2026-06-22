# QUACK SPORTS 🦆🏀

Plataforma moderna para reserva de quadras esportivas, conectando atletas e estabelecimentos de forma ágil e intuitiva.

---

## 📋 Ficha Técnica

### 1. Informações Gerais
- **Nome:** QUACK SPORTS
- **Versão:** 1.0
- **Plataforma:** Android (Nativo)
- **Repositório:** [GitHub](https://github.com/hunterchaves/QUACKSPORTS.git)

### 2. Stack Tecnológica
- **Linguagem:** Kotlin 2.2.10
- **UI:** Jetpack Compose (Material Design 3)
- **Arquitetura:** MVVM + Clean Architecture
- **Banco de Dados:** Cloud Firestore
- **Autenticação:** Firebase Auth & Facebook Login
- **Target SDK:** 35 (Android 15)

---

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

### 💾 Banco de Dados e Persistência
- **Cloud Firestore:** Persistência NoSQL em tempo real para Arenas, Quadras, Reservas e Usuários.
- **Firebase Auth:** Gerenciamento seguro de sessões e perfis de usuários.

### 🔌 Integração com APIs
- **Google Maps SDK:** Localização geográfica e visualização de quadras próximas.
- **Facebook SDK:** Login social integrado para facilidade de acesso.
- **Coil:** Carregamento otimizado de imagens de rede.

### 🛡️ Tratamento de Erros
- **Feedback ao Usuário:** Toasts e estados de erro visuais para falhas de rede ou pagamento.
- **Robustez:** Tratamento de permissões de GPS e visibilidade de pacotes (Android 11 a 15).
- **Prevenção:** Correções de formatação de strings e crashes de UI (`NoSuchMethodError` resolvidos).

---

## 📝 Relatório Técnico Detalhado

### 1. Disponibilidade e Slots
A lógica em `Availability.kt` processa o horário de funcionamento e reservas confirmadas para gerar slots de 1h, garantindo que não haja conflitos de horários.

### 2. Gestão por Papéis (Roles)
O sistema identifica se o usuário é **Atleta**, **Empresa** ou **Admin**, redirecionando para dashboards específicos. Empresas possuem ferramentas exclusivas para gestão de faturamento e cadastro de quadras.

### 3. Persistência em Tempo Real
Utilizamos listeners do Firestore para garantir que, se uma quadra for reservada, a disponibilidade seja atualizada instantaneamente para todos os outros usuários.

---

## 🚀 Publicação (3.0 pts)

### 📦 APK/AAB Assinado
Configurado para geração de build de release otimizada:
```bash
./gradlew assembleRelease
```

### 🖼️ Ícone do Aplicativo
- **Adaptive Icons:** Ícones configurados para compatibilidade total com launchers Android modernos.

### 🌐 Disponibilidade
- Arquivo `google-services.json` integrado e permissões de manifesto validadas para Android 15.

---

## 🛠️ Requisitos de Instalação
1. Clonar o repositório.
2. Inserir chaves de API em `res/values/strings.xml`.
3. Sincronizar Gradle e rodar via Android Studio.

---

## 📤 Instruções de Submissão
- **Formação de Grupos:** Conforme definido em sala de aula.
- **Prazo Final:** 12/06/2026.
- **Formato:** Link do repositório GitHub ([https://github.com/hunterchaves/QUACKSPORTS.git](https://github.com/hunterchaves/QUACKSPORTS.git)) e arquivo do pacote (.apk/.aab) via plataforma oficial da instituição.

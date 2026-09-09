# 📋 Diário de Obra

> Aplicativo Android para registro e acompanhamento diário de obras de construção civil.

Permite que o responsável por uma obra registre, dia a dia, o andamento dos trabalhos — atividades realizadas, condições climáticas, número de trabalhadores presentes e fotos do progresso — tudo organizado por obra cadastrada. Funciona **100% offline**, com todos os dados salvos localmente no próprio dispositivo.

---

## ✨ Funcionalidades

- 🏗️ Cadastro, edição, listagem e exclusão de **obras** (nome, endereço, data de início)
- 📅 Cadastro, edição, listagem e exclusão de **registros diários** por obra
- ☀️ Registro de clima e número de trabalhadores presentes
- 📸 Anexação de fotos aos registros diários
- 📄 Geração de relatórios em PDF
- ✅ Validações básicas de formulário

## 📱 Tecnologias

<p>
  <img src="https://img.shields.io/badge/Kotlin-7F52FF?style=flat&logo=kotlin&logoColor=white" alt="Kotlin"/>
  <img src="https://img.shields.io/badge/Android-3DDC84?style=flat&logo=android&logoColor=white" alt="Android"/>
  <img src="https://img.shields.io/badge/Room-4285F4?style=flat&logo=sqlite&logoColor=white" alt="Room"/>
  <img src="https://img.shields.io/badge/Android%20Studio-146EF5?style=flat&logo=androidstudio&logoColor=white" alt="Android Studio"/>
</p>

- **Kotlin** — linguagem principal
- **Android Studio** — ambiente de desenvolvimento
- **Room** — persistência local de dados sobre SQLite

## 🚀 Como rodar o projeto

1. Clone o repositório
   ```bash
   git clone https://github.com/seu-usuario/diario-de-obra.git
   ```
2. Abra o projeto no Android Studio
3. Aguarde o Gradle sincronizar as dependências
4. Rode em um emulador ou dispositivo físico (`Shift + F10`)

## 🗂️ Estrutura do projeto

```
app/
 ├── data/           # Entities, DAOs e AppDatabase (Room)
 ├── repository/     # Camada de acesso aos dados
 ├── ui/              # Telas (Compose/Views)
 └── viewmodel/       # Lógica de apresentação
```

## 👥 Equipe

| Integrante | Frente principal |
|---|---|
| José Victor Silvestre de Souza | Banco de Dados |
| Pedro Henrique Borges e Silva | Back-end |
| Randson Bredley Bezerra da Silva | Front-end |

## 🎓 Contexto acadêmico

Projeto desenvolvido para a disciplina de **Gerência de Projetos**, do curso de Bacharelado em Engenharia de Software — **IFPE Campus Belo Jardim**.

---

<p align="center">Feito com 💛 por José Victor, Pedro Henrique Borges e Randson Bredley</p>

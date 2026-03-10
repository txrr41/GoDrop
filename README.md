# GoDrop 🚀

Plataforma de dropshipping com sistema de lojas personalizadas para parceiros (Droppers), pagamentos integrados via Stripe, geração de relatórios em PDF e painel administrativo completo.

---

## 📋 Sumário

- [Visão Geral](#visão-geral)
- [Tecnologias](#tecnologias)
- [Funcionalidades](#funcionalidades)
- [Arquitetura](#arquitetura)
- [Instalação e Configuração](#instalação-e-configuração)
- [Variáveis de Ambiente](#variáveis-de-ambiente)
- [Endpoints da API](#endpoints-da-api)
- [Sistema de Níveis Dropper](#sistema-de-níveis-dropper)
- [Fluxo de Pagamento](#fluxo-de-pagamento)
- [Estrutura do Projeto](#estrutura-do-projeto)

---

## Visão Geral

O GoDrop é um sistema completo de dropshipping que permite:

- **Clientes** comprarem produtos diretamente ou através de lojas de parceiros
- **Droppers** (parceiros) criarem suas próprias lojas personalizadas com margens configuráveis
- **Admins** gerenciarem pedidos, usuários, relatórios e aprovações de droppers
- Processamento de pagamentos via cartão, PIX e boleto com split automático de comissões

---

## Tecnologias

### Backend
- **Java 17** + **Spring Boot 3**
- **Spring Security** com JWT e OAuth2 (Google Login)
- **Spring Data JPA** + **Hibernate**
- **PostgreSQL**
- **Stripe API** — Pagamentos e Stripe Connect para split de comissões
- **OpenHTMLtoPDF** — Geração de relatórios em PDF
- **Groq API (LLaMA 3.3)** — Geração de temas de loja com IA
- **JavaMail** — Envio de e-mails transacionais

### Frontend
- **React** + **Vite**
- **Tailwind CSS**

---

## Funcionalidades

### 🛒 Loja e Compras
- Catálogo de produtos com filtros por categoria
- Carrinho de compras com suporte a múltiplos itens
- Checkout com dados do comprador e endereço de entrega
- Pagamento via **cartão de crédito/débito**, **PIX** e **boleto**
- E-mails automáticos de confirmação de pedido e rastreamento

### 👤 Autenticação
- Cadastro e login com e-mail/senha
- Login social via **Google (OAuth2)**
- Sessão gerenciada por **JWT em cookie HttpOnly**

### 📦 Gestão de Pedidos
- Histórico de pedidos por cliente
- Acompanhamento de status em tempo real
- Fluxo de status: `PENDING → PAYMENT_APPROVED → PROCESSING → SHIPPED → DELIVERED`
- Código de rastreamento enviado por e-mail ao despachar

### 🏪 Lojas de Droppers
- Cada dropper pode criar sua própria loja personalizada
- **Geração de tema com IA** (nome, slogan, paleta de cores) via Groq/LLaMA
- Slug único para URL pública da loja (ex: `/loja/minha-loja`)
- Configuração de preço customizado por produto (margem livre acima do custo)
- Ativação/desativação da loja pelo próprio dropper

### 💸 Sistema de Comissões (Stripe Connect)
- Split automático de pagamentos: a margem do dropper é transferida diretamente para sua conta Stripe
- Registro de comissão por pedido com auditoria completa
- Descontos de nível absorvidos pela plataforma (dropper sempre recebe a margem cheia)

### 📊 Painel Administrativo
- Dashboard com KPIs: receita, pedidos, clientes, produtos
- Gerenciamento de produtos (CRUD)
- Gerenciamento de pedidos com atualização de status e despacho
- Aprovação/rejeição de cadastros de droppers
- Gerenciamento de usuários staff com controle de permissões e suspensão
- Relatórios em PDF:
  - Comissões por período
  - Top produtos mais vendidos
  - Estoque crítico

### 🎁 Ofertas e Descontos
- Criação de ofertas por **porcentagem** ou **valor fixo**
- Vinculação por produto ou categoria
- Agendamento automático de ativação/expiração (job a cada 1 minuto)
- Limite de estoque por oferta

---

## Arquitetura

```
┌─────────────────────────────────────────────────────────┐
│                      Frontend (React)                    │
│                   localhost:5173                         │
└────────────────────────┬────────────────────────────────┘
                         │ HTTP / Cookie JWT
┌────────────────────────▼────────────────────────────────┐
│                  Spring Boot API                         │
│                   localhost:8080                         │
│                                                          │
│  ┌──────────────┐  ┌──────────────┐  ┌────────────────┐ │
│  │  Controllers │  │   Services   │  │  Repositories  │ │
│  └──────────────┘  └──────────────┘  └───────┬────────┘ │
│                                               │          │
└───────────────────────────────────────────────┼──────────┘
                                                │
              ┌─────────────────────────────────┤
              │                                 │
   ┌──────────▼──────────┐         ┌────────────▼─────────┐
   │     PostgreSQL       │         │   Stripe API          │
   │     localhost:5432   │         │   (Payments + Connect)│
   └─────────────────────┘         └──────────────────────┘
```

---

## Instalação e Configuração

### Pré-requisitos

- Java 17+
- Maven 3.8+
- PostgreSQL 14+
- Node.js 18+ (para o frontend)
- Conta Stripe (modo teste)
- Conta Google (para OAuth2, opcional)
- Chave Groq API (para geração de tema com IA, opcional)

### 1. Clone o repositório

```bash
git clone https://github.com/seu-usuario/godrop.git
cd godrop
```

### 2. Configure o banco de dados

```sql
CREATE DATABASE godrop;
```

### 3. Configure as variáveis de ambiente

Crie um arquivo `.env` ou configure as variáveis no seu sistema (veja a seção abaixo).

### 4. Execute o backend

```bash
./mvnw spring-boot:run
```

O servidor sobe em `http://localhost:8080`.

### 5. Execute o frontend

```bash
cd frontend
npm install
npm run dev
```

O frontend sobe em `http://localhost:5173`.

---

## Variáveis de Ambiente

| Variável | Descrição |
|---|---|
| `DB_PASSWORD` | Senha do PostgreSQL |
| `JWT_SECRET` | Chave secreta para assinar tokens JWT (mín. 32 chars) |
| `STRIPE_SECRET_KEY` | Chave secreta da API Stripe |
| `STRIPE_WEBHOOK_SECRET` | Secret do webhook Stripe |
| `GMAIL_PASSWORD` | Senha de app do Gmail para envio de e-mails |
| `GOOGLE_CLIENT_ID` | Client ID do OAuth2 Google |
| `GOOGLE_SECRET_KEY` | Client Secret do OAuth2 Google |
| `GROQ_API_KEY` | Chave da API Groq (geração de tema com IA) |

---

## Endpoints da API

### Autenticação
| Método | Rota | Descrição |
|---|---|---|
| `POST` | `/auth/register` | Cadastro de novo usuário |
| `POST` | `/auth/login` | Login com e-mail e senha |
| `GET` | `/auth/me` | Dados do usuário autenticado |
| `POST` | `/auth/logout` | Logout (limpa cookie) |

### Produtos
| Método | Rota | Descrição |
|---|---|---|
| `GET` | `/produtos` | Listar produtos ativos |
| `GET` | `/produtos/{id}` | Buscar produto por ID |
| `POST` | `/produtos` | Criar produto (autenticado) |
| `PUT` | `/produtos/{id}` | Atualizar produto |
| `DELETE` | `/produtos/{id}` | Remover produto (soft delete) |

### Pedidos
| Método | Rota | Descrição |
|---|---|---|
| `GET` | `/api/orders` | Listar pedidos do usuário |
| `GET` | `/api/orders/{id}` | Buscar pedido por ID |

### Pagamentos
| Método | Rota | Descrição |
|---|---|---|
| `POST` | `/api/payments/create` | Criar intenção de pagamento |
| `POST` | `/api/payments/{id}/refund` | Reembolsar pagamento |

### Lojas de Droppers
| Método | Rota | Descrição |
|---|---|---|
| `POST` | `/api/dropper/store` | Criar loja |
| `GET` | `/api/dropper/store/me` | Minha loja |
| `PUT` | `/api/dropper/store` | Atualizar loja |
| `PATCH` | `/api/dropper/store/toggle` | Ativar/desativar loja |
| `POST` | `/api/dropper/store/ai-theme` | Gerar tema com IA |
| `GET` | `/loja/{slug}` | Loja pública por slug |

### Admin
| Método | Rota | Descrição |
|---|---|---|
| `GET` | `/api/admin/orders` | Todos os pedidos |
| `PATCH` | `/api/admin/orders/{id}/status` | Atualizar status do pedido |
| `POST` | `/api/admin/orders/{id}/ship` | Despachar pedido com rastreio |
| `GET` | `/api/admin/orders/stats` | Estatísticas de pedidos |
| `GET` | `/api/admin/droppers` | Listar droppers |
| `PATCH` | `/api/admin/droppers/{id}/approve` | Aprovar dropper |
| `PATCH` | `/api/admin/droppers/{id}/reject` | Rejeitar dropper |
| `GET` | `/api/admin/users` | Listar usuários staff |
| `POST` | `/api/admin/users` | Criar usuário staff |
| `PATCH` | `/api/admin/users/{id}/suspend` | Suspender/reativar usuário |

### Relatórios
| Método | Rota | Descrição |
|---|---|---|
| `GET` | `/dashboard/stats` | Estatísticas do dashboard |
| `GET` | `/dashboard/relatorio/comissoes` | Relatório de comissões (PDF) |
| `GET` | `/dashboard/relatorio/top-produtos` | Top produtos (PDF) |
| `GET` | `/dashboard/relatorio/estoque-critico` | Estoque crítico (PDF) |

---

## Sistema de Níveis Dropper

Os droppers acumulam margem de vendas e sobem de nível, ganhando descontos progressivos nos produtos:

| Nível | Vendas Acumuladas | Desconto |
|---|---|---|
| 🥉 Bronze | R$ 0 – R$ 4.999 | 5% |
| 🥈 Silver | R$ 5.000 – R$ 19.999 | 10% |
| 🥇 Gold | R$ 20.000 – R$ 59.999 | 15% |
| 💎 Platinum | R$ 60.000 – R$ 149.999 | 22% |
| 💠 Diamond | R$ 150.000+ | 30% |

> O desconto é aplicado sobre o valor total da compra do dropper. A plataforma absorve o desconto — o dropper sempre recebe sua margem cheia.

---

## Fluxo de Pagamento

```
Cliente finaliza compra
        │
        ▼
PaymentController cria Order (PENDING)
        │
        ▼
StripePaymentService cria PaymentIntent
  (com TransferData para conta do dropper, se houver split)
        │
        ▼
Cliente paga no frontend (Stripe Elements)
        │
        ▼
Stripe dispara webhook → /api/webhooks/stripe
        │
   ┌────┴─────┐
   │ Sucesso  │ → Order: PAYMENT_APPROVED
   │          │ → Registra comissão
   │          │ → Atualiza nível do dropper
   │          │ → Envia e-mail de confirmação
   │          │
   │ Falha    │ → Order: PAYMENT_FAILED
   └──────────┘
```

---

## Estrutura do Projeto

```
src/main/java/com/project/shiphub/
├── config/                  # Configurações (CORS, Stripe, Jackson)
├── controller/
│   ├── admin/               # Controllers do painel admin
│   ├── auth/                # Login, registro, dropper
│   ├── offer/               # Ofertas e descontos
│   ├── order/               # Pedidos do cliente
│   ├── payment/             # Pagamentos e Stripe Connect
│   ├── product/             # Produtos
│   ├── report/              # Dashboard e relatórios PDF
│   ├── store/               # Lojas de droppers
│   └── webhook/             # Webhooks Stripe
├── dto/                     # Data Transfer Objects
├── model/                   # Entidades JPA
│   ├── auth/                # User, DropperProfile, AdminProfile
│   ├── offer/               # Offer
│   ├── order/               # Order, OrderItem
│   ├── payment/             # Payment, DropperCommission
│   ├── product/             # Product
│   └── store/               # DropperStore, StoreProduct
├── repository/              # Interfaces JpaRepository
├── security/                # JWT, Spring Security, Aspect de permissões
└── service/                 # Lógica de negócio
```

---

## Roles de Usuário

| Role | Descrição |
|---|---|
| `CUSTOMER` | Comprador padrão |
| `DROPPER` | Parceiro com loja própria |
| `STAFF` | Funcionário com permissões configuráveis |
| `OWNER` | Administrador com acesso total |

---

## Licença

Este projeto é privado. Todos os direitos reservados.

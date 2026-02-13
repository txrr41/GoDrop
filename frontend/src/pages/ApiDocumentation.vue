<template>
  <div class="docs-wrapper">
    <header class="docs-header py-16 bg-grey-lighten-4">
      <v-container>
        <v-row align="center">
          <v-col cols="12" md="7">
            <v-chip color="primary" variant="flat" class="mb-4 px-4 font-weight-bold" size="small">
              GUIA DO DESENVOLVEDOR
            </v-chip>
            <h1 class="text-h3 font-weight-black mb-4">Integrando com a Shiphub API</h1>
            <p class="text-h6 text-grey-darken-1 font-weight-regular" style="max-width: 600px;">
              Aprenda como escalar seu e-commerce consumindo nosso estoque global,
              com automação de pedidos e split de pagamento nativo.
            </p>
          </v-col>
          <v-col cols="12" md="5" class="d-none d-md-block">
            <v-img
                src="https://images.unsplash.com/photo-1551434678-e076c223a692?auto=format&fit=crop&q=80&w=800"
                alt="Ambiente de Desenvolvimento"
                class="rounded-xl elevation-12"
                cover
                height="280"
            ></v-img>
          </v-col>
        </v-row>
      </v-container>
    </header>

    <v-container class="py-12">
      <v-row>
        <v-col cols="12" md="3" class="d-none d-md-block">
          <div class="sticky-nav">
            <h4 class="text-overline mb-4 text-primary">Nesta página</h4>
            <nav class="doc-nav">
              <a href="#workflow" class="active">Fluxo de Operação</a>
              <a href="#auth">Autenticação</a>
              <a href="#inventory">Consumo de Estoque</a>
              <a href="#orders">Split & Envio</a>
            </nav>
          </div>
        </v-col>

        <v-col cols="12" md="9">
          <section id="workflow" class="mb-16">
            <h2 class="text-h4 font-weight-bold mb-8">Como funciona o ecossistema</h2>

            <v-timeline side="end" align="start">
              <v-timeline-item dot-color="primary" size="small">
                <template v-slot:opposite>
                  <span class="text-caption font-weight-bold text-primary">PASSO 01</span>
                </template>
                <div>
                  <h3 class="text-h6 font-weight-bold">Conexão via API</h3>
                  <p class="text-body-2 text-grey-darken-1">
                    Sua loja consome nosso endpoint de produtos. Você escolhe quais itens deseja
                    listar em sua vitrine sem precisar comprar nada antecipadamente.
                  </p>
                </div>
              </v-timeline-item>

              <v-timeline-item dot-color="primary" size="small">
                <template v-slot:opposite>
                  <span class="text-caption font-weight-bold text-primary">PASSO 02</span>
                </template>
                <div>
                  <h3 class="text-h6 font-weight-bold">Venda e Notificação</h3>
                  <p class="text-body-2 text-grey-darken-1">
                    Quando um cliente compra no seu site, você envia os dados do pedido
                    para nossa API. Nosso sistema identifica a transação automaticamente.
                  </p>
                </div>
              </v-timeline-item>

              <v-timeline-item dot-color="primary" size="small">
                <template v-slot:opposite>
                  <span class="text-caption font-weight-bold text-primary">PASSO 03</span>
                </template>
                <div>
                  <h3 class="text-h6 font-weight-bold">Split de Pagamento Automático</h3>
                  <p class="text-body-2 text-grey-darken-1">
                    O valor pago pelo cliente é dividido: sua margem de lucro vai para sua conta
                    e o custo do produto/frete vem para a Shiphub. Tudo em tempo real.
                  </p>
                </div>
              </v-timeline-item>

              <v-timeline-item dot-color="success" size="small">
                <template v-slot:opposite>
                  <span class="text-caption font-weight-bold text-success">PASSO 04</span>
                </template>
                <div>
                  <h3 class="text-h6 font-weight-bold text-success">Envio Direto (Fulfillment)</h3>
                  <p class="text-body-2 text-grey-darken-1">
                    Nós processamos o pacote no nosso centro de distribuição e enviamos com
                    etiqueta de rastreio direto para o endereço do seu cliente.
                  </p>
                </div>
              </v-timeline-item>
            </v-timeline>
          </section>

          <section id="inventory" class="mb-16">
            <h2 class="text-h4 font-weight-bold mb-4">Consumindo o Estoque</h2>
            <p class="text-body-1 text-grey-darken-1 mb-6">
              Utilize o método GET para listar os produtos disponíveis para sincronização.
            </p>

            <v-card theme="dark" class="rounded-lg overflow-hidden border-0 shadow-none">
              <div class="d-flex bg-grey-darken-4 px-4 py-2 align-center">
                <span class="text-caption font-weight-bold text-grey">GET /v1/inventory</span>
                <v-spacer></v-spacer>
                <v-icon size="small" color="grey">mdi-content-copy</v-icon>
              </div>
              <v-card-text class="pa-0">
                <pre class="code-block"><code>{
  "status": "success",
  "data": [
    {
      "id": "prod_8823",
      "name": "Smartwatch Pro Max",
      "stock_available": 150,
      "cost_price": 45.90,
      "suggested_price": 129.90,
      "shipping_methods": ["express", "standard"]
    }
  ]
}</code></pre>
              </v-card-text>
            </v-card>
          </section>

          <section id="orders" class="mb-16">
            <v-card class="pa-8 bg-blue-darken-4 text-white rounded-xl" elevation="8">
              <v-row align="center">
                <v-col cols="12" md="8">
                  <h3 class="text-h5 font-weight-bold mb-2">Transparência Total no Split</h3>
                  <p class="text-body-2 opacity-80">
                    Nossa integração com gateways de pagamento garante que você receba
                    seu lucro no momento da venda. Sem esperas, sem faturas manuais.
                  </p>
                </v-col>
                <v-col cols="12" md="4" class="text-center">
                  <v-btn color="white" variant="flat" block class="text-primary font-weight-bold">
                    ABRIR CONTA VENDEDOR
                  </v-btn>
                </v-col>
              </v-row>
            </v-card>
          </section>
        </v-col>
      </v-row>
    </v-container>

    <v-footer class="bg-grey-lighten-4 py-10 border-t">
      <v-container class="text-center">
        <p class="text-caption text-grey-darken-1 mb-0">
          Shiphub API &copy; 2026 - Documentação Oficial para Parceiros Dropshipping.
        </p>
      </v-container>
    </v-footer>
  </div>
</template>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=JetBrains+Mono&family=Inter:wght@400;600;700;800&display=swap');

.docs-wrapper {
  font-family: 'Inter', sans-serif;
  background-color: #fff;
}

.docs-header {
  border-bottom: 1px solid rgba(0,0,0,0.05);
}

.sticky-nav {
  position: sticky;
  top: 40px;
}

.doc-nav {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.doc-nav a {
  text-decoration: none;
  color: #666;
  font-size: 0.95rem;
  font-weight: 500;
  transition: all 0.2s;
  border-left: 2px solid transparent;
  padding-left: 16px;
}

.doc-nav a:hover {
  color: #0061ff;
}

.doc-nav a.active {
  color: #0061ff;
  border-left: 2px solid #0061ff;
  font-weight: 700;
}

.code-block {
  padding: 24px;
  background: #1e1e1e;
  color: #d4d4d4;
  font-family: 'JetBrains Mono', monospace;
  font-size: 0.9rem;
  line-height: 1.5;
  margin: 0;
}

.opacity-80 {
  opacity: 0.8;
}

/* Timeline Customization */
:deep(.v-timeline-item__body) {
  padding-bottom: 40px;
}
</style>
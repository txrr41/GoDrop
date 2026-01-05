// Estado da aplicação
let isLoggedIn = false
let currentUser = null
let cartItems = [] // Added cart state
const notifications = [] // Added notifications state

// Elementos
const sidebar = document.getElementById("sidebar")
const sidebarToggle = document.getElementById("sidebarToggle")
const mobileMenuBtn = document.getElementById("mobileMenuBtn")
const overlay = document.getElementById("overlay")
const themeToggle = document.getElementById("themeToggle")
const mainContent = document.getElementById("mainContent")

// Auth elements
const authButtons = document.getElementById("authButtons")
const userMenu = document.getElementById("userMenu")
const userAvatar = document.getElementById("userAvatar")
const dropdownMenu = document.getElementById("dropdownMenu")
const btnLogin = document.getElementById("btnLogin")
const btnRegister = document.getElementById("btnRegister")
const btnLogout = document.getElementById("btnLogout")
const userName = document.getElementById("userName")

const cartBtn = document.getElementById("cartBtn")
const cartBadge = document.getElementById("cartBadge")
const notificationsBtn = document.getElementById("notificationsBtn")
const notificationBadge = document.getElementById("notificationBadge")
const notificationsDropdown = document.getElementById("notificationsDropdown")
const navHistorico = document.getElementById("navHistorico")

// Modals
const loginModal = document.getElementById("loginModal")
const registerModal = document.getElementById("registerModal")
const loginForm = document.getElementById("loginForm")
const registerForm = document.getElementById("registerForm")
const switchToRegister = document.getElementById("switchToRegister")
const switchToLogin = document.getElementById("switchToLogin")
const productModal = document.getElementById("productModal")
const purchaseModal = document.getElementById("purchaseModal")
const productForm = document.getElementById("productForm")
const purchaseForm = document.getElementById("purchaseForm")

// Navegação
const navItems = document.querySelectorAll(".nav-item")

// Inicialização
document.addEventListener("DOMContentLoaded", () => {
  initTheme()
  initAuth()
  initNavigation()
  initSidebar()
  initModals()
  initCart() // Initialize cart
  initNotifications() // Initialize notifications
  addTooltips()
})

// Tema Dark/Light
function initTheme() {
  const savedTheme = localStorage.getItem("theme") || "light"
  document.documentElement.setAttribute("data-theme", savedTheme)
  updateThemeIcon(savedTheme)

  themeToggle.addEventListener("click", toggleTheme)
}

function toggleTheme() {
  const currentTheme = document.documentElement.getAttribute("data-theme")
  const newTheme = currentTheme === "light" ? "dark" : "light"

  document.documentElement.setAttribute("data-theme", newTheme)
  localStorage.setItem("theme", newTheme)
  updateThemeIcon(newTheme)
}

function updateThemeIcon(theme) {
  const icon = themeToggle.querySelector("i")
  icon.className = theme === "light" ? "ri-moon-line" : "ri-sun-line"
}

// Autenticação
function initAuth() {
  // Verificar se há usuário logado no localStorage
  const savedUser = localStorage.getItem("currentUser")
  if (savedUser) {
    currentUser = JSON.parse(savedUser)
    isLoggedIn = true
    showUserMenu()
  }

  // Event listeners para botões de auth
  btnLogin.addEventListener("click", () => openModal("loginModal"))
  btnRegister.addEventListener("click", () => openModal("registerModal"))
  btnLogout.addEventListener("click", logout)

  // Toggle do dropdown
  userAvatar.addEventListener("click", toggleDropdown)

  // Fechar dropdown ao clicar fora
  document.addEventListener("click", (e) => {
    if (!userMenu.contains(e.target)) {
      dropdownMenu.classList.add("hidden")
      notificationsDropdown.classList.add("hidden")
    }
  })
}

function showUserMenu() {
  authButtons.classList.add("hidden")
  userMenu.classList.remove("hidden")
  userName.textContent = currentUser.name
  navHistorico.classList.remove("hidden") // Show history link when logged in
  updateCartBadge() // Update cart count
  updateNotificationBadge() // Update notification count
}

function showAuthButtons() {
  userMenu.classList.add("hidden")
  authButtons.classList.remove("hidden")
  navHistorico.classList.add("hidden") // Hide history link when logged out
}

function toggleDropdown(e) {
  e.stopPropagation()
  dropdownMenu.classList.toggle("hidden")
  notificationsDropdown.classList.add("hidden") // Close notifications when opening user menu
}

function logout() {
  isLoggedIn = false
  currentUser = null
  cartItems = [] // Clear cart on logout
  localStorage.removeItem("currentUser")
  localStorage.removeItem("cartItems")
  showAuthButtons()
  dropdownMenu.classList.add("hidden")
  updateCartBadge()
}

function initCart() {
  // Load cart from localStorage
  const savedCart = localStorage.getItem("cartItems")
  if (savedCart) {
    cartItems = JSON.parse(savedCart)
    updateCartBadge()
  }

  // Cart button click
  cartBtn.addEventListener("click", (e) => {
    e.stopPropagation()
    showPage("carrinho")
    navItems.forEach((nav) => nav.classList.remove("active"))
    renderCart()
  })
}

function addToCart(productId) {
  if (!isLoggedIn) {
    alert("Faça login para adicionar produtos ao carrinho")
    openModal("loginModal")
    return
  }

  // TODO: Get product data from database
  const product = {
    id: productId,
    name: "Produto Exemplo",
    price: 99.9,
    quantity: 1,
    image: "/placeholder.svg?height=100&width=100",
  }

  // Check if product already in cart
  const existingItem = cartItems.find((item) => item.id === productId)
  if (existingItem) {
    existingItem.quantity++
  } else {
    cartItems.push(product)
  }

  localStorage.setItem("cartItems", JSON.stringify(cartItems))
  updateCartBadge()

  // Show feedback
  showNotification("Produto adicionado ao carrinho!")
}

function buyNow(productId) {
  if (!isLoggedIn) {
    alert("Faça login para realizar a compra")
    openModal("loginModal")
    return
  }

  // Add to cart and go to purchase
  addToCart(productId)
  showPage("carrinho")
  navItems.forEach((nav) => nav.classList.remove("active"))
  renderCart()
}

function removeFromCart(productId) {
  cartItems = cartItems.filter((item) => item.id !== productId)
  localStorage.setItem("cartItems", JSON.stringify(cartItems))
  updateCartBadge()
  renderCart()
}

function updateCartQuantity(productId, change) {
  const item = cartItems.find((item) => item.id === productId)
  if (item) {
    item.quantity += change
    if (item.quantity <= 0) {
      removeFromCart(productId)
    } else {
      localStorage.setItem("cartItems", JSON.stringify(cartItems))
      renderCart()
    }
  }
}

function updateCartBadge() {
  const totalItems = cartItems.reduce((sum, item) => sum + item.quantity, 0)
  if (totalItems > 0) {
    cartBadge.textContent = totalItems
    cartBadge.classList.remove("hidden")
  } else {
    cartBadge.classList.add("hidden")
  }
}

function renderCart() {
  const cartItemsList = document.getElementById("cartItemsList")
  const btnFinalizePurchase = document.getElementById("btnFinalizePurchase")

  if (cartItems.length === 0) {
    cartItemsList.innerHTML = `
      <div class="empty-state">
        <i class="ri-shopping-cart-line"></i>
        <p>Seu carrinho está vazio</p>
        <small>Adicione produtos para continuar</small>
      </div>
    `
    btnFinalizePurchase.disabled = true
    updateCartSummary(0)
    return
  }

  btnFinalizePurchase.disabled = false

  let html = ""
  cartItems.forEach((item) => {
    const itemTotal = item.price * item.quantity
    html += `
      <div class="cart-item">
        <img src="${item.image}" alt="${item.name}" class="cart-item-image">
        <div class="cart-item-info">
          <h4>${item.name}</h4>
          <p class="cart-item-price">R$ ${item.price.toFixed(2)}</p>
        </div>
        <div class="cart-item-controls">
          <button class="btn-icon" onclick="updateCartQuantity(${item.id}, -1)">
            <i class="ri-subtract-line"></i>
          </button>
          <span class="cart-item-quantity">${item.quantity}</span>
          <button class="btn-icon" onclick="updateCartQuantity(${item.id}, 1)">
            <i class="ri-add-line"></i>
          </button>
        </div>
        <div class="cart-item-total">
          <p>R$ ${itemTotal.toFixed(2)}</p>
          <button class="btn-icon" onclick="removeFromCart(${item.id})">
            <i class="ri-delete-bin-line"></i>
          </button>
        </div>
      </div>
    `
  })

  cartItemsList.innerHTML = html

  const subtotal = cartItems.reduce((sum, item) => sum + item.price * item.quantity, 0)
  updateCartSummary(subtotal)
}

function updateCartSummary(subtotal) {
  document.getElementById("cartSubtotal").textContent = `R$ ${subtotal.toFixed(2)}`
  document.getElementById("cartTotal").textContent = `R$ ${subtotal.toFixed(2)}`
}

function finalizePurchase() {
  if (cartItems.length === 0) {
    alert("Seu carrinho está vazio")
    return
  }
  openModal("purchaseModal")
}

function initNotifications() {
  // Load notifications from localStorage or database
  // TODO: Fetch from database

  notificationsBtn.addEventListener("click", (e) => {
    e.stopPropagation()
    notificationsDropdown.classList.toggle("hidden")
    dropdownMenu.classList.add("hidden")
    renderNotifications()
  })
}

function updateNotificationBadge() {
  // TODO: Get unread count from database
  const unreadCount = notifications.filter((n) => !n.read).length
  if (unreadCount > 0) {
    notificationBadge.textContent = unreadCount
    notificationBadge.classList.remove("hidden")
  } else {
    notificationBadge.classList.add("hidden")
  }
}

function renderNotifications() {
  const notificationsList = document.getElementById("notificationsList")

  // TODO: Fetch from database
  if (notifications.length === 0) {
    notificationsList.innerHTML = `
      <div class="empty-state-small">
        <i class="ri-notification-3-line"></i>
        <p>Nenhuma notificação</p>
      </div>
    `
    return
  }

  // Render notifications
  let html = ""
  notifications.forEach((notif) => {
    html += `
      <div class="notification-item ${notif.read ? "read" : "unread"}">
        <i class="${notif.icon}"></i>
        <div>
          <p>${notif.message}</p>
          <small>${notif.time}</small>
        </div>
      </div>
    `
  })
  notificationsList.innerHTML = html
}

function showNotification(message) {
  // Simple toast notification
  const toast = document.createElement("div")
  toast.className = "toast-notification"
  toast.textContent = message
  document.body.appendChild(toast)

  setTimeout(() => {
    toast.classList.add("show")
  }, 100)

  setTimeout(() => {
    toast.classList.remove("show")
    setTimeout(() => {
      document.body.removeChild(toast)
    }, 300)
  }, 3000)
}

// Modals
function initModals() {
  // Event listeners para formulários
  loginForm.addEventListener("submit", handleLogin)
  registerForm.addEventListener("submit", handleRegister)
  productForm.addEventListener("submit", handleProductSubmit)
  purchaseForm.addEventListener("submit", handlePurchaseSubmit)

  // Switch entre modals
  switchToRegister.addEventListener("click", (e) => {
    e.preventDefault()
    closeModal("loginModal")
    openModal("registerModal")
  })

  switchToLogin.addEventListener("click", (e) => {
    e.preventDefault()
    closeModal("registerModal")
    openModal("loginModal")
  })

  // Fechar modal ao clicar no overlay
  loginModal.addEventListener("click", (e) => {
    if (e.target === loginModal) closeModal("loginModal")
  })

  registerModal.addEventListener("click", (e) => {
    if (e.target === registerModal) closeModal("registerModal")
  })

  productModal.addEventListener("click", (e) => {
    if (e.target === productModal) closeModal("productModal")
  })

  purchaseModal.addEventListener("click", (e) => {
    if (e.target === purchaseModal) closeModal("purchaseModal")
  })
}

function openModal(modalId) {
  const modal = document.getElementById(modalId)
  if (modal) {
    modal.classList.remove("hidden")
  }
}

function closeModal(modalId) {
  const modal = document.getElementById(modalId)
  if (modal) {
    modal.classList.add("hidden")
  }
}

function openProductModal() {
  openModal("productModal")
}

function openPurchaseModal() {
  openModal("purchaseModal")
}

function handleLogin(e) {
  e.preventDefault()

  const email = document.getElementById("loginEmail").value
  const password = document.getElementById("loginPassword").value

  // Simulação de login
  currentUser = {
    name: email.split("@")[0],
    email: email,
  }

  isLoggedIn = true
  localStorage.setItem("currentUser", JSON.stringify(currentUser))

  showUserMenu()
  closeModal("loginModal")
  loginForm.reset()
}

function handleRegister(e) {
  e.preventDefault()

  const name = document.getElementById("registerName").value
  const email = document.getElementById("registerEmail").value
  const password = document.getElementById("registerPassword").value

  // Simulação de cadastro
  currentUser = {
    name: name,
    email: email,
  }

  isLoggedIn = true
  localStorage.setItem("currentUser", JSON.stringify(currentUser))

  showUserMenu()
  closeModal("registerModal")
  registerForm.reset()
}

function handleProductSubmit(e) {
  e.preventDefault()

  const productData = {
    name: document.getElementById("productName").value,
    price: document.getElementById("productPrice").value,
    description: document.getElementById("productDescription").value,
    image: document.getElementById("productImage").files[0],
  }

  console.log("[v0] Produto cadastrado:", productData)

  // TODO: Salvar no banco de dados

  showNotification("Produto cadastrado com sucesso!")
  closeModal("productModal")
  productForm.reset()
}

function handlePurchaseSubmit(e) {
  e.preventDefault()

  const purchaseData = {
    buyer: {
      name: document.getElementById("buyerName").value,
      email: document.getElementById("buyerEmail").value,
      phone: document.getElementById("buyerPhone").value,
      cpf: document.getElementById("buyerCPF").value,
    },
    address: {
      cep: document.getElementById("buyerCEP").value,
      street: document.getElementById("buyerStreet").value,
      number: document.getElementById("buyerNumber").value,
      complement: document.getElementById("buyerComplement").value,
      neighborhood: document.getElementById("buyerNeighborhood").value,
      city: document.getElementById("buyerCity").value,
      state: document.getElementById("buyerState").value,
    },
    label: document.getElementById("shippingLabel").files[0],
    items: cartItems,
    total: cartItems.reduce((sum, item) => sum + item.price * item.quantity, 0),
  }

  console.log("[v0] Compra realizada:", purchaseData)

  // TODO: Salvar no banco de dados

  // Clear cart
  cartItems = []
  localStorage.removeItem("cartItems")
  updateCartBadge()

  // Fechar modal de compra
  closeModal("purchaseModal")
  purchaseForm.reset()

  // Abrir página de chat após compra
  showPage("chat")

  // Atualizar navegação
  navItems.forEach((nav) => nav.classList.remove("active"))
  const chatNav = document.querySelector('[data-page="chat"]')
  if (chatNav) chatNav.classList.add("active")

  showNotification("Compra realizada com sucesso! O chat foi aberto para suporte.")
}

// Sidebar
function initSidebar() {
  const savedCollapsed = localStorage.getItem("sidebarCollapsed") === "true"
  if (savedCollapsed) {
    sidebar.classList.add("collapsed")
  }

  // Toggle sidebar (desktop)
  sidebarToggle.addEventListener("click", () => {
    sidebar.classList.toggle("collapsed")
    localStorage.setItem("sidebarCollapsed", sidebar.classList.contains("collapsed"))
  })

  // Toggle sidebar (mobile)
  mobileMenuBtn.addEventListener("click", () => {
    sidebar.classList.add("active")
    overlay.classList.add("active")
  })

  // Fechar sidebar mobile
  overlay.addEventListener("click", closeMobileSidebar)
}

function closeMobileSidebar() {
  sidebar.classList.remove("active")
  overlay.classList.remove("active")
}

// Navegação
function initNavigation() {
  navItems.forEach((item) => {
    item.addEventListener("click", (e) => {
      e.preventDefault()

      // Remover active de todos
      navItems.forEach((nav) => nav.classList.remove("active"))

      // Adicionar active no clicado
      item.classList.add("active")

      const pageId = item.getAttribute("data-page")
      showPage(pageId)

      // Fechar sidebar em mobile
      if (window.innerWidth <= 768) {
        closeMobileSidebar()
      }
    })
  })
}

function showPage(pageId) {
  // Esconder todas as páginas
  const allPages = document.querySelectorAll(".page-content")
  allPages.forEach((page) => page.classList.remove("active"))

  // Mostrar a página selecionada
  const selectedPage = document.getElementById(pageId + "Page")
  if (selectedPage) {
    selectedPage.classList.add("active")

    if (pageId === "carrinho") {
      renderCart()
    }

    if (pageId === "historico") {
      renderOrderHistory()
    }
  }
}

function renderOrderHistory() {
  const orderHistoryList = document.getElementById("orderHistoryList")

  // TODO: Fetch from database
  const orders = [] // Will be populated from database

  if (orders.length === 0) {
    orderHistoryList.innerHTML = `
      <div class="empty-state">
        <i class="ri-history-line"></i>
        <p>Nenhum pedido realizado ainda</p>
        <small>Seus pedidos aparecerão aqui</small>
      </div>
    `
    return
  }

  // Render orders with tracking
  let html = '<div class="orders-list">'
  orders.forEach((order) => {
    html += `
      <div class="order-item" onclick="showOrderDetails(${order.id})">
        <div class="order-info">
          <h4>Pedido #${order.id}</h4>
          <p>${order.items.length} item(ns)</p>
          <p class="order-date">${order.date}</p>
        </div>
        <div>
          <span class="order-status status-${order.status}">${order.statusText}</span>
          <p class="order-value">R$ ${order.total.toFixed(2)}</p>
        </div>
      </div>
    `
  })
  html += "</div>"
  orderHistoryList.innerHTML = html
}

function showOrderDetails(orderId) {
  // TODO: Fetch order details from database
  console.log("[v0] Showing order details for:", orderId)

  const orderDetailsContent = document.getElementById("orderDetailsContent")
  orderDetailsContent.innerHTML = `
    <div class="order-tracking">
      <h3>Pedido #${orderId}</h3>
      <div class="tracking-status">
        <div class="status-step completed">
          <i class="ri-checkbox-circle-line"></i>
          <p>Pedido Confirmado</p>
          <small>01/01/2024</small>
        </div>
        <div class="status-step completed">
          <i class="ri-checkbox-circle-line"></i>
          <p>Pagamento Aprovado</p>
          <small>01/01/2024</small>
        </div>
        <div class="status-step active">
          <i class="ri-truck-line"></i>
          <p>Em Transporte</p>
          <small>02/01/2024</small>
        </div>
        <div class="status-step">
          <i class="ri-home-line"></i>
          <p>Entregue</p>
          <small>Aguardando</small>
        </div>
      </div>
    </div>
  `

  openModal("orderDetailsModal")
}

// Tooltips para sidebar colapsada
function addTooltips() {
  navItems.forEach((item) => {
    const text = item.querySelector(".nav-text").textContent
    item.setAttribute("data-tooltip", text)
  })

  const themeText = themeToggle.querySelector(".nav-text").textContent
  themeToggle.setAttribute("data-tooltip", themeText)
}

// Ajustar header quando sidebar colapsa
const resizeObserver = new ResizeObserver(() => {
  // Código para ajustes futuros se necessário
})

resizeObserver.observe(sidebar)

// Estado da aplicação
let isLoggedIn = false
let currentUser = null

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
    }
  })
}

function showUserMenu() {
  authButtons.classList.add("hidden")
  userMenu.classList.remove("hidden")
  userName.textContent = currentUser.name
}

function showAuthButtons() {
  userMenu.classList.add("hidden")
  authButtons.classList.remove("hidden")
}

function toggleDropdown(e) {
  e.stopPropagation()
  dropdownMenu.classList.toggle("hidden")
}

function logout() {
  isLoggedIn = false
  currentUser = null
  localStorage.removeItem("currentUser")
  showAuthButtons()
  dropdownMenu.classList.add("hidden")
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

  // Fechar modal ao clicar no botão X
  document.querySelectorAll(".modal-close").forEach((btn) => {
    btn.addEventListener("click", () => {
      const modalId = btn.getAttribute("data-modal")
      closeModal(modalId)
    })
  })

  // Fechar modal ao clicar no overlay
  loginModal.addEventListener("click", (e) => {
    if (e.target === loginModal) closeModal("loginModal")
  })

  registerModal.addEventListener("click", (e) => {
    if (e.target === registerModal) closeModal("registerModal")
  })

  productModal.addEventListener("click", (e) => {
    if (e.target.id === "productModal") closeModal("productModal")
  })

  purchaseModal.addEventListener("click", (e) => {
    if (e.target.id === "purchaseModal") closeModal("purchaseModal")
  })
}

function openModal(modalId) {
  document.getElementById(modalId).classList.remove("hidden")
}

function closeModal(modalId) {
  document.getElementById(modalId).classList.add("hidden")
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

  alert("Produto cadastrado com sucesso!\n(Integração com banco pendente)")
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
  }

  console.log("[v0] Compra realizada:", purchaseData)

  // TODO: Salvar no banco de dados

  // Fechar modal de compra
  closeModal("purchaseModal")
  purchaseForm.reset()

  // Abrir página de chat após compra
  showPage("chat")

  // Atualizar navegação
  navItems.forEach((nav) => nav.classList.remove("active"))
  const chatNav = document.querySelector('[data-page="chat"]')
  if (chatNav) chatNav.classList.add("active")

  alert("Compra realizada com sucesso!\nO chat foi aberto para suporte.")
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
  }
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
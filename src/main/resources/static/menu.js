let isLoggedIn = false;
let currentUser = null;
let cartItems = [];
const notifications = [];

// Elementos
const sidebar = document.getElementById("sidebar");
const sidebarToggle = document.getElementById("sidebarToggle");
const mobileMenuBtn = document.getElementById("mobileMenuBtn");
const overlay = document.getElementById("overlay");
const themeToggle = document.getElementById("themeToggle");
const mainContent = document.getElementById("mainContent");

// Auth elements
const authButtons = document.getElementById("authButtons");
const userMenu = document.getElementById("userMenu");
const userAvatar = document.getElementById("userAvatar");
const dropdownMenu = document.getElementById("dropdownMenu");
const btnLogin = document.getElementById("btnLogin");
const btnRegister = document.getElementById("btnRegister");
const btnLogout = document.getElementById("btnLogout");
const userName = document.getElementById("userName");

const cartBtn = document.getElementById("cartBtn");
const cartBadge = document.getElementById("cartBadge");
const notificationsBtn = document.getElementById("notificationsBtn");
const notificationBadge = document.getElementById("notificationBadge");
const notificationsDropdown = document.getElementById("notificationsDropdown");
const navHistorico = document.getElementById("navHistorico");

// Modals
const loginModal = document.getElementById("loginModal");
const registerModal = document.getElementById("registerModal");
const loginForm = document.getElementById("loginForm");
const registerForm = document.getElementById("registerForm");
const switchToRegister = document.getElementById("switchToRegister");
const switchToLogin = document.getElementById("switchToLogin");
const productModal = document.getElementById("productModal");
const purchaseModal = document.getElementById("purchaseModal");
const productForm = document.getElementById("productForm");
const purchaseForm = document.getElementById("purchaseForm");

// Navegação
const navItems = document.querySelectorAll(".nav-item");

// Inicialização
document.addEventListener("DOMContentLoaded", () => {
  initTheme();
  initAuth();
  initNavigation();
  initSidebar();
  initModals();
  initCart();
  initNotifications();
  addTooltips();
});

// ------------------- TEMA -------------------
function initTheme() {
  const savedTheme = localStorage.getItem("theme") || "light";
  document.documentElement.setAttribute("data-theme", savedTheme);
  updateThemeIcon(savedTheme);
  themeToggle.addEventListener("click", toggleTheme);
}

function toggleTheme() {
  const currentTheme = document.documentElement.getAttribute("data-theme");
  const newTheme = currentTheme === "light" ? "dark" : "light";
  document.documentElement.setAttribute("data-theme", newTheme);
  localStorage.setItem("theme", newTheme);
  updateThemeIcon(newTheme);
}

function updateThemeIcon(theme) {
  const icon = themeToggle.querySelector("i");
  icon.className = theme === "light" ? "ri-moon-line" : "ri-sun-line";
}

// ------------------- AUTENTICAÇÃO -------------------
function initAuth() {
  const savedUser = localStorage.getItem("currentUser");
  if (savedUser) {
    currentUser = JSON.parse(savedUser);
    isLoggedIn = true;
    showUserMenu();
  }

  btnLogin.addEventListener("click", () => openModal("loginModal"));
  btnRegister.addEventListener("click", () => openModal("registerModal"));
  btnLogout.addEventListener("click", logout);
  userAvatar.addEventListener("click", toggleDropdown);

  document.addEventListener("click", (e) => {
    if (!userMenu.contains(e.target)) {
      dropdownMenu.classList.add("hidden");
      notificationsDropdown.classList.add("hidden");
    }
  });

  // Login
  loginForm.addEventListener("submit", async (e) => {
    e.preventDefault();
    const dados = {
      email: document.getElementById("loginEmail").value,
      senha: document.getElementById("loginPassword").value,
    };
    try {
      const resposta = await fetch("http://localhost:8080/api/auth/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(dados),
      });
      const resultado = await resposta.json();
      if (resposta.ok) {
        currentUser = resultado;
        isLoggedIn = true;
        localStorage.setItem("currentUser", JSON.stringify(resultado));
        showUserMenu();
        closeModal("loginModal");
        loginForm.reset();
        showNotification("Login realizado com sucesso!");
      } else {
        alert("Erro: " + resultado.mensagem);
      }
    } catch (erro) {
      alert("Erro ao conectar com servidor");
    }
  });

  // Registro
  registerForm.addEventListener("submit", async (e) => {
    e.preventDefault();
    const dados = {
      name: document.getElementById("registerName").value,
      email: document.getElementById("registerEmail").value,
      senha: document.getElementById("registerPassword").value,
    };
    try {
      const resposta = await fetch("http://localhost:8080/api/auth/register", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(dados),
      });
      const resultado = await resposta.json();
      if (resposta.ok) {
        currentUser = resultado;
        isLoggedIn = true;
        localStorage.setItem("currentUser", JSON.stringify(resultado));
        showUserMenu();
        closeModal("registerModal");
        registerForm.reset();
        showNotification("Cadastro realizado com sucesso!");
      } else {
        alert("Erro: " + resultado.mensagem);
      }
    } catch (erro) {
      alert("Erro ao conectar com servidor");
    }
  });
}

function showUserMenu() {
  authButtons.classList.add("hidden");
  userMenu.classList.remove("hidden");
  userName.textContent = currentUser.name;
  navHistorico.classList.remove("hidden");
  updateCartBadge();
  updateNotificationBadge();
}

function showAuthButtons() {
  userMenu.classList.add("hidden");
  authButtons.classList.remove("hidden");
  navHistorico.classList.add("hidden");
}

function toggleDropdown(e) {
  e.stopPropagation();
  dropdownMenu.classList.toggle("hidden");
  notificationsDropdown.classList.add("hidden");
}

function logout() {
  isLoggedIn = false;
  currentUser = null;
  cartItems = [];
  localStorage.removeItem("currentUser");
  localStorage.removeItem("cartItems");
  showAuthButtons();
  dropdownMenu.classList.add("hidden");
  updateCartBadge();
}

// ------------------- CARRINHO -------------------
function initCart() {
  const savedCart = localStorage.getItem("cartItems");
  if (savedCart) {
    cartItems = JSON.parse(savedCart);
    updateCartBadge();
  }

  cartBtn.addEventListener("click", (e) => {
    e.stopPropagation();
    showPage("carrinho");
    navItems.forEach((nav) => nav.classList.remove("active"));
    renderCart();
  });
}

function addToCart(productId) {
  if (!isLoggedIn) {
    alert("Faça login para adicionar produtos ao carrinho");
    openModal("loginModal");
    return;
  }

  const product = {
    id: productId,
    name: "Produto Exemplo",
    price: 99.9,
    quantity: 1,
    image: "/placeholder.svg?height=100&width=100",
  };

  const existingItem = cartItems.find((item) => item.id === productId);
  if (existingItem) existingItem.quantity++;
  else cartItems.push(product);

  localStorage.setItem("cartItems", JSON.stringify(cartItems));
  updateCartBadge();
  showNotification("Produto adicionado ao carrinho!");
}

function removeFromCart(productId) {
  cartItems = cartItems.filter((item) => item.id !== productId);
  localStorage.setItem("cartItems", JSON.stringify(cartItems));
  updateCartBadge();
  renderCart();
}

function updateCartQuantity(productId, change) {
  const item = cartItems.find((item) => item.id === productId);
  if (item) {
    item.quantity += change;
    if (item.quantity <= 0) removeFromCart(productId);
    else {
      localStorage.setItem("cartItems", JSON.stringify(cartItems));
      renderCart();
    }
  }
}

function updateCartBadge() {
  const totalItems = cartItems.reduce((sum, item) => sum + item.quantity, 0);
  if (totalItems > 0) {
    cartBadge.textContent = totalItems;
    cartBadge.classList.remove("hidden");
  } else {
    cartBadge.classList.add("hidden");
  }
}

function renderCart() {
  const cartItemsList = document.getElementById("cartItemsList");
  const btnFinalizePurchase = document.getElementById("btnFinalizePurchase");

  if (cartItems.length === 0) {
    cartItemsList.innerHTML = `
      <div class="empty-state">
        <i class="ri-shopping-cart-line"></i>
        <p>Seu carrinho está vazio</p>
        <small>Adicione produtos para continuar</small>
      </div>
    `;
    btnFinalizePurchase.disabled = true;
    updateCartSummary(0);
    return;
  }

  btnFinalizePurchase.disabled = false;
  let html = "";
  cartItems.forEach((item) => {
    const itemTotal = item.price * item.quantity;
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
    `;
  });

  cartItemsList.innerHTML = html;
  const subtotal = cartItems.reduce((sum, item) => sum + item.price * item.quantity, 0);
  updateCartSummary(subtotal);
}

function updateCartSummary(subtotal) {
  document.getElementById("cartSubtotal").textContent = `R$ ${subtotal.toFixed(2)}`;
  document.getElementById("cartTotal").textContent = `R$ ${subtotal.toFixed(2)}`;
}

function finalizePurchase() {
  if (cartItems.length === 0) {
    alert("Seu carrinho está vazio");
    return;
  }
  openModal("purchaseModal");
}

// ------------------- NOTIFICAÇÕES -------------------
function initNotifications() {
  notificationsBtn.addEventListener("click", (e) => {
    e.stopPropagation();
    notificationsDropdown.classList.toggle("hidden");
    dropdownMenu.classList.add("hidden");
    renderNotifications();
  });
}

function updateNotificationBadge() {
  const unreadCount = notifications.filter((n) => !n.read).length;
  if (unreadCount > 0) {
    notificationBadge.textContent = unreadCount;
    notificationBadge.classList.remove("hidden");
  } else {
    notificationBadge.classList.add("hidden");
  }
}

function renderNotifications() {
  const notificationsList = document.getElementById("notificationsList");
  if (notifications.length === 0) {
    notificationsList.innerHTML = `
      <div class="empty-state-small">
        <i class="ri-notification-3-line"></i>
        <p>Nenhuma notificação</p>
      </div>
    `;
    return;
  }

  let html = "";
  notifications.forEach((notif) => {
    html += `
      <div class="notification-item ${notif.read ? "read" : "unread"}">
        <i class="${notif.icon}"></i>
        <div>
          <p>${notif.message}</p>
          <small>${notif.time}</small>
        </div>
      </div>
    `;
  });
  notificationsList.innerHTML = html;
}

// ------------------- MODALS -------------------
function initModals() {
  switchToRegister.addEventListener("click", (e) => {
    e.preventDefault();
    closeModal("loginModal");
    openModal("registerModal");
  });

  switchToLogin.addEventListener("click", (e) => {
    e.preventDefault();
    closeModal("registerModal");
    openModal("loginModal");
  });

  [loginModal, registerModal, productModal, purchaseModal].forEach((modal) => {
    modal.addEventListener("click", (e) => {
      if (e.target === modal) closeModal(modal.id);
    });
  });

  productForm.addEventListener("submit", handleProductSubmit);
  purchaseForm.addEventListener("submit", handlePurchaseSubmit);
}

function openModal(modalId) {
  const modal = document.getElementById(modalId);
  if (modal) modal.classList.remove("hidden");
}

function closeModal(modalId) {
  const modal = document.getElementById(modalId);
  if (modal) modal.classList.add("hidden");
}

function handleProductSubmit(e) {
  e.preventDefault();
  const productData = {
    name: document.getElementById("productName").value,
    price: document.getElementById("productPrice").value,
    description: document.getElementById("productDescription").value,
    image: document.getElementById("productImage").files[0],
  };
  console.log("Produto cadastrado:", productData);
  showNotification("Produto cadastrado com sucesso!");
  closeModal("productModal");
  productForm.reset();
}

function handlePurchaseSubmit(e) {
  e.preventDefault();
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
  };
  console.log("Compra realizada:", purchaseData);

  cartItems = [];
  localStorage.removeItem("cartItems");
  updateCartBadge();

  closeModal("purchaseModal");
  purchaseForm.reset();
  showPage("chat");
  navItems.forEach((nav) => nav.classList.remove("active"));
  const chatNav = document.querySelector('[data-page="chat"]');
  if (chatNav) chatNav.classList.add("active");

  showNotification("Compra realizada com sucesso! O chat foi aberto para suporte.");
}

// ------------------- SIDEBAR -------------------
function initSidebar() {
  const savedCollapsed = localStorage.getItem("sidebarCollapsed") === "true";
  if (savedCollapsed) sidebar.classList.add("collapsed");

  sidebarToggle.addEventListener("click", () => {
    sidebar.classList.toggle("collapsed");
    localStorage.setItem("sidebarCollapsed", sidebar.classList.contains("collapsed"));
  });

  mobileMenuBtn.addEventListener("click", () => {
    sidebar.classList.add("active");
    overlay.classList.add("active");
  });

  overlay.addEventListener("click", closeMobileSidebar);
}

function closeMobileSidebar() {
  sidebar.classList.remove("active");
  overlay.classList.remove("active");
}

// ------------------- NAVEGAÇÃO -------------------
function initNavigation() {
  navItems.forEach((item) => {
    item.addEventListener("click", (e) => {
      e.preventDefault();
      navItems.forEach((nav) => nav.classList.remove("active"));
      item.classList.add("active");
      const pageId = item.getAttribute("data-page");
      showPage(pageId);
      if (window.innerWidth <= 768) closeMobileSidebar();
    });
  });
}

function showPage(pageId) {
  const allPages = document.querySelectorAll(".page-content");
  allPages.forEach((page) => page.classList.remove("active"));
  const selectedPage = document.getElementById(pageId + "Page");
  if (selectedPage) selectedPage.classList.add("active");
  if (pageId === "carrinho") renderCart();
  if (pageId === "historico") renderOrderHistory();
}

// ------------------- HISTÓRICO -------------------
function renderOrderHistory() {
  const orderHistoryList = document.getElementById("orderHistoryList");
  const orders = [];
  if (orders.length === 0) {
    orderHistoryList.innerHTML = `
      <div class="empty-state">
        <i class="ri-history-line"></i>
        <p>Nenhum pedido realizado ainda</p>
        <small>Seus pedidos aparecerão aqui</small>
      </div>
    `;
    return;
  }
  let html = '<div class="orders-list">';
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
    `;
  });
  html += "</div>";
  orderHistoryList.innerHTML = html;
}

// ------------------- UTILS -------------------
function showNotification(message) {
  const notificationArea = document.getElementById("toastContainer");
  if (!notificationArea) return;
  const toast = document.createElement("div");
  toast.className = "toast";
  toast.textContent = message;
  notificationArea.appendChild(toast);
  setTimeout(() => toast.remove(), 4000);
}

function addTooltips() {
  const tooltipElements = document.querySelectorAll("[data-tooltip]");
  tooltipElements.forEach((el) => {
    el.addEventListener("mouseenter", () => {
      const tooltip = document.createElement("span");
      tooltip.className = "tooltip";
      tooltip.textContent = el.dataset.tooltip;
      el.appendChild(tooltip);
    });
    el.addEventListener("mouseleave", () => {
      const tooltip = el.querySelector(".tooltip");
      if (tooltip) tooltip.remove();
    });
  });
}

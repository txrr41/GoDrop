// ====== Variáveis Globais ======
let isLoggedIn = false;
let currentUser = null;
let cartItems = [];
const notifications = [];

// ====== Inicialização ======
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

// ====== Tema Dark/Light ======
function initTheme() {
  const savedTheme = localStorage.getItem("theme") || "light";
  document.documentElement.setAttribute("data-theme", savedTheme);
  updateThemeIcon(savedTheme);

  document.body.addEventListener("click", (e) => {
    if (e.target.closest("#themeToggle")) toggleTheme();
  });
}

function toggleTheme() {
  const currentTheme = document.documentElement.getAttribute("data-theme");
  const newTheme = currentTheme === "light" ? "dark" : "light";
  document.documentElement.setAttribute("data-theme", newTheme);
  localStorage.setItem("theme", newTheme);
  updateThemeIcon(newTheme);
}

function updateThemeIcon(theme) {
  const themeToggle = document.getElementById("themeToggle");
  if (!themeToggle) return;
  const icon = themeToggle.querySelector("i");
  icon.className = theme === "light" ? "ri-moon-line" : "ri-sun-line";
}

// ====== Autenticação ======
function initAuth() {
  // Carregar usuário
  const savedUser = localStorage.getItem("currentUser");
  if (savedUser) {
    currentUser = JSON.parse(savedUser);
    isLoggedIn = true;
    showUserMenu();
  } else {
    showAuthButtons();
  }

  // Delegação de clique para login/register/logout/avatar
  document.body.addEventListener("click", (e) => {
    if (e.target.closest("#btnLogin")) openModal("loginModal");
    if (e.target.closest("#btnRegister")) openModal("registerModal");
    if (e.target.closest("#btnLogout")) logout();
    if (e.target.closest("#userAvatar")) toggleDropdown(e);
  });

  // Fechar dropdown ao clicar fora
  document.addEventListener("click", (e) => {
    const userMenu = document.getElementById("userMenu");
    const dropdownMenu = document.getElementById("dropdownMenu");
    const notificationsDropdown = document.getElementById("notificationsDropdown");
    if (!userMenu.contains(e.target)) {
      dropdownMenu.classList.add("hidden");
      notificationsDropdown.classList.add("hidden");
    }
  });

  // Login/Register via backend
  handleFormSubmit("loginForm", "loginModal", "login", async (data) => {
    return await fetch("http://localhost:8080/api/auth/login", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(data),
    });
  });

  handleFormSubmit("registerForm", "registerModal", "register", async (data) => {
    return await fetch("http://localhost:8080/api/auth/register", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(data),
    });
  });
}

function showUserMenu() {
  const authButtons = document.getElementById("authButtons");
  const userMenu = document.getElementById("userMenu");
  const userName = document.getElementById("userName");
  const navHistorico = document.getElementById("navHistorico");

  if (authButtons) authButtons.classList.add("hidden");
  if (userMenu) userMenu.classList.remove("hidden");
  if (userName) userName.textContent = currentUser.name;
  if (navHistorico) navHistorico.classList.remove("hidden");

  updateCartBadge();
  updateNotificationBadge();
}

function showAuthButtons() {
  const authButtons = document.getElementById("authButtons");
  const userMenu = document.getElementById("userMenu");
  const navHistorico = document.getElementById("navHistorico");

  if (userMenu) userMenu.classList.add("hidden");
  if (authButtons) authButtons.classList.remove("hidden");
  if (navHistorico) navHistorico.classList.add("hidden");
}

function toggleDropdown(e) {
  e.stopPropagation();
  const dropdownMenu = document.getElementById("dropdownMenu");
  const notificationsDropdown = document.getElementById("notificationsDropdown");
  if (dropdownMenu) dropdownMenu.classList.toggle("hidden");
  if (notificationsDropdown) notificationsDropdown.classList.add("hidden");
}

function logout() {
  isLoggedIn = false;
  currentUser = null;
  cartItems = [];
  localStorage.removeItem("currentUser");
  localStorage.removeItem("cartItems");
  showAuthButtons();
  updateCartBadge();
}

// Função genérica para submit de forms de login/register
function handleFormSubmit(formId, modalId, type, fetchFunc) {
  const form = document.getElementById(formId);
  if (!form) return;

  form.addEventListener("submit", async (e) => {
    e.preventDefault();

    const data = {};
    form.querySelectorAll("input").forEach((input) => {
      data[input.name || input.id] = input.value;
    });

    try {
      const response = await fetchFunc(data);
      const result = await response.json();

      if (response.ok) {
        currentUser = result;
        isLoggedIn = true;
        localStorage.setItem("currentUser", JSON.stringify(result));
        showUserMenu();
        closeModal(modalId);
        form.reset();
        showNotification(`${type === "login" ? "Login" : "Cadastro"} realizado com sucesso!`);
      } else {
        alert("Erro: " + result.mensagem);
      }
    } catch (err) {
      alert("Erro ao conectar com servidor");
    }
  });
}

// ====== Modals ======
function initModals() {
  const modals = ["loginModal", "registerModal", "productModal", "purchaseModal"];
  modals.forEach((id) => {
    const modal = document.getElementById(id);
    if (!modal) return;
    modal.addEventListener("click", (e) => {
      if (e.target === modal) closeModal(id);
    });
  });

  // Switch login/register
  const switchToRegister = document.getElementById("switchToRegister");
  const switchToLogin = document.getElementById("switchToLogin");
  if (switchToRegister) switchToRegister.addEventListener("click", (e) => { e.preventDefault(); closeModal("loginModal"); openModal("registerModal"); });
  if (switchToLogin) switchToLogin.addEventListener("click", (e) => { e.preventDefault(); closeModal("registerModal"); openModal("loginModal"); });
}

function openModal(id) {
  const modal = document.getElementById(id);
  if (modal) modal.classList.remove("hidden");
}

function closeModal(id) {
  const modal = document.getElementById(id);
  if (modal) modal.classList.add("hidden");
}

// ====== Cart ======
function initCart() {
  const savedCart = localStorage.getItem("cartItems");
  if (savedCart) cartItems = JSON.parse(savedCart);

  document.body.addEventListener("click", (e) => {
    if (e.target.closest("#cartBtn")) {
      showPage("carrinho");
      renderCart();
    }

    // Carrinho controls
    if (e.target.closest(".btn-add")) {
      const id = parseInt(e.target.closest(".cart-item").dataset.id);
      updateCartQuantity(id, 1);
    }
    if (e.target.closest(".btn-sub")) {
      const id = parseInt(e.target.closest(".cart-item").dataset.id);
      updateCartQuantity(id, -1);
    }
    if (e.target.closest(".btn-remove")) {
      const id = parseInt(e.target.closest(".cart-item").dataset.id);
      removeFromCart(id);
    }

    if (e.target.closest("#btnFinalizePurchase")) {
      finalizePurchase();
    }
  });

  updateCartBadge();
}

function addToCart(product) {
  if (!isLoggedIn) {
    alert("Faça login para adicionar produtos ao carrinho");
    openModal("loginModal");
    return;
  }

  const existing = cartItems.find((i) => i.id === product.id);
  if (existing) existing.quantity++;
  else cartItems.push({ ...product, quantity: 1 });

  localStorage.setItem("cartItems", JSON.stringify(cartItems));
  updateCartBadge();
  showNotification("Produto adicionado ao carrinho!");
}

function updateCartQuantity(productId, change) {
  const item = cartItems.find((i) => i.id === productId);
  if (!item) return;
  item.quantity += change;
  if (item.quantity <= 0) removeFromCart(productId);
  else {
    localStorage.setItem("cartItems", JSON.stringify(cartItems));
    renderCart();
  }
}

function removeFromCart(productId) {
  cartItems = cartItems.filter((i) => i.id !== productId);
  localStorage.setItem("cartItems", JSON.stringify(cartItems));
  renderCart();
  updateCartBadge();
}

function updateCartBadge() {
  const cartBadge = document.getElementById("cartBadge");
  if (!cartBadge) return;
  const total = cartItems.reduce((sum, i) => sum + i.quantity, 0);
  if (total > 0) {
    cartBadge.textContent = total;
    cartBadge.classList.remove("hidden");
  } else {
    cartBadge.classList.add("hidden");
  }
}

function renderCart() {
  const list = document.getElementById("cartItemsList");
  const btnFinalize = document.getElementById("btnFinalizePurchase");
  if (!list || !btnFinalize) return;

  if (cartItems.length === 0) {
    list.innerHTML = `<div class="empty-state"><p>Carrinho vazio</p></div>`;
    btnFinalize.disabled = true;
    return;
  }

  btnFinalize.disabled = false;
  list.innerHTML = cartItems.map(i => `
    <div class="cart-item" data-id="${i.id}">
      <h4>${i.name}</h4>
      <p>R$ ${i.price.toFixed(2)}</p>
      <button class="btn-sub">-</button>
      <span>${i.quantity}</span>
      <button class="btn-add">+</button>
      <button class="btn-remove">Remover</button>
    </div>
  `).join('');
}

// ====== Notificações ======
function initNotifications() {
  document.body.addEventListener("click", (e) => {
    if (e.target.closest("#notificationsBtn")) {
      const dropdown = document.getElementById("notificationsDropdown");
      const menu = document.getElementById("dropdownMenu");
      if (dropdown) dropdown.classList.toggle("hidden");
      if (menu) menu.classList.add("hidden");
      renderNotifications();
    }
  });

  updateNotificationBadge();
}

function updateNotificationBadge() {
  const badge = document.getElementById("notificationBadge");
  if (!badge) return;
  const unread = notifications.filter(n => !n.read).length;
  if (unread > 0) {
    badge.textContent = unread;
    badge.classList.remove("hidden");
  } else badge.classList.add("hidden");
}

function renderNotifications() {
  const list = document.getElementById("notificationsList");
  if (!list) return;

  if (notifications.length === 0) {
    list.innerHTML = `<div class="empty-state-small"><p>Nenhuma notificação</p></div>`;
    return;
  }

  list.innerHTML = notifications.map(n => `
    <div class="notification-item ${n.read ? "read" : "unread"}">
      <p>${n.message}</p>
    </div>
  `).join('');
}

function showNotification(message) {
  const toast = document.createElement("div");
  toast.className = "toast-notification";
  toast.textContent = message;
  document.body.appendChild(toast);

  setTimeout(() => toast.classList.add("show"), 100);
  setTimeout(() => {
    toast.classList.remove("show");
    setTimeout(() => document.body.removeChild(toast), 300);
  }, 3000);
}

// ====== Sidebar ======
function initSidebar() {
  const sidebar = document.getElementById("sidebar");
  const toggle = document.getElementById("sidebarToggle");
  const mobileBtn = document.getElementById("mobileMenuBtn");
  const overlay = document.getElementById("overlay");
  if (!sidebar) return;

  if (localStorage.getItem("sidebarCollapsed") === "true") sidebar.classList.add("collapsed");

  if (toggle) toggle.addEventListener("click", () => {
    sidebar.classList.toggle("collapsed");
    localStorage.setItem("sidebarCollapsed", sidebar.classList.contains("collapsed"));
  });

  if (mobileBtn) mobileBtn.addEventListener("click", () => {
    sidebar.classList.add("active");
    overlay.classList.add("active");
  });

  if (overlay) overlay.addEventListener("click", () => {
    sidebar.classList.remove("active");
    overlay.classList.remove("active");
  });
}

// ====== Navegação ======
function initNavigation() {
  const navItems = document.querySelectorAll(".nav-item");
  document.body.addEventListener("click", (e) => {
    const item = e.target.closest(".nav-item");
    if (!item) return;
    navItems.forEach(n => n.classList.remove("active"));
    item.classList.add("active");
    const pageId = item.dataset.page;
    if (pageId) showPage(pageId);
    if (window.innerWidth <= 768) {
      document.getElementById("sidebar").classList.remove("active");
      document.getElementById("overlay").classList.remove("active");
    }
  });
}

function showPage(pageId) {
  document.querySelectorAll(".page-content").forEach(p => p.classList.remove("active"));
  const page = document.getElementById(pageId + "Page");
  if (page) page.classList.add("active");
  if (pageId === "carrinho") renderCart();
}

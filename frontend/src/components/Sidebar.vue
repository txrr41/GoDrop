<template>
  <aside :class="['sidebar', { collapsed: !isOpen }]">

    <!-- LOGO -->
    <div class="logo">
      <!-- Sidebar fechada -->
      <img
          v-if="!isOpen"
          src="../assets/Sh__4_-removebg-preview.png"
          alt="GoHome"
          class="logo-icon-close"
      />

      <!-- Sidebar aberta -->
      <div v-else class="logo-open">
        <img
            src="../assets/Sh__2_-removebg-preview.png"
            alt="GoHome"
            class="logo-icon"
        />
        <span class="logo-text"></span>
      </div>
    </div>

    <!-- NAV -->
    <nav class="nav">
      <div class="section">
        <span class="title" v-show="isOpen">Principal</span>
        <ul class="list">
          <li
              v-for="item in mainItems"
              :key="item.name"
              :class="['item', { active: active === item.name }]"
              @click="active = item.name"
          >
            <span class="icon" v-html="item.icon"></span>
            <span class="label" v-show="isOpen">{{ item.name }}</span>
          </li>
        </ul>
      </div>

      <div class="section">
        <span class="title" v-show="isOpen">Conta</span>
        <ul class="list">
          <li
              v-for="item in accountItems"
              :key="item.name"
              :class="['item', { active: active === item.name }]"
              @click="active = item.name"
          >
            <span class="icon" v-html="item.icon"></span>
            <span class="label" v-show="isOpen">{{ item.name }}</span>
          </li>
        </ul>
      </div>
    </nav>

    <!-- TOGGLE -->
    <button class="toggle" @click="isOpen = !isOpen">
      <svg
          width="20"
          height="20"
          fill="none"
          stroke="currentColor"
          stroke-width="2"
          viewBox="0 0 24 24"
      >
        <path v-if="isOpen" d="M15 19l-7-7 7-7" />
        <path v-else d="M9 5l7 7-7 7" />
      </svg>
    </button>

  </aside>
</template>

<script>
export default {
  name: 'Sidebar',

  data() {
    return {
      isOpen: true,
      active: 'Dashboard',

      mainItems: [
        {
          name: 'Dashboard',
          icon: '<svg width="20" height="20" fill="currentColor" viewBox="0 0 24 24"><path d="M4 5a1 1 0 011-1h4a1 1 0 011 1v4a1 1 0 01-1 1H5a1 1 0 01-1-1V5zm10 0a1 1 0 011-1h4a1 1 0 011 1v4a1 1 0 01-1 1h-4a1 1 0 01-1-1V5zM4 15a1 1 0 011-1h4a1 1 0 011 1v4a1 1 0 01-1 1H5a1 1 0 01-1-1v-4zm10 0a1 1 0 011-1h4a1 1 0 011 1v4a1 1 0 01-1 1h-4a1 1 0 01-1-1v-4z"/></svg>'
        },
        {
          name: 'Arquivos',
          icon: '<svg width="20" height="20" fill="currentColor" viewBox="0 0 24 24"><path d="M3 7a2 2 0 012-2h4l2 2h8a2 2 0 012 2v8a2 2 0 01-2 2H5a2 2 0 01-2-2V7z"/></svg>'
        },
        {
          name: 'Cadastros',
          icon: '<svg width="20" height="20" fill="currentColor" viewBox="0 0 24 24"><path d="M12 4.5a2.5 2.5 0 100 5 2.5 2.5 0 000-5zM7 12a2.5 2.5 0 100 5 2.5 2.5 0 000-5zm10 0a2.5 2.5 0 100 5 2.5 2.5 0 000-5z"/></svg>'
        },
        {
          name: 'Consultas',
          icon: '<svg width="20" height="20" fill="currentColor" viewBox="0 0 24 24"><path d="M9 5H7a2 2 0 00-2 2v10a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 012-2h2a2 2 0 012 2M9 5h6m-3 4v6m-3-3h6"/></svg>'
        }
      ],

      accountItems: [
        {
          name: 'Ajuda',
          icon: '<svg width="20" height="20" fill="currentColor" viewBox="0 0 24 24"><path d="M12 2a10 10 0 1010 10A10 10 0 0012 2zm1 17h-2v-2h2zm2.07-7.75l-.9.92A3.4 3.4 0 0013 15h-2v-.5a4 4 0 011.17-2.83l1.24-1.26A2 2 0 0014 9a2 2 0 10-4 0H8a4 4 0 118 0 3.18 3.18 0 01-.93 2.25z"/></svg>'
        },
        {
          name: 'Configurações',
          icon: '<svg width="20" height="20" fill="currentColor" viewBox="0 0 24 24"><path d="M12 15a3 3 0 100-6 3 3 0 000 6z"/><path fill-rule="evenodd" d="M10.325 3.314a2 2 0 013.35 0l.893 1.34a1 1 0 001.158.393l1.54-.513a2 2 0 012.897 2.052l-.257 1.593a1 1 0 00.636 1.105l1.469.588a2 2 0 010 3.736l-1.47.588a1 1 0 00-.635 1.105l.257 1.593a2 2 0 01-2.897 2.052l-1.54-.513a1 1 0 00-1.158.393l-.893 1.34a2 2 0 01-3.35 0l-.893-1.34a1 1 0 00-1.158-.393l-1.54.513a2 2 0 01-2.897-2.052l.257-1.593a1 1 0 00-.636-1.105L2.03 13.132a2 2 0 010-3.736l1.47-.588a1 1 0 00.635-1.105l-.257-1.593a2 2 0 012.897-2.052l1.54.513a1 1 0 001.158-.393l.893-1.34z" clip-rule="evenodd"/></svg>'
        }
      ]
    }
  }
}
</script>

<style scoped>

* {
  font-family: "Roboto" ;
}
.sidebar {
  display: flex;
  flex-direction: column;
  width: 220px;
  height: 100vh;
  background: rgba(255, 255, 255, 0.49);
  color: #4e4c4c;
  padding: 16px;
  transition: width 0.3s ease;
}

.sidebar.collapsed {
  width: 68px;
}

/* LOGO */
.logo {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 56px;
  margin-bottom: 24px;
}

.logo-open {
  display: flex;
  align-items: center;
  gap: 10px;
}

.logo-icon {
  width: 200px;

  margin: 0 0 23px 75px;

}

.logo-icon-close {
  width: 200px;
  margin: 0 10px 0 0;

}

.logo-text {
  font-size: 18px;
  font-weight: 700;
  white-space: nowrap;
}

/* NAV */
.nav {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.section {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.title {
  font-size: 11px;
  text-transform: uppercase;
  opacity: 0.6;
  padding: 0 8px;
  letter-spacing: 0.5px;
}

.list {
  list-style: none;
  padding: 0;
  margin: 0;
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 10px 12px;
  border-radius: 8px;
  cursor: pointer;
  transition: background 0.2s ease;
}

.item:hover {
  background: rgba(255, 255, 255, 0.1);
}

.item.active {
  background: #5fdcfd;
  color: #313139;
}

.icon {
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.label {
  font-size: 14px;
  white-space: nowrap;
}

/* TOGGLE */
.toggle {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 36px;
  height: 36px;
  border: none;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.2);
  color: white;
  cursor: pointer;
  margin-top: auto;
  align-self: center;
  transition: background 0.2s ease;
}

.toggle:hover {
  background: rgba(255, 255, 255, 0.3);
}
</style>

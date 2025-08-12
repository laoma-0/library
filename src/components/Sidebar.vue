<template>
  <el-menu :default-active="activeMenu" class="sidebar-menu" router>
    <el-submenu index="1">
      <template #title>
        <i class="el-icon-setting" />
        <span>系统管理</span>
      </template>
      <el-menu-item
        v-for="item in menuItems"
        :key="item.index"
        :index="item.index"
      >
        {{ item.label }}
      </el-menu-item>
    </el-submenu>
  </el-menu>
</template>

<script>
// 复用安全获取当前用户信息方法
function getCurrentUser() {
  try {
    if (typeof window !== "undefined") {
      const w = eval("window");
      if (w && w.localStorage) {
        const userStr = w.localStorage.getItem("user");
        if (userStr) return JSON.parse(userStr);
      }
    }
  } catch (e) {
    // ignore
  }
  return null;
}

export default {
  name: "Sidebar",
  props: {
    activeMenu: {
      type: String,
      default: "/user-management",
    },
  },
  computed: {
    menuItems() {
      const user = getCurrentUser();
      if (user && user.role === 2) {
        // 读者
        return [
          { index: "/dashboard", label: "数据可视化" },
          { index: "/profile", label: "个人信息" },
          { index: "/book-management", label: "图书管理" },
          { index: "/borrow-management", label: "借阅管理" },
          { index: "/system-config-management", label: "系统管理" },
        ];
      }
      // 管理员或未登录
      return [
        { index: "/dashboard", label: "数据可视化" },
        { index: "/user-management", label: "用户管理" },
        { index: "/book-management", label: "图书管理" },
        { index: "/borrow-management", label: "借阅管理" },
        { index: "/system-config-management", label: "系统管理" },
      ];
    },
  },
};
</script>

<style scoped>
.sidebar-menu {
  width: 220px;
  min-height: 100vh;
  border-right: 1px solid #e6e6e6;
  background: #fff;
}
</style>

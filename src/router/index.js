import Vue from "vue";
import Router from "vue-router";

// import MainLayout from "@/layout/MainLayout.vue";
// import UserManagement from "@/views/UserManagement.vue";
import Login from "@/components/Login.vue";
import UserManagement from "@/views/UserManagement.vue";
import BookManagement from "@/views/BookManagement.vue";
import BorrowManagement from "@/views/BorrowManagement.vue";
import SystemConfigManagement from "@/views/SystemConfigManagement.vue";
import Profile from "@/views/Profile.vue";
import Dashboard from "@/views/Dashboard.vue";
import Register from "@/components/Register.vue";

Vue.use(Router);

const router = new Router({
  mode: "history",
  routes: [
    {
      path: "/login",
      name: "Login",
      component: Login,
      meta: { public: true },
    },
    {
      path: "/register",
      name: "Register",
      component: Register,
      meta: { public: true },
    },
    {
      path: "/user-management",
      name: "UserManagement",
      component: UserManagement,
      meta: { title: "用户管理", roles: ["admin", "manager"] },
    },
    {
      path: "/book-management",
      name: "BookManagement",
      component: BookManagement,
      meta: { title: "图书管理", roles: ["admin", "manager"] },
    },
    {
      path: "/borrow-management",
      name: "BorrowManagement",
      component: BorrowManagement,
      meta: { title: "借阅管理", roles: ["admin", "manager"] },
    },
    {
      path: "/system-config-management",
      name: "SystemConfigManagement",
      component: SystemConfigManagement,
      meta: { title: "系统配置", roles: ["admin"] },
    },
    {
      path: "/profile",
      name: "Profile",
      component: Profile,
      meta: { title: "个人信息" },
    },
    {
      path: "/dashboard",
      name: "Dashboard",
      component: Dashboard,
      meta: { title: "数据可视化", roles: ["admin", "manager"] },
    },
    {
      path: "/",
      redirect: "/login",
    },
    {
      path: "*",
      redirect: "/login",
    },
  ],
});

// 路由登录拦截
router.beforeEach((to, from, next) => {
  // 获取 user 对象
  let user = null;
  try {
    const w = eval("window");
    if (w && w.localStorage) {
      const userStr = w.localStorage.getItem("user");
      if (userStr) user = JSON.parse(userStr);
    }
  } catch (e) {
    // 忽略异常
  }
  const isLoggedIn = user && user.username;

  // 未登录只能访问登录和注册页
  if (!isLoggedIn) {
    if (to.path === "/login" || to.path === "/register") {
      return next();
    } else {
      return next("/login");
    }
  }
  // 已登录访问登录/注册页自动跳转 dashboard
  if (to.path === "/login" || to.path === "/register") {
    if (to.path !== "/dashboard") {
      return next("/dashboard");
    } else {
      return next();
    }
  }
  // 其他情况直接放行
  return next();
});

export default router;

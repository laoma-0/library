<template>
  <div class="profile-layout">
    <Sidebar class="sidebar" :active-menu="'/profile'" />
    <div class="main-content">
      <Breadcrumb :items="[{ text: '个人信息' }]" />
      <div class="content-area">
        <el-card>
          <h2>个人信息</h2>
          <el-descriptions :column="1" border>
            <el-descriptions-item label="用户名">{{
              user.username
            }}</el-descriptions-item>
            <el-descriptions-item label="姓名">{{
              user.realName || "-"
            }}</el-descriptions-item>
            <el-descriptions-item label="角色">{{
              user.role === 1 ? "管理员" : "读者"
            }}</el-descriptions-item>
            <el-descriptions-item label="邮箱">{{
              user.email || "-"
            }}</el-descriptions-item>
            <el-descriptions-item label="注册时间">{{
              user.createTime || "-"
            }}</el-descriptions-item>
          </el-descriptions>
        </el-card>
      </div>
    </div>
  </div>
</template>

<script>
import Sidebar from "@/components/Sidebar.vue";
import Breadcrumb from "@/components/Breadcrumb.vue";

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
    // ignore error
  }
  return {};
}

export default {
  name: "Profile",
  components: { Sidebar, Breadcrumb },
  data() {
    return {
      user: getCurrentUser(),
    };
  },
  mounted() {
    // 未登录则跳转到登录页
    if (!this.user || !this.user.username) {
      this.$router.replace({ path: "/login" });
    }
  },
};
</script>

<style scoped>
.profile-layout {
  display: flex;
  min-height: 100vh;
  background: #f5f7fa;
}
.sidebar {
  width: 220px;
  background: #fff;
  border-right: 1px solid #e6e6e6;
}
.main-content {
  flex: 1;
  padding: 24px;
}
.content-area {
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px 0 rgba(0, 0, 0, 0.04);
  padding: 24px;
}
</style>

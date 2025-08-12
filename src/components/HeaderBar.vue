<template>
  <div class="header-bar">
    <div class="header-title">图书管理系统</div>
    <div class="header-actions">
      <el-button
        type="danger"
        size="mini"
        style="margin-left: 16px"
        @click="logout"
      >
        <i class="el-icon-switch-button" style="margin-right: 4px" />退出登录
      </el-button>
    </div>
  </div>
</template>

<script>
import Swal from "sweetalert2";
export default {
  name: "HeaderBar",
  methods: {
    logout() {
      Swal.fire({
        title: "确定要退出登录吗?",
        icon: "warning",
        showCancelButton: true,
        confirmButtonColor: "#3085d6",
        cancelButtonColor: "#d33",
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        reverseButtons: true,
      }).then((result) => {
        if (result.isConfirmed) {
          try {
            let w;
            try {
              w = eval("window");
            } catch (e) {
              w = undefined;
            }
            if (w && w.localStorage) {
              w.localStorage.removeItem("user");
              w.localStorage.removeItem("user_role");
              w.localStorage.removeItem("login_remember");
              w.localStorage.clear();
            }
          } catch (e) {
            // ignore
          }
          this.$router.replace("/login");
        }
      });
    },
  },
};
</script>

<style scoped>
.header-bar {
  width: 100%;
  height: 56px;
  background: #fff;
  display: flex;
  align-items: center;
  justify-content: space-between;
  box-shadow: 0 2px 8px 0 rgba(0, 0, 0, 0.04);
  padding: 0 32px;
  position: relative;
  z-index: 10;
}
.header-title {
  font-size: 20px;
  font-weight: bold;
  color: #409eff;
}
.header-actions {
  display: flex;
  align-items: center;
}
.el-dropdown-link {
  cursor: pointer;
  color: #333;
  font-size: 15px;
}
</style>

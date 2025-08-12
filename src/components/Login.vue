<template>
  <div class="login-bg">
    <div class="login-center">
      <div class="login-box">
        <h2 class="login-title">图书管理系统登录</h2>
        <el-form
          ref="loginForm"
          :model="loginForm"
          :rules="rules"
          class="login-form"
          @submit.prevent
        >
          <el-form-item prop="username">
            <el-input
              v-model="loginForm.username"
              placeholder="请输入账号"
              prefix-icon="el-icon-user"
              clearable
            />
          </el-form-item>
          <el-form-item prop="password">
            <el-input
              v-model="loginForm.password"
              :type="showPassword ? 'text' : 'password'"
              placeholder="请输入密码"
              prefix-icon="el-icon-lock"
              clearable
            >
              <template #suffix>
                <el-icon
                  style="cursor: pointer"
                  :class="showPassword ? 'el-icon-view' : 'el-icon-view-off'"
                  @click="showPassword = !showPassword"
                />
              </template>
            </el-input>
          </el-form-item>
          <el-form-item prop="role">
            <el-select
              v-model="loginForm.role"
              placeholder="请选择身份"
              style="width: 100%"
            >
              <el-option label="管理员" value="admin" />
              <el-option label="读者" value="user" />
            </el-select>
          </el-form-item>
          <el-form-item prop="remember">
            <el-checkbox v-model="loginForm.remember"> 记住密码 </el-checkbox>
          </el-form-item>
          <el-form-item>
            <el-button
              type="primary"
              :loading="loading"
              style="width: 100%"
              @click="handleLogin"
            >
              登录
            </el-button>
          </el-form-item>
        </el-form>
        <div style="text-align: center; margin-top: -8px">
          <el-button type="text" @click="showRegister"> 注册账号 </el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { Message } from "element-ui";
import axios from "axios";

export default {
  name: "Login",
  data() {
    return {
      loginForm: {
        username: "admin",
        password: "",
        remember: false,
        role: "admin",
      },
      showPassword: false,
      loading: false,
      rules: {
        username: [{ required: true, message: "请输入账号", trigger: "blur" }],
        password: [
          { required: true, message: "请输入密码", trigger: "blur" },
          { min: 6, max: 20, message: "密码长度6-20位", trigger: "blur" },
        ],
        role: [{ required: true, message: "请选择身份", trigger: "change" }],
      },
    };
  },
  methods: {
    handleLogin() {
      if (this.loading) return;
      this.$refs.loginForm.validate((valid) => {
        if (!valid) return;
        this.loading = true;
        axios
          .post("/api/users/login", {
            username: this.loginForm.username,
            password: this.loginForm.password,
            role: this.loginForm.role,
          })
          .then((res) => {
            this.loading = false;
            if (res.data && res.data.success) {
              Message.success("登录成功");
              // 先存储 user 信息
              if (res.data.user) {
                try {
                  const w = eval("window");
                  if (w && w.localStorage) {
                    w.localStorage.setItem(
                      "user",
                      JSON.stringify(res.data.user)
                    );
                  }
                } catch (e) {
                  /* 忽略异常 */
                }
              }
              this.$router.push({ path: "/dashboard" });
            } else {
              Message.error(
                res.data && res.data.message
                  ? res.data.message
                  : "账号或密码错误"
              );
            }
          })
          .catch((err) => {
            this.loading = false;
            let msg = "账号或密码错误";
            if (
              err.response &&
              err.response.data &&
              err.response.data.message
            ) {
              msg = err.response.data.message;
            }
            Message.error(msg);
          });
      });
    },
    showRegister() {
      this.$router.push({ path: "/register" });
    },
  },
};
</script>

<style scoped>
.login-bg {
  min-height: 100vh;
  width: 100vw;
  background: url("/public/背景.jpg") no-repeat center center fixed;
  background-size: cover;
  display: flex;
  align-items: center;
  justify-content: center;
}
.login-center {
  width: 100vw;
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
}
.login-box {
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 4px 32px 0 rgba(0, 0, 0, 0.1);
  padding: 40px 36px 32px 36px;
  min-width: 320px;
  max-width: 360px;
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
}
.login-title {
  text-align: center;
  margin-bottom: 32px;
  color: #409eff;
  font-weight: bold;
  font-size: 24px;
  letter-spacing: 2px;
}
.login-form {
  width: 100%;
}
@media (max-width: 600px) {
  .login-box {
    padding: 24px 10px 18px 10px;
    min-width: 0;
    max-width: 98vw;
  }
  .login-title {
    font-size: 18px;
    margin-bottom: 18px;
  }
}
</style>

<template>
  <div class="register-bg">
    <div class="register-center">
      <div class="register-box">
        <h2 class="register-title">注册账号</h2>
        <el-form
          ref="registerForm"
          :model="registerForm"
          :rules="registerRules"
          label-width="80px"
          class="register-form"
        >
          <el-form-item label="账号" prop="username">
            <el-input
              v-model="registerForm.username"
              placeholder="请输入账号"
            />
          </el-form-item>
          <el-form-item label="密码" prop="password">
            <el-input
              v-model="registerForm.password"
              type="password"
              placeholder="请输入密码"
            />
          </el-form-item>
          <el-form-item label="确认密码" prop="confirmPassword">
            <el-input
              v-model="registerForm.confirmPassword"
              type="password"
              placeholder="请再次输入密码"
            />
          </el-form-item>
          <el-form-item label="身份" prop="role">
            <el-select
              v-model="registerForm.role"
              placeholder="请选择身份"
              style="width: 100%"
            >
              <el-option label="管理员" value="admin" />
              <el-option label="读者" value="user" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button
              type="primary"
              :loading="registerLoading"
              style="width: 100%"
              @click="handleRegister"
            >
              注册
            </el-button>
          </el-form-item>
          <el-form-item>
            <el-button type="text" style="width: 100%" @click="goLogin"
              >返回登录</el-button
            >
          </el-form-item>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script>
import { Message } from "element-ui";
import axios from "axios";

export default {
  name: "Register",
  data() {
    return {
      registerForm: {
        username: "",
        password: "",
        confirmPassword: "",
        role: "",
      },
      registerLoading: false,
      registerRules: {
        username: [{ required: true, message: "请输入账号", trigger: "blur" }],
        password: [
          { required: true, message: "请输入密码", trigger: "blur" },
          { min: 6, max: 20, message: "密码长度6-20位", trigger: "blur" },
        ],
        confirmPassword: [
          { required: true, message: "请确认密码", trigger: "blur" },
          {
            validator: (rule, value, callback) => {
              if (value !== this.registerForm.password) {
                callback("两次输入密码不一致");
              } else {
                callback();
              }
            },
            trigger: "blur",
          },
        ],
        role: [{ required: true, message: "请选择身份", trigger: "change" }],
      },
    };
  },
  methods: {
    handleRegister() {
      this.$refs.registerForm.validate((valid) => {
        if (!valid) return;
        this.registerLoading = true;
        axios
          .post("/api/users/register", {
            username: this.registerForm.username,
            password: this.registerForm.password,
            role: this.registerForm.role,
          })
          .then((res) => {
            this.registerLoading = false;
            if (res.data && res.data.success) {
              Message.success("注册成功，请登录");
              this.goLogin();
            } else {
              Message.error(
                res.data && res.data.message ? res.data.message : "注册失败"
              );
            }
          })
          .catch((err) => {
            this.registerLoading = false;
            let msg = "注册失败";
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
    goLogin() {
      this.$router.push({ path: "/login" });
    },
  },
};
</script>

<style scoped>
.register-bg {
  min-height: 100vh;
  width: 100vw;
  background: url("/public/背景.jpg") no-repeat center center fixed;
  background-size: cover;
  display: flex;
  align-items: center;
  justify-content: center;
}
.register-center {
  width: 100vw;
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
}
.register-box {
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
.register-title {
  text-align: center;
  margin-bottom: 32px;
  color: #409eff;
  font-weight: bold;
  font-size: 24px;
  letter-spacing: 2px;
}
.register-form {
  width: 100%;
}
@media (max-width: 600px) {
  .register-box {
    padding: 24px 10px 18px 10px;
    min-width: 0;
    max-width: 98vw;
  }
  .register-title {
    font-size: 18px;
    margin-bottom: 18px;
  }
}
</style>

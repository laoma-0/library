<template>
  <el-dialog
    :visible="visible"
    @close="$emit('update:visible', false)"
    :title="user && user.id ? '编辑用户' : '添加用户'"
    width="400px"
  >
    <el-form :model="form" :rules="rules" ref="form" label-width="80px">
      <el-form-item label="用户名" prop="username">
        <el-input v-model="form.username" :disabled="!!user && !!user.id" />
      </el-form-item>
      <el-form-item v-if="!user || !user.id" label="密码" prop="password">
        <el-input v-model="form.password" type="password" />
      </el-form-item>
      <el-form-item label="真实姓名" prop="realName">
        <el-input v-model="form.realName" />
      </el-form-item>
      <el-form-item label="手机号" prop="phone">
        <el-input v-model="form.phone" />
      </el-form-item>
      <el-form-item label="邮箱" prop="email">
        <el-input v-model="form.email" />
      </el-form-item>
      <el-form-item label="性别" prop="gender">
        <el-select v-model="form.gender" placeholder="请选择">
          <el-option label="男" value="男" />
          <el-option label="女" value="女" />
        </el-select>
      </el-form-item>
      <el-form-item label="用户类型" prop="type">
        <el-select v-model="form.type" placeholder="请选择">
          <el-option label="管理员" value="管理员" />
          <el-option label="普通用户" value="普通用户" />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-switch
          v-model="form.status"
          active-value="正常"
          inactive-value="禁用"
        />
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="$emit('update:visible', false)"> 取 消 </el-button>
        <el-button type="primary" @click="submit"> 确 定 </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script>
export default {
  name: "UserForm",
  props: {
    user: {
      type: Object,
      default: () => ({}),
    },
    visible: Boolean,
  },
  emits: ["update:visible", "submit"],
  data() {
    return {
      form: {
        username: "",
        password: "",
        realName: "",
        phone: "",
        email: "",
        gender: "",
        type: "",
        status: "正常",
      },
      rules: {
        username: [
          { required: true, message: "请输入用户名", trigger: "blur" },
        ],
        password: [{ required: true, message: "请输入密码", trigger: "blur" }],
        realName: [
          { required: true, message: "请输入真实姓名", trigger: "blur" },
        ],
        phone: [{ required: true, message: "请输入手机号", trigger: "blur" }],
        email: [
          { required: true, message: "请输入邮箱", trigger: "blur" },
          { type: "email", message: "邮箱格式不正确", trigger: "blur" },
        ],
        gender: [{ required: true, message: "请选择性别", trigger: "change" }],
        type: [
          { required: true, message: "请选择用户类型", trigger: "change" },
        ],
      },
    };
  },
  watch: {
    user: {
      immediate: true,
      handler(val) {
        if (val) {
          this.form = { ...val };
        } else {
          this.form = {
            username: "",
            password: "",
            realName: "",
            phone: "",
            email: "",
            gender: "",
            type: "",
            status: "正常",
          };
        }
      },
    },
  },
  methods: {
    submit() {
      this.$refs.form.validate((valid) => {
        if (!valid) return;
        this.$emit("submit", { ...this.form });
        this.$emit("update:visible", false);
      });
    },
  },
};
</script>

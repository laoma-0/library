<template>
  <div class="user-search">
    <el-form :inline="true" :model="searchForm">
      <el-form-item label="用户名">
        <el-input
          v-model="searchForm.username"
          placeholder="请输入用户名"
          clearable
        />
      </el-form-item>
      <el-form-item label="手机号">
        <el-input
          v-model="searchForm.phone"
          placeholder="请输入手机号"
          clearable
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="$emit('search', { ...searchForm })"
          >搜索</el-button
        >
        <el-button @click="reset">重置</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
export default {
  name: "UserSearch",
  data() {
    return {
      searchForm: {
        username: "",
        phone: "",
      },
      expand: false,
    };
  },
  methods: {
    reset() {
      this.searchForm.username = "";
      this.searchForm.phone = "";
      this.$emit("reset");
    },
  },
};
</script>

<style scoped>
.user-search {
  margin-bottom: 16px;
}
</style>

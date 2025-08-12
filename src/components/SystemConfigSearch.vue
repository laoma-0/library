<template>
  <el-form :inline="true" :model="searchForm" class="system-config-search-form">
    <el-form-item label="配置键">
      <el-input v-model="searchForm.configKey" placeholder="请输入配置键" />
    </el-form-item>
    <el-form-item label="描述">
      <el-input v-model="searchForm.description" placeholder="请输入描述" />
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="onSearch">搜索</el-button>
      <el-button @click="onReset">重置</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
export default {
  name: "SystemConfigSearch",
  data() {
    return {
      searchForm: {
        configKey: "",
        description: "",
      },
    };
  },
  methods: {
    onSearch() {
      this.$emit("search", { ...this.searchForm });
    },
    onReset() {
      this.searchForm = { configKey: "", description: "" };
      this.$emit("reset");
    },
  },
};
</script>

<style scoped>
.system-config-search-form {
  margin-bottom: 16px;
}
</style>

<template>
  <el-form :inline="true" :model="searchForm" class="book-search-form">
    <el-form-item label="书名">
      <el-input v-model="searchForm.title" placeholder="请输入书名" />
    </el-form-item>
    <el-form-item label="作者">
      <el-input v-model="searchForm.author" placeholder="请输入作者" />
    </el-form-item>
    <el-form-item label="ISBN">
      <el-input v-model="searchForm.isbn" placeholder="请输入ISBN" />
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="onSearch">搜索</el-button>
      <el-button @click="onReset">重置</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
export default {
  name: "BookSearch",
  data() {
    return {
      searchForm: {
        title: "",
        author: "",
        isbn: "",
      },
    };
  },
  methods: {
    onSearch() {
      this.$emit("search", { ...this.searchForm });
    },
    onReset() {
      this.searchForm = { title: "", author: "", isbn: "" };
      this.$emit("reset");
    },
  },
};
</script>
<template>
  <div class="borrow-search">
    <el-form :inline="true" :model="searchForm">
      <el-form-item label="借阅人">
        <el-select
          v-model="searchForm.userId"
          placeholder="请选择借阅人"
          filterable
          clearable
        >
          <el-option
            v-for="user in userOptions"
            :key="user.id"
            :label="user.realName || user.username"
            :value="user.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="图书名称">
        <el-select
          v-model="searchForm.bookId"
          placeholder="请选择图书"
          filterable
          clearable
        >
          <el-option
            v-for="book in bookOptions"
            :key="book.id"
            :label="book.title"
            :value="book.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="状态">
        <el-select v-model="searchForm.status" clearable placeholder="全部">
          <el-option label="可借阅" :value="0" />
          <el-option label="已归还" :value="1" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="search">搜索</el-button>
        <el-button @click="reset"> 重置 </el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
export default {
  name: "BorrowSearch",
  emits: ["search", "reset"],
  data() {
    return {
      searchForm: {
        userId: "",
        bookId: "",
        status: "",
      },
      userOptions: [],
      bookOptions: [],
    };
  },
  mounted() {
    this.fetchUsers();
    this.fetchBooks();
  },
  methods: {
    fetchUsers() {
      this.$axios &&
        this.$axios.get("/api/users").then((res) => {
          this.userOptions = Array.isArray(res.data)
            ? res.data
            : res.data.data || [];
        });
    },
    fetchBooks() {
      this.$axios &&
        this.$axios.get("/api/books").then((res) => {
          this.bookOptions = res.data || [];
        });
    },
    search() {
      // 只传递有值的参数
      const params = {};
      if (this.searchForm.userId) params.userId = this.searchForm.userId;
      if (this.searchForm.bookId) params.bookId = this.searchForm.bookId;
      if (this.searchForm.status !== "") params.status = this.searchForm.status;
      this.$emit("search", params);
    },
    reset() {
      this.searchForm.userId = "";
      this.searchForm.bookId = "";
      this.searchForm.status = "";
      this.$emit("reset");
    },
  },
};
</script>

<style scoped>
.borrow-search {
  margin-bottom: 16px;
}
</style>

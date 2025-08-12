<template>
  <div style="display: flex; flex: 1; min-height: 0">
    <!-- 侧边栏组件，显示当前激活菜单 -->
    <Sidebar class="sidebar" :active-menu="'/book-management'" />
    <!-- 主内容区域 -->
    <div class="main-content">
      <!-- 面包屑导航组件 -->
      <Breadcrumb :items="breadcrumbItems" />
      <!-- 内容区域 -->
      <div class="content-area">
        <!-- 图书搜索组件，包含搜索和重置功能 -->
        <BookSearch @search="handleSearch" @reset="handleReset" />
        <!-- 操作按钮区域 -->
        <div class="actions">
          <!-- 添加图书按钮，仅管理员可见 -->
          <el-button v-if="isAdmin" type="primary" @click="openBookForm()">
            添加图书
          </el-button>
          <!-- 批量删除按钮，仅管理员可见且在有选中项时可用 -->
          <el-button
              v-if="isAdmin"
              type="danger"
              :disabled="!multipleSelection.length"
              @click="handleBatchDelete"
          >
            批量删除
          </el-button>
        </div>
        <!-- 图书表格组件，显示图书列表 -->
        <BookTable
            :books="pagedBooks"
            :total="total"
            :page="page"
            :page-size="pageSize"
            :show-actions="isAdmin"
            @selection-change="handleSelectionChange"
            @edit="openBookForm"
            @delete="handleDelete"
            @page-change="handlePageChange"
        />
        <!-- 分页组件 -->
<!--        <el-pagination-->
<!--            class="el-pagination is-background"          -->
<!--            style="margin-top: 15px; text-align: right"-->
<!--            background-->
<!--            layout="prev, pager, next, jumper"-->
<!--            :page-size="pageSize"-->
<!--            :current-page="page"-->
<!--            :total="total"-->
<!--            @current-change="handlePageChange"-->
<!--        />-->
      </div>
    </div>
    <!-- 将BookForm移动到主容器内部 -->
    <BookForm
      v-if="showBookForm"
      :book="currentBook"
      :visible="showBookForm"
      @update:visible="showBookForm = $event"
      @submit="handleBookSubmit"
    />
  </div>
</template>

<script>
import Sidebar from "@/components/Sidebar.vue";
import Breadcrumb from "@/components/Breadcrumb.vue";
import BookSearch from "@/components/BookSearch.vue";
import BookTable from "@/components/BookTable.vue";
import BookForm from "@/components/BookForm.vue"; // 新增: 导入图书表单组件

// 添加本地函数实现
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
  return null;
}

export default {
  name: "BookManagement",
  components: {
    Sidebar,
    Breadcrumb,
    BookSearch,
    BookTable,
    BookForm // 新增: 注册图书表单组件
  },
  data() {
    return {
      breadcrumbItems: [{ text: "图书列表" }],
      books: [],
      filteredBooks: [],
      page: 1,
      pageSize: 20, // 每页20条
      multipleSelection: [],
      showBookForm: false,
      currentBook: null,
      total: 0,
      searchParams: {},
      isAdmin: false,
    };
  },
  computed: {
    pagedBooks() {
      return this.filteredBooks;
    },
  },
  created() {
    // 使用导入的 getCurrentUser 函数
    const currentUser = getCurrentUser();
    this.isAdmin = currentUser && currentUser.role === 1;
    this.fetchBooks();
  },
  methods: {
    fetchBooks(params = {}) {
      const query = {
        page: this.page,
        size: this.pageSize,
        ...this.searchParams,  // 合并持久化搜索条件
        ...params,             // 合并即时参数
      };
      this.$axios
          .get("/api/books", {params: query})  // 修改接口地址为标准查询接口
          .then((res) => {
            let list = [];
            let total = 0;
            if (res.data) {
              if (Array.isArray(res.data.data)) {
                list = res.data.data;
                total = res.data.total || res.data.count || 0;
              } else if (Array.isArray(res.data.list)) {
                list = res.data.list;
                total = res.data.total || res.data.count || 0;
              } else if (Array.isArray(res.data)) {
                list = res.data;
                total = res.data.length;
              }
            }
            if (!Array.isArray(list)) list = [];
            this.filteredBooks = list;
            this.total = total;
          })
          .catch(() => {
            this.$message.error("获取图书数据失败，请稍后重试"); // 提供用户友好的错误提示
            this.filteredBooks = [];
            this.total = 0;
          });
    },
    handleSearch(params) {
      this.searchParams = params;
      this.page = 1;  // 重置到第一页
      this.fetchBooks();
    },
    handleReset() {
      this.searchParams = {};
      this.page = 1;
      this.fetchBooks();
    },
    handlePageChange(page) {
      this.page = page;
      this.fetchBooks();
    },
    handleSelectionChange(selection) {
      this.multipleSelection = selection;
    },
    openBookForm(book = null) {
      this.currentBook = book;
      this.showBookForm = true;
    },
    // 新增: 处理表单提交
    handleBookSubmit(book) {
      if (book.id) {
        this.$axios.put(`/api/books/${book.id}`, book).then(() => {
          this.showBookForm = false;
          this.fetchBooks();
        });
      } else {
        this.$axios.post("/api/books", book).then(() => {
          this.showBookForm = false;
          this.fetchBooks();
        });
      }
    },
    handleDelete(book) {
      this.$axios.delete(`/api/books/${book.id}`).then(() => {
        this.fetchBooks();
      });
    },
    handleBatchDelete() {
      if (!this.isAdmin) return;
      this.$confirm("确定批量删除所选图书吗？", "提示", {type: "warning"})
          .then(() => {
            const ids = this.multipleSelection.map((b) => b.id);
            if (!ids.length) {
              this.$message.warning("请先勾选要删除的图书");
              return;
            }
            this.$axios
                ? this.$axios
                    .post("/api/books/delete-batch", {ids}) // 修改为 POST 方法，路径为 /delete-batch
                    .then(() => {
                      this.$message.success("批量删除成功");
                      this.fetchBooks();
                      this.multipleSelection = [];
                    })
                : null;
          })
          .catch(() => {
          });
    },

    // 删除单个图书
    async deleteBook(id) {
      try {
        const confirmed = await this.$confirm('确定要删除该图书吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        });
        if (confirmed) {
          await this.$axios.delete(`/api/books/${id}`);
          this.$message.success('删除成功');
          this.fetchBooks(this.currentPage);
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('删除失败');
        }
      }
    },

    // 删除选中的图书
    async batchDelete() {
      if (this.multipleSelection.length === 0) {
        this.$message.warning('请先选择要删除的图书');
        return;
      }

      try {
        const confirmed = await this.$confirm(`确定要删除选中的${this.multipleSelection.length}本图书吗？`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        });
        if (confirmed) {
          const ids = this.multipleSelection.map(book => book.id);
          await this.$axios.post('/api/books/delete-batch', { ids });
          this.$message.success('批量删除成功');
          this.fetchBooks(this.currentPage);
          this.multipleSelection = [];
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('批量删除失败');
        }
      }
    }
  }
}

</script>

<style scoped>
/* 主布局样式 */
.user-management-layout {
  display: flex;
  min-height: 100vh;
  background: #f5f7fa;
}
/* 侧边栏样式 */
.sidebar {
  width: 220px;
  background: #fff;
  border-right: 1px solid #e6e6e6;
}
/* 主内容区域样式 */
.main-content {
  flex: 1;
  padding: 24px;
}
/* 内容区域样式 */
.content-area {
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px 0 rgba(0, 0, 0, 0.04);
  padding: 24px;
}
/* 操作按钮区域样式 */
.actions {
  margin-bottom: 16px;
  display: flex;
  gap: 12px;
}
</style>
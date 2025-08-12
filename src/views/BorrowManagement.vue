<template>
  <div style="display: flex; height: 100vh; flex-direction: column">
    <HeaderBar />
    <div style="display: flex; flex: 1; min-height: 0">
      <Sidebar class="sidebar" :active-menu="'/borrow-management'" />
      <div class="main-content">
        <Breadcrumb :items="breadcrumbItems" />
        <div class="content-area">
          <BorrowSearch @search="handleSearch" @reset="handleReset" />
          <div class="actions">
            <el-button v-if="isAdmin" type="primary" @click="openBorrowForm()">
              添加借阅
            </el-button>
            <el-button
              v-if="isAdmin"
              type="danger"
              :disabled="!multipleSelection.length"
              @click="handleBatchDelete"
            >
              批量删除
            </el-button>
          </div>
          <BorrowTable
            :borrows="pagedBorrows"
            :total="total"
            :page="page"
            :page-size="pageSize"
            :show-actions="isAdmin"
            @selection-change="handleSelectionChange"
            @edit="openBorrowForm"
            @delete="handleDelete"
            @page-change="handlePageChange"
          />
        </div>
      </div>
    </div>
    <BorrowForm
      v-if="showBorrowForm"
      :borrow="currentBorrow"
      :visible="showBorrowForm"
      @update:visible="showBorrowForm = $event"
      @submit="handleBorrowSubmit"
    />
  </div>
</template>

<script>
import Sidebar from "@/components/Sidebar.vue";
import Breadcrumb from "@/components/Breadcrumb.vue";
import HeaderBar from "@/components/HeaderBar.vue";
import BorrowSearch from "@/components/BorrowSearch.vue";
import BorrowTable from "@/components/BorrowTable.vue";
import BorrowForm from "@/components/BorrowForm.vue";

// 彻底避免 window 变量直接访问，使用 eval 间接访问
function getCurrentUser() {
  try {
    if (typeof window !== "undefined") {
      // 通过 eval 间接访问 window，防止构建工具静态分析报错
      const w = eval("window");
      if (w && w.localStorage) {
        const userStr = w.localStorage.getItem("user");
        if (userStr) return JSON.parse(userStr);
      }
    }
  } catch (e) {
    // 忽略异常
  }
  return null;
}

export default {
  name: "BorrowManagement",
  components: {
    Sidebar,
    Breadcrumb,
    HeaderBar,
    BorrowSearch,
    BorrowTable,
    BorrowForm,
  },
  data() {
    return {
      breadcrumbItems: [{ text: "借阅管理" }],
      borrows: [],
      filteredBorrows: [],
      page: 1,
      pageSize: 10,
      multipleSelection: [],
      showBorrowForm: false,
      currentBorrow: null,
      total: 0,
      searchParams: {},
      isAdmin: false,
    };
  },
  computed: {
    pagedBorrows() {
      return this.filteredBorrows;
    },
  },
  created() {
    const currentUser = getCurrentUser();
    this.isAdmin = currentUser && currentUser.role === 1;
    this.fetchBorrows();
  },
  methods: {
    fetchBorrows(params = {}) {
      const currentUser = getCurrentUser();
      const query = {
        page: this.page,
        size: this.pageSize,
        ...this.searchParams,
        ...params,
      };
      if (currentUser && currentUser.role === 2) {
        query.userId = currentUser.id;
      }
      this.$axios
        ? this.$axios
            .get("/api/borrow-records", { params: query })
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
              this.filteredBorrows = list.map((b) => ({
                ...b,
                userName: b.userName || "",
                bookName: b.bookTitle || "",
                borrowDate: b.borrowDate || "",
                returnDate: b.returnDate || "",
                // 状态统一为“借阅中/已归还”，与后端和搜索一致
                status:
                  b.status === 1 || b.status === "1" ? "已归还" : "借阅中",
              }));
              this.total = total;
            })
            .catch(() => {
              this.filteredBorrows = [];
              this.total = 0;
            })
        : null;
    },
    handleSearch(params) {
      this.searchParams = params;
      this.page = 1;
      this.fetchBorrows();
    },
    handleReset() {
      this.searchParams = {};
      this.page = 1;
      this.fetchBorrows();
    },
    handleSelectionChange(selection) {
      this.multipleSelection = selection;
    },
    openBorrowForm(borrow) {
      this.currentBorrow = borrow || null;
      this.showBorrowForm = true;
    },
    handleBorrowSubmit(borrow) {
      // 提交前将 status 转为数字 0/1，0-可借阅，1-已归还
      if (borrow.status === "可借阅") {
        borrow.status = 0;
      } else if (borrow.status === "已归还") {
        borrow.status = 1;
      }
      // 只保留后端需要的字段，去除多余属性
      const submitData = {
        id: borrow.id,
        userId: borrow.userId,
        bookId: borrow.bookId,
        borrowDate: borrow.borrowDate,
        returnDate: borrow.returnDate,
        status: borrow.status,
      };
      const isEdit = !!borrow.id;
      const user = getCurrentUser();
      const url = isEdit
        ? `/api/borrow-records/${borrow.id}?userId=${user ? user.id : ""}`
        : `/api/borrow-records?userId=${user ? user.id : ""}`;
      const method = isEdit ? "put" : "post";
      this.$axios
        ? this.$axios[method](url, submitData)
            .then(() => {
              this.$message.success(isEdit ? "修改成功" : "添加成功");
              this.showBorrowForm = false;
              this.fetchBorrows();
            })
            .catch((err) => {
              if (err.response && err.response.status === 403) {
                this.$message.error(
                  err.response.data || "没有权限操作借阅记录"
                );
              } else {
                this.$message.error(
                  err.response && err.response.data && err.response.data.message
                    ? err.response.data.message
                    : "操作失败，只允许管理员操作"
                );
              }
            })
        : null;
    },
    handleDelete(borrow) {
      this.$confirm("确定删除该记录吗?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          // 修正：删除时带上 userId 参数，后端需要
          const user = getCurrentUser();
          const userId = user ? user.id : "";
          this.$axios
            .delete(`/api/borrow-records/${borrow.id}`, { params: { userId } })
            .then(() => {
              this.$message.success("删除成功");
              this.fetchBorrows();
            })
            .catch((err) => {
              if (err.response && err.response.status === 403) {
                this.$message.error(
                  err.response.data || "没有权限操作他人借阅记录"
                );
              } else if (err.response && err.response.data) {
                this.$message.error(
                  typeof err.response.data === "string"
                    ? err.response.data
                    : err.response.data.message || "操作失败，只允许管理员操作"
                );
              } else {
                this.$message.error("操作失败，只允许管理员操作");
              }
            });
        })
        .catch(() => {});
    },
    handleBatchDelete() {
      if (this.multipleSelection.length === 0) {
        this.$message.warning("请先选择要删除的记录");
        return;
      }
      this.$confirm("确定删除选中的记录吗?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          // 修正：批量删除时带上 userId 参数，后端需要
          const user = getCurrentUser();
          const userId = user ? user.id : "";
          const ids = this.multipleSelection.map((b) => b.id);
          this.$axios
            .post(
              "/api/borrow-records/batch-delete",
              { ids },
              { params: { userId } }
            )
            .then((res) => {
              if (
                res.data &&
                res.data.failedIds &&
                res.data.failedIds.length > 0
              ) {
                this.$message.warning(
                  `部分记录删除失败，失败ID: ${res.data.failedIds.join(", ")}`
                );
              } else {
                this.$message.success("批量删除成功");
              }
              this.multipleSelection = [];
              this.fetchBorrows();
            })
            .catch((err) => {
              this.$message.error(
                (err.response &&
                  err.response.data &&
                  err.response.data.message) ||
                  "批量删除失败"
              );
            });
        })
        .catch(() => {});
    },
    handlePageChange(page) {
      this.page = page;
      this.fetchBorrows();
    },
  },
};
</script>

<style scoped>
.borrow-management-layout {
  display: flex;
  height: 100vh;
}

.sidebar {
  width: 250px;
}

.main-content {
  flex: 1;
  padding: 20px;
  display: flex;
  flex-direction: column;
}

.content-area {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.actions {
  margin-bottom: 20px;
}
</style>

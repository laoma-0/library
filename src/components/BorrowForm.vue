<template>
  <el-dialog
    :title="borrow && borrow.id ? '编辑借阅' : '添加借阅'"
    :visible="visible"
    width="400px"
    @close="$emit('update:visible', false)"
  >
    <el-form ref="form" :model="form" :rules="rules" label-width="90px">
      <el-form-item label="借阅人" prop="userId">
        <el-select
          v-model="form.userId"
          placeholder="请选择借阅人"
          filterable
          :disabled="!isAdmin"
          @change="onUserChange"
        >
          <el-option
            v-for="user in userOptions"
            :key="user.id"
            :label="user.realName || user.username"
            :value="user.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="图书" prop="bookId">
        <el-select
          v-model="form.bookId"
          placeholder="请选择图书"
          filterable
          :disabled="!isAdmin"
          @change="onBookChange"
        >
          <el-option
            v-for="book in bookOptions"
            :key="book.id"
            :label="book.title"
            :value="book.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="借阅日期" prop="borrowDate">
        <el-date-picker
          v-model="form.borrowDate"
          type="date"
          placeholder="选择日期"
          style="width: 100%"
          :disabled="!isAdmin"
        />
      </el-form-item>
      <el-form-item label="归还日期" prop="returnDate">
        <el-date-picker
          v-model="form.returnDate"
          type="date"
          placeholder="选择日期"
          style="width: 100%"
        />
      </el-form-item>
      <!-- 状态字段仅添加时显示，编辑时不显示 -->
      <el-form-item v-if="!borrow || !borrow.id" label="状态" prop="status">
        <el-select
          v-model="form.status"
          placeholder="请选择"
          :disabled="!isAdmin"
        >
          <el-option label="在借阅" :value="0">
            <template #default>
              <el-tag type="success"> 在借阅 </el-tag>
            </template>
          </el-option>
          <el-option label="已归还" :value="1">
            <template #default>
              <el-tag type="info"> 已归还 </el-tag>
            </template>
          </el-option>
        </el-select>
      </el-form-item>
    </el-form>
    <div v-if="form.borrowDate" style="margin-top: 10px; text-align: right">
      <el-tag
        :type="statusText === '借阅中' ? 'success' : 'info'"
        effect="light"
      >
        {{ statusText }}
      </el-tag>
    </div>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="$emit('update:visible', false)"> 取 消 </el-button>
        <el-button type="primary" @click="submit"> 确 定 </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script>
// 兼容全局localStorage获取当前用户
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
    // intentionally ignore error
  }
  return null;
}
export default {
  name: "BorrowForm",
  props: {
    borrow: {
      type: Object,
      default: () => ({}),
    },
    visible: Boolean,
  },
  emits: ["update:visible", "submit"],
  data() {
    return {
      form: {
        userId: "",
        userName: "",
        bookId: "",
        bookName: "",
        borrowDate: "",
        returnDate: "",
        status: 0, // 默认数字类型，0=借阅中
      },
      bookOptions: [], // 新增：图书下拉选项
      userOptions: [], // 新增：用户下拉选项
    };
  },
  mounted() {
    // 加载所有图书
    this.fetchBooks();
    this.fetchUsers(); // 新增
  },
  computed: {
    isAdmin() {
      const user = getCurrentUser();
      return user && user.role === 1;
    },
    rules() {
      return {
        userId: [{ required: true, message: "请输入借阅人", trigger: "blur" }],
        bookId: [{ required: true, message: "请输入图书", trigger: "blur" }],
        borrowDate: [
          { required: true, message: "请选择借阅日期", trigger: "change" },
        ],
        returnDate: this.isAdmin
          ? []
          : [{ required: true, message: "请选择归还日期", trigger: "change" }],
        status: [{ required: true, message: "请选择状态", trigger: "change" }],
      };
    },
    statusText() {
      return this.form.returnDate ? "已归还" : "借阅中";
    },
  },
  watch: {
    borrow: {
      immediate: true,
      handler(val) {
        if (val) {
          // 自动根据归还日期设置状态
          this.form = { ...val };
          if (typeof this.form.status === "string") {
            // 兼容旧数据
            this.form.status = this.form.status === "已归还" ? 1 : 0;
          }
          if (!("status" in val)) {
            this.form.status = val.returnDate ? 1 : 0;
          }
        } else {
          this.form = {
            userId: "",
            userName: "",
            bookId: "",
            bookName: "",
            borrowDate: "",
            returnDate: "",
            status: 0,
          };
        }
      },
    },
  },
  methods: {
    fetchBooks() {
      // 假设有全局axios
      this.$axios &&
        this.$axios.get("/api/books").then((res) => {
          this.bookOptions = res.data || [];
        });
    },
    fetchUsers() {
      this.$axios &&
        this.$axios.get("/api/users").then((res) => {
          // 兼容后端返回结构
          this.userOptions = Array.isArray(res.data)
            ? res.data
            : res.data.data || [];
        });
    },
    onBookChange(bookId) {
      const book = this.bookOptions.find((b) => b.id === bookId);
      this.form.bookName = book ? book.title : "";
    },
    onUserChange(userId) {
      const user = this.userOptions.find((u) => u.id === userId);
      this.form.userName = user ? user.realName || user.username : "";
    },
    submit() {
      this.$refs.form.validate((valid) => {
        if (!valid) return;
        if (!this.form.bookId) {
          this.$message && this.$message.warning("请选择图书（bookId）");
          return;
        }
        // 自动根据归还日期设置status为数字，后端需要0/1
        let formCopy = { ...this.form };
        formCopy.status = formCopy.returnDate ? 1 : 0; // 1=已归还, 0=借阅中
        this.$emit("submit", formCopy);
        this.$emit("update:visible", false);
      });
    },
  },
};
</script>

<template>
  <el-dialog
    :visible="visible"
    @update:visible="$emit('update:visible', $event)"
    :title="book && book.id ? '编辑图书' : '添加图书'"
  >
    <el-form :model="form" label-width="80px">
      <el-form-item label="书名">
        <el-input v-model="form.title" />
      </el-form-item>
      <el-form-item label="作者">
        <el-input v-model="form.author" />
      </el-form-item>
      <el-form-item label="ISBN">
        <el-input v-model="form.isbn" />
      </el-form-item>
      <el-form-item label="分类ID">
        <el-input v-model="form.categoryId" />
      </el-form-item>
      <el-form-item label="出版社">
        <el-input v-model="form.publisher" />
      </el-form-item>
      <el-form-item label="出版日期">
        <el-date-picker
          v-model="form.publishDate"
          type="date"
          placeholder="选择日期"
        />
      </el-form-item>
      <el-form-item label="价格">
        <el-input v-model="form.price" type="number" />
      </el-form-item>
      <el-form-item label="状态">
        <el-select v-model="form.status">
          <el-option label="可借阅" :value="1" />
          <el-option label="已借出" :value="0" />
        </el-select>
      </el-form-item>
      <el-form-item label="简介">
        <el-input v-model="form.description" type="textarea" />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="$emit('update:visible', false)"> 取 消 </el-button>
      <el-button type="primary" @click="onSubmit"> 确 定 </el-button>
    </template>
  </el-dialog>
</template>

<script>
export default {
  name: "BookForm",
  props: {
    book: {
      type: Object,
      default: () => ({}),
    },
    visible: Boolean,
  },
  emits: ["update:visible", "submit"],
  data() {
    return {
      form: {
        title: "",
        author: "",
        isbn: "",
        categoryId: "",
        publisher: "",
        publishDate: "",
        price: 0,
        status: 1,
        description: "",
      },
    };
  },
  watch: {
    book: {
      immediate: true,
      handler(val) {
        if (val) {
          this.form = { ...val };
        } else {
          this.form = {
            title: "",
            author: "",
            isbn: "",
            categoryId: "",
            publisher: "",
            publishDate: "",
            price: 0,
            status: 1,
            description: "",
          };
        }
      },
    },
  },
  methods: {
    onSubmit() {
      this.$emit("submit", { ...this.form });
    },
  },
};
</script>

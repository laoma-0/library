<template>
  <div>
    <el-table
      :data="books"
      style="width: 100%"
      @selection-change="$emit('selection-change', $event)"
    >
      <el-table-column v-if="showActions" type="selection" width="55" />
      <el-table-column prop="title" label="书名" />
      <el-table-column prop="author" label="作者" />
      <el-table-column prop="isbn" label="ISBN" />
      <el-table-column prop="categoryName" label="分类" />
      <el-table-column v-if="false" prop="categoryId" label="分类ID" />
      <el-table-column prop="publisher" label="出版社" />
      <el-table-column prop="publishDate" label="出版日期" />
      <el-table-column prop="price" label="价格" />
      <el-table-column prop="status" label="状态">
        <template #default="scope">
          <el-tag :type="scope.row.status === 1 ? 'success' : 'info'">
            {{ scope.row.status === 1 ? "可借阅" : "已借出" }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="description" label="简介" />
      <el-table-column v-if="showActions" label="操作" width="180">
        <template #default="scope">
          <el-button size="mini" @click="$emit('edit', scope.row)">
            编辑
          </el-button>
          <el-button
            size="mini"
            type="danger"
            @click="$emit('delete', scope.row)"
          >
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      style="margin-top: 16px; text-align: right"
      background
      layout="prev, pager, next, jumper, total"
      :total="total"
      :page-size="pageSize"
      :current-page="page"
      @current-change="$emit('page-change', $event)"
    />
  </div>
</template>

<script>
export default {
  name: "BookTable",
  props: {
    books: {
      type: Array,
      default: () => [],
    },
    total: {
      type: Number,
      default: 0,
    },
    page: {
      type: Number,
      default: 1,
    },
    pageSize: {
      type: Number,
      default: 10,
    },
    showActions: {
      type: Boolean,
      default: false,
    },
  },
  emits: ["selection-change", "edit", "delete", "page-change"],
};
</script>

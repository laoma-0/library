<template>
  <div>
    <el-table
      :data="borrows"
      border
      stripe
      @selection-change="$emit('selection-change', $event)"
    >
      <el-table-column v-if="showActions" type="selection" width="50" />
      <el-table-column prop="userName" label="借阅人" />
      <el-table-column prop="bookName" label="图书名称" />
      <el-table-column prop="bookCategory" label="图书分类" />
      <el-table-column prop="borrowDate" label="借阅日期" />
      <el-table-column prop="returnDate" label="归还日期" />
      <el-table-column prop="status" label="状态">
        <template #default="scope">
          <el-tag :type="scope.row.status === '借阅中' ? 'success' : 'info'">
            {{ scope.row.status }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column v-if="showActions" label="操作">
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
      style="margin-top: 15px; text-align: right"
      background
      layout="prev, pager, next, jumper"
      :total="total"
      :page-size="pageSize"
      :current-page="page"
      @current-change="$emit('page-change', $event)"
    />
  </div>
</template>

<script>
export default {
  name: "BorrowTable",
  props: {
    borrows: {
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
    multipleSelection: {
      type: Array,
      default: () => [],
    },
    showActions: {
      type: Boolean,
      default: false,
    },
  },
  emits: ["selection-change", "edit", "delete", "page-change"],
};
</script>

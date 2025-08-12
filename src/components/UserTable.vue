<template>
  <div>
    <el-table
      :data="users"
      border
      stripe
      @selection-change="$emit('selection-change', $event)"
      @sort-change="$emit('sort-change', $event)"
    >
      <el-table-column type="selection" width="50" />
      <el-table-column prop="realName" label="姓名" width="120" />
      <el-table-column prop="username" label="用户名" width="120" />
      <el-table-column prop="phone" label="手机号" width="150" />
      <el-table-column prop="email" label="邮箱" width="180" />
      <el-table-column prop="status" label="状态" width="80">
        <template #default="scope">
          <el-tag :type="scope.row.status === '正常' ? 'success' : 'danger'">
            {{ scope.row.status }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="type" label="角色" width="100" />
      <el-table-column prop="createTime" label="注册时间" width="180" />
      <el-table-column label="操作">
        <template #default="scope">
          <el-button size="mini" @click="$emit('edit', scope.row)"
            >编辑</el-button
          >
          <el-button
            size="mini"
            type="danger"
            @click="$emit('delete', scope.row)"
            >删除</el-button
          >
          <el-button
            size="mini"
            :type="scope.row.status === '正常' ? 'warning' : 'success'"
            @click="$emit('status-change', scope.row)"
          >
            {{ scope.row.status === "正常" ? "禁用" : "启用" }}
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
  name: "UserTable",
  props: {
    users: Array,
    total: Number,
    page: Number,
    pageSize: Number,
    multipleSelection: Array,
  },
  emits: [
    "selection-change",
    "sort-change",
    "edit",
    "delete",
    "status-change",
    "page-change",
  ],
};
</script>

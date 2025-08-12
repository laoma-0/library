<template>
  <div>
    <el-table :data="configs" style="width: 100%">
      <el-table-column prop="description" label="描述" />
      <el-table-column prop="configValue" label="配置值" />
      <el-table-column prop="createTime" label="创建时间" />
      <el-table-column prop="updateTime" label="更新时间" />
      <el-table-column v-if="showActions" label="操作" width="160">
        <template #default="scope">
          <el-button size="mini" @click="$emit('edit', scope.row)">
            编辑
          </el-button>
          <el-button
            size="mini"
            :disabled="
              scope.row.isSystem === 1 || scope.row.isSystemText === '是'
            "
            type="danger"
            @click="$emit('delete', scope.row)"
          >
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
export default {
  name: "SystemConfigTable",
  props: {
    configs: {
      type: Array,
      default: () => [],
    },
    showActions: {
      type: Boolean,
      default: false,
    },
  },
  emits: ["edit", "delete"],
};
</script>

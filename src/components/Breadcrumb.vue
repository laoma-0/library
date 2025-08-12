<template>
  <el-breadcrumb separator="/">
    <el-breadcrumb-item
      v-for="(item, idx) in items"
      :key="idx"
      :to="item.to || undefined"
    >
      {{ item.text }}
    </el-breadcrumb-item>
  </el-breadcrumb>
</template>

<script>
export default {
  name: "Breadcrumb",
  props: {
    items: {
      type: Array,
      required: true,
    },
  },
};
</script>

<style scoped>
.el-breadcrumb {
  margin-bottom: 18px;
  font-size: 15px;
}
</style>

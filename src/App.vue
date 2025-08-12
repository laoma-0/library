<template>
  <div id="app">
    <router-view /> <!-- 仅保留路由视图容器，布局由MainLayout处理 -->
  </div>
</template>

<script>
export default {
  name: 'App'  // 确保组件有name属性
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
}
</style>
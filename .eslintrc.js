module.exports = {
  extends: [
    'plugin:vue/recommended',  // 升级为recommended配置
    'eslint:recommended'
  ],
  parserOptions: {
    parser: '@babel/eslint-parser',
    ecmaVersion: 2020,  // 添加ES版本
    sourceType: 'module'  // 支持ES模块
  },
  rules: {
    'vue/multi-word-component-names': 'off'  // 关闭多单词组件名检查
  }
}
module.exports = {
  devServer: {
    proxy: {
      "/api": {
        target: "http://localhost:8084",
        changeOrigin: true,
        pathRewrite: function (path) {
          return path.replace(/^\/api/, "/api");
        },
      },
    },
  },
};
// 此文件内容已正确，无需修改。
// 如果你仍然遇到数据不显示的问题，vue.config.js 不是原因。

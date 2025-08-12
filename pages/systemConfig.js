import {
  showConfirmDialog,
  showSuccessToast,
  showErrorToast,
} from "../utils/swal";

function handleResetConfig() {
  showConfirmDialog("确认重置", "确定要重置系统配置吗？")
    .then((result) => {
      if (result.isConfirmed) {
        // ...重置逻辑...
        showSuccessToast("重置成功");
      }
    })
    .catch(() => {
      showErrorToast("操作被取消");
    });
}

export { handleResetConfig };

// 示例：在页面按钮绑定事件
// <button onClick={handleResetConfig}>重置系统配置</button>

// 在页面渲染部分添加按钮
/*
  <button onClick={handleResetConfig}>重置系统配置</button>
*/

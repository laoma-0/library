const Swal = require("sweetalert2");

function showConfirmDialog(
  title,
  text,
  confirmButtonText = "确定",
  cancelButtonText = "取消"
) {
  return Swal.fire({
    title,
    text,
    icon: "warning",
    showCancelButton: true,
    confirmButtonText,
    cancelButtonText,
  });
}

function showSuccessToast(message) {
  return Swal.fire({
    icon: "success",
    title: message,
    timer: 1500,
    showConfirmButton: false,
    position: "top-end",
    toast: true,
  });
}

function showErrorToast(message) {
  return Swal.fire({
    icon: "error",
    title: message,
    timer: 1500,
    showConfirmButton: false,
    position: "top-end",
    toast: true,
  });
}

module.exports = { showConfirmDialog, showSuccessToast, showErrorToast };

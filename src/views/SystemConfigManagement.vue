<template>
  <div style="display: flex; height: 100vh; flex-direction: column">
    <HeaderBar />
    <div style="display: flex; flex: 1; min-height: 0">
      <Sidebar class="sidebar" :active-menu="'/system-config-management'" />
      <div
        style="
          flex: 1;
          overflow: auto;
          background: #f5f7fa;
          display: flex;
          align-items: center;
          justify-content: center;
        "
      >
        <div class="intro-container">
          <h1 class="intro-title">图书馆简介</h1>
          <p>
            <b>名称：</b>
            {{ libraryInfo && libraryInfo.name ? libraryInfo.name : "-" }}
          </p>
          <p>
            <b>地址：</b>
            {{ libraryInfo && libraryInfo.address ? libraryInfo.address : "-" }}
          </p>
          <p>
            <b>电话：</b>
            {{ libraryInfo && libraryInfo.phone ? libraryInfo.phone : "-" }}
          </p>
          <p>
            <b>邮箱：</b>
            {{ libraryInfo && libraryInfo.email ? libraryInfo.email : "-" }}
          </p>
          <p>
            <b>开放时间：</b><br />
            {{
              libraryInfo && libraryInfo.openingHours
                ? libraryInfo.openingHours
                : "-"
            }}
          </p>
          <el-divider />
          <h2 class="intro-title" style="font-size: 24px; margin-bottom: 16px">
            入馆须知
          </h2>
          <p>
            请您在入馆时仔细阅读以下须知，以确保您的入馆安全。如有疑问，请联系图书馆工作人员。
          </p>
          <p>入馆时请遵守图书馆各项规定，尤其要注意以下事项：</p>
          <el-table
            :data="filteredConfigs"
            style="width: 100%; margin-bottom: 32px"
          >
            <el-table-column label="配置项" prop="description" width="200" />
            <el-table-column label="配置值" prop="configValue" />
          </el-table>
        </div>
        <el-dialog
          v-if="showConfigDialog"
          :title="editingConfig ? '编辑参数' : '新增参数'"
          v-model="showConfigDialog"
          @close="handleDialogClose"
          width="400px"
        >
          <el-form :model="configForm" label-width="80px">
            <el-form-item label="配置项">
              <el-input
                v-model="configForm.configKey"
                :disabled="!!editingConfig"
              />
            </el-form-item>
            <el-form-item label="描述">
              <el-input v-model="configForm.description" />
            </el-form-item>
            <el-form-item label="配置值">
              <el-input v-model="configForm.configValue" />
            </el-form-item>
          </el-form>
          <template #footer>
            <el-button @click="showConfigDialog = false">取消</el-button>
            <el-button type="primary" @click="saveConfig">保存</el-button>
          </template>
        </el-dialog>
      </div>
    </div>
  </div>
</template>

<script>
import Sidebar from "@/components/Sidebar.vue";
import HeaderBar from "@/components/HeaderBar.vue";
import axios from "axios";
import { MessageBox as ElMessageBox, Message as ElMessage } from "element-ui";
export default {
  name: "SystemConfigManagement",
  components: { Sidebar, HeaderBar },
  data() {
    return {
      configs: [],
      libraryInfo: null,
      isAdmin: true, // 临时设为true，确保有操作按钮
      editingConfig: null,
      showConfigDialog: false,
      configForm: {
        id: null,
        configKey: "",
        description: "",
        configValue: "",
      },
    };
  },
  mounted() {
    this.fetchConfigs();
    // 可根据实际情况设置 isAdmin
    // this.isAdmin = ...;
  },
  computed: {
    filteredConfigs() {
      // 只展示非简介类参数
      const introKeys = [
        "library_name",
        "library_address",
        "library_phone",
        "library_email",
        "library_opening_hours",
      ];
      return this.configs.filter((c) => !introKeys.includes(c.configKey));
    },
  },
  methods: {
    async fetchConfigs() {
      const res = await axios.get("/api/system-configs", {
        params: { all: true },
      });
      this.configs = res.data.data || [];
      // 提取图书馆基础信息
      const get = (key) =>
        (this.configs.find((c) => c.configKey === key) || {}).configValue || "";
      this.libraryInfo = {
        name: get("library_name"),
        address: get("library_address"),
        phone: get("library_phone"),
        email: get("library_email"),
        openingHours: get("library_opening_hours"),
      };
    },
    editConfig(row) {
      this.editingConfig = row;
      this.configForm = { ...row };
      this.showConfigDialog = true;
    },
    addConfig() {
      this.editingConfig = null;
      this.configForm = {
        id: null,
        configKey: "",
        description: "",
        configValue: "",
      };
      this.showConfigDialog = true;
    },
    async saveConfig() {
      try {
        if (!this.configForm.configKey || !this.configForm.description) {
          ElMessage.error("配置项和描述不能为空");
          return;
        }
        // 只传递后端需要的字段
        const payload = {
          configKey: this.configForm.configKey,
          description: this.configForm.description,
          configValue: this.configForm.configValue,
        };
        if (this.editingConfig) {
          // 编辑
          await axios.put(`/api/system-configs/${this.configForm.id}`, payload);
          ElMessage.success("修改成功");
        } else {
          // 新增
          await axios.post("/api/system-configs", payload);
          ElMessage.success("新增成功");
        }
        this.showConfigDialog = false;
        this.fetchConfigs();
      } catch (e) {
        ElMessage.error("保存失败");
      }
    },
    deleteConfig(row) {
      // eslint-disable-next-line no-undef
      console.log("删除参数 row:", row);
      ElMessageBox.confirm("确定删除该配置项吗？", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(async () => {
          try {
            const res = await axios.delete(`/api/system-configs/${row.id}`);
            // eslint-disable-next-line no-undef
            console.log("删除返回:", res);
            ElMessage.success("删除成功");
            this.fetchConfigs();
          } catch (e) {
            // eslint-disable-next-line no-undef
            console.error("删除失败:", row.id, e?.response?.data || e);
            ElMessage.error(
              e && e.response && e.response.data && e.response.data.message
                ? e.response.data.message
                : e && e.message
                ? e.message
                : "删除失败"
            );
          }
        })
        .catch(() => {});
    },
    handleDialogClose() {
      this.showConfigDialog = false;
    },
    editIntroConfig(key) {
      // 查找简介配置项
      const config = this.configs.find((c) => c.configKey === key);
      if (config) {
        this.editConfig(config);
      } else {
        // 若不存在则新建，configValue 取当前显示值
        let desc = "";
        let value = "";
        switch (key) {
          case "library_name":
            desc = "名称";
            value =
              this.libraryInfo && this.libraryInfo.name
                ? this.libraryInfo.name
                : "";
            break;
          case "library_address":
            desc = "地址";
            value =
              this.libraryInfo && this.libraryInfo.address
                ? this.libraryInfo.address
                : "";
            break;
          case "library_phone":
            desc = "电话";
            value =
              this.libraryInfo && this.libraryInfo.phone
                ? this.libraryInfo.phone
                : "";
            break;
          case "library_email":
            desc = "邮箱";
            value =
              this.libraryInfo && this.libraryInfo.email
                ? this.libraryInfo.email
                : "";
            break;
          case "library_opening_hours":
            desc = "开放时间";
            value =
              this.libraryInfo && this.libraryInfo.openingHours
                ? this.libraryInfo.openingHours
                : "";
            break;
        }
        this.editingConfig = null;
        this.configForm = {
          id: null,
          configKey: key,
          description: desc,
          configValue: value,
        };
        this.showConfigDialog = true;
      }
    },
  },
};
</script>

<style scoped>
.intro-container {
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  padding: 48px 60px;
  padding-top: 144px;
  max-width: 2000px;
  margin: 64px auto;
}
.intro-title {
  font-size: 32px;
  font-weight: bold;
  color: #409eff;
  margin-bottom: 24px;
  text-align: center;
}
</style>

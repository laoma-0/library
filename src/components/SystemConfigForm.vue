<template>
  <el-dialog
    :visible="visible"
    @close="$emit('update:visible', false)"
    :title="config && config.id ? '编辑配置' : '添加配置'"
    width="500px"
  >
    <el-form :model="form" label-width="100px" :rules="rules" ref="form">
      <el-form-item label="配置键" prop="configKey">
        <el-input
          v-model="form.configKey"
          :disabled="!!config && !!config.id"
        />
      </el-form-item>
      <el-form-item label="配置值" prop="configValue">
        <el-input v-model="form.configValue" type="textarea" />
      </el-form-item>
      <el-form-item label="类型" prop="configType">
        <el-select v-model="form.configType" placeholder="请选择类型">
          <el-option label="文本" :value="0" />
          <el-option label="数字" :value="1" />
          <el-option label="布尔" :value="2" />
          <el-option label="JSON" :value="3" />
        </el-select>
      </el-form-item>
      <el-form-item label="描述" prop="description">
        <el-input v-model="form.description" />
      </el-form-item>
      <el-form-item label="系统内置" prop="isSystem">
        <el-switch
          v-model="form.isSystem"
          :active-value="1"
          :inactive-value="0"
          :disabled="!!config && !!config.id"
        />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="$emit('update:visible', false)">取 消</el-button>
      <el-button type="primary" @click="onSubmit">确 定</el-button>
    </template>
  </el-dialog>
</template>

<script>
export default {
  name: "SystemConfigForm",
  props: {
    config: {
      type: Object,
      default: () => ({}),
    },
    visible: Boolean,
  },
  emits: ["update:visible", "submit"],
  data() {
    return {
      form: {
        configKey: "",
        configValue: "",
        configType: 0,
        description: "",
        isSystem: 0,
      },
      rules: {
        configKey: [
          { required: true, message: "请输入配置键", trigger: "blur" },
        ],
        configValue: [
          { required: true, message: "请输入配置值", trigger: "blur" },
        ],
        configType: [
          { required: true, message: "请选择类型", trigger: "change" },
        ],
      },
    };
  },
  watch: {
    config: {
      immediate: true,
      handler(val) {
        if (val) {
          this.form = { ...val };
        } else {
          this.form = {
            configKey: "",
            configValue: "",
            configType: 0,
            description: "",
            isSystem: 0,
          };
        }
      },
    },
  },
  methods: {
    onSubmit() {
      this.$refs.form.validate((valid) => {
        if (!valid) return;
        this.$emit("submit", { ...this.form });
        this.$emit("update:visible", false);
      });
    },
  },
};
</script>

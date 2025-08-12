<template>
  <div style="display: flex; height: 100vh; flex-direction: column">
    <HeaderBar />
    <div style="display: flex; flex: 1; min-height: 0">
      <Sidebar class="sidebar" :active-menu="'/dashboard'" />
      <div style="flex: 1; overflow: auto">
        <div class="dashboard-container">
          <el-row :gutter="20" class="dashboard-cards">
            <el-col :span="8">
              <el-card>
                <div class="card-title">用户总数</div>
                <div class="card-value">
                  {{ userCount }}
                </div>
              </el-card>
            </el-col>
            <el-col :span="8">
              <el-card>
                <div class="card-title">图书总数</div>
                <div class="card-value">
                  {{ bookCount }}
                </div>
              </el-card>
            </el-col>
            <el-col :span="8">
              <el-card>
                <div class="card-title">借阅总数</div>
                <div class="card-value">
                  {{ borrowCount }}
                </div>
              </el-card>
            </el-col>
          </el-row>
          <el-row :gutter="20" class="dashboard-charts">
            <el-col :span="12">
              <el-card>
                <div class="chart-title">图书分类分布</div>
                <div ref="categoryChart" style="height: 350px"></div>
              </el-card>
            </el-col>
            <el-col :span="12">
              <el-card>
                <div class="chart-title">借阅量趋势</div>
                <div ref="borrowTrendChart" style="height: 350px"></div>
              </el-card>
            </el-col>
          </el-row>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import * as echarts from "echarts";
import axios from "axios";
import Sidebar from "@/components/Sidebar.vue";
import HeaderBar from "@/components/HeaderBar.vue";
export default {
  name: "Dashboard",
  components: {
    Sidebar,
    HeaderBar,
  },
  data() {
    return {
      userCount: 0,
      bookCount: 0,
      borrowCount: 0,
      categoryData: [],
      borrowTrend: {
        months: [],
        data: [],
      },
    };
  },
  mounted() {
    this.fetchAllData();
  },
  methods: {
    async fetchAllData() {
      // 获取用户总数
      const userRes = await axios.get("/api/users");
      this.userCount =
        userRes.data.total ||
        (userRes.data.data ? userRes.data.data.length : 0);
      // 获取图书数据
      const bookRes = await axios.get("/api/books/with-category");
      const books = bookRes.data.data || [];
      this.bookCount = bookRes.data.total || books.length;
      // 分类统计
      const categoryMap = {};
      books.forEach((b) => {
        const name = b.categoryName || "未知";
        categoryMap[name] = (categoryMap[name] || 0) + 1;
      });
      this.categoryData = Object.keys(categoryMap).map((name) => ({
        name,
        value: categoryMap[name],
      }));
      // 获取借阅数据
      const borrowRes = await axios.get("/api/borrow-records");
      const borrows = borrowRes.data.data || [];
      this.borrowCount = borrowRes.data.total || borrows.length;
      // 借阅趋势（按月统计）
      const trendMap = {};
      borrows.forEach((b) => {
        if (b.borrowDate) {
          const d = new Date(b.borrowDate);
          const ym = `${d.getFullYear()}-${(d.getMonth() + 1)
            .toString()
            .padStart(2, "0")}`;
          trendMap[ym] = (trendMap[ym] || 0) + 1;
        }
      });
      const months = Object.keys(trendMap).sort();
      this.borrowTrend.months = months;
      this.borrowTrend.data = months.map((m) => trendMap[m]);
      // 渲染图表
      this.$nextTick(() => {
        this.initCategoryChart();
        this.initBorrowTrendChart();
      });
    },
    initCategoryChart() {
      const chart = echarts.init(this.$refs.categoryChart);
      chart.setOption({
        tooltip: { trigger: "item" },
        legend: { top: "bottom" },
        series: [
          {
            name: "图书分类",
            type: "pie",
            radius: "60%",
            data: this.categoryData,
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: "rgba(0, 0, 0, 0.5)",
              },
            },
          },
        ],
      });
    },
    initBorrowTrendChart() {
      const chart = echarts.init(this.$refs.borrowTrendChart);
      chart.setOption({
        tooltip: { trigger: "axis" },
        xAxis: { type: "category", data: this.borrowTrend.months },
        yAxis: { type: "value" },
        series: [
          {
            name: "借阅量",
            type: "line",
            data: this.borrowTrend.data,
            smooth: true,
          },
        ],
      });
    },
  },
};
</script>

<style scoped>
.dashboard-container {
  padding: 24px;
}
.dashboard-cards {
  margin-bottom: 24px;
}
.card-title {
  font-size: 16px;
  color: #888;
}
.card-value {
  font-size: 32px;
  font-weight: bold;
  margin-top: 8px;
}
.chart-title {
  font-size: 16px;
  margin-bottom: 12px;
}
</style>

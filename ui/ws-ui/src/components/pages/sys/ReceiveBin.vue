<template>
  <el-card class="box-card">
    <div class="mod-log">
      <el-form :inline="true" :model="dataForm" class="demo-form-inline">
        <el-form-item>
          <el-input v-model="dataForm.subject" clearable placeholder="主题"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button @click="getDataList">查询</el-button>
        </el-form-item>
      </el-form>
      <el-table :data="dataList" border style="width: 100%">
        <el-table-column type="selection" width="55"> </el-table-column>
        <el-table-column prop="mailId" label="ID" width="60">
        </el-table-column>
        <el-table-column prop="subject" label="主题" width="300">
        </el-table-column>
        <el-table-column prop="from_user_name" label="发信人" width="200">
        </el-table-column>
        <el-table-column prop="createTime" label="收信日期" width="200">
        </el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope" width="280">
            <el-button size="mini" type="primary" @click="handleReadMail(scope.$index, scope.row)">浏览</el-button>

            <el-button size="mini" type="danger" @click="handleDelete(scope.$index, scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination @size-change="sizeChangeHandle" @current-change="currentChangeHandle" :current-page="pageIndex"
                     :page-sizes="[5, 10, 20, 50, 100]" :page-size="pageSize" :total="totalPage"
                     layout="total, sizes, prev, pager, next, jumper" style="margin-top: 30px">
      </el-pagination>
    </div>

    <el-dialog :title="'来自管理员的通知'" width="35%" :visible.sync="dialogFormVisible" text-align="center">
      <el-form :model="dataDialogForm">
        <el-form-item label="内容" label-width="120px" prop="content">
          <el-input type="textarea" v-model="dataDialogForm.content" placeholder="内容" style="width: 300px"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">我已知晓</el-button>
      </div>
    </el-dialog>

  </el-card>
</template>
<script>
export default {
  name: "receiveBin",
  data() {
    return {
      dataForm: {
        subject: "",
      },

      dataDialogForm: {
        content: "",
      },
      dataList: [],
      pageIndex: 1,
      pageSize: 5,
      totalPage: 0,
      dataListLoading: false,
      dialogFormVisible: false,
      dialogFormSubmitVisible: false,
    };
  },
  methods: {
    sizeChangeHandle(val) {
      this.pageSize = val;
      this.pageIndex = 1;
      this.getDataList();
    },
    currentChangeHandle(val) {
      this.pageIndex = val;
      this.getDataList();
    },
    handleReadMail(index,row){
      this.$http
        .get("/sys/sysMail/showMail?mailId=" + row.mailId)
        .then((res) => {
          console.log(res)
          this.dataDialogForm.content = res.data.data;
          this.dialogFormVisible = true;
        });
    },
    getDataList() {
      if (this.dataListLoading) {
        return;
      }
      this.dataListLoading = true;
      // 声明params 查询的参数
      const params = {
        params: {
          pageIndex: this.pageIndex,
          pageSize: this.pageSize,
          subject: this.dataForm.subject,
        },
      };
      this.$http.get("/sys/sysMail/list", params).then((res) => {
        console.log(res);
        this.dataList = res.data.data.list;
        this.totalPage = res.data.data.totalCount;
        this.dataListLoading = false;
      });
    },

    handleDelete(index, item) {
      // 删除邮件
      this.$confirm("此操作将永久该记录, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          if (this.dialogFormSubmitVisible) {
            return;
          }
          this.dialogFormSubmitVisible = true;
          this.$http
            .get("/sys/sysMail/deleteMail?mailId=" + item.mailId)
            .then((res) => {
              console.log(res)

              this.$message({
                type: "success",
                message: "删除成功!",
              });

              this.dialogFormSubmitVisible = false;
              // 刷新数据
              this.getDataList();
            });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除",
          });
        });
    },
  },
  mounted() {
    this.getDataList();
  },
};
</script>



















































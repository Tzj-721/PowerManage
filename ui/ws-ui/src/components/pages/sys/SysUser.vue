<template>
  <el-card class="box-card">
    <div class="mod-user">
      <el-form :inline="true" :model="dataForm" class="demo-form-inline">
        <el-form-item>
          <el-input v-model="dataForm.userName" clearable placeholder="用户名"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button @click="getDataList">查询</el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="openDialog()">新增</el-button>
        </el-form-item>
      </el-form>
      <el-table :data="dataList" border style="width: 100%">
        <el-table-column type="selection" width="55"> </el-table-column>
        <el-table-column prop="userId" label="ID" width="60">
        </el-table-column>
        <el-table-column prop="username" label="用户名" width="150">
        </el-table-column>
        <el-table-column prop="email" label="邮箱" width="230">
        </el-table-column>
        <el-table-column prop="mobile" label="电话" width="230">
        </el-table-column>
        <el-table-column prop="status" label="状态" width="84">
          <template slot-scope="scope">
            <span>{{ scope.row.status == 1 ? '正常' : '禁用' }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="250">
        </el-table-column>
        <el-table-column label="操作" width="330">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.$index, scope.row)">禁用</el-button>
           
          </template>
        </el-table-column>
      </el-table>
      <el-pagination @size-change="sizeChangeHandle" @current-change="currentChangeHandle" :current-page="pageIndex"
        :page-sizes="[5, 10, 20, 50, 100]" :page-size="pageSize" :total="totalPage"
        layout="total, sizes, prev, pager, next, jumper" style="margin-top: 30px">
      </el-pagination>
    </div>

    <el-dialog :title="dataDialogForm.userId === 0 ? '新增用户' : '更新用户'" width="35%" :visible.sync="dialogFormVisible"
      @close="closeDialog()">
      <el-form :model="dataDialogForm" :rules="rules" ref="ruleForm">
        <el-form-item label="用户名称" label-width="120px" prop="userName">
          <el-input v-model="dataDialogForm.userName" placeholder="用户名称" style="width: 300px"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" label-width="120px" prop="email">
          <el-input v-model="dataDialogForm.email" placeholder="邮箱" style="width: 300px"></el-input>
        </el-form-item>
        <el-form-item label="电话" label-width="120px" prop="mobile">
          <el-input v-model="dataDialogForm.mobile" placeholder="电话" style="width: 300px"></el-input>
        </el-form-item>
        <el-form-item label="初始密码" label-width="120px" prop="rawPassword">
          <el-input v-model="dataDialogForm.rawPassword" placeholder="初始密码" style="width: 300px"></el-input>
        </el-form-item>
        <el-form-item label="分配角色" label-width="120px" prop="roleIds">
          <el-checkbox-group v-model="roleIds">
            <el-checkbox v-for="role in roles" :key="role.roleId" :label="role.roleId">{{ role.roleName }}</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleSubmitFormData('ruleForm')">确 定</el-button>
      </div>
    </el-dialog>


   

  </el-card>
</template>

<script>
export default {
  name: "sysUser",
  data() {
    return {
      dataForm: {
        userName: "",
      }, rules: {
        rawPassword: [{ required: true, message: "请输入初始密码", trigger: "blur" }],
      }, dataDialogForm: {
        userId: 0,
        userName: "",
        rawPassword:"",
        email: "",
        mobile: "",
        roleIds: []
      },
      

      dataList: [],
      roles: [],
      roleIds: [],
      pageIndex: 1,
      pageSize: 5,
      totalPage: 0,
      dataListLoading: false,
      dialogFormVisible: false,
      dialogFormSubmitVisible: false,
      dialogFormVisible2: false,
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

    handleEdit(index, row) {
      this.$http
        .get("/sys/sysUser/queryUserById?userId=" + row.userId)
        .then((res) => {
          //需要更新的账号信息
          var user = res.data.data.user;
          //所有的角色信息
          this.roles = res.data.data.roles;
          this.roleIds = user.roleIds;
          //打开窗口
          this.dataDialogForm = {
            userId: user.userId,
            username: user.username,
            mobile: user.mobile,
            email: user.email

          }
          this.dialogFormVisible = true;
        })
    },



    openDialog() {
      //查询角色信息
      this.$http
        .get("/sys/sysRole/listAll")
        .then((res) => {
          this.roles = res.data.data;
          //打开窗口
          this.dialogFormVisible = true;
          // 绑定需要更新的数据
          // this.dataDialogForm.userId = userId;
          this.dataDialogForm.userName = userName;
          this.dataDialogForm.rawPassword = rawPassword;
          this.dataDialogForm.email = email;
          this.dataDialogForm.mobile = mobile;
        })
    },


    closeDialog() {
      this.dataDialogForm = {
        userId: 0,
        status: 1,
      }
      this.roleIds = []
    },


    handleSubmitFormData(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          if (this.dialogFormSubmitVisible) {
            return;
          }
          this.dialogFormSubmitVisible = true;
          this.dataDialogForm.roleIds = this.roleIds
          this.$http
            .post("/sys/sysUser/save", this.dataDialogForm)
            .then((res) => {
              this.dialogFormVisible = false; // 关闭窗口
              // 清空添加数据的表单
              this.dataDialogForm = {
                userId: 0,
              };
              this.dialogFormSubmitVisible = false;
              // 刷新数据
              this.getDataList();
            });
        } else {
          // console.log('error submit!!');
          return false;
        }
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
          username: this.dataForm.userName,
        },
      };
      this.$http.get("/sys/sysUser/list", params).then((res) => {
        this.dataList = res.data.data.list;
        this.totalPage = res.data.data.totalCount;
        this.dataListLoading = false;
      });
    },
  }, mounted() {
    this.getDataList();
  }
};
</script>
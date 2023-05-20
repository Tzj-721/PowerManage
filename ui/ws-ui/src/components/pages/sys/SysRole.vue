<template>
  <el-card class="box-card">
    <div class="mod-role">
      <el-form :inline="true" :model="dataForm" class="demo-form-inline">
        <el-form-item>
          <el-input v-model="dataForm.roleName" clearable placeholder="角色名称"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button @click="getDataList">查询</el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="openDialog()">新增</el-button>
        </el-form-item>
        <el-form-item>
          <el-button @click="importExcel()">导出</el-button>
        </el-form-item>
      </el-form>
      <el-table id="exportData"  :data="dataList" border style="width: 100%">
        <el-table-column type="selection" width="55"> </el-table-column>
        <el-table-column prop="roleId" label="ID" width="150">
        </el-table-column>
        <el-table-column prop="roleName" label="角色名称" width="180">
        </el-table-column>
        <el-table-column prop="remark" label="备注" width="280">
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180">
        </el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope" width="280">
            <el-button size="mini" type="primary" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.$index, scope.row)">删除</el-button>
            <el-button size="mini" type="primary" @click="handleSendMail(scope.$index, scope.row)">发送邮件</el-button>
            <el-button size="mini" type="primary" @click="copy(scope.$index,scope.row)">复制信息</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination @size-change="sizeChangeHandle" @current-change="currentChangeHandle" :current-page="pageIndex"
        :page-sizes="[5, 10, 20, 50, 100]" :page-size="pageSize" :total="totalPage"
        layout="total, sizes, prev, pager, next, jumper" style="margin-top: 30px">
      </el-pagination>
    </div>

    <el-dialog :title="dataDialogForm.roleId === 0 ? '新增角色' : '更新角色'" width="35%" :visible.sync="dialogFormVisible"
      @close="closeDialog()">
      <el-form :model="dataDialogForm" :rules="rules" ref="ruleForm">
        <el-form-item label="角色名称" label-width="120px" prop="roleName">
          <el-input v-model="dataDialogForm.roleName" placeholder="角色名称" style="width: 300px"></el-input>
        </el-form-item>
        <el-form-item label="描述信息" label-width="120px" prop="remark">
          <el-input type="textarea" v-model="dataDialogForm.remark" style="width: 300px"></el-input>
        </el-form-item>
        <el-form-item label="分配菜单" label-width="120px" prop="menuIds">
          <el-tree :data="roleMenus.treeData" show-checkbox node-key="id" @check="handleChecked"
            :default-checked-keys="roleMenus.checks">
          </el-tree>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleSubmitFormData('ruleForm')">确 定</el-button>
      </div>
    </el-dialog>

    <el-dialog :title="'邮件'" width="35%" :visible.sync="dialogFormVisible2" @close="closeDialog()">
      <el-form :model="dataDialogForm2" :rules="rules2" ref="ruleForm2">
        <el-form-item label="收信角色" label-width="120px" prop="to_role_name">
          <el-input type="textarea" v-model="dataDialogForm2.to_role_name" placeholder="发送对象" style="width: 150px"></el-input>
        </el-form-item>
        <el-form-item label="主题" label-width="120px" prop="subject">
          <el-input type="textarea" v-model="dataDialogForm2.subject" placeholder="主题" style="width: 150px"></el-input>
        </el-form-item>
        <el-form-item label="内容" label-width="120px" prop="content">
          <el-input type="textarea" v-model="dataDialogForm2.content" placeholder="内容" style="width: 300px"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible2 = false">取 消</el-button>
        <el-button type="primary" @click="handleMailData('ruleForm2')">确 定</el-button>
      </div>
    </el-dialog>

  </el-card>
</template>
<script>
import FileSaver from 'file-saver'
import * as XLSX from 'xlsx/xlsx.mjs'
export default {
  name: "sysRole",
  data() {
    var checkRoleName = (rule, value, callback) => {
      if (this.dataDialogForm.roleId !== 0) {
        if (value === "") {
          callback(new Error("请输入角色名称"));
        }
        // 说明是更新操作
        callback();
      } else if (value === "") {
        callback(new Error("请输入角色名称"));
      } else {
        // 调用后端接口 检查 角色名称是否存在
        this.$http
          .get("/sys/sysRole/checkRoleName?roleName=" + value)
          .then((res) => {
            //console.log(res)
            if (res.data.data === "fail") {
              // 说明角色名不存在，可以使用
              callback();
            } else {
              callback(new Error("角色名称重复"));
            }
          });
        //callback();
      }
    };
    return {
      dataForm: {
        roleName: "",
      },
      roleMenus: [],
      dataDialogForm: {
        roleId: 0,
        roleName: "",
        remark: "",
        menuIds: []
      },
      dataDialogForm2: {
        to_role_name: "",
        subject: "",
        content: "",
      },


      menuIds: [],
      dataList: [],
      pageIndex: 1,
      pageSize: 5,
      totalPage: 0,
      dataListLoading: false,
      dialogFormVisible: false,
      dialogFormVisible2: false,
      dialogFormSubmitVisible: false,
      rules: {
        roleName: [{ validator: checkRoleName, trigger: "blur" }],
        remark: [
          { required: true, message: "请输入描述信息", trigger: "blur" },
        ],
      },
      rules2: {
        subject: [{ required: true, message: "请输入主题", trigger: "blur" }],
      },
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
          roleName: this.dataForm.roleName,
        },
      };
      this.$http.get("/sys/sysRole/list", params).then((res) => {
        this.dataList = res.data.data.list;
        this.totalPage = res.data.data.totalCount;
        this.dataListLoading = false;
        //console.log(res);
      });

    },

    //导出excel
    importExcel(){
      this.$http('/sys/sysRole/excel',{responseType: 'blob' }).then(res =>{
        console.log("ok");
      })
      var xlsxParam = { raw: true } // 导出的内容只做解析，不进行格式转换
      var wb = XLSX.utils.table_to_book(document.querySelector('#exportData'), xlsxParam)
      var wbout = XLSX.write(wb, { bookType: 'xlsx', bookSST: true, type: 'array' })
      try {
        FileSaver.saveAs(new Blob([wbout], { type: 'application/octet-stream' }), '角色信息表.xlsx')
      } catch (e) {
        if (typeof console !== 'undefined') {
          console.log(e, wbout)
        }
      }
      return wbout

    },

    //处理更新操作
    handleEdit(index, item) {
      this.$http
        .get("/sys/sysRole/dispatherRoleMenu?roleId=" + item.roleId)
        .then((res) => {
          this.roleMenus = res.data.data;
          // 打开更新的窗口
          this.dialogFormVisible = true;
          // 绑定需要更新的数据
          this.dataDialogForm.roleId = item.roleId;
          this.dataDialogForm.roleName = item.roleName;
          this.dataDialogForm.remark = item.remark;
          //查询角色已绑定菜单
          //和当前所有菜单信息

        });
    },
    handleSendMail(index, item) {
      this.dataDialogForm2.to_role_name = item.roleName;
      this.dialogFormVisible2 = true;
    },
    copy(index,item){
      this.$copyText("ID:"+item.roleId+";角色名称:"+item.roleName+";创建时间:"+item.createTime+";").then(
        function(e) {
          console.log("copy arguments e:", e);
          alert("复制成功!");
        },
        function(e) {
          console.log("copy arguments e:", e);
          alert("复制失败!");
        }
      );
    },
    handleMailData(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.$http
            .post("/sys/sysMail/save", this.dataDialogForm2)
            .then((res) => {
              this.dialogFormVisible2 = false; // 关闭窗口
              // 清空添加数据的表单
              this.dataDialogForm2 = {
                subject: "",
                content: "",
              };
            });
          this.dialogFormVisible2 = false;
          this.$message({
            type: "success",
            message: "发送成功!",
          });
        } else {
          // console.log('error submit!!');
          this.dataDialogForm2 = {
            subject: "",
            content: "",
          };
          return false;
        }
      });

    },
    handleDelete(index, item) {
      // 删除角色信息
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
            .get("/sys/sysRole/deleteRole?roleId=" + item.roleId)
            .then((res) => {
              console.log(res)
              if (res.data.data === '0') {
                // 表示数据不能被删除
                this.$message({
                  type: "warning",
                  message: "该条记录不允许删除!",
                });
              } else {
                this.$message({
                  type: "success",
                  message: "删除成功!",
                });
              }
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

    //新增角色信息
    openDialog() {
      this.$http
        .get("/sys/sysRole/dispatherRoleMenu")
        .then((res) => {
          this.roleMenus = res.data.data;
          // 打开更新的窗口
          this.dialogFormVisible = true;
          // 绑定需要更新的数据
          this.dataDialogForm.roleId = item.roleId;
          this.dataDialogForm.roleName = item.roleName;
          this.dataDialogForm.remark = item.remark;
          //查询角色已绑定菜单
          //和当前所有菜单信息

        });
    },

    closeDialog() {
      // 清空添加数据的表单
      this.dataDialogForm = {
        roleId: 0,
        roleName: "",
        remark: "",
      };
      this.dataDialogForm2 = {
        subject: "",
        content: "",
      };
    },

    handleChecked(currentNode, selectedNodes) {
      this.dataDialogForm.menuIds = selectedNodes.checkedKeys;
    },

    handleSubmitFormData(formName) {
      this.updateRole(formName);
    },
    updateRole(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          if (this.dialogFormSubmitVisible) {
            return;
          }
          this.dialogFormSubmitVisible = true;

          this.$http
            .post("/sys/sysRole/save", this.dataDialogForm)
            .then((res) => {
              this.dialogFormVisible = false; // 关闭窗口
              // 清空添加数据的表单
              this.dataDialogForm = {
                roleId: 0,
                roleName: "",
                remark: "",
                menuIds: []
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
  },
  mounted() {
    this.getDataList();
  },
};
</script>

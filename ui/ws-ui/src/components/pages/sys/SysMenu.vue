<template>
    <el-card class="box-card">
        <div class="mod-menu">
            <el-form :inline="true" :model="dataForm" class="demo-form-inline">
                <el-form-item>
                    <el-input v-model="dataForm.label" clearable placeholder="菜单名称"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button @click="getDataList">查询</el-button>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="openDialog()">新增</el-button>
                </el-form-item>
            </el-form>
            <el-table :data="dataList" border style="width: 100%" row-key="menuId"
                :tree-props="{ children: 'children', hasChildren: 'hasChildren' }">
                <el-table-column type="selection" width="55"> </el-table-column>
                <el-table-column prop="menuId" label="ID">
                </el-table-column>
                <el-table-column prop="label" label="菜单名称">
                </el-table-column>
                <el-table-column prop="path" label="路由地址">
                </el-table-column>
                <el-table-column prop="icon" label="图标">
                </el-table-column>
                <el-table-column prop="parentId" label="父菜单编号">
                </el-table-column>
                <el-table-column prop="orderNum" label="序号">
                </el-table-column>
                <el-table-column label="操作" width="180">
                    <template slot-scope="scope">
                        <el-button size="mini" type="primary" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
                        <el-button size="mini" type="danger" @click="handleDelete(scope.$index, scope.row)">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>
            <el-pagination @size-change="sizeChangeHandle" @current-change="currentChangeHandle" :current-page="pageIndex"
                :page-sizes="[5, 10, 20, 50, 100]" :page-size="pageSize" :total="totalPage"
                layout="total, sizes, prev, pager, next, jumper" style="margin-top: 30px">
            </el-pagination>
        </div>
        <el-dialog :title="dataDialogForm.menuId === 0 ? '新增菜单' : '更新菜单'" width="35%" :visible.sync="dialogFormVisible"
            @close="closeDialog()">
            <el-form :model="dataDialogForm" :rules="rules" ref="ruleForm">
                <el-form-item label="菜单名" label-width="120px" prop="label">
                    <el-input v-model="dataDialogForm.label" placeholder="菜单名" style="width: 300px"></el-input>
                </el-form-item>
                <el-form-item label="路由地址" label-width="120px" prop="path">
                    <el-input v-model="dataDialogForm.path" placeholder="路由地址" style="width: 300px"></el-input>
                </el-form-item>
                <el-form-item label="图标" label-width="120px" prop="icon">
                    <el-input v-model="dataDialogForm.icon" placeholder="图标名称" style="width: 300px"></el-input>
                </el-form-item>
                <el-form-item label="序号" label-width="120px" prop="orderNum">
                    <el-input v-model="dataDialogForm.orderNum" placeholder="序号" type="number"
                        style="width: 300px"></el-input>
                </el-form-item>
                <el-form-item label="父菜单" label-width="120px" prop="parentId">
                    <el-select v-model="dataDialogForm.parentId" placeholder="请选择-不选择即为父菜单" clearable>
                        <el-option v-for="item in options" :key="item.menuId" :label="item.label" :value="item.menuId">
                        </el-option>
                    </el-select>
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
    name: "sysMenu",
    data() {
        return {
            dataForm: {
                label: "",
            }, rules: {
                label: [{ required: true, message: "请输入菜单名称", trigger: "blur" }],
                orderNum: [{ required: true, message: "请输入排序数字", trigger: "blur" }],
            },
            dataList: [],
            pageIndex: 1,
            pageSize: 5,
            totalPage: 0,
            dataListLoading: false,
            dialogFormVisible: false,
            dialogFormSubmitVisible: false,

            dataDialogForm: {
                menuId: 0,
            }, options: [],
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
                .get("/sys/sysMenu/queryMenuById?menuId=" + row.menuId)
                .then(
                    (res) => {
                        //console.log(res);
                        var menu = res.data.data.sysMenu;
                        this.options = res.data.data.parents;
                        this.dataDialogForm = {
                            menuId: menu.menuId,
                            label: menu.label,
                            path: menu.path,
                            icon: menu.icon,
                            orderNum: menu.orderNum,
                            parentId: menu.parentId
                        };
                        //打开窗口
                        this.dialogFormVisible = true;
                    }
                );
        },
        handleDelete(index, item) {
            // 删除菜单信息
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
                        .get("/sys/sysMenu/deleteMenu?menuId=" + item.menuId)
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
        closeDialog() {
            this.dataDialogForm = {
                menuId: 0
            }
        },
        openDialog() {
            //获取所有父菜单数据
            this.$http.get("/sys/sysMenu/listParent").then((res) => {
                this.options = res.data.data;
                //打开窗口
                this.dialogFormVisible = true;
            });
        },
        handleSubmitFormData(formName) {
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    if (this.dialogFormSubmitVisible) {
                        return;
                    }
                    this.dialogFormSubmitVisible = true;

                    this.$http
                        .post("/sys/sysMenu/save", this.dataDialogForm)
                        .then((res) => {
                            this.dialogFormVisible = false; // 关闭窗口
                            // 清空添加数据的表单
                            this.dataDialogForm = {
                                menuId: 0,
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
                    label: this.dataForm.label,
                },
            };
            this.$http.get("/sys/sysMenu/list", params).then((res) => {
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
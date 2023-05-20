<template>
    <div>
        <el-card class="box-card">
            <div class="mod-role">
                <el-form ref="goodForm" :model="form" :rules="rules" style="margin-top: 6px;" size="small"
                    label-width="90px">
                    <el-form-item label="商品名称" prop="subject">
                        <el-input v-model="form.subject" style="width: 35%" />
                    </el-form-item>
                    <el-form-item label="交易编号" prop="traceNo">
                        <el-input v-model="form.traceNo" style="width: 35%" />
                    </el-form-item>
                    <el-form-item label="商品价格" prop="totalAmount">
                        <el-input v-model="form.totalAmount" style="width: 35%" />
                        <span style="color: #C0C0C0;margin-left: 10px;">测试允许区间(0,5000]</span>
                    </el-form-item>
                    <el-form-item label="商品描述" prop="body">
                        <el-input v-model="form.body" style="width: 35%" rows="8" type="textarea" />
                    </el-form-item>
                    <el-form-item label="">
                        <el-button :loading="loading" size="medium" type="primary"
                            @click="doSubmit('goodForm')">去支付</el-button>
                    </el-form-item>
                </el-form>
            </div>
            

        </el-card>



    </div>
</template>

<script>
//import { toAliPay } from '@/api/tools/alipay'
export default {
    name: "toPay",
    data() {
        return {
            url: '',
            // 新窗口的引用
            newWin: null,
            loading: false, form: { subject: '', totalAmount: '', body: '' , traceNo: ''},
            rules: {
                subject: [
                    { required: true, message: '商品名称不能为空', trigger: 'blur' }
                ],
                totalAmount: [
                    { required: true, message: '商品价格不能为空', trigger: 'blur' }
                ],
                body: [
                    { required: true, message: '商品描述不能为空', trigger: 'blur' }
                ],
                traceNo: [
                    { required: true, message: '商品描述不能为空', trigger: 'blur' }
                ]
            }
        }
    },

    // watch: {
    // url(newVal, oldVal) {
    //   if (newVal && this.newWin) {
    //     this.newWin.sessionStorage.clear()
    //     this.newWin.location.href = newVal
    //     // 重定向后把url和newWin重置
    //     this.url = ''
    //     this.newWin = null
    //   }
    // }
    // },

    methods: {
        doSubmit(formName) {
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    // 先打开一个空的新窗口，再请求

                    let url = ''
                    url = "/sys/toPay/pay?subject=" + this.form.subject + "&traceNo=" + this.form.traceNo + "&totalAmount=" + this.form.totalAmount
                    console.log(url)
                    this.$http.get(url)
                        .catch(err => {
                            this.loading = false
                            console.log(err.response.data.message)
                        })
                        .then(res => {
                            console.log(res.data.data)
                            document.querySelector("body").innerHTML = res.data.data;
                            document.forms[0].submit();
                        })
                } else {
                    return false
                }
            })
        }
    }
}
</script>
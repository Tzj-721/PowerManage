
module.exports = {
  devServer: {
    // port: 1664,   //自定义本地服务端口号
    proxy: {
      '/api': {                        //api表示拦截一/api开头的请求路径
        target: 'https://www.vue-js.com/api', //跨域的域名（不需要写路径）
        // ws: false,   //是否代理webSocked
        changeOrigin: true,    //是否开启跨域
        pathRewrite: {          //重写路径
          '^/api': ''    //把/api变为空字符串
        }
      },
    }
  },
}

import Vue from 'vue'
import Router from 'vue-router'
import Home from '@/components/common/Home.vue'

import AllOrder from '@/components/pages/order/AllOrder.vue'
import MyOrder from '@/components/pages/order/MyOrder.vue'
import FirstPage from '@/components/pages/FirstPage.vue'
import Login from '@/components/common/Login.vue'
import SysUser from '@/components/pages/sys/SysUser.vue'
import SysRole from '@/components/pages/sys/SysRole.vue'
import SysMenu from '@/components/pages/sys/SysMenu.vue'
import SysLog from '@/components/pages/sys/SysLog.vue'
import Icon from '@/components/pages/Easy/Icon.vue'
import Register from '@/components/common/Register.vue'
import AlipayTest from "../components/pages/sys/AlipayTest.vue";
import Yaml from "../components/pages/Easy/Yaml.vue";
import ReceiveBin from "../components/pages/sys/ReceiveBin.vue";


Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'Home',// 主路由
      component: Home,
      redirect:'FirstPage',
      children: [ // 嵌套的子路由
      {
        path: '/FirstPage',
        name: 'firstPage',
        component: FirstPage
      },
         {
          path: '/AllOrder',
          name: 'allOrder',
          component: AllOrder
        }, {
          path: '/MyOrder',
          name: 'myOrder',
          component: MyOrder
        }, {
          path: '/SysUser',
          name: 'sysUser',
          component: SysUser
        }, {
          path: '/SysRole',
          name: 'sysRole',
          component: SysRole
        }, {
          path: '/SysMenu',
          name: 'sysMenu',
          component: SysMenu
        }, {
          path: '/SysLog',
          name: 'sysLog',
          component: SysLog
        },
        {
          path: '/ReceiveBin',
          name: 'receiveBin',
          component: ReceiveBin
        },
        {
          path: '/Icon',
          name: 'icon',
          component: Icon
        }, {
          path: '/YamlEdit',
          name: 'yaml',
          component: Yaml
        },{
        path: '/AlipayTest',
        name: 'alipayTest',
        component: AlipayTest
        }
      ]
    }, {
      path: '/Login',
      name: 'login',// 主路由
      component: Login
    },{
       path:'/Register',
       name:'register',
       component:Register,
     }
  ]
})

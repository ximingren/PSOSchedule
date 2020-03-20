import Vue from 'vue'
import App from '@/App'
import store from '@/store/index'
import router from '@/router/index'

import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import './styles/index.scss'

import axios from './config/httpConfig'
import * as globalFilter from './filters/filters'
import '@/icons'

Vue.prototype.$http = axios

for (var key in globalFilter) {
    Vue.filter(key, globalFilter[key])
}

Vue.use(ElementUI)
// 阻止生产消息
Vue.config.productionTip = false

// 全局前置守卫
router.beforeEach((to, from, next) => {
    // 检测用户是否有token
    if (!store.state.UserToken) {
        // 用户未登录,此时动态路由未添加
        if (to.matched.length > 0 && !to.matched.some(record => record.meta.requiresAuth)) {
            next()
        } else {
            next({ path: '/login' })
        }
    } else {
        // 用户登录了，如果检测到当时无权限列表，则为登录之后的首次访问
        if (!store.state.permission.permissionList) {
            // 设置动态路由、权限、菜单
            store.dispatch('permission/FETCH_PERMISSION').then(() => {
                next({ path: to.path })
            })
        } else {
            if (to.path !== '/login') {
                next()
            } else {
                next(from.fullPath)
            }
        }
    }
})

router.afterEach((to, from, next) => {
    var routerList = to.matched
    // 面包屑保留列表
    store.commit('setCrumbList', routerList)
    // 设置当前目录
    store.commit('permission/SET_CURRENT_MENU', to.name)
})

/* eslint-disable no-new */
new Vue({
    // 挂载在id为app的标签上
    el: '#app',
    router,
    store,
    render: h => h(App)
})

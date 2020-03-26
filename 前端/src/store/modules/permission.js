import { fetchPermission } from '@/api/permission'
import router, { DynamicRoutes } from '@/router/index'
import { recursionRouter } from '@/utils/recursion-router'
import dynamicRouter from '@/router/dynamic-router'

export default {
    namespaced: true,
    state: {
        permissionList: null /** 所有路由 */,
        sidebarMenu: [] /** 导航菜单 */,
        currentMenu: '' /** 当前active导航菜单 */,
        control_list: [] /** 完整的权限列表 */,
        account: ''/** 用户角色 */,
        username: ''
    },
    getters: {},
    mutations: {
        SET_ACCOUNT(state, account) {
            state.account = account
        },
        SET_USERNAME(state, username) {
            state.username = username
        },
        SET_PERMISSION(state, routes) {
            state.permissionList = routes
        },
        CLEAR_PERMISSION(state) {
            state.permissionList = null
        },
        SET_MENU(state, menu) {
            state.sidebarMenu = menu
        },
        CLEAR_MENU(state) {
            state.sidebarMenu = []
        },
        SET_CURRENT_MENU(state, currentMenu) {
            state.currentMenu = currentMenu
        },
        SET_CONTROL_LIST(state, list) {
            state.control_list = list
        }
    },
    actions: {
        // 有了token之后
        async FETCH_PERMISSION({ commit, state }) {
            // 获取权限列表
            let permissionList = await fetchPermission()
            // 设置用户名
            commit('SET_ACCOUNT', permissionList.name)
            commit('SET_USERNAME', permissionList.userName)
            // permissionList.data里面的是用户拥有的权限,过滤出用户权限对应的路由列表
            let routes = recursionRouter(permissionList.data, dynamicRouter)
            let MainContainer = DynamicRoutes.find(v => v.path === '')
            let children = MainContainer.children
            commit('SET_CONTROL_LIST', [...children, ...dynamicRouter])
            // 在首页的路由上加上children
            children.push(...routes)
            // 设置菜单
            commit('SET_MENU', children)
            let initialRoutes = router.options.routes
            // 在初始路由加上动态路由
            router.addRoutes(DynamicRoutes)
            commit('SET_PERMISSION', [...initialRoutes, ...DynamicRoutes])
        }
    }
}

const common = () => import('@/pages/common')

// 权限管理
const Permission = () => import('@/pages/permission')
const UserManage = () => import('@/pages/permission/user-manage')
const RoleManage = () => import('@/pages/permission/role-manage')
const MenuManage = () => import('@/pages/permission/menu-manage')
// 课表管理
const cousePlanIndividual = () => import('@/pages/course/coursePlan-individual')
const classTask = () => import('@/pages/course/class-task')
// 教务管理
const collegeManage = () => import('@/pages/educationManage/collegeManage')
const teacherManage = () => import('@/pages/educationManage/teacherManage')
const teachBuildManage = () => import('@/pages/educationManage/teachBuildManage')
const studentManage = () => import('@/pages/educationManage/studentManage')
/* 需要权限判断的路由 */
const dynamicRoutes = [
    {
        path: '/course',
        component: common,
        name: 'course-manage',
        meta: {
            name: '课表管理',
            icon: 'example'
        },
        children: [
            {
                path: 'individual',
                name: 'course-individual',
                component: cousePlanIndividual,
                meta: {
                    name: '个人课表',
                    icon: 'user'
                }
            },
            {
                path: 'task',
                name: 'class-task',
                component: classTask,
                meta: {
                    name: '开课计划',
                    icon: 'table'
                }
            }
        ]
    },
    {
        path: '/education',
        component: common,
        name: 'education-manage',
        meta: {
            name: '教务管理',
            icon: 'user'
        },
        children: [
            {
                path: 'college',
                name: 'college-manage',
                component: collegeManage,
                meta: {
                    name: '学院管理',
                    icon: 'table'
                }
            },
            {
                path: 'teacher',
                name: 'teacher-manage',
                component: teacherManage,
                meta: {
                    name: '教师管理',
                    icon: 'table'
                }
            },
            {
                path: 'teachBuild',
                name: 'teachBuild-manage',
                component: teachBuildManage,
                meta: {
                    name: '教学楼管理',
                    icon: 'table'
                }
            },
            {
                path: 'students',
                name: 'students-manage',
                component: studentManage,
                meta: {
                    name: '学生管理',
                    icon: 'table'
                }
            }
        ]
    }
    // {
    //     path: '/permission',
    //     component: Permission,
    //     name: 'permission',
    //     meta: {
    //         name: '权限管理',
    //         icon: 'table'
    //     },
    //     children: [
    //         {
    //             path: 'user',
    //             name: 'user-manage',
    //             component: UserManage,
    //             meta: {
    //                 name: '用户管理',
    //                 icon: 'table'
    //             }
    //         },
    //         {
    //             path: 'role',
    //             name: 'role-manage',
    //             component: RoleManage,
    //             meta: {
    //                 name: '角色管理',
    //                 icon: 'eye'
    //             }
    //         },
    //         {
    //             path: 'menu',
    //             name: 'menu-manage',
    //             component: MenuManage,
    //             meta: {
    //                 name: '菜单管理',
    //                 icon: 'tree'
    //             }
    //         }
    //     ]
    // }
]

export default dynamicRoutes

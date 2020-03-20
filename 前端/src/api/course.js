import axios from '@/config/httpConfig'

export function fetchPermission() {
    return axios.get('/user/info')
}

// 获取个人课表
export function getCoursePlan(data) {
    return axios.post('/queryCoursePlan', data)
}
// 获得课程名称
export function getCourseInfo() {
    return axios.post('/queryCourseInfo')
}
// 获取开课计划
export function getClassTask(data) {
    return axios.post('/queryClassTask', data)
}
// 保存开课计划
export function saveClassTask(data) {
    return axios.post('/saveClassTask', data)
}
// 删除开课计划
export function deleteClassTask(data) {
    return axios.post('/deleteClassTask', data)
}

import axios from '@/config/httpConfig'

// 获取教师信息
export function getTeacherInfo(data) {
    return axios.post('/queryTeacherInfo', data)
}
// 保存教师
export function saveTeacher(data) {
    return axios.post('/saveTeacherInfo', data)
}
// 保存教师
export function deleteTeacher(data) {
    return axios.post('/deleteTeacherInfo', data)
}
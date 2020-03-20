import axios from '@/config/httpConfig'

// 获取学生信息
export function getStudentInfo(data) {
    return axios.post('/queryStudentInfo', data)
}
// 保存学生
export function saveStudent(data) {
    return axios.post('/saveStudentInfo', data)
}
// 删除学生
export function deleteStudent(data) {
    return axios.post('/deleteStudentInfo', data)
}

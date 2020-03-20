import axios from '@/config/httpConfig'

export function fetchPermission() {
    return axios.get('/user/info')
}

// 获取班级信息
export function getClassInfo() {
    return axios.post('/queryClassInfo')
}

import axios from '@/config/httpConfig'

export function fetchPermission() {
    return axios.get('/user/info')
}

// 获取学院信息
export function getCollegeInfo() {
    return axios.post('/queryCollegeInfo')
}

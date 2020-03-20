import axios from '@/config/httpConfig'

// 获取教学楼信息
export function getTeachBuildInfo(data) {
    return axios.post('/queryTeachBuild', data)
}

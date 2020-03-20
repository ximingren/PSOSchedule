import axios from '@/config/httpConfig'

// 排课
export function courseSchedule(data) {
    return axios.post('/schedule', data)
}

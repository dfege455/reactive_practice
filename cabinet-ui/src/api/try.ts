import request from "../utils/request";

export function getAllUser() {
    return request({
        url: '/api/getAllHello',
        method: 'get'
    })
}
import request from '../utils/request'
import {Login} from '../types/login'

// 登录方法
export function login(name: string, password: string, code?: string, uuid?: string) {
    const data = {
        name,
        password,
        // code,
        // uuid
    }
    return request({
        url: '/api/login',
        headers: {
            isToken: false
        },
        method: 'post',
        data: data
    })
}

// 注册方法
export function register(data: Login) {
    return request({
        url: '/api/register',
        headers: {
            isToken: false
        },
        method: 'post',
        data: data
    })
}

// 获取用户详细信息
export function getInfo() {
    return request({
        url: '/getInfo',
        method: 'get'
    })
}

// 退出方法
export function logout() {
    return request({
        url: '/api/logout',
        method: 'post'
    })
}

// 获取验证码
export function getCodeImg() {
    return request({
        url: '/captchaImage',
        headers: {
            isToken: false
        },
        method: 'get',
        timeout: 20000
    })
}
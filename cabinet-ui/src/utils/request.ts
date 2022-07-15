import axios from "axios";
import {getToken, removeToken} from "./auth";
import errorCode from "./errorCode";
import {tansParams} from "./transfer";
import cache from "./cache";

export let isRelogin = { show: false }

axios.defaults.headers.post['Content-Type'] = 'application/json;charset=utf-8'

const service = axios.create({
    baseURL: import.meta.env.VITE_APP_BASE_API,
    timeout: 10000
})

service.interceptors.request.use(config => {
    const isToken = (config.headers || {}).isToken === false
    const isRepeatSubmit = (config.headers || {}).repeatSubmit === false
    if(getToken() && !isToken){
        config.headers!.Authorization = 'Bearer ' + getToken()
    }
    if(config.method === 'get' && config.params){
        let url = config.url + '?' + tansParams(config.params)
        url = url.slice(0, -1)
        config.params = {};
        config.url = url;
    }
    if (!isRepeatSubmit && (config.method === 'post' || config.method === 'put')) {
        const requestObj = {
            url: config.url,
            data: typeof config.data === 'object' ? JSON.stringify(config.data) : config.data,
            time: new Date().getTime()
        }
        const sessionObj = cache.session.getJSON('sessionObj')
        if (sessionObj === undefined || sessionObj === null || sessionObj === '') {
            cache.session.setJSON('sessionObj', requestObj)
        } else {
            const s_url = sessionObj.url;                // 请求地址
            const s_data = sessionObj.data;              // 请求数据
            const s_time = sessionObj.time;              // 请求时间
            const interval = 1000;                       // 间隔时间(ms)，小于此时间视为重复提交
            if (s_data === requestObj.data && requestObj.time - s_time < interval && s_url === requestObj.url) {
                const message = '数据正在处理，请勿重复提交';
                console.warn(`[${s_url}]: ` + message)
                return Promise.reject(new Error(message))
            } else {
                cache.session.setJSON('sessionObj', requestObj)
            }
        }
    }
    return config
}, error => {
    console.log(error)
    return Promise.reject(error)
})

service.interceptors.response.use(res => {
    const code = res.data.code || 200
    // @ts-ignore
    const msg = errorCode[code] || res.data.msg || errorCode['default']
    if(code === 401){
        if(!isRelogin.show){
            isRelogin.show = true
            /*
            ElMessageBox.confirm('登录状态已过期，您可以继续留在该页面，或者重新登录', '系统提示', {
              confirmButtonText: '重新登录',
              cancelButtonText: '取消',
              type: 'warning'
            }).then(() => {
              isRelogin.show = false;
              useUserStore().logOut().then(() => {
                location.href = '/index';
              })
            }).catch(() => {
              isRelogin.show = false;
            });
            */
        }
        return Promise.reject('无效的会话，或者会话已过期，请重新登录。')
    }else if (code === 403) {

        removeToken()
        /*ElMessage({
            message: msg,
            type: 'error'
        })*/
        return Promise.reject(new Error('无效的会话，或者会话已过期，请重新登录。'))
    } else if (code !== 200) {
        /*ElNotification.error({
            title: msg
        })*/
        return Promise.reject('error')
    } else {
        return  Promise.resolve(res.data)
    }
}, error => {
    console.log('err' + error)
    let { message } = error;
    if (message == "Network Error") {
        message = "后端接口连接异常";
    }
    else if (message.includes("timeout")) {
        message = "系统接口请求超时";
    }
    else if (message.includes("Request failed with status code")) {
        message = "系统接口" + message.substr(message.length - 3) + "异常";
    }
    /*ElMessage({
        message: message,
        type: 'error',
        duration: 5 * 1000
    })*/
    return Promise.reject(error)
})
export default service
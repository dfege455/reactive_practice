import router from "./router";
import {getToken} from "./utils/auth";

const whiteList = ['/login', '/register'];
//'/auth-redirect', '/bind', '/register'

router.beforeEach((to, from, next) => {
    if(getToken()){
        if(to.path === '/login') {
            next({ path: '/' })
        }else {
            next()
        }
    } else {
        if(whiteList.indexOf(to.path) !== -1){
            next()
        }else {
            next(`/login?redirect=${to.fullPath}`)
        }
    }
})
import {defineStore} from "pinia";
import {getToken, removeToken, setToken} from "../../utils/auth";
import {login, logout, register} from "../../api/login";
import {Login} from "../../types/login";
import {Register} from "../../types/register";

const useUserStore = defineStore(
    'user',
    {
        state: () => ({
            token: getToken(),
            name: '',
            roles: [],
            permissions: []
        }),
        actions: {
            login(userInfo: Login) {
                const username = userInfo.name.trim()
                const password = userInfo.password
                return new Promise((resolve, reject) => {
                    login(username, password).then((res: any) => {
                        setToken(res.token)
                        this.token = res.token
                        resolve("登录成功")
                    }).catch(error => {
                        reject(error)
                    })
                })
            },
            logOut() {
                return new Promise((resolve, reject) => {
                    logout().then(() => {
                        this.token = ''
                        removeToken()
                        resolve("注销成功")
                    }).catch(error => {
                        reject(error)
                    })
                })
            },
            register(userInfo: Register) {
                const data = {
                    name: userInfo.name.trim(),
                    password: userInfo.password,
                    invitationCode: userInfo.invitationCode
                }
                return new Promise((resolve, reject) => {
                    register(data).then(() => {
                        resolve("注册成功")
                    }).catch(error => {
                        reject(error)
                    })
                })
            }
        }
    }
)

export default useUserStore
<template>
  <v-app>
    <div class="login">
      <div class="login-form">
        <v-form v-model="valid">
          <h3 class="title">智能储物柜管理系统</h3>
          <v-text-field color="info" variant="underlined" v-model="form.name"
                        label="账号" prepend-inner-icon="mdi-account"
                        :rules="usernameRules"/>
          <v-text-field color="info" v-model="form.password" variant="underlined" :type="'password'"
                        label="密码" prepend-inner-icon="mdi-lock-outline"
                        style="height: 70px"
                        :rules="passwordRules"/>
          <v-row style="height: 60px">
            <v-checkbox style="padding-left: 10px" density="compact" hide-details v-model="rememberMe" label="记住密码"/>
            <v-btn variant="text" color="blue" @click="register">注册账号</v-btn>
          </v-row>

          <v-btn @click.prevent="handleLogin" :disabled="!valid" color="info"
                 style="width:100%" :loading="loading">登录</v-btn>

        </v-form>
      </div>

      <div class="el-login-footer">
        <span>Copyright © 2018-2022 ruoyi.vip All Rights Reserved.</span>
      </div>
    </div>
    <v-snackbar v-model="snackbar">
      登录失败！
      <template #actions>
        <v-btn
            color="pink"
            variant="text"
            @click="snackbar = false">
          Close
        </v-btn>
      </template>
    </v-snackbar>
  </v-app>

</template>

<script lang="ts" setup>
import {onMounted, reactive, ref, toRefs} from "vue";
import useUserStore from "../store/modules/user";
import myRouter from "../router";
import Cookies from "js-cookie";
import { encrypt, decrypt } from "../utils/jsencrypt";
import {useRouter} from "vue-router";

const userStore = useUserStore()
const router = useRouter()

const redirect = ref(undefined)

const state = reactive({
  valid: false,
  form: {
    name: '',
    password: '',
  },
  loading: false,
  snackbar: false,

  rememberMe: false
})
const { valid, form, loading, snackbar, rememberMe } = toRefs(state)

const usernameRules = ref([
  (v: string) => !!v || '请输入用户名'
])
const passwordRules = ref([
  (v: string) => !!v || '请输入密码'
])

onMounted(() => mount())

function mount() {
  getCookie();
}

function register() {
  router.push({ path: '/register' })
}
function handleLogin() {
  loading.value = true
  if (rememberMe.value) {
    Cookies.set("username", form.value.name, { expires: 30 });
    //@ts-ignore
    Cookies.set("password", encrypt(form.value.password), { expires: 30 });
    Cookies.set("rememberMe", rememberMe.value + "", { expires: 30 });
  }else {
    Cookies.remove("username");
    Cookies.remove("password");
    Cookies.remove("rememberMe");
  }
  userStore.login(form.value).then(() => {
    myRouter.push({ path: redirect.value || "/" })
  }).catch(() => {
    loading.value = false
    snackbar.value = true
  })
}
function getCookie() {
  const username = Cookies.get("username");
  const password = Cookies.get("password");
  const rememberMe = Cookies.get("rememberMe");
  form.value = {
    username: username === undefined ? form.value.name : username,
    //@ts-ignore
    password: password === undefined ? form.value.password : decrypt(password),
  };
  state.rememberMe = rememberMe === undefined ? false : Boolean(rememberMe)
}
</script>

<style scoped lang="scss">
.title {
  margin: 0 auto 30px auto;
  text-align: center;
  color: #707070;
}
.login {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  background-image: url("../assets/images/e40bf5ee6864a7dea897839a392542f8.jpeg");
  background-size: cover;
}
.login-form {
  border-radius: 6px;
  background: #ffffff;
  width: 400px;
  padding: 25px 25px 25px 25px;
}
.el-login-footer {
  height: 40px;
  line-height: 40px;
  position: fixed;
  bottom: 0;
  width: 100%;
  text-align: center;
  color: #ffffff;
  font-family: Arial,monospace;
  font-size: 12px;
  letter-spacing: 1px;
}
</style>
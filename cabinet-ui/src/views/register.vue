<template>
  <v-app>
    <div class="login">
      <div class="login-form">
        <v-form v-model="valid">
          <h3 class="title">智能储物柜管理系统</h3>
          <v-text-field color="info" variant="underlined" v-model="form.name"
                        label="账号" prepend-inner-icon="mdi-account"
                        style="height: 70px"
                        :rules="usernameRules"/>
          <v-text-field color="info" variant="underlined" v-model="form.invitationCode"
                        label="邀请码" prepend-inner-icon="mdi-account-plus-outline"
                        style="height: 70px"
                        :rules="codeRules"/>
          <v-text-field color="info" v-model="form.password" variant="underlined" :type="'password'"
                        label="密码" prepend-inner-icon="mdi-lock-outline"
                        style="height: 70px"
                        :rules="passwordRules"/>
          <v-text-field color="info" v-model="passwordConfirm" variant="underlined" :type="'password'"
                        label="确认密码" prepend-inner-icon="mdi-lock-outline"
                        style="height: 70px"
                        :rules="confirmRules"/>
          <v-btn @click.prevent="handleRegister" :disabled="!valid" color="info"
                 style="width:100%" :loading="loading">注册</v-btn>
          <v-btn color="blue" variant="text" @click="reLogin">使用已有账号登录</v-btn>
        </v-form>
      </div>
      <div class="el-login-footer">
        <span>Copyright © 2018-2022 ruoyi.vip All Rights Reserved.</span>
      </div>
    </div>
    <v-snackbar v-model="snackbar">
      注册失败！
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
import {reactive, ref, toRefs} from "vue";
import useUserStore from "../store/modules/user";
import {useRouter} from "vue-router";

const userStore = useUserStore()
const router = useRouter()

const state = reactive({
  valid: false,
  form: {
    name: '',
    password: '',
    invitationCode: ''
  },
  passwordConfirm: '',
  loading: false,
  snackbar: false,
})
const { valid, form, loading, snackbar, passwordConfirm } = toRefs(state)

const usernameRules = ref([
  (v: string) => !!v || '请输入用户名'
])
const passwordRules = ref([
  (v: string) => !!v || '请输入密码'
])

const confirmRules = ref( [
  (v: string) => {
    if(v !== state.form.password)
      return '两次密码输入不一致！'
    return true
  }
])

const codeRules = ref([
  (v: string) => !!v || '请输入邀请码'
])

function handleRegister() {
  loading.value = true
  userStore.register(form.value).then(() => {
    alert("注册成功！")
    router.push({ path: "/login" })
  }).catch(() => {
    loading.value = false
    snackbar.value = true
  })
}

function reLogin() {
  router.push({ path: '/login' })
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
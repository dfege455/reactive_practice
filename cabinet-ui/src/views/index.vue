<template>
{{allUser}}
</template>

<script lang="ts" setup>
import { onMounted, reactive, toRefs } from "vue";
import { getAllUser } from "../api/try";
import {useRouter} from "vue-router";

const router = useRouter()

const state = reactive({
  allUser: []
})

const { allUser } = toRefs(state)
onMounted(() => {
  getAllUser().then((response: any) => {
    state.allUser = response.data
    console.log(response)
  }).catch(e => {
    alert(e)
    console.log("Hello")
    router.push({ path: '/login' })
    console.log("Hello")
  })

})
</script>

<style scoped>

</style>
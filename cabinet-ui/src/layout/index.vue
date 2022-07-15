<template>
  <v-app>
    <v-navigation-drawer elevation="6" app>
      <sidebar/>
    </v-navigation-drawer>
    <v-app-bar app>
      <v-toolbar-title>
        Logo
      </v-toolbar-title>
      <top-bar/>
    </v-app-bar>

    <v-main>
      <v-container fluid>
        <app-main/>
      </v-container>
    </v-main>
  </v-app>


</template>

<script setup lang="ts">
import Sidebar from './components/Sidebar/index.vue'
import AppMain from './components/AppMain.vue'
import TopBar from "./components/TopBar.vue";
</script>

<style scoped>

</style>
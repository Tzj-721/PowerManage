<!--这里是markdown-->
<template>
  <div>
    <el-button icon="el-icon-s-order" size="medium" @click="saveToLocal"></el-button>
    <mavon-editor v-model="content" :default-open="true" :subfield="false" :toolbarsFlag="true" :scrollStyle="true"
                  :editable="true" :lineNumbers="true" :previewClass="''" :markdownClass="''" :hljs="hljs" :marked="marked"
                  :theme="theme" />

  </div>
</template>

<script>
import hljs from 'highlight.js'
import marked from 'marked'
import 'highlight.js/styles/github.css'
import { saveAs } from 'file-saver'

export default {
  data() {
    return {
      content: '',
      hljs,
      marked,
      theme: 'default' // 可选 'dark'、'light'、'default'
    }
  }, methods: {
    saveToLocal() {
      let blob = new Blob([this.content], { type: "text/markdown;charset=utf-8" });
      saveAs(blob, "markdown.md");
    },
  }
}
</script>

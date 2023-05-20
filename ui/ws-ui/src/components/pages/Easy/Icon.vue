<template>
  <div class="icons-container">
    <el-tabs type="border-card">
      <el-tab-pane label="Element-UI Icons">
        <div class="grid">
          <div v-for="item of elementIcons" :key="item" @click="copyToClipboard(item)">
              <div class="icon-item">
                <i :class="'el-icon-' + item" />
                <span>{{ item }}</span>
              </div>
          </div>
        </div>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import cb3 from 'vue-clipboard3'
const{toClipboard} = cb3();

export default {
  name: "Icon",
  data(){
    return{
      elementIcons:[
        'info',
        'error',
        'success',
        'warning',
        'question',
        'back',
        'arrow-left',
        'arrow-down',
        'arrow-right',
        'arrow-up',
        'caret-left',
        'caret-bottom',
        'caret-top',
        'caret-right',
        'd-arrow-left',
        'd-arrow-right',
        'minus',
        'plus',
        'remove',
        'circle-plus',
        'remove-outline',
        'circle-plus-outline',
        'close',
        'check',
        'circle-close',
        'circle-check',
        'circle-close-outline',
        'circle-check-outline',
        'zoom-out',
        'zoom-in',
        'd-caret',
        'sort',
        'sort-down',
        'sort-up',
        'tickets',
        'document',
        'goods',
        'sold-out',
        'news',
        'message',
        'date',
        'printer',
        'time',
        'bell',
        'mobile-phone',
        'service',
        'view',
        'menu',
        'more',
        'more-outline',
        'star-on',
        'star-off',
        'location',
        'location-outline',
        'phone',
        'phone-outline',
        'picture',
        'picture-outline',
        'delete',
        'search',
        'edit',
        'edit-outline',
        'rank',
        'refresh',
        'share',
        'setting',
        'upload',
        'upload2',
        'download',
        'loading'
      ],
    }
  },methods:{
     copyToClipboard(item){
       let copy = '<'+'el-icon-'+item+'/>'
      toClipboard(copy),

        this.$notify({
          title: 'Success',
          message: '复制成功',
          duration: 2000
        });
     }
  }
}
</script>

<style lang="less" scoped>
.icons-container {
  margin: 10px 20px 0;
  overflow: hidden;

  .grid {
    position: relative;
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(120px, 1fr));
  }
  .icon-item {
    margin: 20px;
    height: 85px;
    text-align: center;
    width: 100px;
    float: left;
    font-size: 30px;
    color: #24292e;
    cursor: pointer;
  }
  span {
    display: block;
    font-size: 16px;
    margin-top: 10px;
  }
  .disabled {
    pointer-events: none;
  }
}
</style>

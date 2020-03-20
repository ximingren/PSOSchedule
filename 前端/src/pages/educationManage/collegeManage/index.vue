<template lang="html">
  <el-card class="box-card">
    <div>
      <el-table
        v-loading.body="tableLoading"
        ref="singleTable"
        :data="tableData"
        border
        highlight-current-row
        style="width: 100%">
        <el-table-column
          prop='id'
          label='id'
          width="60">
        </el-table-column>
        <el-table-column
          prop="collegeno"
          label="学院ID">
        </el-table-column>
        <el-table-column
          prop="collegename"
          label="学院">
        </el-table-column>
        </el-table>
    </div>
  </el-card>
</template>
<script>
import { getCollegeInfo } from '@/api/college'
export default {
    data() {
        return {
            tableLoading: false,
            tableData: []
        }
    },
    created() {
        this.initCollegeInfoList()
    },
    methods: {
        async initCollegeInfoList() {
            var data = await getCollegeInfo()
            this.tableData = data.data
        }
    }
}
</script>

<style lang="scss">
.fr{
    float:right;
}
.fl{
    float:left;
}
.search-bar{
    overflow: hidden;
}
</style>

<style>
.tools-bar{
  margin-bottom:20px;
}
</style>

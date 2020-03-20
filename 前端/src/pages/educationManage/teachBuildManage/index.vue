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
          prop="teachbuildno"
          label="教学楼ID"
          min-width="100">
        </el-table-column>
        <el-table-column
          prop="teachbuildname"
          label="教学楼名"
          min-width="100">
        </el-table-column>
      </el-table>
      <div class="pagination-bar">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :page-sizes="[10, 25, 50, 100]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="totalRecord">
        </el-pagination>
      </div>
    </div>
  </el-card>
</template>
<script>
import { getTeachBuildInfo } from '@/api/teachBuild'
import { Message } from 'element-ui'
export default {
    data() {
        return {
            totalRecord: 0,
            pageSize: 10,
            tableLoading: false,
            dialogVisible: false,
            dialogTitle: '新增教师',
            searchData: {
                condition: ''
            },
            dataForm: {
                id: '',
                teacherno: '',
                collegeno: '',
                teachername: '',
                age: '',
                title: ''
            },
            requestData: { 'pageParam': { 'pageNo': 1, 'pageSize': 10 } },
            tableData: [],
            collegeList: [],
            college: {}
        }
    },
    created() {
        this.initTeachBuildInfo()
        this.initCollegeInfoList()
    },
    methods: {
        async initTeachBuildInfo() {
            var data = await getTeachBuildInfo(this.requestData)
            this.tableData = data.data
            this.totalRecord = data.pageData.count
        },
        onDialogClose() {
            this.$refs.dataForm.resetFields()
        },
        async handleSizeChange(val) {
            this.requestData.pageParam.pageSize = val
            const data = await getTeachBuildInfo(this.requestData)
            this.tableData = data.data
        },
        async handleCurrentChange(val) {
            this.requestData.pageParam.pageNo = val
            const data = await getTeachBuildInfo(this.requestData)
            this.tableData = data.data
        },
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

<template lang="html">
<div>
  <el-card class="box-card">
    <div slot="header" class="clearfix">
    <span>个人课表</span>
    </div>
    <div>
      <el-table
        v-loading.body="tableLoading"
        ref="singleTable"
        :data="tableData"
        border
        highlight-current-row
        height='800'
        :cell-style="{height:'10px'}"
        :header-row-style="{fontSize:'20px'}"
        style="width: 100%;margin-top: 30px">
        <el-table-column
          prop="monday"
          style="hegith:30px"
          label="星期一">
          <template slot-scope="scope">
            <div v-html="scope.row.monday">
            </div>
          </template>
        </el-table-column>
        <el-table-column
          prop="tuesday"
          label="星期二">
           <template slot-scope="scope">
            <div v-html="scope.row.tuesday">
            </div>
          </template>
        </el-table-column>
        <el-table-column
          prop="wednesday"
          label="星期三">
            <template slot-scope="scope">
                <div v-html="scope.row.wednesday">
                </div>
            </template>
        </el-table-column>
        <el-table-column
          prop="thursday"
          label="星期四">
          <template slot-scope="scope">
            <div v-html="scope.row.thursday">
            </div>
           </template>
        </el-table-column>
        <el-table-column
          prop="friday"
          label="星期五">
           <template slot-scope="scope">
            <div v-html="scope.row.friday">
            </div>
            </template>
        </el-table-column>
      </el-table>
    </div>
  </el-card>
</div>
</template>
<script>
import { getCoursePlan } from '@/api/course'
export default {
    data() {
        return {
            totalRecord: 0,
            pageSize: 10,
            tableLoading: false,
            dialogVisible: false,
            defaultTreeProps: {
                children: 'childPermList',
                label: 'permissionName'
            },
            tableData: [],
            tempTable: []
        }
    },
    created() {
        this.initList()
    },
    methods: {
        async initList() {
            var requestData = { 'condition': '计算机科学与技术1班' }
            var data = await getCoursePlan(requestData)
            data = data.data
            this.handleCourseData(data)
        },
        handleCourseData(data) {
            for (var i = 1; i < 6; i++) {
                this.tempTable.push({
                    'monday': { [i]: '' },
                    'tuesday': { [i + 5]: '' },
                    'wednesday': { [i + 10]: '' },
                    'thursday': { [i + 15]: '' },
                    'friday': { [i + 20]: '' } })
            }
            var self = this
            data.forEach(function(ele) {
                const classtime = parseInt(ele['classtime'])
                const day = parseInt(classtime / 5) + (classtime % 5 === 0 ? 0 : 1)
                switch (day) {
                case 1:
                    self.addCourseDataToTable('monday', classtime, ele)
                    break
                case 2:
                    self.addCourseDataToTable('tuesday', classtime, ele)
                    break
                case 3:
                    self.addCourseDataToTable('wednesday', classtime, ele)
                    break
                case 4:
                    self.addCourseDataToTable('thursday', classtime, ele)
                    break
                case 5:
                    self.addCourseDataToTable('friday', classtime, ele)
                    break
                }
            })
            this.tempTable.forEach(function(ele) {
                var tempRow = {}
                for (let key in ele) {
                    for (let i in ele[key]) {
                        var name = ''
                        name = name + ele[key][i]
                        if (!ele[key][i]) {
                            name = '<div>&nbsp\n</div><div>&nbsp\n</div><div>&nbsp\n</div>'
                        }
                    }
                    tempRow[key] = name
                }
                self.tableData.push(tempRow)
            })
        },
        addCourseDataToTable(day, classtime, data) {
            this.tempTable.forEach(function(ele) {
                for (let item in ele[day]) {
                    if (parseInt(item) === classtime) {
                        ele[day][classtime] += '<div style="text-align:center;font-size:20px ">' + data['courseno'] + ' </div>'
                        ele[day][classtime] += '<div style="text-align:center;font-size:20px "> ' + data['teacherno'] + '</div>'
                        ele[day][classtime] += '<div style="text-align:center;font-size:20px "> ' + data['classroomno'] + '</div>'
                        ele[day][classtime] += '<div style="text-align:center;font-size:20px "> ' + data['weekssum'] + '周' + '</div>'
                    }
                }
            })
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

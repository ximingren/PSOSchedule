<template lang="html">
  <el-card class="box-card">
    <div class="search-bar">
      <el-form :inline="true" :model="requestData" class="fl">
        <el-form-item >
          <el-select v-model="requestData.collegeno"  placeholder="请选择学院" style="width: 100%;">
            <el-option
              v-for="item in collegeList"
              :key="item.id"
              :label="item.name"
              :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item >
          <el-input v-model="requestData.condition" placeholder="教师名 " @keyup.enter.native="onSearch()"></el-input>
        </el-form-item>
      </el-form>
      <div class="fl">
        <el-button type="text" @click="handleReset">重置</el-button>
        <el-button type="primary" icon="el-icon-search" @click="onSearch">查询</el-button>
      </div>
    </div>
    <div  class="tools-bar">
      <el-button type="success" icon="el-icon-plus" size="small" @click="dialogVisible = true;dialogTitle='新增教师'">新增教师</el-button>
    </div>
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
          prop="teacherno"
          label="教师ID"
          min-width="100">
        </el-table-column>
        <el-table-column
          prop="teachername"
          label="教师"
          min-width="100">
        </el-table-column>
        <el-table-column
          prop="collegeno"
          label="学院"
           :formatter="collegeFixFormatter"
          width="200">
        </el-table-column>
         <el-table-column
          prop="age"
          label="年龄"
          min-width="100">
        </el-table-column>
        <el-table-column
          prop="title"
          label="职称"
          min-width="100">
        </el-table-column>
        <el-table-column
          label="操作"
          fixed="right"
          width="150">
          <template slot-scope="scope">
            <div>
              <el-button
                type="text"
                size="small"
                @click="handleEdit(scope.$index, scope.row)"
              >编辑</el-button>
              <el-button
                type="text"
                size="small"
                @click="handleDelete(scope.$index, scope.row)"
              >删除</el-button>
            </div>
          </template>
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
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" @close="onDialogClose()">
      <el-form ref="dataForm" :model="dataForm" label-width="80px">
        <el-form-item label="教师" prop="teachername">
          <el-input v-model="dataForm.teachername" placeholder="教师"></el-input>
        </el-form-item>
        <el-form-item label="学院" prop="collegeno">
          <el-select v-model="dataForm.collegeno"  placeholder="请选择" style="width: 100%;">
            <el-option
              v-for="item in collegeList"
              :key="item.id"
              :label="item.name"
              :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
         <el-form-item label="年龄" prop="age">
          <el-input v-model="dataForm.age" placeholder="年龄"></el-input>
        </el-form-item>
         <el-form-item label="职称" prop="title">
          <el-input v-model="dataForm.title" placeholder="职称"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="info" @click="onDialogSubmit()" v-if="dialogTitle=='修改教师'">保存</el-button>
        <el-button type="primary" @click="onDialogSubmit()" v-else>立即创建</el-button>
      </div>
    </el-dialog>
  </el-card>
</template>
<script>
import { getTeacherInfo, saveTeacher, deleteTeacher } from '@/api/teacher'
import { getCollegeInfo } from '@/api/college'
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
        this.initTeacherInfo()
        this.initCollegeInfoList()
    },
    methods: {
        async initTeacherInfo() {
            var data = await getTeacherInfo(this.requestData)
            this.tableData = data.data
            this.totalRecord = data.pageData.count
        },
        async initCollegeInfoList() {
            var data = await getCollegeInfo()
            data = data.data
            var self = this
            data.forEach(function(item) {
                self.college[item['collegeno']] = item['collegename']
            })
            for (let key in this.college) {
                this.collegeList.push({ id: key, name: this.college[key] })
            }
        },
        onDialogClose() {
            this.$refs.dataForm.resetFields()
        },
        async handleSizeChange(val) {
            this.requestData.pageParam.pageSize = val
            const data = await getTeacherInfo(this.requestData)
            this.tableData = data.data
        },
        async handleCurrentChange(val) {
            this.requestData.pageParam.pageNo = val
            const data = await getTeacherInfo(this.requestData)
            this.tableData = data.data
        },
        async handleReset() {
            this.requestData.condition = ''
            this.requestData.collegeno = ''
            const data = await getTeacherInfo(this.requestData)
            this.tableData = data.data
        },
        async onSearch() {
            if (this.requestData.collegeno) {
                const data = await getTeacherInfo(this.requestData)
                this.tableData = data.data
            } else {
                Message.error('请选择学院')
            }
        },
        collegeFixFormatter(row, column, cellValue) {
            return (this.college[cellValue])
        },
        handleChangeStatus(index, row) {},
        handleDelete(index, row) {
            this.$confirm('确认删除该教师？', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                deleteTeacher(row)
            })
        },
        handleEdit(index, row) {
            this.dialogVisible = true
            this.dialogTitle = '修改教师'
            for (let x of Object.keys(this.dataForm)) {
                this.dataForm[x] = row[x]
            }
        },
        async onDialogSubmit() {
            await saveTeacher(this.dataForm)
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

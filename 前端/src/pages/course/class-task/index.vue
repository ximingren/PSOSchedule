<template lang="html">
  <el-card class="box-card"
  v-loading="scheduleLoding"
  element-loading-text="正在排课中，请等候！"
  element-loading-spinner="el-icon-loading"
  element-loading-background="rgba(0, 0, 0, 0.8)">
    <div class="search-bar">
      <div>
        <el-form :inline="true" :model="requestData" class="fl">
          <el-input style="display: none;"></el-input>
          <el-form-item >
            <el-input v-model="requestData.condition" placeholder="学期 / 班级 / 课程 / 教师" @keyup.enter.native="onSearch()"></el-input>
          </el-form-item>
        </el-form>
        <div class="fl">
          <el-button type="text" @click="handleReset">重置</el-button>
          <el-button type="primary" icon="el-icon-search" @click="onSearch">查询</el-button>
        </div>
      </div>
     <div class="fr">
        <el-form :inline="true" :model="scheduleData" class="fl">
          <el-input style="display: none;"></el-input>
          <el-form-item >
            <el-input v-model="scheduleData.semester" placeholder="学期" @keyup.enter.native="onSchedule()"></el-input>
          </el-form-item>
        </el-form>
        <div class="fl">
          <el-button type="primary" icon="el-icon-search" @click="onSchedule">排课</el-button>
        </div>
      </div>
    </div>
    <div  class="tools-bar">
      <el-button type="success" icon="el-icon-plus" size="small" @click="dialogVisible = true;dialogTitle='新增开课'">新增开课</el-button>
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
          prop="semester"
          label="学期"
          min-width="150">
        </el-table-column>
        <el-table-column
          prop="collegeno"
          label="学院"
          :formatter="collegeFixFormatter"
          min-width="120">
        </el-table-column>
        <el-table-column
          prop="classno"
          label="班级"
          :formatter="classFormatter"
          width="130">
        </el-table-column>
        <el-table-column
          prop="courseno"
          :formatter="courseFormatter"
          min-width="210"
          label="课程">
        </el-table-column>
        <el-table-column
          prop="teacherno"
          min-width="210"
          :formatter="teacherFormatter"
          label="教师">
        </el-table-column>
        <el-table-column
          prop="courseattr"
          min-width="200"
          :formatter="courseAttrFormatter"
          label="课程属性">
        </el-table-column>
        <el-table-column
          prop="studentnumber"
          label="学生数量"
          width="250">
        </el-table-column>
        <el-table-column
          prop="weekssum"
          label="上课周数"
          width="180">
        </el-table-column>
         <el-table-column
          prop="weeksnumber"
          label="上课学时"
          width="180">
        </el-table-column>
         <el-table-column
          prop="isfix"
          label="排课时间是否固定"
          :formatter="isFixFormatter"
          width="180">
        </el-table-column>
         <el-table-column
          prop="classtime"
          label="排课时间"
          width="180">
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
        <el-form-item label="学期" prop="semester">
          <el-input v-model="dataForm.semester" placeholder="学期"></el-input>
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
        <el-form-item label="班级" prop="classno">
            <el-select v-model="dataForm.classno"  placeholder="请选择" style="width: 100%;">
            <el-option
              v-for="item in classInfoList"
              :key="item.id"
              :label="item.name"
              :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="课程" prop="courseno">
              <el-select v-model="dataForm.courseno"  placeholder="请选择" style="width: 100%;">
            <el-option
              v-for="item in courseList"
              :key="item.id"
              :label="item.name"
              :value="item.id">
            </el-option>
          </el-select>
          </el-form-item>
      <el-form-item label="教师" prop="teacherno">
              <el-select v-model="dataForm.teacherno"  placeholder="请选择" style="width: 100%;">
            <el-option
              v-for="item in teacherList"
              :key="item.id"
              :label="item.name"
              :value="item.id">
            </el-option>
          </el-select>
          </el-form-item>
        <el-form-item label="课程属性" prop="courseattr">
          <el-select v-model="dataForm.courseattr"  placeholder="请选择" style="width: 100%;">
            <el-option
              v-for="item in courseAttrList"
              :key="item.id"
              :label="item.name"
              :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="学生数" prop="studentnumber">
              <el-input v-model="dataForm.studentnumber" placeholder="学生数"></el-input>
        </el-form-item>
        <el-form-item label="上课周数" prop="weekssum">
              <el-input v-model="dataForm.weekssum" placeholder="上课周数"></el-input>
        </el-form-item>
       <el-form-item label="上课学时" prop="weeksnumber">
              <el-input v-model="dataForm.weeksnumber" placeholder="上课学时"></el-input>
        </el-form-item>
        <el-form-item label="排课时间是否固定" prop="isfix">
        <el-select v-model="dataForm.isfix"  placeholder="请选择" style="width: 100%;">
            <el-option
              v-for="item in isFixList"
              :key="item.id"
              :label="item.name"
              :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="排课时间" prop="classtime">
              <el-input v-model="dataForm.classtime" placeholder="排课时间"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="info" @click="onDialogSubmit()" v-if="dialogTitle=='修改开课计划'">保存</el-button>
        <el-button type="primary" @click="onDialogSubmit()" v-else>立即创建</el-button>
      </div>
    </el-dialog>
  </el-card>
</template>
<script>
import { getClassTask, saveClassTask, getCourseInfo, deleteClassTask } from '@/api/course'
import { getCollegeInfo } from '@/api/college'
import { getTeacherInfo } from '@/api/teacher'
import { getClassInfo } from '@/api/class'
import { courseSchedule } from '@/api/schedule'
import { Message } from 'element-ui'
export default {
    data() {
        return {
            totalRecord: 0,
            pageSize: 10,
            tableLoading: false,
            dialogVisible: false,
            scheduleLoding: false,
            dialogTitle: '新增开课',
            searchData: {
                condition: ''
            },
            dataForm: {
                id: '',
                semester: '',
                collegeno: '',
                classno: '',
                courseno: '',
                teacherno: '',
                courseattr: '',
                studentnumber: '',
                weekssum: '',
                weeksnumber: '',
                isfix: '',
                classtime: ''
            },
            requestData: { 'pageParam': { 'pageNo': 1, 'pageSize': 10 } },
            scheduleData: {},
            tableData: [],
            college: {},
            courseAttr: {
                '01': '专业课',
                '02': '选修课',
                '03': '体育课',
                '04': '医学实验课',
                '05': '化学实验课',
                '06': '舞蹈实践课',
                '07': '播音实践课',
                '08': '电子实验课',
                '09': '美术实践课',
                '10': '计算机实验课',
                '11': '音乐表演课',
                '12': '物理实验课'
            },
            isFixList: [
                { 'id': 1,
                    'name': '不固定' },
                { 'id': 2,
                    'name': '固定' }
            ],
            courseAttrList: [],
            collegeList: [],
            classInfoList: [],
            courseList: [],
            teacherList: [],
            class: {},
            course: {},
            teacher: {}
        }
    },
    created() {
        for (let key in this.courseAttr) {
            this.courseAttrList.push({ id: key, name: this.courseAttr[key] })
        }
        this.initCollegeInfoList()
        this.initClassInfo()
        this.initCourseInfo()
        this.initTeacherInfo()
        this.initList()
    },
    methods: {
        initCourseAttr() {
            for (let key in this.courseAttr) {
                this.courseAttrList.push({ id: key, name: this.courseAttr[key] })
            }
        },
        async initTeacherInfo() {
            var data = await getTeacherInfo({})
            data = data.data
            var self = this
            data.forEach(function(item) {
                self.teacher[item['teacherno']] = item['teachername']
            })
            for (let key in this.teacher) {
                this.teacherList.push({ id: key, name: this.teacher[key] })
            }
        },
        async initCourseInfo() {
            var data = await getCourseInfo()
            data = data.data
            var self = this
            data.forEach(function(item) {
                self.course[item['courseno']] = item['coursename']
            })
            for (let key in this.course) {
                this.courseList.push({ id: key, name: this.course[key] })
            }
        },
        async initClassInfo() {
            var data = await getClassInfo()
            data = data.data
            var self = this
            data.forEach(function(item) {
                self.class[item['classno']] = item['classname']
            })
            for (let key in this.class) {
                this.classInfoList.push({ id: key, name: this.class[key] })
            }
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
        async initList() {
            const data = await getClassTask(this.requestData)
            this.tableData = data.data
            this.totalRecord = data.pageData.count
        },
        onDialogClose() {
            this.$refs.dataForm.resetFields()
        },
        handleSizeChange(val) {
            this.requestData.pageParam.pageSize = val
            this.onSearch()
        },
        handleCurrentChange(val) {
            this.requestData.pageParam.pageNo = val
            this.onSearch()
        },
        handleReset() {
            this.requestData.condition = ''
            this.onSearch()
        },
        async onSearch() {
            const data = await getClassTask(this.requestData)
            this.tableData = data.data
        },
        async onSchedule() {
            if (this.scheduleData.semester) {
                this.scheduleLoding = true
                await courseSchedule(this.scheduleData)
                this.scheduleLoding = false
            } else {
                Message.error('请输入学院')
            }
        },
        teacherFormatter(row, column, cellValue) {
            return (this.teacher[cellValue])
        },
        courseFormatter(row, column, cellValue) {
            return (this.course[cellValue])
        },
        classFormatter(row, column, cellValue) {
            return (this.class[cellValue])
        },
        collegeFixFormatter(row, column, cellValue) {
            return (this.college[cellValue])
        },
        isFixFormatter(row, column, cellValue) {
            switch (cellValue) {
            case '1':
                return '不固定'
            case '2':
                return '固定'
            }
        },
        courseAttrFormatter(row, column, cellValue) {
            return this.courseAttr[cellValue]
        },
        handleChangeStatus(index, row) {},
        handleDelete(index, row) {
            this.$confirm('确认删除该开课计划？', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                deleteClassTask(row)
            })
        },
        handleEdit(index, row) {
            this.dialogVisible = true
            this.dialogTitle = '修改开课计划'
            for (let x of Object.keys(this.dataForm)) {
                this.dataForm[x] = row[x]
            }
        },
        async onDialogSubmit() {
            await saveClassTask(this.dataForm)
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

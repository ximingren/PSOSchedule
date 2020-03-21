<template>
    <div>
        <router-view></router-view>
        <el-card>
            <div>今天是{{showTime}},欢迎你！</div>
        </el-card>
    </div>
</template>

<script>

export default {
    data() {
        return {
        }
    },
    created() {
        var nowTime = new Date()
        this.showTime = nowTime.getFullYear() + '-' + (nowTime.getMonth() + 1) + '-' + nowTime.getDate()
    },
    mounted() {

    }
}
</script>

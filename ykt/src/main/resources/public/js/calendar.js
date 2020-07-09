/**
 * Created by Administrator on 2017/11/8.
 */
$("#calePan").panel({
    fit:true
})
$('#calePan').fullCalendar({
    buttonText: {
        today: '今天',
        month: '月',
        week: '周',
        day: '日'
    },
    allDayText: "全天",
    timeFormat: {
        '': 'H:mm{-H:mm}'
    },
    weekMode: "variable",
    columnFormat: {
        month: 'dddd',
        week: 'dddd M-d',
        day: 'dddd M-d'
    },
    titleFormat: {
        month: 'yyyy年 MMMM月',
        week: "[yyyy年] MMMM月d日 { '&#8212;' [yyyy年] MMMM月d日}",
        day: 'yyyy年 MMMM月d日 dddd'
    },
    monthNames: ["1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"],
    dayNames: ["星期天", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"],
    header: {
        left: 'prev,next today',
        center: 'title',
        right: 'month,agendaWeek,agendaDay'
    },
    dayClick:function (date) {
        $("#addPan").dialog({
            closed:false
        })


    },
    eventClick: function (date, allDay, jsEvent, view) {
        //...
    },
    events: function (start, end, callback) {
        //...
    }
});
//
// 新建日程
function  addNew() {
    $("#addPan").dialog({
        closed:false
    })

}
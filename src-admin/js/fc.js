(function(window, $, cytoscape, ejs){

    $.ajaxSettings.async = false; 

    var originalData;


    var drawProcessing = function(cy){
        var canvas = $(cy._private.container).find('canvas')[2],
            ctx = canvas.getContext("2d");
        var offset = 0;

        var count = 0;

        function drawfn(){
            count++;
            offset+=5;
            if (offset > 50) {
                offset = 0;
            }
            
            ctx.clearRect(0,0, canvas.width, canvas.height);
            var pro_edges = cy.$('edge.isProcessing');

            pro_edges.forEach(function(v, i){
                var ePath = v.animatePath.path;
                var eType = v.animatePath.type;

                ctx.moveTo(ePath[0], ePath[1]);
                ctx.lineWidth = 2;
                ctx.strokeStyle = '#fff';
                ctx.fillStyle = '#fff';
                ctx.setLineDash([10,5]);
                ctx.lineDashOffset = -offset;

                switch( eType ){
                    case 'bezier':
                    case 'self':
                    case 'compound':
                    case 'multibezier':
                        for( var pi = 2; pi + 3 < ePath.length; pi += 4 ){
                            ctx.quadraticCurveTo( ePath[ pi ], ePath[ pi + 1], ePath[ pi + 2], ePath[ pi + 3] );
                        }
                        break;

                    case 'straight':
                    case 'segments':
                    case 'haystack':
                        for( var pi = 2; pi + 1 < ePath.length; pi += 2 ){
                            ctx.lineTo( ePath[ pi ], ePath[ pi + 1] );
                        }
                        break;
                }

                ctx.stroke();
            })
            
            setTimeout(drawfn, 150);
        }
        
        drawfn();
    
    }    



    var taskTip = function(){
        var self = this;
        var taskId = this.data().id;
        var taskType = (this.data().taskType == 'autoTasks') ? 'autoTasks' : 'userTasks';
        var originalPoints = this.data().original.points;
        var points = this.data().original[taskType][taskId].points;
        var phtml = ''; 
        (points.length>0) && points.forEach(function(p, pi){
            var status;
            if(!!self._private.classes.isFinished){
                status = '<i class="success">已采集；</i> 采集结果：<i class="result">' + self.data().original.state.points[p].value + '</i>';
            }
            if(!!self._private.classes.isProcessing){
                status = self.data().original.state.points.hasOwnProperty(p) ? ('<i class="success">已采集；</i>采集结果：<i class="result">' + self.data().original.state.points[p].value) + '</i>' : '<i class="ing">进行中</i>';
            }
            if(!self._private.classes.isProcessing && !self._private.classes.isFinished){
                status = '<i class="wait">未开始</i>';
            }
            
            phtml += '<li><span class="point">'+ originalPoints[p] +'：</span><span class="pointState">'+ status +'</span></li>'
        })
        return '<h5>'+ this.data().original[taskType][taskId].description +'</h5><ul class="edgeTip">'+ phtml +'</ul>';
    }
    var autotaskTmpl = function(model, type){
        var items = '';
        var autotask = '';
        originalTasks = originalData[type] || originalData.userTasks;
        model.forEach(function(v, i){
            var points = '';
            
            originalTasks.hasOwnProperty(v) && originalTasks[v].forEach(function(v, i){
                points += '<li>' +
                                '<span class="point">'+ 
                                    v + '：' + originalData.points[v] +
                                '</span>'+ 
                                '<span class="pointState">' + originalData.state.points[v].value + '</span>' +
                            '</li>'
            });
            points = '<ul class="taskItem">'+ points +'</ul>';
            items += '<li><span class="task">'+ v +'</span>'+points+'</li>'
        })
        autotask = '<ul class="taskList auto">'+ items +'</ul>';
        return autotask;
    }

    var parttaskTmpl = function(model){
        var items = '';
        var parttask = '';
        var autotask = '';

        model.forEach(function(v, i){
            (v.tasks.length>0) && (autotask = autotaskTmpl(v.tasks));
            items += '<li><span class="part">'+ (v.ggidKey ||  v.guidKey) +'</span>'+ autotask +'</li>'
        })

        parttask = '<ul class="taskList part">'+ items +'</ul>';
        return parttask;
    }

    var edgeTip = function(){
        var data = this.data().original;
        var autotask, usertask, partgtask, partutask;
        autotask = usertask = partgtask = partutask = '';

        (data.autoTasks.length>0) && (autotask = autotaskTmpl(data.autoTasks, 'autoTasks'));
        (data.userTasks.length>0) && (usertask = autotaskTmpl(data.userTasks));
        (data.partGTasks.length>0) && (partgtask = parttaskTmpl(data.partGTasks));
        (data.partUTasks.length>0) && (partutask = parttaskTmpl(data.partUTasks));

        $.trim(autotask) && (autotask = '<h5 class="taskTitle">自动任务</h5>' + autotask);
        $.trim(usertask) && (usertask = '<h5 class="taskTitle">用户任务</h5>' + usertask);
        $.trim(partgtask) && (partgtask = '<h5 class="taskTitle">参与方组任务</h5>' + partgtask);
        $.trim(partutask) && (partutask = '<h5 class="taskTitle">参与方任务</h5>' + partutask);

        var html = '<div class="edgeTip">' + 
                        ($.trim(autotask + usertask + partutask + partgtask) || '无需任何任务')
                    '</div>';
        return html;
    }

    var dateFormat = function(date, format){
        if(!/^[1-9]\d*$/.test(date)){
            return  date ;
        }
        var date = parseInt(date),
            currentTime =  new Date().getTime(),
            diffTime = currentTime - date;
        var minute = 60*1000,
            hour = 60*minute,
            day = 24*hour,
            format = format || 'YYYY年MM月DD日',
            alwaysDiff = alwaysDiff || false;
        var formatArr = ['YYYY','MM','DD','H','M','S'];
        var date = new Date(date),
            year = date.getFullYear(),
            month = date.getMonth()+1,
            month = (month>9) ? month : '0'+month,
            day = (date.getDate()>9) ? date.getDate() : '0'+ date.getDate(),
            hour = (date.getHours()>9) ? date.getHours() : '0'+ date.getHours(),
            minute = (date.getMinutes()>9) ? date.getMinutes() : '0'+date.getMinutes(),
            second = (date.getSeconds()>9) ? date.getSeconds() : '0'+date.getSeconds(),
            dateArr = [year,month,day,hour,':'+minute,':'+ second];
        for(var i=0; i < formatArr.length; i++){
            format = format.replace(formatArr[i],dateArr[i]);
        }
        return format;
    };

    var PAGE = function(){
        return {
            init: function(){
                originalData = this.getModel();
                this.fcRender();
                this.tmplRender();
            },
            getModel: function(){
                $.getJSON('./json/data4.json', function(res){
                    originalData = res;
                })
                return originalData;
            },
            tmplRender: function(){
                var data_fcDetail = {'fcDetail': {
                    'id': originalData.state.flowId,
                    'uid': originalData.state.guid,
                    'type': originalData.state.flowType,
                    'utype': originalData.state.guid.substr(originalData.state.guid.split('-')[0].length+1),
                    'status': originalData.state.ending || ''
                }};
                var fcDetail = ejs.compile($('#tmpl_fcDetail').html())(data_fcDetail);
                $('#fcDetail').html(fcDetail);

                // var data_ptDetail = {'points': [], 'task': {'type': 'autoTasks'}};
                // var ptDetail = ejs.compile($('#tmpl_ptDetail').html())(data_ptDetail);
                // $('#ptDetail').html(ptDetail);

                var historyPoints = [];
                for( var i in originalData.state.points){
                    var p = originalData.state.points[i];
                    var memo;
                    if(p.memo){
                        (p.memo.indexOf('img:')==0) && (memo = {url: p.memo.substr(4), text: '查看图片'});
                        (p.memo.indexOf('pdf:')==0) && (memo = {url: p.memo.substr(4), text: '查看PDF文件'});
                        !memo && (memo=p.memo);
                    } 
                    historyPoints.push({
                        'name': i,
                        'value': p.value || '未采集',
                        'user': p.operator || '无',
                        'timestamp': dateFormat(p.timestamp, 'YYYY-MM-DD H:M:S') || '无',
                        'description': originalData.points[i] || '无', 
                        'comment': memo || '无'
                    })
                }
                var data_history = {'historyPoints': historyPoints}
                // var data_history = {'historyPoints': [
                //     {'name': '属性', 'value': '文字描述文字描述文字描述文字描述文字描述文字描述文字描述文字描述文字描述文字描述文字描述文字描述文字描述文字描述文字描述文字描述文字描述文字描述文字描', 'user': '采集人', 'timestamp': dateFormat('1482624000000', 'YYYY-MM-DD'), 'description': '文字描述文字描述文字描述文字描文字描述文字描述文字描述文字描', 'comment': '备注备注'},
                //     {'name': '属性', 'value': 'value', 'user': 'user', 'timestamp': dateFormat('1482624000000', 'YYYY-MM-DD'), 'description': '描述', 'comment': '备注'},
                //     {'name': '属性', 'value': 'value', 'user': 'user', 'timestamp': dateFormat('1482624000000', 'YYYY-MM-DD'), 'description': '描述', 'comment': '备注'},
                //     {'name': '属性', 'value': 'value', 'user': 'user', 'timestamp': dateFormat('1482624000000', 'YYYY-MM-DD'), 'description': '描述', 'comment': '备注'},
                //     {'name': '属性', 'value': 'value', 'user': 'user', 'timestamp': dateFormat('1482624000000', 'YYYY-MM-DD'), 'description': '描述', 'comment': '备注'},
                //     {'name': '属性', 'value': 'value', 'user': 'user', 'timestamp': dateFormat('1482624000000', 'YYYY-MM-DD'), 'description': '描述', 'comment': '备注'},
                //     {'name': '属性', 'value': 'value', 'user': 'user', 'timestamp': dateFormat('1482624000000', 'YYYY-MM-DD'), 'description': '描述', 'comment': '备注'},
                //     {'name': '属性', 'value': 'value', 'user': 'user', 'timestamp': dateFormat('1482624000000', 'YYYY-MM-DD'), 'description': '描述', 'comment': '备注'},
                //     {'name': '属性', 'value': 'value', 'user': 'user', 'timestamp': dateFormat('1482624000000', 'YYYY-MM-DD'), 'description': '描述', 'comment': '备注'}
                // ]};
                var history = ejs.compile($('#tmpl_historyContainer').html())(data_history);
                $('#historyContainer').html(history);
            },
            fcRender: function(){
                var cy = new flowChart('cy', originalData);
                var count = 0;
                $('#cy canvas').css('visibility','hidden');
                window.cy = cy.cy;

                cy.cy.onRender(function(){
                    count ++;
                    if(count==2){
                        cy.cy.zoom(0).center();
                        setTimeout(function(){
                            $('#cy canvas').css('visibility','visible');
                        }, 100)
                        cy.cy.delay(100).animate({fit: {padding:20}}, {duration: 300});
                    }
                })
            }
        }
    };

    window.PAGE = PAGE;

    new PAGE().init();
})(window, jQuery, cytoscape, ejs)




		
(function(window, $, cytoscape, ejs){

    $.ajaxSettings.async = false; 

    var originalData;

    var chartEventCallback= function(cy){
        cy.nodes('.task-node').qtip({
            content: function(){
                return this.data().description || '暂无描述';
            },
            show: {
                event: 'mouseover'
            },
            hide: {
                event: 'mouseout'
            },
            position: {
                my: 'bottom center',
                at: 'top center'
            },
            style: {
                classes: 'qtip-bootstrap',
                tip: {
                    width: 16,
                    height: 8
                }
            }
        })

        cy.nodes('.task-node').on('click', function(e){
            var classes = this._private.classes;
            var data = this.data();
            var points = [];
            var task = data.original[(data.taskType=='autoTasks')?'autoTasks':'userTasks'][data.id];
            task && task.points.forEach(function(p, pi){
                var val;
                if(data.original.state.points.hasOwnProperty(p)){
                    if(data.original.state.points[p].memo){
                        var memo = data.original.state.points[p].memo;
                        (memo == 'img') && (val={'url': data.original.state.points[p].value, 'text': '查看图片'});
                        (memo == 'pdf') && (val={'url': data.original.state.points[p].value, 'text': '查看PDF文件'});
                    }

                    !val && (val = data.original.state.points[p].value);
                }else{
                    val = '未采集';
                }
                points.push({'key':p, 'value':val});
            })
            
            var data_ptDetail = {'points': points, 'task': {'type': data.taskType, 'id': data.id, 'classes': classes}};
            var ptDetail = ejs.compile($('#tmpl_ptDetail').html())(data_ptDetail);
            $('#ptDetail>div').html(ptDetail);

            $('.hastip').qtip({
                content: function(){
                    return '<span class="pointer"></span><div class="tip-pointtext-contentbg"></div><div class="tip-pointtextcontent"><div class="content">' + $(this).attr('data') + '</div></div>';
                },
                position: {
                    my: 'bottom right',
                    at: 'top right'
                },
                show: {
                    event: 'click'
                },
                hide: {
                    event: 'unfocus'
                },
                style: {
                    classes: 'qtip-bootstrap tip-pointtext',
                    tip: {
                        width: 16,
                        height: 8
                    }
                }
            })
            $('#refreshBtn').on('click', function(){
                var self = this;
                if($(this).hasClass('disabled')){
                    return;
                }
                $(this).addClass('disabled');
                $(this).parent().parent().append('<p class="successTip">已开始重新执行任务，请稍后刷新页面</p>');
                $('.successTip').delay(1000).fadeIn().delay(1500).fadeOut();
                setTimeout(function(){
                    $(self).removeClass('disabled');
                }, 5000)

                $.ajax({
                    url: '/auto/'+ originalData.state.flowType+'/'+ originalData.state.flowId +'/' + $(this).attr('data'),
                    method: 'POST',
                    async: true
                })
            })
        })

        cy.nodes('.judge-node').qtip({
            content: function(){
                return this.data().description;
            },
            show: {
                event: 'mouseover'
            },
            hide: {
                event: 'mouseout'
            },
            position: {
                my: 'bottom center',
                at: 'top center'
            },
            style: {
                classes: 'qtip-bootstrap',
                tip: {
                    width: 16,
                    height: 8
                }
            }
        })
    };

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
                // var url = '/api/flow/' + location.search.match(new RegExp("[\?\&]id=([^\&]+)", "i"))[1];
                 var url = '../json/data4.json'
                $.getJSON(url, function(res){
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
                    'status': originalData.state.ending || '进行中'
                }};
                var fcDetail = ejs.compile($('#tmpl_fcDetail').html())(data_fcDetail);
                $('#fcDetail').html(fcDetail);

                var historyPoints = [];
                for( var i in originalData.state.points){
                    var p = originalData.state.points[i];
                    var memo = p.memo;
                    var val = false;
                    if(p.memo){
                        (p.memo == 'img') && (val = {url: p.value, text: '查看图片'}) && (memo = false);
                        (p.memo == 'pdf') && (val = {url: p.value, text: '查看PDF文件'}) && (memo = false);
                    } 
                    !val && (val = p.value);
                    historyPoints.push({
                        'name': i,
                        'value': val || '未采集',
                        'user': p.operator || '无',
                        'timestamp': dateFormat(p.timestamp, 'YYYY-MM-DD HMS') || '无',
                        'description': originalData.points[i] || '无', 
                        'comment': memo || '无'
                    })
                }
                var history = ejs.compile($('#tmpl_historyContainer').html())({'historyPoints': historyPoints});
                $('#historyContainer').html(history);
            },
            fcRender: function(){
                var chart = new flowChart('cy', originalData, chartEventCallback, {minZoom: 0.1, layout:{
                    name: 'dagre',
                    // name: 'cose-bilkent',
                    fit: true,
                    // rankDir: "LR"
                }});
                var count = 0;
                $('#cy canvas').css('visibility','hidden');
                window.cy = chart.cy;
                chart.cy.onRender(function(){
                    count ++;
                    if(count==2){
                        chart.cy.zoom(0).center();
                        setTimeout(function(){
                            $('#cy canvas').css('visibility','visible');
                        }, 100)
                        chart.cy.delay(100).animate({fit: {padding:20}}, {duration: 300});
                    }
                })
            }
        }
    };
    
    window.PAGE = PAGE;

    new PAGE().init();
})(window, jQuery, cytoscape, ejs)




		
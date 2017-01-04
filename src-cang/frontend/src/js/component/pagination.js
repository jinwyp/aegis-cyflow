
var avalon = require('avalon2') ;

function heredoc(fn) {
    return fn.toString().replace(/^[^\/]+\/\*!?\s?/, '').replace(/\*\/[^\/]+$/, '').trim().replace(/>\s*</g, '><');
}

var paginationTemplate = heredoc(function() {

    /*

     <div class="table-pagination">
         <span class="table-pagination-first" ms-class="{disabled: @isDisabled('first', 1)}" ms-click="@_changePage($event, 'first', 1)"></span>
         <span class="table-pagination-prev" ms-class="{disabled: @isDisabled('prev', 1)}" ms-click="@_changePage($event, 'prev', @currentPage-1)"></span>
         <span class="separator"></span>

         <span class="table-pagination-input" >页数:
            <input class="ui-pg-input ui-corner-all" type="text" size="2" maxlength="2" ms-duplex="@_inputCurrentPages" ms-blur="@_changePage($event, 'input', @_inputCurrentPages)"> of {{@_totalPage}}
         </span>

         <span class="separator"></span>
         <span class="table-pagination-next" ms-class="{disabled: @isDisabled('next', @_totalPage)}" ms-click="@_changePage($event, 'next', @currentPage+1)"></span>
         <span class="table-pagination-last" ms-class="{disabled: @isDisabled('last', @_totalPage)}" ms-click="@_changePage($event, 'last', @_totalPage)"></span>

         <select class="select ui-pg-selbox ui-widget-content ui-corner-all"  ms-duplex-number="@countPerPage" data-duplex-changed="@_changeCountPerPage($event)">
             <option role="option" value="10">10</option>
             <option role="option" value="20">20</option>
             <option role="option" value="30">30</option>
             <option role="option" value="50">50</option>
             <option role="option" value="100">100</option>
         </select>
         <span class="table-pagination-total">当前 {{@_from}} - {{@_to}},  共 {{@totalCount}} 条 </span>
     </div>


     */

});


avalon.component('ms-pagination2', {
    template: paginationTemplate,
    defaults: {

        currentPage : 1,
        countPerPage : 10,
        totalCount : 0,
        isShowPagination : true,
        changePageNo : avalon.noop,

        _isShow : false,
        _inputCurrentPages : 1,
        _totalPage : 1,
        _from : 1,
        _to : 1,
        $buttons: {},

        onInit : function() {
            var vm = this;

            vm._showPaginations();
            //console.log('init', this.totalPages);

            this.$watch('totalCount', function(){
                setTimeout(function(){
                    vm._showPaginations();
                },2);
            });

            this.$watch('countPerPage', function(){
                setTimeout(function(){
                    vm._showPaginations();
                },2);
            });

            this.$watch('currentPage', function(){
                vm._inputCurrentPages = vm.currentPage
                setTimeout(function(){
                    vm._showPaginations();
                },2);
            });
        },

        onReady : function(){
            //console.log('ready', this.totalPages);
        },

        onViewChange : function(){
            //console.log('views change', this.totalPages);
        },

        isDisabled: function (name, page) {
            this.$buttons[name] = (this.currentPage === Number(page));
            return this.$buttons[name];
        },

        _changeCountPerPage : function(event ){
            var tempskip = (this.currentPage - 1) * Number(this.countPerPage);
            this.changePageNo(this.currentPage, tempskip, this.countPerPage);
        },

        _changePage : function(event, name, pageNo ){

            if (this.$buttons[name] || Number(pageNo) === this.currentPage) {
                return;  //disabled, active不会触发
            }

            event.preventDefault();
            var tempNo = Number(pageNo);

            if (tempNo < 1){
                tempNo = 1 ;
            }else if (tempNo > this._totalPage){
                tempNo = this._totalPage;
            }
            this.currentPage = tempNo;
            var tempskip = (tempNo - 1) * Number(this.countPerPage);
            this.changePageNo(tempNo, tempskip, this.countPerPage);
        },

        _showPaginations : function (totalPages) {
            var vm = this;

            // console.log('Total count: ', vm.totalCount, vm.countPerPage)
            vm._totalPage = Math.ceil(vm.totalCount / vm.countPerPage);

            vm._from = Number(vm.countPerPage) * (Number(vm.currentPage) - 1) + 1;
            vm._to = Number(vm.countPerPage) * Number(vm.currentPage);

            if (vm._to > vm.totalCount) vm._to = vm.totalCount;
            if (vm._from >= vm.total) vm._from = 1;
        }
    }

});


webpackJsonp([3],[
/* 0 */
/*!******************************!*\
  !*** multi adminSessionUser ***!
  \******************************/
/***/ function(module, exports, __webpack_require__) {

	module.exports = __webpack_require__(/*! ./js/page/adminSessionUser.js */14);


/***/ },
/* 1 */,
/* 2 */,
/* 3 */,
/* 4 */
/*!********************************!*\
  !*** ./js/component/header.js ***!
  \********************************/
/***/ function(module, exports, __webpack_require__) {

	/**
	 * Created by JinWYP on 24/11/2016.
	 */
	
	
	
	var $ = __webpack_require__(/*! jquery */ 2) ;
	__webpack_require__(/*! bootstrap/dist/js/bootstrap.js */ 5);
	__webpack_require__(/*! ./notify.js */ 6);
	__webpack_require__(/*! ./avalonFilter.js */ 7);
	
	var rawToken = __webpack_require__(/*! ../service/token.js */ 9).rawToken ;
	var sessionUserId = __webpack_require__(/*! ../service/token.js */ 9).sessionUserId ;
	var sessionUserRole = __webpack_require__(/*! ../service/token.js */ 9).sessionUserRole ;
	
	var userService = __webpack_require__(/*! ../service/user.js */ 8) ;
	
	
	var header = function() {
	
	    // 前端检查是否登录
	    if (!rawToken || !sessionUserId){
	        // window.location.href = '/warehouse/admin/login'
	    }
	
	    // 全局AJAX错误处理
	    $( document ).ajaxError(function(event, jqXHR, settings, thrownError) {
	        if (jqXHR.status === 401 || jqXHR.status === 403){
	            window.location.href = '/warehouse/admin/login'
	        }else{
	            var data = jqXHR.responseText;
	            if (jqXHR.getResponseHeader('content-type').indexOf('json') > -1){
	                data = JSON.parse(jqXHR.responseText);
	                $.notify(data, 'error');
	            }else{
	                console.log(data);
	                $.notify(data.error.message, 'error');
	            }
	
	            console.log(data);
	        }
	    });
	
	    $( document ).ajaxSuccess(function( event, jqXHR, settings, data ) {
	        if (!data.success) {
	            if (data.error.code === 401 || data.error.code === 403){
	                // window.location.href = '/warehouse/admin/login'
	            }else {
	                $.notify(data.error.message, 'error');
	                console.log(data.error);
	            }
	        }
	    });
	
	
	
	    // 退出登录
	    $("#user-logout").on("click", function() {
	        userService.logout();
	    });
	
	
	
	
	    // 点击隐藏的左部菜单, 并记住该设置
	    var tempLeftMenuFolded = localStorage.getItem('leftMenuFolded') || '';
	    var buttonToggleLeftMenu = $("[data-trigger-resize]");
	    var body = $("body");
	    if (tempLeftMenuFolded === 'true'){
	        console.log(tempLeftMenuFolded)
	        body.addClass("aside-collapsed")
	    }else{
	        body.removeClass("aside-collapsed")
	    }
	
	    buttonToggleLeftMenu.on("click", function() {
	        if (body.hasClass("aside-collapsed")){
	            localStorage.setItem('leftMenuFolded', 'false');
	            body.removeClass("aside-collapsed")
	        }else{
	            localStorage.setItem('leftMenuFolded', 'true');
	            body.addClass("aside-collapsed")
	        }
	    });
	
	
	    // 记住点击左部菜单中展开的菜单
	    var tempLeftMenuExpanded = localStorage.getItem('leftMenuExpanded');
	    var leftMenuExpanded = [];
	
	    if (tempLeftMenuExpanded) {
	        leftMenuExpanded = tempLeftMenuExpanded.split(',')
	    }
	    if (leftMenuExpanded.length > 0) {
	        leftMenuExpanded.forEach(function(menuId){
	            $( menuId ).addClass('collapse in')
	        })
	    }
	    $('a[data-toggle="collapse"]').on('click', function() {
	        var index = leftMenuExpanded.indexOf($( this ).attr('href'));
	
	        if (index === -1 ){
	            leftMenuExpanded.push($( this ).attr('href'));
	        }else {
	            leftMenuExpanded.splice(index, 1);
	        }
	
	        localStorage.setItem('leftMenuExpanded', leftMenuExpanded.toString());
	    });
	
	
	    // 记住点击左部菜单中的二级菜单中的选中状态
	    var leftMenuActive = localStorage.getItem('leftMenuActive') || '';
	    if (leftMenuActive) {
	        $( '#'+leftMenuActive ).parent().addClass('active')
	    }
	    $('.collapse li a').on('click', function() {
	
	        leftMenuActive = ($( this ).attr('id'));
	        localStorage.setItem('leftMenuActive', leftMenuActive.toString());
	    });
	
	
	    // 左侧菜单用户信息
	    userService.getSessionUser().done(function(data, textStatus, jqXHR) {
	        if (data.success){
	            $('.user-block-name').html(data.data.username);
	            $('.user-block-role').html(userService.userRoleAllObject[data.data.role]);
	        }else{
	            console.log(data.error);
	        }
	    });
	
	    // 非管理员隐藏用户管理菜单
	    if (sessionUserRole === userService.userRoleAllList[0].name){
	        $('.admin-user-management').removeClass('hidden');
	
	    }
	
	
	};
	
	header();
	
	module.exports = header;

/***/ },
/* 5 */
/*!******************************************!*\
  !*** ./~/bootstrap/dist/js/bootstrap.js ***!
  \******************************************/
/***/ function(module, exports, __webpack_require__) {

	/* WEBPACK VAR INJECTION */(function(jQuery) {/*!
	 * Bootstrap v3.3.7 (http://getbootstrap.com)
	 * Copyright 2011-2016 Twitter, Inc.
	 * Licensed under the MIT license
	 */
	
	if (typeof jQuery === 'undefined') {
	  throw new Error('Bootstrap\'s JavaScript requires jQuery')
	}
	
	+function ($) {
	  'use strict';
	  var version = $.fn.jquery.split(' ')[0].split('.')
	  if ((version[0] < 2 && version[1] < 9) || (version[0] == 1 && version[1] == 9 && version[2] < 1) || (version[0] > 3)) {
	    throw new Error('Bootstrap\'s JavaScript requires jQuery version 1.9.1 or higher, but lower than version 4')
	  }
	}(jQuery);
	
	/* ========================================================================
	 * Bootstrap: transition.js v3.3.7
	 * http://getbootstrap.com/javascript/#transitions
	 * ========================================================================
	 * Copyright 2011-2016 Twitter, Inc.
	 * Licensed under MIT (https://github.com/twbs/bootstrap/blob/master/LICENSE)
	 * ======================================================================== */
	
	
	+function ($) {
	  'use strict';
	
	  // CSS TRANSITION SUPPORT (Shoutout: http://www.modernizr.com/)
	  // ============================================================
	
	  function transitionEnd() {
	    var el = document.createElement('bootstrap')
	
	    var transEndEventNames = {
	      WebkitTransition : 'webkitTransitionEnd',
	      MozTransition    : 'transitionend',
	      OTransition      : 'oTransitionEnd otransitionend',
	      transition       : 'transitionend'
	    }
	
	    for (var name in transEndEventNames) {
	      if (el.style[name] !== undefined) {
	        return { end: transEndEventNames[name] }
	      }
	    }
	
	    return false // explicit for ie8 (  ._.)
	  }
	
	  // http://blog.alexmaccaw.com/css-transitions
	  $.fn.emulateTransitionEnd = function (duration) {
	    var called = false
	    var $el = this
	    $(this).one('bsTransitionEnd', function () { called = true })
	    var callback = function () { if (!called) $($el).trigger($.support.transition.end) }
	    setTimeout(callback, duration)
	    return this
	  }
	
	  $(function () {
	    $.support.transition = transitionEnd()
	
	    if (!$.support.transition) return
	
	    $.event.special.bsTransitionEnd = {
	      bindType: $.support.transition.end,
	      delegateType: $.support.transition.end,
	      handle: function (e) {
	        if ($(e.target).is(this)) return e.handleObj.handler.apply(this, arguments)
	      }
	    }
	  })
	
	}(jQuery);
	
	/* ========================================================================
	 * Bootstrap: alert.js v3.3.7
	 * http://getbootstrap.com/javascript/#alerts
	 * ========================================================================
	 * Copyright 2011-2016 Twitter, Inc.
	 * Licensed under MIT (https://github.com/twbs/bootstrap/blob/master/LICENSE)
	 * ======================================================================== */
	
	
	+function ($) {
	  'use strict';
	
	  // ALERT CLASS DEFINITION
	  // ======================
	
	  var dismiss = '[data-dismiss="alert"]'
	  var Alert   = function (el) {
	    $(el).on('click', dismiss, this.close)
	  }
	
	  Alert.VERSION = '3.3.7'
	
	  Alert.TRANSITION_DURATION = 150
	
	  Alert.prototype.close = function (e) {
	    var $this    = $(this)
	    var selector = $this.attr('data-target')
	
	    if (!selector) {
	      selector = $this.attr('href')
	      selector = selector && selector.replace(/.*(?=#[^\s]*$)/, '') // strip for ie7
	    }
	
	    var $parent = $(selector === '#' ? [] : selector)
	
	    if (e) e.preventDefault()
	
	    if (!$parent.length) {
	      $parent = $this.closest('.alert')
	    }
	
	    $parent.trigger(e = $.Event('close.bs.alert'))
	
	    if (e.isDefaultPrevented()) return
	
	    $parent.removeClass('in')
	
	    function removeElement() {
	      // detach from parent, fire event then clean up data
	      $parent.detach().trigger('closed.bs.alert').remove()
	    }
	
	    $.support.transition && $parent.hasClass('fade') ?
	      $parent
	        .one('bsTransitionEnd', removeElement)
	        .emulateTransitionEnd(Alert.TRANSITION_DURATION) :
	      removeElement()
	  }
	
	
	  // ALERT PLUGIN DEFINITION
	  // =======================
	
	  function Plugin(option) {
	    return this.each(function () {
	      var $this = $(this)
	      var data  = $this.data('bs.alert')
	
	      if (!data) $this.data('bs.alert', (data = new Alert(this)))
	      if (typeof option == 'string') data[option].call($this)
	    })
	  }
	
	  var old = $.fn.alert
	
	  $.fn.alert             = Plugin
	  $.fn.alert.Constructor = Alert
	
	
	  // ALERT NO CONFLICT
	  // =================
	
	  $.fn.alert.noConflict = function () {
	    $.fn.alert = old
	    return this
	  }
	
	
	  // ALERT DATA-API
	  // ==============
	
	  $(document).on('click.bs.alert.data-api', dismiss, Alert.prototype.close)
	
	}(jQuery);
	
	/* ========================================================================
	 * Bootstrap: button.js v3.3.7
	 * http://getbootstrap.com/javascript/#buttons
	 * ========================================================================
	 * Copyright 2011-2016 Twitter, Inc.
	 * Licensed under MIT (https://github.com/twbs/bootstrap/blob/master/LICENSE)
	 * ======================================================================== */
	
	
	+function ($) {
	  'use strict';
	
	  // BUTTON PUBLIC CLASS DEFINITION
	  // ==============================
	
	  var Button = function (element, options) {
	    this.$element  = $(element)
	    this.options   = $.extend({}, Button.DEFAULTS, options)
	    this.isLoading = false
	  }
	
	  Button.VERSION  = '3.3.7'
	
	  Button.DEFAULTS = {
	    loadingText: 'loading...'
	  }
	
	  Button.prototype.setState = function (state) {
	    var d    = 'disabled'
	    var $el  = this.$element
	    var val  = $el.is('input') ? 'val' : 'html'
	    var data = $el.data()
	
	    state += 'Text'
	
	    if (data.resetText == null) $el.data('resetText', $el[val]())
	
	    // push to event loop to allow forms to submit
	    setTimeout($.proxy(function () {
	      $el[val](data[state] == null ? this.options[state] : data[state])
	
	      if (state == 'loadingText') {
	        this.isLoading = true
	        $el.addClass(d).attr(d, d).prop(d, true)
	      } else if (this.isLoading) {
	        this.isLoading = false
	        $el.removeClass(d).removeAttr(d).prop(d, false)
	      }
	    }, this), 0)
	  }
	
	  Button.prototype.toggle = function () {
	    var changed = true
	    var $parent = this.$element.closest('[data-toggle="buttons"]')
	
	    if ($parent.length) {
	      var $input = this.$element.find('input')
	      if ($input.prop('type') == 'radio') {
	        if ($input.prop('checked')) changed = false
	        $parent.find('.active').removeClass('active')
	        this.$element.addClass('active')
	      } else if ($input.prop('type') == 'checkbox') {
	        if (($input.prop('checked')) !== this.$element.hasClass('active')) changed = false
	        this.$element.toggleClass('active')
	      }
	      $input.prop('checked', this.$element.hasClass('active'))
	      if (changed) $input.trigger('change')
	    } else {
	      this.$element.attr('aria-pressed', !this.$element.hasClass('active'))
	      this.$element.toggleClass('active')
	    }
	  }
	
	
	  // BUTTON PLUGIN DEFINITION
	  // ========================
	
	  function Plugin(option) {
	    return this.each(function () {
	      var $this   = $(this)
	      var data    = $this.data('bs.button')
	      var options = typeof option == 'object' && option
	
	      if (!data) $this.data('bs.button', (data = new Button(this, options)))
	
	      if (option == 'toggle') data.toggle()
	      else if (option) data.setState(option)
	    })
	  }
	
	  var old = $.fn.button
	
	  $.fn.button             = Plugin
	  $.fn.button.Constructor = Button
	
	
	  // BUTTON NO CONFLICT
	  // ==================
	
	  $.fn.button.noConflict = function () {
	    $.fn.button = old
	    return this
	  }
	
	
	  // BUTTON DATA-API
	  // ===============
	
	  $(document)
	    .on('click.bs.button.data-api', '[data-toggle^="button"]', function (e) {
	      var $btn = $(e.target).closest('.btn')
	      Plugin.call($btn, 'toggle')
	      if (!($(e.target).is('input[type="radio"], input[type="checkbox"]'))) {
	        // Prevent double click on radios, and the double selections (so cancellation) on checkboxes
	        e.preventDefault()
	        // The target component still receive the focus
	        if ($btn.is('input,button')) $btn.trigger('focus')
	        else $btn.find('input:visible,button:visible').first().trigger('focus')
	      }
	    })
	    .on('focus.bs.button.data-api blur.bs.button.data-api', '[data-toggle^="button"]', function (e) {
	      $(e.target).closest('.btn').toggleClass('focus', /^focus(in)?$/.test(e.type))
	    })
	
	}(jQuery);
	
	/* ========================================================================
	 * Bootstrap: carousel.js v3.3.7
	 * http://getbootstrap.com/javascript/#carousel
	 * ========================================================================
	 * Copyright 2011-2016 Twitter, Inc.
	 * Licensed under MIT (https://github.com/twbs/bootstrap/blob/master/LICENSE)
	 * ======================================================================== */
	
	
	+function ($) {
	  'use strict';
	
	  // CAROUSEL CLASS DEFINITION
	  // =========================
	
	  var Carousel = function (element, options) {
	    this.$element    = $(element)
	    this.$indicators = this.$element.find('.carousel-indicators')
	    this.options     = options
	    this.paused      = null
	    this.sliding     = null
	    this.interval    = null
	    this.$active     = null
	    this.$items      = null
	
	    this.options.keyboard && this.$element.on('keydown.bs.carousel', $.proxy(this.keydown, this))
	
	    this.options.pause == 'hover' && !('ontouchstart' in document.documentElement) && this.$element
	      .on('mouseenter.bs.carousel', $.proxy(this.pause, this))
	      .on('mouseleave.bs.carousel', $.proxy(this.cycle, this))
	  }
	
	  Carousel.VERSION  = '3.3.7'
	
	  Carousel.TRANSITION_DURATION = 600
	
	  Carousel.DEFAULTS = {
	    interval: 5000,
	    pause: 'hover',
	    wrap: true,
	    keyboard: true
	  }
	
	  Carousel.prototype.keydown = function (e) {
	    if (/input|textarea/i.test(e.target.tagName)) return
	    switch (e.which) {
	      case 37: this.prev(); break
	      case 39: this.next(); break
	      default: return
	    }
	
	    e.preventDefault()
	  }
	
	  Carousel.prototype.cycle = function (e) {
	    e || (this.paused = false)
	
	    this.interval && clearInterval(this.interval)
	
	    this.options.interval
	      && !this.paused
	      && (this.interval = setInterval($.proxy(this.next, this), this.options.interval))
	
	    return this
	  }
	
	  Carousel.prototype.getItemIndex = function (item) {
	    this.$items = item.parent().children('.item')
	    return this.$items.index(item || this.$active)
	  }
	
	  Carousel.prototype.getItemForDirection = function (direction, active) {
	    var activeIndex = this.getItemIndex(active)
	    var willWrap = (direction == 'prev' && activeIndex === 0)
	                || (direction == 'next' && activeIndex == (this.$items.length - 1))
	    if (willWrap && !this.options.wrap) return active
	    var delta = direction == 'prev' ? -1 : 1
	    var itemIndex = (activeIndex + delta) % this.$items.length
	    return this.$items.eq(itemIndex)
	  }
	
	  Carousel.prototype.to = function (pos) {
	    var that        = this
	    var activeIndex = this.getItemIndex(this.$active = this.$element.find('.item.active'))
	
	    if (pos > (this.$items.length - 1) || pos < 0) return
	
	    if (this.sliding)       return this.$element.one('slid.bs.carousel', function () { that.to(pos) }) // yes, "slid"
	    if (activeIndex == pos) return this.pause().cycle()
	
	    return this.slide(pos > activeIndex ? 'next' : 'prev', this.$items.eq(pos))
	  }
	
	  Carousel.prototype.pause = function (e) {
	    e || (this.paused = true)
	
	    if (this.$element.find('.next, .prev').length && $.support.transition) {
	      this.$element.trigger($.support.transition.end)
	      this.cycle(true)
	    }
	
	    this.interval = clearInterval(this.interval)
	
	    return this
	  }
	
	  Carousel.prototype.next = function () {
	    if (this.sliding) return
	    return this.slide('next')
	  }
	
	  Carousel.prototype.prev = function () {
	    if (this.sliding) return
	    return this.slide('prev')
	  }
	
	  Carousel.prototype.slide = function (type, next) {
	    var $active   = this.$element.find('.item.active')
	    var $next     = next || this.getItemForDirection(type, $active)
	    var isCycling = this.interval
	    var direction = type == 'next' ? 'left' : 'right'
	    var that      = this
	
	    if ($next.hasClass('active')) return (this.sliding = false)
	
	    var relatedTarget = $next[0]
	    var slideEvent = $.Event('slide.bs.carousel', {
	      relatedTarget: relatedTarget,
	      direction: direction
	    })
	    this.$element.trigger(slideEvent)
	    if (slideEvent.isDefaultPrevented()) return
	
	    this.sliding = true
	
	    isCycling && this.pause()
	
	    if (this.$indicators.length) {
	      this.$indicators.find('.active').removeClass('active')
	      var $nextIndicator = $(this.$indicators.children()[this.getItemIndex($next)])
	      $nextIndicator && $nextIndicator.addClass('active')
	    }
	
	    var slidEvent = $.Event('slid.bs.carousel', { relatedTarget: relatedTarget, direction: direction }) // yes, "slid"
	    if ($.support.transition && this.$element.hasClass('slide')) {
	      $next.addClass(type)
	      $next[0].offsetWidth // force reflow
	      $active.addClass(direction)
	      $next.addClass(direction)
	      $active
	        .one('bsTransitionEnd', function () {
	          $next.removeClass([type, direction].join(' ')).addClass('active')
	          $active.removeClass(['active', direction].join(' '))
	          that.sliding = false
	          setTimeout(function () {
	            that.$element.trigger(slidEvent)
	          }, 0)
	        })
	        .emulateTransitionEnd(Carousel.TRANSITION_DURATION)
	    } else {
	      $active.removeClass('active')
	      $next.addClass('active')
	      this.sliding = false
	      this.$element.trigger(slidEvent)
	    }
	
	    isCycling && this.cycle()
	
	    return this
	  }
	
	
	  // CAROUSEL PLUGIN DEFINITION
	  // ==========================
	
	  function Plugin(option) {
	    return this.each(function () {
	      var $this   = $(this)
	      var data    = $this.data('bs.carousel')
	      var options = $.extend({}, Carousel.DEFAULTS, $this.data(), typeof option == 'object' && option)
	      var action  = typeof option == 'string' ? option : options.slide
	
	      if (!data) $this.data('bs.carousel', (data = new Carousel(this, options)))
	      if (typeof option == 'number') data.to(option)
	      else if (action) data[action]()
	      else if (options.interval) data.pause().cycle()
	    })
	  }
	
	  var old = $.fn.carousel
	
	  $.fn.carousel             = Plugin
	  $.fn.carousel.Constructor = Carousel
	
	
	  // CAROUSEL NO CONFLICT
	  // ====================
	
	  $.fn.carousel.noConflict = function () {
	    $.fn.carousel = old
	    return this
	  }
	
	
	  // CAROUSEL DATA-API
	  // =================
	
	  var clickHandler = function (e) {
	    var href
	    var $this   = $(this)
	    var $target = $($this.attr('data-target') || (href = $this.attr('href')) && href.replace(/.*(?=#[^\s]+$)/, '')) // strip for ie7
	    if (!$target.hasClass('carousel')) return
	    var options = $.extend({}, $target.data(), $this.data())
	    var slideIndex = $this.attr('data-slide-to')
	    if (slideIndex) options.interval = false
	
	    Plugin.call($target, options)
	
	    if (slideIndex) {
	      $target.data('bs.carousel').to(slideIndex)
	    }
	
	    e.preventDefault()
	  }
	
	  $(document)
	    .on('click.bs.carousel.data-api', '[data-slide]', clickHandler)
	    .on('click.bs.carousel.data-api', '[data-slide-to]', clickHandler)
	
	  $(window).on('load', function () {
	    $('[data-ride="carousel"]').each(function () {
	      var $carousel = $(this)
	      Plugin.call($carousel, $carousel.data())
	    })
	  })
	
	}(jQuery);
	
	/* ========================================================================
	 * Bootstrap: collapse.js v3.3.7
	 * http://getbootstrap.com/javascript/#collapse
	 * ========================================================================
	 * Copyright 2011-2016 Twitter, Inc.
	 * Licensed under MIT (https://github.com/twbs/bootstrap/blob/master/LICENSE)
	 * ======================================================================== */
	
	/* jshint latedef: false */
	
	+function ($) {
	  'use strict';
	
	  // COLLAPSE PUBLIC CLASS DEFINITION
	  // ================================
	
	  var Collapse = function (element, options) {
	    this.$element      = $(element)
	    this.options       = $.extend({}, Collapse.DEFAULTS, options)
	    this.$trigger      = $('[data-toggle="collapse"][href="#' + element.id + '"],' +
	                           '[data-toggle="collapse"][data-target="#' + element.id + '"]')
	    this.transitioning = null
	
	    if (this.options.parent) {
	      this.$parent = this.getParent()
	    } else {
	      this.addAriaAndCollapsedClass(this.$element, this.$trigger)
	    }
	
	    if (this.options.toggle) this.toggle()
	  }
	
	  Collapse.VERSION  = '3.3.7'
	
	  Collapse.TRANSITION_DURATION = 350
	
	  Collapse.DEFAULTS = {
	    toggle: true
	  }
	
	  Collapse.prototype.dimension = function () {
	    var hasWidth = this.$element.hasClass('width')
	    return hasWidth ? 'width' : 'height'
	  }
	
	  Collapse.prototype.show = function () {
	    if (this.transitioning || this.$element.hasClass('in')) return
	
	    var activesData
	    var actives = this.$parent && this.$parent.children('.panel').children('.in, .collapsing')
	
	    if (actives && actives.length) {
	      activesData = actives.data('bs.collapse')
	      if (activesData && activesData.transitioning) return
	    }
	
	    var startEvent = $.Event('show.bs.collapse')
	    this.$element.trigger(startEvent)
	    if (startEvent.isDefaultPrevented()) return
	
	    if (actives && actives.length) {
	      Plugin.call(actives, 'hide')
	      activesData || actives.data('bs.collapse', null)
	    }
	
	    var dimension = this.dimension()
	
	    this.$element
	      .removeClass('collapse')
	      .addClass('collapsing')[dimension](0)
	      .attr('aria-expanded', true)
	
	    this.$trigger
	      .removeClass('collapsed')
	      .attr('aria-expanded', true)
	
	    this.transitioning = 1
	
	    var complete = function () {
	      this.$element
	        .removeClass('collapsing')
	        .addClass('collapse in')[dimension]('')
	      this.transitioning = 0
	      this.$element
	        .trigger('shown.bs.collapse')
	    }
	
	    if (!$.support.transition) return complete.call(this)
	
	    var scrollSize = $.camelCase(['scroll', dimension].join('-'))
	
	    this.$element
	      .one('bsTransitionEnd', $.proxy(complete, this))
	      .emulateTransitionEnd(Collapse.TRANSITION_DURATION)[dimension](this.$element[0][scrollSize])
	  }
	
	  Collapse.prototype.hide = function () {
	    if (this.transitioning || !this.$element.hasClass('in')) return
	
	    var startEvent = $.Event('hide.bs.collapse')
	    this.$element.trigger(startEvent)
	    if (startEvent.isDefaultPrevented()) return
	
	    var dimension = this.dimension()
	
	    this.$element[dimension](this.$element[dimension]())[0].offsetHeight
	
	    this.$element
	      .addClass('collapsing')
	      .removeClass('collapse in')
	      .attr('aria-expanded', false)
	
	    this.$trigger
	      .addClass('collapsed')
	      .attr('aria-expanded', false)
	
	    this.transitioning = 1
	
	    var complete = function () {
	      this.transitioning = 0
	      this.$element
	        .removeClass('collapsing')
	        .addClass('collapse')
	        .trigger('hidden.bs.collapse')
	    }
	
	    if (!$.support.transition) return complete.call(this)
	
	    this.$element
	      [dimension](0)
	      .one('bsTransitionEnd', $.proxy(complete, this))
	      .emulateTransitionEnd(Collapse.TRANSITION_DURATION)
	  }
	
	  Collapse.prototype.toggle = function () {
	    this[this.$element.hasClass('in') ? 'hide' : 'show']()
	  }
	
	  Collapse.prototype.getParent = function () {
	    return $(this.options.parent)
	      .find('[data-toggle="collapse"][data-parent="' + this.options.parent + '"]')
	      .each($.proxy(function (i, element) {
	        var $element = $(element)
	        this.addAriaAndCollapsedClass(getTargetFromTrigger($element), $element)
	      }, this))
	      .end()
	  }
	
	  Collapse.prototype.addAriaAndCollapsedClass = function ($element, $trigger) {
	    var isOpen = $element.hasClass('in')
	
	    $element.attr('aria-expanded', isOpen)
	    $trigger
	      .toggleClass('collapsed', !isOpen)
	      .attr('aria-expanded', isOpen)
	  }
	
	  function getTargetFromTrigger($trigger) {
	    var href
	    var target = $trigger.attr('data-target')
	      || (href = $trigger.attr('href')) && href.replace(/.*(?=#[^\s]+$)/, '') // strip for ie7
	
	    return $(target)
	  }
	
	
	  // COLLAPSE PLUGIN DEFINITION
	  // ==========================
	
	  function Plugin(option) {
	    return this.each(function () {
	      var $this   = $(this)
	      var data    = $this.data('bs.collapse')
	      var options = $.extend({}, Collapse.DEFAULTS, $this.data(), typeof option == 'object' && option)
	
	      if (!data && options.toggle && /show|hide/.test(option)) options.toggle = false
	      if (!data) $this.data('bs.collapse', (data = new Collapse(this, options)))
	      if (typeof option == 'string') data[option]()
	    })
	  }
	
	  var old = $.fn.collapse
	
	  $.fn.collapse             = Plugin
	  $.fn.collapse.Constructor = Collapse
	
	
	  // COLLAPSE NO CONFLICT
	  // ====================
	
	  $.fn.collapse.noConflict = function () {
	    $.fn.collapse = old
	    return this
	  }
	
	
	  // COLLAPSE DATA-API
	  // =================
	
	  $(document).on('click.bs.collapse.data-api', '[data-toggle="collapse"]', function (e) {
	    var $this   = $(this)
	
	    if (!$this.attr('data-target')) e.preventDefault()
	
	    var $target = getTargetFromTrigger($this)
	    var data    = $target.data('bs.collapse')
	    var option  = data ? 'toggle' : $this.data()
	
	    Plugin.call($target, option)
	  })
	
	}(jQuery);
	
	/* ========================================================================
	 * Bootstrap: dropdown.js v3.3.7
	 * http://getbootstrap.com/javascript/#dropdowns
	 * ========================================================================
	 * Copyright 2011-2016 Twitter, Inc.
	 * Licensed under MIT (https://github.com/twbs/bootstrap/blob/master/LICENSE)
	 * ======================================================================== */
	
	
	+function ($) {
	  'use strict';
	
	  // DROPDOWN CLASS DEFINITION
	  // =========================
	
	  var backdrop = '.dropdown-backdrop'
	  var toggle   = '[data-toggle="dropdown"]'
	  var Dropdown = function (element) {
	    $(element).on('click.bs.dropdown', this.toggle)
	  }
	
	  Dropdown.VERSION = '3.3.7'
	
	  function getParent($this) {
	    var selector = $this.attr('data-target')
	
	    if (!selector) {
	      selector = $this.attr('href')
	      selector = selector && /#[A-Za-z]/.test(selector) && selector.replace(/.*(?=#[^\s]*$)/, '') // strip for ie7
	    }
	
	    var $parent = selector && $(selector)
	
	    return $parent && $parent.length ? $parent : $this.parent()
	  }
	
	  function clearMenus(e) {
	    if (e && e.which === 3) return
	    $(backdrop).remove()
	    $(toggle).each(function () {
	      var $this         = $(this)
	      var $parent       = getParent($this)
	      var relatedTarget = { relatedTarget: this }
	
	      if (!$parent.hasClass('open')) return
	
	      if (e && e.type == 'click' && /input|textarea/i.test(e.target.tagName) && $.contains($parent[0], e.target)) return
	
	      $parent.trigger(e = $.Event('hide.bs.dropdown', relatedTarget))
	
	      if (e.isDefaultPrevented()) return
	
	      $this.attr('aria-expanded', 'false')
	      $parent.removeClass('open').trigger($.Event('hidden.bs.dropdown', relatedTarget))
	    })
	  }
	
	  Dropdown.prototype.toggle = function (e) {
	    var $this = $(this)
	
	    if ($this.is('.disabled, :disabled')) return
	
	    var $parent  = getParent($this)
	    var isActive = $parent.hasClass('open')
	
	    clearMenus()
	
	    if (!isActive) {
	      if ('ontouchstart' in document.documentElement && !$parent.closest('.navbar-nav').length) {
	        // if mobile we use a backdrop because click events don't delegate
	        $(document.createElement('div'))
	          .addClass('dropdown-backdrop')
	          .insertAfter($(this))
	          .on('click', clearMenus)
	      }
	
	      var relatedTarget = { relatedTarget: this }
	      $parent.trigger(e = $.Event('show.bs.dropdown', relatedTarget))
	
	      if (e.isDefaultPrevented()) return
	
	      $this
	        .trigger('focus')
	        .attr('aria-expanded', 'true')
	
	      $parent
	        .toggleClass('open')
	        .trigger($.Event('shown.bs.dropdown', relatedTarget))
	    }
	
	    return false
	  }
	
	  Dropdown.prototype.keydown = function (e) {
	    if (!/(38|40|27|32)/.test(e.which) || /input|textarea/i.test(e.target.tagName)) return
	
	    var $this = $(this)
	
	    e.preventDefault()
	    e.stopPropagation()
	
	    if ($this.is('.disabled, :disabled')) return
	
	    var $parent  = getParent($this)
	    var isActive = $parent.hasClass('open')
	
	    if (!isActive && e.which != 27 || isActive && e.which == 27) {
	      if (e.which == 27) $parent.find(toggle).trigger('focus')
	      return $this.trigger('click')
	    }
	
	    var desc = ' li:not(.disabled):visible a'
	    var $items = $parent.find('.dropdown-menu' + desc)
	
	    if (!$items.length) return
	
	    var index = $items.index(e.target)
	
	    if (e.which == 38 && index > 0)                 index--         // up
	    if (e.which == 40 && index < $items.length - 1) index++         // down
	    if (!~index)                                    index = 0
	
	    $items.eq(index).trigger('focus')
	  }
	
	
	  // DROPDOWN PLUGIN DEFINITION
	  // ==========================
	
	  function Plugin(option) {
	    return this.each(function () {
	      var $this = $(this)
	      var data  = $this.data('bs.dropdown')
	
	      if (!data) $this.data('bs.dropdown', (data = new Dropdown(this)))
	      if (typeof option == 'string') data[option].call($this)
	    })
	  }
	
	  var old = $.fn.dropdown
	
	  $.fn.dropdown             = Plugin
	  $.fn.dropdown.Constructor = Dropdown
	
	
	  // DROPDOWN NO CONFLICT
	  // ====================
	
	  $.fn.dropdown.noConflict = function () {
	    $.fn.dropdown = old
	    return this
	  }
	
	
	  // APPLY TO STANDARD DROPDOWN ELEMENTS
	  // ===================================
	
	  $(document)
	    .on('click.bs.dropdown.data-api', clearMenus)
	    .on('click.bs.dropdown.data-api', '.dropdown form', function (e) { e.stopPropagation() })
	    .on('click.bs.dropdown.data-api', toggle, Dropdown.prototype.toggle)
	    .on('keydown.bs.dropdown.data-api', toggle, Dropdown.prototype.keydown)
	    .on('keydown.bs.dropdown.data-api', '.dropdown-menu', Dropdown.prototype.keydown)
	
	}(jQuery);
	
	/* ========================================================================
	 * Bootstrap: modal.js v3.3.7
	 * http://getbootstrap.com/javascript/#modals
	 * ========================================================================
	 * Copyright 2011-2016 Twitter, Inc.
	 * Licensed under MIT (https://github.com/twbs/bootstrap/blob/master/LICENSE)
	 * ======================================================================== */
	
	
	+function ($) {
	  'use strict';
	
	  // MODAL CLASS DEFINITION
	  // ======================
	
	  var Modal = function (element, options) {
	    this.options             = options
	    this.$body               = $(document.body)
	    this.$element            = $(element)
	    this.$dialog             = this.$element.find('.modal-dialog')
	    this.$backdrop           = null
	    this.isShown             = null
	    this.originalBodyPad     = null
	    this.scrollbarWidth      = 0
	    this.ignoreBackdropClick = false
	
	    if (this.options.remote) {
	      this.$element
	        .find('.modal-content')
	        .load(this.options.remote, $.proxy(function () {
	          this.$element.trigger('loaded.bs.modal')
	        }, this))
	    }
	  }
	
	  Modal.VERSION  = '3.3.7'
	
	  Modal.TRANSITION_DURATION = 300
	  Modal.BACKDROP_TRANSITION_DURATION = 150
	
	  Modal.DEFAULTS = {
	    backdrop: true,
	    keyboard: true,
	    show: true
	  }
	
	  Modal.prototype.toggle = function (_relatedTarget) {
	    return this.isShown ? this.hide() : this.show(_relatedTarget)
	  }
	
	  Modal.prototype.show = function (_relatedTarget) {
	    var that = this
	    var e    = $.Event('show.bs.modal', { relatedTarget: _relatedTarget })
	
	    this.$element.trigger(e)
	
	    if (this.isShown || e.isDefaultPrevented()) return
	
	    this.isShown = true
	
	    this.checkScrollbar()
	    this.setScrollbar()
	    this.$body.addClass('modal-open')
	
	    this.escape()
	    this.resize()
	
	    this.$element.on('click.dismiss.bs.modal', '[data-dismiss="modal"]', $.proxy(this.hide, this))
	
	    this.$dialog.on('mousedown.dismiss.bs.modal', function () {
	      that.$element.one('mouseup.dismiss.bs.modal', function (e) {
	        if ($(e.target).is(that.$element)) that.ignoreBackdropClick = true
	      })
	    })
	
	    this.backdrop(function () {
	      var transition = $.support.transition && that.$element.hasClass('fade')
	
	      if (!that.$element.parent().length) {
	        that.$element.appendTo(that.$body) // don't move modals dom position
	      }
	
	      that.$element
	        .show()
	        .scrollTop(0)
	
	      that.adjustDialog()
	
	      if (transition) {
	        that.$element[0].offsetWidth // force reflow
	      }
	
	      that.$element.addClass('in')
	
	      that.enforceFocus()
	
	      var e = $.Event('shown.bs.modal', { relatedTarget: _relatedTarget })
	
	      transition ?
	        that.$dialog // wait for modal to slide in
	          .one('bsTransitionEnd', function () {
	            that.$element.trigger('focus').trigger(e)
	          })
	          .emulateTransitionEnd(Modal.TRANSITION_DURATION) :
	        that.$element.trigger('focus').trigger(e)
	    })
	  }
	
	  Modal.prototype.hide = function (e) {
	    if (e) e.preventDefault()
	
	    e = $.Event('hide.bs.modal')
	
	    this.$element.trigger(e)
	
	    if (!this.isShown || e.isDefaultPrevented()) return
	
	    this.isShown = false
	
	    this.escape()
	    this.resize()
	
	    $(document).off('focusin.bs.modal')
	
	    this.$element
	      .removeClass('in')
	      .off('click.dismiss.bs.modal')
	      .off('mouseup.dismiss.bs.modal')
	
	    this.$dialog.off('mousedown.dismiss.bs.modal')
	
	    $.support.transition && this.$element.hasClass('fade') ?
	      this.$element
	        .one('bsTransitionEnd', $.proxy(this.hideModal, this))
	        .emulateTransitionEnd(Modal.TRANSITION_DURATION) :
	      this.hideModal()
	  }
	
	  Modal.prototype.enforceFocus = function () {
	    $(document)
	      .off('focusin.bs.modal') // guard against infinite focus loop
	      .on('focusin.bs.modal', $.proxy(function (e) {
	        if (document !== e.target &&
	            this.$element[0] !== e.target &&
	            !this.$element.has(e.target).length) {
	          this.$element.trigger('focus')
	        }
	      }, this))
	  }
	
	  Modal.prototype.escape = function () {
	    if (this.isShown && this.options.keyboard) {
	      this.$element.on('keydown.dismiss.bs.modal', $.proxy(function (e) {
	        e.which == 27 && this.hide()
	      }, this))
	    } else if (!this.isShown) {
	      this.$element.off('keydown.dismiss.bs.modal')
	    }
	  }
	
	  Modal.prototype.resize = function () {
	    if (this.isShown) {
	      $(window).on('resize.bs.modal', $.proxy(this.handleUpdate, this))
	    } else {
	      $(window).off('resize.bs.modal')
	    }
	  }
	
	  Modal.prototype.hideModal = function () {
	    var that = this
	    this.$element.hide()
	    this.backdrop(function () {
	      that.$body.removeClass('modal-open')
	      that.resetAdjustments()
	      that.resetScrollbar()
	      that.$element.trigger('hidden.bs.modal')
	    })
	  }
	
	  Modal.prototype.removeBackdrop = function () {
	    this.$backdrop && this.$backdrop.remove()
	    this.$backdrop = null
	  }
	
	  Modal.prototype.backdrop = function (callback) {
	    var that = this
	    var animate = this.$element.hasClass('fade') ? 'fade' : ''
	
	    if (this.isShown && this.options.backdrop) {
	      var doAnimate = $.support.transition && animate
	
	      this.$backdrop = $(document.createElement('div'))
	        .addClass('modal-backdrop ' + animate)
	        .appendTo(this.$body)
	
	      this.$element.on('click.dismiss.bs.modal', $.proxy(function (e) {
	        if (this.ignoreBackdropClick) {
	          this.ignoreBackdropClick = false
	          return
	        }
	        if (e.target !== e.currentTarget) return
	        this.options.backdrop == 'static'
	          ? this.$element[0].focus()
	          : this.hide()
	      }, this))
	
	      if (doAnimate) this.$backdrop[0].offsetWidth // force reflow
	
	      this.$backdrop.addClass('in')
	
	      if (!callback) return
	
	      doAnimate ?
	        this.$backdrop
	          .one('bsTransitionEnd', callback)
	          .emulateTransitionEnd(Modal.BACKDROP_TRANSITION_DURATION) :
	        callback()
	
	    } else if (!this.isShown && this.$backdrop) {
	      this.$backdrop.removeClass('in')
	
	      var callbackRemove = function () {
	        that.removeBackdrop()
	        callback && callback()
	      }
	      $.support.transition && this.$element.hasClass('fade') ?
	        this.$backdrop
	          .one('bsTransitionEnd', callbackRemove)
	          .emulateTransitionEnd(Modal.BACKDROP_TRANSITION_DURATION) :
	        callbackRemove()
	
	    } else if (callback) {
	      callback()
	    }
	  }
	
	  // these following methods are used to handle overflowing modals
	
	  Modal.prototype.handleUpdate = function () {
	    this.adjustDialog()
	  }
	
	  Modal.prototype.adjustDialog = function () {
	    var modalIsOverflowing = this.$element[0].scrollHeight > document.documentElement.clientHeight
	
	    this.$element.css({
	      paddingLeft:  !this.bodyIsOverflowing && modalIsOverflowing ? this.scrollbarWidth : '',
	      paddingRight: this.bodyIsOverflowing && !modalIsOverflowing ? this.scrollbarWidth : ''
	    })
	  }
	
	  Modal.prototype.resetAdjustments = function () {
	    this.$element.css({
	      paddingLeft: '',
	      paddingRight: ''
	    })
	  }
	
	  Modal.prototype.checkScrollbar = function () {
	    var fullWindowWidth = window.innerWidth
	    if (!fullWindowWidth) { // workaround for missing window.innerWidth in IE8
	      var documentElementRect = document.documentElement.getBoundingClientRect()
	      fullWindowWidth = documentElementRect.right - Math.abs(documentElementRect.left)
	    }
	    this.bodyIsOverflowing = document.body.clientWidth < fullWindowWidth
	    this.scrollbarWidth = this.measureScrollbar()
	  }
	
	  Modal.prototype.setScrollbar = function () {
	    var bodyPad = parseInt((this.$body.css('padding-right') || 0), 10)
	    this.originalBodyPad = document.body.style.paddingRight || ''
	    if (this.bodyIsOverflowing) this.$body.css('padding-right', bodyPad + this.scrollbarWidth)
	  }
	
	  Modal.prototype.resetScrollbar = function () {
	    this.$body.css('padding-right', this.originalBodyPad)
	  }
	
	  Modal.prototype.measureScrollbar = function () { // thx walsh
	    var scrollDiv = document.createElement('div')
	    scrollDiv.className = 'modal-scrollbar-measure'
	    this.$body.append(scrollDiv)
	    var scrollbarWidth = scrollDiv.offsetWidth - scrollDiv.clientWidth
	    this.$body[0].removeChild(scrollDiv)
	    return scrollbarWidth
	  }
	
	
	  // MODAL PLUGIN DEFINITION
	  // =======================
	
	  function Plugin(option, _relatedTarget) {
	    return this.each(function () {
	      var $this   = $(this)
	      var data    = $this.data('bs.modal')
	      var options = $.extend({}, Modal.DEFAULTS, $this.data(), typeof option == 'object' && option)
	
	      if (!data) $this.data('bs.modal', (data = new Modal(this, options)))
	      if (typeof option == 'string') data[option](_relatedTarget)
	      else if (options.show) data.show(_relatedTarget)
	    })
	  }
	
	  var old = $.fn.modal
	
	  $.fn.modal             = Plugin
	  $.fn.modal.Constructor = Modal
	
	
	  // MODAL NO CONFLICT
	  // =================
	
	  $.fn.modal.noConflict = function () {
	    $.fn.modal = old
	    return this
	  }
	
	
	  // MODAL DATA-API
	  // ==============
	
	  $(document).on('click.bs.modal.data-api', '[data-toggle="modal"]', function (e) {
	    var $this   = $(this)
	    var href    = $this.attr('href')
	    var $target = $($this.attr('data-target') || (href && href.replace(/.*(?=#[^\s]+$)/, ''))) // strip for ie7
	    var option  = $target.data('bs.modal') ? 'toggle' : $.extend({ remote: !/#/.test(href) && href }, $target.data(), $this.data())
	
	    if ($this.is('a')) e.preventDefault()
	
	    $target.one('show.bs.modal', function (showEvent) {
	      if (showEvent.isDefaultPrevented()) return // only register focus restorer if modal will actually get shown
	      $target.one('hidden.bs.modal', function () {
	        $this.is(':visible') && $this.trigger('focus')
	      })
	    })
	    Plugin.call($target, option, this)
	  })
	
	}(jQuery);
	
	/* ========================================================================
	 * Bootstrap: tooltip.js v3.3.7
	 * http://getbootstrap.com/javascript/#tooltip
	 * Inspired by the original jQuery.tipsy by Jason Frame
	 * ========================================================================
	 * Copyright 2011-2016 Twitter, Inc.
	 * Licensed under MIT (https://github.com/twbs/bootstrap/blob/master/LICENSE)
	 * ======================================================================== */
	
	
	+function ($) {
	  'use strict';
	
	  // TOOLTIP PUBLIC CLASS DEFINITION
	  // ===============================
	
	  var Tooltip = function (element, options) {
	    this.type       = null
	    this.options    = null
	    this.enabled    = null
	    this.timeout    = null
	    this.hoverState = null
	    this.$element   = null
	    this.inState    = null
	
	    this.init('tooltip', element, options)
	  }
	
	  Tooltip.VERSION  = '3.3.7'
	
	  Tooltip.TRANSITION_DURATION = 150
	
	  Tooltip.DEFAULTS = {
	    animation: true,
	    placement: 'top',
	    selector: false,
	    template: '<div class="tooltip" role="tooltip"><div class="tooltip-arrow"></div><div class="tooltip-inner"></div></div>',
	    trigger: 'hover focus',
	    title: '',
	    delay: 0,
	    html: false,
	    container: false,
	    viewport: {
	      selector: 'body',
	      padding: 0
	    }
	  }
	
	  Tooltip.prototype.init = function (type, element, options) {
	    this.enabled   = true
	    this.type      = type
	    this.$element  = $(element)
	    this.options   = this.getOptions(options)
	    this.$viewport = this.options.viewport && $($.isFunction(this.options.viewport) ? this.options.viewport.call(this, this.$element) : (this.options.viewport.selector || this.options.viewport))
	    this.inState   = { click: false, hover: false, focus: false }
	
	    if (this.$element[0] instanceof document.constructor && !this.options.selector) {
	      throw new Error('`selector` option must be specified when initializing ' + this.type + ' on the window.document object!')
	    }
	
	    var triggers = this.options.trigger.split(' ')
	
	    for (var i = triggers.length; i--;) {
	      var trigger = triggers[i]
	
	      if (trigger == 'click') {
	        this.$element.on('click.' + this.type, this.options.selector, $.proxy(this.toggle, this))
	      } else if (trigger != 'manual') {
	        var eventIn  = trigger == 'hover' ? 'mouseenter' : 'focusin'
	        var eventOut = trigger == 'hover' ? 'mouseleave' : 'focusout'
	
	        this.$element.on(eventIn  + '.' + this.type, this.options.selector, $.proxy(this.enter, this))
	        this.$element.on(eventOut + '.' + this.type, this.options.selector, $.proxy(this.leave, this))
	      }
	    }
	
	    this.options.selector ?
	      (this._options = $.extend({}, this.options, { trigger: 'manual', selector: '' })) :
	      this.fixTitle()
	  }
	
	  Tooltip.prototype.getDefaults = function () {
	    return Tooltip.DEFAULTS
	  }
	
	  Tooltip.prototype.getOptions = function (options) {
	    options = $.extend({}, this.getDefaults(), this.$element.data(), options)
	
	    if (options.delay && typeof options.delay == 'number') {
	      options.delay = {
	        show: options.delay,
	        hide: options.delay
	      }
	    }
	
	    return options
	  }
	
	  Tooltip.prototype.getDelegateOptions = function () {
	    var options  = {}
	    var defaults = this.getDefaults()
	
	    this._options && $.each(this._options, function (key, value) {
	      if (defaults[key] != value) options[key] = value
	    })
	
	    return options
	  }
	
	  Tooltip.prototype.enter = function (obj) {
	    var self = obj instanceof this.constructor ?
	      obj : $(obj.currentTarget).data('bs.' + this.type)
	
	    if (!self) {
	      self = new this.constructor(obj.currentTarget, this.getDelegateOptions())
	      $(obj.currentTarget).data('bs.' + this.type, self)
	    }
	
	    if (obj instanceof $.Event) {
	      self.inState[obj.type == 'focusin' ? 'focus' : 'hover'] = true
	    }
	
	    if (self.tip().hasClass('in') || self.hoverState == 'in') {
	      self.hoverState = 'in'
	      return
	    }
	
	    clearTimeout(self.timeout)
	
	    self.hoverState = 'in'
	
	    if (!self.options.delay || !self.options.delay.show) return self.show()
	
	    self.timeout = setTimeout(function () {
	      if (self.hoverState == 'in') self.show()
	    }, self.options.delay.show)
	  }
	
	  Tooltip.prototype.isInStateTrue = function () {
	    for (var key in this.inState) {
	      if (this.inState[key]) return true
	    }
	
	    return false
	  }
	
	  Tooltip.prototype.leave = function (obj) {
	    var self = obj instanceof this.constructor ?
	      obj : $(obj.currentTarget).data('bs.' + this.type)
	
	    if (!self) {
	      self = new this.constructor(obj.currentTarget, this.getDelegateOptions())
	      $(obj.currentTarget).data('bs.' + this.type, self)
	    }
	
	    if (obj instanceof $.Event) {
	      self.inState[obj.type == 'focusout' ? 'focus' : 'hover'] = false
	    }
	
	    if (self.isInStateTrue()) return
	
	    clearTimeout(self.timeout)
	
	    self.hoverState = 'out'
	
	    if (!self.options.delay || !self.options.delay.hide) return self.hide()
	
	    self.timeout = setTimeout(function () {
	      if (self.hoverState == 'out') self.hide()
	    }, self.options.delay.hide)
	  }
	
	  Tooltip.prototype.show = function () {
	    var e = $.Event('show.bs.' + this.type)
	
	    if (this.hasContent() && this.enabled) {
	      this.$element.trigger(e)
	
	      var inDom = $.contains(this.$element[0].ownerDocument.documentElement, this.$element[0])
	      if (e.isDefaultPrevented() || !inDom) return
	      var that = this
	
	      var $tip = this.tip()
	
	      var tipId = this.getUID(this.type)
	
	      this.setContent()
	      $tip.attr('id', tipId)
	      this.$element.attr('aria-describedby', tipId)
	
	      if (this.options.animation) $tip.addClass('fade')
	
	      var placement = typeof this.options.placement == 'function' ?
	        this.options.placement.call(this, $tip[0], this.$element[0]) :
	        this.options.placement
	
	      var autoToken = /\s?auto?\s?/i
	      var autoPlace = autoToken.test(placement)
	      if (autoPlace) placement = placement.replace(autoToken, '') || 'top'
	
	      $tip
	        .detach()
	        .css({ top: 0, left: 0, display: 'block' })
	        .addClass(placement)
	        .data('bs.' + this.type, this)
	
	      this.options.container ? $tip.appendTo(this.options.container) : $tip.insertAfter(this.$element)
	      this.$element.trigger('inserted.bs.' + this.type)
	
	      var pos          = this.getPosition()
	      var actualWidth  = $tip[0].offsetWidth
	      var actualHeight = $tip[0].offsetHeight
	
	      if (autoPlace) {
	        var orgPlacement = placement
	        var viewportDim = this.getPosition(this.$viewport)
	
	        placement = placement == 'bottom' && pos.bottom + actualHeight > viewportDim.bottom ? 'top'    :
	                    placement == 'top'    && pos.top    - actualHeight < viewportDim.top    ? 'bottom' :
	                    placement == 'right'  && pos.right  + actualWidth  > viewportDim.width  ? 'left'   :
	                    placement == 'left'   && pos.left   - actualWidth  < viewportDim.left   ? 'right'  :
	                    placement
	
	        $tip
	          .removeClass(orgPlacement)
	          .addClass(placement)
	      }
	
	      var calculatedOffset = this.getCalculatedOffset(placement, pos, actualWidth, actualHeight)
	
	      this.applyPlacement(calculatedOffset, placement)
	
	      var complete = function () {
	        var prevHoverState = that.hoverState
	        that.$element.trigger('shown.bs.' + that.type)
	        that.hoverState = null
	
	        if (prevHoverState == 'out') that.leave(that)
	      }
	
	      $.support.transition && this.$tip.hasClass('fade') ?
	        $tip
	          .one('bsTransitionEnd', complete)
	          .emulateTransitionEnd(Tooltip.TRANSITION_DURATION) :
	        complete()
	    }
	  }
	
	  Tooltip.prototype.applyPlacement = function (offset, placement) {
	    var $tip   = this.tip()
	    var width  = $tip[0].offsetWidth
	    var height = $tip[0].offsetHeight
	
	    // manually read margins because getBoundingClientRect includes difference
	    var marginTop = parseInt($tip.css('margin-top'), 10)
	    var marginLeft = parseInt($tip.css('margin-left'), 10)
	
	    // we must check for NaN for ie 8/9
	    if (isNaN(marginTop))  marginTop  = 0
	    if (isNaN(marginLeft)) marginLeft = 0
	
	    offset.top  += marginTop
	    offset.left += marginLeft
	
	    // $.fn.offset doesn't round pixel values
	    // so we use setOffset directly with our own function B-0
	    $.offset.setOffset($tip[0], $.extend({
	      using: function (props) {
	        $tip.css({
	          top: Math.round(props.top),
	          left: Math.round(props.left)
	        })
	      }
	    }, offset), 0)
	
	    $tip.addClass('in')
	
	    // check to see if placing tip in new offset caused the tip to resize itself
	    var actualWidth  = $tip[0].offsetWidth
	    var actualHeight = $tip[0].offsetHeight
	
	    if (placement == 'top' && actualHeight != height) {
	      offset.top = offset.top + height - actualHeight
	    }
	
	    var delta = this.getViewportAdjustedDelta(placement, offset, actualWidth, actualHeight)
	
	    if (delta.left) offset.left += delta.left
	    else offset.top += delta.top
	
	    var isVertical          = /top|bottom/.test(placement)
	    var arrowDelta          = isVertical ? delta.left * 2 - width + actualWidth : delta.top * 2 - height + actualHeight
	    var arrowOffsetPosition = isVertical ? 'offsetWidth' : 'offsetHeight'
	
	    $tip.offset(offset)
	    this.replaceArrow(arrowDelta, $tip[0][arrowOffsetPosition], isVertical)
	  }
	
	  Tooltip.prototype.replaceArrow = function (delta, dimension, isVertical) {
	    this.arrow()
	      .css(isVertical ? 'left' : 'top', 50 * (1 - delta / dimension) + '%')
	      .css(isVertical ? 'top' : 'left', '')
	  }
	
	  Tooltip.prototype.setContent = function () {
	    var $tip  = this.tip()
	    var title = this.getTitle()
	
	    $tip.find('.tooltip-inner')[this.options.html ? 'html' : 'text'](title)
	    $tip.removeClass('fade in top bottom left right')
	  }
	
	  Tooltip.prototype.hide = function (callback) {
	    var that = this
	    var $tip = $(this.$tip)
	    var e    = $.Event('hide.bs.' + this.type)
	
	    function complete() {
	      if (that.hoverState != 'in') $tip.detach()
	      if (that.$element) { // TODO: Check whether guarding this code with this `if` is really necessary.
	        that.$element
	          .removeAttr('aria-describedby')
	          .trigger('hidden.bs.' + that.type)
	      }
	      callback && callback()
	    }
	
	    this.$element.trigger(e)
	
	    if (e.isDefaultPrevented()) return
	
	    $tip.removeClass('in')
	
	    $.support.transition && $tip.hasClass('fade') ?
	      $tip
	        .one('bsTransitionEnd', complete)
	        .emulateTransitionEnd(Tooltip.TRANSITION_DURATION) :
	      complete()
	
	    this.hoverState = null
	
	    return this
	  }
	
	  Tooltip.prototype.fixTitle = function () {
	    var $e = this.$element
	    if ($e.attr('title') || typeof $e.attr('data-original-title') != 'string') {
	      $e.attr('data-original-title', $e.attr('title') || '').attr('title', '')
	    }
	  }
	
	  Tooltip.prototype.hasContent = function () {
	    return this.getTitle()
	  }
	
	  Tooltip.prototype.getPosition = function ($element) {
	    $element   = $element || this.$element
	
	    var el     = $element[0]
	    var isBody = el.tagName == 'BODY'
	
	    var elRect    = el.getBoundingClientRect()
	    if (elRect.width == null) {
	      // width and height are missing in IE8, so compute them manually; see https://github.com/twbs/bootstrap/issues/14093
	      elRect = $.extend({}, elRect, { width: elRect.right - elRect.left, height: elRect.bottom - elRect.top })
	    }
	    var isSvg = window.SVGElement && el instanceof window.SVGElement
	    // Avoid using $.offset() on SVGs since it gives incorrect results in jQuery 3.
	    // See https://github.com/twbs/bootstrap/issues/20280
	    var elOffset  = isBody ? { top: 0, left: 0 } : (isSvg ? null : $element.offset())
	    var scroll    = { scroll: isBody ? document.documentElement.scrollTop || document.body.scrollTop : $element.scrollTop() }
	    var outerDims = isBody ? { width: $(window).width(), height: $(window).height() } : null
	
	    return $.extend({}, elRect, scroll, outerDims, elOffset)
	  }
	
	  Tooltip.prototype.getCalculatedOffset = function (placement, pos, actualWidth, actualHeight) {
	    return placement == 'bottom' ? { top: pos.top + pos.height,   left: pos.left + pos.width / 2 - actualWidth / 2 } :
	           placement == 'top'    ? { top: pos.top - actualHeight, left: pos.left + pos.width / 2 - actualWidth / 2 } :
	           placement == 'left'   ? { top: pos.top + pos.height / 2 - actualHeight / 2, left: pos.left - actualWidth } :
	        /* placement == 'right' */ { top: pos.top + pos.height / 2 - actualHeight / 2, left: pos.left + pos.width }
	
	  }
	
	  Tooltip.prototype.getViewportAdjustedDelta = function (placement, pos, actualWidth, actualHeight) {
	    var delta = { top: 0, left: 0 }
	    if (!this.$viewport) return delta
	
	    var viewportPadding = this.options.viewport && this.options.viewport.padding || 0
	    var viewportDimensions = this.getPosition(this.$viewport)
	
	    if (/right|left/.test(placement)) {
	      var topEdgeOffset    = pos.top - viewportPadding - viewportDimensions.scroll
	      var bottomEdgeOffset = pos.top + viewportPadding - viewportDimensions.scroll + actualHeight
	      if (topEdgeOffset < viewportDimensions.top) { // top overflow
	        delta.top = viewportDimensions.top - topEdgeOffset
	      } else if (bottomEdgeOffset > viewportDimensions.top + viewportDimensions.height) { // bottom overflow
	        delta.top = viewportDimensions.top + viewportDimensions.height - bottomEdgeOffset
	      }
	    } else {
	      var leftEdgeOffset  = pos.left - viewportPadding
	      var rightEdgeOffset = pos.left + viewportPadding + actualWidth
	      if (leftEdgeOffset < viewportDimensions.left) { // left overflow
	        delta.left = viewportDimensions.left - leftEdgeOffset
	      } else if (rightEdgeOffset > viewportDimensions.right) { // right overflow
	        delta.left = viewportDimensions.left + viewportDimensions.width - rightEdgeOffset
	      }
	    }
	
	    return delta
	  }
	
	  Tooltip.prototype.getTitle = function () {
	    var title
	    var $e = this.$element
	    var o  = this.options
	
	    title = $e.attr('data-original-title')
	      || (typeof o.title == 'function' ? o.title.call($e[0]) :  o.title)
	
	    return title
	  }
	
	  Tooltip.prototype.getUID = function (prefix) {
	    do prefix += ~~(Math.random() * 1000000)
	    while (document.getElementById(prefix))
	    return prefix
	  }
	
	  Tooltip.prototype.tip = function () {
	    if (!this.$tip) {
	      this.$tip = $(this.options.template)
	      if (this.$tip.length != 1) {
	        throw new Error(this.type + ' `template` option must consist of exactly 1 top-level element!')
	      }
	    }
	    return this.$tip
	  }
	
	  Tooltip.prototype.arrow = function () {
	    return (this.$arrow = this.$arrow || this.tip().find('.tooltip-arrow'))
	  }
	
	  Tooltip.prototype.enable = function () {
	    this.enabled = true
	  }
	
	  Tooltip.prototype.disable = function () {
	    this.enabled = false
	  }
	
	  Tooltip.prototype.toggleEnabled = function () {
	    this.enabled = !this.enabled
	  }
	
	  Tooltip.prototype.toggle = function (e) {
	    var self = this
	    if (e) {
	      self = $(e.currentTarget).data('bs.' + this.type)
	      if (!self) {
	        self = new this.constructor(e.currentTarget, this.getDelegateOptions())
	        $(e.currentTarget).data('bs.' + this.type, self)
	      }
	    }
	
	    if (e) {
	      self.inState.click = !self.inState.click
	      if (self.isInStateTrue()) self.enter(self)
	      else self.leave(self)
	    } else {
	      self.tip().hasClass('in') ? self.leave(self) : self.enter(self)
	    }
	  }
	
	  Tooltip.prototype.destroy = function () {
	    var that = this
	    clearTimeout(this.timeout)
	    this.hide(function () {
	      that.$element.off('.' + that.type).removeData('bs.' + that.type)
	      if (that.$tip) {
	        that.$tip.detach()
	      }
	      that.$tip = null
	      that.$arrow = null
	      that.$viewport = null
	      that.$element = null
	    })
	  }
	
	
	  // TOOLTIP PLUGIN DEFINITION
	  // =========================
	
	  function Plugin(option) {
	    return this.each(function () {
	      var $this   = $(this)
	      var data    = $this.data('bs.tooltip')
	      var options = typeof option == 'object' && option
	
	      if (!data && /destroy|hide/.test(option)) return
	      if (!data) $this.data('bs.tooltip', (data = new Tooltip(this, options)))
	      if (typeof option == 'string') data[option]()
	    })
	  }
	
	  var old = $.fn.tooltip
	
	  $.fn.tooltip             = Plugin
	  $.fn.tooltip.Constructor = Tooltip
	
	
	  // TOOLTIP NO CONFLICT
	  // ===================
	
	  $.fn.tooltip.noConflict = function () {
	    $.fn.tooltip = old
	    return this
	  }
	
	}(jQuery);
	
	/* ========================================================================
	 * Bootstrap: popover.js v3.3.7
	 * http://getbootstrap.com/javascript/#popovers
	 * ========================================================================
	 * Copyright 2011-2016 Twitter, Inc.
	 * Licensed under MIT (https://github.com/twbs/bootstrap/blob/master/LICENSE)
	 * ======================================================================== */
	
	
	+function ($) {
	  'use strict';
	
	  // POPOVER PUBLIC CLASS DEFINITION
	  // ===============================
	
	  var Popover = function (element, options) {
	    this.init('popover', element, options)
	  }
	
	  if (!$.fn.tooltip) throw new Error('Popover requires tooltip.js')
	
	  Popover.VERSION  = '3.3.7'
	
	  Popover.DEFAULTS = $.extend({}, $.fn.tooltip.Constructor.DEFAULTS, {
	    placement: 'right',
	    trigger: 'click',
	    content: '',
	    template: '<div class="popover" role="tooltip"><div class="arrow"></div><h3 class="popover-title"></h3><div class="popover-content"></div></div>'
	  })
	
	
	  // NOTE: POPOVER EXTENDS tooltip.js
	  // ================================
	
	  Popover.prototype = $.extend({}, $.fn.tooltip.Constructor.prototype)
	
	  Popover.prototype.constructor = Popover
	
	  Popover.prototype.getDefaults = function () {
	    return Popover.DEFAULTS
	  }
	
	  Popover.prototype.setContent = function () {
	    var $tip    = this.tip()
	    var title   = this.getTitle()
	    var content = this.getContent()
	
	    $tip.find('.popover-title')[this.options.html ? 'html' : 'text'](title)
	    $tip.find('.popover-content').children().detach().end()[ // we use append for html objects to maintain js events
	      this.options.html ? (typeof content == 'string' ? 'html' : 'append') : 'text'
	    ](content)
	
	    $tip.removeClass('fade top bottom left right in')
	
	    // IE8 doesn't accept hiding via the `:empty` pseudo selector, we have to do
	    // this manually by checking the contents.
	    if (!$tip.find('.popover-title').html()) $tip.find('.popover-title').hide()
	  }
	
	  Popover.prototype.hasContent = function () {
	    return this.getTitle() || this.getContent()
	  }
	
	  Popover.prototype.getContent = function () {
	    var $e = this.$element
	    var o  = this.options
	
	    return $e.attr('data-content')
	      || (typeof o.content == 'function' ?
	            o.content.call($e[0]) :
	            o.content)
	  }
	
	  Popover.prototype.arrow = function () {
	    return (this.$arrow = this.$arrow || this.tip().find('.arrow'))
	  }
	
	
	  // POPOVER PLUGIN DEFINITION
	  // =========================
	
	  function Plugin(option) {
	    return this.each(function () {
	      var $this   = $(this)
	      var data    = $this.data('bs.popover')
	      var options = typeof option == 'object' && option
	
	      if (!data && /destroy|hide/.test(option)) return
	      if (!data) $this.data('bs.popover', (data = new Popover(this, options)))
	      if (typeof option == 'string') data[option]()
	    })
	  }
	
	  var old = $.fn.popover
	
	  $.fn.popover             = Plugin
	  $.fn.popover.Constructor = Popover
	
	
	  // POPOVER NO CONFLICT
	  // ===================
	
	  $.fn.popover.noConflict = function () {
	    $.fn.popover = old
	    return this
	  }
	
	}(jQuery);
	
	/* ========================================================================
	 * Bootstrap: scrollspy.js v3.3.7
	 * http://getbootstrap.com/javascript/#scrollspy
	 * ========================================================================
	 * Copyright 2011-2016 Twitter, Inc.
	 * Licensed under MIT (https://github.com/twbs/bootstrap/blob/master/LICENSE)
	 * ======================================================================== */
	
	
	+function ($) {
	  'use strict';
	
	  // SCROLLSPY CLASS DEFINITION
	  // ==========================
	
	  function ScrollSpy(element, options) {
	    this.$body          = $(document.body)
	    this.$scrollElement = $(element).is(document.body) ? $(window) : $(element)
	    this.options        = $.extend({}, ScrollSpy.DEFAULTS, options)
	    this.selector       = (this.options.target || '') + ' .nav li > a'
	    this.offsets        = []
	    this.targets        = []
	    this.activeTarget   = null
	    this.scrollHeight   = 0
	
	    this.$scrollElement.on('scroll.bs.scrollspy', $.proxy(this.process, this))
	    this.refresh()
	    this.process()
	  }
	
	  ScrollSpy.VERSION  = '3.3.7'
	
	  ScrollSpy.DEFAULTS = {
	    offset: 10
	  }
	
	  ScrollSpy.prototype.getScrollHeight = function () {
	    return this.$scrollElement[0].scrollHeight || Math.max(this.$body[0].scrollHeight, document.documentElement.scrollHeight)
	  }
	
	  ScrollSpy.prototype.refresh = function () {
	    var that          = this
	    var offsetMethod  = 'offset'
	    var offsetBase    = 0
	
	    this.offsets      = []
	    this.targets      = []
	    this.scrollHeight = this.getScrollHeight()
	
	    if (!$.isWindow(this.$scrollElement[0])) {
	      offsetMethod = 'position'
	      offsetBase   = this.$scrollElement.scrollTop()
	    }
	
	    this.$body
	      .find(this.selector)
	      .map(function () {
	        var $el   = $(this)
	        var href  = $el.data('target') || $el.attr('href')
	        var $href = /^#./.test(href) && $(href)
	
	        return ($href
	          && $href.length
	          && $href.is(':visible')
	          && [[$href[offsetMethod]().top + offsetBase, href]]) || null
	      })
	      .sort(function (a, b) { return a[0] - b[0] })
	      .each(function () {
	        that.offsets.push(this[0])
	        that.targets.push(this[1])
	      })
	  }
	
	  ScrollSpy.prototype.process = function () {
	    var scrollTop    = this.$scrollElement.scrollTop() + this.options.offset
	    var scrollHeight = this.getScrollHeight()
	    var maxScroll    = this.options.offset + scrollHeight - this.$scrollElement.height()
	    var offsets      = this.offsets
	    var targets      = this.targets
	    var activeTarget = this.activeTarget
	    var i
	
	    if (this.scrollHeight != scrollHeight) {
	      this.refresh()
	    }
	
	    if (scrollTop >= maxScroll) {
	      return activeTarget != (i = targets[targets.length - 1]) && this.activate(i)
	    }
	
	    if (activeTarget && scrollTop < offsets[0]) {
	      this.activeTarget = null
	      return this.clear()
	    }
	
	    for (i = offsets.length; i--;) {
	      activeTarget != targets[i]
	        && scrollTop >= offsets[i]
	        && (offsets[i + 1] === undefined || scrollTop < offsets[i + 1])
	        && this.activate(targets[i])
	    }
	  }
	
	  ScrollSpy.prototype.activate = function (target) {
	    this.activeTarget = target
	
	    this.clear()
	
	    var selector = this.selector +
	      '[data-target="' + target + '"],' +
	      this.selector + '[href="' + target + '"]'
	
	    var active = $(selector)
	      .parents('li')
	      .addClass('active')
	
	    if (active.parent('.dropdown-menu').length) {
	      active = active
	        .closest('li.dropdown')
	        .addClass('active')
	    }
	
	    active.trigger('activate.bs.scrollspy')
	  }
	
	  ScrollSpy.prototype.clear = function () {
	    $(this.selector)
	      .parentsUntil(this.options.target, '.active')
	      .removeClass('active')
	  }
	
	
	  // SCROLLSPY PLUGIN DEFINITION
	  // ===========================
	
	  function Plugin(option) {
	    return this.each(function () {
	      var $this   = $(this)
	      var data    = $this.data('bs.scrollspy')
	      var options = typeof option == 'object' && option
	
	      if (!data) $this.data('bs.scrollspy', (data = new ScrollSpy(this, options)))
	      if (typeof option == 'string') data[option]()
	    })
	  }
	
	  var old = $.fn.scrollspy
	
	  $.fn.scrollspy             = Plugin
	  $.fn.scrollspy.Constructor = ScrollSpy
	
	
	  // SCROLLSPY NO CONFLICT
	  // =====================
	
	  $.fn.scrollspy.noConflict = function () {
	    $.fn.scrollspy = old
	    return this
	  }
	
	
	  // SCROLLSPY DATA-API
	  // ==================
	
	  $(window).on('load.bs.scrollspy.data-api', function () {
	    $('[data-spy="scroll"]').each(function () {
	      var $spy = $(this)
	      Plugin.call($spy, $spy.data())
	    })
	  })
	
	}(jQuery);
	
	/* ========================================================================
	 * Bootstrap: tab.js v3.3.7
	 * http://getbootstrap.com/javascript/#tabs
	 * ========================================================================
	 * Copyright 2011-2016 Twitter, Inc.
	 * Licensed under MIT (https://github.com/twbs/bootstrap/blob/master/LICENSE)
	 * ======================================================================== */
	
	
	+function ($) {
	  'use strict';
	
	  // TAB CLASS DEFINITION
	  // ====================
	
	  var Tab = function (element) {
	    // jscs:disable requireDollarBeforejQueryAssignment
	    this.element = $(element)
	    // jscs:enable requireDollarBeforejQueryAssignment
	  }
	
	  Tab.VERSION = '3.3.7'
	
	  Tab.TRANSITION_DURATION = 150
	
	  Tab.prototype.show = function () {
	    var $this    = this.element
	    var $ul      = $this.closest('ul:not(.dropdown-menu)')
	    var selector = $this.data('target')
	
	    if (!selector) {
	      selector = $this.attr('href')
	      selector = selector && selector.replace(/.*(?=#[^\s]*$)/, '') // strip for ie7
	    }
	
	    if ($this.parent('li').hasClass('active')) return
	
	    var $previous = $ul.find('.active:last a')
	    var hideEvent = $.Event('hide.bs.tab', {
	      relatedTarget: $this[0]
	    })
	    var showEvent = $.Event('show.bs.tab', {
	      relatedTarget: $previous[0]
	    })
	
	    $previous.trigger(hideEvent)
	    $this.trigger(showEvent)
	
	    if (showEvent.isDefaultPrevented() || hideEvent.isDefaultPrevented()) return
	
	    var $target = $(selector)
	
	    this.activate($this.closest('li'), $ul)
	    this.activate($target, $target.parent(), function () {
	      $previous.trigger({
	        type: 'hidden.bs.tab',
	        relatedTarget: $this[0]
	      })
	      $this.trigger({
	        type: 'shown.bs.tab',
	        relatedTarget: $previous[0]
	      })
	    })
	  }
	
	  Tab.prototype.activate = function (element, container, callback) {
	    var $active    = container.find('> .active')
	    var transition = callback
	      && $.support.transition
	      && ($active.length && $active.hasClass('fade') || !!container.find('> .fade').length)
	
	    function next() {
	      $active
	        .removeClass('active')
	        .find('> .dropdown-menu > .active')
	          .removeClass('active')
	        .end()
	        .find('[data-toggle="tab"]')
	          .attr('aria-expanded', false)
	
	      element
	        .addClass('active')
	        .find('[data-toggle="tab"]')
	          .attr('aria-expanded', true)
	
	      if (transition) {
	        element[0].offsetWidth // reflow for transition
	        element.addClass('in')
	      } else {
	        element.removeClass('fade')
	      }
	
	      if (element.parent('.dropdown-menu').length) {
	        element
	          .closest('li.dropdown')
	            .addClass('active')
	          .end()
	          .find('[data-toggle="tab"]')
	            .attr('aria-expanded', true)
	      }
	
	      callback && callback()
	    }
	
	    $active.length && transition ?
	      $active
	        .one('bsTransitionEnd', next)
	        .emulateTransitionEnd(Tab.TRANSITION_DURATION) :
	      next()
	
	    $active.removeClass('in')
	  }
	
	
	  // TAB PLUGIN DEFINITION
	  // =====================
	
	  function Plugin(option) {
	    return this.each(function () {
	      var $this = $(this)
	      var data  = $this.data('bs.tab')
	
	      if (!data) $this.data('bs.tab', (data = new Tab(this)))
	      if (typeof option == 'string') data[option]()
	    })
	  }
	
	  var old = $.fn.tab
	
	  $.fn.tab             = Plugin
	  $.fn.tab.Constructor = Tab
	
	
	  // TAB NO CONFLICT
	  // ===============
	
	  $.fn.tab.noConflict = function () {
	    $.fn.tab = old
	    return this
	  }
	
	
	  // TAB DATA-API
	  // ============
	
	  var clickHandler = function (e) {
	    e.preventDefault()
	    Plugin.call($(this), 'show')
	  }
	
	  $(document)
	    .on('click.bs.tab.data-api', '[data-toggle="tab"]', clickHandler)
	    .on('click.bs.tab.data-api', '[data-toggle="pill"]', clickHandler)
	
	}(jQuery);
	
	/* ========================================================================
	 * Bootstrap: affix.js v3.3.7
	 * http://getbootstrap.com/javascript/#affix
	 * ========================================================================
	 * Copyright 2011-2016 Twitter, Inc.
	 * Licensed under MIT (https://github.com/twbs/bootstrap/blob/master/LICENSE)
	 * ======================================================================== */
	
	
	+function ($) {
	  'use strict';
	
	  // AFFIX CLASS DEFINITION
	  // ======================
	
	  var Affix = function (element, options) {
	    this.options = $.extend({}, Affix.DEFAULTS, options)
	
	    this.$target = $(this.options.target)
	      .on('scroll.bs.affix.data-api', $.proxy(this.checkPosition, this))
	      .on('click.bs.affix.data-api',  $.proxy(this.checkPositionWithEventLoop, this))
	
	    this.$element     = $(element)
	    this.affixed      = null
	    this.unpin        = null
	    this.pinnedOffset = null
	
	    this.checkPosition()
	  }
	
	  Affix.VERSION  = '3.3.7'
	
	  Affix.RESET    = 'affix affix-top affix-bottom'
	
	  Affix.DEFAULTS = {
	    offset: 0,
	    target: window
	  }
	
	  Affix.prototype.getState = function (scrollHeight, height, offsetTop, offsetBottom) {
	    var scrollTop    = this.$target.scrollTop()
	    var position     = this.$element.offset()
	    var targetHeight = this.$target.height()
	
	    if (offsetTop != null && this.affixed == 'top') return scrollTop < offsetTop ? 'top' : false
	
	    if (this.affixed == 'bottom') {
	      if (offsetTop != null) return (scrollTop + this.unpin <= position.top) ? false : 'bottom'
	      return (scrollTop + targetHeight <= scrollHeight - offsetBottom) ? false : 'bottom'
	    }
	
	    var initializing   = this.affixed == null
	    var colliderTop    = initializing ? scrollTop : position.top
	    var colliderHeight = initializing ? targetHeight : height
	
	    if (offsetTop != null && scrollTop <= offsetTop) return 'top'
	    if (offsetBottom != null && (colliderTop + colliderHeight >= scrollHeight - offsetBottom)) return 'bottom'
	
	    return false
	  }
	
	  Affix.prototype.getPinnedOffset = function () {
	    if (this.pinnedOffset) return this.pinnedOffset
	    this.$element.removeClass(Affix.RESET).addClass('affix')
	    var scrollTop = this.$target.scrollTop()
	    var position  = this.$element.offset()
	    return (this.pinnedOffset = position.top - scrollTop)
	  }
	
	  Affix.prototype.checkPositionWithEventLoop = function () {
	    setTimeout($.proxy(this.checkPosition, this), 1)
	  }
	
	  Affix.prototype.checkPosition = function () {
	    if (!this.$element.is(':visible')) return
	
	    var height       = this.$element.height()
	    var offset       = this.options.offset
	    var offsetTop    = offset.top
	    var offsetBottom = offset.bottom
	    var scrollHeight = Math.max($(document).height(), $(document.body).height())
	
	    if (typeof offset != 'object')         offsetBottom = offsetTop = offset
	    if (typeof offsetTop == 'function')    offsetTop    = offset.top(this.$element)
	    if (typeof offsetBottom == 'function') offsetBottom = offset.bottom(this.$element)
	
	    var affix = this.getState(scrollHeight, height, offsetTop, offsetBottom)
	
	    if (this.affixed != affix) {
	      if (this.unpin != null) this.$element.css('top', '')
	
	      var affixType = 'affix' + (affix ? '-' + affix : '')
	      var e         = $.Event(affixType + '.bs.affix')
	
	      this.$element.trigger(e)
	
	      if (e.isDefaultPrevented()) return
	
	      this.affixed = affix
	      this.unpin = affix == 'bottom' ? this.getPinnedOffset() : null
	
	      this.$element
	        .removeClass(Affix.RESET)
	        .addClass(affixType)
	        .trigger(affixType.replace('affix', 'affixed') + '.bs.affix')
	    }
	
	    if (affix == 'bottom') {
	      this.$element.offset({
	        top: scrollHeight - height - offsetBottom
	      })
	    }
	  }
	
	
	  // AFFIX PLUGIN DEFINITION
	  // =======================
	
	  function Plugin(option) {
	    return this.each(function () {
	      var $this   = $(this)
	      var data    = $this.data('bs.affix')
	      var options = typeof option == 'object' && option
	
	      if (!data) $this.data('bs.affix', (data = new Affix(this, options)))
	      if (typeof option == 'string') data[option]()
	    })
	  }
	
	  var old = $.fn.affix
	
	  $.fn.affix             = Plugin
	  $.fn.affix.Constructor = Affix
	
	
	  // AFFIX NO CONFLICT
	  // =================
	
	  $.fn.affix.noConflict = function () {
	    $.fn.affix = old
	    return this
	  }
	
	
	  // AFFIX DATA-API
	  // ==============
	
	  $(window).on('load', function () {
	    $('[data-spy="affix"]').each(function () {
	      var $spy = $(this)
	      var data = $spy.data()
	
	      data.offset = data.offset || {}
	
	      if (data.offsetBottom != null) data.offset.bottom = data.offsetBottom
	      if (data.offsetTop    != null) data.offset.top    = data.offsetTop
	
	      Plugin.call($spy, data)
	    })
	  })
	
	}(jQuery);
	
	/* WEBPACK VAR INJECTION */}.call(exports, __webpack_require__(/*! jquery */ 2)))

/***/ },
/* 6 */
/*!********************************!*\
  !*** ./js/component/notify.js ***!
  \********************************/
/***/ function(module, exports, __webpack_require__) {

	var __WEBPACK_AMD_DEFINE_FACTORY__, __WEBPACK_AMD_DEFINE_ARRAY__, __WEBPACK_AMD_DEFINE_RESULT__;/* Notify.js - http://notifyjs.com/ Copyright (c) 2015 MIT */
	(function (factory) {
		// UMD start
		// https://github.com/umdjs/umd/blob/master/jqueryPluginCommonjs.js
		if (true) {
			// AMD. Register as an anonymous module.
			!(__WEBPACK_AMD_DEFINE_ARRAY__ = [__webpack_require__(/*! jquery */ 2)], __WEBPACK_AMD_DEFINE_FACTORY__ = (factory), __WEBPACK_AMD_DEFINE_RESULT__ = (typeof __WEBPACK_AMD_DEFINE_FACTORY__ === 'function' ? (__WEBPACK_AMD_DEFINE_FACTORY__.apply(exports, __WEBPACK_AMD_DEFINE_ARRAY__)) : __WEBPACK_AMD_DEFINE_FACTORY__), __WEBPACK_AMD_DEFINE_RESULT__ !== undefined && (module.exports = __WEBPACK_AMD_DEFINE_RESULT__));
		} else if (typeof module === 'object' && module.exports) {
			// Node/CommonJS
			module.exports = function( root, jQuery ) {
				if ( jQuery === undefined ) {
					// require('jQuery') returns a factory that requires window to
					// build a jQuery instance, we normalize how we use modules
					// that require this pattern but the window provided is a noop
					// if it's defined (how jquery works)
					if ( typeof window !== 'undefined' ) {
						jQuery = require('jquery');
					}
					else {
						jQuery = require('jquery')(root);
					}
				}
				factory(jQuery);
				return jQuery;
			};
		} else {
			// Browser globals
			factory(jQuery);
		}
	}(function ($) {
		//IE8 indexOf polyfill
		var indexOf = [].indexOf || function(item) {
			for (var i = 0, l = this.length; i < l; i++) {
				if (i in this && this[i] === item) {
					return i;
				}
			}
			return -1;
		};
	
		var pluginName = "notify";
		var pluginClassName = pluginName + "js";
		var blankFieldName = pluginName + "!blank";
	
		var positions = {
			t: "top",
			m: "middle",
			b: "bottom",
			l: "left",
			c: "center",
			r: "right"
		};
		var hAligns = ["l", "c", "r"];
		var vAligns = ["t", "m", "b"];
		var mainPositions = ["t", "b", "l", "r"];
		var opposites = {
			t: "b",
			m: null,
			b: "t",
			l: "r",
			c: null,
			r: "l"
		};
	
		var parsePosition = function(str) {
			var pos;
			pos = [];
			$.each(str.split(/\W+/), function(i, word) {
				var w;
				w = word.toLowerCase().charAt(0);
				if (positions[w]) {
					return pos.push(w);
				}
			});
			return pos;
		};
	
		var styles = {};
	
		var coreStyle = {
			name: "core",
			html: "<div class=\"" + pluginClassName + "-wrapper\">\n	<div class=\"" + pluginClassName + "-arrow\"></div>\n	<div class=\"" + pluginClassName + "-container\"></div>\n</div>",
			css: "." + pluginClassName + "-corner {\n	position: fixed;\n	margin: 5px;\n	z-index: 1050;\n}\n\n." + pluginClassName + "-corner ." + pluginClassName + "-wrapper,\n." + pluginClassName + "-corner ." + pluginClassName + "-container {\n	position: relative;\n	display: block;\n	height: inherit;\n	width: inherit;\n	margin: 3px;\n}\n\n." + pluginClassName + "-wrapper {\n	z-index: 1;\n	position: absolute;\n	display: inline-block;\n	height: 0;\n	width: 0;\n}\n\n." + pluginClassName + "-container {\n	display: none;\n	z-index: 1;\n	position: absolute;\n}\n\n." + pluginClassName + "-hidable {\n	cursor: pointer;\n}\n\n[data-notify-text],[data-notify-html] {\n	position: relative;\n}\n\n." + pluginClassName + "-arrow {\n	position: absolute;\n	z-index: 2;\n	width: 0;\n	height: 0;\n}"
		};
	
		var stylePrefixes = {
			"border-radius": ["-webkit-", "-moz-"]
		};
	
		var getStyle = function(name) {
			return styles[name];
		};
	
		var removeStyle = function(name) {
			if (!name) {
				throw "Missing Style name";
			}
			if (styles[name]) {
				delete styles[name];
			}
		};
	
		var addStyle = function(name, def) {
			if (!name) {
				throw "Missing Style name";
			}
			if (!def) {
				throw "Missing Style definition";
			}
			if (!def.html) {
				throw "Missing Style HTML";
			}
			//remove existing style
			var existing = styles[name];
			if (existing && existing.cssElem) {
				if (window.console) {
					console.warn(pluginName + ": overwriting style '" + name + "'");
				}
				styles[name].cssElem.remove();
			}
			def.name = name;
			styles[name] = def;
			var cssText = "";
			if (def.classes) {
				$.each(def.classes, function(className, props) {
					cssText += "." + pluginClassName + "-" + def.name + "-" + className + " {\n";
					$.each(props, function(name, val) {
						if (stylePrefixes[name]) {
							$.each(stylePrefixes[name], function(i, prefix) {
								return cssText += "	" + prefix + name + ": " + val + ";\n";
							});
						}
						return cssText += "	" + name + ": " + val + ";\n";
					});
					return cssText += "}\n";
				});
			}
			if (def.css) {
				cssText += "/* styles for " + def.name + " */\n" + def.css;
			}
			if (cssText) {
				def.cssElem = insertCSS(cssText);
				def.cssElem.attr("id", "notify-" + def.name);
			}
			var fields = {};
			var elem = $(def.html);
			findFields("html", elem, fields);
			findFields("text", elem, fields);
			def.fields = fields;
		};
	
		var insertCSS = function(cssText) {
			var e, elem, error;
			elem = createElem("style");
			elem.attr("type", 'text/css');
			$("head").append(elem);
			try {
				elem.html(cssText);
			} catch (_) {
				elem[0].styleSheet.cssText = cssText;
			}
			return elem;
		};
	
		var findFields = function(type, elem, fields) {
			var attr;
			if (type !== "html") {
				type = "text";
			}
			attr = "data-notify-" + type;
			return find(elem, "[" + attr + "]").each(function() {
				var name;
				name = $(this).attr(attr);
				if (!name) {
					name = blankFieldName;
				}
				fields[name] = type;
			});
		};
	
		var find = function(elem, selector) {
			if (elem.is(selector)) {
				return elem;
			} else {
				return elem.find(selector);
			}
		};
	
		var pluginOptions = {
			clickToHide: true,
			autoHide: true,
			autoHideDelay: 5000,
			arrowShow: true,
			arrowSize: 5,
			breakNewLines: true,
			elementPosition: "bottom",
			globalPosition: "top right",
			style: "bootstrap",
			className: "error",
			showAnimation: "slideDown",
			showDuration: 400,
			hideAnimation: "slideUp",
			hideDuration: 200,
			gap: 5
		};
	
		var inherit = function(a, b) {
			var F;
			F = function() {};
			F.prototype = a;
			return $.extend(true, new F(), b);
		};
	
		var defaults = function(opts) {
			return $.extend(pluginOptions, opts);
		};
	
		var createElem = function(tag) {
			return $("<" + tag + "></" + tag + ">");
		};
	
		var globalAnchors = {};
	
		var getAnchorElement = function(element) {
			var radios;
			if (element.is('[type=radio]')) {
				radios = element.parents('form:first').find('[type=radio]').filter(function(i, e) {
					return $(e).attr("name") === element.attr("name");
				});
				element = radios.first();
			}
			return element;
		};
	
		var incr = function(obj, pos, val) {
			var opp, temp;
			if (typeof val === "string") {
				val = parseInt(val, 10);
			} else if (typeof val !== "number") {
				return;
			}
			if (isNaN(val)) {
				return;
			}
			opp = positions[opposites[pos.charAt(0)]];
			temp = pos;
			if (obj[opp] !== undefined) {
				pos = positions[opp.charAt(0)];
				val = -val;
			}
			if (obj[pos] === undefined) {
				obj[pos] = val;
			} else {
				obj[pos] += val;
			}
			return null;
		};
	
		var realign = function(alignment, inner, outer) {
			if (alignment === "l" || alignment === "t") {
				return 0;
			} else if (alignment === "c" || alignment === "m") {
				return outer / 2 - inner / 2;
			} else if (alignment === "r" || alignment === "b") {
				return outer - inner;
			}
			throw "Invalid alignment";
		};
	
		var encode = function(text) {
			encode.e = encode.e || createElem("div");
			return encode.e.text(text).html();
		};
	
		function Notification(elem, data, options) {
			if (typeof options === "string") {
				options = {
					className: options
				};
			}
			this.options = inherit(pluginOptions, $.isPlainObject(options) ? options : {});
			this.loadHTML();
			this.wrapper = $(coreStyle.html);
			if (this.options.clickToHide) {
				this.wrapper.addClass(pluginClassName + "-hidable");
			}
			this.wrapper.data(pluginClassName, this);
			this.arrow = this.wrapper.find("." + pluginClassName + "-arrow");
			this.container = this.wrapper.find("." + pluginClassName + "-container");
			this.container.append(this.userContainer);
			if (elem && elem.length) {
				this.elementType = elem.attr("type");
				this.originalElement = elem;
				this.elem = getAnchorElement(elem);
				this.elem.data(pluginClassName, this);
				this.elem.before(this.wrapper);
			}
			this.container.hide();
			this.run(data);
		}
	
		Notification.prototype.loadHTML = function() {
			var style;
			style = this.getStyle();
			this.userContainer = $(style.html);
			this.userFields = style.fields;
		};
	
		Notification.prototype.show = function(show, userCallback) {
			var args, callback, elems, fn, hidden;
			callback = (function(_this) {
				return function() {
					if (!show && !_this.elem) {
						_this.destroy();
					}
					if (userCallback) {
						return userCallback();
					}
				};
			})(this);
			hidden = this.container.parent().parents(':hidden').length > 0;
			elems = this.container.add(this.arrow);
			args = [];
			if (hidden && show) {
				fn = "show";
			} else if (hidden && !show) {
				fn = "hide";
			} else if (!hidden && show) {
				fn = this.options.showAnimation;
				args.push(this.options.showDuration);
			} else if (!hidden && !show) {
				fn = this.options.hideAnimation;
				args.push(this.options.hideDuration);
			} else {
				return callback();
			}
			args.push(callback);
			return elems[fn].apply(elems, args);
		};
	
		Notification.prototype.setGlobalPosition = function() {
			var p = this.getPosition();
			var pMain = p[0];
			var pAlign = p[1];
			var main = positions[pMain];
			var align = positions[pAlign];
			var key = pMain + "|" + pAlign;
			var anchor = globalAnchors[key];
			if (!anchor || !document.body.contains(anchor[0])) {
				anchor = globalAnchors[key] = createElem("div");
				var css = {};
				css[main] = 0;
				if (align === "middle") {
					css.top = '45%';
				} else if (align === "center") {
					css.left = '45%';
				} else {
					css[align] = 0;
				}
				anchor.css(css).addClass(pluginClassName + "-corner");
				$("body").append(anchor);
			}
			return anchor.prepend(this.wrapper);
		};
	
		Notification.prototype.setElementPosition = function() {
			var arrowColor, arrowCss, arrowSize, color, contH, contW, css, elemH, elemIH, elemIW, elemPos, elemW, gap, j, k, len, len1, mainFull, margin, opp, oppFull, pAlign, pArrow, pMain, pos, posFull, position, ref, wrapPos;
			position = this.getPosition();
			pMain = position[0];
			pAlign = position[1];
			pArrow = position[2];
			elemPos = this.elem.position();
			elemH = this.elem.outerHeight();
			elemW = this.elem.outerWidth();
			elemIH = this.elem.innerHeight();
			elemIW = this.elem.innerWidth();
			wrapPos = this.wrapper.position();
			contH = this.container.height();
			contW = this.container.width();
			mainFull = positions[pMain];
			opp = opposites[pMain];
			oppFull = positions[opp];
			css = {};
			css[oppFull] = pMain === "b" ? elemH : pMain === "r" ? elemW : 0;
			incr(css, "top", elemPos.top - wrapPos.top);
			incr(css, "left", elemPos.left - wrapPos.left);
			ref = ["top", "left"];
			for (j = 0, len = ref.length; j < len; j++) {
				pos = ref[j];
				margin = parseInt(this.elem.css("margin-" + pos), 10);
				if (margin) {
					incr(css, pos, margin);
				}
			}
			gap = Math.max(0, this.options.gap - (this.options.arrowShow ? arrowSize : 0));
			incr(css, oppFull, gap);
			if (!this.options.arrowShow) {
				this.arrow.hide();
			} else {
				arrowSize = this.options.arrowSize;
				arrowCss = $.extend({}, css);
				arrowColor = this.userContainer.css("border-color") || this.userContainer.css("border-top-color") || this.userContainer.css("background-color") || "white";
				for (k = 0, len1 = mainPositions.length; k < len1; k++) {
					pos = mainPositions[k];
					posFull = positions[pos];
					if (pos === opp) {
						continue;
					}
					color = posFull === mainFull ? arrowColor : "transparent";
					arrowCss["border-" + posFull] = arrowSize + "px solid " + color;
				}
				incr(css, positions[opp], arrowSize);
				if (indexOf.call(mainPositions, pAlign) >= 0) {
					incr(arrowCss, positions[pAlign], arrowSize * 2);
				}
			}
			if (indexOf.call(vAligns, pMain) >= 0) {
				incr(css, "left", realign(pAlign, contW, elemW));
				if (arrowCss) {
					incr(arrowCss, "left", realign(pAlign, arrowSize, elemIW));
				}
			} else if (indexOf.call(hAligns, pMain) >= 0) {
				incr(css, "top", realign(pAlign, contH, elemH));
				if (arrowCss) {
					incr(arrowCss, "top", realign(pAlign, arrowSize, elemIH));
				}
			}
			if (this.container.is(":visible")) {
				css.display = "block";
			}
			this.container.removeAttr("style").css(css);
			if (arrowCss) {
				return this.arrow.removeAttr("style").css(arrowCss);
			}
		};
	
		Notification.prototype.getPosition = function() {
			var pos, ref, ref1, ref2, ref3, ref4, ref5, text;
			text = this.options.position || (this.elem ? this.options.elementPosition : this.options.globalPosition);
			pos = parsePosition(text);
			if (pos.length === 0) {
				pos[0] = "b";
			}
			if (ref = pos[0], indexOf.call(mainPositions, ref) < 0) {
				throw "Must be one of [" + mainPositions + "]";
			}
			if (pos.length === 1 || ((ref1 = pos[0], indexOf.call(vAligns, ref1) >= 0) && (ref2 = pos[1], indexOf.call(hAligns, ref2) < 0)) || ((ref3 = pos[0], indexOf.call(hAligns, ref3) >= 0) && (ref4 = pos[1], indexOf.call(vAligns, ref4) < 0))) {
				pos[1] = (ref5 = pos[0], indexOf.call(hAligns, ref5) >= 0) ? "m" : "l";
			}
			if (pos.length === 2) {
				pos[2] = pos[1];
			}
			return pos;
		};
	
		Notification.prototype.getStyle = function(name) {
			var style;
			if (!name) {
				name = this.options.style;
			}
			if (!name) {
				name = "default";
			}
			style = styles[name];
			if (!style) {
				throw "Missing style: " + name;
			}
			return style;
		};
	
		Notification.prototype.updateClasses = function() {
			var classes, style;
			classes = ["base"];
			if ($.isArray(this.options.className)) {
				classes = classes.concat(this.options.className);
			} else if (this.options.className) {
				classes.push(this.options.className);
			}
			style = this.getStyle();
			classes = $.map(classes, function(n) {
				return pluginClassName + "-" + style.name + "-" + n;
			}).join(" ");
			return this.userContainer.attr("class", classes);
		};
	
		Notification.prototype.run = function(data, options) {
			var d, datas, name, type, value;
			if ($.isPlainObject(options)) {
				$.extend(this.options, options);
			} else if ($.type(options) === "string") {
				this.options.className = options;
			}
			if (this.container && !data) {
				this.show(false);
				return;
			} else if (!this.container && !data) {
				return;
			}
			datas = {};
			if ($.isPlainObject(data)) {
				datas = data;
			} else {
				datas[blankFieldName] = data;
			}
			for (name in datas) {
				d = datas[name];
				type = this.userFields[name];
				if (!type) {
					continue;
				}
				if (type === "text") {
					d = encode(d);
					if (this.options.breakNewLines) {
						d = d.replace(/\n/g, '<br/>');
					}
				}
				value = name === blankFieldName ? '' : '=' + name;
				find(this.userContainer, "[data-notify-" + type + value + "]").html(d);
			}
			this.updateClasses();
			if (this.elem) {
				this.setElementPosition();
			} else {
				this.setGlobalPosition();
			}
			this.show(true);
			if (this.options.autoHide) {
				clearTimeout(this.autohideTimer);
				this.autohideTimer = setTimeout(this.show.bind(this, false), this.options.autoHideDelay);
			}
		};
	
		Notification.prototype.destroy = function() {
			this.wrapper.data(pluginClassName, null);
			this.wrapper.remove();
		};
	
		$[pluginName] = function(elem, data, options) {
			if ((elem && elem.nodeName) || elem.jquery) {
				$(elem)[pluginName](data, options);
			} else {
				options = data;
				data = elem;
				new Notification(null, data, options);
			}
			return elem;
		};
	
		$.fn[pluginName] = function(data, options) {
			$(this).each(function() {
				var prev = getAnchorElement($(this)).data(pluginClassName);
				if (prev) {
					prev.destroy();
				}
				var curr = new Notification($(this), data, options);
			});
			return this;
		};
	
		$.extend($[pluginName], {
			defaults: defaults,
			addStyle: addStyle,
			removeStyle: removeStyle,
			pluginOptions: pluginOptions,
			getStyle: getStyle,
			insertCSS: insertCSS
		});
	
		//always include the default bootstrap style
		addStyle("bootstrap", {
			html: "<div>\n<span data-notify-text></span>\n</div>",
			classes: {
				base: {
					"font-weight": "bold",
					"padding": "8px 15px 8px 14px",
					"text-shadow": "0 1px 0 rgba(255, 255, 255, 0.5)",
					"background-color": "#fcf8e3",
					"border": "1px solid #fbeed5",
					"border-radius": "4px",
					"white-space": "nowrap",
					"padding-left": "25px",
					"background-repeat": "no-repeat",
					"background-position": "3px 7px"
				},
				error: {
					"color": "#B94A48",
					"background-color": "#F2DEDE",
					"border-color": "#EED3D7",
					"background-image": "url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAtRJREFUeNqkVc1u00AQHq+dOD+0poIQfkIjalW0SEGqRMuRnHos3DjwAH0ArlyQeANOOSMeAA5VjyBxKBQhgSpVUKKQNGloFdw4cWw2jtfMOna6JOUArDTazXi/b3dm55socPqQhFka++aHBsI8GsopRJERNFlY88FCEk9Yiwf8RhgRyaHFQpPHCDmZG5oX2ui2yilkcTT1AcDsbYC1NMAyOi7zTX2Agx7A9luAl88BauiiQ/cJaZQfIpAlngDcvZZMrl8vFPK5+XktrWlx3/ehZ5r9+t6e+WVnp1pxnNIjgBe4/6dAysQc8dsmHwPcW9C0h3fW1hans1ltwJhy0GxK7XZbUlMp5Ww2eyan6+ft/f2FAqXGK4CvQk5HueFz7D6GOZtIrK+srupdx1GRBBqNBtzc2AiMr7nPplRdKhb1q6q6zjFhrklEFOUutoQ50xcX86ZlqaZpQrfbBdu2R6/G19zX6XSgh6RX5ubyHCM8nqSID6ICrGiZjGYYxojEsiw4PDwMSL5VKsC8Yf4VRYFzMzMaxwjlJSlCyAQ9l0CW44PBADzXhe7xMdi9HtTrdYjFYkDQL0cn4Xdq2/EAE+InCnvADTf2eah4Sx9vExQjkqXT6aAERICMewd/UAp/IeYANM2joxt+q5VI+ieq2i0Wg3l6DNzHwTERPgo1ko7XBXj3vdlsT2F+UuhIhYkp7u7CarkcrFOCtR3H5JiwbAIeImjT/YQKKBtGjRFCU5IUgFRe7fF4cCNVIPMYo3VKqxwjyNAXNepuopyqnld602qVsfRpEkkz+GFL1wPj6ySXBpJtWVa5xlhpcyhBNwpZHmtX8AGgfIExo0ZpzkWVTBGiXCSEaHh62/PoR0p/vHaczxXGnj4bSo+G78lELU80h1uogBwWLf5YlsPmgDEd4M236xjm+8nm4IuE/9u+/PH2JXZfbwz4zw1WbO+SQPpXfwG/BBgAhCNZiSb/pOQAAAAASUVORK5CYII=)"
				},
				success: {
					"color": "#468847",
					"background-color": "#DFF0D8",
					"border-color": "#D6E9C6",
					"background-image": "url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAutJREFUeNq0lctPE0Ecx38zu/RFS1EryqtgJFA08YCiMZIAQQ4eRG8eDGdPJiYeTIwHTfwPiAcvXIwXLwoXPaDxkWgQ6islKlJLSQWLUraPLTv7Gme32zoF9KSTfLO7v53vZ3d/M7/fIth+IO6INt2jjoA7bjHCJoAlzCRw59YwHYjBnfMPqAKWQYKjGkfCJqAF0xwZjipQtA3MxeSG87VhOOYegVrUCy7UZM9S6TLIdAamySTclZdYhFhRHloGYg7mgZv1Zzztvgud7V1tbQ2twYA34LJmF4p5dXF1KTufnE+SxeJtuCZNsLDCQU0+RyKTF27Unw101l8e6hns3u0PBalORVVVkcaEKBJDgV3+cGM4tKKmI+ohlIGnygKX00rSBfszz/n2uXv81wd6+rt1orsZCHRdr1Imk2F2Kob3hutSxW8thsd8AXNaln9D7CTfA6O+0UgkMuwVvEFFUbbAcrkcTA8+AtOk8E6KiQiDmMFSDqZItAzEVQviRkdDdaFgPp8HSZKAEAL5Qh7Sq2lIJBJwv2scUqkUnKoZgNhcDKhKg5aH+1IkcouCAdFGAQsuWZYhOjwFHQ96oagWgRoUov1T9kRBEODAwxM2QtEUl+Wp+Ln9VRo6BcMw4ErHRYjH4/B26AlQoQQTRdHWwcd9AH57+UAXddvDD37DmrBBV34WfqiXPl61g+vr6xA9zsGeM9gOdsNXkgpEtTwVvwOklXLKm6+/p5ezwk4B+j6droBs2CsGa/gNs6RIxazl4Tc25mpTgw/apPR1LYlNRFAzgsOxkyXYLIM1V8NMwyAkJSctD1eGVKiq5wWjSPdjmeTkiKvVW4f2YPHWl3GAVq6ymcyCTgovM3FzyRiDe2TaKcEKsLpJvNHjZgPNqEtyi6mZIm4SRFyLMUsONSSdkPeFtY1n0mczoY3BHTLhwPRy9/lzcziCw9ACI+yql0VLzcGAZbYSM5CCSZg1/9oc/nn7+i8N9p/8An4JMADxhH+xHfuiKwAAAABJRU5ErkJggg==)"
				},
				info: {
					"color": "#3A87AD",
					"background-color": "#D9EDF7",
					"border-color": "#BCE8F1",
					"background-image": "url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAABmJLR0QA/wD/AP+gvaeTAAAACXBIWXMAAAsTAAALEwEAmpwYAAAAB3RJTUUH3QYFAhkSsdes/QAAA8dJREFUOMvVlGtMW2UYx//POaWHXg6lLaW0ypAtw1UCgbniNOLcVOLmAjHZolOYlxmTGXVZdAnRfXQm+7SoU4mXaOaiZsEpC9FkiQs6Z6bdCnNYruM6KNBw6YWewzl9z+sHImEWv+vz7XmT95f/+3/+7wP814v+efDOV3/SoX3lHAA+6ODeUFfMfjOWMADgdk+eEKz0pF7aQdMAcOKLLjrcVMVX3xdWN29/GhYP7SvnP0cWfS8caSkfHZsPE9Fgnt02JNutQ0QYHB2dDz9/pKX8QjjuO9xUxd/66HdxTeCHZ3rojQObGQBcuNjfplkD3b19Y/6MrimSaKgSMmpGU5WevmE/swa6Oy73tQHA0Rdr2Mmv/6A1n9w9suQ7097Z9lM4FlTgTDrzZTu4StXVfpiI48rVcUDM5cmEksrFnHxfpTtU/3BFQzCQF/2bYVoNbH7zmItbSoMj40JSzmMyX5qDvriA7QdrIIpA+3cdsMpu0nXI8cV0MtKXCPZev+gCEM1S2NHPvWfP/hL+7FSr3+0p5RBEyhEN5JCKYr8XnASMT0xBNyzQGQeI8fjsGD39RMPk7se2bd5ZtTyoFYXftF6y37gx7NeUtJJOTFlAHDZLDuILU3j3+H5oOrD3yWbIztugaAzgnBKJuBLpGfQrS8wO4FZgV+c1IxaLgWVU0tMLEETCos4xMzEIv9cJXQcyagIwigDGwJgOAtHAwAhisQUjy0ORGERiELgG4iakkzo4MYAxcM5hAMi1WWG1yYCJIcMUaBkVRLdGeSU2995TLWzcUAzONJ7J6FBVBYIggMzmFbvdBV44Corg8vjhzC+EJEl8U1kJtgYrhCzgc/vvTwXKSib1paRFVRVORDAJAsw5FuTaJEhWM2SHB3mOAlhkNxwuLzeJsGwqWzf5TFNdKgtY5qHp6ZFf67Y/sAVadCaVY5YACDDb3Oi4NIjLnWMw2QthCBIsVhsUTU9tvXsjeq9+X1d75/KEs4LNOfcdf/+HthMnvwxOD0wmHaXr7ZItn2wuH2SnBzbZAbPJwpPx+VQuzcm7dgRCB57a1uBzUDRL4bfnI0RE0eaXd9W89mpjqHZnUI5Hh2l2dkZZUhOqpi2qSmpOmZ64Tuu9qlz/SEXo6MEHa3wOip46F1n7633eekV8ds8Wxjn37Wl63VVa+ej5oeEZ/82ZBETJjpJ1Rbij2D3Z/1trXUvLsblCK0XfOx0SX2kMsn9dX+d+7Kf6h8o4AIykuffjT8L20LU+w4AZd5VvEPY+XpWqLV327HR7DzXuDnD8r+ovkBehJ8i+y8YAAAAASUVORK5CYII=)"
				},
				warn: {
					"color": "#C09853",
					"background-color": "#FCF8E3",
					"border-color": "#FBEED5",
					"background-image": "url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAMAAAC6V+0/AAABJlBMVEXr6eb/2oD/wi7/xjr/0mP/ykf/tQD/vBj/3o7/uQ//vyL/twebhgD/4pzX1K3z8e349vK6tHCilCWbiQymn0jGworr6dXQza3HxcKkn1vWvV/5uRfk4dXZ1bD18+/52YebiAmyr5S9mhCzrWq5t6ufjRH54aLs0oS+qD751XqPhAybhwXsujG3sm+Zk0PTwG6Shg+PhhObhwOPgQL4zV2nlyrf27uLfgCPhRHu7OmLgAafkyiWkD3l49ibiAfTs0C+lgCniwD4sgDJxqOilzDWowWFfAH08uebig6qpFHBvH/aw26FfQTQzsvy8OyEfz20r3jAvaKbhgG9q0nc2LbZxXanoUu/u5WSggCtp1anpJKdmFz/zlX/1nGJiYmuq5Dx7+sAAADoPUZSAAAAAXRSTlMAQObYZgAAAAFiS0dEAIgFHUgAAAAJcEhZcwAACxMAAAsTAQCanBgAAAAHdElNRQfdBgUBGhh4aah5AAAAlklEQVQY02NgoBIIE8EUcwn1FkIXM1Tj5dDUQhPU502Mi7XXQxGz5uVIjGOJUUUW81HnYEyMi2HVcUOICQZzMMYmxrEyMylJwgUt5BljWRLjmJm4pI1hYp5SQLGYxDgmLnZOVxuooClIDKgXKMbN5ggV1ACLJcaBxNgcoiGCBiZwdWxOETBDrTyEFey0jYJ4eHjMGWgEAIpRFRCUt08qAAAAAElFTkSuQmCC)"
				}
			}
		});
	
		$(function() {
			insertCSS(coreStyle.css).attr("id", "core-notify");
			$(document).on("click", "." + pluginClassName + "-hidable", function(e) {
				$(this).trigger("notify-hide");
			});
			$(document).on("notify-hide", "." + pluginClassName + "-wrapper", function(e) {
				var elem = $(this).data(pluginClassName);
				if(elem) {
					elem.show(false);
				}
			});
		});
	
	}));


/***/ },
/* 7 */
/*!**************************************!*\
  !*** ./js/component/avalonFilter.js ***!
  \**************************************/
/***/ function(module, exports, __webpack_require__) {

	/**
	 * Created by JinWYP on 09/12/2016.
	 */
	
	
	var avalon = __webpack_require__(/*! avalon2 */ 3) ;
	var userService = __webpack_require__(/*! ../service/user.js */ 8) ;
	var orderService = __webpack_require__(/*! ../service/financeOrder.js */ 10) ;
	
	avalon.filters.rolename= function (str) {
	    if (!str){
	        return str
	    }
	    return userService.userRoleAllObject[str]
	};
	
	avalon.filters.statusname= function (str) {
	    if (!str){
	        return str
	    }
	    return orderService.statusObject[str]
	};
	
	avalon.filters.typename= function (str) {
	    if (!str){
	        return str
	    }else {
	        if (str === 'MYR') return '煤易融';
	        if (str === 'MYD') return '煤易贷';
	        if (str === 'MYG') return '煤易购';
	    }
	};
	
	
	avalon.filters.deposittype= function (str) {
	    if (!str){
	        return str
	    }
	
	    return orderService.depositType[str]
	};

/***/ },
/* 8 */,
/* 9 */,
/* 10 */
/*!************************************!*\
  !*** ./js/service/financeOrder.js ***!
  \************************************/
/***/ function(module, exports, __webpack_require__) {

	/**
	 * Created by JinWYP on 09/12/2016.
	 */
	
	var jQuery = __webpack_require__(/*! jquery */ 2);
	var headers = __webpack_require__(/*! ./token */ 9).headers;
	var url = __webpack_require__(/*! ./token */ 9).url;
	var role = __webpack_require__(/*! ./user.js */ 8).userRoleKeyObject;
	
	var status = [
	    {name : 'financingStep11', displayName:'等待贸易商选择港口,监管方和资金方'},
	    {name : 'financingStep12', displayName:'等待融资方,港口和监管方上传合同及单据'},
	    {name : 'financingStep13', displayName:'融资方, 港口和监管方完成上传合同,待贸易商审核'},
	    // {name : 'financingStep14', displayName:'港口完成上传合同,待贸易商审核'}, // 不需要
	    // {name : 'financingStep15', displayName:'监管方完成上传合同,待贸易商审核'}, // 不需要
	    {name : 'TraderDisapprove', displayName:'贸易商审核不通过，流程结束'},
	    {name : 'financingStep14', displayName:'贸易商审核通过,待贸易商财务放款建议'},
	    {name : 'financingStep15', displayName:'贸易商财务放款建议审核通过,待资金方审核'},
	    {name : 'FundProviderDisapprove', displayName:'资金方审核不通过，流程结束'},
	    {name : 'financingStep16', displayName:'资金方审核通过,待资金方财务放款'},
	    {name : 'financingStep17', displayName:'资金方财务已放款,待贸易商确认收款,银行转账中'},
	
	    {name : 'financingStep18', displayName:'贸易商已自动确认收款,贸易商已自动打款给融资方, 待融资方确认收款,银行转账中'},
	
	    {name : 'financingStep19', displayName:'融资方已自动确认收款,融资放款阶段完成,待融资方回款'},
	    {name : 'repaymentStep20', displayName:'融资方已回款,待贸易商自动确认收款,银行转账中'},
	    {name : 'repaymentStep21', displayName:'融资方已回款,待贸易商放货'},
	
	    {name : 'repaymentStep22', displayName:'贸易商已放货, 融资方回款完成, 待港口放货确认'},
	    {name : 'repaymentStep23', displayName:'港口已完成放货, 待贸易商确认还款是否完成'},
	
	
	
	    // {name : 'repaymentStep32', displayName:'贸易商已放货, 融资方部分回款已完成, 待港口放货确认'},
	    // {name : 'repaymentStep33', displayName:'贸易商已放货, 融资方全部回款已完成, 待港口放货确认'},
	    // {name : 'repaymentStep34', displayName:'港口已确认返回货物,融资方部分回款已完成, 待融资方继续回款'},
	    {name : 'repaymentStep24', displayName:'融资方全部回款已完成, 待贸易商确认回款给资金方'},
	    {name : 'repaymentStep53', displayName:'贸易商已扣押货物（处置货权）,融资方未回款, 待贸易商确认回款给资金方'},
	    {name : 'repaymentStep25', displayName:'贸易商已确认回款给资金方,待贸易商财务放款'},
	    {name : 'repaymentStep26', displayName:'贸易商财务已放款给资金方，银行转账中'},
	    {name : 'success', displayName:'贸易商财务已完成回款给资金方，流程结束'}
	];
	
	var statusObject = {};
	
	status.forEach(function (item, index){
	    statusObject[item.name] = item.displayName;
	});
	
	var actions = [
	    {statusAt:"financingStep11", operator : 'trader', name : 'a11SelectHarborAndSupervisor', displayName : '完成选择港口,监管方和资金方'},
	
	    {statusAt:"financingStep12", operator : 'financer', name : 'a12FinishedUpload', displayName : '确认完成上传资料并提交'},
	    {statusAt:"financingStep12", operator : 'harbor', name : 'a13FinishedUpload', displayName : '确认完成上传资料并已确认货物数量'},
	    {statusAt:"financingStep12", operator : 'supervisor', name : 'a14FinishedUpload', displayName : '确认完成上传资料并提交'},
	
	    {statusAt:"financingStep13", operator : 'trader', name : 'a15Approved', displayName : '审核通过'},
	    {statusAt:"financingStep13", operator : 'trader', name : 'a16NotApproved', displayName : '审核不通过'},
	
	    {statusAt:"financingStep14", operator : 'traderAccountant', name : 'a16traderRecommendAmount', displayName : '确认放款'},
	
	    {statusAt:"financingStep15", operator : 'fundProvider', name : 'a17fundProviderAudit', displayName : '审核通过'},
	    {statusAt:"financingStep15", operator : 'fundProvider', name : 'a19NotApproved', displayName : '审核不通过'},
	
	    {statusAt:"financingStep16", operator : 'fundProviderAccountant', name : 'a18fundProviderAccountantAudit', displayName : '确认放款'},
	    // {statusAt:"financingStep19", operator : 'fundProviderAccountant', name : 'a21auto', displayName : '自动确认收款1'},
	    // {statusAt:"financingStep20", operator : 'fundProviderAccountant', name : 'a22auto', displayName : '自动确认收款2'},
	
	
	    // {statusAt:"financingStep19", operator : 'financer', name : 'a31FirstReturnMoney', displayName : '确认还款'},
	    {statusAt:"financingStep19", operator : 'financer', name : 'a19SecondReturnMoney', displayName : '确认还款'},
	
	    {statusAt:"repaymentStep21", operator : 'trader', name : 'a20noticeHarborRelease', displayName : ' 回款完成,确认放货'},
	
	    {statusAt:"repaymentStep22", operator : 'harbor', name : 'a21harborRelease', displayName : ' 部分回款完成,确认返回货物'},
	    // {statusAt:"repaymentStep33", operator : 'harbor', name : 'a35ConfirmAllCargo', displayName : '全部回款完成,确认返回货物'},
	
	    {statusAt:"repaymentStep23", operator : 'trader', name : 'a22traderAuditIfComplete', displayName : '确认回款部分完成, 需要融资方继续还款'},
	    // {statusAt:"repaymentStep23", operator : 'trader', name : 'a22traderAuditIfComplete', displayName : '确认回款全部完成'},
	
	
	
	    {statusAt:"repaymentStep24", operator : 'trader', name : 'a23ReturnMoney', displayName : '确认回款给资金方'},
	    {statusAt:"repaymentStep25", operator : 'traderAccountant', name : 'a24AccountantReturnMoney', displayName : '确认放款给资金方'},
	
	    {statusAt:"financingStep21", operator : 'trader', name : 'a37Punishment', displayName : '扣押货物(处置货权)'},
	    {statusAt:"repaymentStep34", operator : 'trader', name : 'a38Punishment', displayName : '扣押货物(处置货权)'}
	];
	
	var actionObject = {};
	
	actions.forEach(function (item, index){
	    actionObject[item.name] = item;
	});
	
	
	var contractType = {
	    default : '-',
	    contract : '合同',
	    finance  : '财务单据',
	    business : '业务单据类(质量和数量单据, 运输单据, 货转证明)'
	}
	
	var paymentType = [
	    { name : 'repayment',  displayName : '还款'},
	    { name : 'deposit',  displayName : '保证金'}
	]
	var paymentTypeObject = {};
	var paymentTypeKeyObject = {};
	
	paymentType.forEach(function (type, index){
	    paymentTypeObject[type.name] = type.displayName;
	    paymentTypeKeyObject[type.name] = type.name;
	});
	
	
	var depositType = {
	    notified    : '保证金已通知',
	    alreadyPaid : '保证金已缴纳',
	    transferred : '保证金已到账'
	}
	var depositTypeKeyObject = {
	    notified    : 'notified',
	    alreadyPaid : 'alreadyPaid',
	    transferred : 'transferred'
	};
	
	
	exports.statusList   = status;
	exports.statusObject = statusObject;
	exports.actionList   = actions;
	exports.actionObject = actionObject;
	exports.contractType = contractType;
	exports.paymentType  = paymentTypeObject;
	exports.paymentTypeKey  = paymentTypeKeyObject;
	exports.depositType  = depositType;
	exports.depositTypeKey  = depositTypeKeyObject;
	
	
	
	
	
	exports.getFinanceOrderList = function (query){
	
	    var user = {};
	
	    if (typeof query.userRole !== 'undefined'){
	
	        if (query.userRole === role.financer) {
	            user = {financerUserId : query.userId}
	        }
	        if (query.userRole === role.harbor) {
	            user = {harborUserId : query.userId}
	        }
	        if (query.userRole === role.supervisor) {
	            user = {supervisorUserId : query.userId}
	        }
	        if (query.userRole === role.trader) {
	            user = {traderUserId : query.userId}
	        }
	        if (query.userRole === role.traderAccountant) {
	            user = {traderAccountantUserId : query.userId}
	        }
	        if (query.userRole === role.fundProvider) {
	            user = {fundProviderUserId : query.userId}
	        }
	        if (query.userRole === role.fundProviderAccountant) {
	            user = {fundProviderAccountantUserId : query.userId}
	        }
	    }
	
	    delete query.userRole;
	    delete query.userId;
	
	    var params = jQuery.extend({}, query, user);
	
	    return jQuery.ajax({
	        headers : headers,
	        contentType : 'application/json',
	        dataType : 'json',
	        url      : url.financeOrderList,
	        method   : 'GET',
	        data     : params
	
	    });
	
	};
	
	exports.getFinanceOrderInfoById = function (id, query){
	
	    var params = jQuery.extend({}, query);
	
	    return jQuery.ajax({
	        headers : headers,
	        contentType : 'application/json',
	        dataType : 'json',
	        url      : url.financeOrderList + '/' + id,
	        method   : 'GET',
	        data     : params
	
	    });
	
	};
	
	exports.addNewFinanceOrder = function (order){
	
	    var params = jQuery.extend({}, order);
	
	    return jQuery.ajax({
	        headers : headers,
	        contentType : 'application/json',
	        dataType : 'json',
	        url      : url.financeOrderList,
	        method   : 'POST',
	        data     : JSON.stringify(params)
	
	    });
	
	};
	
	exports.auditFinanceOrder = function (flowId, taskName, taskId, actionName, additionalData){
	    console.log("在流程 %s, 中的任务 %s , 发出的动作: %s", flowId, taskName, actionName)
	    var params = jQuery.extend({}, {
	        "taskId": taskId,
	        "flowId": flowId
	    });
	
	    if (additionalData && additionalData.harborUserId) params.harborUserId = additionalData.harborUserId;
	    if (additionalData && additionalData.harborCompanyId) params.harborCompanyId = additionalData.harborCompanyId;
	    if (additionalData && additionalData.supervisorUserId) params.supervisorUserId = additionalData.supervisorUserId;
	    if (additionalData && additionalData.supervisorCompanyId) params.supervisorCompanyId = additionalData.supervisorCompanyId;
	    if (additionalData && additionalData.fundProviderUserId) params.fundProviderUserId = additionalData.fundProviderUserId;
	    if (additionalData && additionalData.fundProviderCompanyId) params.fundProviderCompanyId = additionalData.fundProviderCompanyId;
	    if (additionalData && additionalData.fundProviderAccountantUserId) params.fundProviderAccountantUserId = additionalData.fundProviderAccountantUserId;
	    if (additionalData && additionalData.fundProviderAccountantCompanyId) params.fundProviderAccountantCompanyId = additionalData.fundProviderAccountantCompanyId;
	
	
	    if (additionalData && additionalData.fileList) params.fileList = additionalData.fileList;
	    if (additionalData && additionalData.harborConfirmAmount) params.harborConfirmAmount = additionalData.harborConfirmAmount;
	
	    if (additionalData && additionalData.fundProviderInterestRate) params.fundProviderInterestRate = additionalData.fundProviderInterestRate;
	    if (additionalData && additionalData.loanValue) params.loanValue = additionalData.loanValue;
	
	    if (additionalData && additionalData.repaymentValue) params.repaymentValue = additionalData.repaymentValue;
	    if (additionalData && additionalData.redemptionAmount) params.redemptionAmount = additionalData.redemptionAmount;
	    if (additionalData && additionalData.goodsReceiveCompanyName) params.goodsReceiveCompanyName = additionalData.goodsReceiveCompanyName;
	
	    if (additionalData && additionalData.approvedStatus === 0) params.approvedStatus = 0;
	    if (additionalData && additionalData.approvedStatus === 1) params.approvedStatus = 1;
	
	    if (additionalData && additionalData.status === 0) params.status = 0;
	    if (additionalData && additionalData.status === 1) params.status = 1;
	
	
	    if (additionalData && additionalData.redemptionAmountDeliveryId) params.redemptionAmountDeliveryId = additionalData.redemptionAmountDeliveryId;
	
	
	
	
	    if (actionName === 'a18fundProviderAccountantAudit' || actionName === 'a21harborRelease' || actionName === 'a23ReturnMoney' || actionName === 'a24AccountantReturnMoney') {
	        params.status = 1
	    }
	
	    return jQuery.ajax({
	        headers : headers,
	        contentType : 'application/json',
	        dataType : 'json',
	        url      : url.financeOrderList + '/action/' + taskName,
	        method   : 'POST',
	        data     :JSON.stringify(params)
	
	    });
	
	};
	
	
	
	
	
	
	exports.addNewDepositOrder = function (order){
	
	    var params = jQuery.extend({}, order);
	
	    return jQuery.ajax({
	        headers : headers,
	        contentType : 'application/json',
	        dataType : 'json',
	        url      : url.depositList,
	        method   : 'POST',
	        data     : JSON.stringify(params)
	
	    });
	
	};
	
	
	exports.updateDepositOrderInfoById = function (order){
	
	    var params = jQuery.extend({}, order);
	
	    var urlTemp = url.depositList + '/' + order.flowId + '?state=' + order.state
	
	    if (order.amount){
	        urlTemp = urlTemp + '&amount=' + order.amount
	    }
	    return jQuery.ajax({
	        headers : headers,
	        contentType : 'application/json',
	        dataType : 'json',
	        url      : urlTemp,
	        method   : 'PUT',
	        data     : JSON.stringify(params)
	
	    });
	
	};
	
	
	
	exports.getFileById = function (id, query){
	    var params = jQuery.extend({}, query);
	
	    window.location = url.files + '/' + id;
	
	};
	
	
	
	
	
	


/***/ },
/* 11 */,
/* 12 */,
/* 13 */,
/* 14 */
/*!*************************************!*\
  !*** ./js/page/adminSessionUser.js ***!
  \*************************************/
/***/ function(module, exports, __webpack_require__) {

	/* WEBPACK VAR INJECTION */(function($) {/**
	 * Created by JinWYP on 24/11/2016.
	 */
	
	
	
	var avalon = __webpack_require__(/*! avalon2 */ 3) ;
	__webpack_require__(/*! ../component/header.js */ 4);
	
	var userService = __webpack_require__(/*! ../service/user.js */ 8) ;
	
	
	var url = window.location.href;
	var urlShowStatus = url.substring(url.lastIndexOf("\/") + 1, url.length);
	
	// console.log(urlShowStatus);
	
	
	var sessionUser = function() {
	
	    var vm = avalon.define({
	        $id : 'passwordController',
	
	        currentUser : {
	            username : '',
	            email : '',
	            phone : '',
	            companyName : '',
	            belongToUser : '', // 资金方财务关联资金方用户ID, 贸易商财务关联贸易商用户ID
	            role : ''
	        },
	
	        modifyPassword : {
	            oldPwd : '',
	            newPwd : '',
	            rePwd : ''
	        },
	
	        errorMessage : {
	            inputEmail : '',
	            inputPhone : '',
	            inputOldPwd : '',
	            inputNewPwd : '',
	            inputRePwd : ''
	        },
	        successInputName : [],
	        errorInputName : [],
	        validate: {
	            onSuccess: function (reasons) {
	                if(vm.successInputName.indexOf(this.id) === -1) vm.successInputName.push(this.id.toString());
	                if(vm.errorInputName.indexOf(this.id) > -1) vm.errorInputName.splice(vm.errorInputName.indexOf(this.id), 1);
	            },
	            onError: function (reasons) {
	                console.log(reasons[0].getMessage());
	                vm.errorMessage[this.id.toString()] = reasons[0].getMessage();
	
	                if (vm.successInputName.indexOf(this.id) > -1) vm.successInputName.splice(vm.successInputName.indexOf(this.id),1);
	                if (vm.errorInputName.indexOf(this.id.toString()) === -1) vm.errorInputName.push(this.id.toString());
	            },
	            onValidateAll: function (reasons) {
	                if (reasons.length) {
	                    console.log('有表单没有通过');
	                    $("input").focus().blur();
	                } else {
	                    console.log('全部通过');
	                    if (vm.pageShowStatus === 'info') {
	
	                        var user = {
	                            // username : vm.currentUser.username,
	                            // role : vm.currentUser.role,
	                            // companyName : vm.currentUser.companyName,
	                            email : vm.currentUser.email,
	                            phone : vm.currentUser.phone
	                        };
	
	                        userService.updateSessionUserInfo(user).done(function (data, textStatus, jqXHR) {
	                            if (data.success) {
	                                $.notify("用户修改信息成功!", 'success');
	                            }
	                        })
	                    }
	                    if (vm.pageShowStatus === 'password') {
	                        userService.updateSessionUserPassword(vm.modifyPassword.oldPwd, vm.modifyPassword.newPwd).done(function (data, textStatus, jqXHR) {
	                            if (data.success) {
	                                $.notify("密码修改成功!", 'success');
	                            }
	                        })
	                    }
	                }
	            }
	        },
	
	        pageShowStatus : 'password'
	
	    });
	
	    if (urlShowStatus === 'password'){
	        vm.pageShowStatus = 'password';
	    }else {
	        vm.pageShowStatus = 'info';
	    }
	
	    userService.getSessionUser().done(function(data, textStatus, jqXHR) {
	        if (data.success){
	            vm.currentUser = data.data;
	        }else{
	            console.log(data.error);
	        }
	    });
	};
	
	
	
	
	
	sessionUser();
	
	
	module.exports = sessionUser;
	/* WEBPACK VAR INJECTION */}.call(exports, __webpack_require__(/*! jquery */ 2)))

/***/ }
]);
//# sourceMappingURL=adminSessionUser.bundle.js.map
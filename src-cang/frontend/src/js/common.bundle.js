/******/ (function(modules) { // webpackBootstrap
/******/ 	// install a JSONP callback for chunk loading
/******/ 	var parentJsonpFunction = window["webpackJsonp"];
/******/ 	window["webpackJsonp"] = function webpackJsonpCallback(chunkIds, moreModules) {
/******/ 		// add "moreModules" to the modules object,
/******/ 		// then flag all "chunkIds" as loaded and fire callback
/******/ 		var moduleId, chunkId, i = 0, callbacks = [];
/******/ 		for(;i < chunkIds.length; i++) {
/******/ 			chunkId = chunkIds[i];
/******/ 			if(installedChunks[chunkId])
/******/ 				callbacks.push.apply(callbacks, installedChunks[chunkId]);
/******/ 			installedChunks[chunkId] = 0;
/******/ 		}
/******/ 		for(moduleId in moreModules) {
/******/ 			modules[moduleId] = moreModules[moduleId];
/******/ 		}
/******/ 		if(parentJsonpFunction) parentJsonpFunction(chunkIds, moreModules);
/******/ 		while(callbacks.length)
/******/ 			callbacks.shift().call(null, __webpack_require__);
/******/ 		if(moreModules[0]) {
/******/ 			installedModules[0] = 0;
/******/ 			return __webpack_require__(0);
/******/ 		}
/******/ 	};
/******/ 	var parentHotUpdateCallback = this["webpackHotUpdate"];
/******/ 	this["webpackHotUpdate"] = 
/******/ 	function webpackHotUpdateCallback(chunkId, moreModules) { // eslint-disable-line no-unused-vars
/******/ 		hotAddUpdateChunk(chunkId, moreModules);
/******/ 		if(parentHotUpdateCallback) parentHotUpdateCallback(chunkId, moreModules);
/******/ 	}
/******/ 	
/******/ 	function hotDownloadUpdateChunk(chunkId) { // eslint-disable-line no-unused-vars
/******/ 		var head = document.getElementsByTagName("head")[0];
/******/ 		var script = document.createElement("script");
/******/ 		script.type = "text/javascript";
/******/ 		script.charset = "utf-8";
/******/ 		script.src = __webpack_require__.p + "" + chunkId + "." + hotCurrentHash + ".hot-update.js";
/******/ 		head.appendChild(script);
/******/ 	}
/******/ 	
/******/ 	function hotDownloadManifest(callback) { // eslint-disable-line no-unused-vars
/******/ 		if(typeof XMLHttpRequest === "undefined")
/******/ 			return callback(new Error("No browser support"));
/******/ 		try {
/******/ 			var request = new XMLHttpRequest();
/******/ 			var requestPath = __webpack_require__.p + "" + hotCurrentHash + ".hot-update.json";
/******/ 			request.open("GET", requestPath, true);
/******/ 			request.timeout = 10000;
/******/ 			request.send(null);
/******/ 		} catch(err) {
/******/ 			return callback(err);
/******/ 		}
/******/ 		request.onreadystatechange = function() {
/******/ 			if(request.readyState !== 4) return;
/******/ 			if(request.status === 0) {
/******/ 				// timeout
/******/ 				callback(new Error("Manifest request to " + requestPath + " timed out."));
/******/ 			} else if(request.status === 404) {
/******/ 				// no update available
/******/ 				callback();
/******/ 			} else if(request.status !== 200 && request.status !== 304) {
/******/ 				// other failure
/******/ 				callback(new Error("Manifest request to " + requestPath + " failed."));
/******/ 			} else {
/******/ 				// success
/******/ 				try {
/******/ 					var update = JSON.parse(request.responseText);
/******/ 				} catch(e) {
/******/ 					callback(e);
/******/ 					return;
/******/ 				}
/******/ 				callback(null, update);
/******/ 			}
/******/ 		};
/******/ 	}
/******/
/******/ 	
/******/ 	
/******/ 	// Copied from https://github.com/facebook/react/blob/bef45b0/src/shared/utils/canDefineProperty.js
/******/ 	var canDefineProperty = false;
/******/ 	try {
/******/ 		Object.defineProperty({}, "x", {
/******/ 			get: function() {}
/******/ 		});
/******/ 		canDefineProperty = true;
/******/ 	} catch(x) {
/******/ 		// IE will fail on defineProperty
/******/ 	}
/******/ 	
/******/ 	var hotApplyOnUpdate = true;
/******/ 	var hotCurrentHash = "ad8d9edd51a4c7e3e71a"; // eslint-disable-line no-unused-vars
/******/ 	var hotCurrentModuleData = {};
/******/ 	var hotCurrentParents = []; // eslint-disable-line no-unused-vars
/******/ 	
/******/ 	function hotCreateRequire(moduleId) { // eslint-disable-line no-unused-vars
/******/ 		var me = installedModules[moduleId];
/******/ 		if(!me) return __webpack_require__;
/******/ 		var fn = function(request) {
/******/ 			if(me.hot.active) {
/******/ 				if(installedModules[request]) {
/******/ 					if(installedModules[request].parents.indexOf(moduleId) < 0)
/******/ 						installedModules[request].parents.push(moduleId);
/******/ 					if(me.children.indexOf(request) < 0)
/******/ 						me.children.push(request);
/******/ 				} else hotCurrentParents = [moduleId];
/******/ 			} else {
/******/ 				console.warn("[HMR] unexpected require(" + request + ") from disposed module " + moduleId);
/******/ 				hotCurrentParents = [];
/******/ 			}
/******/ 			return __webpack_require__(request);
/******/ 		};
/******/ 		for(var name in __webpack_require__) {
/******/ 			if(Object.prototype.hasOwnProperty.call(__webpack_require__, name)) {
/******/ 				if(canDefineProperty) {
/******/ 					Object.defineProperty(fn, name, (function(name) {
/******/ 						return {
/******/ 							configurable: true,
/******/ 							enumerable: true,
/******/ 							get: function() {
/******/ 								return __webpack_require__[name];
/******/ 							},
/******/ 							set: function(value) {
/******/ 								__webpack_require__[name] = value;
/******/ 							}
/******/ 						};
/******/ 					}(name)));
/******/ 				} else {
/******/ 					fn[name] = __webpack_require__[name];
/******/ 				}
/******/ 			}
/******/ 		}
/******/ 	
/******/ 		function ensure(chunkId, callback) {
/******/ 			if(hotStatus === "ready")
/******/ 				hotSetStatus("prepare");
/******/ 			hotChunksLoading++;
/******/ 			__webpack_require__.e(chunkId, function() {
/******/ 				try {
/******/ 					callback.call(null, fn);
/******/ 				} finally {
/******/ 					finishChunkLoading();
/******/ 				}
/******/ 	
/******/ 				function finishChunkLoading() {
/******/ 					hotChunksLoading--;
/******/ 					if(hotStatus === "prepare") {
/******/ 						if(!hotWaitingFilesMap[chunkId]) {
/******/ 							hotEnsureUpdateChunk(chunkId);
/******/ 						}
/******/ 						if(hotChunksLoading === 0 && hotWaitingFiles === 0) {
/******/ 							hotUpdateDownloaded();
/******/ 						}
/******/ 					}
/******/ 				}
/******/ 			});
/******/ 		}
/******/ 		if(canDefineProperty) {
/******/ 			Object.defineProperty(fn, "e", {
/******/ 				enumerable: true,
/******/ 				value: ensure
/******/ 			});
/******/ 		} else {
/******/ 			fn.e = ensure;
/******/ 		}
/******/ 		return fn;
/******/ 	}
/******/ 	
/******/ 	function hotCreateModule(moduleId) { // eslint-disable-line no-unused-vars
/******/ 		var hot = {
/******/ 			// private stuff
/******/ 			_acceptedDependencies: {},
/******/ 			_declinedDependencies: {},
/******/ 			_selfAccepted: false,
/******/ 			_selfDeclined: false,
/******/ 			_disposeHandlers: [],
/******/ 	
/******/ 			// Module API
/******/ 			active: true,
/******/ 			accept: function(dep, callback) {
/******/ 				if(typeof dep === "undefined")
/******/ 					hot._selfAccepted = true;
/******/ 				else if(typeof dep === "function")
/******/ 					hot._selfAccepted = dep;
/******/ 				else if(typeof dep === "object")
/******/ 					for(var i = 0; i < dep.length; i++)
/******/ 						hot._acceptedDependencies[dep[i]] = callback;
/******/ 				else
/******/ 					hot._acceptedDependencies[dep] = callback;
/******/ 			},
/******/ 			decline: function(dep) {
/******/ 				if(typeof dep === "undefined")
/******/ 					hot._selfDeclined = true;
/******/ 				else if(typeof dep === "number")
/******/ 					hot._declinedDependencies[dep] = true;
/******/ 				else
/******/ 					for(var i = 0; i < dep.length; i++)
/******/ 						hot._declinedDependencies[dep[i]] = true;
/******/ 			},
/******/ 			dispose: function(callback) {
/******/ 				hot._disposeHandlers.push(callback);
/******/ 			},
/******/ 			addDisposeHandler: function(callback) {
/******/ 				hot._disposeHandlers.push(callback);
/******/ 			},
/******/ 			removeDisposeHandler: function(callback) {
/******/ 				var idx = hot._disposeHandlers.indexOf(callback);
/******/ 				if(idx >= 0) hot._disposeHandlers.splice(idx, 1);
/******/ 			},
/******/ 	
/******/ 			// Management API
/******/ 			check: hotCheck,
/******/ 			apply: hotApply,
/******/ 			status: function(l) {
/******/ 				if(!l) return hotStatus;
/******/ 				hotStatusHandlers.push(l);
/******/ 			},
/******/ 			addStatusHandler: function(l) {
/******/ 				hotStatusHandlers.push(l);
/******/ 			},
/******/ 			removeStatusHandler: function(l) {
/******/ 				var idx = hotStatusHandlers.indexOf(l);
/******/ 				if(idx >= 0) hotStatusHandlers.splice(idx, 1);
/******/ 			},
/******/ 	
/******/ 			//inherit from previous dispose call
/******/ 			data: hotCurrentModuleData[moduleId]
/******/ 		};
/******/ 		return hot;
/******/ 	}
/******/ 	
/******/ 	var hotStatusHandlers = [];
/******/ 	var hotStatus = "idle";
/******/ 	
/******/ 	function hotSetStatus(newStatus) {
/******/ 		hotStatus = newStatus;
/******/ 		for(var i = 0; i < hotStatusHandlers.length; i++)
/******/ 			hotStatusHandlers[i].call(null, newStatus);
/******/ 	}
/******/ 	
/******/ 	// while downloading
/******/ 	var hotWaitingFiles = 0;
/******/ 	var hotChunksLoading = 0;
/******/ 	var hotWaitingFilesMap = {};
/******/ 	var hotRequestedFilesMap = {};
/******/ 	var hotAvailibleFilesMap = {};
/******/ 	var hotCallback;
/******/ 	
/******/ 	// The update info
/******/ 	var hotUpdate, hotUpdateNewHash;
/******/ 	
/******/ 	function toModuleId(id) {
/******/ 		var isNumber = (+id) + "" === id;
/******/ 		return isNumber ? +id : id;
/******/ 	}
/******/ 	
/******/ 	function hotCheck(apply, callback) {
/******/ 		if(hotStatus !== "idle") throw new Error("check() is only allowed in idle status");
/******/ 		if(typeof apply === "function") {
/******/ 			hotApplyOnUpdate = false;
/******/ 			callback = apply;
/******/ 		} else {
/******/ 			hotApplyOnUpdate = apply;
/******/ 			callback = callback || function(err) {
/******/ 				if(err) throw err;
/******/ 			};
/******/ 		}
/******/ 		hotSetStatus("check");
/******/ 		hotDownloadManifest(function(err, update) {
/******/ 			if(err) return callback(err);
/******/ 			if(!update) {
/******/ 				hotSetStatus("idle");
/******/ 				callback(null, null);
/******/ 				return;
/******/ 			}
/******/ 	
/******/ 			hotRequestedFilesMap = {};
/******/ 			hotAvailibleFilesMap = {};
/******/ 			hotWaitingFilesMap = {};
/******/ 			for(var i = 0; i < update.c.length; i++)
/******/ 				hotAvailibleFilesMap[update.c[i]] = true;
/******/ 			hotUpdateNewHash = update.h;
/******/ 	
/******/ 			hotSetStatus("prepare");
/******/ 			hotCallback = callback;
/******/ 			hotUpdate = {};
/******/ 			for(var chunkId in installedChunks)
/******/ 			{ // eslint-disable-line no-lone-blocks
/******/ 				/*globals chunkId */
/******/ 				hotEnsureUpdateChunk(chunkId);
/******/ 			}
/******/ 			if(hotStatus === "prepare" && hotChunksLoading === 0 && hotWaitingFiles === 0) {
/******/ 				hotUpdateDownloaded();
/******/ 			}
/******/ 		});
/******/ 	}
/******/ 	
/******/ 	function hotAddUpdateChunk(chunkId, moreModules) { // eslint-disable-line no-unused-vars
/******/ 		if(!hotAvailibleFilesMap[chunkId] || !hotRequestedFilesMap[chunkId])
/******/ 			return;
/******/ 		hotRequestedFilesMap[chunkId] = false;
/******/ 		for(var moduleId in moreModules) {
/******/ 			if(Object.prototype.hasOwnProperty.call(moreModules, moduleId)) {
/******/ 				hotUpdate[moduleId] = moreModules[moduleId];
/******/ 			}
/******/ 		}
/******/ 		if(--hotWaitingFiles === 0 && hotChunksLoading === 0) {
/******/ 			hotUpdateDownloaded();
/******/ 		}
/******/ 	}
/******/ 	
/******/ 	function hotEnsureUpdateChunk(chunkId) {
/******/ 		if(!hotAvailibleFilesMap[chunkId]) {
/******/ 			hotWaitingFilesMap[chunkId] = true;
/******/ 		} else {
/******/ 			hotRequestedFilesMap[chunkId] = true;
/******/ 			hotWaitingFiles++;
/******/ 			hotDownloadUpdateChunk(chunkId);
/******/ 		}
/******/ 	}
/******/ 	
/******/ 	function hotUpdateDownloaded() {
/******/ 		hotSetStatus("ready");
/******/ 		var callback = hotCallback;
/******/ 		hotCallback = null;
/******/ 		if(!callback) return;
/******/ 		if(hotApplyOnUpdate) {
/******/ 			hotApply(hotApplyOnUpdate, callback);
/******/ 		} else {
/******/ 			var outdatedModules = [];
/******/ 			for(var id in hotUpdate) {
/******/ 				if(Object.prototype.hasOwnProperty.call(hotUpdate, id)) {
/******/ 					outdatedModules.push(toModuleId(id));
/******/ 				}
/******/ 			}
/******/ 			callback(null, outdatedModules);
/******/ 		}
/******/ 	}
/******/ 	
/******/ 	function hotApply(options, callback) {
/******/ 		if(hotStatus !== "ready") throw new Error("apply() is only allowed in ready status");
/******/ 		if(typeof options === "function") {
/******/ 			callback = options;
/******/ 			options = {};
/******/ 		} else if(options && typeof options === "object") {
/******/ 			callback = callback || function(err) {
/******/ 				if(err) throw err;
/******/ 			};
/******/ 		} else {
/******/ 			options = {};
/******/ 			callback = callback || function(err) {
/******/ 				if(err) throw err;
/******/ 			};
/******/ 		}
/******/ 	
/******/ 		function getAffectedStuff(module) {
/******/ 			var outdatedModules = [module];
/******/ 			var outdatedDependencies = {};
/******/ 	
/******/ 			var queue = outdatedModules.slice();
/******/ 			while(queue.length > 0) {
/******/ 				var moduleId = queue.pop();
/******/ 				var module = installedModules[moduleId];
/******/ 				if(!module || module.hot._selfAccepted)
/******/ 					continue;
/******/ 				if(module.hot._selfDeclined) {
/******/ 					return new Error("Aborted because of self decline: " + moduleId);
/******/ 				}
/******/ 				if(moduleId === 0) {
/******/ 					return;
/******/ 				}
/******/ 				for(var i = 0; i < module.parents.length; i++) {
/******/ 					var parentId = module.parents[i];
/******/ 					var parent = installedModules[parentId];
/******/ 					if(parent.hot._declinedDependencies[moduleId]) {
/******/ 						return new Error("Aborted because of declined dependency: " + moduleId + " in " + parentId);
/******/ 					}
/******/ 					if(outdatedModules.indexOf(parentId) >= 0) continue;
/******/ 					if(parent.hot._acceptedDependencies[moduleId]) {
/******/ 						if(!outdatedDependencies[parentId])
/******/ 							outdatedDependencies[parentId] = [];
/******/ 						addAllToSet(outdatedDependencies[parentId], [moduleId]);
/******/ 						continue;
/******/ 					}
/******/ 					delete outdatedDependencies[parentId];
/******/ 					outdatedModules.push(parentId);
/******/ 					queue.push(parentId);
/******/ 				}
/******/ 			}
/******/ 	
/******/ 			return [outdatedModules, outdatedDependencies];
/******/ 		}
/******/ 	
/******/ 		function addAllToSet(a, b) {
/******/ 			for(var i = 0; i < b.length; i++) {
/******/ 				var item = b[i];
/******/ 				if(a.indexOf(item) < 0)
/******/ 					a.push(item);
/******/ 			}
/******/ 		}
/******/ 	
/******/ 		// at begin all updates modules are outdated
/******/ 		// the "outdated" status can propagate to parents if they don't accept the children
/******/ 		var outdatedDependencies = {};
/******/ 		var outdatedModules = [];
/******/ 		var appliedUpdate = {};
/******/ 		for(var id in hotUpdate) {
/******/ 			if(Object.prototype.hasOwnProperty.call(hotUpdate, id)) {
/******/ 				var moduleId = toModuleId(id);
/******/ 				var result = getAffectedStuff(moduleId);
/******/ 				if(!result) {
/******/ 					if(options.ignoreUnaccepted)
/******/ 						continue;
/******/ 					hotSetStatus("abort");
/******/ 					return callback(new Error("Aborted because " + moduleId + " is not accepted"));
/******/ 				}
/******/ 				if(result instanceof Error) {
/******/ 					hotSetStatus("abort");
/******/ 					return callback(result);
/******/ 				}
/******/ 				appliedUpdate[moduleId] = hotUpdate[moduleId];
/******/ 				addAllToSet(outdatedModules, result[0]);
/******/ 				for(var moduleId in result[1]) {
/******/ 					if(Object.prototype.hasOwnProperty.call(result[1], moduleId)) {
/******/ 						if(!outdatedDependencies[moduleId])
/******/ 							outdatedDependencies[moduleId] = [];
/******/ 						addAllToSet(outdatedDependencies[moduleId], result[1][moduleId]);
/******/ 					}
/******/ 				}
/******/ 			}
/******/ 		}
/******/ 	
/******/ 		// Store self accepted outdated modules to require them later by the module system
/******/ 		var outdatedSelfAcceptedModules = [];
/******/ 		for(var i = 0; i < outdatedModules.length; i++) {
/******/ 			var moduleId = outdatedModules[i];
/******/ 			if(installedModules[moduleId] && installedModules[moduleId].hot._selfAccepted)
/******/ 				outdatedSelfAcceptedModules.push({
/******/ 					module: moduleId,
/******/ 					errorHandler: installedModules[moduleId].hot._selfAccepted
/******/ 				});
/******/ 		}
/******/ 	
/******/ 		// Now in "dispose" phase
/******/ 		hotSetStatus("dispose");
/******/ 		var queue = outdatedModules.slice();
/******/ 		while(queue.length > 0) {
/******/ 			var moduleId = queue.pop();
/******/ 			var module = installedModules[moduleId];
/******/ 			if(!module) continue;
/******/ 	
/******/ 			var data = {};
/******/ 	
/******/ 			// Call dispose handlers
/******/ 			var disposeHandlers = module.hot._disposeHandlers;
/******/ 			for(var j = 0; j < disposeHandlers.length; j++) {
/******/ 				var cb = disposeHandlers[j];
/******/ 				cb(data);
/******/ 			}
/******/ 			hotCurrentModuleData[moduleId] = data;
/******/ 	
/******/ 			// disable module (this disables requires from this module)
/******/ 			module.hot.active = false;
/******/ 	
/******/ 			// remove module from cache
/******/ 			delete installedModules[moduleId];
/******/ 	
/******/ 			// remove "parents" references from all children
/******/ 			for(var j = 0; j < module.children.length; j++) {
/******/ 				var child = installedModules[module.children[j]];
/******/ 				if(!child) continue;
/******/ 				var idx = child.parents.indexOf(moduleId);
/******/ 				if(idx >= 0) {
/******/ 					child.parents.splice(idx, 1);
/******/ 				}
/******/ 			}
/******/ 		}
/******/ 	
/******/ 		// remove outdated dependency from module children
/******/ 		for(var moduleId in outdatedDependencies) {
/******/ 			if(Object.prototype.hasOwnProperty.call(outdatedDependencies, moduleId)) {
/******/ 				var module = installedModules[moduleId];
/******/ 				var moduleOutdatedDependencies = outdatedDependencies[moduleId];
/******/ 				for(var j = 0; j < moduleOutdatedDependencies.length; j++) {
/******/ 					var dependency = moduleOutdatedDependencies[j];
/******/ 					var idx = module.children.indexOf(dependency);
/******/ 					if(idx >= 0) module.children.splice(idx, 1);
/******/ 				}
/******/ 			}
/******/ 		}
/******/ 	
/******/ 		// Not in "apply" phase
/******/ 		hotSetStatus("apply");
/******/ 	
/******/ 		hotCurrentHash = hotUpdateNewHash;
/******/ 	
/******/ 		// insert new code
/******/ 		for(var moduleId in appliedUpdate) {
/******/ 			if(Object.prototype.hasOwnProperty.call(appliedUpdate, moduleId)) {
/******/ 				modules[moduleId] = appliedUpdate[moduleId];
/******/ 			}
/******/ 		}
/******/ 	
/******/ 		// call accept handlers
/******/ 		var error = null;
/******/ 		for(var moduleId in outdatedDependencies) {
/******/ 			if(Object.prototype.hasOwnProperty.call(outdatedDependencies, moduleId)) {
/******/ 				var module = installedModules[moduleId];
/******/ 				var moduleOutdatedDependencies = outdatedDependencies[moduleId];
/******/ 				var callbacks = [];
/******/ 				for(var i = 0; i < moduleOutdatedDependencies.length; i++) {
/******/ 					var dependency = moduleOutdatedDependencies[i];
/******/ 					var cb = module.hot._acceptedDependencies[dependency];
/******/ 					if(callbacks.indexOf(cb) >= 0) continue;
/******/ 					callbacks.push(cb);
/******/ 				}
/******/ 				for(var i = 0; i < callbacks.length; i++) {
/******/ 					var cb = callbacks[i];
/******/ 					try {
/******/ 						cb(outdatedDependencies);
/******/ 					} catch(err) {
/******/ 						if(!error)
/******/ 							error = err;
/******/ 					}
/******/ 				}
/******/ 			}
/******/ 		}
/******/ 	
/******/ 		// Load self accepted modules
/******/ 		for(var i = 0; i < outdatedSelfAcceptedModules.length; i++) {
/******/ 			var item = outdatedSelfAcceptedModules[i];
/******/ 			var moduleId = item.module;
/******/ 			hotCurrentParents = [moduleId];
/******/ 			try {
/******/ 				__webpack_require__(moduleId);
/******/ 			} catch(err) {
/******/ 				if(typeof item.errorHandler === "function") {
/******/ 					try {
/******/ 						item.errorHandler(err);
/******/ 					} catch(err) {
/******/ 						if(!error)
/******/ 							error = err;
/******/ 					}
/******/ 				} else if(!error)
/******/ 					error = err;
/******/ 			}
/******/ 		}
/******/ 	
/******/ 		// handle errors in accept handlers and self accepted module load
/******/ 		if(error) {
/******/ 			hotSetStatus("fail");
/******/ 			return callback(error);
/******/ 		}
/******/ 	
/******/ 		hotSetStatus("idle");
/******/ 		callback(null, outdatedModules);
/******/ 	}
/******/
/******/ 	// The module cache
/******/ 	var installedModules = {};
/******/
/******/ 	// object to store loaded and loading chunks
/******/ 	// "0" means "already loaded"
/******/ 	// Array means "loading", array contains callbacks
/******/ 	var installedChunks = {
/******/ 		10:0
/******/ 	};
/******/
/******/ 	// The require function
/******/ 	function __webpack_require__(moduleId) {
/******/
/******/ 		// Check if module is in cache
/******/ 		if(installedModules[moduleId])
/******/ 			return installedModules[moduleId].exports;
/******/
/******/ 		// Create a new module (and put it into the cache)
/******/ 		var module = installedModules[moduleId] = {
/******/ 			exports: {},
/******/ 			id: moduleId,
/******/ 			loaded: false,
/******/ 			hot: hotCreateModule(moduleId),
/******/ 			parents: hotCurrentParents,
/******/ 			children: []
/******/ 		};
/******/
/******/ 		// Execute the module function
/******/ 		modules[moduleId].call(module.exports, module, module.exports, hotCreateRequire(moduleId));
/******/
/******/ 		// Flag the module as loaded
/******/ 		module.loaded = true;
/******/
/******/ 		// Return the exports of the module
/******/ 		return module.exports;
/******/ 	}
/******/
/******/ 	// This file contains only the entry chunk.
/******/ 	// The chunk loading function for additional chunks
/******/ 	__webpack_require__.e = function requireEnsure(chunkId, callback) {
/******/ 		// "0" is the signal for "already loaded"
/******/ 		if(installedChunks[chunkId] === 0)
/******/ 			return callback.call(null, __webpack_require__);
/******/
/******/ 		// an array means "currently loading".
/******/ 		if(installedChunks[chunkId] !== undefined) {
/******/ 			installedChunks[chunkId].push(callback);
/******/ 		} else {
/******/ 			// start chunk loading
/******/ 			installedChunks[chunkId] = [callback];
/******/ 			var head = document.getElementsByTagName('head')[0];
/******/ 			var script = document.createElement('script');
/******/ 			script.type = 'text/javascript';
/******/ 			script.charset = 'utf-8';
/******/ 			script.async = true;
/******/
/******/ 			script.src = __webpack_require__.p + "" + chunkId + ".chunk.js";
/******/ 			head.appendChild(script);
/******/ 		}
/******/ 	};
/******/
/******/ 	// expose the modules object (__webpack_modules__)
/******/ 	__webpack_require__.m = modules;
/******/
/******/ 	// expose the module cache
/******/ 	__webpack_require__.c = installedModules;
/******/
/******/ 	// __webpack_public_path__
/******/ 	__webpack_require__.p = "/";
/******/
/******/ 	// __webpack_hash__
/******/ 	__webpack_require__.h = function() { return hotCurrentHash; };
/******/ })
/************************************************************************/
/******/ ([
/* 0 */,
/* 1 */,
/* 2 */
/*!*********************************!*\
  !*** ./~/jquery/dist/jquery.js ***!
  \*********************************/
/***/ function(module, exports, __webpack_require__) {

	var __WEBPACK_AMD_DEFINE_ARRAY__, __WEBPACK_AMD_DEFINE_RESULT__;/*!
	 * jQuery JavaScript Library v1.12.4
	 * http://jquery.com/
	 *
	 * Includes Sizzle.js
	 * http://sizzlejs.com/
	 *
	 * Copyright jQuery Foundation and other contributors
	 * Released under the MIT license
	 * http://jquery.org/license
	 *
	 * Date: 2016-05-20T17:17Z
	 */
	
	(function( global, factory ) {
	
		if ( typeof module === "object" && typeof module.exports === "object" ) {
			// For CommonJS and CommonJS-like environments where a proper `window`
			// is present, execute the factory and get jQuery.
			// For environments that do not have a `window` with a `document`
			// (such as Node.js), expose a factory as module.exports.
			// This accentuates the need for the creation of a real `window`.
			// e.g. var jQuery = require("jquery")(window);
			// See ticket #14549 for more info.
			module.exports = global.document ?
				factory( global, true ) :
				function( w ) {
					if ( !w.document ) {
						throw new Error( "jQuery requires a window with a document" );
					}
					return factory( w );
				};
		} else {
			factory( global );
		}
	
	// Pass this if window is not defined yet
	}(typeof window !== "undefined" ? window : this, function( window, noGlobal ) {
	
	// Support: Firefox 18+
	// Can't be in strict mode, several libs including ASP.NET trace
	// the stack via arguments.caller.callee and Firefox dies if
	// you try to trace through "use strict" call chains. (#13335)
	//"use strict";
	var deletedIds = [];
	
	var document = window.document;
	
	var slice = deletedIds.slice;
	
	var concat = deletedIds.concat;
	
	var push = deletedIds.push;
	
	var indexOf = deletedIds.indexOf;
	
	var class2type = {};
	
	var toString = class2type.toString;
	
	var hasOwn = class2type.hasOwnProperty;
	
	var support = {};
	
	
	
	var
		version = "1.12.4",
	
		// Define a local copy of jQuery
		jQuery = function( selector, context ) {
	
			// The jQuery object is actually just the init constructor 'enhanced'
			// Need init if jQuery is called (just allow error to be thrown if not included)
			return new jQuery.fn.init( selector, context );
		},
	
		// Support: Android<4.1, IE<9
		// Make sure we trim BOM and NBSP
		rtrim = /^[\s\uFEFF\xA0]+|[\s\uFEFF\xA0]+$/g,
	
		// Matches dashed string for camelizing
		rmsPrefix = /^-ms-/,
		rdashAlpha = /-([\da-z])/gi,
	
		// Used by jQuery.camelCase as callback to replace()
		fcamelCase = function( all, letter ) {
			return letter.toUpperCase();
		};
	
	jQuery.fn = jQuery.prototype = {
	
		// The current version of jQuery being used
		jquery: version,
	
		constructor: jQuery,
	
		// Start with an empty selector
		selector: "",
	
		// The default length of a jQuery object is 0
		length: 0,
	
		toArray: function() {
			return slice.call( this );
		},
	
		// Get the Nth element in the matched element set OR
		// Get the whole matched element set as a clean array
		get: function( num ) {
			return num != null ?
	
				// Return just the one element from the set
				( num < 0 ? this[ num + this.length ] : this[ num ] ) :
	
				// Return all the elements in a clean array
				slice.call( this );
		},
	
		// Take an array of elements and push it onto the stack
		// (returning the new matched element set)
		pushStack: function( elems ) {
	
			// Build a new jQuery matched element set
			var ret = jQuery.merge( this.constructor(), elems );
	
			// Add the old object onto the stack (as a reference)
			ret.prevObject = this;
			ret.context = this.context;
	
			// Return the newly-formed element set
			return ret;
		},
	
		// Execute a callback for every element in the matched set.
		each: function( callback ) {
			return jQuery.each( this, callback );
		},
	
		map: function( callback ) {
			return this.pushStack( jQuery.map( this, function( elem, i ) {
				return callback.call( elem, i, elem );
			} ) );
		},
	
		slice: function() {
			return this.pushStack( slice.apply( this, arguments ) );
		},
	
		first: function() {
			return this.eq( 0 );
		},
	
		last: function() {
			return this.eq( -1 );
		},
	
		eq: function( i ) {
			var len = this.length,
				j = +i + ( i < 0 ? len : 0 );
			return this.pushStack( j >= 0 && j < len ? [ this[ j ] ] : [] );
		},
	
		end: function() {
			return this.prevObject || this.constructor();
		},
	
		// For internal use only.
		// Behaves like an Array's method, not like a jQuery method.
		push: push,
		sort: deletedIds.sort,
		splice: deletedIds.splice
	};
	
	jQuery.extend = jQuery.fn.extend = function() {
		var src, copyIsArray, copy, name, options, clone,
			target = arguments[ 0 ] || {},
			i = 1,
			length = arguments.length,
			deep = false;
	
		// Handle a deep copy situation
		if ( typeof target === "boolean" ) {
			deep = target;
	
			// skip the boolean and the target
			target = arguments[ i ] || {};
			i++;
		}
	
		// Handle case when target is a string or something (possible in deep copy)
		if ( typeof target !== "object" && !jQuery.isFunction( target ) ) {
			target = {};
		}
	
		// extend jQuery itself if only one argument is passed
		if ( i === length ) {
			target = this;
			i--;
		}
	
		for ( ; i < length; i++ ) {
	
			// Only deal with non-null/undefined values
			if ( ( options = arguments[ i ] ) != null ) {
	
				// Extend the base object
				for ( name in options ) {
					src = target[ name ];
					copy = options[ name ];
	
					// Prevent never-ending loop
					if ( target === copy ) {
						continue;
					}
	
					// Recurse if we're merging plain objects or arrays
					if ( deep && copy && ( jQuery.isPlainObject( copy ) ||
						( copyIsArray = jQuery.isArray( copy ) ) ) ) {
	
						if ( copyIsArray ) {
							copyIsArray = false;
							clone = src && jQuery.isArray( src ) ? src : [];
	
						} else {
							clone = src && jQuery.isPlainObject( src ) ? src : {};
						}
	
						// Never move original objects, clone them
						target[ name ] = jQuery.extend( deep, clone, copy );
	
					// Don't bring in undefined values
					} else if ( copy !== undefined ) {
						target[ name ] = copy;
					}
				}
			}
		}
	
		// Return the modified object
		return target;
	};
	
	jQuery.extend( {
	
		// Unique for each copy of jQuery on the page
		expando: "jQuery" + ( version + Math.random() ).replace( /\D/g, "" ),
	
		// Assume jQuery is ready without the ready module
		isReady: true,
	
		error: function( msg ) {
			throw new Error( msg );
		},
	
		noop: function() {},
	
		// See test/unit/core.js for details concerning isFunction.
		// Since version 1.3, DOM methods and functions like alert
		// aren't supported. They return false on IE (#2968).
		isFunction: function( obj ) {
			return jQuery.type( obj ) === "function";
		},
	
		isArray: Array.isArray || function( obj ) {
			return jQuery.type( obj ) === "array";
		},
	
		isWindow: function( obj ) {
			/* jshint eqeqeq: false */
			return obj != null && obj == obj.window;
		},
	
		isNumeric: function( obj ) {
	
			// parseFloat NaNs numeric-cast false positives (null|true|false|"")
			// ...but misinterprets leading-number strings, particularly hex literals ("0x...")
			// subtraction forces infinities to NaN
			// adding 1 corrects loss of precision from parseFloat (#15100)
			var realStringObj = obj && obj.toString();
			return !jQuery.isArray( obj ) && ( realStringObj - parseFloat( realStringObj ) + 1 ) >= 0;
		},
	
		isEmptyObject: function( obj ) {
			var name;
			for ( name in obj ) {
				return false;
			}
			return true;
		},
	
		isPlainObject: function( obj ) {
			var key;
	
			// Must be an Object.
			// Because of IE, we also have to check the presence of the constructor property.
			// Make sure that DOM nodes and window objects don't pass through, as well
			if ( !obj || jQuery.type( obj ) !== "object" || obj.nodeType || jQuery.isWindow( obj ) ) {
				return false;
			}
	
			try {
	
				// Not own constructor property must be Object
				if ( obj.constructor &&
					!hasOwn.call( obj, "constructor" ) &&
					!hasOwn.call( obj.constructor.prototype, "isPrototypeOf" ) ) {
					return false;
				}
			} catch ( e ) {
	
				// IE8,9 Will throw exceptions on certain host objects #9897
				return false;
			}
	
			// Support: IE<9
			// Handle iteration over inherited properties before own properties.
			if ( !support.ownFirst ) {
				for ( key in obj ) {
					return hasOwn.call( obj, key );
				}
			}
	
			// Own properties are enumerated firstly, so to speed up,
			// if last one is own, then all properties are own.
			for ( key in obj ) {}
	
			return key === undefined || hasOwn.call( obj, key );
		},
	
		type: function( obj ) {
			if ( obj == null ) {
				return obj + "";
			}
			return typeof obj === "object" || typeof obj === "function" ?
				class2type[ toString.call( obj ) ] || "object" :
				typeof obj;
		},
	
		// Workarounds based on findings by Jim Driscoll
		// http://weblogs.java.net/blog/driscoll/archive/2009/09/08/eval-javascript-global-context
		globalEval: function( data ) {
			if ( data && jQuery.trim( data ) ) {
	
				// We use execScript on Internet Explorer
				// We use an anonymous function so that context is window
				// rather than jQuery in Firefox
				( window.execScript || function( data ) {
					window[ "eval" ].call( window, data ); // jscs:ignore requireDotNotation
				} )( data );
			}
		},
	
		// Convert dashed to camelCase; used by the css and data modules
		// Microsoft forgot to hump their vendor prefix (#9572)
		camelCase: function( string ) {
			return string.replace( rmsPrefix, "ms-" ).replace( rdashAlpha, fcamelCase );
		},
	
		nodeName: function( elem, name ) {
			return elem.nodeName && elem.nodeName.toLowerCase() === name.toLowerCase();
		},
	
		each: function( obj, callback ) {
			var length, i = 0;
	
			if ( isArrayLike( obj ) ) {
				length = obj.length;
				for ( ; i < length; i++ ) {
					if ( callback.call( obj[ i ], i, obj[ i ] ) === false ) {
						break;
					}
				}
			} else {
				for ( i in obj ) {
					if ( callback.call( obj[ i ], i, obj[ i ] ) === false ) {
						break;
					}
				}
			}
	
			return obj;
		},
	
		// Support: Android<4.1, IE<9
		trim: function( text ) {
			return text == null ?
				"" :
				( text + "" ).replace( rtrim, "" );
		},
	
		// results is for internal usage only
		makeArray: function( arr, results ) {
			var ret = results || [];
	
			if ( arr != null ) {
				if ( isArrayLike( Object( arr ) ) ) {
					jQuery.merge( ret,
						typeof arr === "string" ?
						[ arr ] : arr
					);
				} else {
					push.call( ret, arr );
				}
			}
	
			return ret;
		},
	
		inArray: function( elem, arr, i ) {
			var len;
	
			if ( arr ) {
				if ( indexOf ) {
					return indexOf.call( arr, elem, i );
				}
	
				len = arr.length;
				i = i ? i < 0 ? Math.max( 0, len + i ) : i : 0;
	
				for ( ; i < len; i++ ) {
	
					// Skip accessing in sparse arrays
					if ( i in arr && arr[ i ] === elem ) {
						return i;
					}
				}
			}
	
			return -1;
		},
	
		merge: function( first, second ) {
			var len = +second.length,
				j = 0,
				i = first.length;
	
			while ( j < len ) {
				first[ i++ ] = second[ j++ ];
			}
	
			// Support: IE<9
			// Workaround casting of .length to NaN on otherwise arraylike objects (e.g., NodeLists)
			if ( len !== len ) {
				while ( second[ j ] !== undefined ) {
					first[ i++ ] = second[ j++ ];
				}
			}
	
			first.length = i;
	
			return first;
		},
	
		grep: function( elems, callback, invert ) {
			var callbackInverse,
				matches = [],
				i = 0,
				length = elems.length,
				callbackExpect = !invert;
	
			// Go through the array, only saving the items
			// that pass the validator function
			for ( ; i < length; i++ ) {
				callbackInverse = !callback( elems[ i ], i );
				if ( callbackInverse !== callbackExpect ) {
					matches.push( elems[ i ] );
				}
			}
	
			return matches;
		},
	
		// arg is for internal usage only
		map: function( elems, callback, arg ) {
			var length, value,
				i = 0,
				ret = [];
	
			// Go through the array, translating each of the items to their new values
			if ( isArrayLike( elems ) ) {
				length = elems.length;
				for ( ; i < length; i++ ) {
					value = callback( elems[ i ], i, arg );
	
					if ( value != null ) {
						ret.push( value );
					}
				}
	
			// Go through every key on the object,
			} else {
				for ( i in elems ) {
					value = callback( elems[ i ], i, arg );
	
					if ( value != null ) {
						ret.push( value );
					}
				}
			}
	
			// Flatten any nested arrays
			return concat.apply( [], ret );
		},
	
		// A global GUID counter for objects
		guid: 1,
	
		// Bind a function to a context, optionally partially applying any
		// arguments.
		proxy: function( fn, context ) {
			var args, proxy, tmp;
	
			if ( typeof context === "string" ) {
				tmp = fn[ context ];
				context = fn;
				fn = tmp;
			}
	
			// Quick check to determine if target is callable, in the spec
			// this throws a TypeError, but we will just return undefined.
			if ( !jQuery.isFunction( fn ) ) {
				return undefined;
			}
	
			// Simulated bind
			args = slice.call( arguments, 2 );
			proxy = function() {
				return fn.apply( context || this, args.concat( slice.call( arguments ) ) );
			};
	
			// Set the guid of unique handler to the same of original handler, so it can be removed
			proxy.guid = fn.guid = fn.guid || jQuery.guid++;
	
			return proxy;
		},
	
		now: function() {
			return +( new Date() );
		},
	
		// jQuery.support is not used in Core but other projects attach their
		// properties to it so it needs to exist.
		support: support
	} );
	
	// JSHint would error on this code due to the Symbol not being defined in ES5.
	// Defining this global in .jshintrc would create a danger of using the global
	// unguarded in another place, it seems safer to just disable JSHint for these
	// three lines.
	/* jshint ignore: start */
	if ( typeof Symbol === "function" ) {
		jQuery.fn[ Symbol.iterator ] = deletedIds[ Symbol.iterator ];
	}
	/* jshint ignore: end */
	
	// Populate the class2type map
	jQuery.each( "Boolean Number String Function Array Date RegExp Object Error Symbol".split( " " ),
	function( i, name ) {
		class2type[ "[object " + name + "]" ] = name.toLowerCase();
	} );
	
	function isArrayLike( obj ) {
	
		// Support: iOS 8.2 (not reproducible in simulator)
		// `in` check used to prevent JIT error (gh-2145)
		// hasOwn isn't used here due to false negatives
		// regarding Nodelist length in IE
		var length = !!obj && "length" in obj && obj.length,
			type = jQuery.type( obj );
	
		if ( type === "function" || jQuery.isWindow( obj ) ) {
			return false;
		}
	
		return type === "array" || length === 0 ||
			typeof length === "number" && length > 0 && ( length - 1 ) in obj;
	}
	var Sizzle =
	/*!
	 * Sizzle CSS Selector Engine v2.2.1
	 * http://sizzlejs.com/
	 *
	 * Copyright jQuery Foundation and other contributors
	 * Released under the MIT license
	 * http://jquery.org/license
	 *
	 * Date: 2015-10-17
	 */
	(function( window ) {
	
	var i,
		support,
		Expr,
		getText,
		isXML,
		tokenize,
		compile,
		select,
		outermostContext,
		sortInput,
		hasDuplicate,
	
		// Local document vars
		setDocument,
		document,
		docElem,
		documentIsHTML,
		rbuggyQSA,
		rbuggyMatches,
		matches,
		contains,
	
		// Instance-specific data
		expando = "sizzle" + 1 * new Date(),
		preferredDoc = window.document,
		dirruns = 0,
		done = 0,
		classCache = createCache(),
		tokenCache = createCache(),
		compilerCache = createCache(),
		sortOrder = function( a, b ) {
			if ( a === b ) {
				hasDuplicate = true;
			}
			return 0;
		},
	
		// General-purpose constants
		MAX_NEGATIVE = 1 << 31,
	
		// Instance methods
		hasOwn = ({}).hasOwnProperty,
		arr = [],
		pop = arr.pop,
		push_native = arr.push,
		push = arr.push,
		slice = arr.slice,
		// Use a stripped-down indexOf as it's faster than native
		// http://jsperf.com/thor-indexof-vs-for/5
		indexOf = function( list, elem ) {
			var i = 0,
				len = list.length;
			for ( ; i < len; i++ ) {
				if ( list[i] === elem ) {
					return i;
				}
			}
			return -1;
		},
	
		booleans = "checked|selected|async|autofocus|autoplay|controls|defer|disabled|hidden|ismap|loop|multiple|open|readonly|required|scoped",
	
		// Regular expressions
	
		// http://www.w3.org/TR/css3-selectors/#whitespace
		whitespace = "[\\x20\\t\\r\\n\\f]",
	
		// http://www.w3.org/TR/CSS21/syndata.html#value-def-identifier
		identifier = "(?:\\\\.|[\\w-]|[^\\x00-\\xa0])+",
	
		// Attribute selectors: http://www.w3.org/TR/selectors/#attribute-selectors
		attributes = "\\[" + whitespace + "*(" + identifier + ")(?:" + whitespace +
			// Operator (capture 2)
			"*([*^$|!~]?=)" + whitespace +
			// "Attribute values must be CSS identifiers [capture 5] or strings [capture 3 or capture 4]"
			"*(?:'((?:\\\\.|[^\\\\'])*)'|\"((?:\\\\.|[^\\\\\"])*)\"|(" + identifier + "))|)" + whitespace +
			"*\\]",
	
		pseudos = ":(" + identifier + ")(?:\\((" +
			// To reduce the number of selectors needing tokenize in the preFilter, prefer arguments:
			// 1. quoted (capture 3; capture 4 or capture 5)
			"('((?:\\\\.|[^\\\\'])*)'|\"((?:\\\\.|[^\\\\\"])*)\")|" +
			// 2. simple (capture 6)
			"((?:\\\\.|[^\\\\()[\\]]|" + attributes + ")*)|" +
			// 3. anything else (capture 2)
			".*" +
			")\\)|)",
	
		// Leading and non-escaped trailing whitespace, capturing some non-whitespace characters preceding the latter
		rwhitespace = new RegExp( whitespace + "+", "g" ),
		rtrim = new RegExp( "^" + whitespace + "+|((?:^|[^\\\\])(?:\\\\.)*)" + whitespace + "+$", "g" ),
	
		rcomma = new RegExp( "^" + whitespace + "*," + whitespace + "*" ),
		rcombinators = new RegExp( "^" + whitespace + "*([>+~]|" + whitespace + ")" + whitespace + "*" ),
	
		rattributeQuotes = new RegExp( "=" + whitespace + "*([^\\]'\"]*?)" + whitespace + "*\\]", "g" ),
	
		rpseudo = new RegExp( pseudos ),
		ridentifier = new RegExp( "^" + identifier + "$" ),
	
		matchExpr = {
			"ID": new RegExp( "^#(" + identifier + ")" ),
			"CLASS": new RegExp( "^\\.(" + identifier + ")" ),
			"TAG": new RegExp( "^(" + identifier + "|[*])" ),
			"ATTR": new RegExp( "^" + attributes ),
			"PSEUDO": new RegExp( "^" + pseudos ),
			"CHILD": new RegExp( "^:(only|first|last|nth|nth-last)-(child|of-type)(?:\\(" + whitespace +
				"*(even|odd|(([+-]|)(\\d*)n|)" + whitespace + "*(?:([+-]|)" + whitespace +
				"*(\\d+)|))" + whitespace + "*\\)|)", "i" ),
			"bool": new RegExp( "^(?:" + booleans + ")$", "i" ),
			// For use in libraries implementing .is()
			// We use this for POS matching in `select`
			"needsContext": new RegExp( "^" + whitespace + "*[>+~]|:(even|odd|eq|gt|lt|nth|first|last)(?:\\(" +
				whitespace + "*((?:-\\d)?\\d*)" + whitespace + "*\\)|)(?=[^-]|$)", "i" )
		},
	
		rinputs = /^(?:input|select|textarea|button)$/i,
		rheader = /^h\d$/i,
	
		rnative = /^[^{]+\{\s*\[native \w/,
	
		// Easily-parseable/retrievable ID or TAG or CLASS selectors
		rquickExpr = /^(?:#([\w-]+)|(\w+)|\.([\w-]+))$/,
	
		rsibling = /[+~]/,
		rescape = /'|\\/g,
	
		// CSS escapes http://www.w3.org/TR/CSS21/syndata.html#escaped-characters
		runescape = new RegExp( "\\\\([\\da-f]{1,6}" + whitespace + "?|(" + whitespace + ")|.)", "ig" ),
		funescape = function( _, escaped, escapedWhitespace ) {
			var high = "0x" + escaped - 0x10000;
			// NaN means non-codepoint
			// Support: Firefox<24
			// Workaround erroneous numeric interpretation of +"0x"
			return high !== high || escapedWhitespace ?
				escaped :
				high < 0 ?
					// BMP codepoint
					String.fromCharCode( high + 0x10000 ) :
					// Supplemental Plane codepoint (surrogate pair)
					String.fromCharCode( high >> 10 | 0xD800, high & 0x3FF | 0xDC00 );
		},
	
		// Used for iframes
		// See setDocument()
		// Removing the function wrapper causes a "Permission Denied"
		// error in IE
		unloadHandler = function() {
			setDocument();
		};
	
	// Optimize for push.apply( _, NodeList )
	try {
		push.apply(
			(arr = slice.call( preferredDoc.childNodes )),
			preferredDoc.childNodes
		);
		// Support: Android<4.0
		// Detect silently failing push.apply
		arr[ preferredDoc.childNodes.length ].nodeType;
	} catch ( e ) {
		push = { apply: arr.length ?
	
			// Leverage slice if possible
			function( target, els ) {
				push_native.apply( target, slice.call(els) );
			} :
	
			// Support: IE<9
			// Otherwise append directly
			function( target, els ) {
				var j = target.length,
					i = 0;
				// Can't trust NodeList.length
				while ( (target[j++] = els[i++]) ) {}
				target.length = j - 1;
			}
		};
	}
	
	function Sizzle( selector, context, results, seed ) {
		var m, i, elem, nid, nidselect, match, groups, newSelector,
			newContext = context && context.ownerDocument,
	
			// nodeType defaults to 9, since context defaults to document
			nodeType = context ? context.nodeType : 9;
	
		results = results || [];
	
		// Return early from calls with invalid selector or context
		if ( typeof selector !== "string" || !selector ||
			nodeType !== 1 && nodeType !== 9 && nodeType !== 11 ) {
	
			return results;
		}
	
		// Try to shortcut find operations (as opposed to filters) in HTML documents
		if ( !seed ) {
	
			if ( ( context ? context.ownerDocument || context : preferredDoc ) !== document ) {
				setDocument( context );
			}
			context = context || document;
	
			if ( documentIsHTML ) {
	
				// If the selector is sufficiently simple, try using a "get*By*" DOM method
				// (excepting DocumentFragment context, where the methods don't exist)
				if ( nodeType !== 11 && (match = rquickExpr.exec( selector )) ) {
	
					// ID selector
					if ( (m = match[1]) ) {
	
						// Document context
						if ( nodeType === 9 ) {
							if ( (elem = context.getElementById( m )) ) {
	
								// Support: IE, Opera, Webkit
								// TODO: identify versions
								// getElementById can match elements by name instead of ID
								if ( elem.id === m ) {
									results.push( elem );
									return results;
								}
							} else {
								return results;
							}
	
						// Element context
						} else {
	
							// Support: IE, Opera, Webkit
							// TODO: identify versions
							// getElementById can match elements by name instead of ID
							if ( newContext && (elem = newContext.getElementById( m )) &&
								contains( context, elem ) &&
								elem.id === m ) {
	
								results.push( elem );
								return results;
							}
						}
	
					// Type selector
					} else if ( match[2] ) {
						push.apply( results, context.getElementsByTagName( selector ) );
						return results;
	
					// Class selector
					} else if ( (m = match[3]) && support.getElementsByClassName &&
						context.getElementsByClassName ) {
	
						push.apply( results, context.getElementsByClassName( m ) );
						return results;
					}
				}
	
				// Take advantage of querySelectorAll
				if ( support.qsa &&
					!compilerCache[ selector + " " ] &&
					(!rbuggyQSA || !rbuggyQSA.test( selector )) ) {
	
					if ( nodeType !== 1 ) {
						newContext = context;
						newSelector = selector;
	
					// qSA looks outside Element context, which is not what we want
					// Thanks to Andrew Dupont for this workaround technique
					// Support: IE <=8
					// Exclude object elements
					} else if ( context.nodeName.toLowerCase() !== "object" ) {
	
						// Capture the context ID, setting it first if necessary
						if ( (nid = context.getAttribute( "id" )) ) {
							nid = nid.replace( rescape, "\\$&" );
						} else {
							context.setAttribute( "id", (nid = expando) );
						}
	
						// Prefix every selector in the list
						groups = tokenize( selector );
						i = groups.length;
						nidselect = ridentifier.test( nid ) ? "#" + nid : "[id='" + nid + "']";
						while ( i-- ) {
							groups[i] = nidselect + " " + toSelector( groups[i] );
						}
						newSelector = groups.join( "," );
	
						// Expand context for sibling selectors
						newContext = rsibling.test( selector ) && testContext( context.parentNode ) ||
							context;
					}
	
					if ( newSelector ) {
						try {
							push.apply( results,
								newContext.querySelectorAll( newSelector )
							);
							return results;
						} catch ( qsaError ) {
						} finally {
							if ( nid === expando ) {
								context.removeAttribute( "id" );
							}
						}
					}
				}
			}
		}
	
		// All others
		return select( selector.replace( rtrim, "$1" ), context, results, seed );
	}
	
	/**
	 * Create key-value caches of limited size
	 * @returns {function(string, object)} Returns the Object data after storing it on itself with
	 *	property name the (space-suffixed) string and (if the cache is larger than Expr.cacheLength)
	 *	deleting the oldest entry
	 */
	function createCache() {
		var keys = [];
	
		function cache( key, value ) {
			// Use (key + " ") to avoid collision with native prototype properties (see Issue #157)
			if ( keys.push( key + " " ) > Expr.cacheLength ) {
				// Only keep the most recent entries
				delete cache[ keys.shift() ];
			}
			return (cache[ key + " " ] = value);
		}
		return cache;
	}
	
	/**
	 * Mark a function for special use by Sizzle
	 * @param {Function} fn The function to mark
	 */
	function markFunction( fn ) {
		fn[ expando ] = true;
		return fn;
	}
	
	/**
	 * Support testing using an element
	 * @param {Function} fn Passed the created div and expects a boolean result
	 */
	function assert( fn ) {
		var div = document.createElement("div");
	
		try {
			return !!fn( div );
		} catch (e) {
			return false;
		} finally {
			// Remove from its parent by default
			if ( div.parentNode ) {
				div.parentNode.removeChild( div );
			}
			// release memory in IE
			div = null;
		}
	}
	
	/**
	 * Adds the same handler for all of the specified attrs
	 * @param {String} attrs Pipe-separated list of attributes
	 * @param {Function} handler The method that will be applied
	 */
	function addHandle( attrs, handler ) {
		var arr = attrs.split("|"),
			i = arr.length;
	
		while ( i-- ) {
			Expr.attrHandle[ arr[i] ] = handler;
		}
	}
	
	/**
	 * Checks document order of two siblings
	 * @param {Element} a
	 * @param {Element} b
	 * @returns {Number} Returns less than 0 if a precedes b, greater than 0 if a follows b
	 */
	function siblingCheck( a, b ) {
		var cur = b && a,
			diff = cur && a.nodeType === 1 && b.nodeType === 1 &&
				( ~b.sourceIndex || MAX_NEGATIVE ) -
				( ~a.sourceIndex || MAX_NEGATIVE );
	
		// Use IE sourceIndex if available on both nodes
		if ( diff ) {
			return diff;
		}
	
		// Check if b follows a
		if ( cur ) {
			while ( (cur = cur.nextSibling) ) {
				if ( cur === b ) {
					return -1;
				}
			}
		}
	
		return a ? 1 : -1;
	}
	
	/**
	 * Returns a function to use in pseudos for input types
	 * @param {String} type
	 */
	function createInputPseudo( type ) {
		return function( elem ) {
			var name = elem.nodeName.toLowerCase();
			return name === "input" && elem.type === type;
		};
	}
	
	/**
	 * Returns a function to use in pseudos for buttons
	 * @param {String} type
	 */
	function createButtonPseudo( type ) {
		return function( elem ) {
			var name = elem.nodeName.toLowerCase();
			return (name === "input" || name === "button") && elem.type === type;
		};
	}
	
	/**
	 * Returns a function to use in pseudos for positionals
	 * @param {Function} fn
	 */
	function createPositionalPseudo( fn ) {
		return markFunction(function( argument ) {
			argument = +argument;
			return markFunction(function( seed, matches ) {
				var j,
					matchIndexes = fn( [], seed.length, argument ),
					i = matchIndexes.length;
	
				// Match elements found at the specified indexes
				while ( i-- ) {
					if ( seed[ (j = matchIndexes[i]) ] ) {
						seed[j] = !(matches[j] = seed[j]);
					}
				}
			});
		});
	}
	
	/**
	 * Checks a node for validity as a Sizzle context
	 * @param {Element|Object=} context
	 * @returns {Element|Object|Boolean} The input node if acceptable, otherwise a falsy value
	 */
	function testContext( context ) {
		return context && typeof context.getElementsByTagName !== "undefined" && context;
	}
	
	// Expose support vars for convenience
	support = Sizzle.support = {};
	
	/**
	 * Detects XML nodes
	 * @param {Element|Object} elem An element or a document
	 * @returns {Boolean} True iff elem is a non-HTML XML node
	 */
	isXML = Sizzle.isXML = function( elem ) {
		// documentElement is verified for cases where it doesn't yet exist
		// (such as loading iframes in IE - #4833)
		var documentElement = elem && (elem.ownerDocument || elem).documentElement;
		return documentElement ? documentElement.nodeName !== "HTML" : false;
	};
	
	/**
	 * Sets document-related variables once based on the current document
	 * @param {Element|Object} [doc] An element or document object to use to set the document
	 * @returns {Object} Returns the current document
	 */
	setDocument = Sizzle.setDocument = function( node ) {
		var hasCompare, parent,
			doc = node ? node.ownerDocument || node : preferredDoc;
	
		// Return early if doc is invalid or already selected
		if ( doc === document || doc.nodeType !== 9 || !doc.documentElement ) {
			return document;
		}
	
		// Update global variables
		document = doc;
		docElem = document.documentElement;
		documentIsHTML = !isXML( document );
	
		// Support: IE 9-11, Edge
		// Accessing iframe documents after unload throws "permission denied" errors (jQuery #13936)
		if ( (parent = document.defaultView) && parent.top !== parent ) {
			// Support: IE 11
			if ( parent.addEventListener ) {
				parent.addEventListener( "unload", unloadHandler, false );
	
			// Support: IE 9 - 10 only
			} else if ( parent.attachEvent ) {
				parent.attachEvent( "onunload", unloadHandler );
			}
		}
	
		/* Attributes
		---------------------------------------------------------------------- */
	
		// Support: IE<8
		// Verify that getAttribute really returns attributes and not properties
		// (excepting IE8 booleans)
		support.attributes = assert(function( div ) {
			div.className = "i";
			return !div.getAttribute("className");
		});
	
		/* getElement(s)By*
		---------------------------------------------------------------------- */
	
		// Check if getElementsByTagName("*") returns only elements
		support.getElementsByTagName = assert(function( div ) {
			div.appendChild( document.createComment("") );
			return !div.getElementsByTagName("*").length;
		});
	
		// Support: IE<9
		support.getElementsByClassName = rnative.test( document.getElementsByClassName );
	
		// Support: IE<10
		// Check if getElementById returns elements by name
		// The broken getElementById methods don't pick up programatically-set names,
		// so use a roundabout getElementsByName test
		support.getById = assert(function( div ) {
			docElem.appendChild( div ).id = expando;
			return !document.getElementsByName || !document.getElementsByName( expando ).length;
		});
	
		// ID find and filter
		if ( support.getById ) {
			Expr.find["ID"] = function( id, context ) {
				if ( typeof context.getElementById !== "undefined" && documentIsHTML ) {
					var m = context.getElementById( id );
					return m ? [ m ] : [];
				}
			};
			Expr.filter["ID"] = function( id ) {
				var attrId = id.replace( runescape, funescape );
				return function( elem ) {
					return elem.getAttribute("id") === attrId;
				};
			};
		} else {
			// Support: IE6/7
			// getElementById is not reliable as a find shortcut
			delete Expr.find["ID"];
	
			Expr.filter["ID"] =  function( id ) {
				var attrId = id.replace( runescape, funescape );
				return function( elem ) {
					var node = typeof elem.getAttributeNode !== "undefined" &&
						elem.getAttributeNode("id");
					return node && node.value === attrId;
				};
			};
		}
	
		// Tag
		Expr.find["TAG"] = support.getElementsByTagName ?
			function( tag, context ) {
				if ( typeof context.getElementsByTagName !== "undefined" ) {
					return context.getElementsByTagName( tag );
	
				// DocumentFragment nodes don't have gEBTN
				} else if ( support.qsa ) {
					return context.querySelectorAll( tag );
				}
			} :
	
			function( tag, context ) {
				var elem,
					tmp = [],
					i = 0,
					// By happy coincidence, a (broken) gEBTN appears on DocumentFragment nodes too
					results = context.getElementsByTagName( tag );
	
				// Filter out possible comments
				if ( tag === "*" ) {
					while ( (elem = results[i++]) ) {
						if ( elem.nodeType === 1 ) {
							tmp.push( elem );
						}
					}
	
					return tmp;
				}
				return results;
			};
	
		// Class
		Expr.find["CLASS"] = support.getElementsByClassName && function( className, context ) {
			if ( typeof context.getElementsByClassName !== "undefined" && documentIsHTML ) {
				return context.getElementsByClassName( className );
			}
		};
	
		/* QSA/matchesSelector
		---------------------------------------------------------------------- */
	
		// QSA and matchesSelector support
	
		// matchesSelector(:active) reports false when true (IE9/Opera 11.5)
		rbuggyMatches = [];
	
		// qSa(:focus) reports false when true (Chrome 21)
		// We allow this because of a bug in IE8/9 that throws an error
		// whenever `document.activeElement` is accessed on an iframe
		// So, we allow :focus to pass through QSA all the time to avoid the IE error
		// See http://bugs.jquery.com/ticket/13378
		rbuggyQSA = [];
	
		if ( (support.qsa = rnative.test( document.querySelectorAll )) ) {
			// Build QSA regex
			// Regex strategy adopted from Diego Perini
			assert(function( div ) {
				// Select is set to empty string on purpose
				// This is to test IE's treatment of not explicitly
				// setting a boolean content attribute,
				// since its presence should be enough
				// http://bugs.jquery.com/ticket/12359
				docElem.appendChild( div ).innerHTML = "<a id='" + expando + "'></a>" +
					"<select id='" + expando + "-\r\\' msallowcapture=''>" +
					"<option selected=''></option></select>";
	
				// Support: IE8, Opera 11-12.16
				// Nothing should be selected when empty strings follow ^= or $= or *=
				// The test attribute must be unknown in Opera but "safe" for WinRT
				// http://msdn.microsoft.com/en-us/library/ie/hh465388.aspx#attribute_section
				if ( div.querySelectorAll("[msallowcapture^='']").length ) {
					rbuggyQSA.push( "[*^$]=" + whitespace + "*(?:''|\"\")" );
				}
	
				// Support: IE8
				// Boolean attributes and "value" are not treated correctly
				if ( !div.querySelectorAll("[selected]").length ) {
					rbuggyQSA.push( "\\[" + whitespace + "*(?:value|" + booleans + ")" );
				}
	
				// Support: Chrome<29, Android<4.4, Safari<7.0+, iOS<7.0+, PhantomJS<1.9.8+
				if ( !div.querySelectorAll( "[id~=" + expando + "-]" ).length ) {
					rbuggyQSA.push("~=");
				}
	
				// Webkit/Opera - :checked should return selected option elements
				// http://www.w3.org/TR/2011/REC-css3-selectors-20110929/#checked
				// IE8 throws error here and will not see later tests
				if ( !div.querySelectorAll(":checked").length ) {
					rbuggyQSA.push(":checked");
				}
	
				// Support: Safari 8+, iOS 8+
				// https://bugs.webkit.org/show_bug.cgi?id=136851
				// In-page `selector#id sibing-combinator selector` fails
				if ( !div.querySelectorAll( "a#" + expando + "+*" ).length ) {
					rbuggyQSA.push(".#.+[+~]");
				}
			});
	
			assert(function( div ) {
				// Support: Windows 8 Native Apps
				// The type and name attributes are restricted during .innerHTML assignment
				var input = document.createElement("input");
				input.setAttribute( "type", "hidden" );
				div.appendChild( input ).setAttribute( "name", "D" );
	
				// Support: IE8
				// Enforce case-sensitivity of name attribute
				if ( div.querySelectorAll("[name=d]").length ) {
					rbuggyQSA.push( "name" + whitespace + "*[*^$|!~]?=" );
				}
	
				// FF 3.5 - :enabled/:disabled and hidden elements (hidden elements are still enabled)
				// IE8 throws error here and will not see later tests
				if ( !div.querySelectorAll(":enabled").length ) {
					rbuggyQSA.push( ":enabled", ":disabled" );
				}
	
				// Opera 10-11 does not throw on post-comma invalid pseudos
				div.querySelectorAll("*,:x");
				rbuggyQSA.push(",.*:");
			});
		}
	
		if ( (support.matchesSelector = rnative.test( (matches = docElem.matches ||
			docElem.webkitMatchesSelector ||
			docElem.mozMatchesSelector ||
			docElem.oMatchesSelector ||
			docElem.msMatchesSelector) )) ) {
	
			assert(function( div ) {
				// Check to see if it's possible to do matchesSelector
				// on a disconnected node (IE 9)
				support.disconnectedMatch = matches.call( div, "div" );
	
				// This should fail with an exception
				// Gecko does not error, returns false instead
				matches.call( div, "[s!='']:x" );
				rbuggyMatches.push( "!=", pseudos );
			});
		}
	
		rbuggyQSA = rbuggyQSA.length && new RegExp( rbuggyQSA.join("|") );
		rbuggyMatches = rbuggyMatches.length && new RegExp( rbuggyMatches.join("|") );
	
		/* Contains
		---------------------------------------------------------------------- */
		hasCompare = rnative.test( docElem.compareDocumentPosition );
	
		// Element contains another
		// Purposefully self-exclusive
		// As in, an element does not contain itself
		contains = hasCompare || rnative.test( docElem.contains ) ?
			function( a, b ) {
				var adown = a.nodeType === 9 ? a.documentElement : a,
					bup = b && b.parentNode;
				return a === bup || !!( bup && bup.nodeType === 1 && (
					adown.contains ?
						adown.contains( bup ) :
						a.compareDocumentPosition && a.compareDocumentPosition( bup ) & 16
				));
			} :
			function( a, b ) {
				if ( b ) {
					while ( (b = b.parentNode) ) {
						if ( b === a ) {
							return true;
						}
					}
				}
				return false;
			};
	
		/* Sorting
		---------------------------------------------------------------------- */
	
		// Document order sorting
		sortOrder = hasCompare ?
		function( a, b ) {
	
			// Flag for duplicate removal
			if ( a === b ) {
				hasDuplicate = true;
				return 0;
			}
	
			// Sort on method existence if only one input has compareDocumentPosition
			var compare = !a.compareDocumentPosition - !b.compareDocumentPosition;
			if ( compare ) {
				return compare;
			}
	
			// Calculate position if both inputs belong to the same document
			compare = ( a.ownerDocument || a ) === ( b.ownerDocument || b ) ?
				a.compareDocumentPosition( b ) :
	
				// Otherwise we know they are disconnected
				1;
	
			// Disconnected nodes
			if ( compare & 1 ||
				(!support.sortDetached && b.compareDocumentPosition( a ) === compare) ) {
	
				// Choose the first element that is related to our preferred document
				if ( a === document || a.ownerDocument === preferredDoc && contains(preferredDoc, a) ) {
					return -1;
				}
				if ( b === document || b.ownerDocument === preferredDoc && contains(preferredDoc, b) ) {
					return 1;
				}
	
				// Maintain original order
				return sortInput ?
					( indexOf( sortInput, a ) - indexOf( sortInput, b ) ) :
					0;
			}
	
			return compare & 4 ? -1 : 1;
		} :
		function( a, b ) {
			// Exit early if the nodes are identical
			if ( a === b ) {
				hasDuplicate = true;
				return 0;
			}
	
			var cur,
				i = 0,
				aup = a.parentNode,
				bup = b.parentNode,
				ap = [ a ],
				bp = [ b ];
	
			// Parentless nodes are either documents or disconnected
			if ( !aup || !bup ) {
				return a === document ? -1 :
					b === document ? 1 :
					aup ? -1 :
					bup ? 1 :
					sortInput ?
					( indexOf( sortInput, a ) - indexOf( sortInput, b ) ) :
					0;
	
			// If the nodes are siblings, we can do a quick check
			} else if ( aup === bup ) {
				return siblingCheck( a, b );
			}
	
			// Otherwise we need full lists of their ancestors for comparison
			cur = a;
			while ( (cur = cur.parentNode) ) {
				ap.unshift( cur );
			}
			cur = b;
			while ( (cur = cur.parentNode) ) {
				bp.unshift( cur );
			}
	
			// Walk down the tree looking for a discrepancy
			while ( ap[i] === bp[i] ) {
				i++;
			}
	
			return i ?
				// Do a sibling check if the nodes have a common ancestor
				siblingCheck( ap[i], bp[i] ) :
	
				// Otherwise nodes in our document sort first
				ap[i] === preferredDoc ? -1 :
				bp[i] === preferredDoc ? 1 :
				0;
		};
	
		return document;
	};
	
	Sizzle.matches = function( expr, elements ) {
		return Sizzle( expr, null, null, elements );
	};
	
	Sizzle.matchesSelector = function( elem, expr ) {
		// Set document vars if needed
		if ( ( elem.ownerDocument || elem ) !== document ) {
			setDocument( elem );
		}
	
		// Make sure that attribute selectors are quoted
		expr = expr.replace( rattributeQuotes, "='$1']" );
	
		if ( support.matchesSelector && documentIsHTML &&
			!compilerCache[ expr + " " ] &&
			( !rbuggyMatches || !rbuggyMatches.test( expr ) ) &&
			( !rbuggyQSA     || !rbuggyQSA.test( expr ) ) ) {
	
			try {
				var ret = matches.call( elem, expr );
	
				// IE 9's matchesSelector returns false on disconnected nodes
				if ( ret || support.disconnectedMatch ||
						// As well, disconnected nodes are said to be in a document
						// fragment in IE 9
						elem.document && elem.document.nodeType !== 11 ) {
					return ret;
				}
			} catch (e) {}
		}
	
		return Sizzle( expr, document, null, [ elem ] ).length > 0;
	};
	
	Sizzle.contains = function( context, elem ) {
		// Set document vars if needed
		if ( ( context.ownerDocument || context ) !== document ) {
			setDocument( context );
		}
		return contains( context, elem );
	};
	
	Sizzle.attr = function( elem, name ) {
		// Set document vars if needed
		if ( ( elem.ownerDocument || elem ) !== document ) {
			setDocument( elem );
		}
	
		var fn = Expr.attrHandle[ name.toLowerCase() ],
			// Don't get fooled by Object.prototype properties (jQuery #13807)
			val = fn && hasOwn.call( Expr.attrHandle, name.toLowerCase() ) ?
				fn( elem, name, !documentIsHTML ) :
				undefined;
	
		return val !== undefined ?
			val :
			support.attributes || !documentIsHTML ?
				elem.getAttribute( name ) :
				(val = elem.getAttributeNode(name)) && val.specified ?
					val.value :
					null;
	};
	
	Sizzle.error = function( msg ) {
		throw new Error( "Syntax error, unrecognized expression: " + msg );
	};
	
	/**
	 * Document sorting and removing duplicates
	 * @param {ArrayLike} results
	 */
	Sizzle.uniqueSort = function( results ) {
		var elem,
			duplicates = [],
			j = 0,
			i = 0;
	
		// Unless we *know* we can detect duplicates, assume their presence
		hasDuplicate = !support.detectDuplicates;
		sortInput = !support.sortStable && results.slice( 0 );
		results.sort( sortOrder );
	
		if ( hasDuplicate ) {
			while ( (elem = results[i++]) ) {
				if ( elem === results[ i ] ) {
					j = duplicates.push( i );
				}
			}
			while ( j-- ) {
				results.splice( duplicates[ j ], 1 );
			}
		}
	
		// Clear input after sorting to release objects
		// See https://github.com/jquery/sizzle/pull/225
		sortInput = null;
	
		return results;
	};
	
	/**
	 * Utility function for retrieving the text value of an array of DOM nodes
	 * @param {Array|Element} elem
	 */
	getText = Sizzle.getText = function( elem ) {
		var node,
			ret = "",
			i = 0,
			nodeType = elem.nodeType;
	
		if ( !nodeType ) {
			// If no nodeType, this is expected to be an array
			while ( (node = elem[i++]) ) {
				// Do not traverse comment nodes
				ret += getText( node );
			}
		} else if ( nodeType === 1 || nodeType === 9 || nodeType === 11 ) {
			// Use textContent for elements
			// innerText usage removed for consistency of new lines (jQuery #11153)
			if ( typeof elem.textContent === "string" ) {
				return elem.textContent;
			} else {
				// Traverse its children
				for ( elem = elem.firstChild; elem; elem = elem.nextSibling ) {
					ret += getText( elem );
				}
			}
		} else if ( nodeType === 3 || nodeType === 4 ) {
			return elem.nodeValue;
		}
		// Do not include comment or processing instruction nodes
	
		return ret;
	};
	
	Expr = Sizzle.selectors = {
	
		// Can be adjusted by the user
		cacheLength: 50,
	
		createPseudo: markFunction,
	
		match: matchExpr,
	
		attrHandle: {},
	
		find: {},
	
		relative: {
			">": { dir: "parentNode", first: true },
			" ": { dir: "parentNode" },
			"+": { dir: "previousSibling", first: true },
			"~": { dir: "previousSibling" }
		},
	
		preFilter: {
			"ATTR": function( match ) {
				match[1] = match[1].replace( runescape, funescape );
	
				// Move the given value to match[3] whether quoted or unquoted
				match[3] = ( match[3] || match[4] || match[5] || "" ).replace( runescape, funescape );
	
				if ( match[2] === "~=" ) {
					match[3] = " " + match[3] + " ";
				}
	
				return match.slice( 0, 4 );
			},
	
			"CHILD": function( match ) {
				/* matches from matchExpr["CHILD"]
					1 type (only|nth|...)
					2 what (child|of-type)
					3 argument (even|odd|\d*|\d*n([+-]\d+)?|...)
					4 xn-component of xn+y argument ([+-]?\d*n|)
					5 sign of xn-component
					6 x of xn-component
					7 sign of y-component
					8 y of y-component
				*/
				match[1] = match[1].toLowerCase();
	
				if ( match[1].slice( 0, 3 ) === "nth" ) {
					// nth-* requires argument
					if ( !match[3] ) {
						Sizzle.error( match[0] );
					}
	
					// numeric x and y parameters for Expr.filter.CHILD
					// remember that false/true cast respectively to 0/1
					match[4] = +( match[4] ? match[5] + (match[6] || 1) : 2 * ( match[3] === "even" || match[3] === "odd" ) );
					match[5] = +( ( match[7] + match[8] ) || match[3] === "odd" );
	
				// other types prohibit arguments
				} else if ( match[3] ) {
					Sizzle.error( match[0] );
				}
	
				return match;
			},
	
			"PSEUDO": function( match ) {
				var excess,
					unquoted = !match[6] && match[2];
	
				if ( matchExpr["CHILD"].test( match[0] ) ) {
					return null;
				}
	
				// Accept quoted arguments as-is
				if ( match[3] ) {
					match[2] = match[4] || match[5] || "";
	
				// Strip excess characters from unquoted arguments
				} else if ( unquoted && rpseudo.test( unquoted ) &&
					// Get excess from tokenize (recursively)
					(excess = tokenize( unquoted, true )) &&
					// advance to the next closing parenthesis
					(excess = unquoted.indexOf( ")", unquoted.length - excess ) - unquoted.length) ) {
	
					// excess is a negative index
					match[0] = match[0].slice( 0, excess );
					match[2] = unquoted.slice( 0, excess );
				}
	
				// Return only captures needed by the pseudo filter method (type and argument)
				return match.slice( 0, 3 );
			}
		},
	
		filter: {
	
			"TAG": function( nodeNameSelector ) {
				var nodeName = nodeNameSelector.replace( runescape, funescape ).toLowerCase();
				return nodeNameSelector === "*" ?
					function() { return true; } :
					function( elem ) {
						return elem.nodeName && elem.nodeName.toLowerCase() === nodeName;
					};
			},
	
			"CLASS": function( className ) {
				var pattern = classCache[ className + " " ];
	
				return pattern ||
					(pattern = new RegExp( "(^|" + whitespace + ")" + className + "(" + whitespace + "|$)" )) &&
					classCache( className, function( elem ) {
						return pattern.test( typeof elem.className === "string" && elem.className || typeof elem.getAttribute !== "undefined" && elem.getAttribute("class") || "" );
					});
			},
	
			"ATTR": function( name, operator, check ) {
				return function( elem ) {
					var result = Sizzle.attr( elem, name );
	
					if ( result == null ) {
						return operator === "!=";
					}
					if ( !operator ) {
						return true;
					}
	
					result += "";
	
					return operator === "=" ? result === check :
						operator === "!=" ? result !== check :
						operator === "^=" ? check && result.indexOf( check ) === 0 :
						operator === "*=" ? check && result.indexOf( check ) > -1 :
						operator === "$=" ? check && result.slice( -check.length ) === check :
						operator === "~=" ? ( " " + result.replace( rwhitespace, " " ) + " " ).indexOf( check ) > -1 :
						operator === "|=" ? result === check || result.slice( 0, check.length + 1 ) === check + "-" :
						false;
				};
			},
	
			"CHILD": function( type, what, argument, first, last ) {
				var simple = type.slice( 0, 3 ) !== "nth",
					forward = type.slice( -4 ) !== "last",
					ofType = what === "of-type";
	
				return first === 1 && last === 0 ?
	
					// Shortcut for :nth-*(n)
					function( elem ) {
						return !!elem.parentNode;
					} :
	
					function( elem, context, xml ) {
						var cache, uniqueCache, outerCache, node, nodeIndex, start,
							dir = simple !== forward ? "nextSibling" : "previousSibling",
							parent = elem.parentNode,
							name = ofType && elem.nodeName.toLowerCase(),
							useCache = !xml && !ofType,
							diff = false;
	
						if ( parent ) {
	
							// :(first|last|only)-(child|of-type)
							if ( simple ) {
								while ( dir ) {
									node = elem;
									while ( (node = node[ dir ]) ) {
										if ( ofType ?
											node.nodeName.toLowerCase() === name :
											node.nodeType === 1 ) {
	
											return false;
										}
									}
									// Reverse direction for :only-* (if we haven't yet done so)
									start = dir = type === "only" && !start && "nextSibling";
								}
								return true;
							}
	
							start = [ forward ? parent.firstChild : parent.lastChild ];
	
							// non-xml :nth-child(...) stores cache data on `parent`
							if ( forward && useCache ) {
	
								// Seek `elem` from a previously-cached index
	
								// ...in a gzip-friendly way
								node = parent;
								outerCache = node[ expando ] || (node[ expando ] = {});
	
								// Support: IE <9 only
								// Defend against cloned attroperties (jQuery gh-1709)
								uniqueCache = outerCache[ node.uniqueID ] ||
									(outerCache[ node.uniqueID ] = {});
	
								cache = uniqueCache[ type ] || [];
								nodeIndex = cache[ 0 ] === dirruns && cache[ 1 ];
								diff = nodeIndex && cache[ 2 ];
								node = nodeIndex && parent.childNodes[ nodeIndex ];
	
								while ( (node = ++nodeIndex && node && node[ dir ] ||
	
									// Fallback to seeking `elem` from the start
									(diff = nodeIndex = 0) || start.pop()) ) {
	
									// When found, cache indexes on `parent` and break
									if ( node.nodeType === 1 && ++diff && node === elem ) {
										uniqueCache[ type ] = [ dirruns, nodeIndex, diff ];
										break;
									}
								}
	
							} else {
								// Use previously-cached element index if available
								if ( useCache ) {
									// ...in a gzip-friendly way
									node = elem;
									outerCache = node[ expando ] || (node[ expando ] = {});
	
									// Support: IE <9 only
									// Defend against cloned attroperties (jQuery gh-1709)
									uniqueCache = outerCache[ node.uniqueID ] ||
										(outerCache[ node.uniqueID ] = {});
	
									cache = uniqueCache[ type ] || [];
									nodeIndex = cache[ 0 ] === dirruns && cache[ 1 ];
									diff = nodeIndex;
								}
	
								// xml :nth-child(...)
								// or :nth-last-child(...) or :nth(-last)?-of-type(...)
								if ( diff === false ) {
									// Use the same loop as above to seek `elem` from the start
									while ( (node = ++nodeIndex && node && node[ dir ] ||
										(diff = nodeIndex = 0) || start.pop()) ) {
	
										if ( ( ofType ?
											node.nodeName.toLowerCase() === name :
											node.nodeType === 1 ) &&
											++diff ) {
	
											// Cache the index of each encountered element
											if ( useCache ) {
												outerCache = node[ expando ] || (node[ expando ] = {});
	
												// Support: IE <9 only
												// Defend against cloned attroperties (jQuery gh-1709)
												uniqueCache = outerCache[ node.uniqueID ] ||
													(outerCache[ node.uniqueID ] = {});
	
												uniqueCache[ type ] = [ dirruns, diff ];
											}
	
											if ( node === elem ) {
												break;
											}
										}
									}
								}
							}
	
							// Incorporate the offset, then check against cycle size
							diff -= last;
							return diff === first || ( diff % first === 0 && diff / first >= 0 );
						}
					};
			},
	
			"PSEUDO": function( pseudo, argument ) {
				// pseudo-class names are case-insensitive
				// http://www.w3.org/TR/selectors/#pseudo-classes
				// Prioritize by case sensitivity in case custom pseudos are added with uppercase letters
				// Remember that setFilters inherits from pseudos
				var args,
					fn = Expr.pseudos[ pseudo ] || Expr.setFilters[ pseudo.toLowerCase() ] ||
						Sizzle.error( "unsupported pseudo: " + pseudo );
	
				// The user may use createPseudo to indicate that
				// arguments are needed to create the filter function
				// just as Sizzle does
				if ( fn[ expando ] ) {
					return fn( argument );
				}
	
				// But maintain support for old signatures
				if ( fn.length > 1 ) {
					args = [ pseudo, pseudo, "", argument ];
					return Expr.setFilters.hasOwnProperty( pseudo.toLowerCase() ) ?
						markFunction(function( seed, matches ) {
							var idx,
								matched = fn( seed, argument ),
								i = matched.length;
							while ( i-- ) {
								idx = indexOf( seed, matched[i] );
								seed[ idx ] = !( matches[ idx ] = matched[i] );
							}
						}) :
						function( elem ) {
							return fn( elem, 0, args );
						};
				}
	
				return fn;
			}
		},
	
		pseudos: {
			// Potentially complex pseudos
			"not": markFunction(function( selector ) {
				// Trim the selector passed to compile
				// to avoid treating leading and trailing
				// spaces as combinators
				var input = [],
					results = [],
					matcher = compile( selector.replace( rtrim, "$1" ) );
	
				return matcher[ expando ] ?
					markFunction(function( seed, matches, context, xml ) {
						var elem,
							unmatched = matcher( seed, null, xml, [] ),
							i = seed.length;
	
						// Match elements unmatched by `matcher`
						while ( i-- ) {
							if ( (elem = unmatched[i]) ) {
								seed[i] = !(matches[i] = elem);
							}
						}
					}) :
					function( elem, context, xml ) {
						input[0] = elem;
						matcher( input, null, xml, results );
						// Don't keep the element (issue #299)
						input[0] = null;
						return !results.pop();
					};
			}),
	
			"has": markFunction(function( selector ) {
				return function( elem ) {
					return Sizzle( selector, elem ).length > 0;
				};
			}),
	
			"contains": markFunction(function( text ) {
				text = text.replace( runescape, funescape );
				return function( elem ) {
					return ( elem.textContent || elem.innerText || getText( elem ) ).indexOf( text ) > -1;
				};
			}),
	
			// "Whether an element is represented by a :lang() selector
			// is based solely on the element's language value
			// being equal to the identifier C,
			// or beginning with the identifier C immediately followed by "-".
			// The matching of C against the element's language value is performed case-insensitively.
			// The identifier C does not have to be a valid language name."
			// http://www.w3.org/TR/selectors/#lang-pseudo
			"lang": markFunction( function( lang ) {
				// lang value must be a valid identifier
				if ( !ridentifier.test(lang || "") ) {
					Sizzle.error( "unsupported lang: " + lang );
				}
				lang = lang.replace( runescape, funescape ).toLowerCase();
				return function( elem ) {
					var elemLang;
					do {
						if ( (elemLang = documentIsHTML ?
							elem.lang :
							elem.getAttribute("xml:lang") || elem.getAttribute("lang")) ) {
	
							elemLang = elemLang.toLowerCase();
							return elemLang === lang || elemLang.indexOf( lang + "-" ) === 0;
						}
					} while ( (elem = elem.parentNode) && elem.nodeType === 1 );
					return false;
				};
			}),
	
			// Miscellaneous
			"target": function( elem ) {
				var hash = window.location && window.location.hash;
				return hash && hash.slice( 1 ) === elem.id;
			},
	
			"root": function( elem ) {
				return elem === docElem;
			},
	
			"focus": function( elem ) {
				return elem === document.activeElement && (!document.hasFocus || document.hasFocus()) && !!(elem.type || elem.href || ~elem.tabIndex);
			},
	
			// Boolean properties
			"enabled": function( elem ) {
				return elem.disabled === false;
			},
	
			"disabled": function( elem ) {
				return elem.disabled === true;
			},
	
			"checked": function( elem ) {
				// In CSS3, :checked should return both checked and selected elements
				// http://www.w3.org/TR/2011/REC-css3-selectors-20110929/#checked
				var nodeName = elem.nodeName.toLowerCase();
				return (nodeName === "input" && !!elem.checked) || (nodeName === "option" && !!elem.selected);
			},
	
			"selected": function( elem ) {
				// Accessing this property makes selected-by-default
				// options in Safari work properly
				if ( elem.parentNode ) {
					elem.parentNode.selectedIndex;
				}
	
				return elem.selected === true;
			},
	
			// Contents
			"empty": function( elem ) {
				// http://www.w3.org/TR/selectors/#empty-pseudo
				// :empty is negated by element (1) or content nodes (text: 3; cdata: 4; entity ref: 5),
				//   but not by others (comment: 8; processing instruction: 7; etc.)
				// nodeType < 6 works because attributes (2) do not appear as children
				for ( elem = elem.firstChild; elem; elem = elem.nextSibling ) {
					if ( elem.nodeType < 6 ) {
						return false;
					}
				}
				return true;
			},
	
			"parent": function( elem ) {
				return !Expr.pseudos["empty"]( elem );
			},
	
			// Element/input types
			"header": function( elem ) {
				return rheader.test( elem.nodeName );
			},
	
			"input": function( elem ) {
				return rinputs.test( elem.nodeName );
			},
	
			"button": function( elem ) {
				var name = elem.nodeName.toLowerCase();
				return name === "input" && elem.type === "button" || name === "button";
			},
	
			"text": function( elem ) {
				var attr;
				return elem.nodeName.toLowerCase() === "input" &&
					elem.type === "text" &&
	
					// Support: IE<8
					// New HTML5 attribute values (e.g., "search") appear with elem.type === "text"
					( (attr = elem.getAttribute("type")) == null || attr.toLowerCase() === "text" );
			},
	
			// Position-in-collection
			"first": createPositionalPseudo(function() {
				return [ 0 ];
			}),
	
			"last": createPositionalPseudo(function( matchIndexes, length ) {
				return [ length - 1 ];
			}),
	
			"eq": createPositionalPseudo(function( matchIndexes, length, argument ) {
				return [ argument < 0 ? argument + length : argument ];
			}),
	
			"even": createPositionalPseudo(function( matchIndexes, length ) {
				var i = 0;
				for ( ; i < length; i += 2 ) {
					matchIndexes.push( i );
				}
				return matchIndexes;
			}),
	
			"odd": createPositionalPseudo(function( matchIndexes, length ) {
				var i = 1;
				for ( ; i < length; i += 2 ) {
					matchIndexes.push( i );
				}
				return matchIndexes;
			}),
	
			"lt": createPositionalPseudo(function( matchIndexes, length, argument ) {
				var i = argument < 0 ? argument + length : argument;
				for ( ; --i >= 0; ) {
					matchIndexes.push( i );
				}
				return matchIndexes;
			}),
	
			"gt": createPositionalPseudo(function( matchIndexes, length, argument ) {
				var i = argument < 0 ? argument + length : argument;
				for ( ; ++i < length; ) {
					matchIndexes.push( i );
				}
				return matchIndexes;
			})
		}
	};
	
	Expr.pseudos["nth"] = Expr.pseudos["eq"];
	
	// Add button/input type pseudos
	for ( i in { radio: true, checkbox: true, file: true, password: true, image: true } ) {
		Expr.pseudos[ i ] = createInputPseudo( i );
	}
	for ( i in { submit: true, reset: true } ) {
		Expr.pseudos[ i ] = createButtonPseudo( i );
	}
	
	// Easy API for creating new setFilters
	function setFilters() {}
	setFilters.prototype = Expr.filters = Expr.pseudos;
	Expr.setFilters = new setFilters();
	
	tokenize = Sizzle.tokenize = function( selector, parseOnly ) {
		var matched, match, tokens, type,
			soFar, groups, preFilters,
			cached = tokenCache[ selector + " " ];
	
		if ( cached ) {
			return parseOnly ? 0 : cached.slice( 0 );
		}
	
		soFar = selector;
		groups = [];
		preFilters = Expr.preFilter;
	
		while ( soFar ) {
	
			// Comma and first run
			if ( !matched || (match = rcomma.exec( soFar )) ) {
				if ( match ) {
					// Don't consume trailing commas as valid
					soFar = soFar.slice( match[0].length ) || soFar;
				}
				groups.push( (tokens = []) );
			}
	
			matched = false;
	
			// Combinators
			if ( (match = rcombinators.exec( soFar )) ) {
				matched = match.shift();
				tokens.push({
					value: matched,
					// Cast descendant combinators to space
					type: match[0].replace( rtrim, " " )
				});
				soFar = soFar.slice( matched.length );
			}
	
			// Filters
			for ( type in Expr.filter ) {
				if ( (match = matchExpr[ type ].exec( soFar )) && (!preFilters[ type ] ||
					(match = preFilters[ type ]( match ))) ) {
					matched = match.shift();
					tokens.push({
						value: matched,
						type: type,
						matches: match
					});
					soFar = soFar.slice( matched.length );
				}
			}
	
			if ( !matched ) {
				break;
			}
		}
	
		// Return the length of the invalid excess
		// if we're just parsing
		// Otherwise, throw an error or return tokens
		return parseOnly ?
			soFar.length :
			soFar ?
				Sizzle.error( selector ) :
				// Cache the tokens
				tokenCache( selector, groups ).slice( 0 );
	};
	
	function toSelector( tokens ) {
		var i = 0,
			len = tokens.length,
			selector = "";
		for ( ; i < len; i++ ) {
			selector += tokens[i].value;
		}
		return selector;
	}
	
	function addCombinator( matcher, combinator, base ) {
		var dir = combinator.dir,
			checkNonElements = base && dir === "parentNode",
			doneName = done++;
	
		return combinator.first ?
			// Check against closest ancestor/preceding element
			function( elem, context, xml ) {
				while ( (elem = elem[ dir ]) ) {
					if ( elem.nodeType === 1 || checkNonElements ) {
						return matcher( elem, context, xml );
					}
				}
			} :
	
			// Check against all ancestor/preceding elements
			function( elem, context, xml ) {
				var oldCache, uniqueCache, outerCache,
					newCache = [ dirruns, doneName ];
	
				// We can't set arbitrary data on XML nodes, so they don't benefit from combinator caching
				if ( xml ) {
					while ( (elem = elem[ dir ]) ) {
						if ( elem.nodeType === 1 || checkNonElements ) {
							if ( matcher( elem, context, xml ) ) {
								return true;
							}
						}
					}
				} else {
					while ( (elem = elem[ dir ]) ) {
						if ( elem.nodeType === 1 || checkNonElements ) {
							outerCache = elem[ expando ] || (elem[ expando ] = {});
	
							// Support: IE <9 only
							// Defend against cloned attroperties (jQuery gh-1709)
							uniqueCache = outerCache[ elem.uniqueID ] || (outerCache[ elem.uniqueID ] = {});
	
							if ( (oldCache = uniqueCache[ dir ]) &&
								oldCache[ 0 ] === dirruns && oldCache[ 1 ] === doneName ) {
	
								// Assign to newCache so results back-propagate to previous elements
								return (newCache[ 2 ] = oldCache[ 2 ]);
							} else {
								// Reuse newcache so results back-propagate to previous elements
								uniqueCache[ dir ] = newCache;
	
								// A match means we're done; a fail means we have to keep checking
								if ( (newCache[ 2 ] = matcher( elem, context, xml )) ) {
									return true;
								}
							}
						}
					}
				}
			};
	}
	
	function elementMatcher( matchers ) {
		return matchers.length > 1 ?
			function( elem, context, xml ) {
				var i = matchers.length;
				while ( i-- ) {
					if ( !matchers[i]( elem, context, xml ) ) {
						return false;
					}
				}
				return true;
			} :
			matchers[0];
	}
	
	function multipleContexts( selector, contexts, results ) {
		var i = 0,
			len = contexts.length;
		for ( ; i < len; i++ ) {
			Sizzle( selector, contexts[i], results );
		}
		return results;
	}
	
	function condense( unmatched, map, filter, context, xml ) {
		var elem,
			newUnmatched = [],
			i = 0,
			len = unmatched.length,
			mapped = map != null;
	
		for ( ; i < len; i++ ) {
			if ( (elem = unmatched[i]) ) {
				if ( !filter || filter( elem, context, xml ) ) {
					newUnmatched.push( elem );
					if ( mapped ) {
						map.push( i );
					}
				}
			}
		}
	
		return newUnmatched;
	}
	
	function setMatcher( preFilter, selector, matcher, postFilter, postFinder, postSelector ) {
		if ( postFilter && !postFilter[ expando ] ) {
			postFilter = setMatcher( postFilter );
		}
		if ( postFinder && !postFinder[ expando ] ) {
			postFinder = setMatcher( postFinder, postSelector );
		}
		return markFunction(function( seed, results, context, xml ) {
			var temp, i, elem,
				preMap = [],
				postMap = [],
				preexisting = results.length,
	
				// Get initial elements from seed or context
				elems = seed || multipleContexts( selector || "*", context.nodeType ? [ context ] : context, [] ),
	
				// Prefilter to get matcher input, preserving a map for seed-results synchronization
				matcherIn = preFilter && ( seed || !selector ) ?
					condense( elems, preMap, preFilter, context, xml ) :
					elems,
	
				matcherOut = matcher ?
					// If we have a postFinder, or filtered seed, or non-seed postFilter or preexisting results,
					postFinder || ( seed ? preFilter : preexisting || postFilter ) ?
	
						// ...intermediate processing is necessary
						[] :
	
						// ...otherwise use results directly
						results :
					matcherIn;
	
			// Find primary matches
			if ( matcher ) {
				matcher( matcherIn, matcherOut, context, xml );
			}
	
			// Apply postFilter
			if ( postFilter ) {
				temp = condense( matcherOut, postMap );
				postFilter( temp, [], context, xml );
	
				// Un-match failing elements by moving them back to matcherIn
				i = temp.length;
				while ( i-- ) {
					if ( (elem = temp[i]) ) {
						matcherOut[ postMap[i] ] = !(matcherIn[ postMap[i] ] = elem);
					}
				}
			}
	
			if ( seed ) {
				if ( postFinder || preFilter ) {
					if ( postFinder ) {
						// Get the final matcherOut by condensing this intermediate into postFinder contexts
						temp = [];
						i = matcherOut.length;
						while ( i-- ) {
							if ( (elem = matcherOut[i]) ) {
								// Restore matcherIn since elem is not yet a final match
								temp.push( (matcherIn[i] = elem) );
							}
						}
						postFinder( null, (matcherOut = []), temp, xml );
					}
	
					// Move matched elements from seed to results to keep them synchronized
					i = matcherOut.length;
					while ( i-- ) {
						if ( (elem = matcherOut[i]) &&
							(temp = postFinder ? indexOf( seed, elem ) : preMap[i]) > -1 ) {
	
							seed[temp] = !(results[temp] = elem);
						}
					}
				}
	
			// Add elements to results, through postFinder if defined
			} else {
				matcherOut = condense(
					matcherOut === results ?
						matcherOut.splice( preexisting, matcherOut.length ) :
						matcherOut
				);
				if ( postFinder ) {
					postFinder( null, results, matcherOut, xml );
				} else {
					push.apply( results, matcherOut );
				}
			}
		});
	}
	
	function matcherFromTokens( tokens ) {
		var checkContext, matcher, j,
			len = tokens.length,
			leadingRelative = Expr.relative[ tokens[0].type ],
			implicitRelative = leadingRelative || Expr.relative[" "],
			i = leadingRelative ? 1 : 0,
	
			// The foundational matcher ensures that elements are reachable from top-level context(s)
			matchContext = addCombinator( function( elem ) {
				return elem === checkContext;
			}, implicitRelative, true ),
			matchAnyContext = addCombinator( function( elem ) {
				return indexOf( checkContext, elem ) > -1;
			}, implicitRelative, true ),
			matchers = [ function( elem, context, xml ) {
				var ret = ( !leadingRelative && ( xml || context !== outermostContext ) ) || (
					(checkContext = context).nodeType ?
						matchContext( elem, context, xml ) :
						matchAnyContext( elem, context, xml ) );
				// Avoid hanging onto element (issue #299)
				checkContext = null;
				return ret;
			} ];
	
		for ( ; i < len; i++ ) {
			if ( (matcher = Expr.relative[ tokens[i].type ]) ) {
				matchers = [ addCombinator(elementMatcher( matchers ), matcher) ];
			} else {
				matcher = Expr.filter[ tokens[i].type ].apply( null, tokens[i].matches );
	
				// Return special upon seeing a positional matcher
				if ( matcher[ expando ] ) {
					// Find the next relative operator (if any) for proper handling
					j = ++i;
					for ( ; j < len; j++ ) {
						if ( Expr.relative[ tokens[j].type ] ) {
							break;
						}
					}
					return setMatcher(
						i > 1 && elementMatcher( matchers ),
						i > 1 && toSelector(
							// If the preceding token was a descendant combinator, insert an implicit any-element `*`
							tokens.slice( 0, i - 1 ).concat({ value: tokens[ i - 2 ].type === " " ? "*" : "" })
						).replace( rtrim, "$1" ),
						matcher,
						i < j && matcherFromTokens( tokens.slice( i, j ) ),
						j < len && matcherFromTokens( (tokens = tokens.slice( j )) ),
						j < len && toSelector( tokens )
					);
				}
				matchers.push( matcher );
			}
		}
	
		return elementMatcher( matchers );
	}
	
	function matcherFromGroupMatchers( elementMatchers, setMatchers ) {
		var bySet = setMatchers.length > 0,
			byElement = elementMatchers.length > 0,
			superMatcher = function( seed, context, xml, results, outermost ) {
				var elem, j, matcher,
					matchedCount = 0,
					i = "0",
					unmatched = seed && [],
					setMatched = [],
					contextBackup = outermostContext,
					// We must always have either seed elements or outermost context
					elems = seed || byElement && Expr.find["TAG"]( "*", outermost ),
					// Use integer dirruns iff this is the outermost matcher
					dirrunsUnique = (dirruns += contextBackup == null ? 1 : Math.random() || 0.1),
					len = elems.length;
	
				if ( outermost ) {
					outermostContext = context === document || context || outermost;
				}
	
				// Add elements passing elementMatchers directly to results
				// Support: IE<9, Safari
				// Tolerate NodeList properties (IE: "length"; Safari: <number>) matching elements by id
				for ( ; i !== len && (elem = elems[i]) != null; i++ ) {
					if ( byElement && elem ) {
						j = 0;
						if ( !context && elem.ownerDocument !== document ) {
							setDocument( elem );
							xml = !documentIsHTML;
						}
						while ( (matcher = elementMatchers[j++]) ) {
							if ( matcher( elem, context || document, xml) ) {
								results.push( elem );
								break;
							}
						}
						if ( outermost ) {
							dirruns = dirrunsUnique;
						}
					}
	
					// Track unmatched elements for set filters
					if ( bySet ) {
						// They will have gone through all possible matchers
						if ( (elem = !matcher && elem) ) {
							matchedCount--;
						}
	
						// Lengthen the array for every element, matched or not
						if ( seed ) {
							unmatched.push( elem );
						}
					}
				}
	
				// `i` is now the count of elements visited above, and adding it to `matchedCount`
				// makes the latter nonnegative.
				matchedCount += i;
	
				// Apply set filters to unmatched elements
				// NOTE: This can be skipped if there are no unmatched elements (i.e., `matchedCount`
				// equals `i`), unless we didn't visit _any_ elements in the above loop because we have
				// no element matchers and no seed.
				// Incrementing an initially-string "0" `i` allows `i` to remain a string only in that
				// case, which will result in a "00" `matchedCount` that differs from `i` but is also
				// numerically zero.
				if ( bySet && i !== matchedCount ) {
					j = 0;
					while ( (matcher = setMatchers[j++]) ) {
						matcher( unmatched, setMatched, context, xml );
					}
	
					if ( seed ) {
						// Reintegrate element matches to eliminate the need for sorting
						if ( matchedCount > 0 ) {
							while ( i-- ) {
								if ( !(unmatched[i] || setMatched[i]) ) {
									setMatched[i] = pop.call( results );
								}
							}
						}
	
						// Discard index placeholder values to get only actual matches
						setMatched = condense( setMatched );
					}
	
					// Add matches to results
					push.apply( results, setMatched );
	
					// Seedless set matches succeeding multiple successful matchers stipulate sorting
					if ( outermost && !seed && setMatched.length > 0 &&
						( matchedCount + setMatchers.length ) > 1 ) {
	
						Sizzle.uniqueSort( results );
					}
				}
	
				// Override manipulation of globals by nested matchers
				if ( outermost ) {
					dirruns = dirrunsUnique;
					outermostContext = contextBackup;
				}
	
				return unmatched;
			};
	
		return bySet ?
			markFunction( superMatcher ) :
			superMatcher;
	}
	
	compile = Sizzle.compile = function( selector, match /* Internal Use Only */ ) {
		var i,
			setMatchers = [],
			elementMatchers = [],
			cached = compilerCache[ selector + " " ];
	
		if ( !cached ) {
			// Generate a function of recursive functions that can be used to check each element
			if ( !match ) {
				match = tokenize( selector );
			}
			i = match.length;
			while ( i-- ) {
				cached = matcherFromTokens( match[i] );
				if ( cached[ expando ] ) {
					setMatchers.push( cached );
				} else {
					elementMatchers.push( cached );
				}
			}
	
			// Cache the compiled function
			cached = compilerCache( selector, matcherFromGroupMatchers( elementMatchers, setMatchers ) );
	
			// Save selector and tokenization
			cached.selector = selector;
		}
		return cached;
	};
	
	/**
	 * A low-level selection function that works with Sizzle's compiled
	 *  selector functions
	 * @param {String|Function} selector A selector or a pre-compiled
	 *  selector function built with Sizzle.compile
	 * @param {Element} context
	 * @param {Array} [results]
	 * @param {Array} [seed] A set of elements to match against
	 */
	select = Sizzle.select = function( selector, context, results, seed ) {
		var i, tokens, token, type, find,
			compiled = typeof selector === "function" && selector,
			match = !seed && tokenize( (selector = compiled.selector || selector) );
	
		results = results || [];
	
		// Try to minimize operations if there is only one selector in the list and no seed
		// (the latter of which guarantees us context)
		if ( match.length === 1 ) {
	
			// Reduce context if the leading compound selector is an ID
			tokens = match[0] = match[0].slice( 0 );
			if ( tokens.length > 2 && (token = tokens[0]).type === "ID" &&
					support.getById && context.nodeType === 9 && documentIsHTML &&
					Expr.relative[ tokens[1].type ] ) {
	
				context = ( Expr.find["ID"]( token.matches[0].replace(runescape, funescape), context ) || [] )[0];
				if ( !context ) {
					return results;
	
				// Precompiled matchers will still verify ancestry, so step up a level
				} else if ( compiled ) {
					context = context.parentNode;
				}
	
				selector = selector.slice( tokens.shift().value.length );
			}
	
			// Fetch a seed set for right-to-left matching
			i = matchExpr["needsContext"].test( selector ) ? 0 : tokens.length;
			while ( i-- ) {
				token = tokens[i];
	
				// Abort if we hit a combinator
				if ( Expr.relative[ (type = token.type) ] ) {
					break;
				}
				if ( (find = Expr.find[ type ]) ) {
					// Search, expanding context for leading sibling combinators
					if ( (seed = find(
						token.matches[0].replace( runescape, funescape ),
						rsibling.test( tokens[0].type ) && testContext( context.parentNode ) || context
					)) ) {
	
						// If seed is empty or no tokens remain, we can return early
						tokens.splice( i, 1 );
						selector = seed.length && toSelector( tokens );
						if ( !selector ) {
							push.apply( results, seed );
							return results;
						}
	
						break;
					}
				}
			}
		}
	
		// Compile and execute a filtering function if one is not provided
		// Provide `match` to avoid retokenization if we modified the selector above
		( compiled || compile( selector, match ) )(
			seed,
			context,
			!documentIsHTML,
			results,
			!context || rsibling.test( selector ) && testContext( context.parentNode ) || context
		);
		return results;
	};
	
	// One-time assignments
	
	// Sort stability
	support.sortStable = expando.split("").sort( sortOrder ).join("") === expando;
	
	// Support: Chrome 14-35+
	// Always assume duplicates if they aren't passed to the comparison function
	support.detectDuplicates = !!hasDuplicate;
	
	// Initialize against the default document
	setDocument();
	
	// Support: Webkit<537.32 - Safari 6.0.3/Chrome 25 (fixed in Chrome 27)
	// Detached nodes confoundingly follow *each other*
	support.sortDetached = assert(function( div1 ) {
		// Should return 1, but returns 4 (following)
		return div1.compareDocumentPosition( document.createElement("div") ) & 1;
	});
	
	// Support: IE<8
	// Prevent attribute/property "interpolation"
	// http://msdn.microsoft.com/en-us/library/ms536429%28VS.85%29.aspx
	if ( !assert(function( div ) {
		div.innerHTML = "<a href='#'></a>";
		return div.firstChild.getAttribute("href") === "#" ;
	}) ) {
		addHandle( "type|href|height|width", function( elem, name, isXML ) {
			if ( !isXML ) {
				return elem.getAttribute( name, name.toLowerCase() === "type" ? 1 : 2 );
			}
		});
	}
	
	// Support: IE<9
	// Use defaultValue in place of getAttribute("value")
	if ( !support.attributes || !assert(function( div ) {
		div.innerHTML = "<input/>";
		div.firstChild.setAttribute( "value", "" );
		return div.firstChild.getAttribute( "value" ) === "";
	}) ) {
		addHandle( "value", function( elem, name, isXML ) {
			if ( !isXML && elem.nodeName.toLowerCase() === "input" ) {
				return elem.defaultValue;
			}
		});
	}
	
	// Support: IE<9
	// Use getAttributeNode to fetch booleans when getAttribute lies
	if ( !assert(function( div ) {
		return div.getAttribute("disabled") == null;
	}) ) {
		addHandle( booleans, function( elem, name, isXML ) {
			var val;
			if ( !isXML ) {
				return elem[ name ] === true ? name.toLowerCase() :
						(val = elem.getAttributeNode( name )) && val.specified ?
						val.value :
					null;
			}
		});
	}
	
	return Sizzle;
	
	})( window );
	
	
	
	jQuery.find = Sizzle;
	jQuery.expr = Sizzle.selectors;
	jQuery.expr[ ":" ] = jQuery.expr.pseudos;
	jQuery.uniqueSort = jQuery.unique = Sizzle.uniqueSort;
	jQuery.text = Sizzle.getText;
	jQuery.isXMLDoc = Sizzle.isXML;
	jQuery.contains = Sizzle.contains;
	
	
	
	var dir = function( elem, dir, until ) {
		var matched = [],
			truncate = until !== undefined;
	
		while ( ( elem = elem[ dir ] ) && elem.nodeType !== 9 ) {
			if ( elem.nodeType === 1 ) {
				if ( truncate && jQuery( elem ).is( until ) ) {
					break;
				}
				matched.push( elem );
			}
		}
		return matched;
	};
	
	
	var siblings = function( n, elem ) {
		var matched = [];
	
		for ( ; n; n = n.nextSibling ) {
			if ( n.nodeType === 1 && n !== elem ) {
				matched.push( n );
			}
		}
	
		return matched;
	};
	
	
	var rneedsContext = jQuery.expr.match.needsContext;
	
	var rsingleTag = ( /^<([\w-]+)\s*\/?>(?:<\/\1>|)$/ );
	
	
	
	var risSimple = /^.[^:#\[\.,]*$/;
	
	// Implement the identical functionality for filter and not
	function winnow( elements, qualifier, not ) {
		if ( jQuery.isFunction( qualifier ) ) {
			return jQuery.grep( elements, function( elem, i ) {
				/* jshint -W018 */
				return !!qualifier.call( elem, i, elem ) !== not;
			} );
	
		}
	
		if ( qualifier.nodeType ) {
			return jQuery.grep( elements, function( elem ) {
				return ( elem === qualifier ) !== not;
			} );
	
		}
	
		if ( typeof qualifier === "string" ) {
			if ( risSimple.test( qualifier ) ) {
				return jQuery.filter( qualifier, elements, not );
			}
	
			qualifier = jQuery.filter( qualifier, elements );
		}
	
		return jQuery.grep( elements, function( elem ) {
			return ( jQuery.inArray( elem, qualifier ) > -1 ) !== not;
		} );
	}
	
	jQuery.filter = function( expr, elems, not ) {
		var elem = elems[ 0 ];
	
		if ( not ) {
			expr = ":not(" + expr + ")";
		}
	
		return elems.length === 1 && elem.nodeType === 1 ?
			jQuery.find.matchesSelector( elem, expr ) ? [ elem ] : [] :
			jQuery.find.matches( expr, jQuery.grep( elems, function( elem ) {
				return elem.nodeType === 1;
			} ) );
	};
	
	jQuery.fn.extend( {
		find: function( selector ) {
			var i,
				ret = [],
				self = this,
				len = self.length;
	
			if ( typeof selector !== "string" ) {
				return this.pushStack( jQuery( selector ).filter( function() {
					for ( i = 0; i < len; i++ ) {
						if ( jQuery.contains( self[ i ], this ) ) {
							return true;
						}
					}
				} ) );
			}
	
			for ( i = 0; i < len; i++ ) {
				jQuery.find( selector, self[ i ], ret );
			}
	
			// Needed because $( selector, context ) becomes $( context ).find( selector )
			ret = this.pushStack( len > 1 ? jQuery.unique( ret ) : ret );
			ret.selector = this.selector ? this.selector + " " + selector : selector;
			return ret;
		},
		filter: function( selector ) {
			return this.pushStack( winnow( this, selector || [], false ) );
		},
		not: function( selector ) {
			return this.pushStack( winnow( this, selector || [], true ) );
		},
		is: function( selector ) {
			return !!winnow(
				this,
	
				// If this is a positional/relative selector, check membership in the returned set
				// so $("p:first").is("p:last") won't return true for a doc with two "p".
				typeof selector === "string" && rneedsContext.test( selector ) ?
					jQuery( selector ) :
					selector || [],
				false
			).length;
		}
	} );
	
	
	// Initialize a jQuery object
	
	
	// A central reference to the root jQuery(document)
	var rootjQuery,
	
		// A simple way to check for HTML strings
		// Prioritize #id over <tag> to avoid XSS via location.hash (#9521)
		// Strict HTML recognition (#11290: must start with <)
		rquickExpr = /^(?:\s*(<[\w\W]+>)[^>]*|#([\w-]*))$/,
	
		init = jQuery.fn.init = function( selector, context, root ) {
			var match, elem;
	
			// HANDLE: $(""), $(null), $(undefined), $(false)
			if ( !selector ) {
				return this;
			}
	
			// init accepts an alternate rootjQuery
			// so migrate can support jQuery.sub (gh-2101)
			root = root || rootjQuery;
	
			// Handle HTML strings
			if ( typeof selector === "string" ) {
				if ( selector.charAt( 0 ) === "<" &&
					selector.charAt( selector.length - 1 ) === ">" &&
					selector.length >= 3 ) {
	
					// Assume that strings that start and end with <> are HTML and skip the regex check
					match = [ null, selector, null ];
	
				} else {
					match = rquickExpr.exec( selector );
				}
	
				// Match html or make sure no context is specified for #id
				if ( match && ( match[ 1 ] || !context ) ) {
	
					// HANDLE: $(html) -> $(array)
					if ( match[ 1 ] ) {
						context = context instanceof jQuery ? context[ 0 ] : context;
	
						// scripts is true for back-compat
						// Intentionally let the error be thrown if parseHTML is not present
						jQuery.merge( this, jQuery.parseHTML(
							match[ 1 ],
							context && context.nodeType ? context.ownerDocument || context : document,
							true
						) );
	
						// HANDLE: $(html, props)
						if ( rsingleTag.test( match[ 1 ] ) && jQuery.isPlainObject( context ) ) {
							for ( match in context ) {
	
								// Properties of context are called as methods if possible
								if ( jQuery.isFunction( this[ match ] ) ) {
									this[ match ]( context[ match ] );
	
								// ...and otherwise set as attributes
								} else {
									this.attr( match, context[ match ] );
								}
							}
						}
	
						return this;
	
					// HANDLE: $(#id)
					} else {
						elem = document.getElementById( match[ 2 ] );
	
						// Check parentNode to catch when Blackberry 4.6 returns
						// nodes that are no longer in the document #6963
						if ( elem && elem.parentNode ) {
	
							// Handle the case where IE and Opera return items
							// by name instead of ID
							if ( elem.id !== match[ 2 ] ) {
								return rootjQuery.find( selector );
							}
	
							// Otherwise, we inject the element directly into the jQuery object
							this.length = 1;
							this[ 0 ] = elem;
						}
	
						this.context = document;
						this.selector = selector;
						return this;
					}
	
				// HANDLE: $(expr, $(...))
				} else if ( !context || context.jquery ) {
					return ( context || root ).find( selector );
	
				// HANDLE: $(expr, context)
				// (which is just equivalent to: $(context).find(expr)
				} else {
					return this.constructor( context ).find( selector );
				}
	
			// HANDLE: $(DOMElement)
			} else if ( selector.nodeType ) {
				this.context = this[ 0 ] = selector;
				this.length = 1;
				return this;
	
			// HANDLE: $(function)
			// Shortcut for document ready
			} else if ( jQuery.isFunction( selector ) ) {
				return typeof root.ready !== "undefined" ?
					root.ready( selector ) :
	
					// Execute immediately if ready is not present
					selector( jQuery );
			}
	
			if ( selector.selector !== undefined ) {
				this.selector = selector.selector;
				this.context = selector.context;
			}
	
			return jQuery.makeArray( selector, this );
		};
	
	// Give the init function the jQuery prototype for later instantiation
	init.prototype = jQuery.fn;
	
	// Initialize central reference
	rootjQuery = jQuery( document );
	
	
	var rparentsprev = /^(?:parents|prev(?:Until|All))/,
	
		// methods guaranteed to produce a unique set when starting from a unique set
		guaranteedUnique = {
			children: true,
			contents: true,
			next: true,
			prev: true
		};
	
	jQuery.fn.extend( {
		has: function( target ) {
			var i,
				targets = jQuery( target, this ),
				len = targets.length;
	
			return this.filter( function() {
				for ( i = 0; i < len; i++ ) {
					if ( jQuery.contains( this, targets[ i ] ) ) {
						return true;
					}
				}
			} );
		},
	
		closest: function( selectors, context ) {
			var cur,
				i = 0,
				l = this.length,
				matched = [],
				pos = rneedsContext.test( selectors ) || typeof selectors !== "string" ?
					jQuery( selectors, context || this.context ) :
					0;
	
			for ( ; i < l; i++ ) {
				for ( cur = this[ i ]; cur && cur !== context; cur = cur.parentNode ) {
	
					// Always skip document fragments
					if ( cur.nodeType < 11 && ( pos ?
						pos.index( cur ) > -1 :
	
						// Don't pass non-elements to Sizzle
						cur.nodeType === 1 &&
							jQuery.find.matchesSelector( cur, selectors ) ) ) {
	
						matched.push( cur );
						break;
					}
				}
			}
	
			return this.pushStack( matched.length > 1 ? jQuery.uniqueSort( matched ) : matched );
		},
	
		// Determine the position of an element within
		// the matched set of elements
		index: function( elem ) {
	
			// No argument, return index in parent
			if ( !elem ) {
				return ( this[ 0 ] && this[ 0 ].parentNode ) ? this.first().prevAll().length : -1;
			}
	
			// index in selector
			if ( typeof elem === "string" ) {
				return jQuery.inArray( this[ 0 ], jQuery( elem ) );
			}
	
			// Locate the position of the desired element
			return jQuery.inArray(
	
				// If it receives a jQuery object, the first element is used
				elem.jquery ? elem[ 0 ] : elem, this );
		},
	
		add: function( selector, context ) {
			return this.pushStack(
				jQuery.uniqueSort(
					jQuery.merge( this.get(), jQuery( selector, context ) )
				)
			);
		},
	
		addBack: function( selector ) {
			return this.add( selector == null ?
				this.prevObject : this.prevObject.filter( selector )
			);
		}
	} );
	
	function sibling( cur, dir ) {
		do {
			cur = cur[ dir ];
		} while ( cur && cur.nodeType !== 1 );
	
		return cur;
	}
	
	jQuery.each( {
		parent: function( elem ) {
			var parent = elem.parentNode;
			return parent && parent.nodeType !== 11 ? parent : null;
		},
		parents: function( elem ) {
			return dir( elem, "parentNode" );
		},
		parentsUntil: function( elem, i, until ) {
			return dir( elem, "parentNode", until );
		},
		next: function( elem ) {
			return sibling( elem, "nextSibling" );
		},
		prev: function( elem ) {
			return sibling( elem, "previousSibling" );
		},
		nextAll: function( elem ) {
			return dir( elem, "nextSibling" );
		},
		prevAll: function( elem ) {
			return dir( elem, "previousSibling" );
		},
		nextUntil: function( elem, i, until ) {
			return dir( elem, "nextSibling", until );
		},
		prevUntil: function( elem, i, until ) {
			return dir( elem, "previousSibling", until );
		},
		siblings: function( elem ) {
			return siblings( ( elem.parentNode || {} ).firstChild, elem );
		},
		children: function( elem ) {
			return siblings( elem.firstChild );
		},
		contents: function( elem ) {
			return jQuery.nodeName( elem, "iframe" ) ?
				elem.contentDocument || elem.contentWindow.document :
				jQuery.merge( [], elem.childNodes );
		}
	}, function( name, fn ) {
		jQuery.fn[ name ] = function( until, selector ) {
			var ret = jQuery.map( this, fn, until );
	
			if ( name.slice( -5 ) !== "Until" ) {
				selector = until;
			}
	
			if ( selector && typeof selector === "string" ) {
				ret = jQuery.filter( selector, ret );
			}
	
			if ( this.length > 1 ) {
	
				// Remove duplicates
				if ( !guaranteedUnique[ name ] ) {
					ret = jQuery.uniqueSort( ret );
				}
	
				// Reverse order for parents* and prev-derivatives
				if ( rparentsprev.test( name ) ) {
					ret = ret.reverse();
				}
			}
	
			return this.pushStack( ret );
		};
	} );
	var rnotwhite = ( /\S+/g );
	
	
	
	// Convert String-formatted options into Object-formatted ones
	function createOptions( options ) {
		var object = {};
		jQuery.each( options.match( rnotwhite ) || [], function( _, flag ) {
			object[ flag ] = true;
		} );
		return object;
	}
	
	/*
	 * Create a callback list using the following parameters:
	 *
	 *	options: an optional list of space-separated options that will change how
	 *			the callback list behaves or a more traditional option object
	 *
	 * By default a callback list will act like an event callback list and can be
	 * "fired" multiple times.
	 *
	 * Possible options:
	 *
	 *	once:			will ensure the callback list can only be fired once (like a Deferred)
	 *
	 *	memory:			will keep track of previous values and will call any callback added
	 *					after the list has been fired right away with the latest "memorized"
	 *					values (like a Deferred)
	 *
	 *	unique:			will ensure a callback can only be added once (no duplicate in the list)
	 *
	 *	stopOnFalse:	interrupt callings when a callback returns false
	 *
	 */
	jQuery.Callbacks = function( options ) {
	
		// Convert options from String-formatted to Object-formatted if needed
		// (we check in cache first)
		options = typeof options === "string" ?
			createOptions( options ) :
			jQuery.extend( {}, options );
	
		var // Flag to know if list is currently firing
			firing,
	
			// Last fire value for non-forgettable lists
			memory,
	
			// Flag to know if list was already fired
			fired,
	
			// Flag to prevent firing
			locked,
	
			// Actual callback list
			list = [],
	
			// Queue of execution data for repeatable lists
			queue = [],
	
			// Index of currently firing callback (modified by add/remove as needed)
			firingIndex = -1,
	
			// Fire callbacks
			fire = function() {
	
				// Enforce single-firing
				locked = options.once;
	
				// Execute callbacks for all pending executions,
				// respecting firingIndex overrides and runtime changes
				fired = firing = true;
				for ( ; queue.length; firingIndex = -1 ) {
					memory = queue.shift();
					while ( ++firingIndex < list.length ) {
	
						// Run callback and check for early termination
						if ( list[ firingIndex ].apply( memory[ 0 ], memory[ 1 ] ) === false &&
							options.stopOnFalse ) {
	
							// Jump to end and forget the data so .add doesn't re-fire
							firingIndex = list.length;
							memory = false;
						}
					}
				}
	
				// Forget the data if we're done with it
				if ( !options.memory ) {
					memory = false;
				}
	
				firing = false;
	
				// Clean up if we're done firing for good
				if ( locked ) {
	
					// Keep an empty list if we have data for future add calls
					if ( memory ) {
						list = [];
	
					// Otherwise, this object is spent
					} else {
						list = "";
					}
				}
			},
	
			// Actual Callbacks object
			self = {
	
				// Add a callback or a collection of callbacks to the list
				add: function() {
					if ( list ) {
	
						// If we have memory from a past run, we should fire after adding
						if ( memory && !firing ) {
							firingIndex = list.length - 1;
							queue.push( memory );
						}
	
						( function add( args ) {
							jQuery.each( args, function( _, arg ) {
								if ( jQuery.isFunction( arg ) ) {
									if ( !options.unique || !self.has( arg ) ) {
										list.push( arg );
									}
								} else if ( arg && arg.length && jQuery.type( arg ) !== "string" ) {
	
									// Inspect recursively
									add( arg );
								}
							} );
						} )( arguments );
	
						if ( memory && !firing ) {
							fire();
						}
					}
					return this;
				},
	
				// Remove a callback from the list
				remove: function() {
					jQuery.each( arguments, function( _, arg ) {
						var index;
						while ( ( index = jQuery.inArray( arg, list, index ) ) > -1 ) {
							list.splice( index, 1 );
	
							// Handle firing indexes
							if ( index <= firingIndex ) {
								firingIndex--;
							}
						}
					} );
					return this;
				},
	
				// Check if a given callback is in the list.
				// If no argument is given, return whether or not list has callbacks attached.
				has: function( fn ) {
					return fn ?
						jQuery.inArray( fn, list ) > -1 :
						list.length > 0;
				},
	
				// Remove all callbacks from the list
				empty: function() {
					if ( list ) {
						list = [];
					}
					return this;
				},
	
				// Disable .fire and .add
				// Abort any current/pending executions
				// Clear all callbacks and values
				disable: function() {
					locked = queue = [];
					list = memory = "";
					return this;
				},
				disabled: function() {
					return !list;
				},
	
				// Disable .fire
				// Also disable .add unless we have memory (since it would have no effect)
				// Abort any pending executions
				lock: function() {
					locked = true;
					if ( !memory ) {
						self.disable();
					}
					return this;
				},
				locked: function() {
					return !!locked;
				},
	
				// Call all callbacks with the given context and arguments
				fireWith: function( context, args ) {
					if ( !locked ) {
						args = args || [];
						args = [ context, args.slice ? args.slice() : args ];
						queue.push( args );
						if ( !firing ) {
							fire();
						}
					}
					return this;
				},
	
				// Call all the callbacks with the given arguments
				fire: function() {
					self.fireWith( this, arguments );
					return this;
				},
	
				// To know if the callbacks have already been called at least once
				fired: function() {
					return !!fired;
				}
			};
	
		return self;
	};
	
	
	jQuery.extend( {
	
		Deferred: function( func ) {
			var tuples = [
	
					// action, add listener, listener list, final state
					[ "resolve", "done", jQuery.Callbacks( "once memory" ), "resolved" ],
					[ "reject", "fail", jQuery.Callbacks( "once memory" ), "rejected" ],
					[ "notify", "progress", jQuery.Callbacks( "memory" ) ]
				],
				state = "pending",
				promise = {
					state: function() {
						return state;
					},
					always: function() {
						deferred.done( arguments ).fail( arguments );
						return this;
					},
					then: function( /* fnDone, fnFail, fnProgress */ ) {
						var fns = arguments;
						return jQuery.Deferred( function( newDefer ) {
							jQuery.each( tuples, function( i, tuple ) {
								var fn = jQuery.isFunction( fns[ i ] ) && fns[ i ];
	
								// deferred[ done | fail | progress ] for forwarding actions to newDefer
								deferred[ tuple[ 1 ] ]( function() {
									var returned = fn && fn.apply( this, arguments );
									if ( returned && jQuery.isFunction( returned.promise ) ) {
										returned.promise()
											.progress( newDefer.notify )
											.done( newDefer.resolve )
											.fail( newDefer.reject );
									} else {
										newDefer[ tuple[ 0 ] + "With" ](
											this === promise ? newDefer.promise() : this,
											fn ? [ returned ] : arguments
										);
									}
								} );
							} );
							fns = null;
						} ).promise();
					},
	
					// Get a promise for this deferred
					// If obj is provided, the promise aspect is added to the object
					promise: function( obj ) {
						return obj != null ? jQuery.extend( obj, promise ) : promise;
					}
				},
				deferred = {};
	
			// Keep pipe for back-compat
			promise.pipe = promise.then;
	
			// Add list-specific methods
			jQuery.each( tuples, function( i, tuple ) {
				var list = tuple[ 2 ],
					stateString = tuple[ 3 ];
	
				// promise[ done | fail | progress ] = list.add
				promise[ tuple[ 1 ] ] = list.add;
	
				// Handle state
				if ( stateString ) {
					list.add( function() {
	
						// state = [ resolved | rejected ]
						state = stateString;
	
					// [ reject_list | resolve_list ].disable; progress_list.lock
					}, tuples[ i ^ 1 ][ 2 ].disable, tuples[ 2 ][ 2 ].lock );
				}
	
				// deferred[ resolve | reject | notify ]
				deferred[ tuple[ 0 ] ] = function() {
					deferred[ tuple[ 0 ] + "With" ]( this === deferred ? promise : this, arguments );
					return this;
				};
				deferred[ tuple[ 0 ] + "With" ] = list.fireWith;
			} );
	
			// Make the deferred a promise
			promise.promise( deferred );
	
			// Call given func if any
			if ( func ) {
				func.call( deferred, deferred );
			}
	
			// All done!
			return deferred;
		},
	
		// Deferred helper
		when: function( subordinate /* , ..., subordinateN */ ) {
			var i = 0,
				resolveValues = slice.call( arguments ),
				length = resolveValues.length,
	
				// the count of uncompleted subordinates
				remaining = length !== 1 ||
					( subordinate && jQuery.isFunction( subordinate.promise ) ) ? length : 0,
	
				// the master Deferred.
				// If resolveValues consist of only a single Deferred, just use that.
				deferred = remaining === 1 ? subordinate : jQuery.Deferred(),
	
				// Update function for both resolve and progress values
				updateFunc = function( i, contexts, values ) {
					return function( value ) {
						contexts[ i ] = this;
						values[ i ] = arguments.length > 1 ? slice.call( arguments ) : value;
						if ( values === progressValues ) {
							deferred.notifyWith( contexts, values );
	
						} else if ( !( --remaining ) ) {
							deferred.resolveWith( contexts, values );
						}
					};
				},
	
				progressValues, progressContexts, resolveContexts;
	
			// add listeners to Deferred subordinates; treat others as resolved
			if ( length > 1 ) {
				progressValues = new Array( length );
				progressContexts = new Array( length );
				resolveContexts = new Array( length );
				for ( ; i < length; i++ ) {
					if ( resolveValues[ i ] && jQuery.isFunction( resolveValues[ i ].promise ) ) {
						resolveValues[ i ].promise()
							.progress( updateFunc( i, progressContexts, progressValues ) )
							.done( updateFunc( i, resolveContexts, resolveValues ) )
							.fail( deferred.reject );
					} else {
						--remaining;
					}
				}
			}
	
			// if we're not waiting on anything, resolve the master
			if ( !remaining ) {
				deferred.resolveWith( resolveContexts, resolveValues );
			}
	
			return deferred.promise();
		}
	} );
	
	
	// The deferred used on DOM ready
	var readyList;
	
	jQuery.fn.ready = function( fn ) {
	
		// Add the callback
		jQuery.ready.promise().done( fn );
	
		return this;
	};
	
	jQuery.extend( {
	
		// Is the DOM ready to be used? Set to true once it occurs.
		isReady: false,
	
		// A counter to track how many items to wait for before
		// the ready event fires. See #6781
		readyWait: 1,
	
		// Hold (or release) the ready event
		holdReady: function( hold ) {
			if ( hold ) {
				jQuery.readyWait++;
			} else {
				jQuery.ready( true );
			}
		},
	
		// Handle when the DOM is ready
		ready: function( wait ) {
	
			// Abort if there are pending holds or we're already ready
			if ( wait === true ? --jQuery.readyWait : jQuery.isReady ) {
				return;
			}
	
			// Remember that the DOM is ready
			jQuery.isReady = true;
	
			// If a normal DOM Ready event fired, decrement, and wait if need be
			if ( wait !== true && --jQuery.readyWait > 0 ) {
				return;
			}
	
			// If there are functions bound, to execute
			readyList.resolveWith( document, [ jQuery ] );
	
			// Trigger any bound ready events
			if ( jQuery.fn.triggerHandler ) {
				jQuery( document ).triggerHandler( "ready" );
				jQuery( document ).off( "ready" );
			}
		}
	} );
	
	/**
	 * Clean-up method for dom ready events
	 */
	function detach() {
		if ( document.addEventListener ) {
			document.removeEventListener( "DOMContentLoaded", completed );
			window.removeEventListener( "load", completed );
	
		} else {
			document.detachEvent( "onreadystatechange", completed );
			window.detachEvent( "onload", completed );
		}
	}
	
	/**
	 * The ready event handler and self cleanup method
	 */
	function completed() {
	
		// readyState === "complete" is good enough for us to call the dom ready in oldIE
		if ( document.addEventListener ||
			window.event.type === "load" ||
			document.readyState === "complete" ) {
	
			detach();
			jQuery.ready();
		}
	}
	
	jQuery.ready.promise = function( obj ) {
		if ( !readyList ) {
	
			readyList = jQuery.Deferred();
	
			// Catch cases where $(document).ready() is called
			// after the browser event has already occurred.
			// Support: IE6-10
			// Older IE sometimes signals "interactive" too soon
			if ( document.readyState === "complete" ||
				( document.readyState !== "loading" && !document.documentElement.doScroll ) ) {
	
				// Handle it asynchronously to allow scripts the opportunity to delay ready
				window.setTimeout( jQuery.ready );
	
			// Standards-based browsers support DOMContentLoaded
			} else if ( document.addEventListener ) {
	
				// Use the handy event callback
				document.addEventListener( "DOMContentLoaded", completed );
	
				// A fallback to window.onload, that will always work
				window.addEventListener( "load", completed );
	
			// If IE event model is used
			} else {
	
				// Ensure firing before onload, maybe late but safe also for iframes
				document.attachEvent( "onreadystatechange", completed );
	
				// A fallback to window.onload, that will always work
				window.attachEvent( "onload", completed );
	
				// If IE and not a frame
				// continually check to see if the document is ready
				var top = false;
	
				try {
					top = window.frameElement == null && document.documentElement;
				} catch ( e ) {}
	
				if ( top && top.doScroll ) {
					( function doScrollCheck() {
						if ( !jQuery.isReady ) {
	
							try {
	
								// Use the trick by Diego Perini
								// http://javascript.nwbox.com/IEContentLoaded/
								top.doScroll( "left" );
							} catch ( e ) {
								return window.setTimeout( doScrollCheck, 50 );
							}
	
							// detach all dom ready events
							detach();
	
							// and execute any waiting functions
							jQuery.ready();
						}
					} )();
				}
			}
		}
		return readyList.promise( obj );
	};
	
	// Kick off the DOM ready check even if the user does not
	jQuery.ready.promise();
	
	
	
	
	// Support: IE<9
	// Iteration over object's inherited properties before its own
	var i;
	for ( i in jQuery( support ) ) {
		break;
	}
	support.ownFirst = i === "0";
	
	// Note: most support tests are defined in their respective modules.
	// false until the test is run
	support.inlineBlockNeedsLayout = false;
	
	// Execute ASAP in case we need to set body.style.zoom
	jQuery( function() {
	
		// Minified: var a,b,c,d
		var val, div, body, container;
	
		body = document.getElementsByTagName( "body" )[ 0 ];
		if ( !body || !body.style ) {
	
			// Return for frameset docs that don't have a body
			return;
		}
	
		// Setup
		div = document.createElement( "div" );
		container = document.createElement( "div" );
		container.style.cssText = "position:absolute;border:0;width:0;height:0;top:0;left:-9999px";
		body.appendChild( container ).appendChild( div );
	
		if ( typeof div.style.zoom !== "undefined" ) {
	
			// Support: IE<8
			// Check if natively block-level elements act like inline-block
			// elements when setting their display to 'inline' and giving
			// them layout
			div.style.cssText = "display:inline;margin:0;border:0;padding:1px;width:1px;zoom:1";
	
			support.inlineBlockNeedsLayout = val = div.offsetWidth === 3;
			if ( val ) {
	
				// Prevent IE 6 from affecting layout for positioned elements #11048
				// Prevent IE from shrinking the body in IE 7 mode #12869
				// Support: IE<8
				body.style.zoom = 1;
			}
		}
	
		body.removeChild( container );
	} );
	
	
	( function() {
		var div = document.createElement( "div" );
	
		// Support: IE<9
		support.deleteExpando = true;
		try {
			delete div.test;
		} catch ( e ) {
			support.deleteExpando = false;
		}
	
		// Null elements to avoid leaks in IE.
		div = null;
	} )();
	var acceptData = function( elem ) {
		var noData = jQuery.noData[ ( elem.nodeName + " " ).toLowerCase() ],
			nodeType = +elem.nodeType || 1;
	
		// Do not set data on non-element DOM nodes because it will not be cleared (#8335).
		return nodeType !== 1 && nodeType !== 9 ?
			false :
	
			// Nodes accept data unless otherwise specified; rejection can be conditional
			!noData || noData !== true && elem.getAttribute( "classid" ) === noData;
	};
	
	
	
	
	var rbrace = /^(?:\{[\w\W]*\}|\[[\w\W]*\])$/,
		rmultiDash = /([A-Z])/g;
	
	function dataAttr( elem, key, data ) {
	
		// If nothing was found internally, try to fetch any
		// data from the HTML5 data-* attribute
		if ( data === undefined && elem.nodeType === 1 ) {
	
			var name = "data-" + key.replace( rmultiDash, "-$1" ).toLowerCase();
	
			data = elem.getAttribute( name );
	
			if ( typeof data === "string" ) {
				try {
					data = data === "true" ? true :
						data === "false" ? false :
						data === "null" ? null :
	
						// Only convert to a number if it doesn't change the string
						+data + "" === data ? +data :
						rbrace.test( data ) ? jQuery.parseJSON( data ) :
						data;
				} catch ( e ) {}
	
				// Make sure we set the data so it isn't changed later
				jQuery.data( elem, key, data );
	
			} else {
				data = undefined;
			}
		}
	
		return data;
	}
	
	// checks a cache object for emptiness
	function isEmptyDataObject( obj ) {
		var name;
		for ( name in obj ) {
	
			// if the public data object is empty, the private is still empty
			if ( name === "data" && jQuery.isEmptyObject( obj[ name ] ) ) {
				continue;
			}
			if ( name !== "toJSON" ) {
				return false;
			}
		}
	
		return true;
	}
	
	function internalData( elem, name, data, pvt /* Internal Use Only */ ) {
		if ( !acceptData( elem ) ) {
			return;
		}
	
		var ret, thisCache,
			internalKey = jQuery.expando,
	
			// We have to handle DOM nodes and JS objects differently because IE6-7
			// can't GC object references properly across the DOM-JS boundary
			isNode = elem.nodeType,
	
			// Only DOM nodes need the global jQuery cache; JS object data is
			// attached directly to the object so GC can occur automatically
			cache = isNode ? jQuery.cache : elem,
	
			// Only defining an ID for JS objects if its cache already exists allows
			// the code to shortcut on the same path as a DOM node with no cache
			id = isNode ? elem[ internalKey ] : elem[ internalKey ] && internalKey;
	
		// Avoid doing any more work than we need to when trying to get data on an
		// object that has no data at all
		if ( ( !id || !cache[ id ] || ( !pvt && !cache[ id ].data ) ) &&
			data === undefined && typeof name === "string" ) {
			return;
		}
	
		if ( !id ) {
	
			// Only DOM nodes need a new unique ID for each element since their data
			// ends up in the global cache
			if ( isNode ) {
				id = elem[ internalKey ] = deletedIds.pop() || jQuery.guid++;
			} else {
				id = internalKey;
			}
		}
	
		if ( !cache[ id ] ) {
	
			// Avoid exposing jQuery metadata on plain JS objects when the object
			// is serialized using JSON.stringify
			cache[ id ] = isNode ? {} : { toJSON: jQuery.noop };
		}
	
		// An object can be passed to jQuery.data instead of a key/value pair; this gets
		// shallow copied over onto the existing cache
		if ( typeof name === "object" || typeof name === "function" ) {
			if ( pvt ) {
				cache[ id ] = jQuery.extend( cache[ id ], name );
			} else {
				cache[ id ].data = jQuery.extend( cache[ id ].data, name );
			}
		}
	
		thisCache = cache[ id ];
	
		// jQuery data() is stored in a separate object inside the object's internal data
		// cache in order to avoid key collisions between internal data and user-defined
		// data.
		if ( !pvt ) {
			if ( !thisCache.data ) {
				thisCache.data = {};
			}
	
			thisCache = thisCache.data;
		}
	
		if ( data !== undefined ) {
			thisCache[ jQuery.camelCase( name ) ] = data;
		}
	
		// Check for both converted-to-camel and non-converted data property names
		// If a data property was specified
		if ( typeof name === "string" ) {
	
			// First Try to find as-is property data
			ret = thisCache[ name ];
	
			// Test for null|undefined property data
			if ( ret == null ) {
	
				// Try to find the camelCased property
				ret = thisCache[ jQuery.camelCase( name ) ];
			}
		} else {
			ret = thisCache;
		}
	
		return ret;
	}
	
	function internalRemoveData( elem, name, pvt ) {
		if ( !acceptData( elem ) ) {
			return;
		}
	
		var thisCache, i,
			isNode = elem.nodeType,
	
			// See jQuery.data for more information
			cache = isNode ? jQuery.cache : elem,
			id = isNode ? elem[ jQuery.expando ] : jQuery.expando;
	
		// If there is already no cache entry for this object, there is no
		// purpose in continuing
		if ( !cache[ id ] ) {
			return;
		}
	
		if ( name ) {
	
			thisCache = pvt ? cache[ id ] : cache[ id ].data;
	
			if ( thisCache ) {
	
				// Support array or space separated string names for data keys
				if ( !jQuery.isArray( name ) ) {
	
					// try the string as a key before any manipulation
					if ( name in thisCache ) {
						name = [ name ];
					} else {
	
						// split the camel cased version by spaces unless a key with the spaces exists
						name = jQuery.camelCase( name );
						if ( name in thisCache ) {
							name = [ name ];
						} else {
							name = name.split( " " );
						}
					}
				} else {
	
					// If "name" is an array of keys...
					// When data is initially created, via ("key", "val") signature,
					// keys will be converted to camelCase.
					// Since there is no way to tell _how_ a key was added, remove
					// both plain key and camelCase key. #12786
					// This will only penalize the array argument path.
					name = name.concat( jQuery.map( name, jQuery.camelCase ) );
				}
	
				i = name.length;
				while ( i-- ) {
					delete thisCache[ name[ i ] ];
				}
	
				// If there is no data left in the cache, we want to continue
				// and let the cache object itself get destroyed
				if ( pvt ? !isEmptyDataObject( thisCache ) : !jQuery.isEmptyObject( thisCache ) ) {
					return;
				}
			}
		}
	
		// See jQuery.data for more information
		if ( !pvt ) {
			delete cache[ id ].data;
	
			// Don't destroy the parent cache unless the internal data object
			// had been the only thing left in it
			if ( !isEmptyDataObject( cache[ id ] ) ) {
				return;
			}
		}
	
		// Destroy the cache
		if ( isNode ) {
			jQuery.cleanData( [ elem ], true );
	
		// Use delete when supported for expandos or `cache` is not a window per isWindow (#10080)
		/* jshint eqeqeq: false */
		} else if ( support.deleteExpando || cache != cache.window ) {
			/* jshint eqeqeq: true */
			delete cache[ id ];
	
		// When all else fails, undefined
		} else {
			cache[ id ] = undefined;
		}
	}
	
	jQuery.extend( {
		cache: {},
	
		// The following elements (space-suffixed to avoid Object.prototype collisions)
		// throw uncatchable exceptions if you attempt to set expando properties
		noData: {
			"applet ": true,
			"embed ": true,
	
			// ...but Flash objects (which have this classid) *can* handle expandos
			"object ": "clsid:D27CDB6E-AE6D-11cf-96B8-444553540000"
		},
	
		hasData: function( elem ) {
			elem = elem.nodeType ? jQuery.cache[ elem[ jQuery.expando ] ] : elem[ jQuery.expando ];
			return !!elem && !isEmptyDataObject( elem );
		},
	
		data: function( elem, name, data ) {
			return internalData( elem, name, data );
		},
	
		removeData: function( elem, name ) {
			return internalRemoveData( elem, name );
		},
	
		// For internal use only.
		_data: function( elem, name, data ) {
			return internalData( elem, name, data, true );
		},
	
		_removeData: function( elem, name ) {
			return internalRemoveData( elem, name, true );
		}
	} );
	
	jQuery.fn.extend( {
		data: function( key, value ) {
			var i, name, data,
				elem = this[ 0 ],
				attrs = elem && elem.attributes;
	
			// Special expections of .data basically thwart jQuery.access,
			// so implement the relevant behavior ourselves
	
			// Gets all values
			if ( key === undefined ) {
				if ( this.length ) {
					data = jQuery.data( elem );
	
					if ( elem.nodeType === 1 && !jQuery._data( elem, "parsedAttrs" ) ) {
						i = attrs.length;
						while ( i-- ) {
	
							// Support: IE11+
							// The attrs elements can be null (#14894)
							if ( attrs[ i ] ) {
								name = attrs[ i ].name;
								if ( name.indexOf( "data-" ) === 0 ) {
									name = jQuery.camelCase( name.slice( 5 ) );
									dataAttr( elem, name, data[ name ] );
								}
							}
						}
						jQuery._data( elem, "parsedAttrs", true );
					}
				}
	
				return data;
			}
	
			// Sets multiple values
			if ( typeof key === "object" ) {
				return this.each( function() {
					jQuery.data( this, key );
				} );
			}
	
			return arguments.length > 1 ?
	
				// Sets one value
				this.each( function() {
					jQuery.data( this, key, value );
				} ) :
	
				// Gets one value
				// Try to fetch any internally stored data first
				elem ? dataAttr( elem, key, jQuery.data( elem, key ) ) : undefined;
		},
	
		removeData: function( key ) {
			return this.each( function() {
				jQuery.removeData( this, key );
			} );
		}
	} );
	
	
	jQuery.extend( {
		queue: function( elem, type, data ) {
			var queue;
	
			if ( elem ) {
				type = ( type || "fx" ) + "queue";
				queue = jQuery._data( elem, type );
	
				// Speed up dequeue by getting out quickly if this is just a lookup
				if ( data ) {
					if ( !queue || jQuery.isArray( data ) ) {
						queue = jQuery._data( elem, type, jQuery.makeArray( data ) );
					} else {
						queue.push( data );
					}
				}
				return queue || [];
			}
		},
	
		dequeue: function( elem, type ) {
			type = type || "fx";
	
			var queue = jQuery.queue( elem, type ),
				startLength = queue.length,
				fn = queue.shift(),
				hooks = jQuery._queueHooks( elem, type ),
				next = function() {
					jQuery.dequeue( elem, type );
				};
	
			// If the fx queue is dequeued, always remove the progress sentinel
			if ( fn === "inprogress" ) {
				fn = queue.shift();
				startLength--;
			}
	
			if ( fn ) {
	
				// Add a progress sentinel to prevent the fx queue from being
				// automatically dequeued
				if ( type === "fx" ) {
					queue.unshift( "inprogress" );
				}
	
				// clear up the last queue stop function
				delete hooks.stop;
				fn.call( elem, next, hooks );
			}
	
			if ( !startLength && hooks ) {
				hooks.empty.fire();
			}
		},
	
		// not intended for public consumption - generates a queueHooks object,
		// or returns the current one
		_queueHooks: function( elem, type ) {
			var key = type + "queueHooks";
			return jQuery._data( elem, key ) || jQuery._data( elem, key, {
				empty: jQuery.Callbacks( "once memory" ).add( function() {
					jQuery._removeData( elem, type + "queue" );
					jQuery._removeData( elem, key );
				} )
			} );
		}
	} );
	
	jQuery.fn.extend( {
		queue: function( type, data ) {
			var setter = 2;
	
			if ( typeof type !== "string" ) {
				data = type;
				type = "fx";
				setter--;
			}
	
			if ( arguments.length < setter ) {
				return jQuery.queue( this[ 0 ], type );
			}
	
			return data === undefined ?
				this :
				this.each( function() {
					var queue = jQuery.queue( this, type, data );
	
					// ensure a hooks for this queue
					jQuery._queueHooks( this, type );
	
					if ( type === "fx" && queue[ 0 ] !== "inprogress" ) {
						jQuery.dequeue( this, type );
					}
				} );
		},
		dequeue: function( type ) {
			return this.each( function() {
				jQuery.dequeue( this, type );
			} );
		},
		clearQueue: function( type ) {
			return this.queue( type || "fx", [] );
		},
	
		// Get a promise resolved when queues of a certain type
		// are emptied (fx is the type by default)
		promise: function( type, obj ) {
			var tmp,
				count = 1,
				defer = jQuery.Deferred(),
				elements = this,
				i = this.length,
				resolve = function() {
					if ( !( --count ) ) {
						defer.resolveWith( elements, [ elements ] );
					}
				};
	
			if ( typeof type !== "string" ) {
				obj = type;
				type = undefined;
			}
			type = type || "fx";
	
			while ( i-- ) {
				tmp = jQuery._data( elements[ i ], type + "queueHooks" );
				if ( tmp && tmp.empty ) {
					count++;
					tmp.empty.add( resolve );
				}
			}
			resolve();
			return defer.promise( obj );
		}
	} );
	
	
	( function() {
		var shrinkWrapBlocksVal;
	
		support.shrinkWrapBlocks = function() {
			if ( shrinkWrapBlocksVal != null ) {
				return shrinkWrapBlocksVal;
			}
	
			// Will be changed later if needed.
			shrinkWrapBlocksVal = false;
	
			// Minified: var b,c,d
			var div, body, container;
	
			body = document.getElementsByTagName( "body" )[ 0 ];
			if ( !body || !body.style ) {
	
				// Test fired too early or in an unsupported environment, exit.
				return;
			}
	
			// Setup
			div = document.createElement( "div" );
			container = document.createElement( "div" );
			container.style.cssText = "position:absolute;border:0;width:0;height:0;top:0;left:-9999px";
			body.appendChild( container ).appendChild( div );
	
			// Support: IE6
			// Check if elements with layout shrink-wrap their children
			if ( typeof div.style.zoom !== "undefined" ) {
	
				// Reset CSS: box-sizing; display; margin; border
				div.style.cssText =
	
					// Support: Firefox<29, Android 2.3
					// Vendor-prefix box-sizing
					"-webkit-box-sizing:content-box;-moz-box-sizing:content-box;" +
					"box-sizing:content-box;display:block;margin:0;border:0;" +
					"padding:1px;width:1px;zoom:1";
				div.appendChild( document.createElement( "div" ) ).style.width = "5px";
				shrinkWrapBlocksVal = div.offsetWidth !== 3;
			}
	
			body.removeChild( container );
	
			return shrinkWrapBlocksVal;
		};
	
	} )();
	var pnum = ( /[+-]?(?:\d*\.|)\d+(?:[eE][+-]?\d+|)/ ).source;
	
	var rcssNum = new RegExp( "^(?:([+-])=|)(" + pnum + ")([a-z%]*)$", "i" );
	
	
	var cssExpand = [ "Top", "Right", "Bottom", "Left" ];
	
	var isHidden = function( elem, el ) {
	
			// isHidden might be called from jQuery#filter function;
			// in that case, element will be second argument
			elem = el || elem;
			return jQuery.css( elem, "display" ) === "none" ||
				!jQuery.contains( elem.ownerDocument, elem );
		};
	
	
	
	function adjustCSS( elem, prop, valueParts, tween ) {
		var adjusted,
			scale = 1,
			maxIterations = 20,
			currentValue = tween ?
				function() { return tween.cur(); } :
				function() { return jQuery.css( elem, prop, "" ); },
			initial = currentValue(),
			unit = valueParts && valueParts[ 3 ] || ( jQuery.cssNumber[ prop ] ? "" : "px" ),
	
			// Starting value computation is required for potential unit mismatches
			initialInUnit = ( jQuery.cssNumber[ prop ] || unit !== "px" && +initial ) &&
				rcssNum.exec( jQuery.css( elem, prop ) );
	
		if ( initialInUnit && initialInUnit[ 3 ] !== unit ) {
	
			// Trust units reported by jQuery.css
			unit = unit || initialInUnit[ 3 ];
	
			// Make sure we update the tween properties later on
			valueParts = valueParts || [];
	
			// Iteratively approximate from a nonzero starting point
			initialInUnit = +initial || 1;
	
			do {
	
				// If previous iteration zeroed out, double until we get *something*.
				// Use string for doubling so we don't accidentally see scale as unchanged below
				scale = scale || ".5";
	
				// Adjust and apply
				initialInUnit = initialInUnit / scale;
				jQuery.style( elem, prop, initialInUnit + unit );
	
			// Update scale, tolerating zero or NaN from tween.cur()
			// Break the loop if scale is unchanged or perfect, or if we've just had enough.
			} while (
				scale !== ( scale = currentValue() / initial ) && scale !== 1 && --maxIterations
			);
		}
	
		if ( valueParts ) {
			initialInUnit = +initialInUnit || +initial || 0;
	
			// Apply relative offset (+=/-=) if specified
			adjusted = valueParts[ 1 ] ?
				initialInUnit + ( valueParts[ 1 ] + 1 ) * valueParts[ 2 ] :
				+valueParts[ 2 ];
			if ( tween ) {
				tween.unit = unit;
				tween.start = initialInUnit;
				tween.end = adjusted;
			}
		}
		return adjusted;
	}
	
	
	// Multifunctional method to get and set values of a collection
	// The value/s can optionally be executed if it's a function
	var access = function( elems, fn, key, value, chainable, emptyGet, raw ) {
		var i = 0,
			length = elems.length,
			bulk = key == null;
	
		// Sets many values
		if ( jQuery.type( key ) === "object" ) {
			chainable = true;
			for ( i in key ) {
				access( elems, fn, i, key[ i ], true, emptyGet, raw );
			}
	
		// Sets one value
		} else if ( value !== undefined ) {
			chainable = true;
	
			if ( !jQuery.isFunction( value ) ) {
				raw = true;
			}
	
			if ( bulk ) {
	
				// Bulk operations run against the entire set
				if ( raw ) {
					fn.call( elems, value );
					fn = null;
	
				// ...except when executing function values
				} else {
					bulk = fn;
					fn = function( elem, key, value ) {
						return bulk.call( jQuery( elem ), value );
					};
				}
			}
	
			if ( fn ) {
				for ( ; i < length; i++ ) {
					fn(
						elems[ i ],
						key,
						raw ? value : value.call( elems[ i ], i, fn( elems[ i ], key ) )
					);
				}
			}
		}
	
		return chainable ?
			elems :
	
			// Gets
			bulk ?
				fn.call( elems ) :
				length ? fn( elems[ 0 ], key ) : emptyGet;
	};
	var rcheckableType = ( /^(?:checkbox|radio)$/i );
	
	var rtagName = ( /<([\w:-]+)/ );
	
	var rscriptType = ( /^$|\/(?:java|ecma)script/i );
	
	var rleadingWhitespace = ( /^\s+/ );
	
	var nodeNames = "abbr|article|aside|audio|bdi|canvas|data|datalist|" +
			"details|dialog|figcaption|figure|footer|header|hgroup|main|" +
			"mark|meter|nav|output|picture|progress|section|summary|template|time|video";
	
	
	
	function createSafeFragment( document ) {
		var list = nodeNames.split( "|" ),
			safeFrag = document.createDocumentFragment();
	
		if ( safeFrag.createElement ) {
			while ( list.length ) {
				safeFrag.createElement(
					list.pop()
				);
			}
		}
		return safeFrag;
	}
	
	
	( function() {
		var div = document.createElement( "div" ),
			fragment = document.createDocumentFragment(),
			input = document.createElement( "input" );
	
		// Setup
		div.innerHTML = "  <link/><table></table><a href='/a'>a</a><input type='checkbox'/>";
	
		// IE strips leading whitespace when .innerHTML is used
		support.leadingWhitespace = div.firstChild.nodeType === 3;
	
		// Make sure that tbody elements aren't automatically inserted
		// IE will insert them into empty tables
		support.tbody = !div.getElementsByTagName( "tbody" ).length;
	
		// Make sure that link elements get serialized correctly by innerHTML
		// This requires a wrapper element in IE
		support.htmlSerialize = !!div.getElementsByTagName( "link" ).length;
	
		// Makes sure cloning an html5 element does not cause problems
		// Where outerHTML is undefined, this still works
		support.html5Clone =
			document.createElement( "nav" ).cloneNode( true ).outerHTML !== "<:nav></:nav>";
	
		// Check if a disconnected checkbox will retain its checked
		// value of true after appended to the DOM (IE6/7)
		input.type = "checkbox";
		input.checked = true;
		fragment.appendChild( input );
		support.appendChecked = input.checked;
	
		// Make sure textarea (and checkbox) defaultValue is properly cloned
		// Support: IE6-IE11+
		div.innerHTML = "<textarea>x</textarea>";
		support.noCloneChecked = !!div.cloneNode( true ).lastChild.defaultValue;
	
		// #11217 - WebKit loses check when the name is after the checked attribute
		fragment.appendChild( div );
	
		// Support: Windows Web Apps (WWA)
		// `name` and `type` must use .setAttribute for WWA (#14901)
		input = document.createElement( "input" );
		input.setAttribute( "type", "radio" );
		input.setAttribute( "checked", "checked" );
		input.setAttribute( "name", "t" );
	
		div.appendChild( input );
	
		// Support: Safari 5.1, iOS 5.1, Android 4.x, Android 2.3
		// old WebKit doesn't clone checked state correctly in fragments
		support.checkClone = div.cloneNode( true ).cloneNode( true ).lastChild.checked;
	
		// Support: IE<9
		// Cloned elements keep attachEvent handlers, we use addEventListener on IE9+
		support.noCloneEvent = !!div.addEventListener;
	
		// Support: IE<9
		// Since attributes and properties are the same in IE,
		// cleanData must set properties to undefined rather than use removeAttribute
		div[ jQuery.expando ] = 1;
		support.attributes = !div.getAttribute( jQuery.expando );
	} )();
	
	
	// We have to close these tags to support XHTML (#13200)
	var wrapMap = {
		option: [ 1, "<select multiple='multiple'>", "</select>" ],
		legend: [ 1, "<fieldset>", "</fieldset>" ],
		area: [ 1, "<map>", "</map>" ],
	
		// Support: IE8
		param: [ 1, "<object>", "</object>" ],
		thead: [ 1, "<table>", "</table>" ],
		tr: [ 2, "<table><tbody>", "</tbody></table>" ],
		col: [ 2, "<table><tbody></tbody><colgroup>", "</colgroup></table>" ],
		td: [ 3, "<table><tbody><tr>", "</tr></tbody></table>" ],
	
		// IE6-8 can't serialize link, script, style, or any html5 (NoScope) tags,
		// unless wrapped in a div with non-breaking characters in front of it.
		_default: support.htmlSerialize ? [ 0, "", "" ] : [ 1, "X<div>", "</div>" ]
	};
	
	// Support: IE8-IE9
	wrapMap.optgroup = wrapMap.option;
	
	wrapMap.tbody = wrapMap.tfoot = wrapMap.colgroup = wrapMap.caption = wrapMap.thead;
	wrapMap.th = wrapMap.td;
	
	
	function getAll( context, tag ) {
		var elems, elem,
			i = 0,
			found = typeof context.getElementsByTagName !== "undefined" ?
				context.getElementsByTagName( tag || "*" ) :
				typeof context.querySelectorAll !== "undefined" ?
					context.querySelectorAll( tag || "*" ) :
					undefined;
	
		if ( !found ) {
			for ( found = [], elems = context.childNodes || context;
				( elem = elems[ i ] ) != null;
				i++
			) {
				if ( !tag || jQuery.nodeName( elem, tag ) ) {
					found.push( elem );
				} else {
					jQuery.merge( found, getAll( elem, tag ) );
				}
			}
		}
	
		return tag === undefined || tag && jQuery.nodeName( context, tag ) ?
			jQuery.merge( [ context ], found ) :
			found;
	}
	
	
	// Mark scripts as having already been evaluated
	function setGlobalEval( elems, refElements ) {
		var elem,
			i = 0;
		for ( ; ( elem = elems[ i ] ) != null; i++ ) {
			jQuery._data(
				elem,
				"globalEval",
				!refElements || jQuery._data( refElements[ i ], "globalEval" )
			);
		}
	}
	
	
	var rhtml = /<|&#?\w+;/,
		rtbody = /<tbody/i;
	
	function fixDefaultChecked( elem ) {
		if ( rcheckableType.test( elem.type ) ) {
			elem.defaultChecked = elem.checked;
		}
	}
	
	function buildFragment( elems, context, scripts, selection, ignored ) {
		var j, elem, contains,
			tmp, tag, tbody, wrap,
			l = elems.length,
	
			// Ensure a safe fragment
			safe = createSafeFragment( context ),
	
			nodes = [],
			i = 0;
	
		for ( ; i < l; i++ ) {
			elem = elems[ i ];
	
			if ( elem || elem === 0 ) {
	
				// Add nodes directly
				if ( jQuery.type( elem ) === "object" ) {
					jQuery.merge( nodes, elem.nodeType ? [ elem ] : elem );
	
				// Convert non-html into a text node
				} else if ( !rhtml.test( elem ) ) {
					nodes.push( context.createTextNode( elem ) );
	
				// Convert html into DOM nodes
				} else {
					tmp = tmp || safe.appendChild( context.createElement( "div" ) );
	
					// Deserialize a standard representation
					tag = ( rtagName.exec( elem ) || [ "", "" ] )[ 1 ].toLowerCase();
					wrap = wrapMap[ tag ] || wrapMap._default;
	
					tmp.innerHTML = wrap[ 1 ] + jQuery.htmlPrefilter( elem ) + wrap[ 2 ];
	
					// Descend through wrappers to the right content
					j = wrap[ 0 ];
					while ( j-- ) {
						tmp = tmp.lastChild;
					}
	
					// Manually add leading whitespace removed by IE
					if ( !support.leadingWhitespace && rleadingWhitespace.test( elem ) ) {
						nodes.push( context.createTextNode( rleadingWhitespace.exec( elem )[ 0 ] ) );
					}
	
					// Remove IE's autoinserted <tbody> from table fragments
					if ( !support.tbody ) {
	
						// String was a <table>, *may* have spurious <tbody>
						elem = tag === "table" && !rtbody.test( elem ) ?
							tmp.firstChild :
	
							// String was a bare <thead> or <tfoot>
							wrap[ 1 ] === "<table>" && !rtbody.test( elem ) ?
								tmp :
								0;
	
						j = elem && elem.childNodes.length;
						while ( j-- ) {
							if ( jQuery.nodeName( ( tbody = elem.childNodes[ j ] ), "tbody" ) &&
								!tbody.childNodes.length ) {
	
								elem.removeChild( tbody );
							}
						}
					}
	
					jQuery.merge( nodes, tmp.childNodes );
	
					// Fix #12392 for WebKit and IE > 9
					tmp.textContent = "";
	
					// Fix #12392 for oldIE
					while ( tmp.firstChild ) {
						tmp.removeChild( tmp.firstChild );
					}
	
					// Remember the top-level container for proper cleanup
					tmp = safe.lastChild;
				}
			}
		}
	
		// Fix #11356: Clear elements from fragment
		if ( tmp ) {
			safe.removeChild( tmp );
		}
	
		// Reset defaultChecked for any radios and checkboxes
		// about to be appended to the DOM in IE 6/7 (#8060)
		if ( !support.appendChecked ) {
			jQuery.grep( getAll( nodes, "input" ), fixDefaultChecked );
		}
	
		i = 0;
		while ( ( elem = nodes[ i++ ] ) ) {
	
			// Skip elements already in the context collection (trac-4087)
			if ( selection && jQuery.inArray( elem, selection ) > -1 ) {
				if ( ignored ) {
					ignored.push( elem );
				}
	
				continue;
			}
	
			contains = jQuery.contains( elem.ownerDocument, elem );
	
			// Append to fragment
			tmp = getAll( safe.appendChild( elem ), "script" );
	
			// Preserve script evaluation history
			if ( contains ) {
				setGlobalEval( tmp );
			}
	
			// Capture executables
			if ( scripts ) {
				j = 0;
				while ( ( elem = tmp[ j++ ] ) ) {
					if ( rscriptType.test( elem.type || "" ) ) {
						scripts.push( elem );
					}
				}
			}
		}
	
		tmp = null;
	
		return safe;
	}
	
	
	( function() {
		var i, eventName,
			div = document.createElement( "div" );
	
		// Support: IE<9 (lack submit/change bubble), Firefox (lack focus(in | out) events)
		for ( i in { submit: true, change: true, focusin: true } ) {
			eventName = "on" + i;
	
			if ( !( support[ i ] = eventName in window ) ) {
	
				// Beware of CSP restrictions (https://developer.mozilla.org/en/Security/CSP)
				div.setAttribute( eventName, "t" );
				support[ i ] = div.attributes[ eventName ].expando === false;
			}
		}
	
		// Null elements to avoid leaks in IE.
		div = null;
	} )();
	
	
	var rformElems = /^(?:input|select|textarea)$/i,
		rkeyEvent = /^key/,
		rmouseEvent = /^(?:mouse|pointer|contextmenu|drag|drop)|click/,
		rfocusMorph = /^(?:focusinfocus|focusoutblur)$/,
		rtypenamespace = /^([^.]*)(?:\.(.+)|)/;
	
	function returnTrue() {
		return true;
	}
	
	function returnFalse() {
		return false;
	}
	
	// Support: IE9
	// See #13393 for more info
	function safeActiveElement() {
		try {
			return document.activeElement;
		} catch ( err ) { }
	}
	
	function on( elem, types, selector, data, fn, one ) {
		var origFn, type;
	
		// Types can be a map of types/handlers
		if ( typeof types === "object" ) {
	
			// ( types-Object, selector, data )
			if ( typeof selector !== "string" ) {
	
				// ( types-Object, data )
				data = data || selector;
				selector = undefined;
			}
			for ( type in types ) {
				on( elem, type, selector, data, types[ type ], one );
			}
			return elem;
		}
	
		if ( data == null && fn == null ) {
	
			// ( types, fn )
			fn = selector;
			data = selector = undefined;
		} else if ( fn == null ) {
			if ( typeof selector === "string" ) {
	
				// ( types, selector, fn )
				fn = data;
				data = undefined;
			} else {
	
				// ( types, data, fn )
				fn = data;
				data = selector;
				selector = undefined;
			}
		}
		if ( fn === false ) {
			fn = returnFalse;
		} else if ( !fn ) {
			return elem;
		}
	
		if ( one === 1 ) {
			origFn = fn;
			fn = function( event ) {
	
				// Can use an empty set, since event contains the info
				jQuery().off( event );
				return origFn.apply( this, arguments );
			};
	
			// Use same guid so caller can remove using origFn
			fn.guid = origFn.guid || ( origFn.guid = jQuery.guid++ );
		}
		return elem.each( function() {
			jQuery.event.add( this, types, fn, data, selector );
		} );
	}
	
	/*
	 * Helper functions for managing events -- not part of the public interface.
	 * Props to Dean Edwards' addEvent library for many of the ideas.
	 */
	jQuery.event = {
	
		global: {},
	
		add: function( elem, types, handler, data, selector ) {
			var tmp, events, t, handleObjIn,
				special, eventHandle, handleObj,
				handlers, type, namespaces, origType,
				elemData = jQuery._data( elem );
	
			// Don't attach events to noData or text/comment nodes (but allow plain objects)
			if ( !elemData ) {
				return;
			}
	
			// Caller can pass in an object of custom data in lieu of the handler
			if ( handler.handler ) {
				handleObjIn = handler;
				handler = handleObjIn.handler;
				selector = handleObjIn.selector;
			}
	
			// Make sure that the handler has a unique ID, used to find/remove it later
			if ( !handler.guid ) {
				handler.guid = jQuery.guid++;
			}
	
			// Init the element's event structure and main handler, if this is the first
			if ( !( events = elemData.events ) ) {
				events = elemData.events = {};
			}
			if ( !( eventHandle = elemData.handle ) ) {
				eventHandle = elemData.handle = function( e ) {
	
					// Discard the second event of a jQuery.event.trigger() and
					// when an event is called after a page has unloaded
					return typeof jQuery !== "undefined" &&
						( !e || jQuery.event.triggered !== e.type ) ?
						jQuery.event.dispatch.apply( eventHandle.elem, arguments ) :
						undefined;
				};
	
				// Add elem as a property of the handle fn to prevent a memory leak
				// with IE non-native events
				eventHandle.elem = elem;
			}
	
			// Handle multiple events separated by a space
			types = ( types || "" ).match( rnotwhite ) || [ "" ];
			t = types.length;
			while ( t-- ) {
				tmp = rtypenamespace.exec( types[ t ] ) || [];
				type = origType = tmp[ 1 ];
				namespaces = ( tmp[ 2 ] || "" ).split( "." ).sort();
	
				// There *must* be a type, no attaching namespace-only handlers
				if ( !type ) {
					continue;
				}
	
				// If event changes its type, use the special event handlers for the changed type
				special = jQuery.event.special[ type ] || {};
	
				// If selector defined, determine special event api type, otherwise given type
				type = ( selector ? special.delegateType : special.bindType ) || type;
	
				// Update special based on newly reset type
				special = jQuery.event.special[ type ] || {};
	
				// handleObj is passed to all event handlers
				handleObj = jQuery.extend( {
					type: type,
					origType: origType,
					data: data,
					handler: handler,
					guid: handler.guid,
					selector: selector,
					needsContext: selector && jQuery.expr.match.needsContext.test( selector ),
					namespace: namespaces.join( "." )
				}, handleObjIn );
	
				// Init the event handler queue if we're the first
				if ( !( handlers = events[ type ] ) ) {
					handlers = events[ type ] = [];
					handlers.delegateCount = 0;
	
					// Only use addEventListener/attachEvent if the special events handler returns false
					if ( !special.setup ||
						special.setup.call( elem, data, namespaces, eventHandle ) === false ) {
	
						// Bind the global event handler to the element
						if ( elem.addEventListener ) {
							elem.addEventListener( type, eventHandle, false );
	
						} else if ( elem.attachEvent ) {
							elem.attachEvent( "on" + type, eventHandle );
						}
					}
				}
	
				if ( special.add ) {
					special.add.call( elem, handleObj );
	
					if ( !handleObj.handler.guid ) {
						handleObj.handler.guid = handler.guid;
					}
				}
	
				// Add to the element's handler list, delegates in front
				if ( selector ) {
					handlers.splice( handlers.delegateCount++, 0, handleObj );
				} else {
					handlers.push( handleObj );
				}
	
				// Keep track of which events have ever been used, for event optimization
				jQuery.event.global[ type ] = true;
			}
	
			// Nullify elem to prevent memory leaks in IE
			elem = null;
		},
	
		// Detach an event or set of events from an element
		remove: function( elem, types, handler, selector, mappedTypes ) {
			var j, handleObj, tmp,
				origCount, t, events,
				special, handlers, type,
				namespaces, origType,
				elemData = jQuery.hasData( elem ) && jQuery._data( elem );
	
			if ( !elemData || !( events = elemData.events ) ) {
				return;
			}
	
			// Once for each type.namespace in types; type may be omitted
			types = ( types || "" ).match( rnotwhite ) || [ "" ];
			t = types.length;
			while ( t-- ) {
				tmp = rtypenamespace.exec( types[ t ] ) || [];
				type = origType = tmp[ 1 ];
				namespaces = ( tmp[ 2 ] || "" ).split( "." ).sort();
	
				// Unbind all events (on this namespace, if provided) for the element
				if ( !type ) {
					for ( type in events ) {
						jQuery.event.remove( elem, type + types[ t ], handler, selector, true );
					}
					continue;
				}
	
				special = jQuery.event.special[ type ] || {};
				type = ( selector ? special.delegateType : special.bindType ) || type;
				handlers = events[ type ] || [];
				tmp = tmp[ 2 ] &&
					new RegExp( "(^|\\.)" + namespaces.join( "\\.(?:.*\\.|)" ) + "(\\.|$)" );
	
				// Remove matching events
				origCount = j = handlers.length;
				while ( j-- ) {
					handleObj = handlers[ j ];
	
					if ( ( mappedTypes || origType === handleObj.origType ) &&
						( !handler || handler.guid === handleObj.guid ) &&
						( !tmp || tmp.test( handleObj.namespace ) ) &&
						( !selector || selector === handleObj.selector ||
							selector === "**" && handleObj.selector ) ) {
						handlers.splice( j, 1 );
	
						if ( handleObj.selector ) {
							handlers.delegateCount--;
						}
						if ( special.remove ) {
							special.remove.call( elem, handleObj );
						}
					}
				}
	
				// Remove generic event handler if we removed something and no more handlers exist
				// (avoids potential for endless recursion during removal of special event handlers)
				if ( origCount && !handlers.length ) {
					if ( !special.teardown ||
						special.teardown.call( elem, namespaces, elemData.handle ) === false ) {
	
						jQuery.removeEvent( elem, type, elemData.handle );
					}
	
					delete events[ type ];
				}
			}
	
			// Remove the expando if it's no longer used
			if ( jQuery.isEmptyObject( events ) ) {
				delete elemData.handle;
	
				// removeData also checks for emptiness and clears the expando if empty
				// so use it instead of delete
				jQuery._removeData( elem, "events" );
			}
		},
	
		trigger: function( event, data, elem, onlyHandlers ) {
			var handle, ontype, cur,
				bubbleType, special, tmp, i,
				eventPath = [ elem || document ],
				type = hasOwn.call( event, "type" ) ? event.type : event,
				namespaces = hasOwn.call( event, "namespace" ) ? event.namespace.split( "." ) : [];
	
			cur = tmp = elem = elem || document;
	
			// Don't do events on text and comment nodes
			if ( elem.nodeType === 3 || elem.nodeType === 8 ) {
				return;
			}
	
			// focus/blur morphs to focusin/out; ensure we're not firing them right now
			if ( rfocusMorph.test( type + jQuery.event.triggered ) ) {
				return;
			}
	
			if ( type.indexOf( "." ) > -1 ) {
	
				// Namespaced trigger; create a regexp to match event type in handle()
				namespaces = type.split( "." );
				type = namespaces.shift();
				namespaces.sort();
			}
			ontype = type.indexOf( ":" ) < 0 && "on" + type;
	
			// Caller can pass in a jQuery.Event object, Object, or just an event type string
			event = event[ jQuery.expando ] ?
				event :
				new jQuery.Event( type, typeof event === "object" && event );
	
			// Trigger bitmask: & 1 for native handlers; & 2 for jQuery (always true)
			event.isTrigger = onlyHandlers ? 2 : 3;
			event.namespace = namespaces.join( "." );
			event.rnamespace = event.namespace ?
				new RegExp( "(^|\\.)" + namespaces.join( "\\.(?:.*\\.|)" ) + "(\\.|$)" ) :
				null;
	
			// Clean up the event in case it is being reused
			event.result = undefined;
			if ( !event.target ) {
				event.target = elem;
			}
	
			// Clone any incoming data and prepend the event, creating the handler arg list
			data = data == null ?
				[ event ] :
				jQuery.makeArray( data, [ event ] );
	
			// Allow special events to draw outside the lines
			special = jQuery.event.special[ type ] || {};
			if ( !onlyHandlers && special.trigger && special.trigger.apply( elem, data ) === false ) {
				return;
			}
	
			// Determine event propagation path in advance, per W3C events spec (#9951)
			// Bubble up to document, then to window; watch for a global ownerDocument var (#9724)
			if ( !onlyHandlers && !special.noBubble && !jQuery.isWindow( elem ) ) {
	
				bubbleType = special.delegateType || type;
				if ( !rfocusMorph.test( bubbleType + type ) ) {
					cur = cur.parentNode;
				}
				for ( ; cur; cur = cur.parentNode ) {
					eventPath.push( cur );
					tmp = cur;
				}
	
				// Only add window if we got to document (e.g., not plain obj or detached DOM)
				if ( tmp === ( elem.ownerDocument || document ) ) {
					eventPath.push( tmp.defaultView || tmp.parentWindow || window );
				}
			}
	
			// Fire handlers on the event path
			i = 0;
			while ( ( cur = eventPath[ i++ ] ) && !event.isPropagationStopped() ) {
	
				event.type = i > 1 ?
					bubbleType :
					special.bindType || type;
	
				// jQuery handler
				handle = ( jQuery._data( cur, "events" ) || {} )[ event.type ] &&
					jQuery._data( cur, "handle" );
	
				if ( handle ) {
					handle.apply( cur, data );
				}
	
				// Native handler
				handle = ontype && cur[ ontype ];
				if ( handle && handle.apply && acceptData( cur ) ) {
					event.result = handle.apply( cur, data );
					if ( event.result === false ) {
						event.preventDefault();
					}
				}
			}
			event.type = type;
	
			// If nobody prevented the default action, do it now
			if ( !onlyHandlers && !event.isDefaultPrevented() ) {
	
				if (
					( !special._default ||
					 special._default.apply( eventPath.pop(), data ) === false
					) && acceptData( elem )
				) {
	
					// Call a native DOM method on the target with the same name name as the event.
					// Can't use an .isFunction() check here because IE6/7 fails that test.
					// Don't do default actions on window, that's where global variables be (#6170)
					if ( ontype && elem[ type ] && !jQuery.isWindow( elem ) ) {
	
						// Don't re-trigger an onFOO event when we call its FOO() method
						tmp = elem[ ontype ];
	
						if ( tmp ) {
							elem[ ontype ] = null;
						}
	
						// Prevent re-triggering of the same event, since we already bubbled it above
						jQuery.event.triggered = type;
						try {
							elem[ type ]();
						} catch ( e ) {
	
							// IE<9 dies on focus/blur to hidden element (#1486,#12518)
							// only reproducible on winXP IE8 native, not IE9 in IE8 mode
						}
						jQuery.event.triggered = undefined;
	
						if ( tmp ) {
							elem[ ontype ] = tmp;
						}
					}
				}
			}
	
			return event.result;
		},
	
		dispatch: function( event ) {
	
			// Make a writable jQuery.Event from the native event object
			event = jQuery.event.fix( event );
	
			var i, j, ret, matched, handleObj,
				handlerQueue = [],
				args = slice.call( arguments ),
				handlers = ( jQuery._data( this, "events" ) || {} )[ event.type ] || [],
				special = jQuery.event.special[ event.type ] || {};
	
			// Use the fix-ed jQuery.Event rather than the (read-only) native event
			args[ 0 ] = event;
			event.delegateTarget = this;
	
			// Call the preDispatch hook for the mapped type, and let it bail if desired
			if ( special.preDispatch && special.preDispatch.call( this, event ) === false ) {
				return;
			}
	
			// Determine handlers
			handlerQueue = jQuery.event.handlers.call( this, event, handlers );
	
			// Run delegates first; they may want to stop propagation beneath us
			i = 0;
			while ( ( matched = handlerQueue[ i++ ] ) && !event.isPropagationStopped() ) {
				event.currentTarget = matched.elem;
	
				j = 0;
				while ( ( handleObj = matched.handlers[ j++ ] ) &&
					!event.isImmediatePropagationStopped() ) {
	
					// Triggered event must either 1) have no namespace, or 2) have namespace(s)
					// a subset or equal to those in the bound event (both can have no namespace).
					if ( !event.rnamespace || event.rnamespace.test( handleObj.namespace ) ) {
	
						event.handleObj = handleObj;
						event.data = handleObj.data;
	
						ret = ( ( jQuery.event.special[ handleObj.origType ] || {} ).handle ||
							handleObj.handler ).apply( matched.elem, args );
	
						if ( ret !== undefined ) {
							if ( ( event.result = ret ) === false ) {
								event.preventDefault();
								event.stopPropagation();
							}
						}
					}
				}
			}
	
			// Call the postDispatch hook for the mapped type
			if ( special.postDispatch ) {
				special.postDispatch.call( this, event );
			}
	
			return event.result;
		},
	
		handlers: function( event, handlers ) {
			var i, matches, sel, handleObj,
				handlerQueue = [],
				delegateCount = handlers.delegateCount,
				cur = event.target;
	
			// Support (at least): Chrome, IE9
			// Find delegate handlers
			// Black-hole SVG <use> instance trees (#13180)
			//
			// Support: Firefox<=42+
			// Avoid non-left-click in FF but don't block IE radio events (#3861, gh-2343)
			if ( delegateCount && cur.nodeType &&
				( event.type !== "click" || isNaN( event.button ) || event.button < 1 ) ) {
	
				/* jshint eqeqeq: false */
				for ( ; cur != this; cur = cur.parentNode || this ) {
					/* jshint eqeqeq: true */
	
					// Don't check non-elements (#13208)
					// Don't process clicks on disabled elements (#6911, #8165, #11382, #11764)
					if ( cur.nodeType === 1 && ( cur.disabled !== true || event.type !== "click" ) ) {
						matches = [];
						for ( i = 0; i < delegateCount; i++ ) {
							handleObj = handlers[ i ];
	
							// Don't conflict with Object.prototype properties (#13203)
							sel = handleObj.selector + " ";
	
							if ( matches[ sel ] === undefined ) {
								matches[ sel ] = handleObj.needsContext ?
									jQuery( sel, this ).index( cur ) > -1 :
									jQuery.find( sel, this, null, [ cur ] ).length;
							}
							if ( matches[ sel ] ) {
								matches.push( handleObj );
							}
						}
						if ( matches.length ) {
							handlerQueue.push( { elem: cur, handlers: matches } );
						}
					}
				}
			}
	
			// Add the remaining (directly-bound) handlers
			if ( delegateCount < handlers.length ) {
				handlerQueue.push( { elem: this, handlers: handlers.slice( delegateCount ) } );
			}
	
			return handlerQueue;
		},
	
		fix: function( event ) {
			if ( event[ jQuery.expando ] ) {
				return event;
			}
	
			// Create a writable copy of the event object and normalize some properties
			var i, prop, copy,
				type = event.type,
				originalEvent = event,
				fixHook = this.fixHooks[ type ];
	
			if ( !fixHook ) {
				this.fixHooks[ type ] = fixHook =
					rmouseEvent.test( type ) ? this.mouseHooks :
					rkeyEvent.test( type ) ? this.keyHooks :
					{};
			}
			copy = fixHook.props ? this.props.concat( fixHook.props ) : this.props;
	
			event = new jQuery.Event( originalEvent );
	
			i = copy.length;
			while ( i-- ) {
				prop = copy[ i ];
				event[ prop ] = originalEvent[ prop ];
			}
	
			// Support: IE<9
			// Fix target property (#1925)
			if ( !event.target ) {
				event.target = originalEvent.srcElement || document;
			}
	
			// Support: Safari 6-8+
			// Target should not be a text node (#504, #13143)
			if ( event.target.nodeType === 3 ) {
				event.target = event.target.parentNode;
			}
	
			// Support: IE<9
			// For mouse/key events, metaKey==false if it's undefined (#3368, #11328)
			event.metaKey = !!event.metaKey;
	
			return fixHook.filter ? fixHook.filter( event, originalEvent ) : event;
		},
	
		// Includes some event props shared by KeyEvent and MouseEvent
		props: ( "altKey bubbles cancelable ctrlKey currentTarget detail eventPhase " +
			"metaKey relatedTarget shiftKey target timeStamp view which" ).split( " " ),
	
		fixHooks: {},
	
		keyHooks: {
			props: "char charCode key keyCode".split( " " ),
			filter: function( event, original ) {
	
				// Add which for key events
				if ( event.which == null ) {
					event.which = original.charCode != null ? original.charCode : original.keyCode;
				}
	
				return event;
			}
		},
	
		mouseHooks: {
			props: ( "button buttons clientX clientY fromElement offsetX offsetY " +
				"pageX pageY screenX screenY toElement" ).split( " " ),
			filter: function( event, original ) {
				var body, eventDoc, doc,
					button = original.button,
					fromElement = original.fromElement;
	
				// Calculate pageX/Y if missing and clientX/Y available
				if ( event.pageX == null && original.clientX != null ) {
					eventDoc = event.target.ownerDocument || document;
					doc = eventDoc.documentElement;
					body = eventDoc.body;
	
					event.pageX = original.clientX +
						( doc && doc.scrollLeft || body && body.scrollLeft || 0 ) -
						( doc && doc.clientLeft || body && body.clientLeft || 0 );
					event.pageY = original.clientY +
						( doc && doc.scrollTop  || body && body.scrollTop  || 0 ) -
						( doc && doc.clientTop  || body && body.clientTop  || 0 );
				}
	
				// Add relatedTarget, if necessary
				if ( !event.relatedTarget && fromElement ) {
					event.relatedTarget = fromElement === event.target ?
						original.toElement :
						fromElement;
				}
	
				// Add which for click: 1 === left; 2 === middle; 3 === right
				// Note: button is not normalized, so don't use it
				if ( !event.which && button !== undefined ) {
					event.which = ( button & 1 ? 1 : ( button & 2 ? 3 : ( button & 4 ? 2 : 0 ) ) );
				}
	
				return event;
			}
		},
	
		special: {
			load: {
	
				// Prevent triggered image.load events from bubbling to window.load
				noBubble: true
			},
			focus: {
	
				// Fire native event if possible so blur/focus sequence is correct
				trigger: function() {
					if ( this !== safeActiveElement() && this.focus ) {
						try {
							this.focus();
							return false;
						} catch ( e ) {
	
							// Support: IE<9
							// If we error on focus to hidden element (#1486, #12518),
							// let .trigger() run the handlers
						}
					}
				},
				delegateType: "focusin"
			},
			blur: {
				trigger: function() {
					if ( this === safeActiveElement() && this.blur ) {
						this.blur();
						return false;
					}
				},
				delegateType: "focusout"
			},
			click: {
	
				// For checkbox, fire native event so checked state will be right
				trigger: function() {
					if ( jQuery.nodeName( this, "input" ) && this.type === "checkbox" && this.click ) {
						this.click();
						return false;
					}
				},
	
				// For cross-browser consistency, don't fire native .click() on links
				_default: function( event ) {
					return jQuery.nodeName( event.target, "a" );
				}
			},
	
			beforeunload: {
				postDispatch: function( event ) {
	
					// Support: Firefox 20+
					// Firefox doesn't alert if the returnValue field is not set.
					if ( event.result !== undefined && event.originalEvent ) {
						event.originalEvent.returnValue = event.result;
					}
				}
			}
		},
	
		// Piggyback on a donor event to simulate a different one
		simulate: function( type, elem, event ) {
			var e = jQuery.extend(
				new jQuery.Event(),
				event,
				{
					type: type,
					isSimulated: true
	
					// Previously, `originalEvent: {}` was set here, so stopPropagation call
					// would not be triggered on donor event, since in our own
					// jQuery.event.stopPropagation function we had a check for existence of
					// originalEvent.stopPropagation method, so, consequently it would be a noop.
					//
					// Guard for simulated events was moved to jQuery.event.stopPropagation function
					// since `originalEvent` should point to the original event for the
					// constancy with other events and for more focused logic
				}
			);
	
			jQuery.event.trigger( e, null, elem );
	
			if ( e.isDefaultPrevented() ) {
				event.preventDefault();
			}
		}
	};
	
	jQuery.removeEvent = document.removeEventListener ?
		function( elem, type, handle ) {
	
			// This "if" is needed for plain objects
			if ( elem.removeEventListener ) {
				elem.removeEventListener( type, handle );
			}
		} :
		function( elem, type, handle ) {
			var name = "on" + type;
	
			if ( elem.detachEvent ) {
	
				// #8545, #7054, preventing memory leaks for custom events in IE6-8
				// detachEvent needed property on element, by name of that event,
				// to properly expose it to GC
				if ( typeof elem[ name ] === "undefined" ) {
					elem[ name ] = null;
				}
	
				elem.detachEvent( name, handle );
			}
		};
	
	jQuery.Event = function( src, props ) {
	
		// Allow instantiation without the 'new' keyword
		if ( !( this instanceof jQuery.Event ) ) {
			return new jQuery.Event( src, props );
		}
	
		// Event object
		if ( src && src.type ) {
			this.originalEvent = src;
			this.type = src.type;
	
			// Events bubbling up the document may have been marked as prevented
			// by a handler lower down the tree; reflect the correct value.
			this.isDefaultPrevented = src.defaultPrevented ||
					src.defaultPrevented === undefined &&
	
					// Support: IE < 9, Android < 4.0
					src.returnValue === false ?
				returnTrue :
				returnFalse;
	
		// Event type
		} else {
			this.type = src;
		}
	
		// Put explicitly provided properties onto the event object
		if ( props ) {
			jQuery.extend( this, props );
		}
	
		// Create a timestamp if incoming event doesn't have one
		this.timeStamp = src && src.timeStamp || jQuery.now();
	
		// Mark it as fixed
		this[ jQuery.expando ] = true;
	};
	
	// jQuery.Event is based on DOM3 Events as specified by the ECMAScript Language Binding
	// http://www.w3.org/TR/2003/WD-DOM-Level-3-Events-20030331/ecma-script-binding.html
	jQuery.Event.prototype = {
		constructor: jQuery.Event,
		isDefaultPrevented: returnFalse,
		isPropagationStopped: returnFalse,
		isImmediatePropagationStopped: returnFalse,
	
		preventDefault: function() {
			var e = this.originalEvent;
	
			this.isDefaultPrevented = returnTrue;
			if ( !e ) {
				return;
			}
	
			// If preventDefault exists, run it on the original event
			if ( e.preventDefault ) {
				e.preventDefault();
	
			// Support: IE
			// Otherwise set the returnValue property of the original event to false
			} else {
				e.returnValue = false;
			}
		},
		stopPropagation: function() {
			var e = this.originalEvent;
	
			this.isPropagationStopped = returnTrue;
	
			if ( !e || this.isSimulated ) {
				return;
			}
	
			// If stopPropagation exists, run it on the original event
			if ( e.stopPropagation ) {
				e.stopPropagation();
			}
	
			// Support: IE
			// Set the cancelBubble property of the original event to true
			e.cancelBubble = true;
		},
		stopImmediatePropagation: function() {
			var e = this.originalEvent;
	
			this.isImmediatePropagationStopped = returnTrue;
	
			if ( e && e.stopImmediatePropagation ) {
				e.stopImmediatePropagation();
			}
	
			this.stopPropagation();
		}
	};
	
	// Create mouseenter/leave events using mouseover/out and event-time checks
	// so that event delegation works in jQuery.
	// Do the same for pointerenter/pointerleave and pointerover/pointerout
	//
	// Support: Safari 7 only
	// Safari sends mouseenter too often; see:
	// https://code.google.com/p/chromium/issues/detail?id=470258
	// for the description of the bug (it existed in older Chrome versions as well).
	jQuery.each( {
		mouseenter: "mouseover",
		mouseleave: "mouseout",
		pointerenter: "pointerover",
		pointerleave: "pointerout"
	}, function( orig, fix ) {
		jQuery.event.special[ orig ] = {
			delegateType: fix,
			bindType: fix,
	
			handle: function( event ) {
				var ret,
					target = this,
					related = event.relatedTarget,
					handleObj = event.handleObj;
	
				// For mouseenter/leave call the handler if related is outside the target.
				// NB: No relatedTarget if the mouse left/entered the browser window
				if ( !related || ( related !== target && !jQuery.contains( target, related ) ) ) {
					event.type = handleObj.origType;
					ret = handleObj.handler.apply( this, arguments );
					event.type = fix;
				}
				return ret;
			}
		};
	} );
	
	// IE submit delegation
	if ( !support.submit ) {
	
		jQuery.event.special.submit = {
			setup: function() {
	
				// Only need this for delegated form submit events
				if ( jQuery.nodeName( this, "form" ) ) {
					return false;
				}
	
				// Lazy-add a submit handler when a descendant form may potentially be submitted
				jQuery.event.add( this, "click._submit keypress._submit", function( e ) {
	
					// Node name check avoids a VML-related crash in IE (#9807)
					var elem = e.target,
						form = jQuery.nodeName( elem, "input" ) || jQuery.nodeName( elem, "button" ) ?
	
							// Support: IE <=8
							// We use jQuery.prop instead of elem.form
							// to allow fixing the IE8 delegated submit issue (gh-2332)
							// by 3rd party polyfills/workarounds.
							jQuery.prop( elem, "form" ) :
							undefined;
	
					if ( form && !jQuery._data( form, "submit" ) ) {
						jQuery.event.add( form, "submit._submit", function( event ) {
							event._submitBubble = true;
						} );
						jQuery._data( form, "submit", true );
					}
				} );
	
				// return undefined since we don't need an event listener
			},
	
			postDispatch: function( event ) {
	
				// If form was submitted by the user, bubble the event up the tree
				if ( event._submitBubble ) {
					delete event._submitBubble;
					if ( this.parentNode && !event.isTrigger ) {
						jQuery.event.simulate( "submit", this.parentNode, event );
					}
				}
			},
	
			teardown: function() {
	
				// Only need this for delegated form submit events
				if ( jQuery.nodeName( this, "form" ) ) {
					return false;
				}
	
				// Remove delegated handlers; cleanData eventually reaps submit handlers attached above
				jQuery.event.remove( this, "._submit" );
			}
		};
	}
	
	// IE change delegation and checkbox/radio fix
	if ( !support.change ) {
	
		jQuery.event.special.change = {
	
			setup: function() {
	
				if ( rformElems.test( this.nodeName ) ) {
	
					// IE doesn't fire change on a check/radio until blur; trigger it on click
					// after a propertychange. Eat the blur-change in special.change.handle.
					// This still fires onchange a second time for check/radio after blur.
					if ( this.type === "checkbox" || this.type === "radio" ) {
						jQuery.event.add( this, "propertychange._change", function( event ) {
							if ( event.originalEvent.propertyName === "checked" ) {
								this._justChanged = true;
							}
						} );
						jQuery.event.add( this, "click._change", function( event ) {
							if ( this._justChanged && !event.isTrigger ) {
								this._justChanged = false;
							}
	
							// Allow triggered, simulated change events (#11500)
							jQuery.event.simulate( "change", this, event );
						} );
					}
					return false;
				}
	
				// Delegated event; lazy-add a change handler on descendant inputs
				jQuery.event.add( this, "beforeactivate._change", function( e ) {
					var elem = e.target;
	
					if ( rformElems.test( elem.nodeName ) && !jQuery._data( elem, "change" ) ) {
						jQuery.event.add( elem, "change._change", function( event ) {
							if ( this.parentNode && !event.isSimulated && !event.isTrigger ) {
								jQuery.event.simulate( "change", this.parentNode, event );
							}
						} );
						jQuery._data( elem, "change", true );
					}
				} );
			},
	
			handle: function( event ) {
				var elem = event.target;
	
				// Swallow native change events from checkbox/radio, we already triggered them above
				if ( this !== elem || event.isSimulated || event.isTrigger ||
					( elem.type !== "radio" && elem.type !== "checkbox" ) ) {
	
					return event.handleObj.handler.apply( this, arguments );
				}
			},
	
			teardown: function() {
				jQuery.event.remove( this, "._change" );
	
				return !rformElems.test( this.nodeName );
			}
		};
	}
	
	// Support: Firefox
	// Firefox doesn't have focus(in | out) events
	// Related ticket - https://bugzilla.mozilla.org/show_bug.cgi?id=687787
	//
	// Support: Chrome, Safari
	// focus(in | out) events fire after focus & blur events,
	// which is spec violation - http://www.w3.org/TR/DOM-Level-3-Events/#events-focusevent-event-order
	// Related ticket - https://code.google.com/p/chromium/issues/detail?id=449857
	if ( !support.focusin ) {
		jQuery.each( { focus: "focusin", blur: "focusout" }, function( orig, fix ) {
	
			// Attach a single capturing handler on the document while someone wants focusin/focusout
			var handler = function( event ) {
				jQuery.event.simulate( fix, event.target, jQuery.event.fix( event ) );
			};
	
			jQuery.event.special[ fix ] = {
				setup: function() {
					var doc = this.ownerDocument || this,
						attaches = jQuery._data( doc, fix );
	
					if ( !attaches ) {
						doc.addEventListener( orig, handler, true );
					}
					jQuery._data( doc, fix, ( attaches || 0 ) + 1 );
				},
				teardown: function() {
					var doc = this.ownerDocument || this,
						attaches = jQuery._data( doc, fix ) - 1;
	
					if ( !attaches ) {
						doc.removeEventListener( orig, handler, true );
						jQuery._removeData( doc, fix );
					} else {
						jQuery._data( doc, fix, attaches );
					}
				}
			};
		} );
	}
	
	jQuery.fn.extend( {
	
		on: function( types, selector, data, fn ) {
			return on( this, types, selector, data, fn );
		},
		one: function( types, selector, data, fn ) {
			return on( this, types, selector, data, fn, 1 );
		},
		off: function( types, selector, fn ) {
			var handleObj, type;
			if ( types && types.preventDefault && types.handleObj ) {
	
				// ( event )  dispatched jQuery.Event
				handleObj = types.handleObj;
				jQuery( types.delegateTarget ).off(
					handleObj.namespace ?
						handleObj.origType + "." + handleObj.namespace :
						handleObj.origType,
					handleObj.selector,
					handleObj.handler
				);
				return this;
			}
			if ( typeof types === "object" ) {
	
				// ( types-object [, selector] )
				for ( type in types ) {
					this.off( type, selector, types[ type ] );
				}
				return this;
			}
			if ( selector === false || typeof selector === "function" ) {
	
				// ( types [, fn] )
				fn = selector;
				selector = undefined;
			}
			if ( fn === false ) {
				fn = returnFalse;
			}
			return this.each( function() {
				jQuery.event.remove( this, types, fn, selector );
			} );
		},
	
		trigger: function( type, data ) {
			return this.each( function() {
				jQuery.event.trigger( type, data, this );
			} );
		},
		triggerHandler: function( type, data ) {
			var elem = this[ 0 ];
			if ( elem ) {
				return jQuery.event.trigger( type, data, elem, true );
			}
		}
	} );
	
	
	var rinlinejQuery = / jQuery\d+="(?:null|\d+)"/g,
		rnoshimcache = new RegExp( "<(?:" + nodeNames + ")[\\s/>]", "i" ),
		rxhtmlTag = /<(?!area|br|col|embed|hr|img|input|link|meta|param)(([\w:-]+)[^>]*)\/>/gi,
	
		// Support: IE 10-11, Edge 10240+
		// In IE/Edge using regex groups here causes severe slowdowns.
		// See https://connect.microsoft.com/IE/feedback/details/1736512/
		rnoInnerhtml = /<script|<style|<link/i,
	
		// checked="checked" or checked
		rchecked = /checked\s*(?:[^=]|=\s*.checked.)/i,
		rscriptTypeMasked = /^true\/(.*)/,
		rcleanScript = /^\s*<!(?:\[CDATA\[|--)|(?:\]\]|--)>\s*$/g,
		safeFragment = createSafeFragment( document ),
		fragmentDiv = safeFragment.appendChild( document.createElement( "div" ) );
	
	// Support: IE<8
	// Manipulating tables requires a tbody
	function manipulationTarget( elem, content ) {
		return jQuery.nodeName( elem, "table" ) &&
			jQuery.nodeName( content.nodeType !== 11 ? content : content.firstChild, "tr" ) ?
	
			elem.getElementsByTagName( "tbody" )[ 0 ] ||
				elem.appendChild( elem.ownerDocument.createElement( "tbody" ) ) :
			elem;
	}
	
	// Replace/restore the type attribute of script elements for safe DOM manipulation
	function disableScript( elem ) {
		elem.type = ( jQuery.find.attr( elem, "type" ) !== null ) + "/" + elem.type;
		return elem;
	}
	function restoreScript( elem ) {
		var match = rscriptTypeMasked.exec( elem.type );
		if ( match ) {
			elem.type = match[ 1 ];
		} else {
			elem.removeAttribute( "type" );
		}
		return elem;
	}
	
	function cloneCopyEvent( src, dest ) {
		if ( dest.nodeType !== 1 || !jQuery.hasData( src ) ) {
			return;
		}
	
		var type, i, l,
			oldData = jQuery._data( src ),
			curData = jQuery._data( dest, oldData ),
			events = oldData.events;
	
		if ( events ) {
			delete curData.handle;
			curData.events = {};
	
			for ( type in events ) {
				for ( i = 0, l = events[ type ].length; i < l; i++ ) {
					jQuery.event.add( dest, type, events[ type ][ i ] );
				}
			}
		}
	
		// make the cloned public data object a copy from the original
		if ( curData.data ) {
			curData.data = jQuery.extend( {}, curData.data );
		}
	}
	
	function fixCloneNodeIssues( src, dest ) {
		var nodeName, e, data;
	
		// We do not need to do anything for non-Elements
		if ( dest.nodeType !== 1 ) {
			return;
		}
	
		nodeName = dest.nodeName.toLowerCase();
	
		// IE6-8 copies events bound via attachEvent when using cloneNode.
		if ( !support.noCloneEvent && dest[ jQuery.expando ] ) {
			data = jQuery._data( dest );
	
			for ( e in data.events ) {
				jQuery.removeEvent( dest, e, data.handle );
			}
	
			// Event data gets referenced instead of copied if the expando gets copied too
			dest.removeAttribute( jQuery.expando );
		}
	
		// IE blanks contents when cloning scripts, and tries to evaluate newly-set text
		if ( nodeName === "script" && dest.text !== src.text ) {
			disableScript( dest ).text = src.text;
			restoreScript( dest );
	
		// IE6-10 improperly clones children of object elements using classid.
		// IE10 throws NoModificationAllowedError if parent is null, #12132.
		} else if ( nodeName === "object" ) {
			if ( dest.parentNode ) {
				dest.outerHTML = src.outerHTML;
			}
	
			// This path appears unavoidable for IE9. When cloning an object
			// element in IE9, the outerHTML strategy above is not sufficient.
			// If the src has innerHTML and the destination does not,
			// copy the src.innerHTML into the dest.innerHTML. #10324
			if ( support.html5Clone && ( src.innerHTML && !jQuery.trim( dest.innerHTML ) ) ) {
				dest.innerHTML = src.innerHTML;
			}
	
		} else if ( nodeName === "input" && rcheckableType.test( src.type ) ) {
	
			// IE6-8 fails to persist the checked state of a cloned checkbox
			// or radio button. Worse, IE6-7 fail to give the cloned element
			// a checked appearance if the defaultChecked value isn't also set
	
			dest.defaultChecked = dest.checked = src.checked;
	
			// IE6-7 get confused and end up setting the value of a cloned
			// checkbox/radio button to an empty string instead of "on"
			if ( dest.value !== src.value ) {
				dest.value = src.value;
			}
	
		// IE6-8 fails to return the selected option to the default selected
		// state when cloning options
		} else if ( nodeName === "option" ) {
			dest.defaultSelected = dest.selected = src.defaultSelected;
	
		// IE6-8 fails to set the defaultValue to the correct value when
		// cloning other types of input fields
		} else if ( nodeName === "input" || nodeName === "textarea" ) {
			dest.defaultValue = src.defaultValue;
		}
	}
	
	function domManip( collection, args, callback, ignored ) {
	
		// Flatten any nested arrays
		args = concat.apply( [], args );
	
		var first, node, hasScripts,
			scripts, doc, fragment,
			i = 0,
			l = collection.length,
			iNoClone = l - 1,
			value = args[ 0 ],
			isFunction = jQuery.isFunction( value );
	
		// We can't cloneNode fragments that contain checked, in WebKit
		if ( isFunction ||
				( l > 1 && typeof value === "string" &&
					!support.checkClone && rchecked.test( value ) ) ) {
			return collection.each( function( index ) {
				var self = collection.eq( index );
				if ( isFunction ) {
					args[ 0 ] = value.call( this, index, self.html() );
				}
				domManip( self, args, callback, ignored );
			} );
		}
	
		if ( l ) {
			fragment = buildFragment( args, collection[ 0 ].ownerDocument, false, collection, ignored );
			first = fragment.firstChild;
	
			if ( fragment.childNodes.length === 1 ) {
				fragment = first;
			}
	
			// Require either new content or an interest in ignored elements to invoke the callback
			if ( first || ignored ) {
				scripts = jQuery.map( getAll( fragment, "script" ), disableScript );
				hasScripts = scripts.length;
	
				// Use the original fragment for the last item
				// instead of the first because it can end up
				// being emptied incorrectly in certain situations (#8070).
				for ( ; i < l; i++ ) {
					node = fragment;
	
					if ( i !== iNoClone ) {
						node = jQuery.clone( node, true, true );
	
						// Keep references to cloned scripts for later restoration
						if ( hasScripts ) {
	
							// Support: Android<4.1, PhantomJS<2
							// push.apply(_, arraylike) throws on ancient WebKit
							jQuery.merge( scripts, getAll( node, "script" ) );
						}
					}
	
					callback.call( collection[ i ], node, i );
				}
	
				if ( hasScripts ) {
					doc = scripts[ scripts.length - 1 ].ownerDocument;
	
					// Reenable scripts
					jQuery.map( scripts, restoreScript );
	
					// Evaluate executable scripts on first document insertion
					for ( i = 0; i < hasScripts; i++ ) {
						node = scripts[ i ];
						if ( rscriptType.test( node.type || "" ) &&
							!jQuery._data( node, "globalEval" ) &&
							jQuery.contains( doc, node ) ) {
	
							if ( node.src ) {
	
								// Optional AJAX dependency, but won't run scripts if not present
								if ( jQuery._evalUrl ) {
									jQuery._evalUrl( node.src );
								}
							} else {
								jQuery.globalEval(
									( node.text || node.textContent || node.innerHTML || "" )
										.replace( rcleanScript, "" )
								);
							}
						}
					}
				}
	
				// Fix #11809: Avoid leaking memory
				fragment = first = null;
			}
		}
	
		return collection;
	}
	
	function remove( elem, selector, keepData ) {
		var node,
			elems = selector ? jQuery.filter( selector, elem ) : elem,
			i = 0;
	
		for ( ; ( node = elems[ i ] ) != null; i++ ) {
	
			if ( !keepData && node.nodeType === 1 ) {
				jQuery.cleanData( getAll( node ) );
			}
	
			if ( node.parentNode ) {
				if ( keepData && jQuery.contains( node.ownerDocument, node ) ) {
					setGlobalEval( getAll( node, "script" ) );
				}
				node.parentNode.removeChild( node );
			}
		}
	
		return elem;
	}
	
	jQuery.extend( {
		htmlPrefilter: function( html ) {
			return html.replace( rxhtmlTag, "<$1></$2>" );
		},
	
		clone: function( elem, dataAndEvents, deepDataAndEvents ) {
			var destElements, node, clone, i, srcElements,
				inPage = jQuery.contains( elem.ownerDocument, elem );
	
			if ( support.html5Clone || jQuery.isXMLDoc( elem ) ||
				!rnoshimcache.test( "<" + elem.nodeName + ">" ) ) {
	
				clone = elem.cloneNode( true );
	
			// IE<=8 does not properly clone detached, unknown element nodes
			} else {
				fragmentDiv.innerHTML = elem.outerHTML;
				fragmentDiv.removeChild( clone = fragmentDiv.firstChild );
			}
	
			if ( ( !support.noCloneEvent || !support.noCloneChecked ) &&
					( elem.nodeType === 1 || elem.nodeType === 11 ) && !jQuery.isXMLDoc( elem ) ) {
	
				// We eschew Sizzle here for performance reasons: http://jsperf.com/getall-vs-sizzle/2
				destElements = getAll( clone );
				srcElements = getAll( elem );
	
				// Fix all IE cloning issues
				for ( i = 0; ( node = srcElements[ i ] ) != null; ++i ) {
	
					// Ensure that the destination node is not null; Fixes #9587
					if ( destElements[ i ] ) {
						fixCloneNodeIssues( node, destElements[ i ] );
					}
				}
			}
	
			// Copy the events from the original to the clone
			if ( dataAndEvents ) {
				if ( deepDataAndEvents ) {
					srcElements = srcElements || getAll( elem );
					destElements = destElements || getAll( clone );
	
					for ( i = 0; ( node = srcElements[ i ] ) != null; i++ ) {
						cloneCopyEvent( node, destElements[ i ] );
					}
				} else {
					cloneCopyEvent( elem, clone );
				}
			}
	
			// Preserve script evaluation history
			destElements = getAll( clone, "script" );
			if ( destElements.length > 0 ) {
				setGlobalEval( destElements, !inPage && getAll( elem, "script" ) );
			}
	
			destElements = srcElements = node = null;
	
			// Return the cloned set
			return clone;
		},
	
		cleanData: function( elems, /* internal */ forceAcceptData ) {
			var elem, type, id, data,
				i = 0,
				internalKey = jQuery.expando,
				cache = jQuery.cache,
				attributes = support.attributes,
				special = jQuery.event.special;
	
			for ( ; ( elem = elems[ i ] ) != null; i++ ) {
				if ( forceAcceptData || acceptData( elem ) ) {
	
					id = elem[ internalKey ];
					data = id && cache[ id ];
	
					if ( data ) {
						if ( data.events ) {
							for ( type in data.events ) {
								if ( special[ type ] ) {
									jQuery.event.remove( elem, type );
	
								// This is a shortcut to avoid jQuery.event.remove's overhead
								} else {
									jQuery.removeEvent( elem, type, data.handle );
								}
							}
						}
	
						// Remove cache only if it was not already removed by jQuery.event.remove
						if ( cache[ id ] ) {
	
							delete cache[ id ];
	
							// Support: IE<9
							// IE does not allow us to delete expando properties from nodes
							// IE creates expando attributes along with the property
							// IE does not have a removeAttribute function on Document nodes
							if ( !attributes && typeof elem.removeAttribute !== "undefined" ) {
								elem.removeAttribute( internalKey );
	
							// Webkit & Blink performance suffers when deleting properties
							// from DOM nodes, so set to undefined instead
							// https://code.google.com/p/chromium/issues/detail?id=378607
							} else {
								elem[ internalKey ] = undefined;
							}
	
							deletedIds.push( id );
						}
					}
				}
			}
		}
	} );
	
	jQuery.fn.extend( {
	
		// Keep domManip exposed until 3.0 (gh-2225)
		domManip: domManip,
	
		detach: function( selector ) {
			return remove( this, selector, true );
		},
	
		remove: function( selector ) {
			return remove( this, selector );
		},
	
		text: function( value ) {
			return access( this, function( value ) {
				return value === undefined ?
					jQuery.text( this ) :
					this.empty().append(
						( this[ 0 ] && this[ 0 ].ownerDocument || document ).createTextNode( value )
					);
			}, null, value, arguments.length );
		},
	
		append: function() {
			return domManip( this, arguments, function( elem ) {
				if ( this.nodeType === 1 || this.nodeType === 11 || this.nodeType === 9 ) {
					var target = manipulationTarget( this, elem );
					target.appendChild( elem );
				}
			} );
		},
	
		prepend: function() {
			return domManip( this, arguments, function( elem ) {
				if ( this.nodeType === 1 || this.nodeType === 11 || this.nodeType === 9 ) {
					var target = manipulationTarget( this, elem );
					target.insertBefore( elem, target.firstChild );
				}
			} );
		},
	
		before: function() {
			return domManip( this, arguments, function( elem ) {
				if ( this.parentNode ) {
					this.parentNode.insertBefore( elem, this );
				}
			} );
		},
	
		after: function() {
			return domManip( this, arguments, function( elem ) {
				if ( this.parentNode ) {
					this.parentNode.insertBefore( elem, this.nextSibling );
				}
			} );
		},
	
		empty: function() {
			var elem,
				i = 0;
	
			for ( ; ( elem = this[ i ] ) != null; i++ ) {
	
				// Remove element nodes and prevent memory leaks
				if ( elem.nodeType === 1 ) {
					jQuery.cleanData( getAll( elem, false ) );
				}
	
				// Remove any remaining nodes
				while ( elem.firstChild ) {
					elem.removeChild( elem.firstChild );
				}
	
				// If this is a select, ensure that it displays empty (#12336)
				// Support: IE<9
				if ( elem.options && jQuery.nodeName( elem, "select" ) ) {
					elem.options.length = 0;
				}
			}
	
			return this;
		},
	
		clone: function( dataAndEvents, deepDataAndEvents ) {
			dataAndEvents = dataAndEvents == null ? false : dataAndEvents;
			deepDataAndEvents = deepDataAndEvents == null ? dataAndEvents : deepDataAndEvents;
	
			return this.map( function() {
				return jQuery.clone( this, dataAndEvents, deepDataAndEvents );
			} );
		},
	
		html: function( value ) {
			return access( this, function( value ) {
				var elem = this[ 0 ] || {},
					i = 0,
					l = this.length;
	
				if ( value === undefined ) {
					return elem.nodeType === 1 ?
						elem.innerHTML.replace( rinlinejQuery, "" ) :
						undefined;
				}
	
				// See if we can take a shortcut and just use innerHTML
				if ( typeof value === "string" && !rnoInnerhtml.test( value ) &&
					( support.htmlSerialize || !rnoshimcache.test( value )  ) &&
					( support.leadingWhitespace || !rleadingWhitespace.test( value ) ) &&
					!wrapMap[ ( rtagName.exec( value ) || [ "", "" ] )[ 1 ].toLowerCase() ] ) {
	
					value = jQuery.htmlPrefilter( value );
	
					try {
						for ( ; i < l; i++ ) {
	
							// Remove element nodes and prevent memory leaks
							elem = this[ i ] || {};
							if ( elem.nodeType === 1 ) {
								jQuery.cleanData( getAll( elem, false ) );
								elem.innerHTML = value;
							}
						}
	
						elem = 0;
	
					// If using innerHTML throws an exception, use the fallback method
					} catch ( e ) {}
				}
	
				if ( elem ) {
					this.empty().append( value );
				}
			}, null, value, arguments.length );
		},
	
		replaceWith: function() {
			var ignored = [];
	
			// Make the changes, replacing each non-ignored context element with the new content
			return domManip( this, arguments, function( elem ) {
				var parent = this.parentNode;
	
				if ( jQuery.inArray( this, ignored ) < 0 ) {
					jQuery.cleanData( getAll( this ) );
					if ( parent ) {
						parent.replaceChild( elem, this );
					}
				}
	
			// Force callback invocation
			}, ignored );
		}
	} );
	
	jQuery.each( {
		appendTo: "append",
		prependTo: "prepend",
		insertBefore: "before",
		insertAfter: "after",
		replaceAll: "replaceWith"
	}, function( name, original ) {
		jQuery.fn[ name ] = function( selector ) {
			var elems,
				i = 0,
				ret = [],
				insert = jQuery( selector ),
				last = insert.length - 1;
	
			for ( ; i <= last; i++ ) {
				elems = i === last ? this : this.clone( true );
				jQuery( insert[ i ] )[ original ]( elems );
	
				// Modern browsers can apply jQuery collections as arrays, but oldIE needs a .get()
				push.apply( ret, elems.get() );
			}
	
			return this.pushStack( ret );
		};
	} );
	
	
	var iframe,
		elemdisplay = {
	
			// Support: Firefox
			// We have to pre-define these values for FF (#10227)
			HTML: "block",
			BODY: "block"
		};
	
	/**
	 * Retrieve the actual display of a element
	 * @param {String} name nodeName of the element
	 * @param {Object} doc Document object
	 */
	
	// Called only from within defaultDisplay
	function actualDisplay( name, doc ) {
		var elem = jQuery( doc.createElement( name ) ).appendTo( doc.body ),
	
			display = jQuery.css( elem[ 0 ], "display" );
	
		// We don't have any data stored on the element,
		// so use "detach" method as fast way to get rid of the element
		elem.detach();
	
		return display;
	}
	
	/**
	 * Try to determine the default display value of an element
	 * @param {String} nodeName
	 */
	function defaultDisplay( nodeName ) {
		var doc = document,
			display = elemdisplay[ nodeName ];
	
		if ( !display ) {
			display = actualDisplay( nodeName, doc );
	
			// If the simple way fails, read from inside an iframe
			if ( display === "none" || !display ) {
	
				// Use the already-created iframe if possible
				iframe = ( iframe || jQuery( "<iframe frameborder='0' width='0' height='0'/>" ) )
					.appendTo( doc.documentElement );
	
				// Always write a new HTML skeleton so Webkit and Firefox don't choke on reuse
				doc = ( iframe[ 0 ].contentWindow || iframe[ 0 ].contentDocument ).document;
	
				// Support: IE
				doc.write();
				doc.close();
	
				display = actualDisplay( nodeName, doc );
				iframe.detach();
			}
	
			// Store the correct default display
			elemdisplay[ nodeName ] = display;
		}
	
		return display;
	}
	var rmargin = ( /^margin/ );
	
	var rnumnonpx = new RegExp( "^(" + pnum + ")(?!px)[a-z%]+$", "i" );
	
	var swap = function( elem, options, callback, args ) {
		var ret, name,
			old = {};
	
		// Remember the old values, and insert the new ones
		for ( name in options ) {
			old[ name ] = elem.style[ name ];
			elem.style[ name ] = options[ name ];
		}
	
		ret = callback.apply( elem, args || [] );
	
		// Revert the old values
		for ( name in options ) {
			elem.style[ name ] = old[ name ];
		}
	
		return ret;
	};
	
	
	var documentElement = document.documentElement;
	
	
	
	( function() {
		var pixelPositionVal, pixelMarginRightVal, boxSizingReliableVal,
			reliableHiddenOffsetsVal, reliableMarginRightVal, reliableMarginLeftVal,
			container = document.createElement( "div" ),
			div = document.createElement( "div" );
	
		// Finish early in limited (non-browser) environments
		if ( !div.style ) {
			return;
		}
	
		div.style.cssText = "float:left;opacity:.5";
	
		// Support: IE<9
		// Make sure that element opacity exists (as opposed to filter)
		support.opacity = div.style.opacity === "0.5";
	
		// Verify style float existence
		// (IE uses styleFloat instead of cssFloat)
		support.cssFloat = !!div.style.cssFloat;
	
		div.style.backgroundClip = "content-box";
		div.cloneNode( true ).style.backgroundClip = "";
		support.clearCloneStyle = div.style.backgroundClip === "content-box";
	
		container = document.createElement( "div" );
		container.style.cssText = "border:0;width:8px;height:0;top:0;left:-9999px;" +
			"padding:0;margin-top:1px;position:absolute";
		div.innerHTML = "";
		container.appendChild( div );
	
		// Support: Firefox<29, Android 2.3
		// Vendor-prefix box-sizing
		support.boxSizing = div.style.boxSizing === "" || div.style.MozBoxSizing === "" ||
			div.style.WebkitBoxSizing === "";
	
		jQuery.extend( support, {
			reliableHiddenOffsets: function() {
				if ( pixelPositionVal == null ) {
					computeStyleTests();
				}
				return reliableHiddenOffsetsVal;
			},
	
			boxSizingReliable: function() {
	
				// We're checking for pixelPositionVal here instead of boxSizingReliableVal
				// since that compresses better and they're computed together anyway.
				if ( pixelPositionVal == null ) {
					computeStyleTests();
				}
				return boxSizingReliableVal;
			},
	
			pixelMarginRight: function() {
	
				// Support: Android 4.0-4.3
				if ( pixelPositionVal == null ) {
					computeStyleTests();
				}
				return pixelMarginRightVal;
			},
	
			pixelPosition: function() {
				if ( pixelPositionVal == null ) {
					computeStyleTests();
				}
				return pixelPositionVal;
			},
	
			reliableMarginRight: function() {
	
				// Support: Android 2.3
				if ( pixelPositionVal == null ) {
					computeStyleTests();
				}
				return reliableMarginRightVal;
			},
	
			reliableMarginLeft: function() {
	
				// Support: IE <=8 only, Android 4.0 - 4.3 only, Firefox <=3 - 37
				if ( pixelPositionVal == null ) {
					computeStyleTests();
				}
				return reliableMarginLeftVal;
			}
		} );
	
		function computeStyleTests() {
			var contents, divStyle,
				documentElement = document.documentElement;
	
			// Setup
			documentElement.appendChild( container );
	
			div.style.cssText =
	
				// Support: Android 2.3
				// Vendor-prefix box-sizing
				"-webkit-box-sizing:border-box;box-sizing:border-box;" +
				"position:relative;display:block;" +
				"margin:auto;border:1px;padding:1px;" +
				"top:1%;width:50%";
	
			// Support: IE<9
			// Assume reasonable values in the absence of getComputedStyle
			pixelPositionVal = boxSizingReliableVal = reliableMarginLeftVal = false;
			pixelMarginRightVal = reliableMarginRightVal = true;
	
			// Check for getComputedStyle so that this code is not run in IE<9.
			if ( window.getComputedStyle ) {
				divStyle = window.getComputedStyle( div );
				pixelPositionVal = ( divStyle || {} ).top !== "1%";
				reliableMarginLeftVal = ( divStyle || {} ).marginLeft === "2px";
				boxSizingReliableVal = ( divStyle || { width: "4px" } ).width === "4px";
	
				// Support: Android 4.0 - 4.3 only
				// Some styles come back with percentage values, even though they shouldn't
				div.style.marginRight = "50%";
				pixelMarginRightVal = ( divStyle || { marginRight: "4px" } ).marginRight === "4px";
	
				// Support: Android 2.3 only
				// Div with explicit width and no margin-right incorrectly
				// gets computed margin-right based on width of container (#3333)
				// WebKit Bug 13343 - getComputedStyle returns wrong value for margin-right
				contents = div.appendChild( document.createElement( "div" ) );
	
				// Reset CSS: box-sizing; display; margin; border; padding
				contents.style.cssText = div.style.cssText =
	
					// Support: Android 2.3
					// Vendor-prefix box-sizing
					"-webkit-box-sizing:content-box;-moz-box-sizing:content-box;" +
					"box-sizing:content-box;display:block;margin:0;border:0;padding:0";
				contents.style.marginRight = contents.style.width = "0";
				div.style.width = "1px";
	
				reliableMarginRightVal =
					!parseFloat( ( window.getComputedStyle( contents ) || {} ).marginRight );
	
				div.removeChild( contents );
			}
	
			// Support: IE6-8
			// First check that getClientRects works as expected
			// Check if table cells still have offsetWidth/Height when they are set
			// to display:none and there are still other visible table cells in a
			// table row; if so, offsetWidth/Height are not reliable for use when
			// determining if an element has been hidden directly using
			// display:none (it is still safe to use offsets if a parent element is
			// hidden; don safety goggles and see bug #4512 for more information).
			div.style.display = "none";
			reliableHiddenOffsetsVal = div.getClientRects().length === 0;
			if ( reliableHiddenOffsetsVal ) {
				div.style.display = "";
				div.innerHTML = "<table><tr><td></td><td>t</td></tr></table>";
				div.childNodes[ 0 ].style.borderCollapse = "separate";
				contents = div.getElementsByTagName( "td" );
				contents[ 0 ].style.cssText = "margin:0;border:0;padding:0;display:none";
				reliableHiddenOffsetsVal = contents[ 0 ].offsetHeight === 0;
				if ( reliableHiddenOffsetsVal ) {
					contents[ 0 ].style.display = "";
					contents[ 1 ].style.display = "none";
					reliableHiddenOffsetsVal = contents[ 0 ].offsetHeight === 0;
				}
			}
	
			// Teardown
			documentElement.removeChild( container );
		}
	
	} )();
	
	
	var getStyles, curCSS,
		rposition = /^(top|right|bottom|left)$/;
	
	if ( window.getComputedStyle ) {
		getStyles = function( elem ) {
	
			// Support: IE<=11+, Firefox<=30+ (#15098, #14150)
			// IE throws on elements created in popups
			// FF meanwhile throws on frame elements through "defaultView.getComputedStyle"
			var view = elem.ownerDocument.defaultView;
	
			if ( !view || !view.opener ) {
				view = window;
			}
	
			return view.getComputedStyle( elem );
		};
	
		curCSS = function( elem, name, computed ) {
			var width, minWidth, maxWidth, ret,
				style = elem.style;
	
			computed = computed || getStyles( elem );
	
			// getPropertyValue is only needed for .css('filter') in IE9, see #12537
			ret = computed ? computed.getPropertyValue( name ) || computed[ name ] : undefined;
	
			// Support: Opera 12.1x only
			// Fall back to style even without computed
			// computed is undefined for elems on document fragments
			if ( ( ret === "" || ret === undefined ) && !jQuery.contains( elem.ownerDocument, elem ) ) {
				ret = jQuery.style( elem, name );
			}
	
			if ( computed ) {
	
				// A tribute to the "awesome hack by Dean Edwards"
				// Chrome < 17 and Safari 5.0 uses "computed value"
				// instead of "used value" for margin-right
				// Safari 5.1.7 (at least) returns percentage for a larger set of values,
				// but width seems to be reliably pixels
				// this is against the CSSOM draft spec:
				// http://dev.w3.org/csswg/cssom/#resolved-values
				if ( !support.pixelMarginRight() && rnumnonpx.test( ret ) && rmargin.test( name ) ) {
	
					// Remember the original values
					width = style.width;
					minWidth = style.minWidth;
					maxWidth = style.maxWidth;
	
					// Put in the new values to get a computed value out
					style.minWidth = style.maxWidth = style.width = ret;
					ret = computed.width;
	
					// Revert the changed values
					style.width = width;
					style.minWidth = minWidth;
					style.maxWidth = maxWidth;
				}
			}
	
			// Support: IE
			// IE returns zIndex value as an integer.
			return ret === undefined ?
				ret :
				ret + "";
		};
	} else if ( documentElement.currentStyle ) {
		getStyles = function( elem ) {
			return elem.currentStyle;
		};
	
		curCSS = function( elem, name, computed ) {
			var left, rs, rsLeft, ret,
				style = elem.style;
	
			computed = computed || getStyles( elem );
			ret = computed ? computed[ name ] : undefined;
	
			// Avoid setting ret to empty string here
			// so we don't default to auto
			if ( ret == null && style && style[ name ] ) {
				ret = style[ name ];
			}
	
			// From the awesome hack by Dean Edwards
			// http://erik.eae.net/archives/2007/07/27/18.54.15/#comment-102291
	
			// If we're not dealing with a regular pixel number
			// but a number that has a weird ending, we need to convert it to pixels
			// but not position css attributes, as those are
			// proportional to the parent element instead
			// and we can't measure the parent instead because it
			// might trigger a "stacking dolls" problem
			if ( rnumnonpx.test( ret ) && !rposition.test( name ) ) {
	
				// Remember the original values
				left = style.left;
				rs = elem.runtimeStyle;
				rsLeft = rs && rs.left;
	
				// Put in the new values to get a computed value out
				if ( rsLeft ) {
					rs.left = elem.currentStyle.left;
				}
				style.left = name === "fontSize" ? "1em" : ret;
				ret = style.pixelLeft + "px";
	
				// Revert the changed values
				style.left = left;
				if ( rsLeft ) {
					rs.left = rsLeft;
				}
			}
	
			// Support: IE
			// IE returns zIndex value as an integer.
			return ret === undefined ?
				ret :
				ret + "" || "auto";
		};
	}
	
	
	
	
	function addGetHookIf( conditionFn, hookFn ) {
	
		// Define the hook, we'll check on the first run if it's really needed.
		return {
			get: function() {
				if ( conditionFn() ) {
	
					// Hook not needed (or it's not possible to use it due
					// to missing dependency), remove it.
					delete this.get;
					return;
				}
	
				// Hook needed; redefine it so that the support test is not executed again.
				return ( this.get = hookFn ).apply( this, arguments );
			}
		};
	}
	
	
	var
	
			ralpha = /alpha\([^)]*\)/i,
		ropacity = /opacity\s*=\s*([^)]*)/i,
	
		// swappable if display is none or starts with table except
		// "table", "table-cell", or "table-caption"
		// see here for display values:
		// https://developer.mozilla.org/en-US/docs/CSS/display
		rdisplayswap = /^(none|table(?!-c[ea]).+)/,
		rnumsplit = new RegExp( "^(" + pnum + ")(.*)$", "i" ),
	
		cssShow = { position: "absolute", visibility: "hidden", display: "block" },
		cssNormalTransform = {
			letterSpacing: "0",
			fontWeight: "400"
		},
	
		cssPrefixes = [ "Webkit", "O", "Moz", "ms" ],
		emptyStyle = document.createElement( "div" ).style;
	
	
	// return a css property mapped to a potentially vendor prefixed property
	function vendorPropName( name ) {
	
		// shortcut for names that are not vendor prefixed
		if ( name in emptyStyle ) {
			return name;
		}
	
		// check for vendor prefixed names
		var capName = name.charAt( 0 ).toUpperCase() + name.slice( 1 ),
			i = cssPrefixes.length;
	
		while ( i-- ) {
			name = cssPrefixes[ i ] + capName;
			if ( name in emptyStyle ) {
				return name;
			}
		}
	}
	
	function showHide( elements, show ) {
		var display, elem, hidden,
			values = [],
			index = 0,
			length = elements.length;
	
		for ( ; index < length; index++ ) {
			elem = elements[ index ];
			if ( !elem.style ) {
				continue;
			}
	
			values[ index ] = jQuery._data( elem, "olddisplay" );
			display = elem.style.display;
			if ( show ) {
	
				// Reset the inline display of this element to learn if it is
				// being hidden by cascaded rules or not
				if ( !values[ index ] && display === "none" ) {
					elem.style.display = "";
				}
	
				// Set elements which have been overridden with display: none
				// in a stylesheet to whatever the default browser style is
				// for such an element
				if ( elem.style.display === "" && isHidden( elem ) ) {
					values[ index ] =
						jQuery._data( elem, "olddisplay", defaultDisplay( elem.nodeName ) );
				}
			} else {
				hidden = isHidden( elem );
	
				if ( display && display !== "none" || !hidden ) {
					jQuery._data(
						elem,
						"olddisplay",
						hidden ? display : jQuery.css( elem, "display" )
					);
				}
			}
		}
	
		// Set the display of most of the elements in a second loop
		// to avoid the constant reflow
		for ( index = 0; index < length; index++ ) {
			elem = elements[ index ];
			if ( !elem.style ) {
				continue;
			}
			if ( !show || elem.style.display === "none" || elem.style.display === "" ) {
				elem.style.display = show ? values[ index ] || "" : "none";
			}
		}
	
		return elements;
	}
	
	function setPositiveNumber( elem, value, subtract ) {
		var matches = rnumsplit.exec( value );
		return matches ?
	
			// Guard against undefined "subtract", e.g., when used as in cssHooks
			Math.max( 0, matches[ 1 ] - ( subtract || 0 ) ) + ( matches[ 2 ] || "px" ) :
			value;
	}
	
	function augmentWidthOrHeight( elem, name, extra, isBorderBox, styles ) {
		var i = extra === ( isBorderBox ? "border" : "content" ) ?
	
			// If we already have the right measurement, avoid augmentation
			4 :
	
			// Otherwise initialize for horizontal or vertical properties
			name === "width" ? 1 : 0,
	
			val = 0;
	
		for ( ; i < 4; i += 2 ) {
	
			// both box models exclude margin, so add it if we want it
			if ( extra === "margin" ) {
				val += jQuery.css( elem, extra + cssExpand[ i ], true, styles );
			}
	
			if ( isBorderBox ) {
	
				// border-box includes padding, so remove it if we want content
				if ( extra === "content" ) {
					val -= jQuery.css( elem, "padding" + cssExpand[ i ], true, styles );
				}
	
				// at this point, extra isn't border nor margin, so remove border
				if ( extra !== "margin" ) {
					val -= jQuery.css( elem, "border" + cssExpand[ i ] + "Width", true, styles );
				}
			} else {
	
				// at this point, extra isn't content, so add padding
				val += jQuery.css( elem, "padding" + cssExpand[ i ], true, styles );
	
				// at this point, extra isn't content nor padding, so add border
				if ( extra !== "padding" ) {
					val += jQuery.css( elem, "border" + cssExpand[ i ] + "Width", true, styles );
				}
			}
		}
	
		return val;
	}
	
	function getWidthOrHeight( elem, name, extra ) {
	
		// Start with offset property, which is equivalent to the border-box value
		var valueIsBorderBox = true,
			val = name === "width" ? elem.offsetWidth : elem.offsetHeight,
			styles = getStyles( elem ),
			isBorderBox = support.boxSizing &&
				jQuery.css( elem, "boxSizing", false, styles ) === "border-box";
	
		// some non-html elements return undefined for offsetWidth, so check for null/undefined
		// svg - https://bugzilla.mozilla.org/show_bug.cgi?id=649285
		// MathML - https://bugzilla.mozilla.org/show_bug.cgi?id=491668
		if ( val <= 0 || val == null ) {
	
			// Fall back to computed then uncomputed css if necessary
			val = curCSS( elem, name, styles );
			if ( val < 0 || val == null ) {
				val = elem.style[ name ];
			}
	
			// Computed unit is not pixels. Stop here and return.
			if ( rnumnonpx.test( val ) ) {
				return val;
			}
	
			// we need the check for style in case a browser which returns unreliable values
			// for getComputedStyle silently falls back to the reliable elem.style
			valueIsBorderBox = isBorderBox &&
				( support.boxSizingReliable() || val === elem.style[ name ] );
	
			// Normalize "", auto, and prepare for extra
			val = parseFloat( val ) || 0;
		}
	
		// use the active box-sizing model to add/subtract irrelevant styles
		return ( val +
			augmentWidthOrHeight(
				elem,
				name,
				extra || ( isBorderBox ? "border" : "content" ),
				valueIsBorderBox,
				styles
			)
		) + "px";
	}
	
	jQuery.extend( {
	
		// Add in style property hooks for overriding the default
		// behavior of getting and setting a style property
		cssHooks: {
			opacity: {
				get: function( elem, computed ) {
					if ( computed ) {
	
						// We should always get a number back from opacity
						var ret = curCSS( elem, "opacity" );
						return ret === "" ? "1" : ret;
					}
				}
			}
		},
	
		// Don't automatically add "px" to these possibly-unitless properties
		cssNumber: {
			"animationIterationCount": true,
			"columnCount": true,
			"fillOpacity": true,
			"flexGrow": true,
			"flexShrink": true,
			"fontWeight": true,
			"lineHeight": true,
			"opacity": true,
			"order": true,
			"orphans": true,
			"widows": true,
			"zIndex": true,
			"zoom": true
		},
	
		// Add in properties whose names you wish to fix before
		// setting or getting the value
		cssProps: {
	
			// normalize float css property
			"float": support.cssFloat ? "cssFloat" : "styleFloat"
		},
	
		// Get and set the style property on a DOM Node
		style: function( elem, name, value, extra ) {
	
			// Don't set styles on text and comment nodes
			if ( !elem || elem.nodeType === 3 || elem.nodeType === 8 || !elem.style ) {
				return;
			}
	
			// Make sure that we're working with the right name
			var ret, type, hooks,
				origName = jQuery.camelCase( name ),
				style = elem.style;
	
			name = jQuery.cssProps[ origName ] ||
				( jQuery.cssProps[ origName ] = vendorPropName( origName ) || origName );
	
			// gets hook for the prefixed version
			// followed by the unprefixed version
			hooks = jQuery.cssHooks[ name ] || jQuery.cssHooks[ origName ];
	
			// Check if we're setting a value
			if ( value !== undefined ) {
				type = typeof value;
	
				// Convert "+=" or "-=" to relative numbers (#7345)
				if ( type === "string" && ( ret = rcssNum.exec( value ) ) && ret[ 1 ] ) {
					value = adjustCSS( elem, name, ret );
	
					// Fixes bug #9237
					type = "number";
				}
	
				// Make sure that null and NaN values aren't set. See: #7116
				if ( value == null || value !== value ) {
					return;
				}
	
				// If a number was passed in, add the unit (except for certain CSS properties)
				if ( type === "number" ) {
					value += ret && ret[ 3 ] || ( jQuery.cssNumber[ origName ] ? "" : "px" );
				}
	
				// Fixes #8908, it can be done more correctly by specifing setters in cssHooks,
				// but it would mean to define eight
				// (for every problematic property) identical functions
				if ( !support.clearCloneStyle && value === "" && name.indexOf( "background" ) === 0 ) {
					style[ name ] = "inherit";
				}
	
				// If a hook was provided, use that value, otherwise just set the specified value
				if ( !hooks || !( "set" in hooks ) ||
					( value = hooks.set( elem, value, extra ) ) !== undefined ) {
	
					// Support: IE
					// Swallow errors from 'invalid' CSS values (#5509)
					try {
						style[ name ] = value;
					} catch ( e ) {}
				}
	
			} else {
	
				// If a hook was provided get the non-computed value from there
				if ( hooks && "get" in hooks &&
					( ret = hooks.get( elem, false, extra ) ) !== undefined ) {
	
					return ret;
				}
	
				// Otherwise just get the value from the style object
				return style[ name ];
			}
		},
	
		css: function( elem, name, extra, styles ) {
			var num, val, hooks,
				origName = jQuery.camelCase( name );
	
			// Make sure that we're working with the right name
			name = jQuery.cssProps[ origName ] ||
				( jQuery.cssProps[ origName ] = vendorPropName( origName ) || origName );
	
			// gets hook for the prefixed version
			// followed by the unprefixed version
			hooks = jQuery.cssHooks[ name ] || jQuery.cssHooks[ origName ];
	
			// If a hook was provided get the computed value from there
			if ( hooks && "get" in hooks ) {
				val = hooks.get( elem, true, extra );
			}
	
			// Otherwise, if a way to get the computed value exists, use that
			if ( val === undefined ) {
				val = curCSS( elem, name, styles );
			}
	
			//convert "normal" to computed value
			if ( val === "normal" && name in cssNormalTransform ) {
				val = cssNormalTransform[ name ];
			}
	
			// Return, converting to number if forced or a qualifier was provided and val looks numeric
			if ( extra === "" || extra ) {
				num = parseFloat( val );
				return extra === true || isFinite( num ) ? num || 0 : val;
			}
			return val;
		}
	} );
	
	jQuery.each( [ "height", "width" ], function( i, name ) {
		jQuery.cssHooks[ name ] = {
			get: function( elem, computed, extra ) {
				if ( computed ) {
	
					// certain elements can have dimension info if we invisibly show them
					// however, it must have a current display style that would benefit from this
					return rdisplayswap.test( jQuery.css( elem, "display" ) ) &&
						elem.offsetWidth === 0 ?
							swap( elem, cssShow, function() {
								return getWidthOrHeight( elem, name, extra );
							} ) :
							getWidthOrHeight( elem, name, extra );
				}
			},
	
			set: function( elem, value, extra ) {
				var styles = extra && getStyles( elem );
				return setPositiveNumber( elem, value, extra ?
					augmentWidthOrHeight(
						elem,
						name,
						extra,
						support.boxSizing &&
							jQuery.css( elem, "boxSizing", false, styles ) === "border-box",
						styles
					) : 0
				);
			}
		};
	} );
	
	if ( !support.opacity ) {
		jQuery.cssHooks.opacity = {
			get: function( elem, computed ) {
	
				// IE uses filters for opacity
				return ropacity.test( ( computed && elem.currentStyle ?
					elem.currentStyle.filter :
					elem.style.filter ) || "" ) ?
						( 0.01 * parseFloat( RegExp.$1 ) ) + "" :
						computed ? "1" : "";
			},
	
			set: function( elem, value ) {
				var style = elem.style,
					currentStyle = elem.currentStyle,
					opacity = jQuery.isNumeric( value ) ? "alpha(opacity=" + value * 100 + ")" : "",
					filter = currentStyle && currentStyle.filter || style.filter || "";
	
				// IE has trouble with opacity if it does not have layout
				// Force it by setting the zoom level
				style.zoom = 1;
	
				// if setting opacity to 1, and no other filters exist -
				// attempt to remove filter attribute #6652
				// if value === "", then remove inline opacity #12685
				if ( ( value >= 1 || value === "" ) &&
						jQuery.trim( filter.replace( ralpha, "" ) ) === "" &&
						style.removeAttribute ) {
	
					// Setting style.filter to null, "" & " " still leave "filter:" in the cssText
					// if "filter:" is present at all, clearType is disabled, we want to avoid this
					// style.removeAttribute is IE Only, but so apparently is this code path...
					style.removeAttribute( "filter" );
	
					// if there is no filter style applied in a css rule
					// or unset inline opacity, we are done
					if ( value === "" || currentStyle && !currentStyle.filter ) {
						return;
					}
				}
	
				// otherwise, set new filter values
				style.filter = ralpha.test( filter ) ?
					filter.replace( ralpha, opacity ) :
					filter + " " + opacity;
			}
		};
	}
	
	jQuery.cssHooks.marginRight = addGetHookIf( support.reliableMarginRight,
		function( elem, computed ) {
			if ( computed ) {
				return swap( elem, { "display": "inline-block" },
					curCSS, [ elem, "marginRight" ] );
			}
		}
	);
	
	jQuery.cssHooks.marginLeft = addGetHookIf( support.reliableMarginLeft,
		function( elem, computed ) {
			if ( computed ) {
				return (
					parseFloat( curCSS( elem, "marginLeft" ) ) ||
	
					// Support: IE<=11+
					// Running getBoundingClientRect on a disconnected node in IE throws an error
					// Support: IE8 only
					// getClientRects() errors on disconnected elems
					( jQuery.contains( elem.ownerDocument, elem ) ?
						elem.getBoundingClientRect().left -
							swap( elem, { marginLeft: 0 }, function() {
								return elem.getBoundingClientRect().left;
							} ) :
						0
					)
				) + "px";
			}
		}
	);
	
	// These hooks are used by animate to expand properties
	jQuery.each( {
		margin: "",
		padding: "",
		border: "Width"
	}, function( prefix, suffix ) {
		jQuery.cssHooks[ prefix + suffix ] = {
			expand: function( value ) {
				var i = 0,
					expanded = {},
	
					// assumes a single number if not a string
					parts = typeof value === "string" ? value.split( " " ) : [ value ];
	
				for ( ; i < 4; i++ ) {
					expanded[ prefix + cssExpand[ i ] + suffix ] =
						parts[ i ] || parts[ i - 2 ] || parts[ 0 ];
				}
	
				return expanded;
			}
		};
	
		if ( !rmargin.test( prefix ) ) {
			jQuery.cssHooks[ prefix + suffix ].set = setPositiveNumber;
		}
	} );
	
	jQuery.fn.extend( {
		css: function( name, value ) {
			return access( this, function( elem, name, value ) {
				var styles, len,
					map = {},
					i = 0;
	
				if ( jQuery.isArray( name ) ) {
					styles = getStyles( elem );
					len = name.length;
	
					for ( ; i < len; i++ ) {
						map[ name[ i ] ] = jQuery.css( elem, name[ i ], false, styles );
					}
	
					return map;
				}
	
				return value !== undefined ?
					jQuery.style( elem, name, value ) :
					jQuery.css( elem, name );
			}, name, value, arguments.length > 1 );
		},
		show: function() {
			return showHide( this, true );
		},
		hide: function() {
			return showHide( this );
		},
		toggle: function( state ) {
			if ( typeof state === "boolean" ) {
				return state ? this.show() : this.hide();
			}
	
			return this.each( function() {
				if ( isHidden( this ) ) {
					jQuery( this ).show();
				} else {
					jQuery( this ).hide();
				}
			} );
		}
	} );
	
	
	function Tween( elem, options, prop, end, easing ) {
		return new Tween.prototype.init( elem, options, prop, end, easing );
	}
	jQuery.Tween = Tween;
	
	Tween.prototype = {
		constructor: Tween,
		init: function( elem, options, prop, end, easing, unit ) {
			this.elem = elem;
			this.prop = prop;
			this.easing = easing || jQuery.easing._default;
			this.options = options;
			this.start = this.now = this.cur();
			this.end = end;
			this.unit = unit || ( jQuery.cssNumber[ prop ] ? "" : "px" );
		},
		cur: function() {
			var hooks = Tween.propHooks[ this.prop ];
	
			return hooks && hooks.get ?
				hooks.get( this ) :
				Tween.propHooks._default.get( this );
		},
		run: function( percent ) {
			var eased,
				hooks = Tween.propHooks[ this.prop ];
	
			if ( this.options.duration ) {
				this.pos = eased = jQuery.easing[ this.easing ](
					percent, this.options.duration * percent, 0, 1, this.options.duration
				);
			} else {
				this.pos = eased = percent;
			}
			this.now = ( this.end - this.start ) * eased + this.start;
	
			if ( this.options.step ) {
				this.options.step.call( this.elem, this.now, this );
			}
	
			if ( hooks && hooks.set ) {
				hooks.set( this );
			} else {
				Tween.propHooks._default.set( this );
			}
			return this;
		}
	};
	
	Tween.prototype.init.prototype = Tween.prototype;
	
	Tween.propHooks = {
		_default: {
			get: function( tween ) {
				var result;
	
				// Use a property on the element directly when it is not a DOM element,
				// or when there is no matching style property that exists.
				if ( tween.elem.nodeType !== 1 ||
					tween.elem[ tween.prop ] != null && tween.elem.style[ tween.prop ] == null ) {
					return tween.elem[ tween.prop ];
				}
	
				// passing an empty string as a 3rd parameter to .css will automatically
				// attempt a parseFloat and fallback to a string if the parse fails
				// so, simple values such as "10px" are parsed to Float.
				// complex values such as "rotate(1rad)" are returned as is.
				result = jQuery.css( tween.elem, tween.prop, "" );
	
				// Empty strings, null, undefined and "auto" are converted to 0.
				return !result || result === "auto" ? 0 : result;
			},
			set: function( tween ) {
	
				// use step hook for back compat - use cssHook if its there - use .style if its
				// available and use plain properties where available
				if ( jQuery.fx.step[ tween.prop ] ) {
					jQuery.fx.step[ tween.prop ]( tween );
				} else if ( tween.elem.nodeType === 1 &&
					( tween.elem.style[ jQuery.cssProps[ tween.prop ] ] != null ||
						jQuery.cssHooks[ tween.prop ] ) ) {
					jQuery.style( tween.elem, tween.prop, tween.now + tween.unit );
				} else {
					tween.elem[ tween.prop ] = tween.now;
				}
			}
		}
	};
	
	// Support: IE <=9
	// Panic based approach to setting things on disconnected nodes
	
	Tween.propHooks.scrollTop = Tween.propHooks.scrollLeft = {
		set: function( tween ) {
			if ( tween.elem.nodeType && tween.elem.parentNode ) {
				tween.elem[ tween.prop ] = tween.now;
			}
		}
	};
	
	jQuery.easing = {
		linear: function( p ) {
			return p;
		},
		swing: function( p ) {
			return 0.5 - Math.cos( p * Math.PI ) / 2;
		},
		_default: "swing"
	};
	
	jQuery.fx = Tween.prototype.init;
	
	// Back Compat <1.8 extension point
	jQuery.fx.step = {};
	
	
	
	
	var
		fxNow, timerId,
		rfxtypes = /^(?:toggle|show|hide)$/,
		rrun = /queueHooks$/;
	
	// Animations created synchronously will run synchronously
	function createFxNow() {
		window.setTimeout( function() {
			fxNow = undefined;
		} );
		return ( fxNow = jQuery.now() );
	}
	
	// Generate parameters to create a standard animation
	function genFx( type, includeWidth ) {
		var which,
			attrs = { height: type },
			i = 0;
	
		// if we include width, step value is 1 to do all cssExpand values,
		// if we don't include width, step value is 2 to skip over Left and Right
		includeWidth = includeWidth ? 1 : 0;
		for ( ; i < 4 ; i += 2 - includeWidth ) {
			which = cssExpand[ i ];
			attrs[ "margin" + which ] = attrs[ "padding" + which ] = type;
		}
	
		if ( includeWidth ) {
			attrs.opacity = attrs.width = type;
		}
	
		return attrs;
	}
	
	function createTween( value, prop, animation ) {
		var tween,
			collection = ( Animation.tweeners[ prop ] || [] ).concat( Animation.tweeners[ "*" ] ),
			index = 0,
			length = collection.length;
		for ( ; index < length; index++ ) {
			if ( ( tween = collection[ index ].call( animation, prop, value ) ) ) {
	
				// we're done with this property
				return tween;
			}
		}
	}
	
	function defaultPrefilter( elem, props, opts ) {
		/* jshint validthis: true */
		var prop, value, toggle, tween, hooks, oldfire, display, checkDisplay,
			anim = this,
			orig = {},
			style = elem.style,
			hidden = elem.nodeType && isHidden( elem ),
			dataShow = jQuery._data( elem, "fxshow" );
	
		// handle queue: false promises
		if ( !opts.queue ) {
			hooks = jQuery._queueHooks( elem, "fx" );
			if ( hooks.unqueued == null ) {
				hooks.unqueued = 0;
				oldfire = hooks.empty.fire;
				hooks.empty.fire = function() {
					if ( !hooks.unqueued ) {
						oldfire();
					}
				};
			}
			hooks.unqueued++;
	
			anim.always( function() {
	
				// doing this makes sure that the complete handler will be called
				// before this completes
				anim.always( function() {
					hooks.unqueued--;
					if ( !jQuery.queue( elem, "fx" ).length ) {
						hooks.empty.fire();
					}
				} );
			} );
		}
	
		// height/width overflow pass
		if ( elem.nodeType === 1 && ( "height" in props || "width" in props ) ) {
	
			// Make sure that nothing sneaks out
			// Record all 3 overflow attributes because IE does not
			// change the overflow attribute when overflowX and
			// overflowY are set to the same value
			opts.overflow = [ style.overflow, style.overflowX, style.overflowY ];
	
			// Set display property to inline-block for height/width
			// animations on inline elements that are having width/height animated
			display = jQuery.css( elem, "display" );
	
			// Test default display if display is currently "none"
			checkDisplay = display === "none" ?
				jQuery._data( elem, "olddisplay" ) || defaultDisplay( elem.nodeName ) : display;
	
			if ( checkDisplay === "inline" && jQuery.css( elem, "float" ) === "none" ) {
	
				// inline-level elements accept inline-block;
				// block-level elements need to be inline with layout
				if ( !support.inlineBlockNeedsLayout || defaultDisplay( elem.nodeName ) === "inline" ) {
					style.display = "inline-block";
				} else {
					style.zoom = 1;
				}
			}
		}
	
		if ( opts.overflow ) {
			style.overflow = "hidden";
			if ( !support.shrinkWrapBlocks() ) {
				anim.always( function() {
					style.overflow = opts.overflow[ 0 ];
					style.overflowX = opts.overflow[ 1 ];
					style.overflowY = opts.overflow[ 2 ];
				} );
			}
		}
	
		// show/hide pass
		for ( prop in props ) {
			value = props[ prop ];
			if ( rfxtypes.exec( value ) ) {
				delete props[ prop ];
				toggle = toggle || value === "toggle";
				if ( value === ( hidden ? "hide" : "show" ) ) {
	
					// If there is dataShow left over from a stopped hide or show
					// and we are going to proceed with show, we should pretend to be hidden
					if ( value === "show" && dataShow && dataShow[ prop ] !== undefined ) {
						hidden = true;
					} else {
						continue;
					}
				}
				orig[ prop ] = dataShow && dataShow[ prop ] || jQuery.style( elem, prop );
	
			// Any non-fx value stops us from restoring the original display value
			} else {
				display = undefined;
			}
		}
	
		if ( !jQuery.isEmptyObject( orig ) ) {
			if ( dataShow ) {
				if ( "hidden" in dataShow ) {
					hidden = dataShow.hidden;
				}
			} else {
				dataShow = jQuery._data( elem, "fxshow", {} );
			}
	
			// store state if its toggle - enables .stop().toggle() to "reverse"
			if ( toggle ) {
				dataShow.hidden = !hidden;
			}
			if ( hidden ) {
				jQuery( elem ).show();
			} else {
				anim.done( function() {
					jQuery( elem ).hide();
				} );
			}
			anim.done( function() {
				var prop;
				jQuery._removeData( elem, "fxshow" );
				for ( prop in orig ) {
					jQuery.style( elem, prop, orig[ prop ] );
				}
			} );
			for ( prop in orig ) {
				tween = createTween( hidden ? dataShow[ prop ] : 0, prop, anim );
	
				if ( !( prop in dataShow ) ) {
					dataShow[ prop ] = tween.start;
					if ( hidden ) {
						tween.end = tween.start;
						tween.start = prop === "width" || prop === "height" ? 1 : 0;
					}
				}
			}
	
		// If this is a noop like .hide().hide(), restore an overwritten display value
		} else if ( ( display === "none" ? defaultDisplay( elem.nodeName ) : display ) === "inline" ) {
			style.display = display;
		}
	}
	
	function propFilter( props, specialEasing ) {
		var index, name, easing, value, hooks;
	
		// camelCase, specialEasing and expand cssHook pass
		for ( index in props ) {
			name = jQuery.camelCase( index );
			easing = specialEasing[ name ];
			value = props[ index ];
			if ( jQuery.isArray( value ) ) {
				easing = value[ 1 ];
				value = props[ index ] = value[ 0 ];
			}
	
			if ( index !== name ) {
				props[ name ] = value;
				delete props[ index ];
			}
	
			hooks = jQuery.cssHooks[ name ];
			if ( hooks && "expand" in hooks ) {
				value = hooks.expand( value );
				delete props[ name ];
	
				// not quite $.extend, this wont overwrite keys already present.
				// also - reusing 'index' from above because we have the correct "name"
				for ( index in value ) {
					if ( !( index in props ) ) {
						props[ index ] = value[ index ];
						specialEasing[ index ] = easing;
					}
				}
			} else {
				specialEasing[ name ] = easing;
			}
		}
	}
	
	function Animation( elem, properties, options ) {
		var result,
			stopped,
			index = 0,
			length = Animation.prefilters.length,
			deferred = jQuery.Deferred().always( function() {
	
				// don't match elem in the :animated selector
				delete tick.elem;
			} ),
			tick = function() {
				if ( stopped ) {
					return false;
				}
				var currentTime = fxNow || createFxNow(),
					remaining = Math.max( 0, animation.startTime + animation.duration - currentTime ),
	
					// Support: Android 2.3
					// Archaic crash bug won't allow us to use `1 - ( 0.5 || 0 )` (#12497)
					temp = remaining / animation.duration || 0,
					percent = 1 - temp,
					index = 0,
					length = animation.tweens.length;
	
				for ( ; index < length ; index++ ) {
					animation.tweens[ index ].run( percent );
				}
	
				deferred.notifyWith( elem, [ animation, percent, remaining ] );
	
				if ( percent < 1 && length ) {
					return remaining;
				} else {
					deferred.resolveWith( elem, [ animation ] );
					return false;
				}
			},
			animation = deferred.promise( {
				elem: elem,
				props: jQuery.extend( {}, properties ),
				opts: jQuery.extend( true, {
					specialEasing: {},
					easing: jQuery.easing._default
				}, options ),
				originalProperties: properties,
				originalOptions: options,
				startTime: fxNow || createFxNow(),
				duration: options.duration,
				tweens: [],
				createTween: function( prop, end ) {
					var tween = jQuery.Tween( elem, animation.opts, prop, end,
							animation.opts.specialEasing[ prop ] || animation.opts.easing );
					animation.tweens.push( tween );
					return tween;
				},
				stop: function( gotoEnd ) {
					var index = 0,
	
						// if we are going to the end, we want to run all the tweens
						// otherwise we skip this part
						length = gotoEnd ? animation.tweens.length : 0;
					if ( stopped ) {
						return this;
					}
					stopped = true;
					for ( ; index < length ; index++ ) {
						animation.tweens[ index ].run( 1 );
					}
	
					// resolve when we played the last frame
					// otherwise, reject
					if ( gotoEnd ) {
						deferred.notifyWith( elem, [ animation, 1, 0 ] );
						deferred.resolveWith( elem, [ animation, gotoEnd ] );
					} else {
						deferred.rejectWith( elem, [ animation, gotoEnd ] );
					}
					return this;
				}
			} ),
			props = animation.props;
	
		propFilter( props, animation.opts.specialEasing );
	
		for ( ; index < length ; index++ ) {
			result = Animation.prefilters[ index ].call( animation, elem, props, animation.opts );
			if ( result ) {
				if ( jQuery.isFunction( result.stop ) ) {
					jQuery._queueHooks( animation.elem, animation.opts.queue ).stop =
						jQuery.proxy( result.stop, result );
				}
				return result;
			}
		}
	
		jQuery.map( props, createTween, animation );
	
		if ( jQuery.isFunction( animation.opts.start ) ) {
			animation.opts.start.call( elem, animation );
		}
	
		jQuery.fx.timer(
			jQuery.extend( tick, {
				elem: elem,
				anim: animation,
				queue: animation.opts.queue
			} )
		);
	
		// attach callbacks from options
		return animation.progress( animation.opts.progress )
			.done( animation.opts.done, animation.opts.complete )
			.fail( animation.opts.fail )
			.always( animation.opts.always );
	}
	
	jQuery.Animation = jQuery.extend( Animation, {
	
		tweeners: {
			"*": [ function( prop, value ) {
				var tween = this.createTween( prop, value );
				adjustCSS( tween.elem, prop, rcssNum.exec( value ), tween );
				return tween;
			} ]
		},
	
		tweener: function( props, callback ) {
			if ( jQuery.isFunction( props ) ) {
				callback = props;
				props = [ "*" ];
			} else {
				props = props.match( rnotwhite );
			}
	
			var prop,
				index = 0,
				length = props.length;
	
			for ( ; index < length ; index++ ) {
				prop = props[ index ];
				Animation.tweeners[ prop ] = Animation.tweeners[ prop ] || [];
				Animation.tweeners[ prop ].unshift( callback );
			}
		},
	
		prefilters: [ defaultPrefilter ],
	
		prefilter: function( callback, prepend ) {
			if ( prepend ) {
				Animation.prefilters.unshift( callback );
			} else {
				Animation.prefilters.push( callback );
			}
		}
	} );
	
	jQuery.speed = function( speed, easing, fn ) {
		var opt = speed && typeof speed === "object" ? jQuery.extend( {}, speed ) : {
			complete: fn || !fn && easing ||
				jQuery.isFunction( speed ) && speed,
			duration: speed,
			easing: fn && easing || easing && !jQuery.isFunction( easing ) && easing
		};
	
		opt.duration = jQuery.fx.off ? 0 : typeof opt.duration === "number" ? opt.duration :
			opt.duration in jQuery.fx.speeds ?
				jQuery.fx.speeds[ opt.duration ] : jQuery.fx.speeds._default;
	
		// normalize opt.queue - true/undefined/null -> "fx"
		if ( opt.queue == null || opt.queue === true ) {
			opt.queue = "fx";
		}
	
		// Queueing
		opt.old = opt.complete;
	
		opt.complete = function() {
			if ( jQuery.isFunction( opt.old ) ) {
				opt.old.call( this );
			}
	
			if ( opt.queue ) {
				jQuery.dequeue( this, opt.queue );
			}
		};
	
		return opt;
	};
	
	jQuery.fn.extend( {
		fadeTo: function( speed, to, easing, callback ) {
	
			// show any hidden elements after setting opacity to 0
			return this.filter( isHidden ).css( "opacity", 0 ).show()
	
				// animate to the value specified
				.end().animate( { opacity: to }, speed, easing, callback );
		},
		animate: function( prop, speed, easing, callback ) {
			var empty = jQuery.isEmptyObject( prop ),
				optall = jQuery.speed( speed, easing, callback ),
				doAnimation = function() {
	
					// Operate on a copy of prop so per-property easing won't be lost
					var anim = Animation( this, jQuery.extend( {}, prop ), optall );
	
					// Empty animations, or finishing resolves immediately
					if ( empty || jQuery._data( this, "finish" ) ) {
						anim.stop( true );
					}
				};
				doAnimation.finish = doAnimation;
	
			return empty || optall.queue === false ?
				this.each( doAnimation ) :
				this.queue( optall.queue, doAnimation );
		},
		stop: function( type, clearQueue, gotoEnd ) {
			var stopQueue = function( hooks ) {
				var stop = hooks.stop;
				delete hooks.stop;
				stop( gotoEnd );
			};
	
			if ( typeof type !== "string" ) {
				gotoEnd = clearQueue;
				clearQueue = type;
				type = undefined;
			}
			if ( clearQueue && type !== false ) {
				this.queue( type || "fx", [] );
			}
	
			return this.each( function() {
				var dequeue = true,
					index = type != null && type + "queueHooks",
					timers = jQuery.timers,
					data = jQuery._data( this );
	
				if ( index ) {
					if ( data[ index ] && data[ index ].stop ) {
						stopQueue( data[ index ] );
					}
				} else {
					for ( index in data ) {
						if ( data[ index ] && data[ index ].stop && rrun.test( index ) ) {
							stopQueue( data[ index ] );
						}
					}
				}
	
				for ( index = timers.length; index--; ) {
					if ( timers[ index ].elem === this &&
						( type == null || timers[ index ].queue === type ) ) {
	
						timers[ index ].anim.stop( gotoEnd );
						dequeue = false;
						timers.splice( index, 1 );
					}
				}
	
				// start the next in the queue if the last step wasn't forced
				// timers currently will call their complete callbacks, which will dequeue
				// but only if they were gotoEnd
				if ( dequeue || !gotoEnd ) {
					jQuery.dequeue( this, type );
				}
			} );
		},
		finish: function( type ) {
			if ( type !== false ) {
				type = type || "fx";
			}
			return this.each( function() {
				var index,
					data = jQuery._data( this ),
					queue = data[ type + "queue" ],
					hooks = data[ type + "queueHooks" ],
					timers = jQuery.timers,
					length = queue ? queue.length : 0;
	
				// enable finishing flag on private data
				data.finish = true;
	
				// empty the queue first
				jQuery.queue( this, type, [] );
	
				if ( hooks && hooks.stop ) {
					hooks.stop.call( this, true );
				}
	
				// look for any active animations, and finish them
				for ( index = timers.length; index--; ) {
					if ( timers[ index ].elem === this && timers[ index ].queue === type ) {
						timers[ index ].anim.stop( true );
						timers.splice( index, 1 );
					}
				}
	
				// look for any animations in the old queue and finish them
				for ( index = 0; index < length; index++ ) {
					if ( queue[ index ] && queue[ index ].finish ) {
						queue[ index ].finish.call( this );
					}
				}
	
				// turn off finishing flag
				delete data.finish;
			} );
		}
	} );
	
	jQuery.each( [ "toggle", "show", "hide" ], function( i, name ) {
		var cssFn = jQuery.fn[ name ];
		jQuery.fn[ name ] = function( speed, easing, callback ) {
			return speed == null || typeof speed === "boolean" ?
				cssFn.apply( this, arguments ) :
				this.animate( genFx( name, true ), speed, easing, callback );
		};
	} );
	
	// Generate shortcuts for custom animations
	jQuery.each( {
		slideDown: genFx( "show" ),
		slideUp: genFx( "hide" ),
		slideToggle: genFx( "toggle" ),
		fadeIn: { opacity: "show" },
		fadeOut: { opacity: "hide" },
		fadeToggle: { opacity: "toggle" }
	}, function( name, props ) {
		jQuery.fn[ name ] = function( speed, easing, callback ) {
			return this.animate( props, speed, easing, callback );
		};
	} );
	
	jQuery.timers = [];
	jQuery.fx.tick = function() {
		var timer,
			timers = jQuery.timers,
			i = 0;
	
		fxNow = jQuery.now();
	
		for ( ; i < timers.length; i++ ) {
			timer = timers[ i ];
	
			// Checks the timer has not already been removed
			if ( !timer() && timers[ i ] === timer ) {
				timers.splice( i--, 1 );
			}
		}
	
		if ( !timers.length ) {
			jQuery.fx.stop();
		}
		fxNow = undefined;
	};
	
	jQuery.fx.timer = function( timer ) {
		jQuery.timers.push( timer );
		if ( timer() ) {
			jQuery.fx.start();
		} else {
			jQuery.timers.pop();
		}
	};
	
	jQuery.fx.interval = 13;
	
	jQuery.fx.start = function() {
		if ( !timerId ) {
			timerId = window.setInterval( jQuery.fx.tick, jQuery.fx.interval );
		}
	};
	
	jQuery.fx.stop = function() {
		window.clearInterval( timerId );
		timerId = null;
	};
	
	jQuery.fx.speeds = {
		slow: 600,
		fast: 200,
	
		// Default speed
		_default: 400
	};
	
	
	// Based off of the plugin by Clint Helfers, with permission.
	// http://web.archive.org/web/20100324014747/http://blindsignals.com/index.php/2009/07/jquery-delay/
	jQuery.fn.delay = function( time, type ) {
		time = jQuery.fx ? jQuery.fx.speeds[ time ] || time : time;
		type = type || "fx";
	
		return this.queue( type, function( next, hooks ) {
			var timeout = window.setTimeout( next, time );
			hooks.stop = function() {
				window.clearTimeout( timeout );
			};
		} );
	};
	
	
	( function() {
		var a,
			input = document.createElement( "input" ),
			div = document.createElement( "div" ),
			select = document.createElement( "select" ),
			opt = select.appendChild( document.createElement( "option" ) );
	
		// Setup
		div = document.createElement( "div" );
		div.setAttribute( "className", "t" );
		div.innerHTML = "  <link/><table></table><a href='/a'>a</a><input type='checkbox'/>";
		a = div.getElementsByTagName( "a" )[ 0 ];
	
		// Support: Windows Web Apps (WWA)
		// `type` must use .setAttribute for WWA (#14901)
		input.setAttribute( "type", "checkbox" );
		div.appendChild( input );
	
		a = div.getElementsByTagName( "a" )[ 0 ];
	
		// First batch of tests.
		a.style.cssText = "top:1px";
	
		// Test setAttribute on camelCase class.
		// If it works, we need attrFixes when doing get/setAttribute (ie6/7)
		support.getSetAttribute = div.className !== "t";
	
		// Get the style information from getAttribute
		// (IE uses .cssText instead)
		support.style = /top/.test( a.getAttribute( "style" ) );
	
		// Make sure that URLs aren't manipulated
		// (IE normalizes it by default)
		support.hrefNormalized = a.getAttribute( "href" ) === "/a";
	
		// Check the default checkbox/radio value ("" on WebKit; "on" elsewhere)
		support.checkOn = !!input.value;
	
		// Make sure that a selected-by-default option has a working selected property.
		// (WebKit defaults to false instead of true, IE too, if it's in an optgroup)
		support.optSelected = opt.selected;
	
		// Tests for enctype support on a form (#6743)
		support.enctype = !!document.createElement( "form" ).enctype;
	
		// Make sure that the options inside disabled selects aren't marked as disabled
		// (WebKit marks them as disabled)
		select.disabled = true;
		support.optDisabled = !opt.disabled;
	
		// Support: IE8 only
		// Check if we can trust getAttribute("value")
		input = document.createElement( "input" );
		input.setAttribute( "value", "" );
		support.input = input.getAttribute( "value" ) === "";
	
		// Check if an input maintains its value after becoming a radio
		input.value = "t";
		input.setAttribute( "type", "radio" );
		support.radioValue = input.value === "t";
	} )();
	
	
	var rreturn = /\r/g,
		rspaces = /[\x20\t\r\n\f]+/g;
	
	jQuery.fn.extend( {
		val: function( value ) {
			var hooks, ret, isFunction,
				elem = this[ 0 ];
	
			if ( !arguments.length ) {
				if ( elem ) {
					hooks = jQuery.valHooks[ elem.type ] ||
						jQuery.valHooks[ elem.nodeName.toLowerCase() ];
	
					if (
						hooks &&
						"get" in hooks &&
						( ret = hooks.get( elem, "value" ) ) !== undefined
					) {
						return ret;
					}
	
					ret = elem.value;
	
					return typeof ret === "string" ?
	
						// handle most common string cases
						ret.replace( rreturn, "" ) :
	
						// handle cases where value is null/undef or number
						ret == null ? "" : ret;
				}
	
				return;
			}
	
			isFunction = jQuery.isFunction( value );
	
			return this.each( function( i ) {
				var val;
	
				if ( this.nodeType !== 1 ) {
					return;
				}
	
				if ( isFunction ) {
					val = value.call( this, i, jQuery( this ).val() );
				} else {
					val = value;
				}
	
				// Treat null/undefined as ""; convert numbers to string
				if ( val == null ) {
					val = "";
				} else if ( typeof val === "number" ) {
					val += "";
				} else if ( jQuery.isArray( val ) ) {
					val = jQuery.map( val, function( value ) {
						return value == null ? "" : value + "";
					} );
				}
	
				hooks = jQuery.valHooks[ this.type ] || jQuery.valHooks[ this.nodeName.toLowerCase() ];
	
				// If set returns undefined, fall back to normal setting
				if ( !hooks || !( "set" in hooks ) || hooks.set( this, val, "value" ) === undefined ) {
					this.value = val;
				}
			} );
		}
	} );
	
	jQuery.extend( {
		valHooks: {
			option: {
				get: function( elem ) {
					var val = jQuery.find.attr( elem, "value" );
					return val != null ?
						val :
	
						// Support: IE10-11+
						// option.text throws exceptions (#14686, #14858)
						// Strip and collapse whitespace
						// https://html.spec.whatwg.org/#strip-and-collapse-whitespace
						jQuery.trim( jQuery.text( elem ) ).replace( rspaces, " " );
				}
			},
			select: {
				get: function( elem ) {
					var value, option,
						options = elem.options,
						index = elem.selectedIndex,
						one = elem.type === "select-one" || index < 0,
						values = one ? null : [],
						max = one ? index + 1 : options.length,
						i = index < 0 ?
							max :
							one ? index : 0;
	
					// Loop through all the selected options
					for ( ; i < max; i++ ) {
						option = options[ i ];
	
						// oldIE doesn't update selected after form reset (#2551)
						if ( ( option.selected || i === index ) &&
	
								// Don't return options that are disabled or in a disabled optgroup
								( support.optDisabled ?
									!option.disabled :
									option.getAttribute( "disabled" ) === null ) &&
								( !option.parentNode.disabled ||
									!jQuery.nodeName( option.parentNode, "optgroup" ) ) ) {
	
							// Get the specific value for the option
							value = jQuery( option ).val();
	
							// We don't need an array for one selects
							if ( one ) {
								return value;
							}
	
							// Multi-Selects return an array
							values.push( value );
						}
					}
	
					return values;
				},
	
				set: function( elem, value ) {
					var optionSet, option,
						options = elem.options,
						values = jQuery.makeArray( value ),
						i = options.length;
	
					while ( i-- ) {
						option = options[ i ];
	
						if ( jQuery.inArray( jQuery.valHooks.option.get( option ), values ) > -1 ) {
	
							// Support: IE6
							// When new option element is added to select box we need to
							// force reflow of newly added node in order to workaround delay
							// of initialization properties
							try {
								option.selected = optionSet = true;
	
							} catch ( _ ) {
	
								// Will be executed only in IE6
								option.scrollHeight;
							}
	
						} else {
							option.selected = false;
						}
					}
	
					// Force browsers to behave consistently when non-matching value is set
					if ( !optionSet ) {
						elem.selectedIndex = -1;
					}
	
					return options;
				}
			}
		}
	} );
	
	// Radios and checkboxes getter/setter
	jQuery.each( [ "radio", "checkbox" ], function() {
		jQuery.valHooks[ this ] = {
			set: function( elem, value ) {
				if ( jQuery.isArray( value ) ) {
					return ( elem.checked = jQuery.inArray( jQuery( elem ).val(), value ) > -1 );
				}
			}
		};
		if ( !support.checkOn ) {
			jQuery.valHooks[ this ].get = function( elem ) {
				return elem.getAttribute( "value" ) === null ? "on" : elem.value;
			};
		}
	} );
	
	
	
	
	var nodeHook, boolHook,
		attrHandle = jQuery.expr.attrHandle,
		ruseDefault = /^(?:checked|selected)$/i,
		getSetAttribute = support.getSetAttribute,
		getSetInput = support.input;
	
	jQuery.fn.extend( {
		attr: function( name, value ) {
			return access( this, jQuery.attr, name, value, arguments.length > 1 );
		},
	
		removeAttr: function( name ) {
			return this.each( function() {
				jQuery.removeAttr( this, name );
			} );
		}
	} );
	
	jQuery.extend( {
		attr: function( elem, name, value ) {
			var ret, hooks,
				nType = elem.nodeType;
	
			// Don't get/set attributes on text, comment and attribute nodes
			if ( nType === 3 || nType === 8 || nType === 2 ) {
				return;
			}
	
			// Fallback to prop when attributes are not supported
			if ( typeof elem.getAttribute === "undefined" ) {
				return jQuery.prop( elem, name, value );
			}
	
			// All attributes are lowercase
			// Grab necessary hook if one is defined
			if ( nType !== 1 || !jQuery.isXMLDoc( elem ) ) {
				name = name.toLowerCase();
				hooks = jQuery.attrHooks[ name ] ||
					( jQuery.expr.match.bool.test( name ) ? boolHook : nodeHook );
			}
	
			if ( value !== undefined ) {
				if ( value === null ) {
					jQuery.removeAttr( elem, name );
					return;
				}
	
				if ( hooks && "set" in hooks &&
					( ret = hooks.set( elem, value, name ) ) !== undefined ) {
					return ret;
				}
	
				elem.setAttribute( name, value + "" );
				return value;
			}
	
			if ( hooks && "get" in hooks && ( ret = hooks.get( elem, name ) ) !== null ) {
				return ret;
			}
	
			ret = jQuery.find.attr( elem, name );
	
			// Non-existent attributes return null, we normalize to undefined
			return ret == null ? undefined : ret;
		},
	
		attrHooks: {
			type: {
				set: function( elem, value ) {
					if ( !support.radioValue && value === "radio" &&
						jQuery.nodeName( elem, "input" ) ) {
	
						// Setting the type on a radio button after the value resets the value in IE8-9
						// Reset value to default in case type is set after value during creation
						var val = elem.value;
						elem.setAttribute( "type", value );
						if ( val ) {
							elem.value = val;
						}
						return value;
					}
				}
			}
		},
	
		removeAttr: function( elem, value ) {
			var name, propName,
				i = 0,
				attrNames = value && value.match( rnotwhite );
	
			if ( attrNames && elem.nodeType === 1 ) {
				while ( ( name = attrNames[ i++ ] ) ) {
					propName = jQuery.propFix[ name ] || name;
	
					// Boolean attributes get special treatment (#10870)
					if ( jQuery.expr.match.bool.test( name ) ) {
	
						// Set corresponding property to false
						if ( getSetInput && getSetAttribute || !ruseDefault.test( name ) ) {
							elem[ propName ] = false;
	
						// Support: IE<9
						// Also clear defaultChecked/defaultSelected (if appropriate)
						} else {
							elem[ jQuery.camelCase( "default-" + name ) ] =
								elem[ propName ] = false;
						}
	
					// See #9699 for explanation of this approach (setting first, then removal)
					} else {
						jQuery.attr( elem, name, "" );
					}
	
					elem.removeAttribute( getSetAttribute ? name : propName );
				}
			}
		}
	} );
	
	// Hooks for boolean attributes
	boolHook = {
		set: function( elem, value, name ) {
			if ( value === false ) {
	
				// Remove boolean attributes when set to false
				jQuery.removeAttr( elem, name );
			} else if ( getSetInput && getSetAttribute || !ruseDefault.test( name ) ) {
	
				// IE<8 needs the *property* name
				elem.setAttribute( !getSetAttribute && jQuery.propFix[ name ] || name, name );
	
			} else {
	
				// Support: IE<9
				// Use defaultChecked and defaultSelected for oldIE
				elem[ jQuery.camelCase( "default-" + name ) ] = elem[ name ] = true;
			}
			return name;
		}
	};
	
	jQuery.each( jQuery.expr.match.bool.source.match( /\w+/g ), function( i, name ) {
		var getter = attrHandle[ name ] || jQuery.find.attr;
	
		if ( getSetInput && getSetAttribute || !ruseDefault.test( name ) ) {
			attrHandle[ name ] = function( elem, name, isXML ) {
				var ret, handle;
				if ( !isXML ) {
	
					// Avoid an infinite loop by temporarily removing this function from the getter
					handle = attrHandle[ name ];
					attrHandle[ name ] = ret;
					ret = getter( elem, name, isXML ) != null ?
						name.toLowerCase() :
						null;
					attrHandle[ name ] = handle;
				}
				return ret;
			};
		} else {
			attrHandle[ name ] = function( elem, name, isXML ) {
				if ( !isXML ) {
					return elem[ jQuery.camelCase( "default-" + name ) ] ?
						name.toLowerCase() :
						null;
				}
			};
		}
	} );
	
	// fix oldIE attroperties
	if ( !getSetInput || !getSetAttribute ) {
		jQuery.attrHooks.value = {
			set: function( elem, value, name ) {
				if ( jQuery.nodeName( elem, "input" ) ) {
	
					// Does not return so that setAttribute is also used
					elem.defaultValue = value;
				} else {
	
					// Use nodeHook if defined (#1954); otherwise setAttribute is fine
					return nodeHook && nodeHook.set( elem, value, name );
				}
			}
		};
	}
	
	// IE6/7 do not support getting/setting some attributes with get/setAttribute
	if ( !getSetAttribute ) {
	
		// Use this for any attribute in IE6/7
		// This fixes almost every IE6/7 issue
		nodeHook = {
			set: function( elem, value, name ) {
	
				// Set the existing or create a new attribute node
				var ret = elem.getAttributeNode( name );
				if ( !ret ) {
					elem.setAttributeNode(
						( ret = elem.ownerDocument.createAttribute( name ) )
					);
				}
	
				ret.value = value += "";
	
				// Break association with cloned elements by also using setAttribute (#9646)
				if ( name === "value" || value === elem.getAttribute( name ) ) {
					return value;
				}
			}
		};
	
		// Some attributes are constructed with empty-string values when not defined
		attrHandle.id = attrHandle.name = attrHandle.coords =
			function( elem, name, isXML ) {
				var ret;
				if ( !isXML ) {
					return ( ret = elem.getAttributeNode( name ) ) && ret.value !== "" ?
						ret.value :
						null;
				}
			};
	
		// Fixing value retrieval on a button requires this module
		jQuery.valHooks.button = {
			get: function( elem, name ) {
				var ret = elem.getAttributeNode( name );
				if ( ret && ret.specified ) {
					return ret.value;
				}
			},
			set: nodeHook.set
		};
	
		// Set contenteditable to false on removals(#10429)
		// Setting to empty string throws an error as an invalid value
		jQuery.attrHooks.contenteditable = {
			set: function( elem, value, name ) {
				nodeHook.set( elem, value === "" ? false : value, name );
			}
		};
	
		// Set width and height to auto instead of 0 on empty string( Bug #8150 )
		// This is for removals
		jQuery.each( [ "width", "height" ], function( i, name ) {
			jQuery.attrHooks[ name ] = {
				set: function( elem, value ) {
					if ( value === "" ) {
						elem.setAttribute( name, "auto" );
						return value;
					}
				}
			};
		} );
	}
	
	if ( !support.style ) {
		jQuery.attrHooks.style = {
			get: function( elem ) {
	
				// Return undefined in the case of empty string
				// Note: IE uppercases css property names, but if we were to .toLowerCase()
				// .cssText, that would destroy case sensitivity in URL's, like in "background"
				return elem.style.cssText || undefined;
			},
			set: function( elem, value ) {
				return ( elem.style.cssText = value + "" );
			}
		};
	}
	
	
	
	
	var rfocusable = /^(?:input|select|textarea|button|object)$/i,
		rclickable = /^(?:a|area)$/i;
	
	jQuery.fn.extend( {
		prop: function( name, value ) {
			return access( this, jQuery.prop, name, value, arguments.length > 1 );
		},
	
		removeProp: function( name ) {
			name = jQuery.propFix[ name ] || name;
			return this.each( function() {
	
				// try/catch handles cases where IE balks (such as removing a property on window)
				try {
					this[ name ] = undefined;
					delete this[ name ];
				} catch ( e ) {}
			} );
		}
	} );
	
	jQuery.extend( {
		prop: function( elem, name, value ) {
			var ret, hooks,
				nType = elem.nodeType;
	
			// Don't get/set properties on text, comment and attribute nodes
			if ( nType === 3 || nType === 8 || nType === 2 ) {
				return;
			}
	
			if ( nType !== 1 || !jQuery.isXMLDoc( elem ) ) {
	
				// Fix name and attach hooks
				name = jQuery.propFix[ name ] || name;
				hooks = jQuery.propHooks[ name ];
			}
	
			if ( value !== undefined ) {
				if ( hooks && "set" in hooks &&
					( ret = hooks.set( elem, value, name ) ) !== undefined ) {
					return ret;
				}
	
				return ( elem[ name ] = value );
			}
	
			if ( hooks && "get" in hooks && ( ret = hooks.get( elem, name ) ) !== null ) {
				return ret;
			}
	
			return elem[ name ];
		},
	
		propHooks: {
			tabIndex: {
				get: function( elem ) {
	
					// elem.tabIndex doesn't always return the
					// correct value when it hasn't been explicitly set
					// http://fluidproject.org/blog/2008/01/09/getting-setting-and-removing-tabindex-values-with-javascript/
					// Use proper attribute retrieval(#12072)
					var tabindex = jQuery.find.attr( elem, "tabindex" );
	
					return tabindex ?
						parseInt( tabindex, 10 ) :
						rfocusable.test( elem.nodeName ) ||
							rclickable.test( elem.nodeName ) && elem.href ?
								0 :
								-1;
				}
			}
		},
	
		propFix: {
			"for": "htmlFor",
			"class": "className"
		}
	} );
	
	// Some attributes require a special call on IE
	// http://msdn.microsoft.com/en-us/library/ms536429%28VS.85%29.aspx
	if ( !support.hrefNormalized ) {
	
		// href/src property should get the full normalized URL (#10299/#12915)
		jQuery.each( [ "href", "src" ], function( i, name ) {
			jQuery.propHooks[ name ] = {
				get: function( elem ) {
					return elem.getAttribute( name, 4 );
				}
			};
		} );
	}
	
	// Support: Safari, IE9+
	// Accessing the selectedIndex property
	// forces the browser to respect setting selected
	// on the option
	// The getter ensures a default option is selected
	// when in an optgroup
	if ( !support.optSelected ) {
		jQuery.propHooks.selected = {
			get: function( elem ) {
				var parent = elem.parentNode;
	
				if ( parent ) {
					parent.selectedIndex;
	
					// Make sure that it also works with optgroups, see #5701
					if ( parent.parentNode ) {
						parent.parentNode.selectedIndex;
					}
				}
				return null;
			},
			set: function( elem ) {
				var parent = elem.parentNode;
				if ( parent ) {
					parent.selectedIndex;
	
					if ( parent.parentNode ) {
						parent.parentNode.selectedIndex;
					}
				}
			}
		};
	}
	
	jQuery.each( [
		"tabIndex",
		"readOnly",
		"maxLength",
		"cellSpacing",
		"cellPadding",
		"rowSpan",
		"colSpan",
		"useMap",
		"frameBorder",
		"contentEditable"
	], function() {
		jQuery.propFix[ this.toLowerCase() ] = this;
	} );
	
	// IE6/7 call enctype encoding
	if ( !support.enctype ) {
		jQuery.propFix.enctype = "encoding";
	}
	
	
	
	
	var rclass = /[\t\r\n\f]/g;
	
	function getClass( elem ) {
		return jQuery.attr( elem, "class" ) || "";
	}
	
	jQuery.fn.extend( {
		addClass: function( value ) {
			var classes, elem, cur, curValue, clazz, j, finalValue,
				i = 0;
	
			if ( jQuery.isFunction( value ) ) {
				return this.each( function( j ) {
					jQuery( this ).addClass( value.call( this, j, getClass( this ) ) );
				} );
			}
	
			if ( typeof value === "string" && value ) {
				classes = value.match( rnotwhite ) || [];
	
				while ( ( elem = this[ i++ ] ) ) {
					curValue = getClass( elem );
					cur = elem.nodeType === 1 &&
						( " " + curValue + " " ).replace( rclass, " " );
	
					if ( cur ) {
						j = 0;
						while ( ( clazz = classes[ j++ ] ) ) {
							if ( cur.indexOf( " " + clazz + " " ) < 0 ) {
								cur += clazz + " ";
							}
						}
	
						// only assign if different to avoid unneeded rendering.
						finalValue = jQuery.trim( cur );
						if ( curValue !== finalValue ) {
							jQuery.attr( elem, "class", finalValue );
						}
					}
				}
			}
	
			return this;
		},
	
		removeClass: function( value ) {
			var classes, elem, cur, curValue, clazz, j, finalValue,
				i = 0;
	
			if ( jQuery.isFunction( value ) ) {
				return this.each( function( j ) {
					jQuery( this ).removeClass( value.call( this, j, getClass( this ) ) );
				} );
			}
	
			if ( !arguments.length ) {
				return this.attr( "class", "" );
			}
	
			if ( typeof value === "string" && value ) {
				classes = value.match( rnotwhite ) || [];
	
				while ( ( elem = this[ i++ ] ) ) {
					curValue = getClass( elem );
	
					// This expression is here for better compressibility (see addClass)
					cur = elem.nodeType === 1 &&
						( " " + curValue + " " ).replace( rclass, " " );
	
					if ( cur ) {
						j = 0;
						while ( ( clazz = classes[ j++ ] ) ) {
	
							// Remove *all* instances
							while ( cur.indexOf( " " + clazz + " " ) > -1 ) {
								cur = cur.replace( " " + clazz + " ", " " );
							}
						}
	
						// Only assign if different to avoid unneeded rendering.
						finalValue = jQuery.trim( cur );
						if ( curValue !== finalValue ) {
							jQuery.attr( elem, "class", finalValue );
						}
					}
				}
			}
	
			return this;
		},
	
		toggleClass: function( value, stateVal ) {
			var type = typeof value;
	
			if ( typeof stateVal === "boolean" && type === "string" ) {
				return stateVal ? this.addClass( value ) : this.removeClass( value );
			}
	
			if ( jQuery.isFunction( value ) ) {
				return this.each( function( i ) {
					jQuery( this ).toggleClass(
						value.call( this, i, getClass( this ), stateVal ),
						stateVal
					);
				} );
			}
	
			return this.each( function() {
				var className, i, self, classNames;
	
				if ( type === "string" ) {
	
					// Toggle individual class names
					i = 0;
					self = jQuery( this );
					classNames = value.match( rnotwhite ) || [];
	
					while ( ( className = classNames[ i++ ] ) ) {
	
						// Check each className given, space separated list
						if ( self.hasClass( className ) ) {
							self.removeClass( className );
						} else {
							self.addClass( className );
						}
					}
	
				// Toggle whole class name
				} else if ( value === undefined || type === "boolean" ) {
					className = getClass( this );
					if ( className ) {
	
						// store className if set
						jQuery._data( this, "__className__", className );
					}
	
					// If the element has a class name or if we're passed "false",
					// then remove the whole classname (if there was one, the above saved it).
					// Otherwise bring back whatever was previously saved (if anything),
					// falling back to the empty string if nothing was stored.
					jQuery.attr( this, "class",
						className || value === false ?
						"" :
						jQuery._data( this, "__className__" ) || ""
					);
				}
			} );
		},
	
		hasClass: function( selector ) {
			var className, elem,
				i = 0;
	
			className = " " + selector + " ";
			while ( ( elem = this[ i++ ] ) ) {
				if ( elem.nodeType === 1 &&
					( " " + getClass( elem ) + " " ).replace( rclass, " " )
						.indexOf( className ) > -1
				) {
					return true;
				}
			}
	
			return false;
		}
	} );
	
	
	
	
	// Return jQuery for attributes-only inclusion
	
	
	jQuery.each( ( "blur focus focusin focusout load resize scroll unload click dblclick " +
		"mousedown mouseup mousemove mouseover mouseout mouseenter mouseleave " +
		"change select submit keydown keypress keyup error contextmenu" ).split( " " ),
		function( i, name ) {
	
		// Handle event binding
		jQuery.fn[ name ] = function( data, fn ) {
			return arguments.length > 0 ?
				this.on( name, null, data, fn ) :
				this.trigger( name );
		};
	} );
	
	jQuery.fn.extend( {
		hover: function( fnOver, fnOut ) {
			return this.mouseenter( fnOver ).mouseleave( fnOut || fnOver );
		}
	} );
	
	
	var location = window.location;
	
	var nonce = jQuery.now();
	
	var rquery = ( /\?/ );
	
	
	
	var rvalidtokens = /(,)|(\[|{)|(}|])|"(?:[^"\\\r\n]|\\["\\\/bfnrt]|\\u[\da-fA-F]{4})*"\s*:?|true|false|null|-?(?!0\d)\d+(?:\.\d+|)(?:[eE][+-]?\d+|)/g;
	
	jQuery.parseJSON = function( data ) {
	
		// Attempt to parse using the native JSON parser first
		if ( window.JSON && window.JSON.parse ) {
	
			// Support: Android 2.3
			// Workaround failure to string-cast null input
			return window.JSON.parse( data + "" );
		}
	
		var requireNonComma,
			depth = null,
			str = jQuery.trim( data + "" );
	
		// Guard against invalid (and possibly dangerous) input by ensuring that nothing remains
		// after removing valid tokens
		return str && !jQuery.trim( str.replace( rvalidtokens, function( token, comma, open, close ) {
	
			// Force termination if we see a misplaced comma
			if ( requireNonComma && comma ) {
				depth = 0;
			}
	
			// Perform no more replacements after returning to outermost depth
			if ( depth === 0 ) {
				return token;
			}
	
			// Commas must not follow "[", "{", or ","
			requireNonComma = open || comma;
	
			// Determine new depth
			// array/object open ("[" or "{"): depth += true - false (increment)
			// array/object close ("]" or "}"): depth += false - true (decrement)
			// other cases ("," or primitive): depth += true - true (numeric cast)
			depth += !close - !open;
	
			// Remove this token
			return "";
		} ) ) ?
			( Function( "return " + str ) )() :
			jQuery.error( "Invalid JSON: " + data );
	};
	
	
	// Cross-browser xml parsing
	jQuery.parseXML = function( data ) {
		var xml, tmp;
		if ( !data || typeof data !== "string" ) {
			return null;
		}
		try {
			if ( window.DOMParser ) { // Standard
				tmp = new window.DOMParser();
				xml = tmp.parseFromString( data, "text/xml" );
			} else { // IE
				xml = new window.ActiveXObject( "Microsoft.XMLDOM" );
				xml.async = "false";
				xml.loadXML( data );
			}
		} catch ( e ) {
			xml = undefined;
		}
		if ( !xml || !xml.documentElement || xml.getElementsByTagName( "parsererror" ).length ) {
			jQuery.error( "Invalid XML: " + data );
		}
		return xml;
	};
	
	
	var
		rhash = /#.*$/,
		rts = /([?&])_=[^&]*/,
	
		// IE leaves an \r character at EOL
		rheaders = /^(.*?):[ \t]*([^\r\n]*)\r?$/mg,
	
		// #7653, #8125, #8152: local protocol detection
		rlocalProtocol = /^(?:about|app|app-storage|.+-extension|file|res|widget):$/,
		rnoContent = /^(?:GET|HEAD)$/,
		rprotocol = /^\/\//,
		rurl = /^([\w.+-]+:)(?:\/\/(?:[^\/?#]*@|)([^\/?#:]*)(?::(\d+)|)|)/,
	
		/* Prefilters
		 * 1) They are useful to introduce custom dataTypes (see ajax/jsonp.js for an example)
		 * 2) These are called:
		 *    - BEFORE asking for a transport
		 *    - AFTER param serialization (s.data is a string if s.processData is true)
		 * 3) key is the dataType
		 * 4) the catchall symbol "*" can be used
		 * 5) execution will start with transport dataType and THEN continue down to "*" if needed
		 */
		prefilters = {},
	
		/* Transports bindings
		 * 1) key is the dataType
		 * 2) the catchall symbol "*" can be used
		 * 3) selection will start with transport dataType and THEN go to "*" if needed
		 */
		transports = {},
	
		// Avoid comment-prolog char sequence (#10098); must appease lint and evade compression
		allTypes = "*/".concat( "*" ),
	
		// Document location
		ajaxLocation = location.href,
	
		// Segment location into parts
		ajaxLocParts = rurl.exec( ajaxLocation.toLowerCase() ) || [];
	
	// Base "constructor" for jQuery.ajaxPrefilter and jQuery.ajaxTransport
	function addToPrefiltersOrTransports( structure ) {
	
		// dataTypeExpression is optional and defaults to "*"
		return function( dataTypeExpression, func ) {
	
			if ( typeof dataTypeExpression !== "string" ) {
				func = dataTypeExpression;
				dataTypeExpression = "*";
			}
	
			var dataType,
				i = 0,
				dataTypes = dataTypeExpression.toLowerCase().match( rnotwhite ) || [];
	
			if ( jQuery.isFunction( func ) ) {
	
				// For each dataType in the dataTypeExpression
				while ( ( dataType = dataTypes[ i++ ] ) ) {
	
					// Prepend if requested
					if ( dataType.charAt( 0 ) === "+" ) {
						dataType = dataType.slice( 1 ) || "*";
						( structure[ dataType ] = structure[ dataType ] || [] ).unshift( func );
	
					// Otherwise append
					} else {
						( structure[ dataType ] = structure[ dataType ] || [] ).push( func );
					}
				}
			}
		};
	}
	
	// Base inspection function for prefilters and transports
	function inspectPrefiltersOrTransports( structure, options, originalOptions, jqXHR ) {
	
		var inspected = {},
			seekingTransport = ( structure === transports );
	
		function inspect( dataType ) {
			var selected;
			inspected[ dataType ] = true;
			jQuery.each( structure[ dataType ] || [], function( _, prefilterOrFactory ) {
				var dataTypeOrTransport = prefilterOrFactory( options, originalOptions, jqXHR );
				if ( typeof dataTypeOrTransport === "string" &&
					!seekingTransport && !inspected[ dataTypeOrTransport ] ) {
	
					options.dataTypes.unshift( dataTypeOrTransport );
					inspect( dataTypeOrTransport );
					return false;
				} else if ( seekingTransport ) {
					return !( selected = dataTypeOrTransport );
				}
			} );
			return selected;
		}
	
		return inspect( options.dataTypes[ 0 ] ) || !inspected[ "*" ] && inspect( "*" );
	}
	
	// A special extend for ajax options
	// that takes "flat" options (not to be deep extended)
	// Fixes #9887
	function ajaxExtend( target, src ) {
		var deep, key,
			flatOptions = jQuery.ajaxSettings.flatOptions || {};
	
		for ( key in src ) {
			if ( src[ key ] !== undefined ) {
				( flatOptions[ key ] ? target : ( deep || ( deep = {} ) ) )[ key ] = src[ key ];
			}
		}
		if ( deep ) {
			jQuery.extend( true, target, deep );
		}
	
		return target;
	}
	
	/* Handles responses to an ajax request:
	 * - finds the right dataType (mediates between content-type and expected dataType)
	 * - returns the corresponding response
	 */
	function ajaxHandleResponses( s, jqXHR, responses ) {
		var firstDataType, ct, finalDataType, type,
			contents = s.contents,
			dataTypes = s.dataTypes;
	
		// Remove auto dataType and get content-type in the process
		while ( dataTypes[ 0 ] === "*" ) {
			dataTypes.shift();
			if ( ct === undefined ) {
				ct = s.mimeType || jqXHR.getResponseHeader( "Content-Type" );
			}
		}
	
		// Check if we're dealing with a known content-type
		if ( ct ) {
			for ( type in contents ) {
				if ( contents[ type ] && contents[ type ].test( ct ) ) {
					dataTypes.unshift( type );
					break;
				}
			}
		}
	
		// Check to see if we have a response for the expected dataType
		if ( dataTypes[ 0 ] in responses ) {
			finalDataType = dataTypes[ 0 ];
		} else {
	
			// Try convertible dataTypes
			for ( type in responses ) {
				if ( !dataTypes[ 0 ] || s.converters[ type + " " + dataTypes[ 0 ] ] ) {
					finalDataType = type;
					break;
				}
				if ( !firstDataType ) {
					firstDataType = type;
				}
			}
	
			// Or just use first one
			finalDataType = finalDataType || firstDataType;
		}
	
		// If we found a dataType
		// We add the dataType to the list if needed
		// and return the corresponding response
		if ( finalDataType ) {
			if ( finalDataType !== dataTypes[ 0 ] ) {
				dataTypes.unshift( finalDataType );
			}
			return responses[ finalDataType ];
		}
	}
	
	/* Chain conversions given the request and the original response
	 * Also sets the responseXXX fields on the jqXHR instance
	 */
	function ajaxConvert( s, response, jqXHR, isSuccess ) {
		var conv2, current, conv, tmp, prev,
			converters = {},
	
			// Work with a copy of dataTypes in case we need to modify it for conversion
			dataTypes = s.dataTypes.slice();
	
		// Create converters map with lowercased keys
		if ( dataTypes[ 1 ] ) {
			for ( conv in s.converters ) {
				converters[ conv.toLowerCase() ] = s.converters[ conv ];
			}
		}
	
		current = dataTypes.shift();
	
		// Convert to each sequential dataType
		while ( current ) {
	
			if ( s.responseFields[ current ] ) {
				jqXHR[ s.responseFields[ current ] ] = response;
			}
	
			// Apply the dataFilter if provided
			if ( !prev && isSuccess && s.dataFilter ) {
				response = s.dataFilter( response, s.dataType );
			}
	
			prev = current;
			current = dataTypes.shift();
	
			if ( current ) {
	
				// There's only work to do if current dataType is non-auto
				if ( current === "*" ) {
	
					current = prev;
	
				// Convert response if prev dataType is non-auto and differs from current
				} else if ( prev !== "*" && prev !== current ) {
	
					// Seek a direct converter
					conv = converters[ prev + " " + current ] || converters[ "* " + current ];
	
					// If none found, seek a pair
					if ( !conv ) {
						for ( conv2 in converters ) {
	
							// If conv2 outputs current
							tmp = conv2.split( " " );
							if ( tmp[ 1 ] === current ) {
	
								// If prev can be converted to accepted input
								conv = converters[ prev + " " + tmp[ 0 ] ] ||
									converters[ "* " + tmp[ 0 ] ];
								if ( conv ) {
	
									// Condense equivalence converters
									if ( conv === true ) {
										conv = converters[ conv2 ];
	
									// Otherwise, insert the intermediate dataType
									} else if ( converters[ conv2 ] !== true ) {
										current = tmp[ 0 ];
										dataTypes.unshift( tmp[ 1 ] );
									}
									break;
								}
							}
						}
					}
	
					// Apply converter (if not an equivalence)
					if ( conv !== true ) {
	
						// Unless errors are allowed to bubble, catch and return them
						if ( conv && s[ "throws" ] ) { // jscs:ignore requireDotNotation
							response = conv( response );
						} else {
							try {
								response = conv( response );
							} catch ( e ) {
								return {
									state: "parsererror",
									error: conv ? e : "No conversion from " + prev + " to " + current
								};
							}
						}
					}
				}
			}
		}
	
		return { state: "success", data: response };
	}
	
	jQuery.extend( {
	
		// Counter for holding the number of active queries
		active: 0,
	
		// Last-Modified header cache for next request
		lastModified: {},
		etag: {},
	
		ajaxSettings: {
			url: ajaxLocation,
			type: "GET",
			isLocal: rlocalProtocol.test( ajaxLocParts[ 1 ] ),
			global: true,
			processData: true,
			async: true,
			contentType: "application/x-www-form-urlencoded; charset=UTF-8",
			/*
			timeout: 0,
			data: null,
			dataType: null,
			username: null,
			password: null,
			cache: null,
			throws: false,
			traditional: false,
			headers: {},
			*/
	
			accepts: {
				"*": allTypes,
				text: "text/plain",
				html: "text/html",
				xml: "application/xml, text/xml",
				json: "application/json, text/javascript"
			},
	
			contents: {
				xml: /\bxml\b/,
				html: /\bhtml/,
				json: /\bjson\b/
			},
	
			responseFields: {
				xml: "responseXML",
				text: "responseText",
				json: "responseJSON"
			},
	
			// Data converters
			// Keys separate source (or catchall "*") and destination types with a single space
			converters: {
	
				// Convert anything to text
				"* text": String,
	
				// Text to html (true = no transformation)
				"text html": true,
	
				// Evaluate text as a json expression
				"text json": jQuery.parseJSON,
	
				// Parse text as xml
				"text xml": jQuery.parseXML
			},
	
			// For options that shouldn't be deep extended:
			// you can add your own custom options here if
			// and when you create one that shouldn't be
			// deep extended (see ajaxExtend)
			flatOptions: {
				url: true,
				context: true
			}
		},
	
		// Creates a full fledged settings object into target
		// with both ajaxSettings and settings fields.
		// If target is omitted, writes into ajaxSettings.
		ajaxSetup: function( target, settings ) {
			return settings ?
	
				// Building a settings object
				ajaxExtend( ajaxExtend( target, jQuery.ajaxSettings ), settings ) :
	
				// Extending ajaxSettings
				ajaxExtend( jQuery.ajaxSettings, target );
		},
	
		ajaxPrefilter: addToPrefiltersOrTransports( prefilters ),
		ajaxTransport: addToPrefiltersOrTransports( transports ),
	
		// Main method
		ajax: function( url, options ) {
	
			// If url is an object, simulate pre-1.5 signature
			if ( typeof url === "object" ) {
				options = url;
				url = undefined;
			}
	
			// Force options to be an object
			options = options || {};
	
			var
	
				// Cross-domain detection vars
				parts,
	
				// Loop variable
				i,
	
				// URL without anti-cache param
				cacheURL,
	
				// Response headers as string
				responseHeadersString,
	
				// timeout handle
				timeoutTimer,
	
				// To know if global events are to be dispatched
				fireGlobals,
	
				transport,
	
				// Response headers
				responseHeaders,
	
				// Create the final options object
				s = jQuery.ajaxSetup( {}, options ),
	
				// Callbacks context
				callbackContext = s.context || s,
	
				// Context for global events is callbackContext if it is a DOM node or jQuery collection
				globalEventContext = s.context &&
					( callbackContext.nodeType || callbackContext.jquery ) ?
						jQuery( callbackContext ) :
						jQuery.event,
	
				// Deferreds
				deferred = jQuery.Deferred(),
				completeDeferred = jQuery.Callbacks( "once memory" ),
	
				// Status-dependent callbacks
				statusCode = s.statusCode || {},
	
				// Headers (they are sent all at once)
				requestHeaders = {},
				requestHeadersNames = {},
	
				// The jqXHR state
				state = 0,
	
				// Default abort message
				strAbort = "canceled",
	
				// Fake xhr
				jqXHR = {
					readyState: 0,
	
					// Builds headers hashtable if needed
					getResponseHeader: function( key ) {
						var match;
						if ( state === 2 ) {
							if ( !responseHeaders ) {
								responseHeaders = {};
								while ( ( match = rheaders.exec( responseHeadersString ) ) ) {
									responseHeaders[ match[ 1 ].toLowerCase() ] = match[ 2 ];
								}
							}
							match = responseHeaders[ key.toLowerCase() ];
						}
						return match == null ? null : match;
					},
	
					// Raw string
					getAllResponseHeaders: function() {
						return state === 2 ? responseHeadersString : null;
					},
	
					// Caches the header
					setRequestHeader: function( name, value ) {
						var lname = name.toLowerCase();
						if ( !state ) {
							name = requestHeadersNames[ lname ] = requestHeadersNames[ lname ] || name;
							requestHeaders[ name ] = value;
						}
						return this;
					},
	
					// Overrides response content-type header
					overrideMimeType: function( type ) {
						if ( !state ) {
							s.mimeType = type;
						}
						return this;
					},
	
					// Status-dependent callbacks
					statusCode: function( map ) {
						var code;
						if ( map ) {
							if ( state < 2 ) {
								for ( code in map ) {
	
									// Lazy-add the new callback in a way that preserves old ones
									statusCode[ code ] = [ statusCode[ code ], map[ code ] ];
								}
							} else {
	
								// Execute the appropriate callbacks
								jqXHR.always( map[ jqXHR.status ] );
							}
						}
						return this;
					},
	
					// Cancel the request
					abort: function( statusText ) {
						var finalText = statusText || strAbort;
						if ( transport ) {
							transport.abort( finalText );
						}
						done( 0, finalText );
						return this;
					}
				};
	
			// Attach deferreds
			deferred.promise( jqXHR ).complete = completeDeferred.add;
			jqXHR.success = jqXHR.done;
			jqXHR.error = jqXHR.fail;
	
			// Remove hash character (#7531: and string promotion)
			// Add protocol if not provided (#5866: IE7 issue with protocol-less urls)
			// Handle falsy url in the settings object (#10093: consistency with old signature)
			// We also use the url parameter if available
			s.url = ( ( url || s.url || ajaxLocation ) + "" )
				.replace( rhash, "" )
				.replace( rprotocol, ajaxLocParts[ 1 ] + "//" );
	
			// Alias method option to type as per ticket #12004
			s.type = options.method || options.type || s.method || s.type;
	
			// Extract dataTypes list
			s.dataTypes = jQuery.trim( s.dataType || "*" ).toLowerCase().match( rnotwhite ) || [ "" ];
	
			// A cross-domain request is in order when we have a protocol:host:port mismatch
			if ( s.crossDomain == null ) {
				parts = rurl.exec( s.url.toLowerCase() );
				s.crossDomain = !!( parts &&
					( parts[ 1 ] !== ajaxLocParts[ 1 ] || parts[ 2 ] !== ajaxLocParts[ 2 ] ||
						( parts[ 3 ] || ( parts[ 1 ] === "http:" ? "80" : "443" ) ) !==
							( ajaxLocParts[ 3 ] || ( ajaxLocParts[ 1 ] === "http:" ? "80" : "443" ) ) )
				);
			}
	
			// Convert data if not already a string
			if ( s.data && s.processData && typeof s.data !== "string" ) {
				s.data = jQuery.param( s.data, s.traditional );
			}
	
			// Apply prefilters
			inspectPrefiltersOrTransports( prefilters, s, options, jqXHR );
	
			// If request was aborted inside a prefilter, stop there
			if ( state === 2 ) {
				return jqXHR;
			}
	
			// We can fire global events as of now if asked to
			// Don't fire events if jQuery.event is undefined in an AMD-usage scenario (#15118)
			fireGlobals = jQuery.event && s.global;
	
			// Watch for a new set of requests
			if ( fireGlobals && jQuery.active++ === 0 ) {
				jQuery.event.trigger( "ajaxStart" );
			}
	
			// Uppercase the type
			s.type = s.type.toUpperCase();
	
			// Determine if request has content
			s.hasContent = !rnoContent.test( s.type );
	
			// Save the URL in case we're toying with the If-Modified-Since
			// and/or If-None-Match header later on
			cacheURL = s.url;
	
			// More options handling for requests with no content
			if ( !s.hasContent ) {
	
				// If data is available, append data to url
				if ( s.data ) {
					cacheURL = ( s.url += ( rquery.test( cacheURL ) ? "&" : "?" ) + s.data );
	
					// #9682: remove data so that it's not used in an eventual retry
					delete s.data;
				}
	
				// Add anti-cache in url if needed
				if ( s.cache === false ) {
					s.url = rts.test( cacheURL ) ?
	
						// If there is already a '_' parameter, set its value
						cacheURL.replace( rts, "$1_=" + nonce++ ) :
	
						// Otherwise add one to the end
						cacheURL + ( rquery.test( cacheURL ) ? "&" : "?" ) + "_=" + nonce++;
				}
			}
	
			// Set the If-Modified-Since and/or If-None-Match header, if in ifModified mode.
			if ( s.ifModified ) {
				if ( jQuery.lastModified[ cacheURL ] ) {
					jqXHR.setRequestHeader( "If-Modified-Since", jQuery.lastModified[ cacheURL ] );
				}
				if ( jQuery.etag[ cacheURL ] ) {
					jqXHR.setRequestHeader( "If-None-Match", jQuery.etag[ cacheURL ] );
				}
			}
	
			// Set the correct header, if data is being sent
			if ( s.data && s.hasContent && s.contentType !== false || options.contentType ) {
				jqXHR.setRequestHeader( "Content-Type", s.contentType );
			}
	
			// Set the Accepts header for the server, depending on the dataType
			jqXHR.setRequestHeader(
				"Accept",
				s.dataTypes[ 0 ] && s.accepts[ s.dataTypes[ 0 ] ] ?
					s.accepts[ s.dataTypes[ 0 ] ] +
						( s.dataTypes[ 0 ] !== "*" ? ", " + allTypes + "; q=0.01" : "" ) :
					s.accepts[ "*" ]
			);
	
			// Check for headers option
			for ( i in s.headers ) {
				jqXHR.setRequestHeader( i, s.headers[ i ] );
			}
	
			// Allow custom headers/mimetypes and early abort
			if ( s.beforeSend &&
				( s.beforeSend.call( callbackContext, jqXHR, s ) === false || state === 2 ) ) {
	
				// Abort if not done already and return
				return jqXHR.abort();
			}
	
			// aborting is no longer a cancellation
			strAbort = "abort";
	
			// Install callbacks on deferreds
			for ( i in { success: 1, error: 1, complete: 1 } ) {
				jqXHR[ i ]( s[ i ] );
			}
	
			// Get transport
			transport = inspectPrefiltersOrTransports( transports, s, options, jqXHR );
	
			// If no transport, we auto-abort
			if ( !transport ) {
				done( -1, "No Transport" );
			} else {
				jqXHR.readyState = 1;
	
				// Send global event
				if ( fireGlobals ) {
					globalEventContext.trigger( "ajaxSend", [ jqXHR, s ] );
				}
	
				// If request was aborted inside ajaxSend, stop there
				if ( state === 2 ) {
					return jqXHR;
				}
	
				// Timeout
				if ( s.async && s.timeout > 0 ) {
					timeoutTimer = window.setTimeout( function() {
						jqXHR.abort( "timeout" );
					}, s.timeout );
				}
	
				try {
					state = 1;
					transport.send( requestHeaders, done );
				} catch ( e ) {
	
					// Propagate exception as error if not done
					if ( state < 2 ) {
						done( -1, e );
	
					// Simply rethrow otherwise
					} else {
						throw e;
					}
				}
			}
	
			// Callback for when everything is done
			function done( status, nativeStatusText, responses, headers ) {
				var isSuccess, success, error, response, modified,
					statusText = nativeStatusText;
	
				// Called once
				if ( state === 2 ) {
					return;
				}
	
				// State is "done" now
				state = 2;
	
				// Clear timeout if it exists
				if ( timeoutTimer ) {
					window.clearTimeout( timeoutTimer );
				}
	
				// Dereference transport for early garbage collection
				// (no matter how long the jqXHR object will be used)
				transport = undefined;
	
				// Cache response headers
				responseHeadersString = headers || "";
	
				// Set readyState
				jqXHR.readyState = status > 0 ? 4 : 0;
	
				// Determine if successful
				isSuccess = status >= 200 && status < 300 || status === 304;
	
				// Get response data
				if ( responses ) {
					response = ajaxHandleResponses( s, jqXHR, responses );
				}
	
				// Convert no matter what (that way responseXXX fields are always set)
				response = ajaxConvert( s, response, jqXHR, isSuccess );
	
				// If successful, handle type chaining
				if ( isSuccess ) {
	
					// Set the If-Modified-Since and/or If-None-Match header, if in ifModified mode.
					if ( s.ifModified ) {
						modified = jqXHR.getResponseHeader( "Last-Modified" );
						if ( modified ) {
							jQuery.lastModified[ cacheURL ] = modified;
						}
						modified = jqXHR.getResponseHeader( "etag" );
						if ( modified ) {
							jQuery.etag[ cacheURL ] = modified;
						}
					}
	
					// if no content
					if ( status === 204 || s.type === "HEAD" ) {
						statusText = "nocontent";
	
					// if not modified
					} else if ( status === 304 ) {
						statusText = "notmodified";
	
					// If we have data, let's convert it
					} else {
						statusText = response.state;
						success = response.data;
						error = response.error;
						isSuccess = !error;
					}
				} else {
	
					// We extract error from statusText
					// then normalize statusText and status for non-aborts
					error = statusText;
					if ( status || !statusText ) {
						statusText = "error";
						if ( status < 0 ) {
							status = 0;
						}
					}
				}
	
				// Set data for the fake xhr object
				jqXHR.status = status;
				jqXHR.statusText = ( nativeStatusText || statusText ) + "";
	
				// Success/Error
				if ( isSuccess ) {
					deferred.resolveWith( callbackContext, [ success, statusText, jqXHR ] );
				} else {
					deferred.rejectWith( callbackContext, [ jqXHR, statusText, error ] );
				}
	
				// Status-dependent callbacks
				jqXHR.statusCode( statusCode );
				statusCode = undefined;
	
				if ( fireGlobals ) {
					globalEventContext.trigger( isSuccess ? "ajaxSuccess" : "ajaxError",
						[ jqXHR, s, isSuccess ? success : error ] );
				}
	
				// Complete
				completeDeferred.fireWith( callbackContext, [ jqXHR, statusText ] );
	
				if ( fireGlobals ) {
					globalEventContext.trigger( "ajaxComplete", [ jqXHR, s ] );
	
					// Handle the global AJAX counter
					if ( !( --jQuery.active ) ) {
						jQuery.event.trigger( "ajaxStop" );
					}
				}
			}
	
			return jqXHR;
		},
	
		getJSON: function( url, data, callback ) {
			return jQuery.get( url, data, callback, "json" );
		},
	
		getScript: function( url, callback ) {
			return jQuery.get( url, undefined, callback, "script" );
		}
	} );
	
	jQuery.each( [ "get", "post" ], function( i, method ) {
		jQuery[ method ] = function( url, data, callback, type ) {
	
			// shift arguments if data argument was omitted
			if ( jQuery.isFunction( data ) ) {
				type = type || callback;
				callback = data;
				data = undefined;
			}
	
			// The url can be an options object (which then must have .url)
			return jQuery.ajax( jQuery.extend( {
				url: url,
				type: method,
				dataType: type,
				data: data,
				success: callback
			}, jQuery.isPlainObject( url ) && url ) );
		};
	} );
	
	
	jQuery._evalUrl = function( url ) {
		return jQuery.ajax( {
			url: url,
	
			// Make this explicit, since user can override this through ajaxSetup (#11264)
			type: "GET",
			dataType: "script",
			cache: true,
			async: false,
			global: false,
			"throws": true
		} );
	};
	
	
	jQuery.fn.extend( {
		wrapAll: function( html ) {
			if ( jQuery.isFunction( html ) ) {
				return this.each( function( i ) {
					jQuery( this ).wrapAll( html.call( this, i ) );
				} );
			}
	
			if ( this[ 0 ] ) {
	
				// The elements to wrap the target around
				var wrap = jQuery( html, this[ 0 ].ownerDocument ).eq( 0 ).clone( true );
	
				if ( this[ 0 ].parentNode ) {
					wrap.insertBefore( this[ 0 ] );
				}
	
				wrap.map( function() {
					var elem = this;
	
					while ( elem.firstChild && elem.firstChild.nodeType === 1 ) {
						elem = elem.firstChild;
					}
	
					return elem;
				} ).append( this );
			}
	
			return this;
		},
	
		wrapInner: function( html ) {
			if ( jQuery.isFunction( html ) ) {
				return this.each( function( i ) {
					jQuery( this ).wrapInner( html.call( this, i ) );
				} );
			}
	
			return this.each( function() {
				var self = jQuery( this ),
					contents = self.contents();
	
				if ( contents.length ) {
					contents.wrapAll( html );
	
				} else {
					self.append( html );
				}
			} );
		},
	
		wrap: function( html ) {
			var isFunction = jQuery.isFunction( html );
	
			return this.each( function( i ) {
				jQuery( this ).wrapAll( isFunction ? html.call( this, i ) : html );
			} );
		},
	
		unwrap: function() {
			return this.parent().each( function() {
				if ( !jQuery.nodeName( this, "body" ) ) {
					jQuery( this ).replaceWith( this.childNodes );
				}
			} ).end();
		}
	} );
	
	
	function getDisplay( elem ) {
		return elem.style && elem.style.display || jQuery.css( elem, "display" );
	}
	
	function filterHidden( elem ) {
	
		// Disconnected elements are considered hidden
		if ( !jQuery.contains( elem.ownerDocument || document, elem ) ) {
			return true;
		}
		while ( elem && elem.nodeType === 1 ) {
			if ( getDisplay( elem ) === "none" || elem.type === "hidden" ) {
				return true;
			}
			elem = elem.parentNode;
		}
		return false;
	}
	
	jQuery.expr.filters.hidden = function( elem ) {
	
		// Support: Opera <= 12.12
		// Opera reports offsetWidths and offsetHeights less than zero on some elements
		return support.reliableHiddenOffsets() ?
			( elem.offsetWidth <= 0 && elem.offsetHeight <= 0 &&
				!elem.getClientRects().length ) :
				filterHidden( elem );
	};
	
	jQuery.expr.filters.visible = function( elem ) {
		return !jQuery.expr.filters.hidden( elem );
	};
	
	
	
	
	var r20 = /%20/g,
		rbracket = /\[\]$/,
		rCRLF = /\r?\n/g,
		rsubmitterTypes = /^(?:submit|button|image|reset|file)$/i,
		rsubmittable = /^(?:input|select|textarea|keygen)/i;
	
	function buildParams( prefix, obj, traditional, add ) {
		var name;
	
		if ( jQuery.isArray( obj ) ) {
	
			// Serialize array item.
			jQuery.each( obj, function( i, v ) {
				if ( traditional || rbracket.test( prefix ) ) {
	
					// Treat each array item as a scalar.
					add( prefix, v );
	
				} else {
	
					// Item is non-scalar (array or object), encode its numeric index.
					buildParams(
						prefix + "[" + ( typeof v === "object" && v != null ? i : "" ) + "]",
						v,
						traditional,
						add
					);
				}
			} );
	
		} else if ( !traditional && jQuery.type( obj ) === "object" ) {
	
			// Serialize object item.
			for ( name in obj ) {
				buildParams( prefix + "[" + name + "]", obj[ name ], traditional, add );
			}
	
		} else {
	
			// Serialize scalar item.
			add( prefix, obj );
		}
	}
	
	// Serialize an array of form elements or a set of
	// key/values into a query string
	jQuery.param = function( a, traditional ) {
		var prefix,
			s = [],
			add = function( key, value ) {
	
				// If value is a function, invoke it and return its value
				value = jQuery.isFunction( value ) ? value() : ( value == null ? "" : value );
				s[ s.length ] = encodeURIComponent( key ) + "=" + encodeURIComponent( value );
			};
	
		// Set traditional to true for jQuery <= 1.3.2 behavior.
		if ( traditional === undefined ) {
			traditional = jQuery.ajaxSettings && jQuery.ajaxSettings.traditional;
		}
	
		// If an array was passed in, assume that it is an array of form elements.
		if ( jQuery.isArray( a ) || ( a.jquery && !jQuery.isPlainObject( a ) ) ) {
	
			// Serialize the form elements
			jQuery.each( a, function() {
				add( this.name, this.value );
			} );
	
		} else {
	
			// If traditional, encode the "old" way (the way 1.3.2 or older
			// did it), otherwise encode params recursively.
			for ( prefix in a ) {
				buildParams( prefix, a[ prefix ], traditional, add );
			}
		}
	
		// Return the resulting serialization
		return s.join( "&" ).replace( r20, "+" );
	};
	
	jQuery.fn.extend( {
		serialize: function() {
			return jQuery.param( this.serializeArray() );
		},
		serializeArray: function() {
			return this.map( function() {
	
				// Can add propHook for "elements" to filter or add form elements
				var elements = jQuery.prop( this, "elements" );
				return elements ? jQuery.makeArray( elements ) : this;
			} )
			.filter( function() {
				var type = this.type;
	
				// Use .is(":disabled") so that fieldset[disabled] works
				return this.name && !jQuery( this ).is( ":disabled" ) &&
					rsubmittable.test( this.nodeName ) && !rsubmitterTypes.test( type ) &&
					( this.checked || !rcheckableType.test( type ) );
			} )
			.map( function( i, elem ) {
				var val = jQuery( this ).val();
	
				return val == null ?
					null :
					jQuery.isArray( val ) ?
						jQuery.map( val, function( val ) {
							return { name: elem.name, value: val.replace( rCRLF, "\r\n" ) };
						} ) :
						{ name: elem.name, value: val.replace( rCRLF, "\r\n" ) };
			} ).get();
		}
	} );
	
	
	// Create the request object
	// (This is still attached to ajaxSettings for backward compatibility)
	jQuery.ajaxSettings.xhr = window.ActiveXObject !== undefined ?
	
		// Support: IE6-IE8
		function() {
	
			// XHR cannot access local files, always use ActiveX for that case
			if ( this.isLocal ) {
				return createActiveXHR();
			}
	
			// Support: IE 9-11
			// IE seems to error on cross-domain PATCH requests when ActiveX XHR
			// is used. In IE 9+ always use the native XHR.
			// Note: this condition won't catch Edge as it doesn't define
			// document.documentMode but it also doesn't support ActiveX so it won't
			// reach this code.
			if ( document.documentMode > 8 ) {
				return createStandardXHR();
			}
	
			// Support: IE<9
			// oldIE XHR does not support non-RFC2616 methods (#13240)
			// See http://msdn.microsoft.com/en-us/library/ie/ms536648(v=vs.85).aspx
			// and http://www.w3.org/Protocols/rfc2616/rfc2616-sec9.html#sec9
			// Although this check for six methods instead of eight
			// since IE also does not support "trace" and "connect"
			return /^(get|post|head|put|delete|options)$/i.test( this.type ) &&
				createStandardXHR() || createActiveXHR();
		} :
	
		// For all other browsers, use the standard XMLHttpRequest object
		createStandardXHR;
	
	var xhrId = 0,
		xhrCallbacks = {},
		xhrSupported = jQuery.ajaxSettings.xhr();
	
	// Support: IE<10
	// Open requests must be manually aborted on unload (#5280)
	// See https://support.microsoft.com/kb/2856746 for more info
	if ( window.attachEvent ) {
		window.attachEvent( "onunload", function() {
			for ( var key in xhrCallbacks ) {
				xhrCallbacks[ key ]( undefined, true );
			}
		} );
	}
	
	// Determine support properties
	support.cors = !!xhrSupported && ( "withCredentials" in xhrSupported );
	xhrSupported = support.ajax = !!xhrSupported;
	
	// Create transport if the browser can provide an xhr
	if ( xhrSupported ) {
	
		jQuery.ajaxTransport( function( options ) {
	
			// Cross domain only allowed if supported through XMLHttpRequest
			if ( !options.crossDomain || support.cors ) {
	
				var callback;
	
				return {
					send: function( headers, complete ) {
						var i,
							xhr = options.xhr(),
							id = ++xhrId;
	
						// Open the socket
						xhr.open(
							options.type,
							options.url,
							options.async,
							options.username,
							options.password
						);
	
						// Apply custom fields if provided
						if ( options.xhrFields ) {
							for ( i in options.xhrFields ) {
								xhr[ i ] = options.xhrFields[ i ];
							}
						}
	
						// Override mime type if needed
						if ( options.mimeType && xhr.overrideMimeType ) {
							xhr.overrideMimeType( options.mimeType );
						}
	
						// X-Requested-With header
						// For cross-domain requests, seeing as conditions for a preflight are
						// akin to a jigsaw puzzle, we simply never set it to be sure.
						// (it can always be set on a per-request basis or even using ajaxSetup)
						// For same-domain requests, won't change header if already provided.
						if ( !options.crossDomain && !headers[ "X-Requested-With" ] ) {
							headers[ "X-Requested-With" ] = "XMLHttpRequest";
						}
	
						// Set headers
						for ( i in headers ) {
	
							// Support: IE<9
							// IE's ActiveXObject throws a 'Type Mismatch' exception when setting
							// request header to a null-value.
							//
							// To keep consistent with other XHR implementations, cast the value
							// to string and ignore `undefined`.
							if ( headers[ i ] !== undefined ) {
								xhr.setRequestHeader( i, headers[ i ] + "" );
							}
						}
	
						// Do send the request
						// This may raise an exception which is actually
						// handled in jQuery.ajax (so no try/catch here)
						xhr.send( ( options.hasContent && options.data ) || null );
	
						// Listener
						callback = function( _, isAbort ) {
							var status, statusText, responses;
	
							// Was never called and is aborted or complete
							if ( callback && ( isAbort || xhr.readyState === 4 ) ) {
	
								// Clean up
								delete xhrCallbacks[ id ];
								callback = undefined;
								xhr.onreadystatechange = jQuery.noop;
	
								// Abort manually if needed
								if ( isAbort ) {
									if ( xhr.readyState !== 4 ) {
										xhr.abort();
									}
								} else {
									responses = {};
									status = xhr.status;
	
									// Support: IE<10
									// Accessing binary-data responseText throws an exception
									// (#11426)
									if ( typeof xhr.responseText === "string" ) {
										responses.text = xhr.responseText;
									}
	
									// Firefox throws an exception when accessing
									// statusText for faulty cross-domain requests
									try {
										statusText = xhr.statusText;
									} catch ( e ) {
	
										// We normalize with Webkit giving an empty statusText
										statusText = "";
									}
	
									// Filter status for non standard behaviors
	
									// If the request is local and we have data: assume a success
									// (success with no data won't get notified, that's the best we
									// can do given current implementations)
									if ( !status && options.isLocal && !options.crossDomain ) {
										status = responses.text ? 200 : 404;
	
									// IE - #1450: sometimes returns 1223 when it should be 204
									} else if ( status === 1223 ) {
										status = 204;
									}
								}
							}
	
							// Call complete if needed
							if ( responses ) {
								complete( status, statusText, responses, xhr.getAllResponseHeaders() );
							}
						};
	
						// Do send the request
						// `xhr.send` may raise an exception, but it will be
						// handled in jQuery.ajax (so no try/catch here)
						if ( !options.async ) {
	
							// If we're in sync mode we fire the callback
							callback();
						} else if ( xhr.readyState === 4 ) {
	
							// (IE6 & IE7) if it's in cache and has been
							// retrieved directly we need to fire the callback
							window.setTimeout( callback );
						} else {
	
							// Register the callback, but delay it in case `xhr.send` throws
							// Add to the list of active xhr callbacks
							xhr.onreadystatechange = xhrCallbacks[ id ] = callback;
						}
					},
	
					abort: function() {
						if ( callback ) {
							callback( undefined, true );
						}
					}
				};
			}
		} );
	}
	
	// Functions to create xhrs
	function createStandardXHR() {
		try {
			return new window.XMLHttpRequest();
		} catch ( e ) {}
	}
	
	function createActiveXHR() {
		try {
			return new window.ActiveXObject( "Microsoft.XMLHTTP" );
		} catch ( e ) {}
	}
	
	
	
	
	// Install script dataType
	jQuery.ajaxSetup( {
		accepts: {
			script: "text/javascript, application/javascript, " +
				"application/ecmascript, application/x-ecmascript"
		},
		contents: {
			script: /\b(?:java|ecma)script\b/
		},
		converters: {
			"text script": function( text ) {
				jQuery.globalEval( text );
				return text;
			}
		}
	} );
	
	// Handle cache's special case and global
	jQuery.ajaxPrefilter( "script", function( s ) {
		if ( s.cache === undefined ) {
			s.cache = false;
		}
		if ( s.crossDomain ) {
			s.type = "GET";
			s.global = false;
		}
	} );
	
	// Bind script tag hack transport
	jQuery.ajaxTransport( "script", function( s ) {
	
		// This transport only deals with cross domain requests
		if ( s.crossDomain ) {
	
			var script,
				head = document.head || jQuery( "head" )[ 0 ] || document.documentElement;
	
			return {
	
				send: function( _, callback ) {
	
					script = document.createElement( "script" );
	
					script.async = true;
	
					if ( s.scriptCharset ) {
						script.charset = s.scriptCharset;
					}
	
					script.src = s.url;
	
					// Attach handlers for all browsers
					script.onload = script.onreadystatechange = function( _, isAbort ) {
	
						if ( isAbort || !script.readyState || /loaded|complete/.test( script.readyState ) ) {
	
							// Handle memory leak in IE
							script.onload = script.onreadystatechange = null;
	
							// Remove the script
							if ( script.parentNode ) {
								script.parentNode.removeChild( script );
							}
	
							// Dereference the script
							script = null;
	
							// Callback if not abort
							if ( !isAbort ) {
								callback( 200, "success" );
							}
						}
					};
	
					// Circumvent IE6 bugs with base elements (#2709 and #4378) by prepending
					// Use native DOM manipulation to avoid our domManip AJAX trickery
					head.insertBefore( script, head.firstChild );
				},
	
				abort: function() {
					if ( script ) {
						script.onload( undefined, true );
					}
				}
			};
		}
	} );
	
	
	
	
	var oldCallbacks = [],
		rjsonp = /(=)\?(?=&|$)|\?\?/;
	
	// Default jsonp settings
	jQuery.ajaxSetup( {
		jsonp: "callback",
		jsonpCallback: function() {
			var callback = oldCallbacks.pop() || ( jQuery.expando + "_" + ( nonce++ ) );
			this[ callback ] = true;
			return callback;
		}
	} );
	
	// Detect, normalize options and install callbacks for jsonp requests
	jQuery.ajaxPrefilter( "json jsonp", function( s, originalSettings, jqXHR ) {
	
		var callbackName, overwritten, responseContainer,
			jsonProp = s.jsonp !== false && ( rjsonp.test( s.url ) ?
				"url" :
				typeof s.data === "string" &&
					( s.contentType || "" )
						.indexOf( "application/x-www-form-urlencoded" ) === 0 &&
					rjsonp.test( s.data ) && "data"
			);
	
		// Handle iff the expected data type is "jsonp" or we have a parameter to set
		if ( jsonProp || s.dataTypes[ 0 ] === "jsonp" ) {
	
			// Get callback name, remembering preexisting value associated with it
			callbackName = s.jsonpCallback = jQuery.isFunction( s.jsonpCallback ) ?
				s.jsonpCallback() :
				s.jsonpCallback;
	
			// Insert callback into url or form data
			if ( jsonProp ) {
				s[ jsonProp ] = s[ jsonProp ].replace( rjsonp, "$1" + callbackName );
			} else if ( s.jsonp !== false ) {
				s.url += ( rquery.test( s.url ) ? "&" : "?" ) + s.jsonp + "=" + callbackName;
			}
	
			// Use data converter to retrieve json after script execution
			s.converters[ "script json" ] = function() {
				if ( !responseContainer ) {
					jQuery.error( callbackName + " was not called" );
				}
				return responseContainer[ 0 ];
			};
	
			// force json dataType
			s.dataTypes[ 0 ] = "json";
	
			// Install callback
			overwritten = window[ callbackName ];
			window[ callbackName ] = function() {
				responseContainer = arguments;
			};
	
			// Clean-up function (fires after converters)
			jqXHR.always( function() {
	
				// If previous value didn't exist - remove it
				if ( overwritten === undefined ) {
					jQuery( window ).removeProp( callbackName );
	
				// Otherwise restore preexisting value
				} else {
					window[ callbackName ] = overwritten;
				}
	
				// Save back as free
				if ( s[ callbackName ] ) {
	
					// make sure that re-using the options doesn't screw things around
					s.jsonpCallback = originalSettings.jsonpCallback;
	
					// save the callback name for future use
					oldCallbacks.push( callbackName );
				}
	
				// Call if it was a function and we have a response
				if ( responseContainer && jQuery.isFunction( overwritten ) ) {
					overwritten( responseContainer[ 0 ] );
				}
	
				responseContainer = overwritten = undefined;
			} );
	
			// Delegate to script
			return "script";
		}
	} );
	
	
	
	
	// data: string of html
	// context (optional): If specified, the fragment will be created in this context,
	// defaults to document
	// keepScripts (optional): If true, will include scripts passed in the html string
	jQuery.parseHTML = function( data, context, keepScripts ) {
		if ( !data || typeof data !== "string" ) {
			return null;
		}
		if ( typeof context === "boolean" ) {
			keepScripts = context;
			context = false;
		}
		context = context || document;
	
		var parsed = rsingleTag.exec( data ),
			scripts = !keepScripts && [];
	
		// Single tag
		if ( parsed ) {
			return [ context.createElement( parsed[ 1 ] ) ];
		}
	
		parsed = buildFragment( [ data ], context, scripts );
	
		if ( scripts && scripts.length ) {
			jQuery( scripts ).remove();
		}
	
		return jQuery.merge( [], parsed.childNodes );
	};
	
	
	// Keep a copy of the old load method
	var _load = jQuery.fn.load;
	
	/**
	 * Load a url into a page
	 */
	jQuery.fn.load = function( url, params, callback ) {
		if ( typeof url !== "string" && _load ) {
			return _load.apply( this, arguments );
		}
	
		var selector, type, response,
			self = this,
			off = url.indexOf( " " );
	
		if ( off > -1 ) {
			selector = jQuery.trim( url.slice( off, url.length ) );
			url = url.slice( 0, off );
		}
	
		// If it's a function
		if ( jQuery.isFunction( params ) ) {
	
			// We assume that it's the callback
			callback = params;
			params = undefined;
	
		// Otherwise, build a param string
		} else if ( params && typeof params === "object" ) {
			type = "POST";
		}
	
		// If we have elements to modify, make the request
		if ( self.length > 0 ) {
			jQuery.ajax( {
				url: url,
	
				// If "type" variable is undefined, then "GET" method will be used.
				// Make value of this field explicit since
				// user can override it through ajaxSetup method
				type: type || "GET",
				dataType: "html",
				data: params
			} ).done( function( responseText ) {
	
				// Save response for use in complete callback
				response = arguments;
	
				self.html( selector ?
	
					// If a selector was specified, locate the right elements in a dummy div
					// Exclude scripts to avoid IE 'Permission Denied' errors
					jQuery( "<div>" ).append( jQuery.parseHTML( responseText ) ).find( selector ) :
	
					// Otherwise use the full result
					responseText );
	
			// If the request succeeds, this function gets "data", "status", "jqXHR"
			// but they are ignored because response was set above.
			// If it fails, this function gets "jqXHR", "status", "error"
			} ).always( callback && function( jqXHR, status ) {
				self.each( function() {
					callback.apply( this, response || [ jqXHR.responseText, status, jqXHR ] );
				} );
			} );
		}
	
		return this;
	};
	
	
	
	
	// Attach a bunch of functions for handling common AJAX events
	jQuery.each( [
		"ajaxStart",
		"ajaxStop",
		"ajaxComplete",
		"ajaxError",
		"ajaxSuccess",
		"ajaxSend"
	], function( i, type ) {
		jQuery.fn[ type ] = function( fn ) {
			return this.on( type, fn );
		};
	} );
	
	
	
	
	jQuery.expr.filters.animated = function( elem ) {
		return jQuery.grep( jQuery.timers, function( fn ) {
			return elem === fn.elem;
		} ).length;
	};
	
	
	
	
	
	/**
	 * Gets a window from an element
	 */
	function getWindow( elem ) {
		return jQuery.isWindow( elem ) ?
			elem :
			elem.nodeType === 9 ?
				elem.defaultView || elem.parentWindow :
				false;
	}
	
	jQuery.offset = {
		setOffset: function( elem, options, i ) {
			var curPosition, curLeft, curCSSTop, curTop, curOffset, curCSSLeft, calculatePosition,
				position = jQuery.css( elem, "position" ),
				curElem = jQuery( elem ),
				props = {};
	
			// set position first, in-case top/left are set even on static elem
			if ( position === "static" ) {
				elem.style.position = "relative";
			}
	
			curOffset = curElem.offset();
			curCSSTop = jQuery.css( elem, "top" );
			curCSSLeft = jQuery.css( elem, "left" );
			calculatePosition = ( position === "absolute" || position === "fixed" ) &&
				jQuery.inArray( "auto", [ curCSSTop, curCSSLeft ] ) > -1;
	
			// need to be able to calculate position if either top or left
			// is auto and position is either absolute or fixed
			if ( calculatePosition ) {
				curPosition = curElem.position();
				curTop = curPosition.top;
				curLeft = curPosition.left;
			} else {
				curTop = parseFloat( curCSSTop ) || 0;
				curLeft = parseFloat( curCSSLeft ) || 0;
			}
	
			if ( jQuery.isFunction( options ) ) {
	
				// Use jQuery.extend here to allow modification of coordinates argument (gh-1848)
				options = options.call( elem, i, jQuery.extend( {}, curOffset ) );
			}
	
			if ( options.top != null ) {
				props.top = ( options.top - curOffset.top ) + curTop;
			}
			if ( options.left != null ) {
				props.left = ( options.left - curOffset.left ) + curLeft;
			}
	
			if ( "using" in options ) {
				options.using.call( elem, props );
			} else {
				curElem.css( props );
			}
		}
	};
	
	jQuery.fn.extend( {
		offset: function( options ) {
			if ( arguments.length ) {
				return options === undefined ?
					this :
					this.each( function( i ) {
						jQuery.offset.setOffset( this, options, i );
					} );
			}
	
			var docElem, win,
				box = { top: 0, left: 0 },
				elem = this[ 0 ],
				doc = elem && elem.ownerDocument;
	
			if ( !doc ) {
				return;
			}
	
			docElem = doc.documentElement;
	
			// Make sure it's not a disconnected DOM node
			if ( !jQuery.contains( docElem, elem ) ) {
				return box;
			}
	
			// If we don't have gBCR, just use 0,0 rather than error
			// BlackBerry 5, iOS 3 (original iPhone)
			if ( typeof elem.getBoundingClientRect !== "undefined" ) {
				box = elem.getBoundingClientRect();
			}
			win = getWindow( doc );
			return {
				top: box.top  + ( win.pageYOffset || docElem.scrollTop )  - ( docElem.clientTop  || 0 ),
				left: box.left + ( win.pageXOffset || docElem.scrollLeft ) - ( docElem.clientLeft || 0 )
			};
		},
	
		position: function() {
			if ( !this[ 0 ] ) {
				return;
			}
	
			var offsetParent, offset,
				parentOffset = { top: 0, left: 0 },
				elem = this[ 0 ];
	
			// Fixed elements are offset from window (parentOffset = {top:0, left: 0},
			// because it is its only offset parent
			if ( jQuery.css( elem, "position" ) === "fixed" ) {
	
				// we assume that getBoundingClientRect is available when computed position is fixed
				offset = elem.getBoundingClientRect();
			} else {
	
				// Get *real* offsetParent
				offsetParent = this.offsetParent();
	
				// Get correct offsets
				offset = this.offset();
				if ( !jQuery.nodeName( offsetParent[ 0 ], "html" ) ) {
					parentOffset = offsetParent.offset();
				}
	
				// Add offsetParent borders
				parentOffset.top  += jQuery.css( offsetParent[ 0 ], "borderTopWidth", true );
				parentOffset.left += jQuery.css( offsetParent[ 0 ], "borderLeftWidth", true );
			}
	
			// Subtract parent offsets and element margins
			// note: when an element has margin: auto the offsetLeft and marginLeft
			// are the same in Safari causing offset.left to incorrectly be 0
			return {
				top:  offset.top  - parentOffset.top - jQuery.css( elem, "marginTop", true ),
				left: offset.left - parentOffset.left - jQuery.css( elem, "marginLeft", true )
			};
		},
	
		offsetParent: function() {
			return this.map( function() {
				var offsetParent = this.offsetParent;
	
				while ( offsetParent && ( !jQuery.nodeName( offsetParent, "html" ) &&
					jQuery.css( offsetParent, "position" ) === "static" ) ) {
					offsetParent = offsetParent.offsetParent;
				}
				return offsetParent || documentElement;
			} );
		}
	} );
	
	// Create scrollLeft and scrollTop methods
	jQuery.each( { scrollLeft: "pageXOffset", scrollTop: "pageYOffset" }, function( method, prop ) {
		var top = /Y/.test( prop );
	
		jQuery.fn[ method ] = function( val ) {
			return access( this, function( elem, method, val ) {
				var win = getWindow( elem );
	
				if ( val === undefined ) {
					return win ? ( prop in win ) ? win[ prop ] :
						win.document.documentElement[ method ] :
						elem[ method ];
				}
	
				if ( win ) {
					win.scrollTo(
						!top ? val : jQuery( win ).scrollLeft(),
						top ? val : jQuery( win ).scrollTop()
					);
	
				} else {
					elem[ method ] = val;
				}
			}, method, val, arguments.length, null );
		};
	} );
	
	// Support: Safari<7-8+, Chrome<37-44+
	// Add the top/left cssHooks using jQuery.fn.position
	// Webkit bug: https://bugs.webkit.org/show_bug.cgi?id=29084
	// getComputedStyle returns percent when specified for top/left/bottom/right
	// rather than make the css module depend on the offset module, we just check for it here
	jQuery.each( [ "top", "left" ], function( i, prop ) {
		jQuery.cssHooks[ prop ] = addGetHookIf( support.pixelPosition,
			function( elem, computed ) {
				if ( computed ) {
					computed = curCSS( elem, prop );
	
					// if curCSS returns percentage, fallback to offset
					return rnumnonpx.test( computed ) ?
						jQuery( elem ).position()[ prop ] + "px" :
						computed;
				}
			}
		);
	} );
	
	
	// Create innerHeight, innerWidth, height, width, outerHeight and outerWidth methods
	jQuery.each( { Height: "height", Width: "width" }, function( name, type ) {
		jQuery.each( { padding: "inner" + name, content: type, "": "outer" + name },
		function( defaultExtra, funcName ) {
	
			// margin is only for outerHeight, outerWidth
			jQuery.fn[ funcName ] = function( margin, value ) {
				var chainable = arguments.length && ( defaultExtra || typeof margin !== "boolean" ),
					extra = defaultExtra || ( margin === true || value === true ? "margin" : "border" );
	
				return access( this, function( elem, type, value ) {
					var doc;
	
					if ( jQuery.isWindow( elem ) ) {
	
						// As of 5/8/2012 this will yield incorrect results for Mobile Safari, but there
						// isn't a whole lot we can do. See pull request at this URL for discussion:
						// https://github.com/jquery/jquery/pull/764
						return elem.document.documentElement[ "client" + name ];
					}
	
					// Get document width or height
					if ( elem.nodeType === 9 ) {
						doc = elem.documentElement;
	
						// Either scroll[Width/Height] or offset[Width/Height] or client[Width/Height],
						// whichever is greatest
						// unfortunately, this causes bug #3838 in IE6/8 only,
						// but there is currently no good, small way to fix it.
						return Math.max(
							elem.body[ "scroll" + name ], doc[ "scroll" + name ],
							elem.body[ "offset" + name ], doc[ "offset" + name ],
							doc[ "client" + name ]
						);
					}
	
					return value === undefined ?
	
						// Get width or height on the element, requesting but not forcing parseFloat
						jQuery.css( elem, type, extra ) :
	
						// Set width or height on the element
						jQuery.style( elem, type, value, extra );
				}, type, chainable ? margin : undefined, chainable, null );
			};
		} );
	} );
	
	
	jQuery.fn.extend( {
	
		bind: function( types, data, fn ) {
			return this.on( types, null, data, fn );
		},
		unbind: function( types, fn ) {
			return this.off( types, null, fn );
		},
	
		delegate: function( selector, types, data, fn ) {
			return this.on( types, selector, data, fn );
		},
		undelegate: function( selector, types, fn ) {
	
			// ( namespace ) or ( selector, types [, fn] )
			return arguments.length === 1 ?
				this.off( selector, "**" ) :
				this.off( types, selector || "**", fn );
		}
	} );
	
	// The number of elements contained in the matched element set
	jQuery.fn.size = function() {
		return this.length;
	};
	
	jQuery.fn.andSelf = jQuery.fn.addBack;
	
	
	
	
	// Register as a named AMD module, since jQuery can be concatenated with other
	// files that may use define, but not via a proper concatenation script that
	// understands anonymous AMD modules. A named AMD is safest and most robust
	// way to register. Lowercase jquery is used because AMD module names are
	// derived from file names, and jQuery is normally delivered in a lowercase
	// file name. Do this after creating the global so that if an AMD module wants
	// to call noConflict to hide this version of jQuery, it will work.
	
	// Note that for maximum portability, libraries that are not jQuery should
	// declare themselves as anonymous modules, and avoid setting a global if an
	// AMD loader is present. jQuery is a special case. For more information, see
	// https://github.com/jrburke/requirejs/wiki/Updating-existing-libraries#wiki-anon
	
	if ( true ) {
		!(__WEBPACK_AMD_DEFINE_ARRAY__ = [], __WEBPACK_AMD_DEFINE_RESULT__ = function() {
			return jQuery;
		}.apply(exports, __WEBPACK_AMD_DEFINE_ARRAY__), __WEBPACK_AMD_DEFINE_RESULT__ !== undefined && (module.exports = __WEBPACK_AMD_DEFINE_RESULT__));
	}
	
	
	
	var
	
		// Map over jQuery in case of overwrite
		_jQuery = window.jQuery,
	
		// Map over the $ in case of overwrite
		_$ = window.$;
	
	jQuery.noConflict = function( deep ) {
		if ( window.$ === jQuery ) {
			window.$ = _$;
		}
	
		if ( deep && window.jQuery === jQuery ) {
			window.jQuery = _jQuery;
		}
	
		return jQuery;
	};
	
	// Expose jQuery and $ identifiers, even in
	// AMD (#7102#comment:10, https://github.com/jquery/jquery/pull/557)
	// and CommonJS for browser emulators (#13566)
	if ( !noGlobal ) {
		window.jQuery = window.$ = jQuery;
	}
	
	return jQuery;
	}));


/***/ },
/* 3 */
/*!**********************************!*\
  !*** ./~/avalon2/dist/avalon.js ***!
  \**********************************/
/***/ function(module, exports, __webpack_require__) {

	/* WEBPACK VAR INJECTION */(function(global) {/*!
	built in 2017-1-4:13:4 version 2.2.4 by 司徒正美
	https://github.com/RubyLouvre/avalon/tree/2.2.3
	
	修正IE下 orderBy BUG
	更改下载Promise的提示
	修复avalon.modern 在Proxy 模式下使用ms-for 循环对象时出错的BUG
	修复effect内部传参 BUG
	重构ms-validate的绑定事件的机制
	
	*/(function (global, factory) {
	     true ? module.exports = factory() : typeof define === 'function' && define.amd ? define(factory) : global.avalon = factory();
	})(this, function () {
	    'use strict';
	
	    var win = typeof window === 'object' ? window : typeof global === 'object' ? global : {};
	
	    var inBrowser = !!win.location && win.navigator;
	    /* istanbul ignore if  */
	
	    var document$1 = inBrowser ? win.document : {
	        createElement: Object,
	        createElementNS: Object,
	        documentElement: 'xx',
	        contains: Boolean
	    };
	    var root = inBrowser ? document$1.documentElement : {
	        outerHTML: 'x'
	    };
	
	    var versions = {
	        objectobject: 7, //IE7-8
	        objectundefined: 6, //IE6
	        undefinedfunction: NaN, // other modern browsers
	        undefinedobject: NaN };
	    /* istanbul ignore next  */
	    var msie = document$1.documentMode || versions[typeof document$1.all + typeof XMLHttpRequest];
	
	    var modern = /NaN|undefined/.test(msie) || msie > 8;
	
	    /*
	     https://github.com/rsms/js-lru
	     entry             entry             entry             entry        
	     ______            ______            ______            ______       
	     | head |.newer => |      |.newer => |      |.newer => | tail |      
	     |  A   |          |  B   |          |  C   |          |  D   |      
	     |______| <= older.|______| <= older.|______| <= older.|______|      
	     
	     removed  <--  <--  <--  <--  <--  <--  <--  <--  <--  <--  <--  added 
	     */
	    function Cache(maxLength) {
	        // 标识当前缓存数组的大小
	        this.size = 0;
	        // 标识缓存数组能达到的最大长度
	        this.limit = maxLength;
	        //  head（最不常用的项），tail（最常用的项）全部初始化为undefined
	
	        this.head = this.tail = void 0;
	        this._keymap = {};
	    }
	
	    Cache.prototype = {
	        put: function put(key, value) {
	            var entry = {
	                key: key,
	                value: value
	            };
	            this._keymap[key] = entry;
	            if (this.tail) {
	                // 如果存在tail（缓存数组的长度不为0），将tail指向新的 entry
	                this.tail.newer = entry;
	                entry.older = this.tail;
	            } else {
	                // 如果缓存数组的长度为0，将head指向新的entry
	                this.head = entry;
	            }
	            this.tail = entry;
	            // 如果缓存数组达到上限，则先删除 head 指向的缓存对象
	            /* istanbul ignore if */
	            if (this.size === this.limit) {
	                this.shift();
	            } else {
	                this.size++;
	            }
	            return value;
	        },
	        shift: function shift() {
	            /* istanbul ignore next */
	            var entry = this.head;
	            /* istanbul ignore if */
	            if (entry) {
	                // 删除 head ，并改变指向
	                this.head = this.head.newer;
	                // 同步更新 _keymap 里面的属性值
	                this.head.older = entry.newer = entry.older = this._keymap[entry.key] = void 0;
	                delete this._keymap[entry.key]; //#1029
	                // 同步更新 缓存数组的长度
	                this.size--;
	            }
	        },
	        get: function get(key) {
	            var entry = this._keymap[key];
	            // 如果查找不到含有`key`这个属性的缓存对象
	            if (entry === void 0) return;
	            // 如果查找到的缓存对象已经是 tail (最近使用过的)
	            /* istanbul ignore if */
	            if (entry === this.tail) {
	                return entry.value;
	            }
	            // HEAD--------------TAIL
	            //   <.older   .newer>
	            //  <--- add direction --
	            //   A  B  C  <D>  E
	            if (entry.newer) {
	                // 处理 newer 指向
	                if (entry === this.head) {
	                    // 如果查找到的缓存对象是 head (最近最少使用过的)
	                    // 则将 head 指向原 head 的 newer 所指向的缓存对象
	                    this.head = entry.newer;
	                }
	                // 将所查找的缓存对象的下一级的 older 指向所查找的缓存对象的older所指向的值
	                // 例如：A B C D E
	                // 如果查找到的是D，那么将E指向C，不再指向D
	                entry.newer.older = entry.older; // C <-- E.
	            }
	            if (entry.older) {
	                // 处理 older 指向
	                // 如果查找到的是D，那么C指向E，不再指向D
	                entry.older.newer = entry.newer; // C. --> E
	            }
	            // 处理所查找到的对象的 newer 以及 older 指向
	            entry.newer = void 0; // D --x
	            // older指向之前使用过的变量，即D指向E
	            entry.older = this.tail; // D. --> E
	            if (this.tail) {
	                // 将E的newer指向D
	                this.tail.newer = entry; // E. <-- D
	            }
	            // 改变 tail 为D 
	            this.tail = entry;
	            return entry.value;
	        }
	    };
	
	    var delayCompile = {};
	
	    var directives = {};
	
	    function directive(name, opts) {
	        if (directives[name]) {
	            avalon.warn(name, 'directive have defined! ');
	        }
	        directives[name] = opts;
	        if (!opts.update) {
	            opts.update = function () {};
	        }
	        if (opts.delay) {
	            delayCompile[name] = 1;
	        }
	        return opts;
	    }
	
	    function delayCompileNodes(dirs) {
	        for (var i in delayCompile) {
	            if ('ms-' + i in dirs) {
	                return true;
	            }
	        }
	    }
	
	    var window$1 = win;
	    function avalon(el) {
	        return new avalon.init(el);
	    }
	
	    avalon.init = function (el) {
	        this[0] = this.element = el;
	    };
	
	    avalon.fn = avalon.prototype = avalon.init.prototype;
	
	    function shadowCopy(destination, source) {
	        for (var property in source) {
	            destination[property] = source[property];
	        }
	        return destination;
	    }
	    var rword = /[^, ]+/g;
	    var rnowhite = /\S+/g; //存在非空字符
	    var platform = {}; //用于放置平台差异的方法与属性
	
	
	    function oneObject(array, val) {
	        if (typeof array === 'string') {
	            array = array.match(rword) || [];
	        }
	        var result = {},
	            value = val !== void 0 ? val : 1;
	        for (var i = 0, n = array.length; i < n; i++) {
	            result[array[i]] = value;
	        }
	        return result;
	    }
	
	    var op = Object.prototype;
	    function quote(str) {
	        return avalon._quote(str);
	    }
	    var inspect = op.toString;
	    var ohasOwn = op.hasOwnProperty;
	    var ap = Array.prototype;
	
	    var hasConsole = typeof console === 'object';
	    avalon.config = { debug: true };
	    function log() {
	        if (hasConsole && avalon.config.debug) {
	            Function.apply.call(console.log, console, arguments);
	        }
	    }
	    function warn() {
	        if (hasConsole && avalon.config.debug) {
	            var method = console.warn || console.log;
	            // http://qiang106.iteye.com/blog/1721425
	            Function.apply.call(method, console, arguments);
	        }
	    }
	    function error(str, e) {
	        throw (e || Error)(str);
	    }
	    function noop() {}
	    function isObject(a) {
	        return a !== null && typeof a === 'object';
	    }
	
	    function range(start, end, step) {
	        // 用于生成整数数组
	        step || (step = 1);
	        if (end == null) {
	            end = start || 0;
	            start = 0;
	        }
	        var index = -1,
	            length = Math.max(0, Math.ceil((end - start) / step)),
	            result = new Array(length);
	        while (++index < length) {
	            result[index] = start;
	            start += step;
	        }
	        return result;
	    }
	
	    var rhyphen = /([a-z\d])([A-Z]+)/g;
	    function hyphen(target) {
	        //转换为连字符线风格
	        return target.replace(rhyphen, '$1-$2').toLowerCase();
	    }
	
	    var rcamelize = /[-_][^-_]/g;
	    function camelize(target) {
	        //提前判断，提高getStyle等的效率
	        if (!target || target.indexOf('-') < 0 && target.indexOf('_') < 0) {
	            return target;
	        }
	        //转换为驼峰风格
	        return target.replace(rcamelize, function (match) {
	            return match.charAt(1).toUpperCase();
	        });
	    }
	
	    var _slice = ap.slice;
	    function slice(nodes, start, end) {
	        return _slice.call(nodes, start, end);
	    }
	
	    var rhashcode = /\d\.\d{4}/;
	    //生成UUID http://stackoverflow.com/questions/105034/how-to-create-a-guid-uuid-in-javascript
	    function makeHashCode(prefix) {
	        /* istanbul ignore next*/
	        prefix = prefix || 'avalon';
	        /* istanbul ignore next*/
	        return String(Math.random() + Math.random()).replace(rhashcode, prefix);
	    }
	    //生成事件回调的UUID(用户通过ms-on指令)
	    function getLongID(fn) {
	        /* istanbul ignore next */
	        return fn.uuid || (fn.uuid = makeHashCode('e'));
	    }
	    var UUID = 1;
	    //生成事件回调的UUID(用户通过avalon.bind)
	    function getShortID(fn) {
	        /* istanbul ignore next */
	        return fn.uuid || (fn.uuid = '_' + ++UUID);
	    }
	
	    var rescape = /[-.*+?^${}()|[\]\/\\]/g;
	    function escapeRegExp(target) {
	        //http://stevenlevithan.com/regex/xregexp/
	        //将字符串安全格式化为正则表达式的源码
	        return (target + '').replace(rescape, '\\$&');
	    }
	
	    var eventHooks = {};
	    var eventListeners = {};
	    var validators = {};
	    var cssHooks = {};
	
	    window$1.avalon = avalon;
	
	    function createFragment() {
	        /* istanbul ignore next  */
	        return document$1.createDocumentFragment();
	    }
	
	    var rentities = /&[a-z0-9#]{2,10};/;
	    var temp = document$1.createElement('div');
	    shadowCopy(avalon, {
	        Array: {
	            merge: function merge(target, other) {
	                //合并两个数组 avalon2新增
	                target.push.apply(target, other);
	            },
	            ensure: function ensure(target, item) {
	                //只有当前数组不存在此元素时只添加它
	                if (target.indexOf(item) === -1) {
	                    return target.push(item);
	                }
	            },
	            removeAt: function removeAt(target, index) {
	                //移除数组中指定位置的元素，返回布尔表示成功与否
	                return !!target.splice(index, 1).length;
	            },
	            remove: function remove(target, item) {
	                //移除数组中第一个匹配传参的那个元素，返回布尔表示成功与否
	                var index = target.indexOf(item);
	                if (~index) return avalon.Array.removeAt(target, index);
	                return false;
	            }
	        },
	        evaluatorPool: new Cache(888),
	        parsers: {
	            number: function number(a) {
	                return a === '' ? '' : parseFloat(a) || 0;
	            },
	            string: function string(a) {
	                return a === null || a === void 0 ? '' : a + '';
	            },
	            "boolean": function boolean(a) {
	                if (a === '') return a;
	                return a === 'true' || a === '1';
	            }
	        },
	        _decode: function _decode(str) {
	            if (rentities.test(str)) {
	                temp.innerHTML = str;
	                return temp.innerText || temp.textContent;
	            }
	            return str;
	        }
	    });
	
	    //============== config ============
	    function config(settings) {
	        for (var p in settings) {
	            var val = settings[p];
	            if (typeof config.plugins[p] === 'function') {
	                config.plugins[p](val);
	            } else {
	                config[p] = val;
	            }
	        }
	        return this;
	    }
	
	    var plugins = {
	        interpolate: function interpolate(array) {
	            var openTag = array[0];
	            var closeTag = array[1];
	            if (openTag === closeTag) {
	                throw new SyntaxError('interpolate openTag cannot equal to closeTag');
	            }
	            var str = openTag + 'test' + closeTag;
	
	            if (/[<>]/.test(str)) {
	                throw new SyntaxError('interpolate cannot contains "<" or ">"');
	            }
	
	            config.openTag = openTag;
	            config.closeTag = closeTag;
	            var o = escapeRegExp(openTag);
	            var c = escapeRegExp(closeTag);
	
	            config.rtext = new RegExp(o + '(.+?)' + c, 'g');
	            config.rexpr = new RegExp(o + '([\\s\\S]*)' + c);
	        }
	    };
	    function createAnchor(nodeValue) {
	        return document$1.createComment(nodeValue);
	    }
	    config.plugins = plugins;
	    config({
	        interpolate: ['{{', '}}'],
	        debug: true
	    });
	    //============  config ============
	
	    shadowCopy(avalon, {
	        shadowCopy: shadowCopy,
	
	        oneObject: oneObject,
	        inspect: inspect,
	        ohasOwn: ohasOwn,
	        rword: rword,
	        version: "2.2.4",
	        vmodels: {},
	
	        directives: directives,
	        directive: directive,
	
	        eventHooks: eventHooks,
	        eventListeners: eventListeners,
	        validators: validators,
	        cssHooks: cssHooks,
	
	        log: log,
	        noop: noop,
	        warn: warn,
	        error: error,
	        config: config,
	
	        modern: modern,
	        msie: msie,
	        root: root,
	        document: document$1,
	        window: window$1,
	        inBrowser: inBrowser,
	
	        isObject: isObject,
	        range: range,
	        slice: slice,
	        hyphen: hyphen,
	        camelize: camelize,
	        escapeRegExp: escapeRegExp,
	        quote: quote,
	
	        makeHashCode: makeHashCode
	
	    });
	
	    /**
	     * 此模块用于修复语言的底层缺陷
	     */
	    function isNative(fn) {
	        return (/\[native code\]/.test(fn)
	        );
	    }
	
	    /* istanbul ignore if*/
	    if (!isNative('司徒正美'.trim)) {
	        var rtrim = /^[\s\uFEFF\xA0]+|[\s\uFEFF\xA0]+$/g;
	        String.prototype.trim = function () {
	            return this.replace(rtrim, '');
	        };
	    }
	    if (!Object.create) {
	        Object.create = function () {
	            function F() {}
	
	            return function (o) {
	                if (arguments.length != 1) {
	                    throw new Error('Object.create implementation only accepts one parameter.');
	                }
	                F.prototype = o;
	                return new F();
	            };
	        }();
	    }
	    var hasDontEnumBug = !{
	        'toString': null
	    }.propertyIsEnumerable('toString');
	    var hasProtoEnumBug = function () {}.propertyIsEnumerable('prototype');
	    var dontEnums = ['toString', 'toLocaleString', 'valueOf', 'hasOwnProperty', 'isPrototypeOf', 'propertyIsEnumerable', 'constructor'];
	    var dontEnumsLength = dontEnums.length;
	    /* istanbul ignore if*/
	    if (!isNative(Object.keys)) {
	        Object.keys = function (object) {
	            //ecma262v5 15.2.3.14
	            var theKeys = [];
	            var skipProto = hasProtoEnumBug && typeof object === 'function';
	            if (typeof object === 'string' || object && object.callee) {
	                for (var i = 0; i < object.length; ++i) {
	                    theKeys.push(String(i));
	                }
	            } else {
	                for (var name in object) {
	                    if (!(skipProto && name === 'prototype') && ohasOwn.call(object, name)) {
	                        theKeys.push(String(name));
	                    }
	                }
	            }
	
	            if (hasDontEnumBug) {
	                var ctor = object.constructor,
	                    skipConstructor = ctor && ctor.prototype === object;
	                for (var j = 0; j < dontEnumsLength; j++) {
	                    var dontEnum = dontEnums[j];
	                    if (!(skipConstructor && dontEnum === 'constructor') && ohasOwn.call(object, dontEnum)) {
	                        theKeys.push(dontEnum);
	                    }
	                }
	            }
	            return theKeys;
	        };
	    }
	    /* istanbul ignore if*/
	    if (!isNative(Array.isArray)) {
	        Array.isArray = function (a) {
	            return Object.prototype.toString.call(a) === '[object Array]';
	        };
	    }
	
	    /* istanbul ignore if*/
	    if (!isNative(isNative.bind)) {
	        /* istanbul ignore next*/
	        Function.prototype.bind = function (scope) {
	            if (arguments.length < 2 && scope === void 0) return this;
	            var fn = this,
	                argv = arguments;
	            return function () {
	                var args = [],
	                    i;
	                for (i = 1; i < argv.length; i++) {
	                    args.push(argv[i]);
	                }for (i = 0; i < arguments.length; i++) {
	                    args.push(arguments[i]);
	                }return fn.apply(scope, args);
	            };
	        };
	    }
	    //https://developer.mozilla.org/zh-CN/docs/Web/JavaScript/Reference/Global_Objects/Array/slice
	    /**
	     * Shim for "fixing" IE's lack of support (IE < 9) for applying slice
	     * on host objects like NamedNodeMap, NodeList, and HTMLCollection
	     * (technically, since host objects have been implementation-dependent,
	     * at least before ES6, IE hasn't needed to work this way).
	     * Also works on strings, fixes IE < 9 to allow an explicit undefined
	     * for the 2nd argument (as in Firefox), and prevents errors when
	     * called on other DOM objects.
	     */
	
	    try {
	        // Can't be used with DOM elements in IE < 9
	        _slice.call(avalon.document.documentElement);
	    } catch (e) {
	        // Fails in IE < 9
	        // This will work for genuine arrays, array-like objects,
	        // NamedNodeMap (attributes, entities, notations),
	        // NodeList (e.g., getElementsByTagName), HTMLCollection (e.g., childNodes),
	        // and will not fail on other DOM objects (as do DOM elements in IE < 9)
	        /* istanbul ignore next*/
	        ap.slice = function (begin, end) {
	            // IE < 9 gets unhappy with an undefined end argument
	            end = typeof end !== 'undefined' ? end : this.length;
	
	            // For native Array objects, we use the native slice function
	            if (Array.isArray(this)) {
	                return _slice.call(this, begin, end);
	            }
	
	            // For array like object we handle it ourselves.
	            var i,
	                cloned = [],
	                size,
	                len = this.length;
	
	            // Handle negative value for "begin"
	            var start = begin || 0;
	            start = start >= 0 ? start : len + start;
	
	            // Handle negative value for "end"
	            var upTo = end ? end : len;
	            if (end < 0) {
	                upTo = len + end;
	            }
	
	            // Actual expected size of the slice
	            size = upTo - start;
	
	            if (size > 0) {
	                cloned = new Array(size);
	                if (this.charAt) {
	                    for (i = 0; i < size; i++) {
	                        cloned[i] = this.charAt(start + i);
	                    }
	                } else {
	                    for (i = 0; i < size; i++) {
	                        cloned[i] = this[start + i];
	                    }
	                }
	            }
	
	            return cloned;
	        };
	    }
	    /* istanbul ignore next*/
	    function iterator(vars, body, ret) {
	        var fun = 'for(var ' + vars + 'i=0,n = this.length; i < n; i++){' + body.replace('_', '((i in this) && fn.call(scope,this[i],i,this))') + '}' + ret;
	        /* jshint ignore:start */
	        return Function('fn,scope', fun);
	        /* jshint ignore:end */
	    }
	    /* istanbul ignore if*/
	    if (!isNative(ap.map)) {
	        avalon.shadowCopy(ap, {
	            //定位操作，返回数组中第一个等于给定参数的元素的索引值。
	            indexOf: function indexOf(item, index) {
	                var n = this.length,
	                    i = ~~index;
	                if (i < 0) i += n;
	                for (; i < n; i++) {
	                    if (this[i] === item) return i;
	                }return -1;
	            },
	            //定位操作，同上，不过是从后遍历。
	            lastIndexOf: function lastIndexOf(item, index) {
	                var n = this.length,
	                    i = index == null ? n - 1 : index;
	                if (i < 0) i = Math.max(0, n + i);
	                for (; i >= 0; i--) {
	                    if (this[i] === item) return i;
	                }return -1;
	            },
	            //迭代操作，将数组的元素挨个儿传入一个函数中执行。Prototype.js的对应名字为each。
	            forEach: iterator('', '_', ''),
	            //迭代类 在数组中的每个项上运行一个函数，如果此函数的值为真，则此元素作为新数组的元素收集起来，并返回新数组
	            filter: iterator('r=[],j=0,', 'if(_)r[j++]=this[i]', 'return r'),
	            //收集操作，将数组的元素挨个儿传入一个函数中执行，然后把它们的返回值组成一个新数组返回。Prototype.js的对应名字为collect。
	            map: iterator('r=[],', 'r[i]=_', 'return r'),
	            //只要数组中有一个元素满足条件（放进给定函数返回true），那么它就返回true。Prototype.js的对应名字为any。
	            some: iterator('', 'if(_)return true', 'return false'),
	            //只有数组中的元素都满足条件（放进给定函数返回true），它才返回true。Prototype.js的对应名字为all。
	            every: iterator('', 'if(!_)return false', 'return true')
	        });
	    }
	
	    //这里放置存在异议的方法
	    var compaceQuote = function () {
	        //https://github.com/bestiejs/json3/blob/master/lib/json3.js
	        var Escapes = {
	            92: "\\\\",
	            34: '\\"',
	            8: "\\b",
	            12: "\\f",
	            10: "\\n",
	            13: "\\r",
	            9: "\\t"
	        };
	
	        var leadingZeroes = '000000';
	        var toPaddedString = function toPaddedString(width, value) {
	            return (leadingZeroes + (value || 0)).slice(-width);
	        };
	        var unicodePrefix = '\\u00';
	        var escapeChar = function escapeChar(character) {
	            var charCode = character.charCodeAt(0),
	                escaped = Escapes[charCode];
	            if (escaped) {
	                return escaped;
	            }
	            return unicodePrefix + toPaddedString(2, charCode.toString(16));
	        };
	        var reEscape = /[\x00-\x1f\x22\x5c]/g;
	        return function (value) {
	            /* istanbul ignore next */
	            reEscape.lastIndex = 0;
	            /* istanbul ignore next */
	            return '"' + (reEscape.test(value) ? String(value).replace(reEscape, escapeChar) : value) + '"';
	        };
	    }();
	    try {
	        avalon._quote = JSON.stringify;
	    } catch (e) {
	        /* istanbul ignore next  */
	        avalon._quote = compaceQuote;
	    }
	
	    var class2type = {};
	    'Boolean Number String Function Array Date RegExp Object Error'.replace(avalon.rword, function (name) {
	        class2type['[object ' + name + ']'] = name.toLowerCase();
	    });
	
	    avalon.type = function (obj) {
	        //取得目标的类型
	        if (obj == null) {
	            return String(obj);
	        }
	        // 早期的webkit内核浏览器实现了已废弃的ecma262v4标准，可以将正则字面量当作函数使用，因此typeof在判定正则时会返回function
	        return typeof obj === 'object' || typeof obj === 'function' ? class2type[inspect.call(obj)] || 'object' : typeof obj;
	    };
	
	    var rfunction = /^\s*\bfunction\b/;
	
	    avalon.isFunction = /* istanbul ignore if */typeof alert === 'object' ? function (fn) {
	        /* istanbul ignore next */
	        try {
	            /* istanbul ignore next */
	            return rfunction.test(fn + '');
	        } catch (e) {
	            /* istanbul ignore next */
	            return false;
	        }
	    } : function (fn) {
	        return inspect.call(fn) === '[object Function]';
	    };
	
	    // 利用IE678 window == document为true,document == window竟然为false的神奇特性
	    // 标准浏览器及IE9，IE10等使用 正则检测
	    /* istanbul ignore next */
	    function isWindowCompact(obj) {
	        if (!obj) {
	            return false;
	        }
	        return obj == obj.document && obj.document != obj; //jshint ignore:line
	    }
	
	    var rwindow = /^\[object (?:Window|DOMWindow|global)\]$/;
	
	    function isWindowModern(obj) {
	        return rwindow.test(inspect.call(obj));
	    }
	
	    avalon.isWindow = isWindowModern(avalon.window) ? isWindowModern : isWindowCompact;
	
	    var enu;
	    var enumerateBUG;
	    for (enu in avalon({})) {
	        break;
	    }
	
	    enumerateBUG = enu !== '0'; //IE6下为true, 其他为false
	
	    /*判定是否是一个朴素的javascript对象（Object），不是DOM对象，不是BOM对象，不是自定义类的实例*/
	    /* istanbul ignore next */
	    function isPlainObjectCompact(obj, key) {
	        if (!obj || avalon.type(obj) !== 'object' || obj.nodeType || avalon.isWindow(obj)) {
	            return false;
	        }
	        try {
	            //IE内置对象没有constructor
	            if (obj.constructor && !ohasOwn.call(obj, 'constructor') && !ohasOwn.call(obj.constructor.prototype, 'isPrototypeOf')) {
	                return false;
	            }
	            var isVBscript = obj.$vbthis;
	        } catch (e) {
	            //IE8 9会在这里抛错
	            return false;
	        }
	        /* istanbul ignore if */
	        if (enumerateBUG) {
	            for (key in obj) {
	                return ohasOwn.call(obj, key);
	            }
	        }
	        for (key in obj) {}
	        return key === undefined$1 || ohasOwn.call(obj, key);
	    }
	
	    /* istanbul ignore next */
	    function isPlainObjectModern(obj) {
	        // 简单的 typeof obj === 'object'检测，会致使用isPlainObject(window)在opera下通不过
	        return inspect.call(obj) === '[object Object]' && Object.getPrototypeOf(obj) === Object.prototype;
	    }
	    /* istanbul ignore next */
	    avalon.isPlainObject = /\[native code\]/.test(Object.getPrototypeOf) ? isPlainObjectModern : isPlainObjectCompact;
	
	    var rcanMix = /object|function/;
	
	    //与jQuery.extend方法，可用于浅拷贝，深拷贝
	    /* istanbul ignore next */
	    avalon.mix = avalon.fn.mix = function () {
	        var n = arguments.length,
	            isDeep = false,
	            i = 0,
	            array = [];
	        if (arguments[0] === true) {
	            isDeep = true;
	            i = 1;
	        }
	        //将所有非空对象变成空对象
	        for (; i < n; i++) {
	            var el = arguments[i];
	            el = el && rcanMix.test(typeof el) ? el : {};
	            array.push(el);
	        }
	        if (array.length === 1) {
	            array.unshift(this);
	        }
	        return innerExtend(isDeep, array);
	    };
	    var undefined$1;
	
	    function innerExtend(isDeep, array) {
	        var target = array[0],
	            copyIsArray,
	            clone,
	            name;
	        for (var i = 1, length = array.length; i < length; i++) {
	            //只处理非空参数
	            var options = array[i];
	            var noCloneArrayMethod = Array.isArray(options);
	            for (name in options) {
	                if (noCloneArrayMethod && !options.hasOwnProperty(name)) {
	                    continue;
	                }
	                try {
	                    var src = target[name];
	                    var copy = options[name]; //当options为VBS对象时报错
	                } catch (e) {
	                    continue;
	                }
	
	                // 防止环引用
	                if (target === copy) {
	                    continue;
	                }
	                if (isDeep && copy && (avalon.isPlainObject(copy) || (copyIsArray = Array.isArray(copy)))) {
	
	                    if (copyIsArray) {
	                        copyIsArray = false;
	                        clone = src && Array.isArray(src) ? src : [];
	                    } else {
	                        clone = src && avalon.isPlainObject(src) ? src : {};
	                    }
	
	                    target[name] = innerExtend(isDeep, [clone, copy]);
	                } else if (copy !== undefined$1) {
	                    target[name] = copy;
	                }
	            }
	        }
	        return target;
	    }
	
	    var rarraylike = /(Array|List|Collection|Map|Arguments)\]$/;
	    /*判定是否类数组，如节点集合，纯数组，arguments与拥有非负整数的length属性的纯JS对象*/
	    /* istanbul ignore next */
	    function isArrayLike(obj) {
	        if (!obj) return false;
	        var n = obj.length;
	        if (n === n >>> 0) {
	            //检测length属性是否为非负整数
	            var type = inspect.call(obj);
	            if (rarraylike.test(type)) return true;
	            if (type !== '[object Object]') return false;
	            try {
	                if ({}.propertyIsEnumerable.call(obj, 'length') === false) {
	                    //如果是原生对象
	                    return rfunction.test(obj.item || obj.callee);
	                }
	                return true;
	            } catch (e) {
	                //IE的NodeList直接抛错
	                return !obj.window; //IE6-8 window
	            }
	        }
	        return false;
	    }
	
	    avalon.each = function (obj, fn) {
	        if (obj) {
	            //排除null, undefined
	            var i = 0;
	            if (isArrayLike(obj)) {
	                for (var n = obj.length; i < n; i++) {
	                    if (fn(i, obj[i]) === false) break;
	                }
	            } else {
	                for (i in obj) {
	                    if (obj.hasOwnProperty(i) && fn(i, obj[i]) === false) {
	                        break;
	                    }
	                }
	            }
	        }
	    };
	    (function () {
	        var welcomeIntro = ["%cavalon.js %c" + avalon.version + " %cin debug mode, %cmore...", "color: rgb(114, 157, 52); font-weight: normal;", "color: rgb(85, 85, 85); font-weight: normal;", "color: rgb(85, 85, 85); font-weight: normal;", "color: rgb(82, 140, 224); font-weight: normal; text-decoration: underline;"];
	        var welcomeMessage = "You're running avalon in debug mode - messages will be printed to the console to help you fix problems and optimise your application.\n\n" + 'To disable debug mode, add this line at the start of your app:\n\n  avalon.config({debug: false});\n\n' + 'Debug mode also automatically shut down amicably when your app is minified.\n\n' + "Get help and support:\n  https://segmentfault.com/t/avalon\n  http://avalonjs.coding.me/\n  http://www.baidu-x.com/?q=avalonjs\n  http://www.avalon.org.cn/\n\nFound a bug? Raise an issue:\n  https://github.com/RubyLouvre/avalon/issues\n\n";
	        if (typeof console === 'object') {
	            var con = console;
	            var method = con.groupCollapsed || con.log;
	            Function.apply.call(method, con, welcomeIntro);
	            con.log(welcomeMessage);
	            if (method !== console.log) {
	                con.groupEnd(welcomeIntro);
	            }
	        }
	    })();
	
	    function toFixedFix(n, prec) {
	        var k = Math.pow(10, prec);
	        return '' + (Math.round(n * k) / k).toFixed(prec);
	    }
	    function numberFilter(number, decimals, point, thousands) {
	        //https://github.com/txgruppi/number_format
	        //form http://phpjs.org/functions/number_format/
	        //number 必需，要格式化的数字
	        //decimals 可选，规定多少个小数位。
	        //point 可选，规定用作小数点的字符串（默认为 . ）。
	        //thousands 可选，规定用作千位分隔符的字符串（默认为 , ），如果设置了该参数，那么所有其他参数都是必需的。
	        number = (number + '').replace(/[^0-9+\-Ee.]/g, '');
	        var n = !isFinite(+number) ? 0 : +number,
	            prec = !isFinite(+decimals) ? 3 : Math.abs(decimals),
	            sep = typeof thousands === 'string' ? thousands : ",",
	            dec = point || ".",
	            s = '';
	
	        // Fix for IE parseFloat(0.55).toFixed(0) = 0;
	        s = (prec ? toFixedFix(n, prec) : '' + Math.round(n)).split('.');
	        if (s[0].length > 3) {
	            s[0] = s[0].replace(/\B(?=(?:\d{3})+(?!\d))/g, sep);
	        }
	        /** //好像没有用
	         var s1 = s[1] || ''
	        
	          if (s1.length < prec) {
	                  s1 += new Array(prec - s[1].length + 1).join('0')
	                  s[1] = s1
	          }
	          **/
	        return s.join(dec);
	    }
	
	    var rscripts = /<script[^>]*>([\S\s]*?)<\/script\s*>/gim;
	    var ron = /\s+(on[^=\s]+)(?:=("[^"]*"|'[^']*'|[^\s>]+))?/g;
	    var ropen = /<\w+\b(?:(["'])[^"]*?(\1)|[^>])*>/ig;
	    var rsanitize = {
	        a: /\b(href)\=("javascript[^"]*"|'javascript[^']*')/ig,
	        img: /\b(src)\=("javascript[^"]*"|'javascript[^']*')/ig,
	        form: /\b(action)\=("javascript[^"]*"|'javascript[^']*')/ig
	    };
	
	    //https://www.owasp.org/index.php/XSS_Filter_Evasion_Cheat_Sheet
	    //    <a href="javasc&NewLine;ript&colon;alert('XSS')">chrome</a> 
	    //    <a href="data:text/html;base64, PGltZyBzcmM9eCBvbmVycm9yPWFsZXJ0KDEpPg==">chrome</a>
	    //    <a href="jav	ascript:alert('XSS');">IE67chrome</a>
	    //    <a href="jav&#x09;ascript:alert('XSS');">IE67chrome</a>
	    //    <a href="jav&#x0A;ascript:alert('XSS');">IE67chrome</a>
	    function sanitizeFilter(str) {
	        return str.replace(rscripts, "").replace(ropen, function (a, b) {
	            var match = a.toLowerCase().match(/<(\w+)\s/);
	            if (match) {
	                //处理a标签的href属性，img标签的src属性，form标签的action属性
	                var reg = rsanitize[match[1]];
	                if (reg) {
	                    a = a.replace(reg, function (s, name, value) {
	                        var quote = value.charAt(0);
	                        return name + "=" + quote + "javascript:void(0)" + quote; // jshint ignore:line
	                    });
	                }
	            }
	            return a.replace(ron, " ").replace(/\s+/g, " "); //移除onXXX事件
	        });
	    }
	
	    /*
	     'yyyy': 4 digit representation of year (e.g. AD 1 => 0001, AD 2010 => 2010)
	     'yy': 2 digit representation of year, padded (00-99). (e.g. AD 2001 => 01, AD 2010 => 10)
	     'y': 1 digit representation of year, e.g. (AD 1 => 1, AD 199 => 199)
	     'MMMM': Month in year (January-December)
	     'MMM': Month in year (Jan-Dec)
	     'MM': Month in year, padded (01-12)
	     'M': Month in year (1-12)
	     'dd': Day in month, padded (01-31)
	     'd': Day in month (1-31)
	     'EEEE': Day in Week,(Sunday-Saturday)
	     'EEE': Day in Week, (Sun-Sat)
	     'HH': Hour in day, padded (00-23)
	     'H': Hour in day (0-23)
	     'hh': Hour in am/pm, padded (01-12)
	     'h': Hour in am/pm, (1-12)
	     'mm': Minute in hour, padded (00-59)
	     'm': Minute in hour (0-59)
	     'ss': Second in minute, padded (00-59)
	     's': Second in minute (0-59)
	     'a': am/pm marker
	     'Z': 4 digit (+sign) representation of the timezone offset (-1200-+1200)
	     format string can also be one of the following predefined localizable formats:
	     
	     'medium': equivalent to 'MMM d, y h:mm:ss a' for en_US locale (e.g. Sep 3, 2010 12:05:08 pm)
	     'short': equivalent to 'M/d/yy h:mm a' for en_US locale (e.g. 9/3/10 12:05 pm)
	     'fullDate': equivalent to 'EEEE, MMMM d,y' for en_US locale (e.g. Friday, September 3, 2010)
	     'longDate': equivalent to 'MMMM d, y' for en_US locale (e.g. September 3, 2010
	     'mediumDate': equivalent to 'MMM d, y' for en_US locale (e.g. Sep 3, 2010)
	     'shortDate': equivalent to 'M/d/yy' for en_US locale (e.g. 9/3/10)
	     'mediumTime': equivalent to 'h:mm:ss a' for en_US locale (e.g. 12:05:08 pm)
	     'shortTime': equivalent to 'h:mm a' for en_US locale (e.g. 12:05 pm)
	     */
	
	    function toInt(str) {
	        return parseInt(str, 10) || 0;
	    }
	
	    function padNumber(num, digits, trim) {
	        var neg = '';
	        /* istanbul ignore if*/
	        if (num < 0) {
	            neg = '-';
	            num = -num;
	        }
	        num = '' + num;
	        while (num.length < digits) {
	            num = '0' + num;
	        }if (trim) num = num.substr(num.length - digits);
	        return neg + num;
	    }
	
	    function dateGetter(name, size, offset, trim) {
	        return function (date) {
	            var value = date["get" + name]();
	            if (offset > 0 || value > -offset) value += offset;
	            if (value === 0 && offset === -12) {
	                /* istanbul ignore next*/
	                value = 12;
	            }
	            return padNumber(value, size, trim);
	        };
	    }
	
	    function dateStrGetter(name, shortForm) {
	        return function (date, formats) {
	            var value = date["get" + name]();
	            var get = (shortForm ? "SHORT" + name : name).toUpperCase();
	            return formats[get][value];
	        };
	    }
	
	    function timeZoneGetter(date) {
	        var zone = -1 * date.getTimezoneOffset();
	        var paddedZone = zone >= 0 ? "+" : "";
	        paddedZone += padNumber(Math[zone > 0 ? "floor" : "ceil"](zone / 60), 2) + padNumber(Math.abs(zone % 60), 2);
	        return paddedZone;
	    }
	    //取得上午下午
	    function ampmGetter(date, formats) {
	        return date.getHours() < 12 ? formats.AMPMS[0] : formats.AMPMS[1];
	    }
	    var DATE_FORMATS = {
	        yyyy: dateGetter("FullYear", 4),
	        yy: dateGetter("FullYear", 2, 0, true),
	        y: dateGetter("FullYear", 1),
	        MMMM: dateStrGetter("Month"),
	        MMM: dateStrGetter("Month", true),
	        MM: dateGetter("Month", 2, 1),
	        M: dateGetter("Month", 1, 1),
	        dd: dateGetter("Date", 2),
	        d: dateGetter("Date", 1),
	        HH: dateGetter("Hours", 2),
	        H: dateGetter("Hours", 1),
	        hh: dateGetter("Hours", 2, -12),
	        h: dateGetter("Hours", 1, -12),
	        mm: dateGetter("Minutes", 2),
	        m: dateGetter("Minutes", 1),
	        ss: dateGetter("Seconds", 2),
	        s: dateGetter("Seconds", 1),
	        sss: dateGetter("Milliseconds", 3),
	        EEEE: dateStrGetter("Day"),
	        EEE: dateStrGetter("Day", true),
	        a: ampmGetter,
	        Z: timeZoneGetter
	    };
	    var rdateFormat = /((?:[^yMdHhmsaZE']+)|(?:'(?:[^']|'')*')|(?:E+|y+|M+|d+|H+|h+|m+|s+|a|Z))(.*)/;
	    var raspnetjson = /^\/Date\((\d+)\)\/$/;
	    function dateFilter(date, format) {
	        var locate = dateFilter.locate,
	            text = "",
	            parts = [],
	            fn,
	            match;
	        format = format || "mediumDate";
	        format = locate[format] || format;
	        if (typeof date === "string") {
	            if (/^\d+$/.test(date)) {
	                date = toInt(date);
	            } else if (raspnetjson.test(date)) {
	                date = +RegExp.$1;
	            } else {
	                var trimDate = date.trim();
	                var dateArray = [0, 0, 0, 0, 0, 0, 0];
	                var oDate = new Date(0);
	                //取得年月日
	                trimDate = trimDate.replace(/^(\d+)\D(\d+)\D(\d+)/, function (_, a, b, c) {
	                    var array = c.length === 4 ? [c, a, b] : [a, b, c];
	                    dateArray[0] = toInt(array[0]); //年
	                    dateArray[1] = toInt(array[1]) - 1; //月
	                    dateArray[2] = toInt(array[2]); //日
	                    return "";
	                });
	                var dateSetter = oDate.setFullYear;
	                var timeSetter = oDate.setHours;
	                trimDate = trimDate.replace(/[T\s](\d+):(\d+):?(\d+)?\.?(\d)?/, function (_, a, b, c, d) {
	                    dateArray[3] = toInt(a); //小时
	                    dateArray[4] = toInt(b); //分钟
	                    dateArray[5] = toInt(c); //秒
	                    if (d) {
	                        //毫秒
	                        dateArray[6] = Math.round(parseFloat("0." + d) * 1000);
	                    }
	                    return "";
	                });
	                var tzHour = 0;
	                var tzMin = 0;
	                trimDate = trimDate.replace(/Z|([+-])(\d\d):?(\d\d)/, function (z, symbol, c, d) {
	                    dateSetter = oDate.setUTCFullYear;
	                    timeSetter = oDate.setUTCHours;
	                    if (symbol) {
	                        tzHour = toInt(symbol + c);
	                        tzMin = toInt(symbol + d);
	                    }
	                    return '';
	                });
	
	                dateArray[3] -= tzHour;
	                dateArray[4] -= tzMin;
	                dateSetter.apply(oDate, dateArray.slice(0, 3));
	                timeSetter.apply(oDate, dateArray.slice(3));
	                date = oDate;
	            }
	        }
	        if (typeof date === 'number') {
	            date = new Date(date);
	        }
	
	        while (format) {
	            match = rdateFormat.exec(format);
	            /* istanbul ignore else */
	            if (match) {
	                parts = parts.concat(match.slice(1));
	                format = parts.pop();
	            } else {
	                parts.push(format);
	                format = null;
	            }
	        }
	        parts.forEach(function (value) {
	            fn = DATE_FORMATS[value];
	            text += fn ? fn(date, locate) : value.replace(/(^'|'$)/g, "").replace(/''/g, "'");
	        });
	        return text;
	    }
	
	    var locate = {
	        AMPMS: {
	            0: '上午',
	            1: '下午'
	        },
	        DAY: {
	            0: '星期日',
	            1: '星期一',
	            2: '星期二',
	            3: '星期三',
	            4: '星期四',
	            5: '星期五',
	            6: '星期六'
	        },
	        MONTH: {
	            0: '1月',
	            1: '2月',
	            2: '3月',
	            3: '4月',
	            4: '5月',
	            5: '6月',
	            6: '7月',
	            7: '8月',
	            8: '9月',
	            9: '10月',
	            10: '11月',
	            11: '12月'
	        },
	        SHORTDAY: {
	            '0': '周日',
	            '1': '周一',
	            '2': '周二',
	            '3': '周三',
	            '4': '周四',
	            '5': '周五',
	            '6': '周六'
	        },
	        fullDate: 'y年M月d日EEEE',
	        longDate: 'y年M月d日',
	        medium: 'yyyy-M-d H:mm:ss',
	        mediumDate: 'yyyy-M-d',
	        mediumTime: 'H:mm:ss',
	        'short': 'yy-M-d ah:mm',
	        shortDate: 'yy-M-d',
	        shortTime: 'ah:mm'
	    };
	    locate.SHORTMONTH = locate.MONTH;
	    dateFilter.locate = locate;
	
	    /**
	    $$skipArray:是系统级通用的不可监听属性
	    $skipArray: 是当前对象特有的不可监听属性
	    
	     不同点是
	     $$skipArray被hasOwnProperty后返回false
	     $skipArray被hasOwnProperty后返回true
	     */
	    var falsy;
	    var $$skipArray = {
	        $id: falsy,
	        $render: falsy,
	        $track: falsy,
	        $element: falsy,
	        $computed: falsy,
	        $watch: falsy,
	        $fire: falsy,
	        $events: falsy,
	        $accessors: falsy,
	        $hashcode: falsy,
	        $mutations: falsy,
	        $vbthis: falsy,
	        $vbsetter: falsy
	    };
	
	    /*
	    https://github.com/hufyhang/orderBy/blob/master/index.js
	    */
	
	    function orderBy(array, by, decend) {
	        var type = avalon.type(array);
	        if (type !== 'array' && type !== 'object') throw 'orderBy只能处理对象或数组';
	        var criteria = typeof by == 'string' ? function (el) {
	            return el && el[by];
	        } : typeof by === 'function' ? by : function (el) {
	            return el;
	        };
	        var mapping = {};
	        var temp = [];
	        __repeat(array, Array.isArray(array), function (key) {
	            var val = array[key];
	            var k = criteria(val, key);
	            if (k in mapping) {
	                mapping[k].push(key);
	            } else {
	                mapping[k] = [key];
	            }
	            temp.push(k);
	        });
	
	        temp.sort();
	        if (decend < 0) {
	            temp.reverse();
	        }
	        var _array = type === 'array';
	        var target = _array ? [] : {};
	        return recovery(target, temp, function (k) {
	            var key = mapping[k].shift();
	            if (_array) {
	                target.push(array[key]);
	            } else {
	                target[key] = array[key];
	            }
	        });
	    }
	
	    function __repeat(array, isArray$$1, cb) {
	        if (isArray$$1) {
	            array.forEach(function (val, index) {
	                cb(index);
	            });
	        } else if (typeof array.$track === 'string') {
	            array.$track.replace(/[^☥]+/g, function (k) {
	                cb(k);
	            });
	        } else {
	            for (var i in array) {
	                if (array.hasOwnProperty(i)) {
	                    cb(i);
	                }
	            }
	        }
	    }
	    function filterBy(array, search) {
	        var type = avalon.type(array);
	        if (type !== 'array' && type !== 'object') throw 'filterBy只能处理对象或数组';
	        var args = avalon.slice(arguments, 2);
	        var stype = avalon.type(search);
	        if (stype === 'function') {
	            var criteria = search;
	        } else if (stype === 'string' || stype === 'number') {
	            if (search === '') {
	                return array;
	            } else {
	                var reg = new RegExp(avalon.escapeRegExp(search), 'i');
	                criteria = function criteria(el) {
	                    return reg.test(el);
	                };
	            }
	        } else {
	            return array;
	        }
	        var index = 0;
	        var isArray$$1 = type === 'array';
	        var target = isArray$$1 ? [] : {};
	        __repeat(array, isArray$$1, function (key) {
	            var val = array[key];
	            if (criteria.apply(val, [val, index].concat(args))) {
	                if (isArray$$1) {
	                    target.push(val);
	                } else {
	                    target[key] = val;
	                }
	            }
	            index++;
	        });
	        return target;
	    }
	
	    function selectBy(data, array, defaults) {
	        if (avalon.isObject(data) && !Array.isArray(data)) {
	            var target = [];
	            return recovery(target, array, function (name) {
	                target.push(data.hasOwnProperty(name) ? data[name] : defaults ? defaults[name] : '');
	            });
	        } else {
	            return data;
	        }
	    }
	
	    function limitBy(input, limit, begin) {
	        var type = avalon.type(input);
	        if (type !== 'array' && type !== 'object') throw 'limitBy只能处理对象或数组';
	        //必须是数值
	        if (typeof limit !== 'number') {
	            return input;
	        }
	        //不能为NaN
	        if (limit !== limit) {
	            return input;
	        }
	        //将目标转换为数组
	        if (type === 'object') {
	            input = convertArray(input, false);
	        }
	        var n = input.length;
	        limit = Math.floor(Math.min(n, limit));
	        begin = typeof begin === 'number' ? begin : 0;
	        if (begin < 0) {
	            begin = Math.max(0, n + begin);
	        }
	        var data = [];
	        for (var i = begin; i < n; i++) {
	            if (data.length === limit) {
	                break;
	            }
	            data.push(input[i]);
	        }
	        var isArray$$1 = type === 'array';
	        if (isArray$$1) {
	            return data;
	        }
	        var target = {};
	        return recovery(target, data, function (el) {
	            target[el.key] = el.value;
	        });
	    }
	
	    function recovery(ret, array, callback) {
	        for (var i = 0, n = array.length; i < n; i++) {
	            callback(array[i]);
	        }
	        return ret;
	    }
	
	    //Chrome谷歌浏览器中js代码Array.sort排序的bug乱序解决办法
	    //http://www.cnblogs.com/yzeng/p/3949182.html
	    function convertArray(array, isArray$$1) {
	        var ret = [],
	            i = 0;
	        __repeat(array, isArray$$1, function (key) {
	            ret[i] = {
	                oldIndex: i,
	                value: array[key],
	                key: key
	            };
	            i++;
	        });
	        return ret;
	    }
	
	    var eventFilters = {
	        stop: function stop(e) {
	            e.stopPropagation();
	            return e;
	        },
	        prevent: function prevent(e) {
	            e.preventDefault();
	            return e;
	        }
	    };
	    var keys = {
	        esc: 27,
	        tab: 9,
	        enter: 13,
	        space: 32,
	        del: 46,
	        up: 38,
	        left: 37,
	        right: 39,
	        down: 40
	    };
	    for (var name$1 in keys) {
	        (function (filter, key) {
	            eventFilters[filter] = function (e) {
	                if (e.which !== key) {
	                    e.$return = true;
	                }
	                return e;
	            };
	        })(name$1, keys[name$1]);
	    }
	
	    //https://github.com/teppeis/htmlspecialchars
	    function escapeFilter(str) {
	        if (str == null) return '';
	
	        return String(str).replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/"/g, '&quot;').replace(/'/g, '&#39;');
	    }
	
	    var filters = avalon.filters = {};
	
	    avalon.composeFilters = function () {
	        var args = arguments;
	        return function (value) {
	            for (var i = 0, arr; arr = args[i++];) {
	                var name = arr[0];
	                var filter = avalon.filters[name];
	                if (typeof filter === 'function') {
	                    arr[0] = value;
	                    try {
	                        value = filter.apply(0, arr);
	                    } catch (e) {}
	                }
	            }
	            return value;
	        };
	    };
	
	    avalon.escapeHtml = escapeFilter;
	
	    avalon.mix(filters, {
	        uppercase: function uppercase(str) {
	            return String(str).toUpperCase();
	        },
	        lowercase: function lowercase(str) {
	            return String(str).toLowerCase();
	        },
	        truncate: function truncate(str, length, end) {
	            //length，新字符串长度，truncation，新字符串的结尾的字段,返回新字符串
	            if (!str) {
	                return '';
	            }
	            str = String(str);
	            if (isNaN(length)) {
	                length = 30;
	            }
	            end = typeof end === "string" ? end : "...";
	            return str.length > length ? str.slice(0, length - end.length) + end : /* istanbul ignore else*/
	            str;
	        },
	
	        camelize: avalon.camelize,
	        date: dateFilter,
	        escape: escapeFilter,
	        sanitize: sanitizeFilter,
	        number: numberFilter,
	        currency: function currency(amount, symbol, fractionSize) {
	            return (symbol || '\xA5') + numberFilter(amount, isFinite(fractionSize) ? /* istanbul ignore else*/fractionSize : 2);
	        }
	    }, { filterBy: filterBy, orderBy: orderBy, selectBy: selectBy, limitBy: limitBy }, eventFilters);
	
	    var rcheckedType = /^(?:checkbox|radio)$/;
	
	    /* istanbul ignore next */
	    function fixElement(dest, src) {
	        if (dest.nodeType !== 1) {
	            return;
	        }
	        var nodeName = dest.nodeName.toLowerCase();
	
	        if (nodeName === "script") {
	            if (dest.text !== src.text) {
	                dest.type = "noexec";
	                dest.text = src.text;
	                dest.type = src.type || "";
	            }
	        } else if (nodeName === 'object') {
	            var params = src.childNodes;
	            if (dest.childNodes.length !== params.length) {
	                avalon.clearHTML(dest);
	                for (var i = 0, el; el = params[i++];) {
	                    dest.appendChild(el.cloneNode(true));
	                }
	            }
	        } else if (nodeName === 'input' && rcheckedType.test(src.nodeName)) {
	
	            dest.defaultChecked = dest.checked = src.checked;
	            if (dest.value !== src.value) {
	                dest.value = src.value;
	            }
	        } else if (nodeName === 'option') {
	            dest.defaultSelected = dest.selected = src.defaultSelected;
	        } else if (nodeName === 'input' || nodeName === 'textarea') {
	            dest.defaultValue = src.defaultValue;
	        }
	    }
	
	    /* istanbul ignore next */
	    function getAll(context) {
	        return typeof context.getElementsByTagName !== 'undefined' ? context.getElementsByTagName('*') : typeof context.querySelectorAll !== 'undefined' ? context.querySelectorAll('*') : [];
	    }
	
	    /* istanbul ignore next */
	    function fixClone(src) {
	        var target = src.cloneNode(true);
	        //http://www.myexception.cn/web/665613.html
	        // target.expando = null
	        var t = getAll(target);
	        var s = getAll(src);
	        for (var i = 0; i < s.length; i++) {
	            fixElement(t[i], s[i]);
	        }
	        return target;
	    }
	
	    /* istanbul ignore next */
	    function fixContains(root, el) {
	        try {
	            //IE6-8,游离于DOM树外的文本节点，访问parentNode有时会抛错
	            while (el = el.parentNode) {
	                if (el === root) return true;
	            }
	        } catch (e) {}
	        return false;
	    }
	
	    avalon.contains = fixContains;
	
	    avalon.cloneNode = function (a) {
	        return a.cloneNode(true);
	    };
	
	    //IE6-11的文档对象没有contains
	    /* istanbul ignore next */
	    function shimHack() {
	        if (msie < 10) {
	            avalon.cloneNode = fixClone;
	        }
	        if (!document$1.contains) {
	            document$1.contains = function (b) {
	                return fixContains(document$1, b);
	            };
	        }
	        if (avalon.modern) {
	            if (!document$1.createTextNode('x').contains) {
	                Node.prototype.contains = function (child) {
	                    //IE6-8没有Node对象
	                    return fixContains(this, child);
	                };
	            }
	        }
	        //firefox 到11时才有outerHTML
	        function fixFF(prop, cb) {
	            if (!(prop in root) && HTMLElement.prototype.__defineGetter__) {
	                HTMLElement.prototype.__defineGetter__(prop, cb);
	            }
	        }
	        fixFF('outerHTML', function () {
	            var div = document$1.createElement('div');
	            div.appendChild(this);
	            return div.innerHTML;
	        });
	        fixFF('children', function () {
	            var children = [];
	            for (var i = 0, el; el = this.childNodes[i++];) {
	                if (el.nodeType === 1) {
	                    children.push(el);
	                }
	            }
	            return children;
	        });
	        fixFF('innerText', function () {
	            //firefox45+, chrome4+ http://caniuse.com/#feat=innertext
	            return this.textContent;
	        });
	    }
	
	    if (inBrowser) {
	        shimHack();
	    }
	
	    function ClassList(node) {
	        this.node = node;
	    }
	
	    ClassList.prototype = {
	        toString: function toString() {
	            var node = this.node;
	            var cls = node.className;
	            var str = typeof cls === 'string' ? cls : cls.baseVal;
	            var match = str.match(rnowhite);
	            return match ? match.join(' ') : '';
	        },
	        contains: function contains(cls) {
	            return (' ' + this + ' ').indexOf(' ' + cls + ' ') > -1;
	        },
	        add: function add(cls) {
	            if (!this.contains(cls)) {
	                this.set(this + ' ' + cls);
	            }
	        },
	        remove: function remove(cls) {
	            this.set((' ' + this + ' ').replace(' ' + cls + ' ', ' '));
	        },
	        set: function set(cls) {
	            cls = cls.trim();
	            var node = this.node;
	            if (typeof node.className === 'object') {
	                //SVG元素的className是一个对象 SVGAnimatedString { baseVal='', animVal=''}，只能通过set/getAttribute操作
	                node.setAttribute('class', cls);
	            } else {
	                node.className = cls;
	            }
	            if (!cls) {
	                node.removeAttribute('class');
	            }
	            //toggle存在版本差异，因此不使用它
	        }
	    };
	
	    function classListFactory(node) {
	        if (!('classList' in node)) {
	            node.classList = new ClassList(node);
	        }
	        return node.classList;
	    }
	
	    'add,remove'.replace(rword, function (method) {
	        avalon.fn[method + 'Class'] = function (cls) {
	            var el = this[0] || {};
	            //https://developer.mozilla.org/zh-CN/docs/Mozilla/Firefox/Releases/26
	            if (cls && typeof cls === 'string' && el.nodeType === 1) {
	                cls.replace(rnowhite, function (c) {
	                    classListFactory(el)[method](c);
	                });
	            }
	            return this;
	        };
	    });
	
	    avalon.shadowCopy(avalon.fn, {
	        hasClass: function hasClass(cls) {
	            var el = this[0] || {};
	            return el.nodeType === 1 && classListFactory(el).contains(cls);
	        },
	        toggleClass: function toggleClass(value, stateVal) {
	            var isBool = typeof stateVal === 'boolean';
	            var me = this;
	            String(value).replace(rnowhite, function (c) {
	                var state = isBool ? stateVal : !me.hasClass(c);
	                me[state ? 'addClass' : 'removeClass'](c);
	            });
	            return this;
	        }
	    });
	
	    var propMap = { //不规则的属性名映射
	        'accept-charset': 'acceptCharset',
	        'char': 'ch',
	        charoff: 'chOff',
	        'class': 'className',
	        'for': 'htmlFor',
	        'http-equiv': 'httpEquiv'
	    };
	    /*
	    contenteditable不是布尔属性
	    http://www.zhangxinxu.com/wordpress/2016/01/contenteditable-plaintext-only/
	    contenteditable=''
	    contenteditable='events'
	    contenteditable='caret'
	    contenteditable='plaintext-only'
	    contenteditable='true'
	    contenteditable='false'
	     */
	    var bools = ['autofocus,autoplay,async,allowTransparency,checked,controls', 'declare,disabled,defer,defaultChecked,defaultSelected,', 'isMap,loop,multiple,noHref,noResize,noShade', 'open,readOnly,selected'].join(',');
	
	    bools.replace(/\w+/g, function (name) {
	        propMap[name.toLowerCase()] = name;
	    });
	
	    var anomaly = ['accessKey,bgColor,cellPadding,cellSpacing,codeBase,codeType,colSpan', 'dateTime,defaultValue,contentEditable,frameBorder,longDesc,maxLength,' + 'marginWidth,marginHeight,rowSpan,tabIndex,useMap,vSpace,valueType,vAlign'].join(',');
	
	    anomaly.replace(/\w+/g, function (name) {
	        propMap[name.toLowerCase()] = name;
	    });
	
	    //module.exports = propMap
	
	    function isVML(src) {
	        var nodeName = src.nodeName;
	        return nodeName.toLowerCase() === nodeName && !!src.scopeName && src.outerText === '';
	    }
	
	    var rvalidchars = /^[\],:{}\s]*$/;
	    var rvalidbraces = /(?:^|:|,)(?:\s*\[)+/g;
	    var rvalidescape = /\\(?:["\\\/bfnrt]|u[\da-fA-F]{4})/g;
	    var rvalidtokens = /"[^"\\\r\n]*"|true|false|null|-?(?:\d+\.|)\d+(?:[eE][+-]?\d+|)/g;
	
	    function compactParseJSON(data) {
	        if (typeof data === 'string') {
	            data = data.trim();
	            if (data) {
	                if (rvalidchars.test(data.replace(rvalidescape, '@').replace(rvalidtokens, ']').replace(rvalidbraces, ''))) {
	                    return new Function('return ' + data)(); // jshint ignore:line
	                }
	            }
	            throw TypeError('Invalid JSON: [' + data + ']');
	        }
	        return data;
	    }
	
	    var rsvg = /^\[object SVG\w*Element\]$/;
	    var ramp = /&amp;/g;
	    function updateAttrs(node, attrs) {
	        for (var attrName in attrs) {
	            try {
	                var val = attrs[attrName];
	                // 处理路径属性
	                /* istanbul ignore if*/
	
	                //处理HTML5 data-*属性 SVG
	                if (attrName.indexOf('data-') === 0 || rsvg.test(node)) {
	                    node.setAttribute(attrName, val);
	                } else {
	                    var propName = propMap[attrName] || attrName;
	                    /* istanbul ignore if */
	                    if (typeof node[propName] === 'boolean') {
	                        if (propName === 'checked') {
	                            node.defaultChecked = !!val;
	                        }
	                        node[propName] = !!val;
	                        //布尔属性必须使用el.xxx = true|false方式设值
	                        //如果为false, IE全系列下相当于setAttribute(xxx,''),
	                        //会影响到样式,需要进一步处理
	                    }
	
	                    if (val === false) {
	                        //移除属性
	                        node.removeAttribute(propName);
	                        continue;
	                    }
	                    //IE6中classNamme, htmlFor等无法检测它们为内建属性　
	                    if (avalon.msie < 8 && /[A-Z]/.test(propName)) {
	                        node[propName] = val + '';
	                        continue;
	                    }
	                    //SVG只能使用setAttribute(xxx, yyy), VML只能使用node.xxx = yyy ,
	                    //HTML的固有属性必须node.xxx = yyy
	                    /* istanbul ignore next */
	                    var isInnate = !avalon.modern && isVML(node) ? true : isInnateProps(node.nodeName, attrName);
	                    if (isInnate) {
	                        if (attrName === 'href' || attrName === 'src') {
	                            /* istanbul ignore if */
	                            if (avalon.msie < 8) {
	                                val = String(val).replace(ramp, '&'); //处理IE67自动转义的问题
	                            }
	                        }
	                        node[propName] = val + '';
	                    } else {
	                        node.setAttribute(attrName, val);
	                    }
	                }
	            } catch (e) {
	                // 对象不支持此属性或方法 src https://github.com/ecomfe/zrender 
	                // 未知名称。\/n
	                // e.message大概这样,需要trim
	                //IE6-8,元素节点不支持其他元素节点的内置属性,如src, href, for
	                /* istanbul ignore next */
	                avalon.log(String(e.message).trim(), attrName, val);
	            }
	        }
	    }
	    var innateMap = {};
	
	    function isInnateProps(nodeName, attrName) {
	        var key = nodeName + ":" + attrName;
	        if (key in innateMap) {
	            return innateMap[key];
	        }
	        return innateMap[key] = attrName in document$1.createElement(nodeName);
	    }
	    try {
	        avalon.parseJSON = JSON.parse;
	    } catch (e) {
	        /* istanbul ignore next */
	        avalon.parseJSON = compactParseJSON;
	    }
	
	    avalon.fn.attr = function (name, value) {
	        if (arguments.length === 2) {
	            this[0].setAttribute(name, value);
	            return this;
	        } else {
	            return this[0].getAttribute(name);
	        }
	    };
	
	    var cssMap = {
	        'float': 'cssFloat'
	    };
	    avalon.cssNumber = oneObject('animationIterationCount,columnCount,order,flex,flexGrow,flexShrink,fillOpacity,fontWeight,lineHeight,opacity,orphans,widows,zIndex,zoom');
	    var prefixes = ['', '-webkit-', '-o-', '-moz-', '-ms-'];
	    /* istanbul ignore next */
	    avalon.cssName = function (name, host, camelCase) {
	        if (cssMap[name]) {
	            return cssMap[name];
	        }
	        host = host || avalon.root.style || {};
	        for (var i = 0, n = prefixes.length; i < n; i++) {
	            camelCase = avalon.camelize(prefixes[i] + name);
	            if (camelCase in host) {
	                return cssMap[name] = camelCase;
	            }
	        }
	        return null;
	    };
	    /* istanbul ignore next */
	    avalon.css = function (node, name, value, fn) {
	        //读写删除元素节点的样式
	        if (node instanceof avalon) {
	            node = node[0];
	        }
	        if (node.nodeType !== 1) {
	            return;
	        }
	        var prop = avalon.camelize(name);
	        name = avalon.cssName(prop) || /* istanbul ignore next*/prop;
	        if (value === void 0 || typeof value === 'boolean') {
	            //获取样式
	            fn = cssHooks[prop + ':get'] || cssHooks['@:get'];
	            if (name === 'background') {
	                name = 'backgroundColor';
	            }
	            var val = fn(node, name);
	            return value === true ? parseFloat(val) || 0 : val;
	        } else if (value === '') {
	            //请除样式
	            node.style[name] = '';
	        } else {
	            //设置样式
	            if (value == null || value !== value) {
	                return;
	            }
	            if (isFinite(value) && !avalon.cssNumber[prop]) {
	                value += 'px';
	            }
	            fn = cssHooks[prop + ':set'] || cssHooks['@:set'];
	            fn(node, name, value);
	        }
	    };
	    /* istanbul ignore next */
	    avalon.fn.css = function (name, value) {
	        if (avalon.isPlainObject(name)) {
	            for (var i in name) {
	                avalon.css(this, i, name[i]);
	            }
	        } else {
	            var ret = avalon.css(this, name, value);
	        }
	        return ret !== void 0 ? ret : this;
	    };
	    /* istanbul ignore next */
	    avalon.fn.position = function () {
	        var offsetParent,
	            offset,
	            elem = this[0],
	            parentOffset = {
	            top: 0,
	            left: 0
	        };
	        if (!elem) {
	            return parentOffset;
	        }
	        if (this.css('position') === 'fixed') {
	            offset = elem.getBoundingClientRect();
	        } else {
	            offsetParent = this.offsetParent(); //得到真正的offsetParent
	            offset = this.offset(); // 得到正确的offsetParent
	            if (offsetParent[0].tagName !== 'HTML') {
	                parentOffset = offsetParent.offset();
	            }
	            parentOffset.top += avalon.css(offsetParent[0], 'borderTopWidth', true);
	            parentOffset.left += avalon.css(offsetParent[0], 'borderLeftWidth', true);
	
	            // Subtract offsetParent scroll positions
	            parentOffset.top -= offsetParent.scrollTop();
	            parentOffset.left -= offsetParent.scrollLeft();
	        }
	        return {
	            top: offset.top - parentOffset.top - avalon.css(elem, 'marginTop', true),
	            left: offset.left - parentOffset.left - avalon.css(elem, 'marginLeft', true)
	        };
	    };
	    /* istanbul ignore next */
	    avalon.fn.offsetParent = function () {
	        var offsetParent = this[0].offsetParent;
	        while (offsetParent && avalon.css(offsetParent, 'position') === 'static') {
	            offsetParent = offsetParent.offsetParent;
	        }
	        return avalon(offsetParent || avalon.root);
	    };
	
	    /* istanbul ignore next */
	    cssHooks['@:set'] = function (node, name, value) {
	        try {
	            //node.style.width = NaN;node.style.width = 'xxxxxxx';
	            //node.style.width = undefine 在旧式IE下会抛异常
	            node.style[name] = value;
	        } catch (e) {}
	    };
	    /* istanbul ignore next */
	    cssHooks['@:get'] = function (node, name) {
	        if (!node || !node.style) {
	            throw new Error('getComputedStyle要求传入一个节点 ' + node);
	        }
	        var ret,
	            styles = window$1.getComputedStyle(node, null);
	        if (styles) {
	            ret = name === 'filter' ? styles.getPropertyValue(name) : styles[name];
	            if (ret === '') {
	                ret = node.style[name]; //其他浏览器需要我们手动取内联样式
	            }
	        }
	        return ret;
	    };
	
	    cssHooks['opacity:get'] = function (node) {
	        var ret = cssHooks['@:get'](node, 'opacity');
	        return ret === '' ? '1' : ret;
	    };
	
	    'top,left'.replace(avalon.rword, function (name) {
	        cssHooks[name + ':get'] = function (node) {
	            var computed = cssHooks['@:get'](node, name);
	            return (/px$/.test(computed) ? computed : avalon(node).position()[name] + 'px'
	            );
	        };
	    });
	
	    var cssShow = {
	        position: 'absolute',
	        visibility: 'hidden',
	        display: 'block'
	    };
	
	    var rdisplayswap = /^(none|table(?!-c[ea]).+)/;
	    /* istanbul ignore next */
	    function showHidden(node, array) {
	        //http://www.cnblogs.com/rubylouvre/archive/2012/10/27/2742529.html
	        if (node.offsetWidth <= 0) {
	            //opera.offsetWidth可能小于0
	            if (rdisplayswap.test(cssHooks['@:get'](node, 'display'))) {
	                var obj = {
	                    node: node
	                };
	                for (var name in cssShow) {
	                    obj[name] = node.style[name];
	                    node.style[name] = cssShow[name];
	                }
	                array.push(obj);
	            }
	            var parent = node.parentNode;
	            if (parent && parent.nodeType === 1) {
	                showHidden(parent, array);
	            }
	        }
	    }
	    /* istanbul ignore next*/
	    avalon.each({
	        Width: 'width',
	        Height: 'height'
	    }, function (name, method) {
	        var clientProp = 'client' + name,
	            scrollProp = 'scroll' + name,
	            offsetProp = 'offset' + name;
	        cssHooks[method + ':get'] = function (node, which, override) {
	            var boxSizing = -4;
	            if (typeof override === 'number') {
	                boxSizing = override;
	            }
	            which = name === 'Width' ? ['Left', 'Right'] : ['Top', 'Bottom'];
	            var ret = node[offsetProp]; // border-box 0
	            if (boxSizing === 2) {
	                // margin-box 2
	                return ret + avalon.css(node, 'margin' + which[0], true) + avalon.css(node, 'margin' + which[1], true);
	            }
	            if (boxSizing < 0) {
	                // padding-box  -2
	                ret = ret - avalon.css(node, 'border' + which[0] + 'Width', true) - avalon.css(node, 'border' + which[1] + 'Width', true);
	            }
	            if (boxSizing === -4) {
	                // content-box -4
	                ret = ret - avalon.css(node, 'padding' + which[0], true) - avalon.css(node, 'padding' + which[1], true);
	            }
	            return ret;
	        };
	        cssHooks[method + '&get'] = function (node) {
	            var hidden = [];
	            showHidden(node, hidden);
	            var val = cssHooks[method + ':get'](node);
	            for (var i = 0, obj; obj = hidden[i++];) {
	                node = obj.node;
	                for (var n in obj) {
	                    if (typeof obj[n] === 'string') {
	                        node.style[n] = obj[n];
	                    }
	                }
	            }
	            return val;
	        };
	        avalon.fn[method] = function (value) {
	            //会忽视其display
	            var node = this[0];
	            if (arguments.length === 0) {
	                if (node.setTimeout) {
	                    //取得窗口尺寸
	                    return node['inner' + name] || node.document.documentElement[clientProp] || node.document.body[clientProp]; //IE6下前两个分别为undefined,0
	                }
	                if (node.nodeType === 9) {
	                    //取得页面尺寸
	                    var doc = node.documentElement;
	                    //FF chrome    html.scrollHeight< body.scrollHeight
	                    //IE 标准模式 : html.scrollHeight> body.scrollHeight
	                    //IE 怪异模式 : html.scrollHeight 最大等于可视窗口多一点？
	                    return Math.max(node.body[scrollProp], doc[scrollProp], node.body[offsetProp], doc[offsetProp], doc[clientProp]);
	                }
	                return cssHooks[method + '&get'](node);
	            } else {
	                return this.css(method, value);
	            }
	        };
	        avalon.fn['inner' + name] = function () {
	            return cssHooks[method + ':get'](this[0], void 0, -2);
	        };
	        avalon.fn['outer' + name] = function (includeMargin) {
	            return cssHooks[method + ':get'](this[0], void 0, includeMargin === true ? 2 : 0);
	        };
	    });
	
	    function getWindow(node) {
	        return node.window || node.defaultView || node.parentWindow || false;
	    }
	
	    /* istanbul ignore if */
	    if (msie < 9) {
	        cssMap['float'] = 'styleFloat';
	        var rnumnonpx = /^-?(?:\d*\.)?\d+(?!px)[^\d\s]+$/i;
	        var rposition = /^(top|right|bottom|left)$/;
	        var ralpha = /alpha\([^)]+\)/i;
	        var ropactiy = /(opacity|\d(\d|\.)*)/g;
	        var ie8 = msie === 8;
	        var salpha = 'DXImageTransform.Microsoft.Alpha';
	        var border = {
	            thin: ie8 ? '1px' : '2px',
	            medium: ie8 ? '3px' : '4px',
	            thick: ie8 ? '5px' : '6px'
	        };
	        cssHooks['@:get'] = function (node, name) {
	            //取得精确值，不过它有可能是带em,pc,mm,pt,%等单位
	            var currentStyle = node.currentStyle;
	            var ret = currentStyle[name];
	            if (rnumnonpx.test(ret) && !rposition.test(ret)) {
	                //①，保存原有的style.left, runtimeStyle.left,
	                var style = node.style,
	                    left = style.left,
	                    rsLeft = node.runtimeStyle.left;
	                //②由于③处的style.left = xxx会影响到currentStyle.left，
	                //因此把它currentStyle.left放到runtimeStyle.left，
	                //runtimeStyle.left拥有最高优先级，不会style.left影响
	                node.runtimeStyle.left = currentStyle.left;
	                //③将精确值赋给到style.left，然后通过IE的另一个私有属性 style.pixelLeft
	                //得到单位为px的结果；fontSize的分支见http://bugs.jquery.com/ticket/760
	                style.left = name === 'fontSize' ? '1em' : ret || 0;
	                ret = style.pixelLeft + 'px';
	                //④还原 style.left，runtimeStyle.left
	                style.left = left;
	                node.runtimeStyle.left = rsLeft;
	            }
	            if (ret === 'medium') {
	                name = name.replace('Width', 'Style');
	                //border width 默认值为medium，即使其为0'
	                if (currentStyle[name] === 'none') {
	                    ret = '0px';
	                }
	            }
	            return ret === '' ? 'auto' : border[ret] || ret;
	        };
	        cssHooks['opacity:set'] = function (node, name, value) {
	            var style = node.style;
	
	            var opacity = Number(value) <= 1 ? 'alpha(opacity=' + value * 100 + ')' : '';
	            var filter = style.filter || '';
	            style.zoom = 1;
	            //不能使用以下方式设置透明度
	            //node.filters.alpha.opacity = value * 100
	            style.filter = (ralpha.test(filter) ? filter.replace(ralpha, opacity) : filter + ' ' + opacity).trim();
	
	            if (!style.filter) {
	                style.removeAttribute('filter');
	            }
	        };
	        cssHooks['opacity:get'] = function (node) {
	            var match = node.style.filter.match(ropactiy) || [];
	            var ret = false;
	            for (var i = 0, el; el = match[i++];) {
	                if (el === 'opacity') {
	                    ret = true;
	                } else if (ret) {
	                    return el / 100 + '';
	                }
	            }
	            return '1'; //确保返回的是字符串
	        };
	    }
	
	    /* istanbul ignore next */
	    avalon.fn.offset = function () {
	        //取得距离页面左右角的坐标
	        var node = this[0],
	            box = {
	            left: 0,
	            top: 0
	        };
	        if (!node || !node.tagName || !node.ownerDocument) {
	            return box;
	        }
	        var doc = node.ownerDocument;
	        var body = doc.body;
	        var root$$1 = doc.documentElement;
	        var win = doc.defaultView || doc.parentWindow;
	        if (!avalon.contains(root$$1, node)) {
	            return box;
	        }
	        //http://hkom.blog1.fc2.com/?mode=m&no=750 body的偏移量是不包含margin的
	        //我们可以通过getBoundingClientRect来获得元素相对于client的rect.
	        //http://msdn.microsoft.com/en-us/library/ms536433.aspx
	        if (node.getBoundingClientRect) {
	            box = node.getBoundingClientRect(); // BlackBerry 5, iOS 3 (original iPhone)
	        }
	        //chrome/IE6: body.scrollTop, firefox/other: root.scrollTop
	        var clientTop = root$$1.clientTop || body.clientTop,
	            clientLeft = root$$1.clientLeft || body.clientLeft,
	            scrollTop = Math.max(win.pageYOffset || 0, root$$1.scrollTop, body.scrollTop),
	            scrollLeft = Math.max(win.pageXOffset || 0, root$$1.scrollLeft, body.scrollLeft);
	        // 把滚动距离加到left,top中去。
	        // IE一些版本中会自动为HTML元素加上2px的border，我们需要去掉它
	        // http://msdn.microsoft.com/en-us/library/ms533564(VS.85).aspx
	        return {
	            top: box.top + scrollTop - clientTop,
	            left: box.left + scrollLeft - clientLeft
	        };
	    };
	
	    //生成avalon.fn.scrollLeft, avalon.fn.scrollTop方法
	    /* istanbul ignore next */
	    avalon.each({
	        scrollLeft: 'pageXOffset',
	        scrollTop: 'pageYOffset'
	    }, function (method, prop) {
	        avalon.fn[method] = function (val) {
	            var node = this[0] || {};
	            var win = getWindow(node);
	            var root$$1 = avalon.root;
	            var top = method === 'scrollTop';
	            if (!arguments.length) {
	                return win ? prop in win ? win[prop] : root$$1[method] : node[method];
	            } else {
	                if (win) {
	                    win.scrollTo(!top ? val : avalon(win).scrollLeft(), top ? val : avalon(win).scrollTop());
	                } else {
	                    node[method] = val;
	                }
	            }
	        };
	    });
	
	    function getDuplexType(elem) {
	        var ret = elem.tagName.toLowerCase();
	        if (ret === 'input') {
	            return rcheckedType.test(elem.type) ? 'checked' : elem.type;
	        }
	        return ret;
	    }
	
	    /**
	     * IE6/7/8中，如果option没有value值，那么将返回空字符串。
	     * IE9/Firefox/Safari/Chrome/Opera 中先取option的value值，如果没有value属性，则取option的innerText值。
	     * IE11及W3C，如果没有指定value，那么node.value默认为node.text（存在trim作），但IE9-10则是取innerHTML(没trim操作)
	     */
	
	    function getOption(node) {
	        if (node.hasAttribute && node.hasAttribute('value')) {
	            return node.getAttribute('value');
	        }
	        var attr = node.getAttributeNode('value');
	        if (attr && attr.specified) {
	            return attr.value;
	        }
	        return node.innerHTML.trim();
	    }
	
	    var valHooks = {
	        'option:get': msie ? getOption : function (node) {
	            return node.value;
	        },
	        'select:get': function selectGet(node, value) {
	            var option,
	                options = node.options,
	                index = node.selectedIndex,
	                getter = valHooks['option:get'],
	                one = node.type === 'select-one' || index < 0,
	                values = one ? null : [],
	                max = one ? index + 1 : options.length,
	                i = index < 0 ? max : one ? index : 0;
	            for (; i < max; i++) {
	                option = options[i];
	                //IE6-9在reset后不会改变selected，需要改用i === index判定
	                //我们过滤所有disabled的option元素，但在safari5下，
	                //如果设置optgroup为disable，那么其所有孩子都disable
	                //因此当一个元素为disable，需要检测其是否显式设置了disable及其父节点的disable情况
	                if ((option.selected || i === index) && !option.disabled && (!option.parentNode.disabled || option.parentNode.tagName !== 'OPTGROUP')) {
	                    value = getter(option);
	                    if (one) {
	                        return value;
	                    }
	                    //收集所有selected值组成数组返回
	                    values.push(value);
	                }
	            }
	            return values;
	        },
	        'select:set': function selectSet(node, values, optionSet) {
	            values = [].concat(values); //强制转换为数组
	            var getter = valHooks['option:get'];
	            for (var i = 0, el; el = node.options[i++];) {
	                if (el.selected = values.indexOf(getter(el)) > -1) {
	                    optionSet = true;
	                }
	            }
	            if (!optionSet) {
	                node.selectedIndex = -1;
	            }
	        }
	    };
	
	    avalon.fn.val = function (value) {
	        var node = this[0];
	        if (node && node.nodeType === 1) {
	            var get = arguments.length === 0;
	            var access = get ? ':get' : ':set';
	            var fn = valHooks[getDuplexType(node) + access];
	            if (fn) {
	                var val = fn(node, value);
	            } else if (get) {
	                return (node.value || '').replace(/\r/g, '');
	            } else {
	                node.value = value;
	            }
	        }
	        return get ? val : this;
	    };
	
	    /* 
	     * 将要检测的字符串的字符串替换成??123这样的格式
	     */
	    var stringNum = 0;
	    var stringPool = {
	        map: {}
	    };
	    var rfill = /\?\?\d+/g;
	    function dig(a) {
	        var key = '??' + stringNum++;
	        stringPool.map[key] = a;
	        return key + ' ';
	    }
	    function fill(a) {
	        var val = stringPool.map[a];
	        return val;
	    }
	    function clearString(str) {
	        var array = readString(str);
	        for (var i = 0, n = array.length; i < n; i++) {
	            str = str.replace(array[i], dig);
	        }
	        return str;
	    }
	
	    function readString(str) {
	        var end,
	            s = 0;
	        var ret = [];
	        for (var i = 0, n = str.length; i < n; i++) {
	            var c = str.charAt(i);
	            if (!end) {
	                if (c === "'") {
	                    end = "'";
	                    s = i;
	                } else if (c === '"') {
	                    end = '"';
	                    s = i;
	                }
	            } else {
	                if (c === end) {
	                    ret.push(str.slice(s, i + 1));
	                    end = false;
	                }
	            }
	        }
	        return ret;
	    }
	
	    var voidTag = {
	        area: 1,
	        base: 1,
	        basefont: 1,
	        bgsound: 1,
	        br: 1,
	        col: 1,
	        command: 1,
	        embed: 1,
	        frame: 1,
	        hr: 1,
	        img: 1,
	        input: 1,
	        keygen: 1,
	        link: 1,
	        meta: 1,
	        param: 1,
	        source: 1,
	        track: 1,
	        wbr: 1
	    };
	
	    var orphanTag = {
	        script: 1,
	        style: 1,
	        textarea: 1,
	        xmp: 1,
	        noscript: 1,
	        template: 1
	    };
	
	    /* 
	     *  此模块只用于文本转虚拟DOM, 
	     *  因为在真实浏览器会对我们的HTML做更多处理,
	     *  如, 添加额外属性, 改变结构
	     *  此模块就是用于模拟这些行为
	     */
	    function makeOrphan(node, nodeName, innerHTML) {
	        switch (nodeName) {
	            case 'style':
	            case 'script':
	            case 'noscript':
	            case 'template':
	            case 'xmp':
	                node.children = [{
	                    nodeName: '#text',
	                    nodeValue: innerHTML
	                }];
	                break;
	            case 'textarea':
	                var props = node.props;
	                props.type = nodeName;
	                props.value = innerHTML;
	                node.children = [{
	                    nodeName: '#text',
	                    nodeValue: innerHTML
	                }];
	                break;
	            case 'option':
	                node.children = [{
	                    nodeName: '#text',
	                    nodeValue: trimHTML(innerHTML)
	                }];
	                break;
	        }
	    }
	
	    //专门用于处理option标签里面的标签
	    var rtrimHTML = /<\w+(\s+("[^"]*"|'[^']*'|[^>])+)?>|<\/\w+>/gi;
	    function trimHTML(v) {
	        return String(v).replace(rtrimHTML, '').trim();
	    }
	
	    //widget rule duplex validate
	
	    //如果直接将tr元素写table下面,那么浏览器将将它们(相邻的那几个),放到一个动态创建的tbody底下
	    function makeTbody(nodes) {
	        var tbody,
	            needAddTbody = false,
	            count = 0,
	            start = 0,
	            n = nodes.length;
	        for (var i = 0; i < n; i++) {
	            var node = nodes[i];
	            if (!tbody) {
	                if (node.nodeName === 'tr') {
	                    //收集tr及tr两旁的注释节点
	                    tbody = {
	                        nodeName: 'tbody',
	                        props: {},
	                        children: []
	                    };
	                    tbody.children.push(node);
	                    needAddTbody = true;
	                    if (start === 0) start = i;
	                    nodes[i] = tbody;
	                }
	            } else {
	                if (node.nodeName !== 'tr' && node.children) {
	                    tbody = false;
	                } else {
	                    tbody.children.push(node);
	                    count++;
	                    nodes[i] = 0;
	                }
	            }
	        }
	
	        if (needAddTbody) {
	            for (i = start; i < n; i++) {
	                if (nodes[i] === 0) {
	                    nodes.splice(i, 1);
	                    i--;
	                    count--;
	                    if (count === 0) {
	                        break;
	                    }
	                }
	            }
	        }
	    }
	
	    function validateDOMNesting(parent, child) {
	
	        var parentTag = parent.nodeName;
	        var tag = child.nodeName;
	        var parentChild = nestObject[parentTag];
	        if (parentChild) {
	            if (parentTag === 'p') {
	                if (pNestChild[tag]) {
	                    avalon.warn('P element can not  add these childlren:\n' + Object.keys(pNestChild));
	                    return false;
	                }
	            } else if (!parentChild[tag]) {
	                avalon.warn(parentTag.toUpperCase() + 'element only add these children:\n' + Object.keys(parentChild) + '\nbut you add ' + tag.toUpperCase() + ' !!');
	                return false;
	            }
	        }
	        return true;
	    }
	
	    function makeObject(str) {
	        return oneObject(str + ',template,#document-fragment,#comment');
	    }
	    var pNestChild = oneObject('div,ul,ol,dl,table,h1,h2,h3,h4,h5,h6,form,fieldset');
	    var tNestChild = makeObject('tr,style,script');
	    var nestObject = {
	        p: pNestChild,
	        // https://html.spec.whatwg.org/multipage/syntax.html#parsing-main-inselect
	        select: makeObject('option,optgroup,#text'),
	        optgroup: makeObject('option,#text'),
	        option: makeObject('#text'),
	        // https://html.spec.whatwg.org/multipage/syntax.html#parsing-main-intd
	        // https://html.spec.whatwg.org/multipage/syntax.html#parsing-main-incaption
	        // No special behavior since these rules fall back to "in body" mode for
	        // all except special table nodes which cause bad parsing behavior anyway.
	
	        // https://html.spec.whatwg.org/multipage/syntax.html#parsing-main-intr
	        tr: makeObject('th,td,style,script'),
	
	        // https://html.spec.whatwg.org/multipage/syntax.html#parsing-main-intbody
	        tbody: tNestChild,
	        tfoot: tNestChild,
	        thead: tNestChild,
	        // https://html.spec.whatwg.org/multipage/syntax.html#parsing-main-incolgroup
	        colgroup: makeObject('col'),
	        // https://html.spec.whatwg.org/multipage/syntax.html#parsing-main-intable
	        // table: oneObject('caption,colgroup,tbody,thead,tfoot,style,script,template,#document-fragment'),
	        // https://html.spec.whatwg.org/multipage/syntax.html#parsing-main-inhead
	        head: makeObject('base,basefont,bgsound,link,style,script,meta,title,noscript,noframes'),
	        // https://html.spec.whatwg.org/multipage/semantics.html#the-html-element
	        html: oneObject('head,body')
	    };
	
	    /**
	     * ------------------------------------------------------------
	     * avalon2.1.1的新式lexer
	     * 将字符串变成一个虚拟DOM树,方便以后进一步变成模板函数
	     * 此阶段只会生成VElement,VText,VComment
	     * ------------------------------------------------------------
	     */
	    function nomalString(str) {
	        return avalon.unescapeHTML(str.replace(rfill, fill));
	    }
	    //https://github.com/rviscomi/trunk8/blob/master/trunk8.js
	
	    var ropenTag = /^<([-A-Za-z0-9_]+)\s*([^>]*?)(\/?)>/;
	    var rendTag = /^<\/([^>]+)>/;
	    var rtagStart = /[\!\/a-z]/i; //闭标签的第一个字符,开标签的第一个英文,注释节点的!
	    var rlineSp = /\\n\s*/g;
	    var rattrs = /([^=\s]+)(?:\s*=\s*(\S+))?/;
	
	    var rcontent = /\S/; //判定里面有没有内容
	    function fromString(str) {
	        return from(str);
	    }
	    avalon.lexer = fromString;
	
	    var strCache = new Cache(100);
	
	    function AST() {}
	    AST.prototype = {
	        init: function init(str) {
	            this.ret = [];
	            var stack = [];
	            stack.last = function () {
	                return stack[stack.length - 1];
	            };
	            this.stack = stack;
	            this.str = str;
	        },
	        gen: function gen() {
	            var breakIndex = 999999;
	            do {
	                this.tryGenText();
	                this.tryGenComment();
	                this.tryGenOpenTag();
	                this.tryGenCloseTag();
	                var node = this.node;
	                this.node = 0;
	                if (!node || --breakIndex === 0) {
	                    break;
	                }
	                if (node.end) {
	                    if (node.nodeName === 'table') {
	                        makeTbody(node.children);
	                    }
	                    delete node.end;
	                }
	            } while (this.str.length);
	            return this.ret;
	        },
	
	        fixPos: function fixPos(str, i) {
	            var tryCount = str.length - i;
	            while (tryCount--) {
	                if (!rtagStart.test(str.charAt(i + 1))) {
	                    i = str.indexOf('<', i + 1);
	                } else {
	                    break;
	                }
	            }
	            if (tryCount === 0) {
	                i = str.length;
	            }
	            return i;
	        },
	        tryGenText: function tryGenText() {
	            var str = this.str;
	            if (str.charAt(0) !== '<') {
	                //处理文本节点
	                var i = str.indexOf('<');
	                if (i === -1) {
	                    i = str.length;
	                } else if (!rtagStart.test(str.charAt(i + 1))) {
	                    //处理`内容2 {{ (idx1 < < <  1 ? 'red' : 'blue' ) + a }} ` 的情况 
	                    i = this.fixPos(str, i);
	                }
	                var nodeValue = str.slice(0, i).replace(rfill, fill);
	                this.str = str.slice(i);
	                this.node = {
	                    nodeName: '#text',
	                    nodeValue: nodeValue
	                };
	                if (rcontent.test(nodeValue)) {
	                    this.tryGenChildren(); //不收集空白节点
	                }
	            }
	        },
	        tryGenComment: function tryGenComment() {
	            if (!this.node) {
	                var str = this.str;
	                var i = str.indexOf('<!--'); //处理注释节点
	                /* istanbul ignore if*/
	                if (i === 0) {
	                    var l = str.indexOf('-->');
	                    if (l === -1) {
	                        avalon.error('注释节点没有闭合' + str);
	                    }
	                    var nodeValue = str.slice(4, l).replace(rfill, fill);
	                    this.str = str.slice(l + 3);
	                    this.node = {
	                        nodeName: '#comment',
	                        nodeValue: nodeValue
	                    };
	                    this.tryGenChildren();
	                }
	            }
	        },
	        tryGenOpenTag: function tryGenOpenTag() {
	            if (!this.node) {
	                var str = this.str;
	                var match = str.match(ropenTag); //处理元素节点开始部分
	                if (match) {
	                    var nodeName = match[1];
	                    var props = {};
	                    if (/^[A-Z]/.test(nodeName) && avalon.components[nodeName]) {
	                        props.is = nodeName;
	                    }
	                    nodeName = nodeName.toLowerCase();
	                    var isVoidTag = !!voidTag[nodeName] || match[3] === '\/';
	                    var node = this.node = {
	                        nodeName: nodeName,
	                        props: {},
	                        children: [],
	                        isVoidTag: isVoidTag
	                    };
	                    var attrs = match[2];
	                    if (attrs) {
	                        this.genProps(attrs, node.props);
	                    }
	                    this.tryGenChildren();
	                    str = str.slice(match[0].length);
	                    if (isVoidTag) {
	                        node.end = true;
	                    } else {
	                        this.stack.push(node);
	                        if (orphanTag[nodeName] || nodeName === 'option') {
	                            var index = str.indexOf('</' + nodeName + '>');
	                            var innerHTML = str.slice(0, index).trim();
	                            str = str.slice(index);
	                            makeOrphan(node, nodeName, nomalString(innerHTML));
	                        }
	                    }
	                    this.str = str;
	                }
	            }
	        },
	        tryGenCloseTag: function tryGenCloseTag() {
	            if (!this.node) {
	                var str = this.str;
	                var match = str.match(rendTag); //处理元素节点结束部分
	                if (match) {
	                    var nodeName = match[1].toLowerCase();
	                    var last = this.stack.last();
	                    /* istanbul ignore if*/
	                    if (!last) {
	                        avalon.error(match[0] + '前面缺少<' + nodeName + '>');
	                        /* istanbul ignore else*/
	                    } else if (last.nodeName !== nodeName) {
	                        var errMsg = last.nodeName + '没有闭合,请注意属性的引号';
	                        avalon.warn(errMsg);
	                        avalon.error(errMsg);
	                    }
	                    var node = this.stack.pop();
	                    node.end = true;
	                    this.node = node;
	                    this.str = str.slice(match[0].length);
	                }
	            }
	        },
	        tryGenChildren: function tryGenChildren() {
	            var node = this.node;
	            var p = this.stack.last();
	            if (p) {
	                validateDOMNesting(p, node);
	                p.children.push(node);
	            } else {
	                this.ret.push(node);
	            }
	        },
	        genProps: function genProps(attrs, props) {
	
	            while (attrs) {
	                var arr = rattrs.exec(attrs);
	
	                if (arr) {
	                    var name = arr[1];
	                    var value = arr[2] || '';
	                    attrs = attrs.replace(arr[0], '');
	                    if (value) {
	                        //https://github.com/RubyLouvre/avalon/issues/1844
	                        if (value.indexOf('??') === 0) {
	                            value = nomalString(value).replace(rlineSp, '').slice(1, -1);
	                        }
	                    }
	                    if (!(name in props)) {
	                        props[name] = value;
	                    }
	                } else {
	                    break;
	                }
	            }
	        }
	    };
	
	    var vdomAst = new AST();
	
	    function from(str) {
	        var cacheKey = str;
	        var cached = strCache.get(cacheKey);
	        if (cached) {
	            return avalon.mix(true, [], cached);
	        }
	        stringPool.map = {};
	        str = clearString(str);
	
	        vdomAst.init(str);
	        var ret = vdomAst.gen();
	        strCache.put(cacheKey, avalon.mix(true, [], ret));
	        return ret;
	    }
	
	    var rhtml = /<|&#?\w+;/;
	    var htmlCache = new Cache(128);
	    var rxhtml = /<(?!area|br|col|embed|hr|img|input|link|meta|param)(([\w:]+)[^>]*)\/>/ig;
	
	    avalon.parseHTML = function (html) {
	        var fragment = createFragment();
	        //处理非字符串
	        if (typeof html !== 'string') {
	            return fragment;
	        }
	        //处理非HTML字符串
	        if (!rhtml.test(html)) {
	            return document$1.createTextNode(html);
	        }
	
	        html = html.replace(rxhtml, '<$1></$2>').trim();
	        var hasCache = htmlCache.get(html);
	        if (hasCache) {
	            return avalon.cloneNode(hasCache);
	        }
	        var vnodes = fromString(html);
	        for (var i = 0, el; el = vnodes[i++];) {
	            var child = avalon.vdom(el, 'toDOM');
	            fragment.appendChild(child);
	        }
	        if (html.length < 1024) {
	            htmlCache.put(html, fragment);
	        }
	        return fragment;
	    };
	
	    avalon.innerHTML = function (node, html) {
	        var parsed = avalon.parseHTML(html);
	        this.clearHTML(node);
	        node.appendChild(parsed);
	    };
	
	    //https://github.com/karloespiritu/escapehtmlent/blob/master/index.js
	    avalon.unescapeHTML = function (html) {
	        return String(html).replace(/&quot;/g, '"').replace(/&#39;/g, '\'').replace(/&lt;/g, '<').replace(/&gt;/g, '>').replace(/&amp;/g, '&');
	    };
	
	    avalon.clearHTML = function (node) {
	        /* istanbul ignore next */
	        while (node.lastChild) {
	            node.removeChild(node.lastChild);
	        }
	        return node;
	    };
	
	    //http://www.feiesoft.com/html/events.html
	    //http://segmentfault.com/q/1010000000687977/a-1020000000688757
	    var canBubbleUp = {
	        click: true,
	        dblclick: true,
	        keydown: true,
	        keypress: true,
	        keyup: true,
	        mousedown: true,
	        mousemove: true,
	        mouseup: true,
	        mouseover: true,
	        mouseout: true,
	        wheel: true,
	        mousewheel: true,
	        input: true,
	        change: true,
	        beforeinput: true,
	        compositionstart: true,
	        compositionupdate: true,
	        compositionend: true,
	        select: true,
	        //http://blog.csdn.net/lee_magnum/article/details/17761441
	        cut: true,
	        copy: true,
	        paste: true,
	        beforecut: true,
	        beforecopy: true,
	        beforepaste: true,
	        focusin: true,
	        focusout: true,
	        DOMFocusIn: true,
	        DOMFocusOut: true,
	        DOMActivate: true,
	        dragend: true,
	        datasetchanged: true
	    };
	
	    /* istanbul ignore if */
	    var hackSafari = avalon.modern && document$1.ontouchstart;
	
	    //添加fn.bind, fn.unbind, bind, unbind
	    avalon.fn.bind = function (type, fn, phase) {
	        if (this[0]) {
	            //此方法不会链
	            return avalon.bind(this[0], type, fn, phase);
	        }
	    };
	
	    avalon.fn.unbind = function (type, fn, phase) {
	        if (this[0]) {
	            var args = _slice.call(arguments);
	            args.unshift(this[0]);
	            avalon.unbind.apply(0, args);
	        }
	        return this;
	    };
	
	    /*绑定事件*/
	    avalon.bind = function (elem, type, fn) {
	        if (elem.nodeType === 1) {
	            var value = elem.getAttribute('avalon-events') || '';
	            //如果是使用ms-on-*绑定的回调,其uuid格式为e12122324,
	            //如果是使用bind方法绑定的回调,其uuid格式为_12
	            var uuid = getShortID(fn);
	            var hook = eventHooks[type];
	            /* istanbul ignore if */
	            if (type === 'click' && hackSafari) {
	                elem.addEventListener('click', avalon.noop);
	            }
	            /* istanbul ignore if */
	            if (hook) {
	                type = hook.type || type;
	                if (hook.fix) {
	                    fn = hook.fix(elem, fn);
	                    fn.uuid = uuid;
	                }
	            }
	            var key = type + ':' + uuid;
	            avalon.eventListeners[fn.uuid] = fn;
	            /* istanbul ignore if */
	            if (value.indexOf(type + ':') === -1) {
	                //同一种事件只绑定一次
	                if (canBubbleUp[type] || avalon.modern && focusBlur[type]) {
	                    delegateEvent(type);
	                } else {
	                    avalon._nativeBind(elem, type, dispatch);
	                }
	            }
	            var keys = value.split(',');
	            /* istanbul ignore if */
	            if (keys[0] === '') {
	                keys.shift();
	            }
	            if (keys.indexOf(key) === -1) {
	                keys.push(key);
	                setEventId(elem, keys.join(','));
	                //将令牌放进avalon-events属性中
	            }
	            return fn;
	        } else {
	            /* istanbul ignore next */
	            var cb = function cb(e) {
	                fn.call(elem, new avEvent(e));
	            };
	
	            avalon._nativeBind(elem, type, cb);
	            return cb;
	        }
	    };
	
	    function setEventId(node, value) {
	        node.setAttribute('avalon-events', value);
	    }
	    /* istanbul ignore next */
	    avalon.unbind = function (elem, type, fn) {
	        if (elem.nodeType === 1) {
	            var value = elem.getAttribute('avalon-events') || '';
	            switch (arguments.length) {
	                case 1:
	                    avalon._nativeUnBind(elem, type, dispatch);
	                    elem.removeAttribute('avalon-events');
	                    break;
	                case 2:
	                    value = value.split(',').filter(function (str) {
	                        return str.indexOf(type + ':') === -1;
	                    }).join(',');
	                    setEventId(elem, value);
	                    break;
	                default:
	                    var search = type + ':' + fn.uuid;
	                    value = value.split(',').filter(function (str) {
	                        return str !== search;
	                    }).join(',');
	                    setEventId(elem, value);
	                    delete avalon.eventListeners[fn.uuid];
	                    break;
	            }
	        } else {
	            avalon._nativeUnBind(elem, type, fn);
	        }
	    };
	
	    var typeRegExp = {};
	
	    function collectHandlers(elem, type, handlers) {
	        var value = elem.getAttribute('avalon-events');
	        if (value && (elem.disabled !== true || type !== 'click')) {
	            var uuids = [];
	            var reg = typeRegExp[type] || (typeRegExp[type] = new RegExp("\\b" + type + '\\:([^,\\s]+)', 'g'));
	            value.replace(reg, function (a, b) {
	                uuids.push(b);
	                return a;
	            });
	            if (uuids.length) {
	                handlers.push({
	                    elem: elem,
	                    uuids: uuids
	                });
	            }
	        }
	        elem = elem.parentNode;
	        var g = avalon.gestureEvents || {};
	        if (elem && elem.getAttribute && (canBubbleUp[type] || g[type])) {
	            collectHandlers(elem, type, handlers);
	        }
	    }
	
	    var rhandleHasVm = /^e/;
	
	    function dispatch(event) {
	        event = new avEvent(event);
	        var type = event.type;
	        var elem = event.target;
	        var handlers = [];
	        collectHandlers(elem, type, handlers);
	        var i = 0,
	            j,
	            uuid,
	            handler;
	        while ((handler = handlers[i++]) && !event.cancelBubble) {
	            var host = event.currentTarget = handler.elem;
	            j = 0;
	            while (uuid = handler.uuids[j++]) {
	                if (event.stopImmediate) {
	                    break;
	                }
	                var fn = avalon.eventListeners[uuid];
	                if (fn) {
	                    var vm = rhandleHasVm.test(uuid) ? handler.elem._ms_context_ : 0;
	                    if (vm && vm.$hashcode === false) {
	                        return avalon.unbind(elem, type, fn);
	                    }
	                    var ret = fn.call(vm || elem, event);
	
	                    if (ret === false) {
	                        event.preventDefault();
	                        event.stopPropagation();
	                    }
	                }
	            }
	        }
	    }
	
	    var focusBlur = {
	        focus: true,
	        blur: true
	    };
	
	    function delegateEvent(type) {
	        var value = root.getAttribute('delegate-events') || '';
	        if (value.indexOf(type) === -1) {
	            //IE6-8会多次绑定同种类型的同一个函数,其他游览器不会
	            var arr = value.match(avalon.rword) || [];
	            arr.push(type);
	            root.setAttribute('delegate-events', arr.join(','));
	            avalon._nativeBind(root, type, dispatch, !!focusBlur[type]);
	        }
	    }
	
	    var eventProto = {
	        webkitMovementY: 1,
	        webkitMovementX: 1,
	        keyLocation: 1,
	        fixEvent: function fixEvent() {},
	        preventDefault: function preventDefault() {
	            var e = this.originalEvent || {};
	            e.returnValue = this.returnValue = false;
	            if (modern && e.preventDefault) {
	                e.preventDefault();
	            }
	        },
	        stopPropagation: function stopPropagation() {
	            var e = this.originalEvent || {};
	            e.cancelBubble = this.cancelBubble = true;
	            if (modern && e.stopPropagation) {
	                e.stopPropagation();
	            }
	        },
	        stopImmediatePropagation: function stopImmediatePropagation() {
	            this.stopPropagation();
	            this.stopImmediate = true;
	        },
	        toString: function toString() {
	            return '[object Event]'; //#1619
	        }
	    };
	
	    function avEvent(event) {
	        if (event.originalEvent) {
	            return event;
	        }
	        for (var i in event) {
	            if (!eventProto[i]) {
	                this[i] = event[i];
	            }
	        }
	        if (!this.target) {
	            this.target = event.srcElement;
	        }
	        var target = this.target;
	        this.fixEvent();
	        this.timeStamp = new Date() - 0;
	        this.originalEvent = event;
	    }
	    avEvent.prototype = eventProto;
	    //针对firefox, chrome修正mouseenter, mouseleave
	    /* istanbul ignore if */
	    if (!('onmouseenter' in root)) {
	        avalon.each({
	            mouseenter: 'mouseover',
	            mouseleave: 'mouseout'
	        }, function (origType, fixType) {
	            eventHooks[origType] = {
	                type: fixType,
	                fix: function fix(elem, fn) {
	                    return function (e) {
	                        var t = e.relatedTarget;
	                        if (!t || t !== elem && !(elem.compareDocumentPosition(t) & 16)) {
	                            delete e.type;
	                            e.type = origType;
	                            return fn.apply(this, arguments);
	                        }
	                    };
	                }
	            };
	        });
	    }
	    //针对IE9+, w3c修正animationend
	    avalon.each({
	        AnimationEvent: 'animationend',
	        WebKitAnimationEvent: 'webkitAnimationEnd'
	    }, function (construct, fixType) {
	        if (window$1[construct] && !eventHooks.animationend) {
	            eventHooks.animationend = {
	                type: fixType
	            };
	        }
	    });
	
	    /* istanbul ignore if */
	    if (!("onmousewheel" in document$1)) {
	        /* IE6-11 chrome mousewheel wheelDetla 下 -120 上 120
	         firefox DOMMouseScroll detail 下3 上-3
	         firefox wheel detlaY 下3 上-3
	         IE9-11 wheel deltaY 下40 上-40
	         chrome wheel deltaY 下100 上-100 */
	        var fixWheelType = document$1.onwheel !== void 0 ? 'wheel' : 'DOMMouseScroll';
	        var fixWheelDelta = fixWheelType === 'wheel' ? 'deltaY' : 'detail';
	        eventHooks.mousewheel = {
	            type: fixWheelType,
	            fix: function fix(elem, fn) {
	                return function (e) {
	                    var delta = e[fixWheelDelta] > 0 ? -120 : 120;
	                    e.wheelDelta = ~~elem._ms_wheel_ + delta;
	                    elem._ms_wheel_ = e.wheelDeltaY = e.wheelDelta;
	                    e.wheelDeltaX = 0;
	                    if (Object.defineProperty) {
	                        Object.defineProperty(e, 'type', {
	                            value: 'mousewheel'
	                        });
	                    }
	                    return fn.apply(this, arguments);
	                };
	            }
	        };
	    }
	
	    /* istanbul ignore if */
	    if (!modern) {
	        delete canBubbleUp.change;
	        delete canBubbleUp.select;
	    }
	    /* istanbul ignore next */
	    avalon._nativeBind = modern ? function (el, type, fn, capture) {
	        el.addEventListener(type, fn, !!capture);
	    } : function (el, type, fn) {
	        el.attachEvent('on' + type, fn);
	    };
	    /* istanbul ignore next */
	    avalon._nativeUnBind = modern ? function (el, type, fn, a) {
	        el.removeEventListener(type, fn, !!a);
	    } : function (el, type, fn) {
	        el.detachEvent('on' + type, fn);
	    };
	    /* istanbul ignore next */
	    avalon.fireDom = function (elem, type, opts) {
	        if (document$1.createEvent) {
	            var hackEvent = document$1.createEvent('Events');
	            hackEvent.initEvent(type, true, true, opts);
	            avalon.shadowCopy(hackEvent, opts);
	            elem.dispatchEvent(hackEvent);
	        } else if (root.contains(elem)) {
	            //IE6-8触发事件必须保证在DOM树中,否则报'SCRIPT16389: 未指明的错误'
	            hackEvent = document$1.createEventObject();
	            if (opts) avalon.shadowCopy(hackEvent, opts);
	            try {
	                elem.fireEvent('on' + type, hackEvent);
	            } catch (e) {
	                avalon.log('fireDom', type, 'args error');
	            }
	        }
	    };
	
	    var rmouseEvent = /^(?:mouse|contextmenu|drag)|click/;
	    /* istanbul ignore next */
	    avEvent.prototype.fixEvent = function () {
	        var event = this;
	        if (event.which == null && event.type.indexOf('key') === 0) {
	            event.which = event.charCode != null ? event.charCode : event.keyCode;
	        }
	        if (rmouseEvent.test(event.type) && !('pageX' in event)) {
	            var DOC = event.target.ownerDocument || document$1;
	            var box = DOC.compatMode === 'BackCompat' ? DOC.body : DOC.documentElement;
	            event.pageX = event.clientX + (box.scrollLeft >> 0) - (box.clientLeft >> 0);
	            event.pageY = event.clientY + (box.scrollTop >> 0) - (box.clientTop >> 0);
	            event.wheelDeltaY = ~~event.wheelDelta;
	            event.wheelDeltaX = 0;
	        }
	    };
	
	    //针对IE6-8修正input
	    /* istanbul ignore if */
	    if (!('oninput' in document$1.createElement('input'))) {
	        eventHooks.input = {
	            type: 'propertychange',
	            fix: function fix(elem, fn) {
	                return function (e) {
	                    if (e.propertyName === 'value') {
	                        e.type = 'input';
	                        return fn.apply(this, arguments);
	                    }
	                };
	            }
	        };
	    }
	
	    var readyList = [];
	
	    function fireReady(fn) {
	        avalon.isReady = true;
	        while (fn = readyList.shift()) {
	            fn(avalon);
	        }
	    }
	
	    avalon.ready = function (fn) {
	        readyList.push(fn);
	        if (avalon.isReady) {
	            fireReady();
	        }
	    };
	
	    avalon.ready(function () {
	        avalon.scan && avalon.scan(document$1.body);
	    });
	
	    /* istanbul ignore next */
	    function bootstrap() {
	        function doScrollCheck() {
	            try {
	                //IE下通过doScrollCheck检测DOM树是否建完
	                root.doScroll('left');
	                fireReady();
	            } catch (e) {
	                setTimeout(doScrollCheck);
	            }
	        }
	        if (document$1.readyState === 'complete') {
	            setTimeout(fireReady); //如果在domReady之外加载
	        } else if (document$1.addEventListener) {
	            document$1.addEventListener('DOMContentLoaded', fireReady, false);
	        } else if (document$1.attachEvent) {
	            //必须传入三个参数，否则在firefox4-26中报错
	            //caught exception: [Exception... "Not enough arguments"  nsresult: "0x
	            document$1.attachEvent('onreadystatechange', function () {
	                if (document$1.readyState === 'complete') {
	                    fireReady();
	                }
	            });
	            try {
	                var isTop = window$1.frameElement === null;
	            } catch (e) {}
	            if (root.doScroll && isTop && window$1.external) {
	                //fix IE iframe BUG
	                doScrollCheck();
	            }
	        }
	
	        avalon.bind(window$1, 'load', fireReady);
	    }
	    if (inBrowser) {
	        bootstrap();
	    }
	
	    /**
	     * ------------------------------------------------------------
	     *                          DOM Api
	     * shim,class,data,css,val,html,event,ready  
	     * ------------------------------------------------------------
	     */
	
	    function fromDOM(dom) {
	        return [from$1(dom)];
	    }
	
	    function from$1(node) {
	        var type = node.nodeName.toLowerCase();
	        switch (type) {
	            case '#text':
	            case '#comment':
	                return {
	                    nodeName: type,
	                    dom: node,
	                    nodeValue: node.nodeValue
	                };
	            default:
	                var props = markProps(node, node.attributes || []);
	                var vnode = {
	                    nodeName: type,
	                    dom: node,
	                    isVoidTag: !!voidTag[type],
	                    props: props
	                };
	                if (type === 'option') {
	                    //即便你设置了option.selected = true,
	                    //option.attributes也找不到selected属性
	                    props.selected = node.selected;
	                }
	                if (orphanTag[type] || type === 'option') {
	                    makeOrphan(vnode, type, node.text || node.innerHTML);
	                    if (node.childNodes.length === 1) {
	                        vnode.children[0].dom = node.firstChild;
	                    }
	                } else if (!vnode.isVoidTag) {
	                    vnode.children = [];
	                    for (var i = 0, el; el = node.childNodes[i++];) {
	                        var child = from$1(el);
	                        if (/\S/.test(child.nodeValue)) {
	                            vnode.children.push(child);
	                        }
	                    }
	                }
	                return vnode;
	        }
	    }
	
	    var rformElement = /input|textarea|select/i;
	
	    function markProps(node, attrs) {
	        var ret = {};
	        for (var i = 0, n = attrs.length; i < n; i++) {
	            var attr = attrs[i];
	            if (attr.specified) {
	                //IE6-9不会将属性名变小写,比如它会将用户的contenteditable变成contentEditable
	                ret[attr.name.toLowerCase()] = attr.value;
	            }
	        }
	        if (rformElement.test(node.nodeName)) {
	            ret.type = node.type;
	            var a = node.getAttributeNode('value');
	            if (a && /\S/.test(a.value)) {
	                //IE6,7中无法取得checkbox,radio的value
	                ret.value = a.value;
	            }
	        }
	        var style = node.style.cssText;
	        if (style) {
	            ret.style = style;
	        }
	        //类名 = 去重(静态类名+动态类名+ hover类名? + active类名)
	        if (ret.type === 'select-one') {
	            ret.selectedIndex = node.selectedIndex;
	        }
	        return ret;
	    }
	
	    function VText(text) {
	        this.nodeName = '#text';
	        this.nodeValue = text;
	    }
	
	    VText.prototype = {
	        constructor: VText,
	        toDOM: function toDOM() {
	            /* istanbul ignore if*/
	            if (this.dom) return this.dom;
	            var v = avalon._decode(this.nodeValue);
	            return this.dom = document$1.createTextNode(v);
	        },
	        toHTML: function toHTML() {
	            return this.nodeValue;
	        }
	    };
	
	    function VComment(text) {
	        this.nodeName = '#comment';
	        this.nodeValue = text;
	    }
	    VComment.prototype = {
	        constructor: VComment,
	        toDOM: function toDOM() {
	            if (this.dom) return this.dom;
	            return this.dom = document$1.createComment(this.nodeValue);
	        },
	        toHTML: function toHTML() {
	            return '<!--' + this.nodeValue + '-->';
	        }
	    };
	
	    function VElement(type, props, children, isVoidTag) {
	        this.nodeName = type;
	        this.props = props;
	        this.children = children;
	        this.isVoidTag = isVoidTag;
	    }
	    VElement.prototype = {
	        constructor: VElement,
	        toDOM: function toDOM() {
	            if (this.dom) return this.dom;
	            var dom,
	                tagName = this.nodeName;
	            if (avalon.modern && svgTags[tagName]) {
	                dom = createSVG(tagName);
	                /* istanbul ignore next*/
	            } else if (!avalon.modern && (VMLTags[tagName] || rvml.test(tagName))) {
	                dom = createVML(tagName);
	            } else {
	                dom = document$1.createElement(tagName);
	            }
	
	            var props = this.props || {};
	
	            for (var i in props) {
	                var val = props[i];
	                if (skipFalseAndFunction(val)) {
	                    /* istanbul ignore if*/
	                    if (specalAttrs[i] && avalon.msie < 8) {
	                        specalAttrs[i](dom, val);
	                    } else {
	                        dom.setAttribute(i, val + '');
	                    }
	                }
	            }
	            var c = this.children || [];
	            var template = c[0] ? c[0].nodeValue : '';
	            switch (this.nodeName) {
	                case 'script':
	                    dom.type = 'noexec';
	                    dom.text = template;
	                    try {
	                        dom.innerHTML = template;
	                    } catch (e) {}
	                    dom.type = props.type || '';
	                    break;
	                case 'noscript':
	                    dom.textContent = template;
	                case 'style':
	                case 'xmp':
	                case 'template':
	                    try {
	                        dom.innerHTML = template;
	                    } catch (e) {
	                        /* istanbul ignore next*/
	                        hackIE(dom, this.nodeName, template);
	                    }
	                    break;
	                case 'option':
	                    //IE6-8,为option添加文本子节点,不会同步到text属性中
	                    /* istanbul ignore next */
	                    if (msie < 9) dom.text = template;
	                default:
	                    /* istanbul ignore next */
	                    if (!this.isVoidTag && this.children) {
	                        this.children.forEach(function (el) {
	                            return c && dom.appendChild(avalon.vdom(c, 'toDOM'));
	                        });
	                    }
	                    break;
	            }
	            return this.dom = dom;
	        },
	
	        /* istanbul ignore next */
	
	        toHTML: function toHTML() {
	            var arr = [];
	            var props = this.props || {};
	            for (var i in props) {
	                var val = props[i];
	                if (skipFalseAndFunction(val)) {
	                    arr.push(i + '=' + avalon.quote(props[i] + ''));
	                }
	            }
	            arr = arr.length ? ' ' + arr.join(' ') : '';
	            var str = '<' + this.nodeName + arr;
	            if (this.isVoidTag) {
	                return str + '/>';
	            }
	            str += '>';
	            if (this.children) {
	                str += this.children.map(function (el) {
	                    return el ? avalon.vdom(el, 'toHTML') : '';
	                }).join('');
	            }
	            return str + '</' + this.nodeName + '>';
	        }
	    };
	    function hackIE(dom, nodeName, template) {
	        switch (nodeName) {
	            case 'style':
	                dom.setAttribute('type', 'text/css');
	                dom.styleSheet.cssText = template;
	                break;
	            case 'xmp': //IE6-8,XMP元素里面只能有文本节点,不能使用innerHTML
	            case 'noscript':
	                dom.textContent = template;
	                break;
	        }
	    }
	    function skipFalseAndFunction(a) {
	        return a !== false && Object(a) !== a;
	    }
	    /* istanbul ignore next */
	    var specalAttrs = {
	        "class": function _class(dom, val) {
	            dom.className = val;
	        },
	        style: function style(dom, val) {
	            dom.style.cssText = val;
	        },
	        type: function type(dom, val) {
	            try {
	                //textarea,button 元素在IE6,7设置 type 属性会抛错
	                dom.type = val;
	            } catch (e) {}
	        },
	        'for': function _for(dom, val) {
	            dom.setAttribute('for', val);
	            dom.htmlFor = val;
	        }
	    };
	
	    function createSVG(type) {
	        return document$1.createElementNS('http://www.w3.org/2000/svg', type);
	    }
	    var svgTags = avalon.oneObject('circle,defs,ellipse,image,line,' + 'path,polygon,polyline,rect,symbol,text,use,g,svg');
	
	    var rvml = /^\w+\:\w+/;
	    /* istanbul ignore next*/
	    function createVML(type) {
	        if (document$1.styleSheets.length < 31) {
	            document$1.createStyleSheet().addRule(".rvml", "behavior:url(#default#VML)");
	        } else {
	            // no more room, add to the existing one
	            // http://msdn.microsoft.com/en-us/library/ms531194%28VS.85%29.aspx
	            document$1.styleSheets[0].addRule(".rvml", "behavior:url(#default#VML)");
	        }
	        var arr = type.split(':');
	        if (arr.length === 1) {
	            arr.unshift('v');
	        }
	        var tag = arr[1];
	        var ns = arr[0];
	        if (!document$1.namespaces[ns]) {
	            document$1.namespaces.add(ns, "urn:schemas-microsoft-com:vml");
	        }
	        return document$1.createElement('<' + ns + ':' + tag + ' class="rvml">');
	    }
	
	    var VMLTags = avalon.oneObject('shape,line,polyline,rect,roundrect,oval,arc,' + 'curve,background,image,shapetype,group,fill,' + 'stroke,shadow, extrusion, textbox, imagedata, textpath');
	
	    function VFragment(children, key, val, index) {
	        this.nodeName = '#document-fragment';
	        this.children = children;
	        this.key = key;
	        this.val = val;
	        this.index = index;
	        this.props = {};
	    }
	    VFragment.prototype = {
	        constructor: VFragment,
	        toDOM: function toDOM() {
	            if (this.dom) return this.dom;
	            var f = this.toFragment();
	            //IE6-11 docment-fragment都没有children属性 
	            this.split = f.lastChild;
	            return this.dom = f;
	        },
	        dispose: function dispose() {
	            this.toFragment();
	            this.innerRender && this.innerRender.dispose();
	            for (var i in this) {
	                this[i] = null;
	            }
	        },
	        toFragment: function toFragment() {
	            var f = createFragment();
	            this.children.forEach(function (el) {
	                return f.appendChild(avalon.vdom(el, 'toDOM'));
	            });
	            return f;
	        },
	        toHTML: function toHTML() {
	            var c = this.children;
	            return c.map(function (el) {
	                return avalon.vdom(el, 'toHTML');
	            }).join('');
	        }
	    };
	
	    /**
	     * 虚拟DOM的4大构造器
	     */
	    avalon.mix(avalon, {
	        VText: VText,
	        VComment: VComment,
	        VElement: VElement,
	        VFragment: VFragment
	    });
	
	    var constNameMap = {
	        '#text': 'VText',
	        '#document-fragment': 'VFragment',
	        '#comment': 'VComment'
	    };
	
	    var vdom = avalon.vdomAdaptor = avalon.vdom = function (obj, method) {
	        if (!obj) {
	            //obj在ms-for循环里面可能是null
	            return method === "toHTML" ? '' : createFragment();
	        }
	        var nodeName = obj.nodeName;
	        if (!nodeName) {
	            return new avalon.VFragment(obj)[method]();
	        }
	        var constName = constNameMap[nodeName] || 'VElement';
	        return avalon[constName].prototype[method].call(obj);
	    };
	
	    avalon.domize = function (a) {
	        return avalon.vdom(a, 'toDOM');
	    };
	
	    avalon.pendingActions = [];
	    avalon.uniqActions = {};
	    avalon.inTransaction = 0;
	    config.trackDeps = false;
	    avalon.track = function () {
	        if (config.trackDeps) {
	            avalon.log.apply(avalon, arguments);
	        }
	    };
	
	    /**
	     * Batch is a pseudotransaction, just for purposes of memoizing ComputedValues when nothing else does.
	     * During a batch `onBecomeUnobserved` will be called at most once per observable.
	     * Avoids unnecessary recalculations.
	     */
	
	    function runActions() {
	        if (avalon.isRunningActions === true || avalon.inTransaction > 0) return;
	        avalon.isRunningActions = true;
	        var tasks = avalon.pendingActions.splice(0, avalon.pendingActions.length);
	        for (var i = 0, task; task = tasks[i++];) {
	            task.update();
	            delete avalon.uniqActions[task.uuid];
	        }
	        avalon.isRunningActions = false;
	    }
	
	    function propagateChanged(target) {
	        var list = target.observers;
	        for (var i = 0, el; el = list[i++];) {
	            el.schedule(); //通知action, computed做它们该做的事
	        }
	    }
	
	    //将自己抛到市场上卖
	    function reportObserved(target) {
	        var action = avalon.trackingAction || null;
	        if (action !== null) {
	
	            avalon.track('征收到', target.expr);
	            action.mapIDs[target.uuid] = target;
	        }
	    }
	
	    var targetStack = [];
	
	    function collectDeps(action, getter) {
	        if (!action.observers) return;
	        var preAction = avalon.trackingAction;
	        if (preAction) {
	            targetStack.push(preAction);
	        }
	        avalon.trackingAction = action;
	        avalon.track('【action】', action.type, action.expr, '开始征收依赖项');
	        //多个observe持有同一个action
	        action.mapIDs = {}; //重新收集依赖
	        var hasError = true,
	            result;
	        try {
	            result = getter.call(action);
	            hasError = false;
	        } finally {
	            if (hasError) {
	                avalon.warn('collectDeps fail', getter + '');
	                action.mapIDs = {};
	                avalon.trackingAction = preAction;
	            } else {
	                // 确保它总是为null
	                avalon.trackingAction = targetStack.pop();
	                try {
	                    resetDeps(action);
	                } catch (e) {
	                    avalon.warn(e);
	                }
	            }
	            return result;
	        }
	    }
	
	    function resetDeps(action) {
	        var prev = action.observers,
	            curr = [],
	            checked = {},
	            ids = [];
	        for (var i in action.mapIDs) {
	            var dep = action.mapIDs[i];
	            if (!dep.isAction) {
	                if (!dep.observers) {
	                    //如果它已经被销毁
	                    delete action.mapIDs[i];
	                    continue;
	                }
	                ids.push(dep.uuid);
	                curr.push(dep);
	                checked[dep.uuid] = 1;
	                if (dep.lastAccessedBy === action.uuid) {
	                    continue;
	                }
	                dep.lastAccessedBy = action.uuid;
	                avalon.Array.ensure(dep.observers, action);
	            }
	        }
	        var ids = ids.sort().join(',');
	        if (ids === action.ids) {
	            return;
	        }
	        action.ids = ids;
	        if (!action.isComputed) {
	            action.observers = curr;
	        } else {
	            action.depsCount = curr.length;
	            action.deps = avalon.mix({}, action.mapIDs);
	            action.depsVersion = {};
	            for (var _i in action.mapIDs) {
	                var _dep = action.mapIDs[_i];
	                action.depsVersion[_dep.uuid] = _dep.version;
	            }
	        }
	
	        for (var _i2 = 0, _dep2; _dep2 = prev[_i2++];) {
	            if (!checked[_dep2.uuid]) {
	                avalon.Array.remove(_dep2.observers, action);
	            }
	        }
	    }
	
	    function transaction(action, thisArg, args) {
	        args = args || [];
	        var name = 'transaction ' + (action.name || action.displayName || 'noop');
	        transactionStart(name);
	        var res = action.apply(thisArg, args);
	        transactionEnd(name);
	        return res;
	    }
	    avalon.transaction = transaction;
	
	    function transactionStart(name) {
	        avalon.inTransaction += 1;
	    }
	
	    function transactionEnd(name) {
	        if (--avalon.inTransaction === 0) {
	            avalon.isRunningActions = false;
	            runActions();
	        }
	    }
	
	    var keyMap = avalon.oneObject("break,case,catch,continue,debugger,default,delete,do,else,false," + "finally,for,function,if,in,instanceof,new,null,return,switch,this," + "throw,true,try,typeof,var,void,while,with," + /* 关键字*/
	    "abstract,boolean,byte,char,class,const,double,enum,export,extends," + "final,float,goto,implements,import,int,interface,long,native," + "package,private,protected,public,short,static,super,synchronized," + "throws,transient,volatile");
	
	    var skipMap = avalon.mix({
	        Math: 1,
	        Date: 1,
	        $event: 1,
	        window: 1,
	        __vmodel__: 1,
	        avalon: 1
	    }, keyMap);
	
	    var rvmKey = /(^|[^\w\u00c0-\uFFFF_])(@|##)(?=[$\w])/g;
	    var ruselessSp = /\s*(\.|\|)\s*/g;
	    var rshortCircuit = /\|\|/g;
	    var brackets = /\(([^)]*)\)/;
	    var rpipeline = /\|(?=\?\?)/;
	    var rregexp = /(^|[^/])\/(?!\/)(\[.+?]|\\.|[^/\\\r\n])+\/[gimyu]{0,5}(?=\s*($|[\r\n,.;})]))/g;
	    var robjectProp = /\.[\w\.\$]+/g; //对象的属性 el.xxx 中的xxx
	    var robjectKey = /(\{|\,)\s*([\$\w]+)\s*:/g; //对象的键名与冒号 {xxx:1,yyy: 2}中的xxx, yyy
	    var rfilterName = /\|(\w+)/g;
	    var rlocalVar = /[$a-zA-Z_][$a-zA-Z0-9_]*/g;
	
	    var exprCache = new Cache(300);
	
	    function addScopeForLocal(str) {
	        return str.replace(robjectProp, dig).replace(rlocalVar, function (el) {
	            if (!skipMap[el]) {
	                return "__vmodel__." + el;
	            }
	            return el;
	        });
	    }
	
	    function addScope(expr, type) {
	        var cacheKey = expr + ':' + type;
	        var cache = exprCache.get(cacheKey);
	        if (cache) {
	            return cache.slice(0);
	        }
	
	        stringPool.map = {};
	        //https://github.com/RubyLouvre/avalon/issues/1849
	        var input = expr.replace(rregexp, function (a, b) {
	            return b + dig(a.slice(b.length));
	        }); //移除所有正则
	        input = clearString(input); //移除所有字符串
	        input = input.replace(rshortCircuit, dig). //移除所有短路运算符
	        replace(ruselessSp, '$1'). //移除.|两端空白
	
	        replace(robjectKey, function (_, a, b) {
	            //移除所有键名
	            return a + dig(b) + ':'; //比如 ms-widget="[{is:'ms-address-wrap', $id:'address'}]"这样极端的情况 
	        }).replace(rvmKey, '$1__vmodel__.'). //转换@与##为__vmodel__
	        replace(rfilterName, function (a, b) {
	            //移除所有过滤器的名字
	            return '|' + dig(b);
	        });
	        input = addScopeForLocal(input); //在本地变量前添加__vmodel__
	
	        var filters = input.split(rpipeline); //根据管道符切割表达式
	        var body = filters.shift().replace(rfill, fill).trim();
	        if (/\?\?\d/.test(body)) {
	            body = body.replace(rfill, fill);
	        }
	        if (filters.length) {
	            filters = filters.map(function (filter) {
	                var bracketArgs = '';
	                filter = filter.replace(brackets, function (a, b) {
	                    if (/\S/.test(b)) {
	                        bracketArgs += ',' + b; //还原字符串,正则,短路运算符
	                    }
	                    return '';
	                });
	                var arg = '[' + avalon.quote(filter.trim()) + bracketArgs + ']';
	                return arg;
	            });
	            filters = 'avalon.composeFilters(' + filters + ')(__value__)';
	            filters = filters.replace(rfill, fill);
	        } else {
	            filters = '';
	        }
	        return exprCache.put(cacheKey, [body, filters]);
	    }
	    var rhandleName = /^__vmodel__\.[$\w\.]+$/;
	    var rfixIE678 = /__vmodel__\.([^(]+)\(([^)]*)\)/;
	    function makeHandle(body) {
	        if (rhandleName.test(body)) {
	            body = body + '($event)';
	        }
	        /* istanbul ignore if */
	        if (msie < 9) {
	            body = body.replace(rfixIE678, function (a, b, c) {
	                return '__vmodel__.' + b + '.call(__vmodel__' + (/\S/.test(c) ? ',' + c : '') + ')';
	            });
	        }
	        return body;
	    }
	    function createGetter(expr, type) {
	        var arr = addScope(expr, type),
	            body;
	        if (!arr[1]) {
	            body = arr[0];
	        } else {
	            body = arr[1].replace(/__value__\)$/, arr[0] + ')');
	        }
	        try {
	            return new Function('__vmodel__', 'return ' + body + ';');
	            /* istanbul ignore next */
	        } catch (e) {
	            avalon.log('parse getter: [', expr, body, ']error');
	            return avalon.noop;
	        }
	    }
	
	    /**
	     * 生成表达式设值函数
	     * @param  {String}  expr
	     */
	    function createSetter(expr, type) {
	        var arr = addScope(expr, type);
	        var body = 'try{ ' + arr[0] + ' = __value__}catch(e){}';
	        try {
	            return new Function('__vmodel__', '__value__', body + ';');
	            /* istanbul ignore next */
	        } catch (e) {
	            avalon.log('parse setter: ', expr, ' error');
	            return avalon.noop;
	        }
	    }
	
	    var actionUUID = 1;
	    //需要重构
	    function Action(vm, options, callback) {
	        for (var i in options) {
	            if (protectedMenbers[i] !== 1) {
	                this[i] = options[i];
	            }
	        }
	
	        this.vm = vm;
	        this.observers = [];
	        this.callback = callback;
	        this.uuid = ++actionUUID;
	        this.ids = '';
	        this.mapIDs = {}; //这个用于去重
	        this.isAction = true;
	        var expr = this.expr;
	        // 缓存取值函数
	        if (typeof this.getter !== 'function') {
	            this.getter = createGetter(expr, this.type);
	        }
	        // 缓存设值函数（双向数据绑定）
	        if (this.type === 'duplex') {
	            this.setter = createSetter(expr, this.type);
	        }
	        // 缓存表达式旧值
	        this.value = NaN;
	        // 表达式初始值 & 提取依赖
	        if (!this.node) {
	            this.value = this.get();
	        }
	    }
	
	    Action.prototype = {
	        getValue: function getValue() {
	            var scope = this.vm;
	            try {
	                return this.getter.call(scope, scope);
	            } catch (e) {
	                avalon.log(this.getter + ' exec error');
	            }
	        },
	        setValue: function setValue(value) {
	            var scope = this.vm;
	            if (this.setter) {
	                this.setter.call(scope, scope, value);
	            }
	        },
	
	
	        // get --> getValue --> getter
	        get: function get(fn) {
	            var name = 'action track ' + this.type;
	
	            if (this.deep) {
	                avalon.deepCollect = true;
	            }
	
	            var value = collectDeps(this, this.getValue);
	            if (this.deep && avalon.deepCollect) {
	                avalon.deepCollect = false;
	            }
	
	            return value;
	        },
	
	
	        /**
	         * 在更新视图前保存原有的value
	         */
	        beforeUpdate: function beforeUpdate() {
	            var v = this.value;
	            return this.oldValue = v && v.$events ? v.$model : v;
	        },
	        update: function update(args, uuid) {
	            var oldVal = this.beforeUpdate();
	            var newVal = this.value = this.get();
	            var callback = this.callback;
	            if (callback && this.diff(newVal, oldVal, args)) {
	                callback.call(this.vm, this.value, oldVal, this.expr);
	            }
	            this._isScheduled = false;
	        },
	        schedule: function schedule() {
	            if (!this._isScheduled) {
	                this._isScheduled = true;
	                if (!avalon.uniqActions[this.uuid]) {
	                    avalon.uniqActions[this.uuid] = 1;
	                    avalon.pendingActions.push(this);
	                }
	
	                runActions(); //这里会还原_isScheduled
	
	            }
	        },
	        removeDepends: function removeDepends() {
	            var self = this;
	            this.observers.forEach(function (depend) {
	                avalon.Array.remove(depend.observers, self);
	            });
	        },
	
	
	        /**
	         * 比较两个计算值是否,一致,在for, class等能复杂数据类型的指令中,它们会重写diff复法
	         */
	        diff: function diff(a, b) {
	            return a !== b;
	        },
	
	
	        /**
	         * 销毁指令
	         */
	        dispose: function dispose() {
	            this.value = null;
	            this.removeDepends();
	            if (this.beforeDispose) {
	                this.beforeDispose();
	            }
	            for (var i in this) {
	                delete this[i];
	            }
	        }
	    };
	
	    var protectedMenbers = {
	        vm: 1,
	        callback: 1,
	
	        observers: 1,
	        oldValue: 1,
	        value: 1,
	        getValue: 1,
	        setValue: 1,
	        get: 1,
	
	        removeDepends: 1,
	        beforeUpdate: 1,
	        update: 1,
	        //diff
	        //getter
	        //setter
	        //expr
	        //vdom
	        //type: "for"
	        //name: "ms-for"
	        //attrName: ":for"
	        //param: "click"
	        //beforeDispose
	        dispose: 1
	    };
	
	    /**
	    * 
	     与Computed等共享UUID
	    */
	    var obid = 1;
	    function Mutation(expr, value, vm) {
	        //构造函数
	        this.expr = expr;
	        if (value) {
	            var childVm = platform.createProxy(value, this);
	            if (childVm) {
	                value = childVm;
	            }
	        }
	        this.value = value;
	        this.vm = vm;
	        try {
	            vm.$mutations[expr] = this;
	        } catch (ignoreIE) {}
	        this.uuid = ++obid;
	        this.updateVersion();
	        this.mapIDs = {};
	        this.observers = [];
	    }
	
	    Mutation.prototype = {
	        get: function get() {
	            if (avalon.trackingAction) {
	                this.collect(); //被收集
	                var childOb = this.value;
	                if (childOb && childOb.$events) {
	                    if (Array.isArray(childOb)) {
	                        childOb.forEach(function (item) {
	                            if (item && item.$events) {
	                                item.$events.__dep__.collect();
	                            }
	                        });
	                    } else if (avalon.deepCollect) {
	                        for (var key in childOb) {
	                            if (childOb.hasOwnProperty(key)) {
	                                var collectIt = childOb[key];
	                            }
	                        }
	                    }
	                }
	            }
	            return this.value;
	        },
	        collect: function collect() {
	            avalon.track(name, '被收集');
	            reportObserved(this);
	        },
	        updateVersion: function updateVersion() {
	            this.version = Math.random() + Math.random();
	        },
	        notify: function notify() {
	            transactionStart();
	            propagateChanged(this);
	            transactionEnd();
	        },
	        set: function set(newValue) {
	            var oldValue = this.value;
	            if (newValue !== oldValue) {
	                if (avalon.isObject(newValue)) {
	                    var hash = oldValue && oldValue.$hashcode;
	                    var childVM = platform.createProxy(newValue, this);
	                    if (childVM) {
	                        if (hash) {
	                            childVM.$hashcode = hash;
	                        }
	                        newValue = childVM;
	                    }
	                }
	                this.value = newValue;
	                this.updateVersion();
	                this.notify();
	            }
	        }
	    };
	
	    function getBody(fn) {
	        var entire = fn.toString();
	        return entire.substring(entire.indexOf('{}') + 1, entire.lastIndexOf('}'));
	    }
	    //如果不存在三目,if,方法
	    var instability = /(\?|if\b|\(.+\))/;
	
	    function __create(o) {
	        var __ = function __() {};
	        __.prototype = o;
	        return new __();
	    }
	
	    function __extends(child, parent) {
	        if (typeof parent === 'function') {
	            var proto = child.prototype = __create(parent.prototype);
	            proto.constructor = child;
	        }
	    }
	    var Computed = function (_super) {
	        __extends(Computed, _super);
	
	        function Computed(name, options, vm) {
	            //构造函数
	            _super.call(this, name, undefined, vm);
	            delete options.get;
	            delete options.set;
	
	            avalon.mix(this, options);
	            this.deps = {};
	            this.type = 'computed';
	            this.depsVersion = {};
	            this.isComputed = true;
	            this.trackAndCompute();
	            if (!('isStable' in this)) {
	                this.isStable = !instability.test(getBody(this.getter));
	            }
	        }
	        var cp = Computed.prototype;
	        cp.trackAndCompute = function () {
	            if (this.isStable && this.depsCount > 0) {
	                this.getValue();
	            } else {
	                collectDeps(this, this.getValue.bind(this));
	            }
	        };
	
	        cp.getValue = function () {
	            return this.value = this.getter.call(this.vm);
	        };
	
	        cp.schedule = function () {
	            var observers = this.observers;
	            var i = observers.length;
	            while (i--) {
	                var d = observers[i];
	                if (d.schedule) {
	                    d.schedule();
	                }
	            }
	        };
	
	        cp.shouldCompute = function () {
	            if (this.isStable) {
	                //如果变动因子确定,那么只比较变动因子的版本
	                var toComputed = false;
	                for (var i in this.deps) {
	                    if (this.deps[i].version !== this.depsVersion[i]) {
	                        toComputed = true;
	                        this.deps[i].version = this.depsVersion[i];
	                    }
	                }
	                return toComputed;
	            }
	            return true;
	        };
	        cp.set = function () {
	            if (this.setter) {
	                avalon.transaction(this.setter, this.vm, arguments);
	            }
	        };
	        cp.get = function () {
	
	            //当被设置了就不稳定,当它被访问了一次就是稳定
	            this.collect();
	
	            if (this.shouldCompute()) {
	                this.trackAndCompute();
	                // console.log('computed 2 分支')
	                this.updateVersion();
	                //  this.reportChanged()
	            }
	
	            //下面这一行好像没用
	            return this.value;
	        };
	        return Computed;
	    }(Mutation);
	
	    /**
	     * 这里放置ViewModel模块的共用方法
	     * avalon.define: 全框架最重要的方法,生成用户VM
	     * IProxy, 基本用户数据产生的一个数据对象,基于$model与vmodel之间的形态
	     * modelFactory: 生成用户VM
	     * canHijack: 判定此属性是否该被劫持,加入数据监听与分发的的逻辑
	     * createProxy: listFactory与modelFactory的封装
	     * createAccessor: 实现数据监听与分发的重要对象
	     * itemFactory: ms-for循环中产生的代理VM的生成工厂
	     * fuseFactory: 两个ms-controller间产生的代理VM的生成工厂
	     */
	
	    avalon.define = function (definition) {
	        var $id = definition.$id;
	        if (!$id) {
	            avalon.error('vm.$id must be specified');
	        }
	        if (avalon.vmodels[$id]) {
	            avalon.warn('error:[' + $id + '] had defined!');
	        }
	        var vm = platform.modelFactory(definition);
	        return avalon.vmodels[$id] = vm;
	    };
	
	    /**
	     * 在未来的版本,avalon改用Proxy来创建VM,因此
	     */
	
	    function IProxy(definition, dd) {
	        avalon.mix(this, definition);
	        avalon.mix(this, $$skipArray);
	        this.$hashcode = avalon.makeHashCode('$');
	        this.$id = this.$id || this.$hashcode;
	        this.$events = {
	            __dep__: dd || new Mutation(this.$id)
	        };
	        if (avalon.config.inProxyMode) {
	            delete this.$mutations;
	            this.$accessors = {};
	            this.$computed = {};
	            this.$track = '';
	        } else {
	            this.$accessors = {
	                $model: modelAccessor
	            };
	        }
	        if (dd === void 0) {
	            this.$watch = platform.watchFactory(this.$events);
	            this.$fire = platform.fireFactory(this.$events);
	        } else {
	            delete this.$watch;
	            delete this.$fire;
	        }
	    }
	
	    platform.modelFactory = function modelFactory(definition, dd) {
	        var $computed = definition.$computed || {};
	        delete definition.$computed;
	        var core = new IProxy(definition, dd);
	        var $accessors = core.$accessors;
	        var keys = [];
	
	        platform.hideProperty(core, '$mutations', {});
	
	        for (var key in definition) {
	            if (key in $$skipArray) continue;
	            var val = definition[key];
	            keys.push(key);
	            if (canHijack(key, val)) {
	                $accessors[key] = createAccessor(key, val);
	            }
	        }
	        for (var _key in $computed) {
	            if (_key in $$skipArray) continue;
	            var val = $computed[_key];
	            if (typeof val === 'function') {
	                val = {
	                    get: val
	                };
	            }
	            if (val && val.get) {
	                val.getter = val.get;
	                val.setter = val.set;
	                avalon.Array.ensure(keys, _key);
	                $accessors[_key] = createAccessor(_key, val, true);
	            }
	        }
	        //将系统API以unenumerable形式加入vm,
	        //添加用户的其他不可监听属性或方法
	        //重写$track
	        //并在IE6-8中增添加不存在的hasOwnPropert方法
	        var vm = platform.createViewModel(core, $accessors, core);
	        platform.afterCreate(vm, core, keys, !dd);
	        return vm;
	    };
	    var $proxyItemBackdoorMap = {};
	
	    function canHijack(key, val, $proxyItemBackdoor) {
	        if (key in $$skipArray) return false;
	        if (key.charAt(0) === '$') {
	            if ($proxyItemBackdoor) {
	                if (!$proxyItemBackdoorMap[key]) {
	                    $proxyItemBackdoorMap[key] = 1;
	                    avalon.warn('ms-for\u4E2D\u7684\u53D8\u91CF' + key + '\u4E0D\u518D\u5EFA\u8BAE\u4EE5$\u4E3A\u524D\u7F00');
	                }
	                return true;
	            }
	            return false;
	        }
	        if (val == null) {
	            avalon.warn('定义vmodel时' + key + '的属性值不能为null undefine');
	            return true;
	        }
	        if (/error|date|function|regexp/.test(avalon.type(val))) {
	            return false;
	        }
	        return !(val && val.nodeName && val.nodeType);
	    }
	
	    function createProxy(target, dd) {
	        if (target && target.$events) {
	            return target;
	        }
	        var vm;
	        if (Array.isArray(target)) {
	            vm = platform.listFactory(target, false, dd);
	        } else if (isObject(target)) {
	            vm = platform.modelFactory(target, dd);
	        }
	        return vm;
	    }
	
	    platform.createProxy = createProxy;
	
	    platform.itemFactory = function itemFactory(before, after) {
	        var keyMap = before.$model;
	        var core = new IProxy(keyMap);
	        var state = avalon.shadowCopy(core.$accessors, before.$accessors); //防止互相污染
	        var data = after.data;
	        //core是包含系统属性的对象
	        //keyMap是不包含系统属性的对象, keys
	        for (var key in data) {
	            var val = keyMap[key] = core[key] = data[key];
	            state[key] = createAccessor(key, val);
	        }
	        var keys = Object.keys(keyMap);
	        var vm = platform.createViewModel(core, state, core);
	        platform.afterCreate(vm, core, keys);
	        return vm;
	    };
	
	    function createAccessor(key, val, isComputed) {
	        var mutation = null;
	        var Accessor = isComputed ? Computed : Mutation;
	        return {
	            get: function Getter() {
	                if (!mutation) {
	                    mutation = new Accessor(key, val, this);
	                }
	                return mutation.get();
	            },
	            set: function Setter(newValue) {
	                if (!mutation) {
	                    mutation = new Accessor(key, val, this);
	                }
	                mutation.set(newValue);
	            },
	            enumerable: true,
	            configurable: true
	        };
	    }
	
	    platform.fuseFactory = function fuseFactory(before, after) {
	        var keyMap = avalon.mix(before.$model, after.$model);
	        var core = new IProxy(avalon.mix(keyMap, {
	            $id: before.$id + after.$id
	        }));
	        var state = avalon.mix(core.$accessors, before.$accessors, after.$accessors); //防止互相污染
	
	        var keys = Object.keys(keyMap);
	        //将系统API以unenumerable形式加入vm,并在IE6-8中添加hasOwnPropert方法
	        var vm = platform.createViewModel(core, state, core);
	        platform.afterCreate(vm, core, keys, false);
	        return vm;
	    };
	
	    function toJson(val) {
	        var xtype = avalon.type(val);
	        if (xtype === 'array') {
	            var array = [];
	            for (var i = 0; i < val.length; i++) {
	                array[i] = toJson(val[i]);
	            }
	            return array;
	        } else if (xtype === 'object') {
	            if (typeof val.$track === 'string') {
	                var obj = {};
	                var arr = val.$track.match(/[^☥]+/g) || [];
	                arr.forEach(function (i) {
	                    var value = val[i];
	                    obj[i] = value && value.$events ? toJson(value) : value;
	                });
	                return obj;
	            }
	        }
	        return val;
	    }
	
	    var modelAccessor = {
	        get: function get() {
	            return toJson(this);
	        },
	        set: avalon.noop,
	        enumerable: false,
	        configurable: true
	    };
	
	    platform.toJson = toJson;
	    platform.modelAccessor = modelAccessor;
	
	    var _splice = ap.splice;
	    var __array__ = {
	        set: function set(index, val) {
	            if (index >>> 0 === index && this[index] !== val) {
	                if (index > this.length) {
	                    throw Error(index + 'set方法的第一个参数不能大于原数组长度');
	                }
	                this.splice(index, 1, val);
	            }
	        },
	        toJSON: function toJSON() {
	            //为了解决IE6-8的解决,通过此方法显式地求取数组的$model
	            return this.$model = platform.toJson(this);
	        },
	        contains: function contains(el) {
	            //判定是否包含
	            return this.indexOf(el) !== -1;
	        },
	        ensure: function ensure(el) {
	            if (!this.contains(el)) {
	                //只有不存在才push
	                this.push(el);
	                return true;
	            }
	            return false;
	        },
	        pushArray: function pushArray(arr) {
	            return this.push.apply(this, arr);
	        },
	        remove: function remove(el) {
	            //移除第一个等于给定值的元素
	            return this.removeAt(this.indexOf(el));
	        },
	        removeAt: function removeAt(index) {
	            //移除指定索引上的元素
	            if (index >>> 0 === index) {
	                return this.splice(index, 1);
	            }
	            return [];
	        },
	        clear: function clear() {
	            this.removeAll();
	            return this;
	        },
	        removeAll: function removeAll(all) {
	            //移除N个元素
	            var size = this.length;
	            var eliminate = Array.isArray(all) ? function (el) {
	                return all.indexOf(el) !== -1;
	            } : typeof all === 'function' ? all : false;
	
	            if (eliminate) {
	                for (var i = this.length - 1; i >= 0; i--) {
	                    if (eliminate(this[i], i)) {
	                        _splice.call(this, i, 1);
	                    }
	                }
	            } else {
	                _splice.call(this, 0, this.length);
	            }
	            this.toJSON();
	            this.$events.__dep__.notify();
	        }
	    };
	    function hijackMethods(array) {
	        for (var i in __array__) {
	            platform.hideProperty(array, i, __array__[i]);
	        }
	    }
	    var __method__ = ['push', 'pop', 'shift', 'unshift', 'splice', 'sort', 'reverse'];
	
	    __method__.forEach(function (method) {
	        var original = ap[method];
	        __array__[method] = function () {
	            // 继续尝试劫持数组元素的属性
	            var core = this.$events;
	
	            var args = platform.listFactory(arguments, true, core.__dep__);
	            var result = original.apply(this, args);
	
	            this.toJSON();
	            core.__dep__.notify(method);
	            return result;
	        };
	    });
	
	    function listFactory(array, stop, dd) {
	        if (!stop) {
	            hijackMethods(array);
	            if (modern) {
	                Object.defineProperty(array, '$model', platform.modelAccessor);
	            }
	            platform.hideProperty(array, '$hashcode', avalon.makeHashCode('$'));
	            platform.hideProperty(array, '$events', { __dep__: dd || new Mutation() });
	        }
	        var _dd = array.$events && array.$events.__dep__;
	        for (var i = 0, n = array.length; i < n; i++) {
	            var item = array[i];
	            if (isObject(item)) {
	                array[i] = platform.createProxy(item, _dd);
	            }
	        }
	        return array;
	    }
	
	    platform.listFactory = listFactory;
	
	    //如果浏览器不支持ecma262v5的Object.defineProperties或者存在BUG，比如IE8
	    //标准浏览器使用__defineGetter__, __defineSetter__实现
	    var canHideProperty = true;
	    try {
	        Object.defineProperty({}, '_', {
	            value: 'x'
	        });
	        delete $$skipArray.$vbsetter;
	        delete $$skipArray.$vbthis;
	    } catch (e) {
	        /* istanbul ignore next*/
	        canHideProperty = false;
	    }
	
	    var protectedVB = { $vbthis: 1, $vbsetter: 1 };
	    /* istanbul ignore next */
	    function hideProperty(host, name, value) {
	        if (canHideProperty) {
	            Object.defineProperty(host, name, {
	                value: value,
	                writable: true,
	                enumerable: false,
	                configurable: true
	            });
	        } else if (!protectedVB[name]) {
	            /* istanbul ignore next */
	            host[name] = value;
	        }
	    }
	
	    function watchFactory(core) {
	        return function $watch(expr, callback, deep) {
	            var w = new Action(core.__proxy__, {
	                deep: deep,
	                type: 'user',
	                expr: expr
	            }, callback);
	            if (!core[expr]) {
	                core[expr] = [w];
	            } else {
	                core[expr].push(w);
	            }
	
	            return function () {
	                w.dispose();
	                avalon.Array.remove(core[expr], w);
	                if (core[expr].length === 0) {
	                    delete core[expr];
	                }
	            };
	        };
	    }
	
	    function fireFactory(core) {
	        return function $fire(expr, a) {
	            var list = core[expr];
	            if (Array.isArray(list)) {
	                for (var i = 0, w; w = list[i++];) {
	                    w.callback.call(w.vm, a, w.value, w.expr);
	                }
	            }
	        };
	    }
	
	    function wrapIt(str) {
	        return '☥' + str + '☥';
	    }
	
	    function afterCreate(vm, core, keys, bindThis) {
	        var ac = vm.$accessors;
	        //隐藏系统属性
	        for (var key in $$skipArray) {
	            if (avalon.msie < 9 && core[key] === void 0) continue;
	            hideProperty(vm, key, core[key]);
	        }
	        //为不可监听的属性或方法赋值
	        for (var i = 0; i < keys.length; i++) {
	            var _key2 = keys[i];
	            if (!(_key2 in ac)) {
	                if (bindThis && typeof core[_key2] === 'function') {
	                    vm[_key2] = core[_key2].bind(vm);
	                    continue;
	                }
	                vm[_key2] = core[_key2];
	            }
	        }
	        vm.$track = keys.join('☥');
	
	        function hasOwnKey(key) {
	            return wrapIt(vm.$track).indexOf(wrapIt(key)) > -1;
	        }
	        if (avalon.msie < 9) {
	            vm.hasOwnProperty = hasOwnKey;
	        }
	        vm.$events.__proxy__ = vm;
	    }
	
	    platform.hideProperty = hideProperty;
	    platform.fireFactory = fireFactory;
	    platform.watchFactory = watchFactory;
	    platform.afterCreate = afterCreate;
	
	    var createViewModel = Object.defineProperties;
	    var defineProperty;
	
	    var timeBucket = new Date() - 0;
	    /* istanbul ignore if*/
	    if (!canHideProperty) {
	        if ('__defineGetter__' in avalon) {
	            defineProperty = function defineProperty(obj, prop, desc) {
	                if ('value' in desc) {
	                    obj[prop] = desc.value;
	                }
	                if ('get' in desc) {
	                    obj.__defineGetter__(prop, desc.get);
	                }
	                if ('set' in desc) {
	                    obj.__defineSetter__(prop, desc.set);
	                }
	                return obj;
	            };
	            createViewModel = function createViewModel(obj, descs) {
	                for (var prop in descs) {
	                    if (descs.hasOwnProperty(prop)) {
	                        defineProperty(obj, prop, descs[prop]);
	                    }
	                }
	                return obj;
	            };
	        }
	        /* istanbul ignore if*/
	        if (msie < 9) {
	            var VBClassPool = {};
	            window.execScript([// jshint ignore:line
	            'Function parseVB(code)', '\tExecuteGlobal(code)', 'End Function' //转换一段文本为VB代码
	            ].join('\n'), 'VBScript');
	
	            var VBMediator = function VBMediator(instance, accessors, name, value) {
	                // jshint ignore:line
	                var accessor = accessors[name];
	                if (arguments.length === 4) {
	                    accessor.set.call(instance, value);
	                } else {
	                    return accessor.get.call(instance);
	                }
	            };
	            createViewModel = function createViewModel(name, accessors, properties) {
	                // jshint ignore:line
	                var buffer = [];
	                buffer.push('\tPrivate [$vbsetter]', '\tPublic  [$accessors]', '\tPublic Default Function [$vbthis](ac' + timeBucket + ', s' + timeBucket + ')', '\t\tSet  [$accessors] = ac' + timeBucket + ': set [$vbsetter] = s' + timeBucket, '\t\tSet  [$vbthis]    = Me', //链式调用
	                '\tEnd Function');
	                //添加普通属性,因为VBScript对象不能像JS那样随意增删属性，必须在这里预先定义好
	                var uniq = {
	                    $vbthis: true,
	                    $vbsetter: true,
	                    $accessors: true
	                };
	                for (name in $$skipArray) {
	                    if (!uniq[name]) {
	                        buffer.push('\tPublic [' + name + ']');
	                        uniq[name] = true;
	                    }
	                }
	                //添加访问器属性 
	                for (name in accessors) {
	                    if (uniq[name]) {
	                        continue;
	                    }
	                    uniq[name] = true;
	                    buffer.push(
	                    //由于不知对方会传入什么,因此set, let都用上
	                    '\tPublic Property Let [' + name + '](val' + timeBucket + ')', //setter
	                    '\t\tCall [$vbsetter](Me, [$accessors], "' + name + '", val' + timeBucket + ')', '\tEnd Property', '\tPublic Property Set [' + name + '](val' + timeBucket + ')', //setter
	                    '\t\tCall [$vbsetter](Me, [$accessors], "' + name + '", val' + timeBucket + ')', '\tEnd Property', '\tPublic Property Get [' + name + ']', //getter
	                    '\tOn Error Resume Next', //必须优先使用set语句,否则它会误将数组当字符串返回
	                    '\t\tSet[' + name + '] = [$vbsetter](Me, [$accessors],"' + name + '")', '\tIf Err.Number <> 0 Then', '\t\t[' + name + '] = [$vbsetter](Me, [$accessors],"' + name + '")', '\tEnd If', '\tOn Error Goto 0', '\tEnd Property');
	                }
	
	                for (name in properties) {
	                    if (!uniq[name]) {
	                        uniq[name] = true;
	                        buffer.push('\tPublic [' + name + ']');
	                    }
	                }
	
	                buffer.push('\tPublic [hasOwnProperty]');
	                buffer.push('End Class');
	                var body = buffer.join('\r\n');
	                var className = VBClassPool[body];
	                if (!className) {
	                    className = avalon.makeHashCode('VBClass');
	                    window.parseVB('Class ' + className + body);
	                    window.parseVB(['Function ' + className + 'Factory(acc, vbm)', //创建实例并传入两个关键的参数
	                    '\tDim o', '\tSet o = (New ' + className + ')(acc, vbm)', '\tSet ' + className + 'Factory = o', 'End Function'].join('\r\n'));
	                    VBClassPool[body] = className;
	                }
	                var ret = window[className + 'Factory'](accessors, VBMediator); //得到其产品
	                return ret; //得到其产品
	            };
	        }
	    }
	
	    platform.createViewModel = createViewModel;
	
	    var impDir = avalon.directive('important', {
	        priority: 1,
	        getScope: function getScope(name, scope) {
	            var v = avalon.vmodels[name];
	            if (v) return v;
	            throw 'error! no vmodel called ' + name;
	        },
	        update: function update(node, attrName, $id) {
	            if (!avalon.inBrowser) return;
	            var dom = avalon.vdom(node, 'toDOM');
	            if (dom.nodeType === 1) {
	                dom.removeAttribute(attrName);
	                avalon(dom).removeClass('ms-controller');
	            }
	            var vm = avalon.vmodels[$id];
	            if (vm) {
	                vm.$element = dom;
	                vm.$render = this;
	                vm.$fire('onReady');
	                delete vm.$events.onReady;
	            }
	        }
	    });
	
	    var impCb = impDir.update;
	
	    avalon.directive('controller', {
	        priority: 2,
	        getScope: function getScope(name, scope) {
	            var v = avalon.vmodels[name];
	            if (v) {
	                v.$render = this;
	                if (scope && scope !== v) {
	                    return platform.fuseFactory(scope, v);
	                }
	                return v;
	            }
	            return scope;
	        },
	        update: impCb
	    });
	
	    avalon.directive('skip', {
	        delay: true
	    });
	
	    var arrayWarn = {};
	    var cssDir = avalon.directive('css', {
	        diff: function diff(newVal, oldVal) {
	            if (Object(newVal) === newVal) {
	                newVal = platform.toJson(newVal); //安全的遍历VBscript
	                if (Array.isArray(newVal)) {
	                    //转换成对象
	                    var b = {};
	                    newVal.forEach(function (el) {
	                        el && avalon.shadowCopy(b, el);
	                    });
	                    newVal = b;
	                    if (!arrayWarn[this.type]) {
	                        avalon.warn('ms-' + this.type + '指令的值不建议使用数组形式了！');
	                        arrayWarn[this.type] = 1;
	                    }
	                }
	
	                var hasChange = false;
	                var patch = {};
	                if (!oldVal) {
	                    //如果一开始为空
	                    patch = newVal;
	                    hasChange = true;
	                } else {
	                    if (this.deep) {
	                        var deep = typeof this.deep === 'number' ? this.deep : 6;
	                        for (var i in newVal) {
	                            //diff差异点  
	                            if (!deepEquals(newVal[i], oldVal[i], 4)) {
	                                this.value = newVal;
	                                return true;
	                            }
	                            patch[i] = newVal[i];
	                        }
	                    } else {
	                        for (var _i3 in newVal) {
	                            //diff差异点
	                            if (newVal[_i3] !== oldVal[_i3]) {
	                                hasChange = true;
	                            }
	                            patch[_i3] = newVal[_i3];
	                        }
	                    }
	
	                    for (var _i4 in oldVal) {
	                        if (!(_i4 in patch)) {
	                            hasChange = true;
	                            patch[_i4] = '';
	                        }
	                    }
	                }
	                if (hasChange) {
	                    this.value = patch;
	                    return true;
	                }
	            }
	            return false;
	        },
	        update: function update(vdom, value) {
	
	            var dom = vdom.dom;
	            if (dom && dom.nodeType === 1) {
	                var wrap = avalon(dom);
	                for (var name in value) {
	                    wrap.css(name, value[name]);
	                }
	            }
	        }
	    });
	
	    var cssDiff = cssDir.diff;
	
	    function getEnumerableKeys(obj) {
	        var res = [];
	        for (var key in obj) {
	            res.push(key);
	        }return res;
	    }
	
	    function deepEquals(a, b, level) {
	        if (level === 0) return a === b;
	        if (a === null && b === null) return true;
	        if (a === undefined && b === undefined) return true;
	        var aIsArray = Array.isArray(a);
	        if (aIsArray !== Array.isArray(b)) {
	            return false;
	        }
	        if (aIsArray) {
	            return equalArray(a, b, level);
	        } else if (typeof a === "object" && typeof b === "object") {
	            return equalObject(a, b, level);
	        }
	        return a === b;
	    }
	
	    function equalArray(a, b, level) {
	        if (a.length !== b.length) {
	            return false;
	        }
	        for (var i = a.length - 1; i >= 0; i--) {
	            try {
	                if (!deepEquals(a[i], b[i], level - 1)) {
	                    return false;
	                }
	            } catch (noThisPropError) {
	                return false;
	            }
	        }
	        return true;
	    }
	
	    function equalObject(a, b, level) {
	        if (a === null || b === null) return false;
	        if (getEnumerableKeys(a).length !== getEnumerableKeys(b).length) return false;
	        for (var prop in a) {
	            if (!(prop in b)) return false;
	            try {
	                if (!deepEquals(a[prop], b[prop], level - 1)) {
	                    return false;
	                }
	            } catch (noThisPropError) {
	                return false;
	            }
	        }
	        return true;
	    }
	
	    /**
	     * ------------------------------------------------------------
	     * 检测浏览器对CSS动画的支持与API名
	     * ------------------------------------------------------------
	     */
	
	    var checker = {
	        TransitionEvent: 'transitionend',
	        WebKitTransitionEvent: 'webkitTransitionEnd',
	        OTransitionEvent: 'oTransitionEnd',
	        otransitionEvent: 'otransitionEnd'
	    };
	    var css3 = void 0;
	    var tran = void 0;
	    var ani = void 0;
	    var name$2 = void 0;
	    var animationEndEvent = void 0;
	    var transitionEndEvent = void 0;
	    var transition = false;
	    var animation = false;
	    //有的浏览器同时支持私有实现与标准写法，比如webkit支持前两种，Opera支持1、3、4
	    for (name$2 in checker) {
	        if (window$1[name$2]) {
	            tran = checker[name$2];
	            break;
	        }
	        /* istanbul ignore next */
	        try {
	            var a = document.createEvent(name$2);
	            tran = checker[name$2];
	            break;
	        } catch (e) {}
	    }
	    if (typeof tran === 'string') {
	        transition = css3 = true;
	        transitionEndEvent = tran;
	    }
	
	    //animationend有两个可用形态
	    //IE10+, Firefox 16+ & Opera 12.1+: animationend
	    //Chrome/Safari: webkitAnimationEnd
	    //http://blogs.msdn.com/b/davrous/archive/2011/12/06/introduction-to-css3-animat ions.aspx
	    //IE10也可以使用MSAnimationEnd监听，但是回调里的事件 type依然为animationend
	    //  el.addEventListener('MSAnimationEnd', function(e) {
	    //     alert(e.type)// animationend！！！
	    // })
	    checker = {
	        'AnimationEvent': 'animationend',
	        'WebKitAnimationEvent': 'webkitAnimationEnd'
	    };
	    for (name$2 in checker) {
	        if (window$1[name$2]) {
	            ani = checker[name$2];
	            break;
	        }
	    }
	    if (typeof ani === 'string') {
	        animation = css3 = true;
	        animationEndEvent = ani;
	    }
	
	    var effectDir = avalon.directive('effect', {
	        priority: 5,
	        diff: function diff(effect) {
	            var vdom = this.node;
	            if (typeof effect === 'string') {
	                this.value = effect = {
	                    is: effect
	                };
	                avalon.warn('ms-effect的指令值不再支持字符串,必须是一个对象');
	            }
	            this.value = vdom.effect = effect;
	            var ok = cssDiff.call(this, effect, this.oldValue);
	            var me = this;
	            if (ok) {
	                setTimeout(function () {
	                    vdom.animating = true;
	                    effectDir.update.call(me, vdom, vdom.effect);
	                });
	                vdom.animating = false;
	                return true;
	            }
	            return false;
	        },
	
	        update: function update(vdom, change, opts) {
	            var dom = vdom.dom;
	            if (dom && dom.nodeType === 1) {
	                //要求配置对象必须指定is属性，action必须是布尔或enter,leave,move
	                var option = change || opts;
	                var is = option.is;
	
	                var globalOption = avalon.effects[is];
	                if (!globalOption) {
	                    //如果没有定义特效
	                    avalon.warn(is + ' effect is undefined');
	                    return;
	                }
	                var finalOption = {};
	                var action = actionMaps[option.action];
	                if (typeof Effect.prototype[action] !== 'function') {
	                    avalon.warn('action is undefined');
	                    return;
	                }
	                //必须预定义特效
	
	                var effect = new avalon.Effect(dom);
	                avalon.mix(finalOption, globalOption, option, { action: action });
	
	                if (finalOption.queue) {
	                    animationQueue.push(function () {
	                        effect[action](finalOption);
	                    });
	                    callNextAnimation();
	                } else {
	
	                    effect[action](finalOption);
	                }
	                return true;
	            }
	        }
	    });
	
	    var move = 'move';
	    var leave = 'leave';
	    var enter = 'enter';
	    var actionMaps = {
	        'true': enter,
	        'false': leave,
	        enter: enter,
	        leave: leave,
	        move: move,
	        'undefined': enter
	    };
	
	    var animationQueue = [];
	    function callNextAnimation() {
	        var fn = animationQueue[0];
	        if (fn) {
	            fn();
	        }
	    }
	
	    avalon.effects = {};
	    avalon.effect = function (name, opts) {
	        var definition = avalon.effects[name] = opts || {};
	        if (css3 && definition.css !== false) {
	            patchObject(definition, 'enterClass', name + '-enter');
	            patchObject(definition, 'enterActiveClass', definition.enterClass + '-active');
	            patchObject(definition, 'leaveClass', name + '-leave');
	            patchObject(definition, 'leaveActiveClass', definition.leaveClass + '-active');
	        }
	        return definition;
	    };
	
	    function patchObject(obj, name, value) {
	        if (!obj[name]) {
	            obj[name] = value;
	        }
	    }
	
	    var Effect = function Effect(dom) {
	        this.dom = dom;
	    };
	
	    avalon.Effect = Effect;
	
	    Effect.prototype = {
	        enter: createAction('Enter'),
	        leave: createAction('Leave'),
	        move: createAction('Move')
	    };
	
	    function execHooks(options, name, el) {
	        var fns = [].concat(options[name]);
	        for (var i = 0, fn; fn = fns[i++];) {
	            if (typeof fn === 'function') {
	                fn(el);
	            }
	        }
	    }
	    var staggerCache = new Cache(128);
	
	    function createAction(action) {
	        var lower = action.toLowerCase();
	        return function (option) {
	            var dom = this.dom;
	            var elem = avalon(dom);
	            //处理与ms-for指令相关的stagger
	            //========BEGIN=====
	            var staggerTime = isFinite(option.stagger) ? option.stagger * 1000 : 0;
	            if (staggerTime) {
	                if (option.staggerKey) {
	                    var stagger = staggerCache.get(option.staggerKey) || staggerCache.put(option.staggerKey, {
	                        count: 0,
	                        items: 0
	                    });
	                    stagger.count++;
	                    stagger.items++;
	                }
	            }
	            var staggerIndex = stagger && stagger.count || 0;
	            //=======END==========
	            var stopAnimationID;
	            var animationDone = function animationDone(e) {
	                var isOk = e !== false;
	                if (--dom.__ms_effect_ === 0) {
	                    avalon.unbind(dom, transitionEndEvent);
	                    avalon.unbind(dom, animationEndEvent);
	                }
	                clearTimeout(stopAnimationID);
	                var dirWord = isOk ? 'Done' : 'Abort';
	                execHooks(option, 'on' + action + dirWord, dom);
	                if (stagger) {
	                    if (--stagger.items === 0) {
	                        stagger.count = 0;
	                    }
	                }
	                if (option.queue) {
	                    animationQueue.shift();
	                    callNextAnimation();
	                }
	            };
	            //执行开始前的钩子
	            execHooks(option, 'onBefore' + action, dom);
	
	            if (option[lower]) {
	                //使用JS方式执行动画
	                option[lower](dom, function (ok) {
	                    animationDone(ok !== false);
	                });
	            } else if (css3) {
	                //使用CSS3方式执行动画
	                elem.addClass(option[lower + 'Class']);
	                elem.removeClass(getNeedRemoved(option, lower));
	
	                if (!dom.__ms_effect_) {
	                    //绑定动画结束事件
	                    elem.bind(transitionEndEvent, animationDone);
	                    elem.bind(animationEndEvent, animationDone);
	                    dom.__ms_effect_ = 1;
	                } else {
	                    dom.__ms_effect_++;
	                }
	                setTimeout(function () {
	                    //用xxx-active代替xxx类名的方式 触发CSS3动画
	                    var time = avalon.root.offsetWidth === NaN;
	                    elem.addClass(option[lower + 'ActiveClass']);
	                    //计算动画时长
	                    time = getAnimationTime(dom);
	                    if (!time === 0) {
	                        //立即结束动画
	                        animationDone(false);
	                    } else if (!staggerTime) {
	                        //如果动画超出时长还没有调用结束事件,这可能是元素被移除了
	                        //如果强制结束动画
	                        stopAnimationID = setTimeout(function () {
	                            animationDone(false);
	                        }, time + 32);
	                    }
	                }, 17 + staggerTime * staggerIndex); // = 1000/60
	            }
	        };
	    }
	
	    avalon.applyEffect = function (dom, vdom, opts) {
	        var cb = opts.cb;
	        var curEffect = vdom.effect;
	        if (curEffect && dom && dom.nodeType === 1) {
	            var hook = opts.hook;
	            var old = curEffect[hook];
	            if (cb) {
	                if (Array.isArray(old)) {
	                    old.push(cb);
	                } else if (old) {
	                    curEffect[hook] = [old, cb];
	                } else {
	                    curEffect[hook] = [cb];
	                }
	            }
	            getAction(opts);
	            avalon.directives.effect.update(vdom, curEffect, avalon.shadowCopy({}, opts));
	        } else if (cb) {
	            cb(dom);
	        }
	    };
	    /**
	     * 获取方向
	     */
	    function getAction(opts) {
	        if (!opts.action) {
	            return opts.action = opts.hook.replace(/^on/, '').replace(/Done$/, '').toLowerCase();
	        }
	    }
	    /**
	     * 需要移除的类名
	     */
	    function getNeedRemoved(options, name) {
	        var name = name === 'leave' ? 'enter' : 'leave';
	        return Array(name + 'Class', name + 'ActiveClass').map(function (cls) {
	            return options[cls];
	        }).join(' ');
	    }
	    /**
	     * 计算动画长度
	     */
	    var transitionDuration = avalon.cssName('transition-duration');
	    var animationDuration = avalon.cssName('animation-duration');
	    var rsecond = /\d+s$/;
	    function toMillisecond(str) {
	        var ratio = rsecond.test(str) ? 1000 : 1;
	        return parseFloat(str) * ratio;
	    }
	
	    function getAnimationTime(dom) {
	        var computedStyles = window$1.getComputedStyle(dom, null);
	        var tranDuration = computedStyles[transitionDuration];
	        var animDuration = computedStyles[animationDuration];
	        return toMillisecond(tranDuration) || toMillisecond(animDuration);
	    }
	    /**
	     * 
	    <!DOCTYPE html>
	    <html>
	        <head>
	            <meta charset="UTF-8">
	            <meta name="viewport" content="width=device-width, initial-scale=1.0">
	            <script src="dist/avalon.js"></script>
	            <script>
	                avalon.effect('animate')
	                var vm = avalon.define({
	                    $id: 'ani',
	                    a: true
	                })
	            </script>
	            <style>
	                .animate-enter, .animate-leave{
	                    width:100px;
	                    height:100px;
	                    background: #29b6f6;
	                    transition:all 2s;
	                    -moz-transition: all 2s; 
	                    -webkit-transition: all 2s;
	                    -o-transition:all 2s;
	                }  
	                .animate-enter-active, .animate-leave{
	                    width:300px;
	                    height:300px;
	                }
	                .animate-leave-active{
	                    width:100px;
	                    height:100px;
	                }
	            </style>
	        </head>
	        <body>
	            <div :controller='ani' >
	                <p><input type='button' value='click' :click='@a =!@a'></p>
	                <div :effect="{is:'animate',action:@a}"></div>
	            </div>
	    </body>
	    </html>
	     * 
	     */
	
	    var none = 'none';
	    function parseDisplay(elem, val) {
	        //用于取得此类标签的默认display值
	        var doc = elem.ownerDocument;
	        var nodeName = elem.nodeName;
	        var key = '_' + nodeName;
	        if (!parseDisplay[key]) {
	            var temp = doc.body.appendChild(doc.createElement(nodeName));
	            val = avalon.css(temp, 'display');
	            doc.body.removeChild(temp);
	            if (val === none) {
	                val = 'block';
	            }
	            parseDisplay[key] = val;
	        }
	        return parseDisplay[key];
	    }
	
	    avalon.parseDisplay = parseDisplay;
	    avalon.directive('visible', {
	        diff: function diff(newVal, oldVal) {
	            var n = !!newVal;
	            if (oldVal === void 0 || n !== oldVal) {
	                this.value = n;
	                return true;
	            }
	        },
	        ready: true,
	        update: function update(vdom, show) {
	            var dom = vdom.dom;
	            if (dom && dom.nodeType === 1) {
	                var display = dom.style.display;
	                var value;
	                if (show) {
	                    if (display === none) {
	                        value = vdom.displayValue;
	                        if (!value) {
	                            dom.style.display = '';
	                            if (dom.style.cssText === '') {
	                                dom.removeAttribute('style');
	                            }
	                        }
	                    }
	                    if (dom.style.display === '' && avalon(dom).css('display') === none &&
	                    // fix firefox BUG,必须挂到页面上
	                    avalon.contains(dom.ownerDocument, dom)) {
	                        value = parseDisplay(dom);
	                    }
	                } else {
	
	                    if (display !== none) {
	                        value = none;
	                        vdom.displayValue = display;
	                    }
	                }
	                var cb = function cb() {
	                    if (value !== void 0) {
	                        dom.style.display = value;
	                    }
	                };
	
	                avalon.applyEffect(dom, vdom, {
	                    hook: show ? 'onEnterDone' : 'onLeaveDone',
	                    cb: cb
	                });
	            }
	        }
	    });
	
	    avalon.directive('text', {
	        delay: true,
	        init: function init() {
	
	            var node = this.node;
	            if (node.isVoidTag) {
	                avalon.error('自闭合元素不能使用ms-text');
	            }
	            var child = { nodeName: '#text', nodeValue: this.getValue() };
	            node.children.splice(0, node.children.length, child);
	            if (inBrowser) {
	                avalon.clearHTML(node.dom);
	                node.dom.appendChild(avalon.vdom(child, 'toDOM'));
	            }
	            this.node = child;
	            var type = 'expr';
	            this.type = this.name = type;
	            var directive$$1 = avalon.directives[type];
	            var me = this;
	            this.callback = function (value) {
	                directive$$1.update.call(me, me.node, value);
	            };
	        }
	    });
	
	    avalon.directive('expr', {
	        update: function update(vdom, value) {
	            value = value == null || value === '' ? '\u200B' : value;
	            vdom.nodeValue = value;
	            //https://github.com/RubyLouvre/avalon/issues/1834
	            if (vdom.dom) vdom.dom.data = value;
	        }
	    });
	
	    avalon.directive('attr', {
	        diff: cssDiff,
	        update: function update(vdom, value) {
	            var props = vdom.props;
	            for (var i in value) {
	                if (!!value[i] === false) {
	                    delete props[i];
	                } else {
	                    props[i] = value[i];
	                }
	            }
	            var dom = vdom.dom;
	            if (dom && dom.nodeType === 1) {
	                updateAttrs(dom, value);
	            }
	        }
	    });
	
	    avalon.directive('html', {
	
	        update: function update(vdom, value) {
	            this.beforeDispose();
	
	            this.innerRender = avalon.scan('<div class="ms-html-container">' + value + '</div>', this.vm, function () {
	                var oldRoot = this.root;
	                if (vdom.children) vdom.children.length = 0;
	                vdom.children = oldRoot.children;
	                this.root = vdom;
	                if (vdom.dom) avalon.clearHTML(vdom.dom);
	            });
	        },
	        beforeDispose: function beforeDispose() {
	            if (this.innerRender) {
	                this.innerRender.dispose();
	            }
	        },
	        delay: true
	    });
	
	    avalon.directive('if', {
	        delay: true,
	        priority: 5,
	        init: function init() {
	            this.placeholder = createAnchor('if');
	            var props = this.node.props;
	            delete props['ms-if'];
	            delete props[':if'];
	            this.fragment = avalon.vdom(this.node, 'toHTML');
	        },
	        diff: function diff(newVal, oldVal) {
	            var n = !!newVal;
	            if (oldVal === void 0 || n !== oldVal) {
	                this.value = n;
	                return true;
	            }
	        },
	        update: function update(vdom, value) {
	            if (this.isShow === void 0 && value) {
	                continueScan(this, vdom);
	                return;
	            }
	            this.isShow = value;
	            var placeholder = this.placeholder;
	
	            if (value) {
	                var p = placeholder.parentNode;
	                continueScan(this, vdom);
	                p && p.replaceChild(vdom.dom, placeholder);
	            } else {
	                //移除DOM
	                this.beforeDispose();
	                vdom.nodeValue = 'if';
	                vdom.nodeName = '#comment';
	                delete vdom.children;
	                var dom = vdom.dom;
	                var p = dom && dom.parentNode;
	                vdom.dom = placeholder;
	                if (p) {
	                    p.replaceChild(placeholder, dom);
	                }
	            }
	        },
	        beforeDispose: function beforeDispose() {
	            if (this.innerRender) {
	                this.innerRender.dispose();
	            }
	        }
	    });
	
	    function continueScan(instance, vdom) {
	        var innerRender = instance.innerRender = avalon.scan(instance.fragment, instance.vm);
	        avalon.shadowCopy(vdom, innerRender.root);
	        delete vdom.nodeValue;
	    }
	
	    avalon.directive('on', {
	        beforeInit: function beforeInit() {
	            this.getter = avalon.noop;
	        },
	        init: function init() {
	            var vdom = this.node;
	            var underline = this.name.replace('ms-on-', 'e').replace('-', '_');
	            var uuid = underline + '_' + this.expr.replace(/\s/g, '').replace(/[^$a-z]/ig, function (e) {
	                return e.charCodeAt(0);
	            });
	            var fn = avalon.eventListeners[uuid];
	            if (!fn) {
	                var arr = addScope(this.expr);
	                var body = arr[0],
	                    filters = arr[1];
	                body = makeHandle(body);
	
	                if (filters) {
	                    filters = filters.replace(/__value__/g, '$event');
	                    filters += '\nif($event.$return){\n\treturn;\n}';
	                }
	                var ret = ['try{', '\tvar __vmodel__ = this;', '\t' + filters, '\treturn ' + body, '}catch(e){avalon.log(e, "in on dir")}'].filter(function (el) {
	                    return (/\S/.test(el)
	                    );
	                });
	                fn = new Function('$event', ret.join('\n'));
	                fn.uuid = uuid;
	                avalon.eventListeners[uuid] = fn;
	            }
	
	            var dom = avalon.vdom(vdom, 'toDOM');
	            dom._ms_context_ = this.vm;
	
	            this.eventType = this.param.replace(/\-(\d)$/, '');
	            delete this.param;
	            avalon(dom).bind(this.eventType, fn);
	        },
	
	        beforeDispose: function beforeDispose() {
	            avalon(this.node.dom).unbind(this.eventType);
	        }
	    });
	
	    var rforAs = /\s+as\s+([$\w]+)/;
	    var rident = /^[$a-zA-Z_][$a-zA-Z0-9_]*$/;
	    var rinvalid = /^(null|undefined|NaN|window|this|\$index|\$id)$/;
	    var rargs = /[$\w_]+/g;
	    avalon.directive('for', {
	        delay: true,
	        priority: 3,
	        beforeInit: function beforeInit() {
	            var str = this.expr,
	                asName;
	            str = str.replace(rforAs, function (a, b) {
	                /* istanbul ignore if */
	                if (!rident.test(b) || rinvalid.test(b)) {
	                    avalon.error('alias ' + b + ' is invalid --- must be a valid JS identifier which is not a reserved name.');
	                } else {
	                    asName = b;
	                }
	                return '';
	            });
	
	            var arr = str.split(' in ');
	            var kv = arr[0].match(rargs);
	            if (kv.length === 1) {
	                //确保avalon._each的回调有三个参数
	                kv.unshift('$key');
	            }
	            this.expr = arr[1];
	            this.keyName = kv[0];
	            this.valName = kv[1];
	            this.signature = avalon.makeHashCode('for');
	            if (asName) {
	                this.asName = asName;
	            }
	
	            delete this.param;
	        },
	        init: function init() {
	            var cb = this.userCb;
	            if (typeof cb === 'string' && cb) {
	                var arr = addScope(cb, 'for');
	                var body = makeHandle(arr[0]);
	                this.userCb = new Function('$event', 'var __vmodel__ = this\nreturn ' + body);
	            }
	            this.node.forDir = this; //暴露给component/index.js中的resetParentChildren方法使用
	            this.fragment = ['<div>', this.fragment, '<!--', this.signature, '--></div>'].join('');
	            this.cache = {};
	        },
	        diff: function diff(newVal, oldVal) {
	            /* istanbul ignore if */
	            if (this.updating) {
	                return;
	            }
	            this.updating = true;
	            var traceIds = createFragments(this, newVal);
	
	            if (this.oldTrackIds === void 0) return true;
	
	            if (this.oldTrackIds !== traceIds) {
	                this.oldTrackIds = traceIds;
	                return true;
	            }
	        },
	        update: function update() {
	
	            if (!this.preFragments) {
	                this.fragments = this.fragments || [];
	                mountList(this);
	            } else {
	                diffList(this);
	                updateList(this);
	            }
	
	            if (this.userCb) {
	                var me = this;
	                setTimeout(function () {
	                    me.userCb.call(me.vm, {
	                        type: 'rendered',
	                        target: me.begin.dom,
	                        signature: me.signature
	                    });
	                }, 0);
	            }
	            delete this.updating;
	        },
	        beforeDispose: function beforeDispose() {
	            this.fragments.forEach(function (el) {
	                el.dispose();
	            });
	        }
	    });
	
	    function getTraceKey(item) {
	        var type = typeof item;
	        return item && type === 'object' ? item.$hashcode : type + ':' + item;
	    }
	
	    //创建一组fragment的虚拟DOM
	    function createFragments(instance, obj) {
	        if (isObject(obj)) {
	            var array = Array.isArray(obj);
	            var ids = [];
	            var fragments = [],
	                i = 0;
	
	            instance.isArray = array;
	            if (instance.fragments) {
	                instance.preFragments = instance.fragments;
	                avalon.each(obj, function (key, value) {
	                    var k = array ? getTraceKey(value) : key;
	
	                    fragments.push({
	                        key: k,
	                        val: value,
	                        index: i++
	                    });
	                    ids.push(k);
	                });
	                instance.fragments = fragments;
	            } else {
	                avalon.each(obj, function (key, value) {
	                    if (!(key in $$skipArray)) {
	                        var k = array ? getTraceKey(value) : key;
	                        fragments.push(new VFragment([], k, value, i++));
	                        ids.push(k);
	                    }
	                });
	                instance.fragments = fragments;
	            }
	            return ids.join(';;');
	        } else {
	            return NaN;
	        }
	    }
	
	    function mountList(instance) {
	        var args = instance.fragments.map(function (fragment, index) {
	            FragmentDecorator(fragment, instance, index);
	            saveInCache(instance.cache, fragment);
	            return fragment;
	        });
	        var list = instance.parentChildren;
	        var i = list.indexOf(instance.begin);
	        list.splice.apply(list, [i + 1, 0].concat(args));
	    }
	
	    function diffList(instance) {
	        var cache = instance.cache;
	        var newCache = {};
	        var fuzzy = [];
	        var list = instance.preFragments;
	
	        list.forEach(function (el) {
	            el._dispose = true;
	        });
	
	        instance.fragments.forEach(function (c, index) {
	            var fragment = isInCache(cache, c.key);
	            //取出之前的文档碎片
	            if (fragment) {
	                delete fragment._dispose;
	                fragment.oldIndex = fragment.index;
	                fragment.index = index; // 相当于 c.index
	
	                resetVM(fragment.vm, instance.keyName);
	                fragment.vm[instance.valName] = c.val;
	                fragment.vm[instance.keyName] = instance.isArray ? index : fragment.key;
	                saveInCache(newCache, fragment);
	            } else {
	                //如果找不到就进行模糊搜索
	                fuzzy.push(c);
	            }
	        });
	        fuzzy.forEach(function (c) {
	            var fragment = fuzzyMatchCache(cache, c.key);
	            if (fragment) {
	                //重复利用
	                fragment.oldIndex = fragment.index;
	                fragment.key = c.key;
	                var val = fragment.val = c.val;
	                var index = fragment.index = c.index;
	
	                fragment.vm[instance.valName] = val;
	                fragment.vm[instance.keyName] = instance.isArray ? index : fragment.key;
	                delete fragment._dispose;
	            } else {
	
	                c = new VFragment([], c.key, c.val, c.index);
	                fragment = FragmentDecorator(c, instance, c.index);
	                list.push(fragment);
	            }
	            saveInCache(newCache, fragment);
	        });
	
	        instance.fragments = list;
	        list.sort(function (a, b) {
	            return a.index - b.index;
	        });
	        instance.cache = newCache;
	    }
	
	    function resetVM(vm, a, b) {
	        if (avalon.config.inProxyMode) {
	            vm.$accessors[a].value = NaN;
	        } else {
	            vm.$accessors[a].set(NaN);
	        }
	    }
	
	    function updateList(instance) {
	        var before = instance.begin.dom;
	        var parent = before.parentNode;
	        var list = instance.fragments;
	        var end = instance.end.dom;
	        for (var i = 0, item; item = list[i]; i++) {
	            if (item._dispose) {
	                list.splice(i, 1);
	                i--;
	                item.dispose();
	                continue;
	            }
	            if (item.oldIndex !== item.index) {
	                var f = item.toFragment();
	                var isEnd = before.nextSibling === null;
	                parent.insertBefore(f, before.nextSibling);
	                if (isEnd && !parent.contains(end)) {
	                    parent.insertBefore(end, before.nextSibling);
	                }
	            }
	            before = item.split;
	        }
	        var ch = instance.parentChildren;
	        var startIndex = ch.indexOf(instance.begin);
	        var endIndex = ch.indexOf(instance.end);
	
	        list.splice.apply(ch, [startIndex + 1, endIndex - startIndex].concat(list));
	    }
	
	    /**
	     * 
	     * @param {type} fragment
	     * @param {type} this
	     * @param {type} index
	     * @returns { key, val, index, oldIndex, this, dom, split, vm}
	     */
	    function FragmentDecorator(fragment, instance, index) {
	        var data = {};
	        data[instance.keyName] = instance.isArray ? index : fragment.key;
	        data[instance.valName] = fragment.val;
	        if (instance.asName) {
	            data[instance.asName] = instance.value;
	        }
	        var vm = fragment.vm = platform.itemFactory(instance.vm, {
	            data: data
	        });
	        if (instance.isArray) {
	            vm.$watch(instance.valName, function (a) {
	                if (instance.value && instance.value.set) {
	                    instance.value.set(vm[instance.keyName], a);
	                }
	            });
	        } else {
	            vm.$watch(instance.valName, function (a) {
	                instance.value[fragment.key] = a;
	            });
	        }
	
	        fragment.index = index;
	        fragment.innerRender = avalon.scan(instance.fragment, vm, function () {
	            var oldRoot = this.root;
	            ap.push.apply(fragment.children, oldRoot.children);
	            this.root = fragment;
	        });
	        return fragment;
	    }
	    // 新位置: 旧位置
	    function isInCache(cache, id) {
	        var c = cache[id];
	        if (c) {
	            var arr = c.arr;
	            /* istanbul ignore if*/
	            if (arr) {
	                var r = arr.pop();
	                if (!arr.length) {
	                    c.arr = 0;
	                }
	                return r;
	            }
	            delete cache[id];
	            return c;
	        }
	    }
	    //[1,1,1] number1 number1_ number1__
	    function saveInCache(cache, component) {
	        var trackId = component.key;
	        if (!cache[trackId]) {
	            cache[trackId] = component;
	        } else {
	            var c = cache[trackId];
	            var arr = c.arr || (c.arr = []);
	            arr.push(component);
	        }
	    }
	
	    function fuzzyMatchCache(cache) {
	        var key;
	        for (var id in cache) {
	            var key = id;
	            break;
	        }
	        if (key) {
	            return isInCache(cache, key);
	        }
	    }
	
	    //根据VM的属性值或表达式的值切换类名，ms-class='xxx yyy zzz:flag'
	    //http://www.cnblogs.com/rubylouvre/archive/2012/12/17/2818540.html
	    function classNames() {
	        var classes = [];
	        for (var i = 0; i < arguments.length; i++) {
	            var arg = arguments[i];
	            var argType = typeof arg;
	            if (argType === 'string' || argType === 'number' || arg === true) {
	                classes.push(arg);
	            } else if (Array.isArray(arg)) {
	                classes.push(classNames.apply(null, arg));
	            } else if (argType === 'object') {
	                for (var key in arg) {
	                    if (arg.hasOwnProperty(key) && arg[key]) {
	                        classes.push(key);
	                    }
	                }
	            }
	        }
	
	        return classes.join(' ');
	    }
	
	    avalon.directive('class', {
	        diff: function diff(newVal, oldVal) {
	            var type = this.type;
	            var vdom = this.node;
	            var classEvent = vdom.classEvent || {};
	            if (type === 'hover') {
	                //在移出移入时切换类名
	                classEvent.mouseenter = activateClass;
	                classEvent.mouseleave = abandonClass;
	            } else if (type === 'active') {
	                //在获得焦点时切换类名
	                classEvent.tabIndex = vdom.props.tabindex || -1;
	                classEvent.mousedown = activateClass;
	                classEvent.mouseup = abandonClass;
	                classEvent.mouseleave = abandonClass;
	            }
	            vdom.classEvent = classEvent;
	
	            var className = classNames(newVal);
	
	            if (typeof oldVal === void 0 || oldVal !== className) {
	                this.value = className;
	
	                vdom['change-' + type] = className;
	                return true;
	            }
	        },
	        update: function update(vdom, value) {
	            var dom = vdom.dom;
	            if (dom && dom.nodeType == 1) {
	
	                var dirType = this.type;
	                var change = 'change-' + dirType;
	                var classEvent = vdom.classEvent;
	                if (classEvent) {
	                    for (var i in classEvent) {
	                        if (i === 'tabIndex') {
	                            dom[i] = classEvent[i];
	                        } else {
	                            avalon.bind(dom, i, classEvent[i]);
	                        }
	                    }
	                    vdom.classEvent = {};
	                }
	                var names = ['class', 'hover', 'active'];
	                names.forEach(function (type) {
	                    if (dirType !== type) return;
	                    if (type === 'class') {
	                        dom && setClass(dom, value);
	                    } else {
	                        var oldClass = dom.getAttribute(change);
	                        if (oldClass) {
	                            avalon(dom).removeClass(oldClass);
	                        }
	                        var name = 'change-' + type;
	                        dom.setAttribute(name, value);
	                    }
	                });
	            }
	        }
	    });
	
	    directives.active = directives.hover = directives['class'];
	
	    var classMap = {
	        mouseenter: 'change-hover',
	        mouseleave: 'change-hover',
	        mousedown: 'change-active',
	        mouseup: 'change-active'
	    };
	
	    function activateClass(e) {
	        var elem = e.target;
	        avalon(elem).addClass(elem.getAttribute(classMap[e.type]) || '');
	    }
	
	    function abandonClass(e) {
	        var elem = e.target;
	        var name = classMap[e.type];
	        avalon(elem).removeClass(elem.getAttribute(name) || '');
	        if (name !== 'change-active') {
	            avalon(elem).removeClass(elem.getAttribute('change-active') || '');
	        }
	    }
	
	    function setClass(dom, neo) {
	        var old = dom.getAttribute('change-class');
	        if (old !== neo) {
	            avalon(dom).removeClass(old).addClass(neo);
	            dom.setAttribute('change-class', neo);
	        }
	    }
	
	    getLongID(activateClass);
	    getLongID(abandonClass);
	
	    function lookupOption(vdom, values) {
	        vdom.children && vdom.children.forEach(function (el) {
	            if (el.nodeName === 'option') {
	                setOption(el, values);
	            } else {
	                lookupOption(el, values);
	            }
	        });
	    }
	
	    function setOption(vdom, values) {
	        var props = vdom.props;
	        if (!('disabled' in props)) {
	            var value = getOptionValue(vdom, props);
	            value = String(value || '').trim();
	            props.selected = values.indexOf(value) !== -1;
	
	            if (vdom.dom) {
	                vdom.dom.selected = props.selected;
	                var v = vdom.dom.selected; //必须加上这个,防止移出节点selected失效
	            }
	        }
	    }
	
	    function getOptionValue(vdom, props) {
	        if (props && 'value' in props) {
	            return props.value + '';
	        }
	        var arr = [];
	        vdom.children.forEach(function (el) {
	            if (el.nodeName === '#text') {
	                arr.push(el.nodeValue);
	            } else if (el.nodeName === '#document-fragment') {
	                arr.push(getOptionValue(el));
	            }
	        });
	        return arr.join('');
	    }
	
	    function getSelectedValue(vdom, arr) {
	        vdom.children.forEach(function (el) {
	            if (el.nodeName === 'option') {
	                if (el.props.selected === true) arr.push(getOptionValue(el, el.props));
	            } else if (el.children) {
	                getSelectedValue(el, arr);
	            }
	        });
	        return arr;
	    }
	
	    var updateDataActions = {
	        input: function input(prop) {
	            //处理单个value值处理
	            var field = this;
	            prop = prop || 'value';
	            var dom = field.dom;
	            var rawValue = dom[prop];
	            var parsedValue = field.parseValue(rawValue);
	
	            //有时候parse后一致,vm不会改变,但input里面的值
	            field.value = rawValue;
	            field.setValue(parsedValue);
	            duplexCb(field);
	            var pos = field.pos;
	            /* istanbul ignore if */
	            if (dom.caret) {
	                field.setCaret(dom, pos);
	            }
	            //vm.aaa = '1234567890'
	            //处理 <input ms-duplex='@aaa|limitBy(8)'/>{{@aaa}} 这种格式化同步不一致的情况 
	        },
	        radio: function radio() {
	            var field = this;
	            if (field.isChecked) {
	                var val = !field.value;
	                field.setValue(val);
	                duplexCb(field);
	            } else {
	                updateDataActions.input.call(field);
	                field.value = NaN;
	            }
	        },
	        checkbox: function checkbox() {
	            var field = this;
	            var array = field.value;
	            if (!Array.isArray(array)) {
	                avalon.warn('ms-duplex应用于checkbox上要对应一个数组');
	                array = [array];
	            }
	            var method = field.dom.checked ? 'ensure' : 'remove';
	            if (array[method]) {
	                var val = field.parseValue(field.dom.value);
	                array[method](val);
	                duplexCb(field);
	            }
	            this.__test__ = array;
	        },
	        select: function select() {
	            var field = this;
	            var val = avalon(field.dom).val(); //字符串或字符串数组
	            if (val + '' !== this.value + '') {
	                if (Array.isArray(val)) {
	                    //转换布尔数组或其他
	                    val = val.map(function (v) {
	                        return field.parseValue(v);
	                    });
	                } else {
	                    val = field.parseValue(val);
	                }
	                field.setValue(val);
	                duplexCb(field);
	            }
	        },
	        contenteditable: function contenteditable() {
	            updateDataActions.input.call(this, 'innerHTML');
	        }
	    };
	
	    function duplexCb(field) {
	        if (field.userCb) {
	            field.userCb.call(field.vm, {
	                type: 'changed',
	                target: field.dom
	            });
	        }
	    }
	
	    function updateDataHandle(event) {
	        var elem = this;
	        var field = elem._ms_duplex_;
	        if (elem.composing) {
	            //防止onpropertychange引发爆栈
	            return;
	        }
	        if (elem.value === field.value) {
	            return;
	        }
	        /* istanbul ignore if*/
	        if (elem.caret) {
	            try {
	                var pos = field.getCaret(elem);
	                field.pos = pos;
	            } catch (e) {}
	        }
	        /* istanbul ignore if*/
	        if (field.debounceTime > 4) {
	            var timestamp = new Date();
	            var left = timestamp - field.time || 0;
	            field.time = timestamp;
	            /* istanbul ignore if*/
	            if (left >= field.debounceTime) {
	                updateDataActions[field.dtype].call(field);
	                /* istanbul ignore else*/
	            } else {
	                clearTimeout(field.debounceID);
	                field.debounceID = setTimeout(function () {
	                    updateDataActions[field.dtype].call(field);
	                }, left);
	            }
	        } else {
	            updateDataActions[field.dtype].call(field);
	        }
	    }
	
	    var rchangeFilter = /\|\s*change\b/;
	    var rdebounceFilter = /\|\s*debounce(?:\(([^)]+)\))?/;
	    function duplexBeforeInit() {
	        var expr = this.expr;
	        if (rchangeFilter.test(expr)) {
	            this.isChanged = true;
	            expr = expr.replace(rchangeFilter, '');
	        }
	        var match = expr.match(rdebounceFilter);
	        if (match) {
	            expr = expr.replace(rdebounceFilter, '');
	            if (!this.isChanged) {
	                this.debounceTime = parseInt(match[1], 10) || 300;
	            }
	        }
	        this.expr = expr;
	    }
	    function duplexInit() {
	        var expr = this.expr;
	        var node = this.node;
	        var etype = node.props.type;
	        this.parseValue = parseValue;
	        //处理数据转换器
	        var parsers = this.param,
	            dtype;
	        var isChecked = false;
	        parsers = parsers ? parsers.split('-').map(function (a) {
	            if (a === 'checked') {
	                isChecked = true;
	            }
	            return a;
	        }) : [];
	        node.duplex = this;
	        if (rcheckedType.test(etype) && isChecked) {
	            //如果是radio, checkbox,判定用户使用了checked格式函数没有
	            parsers = [];
	            dtype = 'radio';
	            this.isChecked = isChecked;
	        }
	        this.parsers = parsers;
	        if (!/input|textarea|select/.test(node.nodeName)) {
	            if ('contenteditable' in node.props) {
	                dtype = 'contenteditable';
	            }
	        } else if (!dtype) {
	            dtype = node.nodeName === 'select' ? 'select' : etype === 'checkbox' ? 'checkbox' : etype === 'radio' ? 'radio' : 'input';
	        }
	        this.dtype = dtype;
	
	        //判定是否使用了 change debounce 过滤器
	        // this.isChecked = /boolean/.test(parsers)
	        if (dtype !== 'input' && dtype !== 'contenteditable') {
	            delete this.isChange;
	            delete this.debounceTime;
	        } else if (!this.isChecked) {
	            this.isString = true;
	        }
	
	        var cb = node.props['data-duplex-changed'];
	        if (cb) {
	            var arr = addScope(cb, 'xx');
	            var body = makeHandle(arr[0]);
	            this.userCb = new Function('$event', 'var __vmodel__ = this\nreturn ' + body);
	        }
	    }
	    function duplexDiff(newVal, oldVal) {
	        if (Array.isArray(newVal)) {
	            if (newVal + '' !== this.compareVal) {
	                this.compareVal = newVal + '';
	                return true;
	            }
	        } else {
	            newVal = this.parseValue(newVal);
	            if (!this.isChecked) {
	                this.value = newVal += '';
	            }
	            if (newVal !== this.compareVal) {
	                this.compareVal = newVal;
	                return true;
	            }
	        }
	    }
	
	    function duplexBind(vdom, addEvent) {
	        var dom = vdom.dom;
	        this.dom = dom;
	        this.vdom = vdom;
	        this.duplexCb = updateDataHandle;
	        dom._ms_duplex_ = this;
	        //绑定事件
	        addEvent(dom, this);
	    }
	
	    var valueHijack = true;
	    try {
	        //#272 IE9-IE11, firefox
	        var setters = {};
	        var aproto = HTMLInputElement.prototype;
	        var bproto = HTMLTextAreaElement.prototype;
	        var newSetter = function newSetter(value) {
	            // jshint ignore:line
	            setters[this.tagName].call(this, value);
	            var data = this._ms_duplex_;
	            if (!this.caret && data && data.isString) {
	                data.duplexCb.call(this, { type: 'setter' });
	            }
	        };
	        var inputProto = HTMLInputElement.prototype;
	        Object.getOwnPropertyNames(inputProto); //故意引发IE6-8等浏览器报错
	        setters['INPUT'] = Object.getOwnPropertyDescriptor(aproto, 'value').set;
	
	        Object.defineProperty(aproto, 'value', {
	            set: newSetter
	        });
	        setters['TEXTAREA'] = Object.getOwnPropertyDescriptor(bproto, 'value').set;
	        Object.defineProperty(bproto, 'value', {
	            set: newSetter
	        });
	        valueHijack = false;
	    } catch (e) {
	        //在chrome 43中 ms-duplex终于不需要使用定时器实现双向绑定了
	        // http://updates.html5rocks.com/2015/04/DOM-attributes-now-on-the-prototype
	        // https://docs.google.com/document/d/1jwA8mtClwxI-QJuHT7872Z0pxpZz8PBkf2bGAbsUtqs/edit?pli=1
	    }
	
	    function parseValue(val) {
	        for (var i = 0, k; k = this.parsers[i++];) {
	            var fn = avalon.parsers[k];
	            if (fn) {
	                val = fn.call(this, val);
	            }
	        }
	        return val;
	    }
	
	    var updateView = {
	        input: function input() {
	            //处理单个value值处理
	            var vdom = this.node;
	            var value = this.value + '';
	            vdom.dom.value = vdom.props.value = value;
	        },
	        updateChecked: function updateChecked(vdom, checked) {
	            if (vdom.dom) {
	                vdom.dom.defaultChecked = vdom.dom.checked = checked;
	            }
	        },
	        radio: function radio() {
	            //处理单个checked属性
	            var node = this.node;
	            var nodeValue = node.props.value;
	            var checked;
	            if (this.isChecked) {
	                checked = !!this.value;
	            } else {
	                checked = this.value + '' === nodeValue;
	            }
	            node.props.checked = checked;
	            updateView.updateChecked(node, checked);
	        },
	        checkbox: function checkbox() {
	            //处理多个checked属性
	            var node = this.node;
	            var props = node.props;
	            var value = props.value + '';
	            var values = [].concat(this.value);
	            var checked = values.some(function (el) {
	                return el + '' === value;
	            });
	
	            props.defaultChecked = props.checked = checked;
	            updateView.updateChecked(node, checked);
	        },
	        select: function select() {
	            //处理子级的selected属性
	            var a = Array.isArray(this.value) ? this.value.map(String) : this.value + '';
	            lookupOption(this.node, a);
	        },
	        contenteditable: function contenteditable() {
	            //处理单个innerHTML 
	
	            var vnodes = fromString(this.value);
	            var fragment = createFragment();
	            for (var i = 0, el; el = vnodes[i++];) {
	                var child = avalon.vdom(el, 'toDOM');
	                fragment.appendChild(child);
	            }
	            avalon.clearHTML(this.dom).appendChild(fragment);
	            var list = this.node.children;
	            list.length = 0;
	            Array.prototype.push.apply(list, vnodes);
	
	            this.duplexCb.call(this.dom);
	        }
	    };
	
	    /* 
	     * 通过绑定事件同步vmodel
	     * 总共有三种方式同步视图
	     * 1. 各种事件 input, change, click, propertychange, keydown...
	     * 2. value属性重写
	     * 3. 定时器轮询
	     */
	
	    function updateDataEvents(dom, data) {
	        var events = {};
	        //添加需要监听的事件
	        switch (data.dtype) {
	            case 'radio':
	            case 'checkbox':
	                events.click = updateDataHandle;
	                break;
	            case 'select':
	                events.change = updateDataHandle;
	                break;
	            case 'contenteditable':
	                /* istanbul ignore if */
	                if (data.isChanged) {
	                    events.blur = updateDataHandle;
	                    /* istanbul ignore else */
	                } else {
	                    /* istanbul ignore if*/
	
	                    if (avalon.modern) {
	                        if (window$1.webkitURL) {
	                            // http://code.metager.de/source/xref/WebKit/LayoutTests/fast/events/
	                            // https://bugs.webkit.org/show_bug.cgi?id=110742
	                            events.webkitEditableContentChanged = updateDataHandle;
	                        } else if (window$1.MutationEvent) {
	                            events.DOMCharacterDataModified = updateDataHandle;
	                        }
	                        events.input = updateDataHandle;
	                        /* istanbul ignore else */
	                    } else {
	                        events.keydown = updateModelKeyDown;
	                        events.paste = updateModelDelay;
	                        events.cut = updateModelDelay;
	                        events.focus = closeComposition;
	                        events.blur = openComposition;
	                    }
	                }
	                break;
	            case 'input':
	                /* istanbul ignore if */
	                if (data.isChanged) {
	                    events.change = updateDataHandle;
	                    /* istanbul ignore else */
	                } else {
	                    //http://www.cnblogs.com/rubylouvre/archive/2013/02/17/2914604.html
	                    //http://www.matts411.com/post/internet-explorer-9-oninput/
	                    if (msie < 10) {
	                        //IE6-8的propertychange有问题,第一次用JS修改值时不会触发,而且你是全部清空value也不会触发
	                        //IE9的propertychange不支持自动完成,退格,删除,复制,贴粘,剪切或点击右边的小X的清空操作
	                        events.propertychange = updateModelHack;
	                        events.paste = updateModelDelay;
	                        events.cut = updateModelDelay;
	                        //IE9在第一次删除字符时不会触发oninput
	                        events.keyup = updateModelKeyDown;
	                    } else {
	                        events.input = updateDataHandle;
	                        events.compositionstart = openComposition;
	                        //微软拼音输入法的问题需要在compositionend事件中处理
	                        events.compositionend = closeComposition;
	                        //https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/TypedArray
	                        //处理低版本的标准浏览器,通过Int8Array进行区分
	                        if (!/\[native code\]/.test(window$1.Int8Array)) {
	                            events.keydown = updateModelKeyDown; //safari < 5 opera < 11
	                            events.paste = updateModelDelay; //safari < 5
	                            events.cut = updateModelDelay; //safari < 5 
	                            if (window$1.netscape) {
	                                // Firefox <= 3.6 doesn't fire the 'input' event when text is filled in through autocomplete
	                                events.DOMAutoComplete = updateDataHandle;
	                            }
	                        }
	                    }
	                }
	                break;
	        }
	
	        if (/password|text/.test(dom.type)) {
	            events.focus = openCaret; //判定是否使用光标修正功能 
	            events.blur = closeCaret;
	            data.getCaret = getCaret;
	            data.setCaret = setCaret;
	        }
	
	        for (var name in events) {
	            avalon.bind(dom, name, events[name]);
	        }
	    }
	
	    function updateModelHack(e) {
	        if (e.propertyName === 'value') {
	            updateDataHandle.call(this, e);
	        }
	    }
	
	    function updateModelDelay(e) {
	        var elem = this;
	        setTimeout(function () {
	            updateDataHandle.call(elem, e);
	        }, 0);
	    }
	
	    function openCaret() {
	        this.caret = true;
	    }
	    /* istanbul ignore next */
	    function closeCaret() {
	        this.caret = false;
	    }
	    /* istanbul ignore next */
	    function openComposition() {
	        this.composing = true;
	    }
	    /* istanbul ignore next */
	    function closeComposition(e) {
	        this.composing = false;
	        updateModelDelay.call(this, e);
	    }
	    /* istanbul ignore next */
	    function updateModelKeyDown(e) {
	        var key = e.keyCode;
	        // ignore
	        //    command            modifiers                   arrows
	        if (key === 91 || 15 < key && key < 19 || 37 <= key && key <= 40) return;
	        updateDataHandle.call(this, e);
	    }
	
	    getShortID(openCaret);
	    getShortID(closeCaret);
	    getShortID(openComposition);
	    getShortID(closeComposition);
	    getShortID(updateDataHandle);
	    getShortID(updateModelHack);
	    getShortID(updateModelDelay);
	    getShortID(updateModelKeyDown);
	
	    //IE6-8要处理光标时需要异步
	    var mayBeAsync = function mayBeAsync(fn) {
	        setTimeout(fn, 0);
	    };
	    /* istanbul ignore next */
	    function setCaret(target, cursorPosition) {
	        var range$$1;
	        if (target.createTextRange) {
	            mayBeAsync(function () {
	                target.focus();
	                range$$1 = target.createTextRange();
	                range$$1.collapse(true);
	                range$$1.moveEnd('character', cursorPosition);
	                range$$1.moveStart('character', cursorPosition);
	                range$$1.select();
	            });
	        } else {
	            target.focus();
	            if (target.selectionStart !== undefined) {
	                target.setSelectionRange(cursorPosition, cursorPosition);
	            }
	        }
	    }
	    /* istanbul ignore next*/
	    function getCaret(target) {
	        var start = 0;
	        var normalizedValue;
	        var range$$1;
	        var textInputRange;
	        var len;
	        var endRange;
	
	        if (target.selectionStart + target.selectionEnd > -1) {
	            start = target.selectionStart;
	        } else {
	            range$$1 = document$1.selection.createRange();
	
	            if (range$$1 && range$$1.parentElement() === target) {
	                len = target.value.length;
	                normalizedValue = target.value.replace(/\r\n/g, '\n');
	
	                textInputRange = target.createTextRange();
	                textInputRange.moveToBookmark(range$$1.getBookmark());
	
	                endRange = target.createTextRange();
	                endRange.collapse(false);
	
	                if (textInputRange.compareEndPoints('StartToEnd', endRange) > -1) {
	                    start = len;
	                } else {
	                    start = -textInputRange.moveStart('character', -len);
	                    start += normalizedValue.slice(0, start).split('\n').length - 1;
	                }
	            }
	        }
	
	        return start;
	    }
	
	    avalon.directive('duplex', {
	        priority: 9999999,
	        beforeInit: duplexBeforeInit,
	        init: duplexInit,
	        diff: duplexDiff,
	        update: function update(vdom, value) {
	            if (!this.dom) {
	                duplexBind.call(this, vdom, updateDataEvents);
	            }
	            //如果不支持input.value的Object.defineProperty的属性支持,
	            //需要通过轮询同步, chrome 42及以下版本需要这个hack
	            pollValue.call(this, avalon.msie, valueHijack);
	            //更新视图
	
	            updateView[this.dtype].call(this);
	        }
	    });
	
	    function pollValue(isIE, valueHijack$$1) {
	        var dom = this.dom;
	        if (this.isString && valueHijack$$1 && !isIE && !dom.valueHijack) {
	            dom.valueHijack = updateDataHandle;
	            var intervalID = setInterval(function () {
	                if (!avalon.contains(avalon.root, dom)) {
	                    clearInterval(intervalID);
	                } else {
	                    dom.valueHijack({ type: 'poll' });
	                }
	            }, 30);
	            return intervalID;
	        }
	    }
	    avalon.__pollValue = pollValue; //export to test
	    /* istanbul ignore if */
	    if (avalon.msie < 8) {
	        var oldUpdate = updateView.updateChecked;
	        updateView.updateChecked = function (vdom, checked) {
	            var dom = vdom.dom;
	            if (dom) {
	                setTimeout(function () {
	                    oldUpdate(vdom, checked);
	                    dom.firstCheckedIt = 1;
	                }, dom.firstCheckedIt ? 31 : 16);
	                //IE6,7 checkbox, radio是使用defaultChecked控制选中状态，
	                //并且要先设置defaultChecked后设置checked
	                //并且必须设置延迟(因为必须插入DOM树才生效)
	            }
	        };
	    }
	
	    avalon.directive('rules', {
	        diff: function diff(rules) {
	            if (isObject(rules)) {
	                var vdom = this.node;
	                vdom.rules = platform.toJson(rules);
	                return true;
	            }
	        }
	    });
	    function isRegExp(value) {
	        return avalon.type(value) === 'regexp';
	    }
	    var rmail = /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/i;
	    var rurl = /^(ftp|http|https):\/\/(\w+:{0,1}\w*@)?(\S+)(:[0-9]+)?(\/|\/([\w#!:.?+=&%@!\-\/]))?$/;
	    function isCorrectDate(value) {
	        if (typeof value === "string" && value) {
	            //是字符串但不能是空字符
	            var arr = value.split("-"); //可以被-切成3份，并且第1个是4个字符
	            if (arr.length === 3 && arr[0].length === 4) {
	                var year = ~~arr[0]; //全部转换为非负整数
	                var month = ~~arr[1] - 1;
	                var date = ~~arr[2];
	                var d = new Date(year, month, date);
	                return d.getFullYear() === year && d.getMonth() === month && d.getDate() === date;
	            }
	        }
	        return false;
	    }
	    //https://github.com/adform/validator.js/blob/master/validator.js
	    avalon.shadowCopy(avalon.validators, {
	        pattern: {
	            message: '必须匹配{{pattern}}这样的格式',
	            get: function get(value, field, next) {
	                var elem = field.dom;
	                var data = field.data;
	                if (!isRegExp(data.pattern)) {
	                    var h5pattern = elem.getAttribute("pattern");
	                    data.pattern = new RegExp('^(?:' + h5pattern + ')$');
	                }
	                next(data.pattern.test(value));
	                return value;
	            }
	        },
	        digits: {
	            message: '必须整数',
	            get: function get(value, field, next) {
	                //整数
	                next(/^\-?\d+$/.test(value));
	                return value;
	            }
	        },
	        number: {
	            message: '必须数字',
	            get: function get(value, field, next) {
	                //数值
	                next(!!value && isFinite(value)); // isFinite('') --> true
	                return value;
	            }
	        },
	        norequired: {
	            message: '',
	            get: function get(value, field, next) {
	                next(true);
	                return value;
	            }
	        },
	        required: {
	            message: '必须填写',
	            get: function get(value, field, next) {
	                next(value !== '');
	                return value;
	            }
	        },
	        equalto: {
	            message: '密码输入不一致',
	            get: function get(value, field, next) {
	                var id = String(field.data.equalto);
	                var other = avalon(document.getElementById(id)).val() || "";
	                next(value === other);
	                return value;
	            }
	        },
	        date: {
	            message: '日期格式不正确',
	            get: function get(value, field, next) {
	                var data = field.data;
	                if (isRegExp(data.date)) {
	                    next(data.date.test(value));
	                } else {
	                    next(isCorrectDate(value));
	                }
	                return value;
	            }
	        },
	        url: {
	            message: 'URL格式不正确',
	            get: function get(value, field, next) {
	                next(rurl.test(value));
	                return value;
	            }
	        },
	        email: {
	            message: 'email格式不正确',
	            get: function get(value, field, next) {
	                next(rmail.test(value));
	                return value;
	            }
	        },
	        minlength: {
	            message: '最少输入{{minlength}}个字',
	            get: function get(value, field, next) {
	                var num = parseInt(field.data.minlength, 10);
	                next(value.length >= num);
	                return value;
	            }
	        },
	        maxlength: {
	            message: '最多输入{{maxlength}}个字',
	            get: function get(value, field, next) {
	                var num = parseInt(field.data.maxlength, 10);
	                next(value.length <= num);
	                return value;
	            }
	        },
	        min: {
	            message: '输入值不能小于{{min}}',
	            get: function get(value, field, next) {
	                var num = parseInt(field.data.min, 10);
	                next(parseFloat(value) >= num);
	                return value;
	            }
	        },
	        max: {
	            message: '输入值不能大于{{max}}',
	            get: function get(value, field, next) {
	                var num = parseInt(field.data.max, 10);
	                next(parseFloat(value) <= num);
	                return value;
	            }
	        },
	        chs: {
	            message: '必须是中文字符',
	            get: function get(value, field, next) {
	                next(/^[\u4e00-\u9fa5]+$/.test(value));
	                return value;
	            }
	        }
	    });
	
	    var valiDir = avalon.directive('validate', {
	        diff: function diff(validator) {
	            var vdom = this.node;
	            if (vdom.validator) {
	                return;
	            }
	            if (isObject(validator)) {
	                //注意，这个Form标签的虚拟DOM有两个验证对象
	                //一个是vmValidator，它是用户VM上的那个原始子对象，也是一个VM
	                //一个是validator，它是vmValidator.$model， 这是为了防止IE6－8添加子属性时添加的hack
	                //也可以称之为safeValidate
	                vdom.vmValidator = validator;
	                validator = platform.toJson(validator);
	                validator.vdom = vdom;
	                vdom.validator = validator;
	                for (var name in valiDir.defaults) {
	                    if (!validator.hasOwnProperty(name)) {
	                        validator[name] = valiDir.defaults[name];
	                    }
	                }
	                validator.fields = validator.fields || [];
	                return true;
	            }
	        },
	        update: function update(vdom) {
	
	            var validator = vdom.validator;
	            var dom = validator.dom = vdom.dom;
	            dom._ms_validate_ = validator;
	            var fields = validator.fields;
	            collectFeild(vdom.children, fields, validator);
	            avalon.bind(document, 'focusin', function (e) {
	                var dom = e.target;
	                var duplex = dom._ms_duplex_;
	                var vdom = (duplex || {}).vdom;
	                if (duplex && vdom.rules && !duplex.validator) {
	                    if (avalon.Array.ensure(fields, duplex)) {
	                        bindValidateEvent(duplex, validator);
	                    }
	                }
	            });
	
	            //为了方便用户手动执行验证，我们需要为原始vmValidate上添加一个onManual方法
	            var v = vdom.vmValidator;
	            try {
	                v.onManual = onManual;
	            } catch (e) {}
	            delete vdom.vmValidator;
	
	            dom.setAttribute('novalidate', 'novalidate');
	
	            function onManual() {
	                valiDir.validateAll.call(validator, validator.onValidateAll);
	            }
	            /* istanbul ignore if */
	            if (validator.validateAllInSubmit) {
	                avalon.bind(dom, 'submit', function (e) {
	                    e.preventDefault();
	                    onManual();
	                });
	            }
	        },
	        validateAll: function validateAll(callback) {
	            var validator = this;
	            var vdom = this.vdom;
	            var fields = validator.fields = [];
	            collectFeild(vdom.children, fields, validator);
	            var fn = typeof callback === 'function' ? callback : validator.onValidateAll;
	            var promises = validator.fields.filter(function (field) {
	                var el = field.dom;
	                return el && !el.disabled && validator.dom.contains(el);
	            }).map(function (field) {
	                return valiDir.validate(field, true);
	            });
	            var uniq = {};
	            return Promise.all(promises).then(function (array) {
	                var reasons = array.concat.apply([], array);
	                if (validator.deduplicateInValidateAll) {
	
	                    reasons = reasons.filter(function (reason) {
	                        var el = reason.element;
	                        var uuid = el.uniqueID || (el.uniqueID = setTimeout('1'));
	
	                        if (uniq[uuid]) {
	                            return false;
	                        } else {
	                            return uniq[uuid] = true;
	                        }
	                    });
	                }
	                fn.call(validator.dom, reasons); //这里只放置未通过验证的组件
	            });
	        },
	
	        validate: function validate(field, isValidateAll, event) {
	            var promises = [];
	            var value = field.value;
	            var elem = field.dom;
	
	            /* istanbul ignore if */
	            if (typeof Promise !== 'function') {
	                //avalon-promise不支持phantomjs
	                avalon.warn('浏览器不支持原生Promise,请下载并<script src=url>引入\nhttps://github.com/RubyLouvre/avalon/blob/master/test/promise.js');
	            }
	            /* istanbul ignore if */
	            if (elem.disabled) return;
	            var rules = field.vdom.rules;
	            var ngs = [],
	                isOk = true;
	            if (!(rules.norequired && value === '')) {
	                for (var ruleName in rules) {
	                    var ruleValue = rules[ruleName];
	                    if (ruleValue === false) continue;
	                    var hook = avalon.validators[ruleName];
	                    var resolve;
	                    promises.push(new Promise(function (a, b) {
	                        resolve = a;
	                    }));
	                    var next = function next(a) {
	                        var reason = {
	                            element: elem,
	                            data: field.data,
	                            message: elem.getAttribute('data-' + ruleName + '-message') || elem.getAttribute('data-message') || hook.message,
	                            validateRule: ruleName,
	                            getMessage: getMessage
	                        };
	                        if (a) {
	                            resolve(true);
	                        } else {
	                            isOk = false;
	                            ngs.push(reason);
	                            resolve(false);
	                        }
	                    };
	                    field.data = {};
	                    field.data[ruleName] = ruleValue;
	                    hook.get(value, field, next);
	                }
	            }
	
	            //如果promises不为空，说明经过验证拦截器
	            return Promise.all(promises).then(function (array) {
	                if (!isValidateAll) {
	                    var validator = field.validator;
	                    if (isOk) {
	                        validator.onSuccess.call(elem, [{
	                            data: field.data,
	                            element: elem
	                        }], event);
	                    } else {
	                        validator.onError.call(elem, ngs, event);
	                    }
	                    validator.onComplete.call(elem, ngs, event);
	                }
	                return ngs;
	            });
	        }
	    });
	
	    function collectFeild(nodes, fields, validator) {
	        for (var i = 0, vdom; vdom = nodes[i++];) {
	            var duplex = vdom.rules && vdom.duplex;
	            if (duplex) {
	                fields.push(duplex);
	                bindValidateEvent(duplex, validator);
	            } else if (vdom.children) {
	                collectFeild(vdom.children, fields, validator);
	            } else if (Array.isArray(vdom)) {
	                collectFeild(vdom, fields, validator);
	            }
	        }
	    }
	
	    function bindValidateEvent(field, validator) {
	
	        var node = field.dom;
	        if (field.validator) {
	            return;
	        }
	        field.validator = validator;
	        /* istanbul ignore if */
	        if (validator.validateInKeyup && !field.isChanged && !field.debounceTime) {
	            avalon.bind(node, 'keyup', function (e) {
	                validator.validate(field, 0, e);
	            });
	        }
	        /* istanbul ignore if */
	        if (validator.validateInBlur) {
	            avalon.bind(node, 'blur', function (e) {
	                validator.validate(field, 0, e);
	            });
	        }
	        /* istanbul ignore if */
	        if (validator.resetInFocus) {
	            avalon.bind(node, 'focus', function (e) {
	                validator.onReset.call(node, e, field);
	            });
	        }
	    }
	    var rformat = /\\?{{([^{}]+)\}}/gm;
	
	    function getMessage() {
	        var data = this.data || {};
	        return this.message.replace(rformat, function (_, name) {
	            return data[name] == null ? '' : data[name];
	        });
	    }
	    valiDir.defaults = {
	        validate: valiDir.validate,
	        onError: avalon.noop,
	        onSuccess: avalon.noop,
	        onComplete: avalon.noop,
	        onManual: avalon.noop,
	        onReset: avalon.noop,
	        onValidateAll: avalon.noop,
	        validateInBlur: true, //@config {Boolean} true，在blur事件中进行验证,触发onSuccess, onError, onComplete回调
	        validateInKeyup: true, //@config {Boolean} true，在keyup事件中进行验证,触发onSuccess, onError, onComplete回调
	        validateAllInSubmit: true, //@config {Boolean} true，在submit事件中执行onValidateAll回调
	        resetInFocus: true, //@config {Boolean} true，在focus事件中执行onReset回调,
	        deduplicateInValidateAll: false //@config {Boolean} false，在validateAll回调中对reason数组根据元素节点进行去重
	    };
	
	    /**
	     * 一个directive装饰器
	     * @returns {directive}
	     */
	    // DirectiveDecorator(scope, binding, vdom, this)
	    // Decorator(vm, options, callback)
	    function Directive(vm, binding, vdom, render) {
	        var type = binding.type;
	        var decorator = avalon.directives[type];
	        if (inBrowser) {
	            var dom = avalon.vdom(vdom, 'toDOM');
	            if (dom.nodeType === 1) {
	                dom.removeAttribute(binding.attrName);
	            }
	            vdom.dom = dom;
	        }
	        var callback = decorator.update ? function (value) {
	            if (!render.mount && /css|visible|duplex/.test(type)) {
	                render.callbacks.push(function () {
	                    decorator.update.call(directive$$1, directive$$1.node, value);
	                });
	            } else {
	                decorator.update.call(directive$$1, directive$$1.node, value);
	            }
	        } : avalon.noop;
	        for (var key in decorator) {
	            binding[key] = decorator[key];
	        }
	        binding.node = vdom;
	        var directive$$1 = new Action(vm, binding, callback);
	        if (directive$$1.init) {
	            //这里可能会重写node, callback, type, name
	            directive$$1.init();
	        }
	        directive$$1.update();
	        return directive$$1;
	    }
	
	    var eventMap = avalon.oneObject('animationend,blur,change,input,' + 'click,dblclick,focus,keydown,keypress,keyup,mousedown,mouseenter,' + 'mouseleave,mousemove,mouseout,mouseover,mouseup,scan,scroll,submit', 'on');
	    function parseAttributes(dirs, tuple) {
	        var node = tuple[0],
	            uniq = {},
	            bindings = [];
	        var hasIf = false;
	        for (var name in dirs) {
	            var value = dirs[name];
	            var arr = name.split('-');
	            // ms-click
	            if (name in node.props) {
	                var attrName = name;
	            } else {
	                attrName = ':' + name.slice(3);
	            }
	            if (eventMap[arr[1]]) {
	                arr.splice(1, 0, 'on');
	            }
	            //ms-on-click
	            if (arr[1] === 'on') {
	                arr[3] = parseFloat(arr[3]) || 0;
	            }
	
	            var type = arr[1];
	            if (type === 'controller' || type === 'important') continue;
	            if (directives[type]) {
	
	                var binding = {
	                    type: type,
	                    param: arr[2],
	                    attrName: attrName,
	                    name: arr.join('-'),
	                    expr: value,
	                    priority: directives[type].priority || type.charCodeAt(0) * 100
	                };
	                if (type === 'if') {
	                    hasIf = true;
	                }
	                if (type === 'on') {
	                    binding.priority += arr[3];
	                }
	                if (!uniq[binding.name]) {
	                    uniq[binding.name] = value;
	                    bindings.push(binding);
	                    if (type === 'for') {
	                        return [avalon.mix(binding, tuple[3])];
	                    }
	                }
	            }
	        }
	        bindings.sort(byPriority);
	
	        if (hasIf) {
	            var ret = [];
	            for (var i = 0, el; el = bindings[i++];) {
	                ret.push(el);
	                if (el.type === 'if') {
	                    return ret;
	                }
	            }
	        }
	        return bindings;
	    }
	    function byPriority(a, b) {
	        return a.priority - b.priority;
	    }
	
	    var rimprovePriority = /[+-\?]/;
	    var rinnerValue = /__value__\)$/;
	    function parseInterpolate(dir) {
	        var rlineSp = /\n\r?/g;
	        var str = dir.nodeValue.trim().replace(rlineSp, '');
	        var tokens = [];
	        do {
	            //aaa{{@bbb}}ccc
	            var index = str.indexOf(config.openTag);
	            index = index === -1 ? str.length : index;
	            var value = str.slice(0, index);
	            if (/\S/.test(value)) {
	                tokens.push(avalon.quote(avalon._decode(value)));
	            }
	            str = str.slice(index + config.openTag.length);
	            if (str) {
	                index = str.indexOf(config.closeTag);
	                var value = str.slice(0, index);
	                var expr = avalon.unescapeHTML(value);
	                if (/\|\s*\w/.test(expr)) {
	                    //如果存在过滤器，优化干掉
	                    var arr = addScope(expr, 'expr');
	                    if (arr[1]) {
	                        expr = arr[1].replace(rinnerValue, arr[0] + ')');
	                    }
	                }
	                if (rimprovePriority) {
	                    expr = '(' + expr + ')';
	                }
	                tokens.push(expr);
	
	                str = str.slice(index + config.closeTag.length);
	            }
	        } while (str.length);
	        return [{
	            expr: tokens.join('+'),
	            name: 'expr',
	            type: 'expr'
	        }];
	    }
	
	    function getChildren(arr) {
	        var count = 0;
	        for (var i = 0, el; el = arr[i++];) {
	            if (el.nodeName === '#document-fragment') {
	                count += getChildren(el.children);
	            } else {
	                count += 1;
	            }
	        }
	        return count;
	    }
	    function groupTree(parent, children) {
	        children && children.forEach(function (vdom) {
	            if (!vdom) return;
	            var vlength = vdom.children && getChildren(vdom.children);
	            if (vdom.nodeName === '#document-fragment') {
	                var dom = createFragment();
	            } else {
	                dom = avalon.vdom(vdom, 'toDOM');
	                var domlength = dom.childNodes && dom.childNodes.length;
	                if (domlength && vlength && domlength > vlength) {
	                    if (!appendChildMayThrowError[dom.nodeName]) {
	                        avalon.clearHTML(dom);
	                    }
	                }
	            }
	            if (vlength) {
	                groupTree(dom, vdom.children);
	                if (vdom.nodeName === 'select') {
	                    var values = [];
	                    getSelectedValue(vdom, values);
	                    lookupOption(vdom, values);
	                }
	            }
	            //高级版本可以尝试 querySelectorAll
	
	            try {
	                if (!appendChildMayThrowError[parent.nodeName]) {
	                    parent.appendChild(dom);
	                }
	            } catch (e) {}
	        });
	    }
	
	    function dumpTree(elem) {
	        var firstChild;
	        while (firstChild = elem.firstChild) {
	            if (firstChild.nodeType === 1) {
	                dumpTree(firstChild);
	            }
	            elem.removeChild(firstChild);
	        }
	    }
	
	    function getRange(childNodes, node) {
	        var i = childNodes.indexOf(node) + 1;
	        var deep = 1,
	            nodes = [],
	            end;
	        nodes.start = i;
	        while (node = childNodes[i++]) {
	            nodes.push(node);
	            if (node.nodeName === '#comment') {
	                if (startWith(node.nodeValue, 'ms-for:')) {
	                    deep++;
	                } else if (node.nodeValue === 'ms-for-end:') {
	                    deep--;
	                    if (deep === 0) {
	                        end = node;
	                        nodes.pop();
	                        break;
	                    }
	                }
	            }
	        }
	        nodes.end = end;
	        return nodes;
	    }
	
	    function startWith(long, short) {
	        return long.indexOf(short) === 0;
	    }
	
	    var appendChildMayThrowError = {
	        '#text': 1,
	        '#comment': 1,
	        script: 1,
	        style: 1,
	        noscript: 1
	    };
	
	    /**
	     * 生成一个渲染器,并作为它第一个遇到的ms-controller对应的VM的$render属性
	     * @param {String|DOM} node
	     * @param {ViewModel|Undefined} vm
	     * @param {Function|Undefined} beforeReady
	     * @returns {Render}
	     */
	    avalon.scan = function (node, vm, beforeReady) {
	        return new Render(node, vm, beforeReady || avalon.noop);
	    };
	
	    /**
	     * avalon.scan 的内部实现
	     */
	    function Render(node, vm, beforeReady) {
	        this.root = node; //如果传入的字符串,确保只有一个标签作为根节点
	        this.vm = vm;
	        this.beforeReady = beforeReady;
	        this.bindings = []; //收集待加工的绑定属性
	        this.callbacks = [];
	        this.directives = [];
	        this.init();
	    }
	
	    Render.prototype = {
	        /**
	         * 开始扫描指定区域
	         * 收集绑定属性
	         * 生成指令并建立与VM的关联
	         */
	        init: function init() {
	            var vnodes;
	            if (this.root && this.root.nodeType > 0) {
	                vnodes = fromDOM(this.root); //转换虚拟DOM
	                //将扫描区域的每一个节点与其父节点分离,更少指令对DOM操作时,对首屏输出造成的频繁重绘
	                dumpTree(this.root);
	            } else if (typeof this.root === 'string') {
	                vnodes = fromString(this.root); //转换虚拟DOM
	            } else {
	                return avalon.warn('avalon.scan first argument must element or HTML string');
	            }
	
	            this.root = vnodes[0];
	            this.vnodes = vnodes;
	            this.scanChildren(vnodes, this.vm, true);
	        },
	        scanChildren: function scanChildren(children, scope, isRoot) {
	            for (var i = 0; i < children.length; i++) {
	                var vdom = children[i];
	                switch (vdom.nodeName) {
	                    case '#text':
	                        scope && this.scanText(vdom, scope);
	                        break;
	                    case '#comment':
	                        scope && this.scanComment(vdom, scope, children);
	                        break;
	                    case '#document-fragment':
	                        this.scanChildren(vdom.children, scope, false);
	                        break;
	                    default:
	                        this.scanTag(vdom, scope, children, false);
	                        break;
	                }
	            }
	            if (isRoot) {
	                this.complete();
	            }
	        },
	
	
	        /**
	         * 从文本节点获取指令
	         * @param {type} vdom 
	         * @param {type} scope
	         * @returns {undefined}
	         */
	        scanText: function scanText(vdom, scope) {
	            if (config.rexpr.test(vdom.nodeValue)) {
	                this.bindings.push([vdom, scope, {
	                    nodeValue: vdom.nodeValue
	                }]);
	            }
	        },
	
	
	        /**
	         * 从注释节点获取指令
	         * @param {type} vdom 
	         * @param {type} scope
	         * @param {type} parentChildren
	         * @returns {undefined}
	         */
	        scanComment: function scanComment(vdom, scope, parentChildren) {
	            if (startWith(vdom.nodeValue, 'ms-for:')) {
	                this.getForBinding(vdom, scope, parentChildren);
	            }
	        },
	
	
	        /**
	         * 从元素节点的nodeName与属性中获取指令
	         * @param {type} vdom 
	         * @param {type} scope
	         * @param {type} parentChildren
	         * @param {type} isRoot 用于执行complete方法
	         * @returns {undefined}
	         */
	        scanTag: function scanTag(vdom, scope, parentChildren, isRoot) {
	            var dirs = {},
	                attrs = vdom.props,
	                hasDir,
	                hasFor;
	            for (var attr in attrs) {
	                var value = attrs[attr];
	                var oldName = attr;
	                if (attr.charAt(0) === ':') {
	                    attr = 'ms-' + attr.slice(1);
	                }
	                if (startWith(attr, 'ms-')) {
	                    dirs[attr] = value;
	                    var type = attr.match(/\w+/g)[1];
	                    type = eventMap[type] || type;
	                    if (!directives[type]) {
	                        avalon.warn(attr + ' has not registered!');
	                    }
	                    hasDir = true;
	                }
	                if (attr === 'ms-for') {
	                    hasFor = value;
	                    delete attrs[oldName];
	                }
	            }
	            var $id = dirs['ms-important'] || dirs['ms-controller'];
	            if ($id) {
	                /**
	                 * 后端渲染
	                 * serverTemplates后端给avalon添加的对象,里面都是模板,
	                 * 将原来后端渲染好的区域再还原成原始样子,再被扫描
	                 */
	                var templateCaches = avalon.serverTemplates;
	                var temp = templateCaches && templateCaches[$id];
	                if (temp) {
	                    avalon.log('前端再次渲染后端传过来的模板');
	                    var node = fromString(tmpl)[0];
	                    for (var i in node) {
	                        vdom[i] = node[i];
	                    }
	                    delete templateCaches[$id];
	                    this.scanTag(vdom, scope, parentChildren, isRoot);
	                    return;
	                }
	                //推算出指令类型
	                var type = dirs['ms-important'] === $id ? 'important' : 'controller';
	                //推算出用户定义时属性名,是使用ms-属性还是:属性
	                var attrName = 'ms-' + type in attrs ? 'ms-' + type : ':' + type;
	
	                if (inBrowser) {
	                    delete attrs[attrName];
	                }
	                var dir = directives[type];
	                scope = dir.getScope.call(this, $id, scope);
	                if (!scope) {
	                    return;
	                } else {
	                    var clazz = attrs['class'];
	                    if (clazz) {
	                        attrs['class'] = (' ' + clazz + ' ').replace(' ms-controller ', '').trim();
	                    }
	                }
	                var render = this;
	                scope.$render = render;
	                this.callbacks.push(function () {
	                    //用于删除ms-controller
	                    dir.update.call(render, vdom, attrName, $id);
	                });
	            }
	            if (hasFor) {
	                if (vdom.dom) {
	                    vdom.dom.removeAttribute(oldName);
	                }
	                return this.getForBindingByElement(vdom, scope, parentChildren, hasFor);
	            }
	
	            if (/^ms\-/.test(vdom.nodeName)) {
	                attrs.is = vdom.nodeName;
	            }
	
	            if (attrs['is']) {
	                if (!dirs['ms-widget']) {
	                    dirs['ms-widget'] = '{}';
	                }
	                hasDir = true;
	            }
	            if (hasDir) {
	                this.bindings.push([vdom, scope, dirs]);
	            }
	            var children = vdom.children;
	            //如果存在子节点,并且不是容器元素(script, stype, textarea, xmp...)
	            if (!orphanTag[vdom.nodeName] && children && children.length && !delayCompileNodes(dirs)) {
	                this.scanChildren(children, scope, false);
	            }
	        },
	
	
	        /**
	         * 将绑定属性转换为指令
	         * 执行各种回调与优化指令
	         * @returns {undefined}
	         */
	        complete: function complete() {
	            this.yieldDirectives();
	            this.beforeReady();
	            if (inBrowser) {
	                var root$$1 = this.root;
	                if (inBrowser) {
	                    var rootDom = avalon.vdom(root$$1, 'toDOM');
	                    groupTree(rootDom, root$$1.children);
	                }
	            }
	
	            this.mount = true;
	            var fn;
	            while (fn = this.callbacks.pop()) {
	                fn();
	            }
	            this.optimizeDirectives();
	        },
	
	
	        /**
	         * 将收集到的绑定属性进行深加工,最后转换指令
	         * @returns {Array<tuple>}
	         */
	        yieldDirectives: function yieldDirectives() {
	            var tuple;
	            while (tuple = this.bindings.shift()) {
	                var vdom = tuple[0],
	                    scope = tuple[1],
	                    dirs = tuple[2],
	                    bindings = [];
	                if ('nodeValue' in dirs) {
	                    bindings = parseInterpolate(dirs);
	                } else if (!('ms-skip' in dirs)) {
	                    bindings = parseAttributes(dirs, tuple);
	                }
	                for (var i = 0, binding; binding = bindings[i++];) {
	                    var dir = directives[binding.type];
	                    if (!inBrowser && /on|duplex|active|hover/.test(binding.type)) {
	                        continue;
	                    }
	                    if (dir.beforeInit) {
	                        dir.beforeInit.call(binding);
	                    }
	
	                    var directive$$1 = new Directive(scope, binding, vdom, this);
	                    this.directives.push(directive$$1);
	                }
	            }
	        },
	
	
	        /**
	         * 修改指令的update与callback方法,让它们以后执行时更加高效
	         * @returns {undefined}
	         */
	        optimizeDirectives: function optimizeDirectives() {
	            for (var i = 0, el; el = this.directives[i++];) {
	                el.callback = directives[el.type].update;
	                el.update = newUpdate;
	                el._isScheduled = false;
	            }
	        },
	
	        update: function update() {
	            for (var i = 0, el; el = this.directives[i++];) {
	                el.update();
	            }
	        },
	
	        /**
	         * 销毁所有指令
	         * @returns {undefined}
	         */
	        dispose: function dispose() {
	            var list = this.directives || [];
	            for (var i = 0, el; el = list[i++];) {
	                el.dispose();
	            }
	            //防止其他地方的this.innerRender && this.innerRender.dispose报错
	            for (var _i5 in this) {
	                if (_i5 !== 'dispose') delete this[_i5];
	            }
	        },
	
	
	        /**
	         * 将循环区域转换为for指令
	         * @param {type} begin 注释节点
	         * @param {type} scope
	         * @param {type} parentChildren
	         * @param {type} userCb 循环结束回调
	         * @returns {undefined}
	         */
	        getForBinding: function getForBinding(begin, scope, parentChildren, userCb) {
	            var expr = begin.nodeValue.replace('ms-for:', '').trim();
	            begin.nodeValue = 'ms-for:' + expr;
	            var nodes = getRange(parentChildren, begin);
	            var end = nodes.end;
	            var fragment = avalon.vdom(nodes, 'toHTML');
	            parentChildren.splice(nodes.start, nodes.length);
	            begin.props = {};
	            this.bindings.push([begin, scope, {
	                'ms-for': expr
	            }, {
	                begin: begin,
	                end: end,
	                expr: expr,
	                userCb: userCb,
	                fragment: fragment,
	                parentChildren: parentChildren
	            }]);
	        },
	
	
	        /**
	         * 在带ms-for元素节点旁添加两个注释节点,组成循环区域
	         * @param {type} vdom
	         * @param {type} scope
	         * @param {type} parentChildren
	         * @param {type} expr
	         * @returns {undefined}
	         */
	        getForBindingByElement: function getForBindingByElement(vdom, scope, parentChildren, expr) {
	            var index = parentChildren.indexOf(vdom); //原来带ms-for的元素节点
	            var props = vdom.props;
	            var begin = {
	                nodeName: '#comment',
	                nodeValue: 'ms-for:' + expr
	            };
	            if (props.slot) {
	                begin.slot = props.slot;
	                delete props.slot;
	            }
	            var end = {
	                nodeName: '#comment',
	                nodeValue: 'ms-for-end:'
	            };
	            parentChildren.splice(index, 1, begin, vdom, end);
	            this.getForBinding(begin, scope, parentChildren, props['data-for-rendered']);
	        }
	    };
	    var viewID;
	
	    function newUpdate() {
	        var oldVal = this.beforeUpdate();
	        var newVal = this.value = this.get();
	        if (this.callback && this.diff(newVal, oldVal)) {
	            this.callback(this.node, this.value);
	            var vm = this.vm;
	            var $render = vm.$render;
	            var list = vm.$events['onViewChange'];
	            /* istanbul ignore if */
	            if (list && $render && $render.root && !avalon.viewChanging) {
	                if (viewID) {
	                    clearTimeout(viewID);
	                    viewID = null;
	                }
	                viewID = setTimeout(function () {
	                    list.forEach(function (el) {
	                        el.callback.call(vm, {
	                            type: 'viewchange',
	                            target: $render.root,
	                            vmodel: vm
	                        });
	                    });
	                });
	            }
	        }
	        this._isScheduled = false;
	    }
	
	    var events = 'onInit,onReady,onViewChange,onDispose,onEnter,onLeave';
	    var componentEvents = avalon.oneObject(events);
	
	    function toObject(value) {
	        var value = platform.toJson(value);
	        if (Array.isArray(value)) {
	            var v = {};
	            value.forEach(function (el) {
	                el && avalon.shadowCopy(v, el);
	            });
	            return v;
	        }
	        return value;
	    }
	    var componentQueue = [];
	    avalon.directive('widget', {
	        delay: true,
	        priority: 4,
	        deep: true,
	        init: function init() {
	            //cached属性必须定义在组件容器里面,不是template中
	            var vdom = this.node;
	            this.cacheVm = !!vdom.props.cached;
	            if (vdom.dom && vdom.nodeName === '#comment') {
	                var comment = vdom.dom;
	            }
	            var oldValue = this.getValue();
	            var value = toObject(oldValue);
	            //外部VM与内部VM
	            // ＝＝＝创建组件的VM＝＝BEGIN＝＝＝
	            var is = vdom.props.is || value.is;
	            this.is = is;
	            var component = avalon.components[is];
	            //外部传入的总大于内部
	            if (!('fragment' in this)) {
	                if (!vdom.isVoidTag) {
	                    //提取组件容器内部的东西作为模板
	                    var text = vdom.children[0];
	                    if (text && text.nodeValue) {
	                        this.fragment = text.nodeValue;
	                    } else {
	                        this.fragment = avalon.vdom(vdom.children, 'toHTML');
	                    }
	                } else {
	                    this.fragment = false;
	                }
	            }
	            //如果组件还没有注册，那么将原元素变成一个占位用的注释节点
	            if (!component) {
	                this.readyState = 0;
	                vdom.nodeName = '#comment';
	                vdom.nodeValue = 'unresolved component placeholder';
	                delete vdom.dom;
	                avalon.Array.ensure(componentQueue, this);
	                return;
	            }
	
	            //如果是非空元素，比如说xmp, ms-*, template
	            var id = value.id || value.$id;
	            var hasCache = avalon.vmodels[id];
	            var fromCache = false;
	            // this.readyState = 1
	            if (hasCache) {
	                comVm = hasCache;
	                this.comVm = comVm;
	                replaceRoot(this, comVm.$render);
	                fromCache = true;
	            } else {
	                if (typeof component === 'function') {
	                    component = new component(value);
	                }
	                var comVm = createComponentVm(component, value, is);
	                this.readyState = 1;
	                fireComponentHook(comVm, vdom, 'Init');
	                this.comVm = comVm;
	
	                // ＝＝＝创建组件的VM＝＝END＝＝＝
	                var innerRender = avalon.scan(component.template, comVm);
	                comVm.$render = innerRender;
	                replaceRoot(this, innerRender);
	                var nodesWithSlot = [];
	                var directives$$1 = [];
	                if (this.fragment || component.soleSlot) {
	                    var curVM = this.fragment ? this.vm : comVm;
	                    var curText = this.fragment || '{{##' + component.soleSlot + '}}';
	                    var childBoss = avalon.scan('<div>' + curText + '</div>', curVM, function () {
	                        nodesWithSlot = this.root.children;
	                    });
	                    directives$$1 = childBoss.directives;
	                    this.childBoss = childBoss;
	                    for (var i in childBoss) {
	                        delete childBoss[i];
	                    }
	                }
	                Array.prototype.push.apply(innerRender.directives, directives$$1);
	
	                var arraySlot = [],
	                    objectSlot = {};
	                //从用户写的元素内部 收集要移动到 新创建的组件内部的元素
	                if (component.soleSlot) {
	                    arraySlot = nodesWithSlot;
	                } else {
	                    nodesWithSlot.forEach(function (el, i) {
	                        //要求带slot属性
	                        if (el.slot) {
	                            var nodes = getRange(nodesWithSlot, el);
	                            nodes.push(nodes.end);
	                            nodes.unshift(el);
	                            objectSlot[el.slot] = nodes;
	                        } else if (el.props) {
	                            var name = el.props.slot;
	                            if (name) {
	                                delete el.props.slot;
	                                if (Array.isArray(objectSlot[name])) {
	                                    objectSlot[name].push(el);
	                                } else {
	                                    objectSlot[name] = [el];
	                                }
	                            }
	                        }
	                    });
	                }
	                //将原来元素的所有孩子，全部移动新的元素的第一个slot的位置上
	                if (component.soleSlot) {
	                    insertArraySlot(innerRender.vnodes, arraySlot);
	                } else {
	                    insertObjectSlot(innerRender.vnodes, objectSlot);
	                }
	            }
	
	            if (comment) {
	                var dom = avalon.vdom(vdom, 'toDOM');
	                comment.parentNode.replaceChild(dom, comment);
	                comVm.$element = innerRender.root.dom = dom;
	                delete this.reInit;
	            }
	
	            //处理DOM节点
	
	            dumpTree(vdom.dom);
	            comVm.$element = vdom.dom;
	            groupTree(vdom.dom, vdom.children);
	            if (fromCache) {
	                fireComponentHook(comVm, vdom, 'Enter');
	            } else {
	                fireComponentHook(comVm, vdom, 'Ready');
	            }
	        },
	        diff: function diff(newVal, oldVal) {
	            if (cssDiff.call(this, newVal, oldVal)) {
	                return true;
	            }
	        },
	
	        update: function update(vdom, value) {
	            //this.oldValue = value //★★防止递归
	
	            switch (this.readyState) {
	                case 0:
	                    if (this.reInit) {
	                        this.init();
	                        this.readyState++;
	                    }
	                    break;
	                case 1:
	                    this.readyState++;
	                    break;
	                default:
	                    this.readyState++;
	                    var comVm = this.comVm;
	                    avalon.viewChanging = true;
	                    avalon.transaction(function () {
	                        for (var i in value) {
	                            if (comVm.hasOwnProperty(i)) {
	                                if (Array.isArray(value[i])) {
	                                    comVm[i] = value[i].concat();
	                                } else {
	                                    comVm[i] = value[i];
	                                }
	                            }
	                        }
	                    });
	
	                    //要保证要先触发孩子的ViewChange 然后再到它自己的ViewChange
	                    fireComponentHook(comVm, vdom, 'ViewChange');
	                    delete avalon.viewChanging;
	                    break;
	            }
	            this.value = avalon.mix(true, {}, value);
	        },
	        beforeDispose: function beforeDispose() {
	            var comVm = this.comVm;
	            if (!this.cacheVm) {
	                fireComponentHook(comVm, this.node, 'Dispose');
	                comVm.$hashcode = false;
	                delete avalon.vmodels[comVm.$id];
	                this.innerRender && this.innerRender.dispose();
	            } else {
	                fireComponentHook(comVm, this.node, 'Leave');
	            }
	        }
	    });
	
	    function replaceRoot(instance, innerRender) {
	        instance.innerRender = innerRender;
	        var root$$1 = innerRender.root;
	        var vdom = instance.node;
	        var slot = vdom.props.slot;
	        for (var i in root$$1) {
	            vdom[i] = root$$1[i];
	        }
	        if (vdom.props && slot) {
	            vdom.props.slot = slot;
	        }
	        innerRender.root = vdom;
	        innerRender.vnodes[0] = vdom;
	    }
	
	    function fireComponentHook(vm, vdom, name) {
	        var list = vm.$events['on' + name];
	        if (list) {
	            list.forEach(function (el) {
	                setTimeout(function () {
	                    el.callback.call(vm, {
	                        type: name.toLowerCase(),
	                        target: vdom.dom,
	                        vmodel: vm
	                    });
	                }, 0);
	            });
	        }
	    }
	
	    function createComponentVm(component, value, is) {
	        var hooks = [];
	        var defaults = component.defaults;
	        collectHooks(defaults, hooks);
	        collectHooks(value, hooks);
	        var obj = {};
	        for (var i in defaults) {
	            var val = value[i];
	            if (val == null) {
	                obj[i] = defaults[i];
	            } else {
	                obj[i] = val;
	            }
	        }
	        obj.$id = value.id || value.$id || avalon.makeHashCode(is);
	        delete obj.id;
	        var def = avalon.mix(true, {}, obj);
	        var vm = avalon.define(def);
	        hooks.forEach(function (el) {
	            vm.$watch(el.type, el.cb);
	        });
	        return vm;
	    }
	
	    function collectHooks(a, list) {
	        for (var i in a) {
	            if (componentEvents[i]) {
	                if (typeof a[i] === 'function' && i.indexOf('on') === 0) {
	                    list.unshift({
	                        type: i,
	                        cb: a[i]
	                    });
	                }
	                //delete a[i] 这里不能删除,会导致再次切换时没有onReady
	            }
	        }
	    }
	
	    function resetParentChildren(nodes, arr) {
	        var dir = arr && arr[0] && arr[0].forDir;
	        if (dir) {
	            dir.parentChildren = nodes;
	        }
	    }
	
	    function insertArraySlot(nodes, arr) {
	        for (var i = 0, el; el = nodes[i]; i++) {
	            if (el.nodeName === 'slot') {
	                resetParentChildren(nodes, arr);
	                nodes.splice.apply(nodes, [i, 1].concat(arr));
	                break;
	            } else if (el.children) {
	                insertArraySlot(el.children, arr);
	            }
	        }
	    }
	
	    function insertObjectSlot(nodes, obj) {
	        for (var i = 0, el; el = nodes[i]; i++) {
	            if (el.nodeName === 'slot') {
	                var name = el.props.name;
	                resetParentChildren(nodes, obj[name]);
	                nodes.splice.apply(nodes, [i, 1].concat(obj[name]));
	                continue;
	            } else if (el.children) {
	                insertObjectSlot(el.children, obj);
	            }
	        }
	    }
	
	    avalon.components = {};
	    avalon.component = function (name, component) {
	
	        component.extend = componentExtend;
	        return addToQueue(name, component);
	    };
	    function addToQueue(name, component) {
	        avalon.components[name] = component;
	        for (var el, i = 0; el = componentQueue[i]; i++) {
	            if (el.is === name) {
	                componentQueue.splice(i, 1);
	                el.reInit = true;
	                delete el.value;
	                el.update();
	                i--;
	            }
	        }
	        return component;
	    }
	
	    function componentExtend(child) {
	        var name = child.displayName;
	        delete child.displayName;
	        var obj = { defaults: avalon.mix(true, {}, this.defaults, child.defaults) };
	        if (child.soleSlot) {
	            obj.soleSlot = child.soleSlot;
	        }
	        obj.template = child.template || this.template;
	        return avalon.component(name, obj);
	    }
	
	    return avalon;
	});
	/* WEBPACK VAR INJECTION */}.call(exports, (function() { return this; }())))

/***/ },
/* 4 */,
/* 5 */,
/* 6 */,
/* 7 */,
/* 8 */
/*!****************************!*\
  !*** ./js/service/user.js ***!
  \****************************/
/***/ function(module, exports, __webpack_require__) {

	/**
	 * Created by JinWYP on 30/11/2016.
	 */
	
	
	var jQuery = __webpack_require__(/*! jquery */ 2);
	var headers = __webpack_require__(/*! ./token */ 9).headers;
	var tokenService = __webpack_require__(/*! ./token */ 9);
	var sessionUserId = tokenService.sessionUserId;
	
	
	
	var url = __webpack_require__(/*! ./token */ 9).url;
	
	
	var userRoleAll = [
	    { name:'systemAdmin', displayName : '系统管理员'},
	    { name:'financer', displayName : '融资方'},
	    { name:'harbor', displayName : '港口'},
	    { name:'supervisor', displayName : '监管'},
	    { name:'trader', displayName : '贸易商'},
	    { name:'traderAccountant', displayName : '贸易商财务'},
	    { name:'fundProvider', displayName : '资金方'},
	    { name:'fundProviderAccountant', displayName : '资金方财务'}
	];
	
	var userRoleTrader = [
	    { name:'trader', displayName : '贸易商'},
	    { name:'traderAccountant', displayName : '贸易商财务'}
	];
	var userRoleFundProvider = [
	    { name:'fundProvider', displayName : '资金方'},
	    { name:'fundProviderAccountant', displayName : '资金方财务'}
	];
	var userRolePartial = userRoleAll.slice(2, 5);
	userRolePartial.push({ name:'fundProvider', displayName : '资金方'});
	
	
	var userRoleAllObject = {};
	var userRoleNameObject = {};
	
	userRoleAll.forEach(function (role, index){
	    userRoleAllObject[role.name] = role.displayName;
	    userRoleNameObject[role.name] = role.name;
	});
	
	exports.userRoleTrader = userRoleTrader;
	exports.userRoleFundProvider = userRoleFundProvider;
	exports.userRoleList = userRolePartial;
	exports.userRoleAllList = userRoleAll;
	exports.userRoleAllObject = userRoleAllObject;
	exports.userRoleKeyObject = userRoleNameObject;
	
	
	exports.login = function (user){
	
	    var params = jQuery.extend({
	        email : '',
	        password : ''
	    }, user);
	
	    return jQuery.ajax({
	        contentType : 'application/json',
	        dataType : 'json',
	        url      : url.login,
	        method   : 'POST',
	        data     : JSON.stringify(params)});
	
	};
	
	exports.logout = function (user){
	
	    tokenService.removeSessionInLocalStorage();
	    window.location.href = '/warehouse/admin/login'
	};
	
	
	exports.getSessionUser = function (query){
	
	    if (sessionUserId){
	        var params = jQuery.extend({}, query);
	
	        return jQuery.ajax({
	            headers : headers,
	            contentType : 'application/json',
	            dataType : 'json',
	            url      : url.session ,
	            method   : 'GET',
	            data     : params
	
	        });
	    }else{
	        return {
	            done : function () {
	                
	            }
	        }
	    }
	};
	
	exports.updateSessionUserInfo = function (user){
	
	    var params = jQuery.extend({}, user);
	
	    return jQuery.ajax({
	        headers : headers,
	        contentType : 'application/json',
	        dataType : 'json',
	        url      : url.session,
	        method   : 'PUT',
	        data     : JSON.stringify(params)
	    });
	
	};
	
	exports.updateSessionUserPassword = function (oldPassword, newPassword){
	
	    var params = jQuery.extend({}, {
	        oldPassword : oldPassword,
	        newPassword : newPassword
	    });
	
	    return jQuery.ajax({
	        headers : headers,
	        contentType : 'application/json',
	        dataType : 'json',
	        url      : url.session + '/password',
	        method   : 'PUT',
	        data     : JSON.stringify(params)
	    });
	
	};
	
	
	
	
	
	exports.getUserList = function (query){
	
	    var params = jQuery.extend({}, query);
	
	    return jQuery.ajax({
	        headers : headers,
	        contentType : 'application/json',
	        dataType : 'json',
	        url      : url.userList,
	        method   : 'GET',
	        data     : params
	
	    });
	
	};
	
	
	exports.getUserInfoById = function (userId, query){
	
	    var params = jQuery.extend({}, query);
	
	    return jQuery.ajax({
	        headers : headers,
	        contentType : 'application/json',
	        dataType : 'json',
	        url      : url.userList + '/' + userId,
	        method   : 'GET',
	        data     : params
	
	    });
	
	};
	
	exports.addNewUser = function (user){
	
	    var params = jQuery.extend({
	        username : '',
	        // password : '',
	        email : '',
	        phone : '',
	        companyName : ''
	        // belongToUser : '', // 资金方财务关联资金方用户ID, 贸易商财务关联贸易商用户ID
	        // role : userRoleAllObject.systemAdmin
	    }, user);
	
	
	    return jQuery.ajax({
	        headers : headers,
	        contentType : 'application/json',
	        dataType : 'json',
	        url      : url.userList,
	        method   : 'POST',
	        data     : JSON.stringify(params)
	
	    });
	
	};
	
	exports.updateUserInfoById = function (userId, user){
	
	    var params = jQuery.extend({}, user);
	
	    return jQuery.ajax({
	        headers : headers,
	        contentType : 'application/json',
	        dataType : 'json',
	        url      : url.userList + '/' + userId,
	        method   : 'PUT',
	        data     : JSON.stringify(params)
	
	    });
	};
	
	exports.resetPasswordByUserId = function (userId, oldPassword, newPassword){
	
	    var params = jQuery.extend({}, {
	        userId : userId,
	        oldPassword : oldPassword || '',
	        newPassword : newPassword || ''
	    });
	
	    return jQuery.ajax({
	        headers : headers,
	        contentType : 'application/json',
	        dataType : 'json',
	        url      : url.userList + '/' + userId + '/password',
	        method   : 'PUT',
	        data     : JSON.stringify(params)
	
	    });
	
	};
	
	
	exports.deleteUser = function (userId, user){
	
	    var params = jQuery.extend({}, user);
	
	    return jQuery.ajax({
	        headers : headers,
	        contentType : 'application/json',
	        dataType : 'json',
	        url      : url.userList + '/' + userId,
	        method   : 'delete',
	        data     : JSON.stringify(params)
	
	    });
	};
	
	
	
	
	
	exports.getCompanyList = function (query){
	
	    var params = jQuery.extend({}, query);
	
	    return jQuery.ajax({
	        headers : headers,
	        contentType : 'application/json',
	        dataType : 'json',
	        url      : url.companyList,
	        method   : 'GET',
	        data     : params
	    });
	
	};
	
	exports.addNewCompany = function (company){
	
	    var params = jQuery.extend({
	        companyName : '',
	        partyClass : ''
	    }, company);
	
	
	    return jQuery.ajax({
	        headers : headers,
	        contentType : 'application/json',
	        dataType : 'json',
	        url      : url.companyList,
	        method   : 'POST',
	        data     : JSON.stringify(params)
	
	    });
	
	};
	
	exports.getCompanyInfoById = function (companyId, query){
	
	    var params = jQuery.extend({}, query);
	
	    return jQuery.ajax({
	        headers : headers,
	        contentType : 'application/json',
	        dataType : 'json',
	        url      : url.companyList + '/' + companyId,
	        method   : 'GET',
	        data     : params
	
	    });
	
	};
	exports.updateCompanyInfoById = function (companyId, company){
	
	    var params = jQuery.extend({}, company);
	
	    return jQuery.ajax({
	        headers : headers,
	        contentType : 'application/json',
	        dataType : 'json',
	        url      : url.companyList + '/' + companyId,
	        method   : 'PUT',
	        data     : JSON.stringify(params)
	
	    });
	
	};
	


/***/ },
/* 9 */
/*!*****************************!*\
  !*** ./js/service/token.js ***!
  \*****************************/
/***/ function(module, exports) {

	/**
	 * Created by JinWYP on 05/12/2016.
	 */
	
	
	var rawToken = localStorage.getItem('feathers-jwt') || '';
	var token = 'Bearer ' + rawToken;
	var sessionUserId = localStorage.getItem('sessionUserId') || '';
	var sessionUserRole = localStorage.getItem('sessionUserRole') || '';
	
	var headers = {
	    "X-Authorization" : token,
	    "x-authorization-userid" : sessionUserId
	};
	
	var prefix = '/api/cang';
	
	var url = {
	    files : '/api/internal/file',
	    financeOrderList : prefix + '/financeorders',
	    depositList : prefix + '/deposits',
	    deliveryList : prefix + '/deliveries',
	    contractList : prefix + '/files',
	    userList : prefix + '/users',
	    companyList : prefix + '/companies',
	    login : prefix + '/auth/login',
	    session : prefix + '/sessionuser'
	};
	
	
	var saveSessionInLocalStorage = function (jwt, user) {
	    localStorage.setItem('feathers-jwt', jwt);
	    localStorage.setItem('sessionUserId', user.userId);
	    localStorage.setItem('sessionUserRole', user.role);
	}
	
	var removeSessionInLocalStorage = function () {
	    localStorage.removeItem('feathers-jwt');
	    localStorage.removeItem('sessionUserId');
	    localStorage.removeItem('sessionUserRole');
	}
	
	module.exports = {
	    url : url,
	    rawToken : rawToken,
	    token : token,
	    headers : headers,
	    sessionUserId : sessionUserId,
	    sessionUserRole : sessionUserRole,
	
	    saveSessionInLocalStorage : saveSessionInLocalStorage,
	    removeSessionInLocalStorage : removeSessionInLocalStorage
	};

/***/ }
/******/ ]);
//# sourceMappingURL=common.bundle.js.map
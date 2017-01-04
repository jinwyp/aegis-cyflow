/**
 * Created by JinWYP on 24/10/2016.
 */

var path = require("path");
var webpack = require('webpack');
var glob = require('glob');

var sourcePath = {
    'js'            : 'js/**/*',
    'jsPage'        : 'js/page/**/*',
    'components'    : 'js/libs/**/*'
};

var distPath = {
    'js'                               : '../dist/js/',
    'jsPage'                           : '../dist/js/page/',
    'jsPageDevTemp'                    : 'js/page-temp-bundle/',
    'components'                       : '../dist/js/libs/'
};




/**
 * 获得路径
 * @param globPath: str
 * @param pathDir: str 对比路径
 * @returns {{}}
 */
function getEntry(globPath, removePrefixDir) {
    var files = glob.sync(globPath);
    var entries = {};
    var entry, dirname, basename, pathname, extname;

    for (var i = 0; i < files.length; i++) {
        entry = files[i];
        dirname = path.dirname(entry);
        extname = path.extname(entry);
        basename = path.basename(entry, extname);
        pathname = path.normalize(path.join(dirname,  basename));

        if (removePrefixDir){
            removePrefixDir = path.normalize(removePrefixDir);
        }else{
            removePrefixDir = dirname
        }

        if(!removePrefixDir.endsWith('/')){
            removePrefixDir = removePrefixDir + '/'
        }

        if(pathname.startsWith(removePrefixDir)){
            pathname = pathname.substring(removePrefixDir.length)
        }

        entries[pathname] = ['./' + entry];

        // console.log(entry, dirname, basename, extname, pathname, removePrefixDir);
    }
    return entries;
}



module.exports = {
    entry: getEntry(sourcePath.jsPage),
    output: {
        path: path.join(__dirname, distPath.jsPageDevTemp),
        filename: "[name].bundle.js",
        chunkFilename: "[id].chunk.js",
        publicPath : '/'
    },


    module: {
        loaders: [

        ]
    },

    plugins: [
        new webpack.ProvidePlugin({
            $: "jquery",
            jQuery: "jquery",
            jquery: 'jquery'

        }),

        new webpack.optimize.CommonsChunkPlugin({
            filename: "common.bundle.js",
            name: "common" // 注意不要.js后缀,  将公共模块提取，生成名为`common`的chunk
        }),

        new webpack.HotModuleReplacementPlugin() //热加载

    ],


    devServer: {
        inline: true, //可以监控js变化
        hot: false, //热启动 Hot Module Replacement
        contentBase: path.join(__dirname),
        publicPath: "/static/admin/js/",
        compress: true,
        port: 8000,  //默认8080
        proxy: {
            "/warehouse": "http://localhost:9000",
            "/static": "http://localhost:9000",
            "/api": "http://localhost:9000",
            "/apimock": "http://192.168.1.54:3030"
        }
    }
};

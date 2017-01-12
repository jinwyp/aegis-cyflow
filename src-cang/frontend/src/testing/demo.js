/**
 * Created by JinWYP on 11/01/2017.
 */


var casper = require('casper').create({
    verbose: true,
    logLevel: "debug"
});
casper.start('http://www.baidu.com/');

casper.then(function() {
    this.echo(this.getTitle());

    this.sendKeys('#kw', 'xxxx')
    this.echo(this.getHTML('#kw'));

    require('utils').dump(this.getElementAttribute('#kw', 'value'));

    this.click('#su');

    this.waitForUrl(/s?wd/, function() {
        this.echo(this.getTitle());
    });

});



casper.run();
function NumberPicker() {
}



NumberPicker.prototype.show = function(options, cb, errCb) {



    var defaults = {
        list: [],
        title:'',
        okText : 'Fine',
        cancelText : 'Annulla',
        min:0,
        max:0,
        reverseSecondList:true,
        checkEnabled:true,
        subtitle1:"da",
        subtitle2:"a",
        subtitleVisible:true
    };

    for (var key in defaults) {
        if (typeof options[key] !== "undefined") {
            defaults[key] = options[key];
        }
    }


    var callback = function(message) {
        var ret = {
            min:message.split("_")[0],
            max:message.split("_")[1]
        }
        cb(ret);
    }

    var errCallback = function(message) {
        if (typeof errCb === 'function') {
            errCb(message);
        }
    }

    cordova.exec(callback,
                 errCallback,
                 "NumberPickerPlugin",
                 "pickNumber",
                 [defaults]
                );
};

var numberPicker = new NumberPicker();
module.exports = numberPicker;

if (!window.plugins) {
    window.plugins = {};
}
if (!window.plugins.numberPicker) {
    window.plugins.numberPicker = numberPicker;
}
var exec = require('cordova/exec');
function NumberPicker() {
    this._callback;
}

NumberPicker.prototype.show = function(options, cb) {
    

  
    var defaults = {
        list: [],
        title:'',
        okText :'Fine',
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
        if (typeof options[key] !== "undefined")
            defaults[key] = options[key];
    }
    this._callback = cb;

    exec(null,
      null,
      "NumberPickerPlugin",
      "openNumberPicker",
      [defaults]
    );
};
NumberPicker.prototype.cancel = function() {
}

NumberPicker.prototype.selectedNumber = function(data) {
    var ret = {
            min:data.split("_")[0],
            max:data.split("_")[1]
        }
    if (this._callback)
        this._callback(ret);
};


var numberPicker = new NumberPicker();
module.exports = numberPicker;

if (!window.plugins) {
    window.plugins = {};
}
if (!window.plugins.numberPicker) {
    window.plugins.numberPicker = numberPicker;
}
# NumberPickerPlugin
Phonegap/Cordova plugin for Android and iOS implementing a native number picker.

# Installation
```
cd your_cordova_based_plugin
cordova plugin add https://github.com/gabrielezereik/NumberPickerPlugin
```
# Usage
In order to open the number picker, all you have to do is to call this function:

```
window.plugins.numberPicker.show(options, onSuccess, onError);
```
where the onSuccess function takes a single parameter with this structure:
```
{
  "min": {"type": "string"},
  "max": {"type": "string"}
}
```
and the onError function takes a single string parameter containing the error.

# Options

The options object is structured as follows: 

```
{
        list:  {"type": "array", "items": { "type": "string"}},
        title: {"type": "string"},
        okText : {"type": "string"},
        cancelText : {"type": "string"},
        min:{"type": "string"},
        max:{"type": "string"},
        reverseSecondList:{"type": "boolean"},
        checkEnabled:{"type": "boolean"},
        subtitle1:{"type": "string"},
        subtitle2:{"type": "string"},
        subtitleVisible:{"type": "boolean"}
}
```

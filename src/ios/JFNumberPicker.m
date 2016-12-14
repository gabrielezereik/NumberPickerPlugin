//  Created by Gabriele Zereik on 10/09/15.
//
//

#import "JFNumberPicker.h"
@interface JFNumberPicker ()

@property (nonatomic) IBOutlet UIView* pickerContainer;
@property (nonatomic) IBOutlet UIPickerView* picker;
@property (nonatomic) IBOutlet UIView* pickerComponentsContainer;
@property (nonatomic) IBOutlet UIView* pickerViewContainer;
@property (nonatomic) IBOutlet UIButton *cancelButton;
@property (nonatomic) IBOutlet UIButton *doneButton;
@property (nonatomic) IBOutlet UILabel *subtitle1;
@property (nonatomic) IBOutlet UILabel *subtitle2;
@property (nonatomic) NSMutableArray * list, * secondList;
@property (nonatomic) NSMutableDictionary *options;
@property (nonatomic) NSInteger * minValue, maxValue;
@property (nonatomic) bool checkEnabled;
@property (nonatomic) bool reverseSecondList;
@property (nonatomic) bool subtitleVisible;
@end

@implementation JFNumberPicker
#define ANIMATION_DURATION 0.3

- (void) openNumberPicker:(CDVInvokedUrlCommand *)command {
    _checkEnabled = true;
    _reverseSecondList = true;
    _options = [command argumentAtIndex:0];
    NSString * all = @"Tutto";

    
    self.list = [_options objectForKey:@"list"];
    self.checkEnabled = [[_options objectForKey:@"checkEnabled"] boolValue];
    self.reverseSecondList = [[_options objectForKey:@"reverseSecondList"] boolValue];
    _subtitleVisible = [[_options objectForKey:@"subtitleVisible"] boolValue];

    if(_reverseSecondList){
        self.secondList = [NSMutableArray arrayWithArray:[[self.list reverseObjectEnumerator] allObjects]];
    }
    else {
        self.secondList = [NSMutableArray arrayWithArray:self.list];
    }
    [self.list insertObject:all atIndex:0];
    [self.secondList insertObject:all atIndex:0];


    [self showPicker];
        CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:@"ok"];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}
- (BOOL) showPicker {
    
    if(!self.pickerContainer){
        [[NSBundle mainBundle] loadNibNamed:@"JFNumberPicker" owner:self options:nil];
    }
    
    [self.doneButton setTitle:[_options objectForKey:@"okText"] forState:UIControlStateNormal];
    [self.cancelButton setTitle:[_options objectForKey:@"title"] forState:UIControlStateNormal];
    if(_subtitleVisible) {
        self.subtitle1.text = [_options objectForKey: @"subtitle1"];
        self.subtitle2.text = [_options objectForKey: @"subtitle2"];
    }
    else {
        self.subtitle1.text = @"";
        self.subtitle2.text = @"";
    }
    _picker.delegate=self;
    _picker.dataSource=self;
    UIInterfaceOrientation deviceOrientation = [UIApplication sharedApplication].statusBarOrientation;
    
    CGFloat width;
    CGFloat height;
    
    [_picker selectRow:[[_options objectForKey:@"min"] integerValue]+1 inComponent:0 animated:NO];
    [_picker selectRow:[[_options objectForKey:@"max"] integerValue]+1 inComponent:1 animated:NO];
    
    [self.pickerContainer removeFromSuperview];
    
    if(UIInterfaceOrientationIsLandscape(deviceOrientation)){
        width = self.webView.superview.frame.size.width;
        height= self.webView.superview.frame.size.height;
    } else {
        width = self.webView.superview.frame.size.width;
        height= self.webView.superview.frame.size.height;
    }

    self.pickerContainer.frame = CGRectMake(0, 0, width, height);
    [self.picker removeFromSuperview];
    
//    self.picker.frame = CGRectMake(0, 0, self.pickerViewContainer.bounds.size.width, self.picker.bounds.size.height);
    

    

    _picker.hidden = true;
    [self.pickerContainer addSubview: self.picker];
    
    [self.webView.superview addSubview: self.pickerContainer];
    
    [self.pickerContainer layoutIfNeeded];
    [self.picker  reloadAllComponents];
    
    CGRect frame = self.pickerComponentsContainer.frame;
    self.pickerComponentsContainer.frame = CGRectOffset(frame,
                                                            0,
                                                            frame.size.height );
    
    self.picker.frame = CGRectOffset(frame,
                                     0,
                                     self.cancelButton.frame.size.height);

    
    self.pickerContainer.backgroundColor = [[UIColor blackColor] colorWithAlphaComponent:0];
    [UIView animateWithDuration:ANIMATION_DURATION
                          delay:0
                        options:UIViewAnimationOptionCurveEaseOut
                     animations:^{
                         self.pickerComponentsContainer.frame = frame;
                         self.pickerContainer.backgroundColor = [[UIColor blackColor] colorWithAlphaComponent:0.4];
                         
                     } completion:^(BOOL finished) {
                         _picker.hidden = false;
                     }];
    
    return true;
}




- (void)jsSelectedNumbers:(NSString *) result {
    NSString *jsCallback = [NSString stringWithFormat:@"window.plugins.numberPicker.selectedNumber(\"%@\");", result];
    [self.commandDelegate evalJs:jsCallback];
}

- (void)jsCancel {
    NSString *jsCallback = @"window.plugins.numberPicker.cancel();";
    [self.commandDelegate evalJs:jsCallback];
}

// returns the number of 'columns' to display.
- (NSInteger)numberOfComponentsInPickerView:(UIPickerView *)pickerView {
    return 2;
}

// returns the # of rows in each component..
- (NSInteger)pickerView:(UIPickerView *)pickerView numberOfRowsInComponent: (NSInteger)component {
    switch (component) {
        case 0:
            return [self.list count];
            break;
        case 1:
            return [self.secondList count];
            break;
    }
}

-(NSString *)pickerView:(UIPickerView *)pickerView titleForRow:(NSInteger)row forComponent:(NSInteger)component {
    switch (component) {
        case 0:
            return [self.list objectAtIndex:row];
            break;

        case 1:
            return [self.secondList objectAtIndex:row];
            break;
    }
}


- (void)pickerView:(UIPickerView *)pickerView didSelectRow:(NSInteger)row inComponent:(NSInteger)component {
    if(!_checkEnabled) return;
    if(_reverseSecondList){
        if(component==0){
            int max = [pickerView selectedRowInComponent:1];
            if(max==0 || row==0) return;
            int newMax = [_secondList count] - row;
            if (max > newMax) {
                [_picker selectRow:newMax inComponent:1 animated:YES];
            }
            return;
        }
        if(component == 1){
            int min = [pickerView selectedRowInComponent:0];
            if(min==0 || row==0) return;
            int max = [_secondList count] - min;
            if (row > max) {
                [_picker selectRow:max inComponent:1 animated:YES];
            }
        }
        return;
    }
    else {
                if([pickerView selectedRowInComponent:1]< [pickerView selectedRowInComponent:0])
                    [_picker selectRow:row inComponent:[pickerView selectedRowInComponent:0] animated:YES];
    }
    
}


#pragma mark - Actions
- (IBAction)doneAction:(id)sender {
    int min =[self.picker selectedRowInComponent:0];
    int max =[self.picker selectedRowInComponent:1];
    if (min!=0 && max!=0 && max > [_secondList count] - min) {
        max = [_secondList count] - min;
    }
    max--;
    min--;
    NSString * returnString = [NSString stringWithFormat:@"%d_%d", min, max];
    [self jsSelectedNumbers:returnString];
    [self hide];

}

- (IBAction)cancelAction:(id)sender {
    [self jsCancel];
    [self hide];
}



- (void)hide {
        CGRect frame = CGRectOffset(self.pickerComponentsContainer.frame,
                                    0,
                                    self.pickerComponentsContainer.frame.size.height);
    _picker.hidden = true;
        [UIView animateWithDuration:ANIMATION_DURATION
                              delay:0
                            options:UIViewAnimationOptionCurveEaseOut
                         animations:^{
                             self.pickerComponentsContainer.frame = frame;
                             self.pickerContainer.backgroundColor = [[UIColor blackColor] colorWithAlphaComponent:0];
                             
                         } completion:^(BOOL finished) {
                             [self.pickerContainer removeFromSuperview];
                         }];
    }


@end

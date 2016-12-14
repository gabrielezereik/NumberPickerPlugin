
//  Created by Gabriele Zereik on 11/09/15.
//
//

#import <Foundation/Foundation.h>
#import <Cordova/CDV.h>

@interface JFNumberPicker: CDVPlugin <UIPickerViewDataSource,UIPickerViewDelegate>

- (void) openNumberPicker:(CDVInvokedUrlCommand *)command;
- (void)jsSelectedNumbers:(NSString *) result;
- (void)jsCancel;
@end

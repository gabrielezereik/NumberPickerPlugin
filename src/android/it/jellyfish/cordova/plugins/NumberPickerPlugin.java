package it.jellyfish.cordova.plugins;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.TextView;

@SuppressLint("NewApi")
public class NumberPickerPlugin extends CordovaPlugin {

	private static final String RESULT_ERROR = "error";
	private static final String RESULT_CANCEL = "cancel";
	private final String pluginName = "NumberPickerPlugin";
	private static int selectedMinIndex = 0;
	private static int selectedMaxIndex = 0;
	private static NumberPicker picker2, picker1;
	private static boolean reverseSecondList = true;

	@Override
	public boolean execute(final String action, final JSONArray data,
			final CallbackContext callbackContext) {
		boolean result = false;
		this.show(data, callbackContext);
		result = true;

		return result;
	}

	public synchronized void show(final JSONArray data,
			final CallbackContext callbackContext) {
		NumberPickerPlugin numberPickerPlugin = this;
		Context currentCtx = cordova.getActivity();
		Runnable runnable;
		JSONObject options = data.optJSONObject(0);

		runnable = runnableNumberPicker(numberPickerPlugin, currentCtx,
				callbackContext, options);
		cordova.getActivity().runOnUiThread(runnable);
	}

	private Runnable runnableNumberPicker(
			final NumberPickerPlugin NumberPickerPlugin,
			final Context currentCtx, final CallbackContext callbackContext,
			final JSONObject options) {
		return new Runnable() {
			@Override
			public void run() {
				JSONArray jsonList;
				try {
					jsonList = options.getJSONArray("list");
					reverseSecondList = options.getBoolean("reverseSecondList");

				} catch (JSONException e) {
					e.printStackTrace();
					return;
				}
				String[] firstList = new String[jsonList.length()+1];
				String[] secondList = new String[jsonList.length()+1];
				int length = jsonList.length();
                firstList[0] = "Tutto";
                secondList[0] = "Tutto";
				if (reverseSecondList) {
					for (int i = 0; i < length; i++) {
						try {
							firstList[i+1] = jsonList.getString(i);
							secondList[length - i] = jsonList.getString(i);

						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				} else {
					for (int i = 0; i < length; i++) {
						try {
							firstList[i+1] = jsonList.getString(i);
							secondList[i+1] = jsonList.getString(i);

						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}
				openDialog(currentCtx, firstList, secondList, callbackContext,
						options);
			}
		};
	}

	private void openDialog(Context context, String[] firstList, String[] secondList, final CallbackContext callbackContext, final JSONObject options) {
		
		int minValue, maxValue;
		String title="", subtitle1 = "", subtitle2 = "", okText = "", cancelText = "";
        final boolean checkEnabled;
        boolean subtitleVisible;
    	 try {
    		 
             minValue = options.getInt("min");
             maxValue = options.getInt("max");
             reverseSecondList = options.getBoolean("reverseSecondList");
             checkEnabled = options.getBoolean("checkEnabled");
             subtitleVisible = options.getBoolean("subtitleVisible");

         } catch (JSONException e) {
             e.printStackTrace();
             return;
         }
    	 
    	 try {
    		 title = options.getString("title");
    		 subtitle1 = options.getString("subtitle1");
    		 subtitle2 = options.getString("subtitle2");
    		 okText = options.getString("okText");
    		 cancelText = options.getString("cancelText");
    	 } catch (JSONException e){
    		 e.printStackTrace();
    	 }
    	
    	
    	
    	
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        
        //        father
        LinearLayout layout = new LinearLayout(context);
        LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(parms);
        
        layout.setGravity(Gravity.CENTER);
        layout.setPadding(2, 2, 2, 2);
        
        LinearLayout firstChild = new LinearLayout(context);
        LinearLayout.LayoutParams childParms = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        firstChild.setOrientation(LinearLayout.HORIZONTAL);
        firstChild.setLayoutParams(childParms);
        firstChild.setGravity(Gravity.CENTER);
        firstChild.setWeightSum(1.0f);
        
        
        TextView da = new TextView(context);
        da.setText(subtitle1);
        da.setPadding(5, 5, 5, 5);
        da.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams child1Parms = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT,0.5f);
        da.setTextSize(20);
        da.setLayoutParams(child1Parms);
        firstChild.addView(da);
        
        TextView a = new TextView(context);
        a.setText(subtitle2);
        a.setPadding(5, 5, 5, 5);
        a.setGravity(Gravity.CENTER);
        a.setTextSize(20);
        a.setLayoutParams(child1Parms);
        firstChild.addView(a);
        
        
        //body
        LinearLayout child = new LinearLayout(context);
        child.setOrientation(LinearLayout.HORIZONTAL);
        child.setLayoutParams(childParms);
        child.setGravity(Gravity.CENTER);
        child.setWeightSum(1.0f);
        
        LinearLayout child1 = new LinearLayout(context);
        child1.setLayoutParams(child1Parms);
        child1.setGravity(Gravity.CENTER);
        child1.setPadding(2, 2, 2, 2);
        picker1 = new NumberPicker(context);
        picker1.setDisplayedValues(firstList);
        picker1.setMinValue(0);
        picker1.setMaxValue(firstList.length-1);
        picker1.setValue(minValue+1);
        selectedMinIndex = minValue+1;
        picker1.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        setDividerColor(picker1,Color.parseColor("#04c29e"));
        picker1.setWrapSelectorWheel(false);

        final int firstLength = firstList.length;
        picker1.setOnValueChangedListener(new OnValueChangeListener() {
            
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                selectedMinIndex = newVal;
                if(selectedMaxIndex==0 || newVal==0 || !checkEnabled) return;
                if(reverseSecondList) {
	                int newMax = firstLength - newVal;
	                if (selectedMaxIndex > newMax) {
	                	picker2.setValue(newMax);
	                	selectedMaxIndex = newMax;
	                }
	                return;
                }
                if(selectedMaxIndex < selectedMinIndex){
                	picker2.setValue(selectedMinIndex);
                	selectedMaxIndex = selectedMinIndex;
                }
            }
        });
        child1.addView(picker1);
        
        
        LinearLayout child2 = new LinearLayout(context);
        LinearLayout.LayoutParams child2Parms = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT,0.5f);
        child2.setLayoutParams(child2Parms);
        child2.setGravity(Gravity.CENTER);
        child2.setPadding(2, 2, 2, 2);
        
        picker2 = new NumberPicker(context);
        picker2.setDisplayedValues(secondList);
        picker2.setMinValue(0);
        picker2.setMaxValue(secondList.length-1);
        picker2.setValue(maxValue+1);
        selectedMaxIndex = maxValue+1;
        picker2.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        setDividerColor(picker2,Color.parseColor("#04c29e"));
        picker2.setWrapSelectorWheel(false);


        picker2.setOnValueChangedListener(new OnValueChangeListener() {
            
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
            	selectedMaxIndex = newVal;
                if(selectedMinIndex==0 || newVal==0 || !checkEnabled) return;
                if(reverseSecondList) {
	                int max = firstLength - selectedMinIndex;
	                if (newVal > max) {
	                	selectedMaxIndex = max;
	                	picker2.setValue(max);
	                }
                    return;
                }
                if(selectedMaxIndex < selectedMinIndex){
                	picker2.setValue(selectedMinIndex);
                	selectedMaxIndex = selectedMinIndex;
                }
            }
        });
        child2.addView(picker2);
        
        child.addView(child1);
        child.addView(child2);
       
        TextView tv = new TextView(context);
        tv.setText(title);
        tv.setPadding(40, 40, 40, 40);
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(20);
        
        layout.addView(tv);
        if(subtitleVisible) {
        	layout.addView(firstChild);
        }
        layout.addView(child);        
        alertDialogBuilder.setView(layout);
        alertDialogBuilder.setNegativeButton(cancelText, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        }); 
        alertDialogBuilder.setPositiveButton(okText, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                selectedMinIndex--;
                selectedMaxIndex--;
                String retValues = selectedMinIndex+"_"+selectedMaxIndex;
                callbackContext.success(retValues);
                
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        try {
            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void setDividerColor(NumberPicker picker, int color) {
        
        java.lang.reflect.Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (java.lang.reflect.Field pf : pickerFields) {
            if (pf.getName().equals("mSelectionDivider")) {
                pf.setAccessible(true);
                try {
                    ColorDrawable colorDrawable = new ColorDrawable(color);
                    pf.set(picker, colorDrawable);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                }
                catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }
    


}
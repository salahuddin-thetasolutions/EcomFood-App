package com.food.ecom.Helpers;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.food.ecom.R;


/**
 * Created by Waseem on 16-Jan-18.
 */

public class CustomDialog extends Dialog implements View.OnClickListener{
//    private static AlertDialog.Builder  mBuilder;
    private Context mContext;
//    private static AlertDialog mDialog;

    private Button mPosButton, mNegButton;


    public CustomDialog(@NonNull Context context) {
        super(context);
        mContext=context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog_layout);
        mPosButton= (Button) findViewById(R.id.PositveBTN);
        mNegButton= (Button) findViewById(R.id.NegativeBTN);
        mPosButton.setOnClickListener(this);
        mNegButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.PositveBTN:
                ((Activity)mContext).finish(); //if the user press the yes button then close the WebActivity.
                break;
            case R.id.NegativeBTN:
                dismiss();  // if the user press No button then dismiss the dialog.
                break;
        }
    }
}

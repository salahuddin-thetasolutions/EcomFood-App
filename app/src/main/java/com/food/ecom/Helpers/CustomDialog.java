package com.food.ecom.Helpers;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.food.ecom.Http.ApiUtils;
import com.food.ecom.R;
import com.food.ecom.model.Message;
import com.food.ecom.model.NotifyData;
import com.food.ecom.model.Product;
import com.food.ecom.model.User;
import com.food.ecom.options.CartListActivity;
import com.food.ecom.startup.MainActivity;
import com.food.ecom.utility.ImageUrlUtils;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by Waseem on 16-Jan-18.
 */

public class CustomDialog extends Dialog implements View.OnClickListener{
//    private static AlertDialog.Builder  mBuilder;
    private Context mContext;
//    private static AlertDialog mDialog;
private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    ArrayList<Product> cartlistProduct;
    private Button mPosButton, mNegButton;
EditText metName,metEmail,metPhone,metAddress;

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
        metName=(EditText)findViewById(R.id.etName);
        metEmail=(EditText)findViewById(R.id.etEmail);
        metPhone=(EditText)findViewById(R.id.etPhone);
        metAddress=(EditText)findViewById(R.id.etAddress);
        mPosButton.setOnClickListener(this);
        mNegButton.setOnClickListener(this);
        mFirebaseInstance = FirebaseDatabase.getInstance();
        // get reference to 'users' node
        mFirebaseDatabase = mFirebaseInstance.getReference("Orders");
        ImageUrlUtils imageUrlUtils = new ImageUrlUtils();
        cartlistProduct =imageUrlUtils.getCartListProduct();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.PositveBTN:
                if (metName.getText().toString().matches(""))
                {
                    metName.setError("Name field is required");
                }else if (metEmail.getText().toString().matches(""))
                {
                    metEmail.setError("Email field is required");
                }else if (metPhone.getText().toString().matches(""))
                {
                    metPhone.setError("Phone field is required");
                }
                else if (metAddress.getText().toString().matches(""))
                {
                    metAddress.setError("Address field is required");
                }else{
                    CreateOrder();
                    Toast.makeText(mContext, "Your Order has been successfully Checkout", Toast.LENGTH_SHORT).show();
                    ((Activity)mContext).finish(); //if the user press the yes button then close the WebActivity.
                }
                break;
            case R.id.NegativeBTN:
                dismiss();  // if the user press No button then dismiss the dialog.
                break;
        }
    }
    /**
     * Creating new user node under 'users'
     */
    private  void CreateOrder() {
        String token = FirebaseInstanceId.getInstance().getToken();
        // TODO
        // In real apps this userId should be fetched
        // by implementing firebase auth
        String  OrderId = mFirebaseDatabase.push().getKey();
        if (TextUtils.isEmpty(OrderId)) {
            OrderId = mFirebaseDatabase.push().getKey();
        }
        User oUser=new User(metName.getText().toString(),metEmail.getText().toString(),metPhone.getText().toString(),metAddress.getText().toString(),token);
        mFirebaseDatabase.child(OrderId).child("User").setValue(oUser);
        for (int i = 0; i < cartlistProduct.size(); i++)
        {
            Product oProduct = cartlistProduct.get(i);
            //do something with i
            // do something with object
            //ImageUrlUtils imageUrlUtils = new ImageUrlUtils();
            mFirebaseDatabase.child(OrderId).child("Product"+i).setValue(oProduct);
            // imageUrlUtils.removeCartListProduct(i);
            //Decrease notification count
            MainActivity.notificationCountCart--;
        }

        //ImageUrlUtils imageUrlUtils = new ImageUrlUtils();
        //cartlistProduct =imageUrlUtils.getCartListProduct();
        //recyclerView.setAdapter(new CartListActivity.SimpleStringRecyclerViewAdapter(recyclerView, cartlistProduct));
        //notifyDataSetChanged();
//        notifyAll();
        // Toast.makeText(mContext, "Successfully Checkout", Toast.LENGTH_SHORT).show();
        SendNotification(token,"Order Status","Your Order has been successfully Checkout");
        ImageUrlUtils imageUrlUtils = new ImageUrlUtils();
        imageUrlUtils.removeCartListAllProducts();
        MainActivity.notificationCountCart=0;
        Intent intent = new Intent(mContext, MainActivity.class);
        mContext.startActivity(intent);
    }

    private void SendNotification(String token,String title,String Message) {
        NotifyData oNotifyData=new NotifyData(title,Message);
        com.food.ecom.model.Message oMessage=new Message(token,oNotifyData,"");
        Call<Message> callresponse= ApiUtils.getAPIService(Constants.BASEURLFCM).sendMessage(oMessage);
        callresponse.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {

                Log.d("Response ", "onResponse");
                //t1.setText("Notification sent");
                Message message = response.body();
                if ( response.code()==201){
                    Toast.makeText(mContext, "Successfully send", Toast.LENGTH_SHORT).show();
                }
                // Log.d("message", message.getMessage_id());
            }
            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                Log.d("Response ", "onFailure");
                //t1.setText("Notification failure");
            }
        });
    }
}

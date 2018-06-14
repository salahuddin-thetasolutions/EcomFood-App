package com.food.ecom.options;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.food.ecom.Helpers.Constants;
import com.food.ecom.Helpers.CustomDialog;
import com.food.ecom.Http.ApiUtils;
import com.food.ecom.R;
import com.food.ecom.model.NotifyData;
import com.food.ecom.model.Message;
import com.food.ecom.model.Product;
import com.food.ecom.product.ItemDetailsActivity;
import com.food.ecom.startup.MainActivity;
import com.food.ecom.utility.ImageUrlUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.food.ecom.fragments.ProductListFragment.STRING_IMAGE_POSITION;
import static com.food.ecom.fragments.ProductListFragment.STRING_IMAGE_URI;

public class CartListActivity extends AppCompatActivity {
    private static Context mContext;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
   static ArrayList<Product> cartlistProduct;
    RecyclerView recyclerView;
    static TextView mtvPrice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_list);
        mContext = CartListActivity.this;
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mtvPrice=(TextView)findViewById(R.id.text_action_bottom1);
        // get reference to 'users' node
        mFirebaseDatabase = mFirebaseInstance.getReference("Orders");
        ImageUrlUtils imageUrlUtils = new ImageUrlUtils();
        cartlistProduct =imageUrlUtils.getCartListProduct();
        //Show cart layout based on items
        setCartLayout();

        recyclerView = (RecyclerView)findViewById(R.id.recyclerview);
        RecyclerView.LayoutManager recylerViewLayoutManager = new LinearLayoutManager(mContext);

        recyclerView.setLayoutManager(recylerViewLayoutManager);
        recyclerView.setAdapter(new CartListActivity.SimpleStringRecyclerViewAdapter(recyclerView, cartlistProduct));
        SetTotalPrice();
    }

    public static class SimpleStringRecyclerViewAdapter
            extends RecyclerView.Adapter<CartListActivity.SimpleStringRecyclerViewAdapter.ViewHolder> {

        private ArrayList<Product> mCartlistProduct;
        private RecyclerView mRecyclerView;

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final SimpleDraweeView mImageView;
            public final LinearLayout mLayoutItem, mLayoutRemove , mLayoutEdit;
            TextView mtvProductName,mtvQty,mtvPrice,mtvSize,mtvDelivery;
            public ViewHolder(View view) {
                super(view);
                mView = view;
                mImageView = (SimpleDraweeView) view.findViewById(R.id.image_cartlist);
                mLayoutItem = (LinearLayout) view.findViewById(R.id.layout_item_desc);
                mLayoutRemove = (LinearLayout) view.findViewById(R.id.layout_action1);
                mLayoutEdit = (LinearLayout) view.findViewById(R.id.layout_action2);
                mtvProductName=(TextView) view.findViewById(R.id.tvProductName);
                mtvPrice=(TextView) view.findViewById(R.id.tvPrice);
                mtvQty=(TextView) view.findViewById(R.id.tvQty);
                mtvDelivery=(TextView) view.findViewById(R.id.tvDelivery);
            }
        }

        public SimpleStringRecyclerViewAdapter(RecyclerView recyclerView, ArrayList<Product> oListProduct) {
            mCartlistProduct= oListProduct;
            mRecyclerView = recyclerView;
        }

        @Override
        public CartListActivity.SimpleStringRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_cartlist_item, parent, false);
            return new CartListActivity.SimpleStringRecyclerViewAdapter.ViewHolder(view);
        }

        @Override
        public void onViewRecycled(CartListActivity.SimpleStringRecyclerViewAdapter.ViewHolder holder) {
            if (holder.mImageView.getController() != null) {
                holder.mImageView.getController().onDetach();
            }
            if (holder.mImageView.getTopLevelDrawable() != null) {
                holder.mImageView.getTopLevelDrawable().setCallback(null);
//                ((BitmapDrawable) holder.mImageView.getTopLevelDrawable()).getBitmap().recycle();
            }
        }

        @Override
        public void onBindViewHolder(final CartListActivity.SimpleStringRecyclerViewAdapter.ViewHolder holder, final int position) {
            final Product oProduct = mCartlistProduct.get(position);
            holder.mImageView.setImageURI(oProduct.getImage());
            holder.mtvQty.setText(oProduct.getQty().toString());
            holder.mtvDelivery.setText("Free");
            holder.mtvProductName.setText(oProduct.getName());
            holder.mtvPrice.setText(oProduct.getPriceString());
            holder.mLayoutItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ItemDetailsActivity.class);
                    intent.putExtra(STRING_IMAGE_URI,oProduct.getImage());
                    intent.putExtra(STRING_IMAGE_POSITION, position);
                    mContext.startActivity(intent);
                }
            });

           //Set click action
            holder.mLayoutRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ImageUrlUtils imageUrlUtils = new ImageUrlUtils();
                    imageUrlUtils.removeCartListProduct(position);
                    notifyDataSetChanged();
                    //Decrease notification count
                    MainActivity.notificationCountCart--;
                    CartListActivity.SetTotalPrice();
                }
            });

            //Set click action
            holder.mLayoutEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                }
            });
        }

        @Override
        public int getItemCount() {
            return mCartlistProduct.size();
        }
    }

    protected void setCartLayout(){
        LinearLayout layoutCartItems = (LinearLayout) findViewById(R.id.layout_items);
        LinearLayout layoutCartPayments = (LinearLayout) findViewById(R.id.layout_payment);
        LinearLayout layoutCartNoItems = (LinearLayout) findViewById(R.id.layout_cart_empty);

        if(MainActivity.notificationCountCart >0){
            layoutCartNoItems.setVisibility(View.GONE);
            layoutCartItems.setVisibility(View.VISIBLE);
            layoutCartPayments.setVisibility(View.VISIBLE);
            layoutCartPayments.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(cartlistProduct!=null) {
                        CustomDialog dialog = new CustomDialog(CartListActivity.this);
                        dialog.show();
                        // the following LOC will change the default layout width height to following .. default is very small.. i don't like.
                        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    }else{
                        Toast.makeText(CartListActivity.this, "Your cart is empty", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else {
            layoutCartNoItems.setVisibility(View.VISIBLE);
            layoutCartItems.setVisibility(View.GONE);
            layoutCartPayments.setVisibility(View.GONE);

            Button bStartShopping = (Button) findViewById(R.id.bAddNew);
            bStartShopping.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });

        }
    }
    public static void SetTotalPrice(){
        double sum = 0;
        for(int i = 0; i < cartlistProduct.size(); i++)
        {
            Product oProduct=cartlistProduct.get(i);
            sum += oProduct.getPrice();
        }
        mtvPrice.setText("Rs- "+ Math.round(sum)+"");
    }
    /**
     * Creating new user node under 'users'
     */
//    private  void CreateOrder() {
//        String token = FirebaseInstanceId.getInstance().getToken();
//        // TODO
//        // In real apps this userId should be fetched
//        // by implementing firebase auth
//      String  OrderId = mFirebaseDatabase.push().getKey();
//        if (TextUtils.isEmpty(OrderId)) {
//            OrderId = mFirebaseDatabase.push().getKey();
//        }
//        mFirebaseDatabase.child(OrderId).child("User").child("name").setValue("Ali");
//        mFirebaseDatabase.child(OrderId).child("User").child("Email").setValue("abc@gmail.com");
//        mFirebaseDatabase.child(OrderId).child("User").child("Phone").setValue("0095522555");
//        mFirebaseDatabase.child(OrderId).child("User").child("Address").setValue("XYZ");
//        mFirebaseDatabase.child(OrderId).child("User").child("Token").setValue(token);
//        for (int i = 0; i < cartlistProduct.size(); i++)
//        {
//            Product oProduct = cartlistProduct.get(i);
//            //do something with i
//            // do something with object
//            //ImageUrlUtils imageUrlUtils = new ImageUrlUtils();
//            mFirebaseDatabase.child(OrderId).child("Product"+i).setValue(oProduct);
//           // imageUrlUtils.removeCartListProduct(i);
//            //Decrease notification count
//            MainActivity.notificationCountCart--;
//        }
//
//        //ImageUrlUtils imageUrlUtils = new ImageUrlUtils();
//        //cartlistProduct =imageUrlUtils.getCartListProduct();
//        //recyclerView.setAdapter(new CartListActivity.SimpleStringRecyclerViewAdapter(recyclerView, cartlistProduct));
//        //notifyDataSetChanged();
////        notifyAll();
//       // Toast.makeText(mContext, "Successfully Checkout", Toast.LENGTH_SHORT).show();
//        SendNotification(token,"Order Status","Your Order has been successfully Checkout");
//        ImageUrlUtils imageUrlUtils = new ImageUrlUtils();
//        imageUrlUtils.removeCartListAllProducts();
//        MainActivity.notificationCountCart=0;
//        Intent intent = new Intent(CartListActivity.this, MainActivity.class);
//        startActivity(intent);
//    }
//
//    private void SendNotification(String token,String title,String Message) {
//        NotifyData oNotifyData=new NotifyData(title,Message);
//        Message oMessage=new Message(token,oNotifyData,"");
//        Call<Message> callresponse= ApiUtils.getAPIService(Constants.BASEURLFCM).sendMessage(oMessage);
//        callresponse.enqueue(new Callback<Message>() {
//            @Override
//            public void onResponse(Call<Message> call, Response<Message> response) {
//
//                Log.d("Response ", "onResponse");
//                //t1.setText("Notification sent");
//                Message message = response.body();
//                if ( response.code()==201){
//                    Toast.makeText(CartListActivity.this, "Successfully send", Toast.LENGTH_SHORT).show();
//                }
//                // Log.d("message", message.getMessage_id());
//            }
//            @Override
//            public void onFailure(Call<Message> call, Throwable t) {
//                Log.d("Response ", "onFailure");
//                //t1.setText("Notification failure");
//            }
//        });
//    }
}

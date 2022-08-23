package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewFriendActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    DatabaseReference mUserRef,requstRef,friendRef;
    String profileImageUrl,username,city,country;
    CircleImageView profileImage;
    TextView Username,address;
    Button btnPerform,btnDecline;
    String CurrentState="nothing_happen";
    String profession;
     String userID;
     String MyprofileImageUrl,Myusername,Mycity,Mycountry,Myprofession;
   String URL="https://fcm.googleapis.com/fcm/send";
   RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_friend);
       userID=getIntent().getStringExtra("userKey");
        requestQueue= Volley.newRequestQueue(this);
        mUserRef= FirebaseDatabase.getInstance().getReference().child("Users");
        requstRef= FirebaseDatabase.getInstance().getReference().child("Requests");
        friendRef= FirebaseDatabase.getInstance().getReference().child("Friends");
        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
        btnPerform=findViewById(R.id.btnPermorm);
        btnDecline=findViewById(R.id.btnDecline);
        profileImage=findViewById(R.id.profileImage);
        Username=findViewById(R.id.Username);
        address=findViewById(R.id.address);

        LoadUser();
        LoadMyProfile();
        btnPerform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PerformAction(userID);
            }
        });
        CheckUserExistance(userID);
        btnDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Unfriend(userID);
            }
        });
    }

    private void LoadMyProfile() {
        mUserRef.child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    MyprofileImageUrl=snapshot.child("profileImage").getValue().toString();
                    Myusername=snapshot.child("username").getValue().toString();
                    Mycity =snapshot.child("city").getValue().toString();
                    Mycountry=snapshot.child("country").getValue().toString();
                    Myprofession=snapshot.child("profession").getValue().toString();
                }
                else
                {
                    Toast.makeText(ViewFriendActivity.this,"Data not Found",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull  DatabaseError error) {
                Toast.makeText(ViewFriendActivity.this,""+error.getMessage().toString(),Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void Unfriend(String userID) {
        if(CurrentState.equals("friend"))
        {
            friendRef.child(mUser.getUid()).child(userID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull  Task<Void> task) {
                    if(task.isSuccessful())
                    {
                        friendRef.child(userID).child(mUser.getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                             if(task.isSuccessful())
                             {
                                 Toast.makeText(ViewFriendActivity.this,"You are Unfriend",Toast.LENGTH_SHORT).show();
                                 CurrentState="nothing_happen";
                                 btnPerform.setText("Send Friend Request");
                                 btnDecline.setVisibility(View.GONE);
                             }
                            }
                        });
                    }

                }
            });
        }
        if(CurrentState.equals("he_sent_pending"))
        {       HashMap hashMap=new HashMap();
                hashMap.put("status","decline");
            requstRef.child(userID).child(mUser.getUid()).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                    {
                      Toast.makeText(ViewFriendActivity.this,"You have Decline Friend",Toast.LENGTH_SHORT).show();
                      CurrentState="he_sent_decline";
                      btnPerform.setVisibility(View.GONE);
                      btnDecline.setVisibility(View.GONE);
                    }
                }
            });
        }
    }

    private void CheckUserExistance(String userID) {
        friendRef.child(mUser.getUid()).child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    CurrentState="friend";
                    btnPerform.setText("Send SMS");
                    btnDecline.setText("Unfriend");
                    btnDecline.setVisibility(View.VISIBLE);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        friendRef.child(userID).child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    CurrentState="friend";
                    btnPerform.setText("Send SMS");
                    btnDecline.setText("Unfriend");
                    btnDecline.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {

            }
        });
        requstRef.child(mUser.getUid()).child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    if(snapshot.child("status").getValue().toString().equals("pending"))
                    {
                        CurrentState="I_sent_pending";
                        btnPerform.setText("CanCel Friend Request");
                        btnDecline.setVisibility(View.GONE);
                    }
                    if(snapshot.child("status").getValue().toString().equals("decline"))
                    {
                        CurrentState="I_sent_decline";
                        btnPerform.setText("CanCel Friend Request");
                        btnDecline.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        requstRef.child(userID).child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    if(snapshot.child("status").getValue().toString().equals("pending"))
                    {
                        CurrentState="he_sent_pending";
                        btnPerform.setText("Accept Friend Request");
                        btnDecline.setText("Decline Friend");
                        btnDecline.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        if(CurrentState.equals("nothing_happen"))
        {
            CurrentState="nothing_happen";
            btnPerform.setText("Send Friend Request");
            btnDecline.setVisibility(View.GONE);
        }
    }

    private void PerformAction(String userID) {
        String pp="Request";
              if(CurrentState.equals("nothing_happen"))
              {
                  HashMap hashMap=new HashMap();
                  hashMap.put("status","pending");
                requstRef.child(mUser.getUid()).child(userID).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful())
                        {
                            sendNotification(pp);
                            Toast.makeText(ViewFriendActivity.this, "You have sent Friend Request", Toast.LENGTH_SHORT).show();
                            btnDecline.setVisibility(View.GONE);
                            CurrentState="I_sent_pending";
                            btnPerform.setText("Cancel Friend Request");
                        }
                        else
                        {
                            Toast.makeText(ViewFriendActivity.this, ""+task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
              }
              if(CurrentState.equals("I_sent_pending")||CurrentState.equals("I_sent_decline"))
              {
                  requstRef.child(mUser.getUid()).child(userID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                      @Override
                      public void onComplete(@NonNull  Task<Void> task) {
                         if(task.isSuccessful())
                         {
                             Toast.makeText(ViewFriendActivity.this, "You have Cancelled Friend Request", Toast.LENGTH_SHORT).show();
                             CurrentState="nothing_happen";
                             btnPerform.setText("Send Friend Request");
                             btnDecline.setVisibility(View.GONE);
                         }
                         else
                         {
                             Toast.makeText(ViewFriendActivity.this, ""+task.getException().toString(), Toast.LENGTH_SHORT).show();
                         }
                         }
                  });
              }
              if(CurrentState.equals("he_sent_pending"))
              {
                 requstRef.child(userID).child(mUser.getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                     @Override
                     public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            HashMap hashMap=new HashMap();
                            hashMap.put("status","friend");
                            hashMap.put("username",username);
                            hashMap.put("profileImageUrl",profileImageUrl);
                            hashMap.put("profession",profession);

                            final HashMap hashMap1=new HashMap();
                            hashMap1.put("status","friend");
                            hashMap1.put("username",Myusername);
                            hashMap1.put("profileImageUrl",MyprofileImageUrl);
                            hashMap1.put("profession",Myprofession);

                            friendRef.child(mUser.getUid()).child(userID).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                             if(task.isSuccessful())
                             {
                                 friendRef.child(userID).child(mUser.getUid()).updateChildren(hashMap1).addOnCompleteListener(new OnCompleteListener() {
                                     @Override
                                     public void onComplete(@NonNull Task task) {
                                         Toast.makeText(ViewFriendActivity.this, "You added friend", Toast.LENGTH_SHORT).show();
                                         CurrentState="friend";
                                         btnPerform.setText("Send SMS");
                                         btnDecline.setText("Unfriend");
                                         btnDecline.setVisibility(View.VISIBLE);

                                     }
                                 });
                             }
                                }
                            });
                        }
                     }
                 }) ;
              }
              if (CurrentState.equals("friend"))
              {
                  Intent intent=new Intent(ViewFriendActivity.this,ChatActivity.class);
                  intent.putExtra("OtherUserID",userID);
                  startActivity(intent);
              }
    }

    private void sendNotification(String pp) {
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("to","/topics/"+userID);
            JSONObject jsonObject1=new JSONObject();
            jsonObject1.put("title","Friend Request from "+Myusername);
            jsonObject1.put("body",pp);

            JSONObject jsonObject2=new JSONObject();
            jsonObject2.put("userID",mUser.getUid());
            jsonObject2.put("type","Request");
            jsonObject.put("notification",jsonObject1);
            jsonObject.put("data",jsonObject2);

            JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST,URL,jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String,String>map=new HashMap<>();
                    map.put("content-type","application/json");
                    map.put("authorization","key=AAAAHijDnfo:APA91bEeUfRtb2h-alFbjyqnaN9f8pHHbrueFCGmZ5g_eMbRytunwLtt_FxdLlEvyzd7sN74DR8QlOckKl1GugEAG9b9_2-NNF9Wfz7EFBWEEH_4-gi5wvLObsjVe3Pf7X8hSbzLvpUz");
                    return map;
                }
            };
            requestQueue.add(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void LoadUser() {
        mUserRef.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    profileImageUrl=snapshot.child("profileImage").getValue().toString();
                    username=snapshot.child("username").getValue().toString();
                    city =snapshot.child("city").getValue().toString();
                    country=snapshot.child("country").getValue().toString();
                    profession=snapshot.child("profession").getValue().toString();
                    Picasso.get().load(profileImageUrl).into(profileImage);
                    Username.setText(username);
                    address.setText(city+","+country);
                }
                else
                {
                    Toast.makeText(ViewFriendActivity.this,"Data not Found",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull  DatabaseError error) {
              Toast.makeText(ViewFriendActivity.this,""+error.getMessage().toString(),Toast.LENGTH_SHORT).show();
            }
        });
    }

}
package com.workshop.chatapp.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.ktx.Firebase;
import com.workshop.chatapp.Models.Messages;
import com.workshop.chatapp.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class ChatAdapter extends RecyclerView.Adapter{

    ArrayList<Messages> messages;
    Context context;

    public ChatAdapter(ArrayList<Messages> messages, Context context, String recieverId) {
        this.messages = messages;
        this.context = context;
        this.recieverId = recieverId;
    }

    String recieverId;

    int isSender=1;
    int isReciever=2;

    public ChatAdapter(ArrayList<Messages> messages, Context context) {
        this.messages = messages;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==isSender){
            View view= LayoutInflater.from(context).inflate(R.layout.sender_view,parent,false);
            return new SenderViewHolder(view);
        }
        else{
            View view= LayoutInflater.from(context).inflate(R.layout.reciever_view,parent,false);
            return new RecieverViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Messages message=messages.get(position);

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new AlertDialog.Builder(context).setTitle("Delete")
                        .setMessage("Do you want to delete this message?")
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //deleting the message
                                FirebaseDatabase database=FirebaseDatabase.getInstance();
                                String senderRoom=FirebaseAuth.getInstance().getUid()+recieverId;
                                database.getReference().child("Chats").child(senderRoom)
                                        .child(message.getMessageId()).setValue(null);
                            }
                        })
                        .setNegativeButton("no", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
                return false;
            }
        });


        if(holder.getClass()==SenderViewHolder.class){
            ((SenderViewHolder) holder).senderMessage.setText(message.getMessage());
            Date date=new Date(message.getTimestamp());
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("HH:MM a");
            String dateString=simpleDateFormat.format(date);
            ((SenderViewHolder) holder).senderTime.setText(dateString.toString());
        }
        else{
            ((RecieverViewHolder) holder).recieverMessage.setText(message.getMessage());
            Date date=new Date(message.getTimestamp());
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("HH:MM a");
            String dateString=simpleDateFormat.format(date);
            ((RecieverViewHolder) holder).recieverTime.setText(dateString.toString());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(messages.get(position).getUid().equals(FirebaseAuth.getInstance().getUid())){
            return isSender;
        }
        else{
            return isReciever;
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class RecieverViewHolder extends RecyclerView.ViewHolder{

        TextView recieverMessage,recieverTime;

        public RecieverViewHolder(@NonNull View itemView) {
            super(itemView);
            recieverMessage=itemView.findViewById(R.id.revieverText);
            recieverTime=itemView.findViewById(R.id.recieverTime);
        }
    }

    public class SenderViewHolder extends RecyclerView.ViewHolder{

        TextView senderMessage,senderTime;

        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            senderMessage=itemView.findViewById(R.id.senderText);
            senderTime=itemView.findViewById(R.id.senderTime);
        }
    }
}

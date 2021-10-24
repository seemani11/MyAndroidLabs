package algonquin.cst2335.thap0011;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import algonquin.cst2335.thap0011.R;

public class ChatRoom extends AppCompatActivity {

    private ArrayList<ChatMessage> messages = new ArrayList<>();
    private RecyclerView chatList;
    private Button sendButtton;
    private Button receiveButtton;
    private EditText textbox;
    private Context context;
    int SEND = 1;
    int RECEIVE = 2;
    private MyChatAdapter adt;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatlayout);

        textbox = (EditText) findViewById(R.id.text_box);
        sendButtton = (Button) findViewById(R.id.send_button);
        receiveButtton = (Button) findViewById(R.id.receive_button);

        chatList = (RecyclerView) findViewById(R.id.myrecycler);

        chatList.setLayoutManager(new LinearLayoutManager(this));


        adt = new MyChatAdapter(context, messages);
        chatList.setAdapter(adt);

        sendButtton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a", Locale.getDefault());
                String currentDateandTime = sdf.format(new Date());

                ChatMessage thisMessage = new ChatMessage(textbox.getText().toString(), SEND, currentDateandTime);
                messages.add(thisMessage);
                adt.notifyItemInserted(messages.size() - 1);
                textbox.setText(""); // clear the text content
            }
        });

        receiveButtton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a", Locale.getDefault());
                String currentDateandTime = sdf.format(new Date());
                ChatMessage thisMessage = new ChatMessage(textbox.getText().toString(), 2, currentDateandTime);
                messages.add(thisMessage);
                adt.notifyItemInserted(messages.size() - 1);
                textbox.setText(""); // clear the text content
                Log.d("Chatroom", "onReceive clicked");
            }
        });
    }


    private class MyRowViews extends RecyclerView.ViewHolder {

        TextView messageText;
        TextView timeText;
        int position;

        public MyRowViews(View itemView) {
            super(itemView);

            itemView.setOnClickListener(click -> {
                this.position = getAbsoluteAdapterPosition();

                AlertDialog.Builder builder = new AlertDialog.Builder(ChatRoom.this);
                builder.setMessage("Do you want to delete the message:" + messageText.getText());
                builder.setTitle("Question:");
                builder.setNegativeButton("No", (dialog, cl) -> {
                });
                builder.setPositiveButton("Yes", (dialog, cl) -> {

                    ChatMessage removedMessage = messages.get(position);
                    messages.remove(position);
                    adt.notifyItemRemoved(position);

                    Snackbar.make(messageText, "You deleted message #" + position, Snackbar.LENGTH_LONG)
                            .setAction("UNDO", clk -> {

                                messages.add(position, removedMessage);
                                adt.notifyItemInserted(position);

                            })
                            .show();
                }).create().show();

            });

            messageText = itemView.findViewById(R.id.message);
            timeText = itemView.findViewById(R.id.time);
        }

        int setPosition(int pos) {
            return position;
        }
    }

    public class MyChatAdapter extends RecyclerView.Adapter<MyRowViews> {
        Context context;
        ArrayList<ChatMessage> messages;

        @Override
        public MyRowViews onCreateViewHolder(ViewGroup parent, int viewType) {

            LayoutInflater inflater = getLayoutInflater();
            int layoutID;
            if (viewType == 1)
                layoutID = R.layout.sent_message;
            else
                layoutID = R.layout.receive_layout;

            View loadedRow = inflater.inflate(layoutID, parent, false); //View loadedRow = inflater.inflate(R.layout.receive_layout, parent, false);
            MyRowViews initRow = new MyRowViews(loadedRow);
            return initRow;
        }

        @Override
        public int getItemViewType(int position) {
            ChatMessage thisRow = messages.get(position);
            int request = thisRow.getSendOrReceive();
            switch (request) {
                case 1:
                    Log.d("getItemViewType", "View 1");
                    return 1;
                case 2:
                    Log.d("getItemViewType", "View 2");
                    return 2;
                default:
                    return -1;
            }
        }

        public MyChatAdapter(Context ctx, ArrayList<ChatMessage> messageList) {
            this.context = ctx;
            this.messages = messageList;
        }

        @Override
        public void onBindViewHolder(MyRowViews holder, int position) {
            holder.messageText.setText(messages.get(position).getMessage());
            holder.timeText.setText(messages.get(position).getTimeSent());
            holder.setPosition(position);
            Log.d("MyChatAdapter", "onBindViewHolder evoked");
        }

        @Override
        public int getItemCount() {
            Log.d("Chatroom", "MyChatAdapter + message size :" + messages.size());
            return messages.size();
        }
    }

    private class ChatMessage {
        String message;
        int sendOrReceive;
        String timeSent;

        public ChatMessage(String messageString, int request, String time) {
            this.message = messageString;
            this.sendOrReceive = request;
            this.timeSent = time;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public int getSendOrReceive() {
            return sendOrReceive;
        }

        public void setSendOrReceive(int sendOrReceive) {
            this.sendOrReceive = sendOrReceive;
        }

        public String getTimeSent() {

            return timeSent;
        }

        public void setTimeSent(String timeSent) {
            this.timeSent = timeSent;
        }
    }
}
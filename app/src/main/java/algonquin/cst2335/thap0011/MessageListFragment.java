package algonquin.cst2335.thap0011;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MessageListFragment extends Fragment {

    private ArrayList<ChatMessage> messages = new ArrayList<>();
    private RecyclerView chatList;
    private Button sendButtton;
    private Button receiveButtton;
    private EditText textbox;
    private Context context;
    int SEND = 1;
    int RECEIVE = 2;
    private MyChatAdapter adt;
    private SQLiteDatabase db;


    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        View chatLayout = inflater.inflate(R.layout.chatlayout, container, false);
        textbox = (EditText) chatLayout.findViewById(R.id.text_box);
        sendButtton = (Button) chatLayout.findViewById(R.id.send_button);
        receiveButtton = (Button) chatLayout.findViewById(R.id.receive_button);

        MyOpenHelper opener = new MyOpenHelper(getContext());
        db = opener.getWritableDatabase();
        Cursor results = db.rawQuery("Select * FROM " + MyOpenHelper.TABLE_NAME + ";", null);

        int _idCol = results.getColumnIndex("_id");
        int messageCol = results.getColumnIndex(MyOpenHelper.col_message);
        int sendCol = results.getColumnIndex(MyOpenHelper.col_send_receive);
        int timeCol = results.getColumnIndex(MyOpenHelper.col_time_sent);

        while(results.moveToNext()) {
            long id = results.getInt(_idCol);
            String message = results.getString(messageCol);
            String time = results.getString(timeCol);
            int sendOrReceive = results.getInt(sendCol);
            messages.add(new ChatMessage(message, sendOrReceive, time, id));
        }

        chatList = (RecyclerView) chatLayout.findViewById(R.id.myrecycler);



        chatList.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        adt = new MessageListFragment.MyChatAdapter(context, messages);
        chatList.setAdapter(adt);

        sendButtton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a", Locale.getDefault());
                String currentDateandTime = sdf.format(new Date());

                ChatMessage thisMessage = new ChatMessage(textbox.getText().toString(), SEND, currentDateandTime);
                ContentValues newRow = new ContentValues();
                newRow.put(MyOpenHelper.col_message, thisMessage.getMessage());
                newRow.put(MyOpenHelper.col_send_receive, thisMessage.getSendOrReceive());
                newRow.put(MyOpenHelper.col_time_sent, thisMessage.getTimeSent());
                long newId = db.insert(MyOpenHelper.TABLE_NAME, MyOpenHelper.col_message, newRow);
                thisMessage.setId(newId);

                messages.add(thisMessage);
                textbox.setText(""); // clear the text content
                adt.notifyItemInserted(messages.size() - 1);

            }
        });

        receiveButtton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a", Locale.getDefault());
                String currentDateandTime = sdf.format(new Date());
                ChatMessage thisMessage = new ChatMessage(textbox.getText().toString(), 2, currentDateandTime);
                ContentValues newRow = new ContentValues();
                newRow.put(MyOpenHelper.col_message, thisMessage.getMessage());
                newRow.put(MyOpenHelper.col_send_receive, 2);
                newRow.put(MyOpenHelper.col_time_sent, thisMessage.getTimeSent());
                long newId = db.insert(MyOpenHelper.TABLE_NAME, MyOpenHelper.col_message, newRow);
                thisMessage.setId(newId);

                messages.add(thisMessage);
                textbox.setText(""); // clear the text content
                adt.notifyItemInserted(messages.size() - 1);
                Log.d("Chatroom", "onReceive clicked");
            }
        });
        return chatLayout;
    }

    public void notifyMessageDeleted(ChatMessage chosenMessage, int chosenPosition) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Do you want to delete the message:" + chosenMessage.getMessage());
        builder.setTitle("Question:");
        builder.setNegativeButton("No", (dialog, cl) -> {
        });
        builder.setPositiveButton("Yes", (dialog, cl) -> {

            MessageListFragment.ChatMessage removedMessage = messages.get(chosenPosition);
            messages.remove(chosenPosition);
            adt.notifyItemRemoved(chosenPosition);

            db.delete(MyOpenHelper.TABLE_NAME, "_id=?", new String[] {Long.toString(removedMessage.getId())});

            Snackbar.make(sendButtton, "You deleted message #" + chosenPosition, Snackbar.LENGTH_LONG)
                    .setAction("UNDO", clk -> {

                        db.execSQL("INSERT INTO " + MyOpenHelper.TABLE_NAME + " values('" + removedMessage.getId() +
                                "','" + removedMessage.getMessage() +
                                "','" + removedMessage.getSendOrReceive() +
                                "','" + removedMessage.getTimeSent() + "');");

                        messages.add(chosenPosition, removedMessage);
                        adt.notifyItemInserted(chosenPosition);

                    })
                    .show();
        }).create().show();
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

    private class MyRowViews extends RecyclerView.ViewHolder {

        TextView messageText;
        TextView timeText;
        int position;

        public MyRowViews(View itemView) {
            super(itemView);

            itemView.setOnClickListener(click -> {
                ChatRoom parentActivity = (ChatRoom) getContext();
                this.position = getAbsoluteAdapterPosition();
                parentActivity.userClickedMessage(messages.get(position), position);


            });

            messageText = itemView.findViewById(R.id.message);
            timeText = itemView.findViewById(R.id.time);
        }

        int setPosition(int pos) {
            return position;
        }
    }

    protected class ChatMessage {
        String message;
        int sendOrReceive;
        String timeSent;
        long id;

        public ChatMessage(String messageString, int request, String time) {
            this.message = messageString;
            this.sendOrReceive = request;
            this.timeSent = time;
        }

        public ChatMessage(String message, int sendOrReceive, String timeSent, long id) {
            this.message = message;
            this.sendOrReceive = sendOrReceive;
            this.timeSent = timeSent;
            setId(id);
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

        public long getId(){
            return id;
        }
        public void setId(long l) {
            this.id = l;
        }
    }
}

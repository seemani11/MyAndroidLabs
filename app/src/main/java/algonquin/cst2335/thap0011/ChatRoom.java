package algonquin.cst2335.thap0011;

import android.content.Context;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Date;

public class ChatRoom extends AppCompatActivity {


    private ArrayList<ChatMessage> messages = new ArrayList<>();
    private RecyclerView chatList;
    private Button sendButtton;
    private Button receiveButtton;
    private EditText textbox;
    private Context context;
    private int SEND = 1;
    private int RECEIVE = 2;
    private MyChatAdapter adt;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatlayout);

        textbox = (EditText) findViewById(R.id.text_box);
        sendButtton = (Button) findViewById(R.id.send_button);
        receiveButtton = (Button) findViewById(R.id.receive_button);

        chatList = (RecyclerView) findViewById(R.id.myrecycler);

        chatList.setLayoutManager(new LinearLayoutManager(this));



        chatList.setAdapter(new MyChatAdapter());
    }

    private class MyChatAdapter extends RecyclerView.Adapter{
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder( RecyclerView.ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }
    }

    // to store the chat message
    private class ChatMessage {
        String message;
        int sendOrReceive;
        Date timeSent;

        public ChatMessage(String messageString, int request, Date time) {
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

        public Date getTimeSent() {

            return timeSent;
        }

        public void setTimeSent(Date timeSent) {
            this.timeSent = timeSent;
        }
    }
}

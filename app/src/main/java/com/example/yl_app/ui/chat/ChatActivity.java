package com.example.yl_app.ui.chat;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.yl_app.R;
import com.example.yl_app.models.ChatMessage;
import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView chatList;
    private EditText etInput;
    private Button btnSend;
    private ChatAdapter adapter;
    private List<ChatMessage> messages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("艺黎小助手");

        chatList = findViewById(R.id.chat_list);
        etInput = findViewById(R.id.et_input);
        btnSend = findViewById(R.id.btn_send);

        chatList.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ChatAdapter(messages);
        chatList.setAdapter(adapter);

        // 欢迎语
        messages.add(new ChatMessage("你好呀！我是艺黎小助手，有什么可以帮助你的吗？", false));
        adapter.notifyDataSetChanged();

        btnSend.setOnClickListener(v -> sendMessage());

        etInput.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                sendMessage();
                return true;
            }
            return false;
        });
    }

    private void sendMessage() {
        String msg = etInput.getText().toString().trim();
        if (msg.isEmpty()) {
            Toast.makeText(this, "请输入内容", Toast.LENGTH_SHORT).show();
            return;
        }

        // 添加用户消息
        messages.add(new ChatMessage(msg, true));
        adapter.notifyItemInserted(messages.size() - 1);
        chatList.scrollToPosition(messages.size() - 1);
        etInput.setText("");

        // 获取回复
        String reply = getLocalReply(msg);

        // 添加AI回复
        messages.add(new ChatMessage(reply, false));
        adapter.notifyItemInserted(messages.size() - 1);
        chatList.scrollToPosition(messages.size() - 1);
    }

    private String getLocalReply(String msg) {
        if (msg.contains("你好") || msg.contains("您好") || msg.contains("嗨")) {
            return "你好呀！我是艺黎小助手，有什么可以帮你的吗？";
        } else if (msg.contains("菜单") || msg.contains("饮品") || msg.contains("喝什么")) {
            return "我们店有奶茶、果茶、纯茶、冰淇淋，还有各种小料可以加哦！\n\n奶茶：12-16元\n果茶：9-18元\n纯茶：7元\n冰淇淋：3元\n小料：1-2元";
        } else if (msg.contains("价格") || msg.contains("多少钱") || msg.contains("价位")) {
            return "奶茶12-16元，果茶9-18元，纯茶7元，冰淇淋3元，小料1-2元~";
        } else if (msg.contains("地址") || msg.contains("在哪") || msg.contains("位置")) {
            return "艺黎奶茶铺位于北京市朝阳区xxx路88号，欢迎来店自取！";
        } else if (msg.contains("营业") || msg.contains("时间") || msg.contains("开门")) {
            return "我们营业时间是早上10点到晚上22点，全天候为您服务~";
        } else if (msg.contains("优惠") || msg.contains("折扣") || msg.contains("券")) {
            return "现在有八折优惠券，可以在「我的」页面领取哦！";
        } else if (msg.contains("外卖") || msg.contains("配送") || msg.contains("送")) {
            return "支持外卖配送，满30元免配送费，请在结算时填写地址~";
        } else if (msg.contains("谢谢") || msg.contains("感谢") || msg.contains("赞")) {
            return "不客气！祝您喝得开心~";
        } else if (msg.contains("推荐") || msg.contains("招牌")) {
            return "招牌推荐：云栖桂露（桂花奶茶）、月映荔枝（荔枝果茶）、紫薯香芋冰淇淋~";
        } else if (msg.contains("小料") || msg.contains("加料")) {
            return "小料有：黑糖珍珠、椰果、小西米、仙草冻、红豆、糯米麻薯、血糯米，每份1-2元~";
        } else if (msg.length() > 20) {
            return "收到啦！小助手正在努力理解中。您可以问我菜单、价格、地址、营业时间等问题~";
        } else {
            return "收到啦！您可以说\"菜单\"看饮品列表，说\"价格\"看价位，或问\"地址\"\"营业时间\"等~";
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
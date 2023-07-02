package com.example.chatnote;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.annotation.NonNull;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatroom.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainNote extends AppCompatActivity {
    private static final int REQUEST_CODE_EDIT = 1;
    //列表展示所有的笔记
    private RecyclerView recyclerView;
    private MemoAdapter memoAdapter;
    private Button buttonNew;

    private List<Memo> memoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_note);

        recyclerView = findViewById(R.id.recyclerView);
        buttonNew = findViewById(R.id.buttonNew);
        //监听new按钮，点击就跳转新建页面
        buttonNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = -1;  // 这里替换为您希望传递的整数参数
                openNoteActivity(position);
            }
        });

        memoList = new ArrayList<>();
        memoAdapter = new MemoAdapter(memoList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(memoAdapter);

        loadMemoListFromTxtFile();
    }

    private void loadMemoListFromTxtFile() {
        memoList.clear();

        String fileName = "memo_list.txt";
        String memoListContent = readFromFile(fileName);

        if (!memoListContent.isEmpty()) {
            String[] memoArray = memoListContent.split("\n");

            for (String memoData : memoArray) {
                String[] parts = memoData.split("::");

                if (parts.length == 2) {
                    String memoTitle = parts[0];
                    String memoContent = parts[1];

                    Memo memo = new Memo(memoTitle, memoContent);
                    memoList.add(memo);
                }
            }

            memoAdapter.notifyDataSetChanged();
        }
    }
    //会进行判断打开的是否合法
    private void openNoteActivity(int position) {
        Intent intent = new Intent(MainNote.this, EditActivity.class);
        if (position >= 0 && position < memoList.size()) {
            Memo memo = memoList.get(position);
            intent.putExtra("title", memo.getTitle());
            intent.putExtra("content", memo.getContent());
            intent.putExtra("position", position);
        }
        startActivityForResult(intent, REQUEST_CODE_EDIT);
    }


    private String readFromFile(String fileName) {
        StringBuilder content = new StringBuilder();

        try {
            File file = new File(getFilesDir(), fileName);
            FileReader reader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(reader);

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line).append("\n");
            }

            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to read file", Toast.LENGTH_SHORT).show();
        }

        return content.toString();
    }

    private void saveToFile(String fileName, String content) {
        try {
            File file = new File(getFilesDir(), fileName);
            FileWriter writer = new FileWriter(file);

            writer.write(content);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to save file", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_EDIT && resultCode == RESULT_OK && data != null) {
            String memoTitle = data.getStringExtra("title");
            String memoContent = data.getStringExtra("content");
            int position = data.getIntExtra("position", -1);

            if (position >= 0 && position < memoList.size()) {
                Memo memo = memoList.get(position);
                memo.setTitle(memoTitle);
                memo.setContent(memoContent);
                memoAdapter.notifyItemChanged(position);
            } else {
                Memo memo = new Memo(memoTitle, memoContent);
                memoList.add(memo);
                memoAdapter.notifyDataSetChanged();
            }
            saveMemoListToTxtFile();
        } else if (requestCode == REQUEST_CODE_EDIT && resultCode == RESULT_CANCELED && data != null) {
            int position = data.getIntExtra("position", -1);
            if (position >= 0 && position < memoList.size()) {
                memoList.remove(position);
                memoAdapter.notifyItemRemoved(position);
                saveMemoListToTxtFile();
            }
        }
    }

    private void saveMemoListToTxtFile() {
        StringBuilder memoListContent = new StringBuilder();

        for (Memo memo : memoList) {
            String memoData = memo.getTitle() + "::" + memo.getContent() + "\n";
            memoListContent.append(memoData);
        }

        saveToFile("memo_list.txt", memoListContent.toString());
    }
    public void clearAllMemos(View view) {
        memoList.clear();
        memoAdapter.notifyDataSetChanged();
        saveMemoListToTxtFile();
        Toast.makeText(this, "All memos cleared", Toast.LENGTH_SHORT).show();
    }


    private static class Memo {
        private String title;
        private String content;

        public Memo(String title, String content) {
            this.title = title;
            this.content = content;
        }

        public String getTitle() {
            return title;
        }

        public String getContent() {
            return content;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }

    private class MemoAdapter extends RecyclerView.Adapter<MemoAdapter.ViewHolder> {
        private List<Memo> memoList;

        public MemoAdapter(List<Memo> memoList) {
            this.memoList = memoList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_memo, parent, false);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Memo memo = memoList.get(position);
            holder.textViewTitle.setText(memo.getTitle());

//            int backgroundColor = holder.getColorForPosition(holder.itemView, position);
//            holder.itemView.setBackgroundColor(backgroundColor);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int clickedPosition = holder.getAdapterPosition();
                    openNoteActivity(clickedPosition);
                }
            });
        }

        @Override
        public int getItemCount() {
            return memoList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private TextView textViewTitle;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                textViewTitle = itemView.findViewById(R.id.textViewMemoTitle);
            }

            public int getColorForPosition(View itemView, int position) {
                int[] colors = {R.color.colorNote1, R.color.colorNote2, R.color.colorNote3};
                int index = position % colors.length;
                return ContextCompat.getColor(itemView.getContext(), colors[index]);
            }
        }
    }
}


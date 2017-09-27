package com.softians.poller.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.softians.poller.R;
import com.softians.poller.model.QuestionDataModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.softians.poller.R.id.hiden;

/**
 * Created by HP on 11-09-2017.
 */

public class QuestionListAdapter extends RecyclerView.Adapter<QuestionListAdapter.ViewHolder> {
    private Context pContext;
    private List<QuestionDataModel> questionListPutter ;
    public static Map<String,String> questionAnswerPair;



    public QuestionListAdapter(Context pContext, List<QuestionDataModel> questionListPutter) {
        this.pContext = pContext;
        this.questionListPutter = questionListPutter;
        questionAnswerPair = new HashMap<String,String>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_list_cardview,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.questionTextView.setText(questionListPutter.get(position).getQuestion());
        holder.answerEditText.setTag(position);
        holder.hidenQuestionId.setText(String.valueOf(questionListPutter.get(position).getquestionId()));
        holder.answerEditText.setText(questionListPutter.get(position).getAnswer());
    }

    @Override
    public int getItemCount() {
        return questionListPutter.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView questionTextView;
        public TextView hidenQuestionId;
        public EditText answerEditText;
        public ViewHolder(View itemView) {
            super(itemView);
            questionTextView =(TextView) itemView.findViewById(R.id.questionTV);
            answerEditText = (EditText)itemView.findViewById(R.id.answerET);
            hidenQuestionId = (TextView) itemView.findViewById(hiden);
            MyTextWatcher myTextWatcher = new MyTextWatcher(answerEditText);
            answerEditText.addTextChangedListener(myTextWatcher);
        }
    }
    private class MyTextWatcher implements TextWatcher {
        private EditText answerEditText;
        public MyTextWatcher(EditText answerEditText)
        {
            this.answerEditText = answerEditText;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            int position = (int)answerEditText.getTag();
            String answer = s.toString();
            questionListPutter.get(position).setAnswer(answer);
            questionAnswerPair.put(String.valueOf(questionListPutter.get(position).getquestionId()),String.valueOf(questionListPutter.get(position).getAnswer()));
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

}


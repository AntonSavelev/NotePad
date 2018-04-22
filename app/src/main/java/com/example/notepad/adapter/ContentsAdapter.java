package com.example.notepad.adapter;

import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.notepad.model.ImageRecord;
import com.example.notepad.R;
import com.example.notepad.model.Record;
import com.example.notepad.model.TextRecord;

import java.util.Collections;
import java.util.List;


public class ContentsAdapter extends RecyclerView.Adapter {

    public static final int TYPE_IMAGE = 0;
    public static final int TYPE_EDIT_TEXT = 1;

    Listener listener;
    private List<Record> contents;

    public ContentsAdapter() {
        this.contents = Collections.EMPTY_LIST;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_IMAGE) {
            CardView cv = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.card_type_image, parent, false);
            return new ContentsAdapter.ImageViewHolder(cv);
        } else if (viewType == TYPE_EDIT_TEXT) {
            View cv = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_type_edit_text, parent, false);
            return new ContentsAdapter.EditTextViewHolder(cv, new MyCustomEditTextListener());
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ImageViewHolder) {
            ((ImageViewHolder) holder).bind(holder, position);
        } else if (holder instanceof EditTextViewHolder) {
            ((EditTextViewHolder) holder).myCustomEditTextListener.updatePosition(holder.getAdapterPosition());
            ((EditTextViewHolder) holder).bind(holder, position);
        }
    }

    @Override
    public int getItemCount() {
        return contents.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (contents.get(position) instanceof TextRecord) {
            return TYPE_EDIT_TEXT;
        } else {
            return TYPE_IMAGE;
        }
    }

    public void setData(List<Record> contents) {
        this.contents = contents;

        if (getItemCount() == 0) {
            contents.add(new TextRecord(""));
        }

    }

    public interface Listener {
        void onClick(int position);
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {

        public ImageView image;
        public CardView cardView;

        public ImageViewHolder(CardView itemView) {
            super(itemView);
            cardView = itemView;
            image = cardView.findViewById(R.id.image);

        }

        public void bind(RecyclerView.ViewHolder holder, final int position) {
            ImageRecord imageRecord = (ImageRecord) contents.get(position);
            Uri uri = Uri.parse(imageRecord.getPhotoUrl());
            ((ImageViewHolder) holder).image.setImageURI(uri);
            CardView cardView = (CardView) holder.itemView;
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onClick(position);
                }
            });
        }
    }

    public class EditTextViewHolder extends RecyclerView.ViewHolder {

        public EditText eText;
        public MyCustomEditTextListener myCustomEditTextListener;

        public EditTextViewHolder(View itemView, MyCustomEditTextListener myCustomEditTextListener) {
            super(itemView);
            this.myCustomEditTextListener = myCustomEditTextListener;
            eText = itemView.findViewById(R.id.eText);
            eText.requestFocus();
            eText.addTextChangedListener(myCustomEditTextListener);
        }

        public void bind(RecyclerView.ViewHolder holder, final int position) {
            TextRecord textRecord = (TextRecord) contents.get(position);
            String content = textRecord.getTextRec();
            ((EditTextViewHolder) holder).eText.setText(content);
            ((EditTextViewHolder) holder).eText.requestFocus();
        }
    }

    private class MyCustomEditTextListener implements TextWatcher {

        private int position;

        public void updatePosition(int position) {
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            TextRecord textRecord = new TextRecord(charSequence.toString());
            contents.set(position, textRecord);
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    }

}

package com.example.notepad.adapter;

import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.notepad.R;

import java.util.Collections;
import java.util.List;


public class ContentsAdapter extends RecyclerView.Adapter {

    public static final int TYPE_IMAGE = 0;
    public static final int TYPE_EDIT_TEXT = 1;
    
    Listener listener;
    private List<String> contents;

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
            return new ContentsAdapter.EditTextViewHolder(cv);
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ImageViewHolder) {
            Uri uri = Uri.parse(contents.get(position));
            ((ImageViewHolder) holder).image.setImageURI(uri);
            CardView cardView = (CardView) holder.itemView;
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onClick(position);
                }
            });
        } else if (holder instanceof EditTextViewHolder) {
            String content = contents.get(position);
            ((EditTextViewHolder) holder).eText.setText(content);
            ((EditTextViewHolder) holder).eText.requestFocus();
        }
    }

    @Override
    public int getItemCount() {
        return contents.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (contents.get(position).contains(".jpg")) {
            return TYPE_IMAGE;
        } else {
            return TYPE_EDIT_TEXT;
        }
    }

    public void setData(List<String> contents) {
        this.contents = contents;
        notifyDataSetChanged();
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
    }

    public class EditTextViewHolder extends RecyclerView.ViewHolder {

        public EditText eText;

        public EditTextViewHolder(View itemView) {
            super(itemView);
            eText = itemView.findViewById(R.id.eText);
        }
    }
}

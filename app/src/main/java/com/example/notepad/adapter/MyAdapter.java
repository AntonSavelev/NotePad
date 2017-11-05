package com.example.notepad.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.notepad.model.Note;
import com.example.notepad.R;

import java.util.Collections;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    Listener listener;
    private List<Note> notes;

    public MyAdapter() {
        this.notes = Collections.EMPTY_LIST;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cv = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new MyAdapter.ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(MyAdapter.ViewHolder holder, final int position) {
        final Note note = notes.get(position);
        holder.bind(note);
        final int noteId = note.getId();
        CardView cardView = holder.cardView;
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(noteId);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public void setData(List<Note> notes) {
        this.notes = notes;
        notifyDataSetChanged();
    }

    public interface Listener {
        void onClick(int noteId);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView noteName;
        TextView noteContent;
        TextView noteDate;
        private CardView cardView;

        public ViewHolder(CardView v) {
            super(v);
            cardView = v;
            noteName = cardView.findViewById(R.id.noteTitle);
            noteContent = cardView.findViewById(R.id.noteContent);
            noteDate = cardView.findViewById(R.id.noteDate);
        }

        public void bind(Note note) {
            noteName.setText(note.getTitle());
            noteContent.setText(note.getContent());
            noteDate.setText(note.getDate());
        }

    }
}

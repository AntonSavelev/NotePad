package com.example.notepad.noteList;

import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.notepad.model.ImageRecord;
import com.example.notepad.model.Note;
import com.example.notepad.R;
import com.example.notepad.model.Record;
import com.example.notepad.model.TextRecord;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NoteListAdapter extends RecyclerView.Adapter<NoteListAdapter.ViewHolder> {

    Listener listener;
    private List<Note> notes;

    public NoteListAdapter() {
        this.notes = Collections.EMPTY_LIST;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    public NoteListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cv = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new NoteListAdapter.ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(NoteListAdapter.ViewHolder holder, final int position) {
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
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
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
        private TextView noteName;
        private TextView noteContent;
        private TextView noteDate;
        private ImageView imageView;
        private CardView cardView;

        public ViewHolder(CardView v) {
            super(v);
            cardView = v;
            noteName = cardView.findViewById(R.id.noteTitle);
            noteContent = cardView.findViewById(R.id.noteContent);
            noteDate = cardView.findViewById(R.id.noteDate);
            imageView = cardView.findViewById(R.id.image);
        }

        public void bind(Note note) {
            List<Record> records = new ArrayList<>();
            records.clear();
            records.addAll(note.getContents());
            Uri uri;
            int size = records.size();
            if (size > 0 && records.get(0) instanceof TextRecord) {
                TextRecord rec = (TextRecord) records.get(0);
                noteContent.setText(rec.getTextRec());
            } else if ((size > 0 && records.get(0) instanceof ImageRecord)) {
                ImageRecord rec = (ImageRecord) records.get(0);
                imageView.setVisibility(View.VISIBLE);
                uri = Uri.parse(rec.getPhotoUrl());
                imageView.setImageURI(uri);
            }
            if (size > 1 && records.get(1) instanceof ImageRecord) {
                ImageRecord rec = (ImageRecord) records.get(1);
                imageView.setVisibility(View.VISIBLE);
                uri = Uri.parse(rec.getPhotoUrl());
                imageView.setImageURI(uri);
            } else {
                imageView.setVisibility(View.GONE);
            }
            noteName.setText(note.getTitle());
            noteDate.setText(note.getTime());
        }
    }
}

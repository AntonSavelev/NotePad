package com.example.notepad.controller;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.support.v7.widget.ShareActionProvider;
import android.widget.Toast;

import com.example.notepad.model.AppDatabase;
import com.example.notepad.model.NoteDao;
import com.example.notepad.adapter.ContentsAdapter;
import com.example.notepad.managers.App;
import com.example.notepad.model.Note;
import com.example.notepad.R;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.widget.Toast.*;

public class NoteActivity extends AppCompatActivity {

    public static final String KEY_NOTE_ID = "key_id";
    public static final String KEY_SAVE_CONTENTS = "key_save_contents";
    public static final String BLANC_ENTRY = "";
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private NoteDao noteDao;
    private List<String> contents;
    private String content;
    private ContentsAdapter contentsAdapter;
    private RecyclerView rv;
    private EditText etTitle;
    private Uri sourceUri;
    private Uri destinationUri;
    private ShareActionProvider myShareActionProvider;
    private GridLayoutManager layoutManager;
    private AppDatabase db;
    private Note note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        initialization();
        if (savedInstanceState != null) {
            contents = savedInstanceState.getStringArrayList(KEY_SAVE_CONTENTS);
        }
        if (isIntentHasExtra()) {
            getNote();
        }
        contentsAdapter.setData(contents);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList(KEY_SAVE_CONTENTS, (ArrayList<String>) contents);
    }

    public void initialization() {
        etTitle = findViewById(R.id.et_title);
        db = App.getInstance().getDatabase();
        noteDao = db.noteDao();
        rv = findViewById(R.id.note_view);
        layoutManager = new GridLayoutManager(NoteActivity.this, 1);
        rv.setLayoutManager(layoutManager);
        rv.setItemAnimator(new DefaultItemAnimator());
        contentsAdapter = new ContentsAdapter();
        contentsAdapter.setListener(new ContentsAdapter.Listener() {
            @Override
            public void onClick(int position) {
                openImage(position);
            }
        });
        rv.setAdapter(contentsAdapter);
        contents = new ArrayList<>();
    }

    public void getNote() {
        final int id = getNoteId();
        new Thread(new Runnable() {
            @Override
            public void run() {
                note = noteDao.getById(id);
                contents.clear();
                contents.addAll(note.getContents());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        etTitle.setText(note.getTitle());
                    }
                });
            }
        }).start();
    }

    public boolean isIntentHasExtra() {
        Intent intent = getIntent();
        if (intent.hasExtra(KEY_NOTE_ID)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isNoteEdit() {
        String savedTitle = note.getTitle();
        List<String> savedListContents = note.getContents();
        String title = etTitle.getText().toString();
        if (savedTitle.equals(title) && savedListContents.equals(contents)) {
            return false;
        }
        return true;
    }

    public boolean isFieldsEmpty() {
        String title = etTitle.getText().toString();
        int contentsSize = contents.size();
        if (!contents.get(0).contains("jpg")) {
            String content = getContent(0);
            if (title.equals("") && contentsSize == 1 && content.equals("")) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_note, menu);
        MenuItem deleteItem = menu.findItem(R.id.delete);
        MenuItem clearItem = menu.findItem(R.id.clear);
//        MenuItem shareItem = menu.findItem(R.id.action_share);
        if (!isIntentHasExtra()) {
            deleteItem.setVisible(false);
//            shareItem.setVisible(false);
        } else {
            clearItem.setVisible(false);
        }
//        myShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);
        return true;
    }

    private void setIntent(String shareContent) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, shareContent);
        myShareActionProvider.setShareIntent(intent);
    }

    public int getNoteId() {
        Intent intent = getIntent();
        int id = intent.getIntExtra(KEY_NOTE_ID, 0);
        return id;
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        sourceUri = Uri.fromFile(image);
        destinationUri = sourceUri;
        content = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Toast.makeText(this, "Error occurred while creating the File", LENGTH_SHORT).show();
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK) {
                UCrop.of(sourceUri, destinationUri)
                        .withAspectRatio(16, 9)
                        .withMaxResultSize(1920, 1080)
                        .start(this, UCrop.REQUEST_CROP);
            }
        }
        if (requestCode == UCrop.REQUEST_CROP) {
            if (resultCode == RESULT_OK) {
                int size = contents.size();
                if (size > 0 && contents.get(size - 1).equals("")) {
                    contents.remove(contents.size() - 1);
                }
                contents.add(content);
                contentsAdapter.notifyDataSetChanged();
            }
        }
    }

    public String getContent(int contentIndex) {
        EditText editText = rv.getChildAt(contentIndex).findViewById(R.id.eText);
        String lastContent = editText.getText().toString();
        return lastContent;
    }

    public void insertNote(final Note note) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                noteDao.insert(note);
            }
        }).start();
        makeText(this, "Заметка успешно сохранена", LENGTH_SHORT).show();
    }

    public void updateNote(final Note note) {
        note.setId(getNoteId());
        new Thread(new Runnable() {
            @Override
            public void run() {
                noteDao.update(note);
            }
        }).start();
        makeText(this, "Заметка отредактирована", LENGTH_SHORT).show();
    }

    public void saveNote() {
        if (isFieldsEmpty()) {
            return;
        }
        String title = etTitle.getText().toString();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyy HH:mm:ss");
        String currentDateAndTime = sdf.format(new Date());
        final Note note = new Note(title, contents, currentDateAndTime);
        if (isIntentHasExtra()) {
            if (isNoteEdit()) {
                updateNote(note);
            }
        } else {
            insertNote(note);
        }
        finish();
    }

    public void checkPermissionAndGetPhoto() {
        int permissionStatus = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
            dispatchTakePictureIntent();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                    0);
        }
    }

    public void clearNote() {
        etTitle.setText(BLANC_ENTRY);
        contents.clear();
        contents.add(BLANC_ENTRY);
        contentsAdapter.notifyDataSetChanged();
    }

    public void deleteNote() {
        final int id = getNoteId();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Note note = noteDao.getById(id);
                noteDao.delete(note);
            }
        }).start();
        makeText(this, "Заметка успешно удалена", LENGTH_SHORT).show();
        finish();
    }

    public void addRec() {
        if (contents.get(contents.size() - 1).contains("jpg")) {
            contents.add(BLANC_ENTRY);
            contentsAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.camera:
                checkPermissionAndGetPhoto();
                break;
            case R.id.save:
                saveNote();
                break;
            case R.id.clear:
                clearNote();
                break;
            case R.id.delete:
                deleteNote();
                break;
            case R.id.rec:
                addRec();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void openImage(int position) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            String localUri = contents.get(position);
            File file = new File(localUri);
            Uri contentUri = FileProvider.getUriForFile(this, "com.example.android.fileprovider", file);
            Intent openFileIntent = new Intent(Intent.ACTION_VIEW);
            openFileIntent.setDataAndTypeAndNormalize(contentUri, "image/*");
            openFileIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(openFileIntent);
        } else {
            String content = contents.get(position);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse("file://" + content), "image/*");
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        saveNote();
    }
}

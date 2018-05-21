package com.example.notepad.note;

import android.Manifest;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.notepad.model.ImageRecord;
import com.example.notepad.model.Record;
import com.example.notepad.model.RecordViewModel;
import com.example.notepad.model.TextRecord;
import com.example.notepad.model.AppDatabase;
import com.example.notepad.model.NoteDao;
import com.example.notepad.managers.App;
import com.example.notepad.model.Note;
import com.example.notepad.R;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static android.widget.Toast.*;

public class NoteActivity extends AppCompatActivity {

    public static final String KEY_NOTE_ID = "key_id";
    public static final String BLANC_ENTRY = "";
    public static final int REQUEST_IMAGE_CAPTURE = 1;
    private int noteId = 0;
    private int id = 0;
    private LiveData<Note> noteLiveData;
    private NoteDao noteDao;
    private List<Record> contents;
    private String content;
    private ContentsAdapter contentsAdapter;
    private RecyclerView rv;
    private EditText etTitle;
    private Uri sourceUri;
    private Uri destinationUri;
    private RecyclerView.LayoutManager layoutManager;
    private AppDatabase db;
    private Note note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        initialization();
        if (isIntentHasExtra()) {
            id = getNoteId();
            getNote();
        } else {
            newNote();
            id = noteId;
            getNote();
        }
        contentsAdapter.setListener(new ContentsAdapter.Listener() {
            @Override
            public void onClick(int position) {
                openImage(position);
            }

            @Override
            public void onChange() {
                if (id != 0 && note != null) {
                    setNoteChangedRecords();
                    updateNote(note);
                }
            }
        });
        rv.setAdapter(contentsAdapter);
        contentsAdapter.setData(contents);
    }

    public void initialization() {
        etTitle = findViewById(R.id.et_title);
        contents = new ArrayList<>();
        db = App.getInstance().getDatabase();
        noteDao = db.noteDao();
        rv = findViewById(R.id.note_view);
        layoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(layoutManager);
        rv.setItemAnimator(new DefaultItemAnimator());
        contentsAdapter = new ContentsAdapter();
    }

    public void getNote() {
        RecordViewModel model = ViewModelProviders.of(NoteActivity.this).get(RecordViewModel.class);
        noteLiveData = model.getData(id);
        noteLiveData.observe(this, new Observer<Note>() {
            @Override
            public void onChanged(@Nullable Note currentNote) {
                note = currentNote;
                contents.clear();
                contents.addAll(note.getContents());
                etTitle.setText(note.getTitle());
            }
        });
    }

    public boolean isIntentHasExtra() {
        Intent intent = getIntent();
        if (intent.hasExtra(KEY_NOTE_ID)) {
            return true;
        } else {
            return false;
        }
    }

    public void setNoteChangedRecords() {
        note.setId(id);
        note.setContents(contents);
        note.setTitle(etTitle.getText().toString());
        note.setTime(getDate());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_note, menu);
        MenuItem deleteItem = menu.findItem(R.id.delete);
        MenuItem clearItem = menu.findItem(R.id.clear);
        if (!isIntentHasExtra()) {
            deleteItem.setVisible(false);
        } else {
            clearItem.setVisible(false);
        }
        return true;
    }

    public int getNoteId() {
        Intent intent = getIntent();
        return intent.getIntExtra(KEY_NOTE_ID, 0);
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
                if (size > 1 && contents.get(size - 1) instanceof TextRecord) {
                    TextRecord textRecord = (TextRecord) contents.get(size - 1);
                    if (textRecord.getTextRec().equals("")) {
                        contents.remove(contents.size() - 1);
                    }
                }
                ImageRecord imageRecord = new ImageRecord();
                imageRecord.setPhotoUrl(content);
                contents.add(imageRecord);
                updateNote(note);
                contentsAdapter.notifyDataSetChanged();
            }
        }
    }

    public void insertNote(final Note note) {
        ExecutorService es = Executors.newSingleThreadExecutor();
        Future<Integer> result = es.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return (int) noteDao.insert(note);
            }
        });
        try {
            noteId = result.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void updateNote(final Note note) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                noteDao.update(note);
            }
        }).start();
    }

    public void newNote() {
        note = new Note("", Collections.EMPTY_LIST, getDate());
        insertNote(note);
    }

    public String getDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyy HH:mm:ss");
        String currentDateAndTime = sdf.format(new Date());
        return currentDateAndTime;
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            dispatchTakePictureIntent();
        }
    }

    public void clearNote() {
        etTitle.setText(BLANC_ENTRY);
        contents.clear();
        TextRecord textRecord = new TextRecord("");
        contents.add(textRecord);
        contentsAdapter.notifyDataSetChanged();
    }

    public void deleteNote() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Note note = noteDao.getNoteById(id);
                noteDao.delete(note);
            }
        }).start();
        noteLiveData.removeObservers(this);
        finish();
    }

    public void addRec() {
        if (contents.get(contents.size() - 1).getClass() == ImageRecord.class) {
            TextRecord textRecord = new TextRecord("");
            contents.add(textRecord);
            contentsAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.camera:
                checkPermissionAndGetPhoto();
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
            ImageRecord imageRecord = (ImageRecord) contents.get(position);
            String localUri = imageRecord.getPhotoUrl();
            File file = new File(localUri);
            Uri contentUri = FileProvider.getUriForFile(this, "com.example.android.fileprovider", file);
            Intent openFileIntent = new Intent(Intent.ACTION_VIEW);
            openFileIntent.setDataAndTypeAndNormalize(contentUri, "image/*");
            openFileIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(openFileIntent);
        } else {
            ImageRecord imageRecord = (ImageRecord) contents.get(position);
            String content = imageRecord.getPhotoUrl();
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse("file://" + content), "image/*");
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(intent);
        }
    }

    public Note createCleanNote() {
        List<Record> records = new ArrayList<>();
        records.add(new TextRecord(""));
        Note cleanNote = new Note("", records, getDate());
        cleanNote.setId(id);
        return cleanNote;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setNoteChangedRecords();
        if (note.equals(createCleanNote())) {
            deleteNote();
        }
    }
}

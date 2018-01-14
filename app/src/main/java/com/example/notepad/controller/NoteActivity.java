package com.example.notepad.controller;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.support.v7.widget.ShareActionProvider;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.notepad.managers.App;
import com.example.notepad.utils.Loader;
import com.example.notepad.R;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.widget.Toast.*;

public class NoteActivity extends AppCompatActivity {

    public static final String KEY_NOTE_ID = "key_id";
    private final int REQUEST_IMAGE_CAPTURE = 0;
    private EditText etTitle;
    private EditText etContent;
    private ImageView mImageView_1;
    private ImageView mImageView_2;
    private ImageView mImageView_3;
    private ImageView mImageView_4;
    private ImageView mImageView_5;
    private Loader loader;
    private String mCurrentPhotoPath_1;
    private String mCurrentPhotoPath_2;
    private String mCurrentPhotoPath_3;
    private String mCurrentPhotoPath_4;
    private String mCurrentPhotoPath_5;
    private MenuItem cameraItem;
    private Uri sourceUri;
    private Uri destinationUri;
    private ShareActionProvider myShareActionProvider;
    private boolean isImageScaled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        initialView();
        loader = App.getLoader();
        if (isIntentHasExtra()) {
            int id = getNoteId();
            loader.readData(id, etTitle, etContent, mImageView_1, mImageView_2, mImageView_3, mImageView_4, mImageView_5);
        }
    }

    public void initialView() {
        etTitle = findViewById(R.id.et_title);
        etContent = findViewById(R.id.et_content);
        mImageView_1 = findViewById(R.id.mImageView1);
        mImageView_2 = findViewById(R.id.mImageView2);
        mImageView_3 = findViewById(R.id.mImageView3);
        mImageView_4 = findViewById(R.id.mImageView4);
        mImageView_5 = findViewById(R.id.mImageView5);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_note, menu);
        MenuItem deleteItem = menu.findItem(R.id.delete);
        MenuItem shareItem = menu.findItem(R.id.action_share);
        cameraItem = menu.findItem(R.id.camera);
        if (!isIntentHasExtra()) {
            deleteItem.setVisible(false);
            shareItem.setVisible(false);
        }
        if (mImageView_5.getVisibility() == ImageView.VISIBLE) {
            cameraItem.setVisible(false);
        }
        myShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);
        setIntent(loader.getContentText(etContent));
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

    public boolean isIntentHasExtra() {
        Intent intent = getIntent();
        if (intent.hasExtra(KEY_NOTE_ID)) {
            return true;
        } else return false;
    }

    public void saveNote() {
        if (loader.isFieldsEmpty(etTitle, etContent)) {
            return;
        }
        int id = getNoteId();
        if (isIntentHasExtra()) {
            if (loader.isNoteEdit(id, etTitle, etContent, mCurrentPhotoPath_1)) {
                return;
            } else {
                loader.saveEditNote(id, etTitle, etContent, mCurrentPhotoPath_1, mCurrentPhotoPath_2, mCurrentPhotoPath_3, mCurrentPhotoPath_4, mCurrentPhotoPath_5);
                Log.d("Log_PHOTO_SAVE_EDIT", mCurrentPhotoPath_1 + "/" + mCurrentPhotoPath_2 + "/" + mCurrentPhotoPath_3 + "/" + mCurrentPhotoPath_4 + "/" + mCurrentPhotoPath_5);
                makeText(this, "Заметка отредактирована", LENGTH_SHORT).show();
            }
        } else {
            loader.saveNewNote(etTitle, etContent, mCurrentPhotoPath_1, mCurrentPhotoPath_2, mCurrentPhotoPath_3, mCurrentPhotoPath_4, mCurrentPhotoPath_5);
            makeText(this, "Заметка успешно сохранена", LENGTH_SHORT).show();
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
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
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            if (!isIntentHasExtra()) {
                switch (requestCode) {
                    case REQUEST_IMAGE_CAPTURE:
                        if (mCurrentPhotoPath_1 != null) {
                            mImageView_1.setImageURI(Uri.parse(mCurrentPhotoPath_1));
                            mImageView_1.setVisibility(ImageView.VISIBLE);
                        }
                        break;
                    case UCrop.REQUEST_CROP:
                        final Uri resultUri = UCrop.getOutput(data);
                        mImageView_1.setImageURI(resultUri);
                        mImageView_1.invalidate();
                        break;
                    case UCrop.RESULT_ERROR:
                        final Throwable cropError = UCrop.getError(data);
                        Toast.makeText(this, cropError.toString(), LENGTH_LONG).show();
                        break;
                }

                if (mCurrentPhotoPath_2 != null) {
                    mImageView_2.setImageURI(Uri.parse(mCurrentPhotoPath_2));
                    mImageView_2.setVisibility(ImageView.VISIBLE);
                }
                if (mCurrentPhotoPath_3 != null) {
                    mImageView_3.setImageURI(Uri.parse(mCurrentPhotoPath_3));
                    mImageView_3.setVisibility(ImageView.VISIBLE);
                }
                if (mCurrentPhotoPath_4 != null) {
                    mImageView_4.setImageURI(Uri.parse(mCurrentPhotoPath_4));
                    mImageView_4.setVisibility(ImageView.VISIBLE);
                }
                if (mCurrentPhotoPath_5 != null) {
                    mImageView_5.setImageURI(Uri.parse(mCurrentPhotoPath_5));
                    mImageView_5.setVisibility(ImageView.VISIBLE);
                    cameraItem.setVisible(false);
                }
            } else {

                if (mCurrentPhotoPath_1 != null && mCurrentPhotoPath_2 == null) {
                    if (mImageView_1.getVisibility() != ImageView.VISIBLE) {
                        mImageView_1.setImageURI(Uri.parse(mCurrentPhotoPath_1));
                        mImageView_1.setVisibility(ImageView.VISIBLE);
                    } else if (mImageView_2.getVisibility() != ImageView.VISIBLE) {
                        mImageView_2.setImageURI(Uri.parse(mCurrentPhotoPath_1));
                        mImageView_2.setVisibility(ImageView.VISIBLE);
                    } else if (mImageView_3.getVisibility() != ImageView.VISIBLE) {
                        mImageView_3.setImageURI(Uri.parse(mCurrentPhotoPath_1));
                        mImageView_3.setVisibility(ImageView.VISIBLE);
                    } else if (mImageView_4.getVisibility() != ImageView.VISIBLE) {
                        mImageView_4.setImageURI(Uri.parse(mCurrentPhotoPath_1));
                        mImageView_4.setVisibility(ImageView.VISIBLE);
                    } else if (mImageView_5.getVisibility() != ImageView.VISIBLE) {
                        mImageView_5.setImageURI(Uri.parse(mCurrentPhotoPath_1));
                        mImageView_5.setVisibility(ImageView.VISIBLE);
                        cameraItem.setVisible(false);
                    }
                }

                if (mCurrentPhotoPath_2 != null && mCurrentPhotoPath_3 == null) {
                    if (mImageView_2.getVisibility() != ImageView.VISIBLE) {
                        mImageView_2.setImageURI(Uri.parse(mCurrentPhotoPath_2));
                        mImageView_2.setVisibility(ImageView.VISIBLE);
                    } else if (mImageView_3.getVisibility() != ImageView.VISIBLE) {
                        mImageView_3.setImageURI(Uri.parse(mCurrentPhotoPath_2));
                        mImageView_3.setVisibility(ImageView.VISIBLE);
                    } else if (mImageView_4.getVisibility() != ImageView.VISIBLE) {
                        mImageView_4.setImageURI(Uri.parse(mCurrentPhotoPath_2));
                        mImageView_4.setVisibility(ImageView.VISIBLE);
                    } else if (mImageView_5.getVisibility() != ImageView.VISIBLE) {
                        mImageView_5.setImageURI(Uri.parse(mCurrentPhotoPath_2));
                        mImageView_5.setVisibility(ImageView.VISIBLE);
                        cameraItem.setVisible(false);
                    }
                }

                if (mCurrentPhotoPath_3 != null && mCurrentPhotoPath_4 == null) {
                    if (mImageView_3.getVisibility() != ImageView.VISIBLE) {
                        mImageView_3.setImageURI(Uri.parse(mCurrentPhotoPath_3));
                        mImageView_3.setVisibility(ImageView.VISIBLE);
                    } else if (mImageView_4.getVisibility() != ImageView.VISIBLE) {
                        mImageView_4.setImageURI(Uri.parse(mCurrentPhotoPath_3));
                        mImageView_4.setVisibility(ImageView.VISIBLE);
                    } else if (mImageView_5.getVisibility() != ImageView.VISIBLE) {
                        mImageView_5.setImageURI(Uri.parse(mCurrentPhotoPath_3));
                        mImageView_5.setVisibility(ImageView.VISIBLE);
                        cameraItem.setVisible(false);
                    }
                }

                if (mCurrentPhotoPath_4 != null && mCurrentPhotoPath_5 == null) {
                    if (mImageView_4.getVisibility() != ImageView.VISIBLE) {
                        mImageView_4.setImageURI(Uri.parse(mCurrentPhotoPath_4));
                        mImageView_4.setVisibility(ImageView.VISIBLE);
                    } else if (mImageView_5.getVisibility() != ImageView.VISIBLE) {
                        mImageView_5.setImageURI(Uri.parse(mCurrentPhotoPath_4));
                        mImageView_5.setVisibility(ImageView.VISIBLE);
                        cameraItem.setVisible(false);
                    }
                }

                if (mCurrentPhotoPath_5 != null) {
                    if (mImageView_5.getVisibility() != ImageView.VISIBLE) {
                        mImageView_5.setImageURI(Uri.parse(mCurrentPhotoPath_5));
                        mImageView_5.setVisibility(ImageView.VISIBLE);
                        cameraItem.setVisible(false);
                    }
                }
            }
            invalidateOptionsMenu();
        }
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
        if (mCurrentPhotoPath_1 == null) {
            mCurrentPhotoPath_1 = image.getAbsolutePath();
        } else if (mCurrentPhotoPath_2 == null) {
            mCurrentPhotoPath_2 = image.getAbsolutePath();
        } else if (mCurrentPhotoPath_3 == null) {
            mCurrentPhotoPath_3 = image.getAbsolutePath();
        } else if (mCurrentPhotoPath_4 == null) {
            mCurrentPhotoPath_4 = image.getAbsolutePath();
        } else if (mCurrentPhotoPath_5 == null) {
            mCurrentPhotoPath_5 = image.getAbsolutePath();
        }
        return image;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.camera:
                int permissionStatus = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
                if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
                    dispatchTakePictureIntent();
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                            0);
                }
                break;
            case R.id.save:
//                finish();
                saveNote();
                break;
            case R.id.clear:
                loader.clearNote(etTitle, etContent);
                makeText(this, "Поля очищены", LENGTH_SHORT).show();
                break;
            case R.id.delete:
                int id = getNoteId();
                loader.deleteNote(id);
                loader.clearNote(etTitle, etContent);
                makeText(this, "Заметка успешно удалена", LENGTH_SHORT).show();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
//        saveNote();
    }

    public void openImage(View v) {
        if (!isImageScaled) {
            v.animate().translationX(150f).translationY(-120f).scaleX(1.75f).scaleY(1.75f).setDuration(500);
        }
        if (isImageScaled) {
            v.animate().translationX(0f).translationY(0f).scaleX(1f).scaleY(1f).setDuration(500);
        }
        isImageScaled = !isImageScaled;
    }

    public void uCropClick(View view) {
        UCrop.of(sourceUri, destinationUri)
                .withAspectRatio(16, 9)
                .withMaxResultSize(720, 480)
                .start(this);
    }
}

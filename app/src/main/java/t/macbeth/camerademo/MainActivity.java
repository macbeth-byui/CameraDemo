package t.macbeth.camerademo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;

/* This demo used the resources from the Android Developer website:
    https://developer.android.com/training/camera/photobasics

    Also uses Glide (see build.gradle) for populating the imageview with a picture file:
    https://github.com/bumptech/glide
 */

public class MainActivity extends AppCompatActivity {

    ImageView ivPicture;
    Uri pictureUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ivPicture = findViewById(R.id.iv_picture);

        Button bTakeNew = findViewById(R.id.b_takenew);

        bTakeNew.setOnClickListener((view) -> {takeNewPicture();});

        // Load the current picture (disable cache since we are using the same file)
        // The ability to save the file requires the Provider tag in AndroidManfiest.xml and
        // also the res/xml/file_paths.xml
        File pictureFile = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "picture.jpg");
        pictureUri = FileProvider.getUriForFile(this, "t.macbeth.camerademo", pictureFile);
        Glide.with(this).
                load(pictureUri).
                diskCacheStrategy(DiskCacheStrategy.NONE).
                skipMemoryCache(true).
                into(ivPicture);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Receive result from camera
        if (requestCode == 0) {
            // Load the picture (disable cache since we are using the same file)
            Glide.with(this).
                    load(pictureUri).
                    diskCacheStrategy(DiskCacheStrategy.NONE).
                    skipMemoryCache(true).
                    into(ivPicture);
        }
    }

    private void takeNewPicture() {
        // This intent will request the camera to open to take a picture
        // The picture will be saved in the picture file
        // onActivityResult will be called when the picture is taken
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            // Send the Intent request to open the camera
            intent.putExtra(MediaStore.EXTRA_OUTPUT, pictureUri);
            startActivityForResult(intent, 0);
        } catch (ActivityNotFoundException e) {
            Log.d("CameraDemo","Error starting camera: "+e);
        }
    }
}
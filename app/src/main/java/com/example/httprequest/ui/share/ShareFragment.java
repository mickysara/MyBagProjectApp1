package com.example.httprequest.ui.share;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.httprequest.BuildConfig;
import com.example.httprequest.ImageFilePath;
import com.example.httprequest.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cz.msebera.android.httpclient.Header;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.app.Activity.RESULT_OK;

public class ShareFragment extends Fragment {

    private ProgressDialog progressDialog;
    private ShareViewModel shareViewModel;
    Button Camera, Gallerry,Submit;
    EditText txtDate, txtTime;
    private ImageView imgPreview;
    private String imageFilePath;
    private EditText Money;
    private int mYear, mMonth, mDay, mHour, mMinute;
    final private int CAPTURE_IMAGE = 1;
    final private int CAPTURE_SAVE_IMAGE = 2;
    final private int PICK_IMAGE = 3;
    final private int CAPTURE_VDO = 4;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        shareViewModel =
                ViewModelProviders.of(this).get(ShareViewModel.class);
        View root = inflater.inflate(R.layout.fragment_share, container, false);

        Money = root.findViewById(R.id.Money);
        txtDate = (EditText)root.findViewById(R.id.in_date);
        txtTime = (EditText)root.findViewById(R.id.in_time);
        Camera = root.findViewById(R.id.cammerabutton);
        Gallerry = root.findViewById(R.id.Gally);
        Submit = root.findViewById(R.id.button2);
        imgPreview = root.findViewById(R.id.imageView);
        txtDate.setOnClickListener(view -> {
            Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) getContext(), new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, 225);
                return;
            }

            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        });
        txtDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {

                if(!hasFocus)
                {

                }else
                {
                    Calendar c = Calendar.getInstance();
                    mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    mDay = c.get(Calendar.DAY_OF_MONTH);


                    DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                            new DatePickerDialog.OnDateSetListener() {

                                @Override
                                public void onDateSet(DatePicker view, int year,
                                                      int monthOfYear, int dayOfMonth) {

                                    txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                                }
                            }, mYear, mMonth, mDay);
                    datePickerDialog.show();
                }


            }
        });
        txtTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {

                if(!hasFocus)
                {

                }else
                {
                    // Get Current Time
                    Calendar c = Calendar.getInstance();
                    mHour = c.get(Calendar.HOUR_OF_DAY);
                    mMinute = c.get(Calendar.MINUTE);

                    // Launch Time Picker Dialog
                    TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                            new TimePickerDialog.OnTimeSetListener() {

                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay,
                                                      int minute) {

                                    txtTime.setText(hourOfDay + ":" + minute);
                                }
                            }, mHour, mMinute, false);
                    timePickerDialog.show();
                }


            }
        });

        txtTime.setOnClickListener(view -> {
            // Get Current Time
            Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

                            txtTime.setText(hourOfDay + ":" + minute);
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        });

        Camera.setOnClickListener(view -> {
            Intent imageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            Uri photoURI = null;
            try {
                photoURI = FileProvider.getUriForFile(getContext(),
                        BuildConfig.APPLICATION_ID + ".provider",
                        createImageFile());
            } catch (IOException e) {
                e.printStackTrace();
            }
            imageIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(imageIntent, CAPTURE_SAVE_IMAGE);

            return ;
        });



        Gallerry.setOnClickListener(view -> {
            Intent pickIntent = new Intent(Intent.ACTION_GET_CONTENT);
            pickIntent.setType("image/*");
            startActivityForResult(pickIntent, PICK_IMAGE);

            return ;
        });

        Submit.setOnClickListener(view ->{
            if(Money.getText().toString().equals("") || txtDate.getText().toString().equals("") || txtTime.getText().toString().equals("")
                    || imageFilePath.equals(""))
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setCancelable(true);
                builder.setTitle("แจ้งเตือน");
                builder.setMessage("กรุณากรอกข้อมูลให้ครบถ้วน");
                builder.setPositiveButton("ตกลง",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d("Dialog","con");
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }else
            {
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setTitle("แจ้งเตือน");
            progressDialog.setMessage("กำลังอัปโหลดรูปภาพกรุณารอสักครู่...");
            progressDialog.show();
            SharedPreferences sp = getContext().getSharedPreferences("USER", Context.MODE_PRIVATE);
            String Iduser = sp.getString("Id_Users","");
            RequestParams params = new RequestParams();
            params.put("Money", Money.getText().toString());
            params.put("Date", txtDate.getText().toString());
            params.put("Time", txtTime.getText().toString());
            File photo = new File(imageFilePath);
            try {
                params.put("Image", photo);
            } catch(FileNotFoundException e) {}


            Log.d("Test",photo.toString());

            params.put("User", Iduser);

            AsyncHttpClient http = new AsyncHttpClient();
            http.post("https://www.harmonicmix.xyz/api/UploadMoney_api", params, new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response ) {
                    JSONObject obj = null;
                    try {
                        obj = new JSONObject(response.toString());


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String status = null;
                    try {
                        status = (String) obj.get("status");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if(status.equals("Success"))
                    {
                        Toast.makeText(getContext(), "สำเร็จ", Toast.LENGTH_LONG).show();
                        Log.d("hello",response.toString());

                    }else
                    {
                        Toast.makeText(getContext(), "ไม่สำเร็จ", Toast.LENGTH_LONG).show();
                        Log.d("hello",response.toString());
                    }
                    progressDialog.dismiss();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    Log.d("onFailure", Integer.toString(statusCode));
                }
            });
          }
        });

        return root;

    }
    private File createImageFile() throws IOException {
        // Create an image file name
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "");
        File image = File.createTempFile(new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()), ".png",storageDir);
        imageFilePath = image.getAbsolutePath();//imageFilePath = "file:" + image.getAbsolutePath();

        return image;
    }
    public void updateImage(File file) {
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(file));
        getContext().sendBroadcast(intent);
        getActivity().sendBroadcast(intent);

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE && resultCode == RESULT_OK) {
            if (data != null && data.getExtras() != null) {
                Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                imageBitmap = this.resizeImage(imageBitmap,1024,1024);//Resize image
                imgPreview.setImageBitmap(imageBitmap);//Preview image
            }
        }else if (requestCode == CAPTURE_SAVE_IMAGE && resultCode == RESULT_OK) {
            Uri imageUri = Uri.parse("file:" + imageFilePath);
            File file = new File(imageUri.getPath());
            try {
                InputStream ims = new FileInputStream(file);
                Bitmap imageBitmap = BitmapFactory.decodeStream(ims);
                imageBitmap = this.resizeImage(imageBitmap,1024,1024);//resize image
                imageBitmap = resolveRotateImage(imageBitmap,imageFilePath);//Resolve auto rotate image
                Log.d("mycat1", imageFilePath);
                imgPreview.setImageBitmap(imageBitmap);//Preview image

                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                File f = new File(imageFilePath);
                Uri contentUri = Uri.fromFile(f);
                mediaScanIntent.setData(contentUri);
                getContext().sendBroadcast(mediaScanIntent);

                //upload image
//                String sourceUri = imageUri.getPath();
//                String uploadUri = getString(R.string.root_url) + getString(R.string.upload_url);
//                new UploadFile(sourceUri,uploadUri).execute("");

            } catch (FileNotFoundException e) {
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            Uri contentURI = data.getData();
            Bitmap bitmap = null;
            try {
                imageFilePath = ImageFilePath.getPath(getContext(), data.getData());
                bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), contentURI);
                imgPreview.setImageBitmap(bitmap);//Preview image
                imgPreview.setImageURI(contentURI);

                Log.d("helloo",imageFilePath);


            } catch (IOException e) {
                e.printStackTrace();
            }

        }else if (requestCode == CAPTURE_VDO && resultCode == RESULT_OK) {

        }else if(resultCode == Activity.RESULT_CANCELED) {
            // User Cancelled the action
        }
    }
    //Resize image
    private Bitmap resizeImage(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    //Resolve auto rotate image problem
    private Bitmap resolveRotateImage(Bitmap bitmap, String photoPath) throws IOException {
        ExifInterface ei = new ExifInterface(photoPath);
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED);

        Bitmap rotatedBitmap = null;
        switch(orientation) {

            case ExifInterface.ORIENTATION_ROTATE_90:
                rotatedBitmap = rotateImage(bitmap, 90);
                break;

            case ExifInterface.ORIENTATION_ROTATE_180:
                rotatedBitmap = rotateImage(bitmap, 180);
                break;

            case ExifInterface.ORIENTATION_ROTATE_270:
                rotatedBitmap = rotateImage(bitmap, 270);
                break;

            case ExifInterface.ORIENTATION_NORMAL:
            default:
                rotatedBitmap = bitmap;
        }
        return rotatedBitmap;
    }

    //Rotate image
    private static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    public void onSubmit(View v)
    {

    }

}
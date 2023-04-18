package com.example.cs480_project;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddingExpenses extends AppCompatActivity {

    private EditText editAmount, editDesc;
    private Spinner chooseType;
    private Button addReceipt, saveButton, cancelButton;
    private ImageButton chooseDate;
    private TextView dateTextView;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private static final int PICK_IMAGE_REQUEST = 1;
    private String currentPhotoPath = null;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_expenses);

        // get references to edit text views
        editAmount = (EditText) findViewById(R.id.editAmount);
        editDesc = (EditText) findViewById(R.id.editDesc);
        dateTextView = findViewById(R.id.dateDisplay);

        // references to spinners
        chooseType = (Spinner) findViewById(R.id.chooseType);

        // get references to buttons
        addReceipt = (Button) findViewById(R.id.addReceipt);
        saveButton = (Button) findViewById(R.id.save_button);
        cancelButton = (Button) findViewById(R.id.cancel_button);
        chooseDate = (ImageButton) findViewById(R.id.chooseDateButton);

        //initialize calendar and date format
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);

        // Create a DatePickerDialog with the current date as the default
        chooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(AddingExpenses.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        String dateString = dateFormat.format(calendar.getTime());
                        chooseDate.setContentDescription(dateString);

                        dateTextView.setText(dateFormat.format(calendar.getTime()));
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        // Set up the spinner adapter for expense types
        ArrayAdapter<CharSequence> adapterTypes = ArrayAdapter.createFromResource(
                this, R.array.expense_types, android.R.layout.simple_spinner_item);
        adapterTypes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        chooseType.setAdapter(adapterTypes);


        //set button to cancel activity
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });

        //set button to save expense
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // get the expense data from the user input
                int id = 1;
                int budgetid = 1;
                String expenseCategory = chooseType.toString();
                double expenseAmount = Double.parseDouble(editAmount.getText().toString());
                String expenseDate = dateTextView.getText().toString();
                String expenseDesc = editDesc.getText().toString();
                byte[] expenseReceipt = currentPhotoPath.getBytes();

//                // create a new Expense object with the user's data
//                Expense expense = new Expense(id, expenseAmount, expenseCategory, expenseDate, expenseDesc, expenseReceipt, budgetid );
//
//                // insert the new expense object into the database
//                ExpenseTrackerDatabaseHelper db = new ExpenseTrackerDatabaseHelper(getApplicationContext());
//                db.addExpense(expense);
//
//                // display a success message to the user
//                Toast.makeText(getApplicationContext(), "Expense saved!", Toast.LENGTH_SHORT).show();

                finish();

            }
        });

        //set button to allow user to upload their receipt
        addReceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // create an intent to select an image from the device's gallery
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                // create an intent to take a picture using the device's camera
                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                // create a file object to store the image captured by the camera
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                    takePicture.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                } catch (IOException ex) {
                    // error occurred while creating the file
                    Log.e(TAG, "Error occurred while creating the File", ex);
                }

                // create a chooser intent to allow the user to select from either the gallery or camera
                Intent chooserIntent = Intent.createChooser(pickPhoto, "Select Receipt");

                // add the camera option to the chooser intent
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{takePicture});

                // start the chooser Intent
                startActivityForResult(chooserIntent, PICK_IMAGE_REQUEST);


            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            // Get the URI of the selected image
            Uri selectedImageUri = data.getData();

        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }


}


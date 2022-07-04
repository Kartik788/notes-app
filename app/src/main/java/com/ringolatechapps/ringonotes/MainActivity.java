package com.ringolatechapps.ringonotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton createNewFile;
    ImageView delete;
    String mfile_Name;
    GridView mGridView;
    MediaPlayer mediaPlayer;
    ArrayList<dataModel> mArrayList;
    String null_string = null;
    ArrayList<Integer> selected_views = new ArrayList<Integer>();
    TextView no_notes_yet;
    ImageView no_notes_yet_img;
    AppUpdateManager mAppUpdateManager;
    static final int RC_UPDATE = 420;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mArrayList = new ArrayList<dataModel>();

        no_notes_yet = findViewById(R.id.no_notes);
        no_notes_yet_img = findViewById(R.id.imageView_nonotes);
        createNewFile = findViewById(R.id.add_file_btn_id);
        delete = findViewById(R.id.delete_id);
        mGridView = findViewById(R.id.grid_view_id);

        mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.delete_sound);


        createNewFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                create a new file or dialog
                vibrate();
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                ViewGroup viewGroup = findViewById(android.R.id.content);
                View dialogView = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_layout, viewGroup, false);
                builder.setView(dialogView);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

                TextView cancel = dialogView.findViewById(R.id.cancel);
                TextView ok = dialogView.findViewById(R.id.ok);
                EditText fileNameEd = dialogView.findViewById(R.id.file_name_ed);


                cance.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mfile_Name = fileNameEd.getText().toString().replace("\\s", "");
                        if (mfile_Name.length() == 0) {
                            fileNameEd.setError("Required");
                        } else {
                            Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                            intent.putExtra("file_name", mfile_Name);
                            intent.putExtra("file_data", null_string);
                            startActivity(intent);
                            alertDialog.dismiss();
                        }

                    }

                });


            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//              delete pair here

                ArrayList<String> filenames = new ArrayList<>();
                SharedPreferences sharedPreferences = getSharedPreferences("NOTES", MODE_PRIVATE);
                SharedPreferences.Editor myEdit = sharedPreferences.edit();

                for (int i : selected_views) {
                    filenames.add(mArrayList.get(i).mFileName);
                }

                for (String s : filenames) {
                    myEdit.remove(s);
                    myEdit.apply();
                }

                selected_views.clear();
                createAndPlaceGridViews();
                delete.setVisibility(View.GONE);


                mediaPlayer.start();

            }
        });

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (selected_views.isEmpty()) {
                    Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                    intent.putExtra("file_name", mArrayList.get(i).mFileName);
                    intent.putExtra("file_data", mArrayList.get(i).mData);
                    startActivity(intent);
                } else {
                    vibrate();
                    ConstraintLayout mconstraintLayout = view.findViewById(R.id.constraint_layout_id);
//                    CardView cardView = view.findViewById(R.id.card_id);

                    delete.setVisibility(View.VISIBLE);
                    if (isPresent(selected_views, i)) {
                        selected_views.remove(new Integer(i));
                        mconstraintLayout.setBackground(getResources().getDrawable(R.drawable.grid_back));
//                        cardView.setCardElevation(6f);
                        if (selected_views.isEmpty()) {
                            delete.setVisibility(View.GONE);
                        }

                    } else {
                        selected_views.add(i);
                        mconstraintLayout.setBackground(getResources().getDrawable(R.drawable.grid_back_selected));
//                        cardView.setCardElevation(50f);

                    }

                }

            }
        });

        mGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (selected_views.isEmpty()) {
                    ConstraintLayout mconstraintLayout = view.findViewById(R.id.constraint_layout_id);
//                    CardView cardView = view.findViewById(R.id.card_id);
                    mconstraintLayout.setBackground(getResources().getDrawable(R.drawable.grid_back_selected));
//                    cardView.setCardElevation(50f);
                    delete.setVisibility(View.VISIBLE);
                    selected_views.add(i);
                }

                return true;
            }
        });

        mAppUpdateManager = AppUpdateManagerFactory.create(MainActivity.this);
        mAppUpdateManager.getAppUpdateInfo().addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo result) {
                if (result.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE && result.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                    try {
                        mAppUpdateManager.startUpdateFlowForResult(result, AppUpdateType.IMMEDIATE, MainActivity.this, RC_UPDATE);
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == RC_UPDATE && requestCode != RESULT_OK) {
            Toast.makeText(this, "cancelled", Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStart() {
        super.onStart();
        selected_views.clear();
        delete.setVisibility(View.GONE);
        createAndPlaceGridViews();

    }

    void createAndPlaceGridViews() {
        mArrayList.clear();

        SharedPreferences sh = getSharedPreferences("NOTES", MODE_PRIVATE);
        Map map = sh.getAll();

        Set<String> set = map.keySet();
        if (set.isEmpty()) {
            mGridView.setVisibility(View.GONE);
            no_notes_yet.setVisibility(View.VISIBLE);
            no_notes_yet_img.setVisibility(View.VISIBLE);
        } else {
            no_notes_yet.setVisibility(View.GONE);
            no_notes_yet_img.setVisibility(View.GONE);
            mGridView.setVisibility(View.VISIBLE);
        }
        Gson gson = new Gson();

        for (String str : set) {


            String json = sh.getString(str, "");
            value_model obj = gson.fromJson(json, value_model.class);
            dataModel mdataModel = new dataModel(str, obj.mdata, obj.mDate);
            mArrayList.add(mdataModel);

        }


        GRID_ADAPTER mgrid_adapter = new GRID_ADAPTER(MainActivity.this, sort_time(mArrayList));
        mGridView.setAdapter(mgrid_adapter);

    }

    // bubble sort
    ArrayList<dataModel> sort_time(ArrayList<dataModel> mArrayList) {

        for (int i = 0; i < mArrayList.size(); i++) {
            for (int j = 1; j < (mArrayList.size() - i); j++) {
                if (mArrayList.get(j - 1).date.before(mArrayList.get(j).date)) {
                    //swap elements
                    dataModel mdataModel = mArrayList.get(j - 1);
                    mArrayList.set(j - 1, mArrayList.get(j));
                    mArrayList.set(j, mdataModel);
                }

            }
        }

        return mArrayList;
    }

    boolean isPresent(ArrayList<Integer> marrayList, int data) {
        for (int i : marrayList) {
            if (data == i) {
                return true;
            }
        }
        return false;

    }


    @Override
    public void onBackPressed() {
        if (selected_views.isEmpty()) {
            super.onBackPressed();
        } else {
            selected_views.clear();
            createAndPlaceGridViews();
            delete.setVisibility(View.GONE);
        }
    }

    public void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        VibrationEffect vibrationEffect1;

        // this is the only type of the vibration which requires system version Oreo (API 26)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            // this effect creates the vibration of default amplitude for 1000ms(1 sec)
            vibrationEffect1 = VibrationEffect.createOneShot(40, VibrationEffect.DEFAULT_AMPLITUDE);

            // it is safe to cancel other vibrations currently taking place
            vibrator.cancel();
            vibrator.vibrate(vibrationEffect1);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        mAppUpdateManager.getAppUpdateInfo().addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo result) {
                if (result.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS && result.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                    try {
                        mAppUpdateManager.startUpdateFlowForResult(result, AppUpdateType.IMMEDIATE, MainActivity.this, RC_UPDATE);
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }
}
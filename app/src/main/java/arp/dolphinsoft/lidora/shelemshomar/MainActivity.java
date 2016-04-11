package arp.dolphinsoft.lidora.shelemshomar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends Activity implements View.OnClickListener {

    Button btn_start_game;
    EditText edit_first_group_name, edit_second_group_name;
    CheckBox check_double_negative;
    Spinner spinner_game_type;
    TextView txt_validation, txv_game_duration;

    ArrayList<String> game_type_list;

    public static final String my_preferences = "my_prefs";
    public static final String pref_language = "lang_key";
    public static final String pref_first_group_name = "first_group_name_key";
    public static final String pref_second_group_name = "second_group_name_key";
    //public static final String pref_game_type = "game_type_key";
    public static final String pref_double_negative = "double_negative_key";

    String lang = "en";

    SharedPreferences shared_preferences;

    Thread t_d;

    int game_timer;
    public static String game_duration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
        }

        getMyLangPreferences();

        setContentView(arp.dolphinsoft.lidora.shelemshomar.R.layout.activity_main);

        initialize();

        getMyPreferences();

        btn_start_game.setOnClickListener(this);

        startGameTimer();
    }

    private void startGameTimer() {
        t_d = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                game_timer++;
                                int min = 0;
                                int sec = 0;
                                int hour = 0;

                                if (game_timer >= 60) {
                                    min = game_timer / 60;
                                    sec = game_timer % 60;
                                } else {
                                    min = 0;
                                    sec = game_timer;
                                }

                                if (min >= 60) {
                                    hour = min / 60;
                                    min = min % 60;
                                }

                                String str_hour = "0";
                                String str_min = "0";
                                String str_sec = "0";

                                try {
                                    str_hour = String.valueOf(hour);
                                    str_min = String.valueOf(min);
                                    str_sec = String.valueOf(sec);
                                } catch (Exception e) {

                                }

                                if (hour < 10) {
                                    str_hour = "0" + hour;
                                }
                                if (min < 10) {
                                    str_min = "0" + min;
                                }
                                if (sec < 10) {
                                    str_sec = "0" + sec;
                                }

                                game_duration = str_hour + ":" + str_min + ":" + str_sec;
                                txv_game_duration.setText(getResources().getString(R.string.game_duration) +
                                        " = " +game_duration);
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        t_d.start();
    }

    private void getMyLangPreferences() {
        shared_preferences = getSharedPreferences(my_preferences, MODE_PRIVATE);

        if (shared_preferences.contains(pref_language)) {
            changeLanguage(shared_preferences.getString(pref_language, "en"));
        }
    }

    private void getMyPreferences() {
        shared_preferences = getSharedPreferences(my_preferences, MODE_PRIVATE);

        if (shared_preferences.contains(pref_language)) {
            changeLanguage(shared_preferences.getString(pref_language, "fa"));
        }

        if (shared_preferences.contains(pref_first_group_name)) {
            edit_first_group_name.setText(shared_preferences.getString(pref_first_group_name, ""));
        }

        if (shared_preferences.contains(pref_second_group_name)) {
            edit_second_group_name.setText(shared_preferences.getString(pref_second_group_name, ""));
        }

        if (shared_preferences.contains(pref_double_negative)) {
            if (shared_preferences.getString(pref_first_group_name, "").contains("1")) {
                check_double_negative.setChecked(true);
            } else {
                check_double_negative.setChecked(false);
            }
        }
    }

    private void setMyPreferences() {
        String double_negative = "0";
//        String game_type = "165";
        String first_group_name = null;
        String second_group_name = null;

        first_group_name = edit_first_group_name.getText().toString();
        second_group_name = edit_second_group_name.getText().toString();

        if (check_double_negative.isChecked() == true) {
            double_negative = "1";
        }

//        game_type = game_type_list.get(spinner_game_type.getSelectedItemPosition());
        SharedPreferences.Editor editor = shared_preferences.edit();
        editor.putString(pref_language, lang);
        editor.putString(pref_first_group_name, first_group_name);
        editor.putString(pref_second_group_name, second_group_name);
        editor.putString(pref_double_negative, double_negative);
//        editor.putString(pref_game_type, game_type);

        editor.commit();
    }

    private void changeLanguage(String lang) {
        String languageToLoad  = lang; // your language
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
    }

    private void initialize() {
        btn_start_game          = (Button)      findViewById(arp.dolphinsoft.lidora.shelemshomar.R.id.btn_start_game);
        edit_first_group_name   = (EditText)    findViewById(arp.dolphinsoft.lidora.shelemshomar.R.id.edit_first_group_name);
        edit_second_group_name  = (EditText)    findViewById(arp.dolphinsoft.lidora.shelemshomar.R.id.edit_second_group_name);
        check_double_negative   = (CheckBox)    findViewById(arp.dolphinsoft.lidora.shelemshomar.R.id.check_double_negative);
        spinner_game_type       = (Spinner)     findViewById(arp.dolphinsoft.lidora.shelemshomar.R.id.spinner_game_type);
        txt_validation          = (TextView)    findViewById(arp.dolphinsoft.lidora.shelemshomar.R.id.txt_validation);
        txv_game_duration       = (TextView)    findViewById(arp.dolphinsoft.lidora.shelemshomar.R.id.txv_game_duration);

        game_type_list = new ArrayList<String>();

        game_type_list.add("165");
        game_type_list.add("185");
        game_type_list.add("200");
        game_type_list.add("225");
        game_type_list.add("230");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, game_type_list);

        spinner_game_type.setAdapter(adapter);
        spinner_game_type.setSelection(0);

        txt_validation.setVisibility(View.INVISIBLE);

        check_double_negative.setChecked(false);

        txv_game_duration.setText("Game duration = 00:00:00");
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == arp.dolphinsoft.lidora.shelemshomar.R.id.btn_start_game) {

            if(edit_first_group_name.getText().toString().equals("")) {
                txt_validation.setVisibility(View.VISIBLE);
                edit_first_group_name.requestFocus();
            } else if(edit_second_group_name.getText().toString().equals("")) {
                txt_validation.setVisibility(View.VISIBLE);
                edit_second_group_name.requestFocus();
            } else {
                int double_negative = 0;
                int game_type = 165;
                String first_group_name = null;
                String second_group_name = null;

                first_group_name = edit_first_group_name.getText().toString();
                second_group_name = edit_second_group_name.getText().toString();

                if (check_double_negative.isChecked() == true) {
                    double_negative = 1;
                }

                game_type = Integer.parseInt(game_type_list.get(
                        spinner_game_type.getSelectedItemPosition()));

                setMyPreferences();

                Intent intent = new Intent(this, GameChartActivity.class);

                intent.putExtra("double_negative", double_negative);
                intent.putExtra("game_type", game_type);
                intent.putExtra("first_group_name", first_group_name);
                intent.putExtra("second_group_name", second_group_name);

                startActivity(intent);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(arp.dolphinsoft.lidora.shelemshomar.R.menu.option_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case arp.dolphinsoft.lidora.shelemshomar.R.id.mnu_about:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

                //alertDialogBuilder.setIcon(R.mipmap.ic_launcher);
                alertDialogBuilder.setCancelable(true);
                alertDialogBuilder.setTitle(arp.dolphinsoft.lidora.shelemshomar.R.string.about_title);
                alertDialogBuilder.setPositiveButton(arp.dolphinsoft.lidora.shelemshomar.R.string.close, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        return;
                    }
                });
                LayoutInflater inflater = this.getLayoutInflater();

                View dialogView = inflater.inflate(arp.dolphinsoft.lidora.shelemshomar.R.layout.lay_about_me, null);

                TextView txv_version = (TextView) dialogView.findViewById(arp.dolphinsoft.lidora.shelemshomar.R.id.txv_version);
                String version_name = BuildConfig.VERSION_NAME;
                String message = getResources().getString(arp.dolphinsoft.lidora.shelemshomar.R.string.version_txt) + " " + version_name;

                txv_version.setText(message);
                alertDialogBuilder.setView(dialogView);

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                break;

//            case R.id.mnu_help:
//                Toast.makeText(this, R.string.comming_soon, Toast.LENGTH_SHORT).show();
//                break;
//
//            case R.id.mnu_theme:
//                Toast.makeText(this, R.string.comming_soon, Toast.LENGTH_SHORT).show();
//                break;

            case arp.dolphinsoft.lidora.shelemshomar.R.id.mnu_lang:
                AlertDialog.Builder alertDialogBuilder_lang = new AlertDialog.Builder(this);

                //alertDialogBuilder.setIcon(R.mipmap.ic_launcher);
                alertDialogBuilder_lang.setCancelable(true);
                alertDialogBuilder_lang.setTitle(arp.dolphinsoft.lidora.shelemshomar.R.string.about_title);
                alertDialogBuilder_lang.setPositiveButton(arp.dolphinsoft.lidora.shelemshomar.R.string.close, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(MainActivity.this, arp.dolphinsoft.lidora.shelemshomar.R.string.restart_message, Toast.LENGTH_SHORT).show();
                        return;
                    }
                });
                LayoutInflater inflater_lang = this.getLayoutInflater();

                View dialogView_lang = inflater_lang.inflate(arp.dolphinsoft.lidora.shelemshomar.R.layout.lay_alter_language, null);
                alertDialogBuilder_lang.setView(dialogView_lang);


                RadioButton radio_lang_eng = (RadioButton) dialogView_lang.findViewById(arp.dolphinsoft.lidora.shelemshomar.R.id.radio_lang_eng);
                RadioButton radio_lang_fa = (RadioButton) dialogView_lang.findViewById(arp.dolphinsoft.lidora.shelemshomar.R.id.radio_lang_fa);

                if (lang.contains("fa")) {
                    radio_lang_fa.setChecked(true);
                } else {
                    radio_lang_eng.setChecked(true);
                }

                radio_lang_eng.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        lang = "en";
                        setMyPreferences();
                    }
                });

                radio_lang_fa.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        lang = "fa";
                        setMyPreferences();
                    }
                });

                AlertDialog alertDialog_lang = alertDialogBuilder_lang.create();
                alertDialog_lang.show();
                break;

            case arp.dolphinsoft.lidora.shelemshomar.R.id.mnu_exit:
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("EXIT", true);
                startActivity(intent);
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return false;
    }

}

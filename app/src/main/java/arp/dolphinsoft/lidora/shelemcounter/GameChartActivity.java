package arp.dolphinsoft.lidora.shelemcounter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class GameChartActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemLongClickListener {

    TextView txv_first_group_name, txv_first_group_point, txv_vs,
            txv_second_group_name, txv_second_group_point;
    Button btn_start_round, btn_end_round;

    private ArrayList<HashMap<String, String>> list;
    ListView list_view;

    int game_type;
    int double_negative;
    String first_group_name;
    String second_group_name;
    int team_starter;
    String read_game_points;
    String get_game_points;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lay_game_chart);

        initialize();

        getIntentExtras();

        makeHeader();

        btn_start_round.setOnClickListener(this);
        btn_end_round.setOnClickListener(this);

        list_view.setOnItemLongClickListener(this);
    }

    private void updateListView() {
        ListViewAdapter adapter=new ListViewAdapter(this, list);
        list_view.setAdapter(adapter);
    }

    private void addReadDataToListView() {
        HashMap<String,String> temp=new HashMap<String, String>();
        String arrow = null;

        if(team_starter == 1) {
            arrow = "<--";
        } else if(team_starter == 2) {
            arrow = "-->";
        }

        temp.put(Constants.FIRST_COLUMN, null);
        temp.put(Constants.SECOND_COLUMN, read_game_points);
        temp.put(Constants.SECOND_ARROW_COLUMN, arrow);
        temp.put(Constants.THIRD_COLUMN, null);
        list.add(temp);

        updateListView();
    }

    private void addDataToListView() {
        deleteLastItemFromList();

        HashMap<String,String> temp=new HashMap<String, String>();

        int read_points = 0;

        try {
            read_points = Integer.parseInt(read_game_points);
        } catch (Exception e) {
            read_points = game_type;
        }

        int first_score = 0;
        int second_score = 0;
        int get_points = Integer.parseInt(get_game_points);

        if (read_points == game_type) { //Chech for Shelem
            if (get_points == 0) {
                first_score = read_points * 2;
            } else {
                first_score = read_points * 2 * -1;
            }
        } else if (game_type - read_points < get_points) {
            first_score = read_points * -1;
        } else {
            first_score = game_type - get_points;
        }

        if (double_negative == 1) {
            if (get_points >= game_type / 2) {
                first_score = read_points * 2 * -1;
            }
        }

        second_score = get_points;

        if (team_starter == 1) {
            temp.put(Constants.FIRST_COLUMN, String.valueOf(first_score));
            temp.put(Constants.SECOND_COLUMN, read_game_points);
            temp.put(Constants.SECOND_ARROW_COLUMN, "<--");
            temp.put(Constants.THIRD_COLUMN, String.valueOf(second_score));
            list.add(temp);
        } else if (team_starter == 2) {
            temp.put(Constants.FIRST_COLUMN, String.valueOf(second_score));
            temp.put(Constants.SECOND_COLUMN, read_game_points);
            temp.put(Constants.SECOND_ARROW_COLUMN, "-->");
            temp.put(Constants.THIRD_COLUMN, String.valueOf(first_score));
            list.add(temp);
        }

        updateListView();

        showSumPointsInHeader();

        checkWinner(getSumListRows(1), getSumListRows(2));
    }

    private void showSumPointsInHeader() {
        int first_player_sum_points = getSumListRows(1);
        int second_player_sum_points = getSumListRows(2);

        txv_first_group_point.setText(String.valueOf(first_player_sum_points));
        txv_second_group_point.setText(String.valueOf(second_player_sum_points));
    }

    private void checkWinner(int first_player_sum_points, int second_player_sum_points) {
        int win_score = 1000 + game_type;

        if (first_player_sum_points > win_score && second_player_sum_points > win_score) {
            if (first_player_sum_points > second_player_sum_points) {
                finishGame(1);
            } else {
                finishGame(2);
            }
        } else if (first_player_sum_points > win_score) {
            finishGame(1);
        } else if (second_player_sum_points > win_score) {
            finishGame(1);
        }
    }

    private void finishGame(int player_num) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

//        alertDialogBuilder.setIcon();
        alertDialogBuilder.setCancelable(true);
        alertDialogBuilder.setMessage(player_num == 1 ? first_group_name : second_group_name +
                "win the game.");
        alertDialogBuilder.setTitle("Game Over");

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        if (player_num == 1) {
            Toast.makeText(this, first_group_name + " win the game.", Toast.LENGTH_SHORT).show();
        } else if (player_num == 2) {
            Toast.makeText(this, second_group_name + " win the game.", Toast.LENGTH_SHORT).show();
        }

        btn_start_round.setEnabled(false);
        btn_end_round.setEnabled(true);
    }

    private void makeHeader() {
        txv_first_group_name.setText(first_group_name);
        txv_second_group_name.setText(second_group_name);
        txv_vs.setText(R.string.vs);
        txv_first_group_point.setText("0");
        txv_second_group_point.setText("0");
    }

    private void getIntentExtras() {
        Intent intent = getIntent();

        game_type           = intent.getIntExtra("game_type", 165);
        double_negative     = intent.getIntExtra("double_negative", 0);
        first_group_name    = intent.getStringExtra("first_group_name");
        second_group_name   = intent.getStringExtra("second_group_name");
    }

    private void initialize() {
        txv_first_group_name    = (TextView) findViewById(R.id.txt_header_first_group_name);
        txv_first_group_point   = (TextView) findViewById(R.id.txt_header_first_group_point);
        txv_vs                  = (TextView) findViewById(R.id.txt_header_vs);
        txv_second_group_name   = (TextView) findViewById(R.id.txt_header_second_group_name);
        txv_second_group_point  = (TextView) findViewById(R.id.txt_header_second_group_point);
        btn_start_round         = (Button) findViewById(R.id.btn_start_round);
        btn_end_round           = (Button) findViewById(R.id.btn_end_round);

        list_view =(ListView)findViewById(R.id.list_view);
        list=new ArrayList<HashMap<String,String>>();

        btn_end_round.setEnabled(false);
    }

    @Override
    public void onClick(View view) {
        if ( view.getId() == R.id.btn_start_round) {
            showAlertForStartRound();
        } else if ( view.getId() == R.id.btn_end_round) {
            showAlertForEndRound();
        }
    }

    private void showAlertForStartRound() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setMessage(R.string.str_start_round);

        LayoutInflater inflater = this.getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.lay_alter_start_dialog, null);
        dialogBuilder.setView(dialogView);

        final RadioButton radio_first_group   = (RadioButton) dialogView.findViewById(R.id.radio_alter_start_first_group);
        final RadioButton radio_second_group  = (RadioButton) dialogView.findViewById(R.id.radio_alter_start_second_group);
        final NumberPicker num_pick_1         = (NumberPicker) dialogView.findViewById(R.id.numberPicker1);
        final NumberPicker num_pick_2         = (NumberPicker) dialogView.findViewById(R.id.numberPicker2);
        final NumberPicker num_pick_3         = (NumberPicker) dialogView.findViewById(R.id.numberPicker3);
        final TextView txt_show_total_read    = (TextView) dialogView.findViewById(R.id.txt_start_alter_show_num_picks_total);
        final CheckBox check_shelem           = (CheckBox) dialogView.findViewById(R.id.check_start_alter_shelem);

        num_pick_1.setMinValue(1);
        num_pick_1.setMaxValue(2);
        num_pick_2.setMinValue(0);
        num_pick_2.setMaxValue(9);
        num_pick_3.setMinValue(0);
        num_pick_3.setMaxValue(1);

        num_pick_1.setDisplayedValues(new String[]{"1", "2"});
        num_pick_2.setDisplayedValues(new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"});
        num_pick_3.setDisplayedValues(new String[]{"0", "5"});

        num_pick_1.setValue(0);
        num_pick_2.setValue(2);
        num_pick_3.setValue(1);

        radio_first_group.setText(first_group_name);
        radio_second_group.setText(second_group_name);
        radio_first_group.setChecked(true);

        String total_num_pickers = calculateNumberPickers(
                num_pick_1.getValue(), num_pick_2.getValue(), num_pick_3.getValue());
        txt_show_total_read.setText(total_num_pickers);

        num_pick_1.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                String total_num_pickers = calculateNumberPickers(
                        num_pick_1.getValue(), num_pick_2.getValue(), num_pick_3.getValue());
                txt_show_total_read.setText(total_num_pickers);
            }
        });

        num_pick_2.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                String total_num_pickers = calculateNumberPickers(
                        num_pick_1.getValue(), num_pick_2.getValue(), num_pick_3.getValue());
                txt_show_total_read.setText(total_num_pickers);
            }
        });

        num_pick_3.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                String total_num_pickers = calculateNumberPickers(
                        num_pick_1.getValue(), num_pick_2.getValue(), num_pick_3.getValue());
                txt_show_total_read.setText(total_num_pickers);
            }
        });

        dialogBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (radio_first_group.isChecked()) {
                    team_starter = 1;
                } else {
                    team_starter = 2;
                }

                read_game_points = txt_show_total_read.getText().toString();

                addReadDataToListView();

                btn_start_round.setEnabled(false);
                btn_end_round.setEnabled(true);

            }
        });

        dialogBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                return;
            }
        });

        check_shelem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    num_pick_1.setEnabled(false);
                    num_pick_2.setEnabled(false);
                    num_pick_3.setEnabled(false);

                    txt_show_total_read.setText(R.string.shelem);
                } else {
                    num_pick_1.setEnabled(true);
                    num_pick_2.setEnabled(true);
                    num_pick_3.setEnabled(true);

                    String total_num_pickers = calculateNumberPickers(
                            num_pick_1.getValue(), num_pick_2.getValue(), num_pick_3.getValue());
                    txt_show_total_read.setText(total_num_pickers);
                }
            }
        });

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    private void showAlertForEndRound() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setMessage(R.string.str_end_round);

        LayoutInflater inflater = this.getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.lay_alter_end_dialog, null);
        dialogBuilder.setView(dialogView);

        final NumberPicker num_pick_1         = (NumberPicker) dialogView.findViewById(R.id.numberPicker1_end);
        final NumberPicker num_pick_2         = (NumberPicker) dialogView.findViewById(R.id.numberPicker2_end);
        final NumberPicker num_pick_3         = (NumberPicker) dialogView.findViewById(R.id.numberPicker3_end);
        final TextView txt_show_total_read    = (TextView) dialogView.findViewById(R.id.txt_end_alter_show_num_picks_total);

        num_pick_1.setMinValue(0);
        num_pick_1.setMaxValue(2);
        num_pick_2.setMinValue(0);
        num_pick_2.setMaxValue(9);
        num_pick_3.setMinValue(0);
        num_pick_3.setMaxValue(1);

        num_pick_1.setDisplayedValues(new String[]{"0", "1", "2"});
        num_pick_2.setDisplayedValues(new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"});
        num_pick_3.setDisplayedValues(new String[]{"0", "5"});

        num_pick_1.setValue(0);
        num_pick_2.setValue(2);
        num_pick_3.setValue(1);

        String total_num_pickers = calculateNumberPickers(
                num_pick_1.getValue(), num_pick_2.getValue(), num_pick_3.getValue());
        total_num_pickers = removeZeroFromString(total_num_pickers);
        txt_show_total_read.setText(total_num_pickers);

        num_pick_1.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                String total_num_pickers = calculateNumberPickers(
                        num_pick_1.getValue(), num_pick_2.getValue(), num_pick_3.getValue());
                total_num_pickers = removeZeroFromString(total_num_pickers);
                txt_show_total_read.setText(total_num_pickers);
            }
        });

        num_pick_2.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                String total_num_pickers = calculateNumberPickers(
                        num_pick_1.getValue(), num_pick_2.getValue(), num_pick_3.getValue());
                total_num_pickers = removeZeroFromString(total_num_pickers);
                txt_show_total_read.setText(total_num_pickers);
            }
        });

        num_pick_3.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                String total_num_pickers = calculateNumberPickers(
                        num_pick_1.getValue(), num_pick_2.getValue(), num_pick_3.getValue());
                total_num_pickers = removeZeroFromString(total_num_pickers);
                txt_show_total_read.setText(total_num_pickers);
            }
        });

        dialogBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                get_game_points = txt_show_total_read.getText().toString();
                addDataToListView();

                btn_start_round.setEnabled(true);
                btn_end_round.setEnabled(false);
            }
        });

        dialogBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                return;
            }
        });

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    private String removeZeroFromString(String input) {
        if (input.startsWith("0")) {
            input = input.substring(1, input.length());
            if (input.startsWith("0")) {
                input = input.substring(1, input.length());
            }
        }
        return input;
    }

    private String calculateNumberPickers(int n1, int n2, int n3) {
        return n1 + "" + n2 + "" + n3 * 5;
    }

    private void deleteLastItemFromList() {
        list.remove(list.size() - 1);
        updateListView();
    }

    private int getSumListRows(int player) {
        int sum = 0;
        if (player == 1) {
            for (int i = 0; i < list.size(); i++) {
                HashMap<String, String> map = list.get(i);
                sum += Integer.parseInt(map.get(Constants.FIRST_COLUMN));
            }
        } else if (player == 2) {
            for (int i = 0; i < list.size(); i++) {
                HashMap<String, String> map = list.get(i);
                sum += Integer.parseInt(map.get(Constants.THIRD_COLUMN));
            }
        }

        return sum;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (btn_start_round.isEnabled()) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

            alertDialogBuilder.setIcon(R.mipmap.ic_launcher);
            alertDialogBuilder.setCancelable(true);
            alertDialogBuilder.setMessage(R.string.prompt_delete);
            alertDialogBuilder.setTitle(R.string.delete_title);
            alertDialogBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    deleteLastItemFromList();
                    showSumPointsInHeader();
                }
            });
            alertDialogBuilder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    return;
                }
            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
        return false;
    }
}

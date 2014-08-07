package fr.upmc.dizainier.app;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;


public class MainActivity extends Activity {
    private int value = 0;
    private static final int MAX = 99;
    TextView result;
    RadioGroup unites;
    RadioGroup dizaines;
    boolean isHexa = false;
    SeekBar seekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        result = (TextView) findViewById(R.id.textViewRes);
        unites = (RadioGroup) findViewById(R.id.radioGroupUnit);
        dizaines = (RadioGroup) findViewById(R.id.radioGroupDiz);
        seekBar = (SeekBar) findViewById(R.id.seekBar);

        OnCheckedChange listener = new OnCheckedChange();
        unites.setOnCheckedChangeListener(listener);
        dizaines.setOnCheckedChangeListener(listener);

        seekBar.setOnSeekBarChangeListener(new OnSeekChange());
        seekBar.setMax(MAX);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void onButtonClick(View view) {
        value = 0;
        updateAllViews();
    }

    public void onToggleClicked(View view) {
        if(! (view instanceof Switch)){
            throw new IllegalArgumentException();
        }
        isHexa = ((Switch) view).isChecked();
        swtichHexa();

    }
    // Attention ne marche que si le RadioµGroup ne contient pas autre chose que des RadioButton
    private class OnCheckedChange implements RadioGroup.OnCheckedChangeListener{
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            int idx = group.indexOfChild(findViewById(checkedId));

            if(group == dizaines){
                value = value % 10; // On vire les anciennes dizaine
                value = value + 10 * idx; // On met les nouvelles
            }else if(group == unites){
                value = value / 10; // meme idee
                value = value*10 + idx;
            }
            updateAllViews();
        }
    }

    private class OnSeekChange implements SeekBar.OnSeekBarChangeListener{

        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {}

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {}

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            value = seekBar.getProgress();
            Log.d("OnSeekBarChangeListener", "nouvelle valeur : " + value);
            updateAllViews();
        }
    }

    // Met à jour tous les composant
    private void updateAllViews(){
        if(value < 0 || value > 100){
            Log.e("updateAllViews", "range foiraux : " + value);
            throw new IndexOutOfBoundsException("Valeur non comprise entre 0 et 100");
        }
        swtichHexa();

        RadioButton radioButton = (RadioButton) unites.getChildAt(value % 10);
        if(! (radioButton instanceof RadioButton)) throw new RuntimeException();
        radioButton.setChecked(true);

        radioButton = (RadioButton) dizaines.getChildAt(value / 10);
        if(! (radioButton instanceof RadioButton)) throw new RuntimeException();
        radioButton.setChecked(true);

        seekBar.setProgress(value);
    }

    private void swtichHexa(){
        if(isHexa) {
            result.setText("0x" + Integer.toHexString(value));
        }else{
            result.setText(Integer.toString(value));
        }
    }


}

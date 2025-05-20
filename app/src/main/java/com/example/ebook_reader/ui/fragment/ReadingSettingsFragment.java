package com.example.ebook_reader.ui.fragment;

//package com.example.ebookreader.ui.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.example.ebook_reader.R;

public class ReadingSettingsFragment extends BottomSheetDialogFragment {
    private SeekBar fontSizeSeekBar;
    private Spinner backgroundColorSpinner;
    private SharedPreferences prefs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reading_settings, container, false);

        fontSizeSeekBar = view.findViewById(R.id.font_size_seekbar);
        backgroundColorSpinner = view.findViewById(R.id.background_color_spinner);
        prefs = getContext().getSharedPreferences("reading_settings", Context.MODE_PRIVATE);

        setupFontSizeSeekBar();
        setupBackgroundColorSpinner();

        return view;
    }

    private void setupFontSizeSeekBar() {
        fontSizeSeekBar.setMax(24);
        fontSizeSeekBar.setProgress(prefs.getInt("font_size", 16) - 12);
        fontSizeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int fontSize = progress + 12;
                prefs.edit().putInt("font_size", fontSize).apply();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private void setupBackgroundColorSpinner() {
        String[] colors = {"Light", "Dark"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, colors);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        backgroundColorSpinner.setAdapter(adapter);
        backgroundColorSpinner.setSelection(prefs.getInt("background_color", 0));
        backgroundColorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                prefs.edit().putInt("background_color", position).apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }
}
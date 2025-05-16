package com.example.ebook_reader_app.fragments;



import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import com.example.booksreading.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class SettingsBottomSheetFragment extends BottomSheetDialogFragment {
    private SeekBar fontSizeSeekBar, brightnessSeekBar;
    private Spinner fontSpinner;
    private View backgroundLight, backgroundDark, backgroundSepia;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings_bottom_sheet, container, false);

        fontSizeSeekBar = view.findViewById(R.id.font_size_seekbar);
        brightnessSeekBar = view.findViewById(R.id.brightness_seekbar);
        fontSpinner = view.findViewById(R.id.font_spinner);
        backgroundLight = view.findViewById(R.id.bg_light);
        backgroundDark = view.findViewById(R.id.bg_dark);
        backgroundSepia = view.findViewById(R.id.bg_sepia);

        fontSizeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Thông báo ReaderActivity để cập nhật cỡ chữ
            }
        });

        brightnessSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Điều chỉnh độ sáng màn hình
                WindowManager.LayoutParams params = getActivity().getWindow().getAttributes();
                params.screenBrightness = progress / 100.0f;
                getActivity().getWindow().setAttributes(params);
            }
        });

        backgroundLight.setOnClickListener(v -> {
            // Thông báo ReaderActivity để đặt nền sáng
        });

        return view;
    }
}
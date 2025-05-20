package com.example.ebook_reader.ui.fragment;

//package com.example.ebookreader.ui.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import com.example.ebook_reader.R;
import com.example.ebook_reader.data.database.AppDatabase;
import com.example.ebook_reader.data.entity.User;

public class UserProfileFragment extends Fragment {
    private TextView nameTextView;
    private TextView emailTextView;
    private Spinner themeSpinner;
    private Button updateButton;
    private AppDatabase db;
    private SharedPreferences prefs;
    private User user;  // Biến thành viên để truy cập trong nhiều chỗ

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);

        nameTextView = view.findViewById(R.id.name_text);
        emailTextView = view.findViewById(R.id.email_text);
        themeSpinner = view.findViewById(R.id.theme_spinner);
        updateButton = view.findViewById(R.id.update_button);
        db = AppDatabase.getInstance(getContext());
        prefs = getContext().getSharedPreferences("auth_prefs", Context.MODE_PRIVATE);

        String uid = prefs.getString("logged_in_user_id", null);
        if (uid != null) {
            new Thread(() -> {
                user = db.userDao().getUserById(uid);
                if (user != null) {
                    getActivity().runOnUiThread(() -> {
                        nameTextView.setText(user.name);
                        emailTextView.setText(user.email);
                    });
                }
            }).start();
        }

        setupThemeSpinner();

        updateButton.setOnClickListener(v -> {
            if (user == null) return;
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Cập nhật hồ sơ");
            View dialogView = inflater.inflate(R.layout.dialog_update_profile, null);
            EditText nameInput = dialogView.findViewById(R.id.name_input);
            nameInput.setText(user.name);
            builder.setView(dialogView);
            builder.setPositiveButton("Lưu", (dialog, which) -> {
                user.name = nameInput.getText().toString();
                new Thread(() -> {
                    db.userDao().update(user);
                    getActivity().runOnUiThread(() -> {
                        nameTextView.setText(user.name);
                    });
                }).start();
            });
            builder.setNegativeButton("Hủy", null);
            builder.show();
        });

        return view;
    }

    private void setupThemeSpinner() {
        String[] themes = {"Sáng", "Tối"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, themes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        themeSpinner.setAdapter(adapter);
        themeSpinner.setSelection(prefs.getInt("theme", 0));
        themeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                prefs.edit().putInt("theme", position).apply();
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });
    }
}

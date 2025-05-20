package com.example.ebook_reader.ui.fragment;

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
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.ebook_reader.R;
import com.example.ebook_reader.data.database.AppDatabase;
import com.example.ebook_reader.data.entity.User;

public class UserProfileFragment extends Fragment {
    private TextView nameTextView;
    private TextView emailTextView;
    private Spinner themeSpinner;
    private Button updateButton;
    private SharedPreferences prefs;
    private UserProfileViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);

        nameTextView = view.findViewById(R.id.name_text);
        emailTextView = view.findViewById(R.id.email_text);
        themeSpinner = view.findViewById(R.id.theme_spinner);
        updateButton = view.findViewById(R.id.update_button);
        prefs = getContext().getSharedPreferences("auth_prefs", Context.MODE_PRIVATE);

        viewModel = new UserProfileViewModel(getContext());
        String uid = prefs.getString("logged_in_user_id", null);
        android.util.Log.d("UserProfile", "UID: " + uid);

        if (uid != null) {
            viewModel.loadUser(uid);
            viewModel.getUser().observe(getViewLifecycleOwner(), user -> {
                if (user != null) {
                    nameTextView.setText(user.name);
                    emailTextView.setText(user.email);
                    android.util.Log.d("UserProfile", "User loaded: " + user.name);
                } else {
                    nameTextView.setText("Không tìm thấy người dùng");
                    emailTextView.setText("");
                    android.util.Log.w("UserProfile", "User is null for UID: " + uid);
                }
            });
        } else {
            nameTextView.setText("Vui lòng đăng nhập");
            emailTextView.setText("");
            android.util.Log.w("UserProfile", "UID is null");
        }

        setupThemeSpinner();

        updateButton.setOnClickListener(v -> {
            if (viewModel.getUser().getValue() == null) {
                Toast.makeText(getContext(), "Không có dữ liệu người dùng để cập nhật", Toast.LENGTH_SHORT).show();
                return;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Cập nhật hồ sơ");
            View dialogView = inflater.inflate(R.layout.dialog_update_profile, null);
            EditText nameInput = dialogView.findViewById(R.id.name_input);
            nameInput.setText(viewModel.getUser().getValue().name);
            builder.setView(dialogView);
            builder.setPositiveButton("Lưu", (dialog, which) -> {
                User updatedUser = viewModel.getUser().getValue();
                updatedUser.name = nameInput.getText().toString();
                new Thread(() -> {
                    AppDatabase.getInstance(getContext()).userDao().update(updatedUser);
                    getActivity().runOnUiThread(() -> nameTextView.setText(updatedUser.name));
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
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                prefs.edit().putInt("theme", position).apply();
                Toast.makeText(getContext(), "Chủ đề sẽ được áp dụng khi khởi động lại", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    public static class UserProfileViewModel extends ViewModel {
        private MutableLiveData<User> userLiveData = new MutableLiveData<>();
        private AppDatabase db;

        public UserProfileViewModel(Context context) {
            db = AppDatabase.getInstance(context);
        }

        public void loadUser(String uid) {
            new Thread(() -> {
                User user = db.userDao().getUserById(uid);
                android.util.Log.d("UserProfileViewModel", "User for uid " + uid + ": " + (user != null ? user.name : "null"));
                userLiveData.postValue(user);
            }).start();
        }

        public LiveData<User> getUser() {
            return userLiveData;
        }
    }
}
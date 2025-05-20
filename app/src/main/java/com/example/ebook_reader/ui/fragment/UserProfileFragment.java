package com.example.ebook_reader.ui.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.ebook_reader.R;
import com.example.ebook_reader.data.database.AppDatabase;
import com.example.ebook_reader.data.entity.User;
import com.example.ebook_reader.util.AuthUtils;

public class UserProfileFragment extends Fragment {
    private TextView nameTextView;
    private TextView emailTextView;
    private Button updateButton;
    private Button changePasswordButton;
    private SharedPreferences prefs;
    private UserProfileViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);

        nameTextView = view.findViewById(R.id.name_text);
        emailTextView = view.findViewById(R.id.email_text);
        updateButton = view.findViewById(R.id.update_button);
        changePasswordButton = view.findViewById(R.id.change_password_button);
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

        changePasswordButton.setOnClickListener(v -> {
            if (viewModel.getUser().getValue() == null) {
                Toast.makeText(getContext(), "Không có dữ liệu người dùng để đổi mật khẩu", Toast.LENGTH_SHORT).show();
                return;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Đổi Mật Khẩu");
            View dialogView = inflater.inflate(R.layout.dialog_change_password, null);
            EditText newPasswordInput = dialogView.findViewById(R.id.new_password_input);
            builder.setView(dialogView);
            builder.setPositiveButton("Lưu", (dialog, which) -> {
                String newPassword = newPasswordInput.getText().toString().trim();
                if (newPassword.isEmpty()) {
                    Toast.makeText(getContext(), "Mật khẩu không được để trống", Toast.LENGTH_SHORT).show();
                    return;
                }
                User updatedUser = viewModel.getUser().getValue();
                String oldPassword = updatedUser.passwordHash;
                // Mã hóa mật khẩu mới trước khi lưu
                String hashedNewPassword = AuthUtils.hashPassword(newPassword);
                updatedUser.passwordHash = hashedNewPassword;
                new Thread(() -> {
                    // Cập nhật mật khẩu trong cơ sở dữ liệu
                    AppDatabase.getInstance(getContext()).userDao().update(updatedUser);

                    // Truy vấn lại dữ liệu để kiểm tra
                    User userAfterUpdate = AppDatabase.getInstance(getContext()).userDao().getUserById(updatedUser.uid);
                    if (userAfterUpdate != null && userAfterUpdate.passwordHash.equals(hashedNewPassword)) {
                        getActivity().runOnUiThread(() -> {
                            Toast.makeText(getContext(), "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                            android.util.Log.d("UserProfile", "Password updated successfully. Old: " + oldPassword + ", New: " + userAfterUpdate.passwordHash);
                        });
                    } else {
                        getActivity().runOnUiThread(() -> {
                            Toast.makeText(getContext(), "Đổi mật khẩu thất bại", Toast.LENGTH_SHORT).show();
                            android.util.Log.e("UserProfile", "Password update failed. Expected: " + hashedNewPassword + ", Found: " + (userAfterUpdate != null ? userAfterUpdate.passwordHash : "null"));
                        });
                    }
                }).start();
            });
            builder.setNegativeButton("Hủy", null);
            builder.show();
        });

        return view;
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
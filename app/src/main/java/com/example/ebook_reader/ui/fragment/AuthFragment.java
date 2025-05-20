package com.example.ebook_reader.ui.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.example.ebook_reader.R;
import com.example.ebook_reader.data.database.AppDatabase;
import com.example.ebook_reader.data.entity.User;
import com.example.ebook_reader.util.AuthUtils;

public class AuthFragment extends Fragment {
    private EditText emailInput;
    private EditText passwordInput;
    private EditText nameInput;
    private Button loginButton;
    private Button registerButton;
    private AppDatabase db;
    private SharedPreferences prefs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_auth, container, false);

        emailInput = view.findViewById(R.id.email_input);
        passwordInput = view.findViewById(R.id.password_input);
        nameInput = view.findViewById(R.id.name_input);
        loginButton = view.findViewById(R.id.login_button);
        registerButton = view.findViewById(R.id.register_button);

        db = AppDatabase.getInstance(getContext());
        prefs = getContext().getSharedPreferences("auth_prefs", Context.MODE_PRIVATE);

        loginButton.setOnClickListener(v -> login());
        registerButton.setOnClickListener(v -> register());

        return view;
    }

    private void login() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(getContext(), "Vui lòng nhập email và mật khẩu", Toast.LENGTH_SHORT).show();
            return;
        }

        String passwordHash = AuthUtils.hashPassword(password);

        new Thread(() -> {
            User user = db.userDao().authenticate(email, passwordHash);
            if (user != null) {
                prefs.edit().putString("logged_in_user_id", user.uid).apply();
                requireActivity().runOnUiThread(() ->
                        Navigation.findNavController(getView()).navigate(R.id.action_auth_to_ebook_list)
                );
            } else {
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(getContext(), "Email hoặc mật khẩu không đúng", Toast.LENGTH_SHORT).show()
                );
            }
        }).start();
    }

    private void register() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        String name = nameInput.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty() || name.isEmpty()) {
            Toast.makeText(getContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(() -> {
            User existingUser = db.userDao().getUserByEmail(email);
            if (existingUser != null) {
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(getContext(), "Email đã được sử dụng", Toast.LENGTH_SHORT).show()
                );
                return;
            }

            User user = new User();
            user.uid = AuthUtils.generateUid();
            user.name = name;
            user.email = email;
            user.passwordHash = AuthUtils.hashPassword(password);

            db.userDao().insert(user);

            prefs.edit().putString("logged_in_user_id", user.uid).apply();

            requireActivity().runOnUiThread(() -> {
                Toast.makeText(getContext(), "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(getView()).navigate(R.id.action_auth_to_ebook_list);
            });
        }).start();
    }
}

package com.example.ebook_reader_app.fragments;



import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import androidx.fragment.app.DialogFragment;
import com.example.booksreading.R;

public class NoteDialogFragment extends DialogFragment {
    private EditText noteEditText;
    private OnNoteSaveListener listener;

    public interface OnNoteSaveListener {
        void onNoteSave(String noteText);
    }

    public void setOnNoteSaveListener(OnNoteSaveListener listener) {
        this.listener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note_dialog, container, false);
        noteEditText = view.findViewById(R.id.note_edit_text);

        view.findViewById(R.id.save_button).setOnClickListener(v -> {
            String noteText = noteEditText.getText().toString();
            if (listener != null) {
                listener.onNoteSave(noteText);
            }
            dismiss();
        });

        return view;
    }
}
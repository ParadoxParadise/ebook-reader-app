package com.example.ebook_reader.ui.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ebook_reader.R;
import com.example.ebook_reader.data.database.AppDatabase;
import com.example.ebook_reader.data.entity.Ebook;
import com.example.ebook_reader.ui.adapter.EbookAdapter;
import com.example.ebook_reader.util.FileUtils;

public class EbookListFragment extends Fragment {
    private RecyclerView recyclerView;
    private EbookAdapter adapter;
    private Spinner filterSpinner;
    private Spinner sortSpinner;
    private AppDatabase db;
    private ActivityResultLauncher<Intent> filePickerLauncher;
    private ActivityResultLauncher<String> requestPermissionLauncher;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        filePickerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                Uri uri = result.getData().getData();
                handleFileSelection(uri);
            }
        });
        requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            Log.d("EbookListFragment", "Permission granted: " + isGranted);
            if (isGranted) {
                openFilePicker();
            } else {
                if (!shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Toast.makeText(getContext(), "Quyền truy cập bộ nhớ bị từ chối. Vui lòng cấp quyền trong cài đặt.", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.setData(Uri.parse("package:" + getContext().getPackageName()));
                    startActivity(intent);
                } else {
                    Toast.makeText(getContext(), "Cần quyền truy cập bộ nhớ để thêm file", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ebook_list, container, false);

        recyclerView = view.findViewById(R.id.ebook_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        filterSpinner = view.findViewById(R.id.filter_spinner);
        sortSpinner = view.findViewById(R.id.sort_spinner);
        db = AppDatabase.getInstance(getContext());
        adapter = new EbookAdapter(db, ebook -> {
            Bundle args = new Bundle();
            args.putInt("ebookId", ebook.id);
            Navigation.findNavController(view).navigate(R.id.action_ebook_list_to_detail, args);
        });
        recyclerView.setAdapter(adapter);

        setupFilterSpinner();
        setupSortSpinner();
        observeAllEbooks();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.ebook_list_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                db.ebookDao().searchEbooks("%" + query + "%").observe(getViewLifecycleOwner(), ebooks -> {
                    adapter.setEbooks(ebooks);
                });
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    observeAllEbooks();
                }
                return true;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add_ebook) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q &&
                    ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                Log.d("EbookListFragment", "Requesting READ_EXTERNAL_STORAGE permission");
                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
            } else {
                Log.d("EbookListFragment", "Opening file picker directly");
                openFilePicker();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/pdf");
        filePickerLauncher.launch(intent);
    }

    private void setupFilterSpinner() {
        String[] filterOptions = {"All", "Fiction", "Non-Fiction", "PDF"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, filterOptions);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filterSpinner.setAdapter(spinnerAdapter);

        filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = filterOptions[position];
                switch (selected) {
                    case "All":
                        observeAllEbooks();
                        break;
                    case "Fiction":
                    case "Non-Fiction":
                        db.ebookDao().filterByGenre(selected).observe(getViewLifecycleOwner(), ebooks -> {
                            adapter.setEbooks(ebooks);
                        });
                        break;
                    case "PDF":
                        db.ebookDao().filterByFormat(selected).observe(getViewLifecycleOwner(), ebooks -> {
                            adapter.setEbooks(ebooks);
                        });
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                observeAllEbooks();
            }
        });
    }

    private void setupSortSpinner() {
        String[] sortOptions = {"Title", "Date Added", "Progress"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, sortOptions);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSpinner.setAdapter(spinnerAdapter);

        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (sortOptions[position]) {
                    case "Title":
                        observeAllEbooks();
                        break;
                    case "Date Added":
                        db.ebookDao().getEbooksByDateAdded().observe(getViewLifecycleOwner(), ebooks -> {
                            adapter.setEbooks(ebooks);
                        });
                        break;
                    case "Progress":
                        db.ebookDao().getEbooksByProgress().observe(getViewLifecycleOwner(), ebooks -> {
                            adapter.setEbooks(ebooks);
                        });
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                observeAllEbooks();
            }
        });
    }

    private void observeAllEbooks() {
        db.ebookDao().getAllEbooks().observe(getViewLifecycleOwner(), ebooks -> {
            adapter.setEbooks(ebooks);
        });
    }

    private void handleFileSelection(Uri uri) {
        if (uri == null) {
            Toast.makeText(getContext(), "Không thể chọn file", Toast.LENGTH_SHORT).show();
            return;
        }

        String fileName = null;
        Cursor cursor = getContext().getContentResolver().query(uri, null, null, null, null);
        try {
            if (cursor != null && cursor.moveToFirst()) {
                int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                if (nameIndex != -1) {
                    fileName = cursor.getString(nameIndex);
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        if (fileName == null) {
            fileName = uri.getLastPathSegment();
        }

        String filePath = FileUtils.saveFileToInternalStorage(getContext(), uri, fileName);
        if (filePath != null) {
            Ebook ebook = new Ebook();
            ebook.title = fileName != null ? fileName : "Unknown Title";
            ebook.author = "Unknown";
            ebook.filePath = filePath;
            ebook.format = "PDF";
            ebook.genre = "Fiction";
            ebook.timestamp = System.currentTimeMillis();
            new Thread(() -> {
                db.ebookDao().insert(ebook);
            }).start();
        } else {
            getActivity().runOnUiThread(() -> {
                Toast.makeText(getContext(), "Lỗi khi lưu file", Toast.LENGTH_SHORT).show();
            });
        }
    }
}
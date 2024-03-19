package vn.id.houta.myapplication.module4;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.SearchView;
import android.widget.TextView;

import vn.id.houta.myapplication.R;

public class Module4Submain1Fragment extends Fragment {

    SearchView searchView;
    TextView textViewTitle;
    SQLiteDatabase sqliteDatabase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_module4_submain1, container, false);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
//        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });
        textViewTitle = view.findViewById(R.id.textViewTitle);
        searchView = view.findViewById(R.id.search);
//        AutoCompleteTextView searchText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
//        searchView.setOnQueryTextListener(new)
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Xử lý khi thanh tìm kiếm được mở ra
                System.out.println("Open");
                textViewTitle.setText("");
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                System.out.println("Close");
                textViewTitle.setText(getResources().getString(R.string.tittle_fragment_module4_submain1));
                return false;
            }
        });
//        sqliteDatabase = SQLiteDatabase.openOrCreateDatabase("ql")
        return view;
    }
}
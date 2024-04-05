package vn.id.houta.myapplication.module4;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
        ;
import java.util.ArrayList;

import vn.id.houta.myapplication.R;
//import vn.id.houta.myapplication.cusmodule.LessonListViewAdapter;
import vn.id.houta.myapplication.model.Lesson;

public class Module41Fragment extends Fragment {

    FirebaseUser firebaseUser;
    SearchView searchView;
    TextView textViewTitle;
    SQLiteDatabase sqliteDatabase;
    ArrayList<Lesson> listLesson;
    LessonListViewAdapter lessonListViewAdapter;
    ListView listViewLesson;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_module4_1, container, false);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
//        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
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
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                lessonListViewAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                lessonListViewAdapter.getFilter().filter(newText);
                lessonListViewAdapter.notifyDataSetChanged();
                return false;
            }
        });
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference lessonsRef = db.collection("lessons");

        lessonsRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    if (querySnapshot != null) {
                        for (QueryDocumentSnapshot document : querySnapshot) {
                            System.out.println(document.getId() + " => " + document.getData());
                        }
                    } else {
                        System.out.println("No documents found in the collection.");
                    }
                } else {
                    Exception e = task.getException();
                    if (e != null) {
                        e.printStackTrace();
                    }
                }
            }
        });

//        sqliteDatabase = SQLiteDatabase.openOrCreateDatabase("ql")
        listLesson = new ArrayList<>();
        listLesson.add(new Lesson(1, "Số 1", "123 Ta cùng đếm", 1, 100, 100));
        listLesson.add(new Lesson(2, "Số 2", "123 Ta cùng đếm", 1, 100, 120));
        listLesson.add(new Lesson(3, "Số 3", "123 Ta cùng đếm", 1, 0, 180));
        listLesson.add(new Lesson(3, "Số 4", "123 Ta cùng đếm", 1, 0, 130));
        listLesson.add(new Lesson(3, "Số 5", "123 Ta cùng đếm", 1, 0, 190));
        listLesson.add(new Lesson(3, "Số 6", "123 Ta cùng đếm", 1, 0, 180));
        listLesson.add(new Lesson(3, "Số 7", "123 Ta cùng đếm", 1, 0, 180));
        listLesson.add(new Lesson(3, "Số 3", "123 Ta cùng đếm", 1, 0, 180));
        listLesson.add(new Lesson(3, "Số 3", "123 Ta cùng đếm", 1, 0, 180));


        lessonListViewAdapter = new LessonListViewAdapter(getActivity(), listLesson);

        listViewLesson = (ListView) view.findViewById(R.id.listViewLesson);
        listViewLesson.setAdapter(lessonListViewAdapter);


        //Lắng nghe bắt sự kiện một phần tử danh sách được chọn
        listViewLesson.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Lesson product = (Lesson) lessonListViewAdapter.getItem(position);
                //Làm gì đó khi chọn sản phẩm (ví dụ tạo một Activity hiện thị chi tiết, biên tập ..)
//                Toast.makeText(MainActivity.this, product.name, Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }

    class LessonListViewAdapter extends BaseAdapter implements Filterable {
        ArrayList<Lesson> listLesson;
        ArrayList<Lesson> ListLessonOld;


        public LessonListViewAdapter(Context context, ArrayList<Lesson> listLesson) {
            this.listLesson = listLesson;
            this.ListLessonOld = listLesson;
        }

        @Override
        public int getCount() {
            //Cần trả về số phần tử mà ListView hiện thị
            return this.listLesson.size();
        }

        @Override
        public Object getItem(int position) {
            //Cần trả về đối tượng dữ liệu phần tử ở vị trí position
            return this.listLesson.get(position);
        }

        @Override
        public long getItemId(int position) {
            //Trả về một ID liên quan đến phần tử ở vị trí position
            return this.listLesson.get(position).getLessonId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View viewLesson;
            if (convertView == null) {
                viewLesson = View.inflate(parent.getContext(), R.layout.lesson_card_view, null);
            } else viewLesson = convertView;

            final Lesson lesson = (Lesson) getItem(position);

            CardView cardView = viewLesson.findViewById(R.id.cardView);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.hide(Module41Fragment.this);
                    fragmentTransaction.add(R.id.frame_layout, new Module41VideoFragment(lesson));
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            });

            ((TextView) viewLesson.findViewById(R.id.textViewLevelVideo)).setText("Cơ bản");
            ((TextView) viewLesson.findViewById(R.id.textViewTittleVideo)).setText(lesson.getTitle());

            int percen = lesson.getPercent();
            ((ProgressBar) viewLesson.findViewById(R.id.progressBar)).setProgress(percen);
            ((TextView) viewLesson.findViewById(R.id.textViewPercent)).setText(String.format("%d%%", percen));
            ((TextView) viewLesson.findViewById(R.id.textViewTimeVideo)).setText(lesson.getTimeVideo());

            return viewLesson;
        }


        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    String strSearch = constraint.toString();
                    if (strSearch.isEmpty()){
                        listLesson = ListLessonOld;
                    }else{
                        ArrayList<Lesson> list = new ArrayList<>();
                        for(Lesson i : ListLessonOld)
                            if(removeAccents(i.getTitle().toLowerCase()).contains(removeAccents(strSearch.toLowerCase())))
                                list.add(i);
                        listLesson = list;
                    }
                    FilterResults results = new FilterResults();
                    results.values = listLesson;
                    return results;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    listLesson = (ArrayList<Lesson>) results.values;
                    notifyDataSetChanged();
                }
            };
        }
    }
    public String removeAccents(String input) {
        return input.replaceAll("[àáâãåä]", "a")
                .replaceAll("[ç]", "c")
                .replaceAll("[èéêë]", "e")
                .replaceAll("[ìíîï]", "i")
                .replaceAll("[ñ]", "n")
                .replaceAll("[òóôõö]", "o")
                .replaceAll("[ùúûü]", "u")
                .replaceAll("[ÿý]", "y");
    }
}
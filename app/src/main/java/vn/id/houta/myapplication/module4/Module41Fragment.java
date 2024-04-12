package vn.id.houta.myapplication.module4;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import vn.id.houta.myapplication.R;
import vn.id.houta.myapplication.database.FirebaseHelper;
import vn.id.houta.myapplication.model.Lesson;

public class Module41Fragment extends Fragment {

    FirebaseUser firebaseUser;
    SearchView searchView;
    TextView textViewTitle;
    ArrayList<Lesson> listLesson;
    LessonListViewAdapter lessonListViewAdapter;
    ListView listViewLesson;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_module4_1, container, false);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
//        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());
        textViewTitle = view.findViewById(R.id.textViewTitle);
        searchView = view.findViewById(R.id.search);
//        AutoCompleteTextView searchText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
//        searchView.setOnQueryTextListener(new)
        searchView.setOnSearchClickListener(v -> {
            // Xử lý khi thanh tìm kiếm được mở ra
            System.out.println("Open");
            textViewTitle.setText("");
        });
        searchView.setOnCloseListener(() -> {
            System.out.println("Close");
            textViewTitle.setText(getResources().getString(R.string.tittle_fragment_module4_submain1));
            return false;
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

        listLesson = new ArrayList<>();
        listViewLesson = view.findViewById(R.id.listViewLesson);
        lessonListViewAdapter = new LessonListViewAdapter(getActivity(), listLesson);
        listViewLesson.setAdapter(lessonListViewAdapter);
        new FirebaseHelper().getLessonsForUser(new FirebaseHelper.GetLessonCallback() {
            @Override
            public void onGetLessonCallback(Lesson lesson) {
                if (lesson != null) {
                    listLesson.add(lesson);
                    System.out.println("add pre lesson " + lesson.getLessonId());
                    lessonListViewAdapter.notifyDataSetChanged();
                }
            }
        });

//        lessonListViewAdapter = new LessonListViewAdapter(getActivity(), listLesson);
//        listViewLesson = (ListView) view.findViewById(R.id.listViewLesson);
//        listViewLesson.setAdapter(lessonListViewAdapter);

//        listViewLesson.setOnItemClickListener((parent, view1, position, id) -> {
//            Lesson product = (Lesson) lessonListViewAdapter.getItem(position);
//        });

        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            lessonListViewAdapter.notifyDataSetChanged();
        }
    }

//    public void updateALessonUser(String lessonID, int timesStudy){
//        new FirebaseHelper().saveTimeStudyLessionForUser(lessonID, timesStudy);
//        Optional<Lesson> foundLesson = listLesson.stream()
//                .filter(lesson -> lesson.getLessonId().equals(lessonID))
//                .findFirst();
//        foundLesson.ifPresent(lesson -> lesson.getUserLesson().setTimesStudy(timesStudy));
//        lessonListViewAdapter.notifyDataSetChanged();
//    }

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
            return this.listLesson.get(position).getStt();
        }

        @SuppressLint("DefaultLocale")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View viewLesson;
            if (convertView == null) {
                viewLesson = View.inflate(parent.getContext(), R.layout.lesson_card_view, null);
            } else viewLesson = convertView;

            final Lesson lesson = (Lesson) getItem(position);

            CardView cardView = viewLesson.findViewById(R.id.cardView);
            cardView.setOnClickListener(v -> {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.hide(Module41Fragment.this);
                fragmentTransaction.add(R.id.frame_layout, new Module41VideoFragment(lesson, listLesson));
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            });

            ((TextView) viewLesson.findViewById(R.id.textViewLevelVideo)).setText("Cơ bản");
            ((TextView) viewLesson.findViewById(R.id.textViewTittleVideo)).setText(lesson.getTitle());

            int percent = lesson.getPercent();
            ((ProgressBar) viewLesson.findViewById(R.id.progressBar)).setProgress(percent);
            ((TextView) viewLesson.findViewById(R.id.textViewPercent)).setText(String.format("%d%%", percent));
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
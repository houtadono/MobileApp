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
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import vn.id.houta.myapplication.R;
import vn.id.houta.myapplication.database.FirebaseHelper;
import vn.id.houta.myapplication.model.Lesson;

public class Module41Fragment extends Fragment {

    SearchView searchView;
    TextView textViewCountLearned;
    ArrayList<Lesson> listLesson;
    LessonRecyclerViewAdapter lessonRecyclerViewAdapter;
    static int count_learned = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_module4_1, container, false);

        listLesson = new ArrayList<>();

        textViewCountLearned = view.findViewById(R.id.textViewCountLearned);
        TextView textViewTimeLearned = view.findViewById(R.id.textViewTimeLearned);


        RecyclerView recyclerViewLesson = view.findViewById(R.id.recyclerViewLesson);
        recyclerViewLesson.setLayoutManager(new LinearLayoutManager(getActivity()));
        lessonRecyclerViewAdapter = new LessonRecyclerViewAdapter(getActivity(), listLesson);
        recyclerViewLesson.setAdapter(lessonRecyclerViewAdapter);
        count_learned = 0;
        new FirebaseHelper().getLessonsForUser(lesson -> {
            if (lesson != null) {
                listLesson.add(lesson);
                System.out.println("add pre lesson " + lesson.getLessonId());
                if (lesson.getUserLesson().isLearned()) count_learned += 1;
                textViewCountLearned.setText(String.format("Đã học: %d/%d", count_learned, listLesson.size()));
                lessonRecyclerViewAdapter.notifyDataSetChanged();
            }
        });


        new FirebaseHelper().getTimeLearnToday(seconds->{
            int hours = seconds / 3600;
            int minutes = (seconds % 3600) / 60;
            textViewTimeLearned.setText("Số giờ hôm nay học: "+ hours + " giờ " + minutes + " phút");
        });

        view.findViewById(R.id.btn_statistic).setOnClickListener(v -> {
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.hide(Module41Fragment.this);
            fragmentTransaction.add(R.id.frame_layout, new StatisticFragment());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });
        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        textViewCountLearned.setText(String.format("Đã học: %d/%d", count_learned, listLesson.size()));
        if (!hidden) {
            lessonRecyclerViewAdapter.notifyDataSetChanged();
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

    class LessonRecyclerViewAdapter extends RecyclerView.Adapter<LessonRecyclerViewAdapter.ViewHolder> implements Filterable {
        private ArrayList<Lesson> listLesson;
        private ArrayList<Lesson> listLessonFull;

        public LessonRecyclerViewAdapter(Context context, ArrayList<Lesson> listLesson) {
            this.listLesson = listLesson;
            this.listLessonFull = new ArrayList<>(listLesson);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lesson_card_view, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Lesson lesson = listLesson.get(position);
            holder.textViewTitle.setText(lesson.getTitle());
            holder.textViewLevel.setText("Cơ bản");
            holder.textViewTime.setText(lesson.getTimeTotal());
            holder.progressBar.setProgress(lesson.getPercent());

            holder.cardView.setOnClickListener(v -> {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.hide((Module41Fragment.this));
                fragmentTransaction.add(R.id.frame_layout, new Module41VideoFragment(lesson, listLesson));
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            });
        }

        @Override
        public int getItemCount() {
            return listLesson.size();
        }

        @Override
        public Filter getFilter() {
            return lessonFilter;
        }

        private Filter lessonFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                ArrayList<Lesson> filteredList = new ArrayList<>();

                if (constraint == null || constraint.length() == 0) {
                    filteredList.addAll(listLessonFull);
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();

                    for (Lesson lesson : listLessonFull) {
                        if (lesson.getTitle().toLowerCase().contains(filterPattern)) {
                            filteredList.add(lesson);
                        }
                    }
                }

                FilterResults results = new FilterResults();
                results.values = filteredList;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                listLesson.clear();
                listLesson.addAll((ArrayList<Lesson>) results.values);
                notifyDataSetChanged();
            }
        };

        class ViewHolder extends RecyclerView.ViewHolder {
            private TextView textViewTitle;
            private TextView textViewLevel;
            private TextView textViewTime;
            private ProgressBar progressBar;
            private CardView cardView;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                textViewTitle = itemView.findViewById(R.id.textViewTittleVideo);
                textViewLevel = itemView.findViewById(R.id.textViewLevelVideo);
                textViewTime = itemView.findViewById(R.id.textViewTimeVideo);
                progressBar = itemView.findViewById(R.id.progressBar);
                cardView = itemView.findViewById(R.id.cardView);
            }
        }
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
            return this.listLesson.get(position).getStt();
        }

        @SuppressLint("DefaultLocale")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View viewLesson;
            if (convertView == null) {
                viewLesson = View.inflate(parent.getContext(), R.layout.item_lesson_card_view, null);
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
//            ((TextView) viewLesson.findViewById(R.id.textViewPercent)).setText(String.format("%d%%", percent));
            ((TextView) viewLesson.findViewById(R.id.textViewTimeVideo)).setText(lesson.getTimeTotal());

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
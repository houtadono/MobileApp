package vn.id.houta.myapplication.module4;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import vn.id.houta.myapplication.R;
import vn.id.houta.myapplication.model.Quiz;

public class Module42Fragment extends Fragment {
    ListView listViewQuiz;
    ArrayList<Quiz> listQuiz;
//    QuizListViewAdapter quizListViewAdapter;
    public Module42Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_module4_2, container, false);
        final int[] idCardQuiz = {R.id.card_quiz_random, R.id.card_quiz_comparison, R.id.card_quiz_counting};
        ArrayList<Quiz> listQuiz = new ArrayList<>();
        listQuiz.add(new Quiz("random", 15, 20));
        listQuiz.add(new Quiz("comparison", 8, 10));
        listQuiz.add(new Quiz("counting", 8, 10));

        for(int i = 0; i < listQuiz.size(); i++){
            final Quiz currentQuiz = listQuiz.get(i);
            view.findViewById(idCardQuiz[i]).setOnClickListener( v->{
                Intent intent = new Intent(requireActivity(), QuizActivity.class);
                intent.putExtra("QUIZ_EXTRA", currentQuiz);
                startActivity(intent);
                requireActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            });
        }

        return view;
    }
//    class QuizListViewAdapter extends BaseAdapter {
//
//        private  ArrayList<Quiz> listQuiz;
//
//        public QuizListViewAdapter(Context context, ArrayList<Quiz> listQuiz) {
//            this.listQuiz = listQuiz;
//        }
//
//        @Override
//        public int getCount() {
//            return this.listQuiz.size();
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return this.listQuiz.get(position);
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return 0;
//        }
//
//        @NonNull
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            View viewQuiz;
//            if (convertView == null) {
//                viewQuiz = View.inflate(parent.getContext(), R.layout.quiz_card_view, null);
//            } else viewQuiz = convertView;
//
//            final Quiz currentQuiz = (Quiz)getItem(position);
//
//            TextView titleTextView = viewQuiz.findViewById(R.id.quiz_title_text);
//
//            titleTextView.setText(currentQuiz.getTitle());
//            ((ImageView) viewQuiz.findViewById(R.id.imageViewQuiz)).setImageResource(
//                getResources().getIdentifier(currentQuiz.getImage(), "drawable", requireActivity().getPackageName())
//            );
//
//            viewQuiz.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intent = new Intent(requireActivity(), QuizActivity.class);
//                    intent.putExtra("QUIZ_EXTRA", currentQuiz);
//                    startActivity(intent);
//                    requireActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//                }
//            });
//
//            return viewQuiz;
//        }
//    }
}
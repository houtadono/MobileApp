package vn.id.houta.myapplication.module4;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import vn.id.houta.myapplication.R;
import vn.id.houta.myapplication.database.FirebaseHelper;
import vn.id.houta.myapplication.model.Quiz;

public class Module42Fragment extends Fragment {
    ListView listViewQuiz;
    ArrayList<Quiz> listQuiz;
    QuizListViewAdapter quizListViewAdapter;
    public Module42Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_module4_2, container, false);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
//        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        listQuiz = new ArrayList<>();
        listViewQuiz = view.findViewById(R.id.listViewQuiz);
        quizListViewAdapter = new QuizListViewAdapter(getActivity(), listQuiz);

        new FirebaseHelper().getQuizs(quiz -> {
            if (quiz != null) {
                listQuiz.add(quiz);
                System.out.println("add pre quiz " + quiz.getQuizId());
                quizListViewAdapter.notifyDataSetChanged();
            }
        });

        return view;
    }
    class QuizListViewAdapter extends BaseAdapter {

        private  ArrayList<Quiz> listQuiz;

        public QuizListViewAdapter(Context context, ArrayList<Quiz> listQuiz) {
            this.listQuiz = listQuiz;
        }

        @Override
        public int getCount() {
            return this.listQuiz.size();
        }

        @Override
        public Object getItem(int position) {
            return this.listQuiz.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View viewQuiz;
            if (convertView == null) {
                viewQuiz = View.inflate(parent.getContext(), R.layout.quiz_card_view, null);
            } else viewQuiz = convertView;

            final Quiz currentQuiz = (Quiz)getItem(position);

            TextView titleTextView = viewQuiz.findViewById(R.id.quiz_title_text);

            titleTextView.setText(currentQuiz.getTitle());
            ((ImageView) viewQuiz.findViewById(R.id.imageViewQuiz)).setImageResource(
                getResources().getIdentifier(currentQuiz.getImage(), "drawable", requireActivity().getPackageName())
            );

            viewQuiz.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

            return viewQuiz;
        }
    }
}
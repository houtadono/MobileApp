package vn.id.houta.myapplication.module4;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.android.material.card.MaterialCardView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import vn.id.houta.myapplication.R;
import vn.id.houta.myapplication.database.FirebaseHelper;
import vn.id.houta.myapplication.model.Ranking;

public class Module43Fragment extends Fragment {
    private ArrayList<Ranking> listRanking;
    private Ranking currentRanking;
    private  ListView listViewRank;
    private int count_stt = 0;
    private  View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_module4_3, container, false);
//        init();
        return view;
    }
    public void init(){
        count_stt = 0;
        listRanking = new ArrayList<>();
        listViewRank = view.findViewById(R.id.listViewRank);
        RankingListViewAdapter rankingListViewAdapter = new RankingListViewAdapter(requireContext(), listRanking);
        new FirebaseHelper().getRankings(ranking -> {
            if(ranking != null){
                count_stt += 1;
                if(count_stt == 1){
                    new FirebaseHelper().loadAvatarFromEmail(requireContext(), view.findViewById(R.id.imageViewTop1), ranking.getId());
                }else if(count_stt == 2){
                    new FirebaseHelper().loadAvatarFromEmail(requireContext(), view.findViewById(R.id.imageViewTop2), ranking.getId());
                }else if(count_stt == 3){
                    new FirebaseHelper().loadAvatarFromEmail(requireContext(), view.findViewById(R.id.imageViewTop3), ranking.getId());
                }
                listRanking.add(ranking);
                listViewRank.setAdapter(rankingListViewAdapter);
                rankingListViewAdapter.notifyDataSetChanged();
                if(ranking.getName().equals("Bạn")){
                    CardView card_item_rank_you = view.findViewById(R.id.card_item_rank_you);
                    ((TextView)card_item_rank_you.findViewById(R.id.textViewSttUserRank)).setText(String.valueOf(count_stt));
                    new FirebaseHelper().loadAvatarFromEmail(requireContext(), card_item_rank_you.findViewById(R.id.imageViewAvatarUserRank), ranking.getId());
                    ((TextView)card_item_rank_you.findViewById(R.id.textViewScoreUserRank)).setText("Điểm số: " + new DecimalFormat("#,###").format(ranking.getScore()));
                    int timeUserRank = (int) ranking.getTime();
                    ((TextView)card_item_rank_you.findViewById(R.id.textViewTimeUserRank)).setText(String.format("%02d:%02d", timeUserRank/60, timeUserRank%60));
                    card_item_rank_you.setOnClickListener(v -> {
                        listViewRank.smoothScrollToPosition(count_stt);
                    });
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        init();
    }

    class RankingListViewAdapter extends BaseAdapter {
        ArrayList<Ranking> listRanking;

        public RankingListViewAdapter(Context context, ArrayList<Ranking> listRanking) {
            this.listRanking = listRanking;
        }

        @Override
        public int getCount() {
            //Cần trả về số phần tử mà ListView hiện thị
            return this.listRanking.size();
        }

        @Override
        public Object getItem(int position) {
            return this.listRanking.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @SuppressLint("DefaultLocale") @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View viewRankUser;
            if (convertView == null) {
                viewRankUser = View.inflate(parent.getContext(), R.layout.item_ranking, null);
            } else viewRankUser = convertView;

            final Ranking ranking = (Ranking) getItem(position);
            System.out.println("view: "+ ranking.getId());
            ((TextView)viewRankUser.findViewById(R.id.textViewSttUserRank)).setText(String.valueOf(position+1));
            new FirebaseHelper().loadAvatarFromEmail(requireContext(), viewRankUser.findViewById(R.id.imageViewAvatarUserRank), ranking.getId());

            ((TextView)viewRankUser.findViewById(R.id.textViewNameUserRank)).setText(ranking.getName());
            ((TextView)viewRankUser.findViewById(R.id.textViewScoreUserRank)).setText(new DecimalFormat("#,###").format(ranking.getScore()));
            long timeUserRank = ranking.getTime();
            ((TextView)viewRankUser.findViewById(R.id.textViewTimeUserRank)).setText(
                    String.format("%02d:%02d", timeUserRank/60, timeUserRank%60));
            MaterialCardView card_item_rank = viewRankUser.findViewById(R.id.card_item_rank);
            if(position == 0){
                card_item_rank.setStrokeColor(requireContext().getColor(R.color.blue_cyan1));
            }else if (position==1) {
                card_item_rank.setStrokeColor(requireContext().getColor(R.color.blue_cyan1));
            }else if (position==2) {
                card_item_rank.setStrokeColor(requireContext().getColor(R.color.blue_cyan1));
            }
            if(ranking.getName().equals("Bạn"))
                card_item_rank.setStrokeColor(requireContext().getColor(R.color.orange));

            return viewRankUser;
        }
    }
}
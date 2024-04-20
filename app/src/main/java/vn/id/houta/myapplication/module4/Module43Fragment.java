package vn.id.houta.myapplication.module4;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;

import vn.id.houta.myapplication.R;
import vn.id.houta.myapplication.database.FirebaseHelper;
import vn.id.houta.myapplication.model.Ranking;

public class Module43Fragment extends Fragment {
    private ArrayList<Ranking> listRanking;
    private Ranking currentRanking;
    private  ListView listViewRank;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_module4_3, container, false);
        listRanking = new ArrayList<>();
        listViewRank = view.findViewById(R.id.listViewRank);
        RankingListViewAdapter rankingListViewAdapter = new RankingListViewAdapter(requireContext(), listRanking);
        new FirebaseHelper().getRankingsModule4(new FirebaseHelper.GetRankCallback(){

            @Override
            public void onGetRankUserCallback(Ranking ranking) {
                if(ranking != null){
                    listRanking.add(ranking);
                    listViewRank.setAdapter(rankingListViewAdapter);
                    rankingListViewAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onGetCurrentRankCallback(Ranking ranking) {
                currentRanking = ranking;
            }
        });

        listViewRank.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // Không cần xử lý trong trạng thái scrollState
                System.out.println("Hmm: " + scrollState + " ");
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(firstVisibleItem > 0) {

                }
                System.out.println("ON scoll " + firstVisibleItem + " " + visibleItemCount + " ");
            }
        });

        Glide.with(requireContext()).load("https://th.bing.com/th/id/OIP.Zkz6fuH31wOe-dsfqkBQkQAAAA?w=233&h=183&c=7&r=0&o=5&dpr=1.3&pid=1.7")
                .circleCrop().into((ImageView) view.findViewById(R.id.imageViewTop1));
        Glide.with(requireContext()).load("https://th.bing.com/th/id/OIP.Zkz6fuH31wOe-dsfqkBQkQAAAA?w=233&h=183&c=7&r=0&o=5&dpr=1.3&pid=1.7")
                .circleCrop().into((ImageView) view.findViewById(R.id.imageViewTop2));
        Glide.with(requireContext()).load("https://th.bing.com/th/id/OIP.Zkz6fuH31wOe-dsfqkBQkQAAAA?w=233&h=183&c=7&r=0&o=5&dpr=1.3&pid=1.7")
                .circleCrop().into((ImageView) view.findViewById(R.id.imageViewTop3));

//        NestedScrollView nestedScrollView = view.findViewById(R.id.nestedScrollView);
//        CardView cView = view.findViewById(R.id.cardViewRank);
//
//        nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
////                int imageViewHeight = imageView.getHeight();
////                int halfImageViewHeight = imageViewHeight / 2;
//            int halfImageViewHeight = 200;
//            if (scrollY >= halfImageViewHeight) {
//                int listViewScrollPosition = scrollY - halfImageViewHeight;
//                listViewRank.setSelectionFromTop(listViewScrollPosition, 0);
////                cView.smoothScrollToPositionFromTop(0, listViewScrollPosition);
//            }
//        });


//        initLineChart(view);
        return view;
    }


    private void initLineChart(View view) {
        LineChart lineChart = view.findViewById(R.id.chart);

        ArrayList<Entry> lineEntries = getDataSet();
        LineDataSet lineDataSet = new LineDataSet(lineEntries, "Work");
        lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        lineDataSet.setDrawHighlightIndicators(true);
        lineDataSet.setHighlightEnabled(true);
        lineDataSet.setHighLightColor(Color.RED);

        lineDataSet.setDrawValues(false);
//        lineDataSet.setValueTextSize(12);
//        lineDataSet.setValueTextColor(Color.WHITE);

        lineDataSet.setLineWidth(2f);
        lineDataSet.setColor(Color.GRAY);

        lineDataSet.setCircleColor(Color.WHITE);
        lineDataSet.setCircleRadius(10f);

//        int holeColor = ContextCompat.getColor(requireContext(), R.color.blue_cyan1);
        lineDataSet.setCircleHoleColor(Color.CYAN);
        lineDataSet.setCircleHoleRadius(8f);

        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        LineData lineData = new LineData(lineDataSet);
        lineChart.animateX(500);
        lineChart.setData(lineData);

        // Setup X Axis
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularityEnabled(true);
        xAxis.setLabelCount(7); // 7 loại là 7 thứ trong tuần
        xAxis.setAxisMinimum(0);
        xAxis.setAxisMaximum(6); // 7 loại là từ 0 đến 6
        xAxis.setTextSize(16f);
        xAxis.setTextColor(Color.GRAY);
        xAxis.setYOffset(20f);
        xAxis.setValueFormatter(new ValueFormatter() {
            private final String[] daysOfWeek = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                int intValue = (int) value;
                if (intValue >= 0 && intValue < daysOfWeek.length) {
                    return daysOfWeek[intValue];
                }
                return "";
            }
        });

        // Setup Y Axis
        YAxis yAxis = lineChart.getAxisLeft();
        yAxis.setAxisMinimum(0);
        yAxis.setAxisMaximum(10);
        yAxis.setGranularity(1f);
        yAxis.setTextSize(16f);
        yAxis.setDrawAxisLine(false);
        yAxis.setTextColor(Color.GRAY);
        yAxis.setXOffset(20f);

        lineChart.getAxisLeft().setCenterAxisLabels(false);
        lineChart.getAxisRight().setEnabled(false);
        lineChart.getXAxis().setDrawGridLines(false);
        lineChart.setPinchZoom(false); // Tắt tính năng thu phóng
        lineChart.setDoubleTapToZoomEnabled(false); // Tắt tính năng zoom bằng double tap
        lineChart.setScaleEnabled(false);
        lineChart.setDrawGridBackground(false);
        lineChart.setGridBackgroundColor(Color.LTGRAY);
        lineChart.getAxisLeft().setSpaceMin(0.5f);
        lineChart.invalidate();
    }


    private ArrayList<Entry> getDataSet() {
        ArrayList<Entry> entries = new ArrayList<>();
        // Tạo dữ liệu giả cho 7 ngày trong tuần, từ chủ nhật đến thứ bảy
        for (int i = 0; i < 7; i++) {
            // Giả sử số giờ là ngẫu nhiên từ 0 đến 10
            float hours = (float) (Math.random() * 10);
            entries.add(new Entry(i, hours));
        }
        return entries;
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
            ((TextView)viewRankUser.findViewById(R.id.textViewSttUserRank)).setText(String.valueOf(position));
            Glide.with(requireContext()).load("https://th.bing.com/th/id/OIP.Zkz6fuH31wOe-dsfqkBQkQAAAA?w=233&h=183&c=7&r=0&o=5&dpr=1.3&pid=1.7")
                    .circleCrop().into((ImageView) viewRankUser.findViewById(R.id.imageViewAvatarUserRank));
            ((TextView)viewRankUser.findViewById(R.id.textViewNameUserRank)).setText(ranking.getName());
            ((TextView)viewRankUser.findViewById(R.id.textViewScoreUserRank)).setText(String.format("%d",ranking.getScore()));
            long timeUserRank = ranking.getTime();
            ((TextView)viewRankUser.findViewById(R.id.textViewTimeUserRank)).setText(
                    String.format("%02d:%02d", timeUserRank/60, timeUserRank%60));
            return viewRankUser;
        }
    }
}
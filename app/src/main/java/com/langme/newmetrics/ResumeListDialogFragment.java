package com.langme.newmetrics;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.langme.newmetrics.dummy.DetailContent;
import com.langme.newmetrics.dummy.ResumeItem;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>A fragment that shows a list of items as a modal bottom sheet.</p>
 * <p>You can show this modal bottom sheet from your activity like this:</p>
 * <pre>
 *     ResumeListDialogFragment.newInstance(30).show(getSupportFragmentManager(), "dialog");
 * </pre>
 * <p>You activity (or fragment) needs to implement {@link ResumeListDialogFragment.Listener}.</p>
 */
public class ResumeListDialogFragment extends BottomSheetDialogFragment {
    private static final String ARG_ITEM_COUNT = "item_count";
    private Listener mListener;
    private PieChart pieChart;

    public static ResumeListDialogFragment newInstance(int itemCount) {
        final ResumeListDialogFragment fragment = new ResumeListDialogFragment();
        final Bundle args = new Bundle();
        args.putInt(ARG_ITEM_COUNT, itemCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_resume_list_dialog, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        final LinearLayout recyclerView = (LinearLayout) view;
        //recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        final List<DetailContent.DetailItem> itemList = (List<DetailContent.DetailItem>)getArguments().getSerializable("resume");

        //recyclerView.setAdapter(new ResumeAdapter(itemList, entries, pieEntryLabels));
        //super(recyclerView.inflate(R.layout.fragment_resume_list_dialog_item, parent, false));
        pieChart = (PieChart) recyclerView.findViewById(R.id.chart1);
        displayGeneral(itemList);
        Button btnGeneral = (Button) recyclerView.findViewById(R.id.general);
        btnGeneral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayGeneral(itemList);
            }
        });
        Button btnRetail = (Button) recyclerView.findViewById(R.id.retail);
        btnRetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayRetail(itemList);
            }
        });
        Button btnSi = (Button) recyclerView.findViewById(R.id.si);
        btnSi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displaySi(itemList);
            }
        });
    }

    private void displayGeneral(List<DetailContent.DetailItem> itemList){
        ArrayList<Entry> entries = new ArrayList<>();
        ArrayList<String> pieEntryLabels = new ArrayList<>();

        pieEntryLabels.add("Conforme");
        pieEntryLabels.add("Non Conforme");

        entries.add(new BarEntry(Utils.computeConforme(itemList), 0));
        entries.add(new BarEntry(Utils.computeNoComforme(itemList), 1));

        PieDataSet pieDataSet = new PieDataSet(entries, "");
        PieData pieData = new PieData(pieEntryLabels, pieDataSet);
        //pieData.setValueFormatter(new PercentFormatter());
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        //pieChart.setUsePercentValues(true);
        pieChart.setData(pieData);
        pieChart.animateY(3000);
    }

    private void displayRetail(List<DetailContent.DetailItem> itemList){
        ArrayList<Entry> entries = new ArrayList<>();
        ArrayList<String> pieEntryLabels = new ArrayList<>();

        float nbBloquante = Utils.computeBloquante(Constantes.type.RETAIL, itemList)*100;
        float nbMajeur = Utils.computeMajor(Constantes.type.RETAIL, itemList)*100;
        float nbMineur = Utils.computeMinor(Constantes.type.RETAIL, itemList)*100;
        float nbProjet = Utils.computeProject(Constantes.type.RETAIL, itemList)*100;
        float nbSupport = Utils.computeSupport(Constantes.type.RETAIL, itemList)*100;

        pieEntryLabels.add("IR01 Bloquantes");
        entries.add(new BarEntry(nbBloquante, 0));
        pieEntryLabels.add("IR01 Majeures");
        entries.add(new BarEntry(nbMajeur, 1));
        pieEntryLabels.add("IR02 Mineures");
        entries.add(new BarEntry(nbMineur, 2));
        pieEntryLabels.add("IR03 Projets");
        entries.add(new BarEntry(nbProjet, 3));
        pieEntryLabels.add("IR04 Support");
        entries.add(new BarEntry(nbSupport, 4));

        PieDataSet pieDataSet = new PieDataSet(entries, "");
        PieData pieData = new PieData(pieEntryLabels, pieDataSet);
        //pieData.setValueFormatter(new PercentFormatter());
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        //pieChart.setUsePercentValues(true);
        pieChart.setData(pieData);
        pieChart.animateY(3000);
    }

    private void displaySi(List<DetailContent.DetailItem> itemList){
        ArrayList<Entry> entries = new ArrayList<>();
        ArrayList<String> pieEntryLabels = new ArrayList<>();

        float nbBloquante = Utils.computeBloquante(Constantes.type.SITI, itemList)*100;
        float nbMajeur = Utils.computeMajor(Constantes.type.SITI, itemList)*100;
        float nbMineur = Utils.computeMinor(Constantes.type.SITI, itemList)*100;
        float nbProjet = Utils.computeProject(Constantes.type.SITI, itemList)*100;
        float nbSupport = Utils.computeSupport(Constantes.type.SITI, itemList)*100;

        pieEntryLabels.add("IR01 Bloquantes");
        entries.add(new BarEntry(nbBloquante, 0));
        pieEntryLabels.add("IR01 Majeures");
        entries.add(new BarEntry(nbMajeur, 1));
        pieEntryLabels.add("IR02 Mineures");
        entries.add(new BarEntry(nbMineur, 2));
        pieEntryLabels.add("IR03 Projets");
        entries.add(new BarEntry(nbProjet, 3));
        pieEntryLabels.add("IR04 Support");
        entries.add(new BarEntry(nbSupport, 4));

        PieDataSet pieDataSet = new PieDataSet(entries, "");
        PieData pieData = new PieData(pieEntryLabels, pieDataSet);
        //pieData.setValueFormatter(new PercentFormatter());
        //pieChart.setUsePercentValues(true);
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieChart.setData(pieData);
        pieChart.animateY(3000);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        final Fragment parent = getParentFragment();
        if (parent != null) {
            mListener = (Listener) parent;
        } else {
            mListener = (Listener) context;
        }
    }

    @Override
    public void onDetach() {
        mListener = null;
        super.onDetach();
    }

    public interface Listener {
        void onResumeClicked(int position);
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        final TextView textName;
        final TextView textNb;
        final PieChart pieChart ;

        ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            // TODO: Customize the item layout
            super(inflater.inflate(R.layout.fragment_resume_list_dialog_item, parent, false));
            pieChart = (PieChart) itemView.findViewById(R.id.chart1);
            textName = (TextView) itemView.findViewById(R.id.itemTxt);
            textNb = (TextView) itemView.findViewById(R.id.itemNb);
            textName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onResumeClicked(getAdapterPosition());
                        dismiss();
                    }
                }
            });
        }
    }

    private class ResumeAdapter extends RecyclerView.Adapter<ViewHolder> {

        private final List<ResumeItem> list;
        ArrayList<Entry> entries;
        ArrayList<String> pieEntryLabels ;
        PieDataSet pieDataSet ;
        PieData pieData ;

        ResumeAdapter(List<ResumeItem> list, ArrayList<Entry> entries, ArrayList<String> PieEntryLabels) {
            this.list = list;
            this.entries = entries;
            this.pieEntryLabels = PieEntryLabels;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.textName.setText(this.list.get(position).getName());
            holder.textNb.setText(String.valueOf(this.list.get(position).getNb()));

            entries = new ArrayList<>();
            pieEntryLabels = new ArrayList<String>();
            pieDataSet = new PieDataSet(entries, "");
            pieData = new PieData(pieEntryLabels, pieDataSet);
            pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
            holder.pieChart.setData(pieData);
            holder.pieChart.animateY(3000);
        }

        @Override
        public int getItemCount() {
            return this.list.size();
        }
    }
}

package com.langme.newmetrics;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.langme.newmetrics.DAO.TaskDAO;
import com.langme.newmetrics.dummy.DetailContent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link IndicatorDetailSheetFragment.OnFragmentInteractionListener} interface
 * create an instance of this fragment.
 */
public class IndicatorDetailSheetFragment extends Fragment {
    private static final String TAG = "Indicator";
    private DetailContent.DetailItem item;
    private OnFragmentInteractionListener mListener;
    private TaskDAO taskDAO;

    public IndicatorDetailSheetFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            taskDAO = new TaskDAO(getContext());
            item = (DetailContent.DetailItem) getArguments().getSerializable("item");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((MainActivity) getActivity()).hideFloatingActionButton();
        // Inflate the layout for this fragment
        View  v = inflater.inflate(R.layout.fragment_indicator_detail_sheet, container, false);
        TextView nameTask = (TextView) v.findViewById(R.id.nameTask);
        nameTask.setText(item.nameTask);

        //HAUT
        TextView state = (TextView) v.findViewById(R.id.state);
        state.setText(item.state);

        TextView critere = (TextView) v.findViewById(R.id.critere);
        critere.setText(item.criticity);

        TextView typlogy = (TextView) v.findViewById(R.id.typlogy);
        typlogy.setText(item.typology);

        TextView info = (TextView) v.findViewById(R.id.info);
        info.setText(item.infSystem);

        TextView ecartDate = (TextView) v.findViewById(R.id.ecartDate);
        ecartDate.setText(item.ecartDate);

        // BAS
        SimpleDateFormat dateformat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
        try {
            TextView dateCree = (TextView) v.findViewById(R.id.dateCree);
            dateCree.setText(dateformat.parse(item.createDate).toString());
        } catch (ParseException ex){
            Log.e(TAG, "onCreateView: " + ex);
        }

        try {
            TextView deliverDate = (TextView) v.findViewById(R.id.deliverDate);
            deliverDate.setText(dateformat.parse(item.deliverDate).toString());
        } catch (ParseException ex){
            Log.e(TAG, "onCreateView: " + ex);
        }

        try {
            TextView echeanceDate = (TextView) v.findViewById(R.id.echeanceDate);
            echeanceDate.setText(dateformat.parse(item.withDateTask).toString());
        } catch (ParseException ex){
            Log.e(TAG, "onCreateView: " + ex);
        }

        final TextView res = (TextView) v.findViewById(R.id.res);
        res.setText(item.finalState.toString());

        Button btnConforme = (Button) v.findViewById(R.id.btnConforme);
        btnConforme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                item.setFinalState(Constantes.State.CONFORME);
                res.setText(item.finalState.toString());
                res.setTextColor(getActivity().getApplicationContext().getResources().getColor(android.R.color.holo_green_light));
                taskDAO.updateTask(item);
            }
        });

        Button btnNoConforme = (Button) v.findViewById(R.id.btnNoConforme);
        btnNoConforme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                item.setFinalState(Constantes.State.NONCONFORME);
                res.setText(item.finalState.toString());
                res.setTextColor(getActivity().getApplicationContext().getResources().getColor(android.R.color.holo_red_light));
                taskDAO.updateTask(item);
            }
        });
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

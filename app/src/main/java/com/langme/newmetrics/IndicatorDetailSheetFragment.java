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

import com.langme.newmetrics.dummy.DetailContent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link IndicatorDetailSheetFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link IndicatorDetailSheetFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IndicatorDetailSheetFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "Indicator";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private DetailContent.DetailItem item;
    private OnFragmentInteractionListener mListener;

    public IndicatorDetailSheetFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment IndicatorDetailSheetFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static IndicatorDetailSheetFragment newInstance(String param1, String param2) {
        IndicatorDetailSheetFragment fragment = new IndicatorDetailSheetFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            item = (DetailContent.DetailItem)getArguments().getSerializable("item");
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
            }
        });

        Button btnNoConforme = (Button) v.findViewById(R.id.btnNoConforme);
        btnNoConforme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                item.setFinalState(Constantes.State.NONCONFORME);
                res.setText(item.finalState.toString());
                res.setTextColor(getActivity().getApplicationContext().getResources().getColor(android.R.color.holo_red_light));
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

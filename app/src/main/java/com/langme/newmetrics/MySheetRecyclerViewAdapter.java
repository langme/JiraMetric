package com.langme.newmetrics;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.langme.newmetrics.SheetFragment.OnListFragmentInteractionListener;
import com.langme.newmetrics.dummy.SheetContent;
import com.langme.newmetrics.dummy.SheetContent.SheetItem;

import java.util.List;

import static com.langme.newmetrics.Constantes.MyPREFERENCES;

/**
 * {@link RecyclerView.Adapter} that can display a {@link SheetItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MySheetRecyclerViewAdapter extends RecyclerView.Adapter<MySheetRecyclerViewAdapter.ViewHolder> {

    private final List<SheetContent.SheetItem> mValues;
    private final OnListFragmentInteractionListener mListener;
    private Context context;
    public MySheetRecyclerViewAdapter(List<SheetContent.SheetItem> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_sheet, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).id);
        holder.mNameView.setText(mValues.get(position).name);
        holder.mDateView.setText(mValues.get(position).date);
        holder.mContentView.setImageResource(android.R.drawable.ic_menu_search);;

        if (position %2 == 0) {
            // NON CONFORME
            holder.mRow.setBackgroundColor(context.getResources().getColor(R.color.holo_light_control_normal));
        } else {
            // CONFORME
            holder.mRow.setBackgroundColor(context.getResources().getColor(R.color.holo_light_button_pressed));
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final LinearLayout mRow;
        public final TextView mIdView;
        public final TextView mNameView;
        public final ImageView mContentView;
        public SheetContent.SheetItem mItem;
        public final TextView mDateView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mRow = (LinearLayout) view.findViewById(R.id.rowSheet);
            mIdView = (TextView) view.findViewById(R.id.id);
            mNameView = (TextView) view.findViewById(R.id.idName);
            mDateView = (TextView) view.findViewById(R.id.idDate);
            mContentView = (ImageView) view.findViewById(R.id.search);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mIdView.getText() + "'";
        }
    }

    /**
     * A simple {@link Fragment} subclass.
     * Activities that contain this fragment must implement the
     * {@link OnFragmentInteractionListener} interface
     * to handle interaction events.
     * Use the {@link SettingsFragment#newInstance} factory method to
     * create an instance of this fragment.
     */
    public static class SettingsFragment extends Fragment {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private static final String ARG_PARAM1 = "param1";
        private static final String ARG_PARAM2 = "param2";
        private static final String TAG = "SettingsFragment";
        private SharedPreferences sharedpreferences;
        private EditText name;
        private EditText state;
        private EditText critic;
        private EditText create;
        private EditText typo;
        private EditText wish;
        private EditText valide;
        private EditText info;
        private EditText cloture;

        // TODO: Rename and change types of parameters
        private String mParam1;
        private String mParam2;
        private View view;
        private OnFragmentInteractionListener mListener;

        public SettingsFragment() {
            // Required empty public constructor
        }

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SettingsFragment.
         */
        // TODO: Rename and change types and number of parameters
        public static SettingsFragment newInstance(String param1, String param2) {
            SettingsFragment fragment = new SettingsFragment();
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
                mParam1 = getArguments().getString(ARG_PARAM1);
                mParam2 = getArguments().getString(ARG_PARAM2);
            }
            sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            View view = inflater.inflate(R.layout.fragment_settings, container, false);
            name = (EditText)view.findViewById(R.id.edtNameTask);
            state = (EditText)view.findViewById(R.id.edtState);
            critic = (EditText)view.findViewById(R.id.edtCriticity);
            create = (EditText)view.findViewById(R.id.edtCreateTask);
            typo = (EditText)view.findViewById(R.id.edtTypology);
            wish = (EditText)view.findViewById(R.id.edtWishDateTask);
            valide = (EditText)view.findViewById(R.id.edtValidatedTask);
            info = (EditText)view.findViewById(R.id.edtInfSystem);
            cloture = (EditText)view.findViewById(R.id.edtColureTask);
            return view;
        }

        @Override
        public void onStart() {
            super.onStart();

            if (Utils.CheckSharesPref(sharedpreferences)) {
                name.setText(sharedpreferences.getString("name", "").toUpperCase());
                state.setText(sharedpreferences.getString("state", "").toUpperCase());
                critic.setText(sharedpreferences.getString("critic", "").toUpperCase());
                create.setText(sharedpreferences.getString("create", "").toUpperCase());
                typo.setText(sharedpreferences.getString("typo", "").toUpperCase());
                wish.setText(sharedpreferences.getString("wish", "").toUpperCase());
                valide.setText(sharedpreferences.getString("valide", "").toUpperCase());
                info.setText(sharedpreferences.getString("info", "").toUpperCase());
                cloture.setText(sharedpreferences.getString("cloture", "").toUpperCase());
            }
        }

        @Override
        public void onResume() {
            super.onResume();
            if(getView() == null){
                return;
            }

            getView().setFocusableInTouchMode(true);
            getView().requestFocus();
            getView().setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
                        // handle back button's click listener
                        Log.d(TAG, "l'utilisateur veut quitter le profil");
                        if (!checkConfiguration()) {
                            showDialogQuit(true);
                        } else{
                            //return chat
                            getActivity().onBackPressed();
                        }
                        return true;
                    }
                    return false;
                }
            });
        }

        // TODO: Rename method, update argument and hook method into UI event
        public void onButtonPressed(Uri uri) {
            if (mListener != null) {
                mListener.onFragmentInteraction(uri);
            }
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

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            super.onCreateOptionsMenu(menu, inflater);
            menu.clear();
            inflater.inflate(R.menu.save_conf, menu);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();

            //noinspection SimplifiableIfStatement
            if (id == R.id.save) {
                if (checkConfiguration()){
                    // retourner au back stack
                    getFragmentManager().popBackStack();
                }
            }

            return true;
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

        private boolean checkConfiguration(){
            View view = getView();
            if (!name.getText().toString().isEmpty()
            && !state.getText().toString().isEmpty()
            && !critic.getText().toString().isEmpty()
            && !create.getText().toString().isEmpty()
            && !typo.getText().toString().isEmpty()
            && !wish.getText().toString().isEmpty()
            && !valide.getText().toString().isEmpty()
            && !info.getText().toString().isEmpty()
            && !cloture.getText().toString().isEmpty()
            ){
                Toast.makeText(getContext(), "La configuration a été enregistrée...", Toast.LENGTH_SHORT).show();
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("name", name.getText().toString());
                editor.putString("state", state.getText().toString());
                editor.putString("critic", critic.getText().toString());
                editor.putString("create", create.getText().toString());
                editor.putString("typo", typo.getText().toString());
                editor.putString("wish", wish.getText().toString());
                editor.putString("valide", valide.getText().toString());
                editor.putString("info", info.getText().toString());
                editor.putString("cloture", cloture.getText().toString());
                editor.commit();
                return true;
            } else {
                Toast.makeText(getContext(), "La configuration n'est pas complète...", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        /**
         * Afficher la boite de dialogue Sauvegarder ces donnes
         * @param backbutton
         */
        private void showDialogQuit(final boolean backbutton){
            AlertDialog dialogue = new AlertDialog.Builder(getActivity())
                    .setTitle("Etes vous sûr de bien vouloir sauvegarder les modifications ?")
                    .setPositiveButton("Sauvegarder",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    // save informations
                                    if(checkConfiguration()){
                                        if (backbutton){
                                            Log.d(TAG, "Back button... ");
                                            getActivity().onBackPressed();
                                        }
                                    }
                                }
                            }
                    )
                    .setNegativeButton("Annuler",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    dialog.dismiss();
                                }
                            }
                    ).create();
            dialogue.show();
        }
    }
}

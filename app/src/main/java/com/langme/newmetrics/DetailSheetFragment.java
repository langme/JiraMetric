package com.langme.newmetrics;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.inputmethodservice.Keyboard;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.langme.newmetrics.dummy.DetailContent;
import com.langme.newmetrics.dummy.DetailContent.DetailItem;
import com.langme.newmetrics.dummy.SheetContent;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.format.CellDateFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.ExcelStyleDateFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import static com.langme.newmetrics.Constantes.MyPREFERENCES;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class DetailSheetFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_PATH= "filePath";
    private static final String TAG= "DetailSheetFragment";
    // TODO: Customize parameters
    private String mFilePath;
    private ReadExcelFile task;
    private OnListFragmentInteractionListener mListener;
    public RecyclerView recyclerView;
    public MyDetailSheetRecyclerViewAdapter mAdapter;
    private File excelFile;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public DetailSheetFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static DetailSheetFragment newInstance(SheetContent.SheetItem item) {
        DetailSheetFragment fragment = new DetailSheetFragment();
       /* Bundle args = new Bundle();
        args.putInt(ARG_PATH, columnCount);
        fragment.setArguments(args);*/
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            SheetContent.SheetItem item = (SheetContent.SheetItem)getArguments().getSerializable("item");
            mFilePath = item.path;
            if (!mFilePath.isEmpty()) {
                File file = new File(mFilePath);
                task = new ReadExcelFile(file);
                task.execute();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detailsheet_list, container, false);
        ((MainActivity) getActivity()).showFloatingActionButton();
        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            //recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            mAdapter = new MyDetailSheetRecyclerViewAdapter(DetailContent.ITEMS, mListener);
            recyclerView.setAdapter(mAdapter);
        }
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.detail_sheet, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.share) {
            if (generateExcel(DetailContent.ITEMS)){
                if (excelFile != null) {
                    final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                    emailIntent.setType("application/excel");
                    emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{});
                    emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, new String("Document Jira généré"));
                    emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Voici le document généré....");
                    emailIntent.putExtra(android.content.Intent.EXTRA_STREAM, Uri.parse("file://" + excelFile.getAbsoluteFile())); //"file://"+getFilesDir()+"/"+fileName)
                    this.startActivity(Intent.createChooser(emailIntent, "Envoi mail..."));
                } else {
                    Toast.makeText(getContext(), "Problème à la génération du fichier...", Toast.LENGTH_SHORT);
                }
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(DetailContent.DetailItem item);
    }

    public class ReadExcelFile extends AsyncTask {
        protected ProgressDialog progressDialog;
        private File file;

        public ReadExcelFile(File fichier) {
            file = fichier;
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                FileInputStream stream = new FileInputStream(file);
                XSSFWorkbook workbook = new XSSFWorkbook(stream);
                XSSFSheet sheet = workbook.getSheetAt(0);
                int rowsCount = sheet.getPhysicalNumberOfRows();
                FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
                int finishTask = 1;
                SharedPreferences sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                int nameId = Utils.findAlphaIndex(sharedpreferences.getString("name", "").toLowerCase());
                int stateId = Utils.findAlphaIndex(sharedpreferences.getString("state", "").toLowerCase());
                int criticId = Utils.findAlphaIndex(sharedpreferences.getString("critic", "").toLowerCase());
                int createId = Utils.findAlphaIndex(sharedpreferences.getString("create", "").toLowerCase());
                int typoId = Utils.findAlphaIndex(sharedpreferences.getString("typo", "").toLowerCase());
                int wishId = Utils.findAlphaIndex(sharedpreferences.getString("wish", "").toLowerCase());
                int valideId = Utils.findAlphaIndex(sharedpreferences.getString("valide", "").toLowerCase());
                int infoId = Utils.findAlphaIndex(sharedpreferences.getString("info", "").toLowerCase());
                int clotureId = Utils.findAlphaIndex(sharedpreferences.getString("cloture", "").toLowerCase());

                for (int r = 1; r<rowsCount; r++) {
                    Row row = sheet.getRow(r);
                    String nameTask = getCellAsString(row, nameId, formulaEvaluator);//B
                    String state = getCellAsString(row, stateId, formulaEvaluator); //C
                    String criticity = getCellAsString(row, criticId, formulaEvaluator); //D
                    String typology = getCellAsString(row, typoId, formulaEvaluator); //I
                    String createTask = getCellAsString(row, createId, formulaEvaluator);
                    String validatedTask = getCellAsString(row, valideId, formulaEvaluator);//L
                    String infSystem = getCellAsString(row, infoId, formulaEvaluator);//P
                    String clotureTask = getCellAsString(row, clotureId, formulaEvaluator); //R
                    String wishDateTask = getCellAsString(row, wishId, formulaEvaluator); //K

                    if (!createTask.isEmpty() && !validatedTask.isEmpty()){
                        Log.i(TAG, "doInBackground: ligne : " + r);
                        // TODO: 10/07/2017 AJOUTER DANS DETAILITER
                        //if (!DetailContent.ITEMS.isEmpty()) {
                            SimpleDateFormat dateformat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
                            try {
                                Date dateEnd = dateformat.parse(validatedTask);
                                Date dateBegin = dateformat.parse(createTask);
                                String ecart = String.valueOf(Utils.getWorkingDaysBetweenTwoDates(dateBegin, dateEnd));
                                DetailItem detailItem = new DetailItem(String.valueOf(finishTask), nameTask, createTask, validatedTask, ecart,
                                        state, typology, criticity, infSystem, clotureTask, wishDateTask);
                                detailItem.setFinalState(Utils.manageConformity(detailItem));
                                DetailContent.addItem(detailItem);
                                finishTask ++;
                            } catch (Exception ex) {
                                Log.e(TAG, "nameTask : " + nameTask + " err :" + ex);
                            }
                        /*} else {
                            Log.e(TAG, "doInBackground: DetailContent empty");
                        }*/
                    } else {
                        Log.i(TAG, "doInBackground: ligne : " + r + "vide");
                    }
                }
            } catch (Exception e) {
            /* proper exception handling to be here */
                Log.e(TAG, "onClick: " +e.toString());
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // TODO: 06/07/2017 tester la validiter du fichier
            progressDialog = ProgressDialog.show(getActivity(), "Traitement du Fichier Excel", "", true, false);
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            progressDialog.dismiss();
            mAdapter.notifyDataSetChanged();
        }
    }

    public static String getCellAsString(Row row, int c, FormulaEvaluator formulaEvaluator) {
        String value = "";
        try {
            Cell cell = row.getCell(c);
            CellValue cellValue = formulaEvaluator.evaluate(cell);

            if (cellValue != null) {
                switch (cellValue.getCellType()) {
                    case Cell.CELL_TYPE_BOOLEAN:
                        value = "" + cellValue.getBooleanValue();
                        break;
                    case Cell.CELL_TYPE_NUMERIC:
                        if (HSSFDateUtil.isCellDateFormatted(row.getCell(c))) {
                            value = row.getCell(c).getDateCellValue().toString();
                        } else {
                            value = "";
                        }
                        break;
                    case Cell.CELL_TYPE_STRING:
                        if (cellValue.getStringValue().contains("/")){
                            String changeDate = Utils.replaceMonth(cellValue.getStringValue());
                            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy hh:mm a", Locale.FRENCH);
                            try {
                                Date date = formatter.parse(changeDate);
                                Log.d(TAG, "getCellAsString: "+ date);
                                value = date.toString();
                            }catch (ParseException e) {
                                Log.e(TAG, "row : " + row.toString() +" error CELL_TYPE_STRING: " + e);
                            }
                        } else {
                            value = "" + cellValue.getStringValue();
                        }
                        break;
                    default:
                }
            } else {
                Log.d(TAG, "cell empty: ");
            }
        } catch (NullPointerException e) {
            /* proper error handling should be here */
            Log.e(TAG, "getCellAsString: row " + row + " col " + c + " error : " + e.toString());
        }
        return value;
    }

    private boolean generateExcel(List<DetailItem> list){
        SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy");
        String date = dateformat.format(new Date());
        String Fnamexls ="metricsJira";
        String ext = ".xls";
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Sample sheet");

        int rownum = 0;
        for (DetailItem item : list) {
            Row row = sheet.createRow(rownum++);
            Cell cellA = row.createCell(0);
            cellA.setCellValue((String)item.getNameTask());
            Cell cellB = row.createCell(1);
            cellB.setCellValue((int)(item.getFinalState() == Constantes.State.CONFORME ? 1 : 0));
        }

        excelFile = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + "/" + Fnamexls + "_" + date + "" + ext);
        FileOutputStream os = null;

        try {
            os = new FileOutputStream(excelFile);
            workbook.write(os);
            Log.w("FileUtils", "Writing file" + excelFile);
            return true;
        } catch (IOException e) {
            Log.w("FileUtils", "Error writing " + excelFile, e);
            return false;
        } catch (Exception e) {
            Log.w("FileUtils", "Failed to save file", e);
            return false;
        } finally {
            try {
                if (null != os)
                    os.close();
            } catch (Exception ex) {
                return false;
            }
        }
    }
}

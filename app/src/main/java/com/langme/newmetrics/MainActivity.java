package com.langme.newmetrics;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.facebook.stetho.Stetho;
import com.langme.newmetrics.DAO.DataBaseCollection;
import com.langme.newmetrics.DAO.FichierDAO;
import com.langme.newmetrics.DAO.TaskDAO;
import com.langme.newmetrics.dummy.DetailContent;
import com.langme.newmetrics.dummy.SheetContent;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.langme.newmetrics.Constantes.MyPREFERENCES;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        SheetFragment.OnListFragmentInteractionListener,
        DetailSheetFragment.OnListFragmentInteractionListener,
        IndicatorDetailSheetFragment.OnFragmentInteractionListener,
        ResumeListDialogFragment.Listener,
        MySheetRecyclerViewAdapter.SettingsFragment.OnFragmentInteractionListener{

    private static String TAG = "MainActivity";
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private static final int MY_PERMISSIONS_REQUEST = 1;
    private Fragment fragment;
    private FloatingActionButton fab;
    private DataBaseCollection    dataBaseCollection = null;
    private static final int REQUEST_CHOOSER = 1234;
    private FichierDAO fichierDAO;
    private ReadExcelFile task;
    private TaskDAO taskDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        try {
            Stetho.initializeWithDefaults(this);
        } catch (Exception e) {
            Log.e(TAG, "failed to initialize Stetho");
        }

        if (isStoragePermissionGranted()) {
            if (savedInstanceState == null) {
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragment = new SheetFragment();
                fragmentTransaction.add(R.id.fragmentLayout, fragment, "SHEET");
                fragmentTransaction.commit();
            }
        }

        dataBaseCollection = new DataBaseCollection(this);
        dataBaseCollection.getReadableDatabase();
        fichierDAO = new FichierDAO(getApplicationContext());
        taskDAO = new TaskDAO(getApplicationContext());

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Fragment f = getSupportFragmentManager().findFragmentById(R.id.listDetail);
                if (f instanceof DetailSheetFragment) {
                    // do something with f
                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }*/

                FragmentManager fragmentManager = MainActivity.this.getSupportFragmentManager();
                List<Fragment> fragments = fragmentManager.getFragments();
                if(fragments != null){
                    for(Fragment fragment : fragments){
                        if(fragment != null && fragment.isVisible())
                            if (fragment instanceof DetailSheetFragment) {
                                // do something with f
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("resume", (Serializable) DetailContent.getITEMS());

                                ResumeListDialogFragment bottomSheetDialogFragment = new ResumeListDialogFragment();
                                bottomSheetDialogFragment.setArguments(bundle);
                                bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
                            }
                    }
                }
            }
        });

        SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        if (!Utils.CheckSharesPref(sharedpreferences)){
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("name", "B"); //B
            editor.putString("state", "C"); //C
            editor.putString("critic", "D");//D
            editor.putString("create", "H");//H
            editor.putString("typo", "I");//I
            editor.putString("wish", "K");//K
            editor.putString("valide", "L");//L
            editor.putString("info", "P");//P
            editor.putString("cloture", "R");//R
            editor.commit();
        }
    }

    @Override
    public void onBackPressed() {
        int count = getFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            super.onBackPressed();
            //additional code
            FragmentManager fragmentManager = MainActivity.this.getSupportFragmentManager();
            List<Fragment> fragments = fragmentManager.getFragments();
            if(fragments != null){
                for(Fragment fra : fragments){
                    if(fra != null && fra.isVisible())
                        fragment = fra;
                }
            }
        } else {
            getFragmentManager().popBackStack();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Log.v(TAG, "R.id.action_settings");
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragment = new MySheetRecyclerViewAdapter.SettingsFragment();
            fragmentTransaction.replace(R.id.fragmentLayout, fragment, "SETTINGS_SHEET");
            fragmentTransaction.addToBackStack("SETTINGS_SHEET");
            fragmentTransaction.commit();
            return true;
        }

        if (id == R.id.action_import) {
            Log.v(TAG, "R.id.action_import");
            Intent getContentIntent = FileUtils.createGetContentIntent();
            Intent intent = Intent.createChooser(getContentIntent, "Select a file");
            startActivityForResult(intent, REQUEST_CHOOSER);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CHOOSER:
                if (resultCode == RESULT_OK) {
                    final Uri uri = data.getData();

                    // Get the File path from the Uri
                    //String path = FileUtils.getPath(this, uri);
                    Log.v(TAG, "REQUEST_CHOOSER : " + uri.getPath());
                    // Alternatively, use FileUtils.getFile(Context, Uri)
                    if (uri != null && FileUtils.isLocal(uri.getPath())) {
                        File file = new File(uri.getPath());
                        String nameFile;
                        long dateFile;
                        String pathFile;
                        if (file != null){
                            nameFile = file.getName();
                            dateFile = file.lastModified();
                            pathFile = file.getAbsolutePath();
                        } else {
                            nameFile = uri.getLastPathSegment();
                            dateFile = 0;
                            pathFile = uri.getPath();
                        }

                        Log.v(TAG, "REQUEST_CHOOSER : insertion en base ");
                        // insertion en base
                        String id = String.valueOf(fichierDAO.getRowsNumber() + 1);
                        final SheetContent.SheetItem sheet = new SheetContent.SheetItem(id, nameFile, String.valueOf(dateFile), pathFile);

                        if (!fichierDAO.isExistFile(nameFile)) {
                            fichierDAO.create(sheet);
                        } else {
                            fichierDAO.update(sheet);
                        }

                        if (!pathFile.isEmpty()) {
                            Log.v(TAG, "REQUEST_CHOOSER : read file " + pathFile);
                            task = new ReadExcelFile(uri, fragment.getContext(), new MyInterface(){
                                @Override
                                public void myMethod(boolean result) {
                                    if (!result) {
                                        // suppression en base du fichier + update
                                        Log.d(TAG, "myMethod f : " + fichierDAO.getID(sheet.getName()));
                                        Log.d(TAG, "myMethod: " + fichierDAO.delete(Integer.valueOf(fichierDAO.getID(sheet.getName()))));

                                        // // TODO: 02/08/2017 remplir la liste faire un update
                                        if (fragment instanceof SheetFragment) {
                                            Log.v(TAG, "REQUEST_CHOOSER : SheetFragment update ");
                                            SheetFragment.update();
                                        }
                                    }
                                }
                            });
                            task.execute();
                        }

                        // // TODO: 02/08/2017 remplir la liste faire un update
                        if (fragment instanceof SheetFragment) {
                            Log.v(TAG, "REQUEST_CHOOSER : SheetFragment update ");
                            SheetFragment.update();
                        }
                    }
                }
                break;
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "Permission Grantedt: ");
                    fragmentManager = getSupportFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragment = new SheetFragment();

                    fragmentTransaction.replace(R.id.fragmentLayout,fragment);
                    fragmentTransaction.commitAllowingStateLoss ();

                } else {
                    Log.d(TAG, "Permission Denied: ");
                    finish();
                }
        }
    }

    /**
     * Savoir si les permissions ont été accordées
     * @return
     */
    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED ) {
                Log.v(TAG,"Permission is granted");
                return true;
            } else {

                Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted");
            return true;
        }
    }

    @Override
    public void onListFragmentInteraction(SheetContent.SheetItem item, String action) {
        if (action.matches("search")) {
            Log.v(TAG, "search onList Interaction SheetContent: " + item.getName());

            SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            if (Utils.CheckSharesPref(sharedpreferences)) {
                Bundle bundle = new Bundle();
                String[] nameFile = item.getPath().split("/");
                Log.v(TAG, "search onList Interaction SheetContent: " + nameFile[nameFile.length - 1]);
                bundle.putString("item", nameFile[nameFile.length - 1]);

                // on replace le fragment par le fragment detail
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragment = new DetailSheetFragment();
                fragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.fragmentLayout, fragment, "SHEET");
                fragmentTransaction.addToBackStack("SHEET");
                fragmentTransaction.commit();
            } else {
                Toast.makeText(getBaseContext(), "La configuration n'est pas définie...", Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.v(TAG, "del onList Interaction SheetContent: " + item.getName());
            if (taskDAO.deleteAll(Integer.parseInt(item.getId()))) {
                Log.v(TAG, "del file " + item.getId());
                fichierDAO.delete(Integer.parseInt(item.getId()));
                Log.v(TAG, "del onList Interaction SheetContent : SheetFragment.update()");
                if (fragment instanceof SheetFragment) {
                    SheetFragment.update();
                }
            }
        }
    }

    @Override
    public void onListFragmentInteraction(DetailContent.DetailItem item) {
        Log.v(TAG, "search onList Interaction DetailContent: " + item.getNameTask());
        Bundle bundle = new Bundle();
        bundle.putSerializable("item", item);

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragment = new IndicatorDetailSheetFragment();
        fragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.fragmentLayout, fragment, "DETAIL_SHEET");
        fragmentTransaction.addToBackStack("DETAIL_SHEET");
        fragmentTransaction.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }

    @Override
    public void onResumeClicked(int position) {
    }

    public void showFloatingActionButton() {
        fab.show();
    }

    public void hideFloatingActionButton() {
        fab.hide();
    }

    public interface MyInterface {
        public void myMethod(boolean result);
    }

    public class ReadExcelFile extends AsyncTask<Void, Void, Boolean> {
        protected ProgressDialog progressDialog;
        private Uri uri;
        private Context mContext;
        private String erreur;
        private MyInterface mListener;

        public ReadExcelFile(Uri uriFile, Context context, MyInterface mListener) {
            this.uri = uriFile;
            this.mContext=context;
            this.mListener  = mListener;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            boolean res = true;
            try {
                Log.d(TAG, "doInBackground: " + this.uri.getPath());
                InputStream stream = getContentResolver().openInputStream(uri);
                XSSFWorkbook workbook = new XSSFWorkbook(stream);
                XSSFSheet sheet = workbook.getSheetAt(0);
                int rowsCount = sheet.getPhysicalNumberOfRows();
                FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
                int finishTask = 1;
                Log.d(TAG, "doInBackground: start sharedpreferences...");
                SharedPreferences sharedpreferences = getApplication().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                int nameId = Utils.findAlphaIndex(sharedpreferences.getString("name", "").toLowerCase());
                int stateId = Utils.findAlphaIndex(sharedpreferences.getString("state", "").toLowerCase());
                int criticId = Utils.findAlphaIndex(sharedpreferences.getString("critic", "").toLowerCase());
                int createId = Utils.findAlphaIndex(sharedpreferences.getString("create", "").toLowerCase());
                int typoId = Utils.findAlphaIndex(sharedpreferences.getString("typo", "").toLowerCase());
                int wishId = Utils.findAlphaIndex(sharedpreferences.getString("wish", "").toLowerCase());
                int valideId = Utils.findAlphaIndex(sharedpreferences.getString("valide", "").toLowerCase());
                int infoId = Utils.findAlphaIndex(sharedpreferences.getString("info", "").toLowerCase());
                int clotureId = Utils.findAlphaIndex(sharedpreferences.getString("cloture", "").toLowerCase());

                Log.d(TAG, "doInBackground: find sharedpreferences...");
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

                    Log.d(TAG, "doInBackground: find each cell for row : " + r);
                    if (!createTask.isEmpty() && !validatedTask.isEmpty()){
                        Log.d(TAG, "doInBackground: ligne validée : " + r);
                        SimpleDateFormat dateformat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
                        try {
                            // connaitre le nom du fchier
                            String[] pathFile = this.uri.getLastPathSegment().split("/");
                            int if_fichier = fichierDAO.getID(pathFile[pathFile.length-1]);
                            Date dateEnd = dateformat.parse(validatedTask);
                            Date dateBegin = dateformat.parse(createTask);
                            String ecart = String.valueOf(Utils.getWorkingDaysBetweenTwoDates(dateBegin, dateEnd));
                            DetailContent.DetailItem detailItem = new DetailContent.DetailItem(String.valueOf(finishTask), nameTask, createTask, validatedTask, ecart,
                                    state, typology, criticity, infSystem, clotureTask, wishDateTask, String.valueOf(if_fichier));
                            detailItem.setFinalState(Utils.manageConformity(detailItem));

                            String[] num = nameTask.split("-");
                            if (taskDAO.isExistTask(Integer.parseInt(num[1]), if_fichier)) {
                                taskDAO.updateTask(detailItem);
                            } else {
                                taskDAO.create(detailItem);
                            }

                            finishTask ++;
                        } catch (Exception ex) {
                            Log.e(TAG, "nameTask : " + nameTask + " err :" + ex);
                        }
                    } else {
                        Log.e(TAG, "doInBackground: ligne non validée : " + r);
                    }
                }
            } catch (Exception e) {
                res = false;
                this.erreur = e.toString();
                Log.e(TAG, "onClick: " +e.toString());
            }
            return res;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(this.mContext);
            progressDialog.setMessage("Traitement du Fichier Excel");
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            progressDialog.dismiss();
            Log.v(TAG, "onPostExecute: finish : ");
            if (mListener != null){
                mListener.myMethod(result);
            }
            if (!result){
                mListener.myMethod(result);
                Toast.makeText(this.mContext, "Erreur : "  + this.erreur, Toast.LENGTH_LONG).show();
            }
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
                                Log.v(TAG, "getCellAsString: "+ date);
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

}

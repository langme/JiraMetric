package com.langme.newmetrics.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.langme.newmetrics.Constantes;
import com.langme.newmetrics.Helper.Helper;
import com.langme.newmetrics.dummy.DetailContent;
import com.langme.newmetrics.dummy.SheetContent;

import java.util.ArrayList;
import java.util.List;

import static com.langme.newmetrics.DAO.FichierDAO.COLUMN_NAME_NAME;

/**
 * Created by melang on 26/07/2017.
 */

public class TaskDAO {
    public static final String TABLE_NAME = "Task";
    /**the id of the task */
    public static final String COLUMN_ID = "_id";
    /**the name of the task */
    public static final String COLUMN_NAME = "name";
    /** the creating date of the task */
    public static final String COLUMN_CREATE_DATE = "createDate";
    /** the delivering date of the task */
    public static final String COLUMN_DELIVER_DATE = "deliverDate";
    /** the gap date of the task */
    public static final String COLUMN_GAP_DATE = "ecartDate";
    /** the state of the task */
    public static final String COLUMN_STATE = "state";
    /** the criticity of the task */
    public static final String COLUMN_CRITICAL = "criticity";
    /** the typology of the task */
    public static final String COLUMN_TYPOLOGY = "typology";
    /** the infSystem of the task */
    public static final String COLUMN_INF_SYS = "infSystem";
    /** the cloturing date of the task */
    public static final String COLUMN_CLOTURE_DATE = "clotureTask";
    /** the wishing date of the task */
    public static final String COLUMN_WISH_DATE = "withDateTask";
    /** the final state of the task */
    public static final String COLUMN_FINAL_STATE = "finalState";
    /** the reference fichier of the task */
    public static final String COLUMN_NAME_REF_FILE = "ref_fichier";
    /** the reference fichier of the task */
    public static final String COLUMN_NUMBER = "number";

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (\n" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                    COLUMN_NAME + Helper.CHAR_TYPE + " DEFAULT (NULL),\n" +
                    COLUMN_CREATE_DATE + Helper.TEXT_TYPE + " DEFAULT (NULL),\n" +
                    COLUMN_DELIVER_DATE + Helper.TEXT_TYPE + " DEFAULT (NULL),\n" +
                    COLUMN_GAP_DATE + Helper.TEXT_TYPE + " DEFAULT (0),\n" +
                    COLUMN_STATE + Helper.TEXT_TYPE + " DEFAULT (NULL),\n" +
                    COLUMN_CRITICAL + Helper.TEXT_TYPE + " DEFAULT (NULL),\n" +
                    COLUMN_TYPOLOGY + Helper.TEXT_TYPE + " DEFAULT (NULL),\n" +
                    COLUMN_INF_SYS + Helper.TEXT_TYPE + " DEFAULT (NULL),\n" +
                    COLUMN_CLOTURE_DATE + Helper.TEXT_TYPE + " DEFAULT (NULL),\n" +
                    COLUMN_WISH_DATE + Helper.TEXT_TYPE + " DEFAULT (NULL),\n" +
                    COLUMN_FINAL_STATE + Helper.TEXT_TYPE + " DEFAULT (NULL),\n" +
                    COLUMN_NUMBER + Helper.INTEGER_TYPE + " DEFAULT (NULL),\n" +
                    COLUMN_NAME_REF_FILE + Helper.TEXT_TYPE + " ,\n" +
                    " FOREIGN KEY ("+ COLUMN_NAME_REF_FILE+") REFERENCES "+ TABLE_NAME +"("+ COLUMN_ID +"));";

    // Supprime l'historique du chat hormis la presentation
    public static final String SQL_DELETE_ENTRIES = "DELETE FROM " + TABLE_NAME + " WHERE "+ COLUMN_ID + " > 5";

    // Database fields
    private SQLiteDatabase database;
    private DataBaseCollection dbHelper;

    private static final String TAG = "TaskDAO";

    public TaskDAO(Context context) {
        dbHelper = new DataBaseCollection(context);
    }

    /**
     * open database connecion
     * @throws SQLException
     */
    private void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    private void close() {
        dbHelper.close();
    }

    /**
     * addDocument
     * Ajouter une tache en base
     * @param task  le document à ajouter à la base
     * @return
     */
    public long create(DetailContent.DetailItem task) {
        open();
        long newRowId = -1;
        try {
            ContentValues cv = new ContentValues();
            cv.put(COLUMN_NAME, task.getNameTask());
            cv.put(COLUMN_CREATE_DATE, task.getCreateDate());
            cv.put(COLUMN_DELIVER_DATE, task.getDeliverDate());
            cv.put(COLUMN_GAP_DATE, task.getEcartDate());
            cv.put(COLUMN_STATE, task.getState());
            cv.put(COLUMN_CRITICAL, task.getCriticity());
            cv.put(COLUMN_TYPOLOGY, task.getTypology());
            cv.put(COLUMN_INF_SYS, task.getInfSystem());
            cv.put(COLUMN_CLOTURE_DATE, task.getClotureTask());
            cv.put(COLUMN_WISH_DATE, task.getWithDateTask());
            cv.put(COLUMN_FINAL_STATE, task.getFinalState().toString());
            cv.put(COLUMN_NUMBER, task.getNumber());
            cv.put(COLUMN_NAME_REF_FILE, task.getRefFile());
            // Insert the new row, returning the primary key value of the new row
            newRowId = database.insert(TABLE_NAME, null, cv);
            Log.v(TAG, "saveItem row id : " + newRowId);
        } catch (Exception ex){
            Log.e(TAG, "create: " + ex);
        }
        close();
        return newRowId;
    }

    /**
     * Add a new person
     * @param task
     * @return the database id
     */
    public long updateTask(DetailContent.DetailItem task){
        long newRowId = 0;
        open();
        try {
            ContentValues cv = new ContentValues();
            cv.put(COLUMN_NAME, task.getNameTask());
            cv.put(COLUMN_CREATE_DATE, task.getCreateDate());
            cv.put(COLUMN_DELIVER_DATE, task.getDeliverDate());
            cv.put(COLUMN_GAP_DATE, task.getEcartDate());
            cv.put(COLUMN_STATE, task.getState());
            cv.put(COLUMN_CRITICAL, task.getCriticity());
            cv.put(COLUMN_TYPOLOGY, task.getTypology());
            cv.put(COLUMN_INF_SYS, task.getInfSystem());
            cv.put(COLUMN_CLOTURE_DATE, task.getClotureTask());
            cv.put(COLUMN_WISH_DATE, task.getWithDateTask());
            cv.put(COLUMN_FINAL_STATE, task.getFinalState().toString());
            cv.put(COLUMN_NUMBER, task.getNumber());
            cv.put(COLUMN_NAME_REF_FILE, task.getRefFile());
            newRowId = database.update(TABLE_NAME, cv, COLUMN_NUMBER + "=" + task.getNumber() + " AND " + COLUMN_NAME_REF_FILE + "=" + task.getRefFile(), null);
        } catch (Exception ex){
            Log.e(TAG, "Exception : " + ex);
        }
        close();
        return newRowId;
    }

    /**
     * On récupère tous les documents
     * @return
     */
    public List<DetailContent.DetailItem> findAll(){
        open();
        try {
            String table = TABLE_NAME;
            String where = null;
            String orderBy = COLUMN_NUMBER + " ASC";
            Cursor cursor = database.query(table, null, where, null, null, null, orderBy);
            if (cursor.getCount() == 0) {
                //addItem(new DummyItem("0", "Item ", null));
            } else {

                try {
                    while (cursor.moveToNext()) {
                        // extraction des valeurs depuis le curseur
                        DetailContent.DetailItem item = DetailContent.createDummyItem(cursor);
                        DetailContent.addItem(item);
                    }
                } finally {
                    cursor.close();
                }
            }
        } catch (Exception ex){
            Log.e(TAG, "findAll : " + ex);
        }
        close();
        return DetailContent.ITEMS;
    }

    /**
     * Permet de selectionner une tache selon son numero si elle existe
     * @param number
     * @return  boolean
     */
    public boolean isExistTask(int number, int refFile){
        boolean res = false;
        open();
        try {
            String table = TABLE_NAME;
            String where = COLUMN_NUMBER + "=" + String.valueOf(number) + " AND " + COLUMN_NAME_REF_FILE + "=" + refFile;

            Cursor cr = database.query(table, null, where, null, null, null, null);
            if (cr != null){
                if(cr.getCount() > 0) {
                    res = true;
                }
            }
        } catch (Exception ex){
            Log.e(TAG, "isExistTask: " + ex);
        }
        close();
        return res;
    }

    public List<DetailContent.DetailItem> findAllTask(int id_file){
        DetailContent.ITEMS.clear();
        open();
        try{
            String table = TABLE_NAME;
            String where = COLUMN_NAME_REF_FILE + "=" + id_file;
            String orderBy = COLUMN_NUMBER + " ASC";

            Cursor cursor = database.query(table, null, where, null, null, null, orderBy);
            if (cursor.getCount() != 0) {
                try {
                    while (cursor.moveToNext()) {
                        // extraction des valeurs depuis le curseur
                        DetailContent.DetailItem item = DetailContent.createDummyItem(cursor);
                        DetailContent.addItem(item);
                    }
                } finally {
                    cursor.close();
                }
            }
        } catch (Exception ex){
            Log.e(TAG, "findAllTask: " + ex);
        }
        close();
        return DetailContent.ITEMS;
    }

    /**
     * Supprime le document
     * @param id_document    l'id du document
     * @return  le nombre de lignes supprimées
     */
    public boolean deleteAll(int id_document) {
        boolean res  = true;
        open();
        int deletedRows = -1;
        try {
            deletedRows = database.delete(TABLE_NAME, COLUMN_NAME_REF_FILE + "=" + id_document, null);
            if (deletedRows == -1){
                res = false;
            }
        } catch (Exception ex){
            Log.e(TAG, "delete: " + ex);
            res = false;
        }
        close();
        return res;
    }
}

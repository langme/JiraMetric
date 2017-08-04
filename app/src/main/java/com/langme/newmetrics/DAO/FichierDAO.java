package com.langme.newmetrics.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.langme.newmetrics.Helper.Helper;
import com.langme.newmetrics.dummy.DetailContent;
import com.langme.newmetrics.dummy.SheetContent;

import java.security.spec.ECField;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by melang on 26/07/2017.
 */

public class FichierDAO {
    public static final String TABLE_NAME = "Fichier";
    public static final String COLUMN_ID = "_id";
    /**the name of the document */
    public static final String COLUMN_NAME_NAME = "name";
    /** the backup date */
    public static final String COLUMN_NAME_DATE = "date";
    /** the backup date */
    public static final String COLUMN_PATH = "path";

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (\n" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                    COLUMN_NAME_NAME + Helper.TEXT_TYPE + " NOT NULL,\n" +
                    COLUMN_PATH + Helper.TEXT_TYPE + " NOT NULL,\n" +
                    COLUMN_NAME_DATE + Helper.TEXT_TYPE + ");\n";

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + TABLE_NAME;
    // Database fields
    private SQLiteDatabase database;
    private DataBaseCollection dbHelper;

    private static final String TAG ="FichierDAO";

    public FichierDAO(Context context) {
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
     * Ajoute le document en base
     * @param document  le document à ajouter à la base
     * @return
     */
    public long create(SheetContent.SheetItem document) {
        open();
        long newRowId = -1;
        try {
            ContentValues cv = new ContentValues();
            cv.put(COLUMN_NAME_NAME, document.getName());
            cv.put(COLUMN_NAME_DATE, document.getDate());
            cv.put(COLUMN_PATH, document.getPath());
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
     * Permet de savoir si un fichier existe
     * @param fileName
     * @return  boolean
     */
    public boolean isExistFile(String fileName){
        boolean res = false;
        open();
        try {
            String selectQuery = "SELECT * FROM " + TABLE_NAME +" WHERE " + COLUMN_NAME_NAME + " =?";
            Cursor c = database.rawQuery(selectQuery, new String[] { fileName });
            if (c.moveToFirst()) {
                res = true;
            }
        } catch (Exception ex){
            Log.e(TAG, "isExistTask: " + ex);
        }
        close();
        return res;
    }

    /**
     * Add a new person
     * @param document
     * @return the database id
     */
    public long update(SheetContent.SheetItem document){
        long newRowId = 0;
        open();
        try {
            ContentValues cv = new ContentValues();
            cv.put(COLUMN_NAME_NAME, document.getName());
            cv.put(COLUMN_NAME_DATE, document.getDate());
            cv.put(COLUMN_PATH, document.getPath());
            newRowId = database.update(TABLE_NAME, cv, COLUMN_NAME_NAME + "=?", null);
        } catch (Exception ex){
            Log.e(TAG, "Exception : " + ex);
        }
        close();
        return newRowId;
    }

    /**
     * Supprime le document
     * @param id    l'id du document à supprimer
     * @return  le nombre de lignes supprimées
     */
    public int delete(int id) {
        open();
        int deletedRows = -1;
        try {
            deletedRows = database.delete(TABLE_NAME, COLUMN_ID + "=" + id, null);
        } catch (Exception ex){
            Log.e(TAG, "delete: " + ex);
        }
        close();
        return deletedRows;
    }

    /**
     * getDocumentCount
     *  Returns the numbers of rows in the Document table.
     * @return
     */
    public int getRowsNumber(){
        open();
        int nbr = 0;
        try {
            Cursor cr = database.query(TABLE_NAME, null, null, null, null, null, null);
            if(cr!=null){
                nbr = cr.getCount();
            }
            Log.v(TAG, "Nombre d'éléments trouvées dans la table Document : " + nbr);
            cr.close();
        } catch (Exception ex){
            Log.e(TAG, "getRowsNumber: " + ex);
        }

        close();
        return nbr;
    }

    public List<SheetContent.SheetItem> getFile(){
        open();
        try {
            String table = TABLE_NAME;
            String where = null;
            String orderBy = COLUMN_ID + " ASC";
            Cursor cursor = database.query(table, null, where, null, null, null, orderBy);
            if (cursor.getCount() == 0) {
                //addItem(new DummyItem("0", "Item ", null));
            } else {

                try {
                    while (cursor.moveToNext()) {
                        // extraction des valeurs depuis le curseur
                        SheetContent.SheetItem item = SheetContent.createDummyItem(cursor);
                        SheetContent.addItem(item);
                    }
                } finally {
                    cursor.close();
                }
            }
        } catch (Exception ex){
            Log.e(TAG, "getFile: " + ex);
        }
        close();
        return SheetContent.ITEMS;
    }

    /**
     * Selectionne l'id du fichier en fonction du nom
     * @param name  le nom de la categorie
     * @return
     */
    public int getID(String name){
        open();
        int ret = -1;
        try {
            Cursor cr = database.rawQuery("select _id from " + TABLE_NAME + " where " + COLUMN_NAME_NAME + "  = ?", new String[]{name});
            if (cr.moveToFirst()) {
                ret = cr.getInt(0);
            }
            cr.close();
        } catch (Exception ex){
            Log.e(TAG, "getID: " + ex);
        }
        close();
        return ret;
    }
}

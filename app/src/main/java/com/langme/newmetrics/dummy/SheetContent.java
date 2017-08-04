package com.langme.newmetrics.dummy;

import android.database.Cursor;
import android.provider.BaseColumns;

import com.langme.newmetrics.DAO.FichierDAO;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class SheetContent {

    /**
     * An array of sample (dummy) items.
     */
    public static List<SheetItem> ITEMS = new ArrayList<SheetItem>();
    private static String TAG = "SheetContent";
    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, SheetItem> ITEM_MAP = new HashMap<String, SheetItem>();

    private static final int COUNT = 25;

    static {
        /*File directory = new File((Environment.getExternalStorageDirectory() + "/" + Environment.DIRECTORY_DOWNLOADS));
        File[] files = directory.listFiles();
        int i = 0;
        for (File file: files) {
            if (file.getName().contains(".xlsx")){
                SheetItem item = new SheetItem(String.valueOf(i), file.getName() , file.getPath());
                addItem(item);
                Log.d(TAG, "create item sheet: " + item);
                i++;
            }
        }*/
    }

    public static void addItem(SheetItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    /**
     * Creer un DummyItem à partir du curseur
     * @param cursor
     * @return
     */
    public static SheetItem createDummyItem(Cursor cursor) {
        long id = cursor.getLong(cursor.getColumnIndex(BaseColumns._ID));
        String name = cursor.getString(cursor.getColumnIndex(FichierDAO.COLUMN_NAME_NAME));
        String date = cursor.getString(cursor.getColumnIndex(FichierDAO.COLUMN_NAME_DATE));
        String path = cursor.getString(cursor.getColumnIndex(FichierDAO.COLUMN_PATH));
        return new SheetItem(String.valueOf(id), name, date, path);
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class SheetItem implements Serializable {
        public final String id;
        public final String name;
        public final String date;
        public final String path;

        public SheetItem(String id, String name, String date, String path) {
            this.id = id;
            this.name = name;
            this.path = path;
            //SimpleDateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy");
            //Date d = new Date(file.lastModified());
            //this.date = dateformat.format(d);
            this.date = date;
        }

        public String getId() {
            return id;
        }
        public String getName() {
            return name;
        }
        public String getDate() {
            return date;
        }
        public String getPath() {
            return path;
        }
    }
}

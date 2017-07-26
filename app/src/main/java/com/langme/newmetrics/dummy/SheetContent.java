package com.langme.newmetrics.dummy;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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
    public static final List<SheetItem> ITEMS = new ArrayList<SheetItem>();
    private static String TAG = "SheetContent";
    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, SheetItem> ITEM_MAP = new HashMap<String, SheetItem>();

    private static final int COUNT = 25;

    static {
        File directory = new File((Environment.getExternalStorageDirectory() + "/" + Environment.DIRECTORY_DOWNLOADS));
        File[] files = directory.listFiles();
        int i = 0;
        for (File file: files) {
            if (file.getName().contains(".xlsx")){
                SheetItem item = new SheetItem(String.valueOf(i), file.getName() , file.getPath());
                addItem(item);
                Log.d(TAG, "create item sheet: " + item);
                i++;
            }
        }
    }

    private static void addItem(SheetItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static SheetItem createSheetItem(int position) {
        return new SheetItem(String.valueOf(position), "Item " + position, makeDetails(position));
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
        public final String path;
        public final String date;

        public SheetItem(String id, String name, String path) {
            this.id = id;
            this.name = name;
            this.path = path;
            File file = new File(path);
            SimpleDateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy");
            Date d = new Date(file.lastModified());
            this.date = dateformat.format(d);
        }

        @Override
        public String toString() {
            return path;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getPath() {
            return path;
        }
    }
}

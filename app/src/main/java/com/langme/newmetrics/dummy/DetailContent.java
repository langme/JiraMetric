package com.langme.newmetrics.dummy;

import android.database.Cursor;
import android.provider.BaseColumns;

import com.langme.newmetrics.Constantes;
import com.langme.newmetrics.DAO.FichierDAO;
import com.langme.newmetrics.DAO.TaskDAO;
import com.langme.newmetrics.Utils;

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
public class DetailContent {

    public static List<DetailItem> getITEMS() {
        return ITEMS;
    }

    /**
     * An array of sample (dummy) items.
     */
    public static List<DetailItem> ITEMS = new ArrayList<DetailItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, DetailItem> ITEM_MAP = new HashMap<String, DetailItem>();

    static {
        // Add some sample items.
        for (int i = 1; i <= 3; i++) {
            //addItem(createDummyItem(i));
        }
    }

    /**
     * Creer un DummyItem à partir du curseur
     * @param cursor
     * @return
     */
    public static DetailContent.DetailItem createDummyItem(Cursor cursor) {
        long id = cursor.getLong(cursor.getColumnIndex(BaseColumns._ID));
        String name = cursor.getString(cursor.getColumnIndex(TaskDAO.COLUMN_NAME));
        String creatDate = cursor.getString(cursor.getColumnIndex(TaskDAO.COLUMN_CREATE_DATE));
        String delivDate = cursor.getString(cursor.getColumnIndex(TaskDAO.COLUMN_DELIVER_DATE));
        String gapDeate = cursor.getString(cursor.getColumnIndex(TaskDAO.COLUMN_GAP_DATE));
        String state = cursor.getString(cursor.getColumnIndex(TaskDAO.COLUMN_STATE));
        String critic = cursor.getString(cursor.getColumnIndex(TaskDAO.COLUMN_CRITICAL));
        String typo = cursor.getString(cursor.getColumnIndex(TaskDAO.COLUMN_TYPOLOGY));
        String infSys = cursor.getString(cursor.getColumnIndex(TaskDAO.COLUMN_INF_SYS));
        String clotureDate = cursor.getString(cursor.getColumnIndex(TaskDAO.COLUMN_CLOTURE_DATE));
        String wishDate = cursor.getString(cursor.getColumnIndex(TaskDAO.COLUMN_WISH_DATE));
        String finalState = cursor.getString(cursor.getColumnIndex(TaskDAO.COLUMN_FINAL_STATE));
        int refFile = cursor.getInt(cursor.getColumnIndex(TaskDAO.COLUMN_NAME_REF_FILE));

        return new DetailContent.DetailItem(String.valueOf(id), name, creatDate, delivDate, gapDeate,
                state, typo, critic , infSys, clotureDate, wishDate, finalState, String.valueOf(refFile));
    }

    public static void addItem(DetailItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
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
    public static class DetailItem implements Serializable {
        public final String id;
        public final String nameTask;
        public final String createDate;
        public final String deliverDate;
        public final String ecartDate;
        public final String state;
        public final String criticity;
        public final String typology;
        public final String infSystem;
        public final String clotureTask;
        public final String withDateTask;
        public Constantes.State finalState;
        public final long number;
        public final String refFile;

        public DetailItem(String id, String name, String create, String deliver, String ecart,
                            String etat, String typologie, String cricicite, String info, String cloture, String wish, String finalState, String refFile) {
            this.id = id;
            this.nameTask = name;
            this.createDate = create;
            this.deliverDate = deliver;
            this.ecartDate = ecart;
            this.state = etat;
            this.criticity = cricicite;
            this.typology = typologie;
            this.infSystem = info;
            this.clotureTask = cloture;
            this.withDateTask = wish;
            if (finalState.matches(String.valueOf(Constantes.State.CONFORME))) {
                this.finalState = Constantes.State.CONFORME;
            } else{
                this.finalState = Constantes.State.NONCONFORME;
            }
            String[] num = name.split("-");
            this.number = Integer.parseInt(num[1]);
            this.refFile = refFile;
        }

        public DetailItem(String id, String name, String create, String deliver, String ecart,
                          String etat, String typologie, String cricicite, String info, String cloture, String wish, String refFile) {
            this.id = id;
            this.nameTask = name;
            this.createDate = create;
            this.deliverDate = deliver;
            this.ecartDate = ecart;
            this.state = etat;
            this.criticity = cricicite;
            this.typology = typologie;
            this.infSystem = info;
            this.clotureTask = cloture;
            this.withDateTask = wish;
            String[] num = name.split("-");
            this.number = Integer.parseInt(num[1]);
            this.refFile = refFile;
        }

        @Override
        public String toString() {
            return id;
        }

        public String getId() {
            return id;
        }

        public String getNameTask() {
            return nameTask;
        }

        public String getCreateDate() {
            return createDate;
        }

        public String getDeliverDate() {
            return deliverDate;
        }

        public String getEcartDate() {
            return ecartDate;
        }

        public String getState() {
            return state;
        }

        public String getCriticity() {
            return criticity;
        }

        public String getTypology() {
            return typology;
        }

        public String getInfSystem() {
            return infSystem;
        }

        public String getClotureTask() {
            return clotureTask;
        }

        public String getWithDateTask() {
            return withDateTask;
        }

        public Constantes.State getFinalState() {
            return finalState;
        }

        public void setFinalState(Constantes.State finalState) {
            this.finalState = finalState;
        }

        public long getNumber() {
            return number;
        }

        public String getRefFile() {
            return refFile;
        }
    }
}

package com.langme.newmetrics.dummy;

import com.langme.newmetrics.Constantes;
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
    public static final List<DetailItem> ITEMS = new ArrayList<DetailItem>();

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

        public DetailItem(String id, String name, String create, String deliver, String ecart,
                            String etat, String typologie, String cricicite, String info, String cloture, String wish) {
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
    }
}

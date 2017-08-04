package com.langme.newmetrics;

/**
 * Created by melang on 10/07/2017.
 */

import android.content.SharedPreferences;
import android.util.Log;

import com.langme.newmetrics.dummy.DetailContent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by melang on 06/07/2017.
 */

public class Utils {
    private static String TAG = "Utils";
    private static String alphabet = "abcdefghijklmnopqrstuvwxyz";

    public static String changeFormatDate(String date){
        String newDate = "";
        if (!date.isEmpty()) {
            // savoir si la date contient des lettres
            if (date.matches(".*[a-z].*")) {
                // si la chaine contient un .
                if (date.contains(".")) {
                    Pattern p = Pattern.compile("(\\d{2}\\/[a-z-é]+.\\/\\d{2})|(([0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9])|([A-Z]{2})");
                    Matcher m = p.matcher(date);
                    boolean b = m.matches();

                    if (b) {
                        // pour chaque groupe
                        for (int i = 0; i <= m.groupCount(); i++) {
                            // affichage de la sous-chaîne capturée
                            Log.i(TAG, "Groupe " + i + " : " + m.group(i));
                        }
                    }


                    // alors la chaine contient un mois ecrit en lettre
                    String[] dateArray = date.split("/");
                    switch (dateArray[1]) {
                        case "jan":
                            break;
                        case "févr":
                            break;
                        case "mar":
                            break;
                        case "avr":
                            break;
                        case "mai":
                            break;
                        case "jui":
                            // attention Pour juin et juillet
                            break;
                        case "aou":
                            break;
                        case "set":
                            break;
                        case "oct":
                            break;
                        case "nov":
                            break;
                        case "déc":
                            break;
                        default:
                            break;
                    }
                }
            } else {
                Pattern p = Pattern.compile("(\\d{2}\\/\\d{2}\\/\\d{2})|(([0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9])|([A-Z]{2})");
                Matcher m = p.matcher(date);
                boolean b = m.matches();

                if (b) {
                    // pour chaque groupe
                    for (int i = 0; i <= m.groupCount(); i++) {
                        // affichage de la sous-chaîne capturée
                        Log.i(TAG, "Groupe " + i + " : " + m.group(i));
                    }
                }
            }
        } else {
            Log.d(TAG, "changeFormatDate: date vide");
        }
        return newDate;
    }

    /**
     * Dans le fichier Excel il y a des mois ecrit en dur
     * @param date
     * @return
     */
    public static String replaceMonth(String date){
        String newDate = "";
        String[] dateArray = date.split("/");
        switch (dateArray[1]) {
            case "janv.":
                newDate = date.replace(dateArray[1],"01");
                break;
            case "févr.":
                newDate = date.replace(dateArray[1],"02");
                break;
            case "mar.":
                newDate = date.replace(dateArray[1],"03");
                break;
            case "avr.":
                newDate = date.replace(dateArray[1],"04");
                break;
            case "mai.":
                newDate = date.replace(dateArray[1],"05");
                break;
            case "jui.":
                newDate = date.replace(dateArray[1],"06");
                break;
            case "juil.":
                newDate = date.replace(dateArray[1],"07");
                break;
            case "aou.":
                newDate = date.replace(dateArray[1],"08");
                break;
            case "sépt.":
                newDate = date.replace(dateArray[1],"09");
                break;
            case "oct.":
                newDate = date.replace(dateArray[1],"10");
                break;
            case "nov.":
                newDate = date.replace(dateArray[1],"11");
                break;
            case "déc.":
                newDate = date.replace(dateArray[1],"12");
                break;
            default:
                break;
        }

        return newDate;
    }

    /**
     * Calcul l'ecart entre 2 dates
     * @param startDate
     * @param endDate
     * @return
     */
    public static long computeDifference(Date startDate, Date endDate){
        //millisecond
        long different = endDate.getTime() - startDate.getTime();

        System.out.println("startDate : " + startDate);
        System.out.println("endDate : "+ endDate);
        System.out.println("different : " + different);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;
        return elapsedDays;
    }

    /**
     * permet de savoir si l'echance du traitement de la tâche a été respectée
     * @param finishDate
     * @param wishDate
     * @return
     */
    public static boolean computeRespectDate(Date finishDate, Date wishDate){
        //millisecond

        int dayFinish =  finishDate.getDate();
        int monthFinish =  finishDate.getMonth();
        int yearFinish =  finishDate.getYear();

        int dayWish = wishDate.getDate();
        int monthWish = wishDate.getMonth();
        int yearWish = wishDate.getYear();

        if (yearWish == yearFinish){
            // true
            if (monthWish >= monthFinish){
                if (dayWish >= dayFinish){
                    return true;
                }
            }
        } else if (yearWish > yearFinish){
            return true;
        }

        return false;
    }


    /**
     * Calcul l'ecart entre 2 dates en comptant uniquement les jours ouvrés
     * @param startDate
     * @param endDate
     * @return
     */
    public static int getWorkingDaysBetweenTwoDates(Date startDate, Date endDate) {

        Calendar startCal = Calendar.getInstance();
        startCal.setTime(startDate);

        Calendar endCal = Calendar.getInstance();
        endCal.setTime(endDate);

        int workDays = 0;

        //Return 0 if start and end are the same
        if (startCal.getTimeInMillis() == endCal.getTimeInMillis()) {
            return 0;
        }

        if (startCal.getTimeInMillis() > endCal.getTimeInMillis()) {
            startCal.setTime(endDate);
            endCal.setTime(startDate);
        }

        List<String> listDayOff = listDayOff(startCal);
        SimpleDateFormat format1 = new SimpleDateFormat("dd/MM");

        do {
            //excluding start date
            startCal.add(Calendar.DAY_OF_MONTH, 1);
            if (startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                String maybDayoff = format1.format(startCal.getTime());
                boolean noDayOff = true;
                for (String day : listDayOff) {
                    if (maybDayoff.matches(day)){
                        Log.d(TAG, "getWorkingDaysBetweenTwoDates: Jour ferie : " + maybDayoff );
                        noDayOff = false;
                        break;
                    }
                }

                if (noDayOff){
                    ++workDays;
                }

            }
        } while (startCal.getTimeInMillis() < endCal.getTimeInMillis()); //excluding end date

        return workDays-1;
    }

    private static List<String> listDayOff(Calendar p_date){
        List<String> list = new ArrayList<>();
        list.add("01/01");
        list.add("01/05");
        list.add("08/05");
        list.add("14/07");
        list.add("15/08");
        list.add("11/11");
        list.add("25/12");

        // Calcul du jour de pâques (algorithme de Oudin (1940))
        // Calcul du nombre d'or - 1
        final Calendar jourFerie = (Calendar) p_date.clone();
        final int intGoldNumber = p_date.get(Calendar.YEAR) % 19;
        // Année divisé par cent
        final int intAnneeDiv100 = (int) (p_date.get(Calendar.YEAR)
                / 100);
        // intEpacte est = 23 - Epacte (modulo 30)
        final int intEpacte = (intAnneeDiv100 - intAnneeDiv100 / 4
                - (8 * intAnneeDiv100 + 13) / 25
                + (19 * intGoldNumber) + 15) % 30;
        // Le nombre de jours à partir du 21 mars
        // pour atteindre la pleine lune Pascale
        final int intDaysEquinoxeToMoonFull = intEpacte - (intEpacte / 28)
                * (1 - (intEpacte / 28) * (29 / (intEpacte + 1))
                * ((21 - intGoldNumber) / 11));
        // Jour de la semaine pour la pleine lune Pascale (0=dimanche)
        final int intWeekDayMoonFull = (p_date.get(Calendar.YEAR)
                + p_date.get(Calendar.YEAR) / 4
                + intDaysEquinoxeToMoonFull + 2 - intAnneeDiv100
                + intAnneeDiv100 / 4) % 7;
        // Nombre de jours du 21 mars jusqu'au dimanche de ou
        // avant la pleine lune Pascale (un nombre entre -6 et 28)
        final int intDaysEquinoxeBeforeFullMoon =
                intDaysEquinoxeToMoonFull - intWeekDayMoonFull;
        // mois de pâques
        final int intMonthPaques = 3
                + (intDaysEquinoxeBeforeFullMoon + 40) / 44;
        // jour de pâques
        final int intDayPaques = intDaysEquinoxeBeforeFullMoon + 28
                - 31 * (intMonthPaques / 4);
        // lundi de pâques
        SimpleDateFormat format1 = new SimpleDateFormat("dd/MM");
        jourFerie.set(p_date.get(Calendar.YEAR), intMonthPaques - 1, intDayPaques + 1);
        final Calendar lundiDePaque = (Calendar) jourFerie.clone();
        list.add(format1.format(lundiDePaque.getTime()));
        // Ascension
        final Calendar ascension = (Calendar) lundiDePaque.clone();
        ascension.add(Calendar.DATE, 38);
        list.add(format1.format(ascension.getTime()));
        //Pentecote
        final Calendar lundiPentecote = (Calendar) lundiDePaque.clone();
        lundiPentecote.add(Calendar.DATE, 48);
        list.add(format1.format(lundiPentecote.getTime()));
        // lundi de pentecote

        return list;
    }

    /**
     * permet de determiner si une tache est conforme ou non
     * @param item
     * @return
     */
    public static Constantes.State manageConformity(DetailContent.DetailItem item){
        Constantes.State res = Constantes.State.NONCONFORME;

        // 1ere phase d'analyse
        if (item.typology.matches("Execution requete") && item.infSystem.matches("SITI")){
            if (!item.ecartDate.isEmpty()) {
                if (item.criticity.matches("Minor")) {
                    if (Integer.parseInt(item.ecartDate) <= 5) {
                        res = Constantes.State.CONFORME;
                    }
                } else if (item.criticity.matches("Major")) {
                    if (Integer.parseInt(item.ecartDate) <= 2) {
                        res = Constantes.State.CONFORME;
                    }
                } else {
                    if (Integer.parseInt(item.ecartDate) <= 1) {
                        res = Constantes.State.CONFORME;
                    }
                }
            }
        } else if (item.typology.matches("Demande intervention")
                || item.typology.matches("Information")
                || item.typology.matches("Assistance BI Query")
                || item.typology.matches("Administrateur User BO")
                || (item.typology.matches("Execution requete") && item.infSystem.matches("SI_Nantes"))){
            if (!item.ecartDate.isEmpty()) {
                if (Integer.parseInt(item.ecartDate) <= 1) {
                    res = Constantes.State.CONFORME;
                }
            }
        } else {
            if (!item.withDateTask.isEmpty() && !item.deliverDate.isEmpty()) {
                SimpleDateFormat dateformat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
                try {
                    Date dateEnd = dateformat.parse(item.withDateTask);
                    Date dateBegin = dateformat.parse(item.deliverDate);
                    boolean respect = computeRespectDate(dateBegin, dateEnd);
                    if (respect) {
                        res = Constantes.State.CONFORME;
                    }
                } catch (Exception ex){
                    Log.e(TAG, "manageConformity date : " + ex);
                }
            } else {
                Log.v(TAG, "manageConformity item vide pour withDateTask or deliverDate");
                res = Constantes.State.CONFORME;
            }
        }

        return res;
    }

    /**
     * permet de trouver l'index dans une chaine de caractere
     * @param c
     * @return
     */
    public static int findAlphaIndex(String c){
        int res = alphabet.indexOf(c);
        return res;
    }

    /**
     * Permet de verifier la configuration enregstree dans les SharedPreferences
     * @param sharedpreferences
     * @return
     */
    public static boolean CheckSharesPref(SharedPreferences sharedpreferences){
        boolean res = true;
        if (sharedpreferences.getString("name", "").isEmpty() ||
        sharedpreferences.getString("state", "").isEmpty() ||
        sharedpreferences.getString("critic", "").isEmpty() ||
        sharedpreferences.getString("create", "").isEmpty() ||
        sharedpreferences.getString("typo", "").isEmpty() ||
        sharedpreferences.getString("wish", "").isEmpty() ||
        sharedpreferences.getString("valide", "").isEmpty() ||
        sharedpreferences.getString("info", "").isEmpty() ||
        sharedpreferences.getString("cloture", "").isEmpty()){
            res = false;
        }

        return res;
    }

    /**
     * calcul le nombre de tache conforme dans la liste
     * @param itemList
     * @return
     */
    public static float computeConforme(List<DetailContent.DetailItem> itemList){
        float res = 0;
        for (DetailContent.DetailItem it : itemList) {
            if (it.getFinalState() == Constantes.State.CONFORME){
                res++;
            }
        }
        return res;
    }

    /**
     * calcul le nombre de tache non conforme dans la liste
     * @param itemList
     * @return
     */
    public static float computeNoComforme(List<DetailContent.DetailItem> itemList){
        float res = 0;
        for (DetailContent.DetailItem it : itemList) {
            if (it.getFinalState() == Constantes.State.NONCONFORME){
                res++;
            }
        }
        return res;
    }

    /**
     * calcul le nombre de tache bloquante dans la liste selon le type
     * @param type
     * @param itemList
     * @return
     */
    public static float computeBloquante(Constantes.type type, List<DetailContent.DetailItem> itemList){
        float res = 0;
        float resConforme = 0;
        float resNoConforme = 0;
        float resTotal = 0;
        String typeDefine = "";
        if (type == Constantes.type.RETAIL){
            typeDefine = "SI_Nantes";
        } else {
            typeDefine = "SITI";
        }
        for (DetailContent.DetailItem it : itemList) {
            if (it.getInfSystem().matches(typeDefine)) {
                if (it.getCriticity().matches("Blocking")) {
                    if (it.getFinalState() == Constantes.State.CONFORME){
                        resConforme++;
                    } else {
                        resNoConforme++;
                    }
                    resTotal++;
                }
            }
        }

        Log.d(TAG, "computeBloquante -> " + resConforme + " Conforme: " + resNoConforme + " Non Conformes: ");
        res = resConforme/resTotal;
        return res;
    }

    /**
     * calcul le nombre de tache majeur dans la liste selon le type
     * @param type
     * @param itemList
     * @return
     */
    public static float computeMajor(Constantes.type type,List<DetailContent.DetailItem>  itemList){
        float res = 0;
        float resConforme = 0;
        float resNoConforme = 0;
        float resTotal = 0;
        String typeDefine = "";
        if (type == Constantes.type.RETAIL){
            typeDefine = "SI_Nantes";
        } else {
            typeDefine = "SITI";
        }
        for (DetailContent.DetailItem it : itemList) {
            if (it.getInfSystem().matches(typeDefine)) {
                if (it.getCriticity().matches("Major")) {
                    if (it.getFinalState() == Constantes.State.CONFORME){
                        resConforme++;
                    } else {
                        resNoConforme++;
                    }
                    resTotal++;
                }
            }
        }

        Log.d(TAG, "computeMajor -> " + resConforme + " Conforme: " + resNoConforme + " Non Conformes: ");
        res = resConforme/resTotal;
        return res;
    }

    /**
     * calcul le nombre de tache mineur dans la liste selon le type
     * @param type
     * @param itemList
     * @return
     */
    public static float computeMinor(Constantes.type type,List<DetailContent.DetailItem>  itemList){
        float res = 0;
        float resConforme = 0;
        float resNoConforme = 0;
        float resTotal = 0;
        String typeDefine = "";
        if (type == Constantes.type.RETAIL){
            typeDefine = "SI_Nantes";
        } else {
            typeDefine = "SITI";
        }
        for (DetailContent.DetailItem it : itemList) {
            if (it.getInfSystem().matches(typeDefine)) {
                if (it.getCriticity().matches("Minor")) {
                    if (it.getFinalState() == Constantes.State.CONFORME){
                        resConforme++;
                    } else {
                        resNoConforme++;
                    }
                    resTotal++;
                }
            }
        }

        Log.d(TAG, "computeMinor -> " + resConforme + " Conforme: " + resNoConforme + " Non Conformes: ");
        res = resConforme/resTotal;
        return res;
    }

    /**
     * calcul le nombre de tache bloquante dans la liste selon le type
     * @param type
     * @param itemList
     * @return
     */
    public static float computeProject(Constantes.type type,List<DetailContent.DetailItem>  itemList){
        float res = 0;
        float resConforme = 0;
        float resNoConforme = 0;
        float resTotal = 0;
        String typeDefine = "";
        if (type == Constantes.type.RETAIL){
            typeDefine = "SI_Nantes";
        } else {
            typeDefine = "SITI";
        }
        for (DetailContent.DetailItem it : itemList) {
            if (it.getInfSystem().matches(typeDefine)) {
                if (!(it.getTypology().matches("Information")
                        || it.getTypology().matches("Demande intervention")
                        || it.getTypology().matches("Assistance BI Query")
                        || (it.getTypology().matches("Execution requete") && it.getInfSystem().matches("SI_Nantes"))
                        || it.getTypology().matches("Administrateur User BO"))) {
                    if (it.getFinalState() == Constantes.State.CONFORME){
                        resConforme++;
                    } else {
                        resNoConforme++;
                    }
                    resTotal++;
                }
            }
        }

        Log.d(TAG, "computeProjet-> " + resConforme + " Conforme: " + resNoConforme + " Non Conformes: ");
        res = resConforme/resTotal;
        return res;
    }
    public static float computeSupport(Constantes.type type,List<DetailContent.DetailItem>  itemList){
        float res = 0;
        float resConforme = 0;
        float resNoConforme = 0;
        float resTotal = 0;
        String typeDefine = "";
        if (type == Constantes.type.RETAIL){
            typeDefine = "SI_Nantes";
        } else {
            typeDefine = "SITI";
        }
        for (DetailContent.DetailItem it : itemList) {
            if (it.getInfSystem().matches(typeDefine)) {
                if (it.getTypology().matches("Information")
                        || it.getTypology().matches("Demande intervention")
                        || it.getTypology().matches("Assistance BI Query")
                        || (it.getTypology().matches("Execution requete") && it.getInfSystem().matches("SI_Nantes"))
                        || it.getTypology().matches("Administrateur User BO")) {
                    if (it.getFinalState() == Constantes.State.CONFORME){
                        resConforme++;
                    } else {
                        resNoConforme++;
                    }
                    resTotal++;
                }
            }
        }

        Log.d(TAG, "computeSupport -> " + resConforme + " Conforme: " + resNoConforme + " Non Conformes: ");
        res = resConforme/resTotal;
        return res;
    }
}

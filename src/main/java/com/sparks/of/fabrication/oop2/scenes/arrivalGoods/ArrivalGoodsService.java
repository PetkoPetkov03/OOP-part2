package com.sparks.of.fabrication.oop2.scenes.arrivalGoods;

import com.sparks.of.fabrication.oop2.Singleton;
import com.sparks.of.fabrication.oop2.models.InvoiceStore;
import com.sparks.of.fabrication.oop2.models.Item;
import com.sparks.of.fabrication.oop2.models.Nomenclature;
import com.sparks.of.fabrication.oop2.models.NomenclatureDetails;
import com.sparks.of.fabrication.oop2.utils.EntityManagerWrapper;
import com.sparks.of.fabrication.oop2.utils.Pair;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ArrivalGoodsService {

    private final EntityManagerWrapper entityManagerWrapper = Singleton.getInstance(EntityManagerWrapper.class);

    public List<Nomenclature> loadItems(LocalDate date) {
        List<Nomenclature> nomenclatureList = new ArrayList<>();
        try {
            Field dateField = InvoiceStore.class.getDeclaredField("date");
            Pair<Boolean, List<InvoiceStore>> result = entityManagerWrapper.findEntityByValAll(InvoiceStore.class, dateField, java.sql.Date.valueOf(date));

            if (result.x()) {
                for (InvoiceStore invoice : result.y()) {
                    nomenclatureList.add(invoice.getNomenclatura());
                }
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Error loading nomenclatures for date: " + e.getMessage());
        }
        System.out.println("RETURNED NOMENCLATURE LIST");
        return nomenclatureList;
    }

    public List<Item> loadItemsForNomenclature(Nomenclature nomenclature) {
        try {
            Field nomenclatureField = NomenclatureDetails.class.getDeclaredField("nomenclature");
            List<String> joinFields = List.of("nomenclature", "item");

            Pair<Boolean, List<NomenclatureDetails>> nomenclatureDetailsPair = entityManagerWrapper.findEntitiesWithJoins(
                    NomenclatureDetails.class, nomenclatureField, nomenclature, joinFields);

            if (nomenclatureDetailsPair.x()) {
                List<Item> items = new ArrayList<>();
                for (NomenclatureDetails details : nomenclatureDetailsPair.y()) {
                    items.add(details.getItem());
                }
                return items;
            }
        } catch (Exception e) {
            System.err.println("Error loading items for nomenclature: " + e.getMessage());
        }
        return new ArrayList<>();
    }
}
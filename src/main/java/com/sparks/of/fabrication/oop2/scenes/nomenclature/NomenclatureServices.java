package com.sparks.of.fabrication.oop2.scenes.nomenclature;

import com.sparks.of.fabrication.oop2.Singleton;
import com.sparks.of.fabrication.oop2.models.Nomenclature;
import com.sparks.of.fabrication.oop2.models.NomenclatureDetails;
import com.sparks.of.fabrication.oop2.utils.EntityManagerWrapper;
import javafx.collections.ObservableList;

import java.lang.reflect.Field;
import java.util.List;

public class NomenclatureServices {
    private EntityManagerWrapper entityManagerWrapper = Singleton.getInstance(EntityManagerWrapper.class);

    protected List<NomenclatureDetails> loadNomenclatureDetails(Field field, Nomenclature nomenclature){
        return entityManagerWrapper.findEntityByValAll(NomenclatureDetails.class,field,nomenclature).y();
    }
}

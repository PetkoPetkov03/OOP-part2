package com.sparks.of.fabrication.oop2.scenes.nomenclature;

import com.sparks.of.fabrication.oop2.utils.Singleton;
import com.sparks.of.fabrication.oop2.models.Nomenclature;
import com.sparks.of.fabrication.oop2.models.NomenclatureDetails;
import com.sparks.of.fabrication.oop2.utils.EntityManagerWrapper;

import java.lang.reflect.Field;
import java.util.List;

/**
 * The NomenclatureServices class provides services related to loading nomenclature details from the database.
 */
public class NomenclatureServices {
    private EntityManagerWrapper entityManagerWrapper = Singleton.getInstance(EntityManagerWrapper.class);

    /**
     * Loads nomenclature details based on the given field and nomenclature.
     *
     * @param field The field to filter by.
     * @param nomenclature The nomenclature used to filter the details.
     * @return A list of NomenclatureDetails.
     */
    protected List<NomenclatureDetails> loadNomenclatureDetails(Field field, Nomenclature nomenclature){
        return entityManagerWrapper.findEntityByValAll(NomenclatureDetails.class, field, nomenclature).y();
    }
}

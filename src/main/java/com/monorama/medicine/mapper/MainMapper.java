package com.monorama.medicine.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.monorama.medicine.model.Medicine;
import com.monorama.medicine.model.MedicineDocument;
import com.monorama.medicine.model.Prescription;
import com.monorama.medicine.model.SearchMedicineParam;

@Mapper
public interface MainMapper {
	
	MedicineDocument getMedicineInfoByKeyString(String keyString);
	List <Medicine> getMedicineSeqByCondition(SearchMedicineParam searchMedicineParam);
	
	List <Prescription> getPrescriptionList();
	Prescription getPrescriptionByPu(String pu);
	int insertPrescription(HashMap <String, String> input);
	int deleteAllPrescription();
}

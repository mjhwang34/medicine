package com.monorama.medicine.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.monorama.medicine.mapper.MainMapper;
import com.monorama.medicine.model.Medicine;
import com.monorama.medicine.model.MedicineDocument;
import com.monorama.medicine.model.SearchMedicineParam;


@Service
public class MedicineService {
	
	private final MainMapper mainMapper;
	
	@Autowired
	public MedicineService(MainMapper mainMapper) {this.mainMapper = mainMapper; }
	
	public MedicineDocument getMedicineInfoByKeyString(String keyString){
		return mainMapper.getMedicineInfoByKeyString(keyString);
	}
	
	public List<Medicine> getMedicineSeqByCondition(SearchMedicineParam searchMedicineParam){
		return mainMapper.getMedicineSeqByCondition(searchMedicineParam);
	}
}

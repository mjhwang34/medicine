package com.monorama.medicine.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.monorama.medicine.mapper.MainMapper;
import com.monorama.medicine.model.Prescription;

@Service
public class PrescriptionService {

	private final MainMapper mainMapper;
	
	@Autowired
	public PrescriptionService(MainMapper mainMapper) {this.mainMapper = mainMapper; }
	
	public List<Prescription> getPrescriptionList(){
		return mainMapper.getPrescriptionList();
	}
	
	public Prescription getPrescriptionByPu(String pu) {
		return mainMapper.getPrescriptionByPu(pu);
	}
	
	public int insertPrescription(HashMap <String, String> input) {
		return mainMapper.insertPrescription(input);
	}
	
	public int deleteAllPrescription() {
		return mainMapper.deleteAllPrescription();
	}
}

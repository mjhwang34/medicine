package com.monorama.medicine.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.monorama.medicine.model.Message;
import com.monorama.medicine.model.Prescription;
import com.monorama.medicine.service.PrescriptionService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/prescriptions") 
@CrossOrigin(origins = "*", allowedHeaders="*")
public class PrescriptionController {
	
	private PrescriptionService prescriptionService;
	
	@Autowired 
	public PrescriptionController(PrescriptionService prescriptionService) {
		this.prescriptionService = prescriptionService;
	}
	
	@GetMapping() // 처방전 리스트 가져오기
	public Message getPrescriptionList() {
		List <Prescription> prescription_list = prescriptionService.getPrescriptionList();
		Message message = new Message();
		message.setData(prescription_list);
		return message;
	}
	
	@GetMapping("/{PU}") // 처방전 한개 가져오기
	public Message getPrescriptionByPu(@PathVariable("PU") String pu) {
		List <Prescription> result_list = new ArrayList<>();
		Prescription prescription = prescriptionService.getPrescriptionByPu(pu);
		if(prescription!=null) {
			result_list.add(prescription);
		}
		Message message = new Message();
		message.setData(result_list);
		return message;
	}
	
	@PostMapping("") // 처방전 추가
	public Message insertPrescription(@RequestBody HashMap<String, String> dataMap) {
		HashMap <String, String> input = new HashMap<>();
		log.info(dataMap.get("PU"));
		log.info(dataMap.get("data"));
		input.put("pu", dataMap.get("PU"));
		input.put("data", dataMap.get("data"));
		int num = prescriptionService.insertPrescription(input);
		HashMap <String, Integer> result = new HashMap<>();
		result.put("updated_rows", num);
		List<HashMap <String, Integer>> result_list = new ArrayList<>();
		result_list.add(result);
		Message message = new Message();
		message.setData(result_list);
		return message;
	}
	
	/*
	@PostMapping("") // 처방전 추가
	public Message insertPrescription(@RequestParam(value="PU") String pu, @RequestParam(value="data") String data) {
		HashMap <String, String> input = new HashMap<>();
		input.put("pu", pu);
		input.put("data", data);
		int num = prescriptionService.insertPrescription(input);
		HashMap <String, Integer> result = new HashMap<>();
		result.put("updated_rows", num);
		List<HashMap <String, Integer>> result_list = new ArrayList<>();
		result_list.add(result);
		Message message = new Message();
		message.setData(result_list);
		return message;
	}*/
	
	@DeleteMapping("") // 모든 처방전 삭제
	public Message deleteAllPrescription() {
		int num = prescriptionService.deleteAllPrescription();
		HashMap <String, Integer> result = new HashMap<>();
		result.put("deleted_rows", num);
		List<HashMap <String, Integer>> result_list = new ArrayList<>();
		result_list.add(result);
		Message message = new Message();
		message.setData(result_list);
		return message;
	}

}

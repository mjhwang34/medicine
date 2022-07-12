package com.monorama.medicine.controller;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.monorama.medicine.model.Medicine;
import com.monorama.medicine.model.MedicineDocument;
import com.monorama.medicine.model.Message;
import com.monorama.medicine.model.SearchMedicineParam;
import com.monorama.medicine.service.MedicineService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/medicines") 
@CrossOrigin(origins = "*", allowedHeaders="*")
public class MedicineController {
	private static String document_file_path = "C:\\Users\\user\\Medicine\\document";
	
	private MedicineService medicineService;
	@Autowired 
	public MedicineController(MedicineService medicineService) {
		this.medicineService = medicineService;
	}
	
	@GetMapping("/{keyString}")
	public Message medicineInfo(@PathVariable("keyString") String keyString){
		MedicineDocument medicineDocument = medicineService.getMedicineInfoByKeyString(keyString);
		if(medicineDocument==null) { // 약품이 존재하지 않을 때
			//throw new NotFoundException();
			Message message = new Message();
			return message;
		}
		String file_name = getFileName(medicineDocument.getItem_seq());
		try {
			BufferedReader reader = null;
			try {
				reader = new BufferedReader(new InputStreamReader(new FileInputStream(document_file_path+"\\document_"+file_name+".json"),"utf-8"));
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			JSONParser parser = new JSONParser();
			try {
				Object obj = parser.parse(reader);
				JSONObject jsonObj = (JSONObject)obj;
				JSONObject dataObj = (JSONObject) jsonObj.get(medicineDocument.getItem_seq());
				JSONArray eeArr = (JSONArray) dataObj.get("EE_DOC_DATA");
				JSONArray udArr = (JSONArray) dataObj.get("UD_DOC_DATA");
				JSONArray nbArr = (JSONArray) dataObj.get("NB_DOC_DATA");
				medicineDocument.setEe_doc_data(eeArr);
				medicineDocument.setUd_doc_data(udArr);
				medicineDocument.setNb_doc_data(nbArr);
			} catch (IOException | ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HashMap <String, MedicineDocument> info = new HashMap<>();
		info.put(medicineDocument.getItem_seq(), medicineDocument);
		List <HashMap<String, MedicineDocument>> dataList = new ArrayList<>();
		dataList.add(info);
		Message message = new Message();
		message.setData(dataList);
		return message;
	}
	
	@GetMapping()
	public Message searchMedicine(
			@RequestParam(value="ITEM_SEQ_LIST", required=false) List<String> item_seq_list,
			@RequestParam(value="EDI_CODE_LIST", required=false) List<String> edi_code_list,
			@RequestParam(value="ITEM_NAME", required=false) String item_name,
			@RequestParam(value="ENTP_NAME", required=false) String entp_name,
			@RequestParam(value="SHAPE", required=false) String shape,
			@RequestParam(value="LINE", required=false) List <String> line_list,
			@RequestParam(value="COLOR", required=false) List <String> color_list
			) {
		for(int i=0; i<edi_code_list.size(); i++) {
			log.info(edi_code_list.get(i));
		}
		SearchMedicineParam searchMedicineParam = new SearchMedicineParam();
		searchMedicineParam.setItem_seq_list(item_seq_list);
		searchMedicineParam.setEdi_code_list(edi_code_list);
		searchMedicineParam.setItem_name(item_name);
		searchMedicineParam.setEntp_name(entp_name);
		searchMedicineParam.setShape(shape);
		searchMedicineParam.setLine_list(line_list);
		searchMedicineParam.setColor_list(color_list);
		List <Medicine> medicine_list = medicineService.getMedicineSeqByCondition(searchMedicineParam);
		Message message = new Message();
		message.setData(medicine_list);
		return message;
	}
	
	public String getFileName(String item_seq) {
		int year = Integer.parseInt(item_seq.substring(0, 4));
		int seq = Integer.parseInt(item_seq.substring(4, 9));
		int temp = (seq-1)/100;
		String range_st = Integer.toString(temp*100+1);
		String range_end = Integer.toString(temp*100+100);
		String five_zeros = "00000";
		range_st = five_zeros.substring(0,5-range_st.length()) + range_st;
		range_end = five_zeros.substring(0, 5-range_end.length()) + range_end;
		return Integer.toString(year)+'_'+range_st+'-'+range_end;
	}
	
	
}

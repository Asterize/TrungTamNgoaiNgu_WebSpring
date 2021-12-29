package com.spring.trungtamngoaingu.Wrapper;

import java.util.ArrayList;
import Model.DiemThiModel;
import Model.PhongThiModel;


public class ThongTinThiSinh_Thi_ModelWrapper {

	private ArrayList<ArrayList<DiemThiModel>> diemThiList;
	private ArrayList<PhongThiModel> phongThiList;

	public ArrayList<ArrayList<DiemThiModel>> getDiemThiList() {
		return diemThiList;
	}

	public void setDiemThiList(ArrayList<ArrayList<DiemThiModel>> diemThiList) {
		this.diemThiList = new ArrayList<>();
		for (ArrayList<DiemThiModel> miniList : diemThiList) {
			this.diemThiList.add(miniList);
		}
	}

	public ArrayList<PhongThiModel> getPhongThiList() {
		return phongThiList;
	}

	public void setPhongThiList(ArrayList<PhongThiModel> phongThiList) {
		this.phongThiList = new ArrayList<>();
		for (PhongThiModel phongThiModel : phongThiList) {
			this.phongThiList.add(phongThiModel);
		}
	}
	
	
}

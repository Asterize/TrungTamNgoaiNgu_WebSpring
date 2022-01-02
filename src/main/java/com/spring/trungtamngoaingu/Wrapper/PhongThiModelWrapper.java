package com.spring.trungtamngoaingu.Wrapper;

import java.util.ArrayList;
import Model.PhongThiModel;


public class PhongThiModelWrapper {
	private ArrayList<PhongThiModel> phongThiList;

	public ArrayList<PhongThiModel> getPhongThiList() {
		return phongThiList;
	}

	public void setPhongThiList(ArrayList<PhongThiModel> listPhong) {
		this.phongThiList = new ArrayList<>();
		for (PhongThiModel phong: listPhong) {
			if (phong.getMaPhong() != null)
				this.phongThiList.add(phong);
		}
	}


}

package com.spring.trungtamngoaingu.Wrapper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import Model.DuThiModel;
import Model.PhongThiModel;
import Model.ThiSinhModel;

public class DuThiModelWrapper {
	private ArrayList<DuThiModel> duThiList;

	public ArrayList<DuThiModel> getDuThiList() {
		return duThiList;
	}

	public void setDuThiList(ArrayList<DuThiModel> listDuThi) {
		this.duThiList = new ArrayList<>();
		for (DuThiModel dt : listDuThi) {
			if (dt.getMaPhong() != null)
				this.duThiList.add(dt);
		}
	}

	public ThiSinhModel getThiSinhInfo(String cmnd) {
		ThiSinhModel model = new ThiSinhModel();
		ThiSinhModel thiSinhTheoCMND = model.getThiSinhTheoCMND(cmnd);
		return thiSinhTheoCMND;
	}

	public PhongThiModel getPhongInfo(String maPhong) {
		PhongThiModel model = new PhongThiModel();
		PhongThiModel phongThiTheoMaPhong = model.getPhongThiTheoMaPhong(maPhong);
		return phongThiTheoMaPhong;
	}

	public String convertDateVietNam(LocalDate date) {
		String formattedDate = date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
		return formattedDate;
	}

}

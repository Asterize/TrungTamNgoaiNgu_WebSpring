package com.spring.trungtamngoaingu.Wrapper;

import java.util.ArrayList;
import java.util.Iterator;
import Model.KhoaThiModel;


public class KhoaThiModelWrapper {
	private ArrayList<KhoaThiModel> khoaThiList;

	public ArrayList<KhoaThiModel> getKhoaThiList() {
		return khoaThiList;
	}

	public void setKhoaThiList(ArrayList<KhoaThiModel> listKhoa) {
		this.khoaThiList = new ArrayList<>();
		for (KhoaThiModel khoa : listKhoa) {
			if (khoa.getMaKhoaThi() != null)
				this.khoaThiList.add(khoa);
		}
	}

	public boolean checkDuplicate(String maKhoaThi) {
		for (Iterator<KhoaThiModel> iterator = khoaThiList.iterator(); iterator.hasNext();) {
			KhoaThiModel khoaThiModel = (KhoaThiModel) iterator.next();
			if (khoaThiModel.getMaKhoaThi().equals(maKhoaThi)) {
				return true;
			}

		}
		return false;
	}

}

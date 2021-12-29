package com.spring.trungtamngoaingu.Wrapper;

import java.util.ArrayList;
import java.util.HashMap;
import DAL.BaiThiDAL;
import DAL.DiemThiDAL;
import DAL.DuThiDAL;
import Model.BaiThiModel;
import Model.DiemThiModel;
import Model.DuThiModel;
import Model.PhongThiModel;
import Model.ThiSinhModel;

public class ThiSinhModelWrapper {
	private ArrayList<ThiSinhModel> thiSinhList;

	public ArrayList<ThiSinhModel> getThiSinhList() {
		return thiSinhList;
	}

	public void setThiSinhList(ArrayList<ThiSinhModel> listThiSinh) {
		this.thiSinhList = new ArrayList<>();
		for (ThiSinhModel ts : listThiSinh) {
			if (ts.getCMND() != null)
				this.thiSinhList.add(ts);
		}
	}

	// Sau khi biết CMND và đã filter mảng
	public HashMap<String, String> getMaPhongVaSBD(ArrayList<ThiSinhModel> listToFind) {
		DuThiDAL duThiDAL = new DuThiDAL();
		HashMap<String, String> mapKQ = new HashMap<>();

		for (ThiSinhModel thiSinh : listToFind) {
			String cmnd = thiSinh.getCMND(); // để liên kết bảng duThi
			String soBaoDanh = null;
			String maPhongThi = null;
			try {
				ArrayList<DuThiModel> thiSinhDuThi = duThiDAL.getAllDT("CMND='" + cmnd + "'", null);
				if (thiSinhDuThi != null) {
					soBaoDanh = thiSinhDuThi.get(0).getSBD();
					maPhongThi = thiSinhDuThi.get(0).getMaPhong();
					mapKQ.put(maPhongThi, soBaoDanh);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return mapKQ;
	}

	// Sau khi biết mã phòng
	public PhongThiModel getThongTinPhongThi(String maPhong) {
		PhongThiModel output = new PhongThiModel();
		try {
			PhongThiModel phongThiTheoMaPhong = output.getPhongThiTheoMaPhong(maPhong);
			return phongThiTheoMaPhong;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	// Sau khi biết mã phòng
	public ArrayList<DiemThiModel> getThongTinBaiThi_Diem(String maPhong, String soBaoDanh) {
		BaiThiDAL baiThiDAL = new BaiThiDAL();

		try {
			ArrayList<BaiThiModel> allBT = baiThiDAL
					.getAllBT("MaPhongThi='" + maPhong + "' and SBD='" + soBaoDanh + "'", null);
			if (allBT.isEmpty()) {
				return null;
			} else {
				String maBaiThi = allBT.get(0).getMaBaiThi();
				DiemThiDAL diemThiDAL = new DiemThiDAL();
				ArrayList<DiemThiModel> baiThiCanTim= diemThiDAL.getAllDT("MaBaiThi='" + maBaiThi + "'", null);
				return baiThiCanTim;
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

}

package com.spring.trungtamngoaingu.Controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.view.RedirectView;

import com.spring.trungtamngoaingu.Wrapper.DuThiModelWrapper;
import com.spring.trungtamngoaingu.Wrapper.KhoaThiModelWrapper;
import com.spring.trungtamngoaingu.Wrapper.PhongThiModelWrapper;
import com.spring.trungtamngoaingu.Wrapper.ThiSinhModelWrapper;
import com.spring.trungtamngoaingu.Wrapper.ThongTinThiSinh_Thi_ModelWrapper;

import DAL.DuThiDAL;
import Model.DiemThiModel;
import Model.DuThiModel;
import Model.KhoaThiModel;
import Model.PhongThiModel;
import Model.ThiSinhModel;

//import com.example.QuanLyTourDuLich.Wrapper.DiaDiemModelWrapper;
//import com.example.QuanLyTourDuLich.Wrapper.GiaTourModelWrapper;
//import com.example.QuanLyTourDuLich.Wrapper.LoaiHinhModelWrapper;
//import com.example.QuanLyTourDuLich.Wrapper.ThamQuanModelWrapper;
//import com.example.QuanLyTourDuLich.Wrapper.TourModelWrapper;
//
//import DAL.DiaDiemDAL;
//import DAL.TourDAL;
//import Model.*;
//
@Controller
public class TrungTamController {
	private ArrayList<ThiSinhModel> allThiSinhs = new ArrayList<>();
	private ArrayList<PhongThiModel> allPhongs = new ArrayList<>();

//	private ArrayList<GiaTourModel> list_gia;
//	private ArrayList<ThamQuanModel> list_thamQuan;
//	private ArrayList<LoaiHinhModel> list_loaiHinh;
//	private ArrayList<DiaDiemModel> list_diaDiem;
//
//	private TourModel tourHienTai = new TourModel();
//	private GiaTourModel giaTour = new GiaTourModel();
//	private LoaiHinhModel loaiHinh = new LoaiHinhModel();
//	private ThamQuanModel thamQuan = new ThamQuanModel();
//
	public TrungTamController() {
		ThiSinhModel thiSinhModel = new ThiSinhModel();
		PhongThiModel phongThiModel = new PhongThiModel();
		try {
			this.allThiSinhs = thiSinhModel.getAllThiSinh();
			this.allPhongs = phongThiModel.getAllPhongThi();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

//
	@RequestMapping("/TraCuu")
	public String traCuuPage(Model model) {
		ThiSinhModelWrapper wrapper = new ThiSinhModelWrapper();
		wrapper.setThiSinhList(allThiSinhs);
		model.addAttribute("allThiSinhs", wrapper);
		return "traCuu";
	}

	@RequestMapping(value = "/TraCuu/LookUpByNameAndPhone/{userName}/{userPhone}", method = RequestMethod.POST)
	public String traCuuThongTinThiSinh(Model model, @PathVariable("userName") String userName, 
			@PathVariable("userPhone")String userPhone) {
		ThiSinhModelWrapper wrapper = new ThiSinhModelWrapper();
		ArrayList<ThiSinhModel> ketQuaTraCuu = new ArrayList<>();

		for (ThiSinhModel thiSinhModel : allThiSinhs) {
			if (thiSinhModel.getHoTen().toLowerCase().contains(userName.toLowerCase())) {
				if (thiSinhModel.getSDT().equals(userPhone)) {
					ketQuaTraCuu.add(thiSinhModel);
				}
			}
		}
		// Nếu kết quả tra ra thành công
		if (!ketQuaTraCuu.isEmpty()) {
			ArrayList<PhongThiModel> ketQuaPhongThi = new ArrayList<>();
			ArrayList<ArrayList<DiemThiModel>> ketQuaDiemThi = new ArrayList<>();
			HashMap<String, String> mapPhong_SBD = wrapper.getMaPhongVaSBD(ketQuaTraCuu);

			for (Entry<String, String> entry : mapPhong_SBD.entrySet()) {
				String maPhong = entry.getKey();
				String soBaoDanh = entry.getValue();

				PhongThiModel thongTinPhongThi = wrapper.getThongTinPhongThi(maPhong);
				ArrayList<DiemThiModel> thongTinBaiThi_Diem = wrapper.getThongTinBaiThi_Diem(maPhong, soBaoDanh);

				ketQuaPhongThi.add(thongTinPhongThi);
				ketQuaDiemThi.add(thongTinBaiThi_Diem);
			}

			ThongTinThiSinh_Thi_ModelWrapper wrapper_traCuu = new ThongTinThiSinh_Thi_ModelWrapper();
			wrapper.setThiSinhList(ketQuaTraCuu);
			wrapper_traCuu.setDiemThiList(ketQuaDiemThi);
			wrapper_traCuu.setPhongThiList(ketQuaPhongThi);
			model.addAttribute("ketQuaTraCuu", wrapper_traCuu);
			model.addAttribute("thiSinhResult", wrapper.getThiSinhList().get(0));
		}

		if (model.containsAttribute("ketQuaTraCuu")) {
			model.addAttribute("coKetQua", true);
		} else {
			model.addAttribute("coKetQua", false);
		}
		model.addAttribute("userName", userName);
		model.addAttribute("userPhone", userPhone);
		return "ketQuaTraCuu";
	}

//
//	@RequestMapping(value = "/changeTourName", method = RequestMethod.POST)
//	public RedirectView changeTourName(String maTour, String newTourName) {
//		try {
//			if (tourHienTai.getMaTour().equals(maTour)) {
//				if (newTourName != null) {
//					tourHienTai.setTenTour(newTourName);
//					tourHienTai.updateTour(tourHienTai);
//					for (Iterator<TourModel> iterator = allTours.iterator(); iterator.hasNext();) {
//						TourModel tourModel = (TourModel) iterator.next();
//						if (tourModel.getMaTour().equals(maTour)) {
//							tourModel.setTenTour(newTourName);
//							break;
//						}
//					}
//				}
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return new RedirectView("/tours/" + maTour);
//	}
//	
//	@RequestMapping(value = "/changeDacDiem", method = RequestMethod.POST)
//	public RedirectView changeDacDiem(String maTour, String newDacDiem) {
//		try {
//			if (tourHienTai.getMaTour().equals(maTour)) {
//				if (newDacDiem != null) {
//					tourHienTai.setDacDiem(newDacDiem);
//					tourHienTai.updateTour(tourHienTai);
//					for (Iterator<TourModel> iterator = allTours.iterator(); iterator.hasNext();) {
//						TourModel tourModel = (TourModel) iterator.next();
//						if (tourModel.getMaTour().equals(maTour)) {
//							tourModel.setDacDiem(newDacDiem);
//							break;
//						}
//					}
//				}
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return new RedirectView("/tours/" + maTour);
//	}
//
//	@RequestMapping(value = "/deleteDiaDiem/{idTour}/{thutu}", method = RequestMethod.GET)
//	public RedirectView deleteDiaDiem(@PathVariable("idTour") String idTour, @PathVariable("thutu") String thutu) {
//		int stt = Integer.parseInt(thutu) - 1;
//		ThamQuanModel thamQuanSelected = list_thamQuan.get(stt);
//		try {
//			thamQuanSelected.deleteThamQuan(thamQuanSelected);
//			list_thamQuan.remove(stt);
//			Collections.sort(list_thamQuan, ThamQuanModel.sttComparator);
//			// Nếu xóa số ở giữa thì đặt lại STT
//			int soLuong = list_thamQuan.size();
//			int sttMoi = stt + 1;
//			for (int i = stt; i < soLuong; i++) {
//				list_thamQuan.get(i).setThutu(sttMoi++);
//			}
//
//			// Update STT cho DB
//			ArrayList<ThamQuanModel> list_temp = new ArrayList<>(list_thamQuan);
//			list_temp.forEach(tmd -> {
//				try {
////                System.out.println(tmd.getMaTour() + "/ " + tmd.getMaDiaDiem() + "/ " + tmd.getThutu());
//					tmd.updateThamQuan(tmd);
//				} catch (Exception ex) {
//					ex.printStackTrace();
//				}
//			});
//			list_thamQuan.clear();
//			list_thamQuan = new ArrayList<>(list_temp);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		return new RedirectView("/tours/" + idTour);
//	}
//
//	@RequestMapping(value = "/changeLoaiHinh", method = RequestMethod.POST)
//	public RedirectView changeLoaiHinh(String maTour, String maLH, String newloaihinh) {
//		for (Iterator<LoaiHinhModel> iterator = list_loaiHinh.iterator(); iterator.hasNext();) {
//			LoaiHinhModel lh = (LoaiHinhModel) iterator.next();
//			if (lh.getTenLoaiHinh().equals(newloaihinh)) {
//				maLH = lh.getMaLoaiHinh();
//				break;
//			}
//		}
//		try {
//			if (tourHienTai.getMaTour().equals(maTour)) {
//				if (newloaihinh != null) {
//					LoaiHinhModel newLH = new LoaiHinhModel(maLH, newloaihinh);
//					tourHienTai.setMaLoaiHinh(maLH);
//					tourHienTai.setLoaiHinh(newLH);
//					tourHienTai.updateTour(tourHienTai);
//
//					for (Iterator<TourModel> iterator = allTours.iterator(); iterator.hasNext();) {
//						TourModel tourModel = (TourModel) iterator.next();
//						if (tourModel.getMaTour().equals(maTour)) {
//							tourModel.setMaLoaiHinh(maLH);
//							tourModel.setLoaiHinh(newLH);
//							break;
//						}
//					}
//				}
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return new RedirectView("/tours/" + maTour);
//	}
//
//	@RequestMapping(value = "/themDiaDiem", method = RequestMethod.POST)
//	public RedirectView themDiaDiem(String maTour, String newDiaDiem) {
//		int thuTuCanThem = (list_thamQuan.size()) + 1;
//		boolean hopLe = true;
//		for (ThamQuanModel thamQuanModel : list_thamQuan) {
//			if (thamQuanModel.getThutu() == thuTuCanThem) {
//				hopLe = false;
//				break;
//			}
//		}
//		if (hopLe) {
//			try {
//				if (tourHienTai.getMaTour().equals(maTour)) {
//					if (newDiaDiem != null) {
//						String maDiaDiemCanThem = "";
//	                    for (DiaDiemModel model : list_diaDiem) {
//	                        if (model.getTenDiaDiem().equalsIgnoreCase(newDiaDiem)) {
//	                            maDiaDiemCanThem = model.getMaDiaDiem();
//	                            break;
//	                        }
//	                    }
//	                    ThamQuanModel newTQ = new ThamQuanModel(maTour, maDiaDiemCanThem, newDiaDiem, thuTuCanThem);
//	                    newTQ.insertThamQuan(newTQ);
//	                    list_thamQuan.add(newTQ);
//	                    Collections.sort(list_thamQuan, ThamQuanModel.sttComparator);
//					}
//				}
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		return new RedirectView("/tours/" + maTour);
//	}
//
	@RequestMapping(value = "/XemDanhSachThiSinhTheoPhong", method = RequestMethod.GET)
	public String xemDanhSachTheoPhongVaKhoaPage(Model model) {
		try {
			PhongThiModelWrapper phong_Wrapper = new PhongThiModelWrapper();
			phong_Wrapper.setPhongThiList(allPhongs);

			KhoaThiModelWrapper khoa_Wrapper = new KhoaThiModelWrapper();
			ArrayList<KhoaThiModel> khoaThiList = new ArrayList<>();
			khoa_Wrapper.setKhoaThiList(khoaThiList);

			for (PhongThiModel phong : phong_Wrapper.getPhongThiList()) {
				if (!khoa_Wrapper.checkDuplicate(phong.getMaKT())) {
					khoa_Wrapper.getKhoaThiList().add(new KhoaThiModel().getKTByMa(phong.getMaKT()));
				}
			}
			model.addAttribute("allPhongs", phong_Wrapper);
			model.addAttribute("allKhoas", khoa_Wrapper);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		boolean coDanhSachPhong = model.containsAttribute("allPhongs");
		boolean coDanhSachKhoa = model.containsAttribute("allKhoas");
		model.addAttribute("coDanhSachPhong", coDanhSachPhong);
		model.addAttribute("coDanhSachKhoa", coDanhSachKhoa);
		return "xemDanhSachThiSinh";
	}

	@RequestMapping(value = "/XemDanhSachThiSinhTheoPhong/TraCuu", method = RequestMethod.POST)
	public String traCuuDanhSachCuaPhong_Khoa(Model model, String comboKhoaThi, String comboPhongThi) {
		DuThiModelWrapper duThi_wrapper = new DuThiModelWrapper();
		DuThiDAL duThiDAL = new DuThiDAL();
		try {
			ArrayList<DuThiModel> listDuThi = duThiDAL.getAllDT("MaPhongThi='" + comboPhongThi + "'", null);
			duThi_wrapper.setDuThiList(listDuThi);
			model.addAttribute("listThiSinh", duThi_wrapper);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		boolean coKetQua = model.containsAttribute("listThiSinh");
		model.addAttribute("coKetQua", coKetQua);
		return "ketQuaDanhSachThiSinh";
	}

//
	@RequestMapping("/")
	public String index() {
//		TourModelWrapper wrapper = new TourModelWrapper();
//		wrapper.setTourList(allTours);
//		model.addAttribute("wrapper", wrapper);
		return "index";
	}
//
//	@RequestMapping(value = "/tours/query/submitQuery", method = RequestMethod.POST)
//	public String processQuery(@ModelAttribute TourModelWrapper wrapper, Model model) {
//
//		System.out.println(wrapper.getTourList() != null ? wrapper.getTourList().size() : "null list");
//		System.out.println("--");
//
//		model.addAttribute("wrapper", wrapper);
//
//		return "tour";
//	}
//
}

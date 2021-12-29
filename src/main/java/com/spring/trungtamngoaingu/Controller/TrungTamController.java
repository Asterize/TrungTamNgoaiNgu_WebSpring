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

import com.spring.trungtamngoaingu.Wrapper.ThiSinhModelWrapper;
import com.spring.trungtamngoaingu.Wrapper.ThongTinThiSinh_Thi_ModelWrapper;

import DAL.DuThiDAL;
import Model.DiemThiModel;
import Model.DuThiModel;
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
		try {
			this.allThiSinhs = thiSinhModel.getAllThiSinh();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

//
	@RequestMapping("/TraCuu")
	public String tours(Model model) {
		ThiSinhModelWrapper wrapper = new ThiSinhModelWrapper();
		wrapper.setThiSinhList(allThiSinhs);
		model.addAttribute("allThiSinhs", wrapper);
		return "traCuu";
	}

	@RequestMapping(value = "/TraCuu/LookUpByNameAndPhone", method = RequestMethod.POST)
	public String themDiaDiem(Model model, String userName, String userPhone) {
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
			wrapper_traCuu.setDiemThiList(ketQuaDiemThi);
			wrapper_traCuu.setPhongThiList(ketQuaPhongThi);
			model.addAttribute("ketQuaTraCuu", wrapper_traCuu);
		}
		
		if (model.containsAttribute("ketQuaTraCuu")) {
			model.addAttribute("coKetQua", true);
		}
		else {
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
//	@RequestMapping(value = "/tours/{idTour}", method = RequestMethod.GET)
//	public String chiTietTour(Model model, @PathVariable("idTour") String idTour) {
//		try {
//			TourDAL tourDAL = new TourDAL();
//			tourHienTai = tourDAL.getAllTours("MaTour='" + idTour + "'", null).get(0);
//
//			// Giá
//			GiaTourModelWrapper wrapper_GiaTour = new GiaTourModelWrapper();
//			list_gia = giaTour.getGiaByMaTour(idTour);
//			wrapper_GiaTour.setGiaTourList(list_gia);
//
//			// Loại hình
//			LoaiHinhModelWrapper wrapper_LoaiHinh = new LoaiHinhModelWrapper();
//			list_loaiHinh = loaiHinh.getAllLoaiHinh();
//			wrapper_LoaiHinh.setLoaiHinhList(list_loaiHinh);
//			for (Iterator<LoaiHinhModel> iterator = list_loaiHinh.iterator(); iterator.hasNext();) {
//				LoaiHinhModel loaiHinh = (LoaiHinhModel) iterator.next();
//				if (loaiHinh.getMaLoaiHinh().equals(tourHienTai.getMaLoaiHinh())) {
//					tourHienTai.setLoaiHinh(loaiHinh);
//					break;
//				}
//			}
//
//			// Lịch trình tham quan
//			ThamQuanModelWrapper wrapper_ThamQuan = new ThamQuanModelWrapper();
//			list_thamQuan = thamQuan.getThamQuanByMaTour(tourHienTai.getMaTour());
//			wrapper_ThamQuan.setThamQuanList(list_thamQuan);
//
//			// Địa điểm tham quan
//			DiaDiemDAL diaDiemDAL = new DiaDiemDAL();
//			list_diaDiem = diaDiemDAL.getDiaDiem(null, null);
//			DiaDiemModelWrapper wrapper_DD = new DiaDiemModelWrapper();
//			wrapper_DD.setDiaDiemList(list_diaDiem);
//
//			// set model
//			model.addAttribute("tourHienTai", tourHienTai);
//			model.addAttribute("wrapperGia", wrapper_GiaTour);
//			model.addAttribute("wrapperLH", wrapper_LoaiHinh);
//			model.addAttribute("wrapperThamQuan", wrapper_ThamQuan);
//			model.addAttribute("wrapperDiaDiem", wrapper_DD);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		return "chitiettour";
//	}
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

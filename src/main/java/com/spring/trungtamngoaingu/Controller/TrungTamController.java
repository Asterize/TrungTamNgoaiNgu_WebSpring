package com.spring.trungtamngoaingu.Controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.view.RedirectView;
import static java.time.temporal.ChronoUnit.DAYS;
import com.spring.trungtamngoaingu.Wrapper.DuThiModelWrapper;
import com.spring.trungtamngoaingu.Wrapper.KhoaThiModelWrapper;
import com.spring.trungtamngoaingu.Wrapper.PhongThiModelWrapper;
import com.spring.trungtamngoaingu.Wrapper.ThiSinhModelWrapper;
import com.spring.trungtamngoaingu.Wrapper.ThongTinThiSinh_Thi_ModelWrapper;

import DAL.DuThiDAL;
import Model.BaiThiModel;
import Model.DangKyModel;
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

	private void refreshAllList() {
		try {
			ThiSinhModel thiSinhModel = new ThiSinhModel();
			PhongThiModel phongThiModel = new PhongThiModel();
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
		refreshAllList();
		ThiSinhModelWrapper wrapper = new ThiSinhModelWrapper();
		wrapper.setThiSinhList(allThiSinhs);
		model.addAttribute("allThiSinhs", wrapper);
		return "traCuu";
	}

	@RequestMapping(value = "/TraCuu/LookUpByNameAndPhone/{userName}/{userPhone}", method = RequestMethod.POST)
	public String traCuuThongTinThiSinh(Model model, @PathVariable("userName") String userName,
			@PathVariable("userPhone") String userPhone) {
		refreshAllList();
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
				// Kiểm tra có điểm chưa
				ketQuaDiemThi.get(0).removeIf(diem -> diem.getDiem() == -1);
			}

			ThongTinThiSinh_Thi_ModelWrapper wrapper_traCuu = new ThongTinThiSinh_Thi_ModelWrapper();
			wrapper.setThiSinhList(ketQuaTraCuu);
			wrapper_traCuu.setDiemThiList(ketQuaDiemThi);
			wrapper_traCuu.setPhongThiList(ketQuaPhongThi);

			if (wrapper.getThiSinhList() == null) {
				model.addAttribute("thiSinhResult", null);
			} else {
				model.addAttribute("ketQuaTraCuu", wrapper_traCuu);
				model.addAttribute("thiSinhResult", wrapper.getThiSinhList().get(0));
			}
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

	@RequestMapping(value = "/XemDanhSachThiSinhTheoPhong", method = RequestMethod.GET)
	public String xemDanhSachTheoPhongVaKhoaPage(Model model) {
		refreshAllList();
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
		refreshAllList();
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

	@RequestMapping(value = "/ThongKe", method = RequestMethod.GET)
	public String thongKePage(Model model) {
		refreshAllList();
		try {
			KhoaThiModelWrapper khoa_Wrapper = new KhoaThiModelWrapper();
			KhoaThiModel khoaModel = new KhoaThiModel();
			ArrayList<KhoaThiModel> allKT = khoaModel.getAllKT();
			khoa_Wrapper.setKhoaThiList(allKT);
			model.addAttribute("allKhoas", khoa_Wrapper);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "thongKe";
	}

	@RequestMapping(value = "/ThongKe/KetQua", method = RequestMethod.POST)
	public String thongKe_KetQua(Model model, String comboKhoaThi, String comboTrinhDo) {
		refreshAllList();
		int soLuongPhong = 0;
//		int soLuongKhoa = 0;
		int soLuongThiSinh = 0;
		try {
			KhoaThiModelWrapper khoa_Wrapper = new KhoaThiModelWrapper();

			// Thống kê phòng
			for (PhongThiModel phong : allPhongs) {
				if (phong.getMaKT().equals(comboKhoaThi)) {
					if (phong.getTenTrinhDo().equals(comboTrinhDo)) {
						soLuongPhong++;
					}
				}
			}

			// Thống kê khóa
//			KhoaThiModel khoaModel = new KhoaThiModel();
//			ArrayList<KhoaThiModel> allKT = khoaModel.getAllKT();
//			soLuongKhoa = allKT.size();
//			khoa_Wrapper.setKhoaThiList(allKT);
//			model.addAttribute("allKhoas", khoa_Wrapper);

			// Thống kê thí sinh theo trình độ
			DangKyModel dangKyModel = new DangKyModel();
			ArrayList<DangKyModel> allDK = dangKyModel.getAllDK();
			for (DangKyModel dangKy : allDK) {
				if (dangKy.getMaKT().equals(comboKhoaThi)) {
					if (dangKy.getTenTrinhDo().equals(comboTrinhDo)) {
						soLuongThiSinh++;
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		KhoaThiModel khoaThiModel = new KhoaThiModel();
		try {
			KhoaThiModel ktByMa = khoaThiModel.getKTByMa(comboKhoaThi);
			model.addAttribute("KhoaThi", ktByMa.getTenKhoaThi());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		model.addAttribute("TrinhDo", comboTrinhDo);

		model.addAttribute("SLPhong", soLuongPhong);
//		model.addAttribute("SLKhoa", soLuongKhoa);
		model.addAttribute("SLThiSinh", soLuongThiSinh);
		return "thongKe_KetQua";
	}

//
	@RequestMapping("/")
	public String index() {
//		TourModelWrapper wrapper = new TourModelWrapper();
//		wrapper.setTourList(allTours);
//		model.addAttribute("wrapper", wrapper);
		return "index";
	}

	@RequestMapping("/DangKyThi")
	public String dangKyDuThiPage(Model model) {
//		ThiSinhModelWrapper wrapper = new ThiSinhModelWrapper();
//		wrapper.setThiSinhList(allThiSinhs);
//		model.addAttribute("allThiSinhs", wrapper);
		return "dienThongTinDangKy"; // html
	}

	private boolean checkNullAndEmpty(String inp) {
		if (inp == null)
			return true;
		if (inp.isBlank())
			return true;
		if (inp.isEmpty())
			return true;
		return false;
	}

	@RequestMapping("/DangKyThi/DangKyKhoaThi")
	public String dangKyDuThi_TraCuu(Model model, String CMND, String ngayCap, String noiCap, String userName,
			String gioiTinh, String ngaySinh, String noiSinh, String userPhone, String email) {
		CMND = CMND.trim();
		
		ThiSinhModel thiSinhModel = new ThiSinhModel();
		ThiSinhModel thiSinhTheoCMND = thiSinhModel.getThiSinhTheoCMND(CMND);
		if (thiSinhTheoCMND != null) {
			model.addAttribute("thiSinhDangKy", thiSinhTheoCMND);
		} else {
			// Xét trường hợp không nhập
			if (checkNullAndEmpty(noiCap) || checkNullAndEmpty(noiSinh) || checkNullAndEmpty(userName)
					|| checkNullAndEmpty(gioiTinh) || checkNullAndEmpty(userPhone) || checkNullAndEmpty(email)) {
				model.addAttribute("failure", "Vui lòng điền đầy đủ thông tin thí sinh đăng kí dự thi!");
				model.addAttribute("thanhCong", false);
				return "ketQuaDangKyDuThi";
			}
			String[] dateSinh = ngaySinh.split("-");
			String[] dateCap = ngayCap.split("-");
			if (dateSinh.length == 3 && dateCap.length == 3) {
				int year = Integer.parseInt(dateSinh[0]);
				int month = Integer.parseInt(dateSinh[1]);
				int day = Integer.parseInt(dateSinh[2]);
				LocalDate ngaySinhLD = LocalDate.of(year, month, day);

				int yearCap = Integer.parseInt(dateCap[0]);
				int monthCap = Integer.parseInt(dateCap[1]);
				int dayCap = Integer.parseInt(dateCap[2]);
				LocalDate ngayCapLD = LocalDate.of(yearCap, monthCap, dayCap);

				model.addAttribute("thiSinhDangKy", new ThiSinhModel(CMND, userName, gioiTinh, ngaySinhLD, noiSinh,
						ngayCapLD, noiCap, userPhone, email));
			}
		}
		model.addAttribute("HopLe", model.containsAttribute("thiSinhDangKy"));

		ThiSinhModelWrapper wrapper = new ThiSinhModelWrapper();
		wrapper.setThiSinhList(allThiSinhs);
		model.addAttribute("allThiSinhs", wrapper);

		KhoaThiModel khoaThiModel = new KhoaThiModel();
		ArrayList<KhoaThiModel> listKhoaThi;
		try {
			listKhoaThi = khoaThiModel.getAllKT();
			listKhoaThi.removeIf(kt -> kt.getNgayThi().isBefore(LocalDate.now()));
			model.addAttribute("danhSachKhoaThi", listKhoaThi);
			model.addAttribute("tenKhoaThiDefault", listKhoaThi.get(0).getTenKhoaThi());
			model.addAttribute("ngayKhoaThiDefault", wrapper.convertDateVietNam(listKhoaThi.get(0).getNgayThi()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "dangKyDuThi"; // html
	}

	@RequestMapping("/DangKyThi/DangKyKhoaThi/NutDangKy")
	public String dangKyDuThi_DangKy(Model model, String comboKhoaThi, String comboTrinhDo, String CMND, String ngayCap,
			String noiCap, String hoTen, String gioiTinh, String ngaySinh, String noiSinh, String phone, String email) {
		CMND = CMND.trim();
		String[] value_array = comboKhoaThi.split("@");
		if (value_array.length == 3) {
			String maKhoaThiSelected = value_array[0];
			KhoaThiModel khoaThiModel = new KhoaThiModel();
			ArrayList<KhoaThiModel> listKhoaThi;
			try {
				listKhoaThi = khoaThiModel.getAllKT();
				listKhoaThi.removeIf(kt -> kt.getNgayThi().isBefore(LocalDate.now()));
				KhoaThiModel ktByMa = khoaThiModel.getKTByMa(maKhoaThiSelected);
				long daysBetween = DAYS.between(LocalDate.now(), ktByMa.getNgayThi());

				if (daysBetween <= 5) {
					model.addAttribute("failure", "Đã hết hạn đăng ký khóa thi này!");
				} else {
					DangKyModel dangKyModel = new DangKyModel();
					if (dangKyModel.checkExistsDangKy(maKhoaThiSelected, CMND)) {
						model.addAttribute("failure",
								"Người dùng đã đăng kí thi khóa thi này. Mời bạn thi lại vào tháng khác!");
					} else {
						// convert String to LocalDate
						ThiSinhModel thiSinhModel = new ThiSinhModel();
						ThiSinhModel ts = thiSinhModel.getThiSinhTheoCMND(CMND);
						if (ts == null) { // Thí sinh chưa có trên Database
							String[] dateSinh = ngaySinh.split("-");
							String[] dateCap = ngayCap.split("-");
							if (dateSinh.length == 3 && dateCap.length == 3) {
								int year = Integer.parseInt(dateSinh[0]);
								int month = Integer.parseInt(dateSinh[1]);
								int day = Integer.parseInt(dateSinh[2]);
								LocalDate ngaySinhLD = LocalDate.of(year, month, day);

								int yearCap = Integer.parseInt(dateCap[0]);
								int monthCap = Integer.parseInt(dateCap[1]);
								int dayCap = Integer.parseInt(dateCap[2]);
								LocalDate ngayCapLD = LocalDate.of(yearCap, monthCap, dayCap);

								ThiSinhModel thiSinhDK = new ThiSinhModel(CMND, hoTen, gioiTinh, ngaySinhLD, noiSinh,
										ngayCapLD, noiCap, phone, email);
								thiSinhDK.addThiSinh();
							}
						} else { // Thí sinh đã có trên Database, đăng ký thi lại
							String[] dateSinh = ngaySinh.split("-");
							String[] dateCap = ngayCap.split("-");
							if (dateSinh.length == 3 && dateCap.length == 3) {
								int year = Integer.parseInt(dateSinh[0]);
								int month = Integer.parseInt(dateSinh[1]);
								int day = Integer.parseInt(dateSinh[2]);
								LocalDate ngaySinhLD = LocalDate.of(year, month, day);

								int yearCap = Integer.parseInt(dateCap[0]);
								int monthCap = Integer.parseInt(dateCap[1]);
								int dayCap = Integer.parseInt(dateCap[2]);
								LocalDate ngayCapLD = LocalDate.of(yearCap, monthCap, dayCap);

								ThiSinhModel thiSinhDK = new ThiSinhModel(CMND, hoTen, gioiTinh, ngaySinhLD, noiSinh,
										ngayCapLD, noiCap, phone, email);
								thiSinhDK.updateThiSinh();
							}
						}
						DangKyModel dangKyModelMoi = new DangKyModel();
						dangKyModelMoi.setCMND(CMND);
						dangKyModelMoi.setMaKT(maKhoaThiSelected);
						dangKyModelMoi.setTenTrinhDo(comboTrinhDo);
						dangKyModelMoi.addDangKyThiSinh();
						model.addAttribute("ketQua", "Đăng ký dự thi thành công");
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		model.addAttribute("thanhCong", !model.containsAttribute("failure"));
		return "ketQuaDangKyDuThi"; // html
	}

	@RequestMapping(value = "/XemChungNhanKetQuaThi", method = RequestMethod.GET)
	public String xemGiayChungNhan_Page(Model model) {
		refreshAllList();
		try {
			KhoaThiModelWrapper khoa_Wrapper = new KhoaThiModelWrapper();
			KhoaThiModel khoaModel = new KhoaThiModel();
			ArrayList<KhoaThiModel> allKT = khoaModel.getAllKT();
			khoa_Wrapper.setKhoaThiList(allKT);
			model.addAttribute("allKhoas", khoa_Wrapper);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "xemChungNhan_Nhap";
	}

	@RequestMapping(value = "/XemChungNhanKetQuaThi/KetQua", method = RequestMethod.POST)
	public String xemGiayChungNhan_Page(Model model, String SBD, String comboKhoaThi) {
		refreshAllList();
		if (checkNullAndEmpty(comboKhoaThi) || checkNullAndEmpty(SBD)) {
			model.addAttribute("fail", "Không tìm thấy giấy chứng nhận.");
		} else {
			DuThiDAL duThiDAL = new DuThiDAL();
			ArrayList<DuThiModel> allDT;
			try {
				allDT = duThiDAL.getAllDT(null, null);
				DuThiModel duThiModel = null;
				for (DuThiModel duThi : allDT) {
					if (duThi.getSBD().equals(SBD) && duThi.getMaKhoaThi().equals(comboKhoaThi)) {
						duThiModel = duThi;
					}
				}
				// Nếu = null là ko tìm ra
				if (duThiModel == null) {
					model.addAttribute("fail", "Không tìm thấy giấy chứng nhận.");
				} else {
					// Info thí sinh
					ThiSinhModel thiSinhModel = new ThiSinhModel();
					ThiSinhModel thiSinhInfo = thiSinhModel.getThiSinhTheoCMND(duThiModel.getCMND());
					model.addAttribute("tenThiSinh", thiSinhInfo.getHoTen());
					model.addAttribute("ngaySinh",
							thiSinhInfo.getNgaySinh().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

					// Info khóa thi
					KhoaThiModel khoaThiModel = new KhoaThiModel();
					KhoaThiModel khoaThiInfo = khoaThiModel.getKTByMa(duThiModel.getMaKhoaThi());
					model.addAttribute("tenKhoaThi", khoaThiInfo.getTenKhoaThi());
					model.addAttribute("ngayThi",
							khoaThiInfo.getNgayThi().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

					// Info trình độ
					DangKyModel dangKyModel = new DangKyModel();
					ArrayList<DangKyModel> allDKByMaKT = dangKyModel.getAllDKByMaKT(duThiModel.getMaKhoaThi());
					for (DangKyModel dangKy : allDKByMaKT) {
						if (dangKy.getCMND().equals(thiSinhInfo.getCMND())) {
							model.addAttribute("tenTrinhDo", dangKy.getTenTrinhDo());
							break;
						}
					}

					// Info điểm thi
					BaiThiModel baiThiModel = new BaiThiModel();
					ArrayList<BaiThiModel> listBaiThi = baiThiModel.getBaiThiByMaPhong(duThiModel.getMaPhong());
					BaiThiModel baiThiInfo = null;
					for (BaiThiModel baiThi : listBaiThi) {
						if (baiThi.getSBD().equals(duThiModel.getSBD())) {
							baiThiInfo = baiThi;
							break;
						}
					}
					if (baiThiInfo != null) {
						DiemThiModel diemThiModel = new DiemThiModel();
						ArrayList<DiemThiModel> listDiemBonMon = diemThiModel
								.getDiemThiOfBaiThi(baiThiInfo.getMaBaiThi());
						double diemDoc = -1, diemNghe = -1, diemNoi = -1, diemViet = -1;
						for (DiemThiModel diemThiMon : listDiemBonMon) {
							if (diemThiMon.getTenPhanThi().equals("Đọc")) {
								diemDoc = diemThiMon.getDiem();
							}
							if (diemThiMon.getTenPhanThi().equals("Nghe")) {
								diemNghe = diemThiMon.getDiem();
							}
							if (diemThiMon.getTenPhanThi().equals("Nói")) {
								diemNoi = diemThiMon.getDiem();
							}
							if (diemThiMon.getTenPhanThi().equals("Viết")) {
								diemViet = diemThiMon.getDiem();
							}
						}
						if (diemDoc == -1 || diemNghe == -1 || diemNoi == -1 || diemViet == -1) {
							model.addAttribute("fail", "Không tìm thấy giấy chứng nhận.");
						}
						double diemTB = (diemDoc + diemNghe + diemNoi + diemViet) / 4.0;
						model.addAttribute("diemDoc", diemDoc);
						model.addAttribute("diemNghe", diemNghe);
						model.addAttribute("diemNoi", diemNoi);
						model.addAttribute("diemViet", diemViet);
						model.addAttribute("diemTB", diemTB);
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		model.addAttribute("timDuocGiay", !model.containsAttribute("fail"));
		return "xemChungNhan_KetQua";
	}
}

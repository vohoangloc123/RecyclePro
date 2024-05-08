package com.example.recyclepro.services;

import com.example.recyclepro.models.ConfigRate;
import com.example.recyclepro.models.Rating;

public class PriceCalculationService {

    public static Rating convertRatingToPercentage(int rating, ConfigRate configRate, double tiLeThanhPhan) {
        double tiLeGia;
        switch (rating) {
            case 1:
                tiLeGia = tiLeThanhPhan * configRate.muc_1;
                break;
            case 2:
                tiLeGia = tiLeThanhPhan * configRate.muc_2;
                break;
            case 3:
                tiLeGia = tiLeThanhPhan * configRate.muc_3;
                break;
            case 4:
                tiLeGia = tiLeThanhPhan * configRate.muc_4;
                break;
            case 5:
                tiLeGia = tiLeThanhPhan * configRate.muc_5;
                break;
            default:
                return null;
        }
        return new Rating(rating, tiLeGia); //trả về đối tượng Rating chứa 2 tham số cho hàm tính giá
    }

    public static double costingPrice(double giaNiemYet, double tiLeChiTra, Rating tiLeGiaPin, Rating tiLeGiaVo, Rating tiLeGiaMan, Rating tiLeGiaHoatDong) {
        System.out.println("\t\t\tgia co ban: " + (giaNiemYet * tiLeChiTra));

        if (tiLeGiaHoatDong.rating == 1 && tiLeGiaPin.rating == 1 && tiLeGiaVo.rating == 1 && tiLeGiaMan.rating == 1) {
            //So sánh nếu trường hợp các rating đều là 1 hết thì sẽ có công thúc tính riêng => (giá niêm yết * tỉ lệ chi trả (0.15) ) / chia nửa giá
            return (giaNiemYet * tiLeChiTra) / 2;
        } else if (tiLeGiaHoatDong.rating == 5 && tiLeGiaPin.rating == 5 && tiLeGiaVo.rating == 5 && tiLeGiaMan.rating == 5) {
            //So sánh nếu trường hợp các rating đều là 5 hết thì sẽ có công thúc tính riêng => như công thức bình thường nhưng tăng thêm 30% của giá niêm yết | thay đổi nếu muốn
            return (giaNiemYet * tiLeChiTra) + (giaNiemYet * tiLeChiTra) * (1 - (tiLeGiaPin.ratePrice + tiLeGiaVo.ratePrice + tiLeGiaMan.ratePrice + tiLeGiaHoatDong.ratePrice)) + (giaNiemYet * 0.3); // bonus them
        } else {
            //Cong thuc tinh gia binh thuong
            return (giaNiemYet * tiLeChiTra) + (giaNiemYet * tiLeChiTra) * (1 - (tiLeGiaPin.ratePrice + tiLeGiaVo.ratePrice + tiLeGiaMan.ratePrice + tiLeGiaHoatDong.ratePrice));
        }
    }
}

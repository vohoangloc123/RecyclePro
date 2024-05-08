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
        System.out.println("\t\t\tgiá cơ bản: " + (giaNiemYet * tiLeChiTra));

        if (tiLeGiaHoatDong.getRating() == 1 && tiLeGiaPin.getRating() == 1 && tiLeGiaVo.getRating() == 1 && tiLeGiaMan.getRating() == 1) {
            //So sánh nếu trường hợp tất cả các rating đều là 1 thì có công thức tính riêng => (giá niêm yết * tỉ lệ chi trả (0.15) ) / 2
            return (giaNiemYet * tiLeChiTra) / 2;
        } else if (tiLeGiaHoatDong.getRating() == 5 && tiLeGiaPin.getRating() == 5 && tiLeGiaVo.getRating() == 5 && tiLeGiaMan.getRating() == 5) {
            //So sánh nếu trường hợp tất cả các rating đều là 5 thì có công thức tính riêng => giống công thức bình thường nhưng tăng thêm 30% của giá niêm yết
            return (giaNiemYet * tiLeChiTra) + (giaNiemYet * tiLeChiTra) * 0.3;
        } else {
            //Công thức tính giá bình thường
            return (giaNiemYet * tiLeChiTra);
        }
    }
}

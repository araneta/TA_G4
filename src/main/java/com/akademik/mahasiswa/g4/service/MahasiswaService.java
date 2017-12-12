package com.akademik.mahasiswa.g4.service;
import com.akademik.mahasiswa.g4.dao.UnivDAO;
import com.akademik.mahasiswa.g4.mapper.MahasiswaMapper;
import com.akademik.mahasiswa.g4.model.db.MahasiswaDBModel;
import com.akademik.mahasiswa.g4.model.rest.FakultasResponseModel;
import com.akademik.mahasiswa.g4.model.rest.ProdiResponseModel;
import com.akademik.mahasiswa.g4.model.rest.UnivModel;
import com.akademik.mahasiswa.g4.model.rest.UnivResponseModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@Primary
public class MahasiswaService {

    @Autowired
    private MahasiswaMapper mahasiswaMapper;

    @Autowired
    private UnivDAO univDAO;

    public MahasiswaDBModel getMahasiswa(String npm)
    {
        return mahasiswaMapper.getMahasiswa(npm);
    }


    public String checkNPM(String npm)
    {
        List<MahasiswaDBModel> mahasiswas = mahasiswaMapper.getAllMahasiswa();
        for(int i = 0; i < mahasiswas.size(); i++)
        {
            if(npm.equals(mahasiswas.get(i).getNpm())) {
                return "npm-duplikat";
            }
        }
        return "npm-available";
    }

    public MahasiswaDBModel getMahasiswaWithUniv(String npm)
    {
        MahasiswaDBModel mahasiswa = mahasiswaMapper.getMahasiswa(npm);
        if(mahasiswa == null) {
            return mahasiswa;
        }

        String idUniv = Integer.toString(mahasiswa.getIdUniv());
        UnivResponseModel univ = univDAO.getUniv(idUniv);
        mahasiswa.setNamaUniv(univ.getResult().getUniversitas().getNamaUniv());

        return mahasiswa;
    }

    public MahasiswaDBModel getMahasiswaAllData(String npm)
    {
        MahasiswaDBModel mahasiswa = mahasiswaMapper.getMahasiswa(npm);
        String password = mahasiswaMapper.getPassword(mahasiswa.getUsername());
        mahasiswa.setPassword(password);

        String idUniv = Integer.toString(mahasiswa.getIdUniv());
        String idFakultas = Integer.toString(mahasiswa.getIdFakultas());
        String idProdi = Integer.toString(mahasiswa.getIdProdi());

        UnivResponseModel univ = univDAO.getUniv(idUniv);
        FakultasResponseModel fakultas = univDAO.getFakultas(idUniv, idFakultas);
        ProdiResponseModel prodi = univDAO.getProdi(idUniv, idFakultas, idProdi);

        mahasiswa.setNamaUniv(univ.getResult().getUniversitas().getNamaUniv());
        mahasiswa.setNamaFakultas(fakultas.getResult().getFakultas().getNamaFakultas());
        mahasiswa.setNamaProdi(prodi.getResult().getProdi().getNamaProdi());

        return mahasiswa;
    }

    public List<MahasiswaDBModel> getAllMahasiswa()
    {
        List<MahasiswaDBModel> mahasiswas = mahasiswaMapper.getAllMahasiswa();

        for(int i = 0; i< mahasiswas.size(); i++) {
            String idUniv = Integer.toString(mahasiswas.get(i).getIdUniv());
            UnivResponseModel univ = univDAO.getUniv(idUniv);
            mahasiswas.get(i).setNamaUniv(univ.getResult().getUniversitas().getNamaUniv());
        }

        return mahasiswas;
    }

    public String deleteMahasiswa(String npm){
        if(mahasiswaMapper.deleteMahasiswa(npm) == -1){
            return "gagal";
        } else {
            return "berhasil";
        }
    }

    public void deleteMahasiswaFromDB(MahasiswaDBModel mahasiswa)
    {
        mahasiswaMapper.deleteMahasiswaDB(mahasiswa.getNpm());
        mahasiswaMapper.deleteUserRole(mahasiswa.getUsername());
        mahasiswaMapper.deleteUser(mahasiswa.getUsername());
    }

    public void addUserMahasiswaToDB(MahasiswaDBModel newMahasiswa)
    {
        mahasiswaMapper.addUser(newMahasiswa);
        mahasiswaMapper.addUserRole(newMahasiswa);
        mahasiswaMapper.addMahasiswa(newMahasiswa);
    }

    public void updateUserMahasiswaToDB(MahasiswaDBModel newMahasiswa)
    {
        mahasiswaMapper.updateUser(newMahasiswa);
        mahasiswaMapper.updateUserRole(newMahasiswa);
        mahasiswaMapper.updateMahasiswa(newMahasiswa);
    }



}

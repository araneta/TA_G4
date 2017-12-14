package com.akademik.mahasiswa.g4.controller;

import com.akademik.mahasiswa.g4.model.rest.JadwalModel;
import com.akademik.mahasiswa.g4.model.view.IRSModel;
import com.akademik.mahasiswa.g4.service.IRSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class IRSController {

    @Autowired
    private IRSService irsService;

    @RequestMapping(value = "/irs", method = RequestMethod.GET)
    public String setIRS(Model model){

        JadwalModel jadwalModel = irsService.getJadwalSekarang();

        if(jadwalModel != null) {
            model.addAttribute("page_title", "Set IRS");
            model.addAttribute("jadwal", jadwalModel);
            return "page-set-irs";
        }
        model.addAttribute("page_title", "IRS Not Found");
        return "not-found";
    }

    @RequestMapping(value = "/irs", method = RequestMethod.POST)
    public String submitIRS(Model model, @ModelAttribute("jadwal") JadwalModel jadwalModel){
        //save IRS to db
        irsService.submitIRS(jadwalModel);
        //show irs
        IRSModel irs = irsService.getIRS();
        if(irs != null) {
            model.addAttribute("irs", irs);
            model.addAttribute("page_title", "Lihat IRS");
            return "page-lihat-irs";
        }else{
            model.addAttribute("page_title", "IRS Not Found");
            return "page-not-found-irs";
        }
    }

    @RequestMapping(value = "/mahasiswa/irs/{npm}", method = RequestMethod.GET)
    public String lihatIRS(Model model, @PathVariable("npm") String npm){
        IRSModel irs = irsService.getIRS(npm);
        if(irs != null) {
            model.addAttribute("page_title", "Lihat IRS");
            model.addAttribute("irs", irs);
            return "page-lihat-irs";
        }else{
            model.addAttribute("page_title", "IRS Not Found");
            return "page-not-found-irs";
        }
    }

}

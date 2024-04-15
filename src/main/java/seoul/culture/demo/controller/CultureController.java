package seoul.culture.demo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import seoul.culture.demo.controller.dto.MarkerInfo;
import seoul.culture.demo.domain.CultureForm;
import seoul.culture.demo.service.SearchService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CultureController {
    private final SearchService searchServie;

    @GetMapping("/")
    public String createCultureForm(Model model){
        model.addAttribute("cultureForm", new CultureForm());
        return "index";
    }

    @GetMapping("/culture/search")
    public String showInfo(CultureForm cultureForm, Model model) throws InterruptedException {
        model.addAttribute("data", cultureForm);
        // 서비스로 넘긴다.
        List<MarkerInfo> markerInfo = searchServie.search(cultureForm);
        model.addAttribute("markerInfo", markerInfo);
        return "map";
    }
}

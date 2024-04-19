package seoul.culture.demo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import seoul.culture.demo.domain.CultureSearchForm;
import seoul.culture.demo.service.SearchService;
import seoul.culture.demo.service.vo.Markable;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class CultureController {
    private final SearchService searchService;

    @GetMapping("/")
    public String createCultureForm(Model model){
        model.addAttribute("cultureForm", new CultureSearchForm());
        return "index";
    }

    @GetMapping("/culture/search")
    public String showInfo(CultureSearchForm formData, Model model) {
        List<Markable> markerInfo = searchService.search(formData);

        model.addAttribute("markerInfo", markerInfo);
        return "map";
    }
}
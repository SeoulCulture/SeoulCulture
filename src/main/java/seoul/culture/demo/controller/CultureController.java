package seoul.culture.demo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import seoul.culture.demo.dto.CultureSearchForm;
import seoul.culture.demo.service.SearchService;

import java.io.IOException;
import java.util.Map;

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

    @RequestMapping(value = "/culture/search")
    // get, post 둘다 받아야 한다. post로 첫 요청 받아도, 렌더링하려면 get 을 날리는 듯?
    public String showInfo(CultureSearchForm formData, Model model) throws IOException {
        if (formData.getLatitude() == null || formData.getLongitude() == null) {
            throw new IllegalArgumentException("위경도정보없음");
        }

        Map<String, Object> map = searchService.search(formData);

        model.addAttribute("markerInfo", map.get("marks"));
        model.addAttribute("latitude", map.get("lat"));
        model.addAttribute("longitude", map.get("lon"));
        return "map";
    }
}
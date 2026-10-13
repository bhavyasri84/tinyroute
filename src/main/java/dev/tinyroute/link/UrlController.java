package dev.tinyroute.link;
import dev.tinyroute.analytics.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
@RestController
public class UrlController {
    private final UrlService service; private final ClickEventRepository clicks;
    public UrlController(UrlService service,ClickEventRepository clicks){this.service=service;this.clicks=clicks;}
    @PostMapping("/api/urls")
    public ResponseEntity<UrlResponse> create(@Valid @RequestBody CreateUrlRequest body,HttpServletRequest req){
        String base=req.getScheme()+"://"+req.getServerName()+((req.getServerPort()==80||req.getServerPort()==443)?"":":"+req.getServerPort());
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(body,base));
    }
    @GetMapping("/{code:[A-Za-z0-9_-]{3,16}}")
    public RedirectView redirect(@PathVariable String code,HttpServletRequest req){
        RedirectView view=new RedirectView(service.resolve(code,req.getHeader("User-Agent"),req.getRemoteAddr()));
        view.setStatusCode(HttpStatus.FOUND);return view;
    }
    @GetMapping("/api/urls/{code}/stats")
    public AnalyticsResponse stats(@PathVariable String code){
        return new AnalyticsResponse(code,clicks.countByShortCode(code));
    }
}

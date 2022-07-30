package com.course2.apisecurity.server.xss;

import com.course2.apisecurity.api.response.xss.XssArticleSearchResponse;
import com.course2.apisecurity.entity.XssArticle;
import com.course2.apisecurity.repository.XssArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/xss/danger/v1/article")
@CrossOrigin(origins = "http://localhost:3000")
public class XssArticleDangerApi {

    @Autowired
    private XssArticleRepository repository;

    @PostMapping(value = "")
    public String create(@RequestBody(required = true) XssArticle article) {
        var savedArticleId = repository.save(article);

        return "Saved as " + savedArticleId;
    }

    @GetMapping(value = "")
    public XssArticleSearchResponse search(@RequestParam(required = true) String query) {
        var articles = repository.findByArticleContainsIgnoreCase(query);

        var response = new XssArticleSearchResponse();
        response.setResult(articles);

        if (articles.size() < 100) {
            response.setQueryCount(
                    "Search for " + query + " returns <strong>" + articles.size() + "</strong> results.");
        } else {
            response.setQueryCount(
                    "Search for " + query + " returns too many results. <em>Use more specific keywords</em>");
        }

        return response;
    }

}

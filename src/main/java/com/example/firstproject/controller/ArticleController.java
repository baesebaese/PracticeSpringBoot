package com.example.firstproject.controller;

import com.example.firstproject.dto.ArticleForm;
import com.example.firstproject.entity.Article;
import com.example.firstproject.repository.ArticleRepostiory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Optional;


@Slf4j
@Controller
public class ArticleController {
    @Autowired
    private ArticleRepostiory articleRepository;
    @GetMapping("/articles/new")
    public String newArticleForm() {
        return "articles/new";
    }

    @PostMapping("/articles/create")
    public String createArticle(ArticleForm form) {
        //System.out.println(form.toString());
        log.info(form.toString());
        // 1. DTO를 엔티티로 변환
        Article article = form.toEntity();
        // 2. 레파지토리로 엔티티를 DB에 저장
        Article saved = articleRepository.save(article);
       //System.out.println(saved.toString());
        log.info("save:" + saved.toString());
        return "redirect:/articles/" + saved.getId();
    }

    @GetMapping("/articles/{id}")
    public String show(@PathVariable Long id, Model model) {
        log.info("id = " + id);
        // 1. id를 조회해 데이터 가져오기
        Article articleEntity = articleRepository.findById(id).orElse(null);

        // 2. 모델에 데이터 등록하기
        model.addAttribute("article", articleEntity);
        // 3. 뷰 페이지 반환하기
        return "articles/show";
    }

    @GetMapping("/articles")
    public String index(Model model) {
        //1. 모든 데이터 가져오기
        List<Article> articleEntityList = articleRepository.findAll();
        // 2. 모델에 데이터 등록하기
        model.addAttribute("articleList", articleEntityList);
        // 3. qb vpdlwl tjfwjdgkrl
        return "articles/index";
    }

    @GetMapping("articles/{id}/edit")
    public String edit(@PathVariable Long id, Model model) { // id를 매개변수로 받는다.
        //수정할 데이터 가져오기
        Article articleEntity = articleRepository.findById(id).orElse(null);
        //모델에 데이터 등록하기
        model.addAttribute("article", articleEntity);

        // 뷰 페이지 설정하기
        return "articles/edit";
    }

    @PostMapping("articles/update")
    public String update(ArticleForm form) {
        log.info("update : " + form.toString());
        // 1. Dto 를 엔티티로 만들기
        Article articleEntity = form.toEntity();
        log.info("update entity : " + articleEntity.toString());
        // 2. 엔티티를 DB에 저장하기
        //  2_1. DB에서 기존 데이터 가져오기
        Article target = articleRepository.findById(articleEntity.getId()).orElse(null);
        //  2_2. 기존 데이터 값을 갱신하기
        if(target != null) {
            articleRepository.save(articleEntity); //엔티티를 DB에 저장하기
        }
        // 3. 수정 결과 페이지로 리다이렉트하기
        return "redirect:/articles/" + articleEntity.getId();
    }
}

package com.course2.apisecurity.entity;

//import javax.persistence.*;

//@Entity
public class XssArticle {
   //  @Id
   //  @GeneratedValue(strategy= GenerationType.AUTO)

    private int articleId;

    private String article;

    public int getArticleId() {
        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }
}

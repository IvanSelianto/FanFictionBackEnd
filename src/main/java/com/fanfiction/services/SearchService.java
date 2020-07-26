package com.fanfiction.services;

import com.fanfiction.models.Chapter;
import com.fanfiction.models.Comment;
import com.fanfiction.models.Composition;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class SearchService {

    @Autowired
    private final EntityManager entityManager;

    @Autowired
    public SearchService(EntityManager entityManager) {
        super();
        this.entityManager = entityManager;
    }

    public Set<Composition> initializeHibernateSearch(String searchRequest) throws InterruptedException {
        Set<Composition> compositionsResponse = new HashSet<>();
        FullTextEntityManager fullTextEntityManager
                = Search.getFullTextEntityManager(entityManager);
        fullTextEntityManager.createIndexer().startAndWait();
        ((List<Chapter>) fullTextEntityManager
                .createFullTextQuery(getQueryForChapters(fullTextEntityManager, searchRequest), Chapter.class).getResultList())
                .forEach(chapter -> compositionsResponse.add(chapter.getComposition()));
        ((List<Comment>) fullTextEntityManager
                .createFullTextQuery(getQueryForComments(fullTextEntityManager, searchRequest), Comment.class).getResultList())
                .forEach(comment -> compositionsResponse.add(comment.getComposition()));
        compositionsResponse.addAll(((List<Composition>) fullTextEntityManager
                .createFullTextQuery(getQueryForCompositions(fullTextEntityManager, searchRequest), Composition.class).getResultList()));
        return compositionsResponse;
    }

    private org.apache.lucene.search.Query getQueryForCompositions(FullTextEntityManager fullTextEntityManager, String searchRequest) {
        return getQueryBuilder(Composition.class, fullTextEntityManager).phrase()
                .onField("title").andField("description")
                .sentence(searchRequest)
                .createQuery();
    }
    private org.apache.lucene.search.Query getQueryForChapters(FullTextEntityManager fullTextEntityManager, String searchRequest) {
        return getQueryBuilder(Chapter.class, fullTextEntityManager).phrase()
                .onField("name").andField("text")
                .sentence(searchRequest)
                .createQuery();
    }
    private org.apache.lucene.search.Query getQueryForComments(FullTextEntityManager fullTextEntityManager, String searchRequest) {
        return getQueryBuilder(Comment.class, fullTextEntityManager).phrase()
                .onField("text")
                .sentence(searchRequest)
                .createQuery();
    }
    private QueryBuilder getQueryBuilder(Class entityClass, FullTextEntityManager fullTextEntityManager) {

        return fullTextEntityManager.getSearchFactory()
                .buildQueryBuilder()
                .forEntity(entityClass)
                .get();
    }
}

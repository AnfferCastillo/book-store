package com.anffercastillo.repositories;

import com.anffercastillo.models.Book;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class CustomBooksRepositoryImpl implements CustomBooksRepository {

  private EntityManager entityManager;

  public CustomBooksRepositoryImpl(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  @Override
  public List<Book> search(String term) {
    Query query =
        entityManager.createQuery("select b from Book b where lower(title) like ?1", Book.class);
    query.setParameter(1, "%" + term.toLowerCase() + "%");

    return query.getResultList();
  }
}

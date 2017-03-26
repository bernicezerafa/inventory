package com.vexios.inventory.dao;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ItemsRepositoryImpl implements ItemsRepository{

    private SessionFactory sessionFactory;

    @Autowired
    public ItemsRepositoryImpl(final SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void addItem(final Item item) {
        sessionFactory.getCurrentSession().saveOrUpdate(item);
    }

    @SuppressWarnings("unchecked")
    public List<Item> getItems() {
        final Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Item.class);
        return (List<Item>) criteria.list();
    }

    public Item getItem(final long id) {
        return (Item) sessionFactory.getCurrentSession().get(Item.class, id);
    }

    public void updateItem(final Item updatedItem) {
        sessionFactory.getCurrentSession().update(updatedItem);
    }
}

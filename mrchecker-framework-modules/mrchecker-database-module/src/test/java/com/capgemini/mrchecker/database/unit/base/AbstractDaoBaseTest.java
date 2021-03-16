package com.capgemini.mrchecker.database.unit.base;

import com.capgemini.mrchecker.database.core.base.IDao;
import com.capgemini.mrchecker.database.tags.UnitTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.ResourceLock;
import org.mockito.Mockito;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@UnitTest
@ResourceLock(value = "AbstractDaoBaseTest")
public abstract class AbstractDaoBaseTest {

    protected static final Double TEST_VALUE = 1d;
    protected static final EntityManager ENTITY_MANAGER_MOCK = mock(EntityManager.class);
    protected static final EntityTransaction ENTITY_TRANSACTION_MOCK = mock(EntityTransaction.class);
    protected final IDao<Double, Integer> sut = getSut();

    protected abstract IDao<Double, Integer> getSut();

    @BeforeEach
    public void init() {
        Mockito.reset(ENTITY_MANAGER_MOCK);
        when(ENTITY_MANAGER_MOCK.getTransaction()).thenReturn(ENTITY_TRANSACTION_MOCK);
    }

    @Test
    public void shouldConstructInstance() {
        IDao<Double, Integer> sut = getSut();

        assertThat(sut, is(notNullValue()));
    }

    @Test
    public void shouldSave() {
        assertThat(sut.save(TEST_VALUE), is(equalTo(TEST_VALUE)));
    }

    @Test
    public void shouldGetOne() {
        when(ENTITY_MANAGER_MOCK.getReference(any(), any())).thenReturn(TEST_VALUE);

        assertThat(sut.getOne(1), is(equalTo(TEST_VALUE)));
    }

    @Test
    public void shouldFindOne() {
        when(ENTITY_MANAGER_MOCK.find(any(), any())).thenReturn(TEST_VALUE);

        assertThat(sut.findOne(1), is(equalTo(TEST_VALUE)));
    }

    @Test
    public void shouldFindAll() {
        CriteriaBuilder criteriaBuilderMock = mock(CriteriaBuilder.class);
        CriteriaQuery criteriaQueryMock = mock(CriteriaQuery.class);
        when(criteriaBuilderMock.createQuery(any())).thenReturn(criteriaQueryMock);
        TypedQuery queryMock = mock(TypedQuery.class);
        List listMock = mock(List.class);
        when(queryMock.getResultList()).thenReturn(listMock);
        when(ENTITY_MANAGER_MOCK.getCriteriaBuilder()).thenReturn(criteriaBuilderMock);
        when(ENTITY_MANAGER_MOCK.createQuery(criteriaQueryMock)).thenReturn(queryMock);

        assertThat(sut.findAll(), is(equalTo(listMock)));
    }

    @Test
    public void shouldUpdate() {
        when(ENTITY_MANAGER_MOCK.merge(any())).thenReturn(TEST_VALUE);

        assertThat(sut.update(TEST_VALUE), is(equalTo(TEST_VALUE)));
    }

    @Test
    public void shouldDeleteByEntity() {
        sut.delete(TEST_VALUE);

        assertTrue(true);
    }

    @Test
    public void shouldDeleteById() {
        when(ENTITY_MANAGER_MOCK.getReference(any(), any())).thenReturn(TEST_VALUE);

        sut.delete(1);

        assertTrue(true);
    }

    @Test
    public void shouldDeleteAll() {
        Query queryMock = mock(Query.class);
        when(ENTITY_MANAGER_MOCK.createQuery((String) any())).thenReturn(queryMock);
        sut.deleteAll();

        assertTrue(true);
    }

    @Test
    public void shouldCount() {
        Query queryMock = mock(Query.class);
        when(queryMock.getSingleResult()).thenReturn(1L);
        when(ENTITY_MANAGER_MOCK.createQuery((String) any())).thenReturn(queryMock);

        assertThat(sut.count(), is(equalTo(1L)));
    }

    @Test
    public void shouldExistsReturnTrue() {
        when(ENTITY_MANAGER_MOCK.find(any(), any())).thenReturn(TEST_VALUE);

        assertThat(sut.exists(1), is(equalTo(true)));
    }

    @Test
    public void shouldExistsReturnFalse() {
        assertThat(sut.exists(1), is(equalTo(false)));
    }
}
package com.capgemini.mrchecker.database.unit.base;

import com.capgemini.mrchecker.database.core.base.AbstractDao;
import com.capgemini.mrchecker.database.core.base.IDao;

public class AbstractDaoTest extends AbstractDaoBaseTest {

    @Override
    protected IDao<Double, Integer> getSut() {
        return new AbstractDao<Double, Integer>(ENTITY_MANAGER_MOCK) {
        };
    }
}